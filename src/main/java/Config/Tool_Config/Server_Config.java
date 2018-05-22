/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Config.Tool_Config;

import java.util.HashMap;

/**
 *
 * @author jan.adamczyk
 */
public class Server_Config {

    private HashMap<String, String> serverList = new HashMap<>();
    private String defaultServer = null;
    private int port = 3000;
    private Boolean autoConnectToServer = false;

    /**
     *
     */
    public Server_Config() {
        serverList.put(
                "localhost", "localhost");
        serverList.put(
                "Ruslan", "10.131.251.208");
    }

    /**
     *
     * @return
     */
    public HashMap<String, String> getServerList() {
        return serverList;
    }

    /**
     *
     * @param serverList
     */
    public void setServerList(HashMap<String, String> serverList) {
        this.serverList = serverList;
    }

    /**
     *
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     *
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     *
     * @return
     */
    public String getDefaultServer() {
        return defaultServer;
    }

    /**
     *
     * @param defaultServer
     */
    public void setDefaultServer(String defaultServer) {
        this.defaultServer = defaultServer;
    }

    /**
     *
     * @return
     */
    public Boolean getAutoConnectToServer() {
        return autoConnectToServer;
    }

    /**
     *
     * @param autoConnectToServer
     */
    public void setAutoConnectToServer(Boolean autoConnectToServer) {
        this.autoConnectToServer = autoConnectToServer;
    }
}
