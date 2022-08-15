package com.parkit.parkingsystem.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {

    /* INITIALISATION DU CONTEXTE DES TESTS */

    private static ParkingSpotDAO parkingSpotDAO;
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    @BeforeAll
    public static void setUp() {
	parkingSpotDAO = new ParkingSpotDAO();
	parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
    }

    /* TESTS CONCERNANT LE COMPORTEMENT DE LA METHODE getNextAvailableSlot */

    @Test
    @DisplayName("La methode renvoie une place de voiture disponible")
    public void testGettingNextAvailableCarSlot() {
	int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
	assertThat(result).isBetween(1, 3);
    }

    @Test
    @DisplayName("La methode renvoie une place de moto disponible")
    public void testGettingNextAvailableBikeSlot() {
	int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
	assertThat(result).isBetween(4, 5);
    }

    @Test
    @DisplayName("La methode lève une exception")
    public void testWhenParameterOfGetNextAvailableSlotIsNull() {
	int result = parkingSpotDAO.getNextAvailableSlot(null);
	assertThat(result).isEqualTo(-1);
    }

    /* TESTS CONCERNANT LE COMPORTEMENT DE LA METHODE updateParking */

    @Test
    @DisplayName("La methode met à jour la place de parking")
    public void testUpdatingParkingSpot() {
	ParkingSpot parkingSpotToUpdate = new ParkingSpot(2, ParkingType.CAR, false);
	boolean result = parkingSpotDAO.updateParking(parkingSpotToUpdate);
	assertThat(result).isTrue();
    }

    @Test
    @DisplayName("La methode echoue lors de la mise à jour la place de parking")
    public void testNoUpdateParkingSpot() {
	ParkingSpot parkingSpotWithError = new ParkingSpot(-1, null, false);
	boolean result = parkingSpotDAO.updateParking(parkingSpotWithError);
	assertThat(result).isFalse();
    }

    @Test
    @DisplayName("La methode lève une exception lors de la mise à jour")
    public void testCatchingExceptionWhenUpdateParking() {
	ParkingSpot parkingSpotToUpdate = new ParkingSpot(1, null, false);
	parkingSpotToUpdate = null;
	boolean result = parkingSpotDAO.updateParking(parkingSpotToUpdate);
	assertThat(result).isFalse();
    }
}
