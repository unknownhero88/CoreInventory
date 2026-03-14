import { useEffect, useState } from "react";
import api from "../../api/api";
import DataTable from "../../components/tables/DataTable";
import StatusBadge from "../../components/ui/StatusBadge";

export default function DeliveriesPage() {
  const [deliveries, setDeliveries] = useState([]);

  useEffect(() => {
    api
      .get("/v1/deliveries")
      .then((res) => {
        setDeliveries(res.data);
      })
      .catch((err) => {
        console.error("Failed to load deliveries", err);
      });
  }, []);

  const columns = [
    { header: "ID", accessor: "id" },
    { header: "Customer", accessor: "customer" },
    { header: "Warehouse", accessor: "warehouse" },
    { header: "Total Quantity", accessor: "quantity" },
    { header: "Date", accessor: "date" },
    { header: "Status", accessor: "status" },
  ];

  const formattedData = deliveries.map((delivery) => ({
    ...delivery,
    status: <StatusBadge status={delivery.status} />,
  }));

  return (
    <div>
      <h2>Deliveries</h2>

      <DataTable columns={columns} data={formattedData} />
    </div>
  );
}
