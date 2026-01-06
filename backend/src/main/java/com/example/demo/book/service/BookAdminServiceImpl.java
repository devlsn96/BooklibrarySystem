package com.example.demo.book.service;

import com.example.demo.book.entity.Book;

public interface BookAdminServiceImpl {

    // 도서 생성하고 : description이 있으면 BookDetail까지 함께 저장
    Book createBook(Book book, String description);

    // 도서 부분 수정 : description이 전달되면 소개도 업데이트/삭제
    Book updateBook(Long bookId, Book book, String description);

    // 도서 삭제 : BookDetail 재고 삭제
    void deleteBook(Long bookId);
}
