package com.apicasystem.ltpselfservice;

import com.apicasystem.ltpselfservice.resources.LoadTestResult;
import com.apicasystem.ltpselfservice.resources.Operator;
import com.apicasystem.ltpselfservice.resources.StandardMetricResult;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.json.Json;
import javax.json.JsonArrayBuilder;

public class JsonGenerator
{

    private Map<String, String> settings;
    private final Debug debug;

    public JsonGenerator()
    {
        this.debug = new Debug(JsonGenerator.class);
    }

    public void setSettings(Map<String, String> settings)
    {
        this.settings = settings;
    }

    private static class MetricDescriptor
    {

        public StandardMetricResult.Metrics metric;
        public String label;
        public String unit;

        private MetricDescriptor(StandardMetricResult.Metrics metric, String label, String unit)
        {
            this.metric = metric;
            this.label = label;
            this.unit = unit;
        }
    }

    public String getOperators()
    {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (int i = 0; i < Operator.values().length; i++)
        {
            Operator operator = Operator.values()[i];
            builder.add(Json.createObjectBuilder().add("name", operator.name()).add("label", operator.label).build());
        }
        StringWriter buf = new StringWriter();
        Json.createWriter(buf).writeArray(builder.build());
        return buf.toString();
    }

    public String getActions()
    {
        List<LoadTestResult> results = Arrays.asList(new LoadTestResult[]
        {
            LoadTestResult.failed
        });

        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (int i = 0; i < results.size(); i++)
        {
            LoadTestResult action = (LoadTestResult) results.get(i);
            builder.add(Json.createObjectBuilder().add("name", action.getId()).add("label", action.getDisplayName()).build());
        }
        StringWriter buf = new StringWriter();
        Json.createWriter(buf).writeArray(builder.build());
        return buf.toString();
    }

    public String getMetrics()
    {
        List<MetricDescriptor> metrics = Arrays.asList(new MetricDescriptor[]
        {
            new MetricDescriptor(StandardMetricResult.Metrics.failure_rate, "Failed loops rate", "%"), new MetricDescriptor(StandardMetricResult.Metrics.average_page_response_time, "Average response time per page", "ms")
        });

        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (int i = 0; i < metrics.size(); i++)
        {
            MetricDescriptor m = (MetricDescriptor) metrics.get(i);
            builder.add(Json.createObjectBuilder().add("name", m.metric.name()).add("label", m.label).add("unit", m.unit).build());
        }
        StringWriter buf = new StringWriter();
        Json.createWriter(buf).writeArray(builder.build());
        return buf.toString();
    }

    public String getThresholds(Map<String, String> settings)
    {
        this.debug.print("getThresholds: %s", new Object[]
        {
            settings
        });

        JsonArrayBuilder builder = Json.createArrayBuilder();

        Pattern pattern = Pattern.compile("threshold\\.(\\d+)\\.value");
        for (String key : settings.keySet())
        {
            Matcher m = pattern.matcher(key);
            if (m.matches())
            {
                String id = m.group(1);
                this.debug.print("getThresholds: id=%s", new Object[]
                {
                    id
                });

                String metric = (String) settings.get("threshold." + id + ".metric");
                String operator = (String) settings.get("threshold." + id + ".operator");
                String value = (String) settings.get("threshold." + id + ".value");
                String action = (String) settings.get("threshold." + id + ".result");

                builder.add(Json.createObjectBuilder().add("metric", metric).add("operator", operator).add("value", Integer.parseInt(value)).add("action", action).build());
            }
        }
        StringWriter buf = new StringWriter();
        Json.createWriter(buf).writeArray(builder.build());
        return buf.toString();
    }

    public String getThresholds()
    {
        return getThresholds(this.settings);
    }
}
