package com.example.ead_mobile.models;

public class ReservationResponseModel {
    private String id;
    private String userNIC;
    private String bookingDate;
    private String reservationDate;
    private String noOfTickets;
    private String route;
    private String train;
    private String startingPoint;
    private String destination;
    private String time;
    private String agentID;

    //ReservationResponse Constructors
    public ReservationResponseModel() {
    }

    public ReservationResponseModel(String id, String userNIC, String bookingDate, String reservationDate, String noOfTickets, String route, String train, String startingPoint, String destination, String time, String agentID) {
        this.id = id;
        this.userNIC = userNIC;
        this.bookingDate = bookingDate;
        this.reservationDate = reservationDate;
        this.noOfTickets = noOfTickets;
        this.route = route;
        this.train = train;
        this.startingPoint = startingPoint;
        this.destination = destination;
        this.time = time;
        this.agentID = agentID;
    }

    //ReservationResponse Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserNIC() {
        return userNIC;
    }

    public void setUserNIC(String userNIC) {
        this.userNIC = userNIC;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getNoOfTickets() {
        return noOfTickets;
    }

    public void setNoOfTickets(String noOfTickets) {
        this.noOfTickets = noOfTickets;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getTrain() {
        return train;
    }

    public void setTrain(String train) {
        this.train = train;
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }
}
