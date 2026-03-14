import { useState } from "react";

export default function OperationForm({ onSubmit }) {
  const [formData, setFormData] = useState({
    type: "RECEIPT",
    productId: "",
    quantity: "",
    fromWarehouse: "",
    toWarehouse: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!formData.productId || !formData.quantity) {
      alert("Product and quantity are required");
      return;
    }

    onSubmit(formData);

    setFormData({
      type: "RECEIPT",
      productId: "",
      quantity: "",
      fromWarehouse: "",
      toWarehouse: "",
    });
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginBottom: "20px" }}>
      <h3>Create Operation</h3>

      <div>
        <label>Operation Type</label>
        <select name="type" value={formData.type} onChange={handleChange}>
          <option value="RECEIPT">Receipt</option>
          <option value="DELIVERY">Delivery</option>
          <option value="TRANSFER">Transfer</option>
          <option value="ADJUSTMENT">Adjustment</option>
        </select>
      </div>

      <div>
        <label>Product ID</label>
        <input
          type="text"
          name="productId"
          value={formData.productId}
          onChange={handleChange}
        />
      </div>

      <div>
        <label>Quantity</label>
        <input
          type="number"
          name="quantity"
          value={formData.quantity}
          onChange={handleChange}
        />
      </div>

      <div>
        <label>From Warehouse</label>
        <input
          type="text"
          name="fromWarehouse"
          value={formData.fromWarehouse}
          onChange={handleChange}
        />
      </div>

      <div>
        <label>To Warehouse</label>
        <input
          type="text"
          name="toWarehouse"
          value={formData.toWarehouse}
          onChange={handleChange}
        />
      </div>

      <button type="submit">Submit</button>
    </form>
  );
}
