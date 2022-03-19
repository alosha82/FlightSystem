package dao;

import entities.*;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.math3.util.Pair;

import java.sql.Connection;
import java.sql.ResultSet;

import java.sql.Statement;
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
        //getAll();
    }

    @SneakyThrows
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
    public T getById(long id)
    {
        return  getByFieldType("Id",""+id);
    }
    @SneakyThrows
    public T getByFieldType(String formattedByPostgresSQLStandardsParameter,String fieldName)
    {
        ResultSet result= stm.executeQuery("select * from "+tableName+" WHERE "+fieldName+"="+formattedByPostgresSQLStandardsParameter);
        //needed because it starts on wrong column
        result.next();
        entityType.setAll(result);
        result.close();
        return entityType;
    }
    @SneakyThrows
    /*
    Possible field types: Numerical,Text,Date
    */
    public T getByFieldType1(String fieldName,String parameter,String sqlFieldType)
    {
        switch (sqlFieldType)
        {
            case("Numerical")->parameter=parameter;
            case("Text")->parameter="\'"+parameter+"\'";
            //TODO- figure out how to give date in SQL query
            case("Date")->parameter="\'"+parameter+"\'";
        }
        ResultSet result= stm.executeQuery("select * from "+tableName+" WHERE "+fieldName+"="+parameter);
        //needed because it starts on wrong column
        result.next();
        entityType.setAll(result);
        result.close();
        return entityType;
    }
    @SneakyThrows
    public void remove(long id)
    {
        stm.executeUpdate("DELETE from "+tableName+" WHERE id="+id);
        System.out.println("done");
    }
    @SneakyThrows
    public void add(T typeOfEntity)
    {
        ArrayList<String> fieldsInStringForm = typeOfEntity.getAllExceptIdInStringFormat();
        ArrayList<String> columnNames = typeOfEntity.getColumnNames();
        //INSERT INTO Administrators (first_name,last_name,user_id) VALUES (
        String stringForExecution = "INSERT INTO "+tableName+" (";
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
    public void update(T typeOfEntity,long id)
    {
        ArrayList<String> columnNames = typeOfEntity.getColumnNames();
        ArrayList<String> fieldsInStringForm = typeOfEntity.getAllExceptIdInStringFormat();
        String stringForUpdate ="UPDATE "+tableName+" SET ";
        for (int i = 0; i < fieldsInStringForm.size(); i++)
        {
            stringForUpdate=stringForUpdate + columnNames.get(i+1)+"="+fieldsInStringForm.get(i)+",";
        }
        stringForUpdate = stringForUpdate.substring(0,stringForUpdate.length()-1);
        stm.executeUpdate(stringForUpdate+" WHERE id="+id);
    }

    /** SELECT "Customers"."First_Name","Customers"."Last_Name"
     FROM "Tickets"
     JOIN "Flights" ON "Tickets"."Flight_Id" = "Flights"."Id"  JOIN "Customers" ON "Tickets"."Customer_Id" = "Customers"."Id"
     join "users" on "Customers.fg="Users"."id"
     */
    @SneakyThrows
    private String generateJoinMultipleByQuery(Map<String,Collection<String>> tablesToColumnsMap,
                                              String from,
                                              Map<Pair<String, String>, Pair<String, String>> foreignFieldToOriginalField,
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

    private String quote(String unquoted) {return "\""+unquoted+"\"";}
    private String strDotStrQuoted(String str1, String str2) {return quote(str1)+"."+quote(str2);}

    @SneakyThrows
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
    allColumnsToDisplay should be ordered in such order: first array index is an array of columns of this table that you want to select
    the next array index is an array of columns of the other table that you want to select
    To work properly it needs an entity that supports the join with the correct number of columns and types, that entity should be transferred typeOfEntity.
    */
    public<V extends IEntities> ArrayList<V> joinTwoBy(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                       String thisFK,
                                                       String otherTableName,
                                                       String otherPK,
                                                       V typeOfEntity)
    {
        return joinTwoByWithWhereClause(tablesToColumnsTODisplayMap,thisFK,otherTableName,otherPK,typeOfEntity,"");
    }

    public ResultSet joinTwoByGetResultSet(Map<String,Collection<String>> tablesToColumnsTODisplayMap,String thisFK,String otherTableName,String otherPK)
    {
        return joinTwoByWithWhereClauseGetResultSet(tablesToColumnsTODisplayMap,thisFK,otherTableName,otherPK,"");
    }

    public <V extends IEntities> ArrayList<V> joinTwoByWithWhereClause(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                                       String thisFK,String otherTableName,
                                                                       String otherPK,
                                                                       V typeOfEntity,
                                                                       String whereClause)
    {
        Map<Pair<String,String>,Pair<String,String>> foreignToOrigins=new HashMap<>();
        foreignToOrigins.put(new Pair<>(tableName, thisFK), new Pair<>(otherTableName, otherPK));
        String stringToExecute = generateJoinMultipleByQuery(tablesToColumnsTODisplayMap,tableName,foreignToOrigins,whereClause);
        return executeQueryAndSaveInTheProperEntity(stringToExecute,typeOfEntity);
    }

    @SneakyThrows
    /*the caller needs to close the ResultSet connection */
    public ResultSet joinTwoByWithWhereClauseGetResultSet(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                          String thisFK,
                                                          String otherTableName,
                                                          String otherPK,
                                                          String whereClause)
    {
        Map<Pair<String,String>,Pair<String,String>> foreignToOrigins=new HashMap<>();
        foreignToOrigins.put(new Pair<>(tableName, thisFK), new Pair<>(otherTableName, otherPK));
        String stringToExecute = generateJoinMultipleByQuery(tablesToColumnsTODisplayMap,tableName,foreignToOrigins,whereClause);
        return stm.executeQuery(stringToExecute);
    }

    /*
    function joinMultipleBy, and its brothers in name, joins the current type of the DAO with other tables.
    allColumnsToDisplay should be ordered in such order: first array index is an array of columns of this table that you want to select
    the next array index is an array of columns of the first table in otherTablesNames array  that you want to select and so on
    To work properly it needs an entity that supports the join with the correct number of columns and types, that entity should be transferred typeOfEntity.
    */
    public <V extends IEntities> ArrayList<V> joinMultipleBy(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                             String from,
                                                             Map<Pair<String, String>, Pair<String, String>> foreignFieldToOriginalField,
                                                             V typeOfEntity)
    {
        return joinMultipleByWithWhereClause(tablesToColumnsTODisplayMap,from,foreignFieldToOriginalField,typeOfEntity,"");
    }

    public ResultSet joinMultipleByGetResultSet(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                String from,
                                                Map<Pair<String, String>, Pair<String, String>> foreignFieldToOriginalField)
    {
        return joinMultipleByWithWhereClauseGetResultSet(tablesToColumnsTODisplayMap,from,foreignFieldToOriginalField,"");
    }

    public <V extends IEntities> ArrayList<V> joinMultipleByWithWhereClause(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                                            String from,
                                                                            Map<Pair<String, String>, Pair<String, String>> foreignFieldToOriginalField,
                                                                            V typeOfEntity,
                                                                            String whereClause)
    {
        String query = generateJoinMultipleByQuery(tablesToColumnsTODisplayMap,from,foreignFieldToOriginalField,whereClause);
        return executeQueryAndSaveInTheProperEntity(query,typeOfEntity);
    }

    @SneakyThrows
    /*the caller needs to close the ResultSet connection */
    public ResultSet joinMultipleByWithWhereClauseGetResultSet(Map<String,Collection<String>> tablesToColumnsTODisplayMap,
                                                               String from,
                                                               Map<Pair<String, String>, Pair<String, String>> foreignFieldToOriginalField,
                                                               String whereClause)
    {
        String query = generateJoinMultipleByQuery(tablesToColumnsTODisplayMap,from,foreignFieldToOriginalField,whereClause);
        return stm.executeQuery(query);
    }

    @SneakyThrows
    public void closeAllDAOConnections()
    {
        connection.close();
        stm.close();
    }
}
