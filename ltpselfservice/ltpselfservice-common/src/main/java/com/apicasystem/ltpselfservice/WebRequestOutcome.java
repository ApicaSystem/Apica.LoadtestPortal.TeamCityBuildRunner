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
public class WebRequestOutcome
{
    private int httpResponseCode;
    private String rawResponseContent;
    private String exceptionMessage;
    private boolean webRequestSuccessful;

    public boolean isWebRequestSuccessful()
    {
        return webRequestSuccessful;
    }

    public void setWebRequestSuccessful(boolean webRequestSuccessful)
    {
        this.webRequestSuccessful = webRequestSuccessful;
    }

    public String getExceptionMessage()
    {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage)
    {
        this.exceptionMessage = exceptionMessage;
    }
    
    public int getHttpResponseCode()
    {
        return httpResponseCode;
    }

    public void setHttpResponseCode(int httpResponseCode)
    {
        this.httpResponseCode = httpResponseCode;
    }

    public String getRawResponseContent()
    {
        return rawResponseContent;
    }

    public void setRawResponseContent(String rawResponseContent)
    {
        this.rawResponseContent = rawResponseContent;
    }
}
