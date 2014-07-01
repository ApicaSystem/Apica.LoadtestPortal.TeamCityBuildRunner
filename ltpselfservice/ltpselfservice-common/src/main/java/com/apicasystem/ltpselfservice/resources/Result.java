/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice.resources;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import javax.json.JsonNumber;
import javax.json.JsonObject;

/**
 *
 * @author andras.nemes
 */
public abstract class Result
{

    public static final String PREFIX = "__li_";
    public final Date timestamp;
    public final int offset;

    protected Result(JsonObject json)
    {
        this.offset = json.getInt("offset", 0);
        JsonNumber n = json.getJsonNumber("timestamp");
        this.timestamp = (n != null ? DateUtils.toDateFromTimestamp(n.longValue()) : null);
    }

    protected double getDouble(JsonObject json, String name, double defaultValue)
    {
        try
        {
            JsonNumber number = json.getJsonNumber(name);
            if (number != null)
            {
                return number.doubleValue();
            }
        } catch (Exception ignore)
        {
        }
        return defaultValue;
    }

    protected URL getUrl(JsonObject json, String name)
    {
        try
        {
            String url = json.getString(name);
            if (url != null)
            {
                return new URL(url);
            }
        } catch (MalformedURLException ignore)
        {
        }
        return null;
    }

    protected StringBuilder toString(StringBuilder buf)
    {
        return buf.append("timestamp=").append(DateUtils.toIso8601(this.timestamp)).append(", ").append("offset=").append(this.offset);
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "{" + toString(new StringBuilder()) + '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Result))
        {
            return false;
        }
        Result result = (Result) o;
        if (this.offset != result.offset)
        {
            return false;
        }
        if (this.timestamp != null ? !this.timestamp.equals(result.timestamp) : result.timestamp != null)
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int result = this.timestamp != null ? this.timestamp.hashCode() : 0;
        result = 31 * result + this.offset;
        return result;
    }
}
