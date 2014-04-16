/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice;

import com.apicasystem.ltpselfservice.loadtest.*;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildRunnerContext;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author andras.nemes
 */
public class ApicaBuildProcess extends FutureBasedBuildProcess
{

    private final AgentRunningBuild build;
    private final BuildRunnerContext context;
    private final ArtifactsWatcher artifactsWatcher;

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
        logger.started("Apica Self Service Load Test");

        LoadTestParameters params = new LoadTestParameters(this.context.getRunnerParameters());
        JobParamsValidationResult validationResult = validateJobParams(params);
        if (!validationResult.isAllParamsPresent())
        {
            logger.failure(validationResult.getExceptionMessage());
            return BuildFinishedStatus.FINISHED_FAILED;
        }
        logger.message("Load test preset name: ".concat(params.get(LtpSelfServiceConstants.SETTINGS_LTP_PRESET_NAME, "")));
        logger.message("Load test file name: ".concat(params.get(LtpSelfServiceConstants.SETTINGS_LTP_RUNNABLE_FILE, "")));
        return runApicaSelfServiceJob(logger, params);
    }

    private JobParamsValidationResult validateJobParams(LoadTestParameters params)
    {
        JobParamsValidationResult res = new JobParamsValidationResult();
        StringBuilder sb = new StringBuilder();
        res.setAllParamsPresent(true);
        String loadtestPresetName = params.get(LtpSelfServiceConstants.SETTINGS_LTP_PRESET_NAME, "");
        String loadtestFileName = params.get(LtpSelfServiceConstants.SETTINGS_LTP_RUNNABLE_FILE, "");
        String username = params.get(LtpSelfServiceConstants.SETTINGS_LTP_USERNAME, "");
        String password = params.get(LtpSelfServiceConstants.SETTINGS_LTP_PASSWORD, "");

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
        if (username.equals(""))
        {
            sb.append("Unable to retrieve the LTP username. Please re-enter it on the setup page. ");
            res.setAllParamsPresent(false);
        }
        if (password.equals(""))
        {
            sb.append("Unable to retrieve the LTP password. Please re-enter it on the setup page. ");
            res.setAllParamsPresent(false);
        }

        res.setExceptionMessage(sb.toString());
        return res;
    }

    private BuildFinishedStatus runApicaSelfServiceJob(TeamCityLoadTestLogger logger, LoadTestParameters params)
    {
        logger.message("Attempting to initiate load test...");
        String loadtestPresetName = params.get(LtpSelfServiceConstants.SETTINGS_LTP_PRESET_NAME, "");
        String loadtestFileName = params.get(LtpSelfServiceConstants.SETTINGS_LTP_RUNNABLE_FILE, "");
        String username = params.get(LtpSelfServiceConstants.SETTINGS_LTP_USERNAME, "");
        String password = params.get(LtpSelfServiceConstants.SETTINGS_LTP_PASSWORD, "");
        BuildFinishedStatus status = BuildFinishedStatus.FINISHED_SUCCESS;

        try
        {
            String scheme = LtpSelfServiceConstants.LTP_WEB_SERVICE_SCHEME;
            String version = LtpSelfServiceConstants.LTP_WEB_SERVICE_VERSION;
            String sep = LtpSelfServiceConstants.URL_SEPARATOR;
            int port = LtpSelfServiceConstants.LTP_WEB_SERVICE_PORT;
            String ltpBaseUri = LtpSelfServiceConstants.LTP_WEB_SERVICE_BASE_URL;
            String startByPresetUriExtension = LtpSelfServiceConstants.LTP_WEB_SERVICE_JOBS_BY_PRESET_ENDPOINT;
            String jobStatusExtension = LtpSelfServiceConstants.LTP_WEB_SERVICE_JOBS_ENDPOINT;
            URI startByPresetUri = new URI(scheme, null, ltpBaseUri, port, sep.concat(version).concat(sep).concat(startByPresetUriExtension), null, null);
            TransmitJobRequestArgs transmitJobRequestArgs = new TransmitJobRequestArgs();
            transmitJobRequestArgs.setFileName(loadtestFileName);
            transmitJobRequestArgs.setLtpApiEndpoint(startByPresetUri);
            transmitJobRequestArgs.setPassword(password);
            transmitJobRequestArgs.setPresetName(loadtestPresetName);
            transmitJobRequestArgs.setUsername(username);
            StartJobByPresetResponse startByPresetResponse = transmitJob(transmitJobRequestArgs);
            if (startByPresetResponse.getJobId() > 0)
            {
                int jobId = startByPresetResponse.getJobId();
                logger.message("Successfully inserted job. Job id: ".concat(Integer.toString(jobId)));
                JobStatusRequest jobStatusRequest = new JobStatusRequest();
                URI jobStatusUri = new URI(scheme, null, ltpBaseUri,
                        port, sep.concat(version).concat(sep).concat(jobStatusExtension).concat(sep).concat(Integer.toString(jobId)), null, null);
                jobStatusRequest.setJobId(jobId);
                jobStatusRequest.setLtpApiEndpoint(jobStatusUri);
                jobStatusRequest.setPassword(password);
                jobStatusRequest.setUsername(username);
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
                            .concat(sep).concat(Integer.toString(jobId).concat(sep).concat(LtpSelfServiceConstants.LTP_WEB_SERVICE_JOB_STATISTICS_ENDPOINT)), null, null);
                    LoadtestJobSummaryRequest summaryRequest = new LoadtestJobSummaryRequest();
                    summaryRequest.setJobId(jobId);
                    summaryRequest.setLtpApiEndpoint(jobSummaryUri);
                    summaryRequest.setPassword(password);
                    summaryRequest.setUsername(username);
                    LoadtestJobSummaryResponse summaryResponse = getJobSummaryResponse(summaryRequest);
                    logJobSummary(summaryResponse, logger);
                    saveJobSummary(summaryResponse, logger, loadtestPresetName);

                } else if (jobStatus.isJobFaulted())
                {
                    logger.failure("Job has finished with an error: ".concat(jobStatus.getStatusMessage()));
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
            File f = storeLoadtestResults(presetStatistics);
            logger.message("Saved the load test results on the agent repository: " + f.getAbsolutePath());
            
        } catch (Exception ex)
        {
            logger.message("Failed to save the loadtest statistics: " + ex.getMessage());
        }
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

    private LoadtestJobSummaryResponse getJobSummaryResponse(LoadtestJobSummaryRequest jobSummaryRequest)
    {
        LoadtestJobSummaryResponse resp = new LoadtestJobSummaryResponse();
        resp.setException("");
        HttpUrlConnectionArguments connectionArgs = new HttpUrlConnectionArguments();
        connectionArgs.setWebMethod("GET");
        connectionArgs.setPassword(jobSummaryRequest.getPassword());
        connectionArgs.setUsername(jobSummaryRequest.getUsername());
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
        connectionArgs.setPassword(jobStatusRequest.getPassword());
        connectionArgs.setUsername(jobStatusRequest.getUsername());
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
        } catch (Exception ex)
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
        connectionArgs.setPassword(transmitJobArgs.getPassword());

        Gson gson = new com.google.gson.Gson();
        TransmitJobPresetArgs presetArgs = new TransmitJobPresetArgs(transmitJobArgs.getPresetName(), transmitJobArgs.getFileName());
        String jsonifiedPresetArgs = gson.toJson(presetArgs);
        connectionArgs.setRequestBody(jsonifiedPresetArgs);
        connectionArgs.setUri(transmitJobArgs.getLtpApiEndpoint());
        connectionArgs.setUsername(transmitJobArgs.getUsername());

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
        String auth = getBase64Credentials(connectionArguments.getUsername(), connectionArguments.getPassword());
        connection.setRequestProperty("Authorization", "Basic ".concat(auth));
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

    private String getBase64Credentials(String username, String password)
    {
        String auth = username.concat(":").concat(password);
        return new String(Base64.encodeBase64(auth.getBytes()));
    }
}
