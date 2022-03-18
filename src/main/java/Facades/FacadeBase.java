package Facades;

import dao.GenericDAO;
import entities.AirlineCompanies;
import entities.Countries;
import entities.Flights;
import entities.Users;

import java.util.ArrayList;

public abstract class FacadeBase
{
//    public abstract ArrayList<Flights> get_flights_by_parameters(int origin_country_id, int destination_country_id, Date date);
//    public abstract ArrayList<AirlineCompanies> get_airline_by_parameters(  );

    public ArrayList<Flights> getAllFlights()
    {
        ArrayList<Flights> flights;
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights", new Flights());
        flights=flightsDAO.getAll();
        flightsDAO.closeAllDAOConnections();
        return flights;
    }

    public Flights getFlightById(int id)
    {
        Flights flight;
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights", new Flights());
        flight = flightsDAO.getById(id);
        flightsDAO.closeAllDAOConnections();
        return flight;
    }

    public ArrayList<AirlineCompanies> getAllAirlines()
    {
        ArrayList<AirlineCompanies> airlineCompanies;
        GenericDAO<AirlineCompanies> airlineCompaniesDAO = new GenericDAO<>("Airline_Companies", new AirlineCompanies());
        airlineCompanies=airlineCompaniesDAO.getAll();
        airlineCompaniesDAO.closeAllDAOConnections();
        return airlineCompanies;
    }

    public AirlineCompanies getAirlineById(int id)
    {
        AirlineCompanies airlineCompany;
        GenericDAO<AirlineCompanies> airlineCompaniesDAO = new GenericDAO<>("Airline_Companies", new AirlineCompanies());
        airlineCompany=airlineCompaniesDAO.getById(id);
        airlineCompaniesDAO.closeAllDAOConnections();
        return airlineCompany;
    }

    public ArrayList<Countries> getAllCountries()
    {
        ArrayList<Countries> countries;
        GenericDAO<Countries> countriesDAO = new GenericDAO<>("Countries", new Countries());
        countries=countriesDAO.getAll();
        countriesDAO.closeAllDAOConnections();
        return countries;
    }
    public Countries getCountry(int id)
    {
        Countries country;
        GenericDAO<Countries> countriesDAO = new GenericDAO<>("Countries", new Countries());
        country=countriesDAO.getById(id);
        countriesDAO.closeAllDAOConnections();
        return country;
    }
    public Users createNewUser (String username, String password, String email, int userRole)
    {
        Users user= new Users();
        user.setUserName(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setUserRole(userRole);
        return user;
    }
}
