import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import AppLayout from "./layout/AppLayout";

import LoginPage from "./pages/auth/LoginPage";
import DashboardPage from "./pages/dashboard/DashboardPage";
import ProductsPage from "./pages/products/ProductsPage";
import LandingPage from "./pages/LandingPage";
import ReceiptsPage from "./pages/operations/ReceiptsPage";
import DeliveriesPage from "./pages/operations/DeliveriesPage";
import TransfersPage from "./pages/operations/TransfersPage";
import AdjustmentsPage from "./pages/operations/AdjustmentsPage";
import RegisterPage from "./pages/auth/RegisterPage";
import MoveHistoryPage from "./pages/history/MoveHistoryPage";

import StockPage from "./pages/stock/StockPage";

import WarehousesPage from "./pages/warehouse/WarehousesPage";
import LocationsPage from "./pages/warehouse/LocationsPage";

function App() {
  const token = localStorage.getItem("token");

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LandingPage />} />

        {/* Login route */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route
          path="/dashboard"
          element={
            token ? (
              <AppLayout>
                <DashboardPage />
              </AppLayout>
            ) : (
              <Navigate to="/login" />
            )
          }
        />

        <Route
          path="/products"
          element={
            token ? (
              <AppLayout>
                <ProductsPage />
              </AppLayout>
            ) : (
              <Navigate to="/login" />
            )
          }
        />

        <Route
          path="/receipts"
          element={
            token ? (
              <AppLayout>
                <ReceiptsPage />
              </AppLayout>
            ) : (
              <Navigate to="/login" />
            )
          }
        />

        <Route
          path="/deliveries"
          element={
            token ? (
              <AppLayout>
                <DeliveriesPage />
              </AppLayout>
            ) : (
              <Navigate to="/login" />
            )
          }
        />

        <Route
          path="/transfers"
          element={
            token ? (
              <AppLayout>
                <TransfersPage />
              </AppLayout>
            ) : (
              <Navigate to="/login" />
            )
          }
        />

        <Route
          path="/adjustments"
          element={
            token ? (
              <AppLayout>
                <AdjustmentsPage />
              </AppLayout>
            ) : (
              <Navigate to="/login" />
            )
          }
        />

        <Route
          path="/history"
          element={
            token ? (
              <AppLayout>
                <MoveHistoryPage />
              </AppLayout>
            ) : (
              <Navigate to="/login" />
            )
          }
        />

        <Route
          path="/stock"
          element={
            token ? (
              <AppLayout>
                <StockPage />
              </AppLayout>
            ) : (
              <Navigate to="/login" />
            )
          }
        />

        <Route
          path="/warehouses"
          element={
            token ? (
              <AppLayout>
                <WarehousesPage />
              </AppLayout>
            ) : (
              <Navigate to="/login" />
            )
          }
        />

        <Route
          path="/locations"
          element={
            token ? (
              <AppLayout>
                <LocationsPage />
              </AppLayout>
            ) : (
              <Navigate to="/login" />
            )
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
