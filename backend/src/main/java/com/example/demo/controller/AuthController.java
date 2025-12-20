package com.example.demo.controller;

import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        authService.signup(
                body.get("memberId"),
                body.get("password"),
                body.get("name"),
                body.get("phone"),
                body.get("address")
        );
        return ResponseEntity.status(201).body(Map.of("msg", "가입완료"));
    }

    // 관리자 추가 API
    @PostMapping("/admin/signup")
    public ResponseEntity<?> signupAdmin(@RequestBody Map<String, String> body) {
        // 서비스 호출
        Long newEmployeeId = authService.signupAdmin(
                body.get("password"),
                body.get("name"),
                body.get("phone"),
                body.get("address")
        );

        // 결과 반환 (중요: 발급된 사원번호를 알려줌)
        Map<String, Object> response = new HashMap<>();
        response.put("msg", "관리자 등록 완료");
        response.put("employeeId", newEmployeeId); // "당신의 사원번호는 2번입니다" 라고 알려줌

        return ResponseEntity.status(201).body(response);
    }

    // 회원 로그인
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String token = authService.login(body.get("memberId"), body.get("password"));
        return ResponseEntity.ok(Map.of("accessToken", token));
    }

    // 관리자 로그인
    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody Map<String, Object> body) {
        Long employeeId = Long.valueOf(String.valueOf(body.get("employeeId"))); // 숫자 변환
        String password = (String) body.get("password");

        String token = authService.adminLogin(employeeId, password);
        return ResponseEntity.ok(Map.of("accessToken", token, "role", "ADMIN"));
    }
}