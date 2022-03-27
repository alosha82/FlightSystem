package Facades;

import dao.GenericDAO;
import entities.AirlineCompanies;
import entities.Customers;
import entities.Flights;
import entities.Tickets;
import logintoken.LoginToken;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class AirlineFacadeTest {

    @Test
    @SneakyThrows
    void updateAirline()
    {
        LoginToken loginToken =new LoginToken();
        AirlineCompanies airline;
        AirlineCompanies airlineAfter;
        loginToken.setId(1l);
        loginToken.setName("someairline");
        loginToken.setRole("Airline_Company");
        AirlineFacade airlineFacade= new AirlineFacade(loginToken);
        GenericDAO<AirlineCompanies> airlineCompaniesDAO= new GenericDAO<>("Airline_Companies",new AirlineCompanies());
        airline=airlineCompaniesDAO.getById(1);
        airline.setName(airline.getName()+1);
        airlineFacade.updateAirline(airline);
        airlineAfter=airlineCompaniesDAO.getById(1);
        assertEquals(airline,airlineAfter);
    }

    @Test
    @SneakyThrows
    void updateFlight()
    {
        LoginToken loginToken =new LoginToken();
        Flights flight;
        Flights flightAfter;
        loginToken.setId(1l);
        loginToken.setName("someairline");
        loginToken.setRole("Airline_Company");
        AirlineFacade airlineFacade= new AirlineFacade(loginToken);
        GenericDAO<Flights> flightsDAO= new GenericDAO<>("Flights",new Flights());
        flight=flightsDAO.getById(4);
        flight.setRemainingTickets(flight.getRemainingTickets()+1);
        airlineFacade.updateFlight(flight);
        flightAfter=flightsDAO.getById(4);
        assertEquals(flight,flightAfter);
    }

    @Test
    @SneakyThrows
    void addFlight()
    {
        LoginToken loginToken =new LoginToken();
        loginToken.setId(1l);
        loginToken.setName("someairline");
        loginToken.setRole("Airline_Company");
        AirlineFacade airlineFacade= new AirlineFacade(loginToken);
        GenericDAO<Flights> flightsDAO= new GenericDAO<>("Flights",new Flights());
        val flightsForTest = new Flights();
        flightsForTest.setId(6l);
        flightsForTest.setOriginCountryId(1);
        flightsForTest.setDestinationCountryId(2);
        flightsForTest.setAirlineCompanyId(1l);
        flightsForTest.setDepartureTime(Timestamp.valueOf("2022-03-26 23:10:25"));
        flightsForTest.setLandingTime(Timestamp.valueOf("2022-03-27 03:10:25"));
        flightsForTest.setRemainingTickets(100);
        airlineFacade.addFlight(flightsForTest);

        assertEquals(flightsForTest, flightsDAO.getById(6));
    }

    @Test
    @SneakyThrows
    void removeFlight()
    {
        LoginToken loginToken =new LoginToken();
        loginToken.setId(1l);
        loginToken.setName("someairline");
        loginToken.setRole("Airline_Company");
        AirlineFacade airlineFacade= new AirlineFacade(loginToken);
        val flightsForTest = new Flights();
        flightsForTest.setId(6l);
        flightsForTest.setOriginCountryId(1);
        flightsForTest.setDestinationCountryId(2);
        flightsForTest.setAirlineCompanyId(1l);
        flightsForTest.setDepartureTime(Timestamp.valueOf("2022-03-26 23:10:25"));
        flightsForTest.setLandingTime(Timestamp.valueOf("2022-03-27 03:10:25"));
        flightsForTest.setRemainingTickets(100);
        airlineFacade.removeFlight(flightsForTest);
    }

    @Test
    @SneakyThrows
    void getMyFlights()
    {
        LoginToken loginToken =new LoginToken();
        loginToken.setId(1l);
        loginToken.setName("someairline");
        loginToken.setRole("Airline_Company");
        AirlineFacade airlineFacade= new AirlineFacade(loginToken);
        System.out.println(airlineFacade.getMyFlights());
    }
}