/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Frame;

import Frame.generated_PASSAT_DATA_Frame.Passat_Data_Frame;
import Frame.Schema.PASSAT_Frame;
import HelpClasses.ValuesList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jan.adamczyk
 */
public class PASSAT_Frame_Parser {

    private final Gson gson;

    /**
     *
     */
    public PASSAT_Frame_Parser() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     *
     * @param jsonString
     * @return
     */
    public PASSAT_Frame parseJSONStringtoPASSAT(String jsonString) {
        PASSAT_Frame frame = gson.fromJson(jsonString, PASSAT_Frame.class);
        return frame;
    }

    /**
     *
     * @param frame
     * @return
     */
    public String parsePASSATtoJSONString(PASSAT_Frame frame) {
        String jsonString = gson.toJson(frame);
        return jsonString;
    }

    /**
     *
     * @param jsonStrings
     * @return
     */
    public ArrayList<Passat_Data_Frame> parseJSONStringToPASSAT(ArrayList<String> jsonStrings) {
        ArrayList<Passat_Data_Frame> frames = new ArrayList<>();
        jsonStrings.stream().map((jsonString) -> gson.fromJson(jsonString, Passat_Data_Frame.class)).forEachOrdered((frame) -> {
            frames.add(frame);
        });
        return frames;
    }

    /**
     *
     * @param jsonStrings
     * @return
     */
    public ValuesList parseJSONStringToNodeRedFrame(ArrayList<String> jsonStrings) {
        ValuesList frames = new ValuesList();
        jsonStrings.stream().map((jsonString) -> gson.fromJson(jsonString, Node_Red_DataArray.class)).forEachOrdered((frame) -> {
            frames.add(frame);
        });
        return frames;
    }

    /**
     *
     */
    public enum headerDestination {

        /**
         *
         */
        unknown,
        /**
         *
         */
        client,
        /**
         *
         */
        hardware_manager,
        /**
         *
         */
        storage_manager,
        /**
         *
         */
        acquisition
    };

    /**
     *
     */
    public static final Map<headerDestination, String> headerDestinationMap = new HashMap<headerDestination, String>() {
        {
            put(headerDestination.client, "client");
            put(headerDestination.hardware_manager, "hardware_manager");
            put(headerDestination.storage_manager, "storage_manager");
            put(headerDestination.acquisition, "acquisition");
        }
    };

    /**
     *
     */
    public enum headerType {

        /**
         *
         */
        unknown,
        /**
         *
         */
        request,
        /**
         *
         */
        response,
        /**
         *
         */
        general
    };

    /**
     *
     */
    public static final Map<headerType, String> headerTypeMap = new HashMap<headerType, String>() {
        {
            put(headerType.request, "request");
            put(headerType.response, "response");
            put(headerType.general, "general");
        }
    };

    /**
     *
     */
    public enum headerCmdClass {

        /**
         *
         */
        unknown,
        /**
         *
         */
        employ,
        /**
         *
         */
        control
    };

    /**
     *
     */
    public static final Map<headerCmdClass, String> headerCmdClassMap = new HashMap<headerCmdClass, String>() {
        {
            put(headerCmdClass.employ, "employ");
            put(headerCmdClass.control, "control");
        }
    };

    /**
     *
     */
    public enum headerCommand {

        /**
         *
         */
        unknown,
        /**
         *
         */
        info,
        /**
         *
         */
        join,
        /**
         *
         */
        exit,
        /**
         *
         */
        data,
        /**
         *
         */
        acquisition
    };

    /**
     *
     */
    public static final Map<headerCommand, String> headerCommandMap = new HashMap<headerCommand, String>() {
        {
            put(headerCommand.info, "info");
            put(headerCommand.join, "join");
            put(headerCommand.exit, "exit");
            put(headerCommand.data, "data");
            put(headerCommand.acquisition, "acquisition");
        }
    };

    /**
     *
     */
    public enum frameVariant {

        /**
         *
         */
        unknown,
        /**
         *
         */
        aq_info_res,
        /**
         *
         */
        aq_join_res,
        /**
         *
         */
        aq_exit_res,
        /**
         *
         */
        aq_data,
        /**
         *
         */
        hw_info_res
    };

}
