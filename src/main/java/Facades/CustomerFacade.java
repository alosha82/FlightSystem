package Facades;

import dao.GenericDAO;
import entities.Customers;
import entities.Tickets;

import java.util.ArrayList;

public class CustomerFacade
{
    Customers customer;

    public CustomerFacade(Customers customer)
    {
        this.customer = customer;
    }

    public void updateCustomer (Customers customer)
    {
        GenericDAO<Customers> customersDAO = new GenericDAO<>("Customers",new Customers());
        if (customer.getId()==null)
            System.out.println("Id must be provided inside the customer. No update was made to the DataBase");
        else
            customersDAO.update(customer,customer.getId());
    }

    public void addTicket(Tickets ticket) throws Exception
    {
        ArrayList<Tickets> tickets;
        GenericDAO<Tickets> ticketsDAO = new GenericDAO<>("Tickets",new Tickets());
        tickets=ticketsDAO.getAll();
        for (int i = 0; i < tickets.size(); i++)
        {
            if ((tickets.get(i).getCostumersId()==ticket.getCostumersId())&&(tickets.get(i).getFlightId()==ticket.getFlightId()))
                throw new Exception("That ticket is already in the DataBase");
        }
        ticketsDAO.add(ticket);
    }

    public void removeTicket(Tickets ticket)
    {
        GenericDAO<Tickets> ticketsDAO = new GenericDAO<>("Tickets",new Tickets());
        if (ticket.getId()==null)
            System.out.println("Id must be provided inside the ticket. No removal was made in the DataBase");
        else
            ticketsDAO.remove(ticket.getId());
    }

    public ArrayList<Tickets> getMyTickets() throws Exception
    {
        GenericDAO<Tickets> ticketsDAO = new GenericDAO<>("Tickets",new Tickets());
        ArrayList<ArrayList<String>> columns = new ArrayList<>();
        columns.get(0).add("Id");
        columns.get(0).add("Flight_Id");
        columns.get(0).add("Customer_Id");
        if (customer.getId()==null)
            throw new Exception("Id must be provided inside the customer. Can not get tickets from DataBase");
        else
            return ticketsDAO.joinTwoByWithWhereClause(columns,"Customer_Id","Customers","Id"
                ,new Tickets(),"WHERE \"Customers\".\"Id\"="+customer.getId());
    }
}