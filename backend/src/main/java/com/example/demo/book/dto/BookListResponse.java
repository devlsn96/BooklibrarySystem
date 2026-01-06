package com.example.demo.book.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookListResponse {

    private long count; // 총 도서 개수
    private List<BookResponse> books; // 실제 도서 정보 목록
}