const footerStyle = {
  height: "20mm",
  width: "100%", // ⭐ 화면 가로 전체 폭을 꽉 채우기
  backgroundColor: "#2C3E50", // ← 임의 색상 (추후 변경)
  color: "white",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  fontSize: "0.9rem",
  textAlign: "center",
};

const Footer = () => {
  return (
    <footer style={footerStyle}>© 2025 AIVLE_03_05 도서관리 시스템</footer>
  );
};

export default Footer;
