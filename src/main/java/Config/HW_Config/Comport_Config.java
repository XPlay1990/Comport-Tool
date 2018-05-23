/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Config.HW_Config;

import java.util.HashMap;

/**
 *
 * @author jan.adamczyk
 */
public class Comport_Config implements HW_Interface {

    private String last_Used_Port = "COM1";
    private HashMap<String, Object> config = new HashMap<>();

    public Comport_Config() {
        config.put("baudrate", 400000);
        config.put("dataBits", 8);
        config.put("stopBits", 1);
        config.put("shownChannels", 60);
    }

    public String getLast_Used_Port() {
        return last_Used_Port;
    }

    public void setLast_Used_Port(String last_Used_Port) {
        this.last_Used_Port = last_Used_Port;
    }

    public HashMap<String, Object> getConfig() {
        return config;
    }

    public void setConfig(HashMap<String, Object> config) {
        this.config = config;
    }

    @Override
    public void setParameter(String name, Object value) {
        config.put(name, value);
    }

    @Override
    public void removeParameter(String name) {
        config.remove(name);
    }
}
