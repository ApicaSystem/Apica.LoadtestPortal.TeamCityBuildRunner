package com.apicasystem.ltpselfservice;

import java.io.PrintWriter;
import java.io.StringWriter;
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

public class SelfServiceSummaryTab extends ApicaLoadTestTabBase
{

    private Debug debug = new Debug(this);

    public SelfServiceSummaryTab(@NotNull PagePlaces pagePlaces,
            @NotNull SBuildServer server, @NotNull PluginDescriptor descriptor)
    {
        super(pagePlaces, server, descriptor);
        setTabTitle(getTitle());
        setPluginName(getClass().getSimpleName());
        setIncludeUrl(descriptor.getPluginResourcesPath(getJspName()));
        addCssFile(descriptor.getPluginResourcesPath("css/style.css"));

    }

    protected String getTitle()
    {
        return "Apica Load Test Summary";
    }

    protected String getJspName()
    {
        return getClass().getSimpleName() + ".jsp";
    }

    protected void fillModel(@NotNull Map<String, Object> model,
            @NotNull HttpServletRequest request, @NotNull SBuild build)
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

        String buildSteps = getListOfBuildSteps(build);
        model.put("buildSteps", buildSteps);
        SummaryArtifactReadingResult artifactReadingResult = loadRawResultsFromArtifact(request);
        if (artifactReadingResult == null || !artifactReadingResult.isHasResult()
                || artifactReadingResult.getLoadTestStatistics() == null)
        {
            model.put("noResults", true);
            model.put("exception", artifactReadingResult.getExceptionReason());
            return;
        }

        model.put("hasResults", true);
        SelfServiceStatisticsOfPreset stats = artifactReadingResult.getLoadTestStatistics();
        SelfServiceStatistics statistics = stats.getStatistics();
        model.put("presetName", stats.getPresetName());
        model.put("jobid", stats.getJobId());
        model.put("link", stats.getLinkToTestResult());
        model.put("dateOfInsertion", statistics.getDateOfInsertion());
        model.put("totalPassedLoops", statistics.getTotalPassedLoops());
        model.put("totalFailedLoops", statistics.getTotalFailedLoops());
        model.put("averageNetworkThroughput", statistics.getAverageNetworkThroughput());
        model.put("networkThroughputUnit", statistics.getNetworkThroughputUnit());
        model.put("averageSessionTimePerLoop", statistics.getAverageSessionTimePerLoop());
        model.put("averageResponseTimePerLoop", statistics.getAverageResponseTimePerLoop());
        model.put("webTransactionRate", statistics.getWebTransactionRate());
        model.put("averageResponseTimePerPage", statistics.getAverageResponseTimePerPage());
        model.put("totalHttpCalls", statistics.getTotalHttpCalls());
        model.put("averageNetworkConnectTime", statistics.getAverageNetworkConnectTime());
        model.put("totalTransmittedBytes", statistics.getTotalTransmittedBytes());
    }

    public String getListOfBuildSteps(SBuild build)
    {
        try
        {
            List<String> buildSteps = new ArrayList<String>();
            if (build != null)
            {
                Iterator<SBuildRunnerDescriptor> descriptorIterator = build.getBuildType().getBuildRunners().iterator();
                while (descriptorIterator.hasNext())
                {
                    SBuildRunnerDescriptor desc = descriptorIterator.next();
                    String type = desc.getType();
                    if (type != null)
                    {
                        buildSteps.add(type);
                    }
                }
            }
            if (buildSteps.size() > 0)
            {
                StringBuilder sb = new StringBuilder();
                for (String buildStep : buildSteps)
                {
                    sb.append(buildStep).append(";");
                }
                return sb.toString();
            }
        } catch (Exception ex)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exception = sw.toString();
            return exception;
        }

        return "Could not locate build steps";
    }
}
