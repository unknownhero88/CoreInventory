import { useEffect, useState } from "react";
import api from "../../api/api";
import DataTable from "../../components/tables/DataTable";

export default function WarehousesPage() {
  const [warehouses, setWarehouses] = useState([]);

  useEffect(() => {
    api
      .get("/v1/warehouses")
      .then((res) => {
        setWarehouses(res.data);
      })
      .catch((err) => {
        console.error("Failed to load warehouses", err);
      });
  }, []);

  const columns = [
    { header: "ID", accessor: "id" },
    { header: "Warehouse Name", accessor: "name" },
    { header: "Address", accessor: "address" },
    { header: "Manager", accessor: "managerName" },
  ];

  return (
    <div>
      <h2>Warehouses</h2>

      <DataTable columns={columns} data={warehouses} />
    </div>
  );
}
