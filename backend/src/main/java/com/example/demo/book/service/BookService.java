package com.example.demo.book.service;

import com.example.demo.book.dto.BookListResponse;
import com.example.demo.book.dto.BookResponse;
// Note: BookRequest is removed as it's only used by non-Read methods
import com.example.demo.book.entity.Book;
import com.example.demo.book.entity.BookDetail;
import com.example.demo.book.entity.BookManagement;
import com.example.demo.book.repository.BookDetailRepository;
import com.example.demo.book.repository.BookManagementRepository;
import com.example.demo.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService implements BookServiceImpl, BookAdminServiceImpl {

        private final BookRepository bookRepository;
        private final BookDetailRepository bookDetailRepository;
        private final BookManagementRepository bookManagementRepository;

        // ======================================================
        // 1. 도서 목록 및 검색 조회 (Controller의 GET /api/books 지원)
        // ======================================================
        @Transactional(readOnly = true)
        public BookListResponse getAllBooks(Integer page, String sort, String keyword) {
                // 0-based index 처리
                int pageNum = (page != null && page > 0) ? page - 1 : 0;
                Sort sortObj = Sort.by("id");
                if ("latest".equals(sort)) {
                        sortObj = Sort.by("registrationDate").descending();
                } else if (sort != null && !sort.isEmpty()) {
                        sortObj = Sort.by(sort);
                }

                // Spring Data JPA를 사용한 페이지네이션/정렬
                Pageable pageable = PageRequest.of(pageNum, 10, sortObj);
                Page<Book> bookPage;

                if (keyword != null && !keyword.isEmpty()) {
                        bookPage = bookRepository.findByTitleContainingOrAuthorContaining(keyword, keyword, pageable);
                } else {
                        bookPage = bookRepository.findAll(pageable);
                }

                List<BookResponse> bookResponses = bookPage.getContent().stream()
                                .map(book -> mapToResponse(book, false)) // 목록 조회 시 상세 정보 제외
                                .collect(Collectors.toList());

                // API 명세에 따른 BookListResponse 구성 (count 포함)
                return BookListResponse.builder()
                                .count(bookPage.getTotalElements())
                                .books(bookResponses)
                                .build();
        }

        // ======================================================
        // 2. 도서 상세 조회 (Controller의 GET /api/books/{bookId} 지원)
        // ======================================================
        @Transactional(readOnly = true)
        public BookResponse getBookById(Long bookNo) {
                Book book = bookRepository.findById(bookNo)
                                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookNo));

                // DTO 변환 및 반환
                return mapToResponse(book);
        }

        // ======================================================
        // 4. 헬퍼 메서드: DTO 변환
        // ======================================================
        private BookResponse mapToResponse(Book book) {
                // 기본값: 상세 정보 포함 (기존 코드 호환성을 위해)
                return mapToResponse(book, true);
        }

        private BookResponse mapToResponse(Book book, boolean includeDetail) {
                String description = null;

                if (includeDetail) {
                        description = bookDetailRepository.findById(book.getId())
                                        .map(BookDetail::getDescription)
                                        .orElse(null);
                }

                String genre = book.getGenre();
                String tag = book.getTag();
                // 대출 가능 여부 확인: 대출되지 않은(isLoaned=false) 책이 하나라도 있으면 "대출중 아님(false)"
                boolean isAvailable = bookManagementRepository.findFirstByBookIdAndIsLoanedFalse(book.getId())
                                .isPresent();
                Boolean isLoaned = !isAvailable;

                return BookResponse.builder()
                                .bookNo(book.getId())
                                .title(book.getTitle())
                                .author(book.getAuthor())
                                .publisher(book.getPublisher())
                                .coverImageUrl(book.getCoverImage())
                                .price(book.getPrice())
                                .registerDate(book.getRegistrationDate())
                                .description(description) // includeDetail이 false면 null
                                .genre(genre)
                                .tag(tag)
                                .isLoaned(isLoaned)
                                .build();
        }
        // 원래 있던 bookdelete, insert부분 태민님이 분리해서 다른 파일로 분리했습니다.

        @Override
        public Book createBook(Book book, String description) {
                Book saved = bookRepository.save(book);
                // 책소개가 비어있지 않으면 BookDetail도 함께 저장 (@MapsId)
                if (description != null && !description.isBlank()) {
                        BookDetail detail = BookDetail.builder()
                                .book(saved)
                                .description(description)
                                .build();
                        bookDetailRepository.save(detail);
                }
                // 도서 등록 시 기본 대여가능 재고 1개 생성
                BookManagement stock = BookManagement.builder()
                        .book(saved)
                        .isLoaned(false)
                        .build();
                bookManagementRepository.save(stock);
                return saved;
        }

        @Override
        public Book updateBook(Long bookId, Book updated, String description) {
                Book book = bookRepository.findById(bookId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 도서를 찾을 수 없습니다: " + bookId));

                // null이 아닌 필드만 업데이트
                if (updated.getTitle() != null) book.setTitle(updated.getTitle());
                if (updated.getAuthor() != null) book.setAuthor(updated.getAuthor());
                if (updated.getPublisher() != null) book.setPublisher(updated.getPublisher());
                if (updated.getGenre() != null) book.setGenre(updated.getGenre());
                if (updated.getTag() != null) book.setTag(updated.getTag());
                if (updated.getCoverImage() != null) book.setCoverImage(updated.getCoverImage());
                if (updated.getPrice() != null) book.setPrice(updated.getPrice());
                if (updated.getRegistrationDate() != null) book.setRegistrationDate(updated.getRegistrationDate());

                Book saved = bookRepository.save(book);

                // description이 null이 아니면 소개 처리 (null이면 기존 유지)
                if (description != null) {
                        if (description.isBlank()) {
                                bookDetailRepository.findById(bookId).ifPresent(bookDetailRepository::delete);
                        } else {
                                BookDetail detail = bookDetailRepository.findById(bookId)
                                        .orElseGet(() -> BookDetail.builder()
                                                .book(saved)
                                                .build());
                                detail.setDescription(description);
                                bookDetailRepository.save(detail);
                        }
                }
                return saved;
        }

        @Override
        public void deleteBook(Long bookId) {
                // 요청에 포함된 bookId가 실제 존재하는지 확인 후 삭제
                if (!bookRepository.existsById(bookId)) {
                        return;
                }
                // FK 제약 방지를 위해 연관 데이터 먼저 삭제
                bookDetailRepository.findById(bookId).ifPresent(bookDetailRepository::delete);
                bookManagementRepository.deleteByBookId(bookId);
                bookRepository.deleteById(bookId);
        }

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
