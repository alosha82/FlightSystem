package entities;

import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;


@Getter
public class Tickets implements IEntities
{
    private Long id;
    private Long flightId;
    private Long costumersId;
    private ArrayList<String> columnNames=new ArrayList<>();

    public Tickets()
    {
        columnNames.add("Id");
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFlightId(long flightId)
    {
        this.flightId = flightId;
        if(!columnNames.contains("Flight_Id"))
            columnNames.add("Flight_Id");
    }

    public void setCostumersId(long costumersId)
    {
        this.costumersId = costumersId;
        if(!columnNames.contains("Costumers_Id"))
            columnNames.add("Costumers_Id");
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
        setFlightId(result.getLong(columnNames.get(i++)));
        setCostumersId(result.getLong(columnNames.get(i++)));
    }

    /**Returns list of values that were set in string format.
     *columnNames initiated with ia Id column as a placeholder*/
    public ArrayList<String> getAllNeededValuesExceptIdInStringFormat()
    {
        ArrayList<String> getterArray = new ArrayList<>();
        if(columnNames.contains("Flight_Id"))
            getterArray.add(""+getFlightId());
        if(columnNames.contains("Costumers_Id"))
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
