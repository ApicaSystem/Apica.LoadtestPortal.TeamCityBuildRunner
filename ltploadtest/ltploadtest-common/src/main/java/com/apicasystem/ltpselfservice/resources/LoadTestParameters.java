package com.apicasystem.ltpselfservice.resources;

import java.util.Map;

public class LoadTestParameters
{

    private final Map<String, String> parameters;
    private static final String NULL_String = null;

    public LoadTestParameters(Map<String, String> parameters)
    {
        this.parameters = parameters;
    }

    public int get(String key, int defaultValue)
    {
        try
        {
            return Integer.parseInt(get(key, NULL_String));
        } catch (NumberFormatException e)
        {
        }
        return defaultValue;
    }

    public boolean get(String key, boolean defaultValue)
    {
        try
        {
            return Boolean.parseBoolean(get(key, NULL_String));
        } catch (Exception e)
        {
        }
        return defaultValue;
    }

    public float get(String key, float defaultValue)
    {
        try
        {
            return Float.parseFloat(get(key, NULL_String));
        } catch (NumberFormatException e)
        {
        }
        return defaultValue;
    }

    public String get(String key, String defaultValue)
    {
        return (String) this.parameters.get(key);
    }

    public StandardMetricResult.Metrics get(String key, StandardMetricResult.Metrics defaultValue)
    {
        try
        {
            return StandardMetricResult.Metrics.valueOf(get(key, defaultValue.name()));
        } catch (Exception e)
        {
        }
        return defaultValue;
    }

    public LoadTestResult get(String key, LoadTestResult defaultValue)
    {
        return LoadTestResult.valueOf(get(key, defaultValue.name()));
    }

    public Operator get(String key, Operator defaultValue)
    {
        return Operator.valueOf(get(key, defaultValue.name()));
    }

    public boolean has(String key)
    {
        return this.parameters.containsKey(key);
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
