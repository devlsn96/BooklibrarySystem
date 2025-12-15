package com.example.demo.loan.controller;

import com.example.demo.loan.dto.MyLoanResponse;
import com.example.demo.loan.service.MyLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class MyLoanController {

    private final MyLoanService myLoanService;

    @GetMapping("/my")
    public List<MyLoanResponse> getMyLoans(@RequestHeader("memberId") String memberId) {
        return myLoanService.getMyLoans(memberId);
    }
}
