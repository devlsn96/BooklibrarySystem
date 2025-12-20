package com.example.demo.config;

import com.example.demo.employee.entity.Employee;
import com.example.demo.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // 관리자가 한 명도 없으면 자동으로 1명 만듭니다.
        if (employeeRepository.count() == 0) {
            Employee admin = Employee.builder()
                    .password(passwordEncoder.encode("admin1234"))
                    .name("최고관리자")
                    .build();

            employeeRepository.save(admin); // DB에 저장 (이때 번호가 생성됨)

            System.out.println("======================================");
            System.out.println("✅ 최고관리자 계정이 생성되었습니다!");
            System.out.println("👉 사원번호(ID): 1 (자동 생성됨)");
            System.out.println("👉 비밀번호(PW): admin1234");
            System.out.println("======================================");
        }
    }
}