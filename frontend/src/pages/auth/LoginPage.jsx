import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/api";

export default function LoginPage() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
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
      .post("/auth/login", form)
      .then((res) => {
        const token = res.data.accessToken;

        localStorage.setItem("token", token);

        navigate("/dashboard");
      })
      .catch((err) => {
        setError("Invalid email or password");
        console.error(err);
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
          width: "320px",
        }}
      >
        <h2 style={{ marginBottom: "20px" }}>Login</h2>

        {error && <p style={{ color: "red", marginBottom: "10px" }}>{error}</p>}

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
            background: "#3498db",
            border: "none",
            color: "white",
            borderRadius: "5px",
            cursor: "pointer",
          }}
        >
          Login
        </button>
      </form>
    </div>
  );
}
