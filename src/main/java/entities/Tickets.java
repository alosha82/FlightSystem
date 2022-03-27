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
public class Tickets implements IEntities
{
    private Long id;
    private Long flightId;
    private Long costumersId;
    private HashSet<String> columnNames=new HashSet<>();

    public Tickets()
    {
        columnNames.add("Id");
    }

    public Tickets(Tickets ticket) {
        columnNames.add("Id");
        if(ticket.getId()!=null)
            setId(ticket.getId());
        if(ticket.getFlightId()!=null)
            setFlightId(ticket.getFlightId());
        if(ticket.getCostumersId()!=null)
            setCostumersId(ticket.getCostumersId());
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
        if(!columnNames.contains("Customer_Id"))
            columnNames.add("Customer_Id");
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
        setFlightId(result.getLong(columnNames.get(i++)));
        setCostumersId(result.getLong(columnNames.get(i++)));
    }

    /**Returns list of values that were set in string format.
     *columnNames initiated with ia Id column as a placeholder*/
    public LinkedHashMap<String,String> getAllNeededValuesExceptIdInStringFormat()
    {
        LinkedHashMap<String,String> getterArray = new LinkedHashMap<>();
        if(columnNames.contains("Flight_Id"))
            getterArray.put("Flight_Id",""+getFlightId());
        if(columnNames.contains("Customer_Id"))
            getterArray.put("Customer_Id",""+getCostumersId());
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
