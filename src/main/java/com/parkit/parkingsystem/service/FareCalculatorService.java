package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
	if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
	    throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
	}

	long inTime = ticket.getInTime().getTime();
	long outTime = ticket.getOutTime().getTime();

	long duration = outTime - inTime;

	switch (ticket.getParkingSpot().getParkingType()) {
	case CAR: {
	    if (duration <= 1800000) {
		ticket.setPrice(0);
	    } else if (duration > 1800000) {
		ticket.setPrice(((double) duration / 3600000) * Fare.CAR_RATE_PER_HOUR);
	    }
	    break;
	}
	case BIKE: {
	    if (duration <= 1800000) {
		ticket.setPrice(0);
	    } else if (duration > 1800000) {
		ticket.setPrice(((double) duration / 3600000) * Fare.BIKE_RATE_PER_HOUR);
	    }
	    break;
	}
	default:
	    throw new IllegalArgumentException("Unkown Parking Type");
	}
    }

    public void calculateFareWithFivePercentDiscount(Ticket ticket) {
	if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
	    throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
	}

	long inTime = ticket.getInTime().getTime();
	long outTime = ticket.getOutTime().getTime();

	long duration = outTime - inTime;

	switch (ticket.getParkingSpot().getParkingType()) {
	case CAR: {
	    if (duration <= 1800000) {
		ticket.setPrice(0);
	    } else if (duration > 1800000) {
		ticket.setPrice(((double) duration / 3600000) * Fare.CAR_RATE_PER_HOUR_DISCOUNT);
	    }
	    break;
	}
	case BIKE: {
	    if (duration <= 1800000) {
		ticket.setPrice(0);
	    } else if (duration > 1800000) {
		ticket.setPrice(((double) duration / 3600000) * Fare.BIKE_RATE_PER_HOUR_DISCOUNT);
	    }
	    break;
	}
	default:
	    throw new IllegalArgumentException("Unkown Parking Type");
	}
    }
}