/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

    public SaveStatisticsInLoadtestHistoryResult saveLoadtestHistoryInStore(SelfServiceStatisticsOfPreset stats)
    {
        SaveStatisticsInLoadtestHistoryResult res = new SaveStatisticsInLoadtestHistoryResult();
        res.setException("");
        res.setSaved(false);
        res.setRecordAlreadyExists(false);
        String root = this.buildServer.getServerRootPath();
        String pluginFolder = pluginDescriptor.getPluginResourcesPath();
        String storageFilePath = root.concat(pluginFolder).concat(LtpSelfServiceConstants.LOADTEST_HISTORY_STORAGE_FOLDER)
                .concat(LtpSelfServiceConstants.URL_SEPARATOR)
                .concat(LtpSelfServiceConstants.LOADTEST_HISTORY_STORAGE_FILE);
        File sourceFile = new File(storageFilePath);
        res.setException(storageFilePath);
        if (!sourceFile.exists())
        {
            try
            {
                boolean fileCreationSuccess = sourceFile.createNewFile();
                if (!fileCreationSuccess)
                {
                    res.setException("Could not create the loadtest history storage file.");
                }
            } catch (IOException ex)
            {
                res.setException("Could not create the loadtest history storage file: "
                        .concat(ex.getMessage().concat(": ")
                .concat(storageFilePath).concat(", ").concat(sourceFile.getAbsolutePath())
                        .concat(", server root: ").concat(root)));
            }
        }

        if (sourceFile.exists())
        {
            if (sourceFile.canRead() && sourceFile.canWrite())
            {
                try
                {
                    FileReader fr = new FileReader(sourceFile);
                    BufferedReader br = new BufferedReader(fr);
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null)
                    {
                        sb.append(line);
                    }
                    String rawJson = sb.toString();
                    if (rawJson == null || rawJson.equals(""))
                    {
                        //file is empty
                        LoadTestHistory loadtestHistory = new LoadTestHistory();
                        ArrayList<SelfServiceStatistics> presetResultHistory = new ArrayList<SelfServiceStatistics>();
                        loadtestHistory.setPresetName(stats.getPresetName());
                        presetResultHistory.add(stats.getStatistics());
                        loadtestHistory.setPresetResultHistory(presetResultHistory);
                        String json = new Gson().toJson(loadtestHistory);
                        try
                        {
                            saveToLocalStorage(sourceFile, json);
                            res.setSaved(true);
                        } catch (IOException ioe)
                        {
                            res.setException("IOException while while saving to history source file: ".concat(ioe.getMessage()));
                        }
                    } else
                    {
                        LoadTestHistory loadtestHistory = convertFromJson(sb.toString());
                    }

                } catch (FileNotFoundException ex)
                {
                    res.setException("File not found: ".concat(sourceFile.getAbsolutePath()));
                } catch (IOException ex)
                {
                    res.setException("IOException while reading the source file: ".concat(ex.getMessage()));
                }
            } else
            {
                res.setException("Cannot read or write to history source file.");
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
            } catch (Exception ex)
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
        } catch (Exception ex)
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

    private LoadTestHistory convertFromJson(String json)
    {
        Gson gson = new Gson();
        return gson.fromJson(json, LoadTestHistory.class);
    }

    private void saveToLocalStorage(File sourceFile, String contents) throws IOException
    {
        FileWriter fileWriter = new FileWriter(sourceFile);
        if (!sourceFile.exists())
        {
            throw new IOException("History source file doesn't exist.");
        }
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(contents);
        bw.close();
    }
}
