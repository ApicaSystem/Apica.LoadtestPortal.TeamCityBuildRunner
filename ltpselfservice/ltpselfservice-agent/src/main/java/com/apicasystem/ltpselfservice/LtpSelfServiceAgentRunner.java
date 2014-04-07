/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentBuildRunner;
import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.BuildProcess;
import jetbrains.buildServer.agent.BuildRunnerContext;

/**
 *
 * @author andras.nemes
 */
public class LtpSelfServiceAgentRunner implements AgentBuildRunner, AgentBuildRunnerInfo
{

    public BuildProcess createBuildProcess(AgentRunningBuild arb, BuildRunnerContext brc) throws RunBuildException
    {
        return null;
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
