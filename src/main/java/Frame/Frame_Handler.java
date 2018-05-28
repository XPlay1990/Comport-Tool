/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Frame;

import DataEvaluation.DataEvaluator_Abstract;
import Frame.Schema.PASSAT_Frame;
import Frame.Schema.PASSAT_Header;
import com.google.gson.JsonSyntaxException;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author jan.adamczyk
 */
public class Frame_Handler extends Observable implements Runnable {

    private final PASSAT_Frame_Parser parser;
    private String jsonString;
    
    private DataEvaluator_Abstract dataEvaluator;
    ExecutorService executor;

    @Override
    public void run() {
        handleJSONString(jsonString);
    }

    /**
     *
     */
    public Frame_Handler() {
        this.parser = new PASSAT_Frame_Parser();
    }

    /**
     *
     * @param json
     */
    private void handleJSONString(String json) {
        try {
            PASSAT_Frame passat_Frame = parser.parseJSONStringtoPASSAT(json);
            evaluatePASSAT_FRAME(passat_Frame);
        } catch (JsonSyntaxException e) {
            //update GUI-Errorcounter
            System.out.println("FRAME PARSING ERROR");
        }
    }

    private void evaluatePASSAT_FRAME(PASSAT_Frame passat_Frame) {
        PASSAT_Header header = passat_Frame.getHeader();
        HashMap<String, String> headerMessage = header.getmessage();

        switch (headerMessage.get("type")) {
            case "":
                break;
            case "1":
                break;
            case "data":
//                dataEvaluator.setData(passat_Frame.getData());
                
                break;
            default:
                break;
        }
    }

    /**
     *
     * @param jsonString
     */
    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    /**
     *
     * @param dataEvaluator
     */
    public void setDataEvaluator(DataEvaluator_Abstract dataEvaluator) {
        this.dataEvaluator = dataEvaluator;
    }
    
     public void initGraphComponents(DataEvaluator_Abstract dataEvaluator){
         this.dataEvaluator = dataEvaluator;
     }
}
