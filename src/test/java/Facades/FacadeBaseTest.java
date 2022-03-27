package Facades;

import dao.GenericDAO;
import entities.*;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FacadeBaseTest {

    @Test
    void get_flights_by_parameters()
    {
        FacadeBase x=new AnonymousFacade();
        ArrayList<Flights> flights = x.getFlightsByParameters(1,2,Timestamp.valueOf("2016-06-21 19:10:25"));
        val flightsForTest = new Flights();
        flightsForTest.setId(4l);
        flightsForTest.setOriginCountryId(1);
        flightsForTest.setDestinationCountryId(2);
        flightsForTest.setAirlineCompanyId(1l);
        flightsForTest.setDepartureTime(Timestamp.valueOf("2016-06-21 19:10:25"));
        flightsForTest.setLandingTime(Timestamp.valueOf("2016-06-22 19:10:25"));
        flightsForTest.setRemainingTickets(100);
        assertEquals(flights.get(0),flightsForTest);
    }

    @Test
    void get_airline_by_parameters()
    {
        FacadeBase x=new AnonymousFacade();
        ArrayList<AirlineCompanies> airlines = x.getAirlineByParameters(1);
        val AirlineForTest = new AirlineCompanies();
        AirlineForTest.setId(1);
        AirlineForTest.setName("someairline");
        AirlineForTest.setCountryId(1);
        AirlineForTest.setUserId(2);
        assertEquals(airlines.get(0),AirlineForTest);
    }

    @Test
    void getAllFlights()
    {
        FacadeBase x=new AnonymousFacade();
        ArrayList<Flights> flights = x.getAllFlights();
        val flightsForTest = new Flights();
        flightsForTest.setId(4l);
        flightsForTest.setOriginCountryId(1);
        flightsForTest.setDestinationCountryId(2);
        flightsForTest.setAirlineCompanyId(1l);
        flightsForTest.setDepartureTime(Timestamp.valueOf("2016-06-21 19:10:25"));
        flightsForTest.setLandingTime(Timestamp.valueOf("2016-06-22 19:10:25"));
        flightsForTest.setRemainingTickets(100);
        assertEquals(flights.get(0),flightsForTest);
    }

    @Test
    void getFlightById()
    {
        FacadeBase x=new AnonymousFacade();
        Flights flight = x.getFlightById(4);
        val flightsForTest = new Flights();
        flightsForTest.setId(4l);
        flightsForTest.setOriginCountryId(1);
        flightsForTest.setDestinationCountryId(2);
        flightsForTest.setAirlineCompanyId(1l);
        flightsForTest.setDepartureTime(Timestamp.valueOf("2016-06-21 19:10:25"));
        flightsForTest.setLandingTime(Timestamp.valueOf("2016-06-22 19:10:25"));
        flightsForTest.setRemainingTickets(100);
        System.out.println(flight);
        System.out.println(flightsForTest);
        assertEquals(flight,flightsForTest);
    }

    @Test
    void getAllAirlines()
    {
        FacadeBase x=new AnonymousFacade();
        ArrayList<AirlineCompanies> airlines = x.getAllAirlines();
        val AirlineForTest = new AirlineCompanies();
        AirlineForTest.setId(1);
        AirlineForTest.setName("someairline");
        AirlineForTest.setCountryId(1);
        AirlineForTest.setUserId(2);
        assertEquals(airlines.get(0),AirlineForTest);
    }

    @Test
    void getAirlineById()
    {
        FacadeBase x=new AnonymousFacade();
        AirlineCompanies airlines = x.getAirlineById(1);
        val AirlineForTest = new AirlineCompanies();
        AirlineForTest.setId(1);
        AirlineForTest.setName("someairline");
        AirlineForTest.setCountryId(1);
        AirlineForTest.setUserId(2);
        assertEquals(airlines,AirlineForTest);
    }

    @Test
    void getFlightsByOriginCountryId()
    {
        FacadeBase x=new AnonymousFacade();
        ArrayList<Flights> flights = x.getFlightsByOriginCountryId(1);
        val flightsForTest = new Flights();
        flightsForTest.setId(4l);
        flightsForTest.setOriginCountryId(1);
        flightsForTest.setDestinationCountryId(2);
        flightsForTest.setAirlineCompanyId(1l);
        flightsForTest.setDepartureTime(Timestamp.valueOf("2016-06-21 19:10:25"));
        flightsForTest.setLandingTime(Timestamp.valueOf("2016-06-22 19:10:25"));
        flightsForTest.setRemainingTickets(100);
        assertEquals(flights.get(0),flightsForTest);
    }

    @Test
    void getFlightsByDestinationCountryId()
    {
        FacadeBase x=new AnonymousFacade();
        ArrayList<Flights> flights = x.getFlightsByDestinationCountryId(2);
        val flightsForTest = new Flights();
        flightsForTest.setId(4l);
        flightsForTest.setOriginCountryId(1);
        flightsForTest.setDestinationCountryId(2);
        flightsForTest.setAirlineCompanyId(1l);
        flightsForTest.setDepartureTime(Timestamp.valueOf("2016-06-21 19:10:25"));
        flightsForTest.setLandingTime(Timestamp.valueOf("2016-06-22 19:10:25"));
        flightsForTest.setRemainingTickets(100);
        assertEquals(flights.get(0),flightsForTest);
    }

    @Test
    void getFlightsByDepartureDate()
    {
        FacadeBase x=new AnonymousFacade();
        ArrayList<Flights> flights = x.getFlightsByDepartureDate(Timestamp.valueOf("2016-06-21 19:10:25"));
        val flightsForTest = new Flights();
        flightsForTest.setId(4l);
        flightsForTest.setOriginCountryId(1);
        flightsForTest.setDestinationCountryId(2);
        flightsForTest.setAirlineCompanyId(1l);
        flightsForTest.setDepartureTime(Timestamp.valueOf("2016-06-21 19:10:25"));
        flightsForTest.setLandingTime(Timestamp.valueOf("2016-06-22 19:10:25"));
        flightsForTest.setRemainingTickets(100);
        assertEquals(flights.get(0),flightsForTest);
    }

    @Test
    void getFlightsByLandingDate()
    {
        FacadeBase x=new AnonymousFacade();
        ArrayList<Flights> flights = x.getFlightsByLandingDate(Timestamp.valueOf("2016-06-22 19:10:25"));
        val flightsForTest = new Flights();
        flightsForTest.setId(4l);
        flightsForTest.setOriginCountryId(1);
        flightsForTest.setDestinationCountryId(2);
        flightsForTest.setAirlineCompanyId(1l);
        flightsForTest.setDepartureTime(Timestamp.valueOf("2016-06-21 19:10:25"));
        flightsForTest.setLandingTime(Timestamp.valueOf("2016-06-22 19:10:25"));
        flightsForTest.setRemainingTickets(100);
        assertEquals(flights.get(0),flightsForTest);
    }

    @Test
    void getAllCountries()
    {
        FacadeBase x=new AnonymousFacade();
        ArrayList<Countries> airlines = x.getAllCountries();
        val countryForTest = new Countries();
        countryForTest.setId(1);
        countryForTest.setName("Russia");
        assertEquals(airlines.get(0),countryForTest);
    }

    @Test
    void getCountry()
    {
        FacadeBase x=new AnonymousFacade();
        Countries countries = x.getCountry(1);
        val countryForTest = new Countries();
        countryForTest.setId(1);
        countryForTest.setName("Russia");
        assertEquals(countries,countryForTest);
    }

    @Test
    void createNewUser()
    {
        FacadeBase x=new AnonymousFacade();
        Users getUser1;
        Users getUser2;
        Users getUser3;
        Administrators getAdministrator;
        AirlineCompanies getAirlineCompany;
        Customers getCustomer;
        Users newUserToAdd1 = new Users();
        newUserToAdd1.setId(10);
        newUserToAdd1.setUserName("createfromnewuser1");
        newUserToAdd1.setPassword("11111111");
        newUserToAdd1.setEmail("u@t1");
        newUserToAdd1.setUserRole(1);
        Users newUserToAdd2 = new Users();
        newUserToAdd2.setId(11);
        newUserToAdd2.setUserName("createfromnewuser2");
        newUserToAdd2.setPassword("11111112");
        newUserToAdd2.setEmail("u@t2");
        newUserToAdd2.setUserRole(2);
        Users newUserToAdd3 = new Users();
        newUserToAdd3.setId(12);
        newUserToAdd3.setUserName("createfromnewuser3");
        newUserToAdd3.setPassword("11111113");
        newUserToAdd3.setEmail("u@t3");
        newUserToAdd3.setUserRole(3);
        Administrators newAdministratorToAdd = new Administrators();
        newAdministratorToAdd.setId(3);
        newAdministratorToAdd.setFirstName("createfromnewuseradministratorrole");
        newAdministratorToAdd.setLastName("createfromnewuseradministratorrolefortest");
        newAdministratorToAdd.setUserId(4);
        AirlineCompanies newAirlineToAdd = new AirlineCompanies();
        newAirlineToAdd.setId(3);
        newAirlineToAdd.setName("createfromnewuserairlinerole");
        newAirlineToAdd.setCountryId(2);
        newAirlineToAdd.setUserId(5);
        Customers newCustomerToAdd = new Customers();
        newCustomerToAdd.setId(3);
        newCustomerToAdd.setFirstName("createfromnewusercostomerrole");
        newCustomerToAdd.setLastName("createfromnewusercostomerrolefortest");
        newCustomerToAdd.setAddress("createfromnewusercostomerroleaddressfortest");
        newCustomerToAdd.setPhoneNumber("createfromnewusercostomerrolephonefortest");
        newCustomerToAdd.setCreditCardNumber("createfromnewusercostomerrolecreditfortest");
        newCustomerToAdd.setUserId(6);

//        x.createNewUser(newUserToAdd1,newAdministratorToAdd);
//        x.createNewUser(newUserToAdd2,newAirlineToAdd);
//        x.createNewUser(newUserToAdd3,newCustomerToAdd);
        GenericDAO<Users> usersDAO = new GenericDAO<>("Users",new Users());
        getUser1=usersDAO.getById(10);
        getUser2=usersDAO.getById(11);
        getUser3=usersDAO.getById(12);
        GenericDAO<Administrators> administratorsDAO = new GenericDAO<>("Administrators",new Administrators());
        getAdministrator=administratorsDAO.getById(3);
        GenericDAO<AirlineCompanies> airlineCompaniesDAO = new GenericDAO<>("Airline_Companies",new AirlineCompanies());
        getAirlineCompany=airlineCompaniesDAO.getById(3);
        GenericDAO<Customers> customersDAO = new GenericDAO<>("Customers",new Customers());
        getCustomer=customersDAO.getById(3);

        assertEquals(newUserToAdd1,getUser1);
        assertEquals(newUserToAdd2,getUser2);
        assertEquals(newUserToAdd3,getUser3);
        assertEquals(newAdministratorToAdd,getAdministrator);
        assertEquals(newAirlineToAdd,getAirlineCompany);
        assertEquals(newCustomerToAdd,getCustomer);
    }
}