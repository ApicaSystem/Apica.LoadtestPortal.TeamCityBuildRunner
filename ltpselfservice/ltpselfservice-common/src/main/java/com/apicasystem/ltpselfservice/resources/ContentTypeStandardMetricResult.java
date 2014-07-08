/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice.resources;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import javax.json.JsonObject;

/**
 *
 * @author andras.nemes
 */
public class ContentTypeStandardMetricResult extends StandardMetricResult
{
    public final Map<String, Integer> types;

    public ContentTypeStandardMetricResult(StandardMetricResult.Metrics m, JsonObject json)
    {
        super(m, json);

        Map<String, Integer> map = new TreeMap();
        JsonObject content_type = json.getJsonObject("content_type");
        for (String type : content_type.keySet())
        {
            map.put(type, Integer.valueOf(content_type.getInt(type, 0)));
        }
        this.types = Collections.unmodifiableMap(map);
    }

    @Override
    protected StringBuilder toString(StringBuilder buf)
    {
        return super.toString(buf).append(", ").append("types=").append(this.types);
    }
}
