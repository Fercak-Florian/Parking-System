package com.parkit.parkingsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
	try {
	    lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

	    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	    Ticket ticket = new Ticket();
	    ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
	    ticket.setParkingSpot(parkingSpot);
	    ticket.setVehicleRegNumber("ABCDEF");
	    when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
	    lenient().when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

	    lenient().when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

	    parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new RuntimeException("Failed to set up test mock objects");
	}
    }

    @Test
    public void testOutTimeWhenProcessExitingVehicle() {
	parkingService.processExitingVehicle(new Date());
	Ticket ticketAfterExitingProcess = ticketDAO.getTicket("ABCDEF");

	verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
	assertThat(ticketAfterExitingProcess.getOutTime()).isNotNull();
    }

    @Test
    @DisplayName("Test pour un utilisateur regulier lors de la sortie")
    public void testRecurringUserExiting() {
	when(ticketDAO.getCountForVehicleRegNumber("ABCDEF")).thenReturn(2);

	parkingService.processExitingVehicle(new Date());
	Ticket ticketAfterExitingProcess = ticketDAO.getTicket("ABCDEF");

	assertThat(ticketAfterExitingProcess.getPrice()).isLessThan(1.5);
    }

    @Test
    @DisplayName("Test pour la récuperation d'un ticket null")
    public void testProcessExitingVehicleWithANullTicket() {
	when(ticketDAO.getTicket(anyString())).thenReturn(null);

	parkingService.processExitingVehicle(new Date());
	Ticket ticketAfterIncomingProcess = ticketDAO.getTicket("ABCDEF");

	assertThat(ticketAfterIncomingProcess).isNull();
    }

    @Test
    @DisplayName("Test la mise à jour d'un ticket null")
    public void testProcessExitingVehicleWithUpdatingANullTicket() {
	when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

	parkingService.processExitingVehicle(new Date());
	Ticket ticketAfterExitingProcess = ticketDAO.getTicket("ABCDEF");

	assertThat(ticketAfterExitingProcess.getOutTime()).isNotNull();
    }

    @Test
    @DisplayName("On teste que l'heure d'entrée n'est pas nulle")
    public void testInTimeWhenProcessIncomingVehicle() {
	when(inputReaderUtil.readSelection()).thenReturn(1);
	when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

	parkingService.processIncomingVehicle();
	Ticket ticketAfterIncomingProcess = ticketDAO.getTicket("ABCDEF");

	assertThat(ticketAfterIncomingProcess.getInTime()).isNotNull();
    }

    @Test
    @DisplayName("Test pour un utilisateur regulier lors de l'entrée")
    public void testRecurringUserIncoming() {
	when(inputReaderUtil.readSelection()).thenReturn(1);
	when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
	when(ticketDAO.getCountForVehicleRegNumber("ABCDEF")).thenReturn(1);

	parkingService.processIncomingVehicle();
	Ticket ticketAfterIncomingProcess = ticketDAO.getTicket("ABCDEF");

	assertThat(ticketAfterIncomingProcess.getPrice()).isEqualTo(0);
    }

    @Test
    @DisplayName("on teste avec un parkingSpot id = 0")
    public void testWithAZeroIdParkingSpotInProcessIncomingVehicle() {
	when(inputReaderUtil.readSelection()).thenReturn(1);
	when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);

	parkingService.processIncomingVehicle();
	Ticket ticketAfterIncomingProcess = ticketDAO.getTicket("ABCDEF");

	assertThat(ticketAfterIncomingProcess.getPrice()).isEqualTo(0);
    }
}
