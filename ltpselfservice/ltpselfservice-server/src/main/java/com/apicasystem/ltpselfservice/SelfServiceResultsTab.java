/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author andras.nemes
 */
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
            model.put("hasResults", true);
            model.put("loadFailure", "");
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(LtpSelfServiceConstants.LOADTEST_PORTAL_ROOT)
                    .append(LtpSelfServiceConstants.LOADTEST_PORTAL_CI_CONTROLLER)
                    .append("?testInstanceId=").append(metadataResult.getMetadata().getPresetTestInstanceId())
                    .append("&authToken=").append(metadataResult.getMetadata().getApiToken());
            model.put("resultUrl", urlBuilder.toString());
        }
        else
        {
            model.put("hasResults", false);
            model.put("loadFailure", metadataResult.getLoadFailureReason());
        }
        
        /*
        SummaryArtifactReadingResult artifactReadingResult = loadRawResultsFromArtifact(request);
        if (artifactReadingResult == null || !artifactReadingResult.isHasResult()
        || artifactReadingResult.getLoadTestStatistics() == null)
        {
        model.put("noResults", true);
        model.put("exception", artifactReadingResult.getExceptionReason());
        return;
        }
        SelfServiceStatisticsOfPreset stats = artifactReadingResult.getLoadTestStatistics();
        SaveStatisticsInLoadtestHistoryResult saveHistoryResult = saveLoadtestHistoryInStore(stats);
        model.put("statsSaved", saveHistoryResult.isSaved());
        model.put("saveException", saveHistoryResult.getException());
        model.put("statsExists", saveHistoryResult.isRecordAlreadyExists());
        try
        {
        String rawJsonSource = super.readRawLoadtestHistory();
        model.put("historyLoadFailed", false);
        model.put("historyLoaded", true);
        model.put("rawHistory", rawJsonSource);
        } catch (IOException ex)
        {
        model.put("historyLoadFailed", true);
        model.put("historyLoaded", false);
        model.put("historyLoadException", ex.getMessage());
        }*/
    }
}
