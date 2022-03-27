package Facades;

import dao.GenericDAO;
import entities.Customers;
import entities.Flights;
import entities.Tickets;
import logintoken.LoginToken;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CustomerFacadeTest {

    @Test
    @SneakyThrows
    void updateCustomer()
    {
        LoginToken loginToken =new LoginToken();
        Customers customer;
        Customers customerAfter;
        loginToken.setId(1l);
        loginToken.setName("Vasa");
        loginToken.setRole("Customer");
        CustomerFacade customerFacade= new CustomerFacade(loginToken);
        GenericDAO<Customers> customersDAO= new GenericDAO<>("Customers",new Customers());
        customer=customersDAO.getById(1);
        customer.setLastName(customer.getLastName()+1);
        customerFacade.updateCustomer(customer);
        customerAfter=customersDAO.getById(1);
        assertEquals(customer,customerAfter);
    }

    @Test
    @SneakyThrows
    void addTicket()
    {
        LoginToken loginToken =new LoginToken();
        Tickets ticket=new Tickets();
        loginToken.setId(1l);
        loginToken.setName("Vasa");
        loginToken.setRole("Customer");
        CustomerFacade customerFacade= new CustomerFacade(loginToken);
        ticket.setId(5);
        ticket.setFlightId(5);
        ticket.setCostumersId(1);
        customerFacade.addTicket(ticket);
        GenericDAO<Tickets> ticketsDAO = new GenericDAO<>("Tickets",new Tickets());
        assertEquals(ticket, ticketsDAO.getById(5));
    }

    @Test
    @SneakyThrows
    void removeTicket()
    {
        LoginToken loginToken =new LoginToken();
        Tickets ticket=new Tickets();
        loginToken.setId(3l);
        loginToken.setName("createfromnewusercostomerrole");
        loginToken.setRole("Customer");
        CustomerFacade customerFacade= new CustomerFacade(loginToken);
        ticket.setId(3);
        ticket.setFlightId(5);
        ticket.setCostumersId(3);
        customerFacade.removeTicket(ticket);
    }

    @Test
    @SneakyThrows
    void getFlightsByCustomer()
    {
        LoginToken loginToken =new LoginToken();
        Customers customer;
        ArrayList<Flights> flights;
        Flights flightsForTest;
        loginToken.setId(1l);
        loginToken.setName("Vasa");
        loginToken.setRole("Customer");
        CustomerFacade customerFacade= new CustomerFacade(loginToken);
        GenericDAO<Customers> customersDAO= new GenericDAO<>("Customers",new Customers());
        customer=customersDAO.getById(1);
        flights = customerFacade.getFlightsByCustomer(customer);
        GenericDAO<Flights> flightsDAO= new GenericDAO<>("Flights",new Flights());
        flightsForTest=flightsDAO.getById(4);
        assertEquals(flightsForTest,flights.get(0));
    }

    @Test
    @SneakyThrows
    void getMyTickets()
    {
        LoginToken loginToken =new LoginToken();
        loginToken.setId(1l);
        loginToken.setName("Vasa");
        loginToken.setRole("Customer");
        CustomerFacade customerFacade= new CustomerFacade(loginToken);
        System.out.println(customerFacade.getMyTickets());
    }


}