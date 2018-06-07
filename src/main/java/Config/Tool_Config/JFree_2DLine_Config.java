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

    private int channelNumber = 60;
    private ArrayList<Integer> activeChannelList = new ArrayList<>();
    private HashMap<Integer, String> channelNumberToNameMapping = new HashMap<>();
    private transient HashMap<String, Integer> channelNameToNumberMapping = new HashMap<>();
    private int x_Values_Shown = 500;
    private int maximum_x_Values_Shown = 2500;
    private boolean autoRange = true;
    private boolean antiAliasing = true;
    private int min_y;
    private int max_y;

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

        createChannelNameToNumberMapping();
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
    public HashMap<Integer, String> getChannelNumberToNameMapping() {
        return channelNumberToNameMapping;
    }

    /**
     *
     * @param channelNumberToNameMapping
     */
    public void setChannelNumberToNameMapping(HashMap<Integer, String> channelNumberToNameMapping) {
        this.channelNumberToNameMapping = channelNumberToNameMapping;
        createChannelNameToNumberMapping();
    }

    //@JsonIgnore
    /**
     *
     * @return
     */
    public HashMap<String, Integer> getChannelNameToNumberMapping() {
        return channelNameToNumberMapping;
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

    /**
     *
     * @return
     */
    public int getMaximum_x_Values_Shown() {
        return maximum_x_Values_Shown;
    }

    /**
     *
     * @param maximum_x_Values_Shown
     */
    public void setMaximum_x_Values_Shown(int maximum_x_Values_Shown) {
        this.maximum_x_Values_Shown = maximum_x_Values_Shown;
    }

    @Override
    public int getMax_y() {
        return max_y;
    }

    @Override
    public void setMax_y(int max_y) {
        this.max_y = max_y;
    }

    public boolean isAntiAliasing() {
        return antiAliasing;
    }

    public void setAntiAliasing(boolean antiAliasing) {
        this.antiAliasing = antiAliasing;
    }

    /**
     *
     */
    public void createChannelNameToNumberMapping() {
        channelNameToNumberMapping = new HashMap<>();
        channelNumberToNameMapping.entrySet().forEach((entry) -> {
            channelNameToNumberMapping.put(entry.getValue(), entry.getKey());
        });
    }
}
