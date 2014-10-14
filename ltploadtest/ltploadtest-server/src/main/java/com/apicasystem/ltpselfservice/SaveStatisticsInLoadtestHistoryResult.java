package com.apicasystem.ltpselfservice;

public class SaveStatisticsInLoadtestHistoryResult
{

    private boolean saved;
    private boolean recordAlreadyExists;
    private String exception;

    public boolean isSaved()
    {
        return saved;
    }

    public void setSaved(boolean saved)
    {
        this.saved = saved;
    }

    public boolean isRecordAlreadyExists()
    {
        return recordAlreadyExists;
    }

    public void setRecordAlreadyExists(boolean recordAlreadyExists)
    {
        this.recordAlreadyExists = recordAlreadyExists;
    }

    public String getException()
    {
        return exception;
    }

    public void setException(String exception)
    {
        this.exception = exception;
    }
}
