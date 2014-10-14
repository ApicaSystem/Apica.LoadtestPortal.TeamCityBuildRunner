package com.apicasystem.ltpselfservice.loadtest;

import java.net.URI;

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
