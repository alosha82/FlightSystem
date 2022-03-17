package Facades;

import dao.GenericDAO;
import entities.AirlineCompanies;
import entities.Flights;
import logintoken.LoginToken;
import lombok.Getter;


import java.util.ArrayList;

@Getter
public class AirlineFacade extends FacadeBase
{
    LoginToken token;

    public AirlineFacade(LoginToken loginToken)
    {
        this.token = loginToken;
    }

    public void updateAirline (AirlineCompanies airlineCompany) throws Exception
    {
        if (this.token.getId()!=airlineCompany.getId())
            throw new Exception("You can not update other airline companies");
        GenericDAO<AirlineCompanies> airlineCompaniesDAO = new GenericDAO<>("AirlineCompanies",new AirlineCompanies());
        if (airlineCompany.getId()==null)
            System.out.println("Id must be provided inside the airlineCompany. No update was made to the DataBase");
        else
            airlineCompaniesDAO.update(airlineCompany,airlineCompany.getId());
    }
    public void updateFlight (Flights flight) throws Exception
    {
        if (token.getId()!=flight.getAirlineCompanyId())
            throw new Exception("You can not update flights of another airline company");
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights",new Flights());
        if (flight.getId()==null)
            System.out.println("Id must be provided inside the flight. No update was made to the DataBase");
        else
            flightsDAO.update(flight,flight.getId());
    }

    public void addFlight(Flights flight) throws Exception
    {
        ArrayList<Flights> flights;
        Flights x = new Flights();
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights",new Flights());
        if (token.getId()!=flight.getAirlineCompanyId())
            throw new Exception("You can not add flights of another airline company");
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
        if (flight.getRemainingTickets()<0)
            throw new Exception("Remaining tickets is negative. Can not add that flight to the DataBase");
        if (flight.getOriginCountryId()==flight.getDestinationCountryId())
            throw new Exception("We do net provide flights within the country. Can not add that flight to the DataBase");
        if (flight.getLandingTime().before(flight.getDepartureTime()))
            throw new Exception("There is a mix up in your flight times. it is impossible to land before you take flight. Can not add that flight to the DataBase");
        flightsDAO.add(flight);
    }

    public void removeFlight(Flights flight) throws Exception
    {
        if (token.getId()!=flight.getAirlineCompanyId())
            throw new Exception("You can not remove flights of another airline company");
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
        columns.add(new ArrayList<>());
        columns.get(0).add("Id");
        columns.get(0).add("Airline_Company_Id");
        columns.get(0).add("Origin_Country_Id");
        columns.get(0).add("Destination_Country_Id");
        columns.get(0).add("Departure_time");
        columns.get(0).add("Landing_time");
        columns.get(0).add("Remaining_Tickets");
        if (token.getId()==null)
            throw new Exception("Id must be provided inside the airline company. Can not get flights from DataBase");
        else
            return flightsDAO.joinTwoByWithWhereClause(columns,"Airline_Company_Id","AirlineCompanies","Id"
                    ,new Flights(),"WHERE \"Customers\".\"Id\"="+token.getId());
    }
}
