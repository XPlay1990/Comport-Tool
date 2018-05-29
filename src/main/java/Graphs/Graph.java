/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Graphs;

import java.util.ArrayList;

/**
 *
 * @author jan.adamczyk
 */
public interface Graph extends Runnable {

    /**
     *
     * @param channelNames
     */
    public void removeChannels(ArrayList<String> channelNames);

    /**
     *
     * @param channelNames
     */
    public void addChannels(ArrayList<String> channelNames);

    /**
     *
     */
    public void removeAllSeries();

    /**
     *
     * @param oldName
     * @param newName
     */
    public void changeSeriesName(String oldName, String newName);
    
    /**
     *
     * @param data
     */
    public void setDataToProcess(ArrayList<Integer> data);
}
