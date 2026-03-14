import { useEffect, useState } from "react";
import api from "../../api/api";
import DataTable from "../../components/tables/DataTable";
import StatusBadge from "../../components/ui/StatusBadge";

export default function AdjustmentsPage() {
  const [adjustments, setAdjustments] = useState([]);

  useEffect(() => {
    api
      .get("/v1/adjustments")
      .then((res) => {
        setAdjustments(res.data);
      })
      .catch((err) => {
        console.error("Failed to load adjustments", err);
      });
  }, []);

  const columns = [
    { header: "ID", accessor: "id" },
    { header: "Product", accessor: "productName" },
    { header: "Warehouse", accessor: "warehouse" },
    { header: "Quantity Change", accessor: "quantity" },
    { header: "Reason", accessor: "reason" },
    {
      header: "Status",
      accessor: "status",
    },
  ];

  const formattedData = adjustments.map((adj) => ({
    ...adj,
    status: <StatusBadge status={adj.status} />,
  }));

  return (
    <div>
      <h2>Stock Adjustments</h2>

      <DataTable columns={columns} data={formattedData} />
    </div>
  );
}
