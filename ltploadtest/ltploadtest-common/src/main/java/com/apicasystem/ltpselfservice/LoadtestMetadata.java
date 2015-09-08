package com.apicasystem.ltpselfservice;

public class LoadtestMetadata
{

    private String apiToken;
    private int presetTestInstanceId;
    private String environmentType;
    
    public String getEnvironmentType()
    {
        return environmentType;
    }

    public void setEnvironmentType(String environmentType)
    {
        this.environmentType = environmentType;
    }

    public String getApiToken()
    {
        return apiToken;
    }

    public void setApiToken(String apiToken)
    {
        this.apiToken = apiToken;
    }

    public int getPresetTestInstanceId()
    {
        return presetTestInstanceId;
    }

    public void setPresetTestInstanceId(int presetTestInstanceId)
    {
        this.presetTestInstanceId = presetTestInstanceId;
    }
}
