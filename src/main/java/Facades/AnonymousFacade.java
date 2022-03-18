package Facades;

import dao.GenericDAO;
import entities.Users;
import logintoken.LoginToken;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.ArrayList;

public class AnonymousFacade extends FacadeBase
{
    @SneakyThrows
    public FacadeBase login(String username,String password) throws Exception
    {
        GenericDAO<Users> usersDAO = new GenericDAO<>("Users",new Users());
        ArrayList<ArrayList<String>> columns = new ArrayList<>();
        columns.add(new ArrayList<>());
        columns.add(new ArrayList<>());
        columns.get(0).add("Id");
        columns.get(0).add("Username");
        columns.get(0).add("Password");
        columns.get(1).add("Role_Name");
        ResultSet joined = usersDAO.joinTwoByWithWhereClauseGetResultSet(columns,"User_Role","User_Roles","Id"
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
}
