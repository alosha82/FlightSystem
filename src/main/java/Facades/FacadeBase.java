package Facades;

import dao.GenericDAO;
import entities.*;

import java.sql.Timestamp;
import java.util.ArrayList;

public abstract class FacadeBase
{
    public  ArrayList<Flights> get_flights_by_parameters(int origin_country_id, int destination_country_id, Timestamp date)
    {
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights", new Flights());
        ArrayList<String> parameters =new ArrayList<>();
        parameters.add(""+origin_country_id);
        parameters.add(""+destination_country_id);
        parameters.add("timestamp \'"+date+"\'");
        return flightsDAO.runSQLFunction("get_flights_by_parameters",parameters,new Flights());
    }
    public  ArrayList<AirlineCompanies> get_airline_by_parameters(int countryId)
    {
        GenericDAO<AirlineCompanies> airlineCompaniesDAO = new GenericDAO<>("AirlineCompanies", new AirlineCompanies());
        return airlineCompaniesDAO.getByFieldTypeArr(""+countryId,"Country_Id");
    }

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
    public <T extends IEntities> void createNewUser (Users user, T entityOfRole)//(String username, String password, String email, int userRole)
    {
        GenericDAO<Users> usersDAO =new GenericDAO<>("Users", new Users());
        if(entityOfRole instanceof Customers)
        {
            usersDAO.add(user);
            GenericDAO<Customers> customerDAO =new GenericDAO<>("Customers", new Customers());
            customerDAO.add((Customers) entityOfRole);
        }
        else if (entityOfRole instanceof Administrators)
        {
            usersDAO.add(user);
            GenericDAO<Administrators> administratorDAO =new GenericDAO<>("Administrators", new Administrators());
            administratorDAO.add((Administrators) entityOfRole);
        }
        else if (entityOfRole instanceof AirlineCompanies)
        {
            usersDAO.add(user);
            GenericDAO<AirlineCompanies> airlineCompaniesDAO =new GenericDAO<>("AirlineCompanies", new AirlineCompanies());
            airlineCompaniesDAO.add((AirlineCompanies) entityOfRole);
        }
        else
        {
            System.out.println(" entityOfRole is not one of the available roles. Nothing added to the database");
        }
    }
}
