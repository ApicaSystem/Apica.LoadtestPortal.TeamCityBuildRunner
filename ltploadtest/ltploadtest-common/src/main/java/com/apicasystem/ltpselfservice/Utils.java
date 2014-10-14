package com.apicasystem.ltpselfservice;

public class Utils
{

    public static boolean isBlank(String s)
    {
        return (s == null) || (s.trim().length() == 0);
    }

    public static String toInitialCase(String s)
    {
        if (isBlank(s))
        {
            return s;
        }
        if (s.length() == 1)
        {
            return s.toUpperCase();
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
