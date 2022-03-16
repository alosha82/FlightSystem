//package dao;
//
//import entities.Administrators;
//import lombok.SneakyThrows;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.ArrayList;
//
//public class AdministratorsDOA
//{
//    //Todo input the name of the database in sql after I create it in sql
//    String url="database name";
//    private ArrayList<Administrators> administrators = new ArrayList<>();
//    PostgresConnection sqLiteConnection =new PostgresConnection();
//    Connection connection = sqLiteConnection.getConnection(url,"1");
//    Statement stm = sqLiteConnection.getStatement();
//    @SneakyThrows
//    public ArrayList<Administrators> getAllAdministrators()
//    {
//        ResultSet result= stm.executeQuery("select * from Administrators");
//        while(result.next())
//        {
//            Administrators administrator =new Administrators();
//            administrator.setId(result.getInt("id"));
//            administrator.setFirstName("\'"+result.getString("first_name")+"\'");
//            administrator.setLastName("\'"+result.getString("last_name")+"\'");
//            administrator.setUserId(result.getLong("user_id"));
//        }
//        result.close();
//        return administrators;
//    }
//    @SneakyThrows
//    public Administrators getAdministratorById(int id)
//    {
//        ResultSet result= stm.executeQuery("select * from Administrators WHERE id="+id);
//        //needed because it starts on wrong column
//        result.next();
//        Administrators administrator =new Administrators();
//        administrator.setId(result.getInt("id"));
//        administrator.setFirstName("\'"+result.getString("first_name")+"\'");
//        administrator.setLastName("\'"+result.getString("last_name")+"\'");
//        administrator.setUserId(result.getLong("user_id"));
//        result.close();
//        return administrator;
//    }
//    @SneakyThrows
//    public void removeAdministrator(int id)
//    {
//        stm.executeUpdate("DELETE from Administrators WHERE id="+id);
//        System.out.println("done");
//    }
//    @SneakyThrows
//    public void addAdministrator(Administrators administrator)
//    {
//        stm.executeUpdate("INSERT INTO Administrators (first_name,last_name,user_id) " +
//                "VALUES " +
//                "("+administrator.getFirstName()+","+administrator.getLastName()+","+administrator.getUserId()+")");
//    }
//    @SneakyThrows
//    public void updateAdministrator(Administrators administrator,int id)
//    {
//        String stringForUpdate = "UPDATE Administrators SET first_name="
//                +administrator.getFirstName()+",last_name="
//                +administrator.getLastName()+",user_id="
//                +administrator.getUserId()+" WHERE id="+id;
//        stm.executeUpdate(stringForUpdate);
//    }
//}
