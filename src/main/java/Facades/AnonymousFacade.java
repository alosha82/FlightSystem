package Facades;

import dao.GenericDAO;
import entities.Customers;
import entities.Users;
import logintoken.LoginToken;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.*;

public class AnonymousFacade extends FacadeBase
{
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
            loginToken.setId(joined.getLong("Id"));
            loginToken.setName(username);
            loginToken.setRole(joined.getString("Role_Name"));
            String role = joined.getString("Role_Name");
            joined.close();
            switch(role)
            {
                case("Administrator")->
                        {
                            return new AdministratorFacade(loginToken);
                        }
                case("Customer")->
                        {
                            return new CustomerFacade(loginToken);
                        }
                case("AirLine")->
                        {
                            return new AirlineFacade(loginToken);
                        }
                default -> throw new Exception("Unknown role name in the database");
            }
        }
        else
        {
            throw new Exception("No Such username or password found in database");
        }
    }

    public void addCustomer(Users user, Customers customer)
    {
        createNewUser(user,customer);
    }
}
