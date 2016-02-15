package com.apicasystem.ltpselfservice;

import com.apicasystem.ltpselfservice.resources.LoadTestParameters;
import com.apicasystem.ltpselfservice.loadtest.*;
import com.apicasystem.ltpselfservice.resources.LtpEnvironmentType;
import com.apicasystem.ltpselfservice.resources.Operator;
import com.apicasystem.ltpselfservice.resources.StandardMetricResult;
import com.apicasystem.ltpselfservice.resources.Threshold;
import com.apicasystem.ltpselfservice.resources.ThresholdType;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import jetbrains.buildServer.messages.DefaultMessagesInfo;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ApicaBuildProcess extends FutureBasedBuildProcess
{

    private LoadtestMetadata loadtestMetadata;
    private final AgentRunningBuild build;
    private final BuildRunnerContext context;
    private final ArtifactsWatcher artifactsWatcher;
    private final String NL = "\r\n";
    private static final String[] charsToBeUrlEncoded = {"%", " ", "!", "\"", "#", "&", "/", "(", ")", "=", "?", "@", "£", "$", "€", "{", "[", "]", "}", "\\", "'", "*", "^", "<", ">", ",", ";", ":", "~"};
    private static final String[] encodedUrlChars = {"%25","%20", "%21", "%22", "%23", "%26", "%2F", "%28", "%29", "%3D", "%3F", "%40", "%C2%A3", "%24", "%E2%82%AC", "%7B", "%5B", "%5D", "%7D", "%5C", "%27", "%2A", "%5E", "%3C", "%3E", "%2C", "%3B", "%3A", "%7E"};    

    public ApicaBuildProcess(AgentRunningBuild build, BuildRunnerContext context,
            ArtifactsWatcher artifactsWatcher)
    {
        this.build = build;
        this.context = context;
        this.artifactsWatcher = artifactsWatcher;
    }

    public BuildFinishedStatus call() throws Exception
    {
        TeamCityLoadTestLogger logger = new TeamCityLoadTestLogger(this.build.getBuildLogger());
        logger.started("Apica Loadtest");

        LoadTestParameters params = new LoadTestParameters(this.context.getRunnerParameters());
        ApicaSettings apicaSettings = new ApicaSettings();
        LtpEnvironmentType parseEnvironmentType = apicaSettings.parseEnvironmentType(this.context.getRunnerParameters());
        JobParamsValidationResult validationResult = validateJobParams(params, parseEnvironmentType);
        if (!validationResult.isAllParamsPresent())
        {
            logger.failure(validationResult.getExceptionMessage());
            return BuildFinishedStatus.FINISHED_FAILED;
        }

        List<Threshold> absoluteThresholds = new ArrayList<Threshold>();
        try
        {
            absoluteThresholds = apicaSettings.parseThresholds(this.context.getRunnerParameters(), ThresholdType.Absolute);
            logger.message("Absolute threshold values: \r\n".concat(apicaSettings.stringifiedThresholds(this.context.getRunnerParameters(), ThresholdType.Absolute)));

        } catch (Exception ex)
        {
            String message = ex.getMessage() == null ? "Null pointer exception." : ex.getMessage();
            logger.message("Could not read absolute threshold values: "
                    .concat(message).concat("\r\n"));
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.message(sw.toString());
            logger.message("\r\n");
            logger.message(params.toString());
        }

        List<Threshold> relativeThresholds = new ArrayList<Threshold>();
        try
        {
            relativeThresholds = apicaSettings.parseThresholds(this.context.getRunnerParameters(), ThresholdType.Relative);
            logger.message("Relative threshold values: \r\n".concat(apicaSettings.stringifiedThresholds(this.context.getRunnerParameters(), ThresholdType.Relative)));
        } catch (Exception ex)
        {
            String message = ex.getMessage() == null ? "Null pointer exception." : ex.getMessage();
            logger.message("Could not read relative threshold values: "
                    .concat(message).concat("\r\n"));
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.message(sw.toString());
            logger.message("\r\n");
            logger.message(params.toString());
        }

        logger.message("Load test preset name: ".concat(params.get(LtpSelfServiceConstants.SETTINGS_LTP_PRESET_NAME, "")));
        logger.message("Load test file name: ".concat(params.get(LtpSelfServiceConstants.SETTINGS_LTP_RUNNABLE_FILE, "")));
        logger.message("Load test environment: ".concat(parseEnvironmentType.getDisplayName()));
        return runApicaSelfServiceJob(logger, params, absoluteThresholds, relativeThresholds, parseEnvironmentType);
    }

    private SelfServiceStatisticsOfPreset extractTestResultOfPreviousSuccessfulBuild(TeamCityLoadTestLogger logger)
    {
        String teamcityRestUrl = getLatestBuildGetterUrl();
        logger.message("****************************************************************");
        logger.message("------ STARTING TEST RESULT EXTRACTION FROM PREVIOUS BUILD -----");
        try
        {
            String auth = getBuilderAuthBase64();
            String latestBuildXml = executeCallToTeamcityRestApi(teamcityRestUrl, auth);
            logger.message("Content of latest build:");
            logger.message(latestBuildXml);

            String artifactsLink = getLatestBuildArtifactHref(latestBuildXml);
            logger.message("Extracted artifacts link:");
            logger.message(artifactsLink);

            String artifactsGetterLink = getArtifactsGetterLink(artifactsLink);
            logger.message("Extracted artifacts getter link:");
            logger.message(artifactsGetterLink);

            String artifactFilesXml = executeCallToTeamcityRestApi(artifactsGetterLink, auth);
            logger.message("Artifacts content of latest successful build:");
            logger.message(artifactFilesXml);

            String testResultDownloadLink = getLatestTestResultDownloadLink(artifactFilesXml);
            logger.message("Extracted test result download link:");
            logger.message(testResultDownloadLink);

            if (testResultDownloadLink == null)
            {
                throw new NullPointerException("Failed to locate the load test result artifact from the previous successful load test.");
            }
            String testResultFileContent = executeCallToTeamcityRestApi(getArtifactFileDownloaderLink(testResultDownloadLink), auth);
            Gson gson = new Gson();
            SelfServiceStatisticsOfPreset statisticsOfPreviousBuild = gson.fromJson(testResultFileContent, SelfServiceStatisticsOfPreset.class);
            logger.message("Successfully parsed the content of the most recent test result:");
            logger.message(testResultFileContent);
            logger.message("------ TEST RESULT EXTRACTION FROM PREVIOUS BUILD SUCCESS  -----");
            logger.message("****************************************************************");
            return statisticsOfPreviousBuild;
        } catch (MalformedURLException e)
        {
            logger.message("Malformed url exception during extraction of previous successful build: ".concat(e.getMessage()).concat(NL).concat(getExceptionStacktrace(e)));
        } catch (IOException e)
        {
            logger.message("IOException during extraction of previous successful build: ".concat(e.getMessage()).concat(NL).concat(getExceptionStacktrace(e)));
        } catch (ParserConfigurationException e)
        {
            logger.message("ParserConfigurationException during extraction of previous successful build: ".concat(e.getMessage()).concat(NL).concat(getExceptionStacktrace(e)));
        } catch (SAXException e)
        {
            logger.message("SAXException during extraction of previous successful build: ".concat(e.getMessage()).concat(NL).concat(getExceptionStacktrace(e)));
        } catch (JsonSyntaxException e)
        {
            logger.message("Json syntax exception exception during extraction of previous successful build: ".concat(e.getMessage()).concat(NL).concat(getExceptionStacktrace(e)));
        } catch (NullPointerException e)
        {
            logger.message("Null pointer exception during extraction of previous successful build: ".concat(e.getMessage()).concat(NL).concat(getExceptionStacktrace(e)));
        }
        logger.message("------  TEST RESULT EXTRACTION FROM PREVIOUS BUILD FAILED  -----");
        logger.message("****************************************************************");
        return null;
    }

    private JobParamsValidationResult validateJobParams(LoadTestParameters params, LtpEnvironmentType environmentType)
    {
        JobParamsValidationResult res = new JobParamsValidationResult();
        StringBuilder sb = new StringBuilder();
        res.setAllParamsPresent(true);
        String loadtestPresetName = params.get(LtpSelfServiceConstants.SETTINGS_LTP_PRESET_NAME, "");
        String loadtestFileName = params.get(LtpSelfServiceConstants.SETTINGS_LTP_RUNNABLE_FILE, "");
        String authToken = params.get(LtpSelfServiceConstants.SETTINGS_LTP_API_AUTH_TOKEN, "");

        if (loadtestPresetName.equals(""))
        {
            sb.append("Unable to retrieve the loadtest preset name. Please re-enter it on the setup page. ");
            res.setAllParamsPresent(false);
        }
        if (loadtestFileName.equals(""))
        {
            sb.append("Unable to retrieve the loadtest file name. Please re-enter it on the setup page. ");
            res.setAllParamsPresent(false);
        }
        if (authToken.equals(""))
        {
            sb.append("Unable to retrieve the LTP auth token. Please re-enter it on the setup page. ");
            res.setAllParamsPresent(false);
        }
        int testInstanceId = -1;
        if (!authToken.equals("") && !loadtestPresetName.equals(""))
        {
            ServerSideLtpApiWebService serverSideService = new ServerSideLtpApiWebService(environmentType);
            PresetResponse presetResponse = serverSideService.checkPreset(authToken, loadtestPresetName);
            if (!presetResponse.isPresetExists())
            {
                sb.append("Cannot find this preset: ").append(loadtestPresetName).append(". ");
                res.setAllParamsPresent(false);
            } else
            {
                testInstanceId = presetResponse.getTestInstanceId();
                if (testInstanceId < 1)
                {
                    sb.append("The preset is not linked to a valid test instance. Please check in LTP if you have selected an existing test instance for the preset");
                    res.setAllParamsPresent(false);
                }
            }
        }
        res.setExceptionMessage(sb.toString());
        if (res.isAllParamsPresent())
        {
            loadtestMetadata = new LoadtestMetadata();
            loadtestMetadata.setApiToken(authToken);
            loadtestMetadata.setPresetTestInstanceId(testInstanceId);
            loadtestMetadata.setEnvironmentType(environmentType.name());
        }
        return res;
    }

    private BuildFinishedStatus runApicaSelfServiceJob(TeamCityLoadTestLogger logger, LoadTestParameters params,
            List<Threshold> absoluteThresholds, List<Threshold> relativeThresholds, LtpEnvironmentType environmentType)
    {
        logger.message("Attempting to initiate load test...");

        SelfServiceStatistics statistics = null;
        if (relativeThresholds != null && relativeThresholds.size() > 0)
        {
            logger.message("There's at least one relative threshold saved. Will now try to extract the load test result of the previous successful load test.");
            SelfServiceStatisticsOfPreset testResultOfPreviousSuccessfulBuild = extractTestResultOfPreviousSuccessfulBuild(logger);
            if (testResultOfPreviousSuccessfulBuild != null)
            {
                statistics = testResultOfPreviousSuccessfulBuild.getStatistics();
            }
        }
        String loadtestPresetName = params.get(LtpSelfServiceConstants.SETTINGS_LTP_PRESET_NAME, "");
        String loadtestFileName = params.get(LtpSelfServiceConstants.SETTINGS_LTP_RUNNABLE_FILE, "");
        String authToken = params.get(LtpSelfServiceConstants.SETTINGS_LTP_API_AUTH_TOKEN, "");
        BuildFinishedStatus status = BuildFinishedStatus.FINISHED_SUCCESS;
        LoadtestJobSummaryResponse summaryResponseIfTestPasses = null;
        try
        {
            String scheme = LtpSelfServiceConstants.LTP_WEB_SERVICE_SCHEME;
            String version = EnvironmentFactory.getLtpWebServiceVersion(environmentType);
            String sep = LtpSelfServiceConstants.URL_SEPARATOR;
            int port = LtpSelfServiceConstants.LTP_WEB_SERVICE_PORT;
            String ltpBaseUri = EnvironmentFactory.getLtpWebServiceBaseUrl(environmentType);
            String startByPresetUriExtension = LtpSelfServiceConstants.LTP_WEB_SERVICE_JOBS_BY_PRESET_ENDPOINT;
            String jobStatusExtension = LtpSelfServiceConstants.LTP_WEB_SERVICE_JOBS_ENDPOINT;
            String tokenExtension = LtpSelfServiceConstants.LTP_WEB_SERVICE_AUTH_TOKEN_QUERY_STRING.concat("=").concat(authToken);
            URI startByPresetUri = new URI(scheme, null, ltpBaseUri, port, sep.concat(version).concat(sep)
                    .concat(startByPresetUriExtension), tokenExtension, null);
            TransmitJobRequestArgs transmitJobRequestArgs = new TransmitJobRequestArgs();
            transmitJobRequestArgs.setFileName(loadtestFileName);
            transmitJobRequestArgs.setLtpApiEndpoint(startByPresetUri);
            transmitJobRequestArgs.setAuthToken(authToken);
            transmitJobRequestArgs.setPresetName(loadtestPresetName);
            StartJobByPresetResponse startByPresetResponse = transmitJob(transmitJobRequestArgs);
            if (startByPresetResponse.getJobId() > 0)
            {
                int jobId = startByPresetResponse.getJobId();
                logger.message("Successfully inserted job. Job id: ".concat(Integer.toString(jobId)));
                JobStatusRequest jobStatusRequest = new JobStatusRequest();
                URI jobStatusUri = new URI(scheme, null, ltpBaseUri,
                        port, sep.concat(version).concat(sep).concat(jobStatusExtension)
                        .concat(sep).concat(Integer.toString(jobId)), tokenExtension, null);
                jobStatusRequest.setJobId(jobId);
                jobStatusRequest.setLtpApiEndpoint(jobStatusUri);
                jobStatusRequest.setAuthToken(authToken);
                JobStatusResponse jobStatus = checkJobResponse(jobStatusRequest);
                logJobStatus(jobStatus, logger);
                while (!jobStatus.isJobCompleted())
                {
                    jobStatus = checkJobResponse(jobStatusRequest);
                    logJobStatus(jobStatus, logger);
                    try
                    {
                        Thread.sleep(LtpSelfServiceConstants.LTP_LOADTEST_STATUS_CHECK_INTERVAL_SECONDS * 1000);
                    } catch (InterruptedException ex)
                    {
                    }
                }

                if (jobStatus.isCompleted() && !jobStatus.isJobFaulted())
                {
                    logger.message("Job has finished normally. Will now attempt to retrieve some job statistics.");
                    URI jobSummaryUri = new URI(scheme, null, ltpBaseUri, port,
                            (sep).concat(version).concat(sep).concat(jobStatusExtension)
                            .concat(sep).concat(Integer.toString(jobId).concat(sep)
                                    .concat(LtpSelfServiceConstants.LTP_WEB_SERVICE_JOB_STATISTICS_ENDPOINT)), tokenExtension, null);
                    LoadtestJobSummaryRequest summaryRequest = new LoadtestJobSummaryRequest();
                    summaryRequest.setJobId(jobId);
                    summaryRequest.setLtpApiEndpoint(jobSummaryUri);
                    summaryRequest.setAuthToken(authToken);
                    LoadtestJobSummaryResponse summaryResponse = getJobSummaryResponse(summaryRequest);
                    logJobSummary(summaryResponse, logger);
                    saveJobSummary(summaryResponse, logger, loadtestPresetName);
                    summaryResponseIfTestPasses = summaryResponse;
                    try
                    {
                        logger.message("Storing load test metadata...");
                        storeLoadtestMetadata(loadtestMetadata);
                    } catch (IOException ioe)
                    {
                        logger.failure("Unable to save loadtest metadata: ".concat(ioe.getMessage()));
                    }

                    if (absoluteThresholds.size() > 0)
                    {
                        logger.message("Evaluating absolute threshold values...");
                        ThresholdEvaluationResult absoluteThresholdEvaluation = evaluateAbsoluteThreshold(summaryResponse, absoluteThresholds);
                        if (absoluteThresholdEvaluation.isThresholdBroken())
                        {
                            logger.failure(absoluteThresholdEvaluation.toString());
                            status = BuildFinishedStatus.FINISHED_FAILED;
                        } else
                        {
                            logger.message(absoluteThresholdEvaluation.toString());
                        }
                    }

                    if (relativeThresholds.size() > 0)
                    {
                        if (statistics != null)
                        {
                            logger.message("Evaluating relative threshold values...");

                            ThresholdEvaluationResult relativeThresholdEvaluation = evaluateRelativeThreshold(summaryResponse.getPerformanceSummary(), statistics, relativeThresholds);
                            if (relativeThresholdEvaluation.isThresholdBroken())
                            {
                                logger.failure(relativeThresholdEvaluation.toString());
                                status = BuildFinishedStatus.FINISHED_FAILED;
                            } else
                            {
                                logger.message(relativeThresholdEvaluation.toString());
                            }
                        } else
                        {
                            logger.message("Cannot evaluate relative thresholds due to previous exception. Check the build log above.");
                        }

                    }

                } else if (jobStatus.isJobFaulted())
                {
                    logger.failure("Job has finished with an error: ".concat(jobStatus.getStatusMessage()));
                    status = BuildFinishedStatus.FINISHED_FAILED;
                }

            } else
            {
                logger.failure(startByPresetResponse.getException());
                status = BuildFinishedStatus.FINISHED_FAILED;
            }

        } catch (URISyntaxException ex)
        {
            logger.failure(ex.getMessage());
            status = BuildFinishedStatus.FINISHED_FAILED;
        }

        if (status == BuildFinishedStatus.FINISHED_SUCCESS)
        {
            String statusMessage = String.format("##teamcity[buildStatus status='%s' text='%s']", "SUCCESS", "Success");
            if (summaryResponseIfTestPasses != null)
            {
                statusMessage = String.format("##teamcity[buildStatus status='%s' text='%s']", "SUCCESS", "Success: ".concat(summaryResponseIfTestPasses.toStatusMessageString()));
            }

            BuildProgressLogger buildLogger = this.build.getBuildLogger();
            buildLogger.logMessage(DefaultMessagesInfo.createTextMessage(statusMessage));
        }
        return status;
    }

    private void logJobStatus(JobStatusResponse jobStatusResponse, TeamCityLoadTestLogger logger)
    {
        if (jobStatusResponse.getException().equals(""))
        {
            logger.message(jobStatusResponse.toString());
        } else
        {
            logger.message("Exception when retrieving job status: " + jobStatusResponse.getException());
        }
    }

    private void saveJobSummary(LoadtestJobSummaryResponse jobSummaryResponse,
            TeamCityLoadTestLogger logger, String presetName)
    {
        logger.message("About to save the job statistics in an artifact...");
        try
        {
            SelfServiceStatistics stats = new SelfServiceStatistics();
            PerformanceSummary ps = jobSummaryResponse.getPerformanceSummary();
            stats.setAverageNetworkConnectTime(ps.getAverageNetworkConnectTime());
            stats.setAverageNetworkThroughput(ps.getAverageNetworkThroughput());
            stats.setAverageResponseTimePerLoop(ps.getAverageResponseTimePerLoop());
            stats.setAverageResponseTimePerPage(ps.getAverageResponseTimePerPage());
            stats.setAverageSessionTimePerLoop(ps.getAverageSessionTimePerLoop());
            stats.setDateOfInsertion(new Date());
            stats.setNetworkThroughputUnit(ps.getNetworkThroughputUnit());
            stats.setTotalFailedLoops(ps.getTotalFailedLoops());
            stats.setTotalHttpCalls(ps.getTotalHttpCalls());
            stats.setTotalPassedLoops(ps.getTotalPassedLoops());
            stats.setTotalTransmittedBytes(ps.getTotalTransmittedBytes());
            stats.setWebTransactionRate(ps.getWebTransactionRate());
            SelfServiceStatisticsOfPreset presetStatistics = new SelfServiceStatisticsOfPreset();
            presetStatistics.setPresetName(presetName);
            presetStatistics.setStatistics(stats);
            presetStatistics.setJobId(jobSummaryResponse.getJobId());
            presetStatistics.setLinkToTestResult(jobSummaryResponse.getLinkToTestResults());
            File f = storeLoadtestResults(presetStatistics);
            logger.message("Saved the load test results on the agent repository: " + f.getAbsolutePath());

        } catch (Exception ex)
        {
            logger.message("Failed to save the loadtest statistics: " + ex.getMessage());
        }
    }

    private File storeLoadtestMetadata(LoadtestMetadata loadtestMetadata) throws IOException
    {
        File buildDir = this.build.getBuildTempDirectory();
        File metadataFile = new File(buildDir, "load-test-metadata.txt");
        FileWriter fileWriter = new FileWriter(metadataFile);
        if (!metadataFile.exists())
        {
            metadataFile.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(fileWriter);
        Gson gson = new Gson();
        String jsonified = gson.toJson(loadtestMetadata);
        bw.write(jsonified);
        bw.close();
        this.artifactsWatcher.addNewArtifactsPath(metadataFile.getAbsolutePath());
        return metadataFile;
    }

    private File storeLoadtestResults(SelfServiceStatisticsOfPreset presetStatistics) throws IOException
    {
        File buildDir = this.build.getBuildTempDirectory();
        File resultsFile = new File(buildDir, "load-test-results.txt");
        FileWriter fileWriter = new FileWriter(resultsFile);
        if (!resultsFile.exists())
        {
            resultsFile.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(fileWriter);
        Gson gson = new Gson();
        String jsonified = gson.toJson(presetStatistics);
        bw.write(jsonified);
        bw.close();
        this.artifactsWatcher.addNewArtifactsPath(resultsFile.getAbsolutePath());
        return resultsFile;
    }

    private void logJobSummary(LoadtestJobSummaryResponse jobSummaryResponse, TeamCityLoadTestLogger logger)
    {
        if (jobSummaryResponse.getException().equals(""))
        {
            logger.message(jobSummaryResponse.toString());
        } else
        {
            logger.message("Exception when retrieving job summary statistics: " + jobSummaryResponse.getException());
        }
    }

    private ThresholdEvaluationResult evaluateRelativeThreshold(PerformanceSummary performanceSummaryOfCurrentJob, SelfServiceStatistics statsOfPreviousLoadtest, List<Threshold> relativeThresholds)
    {
        ThresholdEvaluationResult res = new ThresholdEvaluationResult();

        StringBuilder rawOutputBuilder = new StringBuilder();
        for (Threshold relativeThreshold : relativeThresholds)
        {
            rawOutputBuilder.append("Relative threshold ").append(relativeThreshold.toRelativeThresholdString()).append("\r\n");
            StandardMetricResult.Metrics metric = relativeThreshold.getMetric();
            int thresholdValue = relativeThreshold.getThresholdValue();
            Operator operator = relativeThreshold.getOperator();
            if (metric == StandardMetricResult.Metrics.average_page_response_time)
            {
                double newAverageResponseTimePerPage = performanceSummaryOfCurrentJob.getAverageResponseTimePerPage();
                double previousAverageResponseTimePerPage = statsOfPreviousLoadtest.getAverageResponseTimePerPage();

                double diffAverageResponseTimePerPage = (newAverageResponseTimePerPage - previousAverageResponseTimePerPage);
                double percChangeAverageResponseTimePerPage = 100.0 * (diffAverageResponseTimePerPage / previousAverageResponseTimePerPage);
                String description = ("Previous response time per page: ")
                        .concat(Double.toString(previousAverageResponseTimePerPage)).concat(", new response time per page: ")
                        .concat(Double.toString(newAverageResponseTimePerPage)).concat(", percentage change: ")
                        .concat(Double.toString(percChangeAverageResponseTimePerPage));
                rawOutputBuilder.append(description);
                switch (operator)
                {
                    case greaterThan:
                        if (thresholdValue < percChangeAverageResponseTimePerPage)
                        {
                            res.setThresholdBroken(true);
                            res.addThresholdExceededDescription(description.concat(". Outcome: FAILED"));
                        } else
                        {
                            res.addThresholdPassedDescription(description.concat(". Outcome: PASSED"));
                        }
                        break;
                    case lessThan:
                        if (thresholdValue > percChangeAverageResponseTimePerPage)
                        {
                            res.setThresholdBroken(true);
                            res.addThresholdExceededDescription(description.concat(". Outcome: FAILED"));
                        } else
                        {
                            res.addThresholdPassedDescription(description.concat(". Outcome: PASSED"));
                        }
                        break;
                }
            }

            if (metric == StandardMetricResult.Metrics.failure_rate)
            {
                int newPassedLoops = performanceSummaryOfCurrentJob.getTotalPassedLoops();
                int newFailedLoops = performanceSummaryOfCurrentJob.getTotalFailedLoops();
                int newTotalLoops = newPassedLoops + newFailedLoops;
                double newFailedLoopsShare = (double) (newFailedLoops * 100.0) / (double) (newTotalLoops * 100.0);
                int previousPassedLoops = performanceSummaryOfCurrentJob.getTotalPassedLoops();
                int previousFailedLoops = performanceSummaryOfCurrentJob.getTotalFailedLoops();
                int previousTotalLoops = previousPassedLoops + previousFailedLoops;
                double previousFailedLoopsShare = (double) (previousFailedLoops * 100.0) / (double) (previousTotalLoops * 100.0);
                double percChangeFailedLoops = newFailedLoopsShare - previousFailedLoopsShare;

                String description = ("Previous failed loops share: ")
                        .concat(Double.toString(previousFailedLoopsShare)).concat("% , new failed loops share: ")
                        .concat(Double.toString(newFailedLoopsShare)).concat("%, percentage change: ").concat(Double.toString(percChangeFailedLoops));
                rawOutputBuilder.append(description);
                switch (operator)
                {
                    case greaterThan:
                        if (thresholdValue < percChangeFailedLoops)
                        {
                            res.setThresholdBroken(true);
                            res.addThresholdExceededDescription(description.concat(". Outcome: FAILED"));
                        } else
                        {
                            res.addThresholdPassedDescription(description.concat(". Outcome: PASSED"));
                        }
                        break;
                    case lessThan:
                        if (thresholdValue > percChangeFailedLoops)
                        {
                            res.setThresholdBroken(true);
                            res.addThresholdExceededDescription(description.concat(". Outcome: FAILED"));
                        } else
                        {
                            res.addThresholdPassedDescription(description.concat(". Outcome: PASSED"));
                        }
                        break;
                }
            }
        }
        res.setRawEvaluationResult(rawOutputBuilder.toString());
        return res;
    }

    private ThresholdEvaluationResult evaluateAbsoluteThreshold(LoadtestJobSummaryResponse jobSummary, List<Threshold> absoluteThresholds)
    {
        ThresholdEvaluationResult res = new ThresholdEvaluationResult();
        res.setThresholdBroken(false);
        StringBuilder rawOutputBuilder = new StringBuilder();
        for (Threshold threshold : absoluteThresholds)
        {
            rawOutputBuilder.append("Absolute threshold ").append(threshold.toString()).append("\r\n");

            StandardMetricResult.Metrics metric = threshold.getMetric();
            int thresholdValue = threshold.getThresholdValue();
            Operator operator = threshold.getOperator();
            if (metric == StandardMetricResult.Metrics.average_page_response_time)
            {
                double averageResponseTimePerPage = jobSummary.getPerformanceSummary().getAverageResponseTimePerPage();
                int responseTimePerPageMillis = (int) (averageResponseTimePerPage * 1000);

                rawOutputBuilder.append("responseTimePerPageMillis: ")
                        .append(Integer.toString(responseTimePerPageMillis)).append("\r\n");

                switch (operator)
                {
                    case greaterThan:
                        if (thresholdValue < responseTimePerPageMillis)
                        {
                            res.setThresholdBroken(true);
                            String description = "Actual response time per page "
                                    .concat(Integer.toString(responseTimePerPageMillis))
                                    .concat(" exceeds threshold value of ").concat(Integer.toString(thresholdValue))
                                    .concat(" ms. Outcome: FAILED");
                            res.addThresholdExceededDescription(description);
                        } else
                        {
                            String description = "Actual response time per page "
                                    .concat(Integer.toString(responseTimePerPageMillis))
                                    .concat(" is lower than threshold value of ").concat(Integer.toString(thresholdValue))
                                    .concat(" ms. Outcome: PASSED");
                            res.addThresholdPassedDescription(description);
                        }
                        break;
                    case lessThan:
                        if (thresholdValue > responseTimePerPageMillis)
                        {
                            res.setThresholdBroken(true);
                            String description = "Actual response time per page "
                                    .concat(Integer.toString(responseTimePerPageMillis))
                                    .concat("ms is lower than threshold of ").concat(Integer.toString(thresholdValue))
                                    .concat(" ms. Outcome: FAILED");
                            res.addThresholdExceededDescription(description);
                        } else
                        {
                            String description = "Actual response time per page "
                                    .concat(Integer.toString(responseTimePerPageMillis))
                                    .concat("ms exceeds threshold of ").concat(Integer.toString(thresholdValue))
                                    .concat(" ms. Outcome: PASSED");
                            res.addThresholdPassedDescription(description);
                        }
                        break;
                }
            }

            if (metric == StandardMetricResult.Metrics.failure_rate)
            {
                int passedLoops = jobSummary.getPerformanceSummary().getTotalPassedLoops();
                int failedLoops = jobSummary.getPerformanceSummary().getTotalFailedLoops();
                int totalLoops = passedLoops + failedLoops;
                double failedLoopsShare = (double) (failedLoops * 100.0) / (double) (totalLoops * 100.0);

                rawOutputBuilder.append("failedLoopsShare: ").append(Double.toString(failedLoopsShare))
                        .append("\r\n");

                switch (operator)
                {
                    case greaterThan:
                        if (thresholdValue < failedLoopsShare)
                        {
                            res.setThresholdBroken(true);
                            String description = "Actual failed loops rate "
                                    .concat(Double.toString(failedLoopsShare))
                                    .concat(" exceeds threshold value of ").concat(Integer.toString(thresholdValue))
                                    .concat(" %. Outcome: FAILED");
                            res.addThresholdExceededDescription(description);
                        } else
                        {
                            String description = "Actual failed loops rate "
                                    .concat(Double.toString(failedLoopsShare))
                                    .concat(" is lower than threshold value of ").concat(Integer.toString(thresholdValue))
                                    .concat(" %. Outcome: PASSED");
                            res.addThresholdPassedDescription(description);
                        }
                        break;
                    case lessThan:
                        if (thresholdValue > failedLoopsShare)
                        {
                            res.setThresholdBroken(true);
                            String description = "Actual failed loops rate "
                                    .concat(Double.toString(failedLoopsShare))
                                    .concat(" is lower than threshold value of ").concat(Integer.toString(thresholdValue))
                                    .concat(" %. Outcome: FAILED");
                            res.addThresholdExceededDescription(description);
                        } else
                        {
                            String description = "Actual failed loops rate "
                                    .concat(Double.toString(failedLoopsShare))
                                    .concat(" is lower than threshold value of ").concat(Integer.toString(thresholdValue))
                                    .concat(" %. Outcome: PASSED");
                            res.addThresholdPassedDescription(description);
                        }
                        break;
                }
            }
        }

        res.setRawEvaluationResult(rawOutputBuilder.toString());
        return res;
    }

    private LoadtestJobSummaryResponse getJobSummaryResponse(LoadtestJobSummaryRequest jobSummaryRequest)
    {
        LoadtestJobSummaryResponse resp = new LoadtestJobSummaryResponse();
        resp.setException("");
        HttpUrlConnectionArguments connectionArgs = new HttpUrlConnectionArguments();
        connectionArgs.setWebMethod("GET");
        connectionArgs.setAuthToken(jobSummaryRequest.getAuthToken());
        connectionArgs.setUri(jobSummaryRequest.getLtpApiEndpoint());
        Gson gson = new Gson();
        try
        {
            HttpURLConnection con = buildUrlRequest(connectionArgs);
            int responseCode = con.getResponseCode();
            if (responseCode < 300)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                resp = gson.fromJson(response.toString(), LoadtestJobSummaryResponse.class);
            } else
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                resp.setException(response.toString());
            }
        } catch (Exception ex)
        {
            resp.setException(ex.getMessage());
        }
        return resp;
    }

    private JobStatusResponse checkJobResponse(JobStatusRequest jobStatusRequest)
    {
        JobStatusResponse resp = new JobStatusResponse();
        resp.setException("");

        HttpUrlConnectionArguments connectionArgs = new HttpUrlConnectionArguments();
        connectionArgs.setWebMethod("GET");
        connectionArgs.setAuthToken(jobStatusRequest.getAuthToken());
        connectionArgs.setUri(jobStatusRequest.getLtpApiEndpoint());
        Gson gson = new Gson();
        try
        {
            HttpURLConnection con = buildUrlRequest(connectionArgs);
            int responseCode = con.getResponseCode();
            if (responseCode < 300)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                resp = gson.fromJson(response.toString(), JobStatusResponse.class);
            } else
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                resp.setException(response.toString());
            }
        } catch (JsonSyntaxException ex)
        {
            resp.setException("Json exception: ".concat(ex.getMessage()));
        } catch (IOException ex)
        {
            resp.setException(ex.getMessage());
        }
        return resp;
    }

    private StartJobByPresetResponse transmitJob(TransmitJobRequestArgs transmitJobArgs)
    {
        StartJobByPresetResponse resp = new StartJobByPresetResponse();
        resp.setException("");
        resp.setJobId(-1);
        HttpUrlConnectionArguments connectionArgs = new HttpUrlConnectionArguments();
        connectionArgs.setContentType("application/json");
        connectionArgs.setWebMethod("POST");
        connectionArgs.setAuthToken(transmitJobArgs.getAuthToken());
        Gson gson = new com.google.gson.Gson();
        TransmitJobPresetArgs presetArgs = new TransmitJobPresetArgs(transmitJobArgs.getPresetName(), transmitJobArgs.getFileName());
        String jsonifiedPresetArgs = gson.toJson(presetArgs);
        connectionArgs.setRequestBody(jsonifiedPresetArgs);
        connectionArgs.setUri(transmitJobArgs.getLtpApiEndpoint());

        try
        {
            HttpURLConnection con = buildUrlRequest(connectionArgs);
            int responseCode = con.getResponseCode();
            if (responseCode < 300)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }

                resp = gson.fromJson(response.toString(), StartJobByPresetResponse.class);
            } else
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                resp.setException(response.toString());
            }
        } catch (Exception ex)
        {
            resp.setException(ex.getMessage());
        }

        return resp;
    }

    private HttpURLConnection buildUrlRequest(HttpUrlConnectionArguments connectionArguments) throws MalformedURLException, IOException
    {
        HttpURLConnection connection = null;
        URL url = connectionArguments.getUri().toURL();
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(connectionArguments.getWebMethod());
        if (!Utils.isBlank(connectionArguments.getContentType()))
        {
            connection.setRequestProperty("Content-Type", connectionArguments.getContentType());
        }
        if (!Utils.isBlank(connectionArguments.getRequestBody()))
        {
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(connectionArguments.getRequestBody());
            wr.flush();
            wr.close();
        }
        return connection;
    }

    private String getExceptionStacktrace(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    private Document loadXMLFromString(String xml) throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    private String getBuilderAuthBase64()
    {
        String buildPassword = this.build.getAccessCode();
        String buildUsername = build.getAccessUser();
        String basicAuthString = buildUsername.concat(":").concat(buildPassword);
        byte[] authStringBytes = basicAuthString.getBytes();
        byte[] authStringBytesBase64 = Base64.encodeBase64(authStringBytes);
        String authStringBytesBase64Stringified = new String(authStringBytesBase64);
        return authStringBytesBase64Stringified;
    }

    private String executeCallToTeamcityRestApi(String teamcityRestUrl, String basicAuthString) throws MalformedURLException, IOException
    {
        URL url = new URL(teamcityRestUrl);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic " + basicAuthString);
        InputStream is = urlConnection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);

        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuilder latestBuildXmlBuilder = new StringBuilder();
        while ((numCharsRead = isr.read(charArray)) > 0)
        {
            latestBuildXmlBuilder.append(charArray, 0, numCharsRead);
        }
        String response = latestBuildXmlBuilder.toString();
        return response;
    }

    private String getLatestBuildGetterUrl()
    {
        BuildAgentConfiguration agentConfiguration = this.build.getAgentConfiguration();
        String serverUrl = agentConfiguration.getServerUrl();
        String buildTypeId = this.build.getBuildTypeId();
        String projectNameForUrl = prepareUrlEndodedProjectName(this.build.getProjectName());
        String status = "SUCCESS";
        String teamcityRestUrl = serverUrl.concat("/httpAuth/app/rest/builds/buildType:").concat(buildTypeId)
                .concat(",project:").concat(projectNameForUrl).concat(",status:").concat(status);
        return teamcityRestUrl;
    }

    private String getArtifactsGetterLink(String artifactsHref)
    {
        BuildAgentConfiguration agentConfiguration = this.build.getAgentConfiguration();
        String serverUrl = agentConfiguration.getServerUrl();
        String teamcityRestUrl = serverUrl.concat(artifactsHref);
        return teamcityRestUrl;
    }

    private String getArtifactFileDownloaderLink(String artifactFileHref)
    {
        BuildAgentConfiguration agentConfiguration = this.build.getAgentConfiguration();
        String serverUrl = agentConfiguration.getServerUrl();
        String teamcityRestUrl = serverUrl.concat(artifactFileHref);
        return teamcityRestUrl;
    }

    private String getLatestBuildArtifactHref(String latestBuildXml) throws ParserConfigurationException, SAXException, IOException
    {
        Document document = loadXMLFromString(latestBuildXml);
        document.getDocumentElement().normalize();
        NodeList elementsByTagName = document.getElementsByTagName("artifacts");
        if (elementsByTagName != null)
        {
            int length = elementsByTagName.getLength();
            if (length >= 1)
            {
                Node item = elementsByTagName.item(0);
                Element element = (Element) item;
                String artifactHref = element.getAttribute("href");
                return artifactHref;
            }
        }

        return null;
    }

    private String getLatestTestResultDownloadLink(String artifactFilesXml) throws ParserConfigurationException, SAXException, IOException
    {
        Document document = loadXMLFromString(artifactFilesXml);
        document.getDocumentElement().normalize();
        NodeList fileElements = document.getElementsByTagName("file");
        if (fileElements != null)
        {
            int length = fileElements.getLength();
            for (int i = 0; i < length; i++)
            {
                Node node = fileElements.item(i);
                Element element = (Element) node;
                String artifactFilename = element.getAttribute("name");
                if (artifactFilename.equals("load-test-results.txt"))
                {
                    NodeList childNodesOfFile = node.getChildNodes();
                    for (int z = 0; z < childNodesOfFile.getLength(); z++)
                    {
                        Node currentNode = childNodesOfFile.item(z);
                        if (currentNode.getNodeName().equals("content"))
                        {
                            Element hrefElement = (Element) currentNode;
                            String artifactDownloadLink = hrefElement.getAttribute("href");
                            return artifactDownloadLink;
                        }
                    }
                }
            }
        }

        return null;
    }
    
    private String prepareUrlEndodedProjectName(String projectName)
    {
        String prepared = projectName;
        
        for (int i = 0; i < charsToBeUrlEncoded.length; i++)
        {
            String s = charsToBeUrlEncoded[i];
            String repl = encodedUrlChars[i];
            if (prepared.indexOf(s) > -1)
            {
                prepared = prepared.replace(s, repl);
            }
        }
        return prepared;
    }
        
}
