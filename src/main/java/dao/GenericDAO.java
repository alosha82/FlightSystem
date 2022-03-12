package dao;

import entities.*;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GenericDAO<T extends IEntities>
{
    //Todo input the name of the database in sql after I create it in sql
    private String url="database name";
    private String tableName;
    private T entityType;
    private List<T> arrayOfEntityType;
    private PostgresConnection postgresConnection =new PostgresConnection();
    private Connection connection = postgresConnection.getConnection(url,"1");
    private Statement stm = postgresConnection.getStatement();

    public GenericDAO(String tableName, T tableType) {
        this.tableName=tableName;
        this.entityType = tableType;
        this.arrayOfEntityType = new ArrayList<>();
        getAllT(tableName);
    }

    @SneakyThrows
    public List<T> getAllT(String tableName)
    {
        ResultSet result= stm.executeQuery("select * from "+tableName);
        while(result.next())
        {
            entityType.setAll(result);
        }
        result.close();
        return arrayOfEntityType;
    }
    @SneakyThrows
    public T getAdministratorById(int id)
    {
        ResultSet result= stm.executeQuery("select * from "+tableName+" WHERE id="+id);
        //needed because it starts on wrong column
        result.next();
        entityType.setAll(result);
        result.close();
        return entityType;
    }
    @SneakyThrows
    public void removeAdministrator(int id)
    {
        stm.executeUpdate("DELETE from "+tableName+" WHERE id="+id);
        System.out.println("done");
    }
    @SneakyThrows
    public void addAdministrator(Administrators administrator)
    {
        ArrayList<String> fieldsInStringForm = administrator.getAllExceptIdInStringFormat();
        ArrayList<String> columnNames = administrator.getColumnNames();
        //INSERT INTO Administrators (first_name,last_name,user_id) VALUES (
        String stringForExecution = "INSERT INTO "+tableName+" (";
        for (int i = 0; i < columnNames.size(); i++)
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
    public void updateAdministrator(Administrators administrator,int id)
    {
        ArrayList<String> columnNames = administrator.getColumnNames();
        ArrayList<String> fieldsInStringForm = administrator.getAllExceptIdInStringFormat();
        String stringForUpdate ="UPDATE "+tableName+" SET ";
        for (int i = 0; i < fieldsInStringForm.size(); i++)
        {
            stringForUpdate=stringForUpdate + columnNames.get(i+1)+"="+fieldsInStringForm.get(i)+",";
        }
        stringForUpdate = stringForUpdate.substring(0,stringForUpdate.length()-1);
        stm.executeUpdate(stringForUpdate+" WHERE id="+id);
    }
}
