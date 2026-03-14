import { useEffect, useState } from "react";
import api from "../../api/api";
import DataTable from "../../components/tables/DataTable";
import StatusBadge from "../../components/ui/StatusBadge";

export default function MoveHistoryPage() {
  const [moves, setMoves] = useState([]);

  useEffect(() => {
    api
      .get("/v1/movements")
      .then((res) => {
        setMoves(res.data);
      })
      .catch((err) => {
        console.error("Failed to load move history", err);
      });
  }, []);

  const columns = [
    { header: "Reference", accessor: "reference" },
    { header: "Date", accessor: "date" },
    { header: "Contact", accessor: "contact" },
    { header: "From", accessor: "fromLocation" },
    { header: "To", accessor: "toLocation" },
    { header: "Quantity", accessor: "quantity" },
    {
      header: "Status",
      accessor: "status",
      render: (row) => <StatusBadge status={row.status} />,
    },
  ];

  const formattedData = moves.map((move) => ({
    ...move,
    status: <StatusBadge status={move.status} />,
  }));

  return (
    <div>
      <h2>Move History</h2>

      <DataTable columns={columns} data={formattedData} />
    </div>
  );
}
