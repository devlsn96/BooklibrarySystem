package com.example.demo.user.repository;

import com.example.demo.user.entity.MemberManagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberManagementRepository extends JpaRepository<MemberManagement, Long> {
}