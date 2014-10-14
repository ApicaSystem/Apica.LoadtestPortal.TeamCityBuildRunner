package com.apicasystem.ltpselfservice;

import com.apicasystem.ltpselfservice.resources.LtpEnvironmentType;

public class LoadtestEnvironmentVariables
{

    public static String ltpWebServiceBaseUrl(LtpEnvironmentType environmentType)
    {
        return EnvironmentFactory.getLtpWebServiceBaseUrl(environmentType);
    }

    public static String ltpWebServiceVersion(LtpEnvironmentType environmentType)
    {
        return EnvironmentFactory.getLtpWebServiceVersion(environmentType);
    }

    public static String ltpRoot(LtpEnvironmentType environmentType)
    {
        return EnvironmentFactory.getLoadtestPortalRoot(environmentType);
    }
}
