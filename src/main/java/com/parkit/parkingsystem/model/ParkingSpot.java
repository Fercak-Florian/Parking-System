package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

public class ParkingSpot implements Cloneable {
    private int number;
    private ParkingType parkingType;
    private boolean isAvailable;

    public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
	this.number = number;
	this.parkingType = parkingType;
	this.isAvailable = isAvailable;
    }

    /* IMPLEMENTATION DE LA METHODE CLONE */
    @Override
    public ParkingSpot clone() {
	ParkingSpot parkingSpot = null;
	try {
	    parkingSpot = (ParkingSpot) super.clone();
	} catch (CloneNotSupportedException cnse) {
	    cnse.printStackTrace(System.err);
	}
	return parkingSpot;
    }

    public int getId() {
	return number;
    }

    public void setId(int number) {
	this.number = number;
    }

    public ParkingType getParkingType() {
	return parkingType;
    }

    public void setParkingType(ParkingType parkingType) {
	this.parkingType = parkingType;
    }

    public boolean isAvailable() {
	return isAvailable;
    }

    public void setAvailable(boolean available) {
	isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null || getClass() != o.getClass())
	    return false;
	ParkingSpot that = (ParkingSpot) o;
	return number == that.number;
    }

    @Override
    public int hashCode() {
	return number;
    }
}
