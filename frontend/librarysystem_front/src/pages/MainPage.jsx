import { useState, useEffect } from "react";
import {
  Box,
  FormControl,
  Grid,
  InputLabel,
  NativeSelect,
  Pagination,
  PaginationItem,
  Stack,
} from "@mui/material";
import BookCard from "../books/BookCard";
import { fetchBooks, searchBooks } from "../services/bookService";
import { useSearch } from "../context/SearchContext";
import { Link } from "react-router-dom";

export default function MainPage() {
  const { keyword } = useSearch();

  const [books, setBooks] = useState([]);
  const [page, setPage] = useState(1);
  const [sort, setSort] = useState("latest");
  const [totalPages, setTotalPages] = useState(1); // 전체 페이지

  useEffect(() => {
    setPage(1);
  }, [keyword, sort]);

  // 검색어 바뀌면 페이지 초기화
  useEffect(() => {
    if (!keyword || keyword.trim() === "") {
      setPage(1);
    }
  }, [keyword]);

  // 도서 조회
  useEffect(() => {
    const loadBooks = async () => {
      try {
        // 🔍 검색 모드
        if (keyword && keyword.trim() !== "") {
          const data = await searchBooks(keyword);
          setBooks(data.books ?? data);
          setTotalPages(1);
          return;
        }

        // 📚 일반 목록 + 정렬 + 페이지
        const data = await fetchBooks({ page, sort });
        setBooks(data.books);
        setTotalPages(data.totalPages);
      } catch (err) {
        console.error("도서 목록 불러오기 실패:", err);
      }
    };

    loadBooks();
  }, [keyword, page, sort]);

  const handleSortChange = (e) => {
    setSort(e.target.value);
  };

  return (
    <Box sx={{ p: 2, position: "relative" }}>
      <h2>도서 목록</h2>

      {/* 정렬 - latest, title, price */}
      <FormControl
        sx={{ minWidth: 120, top: 30, position: "absolute", right: 0 }}
      >
        <InputLabel variant="standard" htmlFor="sort-label">
          sorting
        </InputLabel>
        <NativeSelect
          inputProps={{
            name: "sort",
            id: "sort-label",
          }}
          value={sort}
          onChange={handleSortChange}
          style={{ fontSize: "11px" }}
        >
          <option value="latest">최신순</option>
          <option value="title">제목순</option>
          <option value="price">가격순</option>
        </NativeSelect>
      </FormControl>

      {/* 도서목록 */}
      {books.length === 0 ? (
        <p>검색 결과가 없습니다.</p>
      ) : (
        <Box sx={{ width: "100%", marginTop: 5 }}>
          <Grid
            container
            spacing={{ xs: 2, md: 3 }}
            columns={{ xs: 4, sm: 8, md: 12 }}
          >
            {books.map((book) => (
              <Grid key={book.bookNo} size={{ xs: 4, sm: 4, md: 4 }}>
                <BookCard book={book} />
              </Grid>
            ))}
          </Grid>
        </Box>
      )}
      {/* 페이징처리 */}
      {(!keyword || keyword.trim() === "") && (
        <Stack spacing={2} sx={{ mt: 4, alignItems: "center" }}>
          <Pagination
            count={totalPages}
            page={page}
            onChange={(e, value) => setPage(value)}
            renderItem={(item) => (
              <PaginationItem
                component={Link}
                to={`?page=${item.page}&sort=${sort}`}
                {...item}
              />
            )}
          />
        </Stack>
      )}
    </Box>
  );
}
