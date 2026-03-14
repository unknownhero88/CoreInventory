import { useEffect, useState } from "react";
import api from "../../api/api";
import DataTable from "../../components/tables/DataTable";

export default function ProductsPage() {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    api
      .get("/v1/products")
      .then((res) => {
        setProducts(res.data);
      })
      .catch((err) => {
        console.error("Failed to load products", err);
      });
  }, []);

  const columns = [
    { header: "ID", accessor: "id" },
    { header: "Name", accessor: "name" },
    { header: "SKU", accessor: "sku" },
    { header: "Category", accessor: "category" },
    { header: "Price", accessor: "price" },
    { header: "Stock", accessor: "stock" },
  ];

  return (
    <div>
      <h2>Products</h2>

      <DataTable columns={columns} data={products} />
    </div>
  );
}
