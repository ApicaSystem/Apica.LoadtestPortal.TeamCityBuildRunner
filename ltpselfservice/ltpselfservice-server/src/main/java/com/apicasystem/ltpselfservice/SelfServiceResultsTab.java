/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice;

import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author andras.nemes
 */
public class SelfServiceResultsTab extends SelfServiceSummaryTab
{

    public SelfServiceResultsTab(@NotNull PagePlaces pagePlaces, @NotNull SBuildServer server, @NotNull PluginDescriptor descriptor)
    {
        super(pagePlaces, server, descriptor);
    }

    @Override
    protected String getTitle()
    {
        return "Self service run results";
    }
}
