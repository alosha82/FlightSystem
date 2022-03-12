package entities;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

@Setter
@Getter
public class AirlineCompanies implements IEntities
{
    private long id;
    private String name;
    private int countryId;
    private long userId;
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
        getterArray.add(getName());
        getterArray.add(""+getCountryId());
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
