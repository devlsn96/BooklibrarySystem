package com.example.demo.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
public class LoanResponse {
    private Long loanId;
    private LocalDate dueDate;
}
