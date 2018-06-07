/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Frame;

import Frame.generated_PASSAT_DATA_Frame.Passat_Data_Frame;
import Frame.Schema.PASSAT_Frame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;

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
     * REMOVE AFTER CORRECT IMPL parses jsonstring to object
     *
     * @param jsonStrings
     * @return
     */
    public ArrayList<Passat_Data_Frame> parseTest(ArrayList<String> jsonStrings) {
        ArrayList<Passat_Data_Frame> frames = new ArrayList<>();
        jsonStrings.stream().map((jsonString) -> gson.fromJson(jsonString, Passat_Data_Frame.class)).forEachOrdered((frame) -> {
            frames.add(frame);
        });
        return frames;
    }
}
