import { useEffect, useState } from "react";
import api from "../../api/api";
import DataTable from "../../components/tables/DataTable";
import StatusBadge from "../../components/ui/StatusBadge";

export default function ReceiptsPage() {
  const [receipts, setReceipts] = useState([]);

  useEffect(() => {
    api
      .get("/v1/receipts")
      .then((res) => {
        setReceipts(res.data);
      })
      .catch((err) => {
        console.error("Failed to load receipts", err);
      });
  }, []);

  const columns = [
    { header: "ID", accessor: "id" },
    { header: "Supplier", accessor: "supplier" },
    { header: "Warehouse", accessor: "warehouse" },
    { header: "Quantity", accessor: "quantity" },
    { header: "Date", accessor: "date" },
    { header: "Status", accessor: "status" },
  ];

  const formattedData = receipts.map((receipt) => ({
    ...receipt,
    status: <StatusBadge status={receipt.status} />,
  }));

  return (
    <div>
      <h2>Receipts</h2>

      <DataTable columns={columns} data={formattedData} />
    </div>
  );
}
