package Facades;

import dao.GenericDAO;
import entities.*;
import logintoken.LoginToken;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.*;

@Getter
@Setter
public class AdministratorFacade extends AnonymousFacade
{
    LoginToken token;
    GenericDAO<Administrators> administratorsDAO = new GenericDAO<>("Administrators",new Administrators());
    Administrators administrators;
    public AdministratorFacade(LoginToken token) throws Exception{
        this.token = token;
        try
        {
            administrators = administratorsDAO.getById(token.getId());
        }
        catch (Exception e)
        {
            throw new Exception("No administrators with your id");
        }


    }
    /**Gets all customers*/
    public ArrayList<Customers> getAllCustomers() throws Exception
    {
        if(administrators==null)
            throw new Exception("No administrators with your id");
        ArrayList<Customers> customers;
        GenericDAO<Customers> customersDAO = new GenericDAO<>("Customers", new Customers());
        customers = customersDAO.getAll();
        customersDAO.closeAllDAOConnections();
        return customers;
    }
    /**Checks the details of the user*/
    private boolean isUserHaveCorrectDetails(Users user) throws Exception
    {
        String password= user.getPassword();
        if(password.contains(" "))
            throw new Exception("Spaces are not allowed in the password.");
        if(password.length()<7)
            throw new Exception("Your password is to short. Can not add such a user with that kind of insecure password to the database.");
        if((user.getEmail().charAt(0) == '@')||(user.getEmail().charAt(0) == ' ') || !(user.getEmail().contains("@")))
            throw new Exception("Invalid Email.");
        return true;
    }
    @SneakyThrows
    /**Adds customer and its user entity*/
    public void addCustomer(Users user ,Customers customer)
    {
        if(administrators==null)
            throw new Exception("No administrators with your id");
        if (isUserHaveCorrectDetails(user))
        createNewUser(user,customer);
    }
    /**Adds Airline and its user entity*/
    public void addAirline(Users user,AirlineCompanies airlineCompany) throws Exception
    {
        if(administrators==null)
            throw new Exception("No administrators with your id");
        if (isUserHaveCorrectDetails(user))
            createNewUser(user,airlineCompany);
    }
    /**Adds Administrator and its user entity*/
    public void addAdministrator(Users user,Administrators administrator) throws Exception
    {
        if(administrators==null)
            throw new Exception("No administrators with your id");
        if (isUserHaveCorrectDetails(user))
            createNewUser(user,administrator);
    }

    /**Removes customer its tickets and its user entity*/
    public void removeCustomer(Customers customer) throws Exception
    {
        if(administrators==null)
            throw new Exception("No administrators with your id");
        ArrayList<Tickets> customerTickets;
        GenericDAO<Customers> customersDAO = new GenericDAO<>("Customers",new Customers());
        GenericDAO<Tickets> ticketsDAO = new GenericDAO<>("Tickets",new Tickets());
        GenericDAO<Users> usersDAO = new GenericDAO<>("Users",new Users());
        if (customer.getId()==null)
            System.out.println("Id must be provided inside the customer. No removal was made in the DataBase");
        else {
            try
            {
                customerTickets = getTicketsByCustomer(customer);
                for (Tickets customerTicket : customerTickets) {
                    ticketsDAO.remove(customerTicket);
                }
                customersDAO.remove(customer);
                usersDAO.remove(usersDAO.getById(customer.getUserId()));
            }
            catch (Exception e)
            {
                System.out.println("Remove all other connections first" );
            }finally {
                customersDAO.closeAllDAOConnections();
                usersDAO.closeAllDAOConnections();
                ticketsDAO.closeAllDAOConnections();
            }

        }
        customersDAO.closeAllDAOConnections();
    }
    /**Removes Airline its flights its tickets and its user entity*/
    public void removeAirline(AirlineCompanies airlineCompany) throws Exception
    {
        if(administrators==null)
            throw new Exception("No administrators with your id");
        ArrayList<Flights> airlineFlights;
        GenericDAO<AirlineCompanies> airlineCompaniesDAO = new GenericDAO<>("Airline_Companies",new AirlineCompanies());
        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights",new Flights());
        GenericDAO<Tickets> ticketsDAO = new GenericDAO<>("Tickets",new Tickets());
        GenericDAO<Users> usersDAO = new GenericDAO<>("Users",new Users());
        if (airlineCompany.getId()==null)
            System.out.println("Id must be provided inside the airlineCompany. No removal was made in the DataBase");
        else {
            try {
                airlineFlights = getFlightsByAirlineCompany(airlineCompany);
                airlineFlights.forEach(e-> {
                    try {
                        getTicketsByFlight(e).forEach(s-> {
                            try {
                                ticketsDAO.remove(s);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    try {
                        flightsDAO.remove(e);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                airlineCompaniesDAO.remove(airlineCompany);
                usersDAO.remove(usersDAO.getById(airlineCompany.getUserId()));
            } catch (Exception e) {
                System.out.println("Remove all other connections first" );
            }finally {
                flightsDAO.closeAllDAOConnections();
                airlineCompaniesDAO.closeAllDAOConnections();
                usersDAO.closeAllDAOConnections();
            }
        }
        airlineCompaniesDAO.closeAllDAOConnections();
    }
    /**Removes Administrator and its user entity*/
    public void removeAdministrator(Administrators administrator) throws Exception
    {
        if(administrators==null)
            throw new Exception("No administrators with your id");
        GenericDAO<Administrators> administratorsDAO = new GenericDAO<>("Administrators",new Administrators());
        GenericDAO<Users> usersDAO = new GenericDAO<>("Users",new Users());
        if (administrator.getId()==null)
            System.out.println("Id must be provided inside the administrator. No removal was made in the DataBase");
        else {
            try {
                administratorsDAO.remove(administrator);
                usersDAO.remove(usersDAO.getById(administrator.getUserId()));
            } catch (Exception e) {
                System.out.println("Remove all other connections first");
            }finally
            {
                administratorsDAO.closeAllDAOConnections();
                usersDAO.closeAllDAOConnections();
            }
        }
        administratorsDAO.closeAllDAOConnections();
    }
}
