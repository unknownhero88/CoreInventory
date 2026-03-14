import Sidebar from "./Sidebar";
import Navbar from "./Navbar";

export default function AppLayout({ children }) {
  return (
    <div style={{ display: "flex", minHeight: "100vh", background: "#111" }}>
      <Sidebar />

      <div style={{ flex: 1 }}>
        <Navbar />

        <div style={{ padding: "20px", color: "white" }}>{children}</div>
      </div>
    </div>
  );
}
