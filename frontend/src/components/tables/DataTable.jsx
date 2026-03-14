import React from "react";

export default function DataTable({ columns = [], data = [] }) {
  return (
    <div style={{ overflowX: "auto", marginTop: "20px" }}>
      <table
        style={{
          width: "100%",
          borderCollapse: "collapse",
          background: "#1e1e1e",
          color: "white",
        }}
      >
        <thead>
          <tr>
            {columns.map((col) => (
              <th
                key={col.accessor}
                style={{
                  borderBottom: "1px solid #444",
                  padding: "10px",
                  textAlign: "left",
                }}
              >
                {col.header}
              </th>
            ))}
          </tr>
        </thead>

        <tbody>
          {data.length === 0 ? (
            <tr>
              <td colSpan={columns.length} style={{ padding: "15px" }}>
                No data available
              </td>
            </tr>
          ) : (
            data.map((row, index) => (
              <tr key={index}>
                {columns.map((col) => (
                  <td
                    key={col.accessor}
                    style={{
                      padding: "10px",
                      borderBottom: "1px solid #333",
                    }}
                  >
                    {row[col.accessor]}
                  </td>
                ))}
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
