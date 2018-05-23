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
    private ArrayList<Integer> activeChannelList = new ArrayList<>();
    private HashMap<Integer, String> channelNumberToNameMapping = new HashMap<>();

    /**
     *
     */
    public JFree_2DLine_Config() {

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
}
