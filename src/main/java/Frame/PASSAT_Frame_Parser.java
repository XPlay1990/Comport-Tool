/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Frame;

import Frame.Schema.PASSAT_Frame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
}
