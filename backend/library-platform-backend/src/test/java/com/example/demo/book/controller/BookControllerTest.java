package com.example.demo.book.controller;

import com.example.demo.book.dto.BookListResponse;
import com.example.demo.book.dto.BookResponse;
import com.example.demo.book.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * [BookControllerTest]
 * 역할: BookController의 HTTP 엔드포인트가 API 규격에 맞는지 격리하여 검증합니다.
 */
@WebMvcTest(BookController.class) // Controller 계층만 로드하여 테스트
@WithMockUser // Spring Security 인증 모의 처리
class BookControllerTest {

        @Autowired
        private MockMvc mockMvc; // HTTP 요청 시뮬레이션 객체

        // Controller가 의존하는 BookService는 가짜(Mock) 객체로 대체
        @MockBean
        private BookService bookService;

        // ==========================================
        // 1. 도서 상세 조회 테스트 (성공: GET /api/books/{id})
        // ==========================================
        @Test
        void getBookById_shouldReturnBookDetails() throws Exception {
                Long bookId = 10L;
                // Mock 응답 DTO: 모든 필드를 채워서 검증
                BookResponse mockResponse = BookResponse.builder()
                                .bookNo(bookId)
                                .title("객체지향의 사실과 오해")
                                .author("조영호")
                                .publisher("위키북스")
                                .coverImageUrl("http://example.com/cover.jpg")
                                .price(25000)
                                .registerDate(java.time.LocalDate.now())
                                .summary("객체지향이란 무엇인가...")
                                .build();

                given(bookService.getBookById(bookId)).willReturn(mockResponse);

                // HTTP 요청 시뮬레이션 및 검증
                mockMvc.perform(get("/api/books/{bookId}", bookId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print()) // ★ 요청과 응답 전문을 콘솔에 출력
                                .andExpect(status().isOk()) // HTTP 200 OK 확인
                                .andExpect(jsonPath("$.bookNo").value(bookId))
                                .andExpect(jsonPath("$.title").value("객체지향의 사실과 오해"));
        }

        // ==========================================
        // 3. 도서 목록/검색 조회 테스트 (GET /api/books)
        // ==========================================
        @Test
        void getAllBooks_shouldReturnListOfBooksWithCount() throws Exception {
                // Mock 응답 DTO: 목록 컨테이너
                BookListResponse mockListResponse = BookListResponse.builder()
                                .count(1L)
                                .books(Collections.singletonList(
                                                BookResponse.builder().bookNo(1L).title("테스트 북").build()))
                                .build();

                given(bookService.getAllBooks(any(), any(), any())).willReturn(mockListResponse);

                // HTTP 요청 시뮬레이션: 쿼리 파라미터 포함
                mockMvc.perform(get("/api/books")
                                .param("page", "1")
                                .param("sort", "title")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print()) // ★ 요청과 응답 전문을 콘솔에 출력
                                .andExpect(status().isOk()) // HTTP 200 OK 확인
                                .andExpect(jsonPath("$.count").value(1)) // API 명세: count 필드 검증
                                .andExpect(jsonPath("$.books[0].title").value("테스트 북")); // 목록의 첫 번째 아이템 검증
        }
}