package com.apicasystem.ltpselfservice;

import com.apicasystem.ltpselfservice.resources.LtpEnvironmentType;

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
        return Environment.PRODUCTION;
    }

    public static String getLtpWebServiceBaseUrl(LtpEnvironmentType environmentSetting)
    {
        Environment currentEnvironment = getCurrentEnvironment(environmentSetting);
        switch (currentEnvironment)
        {
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
        switch (currentEnvironment)
        {
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
        switch (currentEnvironment)
        {
            case PRODUCTION:
                return "https://loadtest.apicasystem.com/";
            case TRIAL:
                return "http://loadtest-trial.apicasystem.com/";
            default:
                return "https://loadtest.apicasystem.com/";
        }
    }
}
