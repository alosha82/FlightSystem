package entities;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

@Getter
@Setter
public class UserRoles implements IEntities
{
    private Integer id;
    private String roleName;
    private ArrayList<String> columnNames=new ArrayList<>();

    public UserRoles()
    {
        columnNames.add("Id");
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
        if(!columnNames.contains("Role_Name"))
            columnNames.add("Role_Name");
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
        setRoleName("\'"+result.getString(columnNames.get(i++))+"\'");
    }

    public ArrayList<String> getAllExceptIdInStringFormat()
    {
        ArrayList<String> getterArray = new ArrayList<>();
        if(columnNames.contains("Role_Name"))
            getterArray.add(getRoleName());
        return getterArray;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", roleName='" + roleName + '\'' +"\n"
                ;
    }
}
