package entities;

import java.sql.ResultSet;
import java.util.ArrayList;

public interface IEntities
{
    public void setAll(ResultSet result);
    public ArrayList<String> getAllNeededValuesExceptIdInStringFormat();
    public ArrayList<String> getColumnNames();
}
