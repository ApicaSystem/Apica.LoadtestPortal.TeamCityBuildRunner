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
