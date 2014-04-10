/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.util.PropertiesUtil;

/**
 *
 * @author andras.nemes
 */
public class LtpSelfServiceTunTypePropertiesProcessor implements PropertiesProcessor
{
    public Collection<InvalidProperty> process(Map<String, String> properties)
    {
        List<InvalidProperty> result = new ArrayList<InvalidProperty>();
        final String username = properties.get(LtpSelfServiceConstants.SETTINGS_LTP_USERNAME);
        final String password = properties.get(LtpSelfServiceConstants.SETTINGS_LTP_PASSWORD);
        final String presetName = properties.get(LtpSelfServiceConstants.SETTINGS_LTP_PRESET_NAME);
        final String runnableFileName = properties.get(LtpSelfServiceConstants.SETTINGS_LTP_RUNNABLE_FILE);
        
        if (PropertiesUtil.isEmptyOrNull(username))
        {
            result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_USERNAME, "LTP user name must be specified."));            
        }
        if (PropertiesUtil.isEmptyOrNull(password))
        {
            result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_PASSWORD, "LTP password must be specified."));
        }
        if (PropertiesUtil.isEmptyOrNull(presetName))
        {
            result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_PRESET_NAME, "Please provide load test preset name."));
        }
        if (PropertiesUtil.isEmptyOrNull(runnableFileName))
        {
            result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_RUNNABLE_FILE, "Please provide a valid load test file name."));
        }
        else
        {
            if (!fileIsZip(runnableFileName) && !fileIsClass(runnableFileName))
            {
                result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_RUNNABLE_FILE, "A load test file is either a .class or a .zip file."));
            }
        }
        
        if (result.size() == 0)
        {
            ServerSideLtpApiWebService serverSideService = new ServerSideLtpApiWebService();
            PresetResponse presetResponse = serverSideService.checkPreset(username, password, presetName);
            if (!presetResponse.isPresetExists())
            {
                if (presetResponse.getException() != null && !presetResponse.getException().equals(""))
                {
                    result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_PRESET_NAME, presetResponse.getException()));
                }
                else
                {
                    result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_PRESET_NAME, "No such preset found."));
                }
            }
            RunnableFileResponse runnableFileResponse = serverSideService.checkRunnableFile(username, password, runnableFileName);
            if (!runnableFileResponse.isFileExists())
            {
                if (runnableFileResponse.getException() != null && !runnableFileResponse.getException().equals(""))
                {
                    result.add(new InvalidProperty(LtpSelfServiceConstants.SETTINGS_LTP_RUNNABLE_FILE, runnableFileResponse.getException()));
                }
                else
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
