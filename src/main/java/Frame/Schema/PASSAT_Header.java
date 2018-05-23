/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Frame.Schema;

import java.util.HashMap;

/**
 *
 * @author jan.adamczyk
 */
public class PASSAT_Header {

    private HashMap<String, String> message = new HashMap<>();

    /**
     *
     */
    public PASSAT_Header() {
        message.put("source", "client");
        message.put("target", "client");
        message.put("msg_type", "client");
        message.put("frame_type", "client");
        message.put("frame_subtype", "client");
    }

    /**
     *
     * @return
     */
    public HashMap<String, String> getmessage() {
        return message;
    }

    /**
     *
     * @param header
     */
    public void setmessage(HashMap<String, String> header) {
        this.message = header;
    }
}
