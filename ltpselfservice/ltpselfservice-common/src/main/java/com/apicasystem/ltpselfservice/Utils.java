/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice;

/**
 *
 * @author andras.nemes
 */
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
