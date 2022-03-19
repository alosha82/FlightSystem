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
    private Long id;
    private String firstName;
    private String LastName;
    private String address;
    private String phoneNumber;
    private String creditCardNumber;
    private Long userId;
    private ArrayList<String> columnNames=new ArrayList<>();

    public Customers()
    {
        columnNames.add("Id");
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
        if(!columnNames.contains("First_Name"))
            columnNames.add("First_Name");
    }

    public void setLastName(String lastName)
    {
        LastName = lastName;
        if(!columnNames.contains("Last_Name"))
            columnNames.add("Last_Name");
    }

    public void setAddress(String address)
    {
        this.address = address;
        if(!columnNames.contains("Address"))
            columnNames.add("Address");
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
        if(!columnNames.contains("Phone_No"))
            columnNames.add("Phone_No");
    }

    public void setCreditCardNumber(String creditCardNumber)
    {
        this.creditCardNumber = creditCardNumber;
        if(!columnNames.contains("Credit_Card_No"))
            columnNames.add("Credit_Card_No");
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
        if(!columnNames.contains("User_Id"))
            columnNames.add("User_Id");
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
        setFirstName("\'"+result.getString(columnNames.get(i++))+"\'");
        setLastName("\'"+result.getString(columnNames.get(i++))+"\'");
        setAddress("\'"+result.getString(columnNames.get(i++))+"\'");
        setPhoneNumber("\'"+result.getString(columnNames.get(i++))+"\'");
        setCreditCardNumber("\'"+result.getString(columnNames.get(i++))+"\'");
        setUserId(result.getLong(columnNames.get(i++)));
    }

    public ArrayList<String> getAllExceptIdInStringFormat()
    {
        ArrayList<String> getterArray = new ArrayList<>();
        if(columnNames.contains("First_Name"))
            getterArray.add(getFirstName());
        if(columnNames.contains("Last_Name"))
            getterArray.add(getLastName());
        if(columnNames.contains("Address"))
            getterArray.add(getAddress());
        if(columnNames.contains("Phone_No"))
            getterArray.add(getPhoneNumber());
        if(columnNames.contains("Credit_Card_No"))
            getterArray.add(getCreditCardNumber());
        if(columnNames.contains("User_Id"))
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
