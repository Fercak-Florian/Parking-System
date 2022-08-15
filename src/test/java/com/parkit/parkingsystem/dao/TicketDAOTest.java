package com.parkit.parkingsystem.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class TicketDAOTest {

    /* INITIALISATION DU CONTEXTE DES TESTS */

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static ParkingSpot parkingSpot;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    private static Ticket ticketToSave;
    private static Ticket ticketToUpdate;

    @BeforeAll
    private static void setUp() throws Exception {
	parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
	parkingSpotDAO = new ParkingSpotDAO();
	parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
	ticketDAO = new TicketDAO();
	ticketDAO.dataBaseConfig = dataBaseTestConfig;
	dataBasePrepareService = new DataBasePrepareService();
	ticketToSave = new Ticket();
	ticketToUpdate = new Ticket();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
	dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {
    }

    /* TESTS CONCERANT LE COMPORTEMENT DE LA METHODE saveTicket */

    @Test
    @DisplayName("La methode sauvegarde un ticket en BDD")
    public void testSavingATicketInDataBase() {
	// GIVEN
	Date inTime = new Date();
	Date outTime = new Date();
	inTime.setTime(System.currentTimeMillis());
	outTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000)); // AJOUT 1 HEURE

	ticketToSave.setId(1);
	ticketToSave.setParkingSpot(parkingSpot);
	ticketToSave.setVehicleRegNumber("AT-444-ST");
	ticketToSave.setPrice(5);
	ticketToSave.setInTime(inTime);
	ticketToSave.setOutTime(outTime);

	// WHEN
	ticketDAO.saveTicket(ticketToSave);
	Ticket savedTicket = ticketDAO.getTicket("AT-444-ST");

	// THEN
	assertThat(savedTicket.getId()).isEqualTo(ticketToSave.getId());

	assertThat(savedTicket.getParkingSpot()).isEqualTo(ticketToSave.getParkingSpot());
	assertThat(savedTicket.getPrice()).isEqualTo(savedTicket.getPrice());

	long offset = 900;
	assertThat(savedTicket.getInTime().getTime()).isCloseTo(ticketToSave.getInTime().getTime(), within(offset));
	assertThat(savedTicket.getOutTime().getTime()).isCloseTo(ticketToSave.getOutTime().getTime(), within(offset));
    }

    @Test
    @DisplayName("La methode lève une exception")
    public void testFailedToSaveTicket() {
	ticketToSave = null;
	boolean result = ticketDAO.saveTicket(ticketToSave);
	assertThat(result).isFalse();
    }

    /* TESTS CONCERNANT LE COMPORTEMENT DE LA METHODE getTicket */

    @Test
    @DisplayName("La methode 'echoue' lors de la recuperation d'un ticket null")
    public void testFailedToGetTicket() {
	Ticket gettedTicket = new Ticket();
	gettedTicket = ticketDAO.getTicket(null);
	assertThat(gettedTicket).isNull();
    }

    /* TESTS CONCERNANT LE COMPORTEMENT DE LA METHODE updateTicket */

    @Test
    @DisplayName("La methode met à jour un ticket en BDD")
    public void testUpdatingATicketInDataBase() {
	// GIVEN
	testSavingATicketInDataBase();
	Date outTime = new Date();
	outTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000)); // AJOUT 1 HEURE

	ticketToUpdate.setId(1);
	ticketToUpdate.setPrice(10);
	ticketToUpdate.setOutTime(outTime);

	// WHEN
	boolean result = ticketDAO.updateTicket(ticketToUpdate);
	Ticket updatedTicket = ticketDAO.getTicket("AT-444-ST");

	// THEN
	assertThat(result).isTrue();
	assertThat(updatedTicket.getId()).isEqualTo(ticketToUpdate.getId());
	assertThat(updatedTicket.getPrice()).isEqualTo(ticketToUpdate.getPrice());

	long offset = 900;
	assertThat(updatedTicket.getOutTime().getTime()).isCloseTo(ticketToUpdate.getOutTime().getTime(),
		within(offset));

    }

    @Test
    @DisplayName("La methode lève une exception lors de la mise à jour d'un ticket")
    public void testFaillingToUpdateATicketInDataBase() {
	ticketToUpdate = null;
	boolean result = ticketDAO.updateTicket(ticketToUpdate);
	assertThat(result).isFalse();
    }

    /* TESTS CONCERNANT LE COMPORTEMENT DE LA METHODE getCountForVehicleRegNumber */

    @Test
    @DisplayName("La methode compte le nombre de ticket dans la BDD")
    public void getCountForVehicleRegNumber() {
	testSavingATicketInDataBase();
	int result = ticketDAO.getCountForVehicleRegNumber("AT-444-ST");
	assertThat(result).isGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("La methode compte le nombre de ticket pour une plaque d'immatriculation inconnue")
    public void testGetForVehicleRegNumberWithUnknownRegistrationNumber() {
	int result = ticketDAO.getCountForVehicleRegNumber(";");
	assertThat(result).isEqualTo(0);
    }
}
