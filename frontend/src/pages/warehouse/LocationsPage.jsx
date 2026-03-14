import { useEffect, useState } from "react";
import api from "../../api/api";
import DataTable from "../../components/tables/DataTable";

export default function LocationsPage() {
  const [locations, setLocations] = useState([]);

  useEffect(() => {
    api
      .get("/v1/locations")
      .then((res) => {
        setLocations(res.data);
      })
      .catch((err) => {
        console.error("Failed to load locations", err);
      });
  }, []);

  const columns = [
    { header: "ID", accessor: "id" },
    { header: "Location Name", accessor: "name" },
    { header: "Warehouse", accessor: "warehouseName" },
    { header: "Description", accessor: "description" },
  ];

  return (
    <div>
      <h2>Locations</h2>

      <DataTable columns={columns} data={locations} />
    </div>
  );
}
