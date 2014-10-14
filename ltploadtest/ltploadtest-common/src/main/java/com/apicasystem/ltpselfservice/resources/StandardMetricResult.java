package com.apicasystem.ltpselfservice.resources;

import javax.json.JsonNumber;
import javax.json.JsonObject;

public class StandardMetricResult extends Result
{

    public final Metrics metric;
    public final Number value;
    public final Number count;

    public static enum Metrics
    {

        failure_rate("percent", "Failure loop rate", "%"), average_page_response_time("value", "Average page response time", "ms");

        public final String id;
        public final String valueName;
        public final Boolean integral;
        public final Class<? extends StandardMetricResult> resultType;
        private String description;
        private String unit;

        private Metrics(Class<? extends StandardMetricResult> resultType, String description, String unit)
        {
            this(null, null, unit, resultType);
            this.description = description;
        }

        private Metrics(String valueName, String description, String unit)
        {
            this(valueName, Boolean.valueOf(false), unit, StandardMetricResult.class);
            this.description = description;
        }

        private Metrics(String valueName, String description, String unit, Boolean integral)
        {
            this(valueName, integral, unit, StandardMetricResult.class);
            this.description = description;
        }

        private Metrics(String valueName, Boolean integral, String unit, Class<? extends StandardMetricResult> resultType)
        {
            this.valueName = valueName;
            this.integral = integral;
            this.resultType = resultType;
            this.id = ("__li_" + name());
            this.unit = unit;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public String getUnit()
        {
            return this.unit;
        }

        @Override
        public String toString()
        {
            return description;
        }
    }

    public StandardMetricResult(Metrics m, JsonObject json)
    {
        super(json);
        this.metric = m;

        JsonNumber jsonNumber = json.getJsonNumber(this.metric.valueName);
        if (jsonNumber != null)
        {
            this.value = Double.valueOf(this.metric.integral.booleanValue() ? jsonNumber.longValue() : jsonNumber.doubleValue());
        } else
        {
            this.value = null;
        }
        if ((this.metric.valueName != null) && (this.metric.valueName.equals("percent")))
        {
            this.count = Integer.valueOf(json.getInt("value", 0));
        } else
        {
            this.count = null;
        }
    }

    @Override
    protected StringBuilder toString(StringBuilder buf)
    {
        return super.toString(buf).append(", ").append("metric=").append(this.metric).append(", ").append("value=").append(this.value).append(", ").append("count=").append(this.count);
    }

}
