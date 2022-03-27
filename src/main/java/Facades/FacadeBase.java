package Facades;

import dao.GenericDAO;
import entities.*;
import lombok.SneakyThrows;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class FacadeBase
{
    /**Runs a function created in the pg4admin(SQL). Returns: Flights By Parameters: origin country, destination country, date plus time*/
    public  ArrayList<Flights> getFlightsByParameters(int origin_country_id, int destination_country_id, Timestamp date)
    {
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights", new Flights());
        ArrayList<String> parameters =new ArrayList<>();
        parameters.add(""+origin_country_id);
        parameters.add(""+destination_country_id);
        parameters.add("timestamp \'"+date+"\'");
        ArrayList<Flights> listOfFlights=flightsDAO.runSQLFunction("get_flights_by_parameters",parameters,new Flights());
        flightsDAO.closeAllDAOConnections();
        return listOfFlights;
    }
    /**Returns: airline By country. a.k.a getAirlineByCountryId*/
    public  ArrayList<AirlineCompanies> getAirlineByParameters(int countryId)
    {
        GenericDAO<AirlineCompanies> airlineCompaniesDAO = new GenericDAO<>("Airline_Companies", new AirlineCompanies());
        ArrayList<AirlineCompanies> listOfAirlinesByCountry=airlineCompaniesDAO.getByFieldTypeArr(""+countryId,"Country_Id");
        airlineCompaniesDAO.closeAllDAOConnections();
        return listOfAirlinesByCountry;
    }
    /**Returns: all flights*/
    public ArrayList<Flights> getAllFlights()
    {
        ArrayList<Flights> flights;
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights", new Flights());
        flights=flightsDAO.getAll();
        flightsDAO.closeAllDAOConnections();
        return flights;
    }
    /**Returns: flights by id*/
    public Flights getFlightById(int id)
    {
        Flights flight;
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights", new Flights());
        flight = flightsDAO.getById(id);
        flightsDAO.closeAllDAOConnections();
        return flight;
    }
    /**Returns: all airlines*/
    public ArrayList<AirlineCompanies> getAllAirlines()
    {
        ArrayList<AirlineCompanies> airlineCompanies;
        GenericDAO<AirlineCompanies> airlineCompaniesDAO = new GenericDAO<>("Airline_Companies", new AirlineCompanies());
        airlineCompanies=airlineCompaniesDAO.getAll();
        airlineCompaniesDAO.closeAllDAOConnections();
        return airlineCompanies;
    }
    /**Returns: airline by id*/
    public AirlineCompanies getAirlineById(int id)
    {
        AirlineCompanies airlineCompany;
        GenericDAO<AirlineCompanies> airlineCompaniesDAO = new GenericDAO<>("Airline_Companies", new AirlineCompanies());
        airlineCompany=airlineCompaniesDAO.getById(id);
        airlineCompaniesDAO.closeAllDAOConnections();
        return airlineCompany;
    }
    /**Returns: flights by Origin Country*/
    public  ArrayList<Flights>getFlightsByOriginCountryId(int origin_country_id)
    {
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights", new Flights());
        ArrayList<Flights> listOfFlights=flightsDAO.getByFieldTypeArr(""+origin_country_id,"Origin_Country_Id");
        flightsDAO.closeAllDAOConnections();
        return listOfFlights;
    }
    /**Returns: flights by Destination Country*/
    public  ArrayList<Flights>getFlightsByDestinationCountryId(int destination_country_id)
    {
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights", new Flights());
        ArrayList<Flights> listOfFlights=flightsDAO.getByFieldTypeArr(""+destination_country_id,"Destination_Country_Id");
        flightsDAO.closeAllDAOConnections();
        return listOfFlights;
    }
    /**Returns: flights by Departure Date*/
    public  ArrayList<Flights> getFlightsByDepartureDate(Timestamp date)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights", new Flights());
        ArrayList<Flights> listOfFlightsByDepartureDate=flightsDAO.getByFieldTypeArr
                ("\'"+dateFormat.format(date)+"\'","DATE(\"Departure_Time\")");
        flightsDAO.closeAllDAOConnections();
        return listOfFlightsByDepartureDate;
    }
    /**Returns: flights by landing Date*/
    public  ArrayList<Flights> getFlightsByLandingDate(Timestamp date)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights", new Flights());
        ArrayList<Flights> listOfFlightsByDepartureDate=flightsDAO.getByFieldTypeArr
                ("\'"+dateFormat.format(date)+"\'","DATE(\"Landing_Time\")");
        flightsDAO.closeAllDAOConnections();
        return listOfFlightsByDepartureDate;
    }
    /**Returns: all Countries*/
    public ArrayList<Countries> getAllCountries()
    {
        ArrayList<Countries> countries;
        GenericDAO<Countries> countriesDAO = new GenericDAO<>("Countries", new Countries());
        countries=countriesDAO.getAll();
        countriesDAO.closeAllDAOConnections();
        return countries;
    }
    /**Returns: Countries by id*/
    public Countries getCountry(int id)
    {
        Countries country;
        GenericDAO<Countries> countriesDAO = new GenericDAO<>("Countries", new Countries());
        country=countriesDAO.getById(id);
        countriesDAO.closeAllDAOConnections();
        return country;
    }
    /**Creates a new user and its entity for the role,
     * then adds them to the database (Big function. Mainly because of try and catch)*/
    @SneakyThrows
    public <T extends IEntities> void createNewUser (Users user, T entityOfRole)
    {
        GenericDAO<Users> usersDAO =new GenericDAO<>("Users", new Users());
        if(entityOfRole instanceof Customers)
        {
            String x = getUserRoleName(user);
            System.out.println(x);
            if(!getUserRoleName(user).equals("\'Customer\'"))
            {
                System.out.println("The user that was provided has a role different from the entityOfRole.");
                return;
            }
            try {
                usersDAO.add(user);
            }catch (Exception e)
            {
                throw new Exception("User Exists");
            }
            GenericDAO<Customers> customerDAO =new GenericDAO<>("Customers", new Customers());
            Users userIdAfterSet = usersDAO.getByFieldType(user.getUserName(),"Username");
            ((Customers) entityOfRole).setUserId(userIdAfterSet.getId());
            try {
                customerDAO.add((Customers) entityOfRole);
            }catch (Exception e)
            {
                usersDAO.remove(user);
                throw new Exception("Customer Exists");
            }
            customerDAO.closeAllDAOConnections();
            usersDAO.closeAllDAOConnections();
        }
        else if (entityOfRole instanceof Administrators)
        {
            String x = getUserRoleName(user);
            System.out.println(x);
            System.out.println(getUserRoleName(user).equals("\'Administrator\'"));
            if(!getUserRoleName(user).equals("\'Administrator\'"))
            {
                System.out.println("The user that was provided has a role different from the entityOfRole.");
                return;
            }
            try {
                usersDAO.add(user);
            }catch (Exception e)
            {
                throw new Exception("User Exists");
            }
            GenericDAO<Administrators> administratorDAO =new GenericDAO<>("Administrators", new Administrators());
            Users userIdAfterSet = usersDAO.getByFieldType(user.getUserName(),"Username");
            ((Administrators) entityOfRole).setUserId(userIdAfterSet.getId());
            try {
                administratorDAO.add((Administrators) entityOfRole);
            }catch (Exception e)
            {
                usersDAO.remove(user);
                throw new Exception("Administrator Exists");
            }
            administratorDAO.closeAllDAOConnections();
            usersDAO.closeAllDAOConnections();
        }
        else if (entityOfRole instanceof AirlineCompanies)
        {
            String x = getUserRoleName(user);
            System.out.println(x);
            if(!getUserRoleName(user).equals("\'Airline_Company\'"))
            {
                System.out.println("The user that was provided has a role different from the entityOfRole.");
                return;
            }
            try {
                usersDAO.add(user);
            }catch (Exception e)
            {
                throw new Exception("User Exists");
            }
            GenericDAO<AirlineCompanies> airlineCompaniesDAO =new GenericDAO<>("Airline_Companies", new AirlineCompanies());
            Users userIdAfterSet = usersDAO.getByFieldType(user.getUserName(),"Username");
            ((AirlineCompanies) entityOfRole).setUserId(userIdAfterSet.getId());
            try {
                airlineCompaniesDAO.add((AirlineCompanies) entityOfRole);
            }catch (Exception e)
            {
                usersDAO.remove(user);
                throw new Exception("Airline company Exists");
            }
            airlineCompaniesDAO.add((AirlineCompanies) entityOfRole);
            airlineCompaniesDAO.closeAllDAOConnections();
            usersDAO.closeAllDAOConnections();
        }
        else
        {
            usersDAO.closeAllDAOConnections();
            System.out.println("entityOfRole is not one of the available roles. Nothing added to the database");
        }
    }
    @SneakyThrows
    /**Get user role name*/
    private String getUserRoleName(Users user)
    {
        GenericDAO<UserRoles> userRolesDAO =new GenericDAO<>("User_Roles", new UserRoles());
        if (user.getUserRole()==null)
        {
            System.out.println("The user provided has no role id. Can not perform the necessary checks.");
        }
        UserRoles u= userRolesDAO.getById(user.getUserRole());
        return u.getRoleName();
    }
    /**Get tickets by customer(Used in removes mainly in administrator facade)*/
    public ArrayList<Tickets> getTicketsByCustomer(Customers customer) throws Exception
    {
        ArrayList<Tickets> tickets;
        GenericDAO<Tickets> ticketsDAO = new GenericDAO<>("Tickets",new Tickets());
        if (customer.getId()==null)
        {
            ticketsDAO.closeAllDAOConnections();
            throw new Exception("Id must be provided inside the customer. Can not get tickets from DataBase");
        }
        else
        {
            tickets=ticketsDAO.getAllWithWhereClause("WHERE \"Customer_Id\"="+customer.getId());
            ticketsDAO.closeAllDAOConnections();
            return tickets;
        }
    }
    /**Get tickets by flight(Used in removes mainly in administrator facade)*/
    public ArrayList<Tickets> getTicketsByFlight(Flights flight) throws Exception
    {
        ArrayList<Tickets> tickets;
        GenericDAO<Tickets> ticketsDAO = new GenericDAO<>("Tickets",new Tickets());
        if (flight.getId()==null)
        {
            ticketsDAO.closeAllDAOConnections();
            throw new Exception("Id must be provided inside the flight. Can not get tickets from DataBase");
        }
        else
        {
            tickets=ticketsDAO.getAllWithWhereClause("WHERE \"Flight_Id\"="+flight.getId());
            ticketsDAO.closeAllDAOConnections();
            return tickets;
        }
    }
    /**Get Flights by AirlineCompany (Used in removes mainly in administrator facade)*/
    public ArrayList<Flights> getFlightsByAirlineCompany(AirlineCompanies airline) throws Exception
    {
        ArrayList<Flights> flights;
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights",new Flights());
        if (airline.getId()==null)
        {
            flightsDAO.closeAllDAOConnections();
            throw new Exception("Id must be provided inside the airline. Can not get flights from DataBase");
        }
        else
        {
            flights=flightsDAO.getAllWithWhereClause("WHERE \"Airline_Company_Id\"="+airline.getId());
            flightsDAO.closeAllDAOConnections();
            return flights;
        }
    }
}
