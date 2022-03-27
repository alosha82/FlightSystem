package Facades;

import dao.GenericDAO;
import entities.Administrators;
import entities.AirlineCompanies;
import entities.Customers;
import entities.Users;
import logintoken.LoginToken;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.*;

public class AnonymousFacade extends FacadeBase
{
    /**Joins Users with User_Roles and filters the joined entity by username and password,
     * then decides what facade to return to the caller dy the role that was acquainted through joining*/
    @SneakyThrows
    public AnonymousFacade login(String username,String password) throws Exception
    {
        GenericDAO<Users> usersDAO = new GenericDAO<>("Users",new Users());
        Map<String, Collection<String>> tablesToColumnsMap=new HashMap<>();
        tablesToColumnsMap.put("Users", List.of("Id", "Username","Password"));
        tablesToColumnsMap.put("User_Roles", List.of("Role_Name"));

        ResultSet joined = usersDAO.joinTwoByWithWhereClauseGetResultSet(tablesToColumnsMap,"User_Role","User_Roles","Id"
                , "WHERE \"Users\".\"Username\"=\'"+username+"\' AND \"Users\".\"Password\"=\'"+password+"\'");
        if (joined.next())
        {
            LoginToken loginToken = new LoginToken();
            loginToken.setName(username);
            loginToken.setRole(joined.getString("Role_Name"));
            long userId = joined.getLong("Id");
            String role = joined.getString("Role_Name");
            joined.close();
            switch(role)
            {
                case("Administrator")->
                        {
                            GenericDAO<Administrators> administratorsDAO=new GenericDAO<>("Administrators",new Administrators());
                            loginToken.setId(administratorsDAO.getByFieldType(""+userId,"User_Id").getId());
                            return new AdministratorFacade(loginToken);
                        }
                case("Customer")->
                        {
                            GenericDAO<Customers> customersDAO=new GenericDAO<>("Customers",new Customers());
                            loginToken.setId(customersDAO.getByFieldType(""+userId,"User_Id").getId());
                            return new CustomerFacade(loginToken);
                        }
                case("Airline_Company")->
                        {
                            GenericDAO<AirlineCompanies> airlineCompaniesDAO=new GenericDAO<>("Airline_Companies",new AirlineCompanies());
                            loginToken.setId(airlineCompaniesDAO.getByFieldType(""+userId,"User_Id").getId());
                            return new AirlineFacade(loginToken);
                        }
                default -> throw new Exception("Unknown role name in the database.");
            }
        }
        else
        {
            throw new Exception("No Such username or password found in database.");
        }
    }

    /**Adds customer and its user entity*/
    public void addCustomer(Users user, Customers customer)throws Exception
    {
        String password= user.getPassword();
        if(password.contains(" "))
            throw new Exception("Spaces are not allowed in the password.");
        if(password.length()<7)
            throw new Exception("Your password is to short. Can not add such a user with that kind of insecure password to the database.");
        if((user.getEmail().charAt(0) == '@')||(user.getEmail().charAt(0) == ' ') || !(user.getEmail().contains("@")))
            throw new Exception("Invalid Email.");
        createNewUser(user,customer);
    }
}
