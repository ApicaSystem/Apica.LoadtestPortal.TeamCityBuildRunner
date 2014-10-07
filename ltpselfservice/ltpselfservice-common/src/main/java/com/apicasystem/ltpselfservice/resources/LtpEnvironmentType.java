/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice.resources;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andras.nemes
 */
public enum LtpEnvironmentType
{

    production, trial;

    public final String label;

    private LtpEnvironmentType()
    {
        if (name().equalsIgnoreCase("production"))
        {
            this.label = "Enterprise (loadtest.apicasystem.com)";
        } else if (name().equalsIgnoreCase("trial"))
        {
            this.label = "Trial (loadtest-trial.apicasystem.com)";
        }
        else
        {
            this.label = "Unknown";
        }
    }

    public static List<String> names()
    {
        LtpEnvironmentType[] environmentTypes = values();
        List<String> result = new ArrayList(environmentTypes.length);
        for (int i = 0; i < environmentTypes.length; i++)
        {
            result.add(environmentTypes[i].name());
        }
        return result;
    }

    public String getId()
    {
        return name();
    }

    public String getDisplayName()
    {
        return this.label;
    }
}
