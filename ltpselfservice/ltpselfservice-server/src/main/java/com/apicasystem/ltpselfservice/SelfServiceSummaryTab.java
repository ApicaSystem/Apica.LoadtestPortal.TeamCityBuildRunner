/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifact;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifacts;
import jetbrains.buildServer.serverSide.artifacts.BuildArtifactsViewMode;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.ViewLogTab;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author andras.nemes
 */
public class SelfServiceSummaryTab extends ViewLogTab
{
    private Debug debug = new Debug(this);

    public SelfServiceSummaryTab(@NotNull PagePlaces pagePlaces, @NotNull SBuildServer server, @NotNull PluginDescriptor descriptor)
    {
        super("", "", pagePlaces, server);
        debug.setEnabled(true);

        setTabTitle(getTitle());
        setPluginName(getClass().getSimpleName());
        setIncludeUrl(descriptor.getPluginResourcesPath(getJspName()));
        //addCssFile(descriptor.getPluginResourcesPath("css/style.css"));
        this.debug.print("created");
    }

    protected String getTitle()
    {
        return "Load Test Summary";
    }

    protected String getJspName()
    {
        return getClass().getSimpleName() + ".jsp";
    }

    protected void fillModel(@NotNull Map<String, Object> model, @NotNull HttpServletRequest request, @NotNull SBuild build)
    {
    }
}
