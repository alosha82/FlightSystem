import dao.GenericDAO;
import entities.Flights;
import org.apache.commons.math3.util.Pair;

import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        String x ="hello";
        String y = x.substring(0,x.length()-1);
        System.out.println(y);
        GenericDAO<Flights> t = new GenericDAO<>("Flights",new Flights());
        Map<String,Collection<String>> tablesToColumnsMap=new HashMap<>();
        tablesToColumnsMap.put("Customers", List.of("First_Name", "Last_Name"));
        Map<Pair<String,String>,Pair<String,String>> foreignsToOrigins=new HashMap<>();
        foreignsToOrigins.put(new Pair<>("Tickets", "Flight_Id"), new Pair<>("Flights", "Id"));
        foreignsToOrigins.put(new Pair<>("Tickets", "Customer_Id"), new Pair<>("Customers", "Id"));

        //SELECT "Customers"."First_Name","Customers"."Last_Name" from "Flights" JOIN "Tickets" ON "Tickets"."Flight_Id" = "Flights"."Id"  JOIN "Customers" ON "Tickets"."Customer_Id" = "Customers"."Id"
        //String j = t.generateJoinMultipleByQuery(tablesToColumnsMap,"Tickets",foreignsToOrigins,"");
//        System.out.println(j);


    }
}
