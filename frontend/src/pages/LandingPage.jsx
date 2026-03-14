import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function LandingPage() {
  const navigate = useNavigate();

  const [feedback, setFeedback] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();

    alert("Thank you for your feedback!");

    setFeedback("");
  };

  return (
    <div style={{ fontFamily: "Arial", background: "#111", color: "white" }}>
      {/* HERO SECTION */}
      <div style={{ textAlign: "center", padding: "80px 20px" }}>
        <h1>CoreInventory</h1>

        <p>Smart Inventory Management System</p>

        <div style={{ marginTop: "20px" }}>
          <button
            onClick={() => navigate("/login")}
            style={{ marginRight: "10px", padding: "10px 20px" }}
          >
            Login
          </button>

          <button
            onClick={() => navigate("/register")}
            style={{ padding: "10px 20px" }}
          >
            Register
          </button>
        </div>
      </div>

      {/* FEATURES SECTION */}
      <div style={{ padding: "40px 20px", background: "#1c1c1c" }}>
        <h2 style={{ textAlign: "center" }}>Features</h2>

        <div
          style={{
            display: "flex",
            justifyContent: "center",
            gap: "40px",
            marginTop: "30px",
            flexWrap: "wrap",
          }}
        >
          <Feature
            title="Inventory Tracking"
            description="Track all product stock across warehouses."
          />

          <Feature
            title="Stock Transfers"
            description="Move products between warehouses easily."
          />

          <Feature
            title="Operation History"
            description="Complete ledger of stock movements."
          />

          <Feature
            title="Warehouse Management"
            description="Manage multiple warehouses and locations."
          />
        </div>
      </div>

      {/* FEEDBACK SECTION */}
      <div style={{ padding: "40px 20px" }}>
        <h2 style={{ textAlign: "center" }}>Feedback</h2>

        <form
          onSubmit={handleSubmit}
          style={{
            maxWidth: "400px",
            margin: "auto",
            display: "flex",
            flexDirection: "column",
            gap: "10px",
          }}
        >
          <textarea
            placeholder="Your feedback..."
            value={feedback}
            onChange={(e) => setFeedback(e.target.value)}
            required
            style={{ padding: "10px", minHeight: "100px" }}
          />

          <button type="submit">Submit Feedback</button>
        </form>
      </div>

      {/* FOOTER */}
      <div
        style={{
          textAlign: "center",
          padding: "20px",
          background: "#1c1c1c",
          marginTop: "40px",
        }}
      >
        © 2026 CoreInventory
      </div>
    </div>
  );
}

function Feature({ title, description }) {
  return (
    <div
      style={{
        width: "220px",
        background: "#222",
        padding: "20px",
        borderRadius: "8px",
      }}
    >
      <h3>{title}</h3>
      <p style={{ color: "#aaa" }}>{description}</p>
    </div>
  );
}
