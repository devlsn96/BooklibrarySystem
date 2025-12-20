package com.example.demo.book.service;

import com.example.demo.book.dto.BookListResponse;
import com.example.demo.book.dto.BookRequest;
import com.example.demo.book.dto.BookResponse;
import com.example.demo.book.entity.Book;

public interface BookService {

    BookListResponse getAllBooks(Integer page, String sort, String keyword);

    BookResponse getBookById(Long bookNo);

}