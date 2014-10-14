package com.apicasystem.ltpselfservice.resources;

public class Util
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

    public static String percentageBar(double percentage)
    {
        char dot = '.';
        char mark = '#';
        int slots = 40;

        StringBuilder bar = new StringBuilder(replicate(String.valueOf('.'), 40));
        int numSlots = (int) (40.0D * percentage / 100.0D);
        for (int k = 0; k < numSlots; k++)
        {
            bar.setCharAt(k, '#');
        }
        return String.format("[%s] %3.0f%%", new Object[]
        {
            bar, Double.valueOf(percentage)
        });
    }

    public static String replicate(String s, int times)
    {
        StringBuilder b = new StringBuilder(s.length() * times);
        for (int k = 1; k <= times; k++)
        {
            b.append(s);
        }
        return b.toString();
    }

    public static boolean startsWith(Object obj, String prefix)
    {
        return (obj != null) && (obj.toString().trim().startsWith(prefix));
    }
}
