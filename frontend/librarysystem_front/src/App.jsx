import { createBrowserRouter, RouterProvider } from "react-router-dom";

import Layout from "./layout/Layout";
import MainPage from "./pages/MainPage";
import NewBookPage from "./pages/NewBookPage";
import DetailBookPage from "./pages/DetailBookPage";
import EditBookPage from "./pages/EditBookPage";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import AdminLoginPage from "./pages/AdminLoginPage";
import { SearchProvider } from "./context/SearchContext";
import { AuthProvider } from "./context/AuthContext";

export default function App() {
  const router = createBrowserRouter([
    {
      path: "/",
      element: <AuthProvider><SearchProvider><Layout /></SearchProvider></AuthProvider>,
      children: [
        { index: true, element: <MainPage /> }, // 메인 페이지
        { path: "add-book", element: <NewBookPage /> }, // 도서 등록 페이지
        { path: "book/:bookId", element: <DetailBookPage /> }, // 상세 페이지 이동
        { path: "book/:bookId/edit", element: <EditBookPage /> }, // 수정 페이지
      ],
    },
    { path: "login", element: <AuthProvider><LoginPage /></AuthProvider> }, // 로그인 페이지
    { path: "signup", element: <SignupPage /> }, // 회원가입 페이지
    { path: "/admin/login", element: <AuthProvider><AdminLoginPage /></AuthProvider> }, // 관리자 로그인 페이지
  ]);

  return <RouterProvider router={router} />;
}
