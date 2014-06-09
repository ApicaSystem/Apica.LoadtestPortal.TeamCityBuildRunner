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
public class EnvironmentFactory
{
    private static Environment getCurrentEnvironment()
    {
        return Environment.ALPHA;
    }
    
    public static String getLtpWebServiceBaseUrl()
    {
        Environment currentEnvironment = getCurrentEnvironment();
        switch(currentEnvironment)
        {
            case ALPHA:
            case PRODUCTION:
                return "api-ltp.apicasystem.com";
            case TRIAL:
                return "api-ltp-aws.apicasystem.com";
            default:
                return "api-ltp.apicasystem.com";
        }
    }
    
    public static String getLtpWebServiceVersion()
    {
        Environment currentEnvironment = getCurrentEnvironment();
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
    
    public static String getLoadtestPortalRoot()
    {
        Environment currentEnvironment = getCurrentEnvironment();
        switch(currentEnvironment)
        {
            case ALPHA:
                return "http://loadtestalpha.apicasystem.com/";
            case PRODUCTION:
                return "http://loadtest.apicasystem.com/";
            case TRIAL:
                return "http://loadtestaws.apicasystem.com/";
            default:
                return "http://loadtest.apicasystem.com/";
        }
    }
}
