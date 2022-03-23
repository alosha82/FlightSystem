package entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

@Getter
@Setter
@EqualsAndHashCode
public class UserRoles implements IEntities
{
    private Integer id;
    private String roleName;
    private ArrayList<String> columnNames=new ArrayList<>();

    public UserRoles()
    {
        columnNames.add("Id");
    }

    public UserRoles(UserRoles userRole) {
        columnNames.add("Id");
        if(userRole.getId()!=null)
            setId(userRole.getId());
        if(userRole.getRoleName()!=null)
            setRoleName(userRole.getRoleName());
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoleName(String roleName)
    {
        if(roleName != null)
        {
            roleName=roleName.replace("\'","");
            this.roleName = "\'"+roleName+"\'";
        }
        if(!columnNames.contains("Role_Name"))
            columnNames.add("Role_Name");
    }

    @SneakyThrows
    public void setAll(ResultSet result)
    {
        ResultSetMetaData rsmd = result.getMetaData();
        ArrayList<String> columnNames = new ArrayList<>();
        for (int i = 1; i <=rsmd.getColumnCount(); i++)
        {
            columnNames.add(rsmd.getColumnName(i));
        }
        int i=0;
        setId(result.getInt(columnNames.get(i++)));
        setRoleName(result.getString(columnNames.get(i++)));
    }

    /**Returns list of values that were set in string format.
     *columnNames initiated with ia Id column as a placeholder*/
    public ArrayList<String> getAllNeededValuesExceptIdInStringFormat()
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
