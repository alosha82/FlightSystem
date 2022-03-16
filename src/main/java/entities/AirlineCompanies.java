package entities;

import lombok.Getter;

import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

@Getter
public class AirlineCompanies implements IEntities
{
    private Long id;
    private String name;
    private Integer countryId;
    private Long userId;
    private ArrayList<String> columnNames;

    public AirlineCompanies()
    {
        columnNames.add("Id");
    }

    private void setId(long id)
    {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
        if (!columnNames.contains("Name"))
            columnNames.add("Name");
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
        if (!columnNames.contains("Country_Id"))
            columnNames.add("Country_Id");
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
        for (int i = 0; i < rsmd.getColumnCount(); i++)
        {
            columnNames.add(rsmd.getColumnName(i));
        }
        int i=0;
        setId(result.getLong(columnNames.get(i++)));
        setName("\'"+result.getString(columnNames.get(i++))+"\'");
        setCountryId(result.getInt(columnNames.get(i++)));
        setUserId(result.getLong(columnNames.get(i++)));
    }
    public ArrayList<String> getAllExceptIdInStringFormat()
    {
        ArrayList<String> getterArray = new ArrayList<>();
        if (columnNames.contains("Name"))
            getterArray.add(getName());
        if (columnNames.contains("Country_Id"))
            getterArray.add(""+getCountryId());
        if (columnNames.contains("User_Id"))
            getterArray.add(""+getUserId());
        return getterArray;
    }
    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + '\'' +
                ", countryId=" + countryId +
                ", userId=" + userId + "\n"
                ;
    }
}
