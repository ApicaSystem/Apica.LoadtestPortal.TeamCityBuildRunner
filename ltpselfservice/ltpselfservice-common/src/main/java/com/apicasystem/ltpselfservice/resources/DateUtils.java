/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice.resources;

import java.util.Date;
import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author andras.nemes
 */
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
