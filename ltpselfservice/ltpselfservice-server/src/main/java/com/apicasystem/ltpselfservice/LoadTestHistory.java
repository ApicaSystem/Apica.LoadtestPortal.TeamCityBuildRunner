/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author andras.nemes
 */
public class LoadTestHistory
{
    private HashMap<String, ArrayList<SelfServiceStatistics>> presetResultHistoryCollection;
    private String presetName;
    private ArrayList<SelfServiceStatistics> presetResultHistory;

    public String getPresetName()
    {
        return presetName;
    }

    public void setPresetName(String presetName)
    {
        this.presetName = presetName;
    }

    public ArrayList<SelfServiceStatistics> getPresetResultHistory()
    {
        return presetResultHistory;
    }

    public void setPresetResultHistory(ArrayList<SelfServiceStatistics> presetResultHistory)
    {
        this.presetResultHistory = presetResultHistory;
    }
}
