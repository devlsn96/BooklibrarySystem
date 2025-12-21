import { useState } from "react";
import { Box, Paper, Typography, TextField, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { adminLogin } from "../services/bookService";
import { useAuth } from "../context/AuthContext";

export default function AdminLoginPage() {
  const navigate = useNavigate();
  const { authLogin } = useAuth();
  const [employeeId, setEmployeeId] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!employeeId || !password) {
      alert("사원번호와 비밀번호를 모두 입력해 주세요.");
      return;
    }

    try {
      setLoading(true);

      // 관리자 로그인 API 호출 { accessToken, role } 형태를 반환한다고 가정
      const data = await adminLogin({ employeeId, password });

      const { accessToken, role, loginId } = data;

      if (!accessToken) {
        alert("로그인 응답에서 토큰을 받지 못했습니다.");
        return;
      }

      // 로그인
      authLogin({ accessToken, role, loginId });

      // 로그인 성공 → 메인 페이지나 관리자 전용 페이지로 이동
      alert("관리자 로그인에 성공했습니다.");
      navigate("/");
    } catch (error) {
      console.error("관리자 로그인 실패:", error);
      const msg =
        error.response?.data?.msg ||
        error.response?.data?.message ||
        "관리자 로그인에 실패했습니다. 사원번호와 비밀번호를 다시 확인해 주세요.";
      alert(msg);
    } finally {
      setLoading(false);
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
          관리자 로그인
        </Typography>

        {/* 폼 */}
        <Box
          component="form"
          onSubmit={handleSubmit}
          sx={{ display: "flex", flexDirection: "column", gap: 2.5 }}
        >
          {/* 사원번호 */}
          <TextField
            label="사원번호"
            variant="outlined"
            size="small"
            fullWidth
            placeholder="사원번호를 입력하세요"
            value={employeeId}
            onChange={(e) => setEmployeeId(e.target.value)}
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
            disabled={loading}
          >
            {loading ? "로그인 중..." : "로그인"}
          </Button>
        </Box>

        {/* 일반 회원 로그인으로 이동 */}
        <Typography
          variant="body2"
          align="center"
          sx={{ mt: 2, cursor: "pointer" }}
          onClick={() => navigate("/login")}
        >
          일반 회원 로그인으로 돌아가기
        </Typography>

        {/* 메인으로 이동 */}
        <Button
          variant="text"
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
      </Paper>
    </Box>
  );
}
