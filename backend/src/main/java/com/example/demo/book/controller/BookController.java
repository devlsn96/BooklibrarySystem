package com.example.demo.book.controller;

import com.example.demo.book.dto.*;
import com.example.demo.book.entity.Book;
import com.example.demo.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService; // Service (팀원과의 계약) 주입

    // ==========================================
    // 1. 도서 목록 조회 (담당 영역: GET /api/books)
    // ==========================================
    @GetMapping
    public ResponseEntity<BookListResponse> getAllBooks(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "sort", defaultValue = "latest") String sort,
            @RequestParam(name = "keyword", required = false) String keyword) {

        // ★ [목록 조회/검색/페이지네이션 로직] Service에 위임
        BookListResponse response = bookService.getAllBooks(page, sort, keyword);
        return ResponseEntity.ok(response);
    }

    // ==========================================
    // 2. 도서 검색 ( GET /api/books/search)
    // ==========================================
    @GetMapping("/search")
    public ResponseEntity<BookListResponse> searchBooks(
            @RequestParam(name = "keyword", required = false) String keyword) {

        // ★ [검색 로직] Service의 getAllBooks 메서드를 재사용하여 검색 위임
        BookListResponse response = bookService.getAllBooks(1, "latest", keyword);
        return ResponseEntity.ok(response);
    }

    // ==========================================
    // 3. 도서 상세 조회 ( GET /api/books/{bookId})
    // ==========================================
    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable(name = "bookId") Long bookId) {

        // ★ [상세 조회 로직] Service에 위임하여 상세 정보 및 재고 계산 요청
        BookResponse book = bookService.getBookById(bookId);
        return ResponseEntity.ok(book);
    }
    /**
     * 관리자 대여가능여부 확인 및 증감
     * POST /admin/books/{bookId}/stock
     * 요청 바디: { "count": <증감 수량, 기본 1 / 0이면 대여 불가로 처리> }
     * 응답: { "stockcount": <대출 가능 재고 수량> }
     */
    @PostMapping("/admin/{bookId}/stock")
    public StockResponse checkAvailability(@PathVariable("bookId") Long bookId,
                                           @RequestBody StockRequest request) {
        int count = request.getStockcount(); // 기본 1
        int current = bookService.restock(bookId, count);
        return new StockResponse(current);
    }

    // 관리자 도서 등록: POST /admin/books
    @PostMapping("/admin")
    public AdminBookResponse createBook(@RequestBody AdminBookRequest req) {
        Book book = buildBookFromRequest(req, true);
        Book saved = bookService.createBook(book, req.getDescription());
        return new AdminBookResponse(saved.getId(), "등록완료");
    }

    // 관리자 도서 수정: PATCH /admin/books/{bookId}
    @PatchMapping("/admin/{bookId}")
    public AdminBookResponse updateBook(@PathVariable("bookId") Long bookId,
                                        @RequestBody AdminBookRequest req) {
        Book book = buildBookFromRequest(req, false);
        Book updated = bookService.updateBook(bookId, book, req.getDescription());
        return new AdminBookResponse(updated.getId(), "수정완료");
    }

    /**
     * 관리자 도서 삭제
     * DELETE /admin/books/{bookId}
     */
    @DeleteMapping("/admin/{bookId}")
    public AdminBookResponse deleteBook(@PathVariable("bookId") Long bookId) {
        bookService.deleteBook(bookId);
        return new AdminBookResponse(bookId, "삭제완료");
    }

    /**
     * AdminBookRequest를 Book 엔티티로 변환
     * @param req 요청 DTO
     * @param useDefaultRegistrationDate true면 등록일 미지정 시 오늘 날짜로 설정
     */
    private Book buildBookFromRequest(AdminBookRequest req, boolean useDefaultRegistrationDate) {
        return Book.builder()
                .title(req.getTitle())
                .author(req.getAuthor())
                .publisher(req.getPublisher())
                .genre(req.getGenre())
                .tag(req.getTag())
                .coverImage(req.getCoverImageUrl())
                .price(req.getPrice())
                .registrationDate(useDefaultRegistrationDate && req.getRegistrationDate() == null
                        ? LocalDate.now()
                        : req.getRegistrationDate())
                .build();
    }

}