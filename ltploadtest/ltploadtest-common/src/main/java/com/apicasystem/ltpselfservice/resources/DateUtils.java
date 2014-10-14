package com.apicasystem.ltpselfservice.resources;

import java.util.Date;
import org.joda.time.format.ISODateTimeFormat;

public class DateUtils
{

    public static Date toDateFromIso8601(String s)
    {
        if (s == null)
        {
            return null;
        }
        return ISODateTimeFormat.dateTimeNoMillis().parseDateTime(s).toDate();
    }

    public static String toIso8601(Date date)
    {
        if (date == null)
        {
            return null;
        }
        return ISODateTimeFormat.dateTimeNoMillis().print(date.getTime());
    }

    public static Date toDateFromTimestamp(long ts)
    {
        return new Date(ts / 1000L);
    }
}
