import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Slidebar() {
  const { isAdmin } = useAuth();
  const sidebarStyle = {
    display: "flex",
    flexDirection: "column",
    width: "180px",
    padding: "20px",
    gap: "15px",
    backgroundColor: "black",
    color: "white",
    borderRight: "1px solid #ddd",
  };

  const linkStyle = {
    textDecoration: "none",
    color: "white",
    fontSize: "18px",
    padding: "8px 0",
  };

  return (
    <div style={sidebarStyle}>
      <Link to="/" style={linkStyle}>
        도서목록
      </Link>

      {/* 관리자일 때만 도서 추가 버튼 노출 */}
      {isAdmin && (
        <Link to="/add-book" style={linkStyle}>
          도서 추가
        </Link>
      )}
    </div>
  );
}
