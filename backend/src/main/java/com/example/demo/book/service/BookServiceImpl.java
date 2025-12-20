package com.example.demo.book.service;

import com.example.demo.book.dto.BookListResponse;
import com.example.demo.book.dto.BookResponse;

public interface BookServiceImpl {

    BookListResponse getAllBooks(Integer page, String sort, String keyword);

    BookResponse getBookById(Long bookNo);

}