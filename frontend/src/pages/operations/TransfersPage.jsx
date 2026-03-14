import { useEffect, useState } from "react";
import api from "../../api/api";
import DataTable from "../../components/tables/DataTable";
import StatusBadge from "../../components/ui/StatusBadge";

export default function TransfersPage() {
  const [transfers, setTransfers] = useState([]);

  useEffect(() => {
    api
      .get("/v1/transfers")
      .then((res) => {
        setTransfers(res.data);
      })
      .catch((err) => {
        console.error("Failed to load transfers", err);
      });
  }, []);

  const columns = [
    { header: "ID", accessor: "id" },
    { header: "Product", accessor: "productName" },
    { header: "From Warehouse", accessor: "fromWarehouse" },
    { header: "To Warehouse", accessor: "toWarehouse" },
    { header: "Quantity", accessor: "quantity" },
    { header: "Date", accessor: "date" },
    { header: "Status", accessor: "status" },
  ];

  const formattedData = transfers.map((transfer) => ({
    ...transfer,
    status: <StatusBadge status={transfer.status} />,
  }));

  return (
    <div>
      <h2>Transfers</h2>
      <DataTable columns={columns} data={formattedData} />
    </div>
  );
}
