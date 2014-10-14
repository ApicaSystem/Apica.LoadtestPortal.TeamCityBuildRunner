package com.apicasystem.ltpselfservice;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public abstract class ApicaLoadTestTabBase extends ViewLogTab
{

    private final PluginDescriptor pluginDescriptor;
    private final SBuildServer buildServer;

    public ApicaLoadTestTabBase(@NotNull PagePlaces pagePlaces,
            @NotNull SBuildServer server, @NotNull PluginDescriptor descriptor)
    {
        super("", "", pagePlaces, server);
        buildServer = server;
        this.pluginDescriptor = descriptor;
    }

    public LoadtestMetadataReadResult loadMetadataFromArtifact(HttpServletRequest request)
    {
        LoadtestMetadata metadata = new LoadtestMetadata();
        LoadtestMetadataReadResult res = new LoadtestMetadataReadResult();
        metadata.setApiToken("token");
        metadata.setPresetTestInstanceId(-1);
        res.setMetadata(metadata);
        res.setLoadSuccess(true);
        res.setLoadFailureReason("");

        SBuild sBuild = getBuild(request);

        if (sBuild == null)
        {
            res.setLoadFailureReason("No build available.");
            res.setLoadSuccess(false);
        } else
        {
            BuildArtifacts artifacts = sBuild.getArtifacts(BuildArtifactsViewMode.VIEW_ALL);
            BuildArtifact metadataArtifact = artifacts.getArtifact("load-test-metadata.txt");

            if (metadataArtifact == null)
            {
                res.setLoadFailureReason("Loadtest metadata artifact not found.");
                res.setLoadSuccess(false);
            } else
            {
                try
                {
                    InputStream inputStream = metadataArtifact.getInputStream();
                    InputStreamReader isr = new InputStreamReader(inputStream);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null)
                    {
                        sb.append(line);
                    }
                    try
                    {
                        br.close();
                    } catch (IOException ex)
                    {
                    }
                    String rawJson = sb.toString();
                    res.setPureJsonContent(rawJson);
                    metadata = new Gson().fromJson(rawJson, LoadtestMetadata.class);
                    res.setMetadata(metadata);
                } catch (Exception ex)
                {
                    res.setLoadSuccess(false);
                    res.setLoadFailureReason("Exception when loading metadata content: ".concat(ex.getMessage()));
                }
            }
        }

        return res;
    }

    public SummaryArtifactReadingResult loadRawResultsFromArtifact(HttpServletRequest request)
    {
        SummaryArtifactReadingResult res = new SummaryArtifactReadingResult();
        res.setExceptionReason("");
        res.setHasResult(false);
        res.setRawJsonContent("");

        SBuild sBuild = getBuild(request);

        if (sBuild == null)
        {
            return null;
        }
        BuildArtifacts artifacts = sBuild.getArtifacts(BuildArtifactsViewMode.VIEW_ALL);
        BuildArtifact resultsArtifact = artifacts.getArtifact("load-test-results.txt");
        if (resultsArtifact == null)
        {
            res.setExceptionReason("No result artifact found in the artifacts collection.");
        }
        try
        {
            InputStream inputStream = resultsArtifact.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }
            try
            {
                br.close();
            } catch (IOException ex)
            {
            }
            res.setHasResult(true);
            res.setRawJsonContent(sb.toString());
            try
            {
                SelfServiceStatisticsOfPreset stats = convertToStatistics(res.getRawJsonContent());
                res.setLoadTestStatistics(stats);
            } catch (Exception ex)
            {
                res.setLoadTestStatistics(null);
                res.setExceptionReason("Could not convert raw json to statistics: ".concat(ex.getMessage()));
            }
        } catch (IOException ex)
        {
            res.setExceptionReason("Exception when loading artifact content: ".concat(ex.getMessage()));
        }

        return res;
    }

    private SelfServiceStatisticsOfPreset convertToStatistics(String rawJson)
    {
        Gson gson = new Gson();
        return gson.fromJson(rawJson, SelfServiceStatisticsOfPreset.class);
    }

    public LoadTestHistory convertFromJson(String json)
    {
        Gson gson = new Gson();
        return gson.fromJson(json, LoadTestHistory.class);
    }
}
