package entities;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

@Setter
@Getter
public class Tickets implements IEntities
{
    private long id;
    private long flightId;
    private long costumersId;
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
        setFlightId(result.getLong(columnNames.get(i++)));
        setCostumersId(result.getLong(columnNames.get(i++)));
        this.columnNames=columnNames;
    }

    public ArrayList<String> getAllExceptIdInStringFormat()
    {
        ArrayList<String> getterArray = new ArrayList<>();
        getterArray.add(""+getFlightId());
        getterArray.add(""+getCostumersId());
        return getterArray;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", flightId=" + flightId +
                ", costumersId=" + costumersId +"\n"
                ;
    }
}
