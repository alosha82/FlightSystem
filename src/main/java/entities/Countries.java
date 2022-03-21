package entities;

import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

@Getter
public class Countries implements IEntities
{
    private Integer id;
    private String name;
    private ArrayList<String> columnNames=new ArrayList<>();

    public Countries()
    {
        columnNames.add("Id");
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
        if (!columnNames.contains("Name"))
            columnNames.add("Name");
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
        setName("\'"+result.getString(columnNames.get(i++))+"\'");
    }

    /**Returns list of values that were set in string format.
     *columnNames initiated with ia Id column as a placeholder*/
    public ArrayList<String> getAllNeededValuesExceptIdInStringFormat()
    {
        ArrayList<String> getterArray = new ArrayList<>();
        if (columnNames.contains("Name"))
            getterArray.add(getName());
        return getterArray;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + "\'"+"\n"
                ;
    }
}
