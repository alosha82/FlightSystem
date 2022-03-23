package Facades;

import dao.GenericDAO;
import entities.Customers;
import entities.Flights;
import entities.Tickets;
import logintoken.LoginToken;
import lombok.Getter;
import org.apache.commons.math3.util.Pair;

import java.util.*;

@Getter
public class CustomerFacade extends AnonymousFacade
{

    LoginToken token;
    public CustomerFacade(LoginToken loginToken)
    {
        this.token = loginToken;
    }

    public void updateCustomer (Customers customer)throws Exception
    {
        if (this.token.getId()!=customer.getId())
            throw new Exception("You can not update other customers");
        GenericDAO<Customers> customersDAO = new GenericDAO<>("Customers",new Customers());
        if (customer.getId()==null)
            System.out.println("Id must be provided inside the customer. No update was made to the DataBase");
        else
            customersDAO.update(customer,customer.getId());
        customersDAO.closeAllDAOConnections();
    }

    public void addTicket(Tickets ticket) throws Exception
    {
        if (token.getId()!=ticket.getCostumersId())
            throw new Exception("You can not add tickets of another customer");
        ArrayList<Tickets> tickets;
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights",new Flights());
        GenericDAO<Tickets> ticketsDAO = new GenericDAO<>("Tickets",new Tickets());
        tickets=ticketsDAO.getAll();
        for (int i = 0; i < tickets.size(); i++)
        {
            if ((tickets.get(i).getCostumersId()==ticket.getCostumersId())&&(tickets.get(i).getFlightId()==ticket.getFlightId()))
                throw new Exception("That ticket is already in the DataBase");
        }
        ticketsDAO.add(ticket);
        Flights flightOfTheTicket = flightsDAO.getByFieldType(""+ticket.getFlightId(),"Id");
        flightOfTheTicket.setRemainingTickets(flightOfTheTicket.getRemainingTickets()-1);
        flightsDAO.update(flightOfTheTicket,flightOfTheTicket.getId());
        flightsDAO.closeAllDAOConnections();
        ticketsDAO.closeAllDAOConnections();
    }

    public void removeTicket(Tickets ticket) throws Exception
    {
        if (token.getId()!=ticket.getCostumersId())
            throw new Exception("You can not remove tickets of another customer");
        GenericDAO<Tickets> ticketsDAO = new GenericDAO<>("Tickets",new Tickets());
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights",new Flights());
        if (ticket.getId()==null)
            System.out.println("Id must be provided inside the ticket. No removal was made in the DataBase");
        else
        {
            ticketsDAO.remove(ticket.getId());
            Flights flightOfTheTicket = flightsDAO.getByFieldType(""+ticket.getFlightId(),"Id");
            flightOfTheTicket.setRemainingTickets(flightOfTheTicket.getRemainingTickets()+1);
            flightsDAO.update(flightOfTheTicket,flightOfTheTicket.getId());
        }
        flightsDAO.closeAllDAOConnections();
        ticketsDAO.closeAllDAOConnections();
    }

    /**Implementation of this method uses joinMultipleByWithWhereClause.*/
    public ArrayList<Flights>getFlightsByCustomer(Customers customer)
    {
        ArrayList<Flights> flights;
        Map<String,Collection<String>> tablesToColumnsMap=new HashMap<>();
        GenericDAO<Flights> flightDAO = new GenericDAO<>("Flights",new Flights());
        tablesToColumnsMap.put("Flights", List.of("Id", "Airline_Company_Id","Origin_Country_Id","Destination_Country_Id"
                ,"Departure_time","Landing_time","Remaining_Tickets"));
        LinkedHashMap<Pair<String,String>,Pair<String,String>> foreignsToOrigins=new LinkedHashMap<>();
        foreignsToOrigins.put(new Pair<>("Tickets", "Flight_Id"), new Pair<>("Flights", "Id"));
        foreignsToOrigins.put(new Pair<>("Customers", "Id"),new Pair<>("Tickets", "Customer_Id"));
        String whereClose = "WHERE \"Customers\".\"Id\" = "+ customer.getId();
        flights =flightDAO.joinMultipleByWithWhereClause(tablesToColumnsMap,"Tickets",foreignsToOrigins,new Flights(),whereClose);
        flightDAO.closeAllDAOConnections();
        return flights;
    }

    /**Joins Tickets with Customers and filters the joined entity by Customers.Id*/
    public ArrayList<Tickets> getMyTickets() throws Exception
    {
        ArrayList<Tickets> tickets;
        GenericDAO<Tickets> ticketsDAO = new GenericDAO<>("Tickets",new Tickets());
        ArrayList<ArrayList<String>> columns = new ArrayList<>();
        columns.add(new ArrayList<>());
        columns.get(0).add("Id");
        columns.get(0).add("Flight_Id");
        columns.get(0).add("Customer_Id");
        Map<String, Collection<String>> tablesToColumnsMap=new HashMap<>();
        tablesToColumnsMap.put("Tickets", List.of("Id", "Flight_Id","Customer_Id"));
        if (token.getId()==null)
        {
            ticketsDAO.closeAllDAOConnections();
            throw new Exception("Id must be provided inside the customer. Can not get tickets from DataBase");
        }
        else
        {
            tickets = ticketsDAO.joinTwoByWithWhereClause(tablesToColumnsMap, "Customer_Id", "Customers", "Id"
                    , new Tickets(), "WHERE \"Customers\".\"Id\"=" + token.getId());
            ticketsDAO.closeAllDAOConnections();
            return tickets;
        }
    }
}
