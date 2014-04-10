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
}
