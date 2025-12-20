package com.example.demo.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 등록번호

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDate registrationDate; // 등록일
}
