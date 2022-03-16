package Facades;

import dao.GenericDAO;
import entities.AirlineCompanies;
import entities.Countries;
import entities.Flights;
import entities.Users;

import java.util.ArrayList;
import java.util.Date;

public abstract class FacadeBase
{
    public abstract ArrayList<Flights> get_flights_by_parameters(int origin_country_id, int destination_country_id, Date date);
    public abstract ArrayList<AirlineCompanies> get_airline_by_parameters(  );

    public ArrayList<Flights> getAllFlights()
    {
        ArrayList<Flights> flights;
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights", new Flights());
        flights=flightsDAO.getAll();
        return flights;
    }

    public Flights getFlightById(int id)
    {
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights", new Flights());
        return flightsDAO.getById(id);
    }

    public ArrayList<AirlineCompanies> getAllAirlines()
    {
        ArrayList<AirlineCompanies> airlineCompanies;
        GenericDAO<AirlineCompanies> airlineCompaniesDAO = new GenericDAO<>("Airline_Companies", new AirlineCompanies());
        airlineCompanies=airlineCompaniesDAO.getAll();
        return airlineCompanies;
    }

    public AirlineCompanies getAirlineById(int id)
    {
        GenericDAO<AirlineCompanies> airlineCompaniesDAO = new GenericDAO<>("Airline_Companies", new AirlineCompanies());
        return airlineCompaniesDAO.getById(id);
    }

    public ArrayList<Countries> getAllCountries()
    {
        ArrayList<Countries> countries;
        GenericDAO<Countries> countriesDAO = new GenericDAO<>("Countries", new Countries());
        countries=countriesDAO.getAll();
        return countries;
    }
    public Countries getCountry(int id)
    {

        GenericDAO<Countries> countriesDAO = new GenericDAO<>("Countries", new Countries());
        return countriesDAO.getById(id);
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
