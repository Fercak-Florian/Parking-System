package com.parkit.parkingsystem.model;

import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public ParkingSpot getParkingSpot() {
	ParkingSpot parkingSpotClone = new ParkingSpot(parkingSpot.getId(), parkingSpot.getParkingType(),
		parkingSpot.isAvailable());
	return parkingSpotClone;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
	ParkingSpot parkingSpotClone = new ParkingSpot(parkingSpot.getId(), parkingSpot.getParkingType(),
		parkingSpot.isAvailable());
	this.parkingSpot = parkingSpotClone;
    }

    public String getVehicleRegNumber() {
	return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
	this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
	return price;
    }

    public void setPrice(double price) {
	this.price = price;
    }

    public Date getInTime() {
	if (inTime == null) {
	    return null;
	} else {
	    return (Date) inTime.clone();
	}
    }

    public void setInTime(Date inTime) {
	if (inTime == null) {
	    this.inTime = null;
	} else {
	    this.inTime = (Date) inTime.clone();
	}
    }

    public Date getOutTime() {
	if (outTime == null) {
	    return null;
	} else {
	    return (Date) this.outTime.clone();
	}
    }

    public void setOutTime(Date outTime) {
	if (outTime == null) {
	    this.outTime = null;
	} else {
	    this.outTime = (Date) outTime.clone();
	}
    }
}
