package doa;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresConnection
{
    private Connection connection=null;
    private Statement statement=null;

    public PostgresConnection() {

    }
    @SneakyThrows
    public Connection getConnection(String dataBaseName,String password) {
        Class.forName("org.postgresql.Driver");
        connection= DriverManager.getConnection
                    ("jdbc:postgresql://localhost:5432/"+dataBaseName,"postgres",password);//db db3 sql sql3
        System.out.println("Connection to postgres database named "+dataBaseName+" established.");
        return this.connection;
    }

    public Statement getStatement() {
        try {
            statement=this.connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }
}
