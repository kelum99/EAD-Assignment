import React from 'react';
import { Route, Routes } from 'react-router-dom';
import UserManagement from "./pages/UserManagement";
import Login from "./pages/Login";
import TravelerManagement from "./pages/TravelerManagement";
import TrainManagement from "./pages/TrainManagement";
import TicketBookingManagement from "./pages/TicketBookingManagement";
export default function MainRoutes() {
    return (
        <>
            <Routes>
                <Route path="/" element={<Login />} />
                <Route path="/user-management" element={<UserManagement />} />
                <Route path="/traveler-management" element={<TravelerManagement />} />
                <Route path="/ticket-booking-management" element={<TicketBookingManagement />} />
                <Route path="/train-management" element={<TrainManagement />} />
            </Routes>
        </>
    );
}
