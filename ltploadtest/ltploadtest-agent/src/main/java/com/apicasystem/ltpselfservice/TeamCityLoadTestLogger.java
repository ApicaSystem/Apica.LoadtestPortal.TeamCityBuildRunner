package com.apicasystem.ltpselfservice;

import jetbrains.buildServer.agent.BuildProgressLogger;

public class TeamCityLoadTestLogger implements LoadTestLogger
{

    private final BuildProgressLogger logger;

    public TeamCityLoadTestLogger(BuildProgressLogger logger)
    {
        this.logger = logger;
    }

    public BuildProgressLogger getLogger()
    {
        return this.logger;
    }

    public void message(String msg)
    {
        this.logger.progressStarted(msg);
    }

    public void message(String fmt, Object... args)
    {
        this.logger.progressMessage(String.format(fmt, args));
    }

    public void failure(String reason)
    {
        this.logger.buildFailureDescription(reason);
    }

    public void started(String msg)
    {
        this.logger.progressStarted(msg);
    }

    public void finished(String msg)
    {
        if (!Utils.isBlank(msg))
        {
            message(msg);
        }
        this.logger.progressFinished();
    }
}
