import { useEffect, useState } from "react";
import api from "../../api/api";
import KPIBox from "../../components/dashboard/KPIBox";

export default function DashboardPage() {
  const [kpis, setKpis] = useState({
    totalProducts: 0,
    lowStock: 0,
    pendingReceipts: 0,
    pendingDeliveries: 0,
    transfers: 0,
  });

  useEffect(() => {
    api
      .get("/v1/dashboard/kpis")
      .then((res) => {
        setKpis(res.data);
      })
      .catch((err) => {
        console.error("Failed to load dashboard KPIs", err);
      });
  }, []);

  return (
    <div>
      <h1 style={{ marginBottom: "20px" }}>Dashboard</h1>

      <div
        style={{
          display: "flex",
          gap: "20px",
          flexWrap: "wrap",
        }}
      >
        <KPIBox
          title="Total Products"
          value={kpis.totalProducts}
          color="#3498db"
        />

        <KPIBox title="Low Stock" value={kpis.lowStock} color="#e74c3c" />

        <KPIBox
          title="Pending Receipts"
          value={kpis.pendingReceipts}
          color="#f39c12"
        />

        <KPIBox
          title="Pending Deliveries"
          value={kpis.pendingDeliveries}
          color="#9b59b6"
        />

        <KPIBox title="Transfers" value={kpis.transfers} color="#2ecc71" />
      </div>
    </div>
  );
}
