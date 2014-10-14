package com.apicasystem.ltpselfservice.loadtest;

import java.net.URI;

public class JobStatusRequest
{

    private URI ltpApiEndpoint;
    private String authToken;
    private int jobId;

    public URI getLtpApiEndpoint()
    {
        return ltpApiEndpoint;
    }

    public void setLtpApiEndpoint(URI ltpApiEndpoint)
    {
        this.ltpApiEndpoint = ltpApiEndpoint;
    }

    public String getAuthToken()
    {
        return authToken;
    }

    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
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
