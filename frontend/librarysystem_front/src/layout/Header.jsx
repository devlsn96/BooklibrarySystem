import { useNavigate } from "react-router-dom";
import { useSearch } from "../context/SearchContext";
import styled from "@emotion/styled";
import SearchIcon from "@mui/icons-material/Search";
import { AppBar, Avatar, Box, Button, IconButton, InputBase, Toolbar, Typography } from "@mui/material";
import { useAuth } from "../context/AuthContext";

// 검색창 스타일
const SearchBox = styled("div")(() => ({
  position: "relative",
  borderRadius: 8,
  backgroundColor: "#f1f3f5",
  width: "100%",
  maxWidth: "800px",
  display: "flex",
  alignItems: "center",
  padding: "6px 14px",
}));

export default function Header() {
  const navigate = useNavigate();
  const { keyword, setKeyword } = useSearch();
  const { isLoggedIn, authLogout } = useAuth();

  return (
    <AppBar
      position="static"
      elevation={0}
      sx={{
        backgroundColor: "#ffffff",
        color: "#000",
        paddingX: 3,
        borderBottom: "1px solid #e5e5e5",
      }}
    >
      <Toolbar
        sx={{
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
        }}
      >
        {/* 로고 */}
        <Box
          sx={{ display: "flex", alignItems: "center", cursor: "pointer" }}
          onClick={() => navigate("/")}
        >
          <Typography variant="h6" sx={{ fontWeight: 600 }}>
            BookShelf
          </Typography>
        </Box>

        {/* 오른쪽 영역: 검색창 + 버튼들 */}
        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            gap: 3,
            flex: 1,
            justifyContent: "flex-end",
            ml: 4, // 로고와 검색창 사이 여유 간격
          }}
        >
          {/* 검색창 */}
          <SearchBox>
            <SearchIcon fontSize="small" sx={{ color: "#868e96", mr: 1 }} />
            <InputBase
              placeholder="검색"
              sx={{ width: "100%", fontSize: 14 }}
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
            />
          </SearchBox>

          {/* 로그인 여부에 따라 우측 UI 분기 */}
          {isLoggedIn ? (
            <>
              {/* 프로필 */}
              <IconButton sx={{ p: 0 }}>
                <Avatar
                  sx={{ width: 36, height: 36 }}
                  src="/default-profile.png" // 없으면 그냥 기본 색 원으로 나옴
                  alt="프로필"
                />
              </IconButton>

              {/* 로그아웃 버튼 */}
              <Button
                variant="text"
                sx={{
                  color: "#1f2937",
                  fontWeight: 600,
                  whiteSpace: "nowrap",
                  flexShrink: 0,
                }}
                onClick={authLogout}
              >
                로그아웃
              </Button>
            </>
          ) : (
            <>
              {/* 로그인 버튼 */}
              <Button
                variant="text"
                sx={{
                  color: "#1f2937",
                  fontWeight: 600,
                  whiteSpace: "nowrap", // 줄바꿈 방지
                  flexShrink: 0, // 버튼 쪼그라드는 것 방지
                }}
                onClick={() => navigate("/login")}
              >
                로그인
              </Button>

              {/* 회원가입 버튼 */}
              <Button
                variant="contained"
                sx={{
                  backgroundColor: "#3b82f6",
                  borderRadius: "20px",
                  px: 2.5,
                  py: 0.8,
                  fontWeight: 600,
                  whiteSpace: "nowrap",
                  minWidth: 96,
                  flexShrink: 0,
                }}
                onClick={() => navigate("/signup")}
              >
                회원가입
              </Button>
            </>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
}
