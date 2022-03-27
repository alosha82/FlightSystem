package Exceptions;

public class PasswordSpacedException extends Exception
{
    public PasswordSpacedException(String errorMessage)
    {
        super(errorMessage);
    }
}
