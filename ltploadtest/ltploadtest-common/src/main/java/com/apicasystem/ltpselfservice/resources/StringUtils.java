package com.apicasystem.ltpselfservice.resources;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtils
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

    public static boolean startsWith(String target, String prefix)
    {
        return (target != null) && (prefix != null) && (target.length() > 0) && (target.startsWith(prefix));
    }

    public static String fixEmpty(String s)
    {
        if (isBlank(s))
        {
            return null;
        }
        return s.trim();
    }

    public static boolean tryParseInt(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException nfe)
        {
            return false;
        }
    }

    public static String replicate(String s, int times)
    {
        if (s == null)
        {
            return null;
        }
        if ((times <= 0) || (s.length() == 0))
        {
            return "";
        }
        StringBuilder b = new StringBuilder(s.length() * times);
        for (int k = 1; k <= times; k++)
        {
            b.append(s);
        }
        return b.toString();
    }

    public static String md5(String s)
    {
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(s.getBytes(Charset.forName("UTF-8")));
            StringBuilder buf = new StringBuilder(2 * digest.length);
            for (byte oneByte : digest)
            {
                buf.append(Integer.toHexString(oneByte & 0xFF | 0x100).substring(1, 3));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException ignore)
        {
        }
        return s;
    }
}
