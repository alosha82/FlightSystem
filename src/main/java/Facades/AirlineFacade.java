package Facades;

import dao.GenericDAO;
import entities.AirlineCompanies;
import entities.Customers;
import entities.Flights;
import entities.Tickets;

import java.util.ArrayList;

public class AirlineFacade
{
    AirlineCompanies airlineCompany;

    public AirlineFacade(AirlineCompanies airlineCompany)
    {
        this.airlineCompany = airlineCompany;
    }

    public void updateAirline (AirlineCompanies airlineCompany)
    {
        GenericDAO<AirlineCompanies> airlineCompaniesDAO = new GenericDAO<>("AirlineCompanies",new AirlineCompanies());
        if (airlineCompany.getId()==null)
            System.out.println("Id must be provided inside the flight. No update was made to the DataBase");
        else
            airlineCompaniesDAO.update(airlineCompany,airlineCompany.getId());
    }
    public void updateFlight (Flights flight)
    {
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights",new Flights());
        if (flight.getId()==null)
            System.out.println("Id must be provided inside the flight. No update was made to the DataBase");
        else
            flightsDAO.update(flight,flight.getId());
    }

    public void addFlight(Flights flight) throws Exception
    {
        ArrayList<Flights> flights;
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights",new Flights());
        flights=flightsDAO.getAll();
        for (int i = 0; i < flights.size(); i++)
        {
            if ((flights.get(i).getAirlineCompanyId()==flight.getAirlineCompanyId())
                    &&(flights.get(i).getOriginCountryId()==flight.getOriginCountryId())
                    &&(flights.get(i).getDestinationCountryId()==flight.getDestinationCountryId())
                    &&(flights.get(i).getDepartureTime()==flight.getDepartureTime())
                    &&(flights.get(i).getLandingTime()==flight.getLandingTime())
                    &&(flights.get(i).getRemainingTickets()==flight.getRemainingTickets()))
                throw new Exception("That flight is already in the DataBase");
        }
        flightsDAO.add(flight);
    }

    public void removeFlight(Flights flight)
    {
        GenericDAO<Flights> ticketsDAO = new GenericDAO<>("Flights",new Flights());
        if (flight.getId()==null)
            System.out.println("Id must be provided inside the flight. No removal was made in the DataBase");
        else
            ticketsDAO.remove(flight.getId());
    }

    public ArrayList<Flights> getMyFlights() throws Exception
    {
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights",new Flights());
        ArrayList<ArrayList<String>> columns = new ArrayList<>();
        columns.get(0).add("Id");
        columns.get(0).add("Airline_Company_Id");
        columns.get(0).add("Origin_Country_Id");
        columns.get(0).add("Destination_Country_Id");
        columns.get(0).add("Departure_time");
        columns.get(0).add("Landing_time");
        columns.get(0).add("Remaining_Tickets");
        if (airlineCompany.getId()==null)
            throw new Exception("Id must be provided inside the airline company. Can not get flights from DataBase");
        else
            return flightsDAO.joinTwoByWithWhereClause(columns,"Airline_Company_Id","AirlineCompanies","Id"
                    ,new Flights(),"WHERE \"Customers\".\"Id\"="+airlineCompany.getId());
    }
}
