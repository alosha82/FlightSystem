package entities;

import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.ArrayList;

@Getter
public class Flights implements IEntities
{
    private Long id;
    private Long AirlineCompanyId;
    private Integer originCountryId;
    private Integer destinationCountryId;
    //Todo Subject to change
    private Timestamp departureTime;
    //Todo Subject to change
    private Timestamp landingTime;
    private Integer remainingTickets;
    private ArrayList<String> columnNames;

    public Flights()
    {
        columnNames.add("Id");
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAirlineCompanyId(Long airlineCompanyId) {
        AirlineCompanyId = airlineCompanyId;
        if(!columnNames.contains("Airline_Company_Id"))
            columnNames.add("Airline_Company_Id");
    }

    public void setOriginCountryId(Integer originCountryId)
    {
        this.originCountryId = originCountryId;
        if(!columnNames.contains("Origin_Country_Id"))
            columnNames.add("Origin_Country_Id");
    }

    public void setDestinationCountryId(Integer destinationCountryId)
    {
        this.destinationCountryId = destinationCountryId;
        if(!columnNames.contains("Destination_Country_Id"))
            columnNames.add("Destination_Country_Id");
    }

    public void setDepartureTime(Timestamp departureTime)
    {
        this.departureTime = departureTime;
        if(!columnNames.contains("Departure_Time"))
            columnNames.add("Departure_Time");
    }

    public void setLandingTime(Timestamp landingTime)
    {
        this.landingTime = landingTime;
        if(!columnNames.contains("Landing_Time"))
            columnNames.add("Landing_Time");
    }

    public void setRemainingTickets(Integer remainingTickets)
    {
        this.remainingTickets = remainingTickets;
        if(!columnNames.contains("Remaining_Tickets"))
            columnNames.add("Remaining_Tickets");
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
        setAirlineCompanyId(result.getLong(columnNames.get(i++)));
        setOriginCountryId(result.getInt(columnNames.get(i++)));
        setDestinationCountryId(result.getInt(columnNames.get(i++)));
        setDepartureTime(result.getTimestamp(columnNames.get(i++)));
        setLandingTime(result.getTimestamp(columnNames.get(i++)));
        setRemainingTickets(result.getInt(columnNames.get(i++)));
    }

    public ArrayList<String> getAllExceptIdInStringFormat()
    {
        ArrayList<String> getterArray = new ArrayList<>();
        if(columnNames.contains("Airline_Company_Id"))
            getterArray.add(""+getAirlineCompanyId());
        if(columnNames.contains("Origin_Country_Id"))
            getterArray.add(""+getOriginCountryId());
        if(columnNames.contains("Destination_Country_Id"))
            getterArray.add(""+getDestinationCountryId());
        if(columnNames.contains("Departure_Time"))
            getterArray.add(""+getDepartureTime());
        if(columnNames.contains("Landing_Time"))
            getterArray.add(""+getLandingTime());
        if(columnNames.contains("Remaining_Tickets"))
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
