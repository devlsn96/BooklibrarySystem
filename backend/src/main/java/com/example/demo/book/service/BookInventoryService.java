package com.example.demo.book.service;

import com.example.demo.book.entity.Book;
import com.example.demo.book.entity.BookManagement;
import com.example.demo.book.repository.BookManagementRepository;
import com.example.demo.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookInventoryService {

    private final BookRepository bookRepository;
    private final BookManagementRepository bookManagementRepository;

    /**
     * 도서 대여가능 여부/재고 처리 (재고는 0 또는 1로만 관리)
     *
     * count <= 0 : 모든 재고를 대여불가(true)로 바꾸고 0 반환
     * count > 0  : 최대 1개만 대여가능(false)로 두고, 나머지는 제거/대여불가로 처리 후 1 반환
     */
    public int restock(Long bookId, int count) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));

        // 0 이하면 모든 재고를 대여불가(true)로 리셋 후 0 반환
        if (count <= 0) {
            List<BookManagement> records = bookManagementRepository.findByBookId(bookId);
            records.forEach(bm -> bm.setIsLoaned(true)); // 대여불가 표시
            if (!records.isEmpty()) {
                bookManagementRepository.saveAll(records);
            }
            return 0;
        }

        // count > 0이면 재고를 최대 1개(대여가능=false)만 유지
        List<BookManagement> records = bookManagementRepository.findByBookId(bookId);
        if (records.isEmpty()) {
            BookManagement bm = BookManagement.builder()
                    .book(book)
                    .isLoaned(false)
                    .build();
            bookManagementRepository.save(bm);
            return 1;
        }

        BookManagement available = records.get(0);
        available.setIsLoaned(false);

        // 나머지 레코드는 제거하여 1개만 유지
        if (records.size() > 1) {
            bookManagementRepository.deleteAll(records.subList(1, records.size()));
        }
        bookManagementRepository.save(available);

        return 1;
    }
}
