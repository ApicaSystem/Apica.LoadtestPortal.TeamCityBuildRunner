/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice;

import com.apicasystem.ltpselfservice.resources.LtpEnvironmentType;

/**
 *
 * @author andras.nemes
 */
public class EnvironmentFactory
{
    private static Environment getCurrentEnvironment(LtpEnvironmentType environmentSetting)
    {
        switch (environmentSetting)
        {
            case production:
                return Environment.PRODUCTION;
            case trial:
                return Environment.TRIAL;
        }
        return Environment.ALPHA;
    }
    
    public static String getLtpWebServiceBaseUrl(LtpEnvironmentType environmentSetting)
    {
        Environment currentEnvironment = getCurrentEnvironment(environmentSetting);
        switch(currentEnvironment)
        {
            case ALPHA:
            case PRODUCTION:
                return "api-ltp.apicasystem.com";
            case TRIAL:
                return "api-ltp-trial.apicasystem.com";
            default:
                return "api-ltp.apicasystem.com";
        }
    }
    
    public static String getLtpWebServiceVersion(LtpEnvironmentType environmentSetting)
    {
        Environment currentEnvironment = getCurrentEnvironment(environmentSetting);
        switch(currentEnvironment)
        {
            case ALPHA:
                return "alpha";
            case PRODUCTION:
            case TRIAL:
                return "v1";
            default:
                return "v1";
        }
    }
    
    public static String getLoadtestPortalRoot(LtpEnvironmentType environmentSetting)
    {
        Environment currentEnvironment = getCurrentEnvironment(environmentSetting);
        switch(currentEnvironment)
        {
            //blah blah
            case ALPHA:
                return "http://loadtestalpha.apicasystem.com/";
            case PRODUCTION:
                return "http://loadtest.apicasystem.com/";
            case TRIAL:
                return "http://loadtest-trial.apicasystem.com/";
            default:
                return "http://loadtest.apicasystem.com/";
        }
    }
}
