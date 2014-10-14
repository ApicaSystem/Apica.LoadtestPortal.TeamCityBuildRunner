package com.apicasystem.ltpselfservice;

public abstract interface LoadTestLogger
{
    public abstract void started(String paramString);
    public abstract void finished(String paramString);
    public abstract void message(String paramString);
    public abstract void message(String paramString, Object... paramVarArgs);
    public abstract void failure(String paramString);
}
