/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice;

import java.net.URI;
import java.net.URISyntaxException;
import com.google.gson.Gson;

/**
 *
 * @author andras.nemes
 */
public class ServerSideLtpApiWebService
{
    private CommonLtpWebApiService commonService;
    private final String scheme = LtpSelfServiceConstants.LTP_WEB_SERVICE_SCHEME;
    private final String baseUri = LtpSelfServiceConstants.LTP_WEB_SERVICE_BASE_URL;
    private final String version = LtpSelfServiceConstants.LTP_WEB_SERVICE_VERSION;
    private final String separator = LtpSelfServiceConstants.URL_SEPARATOR;
    private final int port = LtpSelfServiceConstants.LTP_WEB_SERVICE_PORT;

    public ServerSideLtpApiWebService()
    {
        commonService = new CommonLtpWebApiService();
    }

    public PresetResponse checkPreset(String authToken, String presetName)
    {
        String presetUriExtension = LtpSelfServiceConstants.LTP_WEB_SERVICE_PRESET_ENDPOINT;
        PresetResponse presetResponse = new PresetResponse();
        presetResponse.setException("");
        try
        {
            String tokenExtension = LtpSelfServiceConstants.LTP_WEB_SERVICE_AUTH_TOKEN_QUERY_STRING.concat("=").concat(authToken);
            URI presetUri = new URI(scheme, null, baseUri, port, separator.concat(version).concat(separator)
                    .concat(presetUriExtension).concat(separator).concat(presetName)
                    , tokenExtension, null);
            WebRequestOutcome presetRequestOutcome = commonService.makeWebRequest(presetUri);
            if (presetRequestOutcome.isWebRequestSuccessful())
            {
                if (presetRequestOutcome.getHttpResponseCode() < 300)
                {
                    Gson gson = new Gson();
                    presetResponse = gson.fromJson(presetRequestOutcome.getRawResponseContent(), PresetResponse.class);
                } else
                {
                    presetResponse.setException(presetRequestOutcome.getExceptionMessage());
                }
            } else
            {
                presetResponse.setException(presetRequestOutcome.getExceptionMessage());
            }
        } catch (URISyntaxException ex)
        {
            presetResponse.setException(ex.getMessage());
        }
        return presetResponse;
    }

    public RunnableFileResponse checkRunnableFile(String authToken, String fileName)
    {
        String fileUriExtension = LtpSelfServiceConstants.LTP_WEB_SERVICE_FILES_ENPOINT;
        RunnableFileResponse runnableFileResponse = new RunnableFileResponse();
        runnableFileResponse.setException("");
        try
        {
            String tokenExtension = LtpSelfServiceConstants.LTP_WEB_SERVICE_AUTH_TOKEN_QUERY_STRING.concat("=").concat(authToken);
            URI fileUri = new URI(scheme, null, baseUri, port, separator.concat(version).concat(separator)
                    .concat(fileUriExtension).concat(separator).concat(fileName)
                    , tokenExtension, null);
            WebRequestOutcome fileRequestOutcome = commonService.makeWebRequest(fileUri);
            if (fileRequestOutcome.isWebRequestSuccessful())
            {
                if (fileRequestOutcome.getHttpResponseCode() < 300)
                {
                    Gson gson = new Gson();
                    runnableFileResponse = gson.fromJson(fileRequestOutcome.getRawResponseContent(), RunnableFileResponse.class);
                } else
                {
                    runnableFileResponse.setException(fileRequestOutcome.getExceptionMessage());
                }
            } else
            {
                runnableFileResponse.setException(fileRequestOutcome.getExceptionMessage());
            }
        } catch (URISyntaxException ex)
        {
            runnableFileResponse.setException(ex.getMessage());
        }
        return runnableFileResponse;
    }
}
