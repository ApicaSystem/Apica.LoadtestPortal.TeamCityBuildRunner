/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice;

import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;

/**
 *
 * @author andras.nemes
 */
public class SelfServiceFactory implements CommandLineBuildServiceFactory, AgentBuildRunnerInfo 
{

    public CommandLineBuildService createService()
    {
        return new LtpSelfService();
    }

    public AgentBuildRunnerInfo getBuildRunnerInfo()
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
