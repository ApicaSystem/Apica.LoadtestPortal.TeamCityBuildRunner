/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice;

/**
 *
 * @author andras.nemes
 */
public interface LtpSelfServiceConstants
{
    String SETTINGS_LTP_API_AUTH_TOKEN = "apica.selfservice.auth.token";
    String SETTINGS_LTP_PRESET_NAME = "apica.selfservice.preset";
    String SETTINGS_LTP_RUNNABLE_FILE = "apica.selfservice.runnablefile";
    String SETTINGS_LTP_PRESET_TESTINSTANCE_ID = "apica.selfservice.preset.instanceid";
    String SETTINGS_LTP_THRESHOLD_SETTINGS = "apica.selfservice.thresholds";
    
    String RUNNER_TYPE = "LTP SelfService";
    String RUNNER_DISPLAY_NAME = "Apica Self Service";
    String RUNNER_DESCRIPTION = "Apica Self Service runner";
    
    String LTP_WEB_SERVICE_BASE_URL = EnvironmentFactory.getLtpWebServiceBaseUrl();
    String LTP_WEB_SERVICE_SCHEME = "http";
    String LTP_WEB_SERVICE_VERSION = EnvironmentFactory.getLtpWebServiceVersion();
    String LTP_WEB_SERVICE_PRESET_ENDPOINT = "selfservicepresets";
    String LTP_WEB_SERVICE_FILES_ENPOINT = "selfservicefiles";
    String LTP_WEB_SERVICE_JOBS_BY_PRESET_ENDPOINT = "selfservicejobs/preset";
    String LTP_WEB_SERVICE_JOBS_ENDPOINT = "selfservicejobs";
    String LTP_WEB_SERVICE_JOB_STATISTICS_ENDPOINT = "summary";
    String LTP_WEB_SERVICE_AUTH_TOKEN_QUERY_STRING = "token";
    int LTP_WEB_SERVICE_PORT = 80;
    int LTP_LOADTEST_STATUS_CHECK_INTERVAL_SECONDS = 10;
    String LOADTEST_PORTAL_ROOT = EnvironmentFactory.getLoadtestPortalRoot();
    String LOADTEST_PORTAL_CI_CONTROLLER = "ContinuousIntegrationTeamCity";
    
    String URL_SEPARATOR = "/";
}
