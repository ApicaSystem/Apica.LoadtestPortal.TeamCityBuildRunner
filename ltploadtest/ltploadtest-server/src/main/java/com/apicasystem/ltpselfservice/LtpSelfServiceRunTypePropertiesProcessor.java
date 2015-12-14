package com.apicasystem.ltpselfservice;

import com.apicasystem.ltpselfservice.resources.LtpEnvironmentType;
import com.apicasystem.ltpselfservice.resources.Threshold;
import com.apicasystem.ltpselfservice.resources.ThresholdType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.util.PropertiesUtil;

public class LtpSelfServiceRunTypePropertiesProcessor implements PropertiesProcessor
{
    private List<Threshold> relativeThresholds;
    private List<Threshold> absoluteThresholds;
    private LtpEnvironmentType environmentType;

    public Collection<InvalidProperty> process(Map<String, String> properties)
    {
        List<InvalidProperty> result = new ArrayList<InvalidProperty>();
        final String authToken = properties.get(LtpSelfServiceConstants.SETTINGS_LTP_API_AUTH_TOKEN);
        final String presetName = properties.get(LtpSelfServiceConstants.SETTINGS_LTP_PRESET_NAME);
        final String runnableFileName = properties.get(LtpSelfServiceConstants.SETTINGS_LTP_RUNNABLE_FILE);

        try
        {
            environmentType = ApicaSettings.instance().parseEnvironmentType(properties);
        } catch (Exception ex)
        {
            result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_ENVIRONMENT, ex.getMessage()));
        }

        try
        {
            this.absoluteThresholds = ApicaSettings.instance().parseThresholds(properties, ThresholdType.Absolute);
        } catch (Exception ex)
        {
            result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_THRESHOLD_SETTINGS, ex.getMessage()));
        }
        
        try
        {
            this.relativeThresholds = ApicaSettings.instance().parseThresholds(properties, ThresholdType.Relative);
        }
        catch (Exception ex)
        {
            result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_RELATIVE_THRESHOLD_SETTINGS, ex.getMessage()));
        }

        if (PropertiesUtil.isEmptyOrNull(authToken))
        {
            result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_API_AUTH_TOKEN, "LTP user must have an authentication token."));
        }
        if (PropertiesUtil.isEmptyOrNull(presetName))
        {
            result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_PRESET_NAME, "Please provide load test preset name."));
        }
        if (PropertiesUtil.isEmptyOrNull(runnableFileName))
        {
            result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_RUNNABLE_FILE, "Please provide a valid load test file name."));
        } else
        {
            if (!fileIsZip(runnableFileName) && !fileIsClass(runnableFileName))
            {
                result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_RUNNABLE_FILE, "A load test file is either a .class or a .zip file."));
            }
        }

        if (result.size() == 0)
        {
            ServerSideLtpApiWebService serverSideService = new ServerSideLtpApiWebService(environmentType);
            PresetResponse presetResponse = serverSideService.checkPreset(authToken, presetName);
            if (!presetResponse.isPresetExists())
            {
                if (presetResponse.getException() != null && !presetResponse.getException().equals(""))
                {
                    result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_PRESET_NAME, presetResponse.getException()));
                } else
                {
                    result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_PRESET_NAME, "No such preset found."));
                }
            } else //validate test instance id
            {
                if (presetResponse.getTestInstanceId() < 1)
                {
                    result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_PRESET_TESTINSTANCE_ID, "The preset is not linked to a valid test instance. Please check in LTP if you have selected an existing test instance for the preset."));
                }
            }
            RunnableFileResponse runnableFileResponse = serverSideService.checkRunnableFile(authToken, runnableFileName);
            if (!runnableFileResponse.isFileExists())
            {
                if (runnableFileResponse.getException() != null && !runnableFileResponse.getException().equals(""))
                {
                    result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_RUNNABLE_FILE, runnableFileResponse.getException()));
                } else
                {
                    result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_RUNNABLE_FILE, "No such load test file found."));
                }
            }
        }

        return result;
    }

    private boolean fileIsZip(String fileName)
    {
        return fileName.endsWith(".zip");
    }

    private boolean fileIsClass(String fileName)
    {
        return fileName.endsWith(".class");
    }

}
