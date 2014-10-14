package com.apicasystem.ltpselfservice;

import com.google.gson.annotations.SerializedName;

public class RunnableFileResponse
{

    @SerializedName("fileexists")
    private boolean fileExists;
    @SerializedName("exception")
    private String exception;

    public boolean isFileExists()
    {
        return fileExists;
    }

    public void setFileExists(boolean fileExists)
    {
        this.fileExists = fileExists;
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
