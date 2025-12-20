package com.example.demo.loan.service;

import com.example.demo.book.entity.BookManagement;
import com.example.demo.book.repository.BookManagementRepository;
import com.example.demo.loan.dto.LoanRequest;
import com.example.demo.loan.dto.LoanResponse;
import com.example.demo.loan.dto.MyLoanResponse;
import com.example.demo.loan.dto.ReturnResponse;
import com.example.demo.loan.entity.Loan;
import com.example.demo.loan.repository.LoanRepository;
import com.example.demo.user.entity.Member;
import com.example.demo.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookManagementRepository bookManagementRepository;
    private final MemberRepository memberRepository;

    public LoanResponse loanBook(LoanRequest request) {

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("회원 정보가 없습니다."));

        BookManagement bookManagement = bookManagementRepository
                .findFirstByBookIdAndIsLoanedFalse(request.getBookId())
                .orElseThrow(() -> new RuntimeException("대여 가능한 재고가 없습니다."));

        bookManagement.setIsLoaned(true); // 재고 상태 변경

        Loan loan = Loan.builder()
                .bookManagement(bookManagement)
                .member(member)
                .loanDate(LocalDate.now())
                .fee(0)
                .build();

        loanRepository.save(loan);

        LocalDate due = loan.getLoanDate().plusDays(7);

        return new LoanResponse(loan.getId(), due);
    }
    public List<MyLoanResponse> getMyLoans(String memberId) {

        List<Loan> loans = loanRepository.findByMemberId(memberId);

        return loans.stream().map(loan -> {
            LocalDate dueDate = loan.getLoanDate().plusDays(7);

            // 상태 계산
            String status;
            if (loan.getStatus() == Loan.LoanStatus.RETURNED) {
                status = "반납완료";
            } else if (LocalDate.now().isAfter(dueDate)) {
                status = "연체";
            } else {
                status = "대여중";
            }

            return new MyLoanResponse(
                    loan.getId(),
                    loan.getBookManagement().getBook().getTitle(),
                    dueDate.toString(),
                    status
            );
        }).toList();
    }
    // 반납 처리
    public ReturnResponse returnBook(Long loanId) {
        // 대여 내역 조회
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("대여 정보가 없습니다."));

        // 연체료 계산
        LocalDate dueDate = loan.getLoanDate().plusDays(7);
        LocalDate today = LocalDate.now();

        int penalty = 0;
        if (today.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, today);
            penalty = (int) daysLate * 1000; // 1일당 1000원 예시
        }

        // 재고 복구
        BookManagement book = loan.getBookManagement();
        book.setIsLoaned(false);
        bookManagementRepository.save(book);

        // Response
        return new ReturnResponse("반납완료", penalty);
    }
}
