/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Frame;

import Frame.Schema.PASSAT_Frame;
import Frame.Schema.PASSAT_Header;
import java.util.HashMap;
import java.util.Observable;

/**
 *
 * @author jan.adamczyk
 */
public class Frame_Handler extends Observable {

    PASSAT_Frame_Parser parser;

    /**
     *
     * @param json
     */
    public void handleJSONString(String json) {
        PASSAT_Frame passat_Frame = parser.parseJSONStringtoPASSAT(json);
        evaluatePASSAT_FRAME(passat_Frame);
    }

    private void evaluatePASSAT_FRAME(PASSAT_Frame passat_Frame) {
        PASSAT_Header header = passat_Frame.getHeader();
        HashMap<String, String> headerMessage = header.getmessage();
        switch (headerMessage.get("type")) {
            case "":
                break;
            case "1":
                break;
            case "2":
                break;
            default:
                break;
        }
    }

    /**
     *
     */
    public Frame_Handler() {
        this.parser = new PASSAT_Frame_Parser();
    }
}
