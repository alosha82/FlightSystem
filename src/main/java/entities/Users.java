package entities;

import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

@Getter
public class Users implements IEntities
{
    private Long id;
    private String userName;
    private String password;
    private String email;
    private Integer userRole;
    private ArrayList<String> columnNames=new ArrayList<>();

    public Users()
    {
        columnNames.add("Id");
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
        if(!columnNames.contains("User_Name"))
            columnNames.add("User_Name");
    }

    public void setPassword(String password)
    {
        this.password = password;
        if(!columnNames.contains("Password"))
            columnNames.add("Password");
    }

    public void setEmail(String email)
    {
        this.email = email;
        if(!columnNames.contains("Email"))
            columnNames.add("Email");
    }

    public void setUserRole(int userRole)
    {
        this.userRole = userRole;
        if(!columnNames.contains("User_Role"))
            columnNames.add("User_Role");
    }

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

    /**Returns list of values that were set in string format.
     *columnNames initiated with ia Id column as a placeholder*/
    public ArrayList<String> getAllNeededValuesExceptIdInStringFormat()
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
                ", userName= " + userName +
                ", password= " + password +
                ", email= " + email +
                ", userRole= " + userRole +"\n"
                ;
    }
}
