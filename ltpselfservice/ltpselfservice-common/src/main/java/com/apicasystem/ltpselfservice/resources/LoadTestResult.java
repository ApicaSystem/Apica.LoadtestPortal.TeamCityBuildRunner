/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice.resources;

/**
 *
 * @author andras.nemes
 */
public enum LoadTestResult
{
    aborted("Aborted"), unstable("Unstable"), failed("Failed"), error("Error");

    private String description;
    
    private LoadTestResult(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    
    public String getId()
    {
        return name();
    }

    public String getDisplayName()
    {
        return StringUtils.toInitialCase(name());
    }
    
    @Override
    public String toString()
    {
        return this.description;
    }
}
