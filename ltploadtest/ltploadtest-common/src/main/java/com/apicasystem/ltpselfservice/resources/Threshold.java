package com.apicasystem.ltpselfservice.resources;

import java.util.List;

public class Threshold
{

    private final int id;
    private final StandardMetricResult.Metrics metric;
    private final int thresholdValue;
    private final LoadTestResult result;
    private final Operator operator;
    private final BoundedDroppingQueue<Integer> values;
    private int lastOffset;
    private int lastAggregatedValue;
    private boolean lastExceededValue;

    public Threshold(int id, StandardMetricResult.Metrics metric, Operator operator, int thresholdValue, LoadTestResult result)
    {
        this.id = id;
        this.metric = metric;
        this.operator = operator;
        this.thresholdValue = thresholdValue;
        this.result = result;
        this.values = new BoundedDroppingQueue();
        this.lastOffset = -1;
    }

    public int getId()
    {
        return this.id;
    }

    public int getThresholdValue()
    {
        return thresholdValue;
    }

    public StandardMetricResult.Metrics getMetric()
    {
        return this.metric;
    }

    public Operator getOperator()
    {
        return operator;
    }

    public LoadTestResult getResult()
    {
        return this.result;
    }

    public int getAggregatedValue()
    {
        return ListUtils.median(this.values.toList());
    }

    public void accumulate(List<? extends StandardMetricResult> metricValues)
    {
        if ((metricValues == null) || (metricValues.isEmpty()))
        {
            return;
        }
        for (StandardMetricResult v : metricValues)
        {
            if (this.lastOffset < v.offset)
            {
                this.values.put(Integer.valueOf(v.value.intValue()));
            }
        }
        this.lastOffset = ((StandardMetricResult) ListUtils.last(metricValues)).offset;
    }

    public boolean isExceeded()
    {
        this.lastAggregatedValue = getAggregatedValue();
        this.lastExceededValue = false;
        switch (operator.ordinal())
        {
            case 1:
                this.lastExceededValue = (this.lastAggregatedValue < this.thresholdValue);
                break;
            case 2:
                this.lastExceededValue = (this.lastAggregatedValue > this.thresholdValue);
        }
        return this.lastExceededValue;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("If ").append(metric.toString()).append(" ").append(operator.toString())
                .append(" ").append(Integer.toString(thresholdValue)).append(" ")
                .append(metric.getUnit())
                .append(" then mark test as ").append(result.toString());

        return sb.toString();
    }
    
    public String toRelativeThresholdString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("If ").append(metric.toString()).append(" of current test is ").append(operator.getDescription())
                .append(" than in previous successful test by ").append(Integer.toString(thresholdValue)).append("%")                
                .append(" then mark test as ").append(result.toString());

        return sb.toString();
    }

    public String getReason()
    {
        if (this.lastExceededValue)
        {
            return String.format("Metric '%s' has aggregated-value=%d %s %d as threshold", new Object[]
            {
                this.metric.name(), Integer.valueOf(this.lastAggregatedValue), this.operator.symbol, Integer.valueOf(this.thresholdValue)
            });
        }
        return String.format("Metric %s: aggregated-value=%d", new Object[]
        {
            this.metric.name(), Integer.valueOf(this.lastAggregatedValue)
        });
    }
}
