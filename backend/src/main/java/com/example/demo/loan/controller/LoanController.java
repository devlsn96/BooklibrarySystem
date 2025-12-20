package com.example.demo.loan.controller;

import com.example.demo.loan.dto.LoanRequest;
import com.example.demo.loan.dto.LoanResponse;
import com.example.demo.loan.dto.MyLoanResponse;
import com.example.demo.loan.dto.ReturnResponse;
import com.example.demo.loan.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public LoanResponse loanBook(@RequestBody LoanRequest request) {
        return loanService.loanBook(request);
    }
    @GetMapping("/my")
    public List<MyLoanResponse> getMyLoans(@RequestHeader("memberId") String memberId) {
        return loanService.getMyLoans(memberId);
    }
    // PATCH /api/loans/{loanId}/return
    @PatchMapping("/{loanId}/return")
    public ReturnResponse returnBook(@PathVariable Long loanId) {
        return loanService.returnBook(loanId);
    }
}
