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
public class JobParamsValidationResult
{
    private boolean allParamsPresent;
    private String exceptionMessage;

    public boolean isAllParamsPresent()
    {
        return allParamsPresent;
    }

    public void setAllParamsPresent(boolean allParamsPresent)
    {
        this.allParamsPresent = allParamsPresent;
    }

    public String getExceptionMessage()
    {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage)
    {
        this.exceptionMessage = exceptionMessage;
    }
}
