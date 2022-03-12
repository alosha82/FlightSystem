package doa;

import entities.AirlineCompanies;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AirlineCompaniesDOA
{
    //Todo input the name of the database in sql after I create it in sql
    String url="database name";
    private ArrayList<AirlineCompanies> airlineCompanies = new ArrayList<>();
    PostgresConnection sqLiteConnection =new PostgresConnection();
    Connection connection = sqLiteConnection.getConnection(url,"1");
    Statement stm = sqLiteConnection.getStatement();
    @SneakyThrows
    public ArrayList<AirlineCompanies> getAllAirlineCompanies()
    {
        ResultSet result= stm.executeQuery("select * from AirlineCompanies");
        while(result.next())
        {
            AirlineCompanies airlineCompany =new AirlineCompanies();
            airlineCompany.setId(result.getInt("id"));
            airlineCompany.setName("\'"+result.getString("name")+"\'");
            airlineCompany.setCountryId(result.getInt("country_id"));
            airlineCompany.setUserId(result.getLong("user_id"));
        }
        result.close();
        return airlineCompanies;
    }
    @SneakyThrows
    public AirlineCompanies getAirlineCompanyById(int id)
    {
        ResultSet result= stm.executeQuery("select * from AirlineCompanies WHERE id="+id);
        //needed because it starts on wrong column
        result.next();
        AirlineCompanies airlineCompany =new AirlineCompanies();
        airlineCompany.setId(result.getInt("id"));
        airlineCompany.setName("\'"+result.getString("name")+"\'");
        airlineCompany.setCountryId(result.getInt("country_id"));
        airlineCompany.setUserId(result.getLong("user_id"));
        result.close();
        return airlineCompany;
    }
    @SneakyThrows
    public void removeAirlineCompany(int id)
    {
        stm.executeUpdate("DELETE from AirlineCompanies WHERE id="+id);
        System.out.println("done");
    }
    @SneakyThrows
    public void addAirlineCompany(AirlineCompanies airlineCompany)
    {
        stm.executeUpdate("INSERT INTO AirlineCompanies (name,country_id,user_id) " +
                "VALUES " +
                "("+airlineCompany.getName()+","+airlineCompany.getCountryId()+","+airlineCompany.getUserId()+")");
    }
    @SneakyThrows
    public void updateAirlineCompany(AirlineCompanies airlineCompany,int id)
    {
        String stringForUpdate = "UPDATE AirlineCompanies SET name="
                +airlineCompany.getName()+",country_id="
                +airlineCompany.getCountryId()+",user_id="
                +airlineCompany.getUserId()+" WHERE id="+id;
        stm.executeUpdate(stringForUpdate);
    }
}
