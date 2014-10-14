package com.apicasystem.ltpselfservice;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentBuildRunner;
import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.BuildProcess;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class LtpSelfServiceAgentRunner implements AgentBuildRunner, AgentBuildRunnerInfo
{

    private static final Logger LOG = Logger.getLogger(LtpSelfServiceAgentRunner.class);
    @NotNull
    private final ArtifactsWatcher artifactsWatcher;

    public LtpSelfServiceAgentRunner(@NotNull ArtifactsWatcher artifactsWatcher)
    {
        this.artifactsWatcher = artifactsWatcher;
        LOG.info("init");
    }

    public BuildProcess createBuildProcess(@NotNull AgentRunningBuild arb, @NotNull BuildRunnerContext brc) throws RunBuildException
    {
        LOG.info("createBuildProcess");
        return new ApicaBuildProcess(arb, brc, this.artifactsWatcher);
    }

    public AgentBuildRunnerInfo getRunnerInfo()
    {
        return this;
    }

    public String getType()
    {
        return LtpSelfServiceConstants.RUNNER_TYPE;
    }

    public boolean canRun(BuildAgentConfiguration bac)
    {
        return true;
    }

}
