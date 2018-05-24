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
public class JFree_2DLine_Config implements Graph_Config {

    private Boolean offset = false;
    private int channelNumber = 60;
    private ArrayList<Integer> activeChannelList = new ArrayList<>();
    private HashMap<Integer, String> channelNumberToNameMapping = new HashMap<>();
    private int x_Values_Shown = 500;
    private boolean autoRange = true;
    private int min_y;
    private int min_x;

    /**
     *
     */
    public JFree_2DLine_Config() {
        for (int i = 0; i < channelNumber; i++) {
            activeChannelList.add(i);
        }
        for (int i = 0; i < channelNumber; i++) {
            channelNumberToNameMapping.put(i, "Channel_" + i);
        }
    }

    /**
     *
     * @return
     */
    public ArrayList<Integer> getActiveChannelList() {
        return activeChannelList;
    }

    /**
     *
     * @param activeChannelList
     */
    public void setActiveChannelList(ArrayList<Integer> activeChannelList) {
        this.activeChannelList = activeChannelList;
    }

    /**
     *
     * @return
     */
    public Boolean getOffset() {
        return offset;
    }

    /**
     *
     * @param offset
     */
    public void setOffset(Boolean offset) {
        this.offset = offset;
    }

    /**
     *
     * @return
     */
    public HashMap<Integer, String> getChannelNumberToNameMapping() {
        return channelNumberToNameMapping;
    }

    /**
     *
     * @param channelNumberToNameMapping
     */
    public void setChannelNumberToNameMapping(HashMap<Integer, String> channelNumberToNameMapping) {
        this.channelNumberToNameMapping = channelNumberToNameMapping;
    }

    /**
     *
     * @return
     */
    public int getX_Values_Shown() {
        return x_Values_Shown;
    }

    /**
     *
     * @param x_Values_Shown
     */
    public void setX_Values_Shown(int x_Values_Shown) {
        this.x_Values_Shown = x_Values_Shown;
    }

    /**
     *
     * @return
     */
    public int getMin_y() {
        return min_y;
    }

    /**
     *
     * @param min_y
     */
    public void setMin_y(int min_y) {
        this.min_y = min_y;
    }

    /**
     *
     * @return
     */
    public int getMin_x() {
        return min_x;
    }

    /**
     *
     * @param min_x
     */
    public void setMin_x(int min_x) {
        this.min_x = min_x;
    }

    /**
     *
     * @return
     */
    public boolean isAutoRange() {
        return autoRange;
    }

    /**
     *
     * @param autoRange
     */
    public void setAutoRange(boolean autoRange) {
        this.autoRange = autoRange;
    }

    /**
     *
     * @return
     */
    public int getChannelNumber() {
        return channelNumber;
    }

    /**
     *
     * @param channelNumber
     */
    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }

}
