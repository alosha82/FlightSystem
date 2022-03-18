package Facades;

import dao.GenericDAO;
import entities.Administrators;
import entities.AirlineCompanies;
import entities.Customers;
import logintoken.LoginToken;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
public class AdministratorFacade extends FacadeBase
{
    LoginToken token;

    public AdministratorFacade(LoginToken token) {this.token = token;}

    public ArrayList<Customers> getAllCustomers()
    {
        ArrayList<Customers> customers;
        GenericDAO<Customers> customersDAO = new GenericDAO<>("Customers",new Customers());
        customers=customersDAO.getAll();
        customersDAO.closeAllDAOConnections();
        return customers;
    }

    public void addCustomer(Customers customer) throws Exception
    {
        ArrayList<Customers> customers;
        GenericDAO<Customers> customersDAO = new GenericDAO<>("Customers",new Customers());
        customers=customersDAO.getAll();
        for (int i = 0; i < customers.size(); i++)
        {
            if ((customers.get(i).getFirstName().equals(customer.getFirstName()))
                    &&(customers.get(i).getLastName().equals(customer.getLastName()))
                    &&(customers.get(i).getAddress().equals(customer.getAddress()))
                    &&(customers.get(i).getPhoneNumber().equals(customer.getPhoneNumber()))
                    &&(customers.get(i).getCreditCardNumber().equals(customer.getCreditCardNumber()))
                    &&(customers.get(i).getUserId()==customer.getUserId()))
            {
                customersDAO.closeAllDAOConnections();
                throw new Exception("That Customer is already in the DataBase");
            }
        }
        customersDAO.add(customer);
        customersDAO.closeAllDAOConnections();
    }

    public void addAirline(AirlineCompanies airlineCompany) throws Exception
    {
        ArrayList<AirlineCompanies> airlineCompanies;
        GenericDAO<AirlineCompanies> airlineCompaniesDAO = new GenericDAO<>("AirlineCompanies",new AirlineCompanies());
        airlineCompanies=airlineCompaniesDAO.getAll();
        for (int i = 0; i < airlineCompanies.size(); i++)
        {
            if ((airlineCompanies.get(i).getName().equals(airlineCompany.getName()))
                    &&(airlineCompanies.get(i).getCountryId()==airlineCompany.getCountryId())
                    &&(airlineCompanies.get(i).getUserId()==airlineCompany.getUserId()))
            {
                airlineCompaniesDAO.closeAllDAOConnections();
                throw new Exception("That airline is already in the DataBase");
            }
        }
        airlineCompaniesDAO.add(airlineCompany);
        airlineCompaniesDAO.closeAllDAOConnections();
    }

    public void addAdministrator(Administrators administrator) throws Exception
    {
        ArrayList<Administrators> administrators;
        GenericDAO<Administrators> administratorsDAO = new GenericDAO<>("Administrators",new Administrators());
        administrators=administratorsDAO.getAll();
        for (int i = 0; i < administrators.size(); i++)
        {
            if ((administrators.get(i).getFirstName().equals(administrator.getFirstName()))
                    &&(administrators.get(i).getLastName().equals(administrator.getLastName()))
                    &&(administrators.get(i).getUserId()==administrator.getUserId()))
            {
                administratorsDAO.closeAllDAOConnections();
                throw new Exception("That administrator is already in the DataBase");
            }
        }
        administratorsDAO.add(administrator);
        administratorsDAO.closeAllDAOConnections();
    }

    public void removeCustomer(Customers customer)
    {
        GenericDAO<Customers> customersDAO = new GenericDAO<>("Customers",new Customers());
        if (customer.getId()==null)
            System.out.println("Id must be provided inside the ticket. No removal was made in the DataBase");
        else
            customersDAO.remove(customer.getId());
        customersDAO.closeAllDAOConnections();
    }

    public void removeAirline(AirlineCompanies airlineCompany)
    {
        GenericDAO<AirlineCompanies> airlineCompaniesDAO = new GenericDAO<>("AirlineCompanies",new AirlineCompanies());
        if (airlineCompany.getId()==null)
            System.out.println("Id must be provided inside the ticket. No removal was made in the DataBase");
        else
            airlineCompaniesDAO.remove(airlineCompany.getId());
        airlineCompaniesDAO.closeAllDAOConnections();
    }

    public void removeAdministrator(Administrators administrator)
    {
        GenericDAO<Administrators> administratorsDAO = new GenericDAO<>("Administrators",new Administrators());
        if (administrator.getId()==null)
            System.out.println("Id must be provided inside the ticket. No removal was made in the DataBase");
        else
            administratorsDAO.remove(administrator.getId());
        administratorsDAO.closeAllDAOConnections();
    }
}
