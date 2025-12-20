package com.example.demo.book.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookListResponse {

    private long count; // 총 도서 개수
    private List<BookResponse> books; // 실제 도서 정보 목록
}