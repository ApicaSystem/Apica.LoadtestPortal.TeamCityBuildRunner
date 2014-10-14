package com.apicasystem.ltpselfservice;

import com.google.gson.annotations.SerializedName;

public class PresetResponse
{

    @SerializedName("presetexists")
    private boolean presetExists;
    @SerializedName("exception")
    private String exception;
    @SerializedName("testinstanceid")
    private int testInstanceId;

    public int getTestInstanceId()
    {
        return testInstanceId;
    }

    public void setTestInstanceId(int testInstanceId)
    {
        this.testInstanceId = testInstanceId;
    }

    public boolean isPresetExists()
    {
        return presetExists;
    }

    public void setPresetExists(boolean presetExists)
    {
        this.presetExists = presetExists;
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
