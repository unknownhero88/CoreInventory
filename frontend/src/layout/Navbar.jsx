import { useNavigate } from "react-router-dom";

export default function Navbar() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <div
      style={{
        height: "60px",
        background: "#1c1c1c",
        color: "white",
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        padding: "0 20px",
        borderBottom: "1px solid #333",
      }}
    >
      {/* Left side */}
      <h2>CoreInventory</h2>

      {/* Right side */}
      <div style={{ display: "flex", alignItems: "center", gap: "15px" }}>
        <span>Admin</span>

        <button
          onClick={handleLogout}
          style={{
            padding: "6px 12px",
            background: "#e74c3c",
            border: "none",
            color: "white",
            borderRadius: "4px",
            cursor: "pointer",
          }}
        >
          Logout
        </button>
      </div>
    </div>
  );
}
