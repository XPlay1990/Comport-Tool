/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Config;

import Config.HW_Config.HW_Config;
import Config.Tool_Config.Tool_Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author jan.adamczyk
 */
public class Config_JSON {

    private Tool_Config tool_Config = new Tool_Config();
    private HW_Config hw_Config = new HW_Config();

    /**
     *
     * @return @throws FileNotFoundException
     */
    public static Config_JSON main() throws FileNotFoundException {
        Gson gson = new Gson();
        Config_JSON cfg = gson.fromJson(new FileReader("cfg.json"), Config_JSON.class);
        return cfg;
    }

    /**
     *
     * @throws IOException
     */
    public void toJSON() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter filewriter = new FileWriter("cfg.json")) {
            gson.toJson(this, filewriter);
            filewriter.flush();
        }
    }

    /**
     *
     * @return
     */
    public Tool_Config getTool_Config() {
        return tool_Config;
    }

    /**
     *
     * @param tool_Config
     */
    public void setTool_Config(Tool_Config tool_Config) {
        this.tool_Config = tool_Config;
    }

    /**
     *
     * @return
     */
    public HW_Config getHw_Config() {
        return hw_Config;
    }

    /**
     *
     * @param hw_Config
     */
    public void setHw_Config(HW_Config hw_Config) {
        this.hw_Config = hw_Config;
    }

    
}
