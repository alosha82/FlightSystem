package entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

@Getter
@EqualsAndHashCode
public class AirlineCompanies implements IEntities
{
    private Long id;
    private String name;
    private Integer countryId;
    private Long userId;
    private HashSet<String> columnNames=new HashSet<>();

    public AirlineCompanies()
    {
        columnNames.add("Id");
    }

    public AirlineCompanies(AirlineCompanies airlineCompany)
    {
        columnNames.add("Id");
        if(airlineCompany.getId()!=null)
            setId(airlineCompany.getId());
        if(airlineCompany.getName()!=null)
            setName(airlineCompany.getName());
        if(airlineCompany.getCountryId()!=null)
            setCountryId(airlineCompany.getCountryId());
        if(airlineCompany.getUserId()!=null)
            setUserId(airlineCompany.getUserId());
    }


    public void setId(long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        if(name != null)
        {
            name=name.replace("\'","");
            this.name = "\'"+name+"\'";
        }
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
        for (int i = 1; i <=rsmd.getColumnCount(); i++)
        {
            columnNames.add(rsmd.getColumnName(i));
        }
        int i=0;
        setId(result.getLong(columnNames.get(i++)));
        setName(result.getString(columnNames.get(i++)));
        setCountryId(result.getInt(columnNames.get(i++)));
        setUserId(result.getLong(columnNames.get(i++)));
    }
    /**Returns list of values that were set in string format.
     *columnNames initiated with ia Id column as a placeholder*/
    public LinkedHashMap<String,String> getAllNeededValuesExceptIdInStringFormat()
    {
        LinkedHashMap<String,String> getterArray = new LinkedHashMap<>();
        if (columnNames.contains("Name"))
            getterArray.put("Name",getName());
        if (columnNames.contains("Country_Id"))
            getterArray.put("Country_Id",""+getCountryId());
        if (columnNames.contains("User_Id"))
            getterArray.put("User_Id",""+getUserId());
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
