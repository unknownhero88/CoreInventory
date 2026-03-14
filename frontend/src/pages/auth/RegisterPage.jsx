import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/api";

export default function RegisterPage() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    api
      .post("/auth/register", form)
      .then(() => {
        alert("Registration successful!");
        navigate("/login");
      })
      .catch((err) => {
        console.error(err);
        setError("Registration failed. Try again.");
      });
  };

  return (
    <div
      style={{
        height: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        background: "#111",
        color: "white",
      }}
    >
      <form
        onSubmit={handleSubmit}
        style={{
          background: "#1c1c1c",
          padding: "30px",
          borderRadius: "8px",
          width: "350px",
        }}
      >
        <h2 style={{ marginBottom: "20px" }}>Register</h2>

        {error && <p style={{ color: "red" }}>{error}</p>}

        <div style={{ marginBottom: "15px" }}>
          <label>Full Name</label>

          <input
            type="text"
            name="name"
            value={form.name}
            onChange={handleChange}
            required
            style={{
              width: "100%",
              padding: "8px",
              marginTop: "5px",
            }}
          />
        </div>

        <div style={{ marginBottom: "15px" }}>
          <label>Email</label>

          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            required
            style={{
              width: "100%",
              padding: "8px",
              marginTop: "5px",
            }}
          />
        </div>

        <div style={{ marginBottom: "20px" }}>
          <label>Password</label>

          <input
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
            required
            style={{
              width: "100%",
              padding: "8px",
              marginTop: "5px",
            }}
          />
        </div>

        <button
          type="submit"
          style={{
            width: "100%",
            padding: "10px",
            background: "#2ecc71",
            border: "none",
            color: "white",
            borderRadius: "5px",
            cursor: "pointer",
          }}
        >
          Register
        </button>
      </form>
    </div>
  );
}
