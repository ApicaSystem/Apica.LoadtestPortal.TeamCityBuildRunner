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
public class ContentTypeLoadTimeStandardMetricResult extends StandardMetricResult
{

    public final String type;
    public final Double minimum;
    public final Double maximum;

    public ContentTypeLoadTimeStandardMetricResult(StandardMetricResult.Metrics m, JsonObject json)
    {
        super(m, json);

        this.type = json.getString("content_type", null);
        this.minimum = Double.valueOf(getDouble(json, "min", 0.0D));
        this.maximum = Double.valueOf(getDouble(json, "max", 0.0D));
    }

    @Override
    protected StringBuilder toString(StringBuilder buf)
    {
        return super.toString(buf).append(", ").append("type=").append(this.type).append(", ").append("minimum=").append(this.minimum).append(", ").append("maximum=").append(this.maximum);
    }
}
