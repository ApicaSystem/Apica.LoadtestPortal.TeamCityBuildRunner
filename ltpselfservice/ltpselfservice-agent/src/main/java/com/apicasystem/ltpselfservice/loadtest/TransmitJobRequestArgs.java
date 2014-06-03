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
public class TransmitJobRequestArgs
{
    private URI ltpApiEndpoint;
    private String authToken;
    private String fileName;
    private String presetName;

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

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getPresetName()
    {
        return presetName;
    }

    public void setPresetName(String presetName)
    {
        this.presetName = presetName;
    }
}
