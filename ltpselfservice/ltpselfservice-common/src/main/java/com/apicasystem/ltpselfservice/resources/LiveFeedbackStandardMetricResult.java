/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice.resources;

import javax.json.JsonObject;

/**
 *
 * @author andras.nemes
 */
public class LiveFeedbackStandardMetricResult extends StandardMetricResult
{

    public final LoadZones zone;
    public final Location location;
    public final Double percent;
    public final String type;
    public final String message;

    public static class Location
    {

        public final double latitude;
        public final double longitude;

        public Location(double latitude, double longitude)
        {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public String toString()
        {
            return "{lat=" + this.latitude + ", lng=" + this.longitude + '}';
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if ((o == null) || (getClass() != o.getClass()))
            {
                return false;
            }
            Location location = (Location) o;
            if (Double.compare(location.latitude, this.latitude) != 0)
            {
                return false;
            }
            if (Double.compare(location.longitude, this.longitude) != 0)
            {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode()
        {
            long temp = Double.doubleToLongBits(this.latitude);
            int result = (int) (temp ^ temp >>> 32);
            temp = Double.doubleToLongBits(this.longitude);
            result = 31 * result + (int) (temp ^ temp >>> 32);
            return result;
        }
    }

    public LiveFeedbackStandardMetricResult(StandardMetricResult.Metrics m, JsonObject json)
    {
        super(m, json);

        this.zone = LoadZones.valueOf(json.getInt("load_zone_id", 1));
        this.location = new Location(getDouble(json, "lat", 0.0D), getDouble(json, "lng", 0.0D));
        this.percent = Double.valueOf(getDouble(json, "percent", 0.0D));
        this.type = json.getString("type", null);
        this.message = json.getString("msg", null);
    }

    protected StringBuilder toString(StringBuilder buf)
    {
        return super.toString(buf).append(", ").append("zone=").append(this.zone).append(", ").append("location=").append(this.location).append(", ").append("percent=").append(this.percent).append(", ").append("type=").append(this.type).append(", ").append("message=").append(this.message);
    }
}
