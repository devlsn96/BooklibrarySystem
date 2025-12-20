package com.example.demo.book.dto;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {

    private Long bookNo;
    private String title;
    private String author;
    private String publisher;
    private String coverImageUrl;
    private Integer price;
    private LocalDate registerDate;

    private String description;
    private String genre;
    private String tag;
    private Boolean isLoaned;

}