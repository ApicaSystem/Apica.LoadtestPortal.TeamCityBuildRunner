/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice.loadtest;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 *
 * @author andras.nemes
 */
public class LoadtestJobSummaryResponse
{
    private final String NL = "\r\n";
    
    @SerializedName("Link to testresults")
    private String linkToTestResults;
    @SerializedName("Performance summary")
    private PerformanceSummary performanceSummary;
    @SerializedName("exception")
    private String exception;
    @SerializedName("Self service job id")
    private int jobId;

    public int getJobId()
    {
        return jobId;
    }

    public void setJobId(int jobId)
    {
        this.jobId = jobId;
    }

    public String getException()
    {
        return exception;
    }

    public void setException(String exception)
    {
        this.exception = exception;
    }
    
    public String getLinkToTestResults()
    {
        return this.linkToTestResults;
    }
    
    public PerformanceSummary getPerformanceSummary()
    {
        return this.performanceSummary;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("--- Load test job summary message ---%n", new Object[0]));
        sb.append("Job id: ").append(getJobId()).append(NL);
        sb.append("Message date UTC: ").append(new Date()).append(NL);
        //sb.append("Link to results: ").append(getLinkToTestResults()).append(NL);
        PerformanceSummary summary = getPerformanceSummary();
        sb.append("Total passed loops: ").append(summary.getTotalPassedLoops()).append(NL);
        sb.append("Total failed loops: ").append(summary.getTotalFailedLoops()).append(NL);
        sb.append("Average network throughput: ").append(summary.getAverageNetworkThroughput())
                .append(" ").append(summary.getNetworkThroughputUnit()).append(NL);
        sb.append("Average session time per loop (s): ")
                .append(summary.getAverageSessionTimePerLoop()).append(NL);
        sb.append("Average response time per loop (s): ")
                .append(summary.getAverageResponseTimePerLoop()).append(NL);
        sb.append("Web transaction rate (Hits/s): ")
                .append(summary.getWebTransactionRate()).append(NL);
        sb.append("Average response time per page (s): ")
                .append(summary.getAverageResponseTimePerPage()).append(NL);
        sb.append("Total http(s) calls: ")
                .append(summary.getTotalHttpCalls()).append(NL);
        sb.append("Avg network connect time (ms): ")
                .append(summary.getAverageNetworkConnectTime()).append(NL);
        sb.append("Total transmitted bytes: ")
                .append(summary.getTotalTransmittedBytes()).append(NL);
        sb.append(String.format("--- END ---%n", new Object[0]));
        return sb.toString();
    }
}
