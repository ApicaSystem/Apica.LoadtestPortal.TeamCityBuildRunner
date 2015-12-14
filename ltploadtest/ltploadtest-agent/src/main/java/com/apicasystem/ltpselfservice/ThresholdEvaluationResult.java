package com.apicasystem.ltpselfservice;

import java.util.ArrayList;
import java.util.List;

public class ThresholdEvaluationResult
{

    private boolean thresholdBroken;
    private final List<String> exceededThresholdsDescriptions;
    private final List<String> passedThresholdsDescriptions;
    private String rawEvaluationResult;

    public String getRawEvaluationResult()
    {
        return rawEvaluationResult;
    }

    public void setRawEvaluationResult(String rawEvaluationResult)
    {
        this.rawEvaluationResult = rawEvaluationResult;
    }

    public ThresholdEvaluationResult()
    {
        exceededThresholdsDescriptions = new ArrayList<String>();
        passedThresholdsDescriptions = new ArrayList<String>();
    }

    public boolean isThresholdBroken()
    {
        return thresholdBroken;
    }

    public void setThresholdBroken(boolean thresholdBroken)
    {
        this.thresholdBroken = thresholdBroken;
    }

    public List<String> getExceededThresholdsDescriptions()
    {
        return exceededThresholdsDescriptions;
    }

    public void addThresholdExceededDescription(String description)
    {
        exceededThresholdsDescriptions.add(description);
    }
    
    public void addThresholdPassedDescription(String description)
    {
        passedThresholdsDescriptions.add(description);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (String exceeded : exceededThresholdsDescriptions)
        {
            sb.append(exceeded).append("\r\n");
        }
        for (String passed : passedThresholdsDescriptions)
        {
            sb.append(passed).append("\r\n");
        }
        return sb.toString();
    }
}
