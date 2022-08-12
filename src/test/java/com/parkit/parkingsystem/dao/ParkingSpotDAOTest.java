package com.parkit.parkingsystem.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

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

    private static ParkingSpotDAO parkingSpotDAO;
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    @BeforeAll
    public static void setUp() {
	parkingSpotDAO = new ParkingSpotDAO();
	parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
    }

    @Test
    @DisplayName("Test si une place de voiture est disponible")
    public void testGettingNextAvailableCarSlot() {
	int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
	assertThat(result).isBetween(1, 3);
    }

    @Test
    @DisplayName("Test si une place de moto est disponible")
    public void testGettingNextAvailableBikeSlot() {
	int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
	assertThat(result).isBetween(4, 5);
    }

    @Test
    @DisplayName("Test quand un type null est passé on obtient -1")
    public void testWhenParameterOfGetNextAvailableSlotIsNull() {
	int result = parkingSpotDAO.getNextAvailableSlot(null);
	assertThat(result).isEqualTo(-1);
    }

    @Test
    @DisplayName("Test que la mise à jour d'une place de parking est ok")
    public void testUpdatingParkingSpot() {
	ParkingSpot parkingSpotToUpdate = new ParkingSpot(2, ParkingType.CAR, false);
	boolean result = parkingSpotDAO.updateParking(parkingSpotToUpdate);
	assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Test que la mise à jour n'est pas faite")
    public void testNoUpdateParkingSpot() {
	ParkingSpot parkingSpotWithError = new ParkingSpot(-1, null, false);
	boolean result = parkingSpotDAO.updateParking(parkingSpotWithError);
	assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Test pour une erreur dans le process de mise à jour")
    public void testCatchingExceptionWhenUpdateParking() throws SQLException {
	ParkingSpot parkingSpotToUpdate = new ParkingSpot(1, null, false);
	parkingSpotToUpdate = null;
	boolean result = parkingSpotDAO.updateParking(parkingSpotToUpdate);
	assertThat(result).isFalse();
    }
}
