package com.apicasystem.ltpselfservice.resources;

public enum LoadTestResult
{

    aborted("Aborted"), unstable("Unstable"), failed("Failed"), error("Error");

    private final String description;

    private LoadTestResult(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public String getId()
    {
        return name();
    }

    public String getDisplayName()
    {
        return StringUtils.toInitialCase(name());
    }

    @Override
    public String toString()
    {
        return this.description;
    }
}
