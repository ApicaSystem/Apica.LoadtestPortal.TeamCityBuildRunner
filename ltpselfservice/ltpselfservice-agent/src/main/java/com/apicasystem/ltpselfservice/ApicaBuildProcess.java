/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice;

import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;

/**
 *
 * @author andras.nemes
 */
public class ApicaBuildProcess extends FutureBasedBuildProcess
{
    private final AgentRunningBuild build;
    private final BuildRunnerContext context;
    private final ArtifactsWatcher artifactsWatcher;

    public ApicaBuildProcess(AgentRunningBuild build, BuildRunnerContext context,
            ArtifactsWatcher artifactsWatcher)
    {
        this.build = build;
        this.context = context;
        this.artifactsWatcher = artifactsWatcher;
    }

    public BuildFinishedStatus call() throws Exception
    {
        TeamCityLoadTestLogger logger = new TeamCityLoadTestLogger(this.build.getBuildLogger());
        logger.started("Load Test");
        LoadTestParameters params = new LoadTestParameters(this.context.getRunnerParameters());
        String loadtestPresetName = params.get("", "");
        String loadtestFileName = params.get("", "");
        Thread.sleep(3000);
        return BuildFinishedStatus.FINISHED_SUCCESS;
    }
}
