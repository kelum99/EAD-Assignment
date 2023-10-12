package com.example.ead_mobile.models;

public class ReservationRequestModel {
    private String UserNIC;
    private String BookingDate;
    private String ReservationDate;
    private String NoOfTickets;
    private String Route;
    private String Train;
    private String StartingPoint;
    private String Destination;
    private String Time;
    private String AgentID;

    public ReservationRequestModel() {
    }

    public ReservationRequestModel(String userNIC, String bookingDate, String reservationDate, String noOfTickets, String route, String train, String startingPoint, String destination, String time, String agentID) {
        UserNIC = userNIC;
        BookingDate = bookingDate;
        ReservationDate = reservationDate;
        NoOfTickets = noOfTickets;
        Route = route;
        Train = train;
        StartingPoint = startingPoint;
        Destination = destination;
        Time = time;
        AgentID = agentID;
    }

    public String getUserNIC() {
        return UserNIC;
    }

    public void setUserNIC(String userNIC) {
        UserNIC = userNIC;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
    }

    public String getReservationDate() {
        return ReservationDate;
    }

    public void setReservationDate(String reservationDate) {
        ReservationDate = reservationDate;
    }

    public String getNoOfTickets() {
        return NoOfTickets;
    }

    public void setNoOfTickets(String noOfTickets) {
        NoOfTickets = noOfTickets;
    }

    public String getRoute() {
        return Route;
    }

    public void setRoute(String route) {
        Route = route;
    }

    public String getTrain() {
        return Train;
    }

    public void setTrain(String train) {
        Train = train;
    }

    public String getStartingPoint() {
        return StartingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        StartingPoint = startingPoint;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getAgentID() {
        return AgentID;
    }

    public void setAgentID(String agentID) {
        AgentID = agentID;
    }
}
