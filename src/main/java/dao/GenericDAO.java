package dao;

import entities.*;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.math3.util.Pair;

import java.sql.Connection;
import java.sql.ResultSet;

import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class GenericDAO<T extends IEntities>
{
    private String dataBaseName="FlightSystem";
    private String tableName;
    private T entityType;
    private ArrayList<T> arrayOfEntityType;
    private PostgresConnection postgresConnection =new PostgresConnection();
    private Connection connection = postgresConnection.getConnection(dataBaseName,"1");
    private Statement stm = postgresConnection.getStatement();

    public GenericDAO(String tableName, T tableType) {
        this.tableName=tableName;
        this.entityType = tableType;
        this.arrayOfEntityType = new ArrayList<>();
    }

    @SneakyThrows
    /** Gets everything from a table and saves in the proper entity.
     * Returns: Arraylist of the DAO's type(entity)  */
    public ArrayList<T> getAll()
    {
        ResultSet result= stm.executeQuery("select * from "+tableName);
        while(result.next())
        {
            entityType.setAll(result);
            arrayOfEntityType.add(entityType);
        }
        result.close();
        return arrayOfEntityType;
    }
    @SneakyThrows
    /** Executes an SQL function.
     * Returns: ResultSet
     * the caller needs to close the ResultSet connection */
    public ResultSet runSQLFunctionGetResultSet(String functionName, List<String> properlyFormattedParameters)
    {
        String stringToExecute ="SELECT * FROM "+functionName+"(";
        for (int i = 0; i < properlyFormattedParameters.size(); i++) {
            stringToExecute=stringToExecute+properlyFormattedParameters.get(i)+",";
        }
        stringToExecute = stringToExecute.substring(0,stringToExecute.length()-1);
        return stm.executeQuery(stringToExecute);
    }

    @SneakyThrows
    /** Executes an SQL function. used to execute get_flights_by_parameters from sql stored functions
     * Has to receive an entity That supports the output table of the function.
     * Returns: ArrayList of the received entity.
     *  */
    public <V extends IEntities> ArrayList<V> runSQLFunction(String functionName, List<String> properlyFormattedParameters,V typeOfEntity)
    {
        String stringToExecute ="SELECT * FROM "+functionName+"(";
        for (int i = 0; i < properlyFormattedParameters.size(); i++) {
            stringToExecute=stringToExecute+properlyFormattedParameters.get(i)+",";
        }
        stringToExecute = stringToExecute.substring(0,stringToExecute.length()-1);
        return executeQueryAndSaveInTheProperEntity(stringToExecute,typeOfEntity);
    }

    @SneakyThrows
    /** Gets one line from the table because the id is unique.
     * Returns:  DAO's type(entity)  */
    public T getById(long id)
    {
        return  getByFieldType("Id",""+id);
    }

    @SneakyThrows
    /** Gets only one line from the table(the first one) matching the parameter. If needed more the one value use getByFieldTypeArr
     * Returns:  DAO's type(entity)  */
    public T getByFieldType(String formattedByPostgresSQLStandardsParameter,String fieldName)
    {
        ResultSet result= stm.executeQuery("select * from "+quote(tableName)+" WHERE "+fieldName+"="+formattedByPostgresSQLStandardsParameter);
        //needed because it starts on wrong column
        result.next();
        entityType.setAll(result);
        result.close();
        return entityType;
    }
   /**Those functions implemented by getByFieldTypeArr method trough GenericDOA<Flights> as a proof of concept I will implement them here.
    They should be implemented in business logic*/
//    public  ArrayList<AirlineCompanies> getAirlinesByCountry(int country_id) implemented in FacadeBase (get_airline_by_parameters) uses getByFieldTypeArr;
//    public  ArrayList<Flights>getFlightsByOriginCountryId(int origin_country_id)
//    {
//        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights", new Flights());
//        ArrayList<Flights> listOfFlights=flightsDAO.getByFieldTypeArr(""+origin_country_id,"Country_Id");
//        flightsDAO.closeAllDAOConnections();
//        return listOfFlights;
//    }
//    public  ArrayList<Flights>getFlightsByDestinationCountryId(int destination_country_id)
//    {
//        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights", new Flights());
//        ArrayList<Flights> listOfFlights=flightsDAO.getByFieldTypeArr(""+destination_country_id,"Country_Id");
//        flightsDAO.closeAllDAOConnections();
//        return listOfFlights;
//    }
//    public  ArrayList<Flights> getFlightsByDepartureDate(Timestamp date)
//    {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights", new Flights());
//        ArrayList<Flights> listOfFlightsByDepartureDate=flightsDAO.getByFieldTypeArr
//                (""+dateFormat.format(date),"DATE(\"Departure_Date\")");
//        flightsDAO.closeAllDAOConnections();
//        return listOfFlightsByDepartureDate;
//    }
//
//    public  ArrayList<Flights> getFlightsByLandingDate(Timestamp date)
//    {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        GenericDAO<Flights> flightsDAO = new GenericDAO<>("Flights", new Flights());
//        ArrayList<Flights> listOfFlightsByDepartureDate=flightsDAO.getByFieldTypeArr
//                (""+dateFormat.format(date),"DATE(\"Landing_Date\")");
//        flightsDAO.closeAllDAOConnections();
//        return listOfFlightsByDepartureDate;
//    }
    @SneakyThrows
    /** Gets every line from the table matching the parameter.
     * Returns: Arraylist of the DAO's type(entity)  */
    public ArrayList<T> getByFieldTypeArr(String formattedByPostgresSQLStandardsParameter,String formattedFieldName)
    {
        String stringToExecute ="select * from "+quote(tableName)+" WHERE "+formattedFieldName+"="+formattedByPostgresSQLStandardsParameter;
        // might be a problem because the entityType might already have values.(might be problematic if the table has null fields. not sure)
        return executeQueryAndSaveInTheProperEntity(stringToExecute,entityType);
    }
    /** Removes a line from the table by its id.
     * Will fail if the entity in the table has somebody pointing to it  */
    public void remove(long id) throws Exception
    {
        stm.executeUpdate("DELETE from "+quote(tableName)+" WHERE id="+id);
        System.out.println("done");
    }
    @SneakyThrows
    /** Adds an entity to the table.
     * Will fail if the entity has the same values for the columns marked unique  */
    public void add(T typeOfEntity)
    {
        ArrayList<String> fieldsInStringForm = typeOfEntity.getAllNeededValuesExceptIdInStringFormat();
        ArrayList<String> columnNames = typeOfEntity.getColumnNames();
        //INSERT INTO Administrators (first_name,last_name,user_id) VALUES (
        String stringForExecution = "INSERT INTO "+quote(tableName)+" (";
        for (int i = 1; i < columnNames.size(); i++)
        {
            stringForExecution=stringForExecution+columnNames.get(i)+",";
        }
        stringForExecution = stringForExecution.substring(0,stringForExecution.length()-1);
        stringForExecution = stringForExecution+") VALUES (";
        for (int i = 0; i < fieldsInStringForm.size(); i++)
        {
            stringForExecution=stringForExecution+fieldsInStringForm.get(i)+",";
        }
        stringForExecution = stringForExecution.substring(0,stringForExecution.length()-1);
        stm.executeUpdate(stringForExecution+")");
    }
    @SneakyThrows
    /** Updates an entity to the table.
     * Can fail*/
    public void update(T typeOfEntity,long id)
    {
        ArrayList<String> columnNames = typeOfEntity.getColumnNames();
        ArrayList<String> fieldsInStringForm = typeOfEntity.getAllNeededValuesExceptIdInStringFormat();
        String stringForUpdate ="UPDATE "+quote(tableName)+" SET ";
        for (int i = 0; i < fieldsInStringForm.size(); i++)
        {
            stringForUpdate=stringForUpdate + columnNames.get(i+1)+"="+fieldsInStringForm.get(i)+",";
        }
        stringForUpdate = stringForUpdate.substring(0,stringForUpdate.length()-1);
        stm.executeUpdate(stringForUpdate+" WHERE id="+id);
    }

    /** Sample to the output query SELECT "Customers"."First_Name","Customers"."Last_Name"
     FROM "Tickets"
     JOIN "Flights" ON "Tickets"."Flight_Id" = "Flights"."Id"  JOIN "Customers" ON "Tickets"."Customer_Id" = "Customers"."Id"
     JOIN "Users" on "Customers"."User_Id="Users"."id"
     */
    /**Generates a query to the join function.
     * Returns: String(the generated query)
     * Parameters:
     * tablesToColumnsMap - Map of table names to a collection of the names of the columns you want to see
     * from - the name of the table after FROM statement
     * foreignFieldToOriginalField - Map with two pairs in it. The pairs are the parameters from each side of the = in the condition part
     * whereClose - the ware condition after join. Might be passed as "" if not needed*/
    @SneakyThrows
    public String generateJoinMultipleByQuery(Map<String,Collection<String>> tablesToColumnsMap,
                                              String from,
                                              LinkedHashMap<Pair<String, String>, Pair<String, String>> foreignFieldToOriginalField,
                                              String whereClause)
    {

        val selectContents = "SELECT " + tablesToColumnsMap.entrySet().stream()
                        .map(e -> e.getValue().stream()
                                .map(colName -> strDotStrQuoted(e.getKey(),colName))
                                .collect(Collectors.joining(",")))
                        .collect(Collectors.joining(","));

        // we work on a single foreign table, so they all have the same table name
        String fromContents = "FROM " + quote(from);

        val joinContents = foreignFieldToOriginalField.entrySet().stream()
                .map(entry -> {
                    val foreign = entry.getKey();
                    val original = entry.getValue();
                    String foreignTableName = foreign.getFirst();
                    String foreignFieldName = foreign.getSecond();
                    String originalTableName = original.getFirst();
                    String originalFieldName = original.getSecond();
                    return "JOIN " + quote(foreignTableName) + " ON "
                            + strDotStrQuoted(foreignTableName, foreignFieldName) +"=" +strDotStrQuoted(originalTableName, originalFieldName);
                })
                .collect(Collectors.joining(" "));
        String stringToExecute = String.join(" ", selectContents, fromContents, joinContents);
        return stringToExecute+whereClause;
    }
    /** Adds quotes */
    private String quote(String unquoted) {return "\""+unquoted+"\"";}
    /** Quotes the strings then joins them by a dot */
    private String strDotStrQuoted(String str1, String str2) {return quote(str1)+"."+quote(str2);}

    @SneakyThrows
    /** Executes a query and saves the result in an array list of the received entity type. Note: the type may not be the same as the DAO's*/
    private <V extends IEntities> ArrayList<V> executeQueryAndSaveInTheProperEntity(String query,V typeOfEntity)
    {
        ArrayList<V> ArrayListOfTypeOfEntity=new ArrayList<>();
        ResultSet result= stm.executeQuery(query);
        while(result.next())
        {
            typeOfEntity.setAll(result);
            ArrayListOfTypeOfEntity.add(typeOfEntity);
        }
        result.close();
        return ArrayListOfTypeOfEntity;
    }
    /**
    function joinTwoBy, and its brothers in name, joins the current type of the DAO with another table.
     Returns:An array list of the received entity type. Note: the type may not be the same as the DAO's
     * Parameters:
     * tablesToColumnsToDisplayMap - Map of table names to a collection of the names of the columns you want to see
     * thisFK - Name of the column that is the foreign key of this DAO table
     * otherTableName - Name of the table you want to join
     * otherPK - Name of the column that is the primary key of the other table
     * typeOfEntity - Type of entity that can house inside the join output by your specification
    */
    public<V extends IEntities> ArrayList<V> joinTwoBy(Map<String,Collection<String>> tablesToColumnsToDisplayMap,
                                                       String thisFK,
                                                       String otherTableName,
                                                       String otherPK,
                                                       V typeOfEntity)
    {
        return joinTwoByWithWhereClause(tablesToColumnsToDisplayMap,thisFK,otherTableName,otherPK,typeOfEntity,"");
    }
    /**
     function joinTwoBy, and its brothers in name, joins the current type of the DAO with another table.
     Returns: ResultSet
     * Parameters:
     * tablesToColumnsToDisplayMap - Map of table names to a collection of the names of the columns you want to see
     * thisFK - Name of the column that is the foreign key of this DAO table
     * otherTableName - Name of the table you want to join
     * otherPK - Name of the column that is the primary key of the other table
     */
    /**the caller needs to close the ResultSet connection */
    public ResultSet joinTwoByGetResultSet(Map<String,Collection<String>> tablesToColumnsTODisplayMap,String thisFK,String otherTableName,String otherPK)
    {
        return joinTwoByWithWhereClauseGetResultSet(tablesToColumnsTODisplayMap,thisFK,otherTableName,otherPK,"");
    }
    /**
     function joinTwoBy, and its brothers in name, joins the current type of the DAO with another table.
     Returns:An array list of the received entity type. Note: the type may not be the same as the DAO's
     * Parameters:
     * tablesToColumnsToDisplayMap - Map of table names to a collection of the names of the columns you want to see
     * thisFK - Name of the column that is the foreign key of this DAO table
     * otherTableName - Name of the table you want to join
     * otherPK - Name of the column that is the primary key of the other table
     * typeOfEntity - Type of entity that can house inside the join output by your specification
     * whereClause -the ware condition after join. Might be passed as "" if not needed
     */
    public <V extends IEntities> ArrayList<V> joinTwoByWithWhereClause(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                                       String thisFK,String otherTableName,
                                                                       String otherPK,
                                                                       V typeOfEntity,
                                                                       String whereClause)
    {
        LinkedHashMap<Pair<String,String>,Pair<String,String>> foreignToOrigins=new LinkedHashMap<>();
        foreignToOrigins.put(new Pair<>(tableName, thisFK), new Pair<>(otherTableName, otherPK));
        String stringToExecute = generateJoinMultipleByQuery(tablesToColumnsTODisplayMap,otherTableName,foreignToOrigins,whereClause);
        return executeQueryAndSaveInTheProperEntity(stringToExecute,typeOfEntity);
    }

    @SneakyThrows
    /**
     function joinTwoBy, and its brothers in name, joins the current type of the DAO with another table.
     Returns: ResultSet
     * Parameters:
     * tablesToColumnsToDisplayMap - Map of table names to a collection of the names of the columns you want to see
     * thisFK - Name of the column that is the foreign key of this DAO table
     * otherTableName - Name of the table you want to join
     * otherPK - Name of the column that is the primary key of the other table
     */
    /**the caller needs to close the ResultSet connection */
    public ResultSet joinTwoByWithWhereClauseGetResultSet(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                          String thisFK,
                                                          String otherTableName,
                                                          String otherPK,
                                                          String whereClause)
    {
        LinkedHashMap<Pair<String,String>,Pair<String,String>> foreignToOrigins=new LinkedHashMap<>();
        foreignToOrigins.put(new Pair<>(tableName, thisFK), new Pair<>(otherTableName, otherPK));
        String stringToExecute = generateJoinMultipleByQuery(tablesToColumnsTODisplayMap,otherTableName,foreignToOrigins,whereClause);
        System.out.println(stringToExecute);
        return stm.executeQuery(stringToExecute);
    }

    /**function joinMultipleBy, and its brothers in name, joins the multiple tables(no connection to this DAO's table name).
     * Returns: An array list of the received entity type. Note: the type may not be the same as the DAO's
     * Parameters:
     * tablesToColumnsMap - Map of table names to a collection of the names of the columns you want to see
     * from - the name of the table after FROM statement
     * foreignFieldToOriginalField - Map with two pares in it. The pairs are the parameters from each side of the = in the condition part
     * typeOfEntity - Type of entity that can house inside the join output by your specification
     */
    public <V extends IEntities> ArrayList<V> joinMultipleBy(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                             String from,
                                                             LinkedHashMap<Pair<String, String>, Pair<String, String>> foreignFieldToOriginalField,
                                                             V typeOfEntity)
    {
        return joinMultipleByWithWhereClause(tablesToColumnsTODisplayMap,from,foreignFieldToOriginalField,typeOfEntity,"");
    }
    /**function joinMultipleBy, and its brothers in name, joins the multiple tables(no connection to this DAO's table name).
     * Returns: An array list of the received entity type. Note: the type may not be the same as the DAO's
     * Parameters:
     * tablesToColumnsMap - Map of table names to a collection of the names of the columns you want to see
     * from - the name of the table after FROM statement
     * foreignFieldToOriginalField - Map with two pares in it. The pairs are the parameters from each side of the = in the condition part
     */
    /**the caller needs to close the ResultSet connection */
    public ResultSet joinMultipleByGetResultSet(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                String from,
                                                LinkedHashMap<Pair<String, String>, Pair<String, String>> foreignFieldToOriginalField)
    {
        return joinMultipleByWithWhereClauseGetResultSet(tablesToColumnsTODisplayMap,from,foreignFieldToOriginalField,"");
    }

     /**Implementation of this method uses joinMultipleByWithWhereClause.
      * The greyed out function bellow should be implemented in the business logic if needed.
      * Wrote it here because it was part of the assignment*/
//    public ArrayList<Flights>getFlightsByCustomer(Customers customer)
//    {
//        ArrayList<Flights> flights;
//        Map<String,Collection<String>> tablesToColumnsMap=new HashMap<>();
//        GenericDAO<Flights> flightDAO = new GenericDAO<>("Flights",new Flights());
//        tablesToColumnsMap.put("Flights", List.of("Id", "Airline_Company_Id","Origin_Country_Id","Destination_Country_Id"
//                ,"Departure_time","Landing_time","Remaining_Tickets"));
//        LinkedHashMap<Pair<String,String>,Pair<String,String>> foreignsToOrigins=new LinkedHashMap<>();
//        foreignsToOrigins.put(new Pair<>("Tickets", "Flight_Id"), new Pair<>("Flights", "Id"));
//        foreignsToOrigins.put(new Pair<>("Tickets", "Customer_Id"), new Pair<>("Customers", "Id"));
//        String whereClose = "WHERE \"Customers\".\"Id\" = "+ customer.getId();
//        flights =flightDAO.joinMultipleByWithWhereClause(tablesToColumnsMap,"Tickets",foreignsToOrigins,new Flights(),whereClose);
//        flightDAO.closeAllDAOConnections();
//        return flights;
//    }
    /**function joinMultipleBy, and its brothers in name, joins the multiple tables(no connection to this DAO's table name).
     * Returns: An array list of the received entity type. Note: the type may not be the same as the DAO's
     * Parameters:
     * tablesToColumnsMap - Map of table names to a collection of the names of the columns you want to see
     * from - the name of the table after FROM statement
     * foreignFieldToOriginalField - Map with two pares in it. The pairs are the parameters from each side of the = in the condition part
     * typeOfEntity - Type of entity that can house inside the join output by your specification
     * whereClose - the ware condition after join. Might be passed as "" if not needed
     */
    public <V extends IEntities> ArrayList<V> joinMultipleByWithWhereClause(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                                            String from,
                                                                            LinkedHashMap<Pair<String, String>, Pair<String, String>> foreignFieldToOriginalField,
                                                                            V typeOfEntity,
                                                                            String whereClause)
    {
        String query = generateJoinMultipleByQuery(tablesToColumnsTODisplayMap,from,foreignFieldToOriginalField,whereClause);
        return executeQueryAndSaveInTheProperEntity(query,typeOfEntity);
    }

    @SneakyThrows
    /**function joinMultipleBy, and its brothers in name, joins the multiple tables(no connection to this DAO's table name).
     * Returns: An array list of the received entity type. Note: the type may not be the same as the DAO's
     * Parameters:
     * tablesToColumnsMap - Map of table names to a collection of the names of the columns you want to see
     * from - the name of the table after FROM statement
     * foreignFieldToOriginalField - Map with two pares in it. The pairs are the parameters from each side of the = in the condition part
     * whereClose - the ware condition after join. Might be passed as "" if not needed
     */
    /**the caller needs to close the ResultSet connection */
    public ResultSet joinMultipleByWithWhereClauseGetResultSet(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                               String from,
                                                               LinkedHashMap<Pair<String, String>, Pair<String, String>> foreignFieldToOriginalField,
                                                               String whereClause)
    {
        String query = generateJoinMultipleByQuery(tablesToColumnsTODisplayMap,from,foreignFieldToOriginalField,whereClause);
        return stm.executeQuery(query);
    }

    @SneakyThrows
    /**closes connection */
    public void closeAllDAOConnections()
    {
        connection.close();
        stm.close();
    }
}
