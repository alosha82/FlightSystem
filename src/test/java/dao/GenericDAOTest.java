package dao;


import entities.Flights;
import entities.UserRoles;
import entities.Users;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.apache.commons.math3.util.Pair;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericDAOTest {

    @org.junit.jupiter.api.Test
    void getAll()
    {
        ArrayList<UserRoles> userRoles;
        GenericDAO<UserRoles> userRolesDAO= new GenericDAO<>("User_Roles",new UserRoles());
        userRoles = userRolesDAO.getAll();
        ArrayList<UserRoles> myUserRoles=new ArrayList<>();
        val userRolesForTest = new UserRoles();
        userRolesForTest.setId(1);
        userRolesForTest.setRoleName("Administrator");
        myUserRoles.add(userRolesForTest);
        val userRolesForTest1 = new UserRoles();
        userRolesForTest1.setId(2);
        userRolesForTest1.setRoleName("Airline_Company");
        myUserRoles.add(userRolesForTest1);
        val userRolesForTest2 = new UserRoles();
        userRolesForTest2.setId(3);
        userRolesForTest2.setRoleName("Customer");
        myUserRoles.add(userRolesForTest2);
        assertEquals(userRoles,myUserRoles);
    }

    @org.junit.jupiter.api.Test
    void runSQLFunction()
    {
        ArrayList<Users> users= new ArrayList<>();
        ArrayList<Users> testUsers=new ArrayList<>();
        GenericDAO<Users> usersDAO = new GenericDAO<>("Users",new Users());
        Users userForTest = new Users();
        userForTest.setId(1);
        userForTest.setUserName("someusername");
        userForTest.setPassword("12345");
        userForTest.setEmail("a@b");
        userForTest.setUserRole(1);
//        usersDAO.add(userForTest);
        testUsers.add(userForTest);
        userForTest=new Users(usersDAO.runSQLFunction("get_user_by_username",List.of("\'someusername\'"),new Users()).get(0));
        users.add(userForTest);
        assertEquals(users,testUsers);
    }

    @org.junit.jupiter.api.Test
    void getById()
    {
        UserRoles userRoles;
        GenericDAO<UserRoles> userRolesDAO= new GenericDAO<>("User_Roles",new UserRoles());
        userRoles = userRolesDAO.getById(1);
        val userRolesForTest = new UserRoles();
        userRolesForTest.setId(1);
        userRolesForTest.setRoleName("Administrator");
        assertEquals(userRoles,userRolesForTest);
    }

    @org.junit.jupiter.api.Test
    void getByFieldType()
    {
        UserRoles userRoles;
        GenericDAO<UserRoles> userRolesDAO= new GenericDAO<>("User_Roles",new UserRoles());
        userRoles = userRolesDAO.getByFieldType("\'Administrator\'","Role_Name");
        val userRolesForTest = new UserRoles();
        userRolesForTest.setId(1);
        userRolesForTest.setRoleName("Administrator");
        assertEquals(userRoles,userRolesForTest);
    }

    @org.junit.jupiter.api.Test
    void getByFieldTypeArr()
    {
        ArrayList<UserRoles> userRoles;
        GenericDAO<UserRoles> userRolesDAO= new GenericDAO<>("User_Roles",new UserRoles());
        userRoles = userRolesDAO.getByFieldTypeArr("\'Administrator\'","Role_Name");
        ArrayList<UserRoles> myUserRoles=new ArrayList<>();
        val userRolesForTest = new UserRoles();
        userRolesForTest.setId(1);
        userRolesForTest.setRoleName("Administrator");
        myUserRoles.add(userRolesForTest);
        assertEquals(userRoles,myUserRoles);
    }

    @org.junit.jupiter.api.Test
    @SneakyThrows
    void remove()
    {
        UserRoles userRoles;
        GenericDAO<UserRoles> userRolesDAO=new GenericDAO<>("User_Roles",new UserRoles());
        userRolesDAO.remove(10);
    }

    @org.junit.jupiter.api.Test
    void add()
    {
        UserRoles userRoles;
        GenericDAO<UserRoles> userRolesDAO=new GenericDAO<>("User_Roles",new UserRoles());
        val userRolesForTest = new UserRoles();
        userRolesForTest.setId(10);
        userRolesForTest.setRoleName("Administrator4");
        userRolesDAO.add(userRolesForTest);
        userRoles = userRolesDAO.getById(10);
        assertEquals(userRoles,userRolesForTest);
    }

    @org.junit.jupiter.api.Test
    void update()
    {
        UserRoles userRoles;
        GenericDAO<UserRoles> userRolesDAO= new GenericDAO<>("User_Roles",new UserRoles());
        val userRolesForTest = new UserRoles();
        userRolesForTest.setId(1);
        userRolesForTest.setRoleName("Administrator1");
        userRolesDAO.update(userRolesForTest,1);
        userRoles = userRolesDAO.getById(1);
        assertEquals(userRoles,userRolesForTest);

    }

    @org.junit.jupiter.api.Test
    void generateJoinMultipleByQuery()
    {
        GenericDAO<Flights> t = new GenericDAO<>("Flights",new Flights());
        Map<String, Collection<String>> tablesToColumnsMap=new HashMap<>();
        tablesToColumnsMap.put("Customers", List.of("First_Name", "Last_Name"));
        LinkedHashMap<Pair<String,String>,Pair<String,String>> foreignsToOrigins=new LinkedHashMap<>();
        foreignsToOrigins.put(new Pair<>("Tickets", "Flight_Id"), new Pair<>("Flights", "Id"));
        foreignsToOrigins.put(new Pair<>("Customers", "Id"),new Pair<>("Tickets", "Customer_Id"));
        String generatedString = t.generateJoinMultipleByQuery(tablesToColumnsMap,"Flights",foreignsToOrigins,"");
        String stringForTest = "SELECT \"Customers\".\"First_Name\",\"Customers\".\"Last_Name\" FROM \"Flights\" JOIN \"Tickets\" ON \"Tickets\".\"Flight_Id\"=\"Flights\".\"Id\" JOIN \"Customers\" ON \"Customers\".\"Id\"=\"Tickets\".\"Customer_Id\"";
        System.out.println(generatedString);
        assertEquals(generatedString,stringForTest);
    }

    @org.junit.jupiter.api.Test
    void joinTwoBy()
    {

    }

    @org.junit.jupiter.api.Test
    void joinTwoByWithWhereClause() {
    }

    @org.junit.jupiter.api.Test
    void joinMultipleBy() {
    }

    @org.junit.jupiter.api.Test
    void joinMultipleByWithWhereClause() {
    }


}