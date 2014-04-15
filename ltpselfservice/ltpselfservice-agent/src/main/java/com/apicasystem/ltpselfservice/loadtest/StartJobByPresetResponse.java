/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice.loadtest;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author andras.nemes
 */
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
