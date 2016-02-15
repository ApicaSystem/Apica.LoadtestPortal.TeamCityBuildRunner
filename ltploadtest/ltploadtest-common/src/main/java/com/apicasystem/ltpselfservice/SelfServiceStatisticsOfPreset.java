package com.apicasystem.ltpselfservice;

public class SelfServiceStatisticsOfPreset
{
    private String linkToTestResult;
    private int jobId;
    private String presetName;
    private SelfServiceStatistics statistics;

    public String getLinkToTestResult()
    {
        return linkToTestResult;
    }

    public void setLinkToTestResult(String linkToTestResult)
    {
        this.linkToTestResult = linkToTestResult;
    }

    public int getJobId()
    {
        return jobId;
    }

    public void setJobId(int jobId)
    {
        this.jobId = jobId;
    }

    public String getPresetName()
    {
        return presetName;
    }

    public void setPresetName(String presetName)
    {
        this.presetName = presetName;
    }

    public SelfServiceStatistics getStatistics()
    {
        return statistics;
    }

    public void setStatistics(SelfServiceStatistics statistics)
    {
        this.statistics = statistics;
    }
}
