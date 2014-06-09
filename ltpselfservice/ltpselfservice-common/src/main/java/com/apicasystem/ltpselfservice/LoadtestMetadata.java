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
public class LoadtestMetadata
{
    private String apiToken;
    private int presetTestInstanceId;

    public String getApiToken()
    {
        return apiToken;
    }

    public void setApiToken(String apiToken)
    {
        this.apiToken = apiToken;
    }

    public int getPresetTestInstanceId()
    {
        return presetTestInstanceId;
    }

    public void setPresetTestInstanceId(int presetTestInstanceId)
    {
        this.presetTestInstanceId = presetTestInstanceId;
    }
}
