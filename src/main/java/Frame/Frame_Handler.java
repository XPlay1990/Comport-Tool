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
import HelpClasses.ValuesList;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jan.adamczyk
 */
public class Frame_Handler extends DataEvaluator_Abstract {

    private final PASSAT_Frame_Parser parser;
    private ArrayList<String> jsonInputs;

    private DataEvaluator_Abstract dataEvaluator;
    ExecutorService executor;

    @Override
    public void run() {
        handleJSONString(jsonInputs);
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
    private void handleJSONString(ArrayList<String> jsonInputs) {
        try {
//            PASSAT_Frame passat_Frame = parser.parseJSONStringtoPASSAT(json);
//            evaluatePASSAT_FRAME(passat_Frame);
            ArrayList<Passat_Data_Frame> frames = parser.parseTest(jsonInputs);
            evaluateTest(frames);
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

    //TODO: REMOVE after correct IMPL
    private void evaluateTest(ArrayList<Passat_Data_Frame> passat_Frames) {
        ValuesList valueLists = new ValuesList();
        for (Passat_Data_Frame passat_Frame : passat_Frames) {
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
                    valueLists.add(valueList);

                    break;
                default:
                    break;
            }
        }

        if (!valueLists.isEmpty() && dataEvaluator != null) {
            dataEvaluator.setData(valueLists);
            {
                try {
                    startThreadAndWaitForCompletition(dataEvaluator, executor);
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(Frame_Handler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     *
     * @param inputList
     */
    public void setJsonString(ArrayList<String> inputList) {
        this.jsonInputs = inputList;
    }

    /**
     *
     * @param dataEvaluator
     */
    public synchronized void initGraphComponents(DataEvaluator_Abstract dataEvaluator) {
        this.dataEvaluator = dataEvaluator;
    }

    @Override
    public void setData(ValuesList data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void processData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
