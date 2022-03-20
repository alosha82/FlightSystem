import Facades.CustomerFacade;
import dao.GenericDAO;
import entities.Flights;
import entities.Users;
import logintoken.LoginToken;
import org.apache.commons.math3.util.Pair;

import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        String x ="hello";
        String y = x.substring(0,x.length()-1);
        System.out.println(y);
//        GenericDAO<Flights> t = new GenericDAO<>("Flights",new Flights());
//        Map<String,Collection<String>> tablesToColumnsMap=new HashMap<>();
//        tablesToColumnsMap.put("Customers", List.of("First_Name", "Last_Name"));
//        LinkedHashMap<Pair<String,String>,Pair<String,String>> foreignsToOrigins=new LinkedHashMap<>();
//        foreignsToOrigins.put(new Pair<>("Tickets", "Flight_Id"), new Pair<>("Flights", "Id"));
//        foreignsToOrigins.put(new Pair<>("Tickets", "Customer_Id"), new Pair<>("Customers", "Id"));
//        foreignsToOrigins.put(new Pair<>("Users", "id"), new Pair<>("Customers", "User_Id"));
//
//
////        SELECT "Customers"."First_Name","Customers"."Last_Name" from "Flights" JOIN "Tickets" ON "Tickets"."Flight_Id" = "Flights"."Id"  JOIN "Customers" ON "Tickets"."Customer_Id" = "Customers"."Id"
//        String j = t.generateJoinMultipleByQuery(tablesToColumnsMap,"Flights",foreignsToOrigins,"");
//        System.out.println(j);
//        Map<String,Collection<String>> tablesToColumnsMap1=new HashMap<>();
//        tablesToColumnsMap.put("Flights", List.of("Remaining_Tickets"));
//        LinkedHashMap<Pair<String,String>,Pair<String,String>> foreignsToOrigins1=new LinkedHashMap<>();
//        foreignsToOrigins.put(new Pair<>("Flights", "Origin_Country_Id"), new Pair<>("Country", "Id"));
//        t.joinTwoByGetResultSet(tablesToColumnsMap1,"Origin_Country_Id","Country","Id");

        CustomerFacade cf = new CustomerFacade(new LoginToken());
        cf.createNewUser(new Users(),new Flights());




    }
}
