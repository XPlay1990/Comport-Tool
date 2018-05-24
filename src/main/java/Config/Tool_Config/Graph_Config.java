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
}
