package com.apicasystem.ltpselfservice.loadtest;

import com.google.gson.annotations.SerializedName;

public class TransmitJobPresetArgs
{

    @SerializedName("PresetName")
    private String presetName;
    @SerializedName("RunnableFileName")
    private String runnableFileName;

    public TransmitJobPresetArgs(String presetName, String runnableFileName)
    {
        this.presetName = presetName;
        this.runnableFileName = runnableFileName;
    }

    public String getPresetName()
    {
        return presetName;
    }

    public void setPresetName(String presetName)
    {
        this.presetName = presetName;
    }

    public String getRunnableFileName()
    {
        return runnableFileName;
    }

    public void setRunnableFileName(String runnableFileName)
    {
        this.runnableFileName = runnableFileName;
    }
}
