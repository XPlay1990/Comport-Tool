/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Config.Tool_Config;

/**
 *
 * @author jan.adamczyk
 */
public class Tool_Config {

    private Server_Config server_Config = new Server_Config();
    private Graph_Config graph_Config = new Graph_Config();

    /**
     *
     * @return
     */
    public Server_Config getServer_Config() {
        return server_Config;
    }

    /**
     *
     * @param server_Config
     */
    public void setServer_Config(Server_Config server_Config) {
        this.server_Config = server_Config;
    }

    /**
     *
     * @return
     */
    public Graph_Config getGraph_Config() {
        return graph_Config;
    }

    /**
     *
     * @param graph_Config
     */
    public void setGraph_Config(Graph_Config graph_Config) {
        this.graph_Config = graph_Config;
    }
}
