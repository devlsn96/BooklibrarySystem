import Box from "@mui/material/Box";
import { Outlet } from "react-router-dom";

import Header from "./Header";
import Slidebar from "./Slidebar";
import Footer from "./Footer";

export default function Layout() {
  return (
    <Box sx={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}>
      {/* 헤더 */}
      <Header />

      {/* 사이드바 + 메인 화면 입니다! */}
      <Box sx={{ display: "flex", flex: 1, height:"100vh" }}>
        <Slidebar />

        {/* 요기가 메인 */}
        <Box
          component="main"
          sx={{
            flex: 1,
            p: 3,
            backgroundColor: "#f8f9fa",
          }}
        >
          <Outlet />
        </Box>
      </Box>

      {/* 푸터 */}
      <Footer />
    </Box>
  );
}
