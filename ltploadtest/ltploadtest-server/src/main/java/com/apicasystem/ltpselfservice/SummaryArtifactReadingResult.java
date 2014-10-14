package com.apicasystem.ltpselfservice;

public class SummaryArtifactReadingResult
{

    private String rawJsonContent;
    private SelfServiceStatisticsOfPreset loadTestStatistics;
    private String exceptionReason;
    private boolean hasResult;

    public SelfServiceStatisticsOfPreset getLoadTestStatistics()
    {
        return loadTestStatistics;
    }

    public void setLoadTestStatistics(SelfServiceStatisticsOfPreset loadTestStatistics)
    {
        this.loadTestStatistics = loadTestStatistics;
    }

    public String getRawJsonContent()
    {
        return rawJsonContent;
    }

    public void setRawJsonContent(String rawJsonContent)
    {
        this.rawJsonContent = rawJsonContent;
    }

    public String getExceptionReason()
    {
        return exceptionReason;
    }

    public void setExceptionReason(String exceptionReason)
    {
        this.exceptionReason = exceptionReason;
    }

    public boolean isHasResult()
    {
        return hasResult;
    }

    public void setHasResult(boolean hasResult)
    {
        this.hasResult = hasResult;
    }
}
