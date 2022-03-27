package Facades;

import dao.GenericDAO;
import entities.Customers;
import entities.Flights;
import logintoken.LoginToken;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class AdministratorFacadeTest {

    @Test
    @SneakyThrows
    void getAllCustomers()
    {
        LoginToken loginToken =new LoginToken();
        loginToken.setId(3l);
        loginToken.setName("createfromnewuseradministratorrole");
        loginToken.setRole("Administrator");
        AdministratorFacade administratorFacade= new AdministratorFacade(loginToken);
        System.out.println(administratorFacade.getAllCustomers());
    }
}