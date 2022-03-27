package entities;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface IEntities
{
    public void setAll(ResultSet result);
    public LinkedHashMap<String,String> getAllNeededValuesExceptIdInStringFormat();
    public Long getId();
}
