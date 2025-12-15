import { useState } from "react";
import { Box, Paper, Typography, TextField, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { login } from "../services/bookService";
import { useAuth } from "../context/AuthContext";

export default function LoginPage() {
  const navigate = useNavigate();
  const { authLogin } = useAuth();

  const [memberId, setMemberId] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      // 로그인 API 호출
      const res = await login(memberId, password);

      const { accessToken, role, loginId } = res;

      if (!accessToken) {
        alert("로그인 응답에서 토큰을 받지 못했습니다.");
        return;
      }

      // role 체크: ADMIN 이 아니라면 관리자 페이지 접근 차단
      if (role !== "USER") {
        alert("등록된 유저가 아닙니다.");
        return;
      }

      // 토큰 저장
      authLogin({ accessToken, role, loginId });

      // 로그인 성공 → 메인으로 이동
      alert("일반 회원으로 로그인에 성공했습니다.");
      navigate("/");
    } catch (error) {
      console.error("로그인 실패:", error);
      alert("로그인에 실패했습니다. 회원번호와 비밀번호를 다시 확인해주세요.");
    }
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        bgcolor: "#e9ecef",
      }}
    >
      <Paper
        elevation={4}
        sx={{
          width: 420,
          p: 4,
          borderRadius: 3,
        }}
      >
        {/* 상단 타이틀 */}
        <Typography variant="h6" align="center" sx={{ fontWeight: 700, mb: 1 }}>
          BookShelf
        </Typography>
        <Typography variant="h5" align="center" sx={{ fontWeight: 700, mb: 4 }}>
          로그인
        </Typography>

        {/* 폼 */}
        <Box
          component="form"
          onSubmit={handleSubmit}
          sx={{ display: "flex", flexDirection: "column", gap: 2.5 }}
        >
          {/* 회원번호 */}
          <TextField
            label="회원번호"
            variant="outlined"
            size="small"
            fullWidth
            placeholder="회원번호를 입력하세요"
            value={memberId}
            onChange={(e) => setMemberId(e.target.value)}
          />

          {/* 비밀번호 */}
          <TextField
            label="비밀번호"
            type="password"
            variant="outlined"
            size="small"
            fullWidth
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />

          <Button
            type="submit"
            variant="contained"
            sx={{
              mt: 1,
              borderRadius: 999,
              py: 1.1,
            }}
          >
            로그인
          </Button>
        </Box>

        {/* 회원가입 이동 */}
        <Typography
          variant="body2"
          align="center"
          sx={{ mt: 2, cursor: "pointer" }}
          onClick={() => navigate("/signup")}
        >
          회원가입
        </Typography>

        {/* 메인으로 이동 */}
        <Button
          variant="body2"
          size="small"
          sx={{
            mt: 1,
            display: "block",
            mx: "auto",
          }}
          onClick={() => navigate("/")}
        >
          메인으로 이동
        </Button>
        {/* 관리자 로그인 이동 */}
        <Button
          variant="text"
          size="small"
          sx={{
            mt: 1,
            display: "block",
            mx: "auto",
            color: "#6b7280",
          }}
          onClick={() => navigate("/admin/login")}
        >
          관리자 로그인
        </Button>
      </Paper>
    </Box>
  );
}
