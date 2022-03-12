package entities;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

@Getter
@Setter
public class Customers implements IEntities
{
    private long id;
    private String firstName;
    private String LastName;
    private String address;
    private String phoneNumber;
    private String creditCardNumber;
    private long userId;
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
        setFirstName("\'"+result.getString(columnNames.get(i++))+"\'");
        setLastName("\'"+result.getString(columnNames.get(i++))+"\'");
        setAddress("\'"+result.getString(columnNames.get(i++))+"\'");
        setPhoneNumber("\'"+result.getString(columnNames.get(i++))+"\'");
        setCreditCardNumber("\'"+result.getString(columnNames.get(i++))+"\'");
        setUserId(result.getLong(columnNames.get(i++)));
        this.columnNames=columnNames;
    }

    public ArrayList<String> getAllExceptIdInStringFormat()
    {
        ArrayList<String> getterArray = new ArrayList<>();
        getterArray.add(getFirstName());
        getterArray.add(getLastName());
        getterArray.add(getAddress());
        getterArray.add(getPhoneNumber());
        getterArray.add(getCreditCardNumber());
        getterArray.add(""+getUserId());
        return getterArray;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", creditCardNumber='" + creditCardNumber + '\'' +
                ", userId=" + userId +"\n"
                ;
    }
}
