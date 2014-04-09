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
    String SETTINGS_LTP_USERNAME = "apica.selfservice.username";
    String SETTINGS_LTP_PASSWORD = "apica.selfservice.password";
    String SETTINGS_LTP_PRESET_NAME = "apica.selfservice.preset";
    String SETTINGS_LTP_RUNNABLE_FILE = "apica.selfservice.runnablefile";
    
    String RUNNER_TYPE = "LTP SelfService";
    String RUNNER_DISPLAY_NAME = "Apica Self Service";
    String RUNNER_DESCRIPTION = "Apica Self Service runner";
    
    String LTP_WEB_SERVICE_BASE_URL = "http://api-ltp-apicasystem.com/v1/";
}
