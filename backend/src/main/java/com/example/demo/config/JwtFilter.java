package com.example.demo.config;

import com.example.demo.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. 헤더에서 토큰 꺼내기
        String authorizationHeader = request.getHeader("Authorization");

        // 2. 토큰이 있고, "Bearer "로 시작하는지 확인
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // "Bearer " 떼기

            try {
                // 3. 토큰에서 ID와 권한(Role) 꺼내기
                String id = authService.extractRole(token);
                String role = authService.extractRole(token);

                // 4. 권한을 스프링 스타일("ROLE_ADMIN")로 변환
                // 스프링은 권한 앞에 무조건 "ROLE_"이 붙어야 인식합니다!
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

                // 5. 출입증(Authentication) 만들기
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(id, null, List.of(authority));

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. 출입증 제출 (로그인 했는지)
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (Exception e) {
                // 토큰이 위조되었거나 만료된 경우 -> 그냥 무시하고 넘어가면(로그인 안 된 걸로 침) 됨
                System.out.println("토큰 에러: " + e.getMessage());
            }
        }

        // 다음 단계로 통과
        filterChain.doFilter(request, response);
    }
}