package entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

@Getter
@EqualsAndHashCode
public class Administrators implements IEntities
{
    private Integer id;
    private String firstName;
    private String lastName;
    private Long userId;
    private ArrayList<String> columnNames=new ArrayList<>();

    public Administrators()
    {
        columnNames.add("Id");
    }

    public Administrators(Administrators administrators) {
        columnNames.add("Id");
        if(administrators.getId()!=null)
            setId(administrators.getId());
        if(administrators.getFirstName()!=null)
            setFirstName(administrators.getFirstName().replace("\'",""));
        if(administrators.getLastName()!=null)
            setLastName(administrators.getLastName().replace("\'",""));
        if(administrators.getUserId()!=null)
            setUserId(administrators.getUserId());
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setFirstName(String firstName)
    {
        if(firstName != null)
            this.firstName = "\'"+firstName+"\'";
        if (!columnNames.contains("First_Name"))
            columnNames.add("First_Name");
    }

    public void setLastName(String lastName)
    {
        if(lastName != null)
            this.lastName = "\'"+lastName+"\'";
        if (!columnNames.contains("Last_Name"))
            columnNames.add("Last_Name");
    }

    public void setUserId(long userId) {
        this.userId = userId;
        if (!columnNames.contains("User_Id"))
            columnNames.add("User_Id");
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
        setFirstName(result.getString(columnNames.get(i++)));
        setLastName(result.getString(columnNames.get(i++)));
        setUserId(result.getLong(columnNames.get(i++)));
    }
    /**Returns list of values that were set in string format.
     *columnNames initiated with ia Id column as a placeholder*/
    public ArrayList<String> getAllNeededValuesExceptIdInStringFormat()
    {
        ArrayList<String> getterArray = new ArrayList<>();
        if (columnNames.contains("First_Name"))
            getterArray.add(getFirstName());
        if (columnNames.contains("Last_Name"))
            getterArray.add(getLastName());
        if (columnNames.contains("User_Id"))
            getterArray.add(""+getUserId());
        return getterArray;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userId=" + userId +"\n"
                ;
    }
}
