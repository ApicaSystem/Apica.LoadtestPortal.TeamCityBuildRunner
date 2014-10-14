package com.apicasystem.ltpselfservice;

import java.util.Date;
import java.util.UUID;

public class SelfServiceStatistics implements Comparable<SelfServiceStatistics>
{

    private final UUID statisticsId;
    private Date dateOfInsertion;
    private int totalPassedLoops;
    private int totalFailedLoops;
    private double averageNetworkThroughput;
    private String networkThroughputUnit;
    private double averageSessionTimePerLoop;
    private double averageResponseTimePerLoop;
    private double webTransactionRate;
    private double averageResponseTimePerPage;
    private int totalHttpCalls;
    private int averageNetworkConnectTime;
    private long totalTransmittedBytes;

    public SelfServiceStatistics()
    {
        statisticsId = UUID.randomUUID();
    }

    public UUID getStatisticsId()
    {
        return statisticsId;
    }

    public Date getDateOfInsertion()
    {
        return dateOfInsertion;
    }

    public void setDateOfInsertion(Date dateOfInsertion)
    {
        this.dateOfInsertion = dateOfInsertion;
    }

    public int getTotalPassedLoops()
    {
        return totalPassedLoops;
    }

    public void setTotalPassedLoops(int totalPassedLoops)
    {
        this.totalPassedLoops = totalPassedLoops;
    }

    public int getTotalFailedLoops()
    {
        return totalFailedLoops;
    }

    public void setTotalFailedLoops(int totalFailedLoops)
    {
        this.totalFailedLoops = totalFailedLoops;
    }

    public double getAverageNetworkThroughput()
    {
        return averageNetworkThroughput;
    }

    public void setAverageNetworkThroughput(double averageNetworkThroughput)
    {
        this.averageNetworkThroughput = averageNetworkThroughput;
    }

    public String getNetworkThroughputUnit()
    {
        return networkThroughputUnit;
    }

    public void setNetworkThroughputUnit(String networkThroughputUnit)
    {
        this.networkThroughputUnit = networkThroughputUnit;
    }

    public double getAverageSessionTimePerLoop()
    {
        return averageSessionTimePerLoop;
    }

    public void setAverageSessionTimePerLoop(double averageSessionTimePerLoop)
    {
        this.averageSessionTimePerLoop = averageSessionTimePerLoop;
    }

    public double getAverageResponseTimePerLoop()
    {
        return averageResponseTimePerLoop;
    }

    public void setAverageResponseTimePerLoop(double averageResponseTimePerLoop)
    {
        this.averageResponseTimePerLoop = averageResponseTimePerLoop;
    }

    public double getWebTransactionRate()
    {
        return webTransactionRate;
    }

    public void setWebTransactionRate(double webTransactionRate)
    {
        this.webTransactionRate = webTransactionRate;
    }

    public double getAverageResponseTimePerPage()
    {
        return averageResponseTimePerPage;
    }

    public void setAverageResponseTimePerPage(double averageResponseTimePerPage)
    {
        this.averageResponseTimePerPage = averageResponseTimePerPage;
    }

    public int getTotalHttpCalls()
    {
        return totalHttpCalls;
    }

    public void setTotalHttpCalls(int totalHttpCalls)
    {
        this.totalHttpCalls = totalHttpCalls;
    }

    public int getAverageNetworkConnectTime()
    {
        return averageNetworkConnectTime;
    }

    public void setAverageNetworkConnectTime(int averageNetworkConnectTime)
    {
        this.averageNetworkConnectTime = averageNetworkConnectTime;
    }

    public long getTotalTransmittedBytes()
    {
        return totalTransmittedBytes;
    }

    public void setTotalTransmittedBytes(long totalTransmittedBytes)
    {
        this.totalTransmittedBytes = totalTransmittedBytes;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 29 * hash + (this.statisticsId != null ? this.statisticsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final SelfServiceStatistics other = (SelfServiceStatistics) obj;
        if (this.statisticsId != other.statisticsId
                && (this.statisticsId == null || !this.statisticsId.equals(other.statisticsId)))
        {
            return false;
        }
        return true;
    }

    public int compareTo(SelfServiceStatistics o)
    {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if (this.equals(o))
        {
            return EQUAL;
        }

        if (this.dateOfInsertion.equals(o.getDateOfInsertion()))
        {
            return EQUAL;
        }

        if (this.dateOfInsertion.before(o.getDateOfInsertion()))
        {
            return BEFORE;
        }

        if (this.dateOfInsertion.after(o.getDateOfInsertion()))
        {
            return AFTER;
        }

        return AFTER;
    }

}
