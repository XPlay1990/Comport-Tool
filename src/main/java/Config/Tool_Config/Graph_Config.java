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
public class Graph_Config {

    private Boolean offset = false;
    private ArrayList<Integer> activeChannelList = new ArrayList<>();
    private HashMap<Integer, String> channelNumberToNameMapping = new HashMap<>();

    public ArrayList<Integer> getActiveChannelList() {
        return activeChannelList;
    }

    public void setActiveChannelList(ArrayList<Integer> activeChannelList) {
        this.activeChannelList = activeChannelList;
    }

    public Boolean getOffset() {
        return offset;
    }

    public void setOffset(Boolean offset) {
        this.offset = offset;
    }

    public HashMap<Integer, String> getChannelNumberToNameMapping() {
        return channelNumberToNameMapping;
    }

    public void setChannelNumberToNameMapping(HashMap<Integer, String> channelNumberToNameMapping) {
        this.channelNumberToNameMapping = channelNumberToNameMapping;
    }
    
    
}
