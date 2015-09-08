package com.apicasystem.ltpselfservice;

import com.apicasystem.ltpselfservice.resources.LtpEnvironmentType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildRunnerDescriptor;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;

public class SelfServiceResultsTab extends ApicaLoadTestTabBase
{

    public SelfServiceResultsTab(@NotNull PagePlaces pagePlaces, @NotNull SBuildServer server, @NotNull PluginDescriptor descriptor)
    {
        super(pagePlaces, server, descriptor);
        setTabTitle(getTitle());
        setPluginName(getClass().getSimpleName());
        setIncludeUrl(descriptor.getPluginResourcesPath(getJspName()));
        addCssFile(descriptor.getPluginResourcesPath("css/style.css"));
    }

    protected String getTitle()
    {
        return "Apica Load Test Trends";
    }

    protected String getJspName()
    {
        return getClass().getSimpleName() + ".jsp";
    }

    @Override
    protected void fillModel(Map<String, Object> model, HttpServletRequest request, SBuild build)
    {
        
        if (model == null)
        {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]
            {
                "model", "com/apicasystem/teamcity_plugin/SelfServiceSummaryTab", "fillModel"
            }));
        }
        if (request == null)
        {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]
            {
                "request", "com/apicasystem/teamcity_plugin/SelfServiceSummaryTab", "fillModel"
            }));
        }
        if (build == null)
        {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]
            {
                "build", "com/apicasystem/teamcity_plugin/SelfServiceSummaryTab", "fillModel"
            }));
        }

        LoadtestMetadataReadResult metadataResult = loadMetadataFromArtifact(request);
        
        
        if (metadataResult.isLoadSuccess())
        {
            LoadtestMetadata metadata = metadataResult.getMetadata();
            LtpEnvironmentType environmentType = LtpEnvironmentType.valueOf(metadata.getEnvironmentType());
            model.put("hasResults", true);
            model.put("loadFailure", "");
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(EnvironmentFactory.getLoadtestPortalRoot(environmentType))
                    .append(LtpSelfServiceConstants.LOADTEST_PORTAL_CI_CONTROLLER)
                    .append("?testInstanceId=").append(metadataResult.getMetadata().getPresetTestInstanceId())
                    .append("&authToken=").append(metadataResult.getMetadata().getApiToken());
            model.put("resultUrl", urlBuilder.toString());
        } else
        {
            model.put("hasResults", false);
            model.put("loadFailure", metadataResult.getLoadFailureReason());            
        }
    }
    
    
}
