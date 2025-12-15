package com.example.demo.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ReturnResponse {
    private String msg;
    private boolean isLoaned;
    private int penalty;
}
