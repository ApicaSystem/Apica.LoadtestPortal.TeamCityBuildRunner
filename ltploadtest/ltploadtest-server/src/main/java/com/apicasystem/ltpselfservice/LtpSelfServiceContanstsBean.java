package com.apicasystem.ltpselfservice;

import org.jetbrains.annotations.NotNull;

public class LtpSelfServiceContanstsBean
{

    @NotNull
    public String getLtpApiAuthToken()
    {
        return LtpSelfServiceConstants.SETTINGS_LTP_API_AUTH_TOKEN;
    }

    @NotNull
    public String getLtpPresetName()
    {
        return LtpSelfServiceConstants.SETTINGS_LTP_PRESET_NAME;
    }

    @NotNull
    public String getLtpRunnableFileName()
    {
        return LtpSelfServiceConstants.SETTINGS_LTP_RUNNABLE_FILE;
    }

    @NotNull
    public String getLtpPresetTestInstanceId()
    {
        return LtpSelfServiceConstants.SETTINGS_LTP_PRESET_TESTINSTANCE_ID;
    }

    @NotNull
    public String getLtpThresholdSettings()
    {
        return LtpSelfServiceConstants.SETTINGS_LTP_THRESHOLD_SETTINGS;
    }

    @NotNull
    public String getLtpEnvironment()
    {
        return LtpSelfServiceConstants.SETTINGS_LTP_ENVIRONMENT;
    }
}
