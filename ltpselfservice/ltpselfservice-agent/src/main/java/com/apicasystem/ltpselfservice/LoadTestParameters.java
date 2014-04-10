/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice;

import java.util.Map;

/**
 *
 * @author andras.nemes
 */
public class LoadTestParameters
{
    private final Map<String, String> parameters;
    private static final String NULL_String = null;

    public LoadTestParameters(Map<String, String> parameters)
    {
        this.parameters = parameters;
    }

    public String get(String key, String defaultValue)
    {
        return (String) this.parameters.get(key);
    }

    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder(10000);
        buf.append(String.format("--- Load Test Parameters ---%n", new Object[0]));
        for (Map.Entry<String, String> e : this.parameters.entrySet())
        {
            buf.append(String.format("  %s=%s%n", new Object[]
            {
                e.getKey(), e.getValue()
            }));
        }
        buf.append(String.format("--- END ---%n", new Object[0]));
        return buf.toString();
    }
    
    
}
