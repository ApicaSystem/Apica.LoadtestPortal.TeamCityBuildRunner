/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apicasystem.ltpselfservice;

import java.util.ArrayList;

/**
 *
 * @author andras.nemes
 */
public class SelfServiceStatisticsOfPreset
{
    private String presetName;
    private SelfServiceStatistics statistics;

    public String getPresetName()
    {
        return presetName;
    }

    public void setPresetName(String presetName)
    {
        this.presetName = presetName;
    }

    public SelfServiceStatistics getStatistics()
    {
        return statistics;
    }

    public void setStatistics(SelfServiceStatistics statistics)
    {
        this.statistics = statistics;
    }
}
