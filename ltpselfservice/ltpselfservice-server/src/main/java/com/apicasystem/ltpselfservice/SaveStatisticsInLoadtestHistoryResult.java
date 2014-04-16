/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice;

/**
 *
 * @author andras.nemes
 */
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
