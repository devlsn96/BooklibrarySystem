package com.example.demo.book.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequest {

    private String title;
    private String author;
    private String publisher;
    private String genre;
    private String tag;
    private Integer price;
    private String summary;
}