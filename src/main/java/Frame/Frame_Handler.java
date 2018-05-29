/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Frame;

import DataEvaluation.DataEvaluator_Abstract;
import Frame.PASSAT_DATA_Frame.DataArray;
import Frame.PASSAT_DATA_Frame.Header;
import Frame.PASSAT_DATA_Frame.Passat_Data_Frame;
import Frame.Schema.PASSAT_Frame;
import Frame.Schema.PASSAT_Header;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author jan.adamczyk
 */
public class Frame_Handler extends DataEvaluator_Abstract {

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
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     *
     * @param json
     */
    private void handleJSONString(String json) {
        try {
//            PASSAT_Frame passat_Frame = parser.parseJSONStringtoPASSAT(json);
//            evaluatePASSAT_FRAME(passat_Frame);
            Passat_Data_Frame frame = parser.parseTest(jsonString);
            evaluateTest(frame);
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

    private void evaluateTest(Passat_Data_Frame passat_Frame) {
        Header header = passat_Frame.getHeader();
        String command = header.getCommand();

        switch (command) {
            case "":
                break;
            case "1":
                break;
            case "data":
                ArrayList<Integer> valueList = new ArrayList<>();
                List<DataArray> dataArray = passat_Frame.getData().getTargets().get(0).getTargetElement().getDataset().getDataArray();
                dataArray.forEach((data) -> {
                    valueList.add(data.getDataDescr().getDataValue());
                });
                dataEvaluator.setData(valueList);
                startThreadAndWaitForCompletition(dataEvaluator, executor);
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
    public synchronized void initGraphComponents(DataEvaluator_Abstract dataEvaluator) {
        this.dataEvaluator = dataEvaluator;
    }

    @Override
    public void setData(ArrayList<Integer> data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void processData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
