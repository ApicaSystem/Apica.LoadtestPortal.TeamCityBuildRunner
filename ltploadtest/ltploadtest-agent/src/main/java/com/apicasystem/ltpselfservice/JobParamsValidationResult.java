package com.apicasystem.ltpselfservice;

public class JobParamsValidationResult
{

    private boolean allParamsPresent;
    private String exceptionMessage;

    public boolean isAllParamsPresent()
    {
        return allParamsPresent;
    }

    public void setAllParamsPresent(boolean allParamsPresent)
    {
        this.allParamsPresent = allParamsPresent;
    }

    public String getExceptionMessage()
    {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage)
    {
        this.exceptionMessage = exceptionMessage;
    }
}
