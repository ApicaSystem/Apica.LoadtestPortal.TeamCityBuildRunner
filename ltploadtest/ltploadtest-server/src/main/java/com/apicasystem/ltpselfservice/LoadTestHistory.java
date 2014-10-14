package com.apicasystem.ltpselfservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class LoadTestHistory
{
    private HashMap<String, ArrayList<SelfServiceStatistics>> presetResultHistoryCollection;

    public HashMap<String, ArrayList<SelfServiceStatistics>> getPresetResultHistoryCollection()
    {
        return presetResultHistoryCollection;
    }

    public void setPresetResultHistoryCollection(HashMap<String, ArrayList<SelfServiceStatistics>> presetResultHistoryCollection)
    {
        this.presetResultHistoryCollection = presetResultHistoryCollection;
    }
    
    public boolean historyEntryExists(String presetName, SelfServiceStatistics selfServiceStatistics)
    {
        boolean exists = false;
        if (presetResultHistoryCollection.containsKey(presetName))
        {
            ArrayList<SelfServiceStatistics> statHistoryOfPreset = presetResultHistoryCollection.get(presetName);
            if (statHistoryOfPreset.contains(selfServiceStatistics))
            {
                exists = true;
            }            
        }
        return exists;
    }
    
    public void addNewHistoryEntry(String presetName, SelfServiceStatistics selfServiceStatistics)
    {
        if (this.presetResultHistoryCollection.containsKey(presetName))
        {
            ArrayList<SelfServiceStatistics> stats = this.presetResultHistoryCollection.get(presetName);
            stats.add(selfServiceStatistics);
            Collections.sort(stats);            
        }
        else
        {
            ArrayList<SelfServiceStatistics> stats = new ArrayList<SelfServiceStatistics>();
            stats.add(selfServiceStatistics);
            this.presetResultHistoryCollection.put(presetName, stats);
        }
    }
}
