/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.runner.BuildServiceAdapter;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;

/**
 *
 * @author andras.nemes
 */
public class LtpSelfService extends BuildServiceAdapter
{

    @Override
    public ProgramCommandLine makeProgramCommandLine() throws RunBuildException
    {
        return null;
    }
    
}
