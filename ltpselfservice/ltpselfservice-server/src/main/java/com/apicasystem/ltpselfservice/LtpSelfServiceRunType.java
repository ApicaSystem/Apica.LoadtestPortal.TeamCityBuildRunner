/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice;

import java.util.HashMap;
import java.util.Map;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author andras.nemes
 */
public class LtpSelfServiceRunType extends RunType
{
    private final PluginDescriptor pluginDescriptor;
    private final ApicaSettings settings;
    
    public LtpSelfServiceRunType(@NotNull RunTypeRegistry runTypeRegistry, 
            @NotNull PluginDescriptor pluginDescriptor, @NotNull ApicaSettings settings)
    {
        this.pluginDescriptor = pluginDescriptor;
        runTypeRegistry.registerRunType(this);
        this.settings = settings;
    }
    
    @Override
    @NotNull
    public String getType()
    {
        return LtpSelfServiceConstants.RUNNER_TYPE;
    }

    @Override
    @NotNull
    public String getDisplayName()
    {
        return LtpSelfServiceConstants.RUNNER_DISPLAY_NAME;
    }

    @Override
    @NotNull
    public String getDescription()
    {
        return LtpSelfServiceConstants.RUNNER_DESCRIPTION;
    }

    @Override
    @Nullable
    public PropertiesProcessor getRunnerPropertiesProcessor()
    {
        return new LtpSelfServiceRunTypePropertiesProcessor();
    }

    @Override
    @Nullable
    public String getEditRunnerParamsJspFilePath()
    {
        return this.pluginDescriptor.getPluginResourcesPath("editLtpSelfServiceSettings.jsp");
    }

    @Override
    @Nullable
    public String getViewRunnerParamsJspFilePath()
    {
        return this.pluginDescriptor.getPluginResourcesPath("viewLtpSelfServiceSettings.jsp");
    }

    @Override
    @Nullable
    public Map<String, String> getDefaultRunnerProperties()
    {
        Map<String, String> parameters = new HashMap<String, String>();
        
        return parameters;
    }
    
}
