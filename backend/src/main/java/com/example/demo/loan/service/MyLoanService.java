package com.example.demo.loan.service;

import com.example.demo.loan.dto.MyLoanResponse;
import com.example.demo.loan.entity.Loan;
import com.example.demo.loan.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyLoanService {

    private final LoanRepository loanRepository;

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
}
