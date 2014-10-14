package com.apicasystem.ltpselfservice;

import java.util.ArrayList;
import java.util.List;

public class ThresholdEvaluationResult
{

    private boolean thresholdBroken;
    private final List<String> exceededThresholdsDescription;
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
        exceededThresholdsDescription = new ArrayList<String>();
    }

    public boolean isThresholdBroken()
    {
        return thresholdBroken;
    }

    public void setThresholdBroken(boolean thresholdBroken)
    {
        this.thresholdBroken = thresholdBroken;
    }

    public List<String> getExceededThresholdsDescription()
    {
        return exceededThresholdsDescription;
    }

    public void addThresholdExceededDescription(String description)
    {
        exceededThresholdsDescription.add(description);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (String string : exceededThresholdsDescription)
        {
            sb.append(string).append("\r\n");
        }
        return sb.toString();
    }
}
