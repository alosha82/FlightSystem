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
public class Countries implements IEntities
{
    private Integer id;
    private String name;
    private HashSet<String> columnNames=new HashSet<>();

    public Countries()
    {
        columnNames.add("Id");
    }

    public Countries(Countries country)
    {
        columnNames.add("Id");
        if(country.getId()!=null)
            setId(Math.toIntExact(country.getId()));
        if(country.getName()!=null)
            setName(country.getName().replace("\'",""));
    }

    public Long getId() {
        return Long.parseLong(""+id);
    }

    public void setId(int id) {
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
        setName(result.getString(columnNames.get(i++)));
    }

    /**Returns list of values that were set in string format.
     *columnNames initiated with ia Id column as a placeholder*/
    public LinkedHashMap<String,String> getAllNeededValuesExceptIdInStringFormat()
    {
        LinkedHashMap<String,String> getterArray = new LinkedHashMap<>();
        if (columnNames.contains("Name"))
            getterArray.put("Name",getName());
        return getterArray;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + "\'"+"\n"
                ;
    }
}
