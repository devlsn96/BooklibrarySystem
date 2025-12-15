package com.example.demo.member.repository;

import com.example.demo.member.entity.MemberManagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberManagementRepository extends JpaRepository<MemberManagement, Long> {
}