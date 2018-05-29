/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Config.Tool_Config;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author jan.adamczyk
 */
public interface Graph_Config {

    /**
     *
     * @return
     */
    public ArrayList<Integer> getActiveChannelList();

    /**
     *
     * @param activeChannelList
     */
    public void setActiveChannelList(ArrayList<Integer> activeChannelList);

    /**
     *
     * @return
     */
    public Boolean getOffset();

    /**
     *
     * @param offset
     */
    public void setOffset(Boolean offset);

    /**
     *
     * @return
     */
    public HashMap<Integer, String> getChannelNumberToNameMapping();

    /**
     *
     * @param channelNumberToNameMapping
     */
    public void setChannelNumberToNameMapping(HashMap<Integer, String> channelNumberToNameMapping);

    /**
     *
     * @return
     */
    public HashMap<String, Integer> getChannelNameToNumberMapping();

    /**
     *
     * @return
     */
    public int getX_Values_Shown();

    /**
     *
     * @param x_Values_Shown
     */
    public void setX_Values_Shown(int x_Values_Shown);

    /**
     *
     * @return
     */
    public int getMin_y();

    /**
     *
     * @param min_y
     */
    public void setMin_y(int min_y);

    /**
     *
     * @return
     */
    public int getMax_y();

    /**
     *
     * @param max_y
     */
    public void setMax_y(int max_y);

    /**
     *
     * @return
     */
    public boolean isAutoRange();

    /**
     *
     * @param autoRange
     */
    public void setAutoRange(boolean autoRange);

    /**
     *
     * @return
     */
    public int getChannelNumber();

    /**
     *
     * @param channelNumber
     */
    public void setChannelNumber(int channelNumber);

    /**
     *
     * @return
     */
    public int getMaximum_x_Values_Shown();

    /**
     *
     * @param maximum_x_Values_Shown
     */
    public void setMaximum_x_Values_Shown(int maximum_x_Values_Shown);

    /**
     *
     */
    public void createChannelNameToNumberMapping();
}
