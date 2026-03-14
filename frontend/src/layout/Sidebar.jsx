import { Link, useLocation } from "react-router-dom";

export default function Sidebar() {
  const location = useLocation();

  const menuItems = [
    { name: "Dashboard", path: "/dashboard" },
    { name: "Products", path: "/products" },
    { name: "Receipts", path: "/receipts" },
    { name: "Deliveries", path: "/deliveries" },
    { name: "Transfers", path: "/transfers" },
    { name: "Adjustments", path: "/adjustments" },
    { name: "Move History", path: "/history" },
    { name: "Stock", path: "/stock" },
    { name: "Warehouses", path: "/warehouses" },
  ];

  return (
    <div
      style={{
        width: "220px",
        background: "#1c1c1c",
        color: "white",
        minHeight: "100vh",
        padding: "20px",
      }}
    >
      <h2 style={{ marginBottom: "30px" }}>CoreInventory</h2>

      <nav style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
        {menuItems.map((item) => (
          <Link
            key={item.path}
            to={item.path}
            style={{
              padding: "10px",
              borderRadius: "5px",
              textDecoration: "none",
              color: "white",
              background:
                location.pathname === item.path ? "#333" : "transparent",
            }}
          >
            {item.name}
          </Link>
        ))}
      </nav>
    </div>
  );
}
