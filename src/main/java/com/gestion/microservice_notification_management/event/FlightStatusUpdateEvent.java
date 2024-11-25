package com.gestion.microservice_notification_management.event;

import java.io.Serializable;

public class FlightStatusUpdateEvent implements Serializable {
    private Integer flightId;
    private String newStatus;

    public FlightStatusUpdateEvent() {}

    public FlightStatusUpdateEvent(Integer flightId, String newStatus) {
        this.flightId = flightId;
        this.newStatus = newStatus;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}
