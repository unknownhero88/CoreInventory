import "./KPIBox.css";

export default function KPIBox({ title, value, color = "#2c3e50" }) {
  return (
    <div className="kpi-box" style={{ borderLeft: `5px solid ${color}` }}>
      <div className="kpi-content">
        <h3 className="kpi-title">{title}</h3>
        <h1 className="kpi-value">{value}</h1>
      </div>
    </div>
  );
}
