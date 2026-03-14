import React from "react";

export default function StatusBadge({ status }) {
  const getStyle = () => {
    switch (status) {
      case "DRAFT":
        return { backgroundColor: "#6c757d", color: "white" };

      case "READY":
        return { backgroundColor: "#0d6efd", color: "white" };

      case "DONE":
        return { backgroundColor: "#198754", color: "white" };

      case "CANCELLED":
        return { backgroundColor: "#dc3545", color: "white" };

      default:
        return { backgroundColor: "#343a40", color: "white" };
    }
  };

  return (
    <span
      style={{
        padding: "4px 10px",
        borderRadius: "6px",
        fontSize: "12px",
        fontWeight: "bold",
        display: "inline-block",
        ...getStyle(),
      }}
    >
      {status}
    </span>
  );
}
