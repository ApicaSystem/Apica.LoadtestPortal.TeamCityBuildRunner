package com.apicasystem.ltpselfservice.loadtest;

import com.google.gson.annotations.SerializedName;

public class StartJobByPresetResponse
{

    @SerializedName("exception")
    private String exception;
    @SerializedName("jobid")
    private int jobId;

    public String getException()
    {
        return exception;
    }

    public void setException(String exception)
    {
        this.exception = exception;
    }

    public int getJobId()
    {
        return jobId;
    }

    public void setJobId(int jobId)
    {
        this.jobId = jobId;
    }
}
