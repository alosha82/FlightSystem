package dao;

import entities.*;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;

import java.sql.Statement;
import java.util.ArrayList;

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
        getAll();
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

//    @SneakyThrows
//    private String generateJoinTwoByQuery(ArrayList<ArrayList<String>> allColumnsToDisplay, String thisFK, String otherTableName, String otherPK,String whereClause)
//    {
//        ArrayList<String> tablesNames =new ArrayList<>();
//        tablesNames.add(tableName);
//        tablesNames.add(otherTableName);
//        String stringToExecute = "SELECT ";
//        for (int i = 0; i < allColumnsToDisplay.size(); i++)
//        {
//            for (int j = 0; j < allColumnsToDisplay.get(i).size(); j++) {
//                stringToExecute=stringToExecute+"\""+tablesNames.get(i)+"\".\""+allColumnsToDisplay.get(i).get(j)+"\",";
//            }
//        }
//        stringToExecute=stringToExecute.substring(0,stringToExecute.length()-1);
//        stringToExecute=stringToExecute+" from \""+tableName+"\" JOIN \""+otherTableName+"\" ON \""+tableName+"\".\""+thisFK+"\" = \""+otherTableName+"\".\""+otherPK+"\"";
//        stringToExecute=stringToExecute+whereClause;
//        return stringToExecute;
//    }
    @SneakyThrows

    private String generateJoinMultipleByQuery(ArrayList<ArrayList<String>> allColumnsToDisplay,
                                               ArrayList<String> allNeededFKS,
                                               ArrayList<String> otherTablesNames,
                                               ArrayList<String> allNeededPKS,
                                               String whereClause)
    {
        //        SELECT "Airline_Companies"."Id","Airline_Companies"."Name","Airline_Companies"."Country_Id" from "Airline_Companies"
        //        join "Users" on "Airline_Companies"."User_id"="Users"."Id"
        otherTablesNames.add(0,tableName);
        String stringToExecute = "SELECT ";
        for (int i = 0; i < allColumnsToDisplay.size(); i++)
        {
            for (int j = 0; j < allColumnsToDisplay.get(i).size(); j++) {
                stringToExecute=stringToExecute+"\""+otherTablesNames.get(i)+"\".\""+allColumnsToDisplay.get(i).get(j)+"\",";
            }
        }
        stringToExecute=stringToExecute.substring(0,stringToExecute.length()-1);
        stringToExecute=stringToExecute+" from \""+otherTablesNames.get(0)+"\"";
        for (int i = 0; i < otherTablesNames.size()-1; i++)
        {
            stringToExecute=stringToExecute+" JOIN \""+otherTablesNames.get(i+1)+"\" ON \""
                    +otherTablesNames.get(i)+"\".\""+allNeededFKS.get(i)+"\" = \""+otherTablesNames.get(i+1)+"\".\""+allNeededPKS.get(i)+"\" ";
        }
        stringToExecute=stringToExecute+whereClause;
        return stringToExecute;
    }

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
    /*
    function joinTwoBy, and its brothers in name, joins the current type of the DAO with another table.
    allColumnsToDisplay should be ordered in such order: first array index is an array of columns of this table that you want to select
    the next array index is an array of columns of the other table that you want to select
    To work properly it needs an entity that supports the join with the correct number of columns and types, that entity should be transferred typeOfEntity.
    */
    public<V extends IEntities> ArrayList<V> joinTwoBy(ArrayList<ArrayList<String>> allColumnsToDisplay,
                                                       String thisFK,
                                                       String otherTableName,
                                                       String otherPK,
                                                       V typeOfEntity)
    {
        return joinTwoByWithWhereClause(allColumnsToDisplay,thisFK,otherTableName,otherPK,typeOfEntity,"");
    }

    public ResultSet joinTwoByGetResultSet(ArrayList<ArrayList<String>> allColumnsToDisplay,String thisFK,String otherTableName,String otherPK)
    {
        return joinTwoByWithWhereClauseGetResultSet(allColumnsToDisplay,thisFK,otherTableName,otherPK,"");
    }

    public <V extends IEntities> ArrayList<V> joinTwoByWithWhereClause(ArrayList<ArrayList<String>> allColumnsToDisplay,
                                                                       String thisFK,String otherTableName,
                                                                       String otherPK,
                                                                       V typeOfEntity,
                                                                       String whereClause)
    {
        ArrayList<String> foreignKey = new ArrayList<>();
        ArrayList<String> otherTableNameArr = new ArrayList<>();
        ArrayList<String> primaryKey = new ArrayList<>();
        foreignKey.add(thisFK);
        otherTableNameArr.add(otherTableName);
        primaryKey.add(otherPK);
        String stringToExecute = generateJoinMultipleByQuery(allColumnsToDisplay,foreignKey,otherTableNameArr,primaryKey,whereClause);
        return executeQueryAndSaveInTheProperEntity(stringToExecute,typeOfEntity);
    }

    @SneakyThrows
    /*the caller needs to close the ResultSet connection */
    public ResultSet joinTwoByWithWhereClauseGetResultSet(ArrayList<ArrayList<String>> allColumnsToDisplay,
                                                          String thisFK,
                                                          String otherTableName,
                                                          String otherPK,
                                                          String whereClause)
    {
        ArrayList<String> foreignKey = new ArrayList<>();
        ArrayList<String> otherTableNameArr = new ArrayList<>();
        ArrayList<String> primaryKey = new ArrayList<>();
        foreignKey.add(thisFK);
        otherTableNameArr.add(otherTableName);
        primaryKey.add(otherPK);
        String stringToExecute = generateJoinMultipleByQuery(allColumnsToDisplay,foreignKey,otherTableNameArr,primaryKey,whereClause);
        return stm.executeQuery(stringToExecute);
    }

    /*
    function joinMultipleBy, and its brothers in name, joins the current type of the DAO with other tables.
    allColumnsToDisplay should be ordered in such order: first array index is an array of columns of this table that you want to select
    the next array index is an array of columns of the first table in otherTablesNames array  that you want to select and so on
    To work properly it needs an entity that supports the join with the correct number of columns and types, that entity should be transferred typeOfEntity.
    */
    public <V extends IEntities> ArrayList<V> joinMultipleBy(ArrayList<ArrayList<String>> allColumnsToDisplay,
                                                             ArrayList<String> allNeededFKS,
                                                             ArrayList<String> otherTablesNames,
                                                             ArrayList<String> allNeededPKS,
                                                             V typeOfEntity)
    {
        return joinMultipleByWithWhereClause(allColumnsToDisplay,allNeededFKS,otherTablesNames,allNeededPKS,typeOfEntity,"");
    }

    public ResultSet joinMultipleByGetResultSet(ArrayList<ArrayList<String>> allColumnsToDisplay,
                                                ArrayList<String> allNeededFKS,
                                                ArrayList<String> otherTablesNames,
                                                ArrayList<String> allNeededPKS)
    {
        return joinMultipleByWithWhereClauseGetResultSet(allColumnsToDisplay,allNeededFKS,otherTablesNames,allNeededPKS,"");
    }

    public <V extends IEntities> ArrayList<V> joinMultipleByWithWhereClause(ArrayList<ArrayList<String>> allColumnsToDisplay,
                                                                            ArrayList<String> allNeededFKS,
                                                                            ArrayList<String> otherTablesNames,
                                                                            ArrayList<String> allNeededPKS,
                                                                            V typeOfEntity,
                                                                            String whereClause)
    {
        String query = generateJoinMultipleByQuery(allColumnsToDisplay,allNeededFKS,otherTablesNames,allNeededPKS,whereClause);
        return executeQueryAndSaveInTheProperEntity(query,typeOfEntity);
    }

    @SneakyThrows
    /*the caller needs to close the ResultSet connection */
    public ResultSet joinMultipleByWithWhereClauseGetResultSet(ArrayList<ArrayList<String>> allColumnsToDisplay,
                                                               ArrayList<String> allNeededFKS,
                                                               ArrayList<String> otherTablesNames,
                                                               ArrayList<String> allNeededPKS,
                                                               String whereClause)
    {
        String query = generateJoinMultipleByQuery(allColumnsToDisplay,allNeededFKS,otherTablesNames,allNeededPKS,whereClause);
        return stm.executeQuery(query);
    }

    @SneakyThrows
    public void closeAllDAOConnections()
    {
        connection.close();
        stm.close();
    }
}
