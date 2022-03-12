package entities;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

@Getter
@Setter
public class Users implements IEntities
{
    private long id;
    private String userName;
    private String password;
    private String email;
    private int userRole;
    private ArrayList<String> columnNames;

    @SneakyThrows
    public void setAll(ResultSet result)
    {
        ResultSetMetaData rsmd = result.getMetaData();
        ArrayList<String> columnNames = new ArrayList<>();
        for (int i = 0; i < rsmd.getColumnCount(); i++)
        {
            columnNames.add(rsmd.getColumnName(i));
        }
        int i=0;
        setId(result.getInt(columnNames.get(i++)));
        setUserName("\'"+result.getString(columnNames.get(i++))+"\'");
        setPassword("\'"+result.getString(columnNames.get(i++))+"\'");
        setEmail("\'"+result.getString(columnNames.get(i++))+"\'");
        setUserRole(result.getInt(columnNames.get(i++)));
        this.columnNames=columnNames;
    }

    public ArrayList<String> getAllExceptIdInStringFormat()
    {
        ArrayList<String> getterArray = new ArrayList<>();
        getterArray.add(getUserName());
        getterArray.add(getPassword());
        getterArray.add(getEmail());
        getterArray.add(""+getUserRole());
        return getterArray;
    }


    @Override
    public String toString() {
        return "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", userRole=" + userRole +"\n"
                ;
    }
}
