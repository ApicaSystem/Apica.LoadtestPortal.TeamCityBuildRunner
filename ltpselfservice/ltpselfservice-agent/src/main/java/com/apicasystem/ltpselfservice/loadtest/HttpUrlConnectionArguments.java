/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice.loadtest;

import java.net.URI;

/**
 *
 * @author andras.nemes
 */
public class HttpUrlConnectionArguments
{
    private String authToken;
    private String webMethod;
    private String requestBody;
    private URI uri;
    private String contentType;

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getAuthToken()
    {
        return authToken;
    }

    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }
    
    public String getWebMethod()
    {
        return webMethod;
    }

    public void setWebMethod(String webMethod)
    {
        this.webMethod = webMethod;
    }

    public String getRequestBody()
    {
        return requestBody;
    }

    public void setRequestBody(String requestBody)
    {
        this.requestBody = requestBody;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }
}
