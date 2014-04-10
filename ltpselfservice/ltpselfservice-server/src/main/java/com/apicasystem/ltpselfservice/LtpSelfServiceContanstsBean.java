/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice;
import org.jetbrains.annotations.NotNull;
import com.apicasystem.ltpselfservice.LtpSelfServiceConstants;

/**
 *
 * @author andras.nemes
 */
public class LtpSelfServiceContanstsBean
{
    @NotNull
    public String getLtpUserName()
    {
        return LtpSelfServiceConstants.SETTINGS_LTP_USERNAME;             
    }
    
    @NotNull
    public String getLtpPassword()
    {
        return LtpSelfServiceConstants.SETTINGS_LTP_PASSWORD;
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
    public String getLtpApiBaseHttpAddress()
    {
        return LtpSelfServiceConstants.LTP_WEB_SERVICE_BASE_URL;
    }
}
