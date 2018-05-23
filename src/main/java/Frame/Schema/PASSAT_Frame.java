/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Frame.Schema;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author jan.adamczyk
 */
public class PASSAT_Frame implements Frame {
//    private String source;
//    private String target;
//    private String msg_type;
//    private String frame_type;
//    private String frame_subtype;
//    private HashMap<String, Object> data;

    private PASSAT_Header header = new PASSAT_Header();
    private ArrayList<Object> data = new ArrayList<>();

    /**
     *
     * @throws IOException
     */
    public void toJson() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter filewriter = new FileWriter("Passat_Frame.json")) {
            gson.toJson(this, filewriter);
            filewriter.flush();
        }
    }

    /**
     *
     * @return
     */
    public PASSAT_Header getHeader() {
        return header;
    }

    /**
     *
     * @param header
     */
    public void setHeader(PASSAT_Header header) {
        this.header = header;
    }

    /**
     *
     * @return
     */
    public ArrayList<Object> getData() {
        return data;
    }

    /**
     *
     * @param data
     */
    public void setData(ArrayList<Object> data) {
        this.data = data;
    }

}
