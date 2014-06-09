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
public class LoadtestMetadataReadResult
{
    private LoadtestMetadata metadata;
    private boolean loadSuccess;
    private String loadFailureReason;
    private String pureJsonContent;

    public String getPureJsonContent()
    {
        return pureJsonContent;
    }

    public void setPureJsonContent(String pureJsonContent)
    {
        this.pureJsonContent = pureJsonContent;
    }
    
    public LoadtestMetadata getMetadata()
    {
        return metadata;
    }

    public void setMetadata(LoadtestMetadata metadata)
    {
        this.metadata = metadata;
    }

    public boolean isLoadSuccess()
    {
        return loadSuccess;
    }

    public void setLoadSuccess(boolean loadSuccess)
    {
        this.loadSuccess = loadSuccess;
    }

    public String getLoadFailureReason()
    {
        return loadFailureReason;
    }

    public void setLoadFailureReason(String loadFailureReason)
    {
        this.loadFailureReason = loadFailureReason;
    }    
}
