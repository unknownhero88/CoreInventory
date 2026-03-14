import { useEffect, useState } from "react";
import api from "../../api/api";
import DataTable from "../../components/tables/DataTable";

export default function StockPage() {
  const [stock, setStock] = useState([]);

  useEffect(() => {
    api
      .get("/v1/stock")
      .then((res) => {
        setStock(res.data);
      })
      .catch((err) => {
        console.error("Failed to load stock", err);
      });
  }, []);

  const columns = [
    { header: "Product", accessor: "productName" },
    { header: "Warehouse", accessor: "warehouseName" },
    { header: "Location", accessor: "locationName" },
    { header: "Quantity", accessor: "quantity" },
  ];

  return (
    <div>
      <h2>Stock</h2>

      <DataTable columns={columns} data={stock} />
    </div>
  );
}
