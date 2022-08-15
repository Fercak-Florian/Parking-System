package com.parkit.parkingsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorServiceTest {

    /* INITIALISATION DU CONTEXTE DES TESTS */

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
	fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
	ticket = new Ticket();
    }

    /*
     * ===============================================================
     * =================== CALCUL DU PRIX STANDARD ===================
     * ===============================================================
     */

    @Test
    @DisplayName("La methode calcule le prix pour 1 voiture stationnant 1H")
    public void calculateFareCar() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertThat(ticket.getPrice()).isEqualTo(Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    @DisplayName("La methode calcule le prix pour 1 moto stationnant 1H")
    public void calculateFareBike() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertThat(ticket.getPrice()).isEqualTo(Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    @DisplayName("La methode calcule le prix pour 1 moto stationnant 45 minutes")
    public void calculateFareBikeWithLessThanOneHourParkingTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertThat(ticket.getPrice()).isEqualTo((0.75 * Fare.BIKE_RATE_PER_HOUR));
    }

    @Test
    @DisplayName("La methode calcule le prix pour 1 voiture stationnant 45 minutes")
    public void calculateFareCarWithLessThanOneHourParkingTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertThat(ticket.getPrice()).isEqualTo((0.75 * Fare.CAR_RATE_PER_HOUR));
    }

    @Test
    @DisplayName("La methode calcule le prix pour 1 voiture stationnant 1 journée")
    public void calculateFareCarWithMoreThanADayParkingTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertThat(ticket.getPrice()).isEqualTo((24 * Fare.CAR_RATE_PER_HOUR));
    }

    @Test
    @DisplayName("La methode calcule le prix pour 1 voiture stationnant moins de 30 minutes : 0 ")
    public void calculateFareCarWithLessThanThirtyMinutes() {
	Date inTime = new Date();
	Date outTime = new Date();
	outTime.setTime(System.currentTimeMillis() + 30 * 60 * 1000); // HEURE ACTUELLE + 30 MINUTES
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertThat(ticket.getPrice()).isEqualTo((0));
    }

    @Test
    @DisplayName("La methode calcule le prix pour 1 moto stationnant moins de 30 minutes : 0 ")
    public void calculateFareBikeWithLessThanThirtyMinutes() {
	Date inTime = new Date();
	Date outTime = new Date();
	outTime.setTime(System.currentTimeMillis() + 30 * 60 * 1000); // HEURE ACTUELLE + 30 MINUTES
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertThat(ticket.getPrice()).isEqualTo((0));
    }

    @Test
    @DisplayName("La methode lève une exception pour un type de vehicule inconnu")
    public void calculateFareUnkownType() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    @DisplayName("La methode lève une exception pour une heure d'entrée future")
    public void calculateFareBikeWithFutureInTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000)); // AJOUT D'1 HEURE
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    @DisplayName("La methode lève une exception pour une heure de sortie nulle")
    public void calculateFareWithANullOutTime() {
	Date inTime = new Date();
	Date outTime = null;
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    @DisplayName("La methode lève une exception pour une heure de sortie précédent l'heure d'entrée")
    public void calculateFareWithAOutTimeBeforeInTime() {
	Date inTime = new Date();
	Date outTime = new Date();
	outTime.setTime(System.currentTimeMillis() - 30 * 60 * 1000); // HEURE ACTUELLE - 30 MINUTES
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    /*
     * ===============================================================
     * ================ CALCUL DU PRIX AVEC REDUCTION ================
     * ===============================================================
     */

    @Test
    @DisplayName("La methode calcule une réduction de 5% pour utilisateur voiture réguliers")
    public void calculateFivePercentDiscountForCarDriverRecurringUsers() {
	Date inTime = new Date();
	Date outTime = new Date();
	outTime.setTime(System.currentTimeMillis() + 60 * 60 * 1000); // HEURE ACTUELLE + 1H
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFareWithFivePercentDiscount(ticket);
	assertThat(ticket.getPrice()).isEqualTo(1 * Fare.CAR_RATE_PER_HOUR_DISCOUNT);
    }

    @Test
    @DisplayName("La methode calcule une réduction de 5% pour utilisateur moto réguliers")
    public void calculateFivePercentDiscountForBikeDriverRecurringUsers() {
	Date inTime = new Date();
	Date outTime = new Date();
	outTime.setTime(System.currentTimeMillis() + 60 * 60 * 1000); // HEURE ACTUELLE + 1H
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFareWithFivePercentDiscount(ticket);
	assertThat(ticket.getPrice()).isEqualTo(1 * Fare.BIKE_RATE_PER_HOUR_DISCOUNT);
    }

    @Test
    @DisplayName("La methode calcule le prix avec reduction pour 1 moto stationnant 45 minutes")
    public void calculateDiscountFareForBikeWithLessThanOneHourParkingTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFareWithFivePercentDiscount(ticket);
	assertThat(ticket.getPrice()).isEqualTo((0.75 * Fare.BIKE_RATE_PER_HOUR_DISCOUNT));
    }

    @Test
    @DisplayName("La methode calcule le prix avec reduction pour 1 voiture stationnant 45 minutes")
    public void calculateDiscountFareForCarWithLessThanOneHourParkingTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFareWithFivePercentDiscount(ticket);
	assertThat(ticket.getPrice()).isEqualTo((0.75 * Fare.CAR_RATE_PER_HOUR_DISCOUNT));
    }

    @Test
    @DisplayName("La methode calcule le prix avec reduction pour 1 moto stationnant moins de 30 minutes : 0")
    public void calculateDiscountFareBikeWithLessThanThirtyMinutes() {
	Date inTime = new Date();
	Date outTime = new Date();
	outTime.setTime(System.currentTimeMillis() + 30 * 60 * 1000); // HEURE ACTUELLE + 30 MINUTES
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFareWithFivePercentDiscount(ticket);
	assertThat(ticket.getPrice()).isEqualTo((0));
    }

    @Test
    @DisplayName("La methode calcule le prix avec reduction pour 1 voiture stationnant moins de 30 minutes : 0")
    public void calculateDiscountFareCarWithLessThanThirtyMinutes() {
	Date inTime = new Date();
	Date outTime = new Date();
	outTime.setTime(System.currentTimeMillis() + 30 * 60 * 1000); // HEURE ACTUELLE + 30 MINUTES
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFareWithFivePercentDiscount(ticket);
	assertThat(ticket.getPrice()).isEqualTo((0));
    }

    @Test
    @DisplayName("La methode lève une exception pour une heure de sortie nulle")
    public void calculateDiscountFareWithANullOutTime() {
	Date inTime = new Date();
	Date outTime = null;
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	assertThrows(NullPointerException.class,
		() -> fareCalculatorService.calculateFareWithFivePercentDiscount((ticket)));
    }

    @Test
    @DisplayName("La methode lève une exception pour une heure de sortie precedent l'heure d'entrée")
    public void calculateDiscountFareWithAOutTimeBeforeInTime() {
	Date inTime = new Date();
	Date outTime = new Date();
	outTime.setTime(System.currentTimeMillis() - 30 * 60 * 1000); // HEURE ACTUELLE - 30 MINUTES
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	assertThrows(IllegalArgumentException.class,
		() -> fareCalculatorService.calculateFareWithFivePercentDiscount(ticket));
    }
}
