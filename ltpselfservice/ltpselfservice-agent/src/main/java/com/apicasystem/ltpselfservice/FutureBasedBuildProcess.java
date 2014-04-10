/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProcess;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author andras.nemes
 */
public abstract class FutureBasedBuildProcess implements BuildProcess, Callable<BuildFinishedStatus>
{
    private static final Logger LOG = Logger.getLogger(FutureBasedBuildProcess.class);
    private Future<BuildFinishedStatus> futureStatus;   
    

    public void start() throws RunBuildException
    {
        try
        {
            this.futureStatus = Executors.newSingleThreadExecutor().submit(this);
            LOG.info("Build process started");
        } catch (RejectedExecutionException e)
        {
            LOG.error("Build process failed to start", e);
            throw new RunBuildException(e);
        }
    }

    @NotNull
    public BuildFinishedStatus waitFor() throws RunBuildException
    {
        try
        {
            BuildFinishedStatus status = (BuildFinishedStatus) this.futureStatus.get();
            LOG.info("Build process was finished");
            return status;
        } catch (InterruptedException e)
        {
            throw new RunBuildException(e);
        } catch (ExecutionException e)
        {
            throw new RunBuildException(e);
        } catch (CancellationException e)
        {
            LOG.info("Build process was interrupted", e);
        }
        return BuildFinishedStatus.INTERRUPTED;
    }

    public void interrupt()
    {
        this.futureStatus.cancel(true);
    }

    public boolean isInterrupted()
    {
        return (this.futureStatus.isCancelled()) && (isFinished());
    }

    public boolean isFinished()
    {
        return this.futureStatus.isDone();
    }
}
