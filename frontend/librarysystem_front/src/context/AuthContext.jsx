import { createContext, useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [role, setRole] = useState(null);

  const isAdmin = role === "ADMIN";

  // 🔁 최초 마운트 시 로그인 복구
  useEffect(() => {
    const token = sessionStorage.getItem("accessToken");
    const storedRole = sessionStorage.getItem("role");
    const loginId = sessionStorage.getItem("loginId");

    if (token && storedRole) {
      setIsLoggedIn(true);
      setRole(storedRole);
    }
  }, []);

  // 🔐 로그인 (관리자 / 유저 공통)
  function authLogin({ accessToken, role, loginId }) {
    sessionStorage.setItem("accessToken", accessToken);
    sessionStorage.setItem("role", role);
    sessionStorage.setItem("loginId", loginId);

    setIsLoggedIn(true);
    setRole(role);
  };

  // 🚪 로그아웃
  function authLogout() {
    sessionStorage.clear();
    setIsLoggedIn(false);
    setRole(null);
    navigate("/login");
  };

  return (
    <AuthContext.Provider value={{ isLoggedIn, role, isAdmin, authLogin, authLogout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
