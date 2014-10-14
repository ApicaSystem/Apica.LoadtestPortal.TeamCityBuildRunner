package com.apicasystem.ltpselfservice.resources;

import java.util.ArrayList;
import java.util.List;

public enum DelayUnit
{

    seconds, users;

    public final String label;

    private DelayUnit()
    {
        this.label = Util.toInitialCase(name());
    }

    public static List<String> names()
    {
        DelayUnit[] units = values();
        List<String> result = new ArrayList(units.length);
        for (int i = 0; i < units.length; i++)
        {
            result.add(units[i].name());
        }
        return result;
    }

    public String getId()
    {
        return name();
    }

    public String getDisplayName()
    {
        return this.label;
    }
}
