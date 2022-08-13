package com.parkit.parkingsystem.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception {
	parkingSpotDAO = new ParkingSpotDAO();
	parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
	ticketDAO = new TicketDAO();
	ticketDAO.dataBaseConfig = dataBaseTestConfig;
	dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
	when(inputReaderUtil.readSelection()).thenReturn(1);
	when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterEach
    private void verifyPerTest() throws Exception {
	verify(inputReaderUtil).readSelection();
	verify(inputReaderUtil, atLeast(1)).readVehicleRegistrationNumber();
    }

    @AfterAll
    private static void tearDown() {
    }

    @Test
    @DisplayName("Test du process d'entrée d'un vehicule")
    public void testParkingACar() {
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	Ticket ticketBeforeProcess = ticketDAO.getTicket("ABCDEF");
	assertThat(ticketBeforeProcess).isNull();

	parkingService.processIncomingVehicle();
	Ticket ticketAfterProcess = ticketDAO.getTicket("ABCDEF");

	assertThat(ticketAfterProcess).isNotNull();
	assertThat(ticketAfterProcess.getInTime()).isNotNull();
	assertThat(ticketAfterProcess.getVehicleRegNumber()).isEqualTo("ABCDEF");
	assertThat(ticketAfterProcess.getParkingSpot().isAvailable()).isFalse();
    }

    @Test
    @DisplayName("Test du process de sortie d'un vehicule")
    public void testParkingLotExit() {
	ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

	testParkingACar();
	parkingService.processExitingVehicle(new Date(System.currentTimeMillis() + 60 * 60 * 1000));
	Ticket ticketAfterExitProcess = ticketDAO.getTicket("ABCDEF");

	assertThat(ticketAfterExitProcess).isNotNull();
	assertThat(ticketAfterExitProcess.getPrice()).isNotNull();
	assertThat(ticketAfterExitProcess.getOutTime()).isNotNull();
	assertThat(ticketAfterExitProcess.getInTime()).isBeforeOrEqualTo(ticketAfterExitProcess.getOutTime());
    }
}
