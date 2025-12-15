package com.example.demo.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyLoanResponse {
    private Long loanId;
    private String bookTitle;
    private String returnDate;
    private String status;
}