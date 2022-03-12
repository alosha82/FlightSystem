package entities;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

@Getter
@Setter
public class Flights implements IEntities
{
    private long id;
    private long AirlineCompanyId;
    private int originCountryId;
    private int destinationCountryId;
    //Todo Subject to change
    private String departureTime;
    //Todo Subject to change
    private String landingTime;
    private int remainingTickets;
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
        setAirlineCompanyId(result.getLong(columnNames.get(i++)));
        setOriginCountryId(result.getInt(columnNames.get(i++)));
        setDestinationCountryId(result.getInt(columnNames.get(i++)));
        setDepartureTime("\'"+result.getString(columnNames.get(i++))+"\'");
        setLandingTime("\'"+result.getString(columnNames.get(i++))+"\'");
        setRemainingTickets(result.getInt(columnNames.get(i++)));
        this.columnNames=columnNames;
    }

    public ArrayList<String> getAllExceptIdInStringFormat()
    {
        ArrayList<String> getterArray = new ArrayList<>();
        getterArray.add(""+getAirlineCompanyId());
        getterArray.add(""+getOriginCountryId());
        getterArray.add(""+getDestinationCountryId());
        getterArray.add(getDepartureTime());
        getterArray.add(getLandingTime());
        getterArray.add(""+getRemainingTickets());
        return getterArray;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", AirlineCompanyId=" + AirlineCompanyId +
                ", originCountryId=" + originCountryId +
                ", destinationCountryId=" + destinationCountryId +
                ", departureDTime='" + departureTime + '\'' +
                ", arrivalTime='" + landingTime + '\'' +
                ", remainingTickets=" + remainingTickets +"\n"
                ;
    }
}
