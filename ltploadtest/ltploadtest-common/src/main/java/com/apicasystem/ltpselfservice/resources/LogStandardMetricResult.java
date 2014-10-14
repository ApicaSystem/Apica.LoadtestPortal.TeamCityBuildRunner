package com.apicasystem.ltpselfservice.resources;

import javax.json.JsonObject;

public class LogStandardMetricResult extends StandardMetricResult
{

    public final LoadZones zone;
    public final Integer scenarioId;
    public final String level;
    public final String message;

    public LogStandardMetricResult(StandardMetricResult.Metrics m, JsonObject json)
    {
        super(m, json);

        this.zone = LoadZones.valueOf(json.getInt("load_zone_id", 1));
        this.scenarioId = Integer.valueOf(json.getInt("user_scenario_id", 0));
        this.level = json.getString("level", null);
        this.message = json.getString("message", null);
    }

    @Override
    protected StringBuilder toString(StringBuilder buf)
    {
        return super.toString(buf).append(", ").append("zone=").append(this.zone).append(", ").append("scenario=").append(this.scenarioId).append(", ").append("level=").append(this.level).append(", ").append("message=").append(this.message);
    }
}
