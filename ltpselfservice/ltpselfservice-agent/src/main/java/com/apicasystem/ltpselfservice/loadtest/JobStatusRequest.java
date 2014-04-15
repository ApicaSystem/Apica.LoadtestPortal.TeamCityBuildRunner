/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice.loadtest;

import java.net.URI;

/**
 *
 * @author andras.nemes
 */
public class JobStatusRequest
{
    private URI ltpApiEndpoint;
    private String username; 
    private String password;
    private int jobId;

    public URI getLtpApiEndpoint()
    {
        return ltpApiEndpoint;
    }

    public void setLtpApiEndpoint(URI ltpApiEndpoint)
    {
        this.ltpApiEndpoint = ltpApiEndpoint;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
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
