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

    public ArrayList<Integer> getActiveChannelList();

    public void setActiveChannelList(ArrayList<Integer> activeChannelList);

    public Boolean getOffset();

    public void setOffset(Boolean offset);

    public HashMap<Integer, String> getChannelNumberToNameMapping();

    public void setChannelNumberToNameMapping(HashMap<Integer, String> channelNumberToNameMapping);
}
