package com.apicasystem.ltpselfservice.loadtest;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class JobStatusResponse
{

    @SerializedName("id")
    private int jobId;
    @SerializedName("message")
    private String statusMessage;
    @SerializedName("completed")
    private boolean completed;
    @SerializedName("error")
    private boolean jobFaulted;
    @SerializedName("exception")
    private String exception;

    public int getJobId()
    {
        return jobId;
    }

    public void setJobId(int jobId)
    {
        this.jobId = jobId;
    }

    public String getStatusMessage()
    {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted(boolean completed)
    {
        this.completed = completed;
    }

    public boolean isJobFaulted()
    {
        return jobFaulted;
    }

    public void setJobFaulted(boolean jobFaulted)
    {
        this.jobFaulted = jobFaulted;
    }

    public String getException()
    {
        return exception;
    }

    public void setException(String exception)
    {
        this.exception = exception;
    }

    public boolean isJobCompleted()
    {
        return jobFaulted || completed;
    }

    @Override
    public String toString()
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("--- Load Test message ---%n", new Object[0]));
            sb.append("Job id: ").append(getJobId()).append("\r\n");
            sb.append("Message date UTC: ").append(new Date()).append("\r\n");
            sb.append("Job message: ").append(getStatusMessage()).append("\r\n");;
            sb.append(String.format("--- END ---%n", new Object[0]));
            return sb.toString();
        } catch (Exception ex)
        {
            return "Could not read job status response properties.";
        }
    }
}
