/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Frame;

import DataEvaluation.AQ_Response_Evaluator;
import DataEvaluation.DataEvaluator_Abstract;
import Frame.generated_PASSAT_DATA_Frame.DataArray;
import Frame.generated_PASSAT_DATA_Frame.Header;
import Frame.generated_PASSAT_DATA_Frame.Passat_Data_Frame;
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

    private final String TAG = "Frame_Handler: ";
    private final PASSAT_Frame_Parser parser;
    private ArrayList<String> jsonInputs;

    private DataEvaluator_Abstract dataEvaluator;
    private AQ_Response_Evaluator aq_info_implementer;
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
            System.out.println("FRAME PARSING ERROR! " + e.toString());
        }
    }

    /*
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
     */
    //TODO: REMOVE after correct IMPL
    private void evaluateTest(ArrayList<Passat_Data_Frame> passat_Frames) {
        ValuesList valueLists = new ValuesList();
        for (Passat_Data_Frame passat_Frame : passat_Frames) {
            Header header = passat_Frame.getHeader();
            //String command = header.getCommand();
            PASSAT_Frame_Parser.frameVariant _frameVariant = getFrameVariant(header);

            switch (_frameVariant) {
                case aq_join_res:
                    //System.out.println("evaluateTest "  + "aq_join_res frame received.");
                    aq_info_implementer.updateAcquisitionsList(passat_Frame);
                    break;
                case aq_info_res:
                    //System.out.println("evaluateTest "  + "aq_info_res frame received.");
                    //aq_info_implementer.updateAcquisitionsList(passat_Frame.getData() );
                    aq_info_implementer.updateAcquisitionsList(passat_Frame);
                    break;
                case aq_data:
                    ArrayList<Integer> valueList = new ArrayList<>();
                    List<DataArray> dataArray = passat_Frame.getData().getTargets().get(0).getTargetElement().getDataset().getDataArray();
                    dataArray.forEach((data) -> {
                        valueList.add((int) data.getDataDescr().getDataValue());
                    });
                    valueLists.add(valueList);
                    //System.out.println("valueLists size: " + valueLists.size());
                    break;
                case aq_exit_res:
                    aq_info_implementer.updateAcquisitionsList(passat_Frame);
                    break;
                case hw_info_res:
                    //System.out.println("evaluateTest "  + "hw_info_res frame received.");
                    aq_info_implementer.updateAcquisitionsList(passat_Frame);
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

    public synchronized void initGraphComponents(DataEvaluator_Abstract dataEvaluator,
            AQ_Response_Evaluator aq_info_implementer) {
        this.dataEvaluator = dataEvaluator;
        this.aq_info_implementer = aq_info_implementer;
    }

    public synchronized void initGraphComponents(AQ_Response_Evaluator aq_info_implementer) {

        this.aq_info_implementer = aq_info_implementer;
    }

    @Override
    public void setData(ValuesList data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void processData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public PASSAT_Frame_Parser.frameVariant getFrameVariant(Header _header) {
        if (PASSAT_Frame_Parser.headerDestinationMap.get(PASSAT_Frame_Parser.headerDestination.acquisition).equals(_header.getSource().trim())
                && PASSAT_Frame_Parser.headerDestinationMap.get(PASSAT_Frame_Parser.headerDestination.client).equals(_header.getTarget().trim())
                && PASSAT_Frame_Parser.headerCommandMap.get(PASSAT_Frame_Parser.headerCommand.join).equals(_header.getCommand().trim())
                && PASSAT_Frame_Parser.headerCmdClassMap.get(PASSAT_Frame_Parser.headerCmdClass.control).equals(_header.getCmdclass().trim())
                && PASSAT_Frame_Parser.headerTypeMap.get(PASSAT_Frame_Parser.headerType.response).equals(_header.getType().trim())) {
            //System.out.println(TAG + "getFrameVariant: " + "frameVariant.aq_join_res");
            return PASSAT_Frame_Parser.frameVariant.aq_join_res;
        }

        if (PASSAT_Frame_Parser.headerDestinationMap.get(PASSAT_Frame_Parser.headerDestination.acquisition).equals(_header.getSource().trim())
                && PASSAT_Frame_Parser.headerDestinationMap.get(PASSAT_Frame_Parser.headerDestination.client).equals(_header.getTarget().trim())
                && PASSAT_Frame_Parser.headerCommandMap.get(PASSAT_Frame_Parser.headerCommand.info).equals(_header.getCommand().trim())
                && PASSAT_Frame_Parser.headerCmdClassMap.get(PASSAT_Frame_Parser.headerCmdClass.employ).equals(_header.getCmdclass().trim())
                && PASSAT_Frame_Parser.headerTypeMap.get(PASSAT_Frame_Parser.headerType.response).equals(_header.getType().trim())) {
            return PASSAT_Frame_Parser.frameVariant.aq_info_res;
        }

        if (PASSAT_Frame_Parser.headerDestinationMap.get(PASSAT_Frame_Parser.headerDestination.acquisition).equals(_header.getSource().trim())
                && PASSAT_Frame_Parser.headerDestinationMap.get(PASSAT_Frame_Parser.headerDestination.client).equals(_header.getTarget().trim())
                && PASSAT_Frame_Parser.headerCommandMap.get(PASSAT_Frame_Parser.headerCommand.data).equals(_header.getCommand().trim())
                && PASSAT_Frame_Parser.headerCmdClassMap.get(PASSAT_Frame_Parser.headerCmdClass.employ).equals(_header.getCmdclass().trim())
                && PASSAT_Frame_Parser.headerTypeMap.get(PASSAT_Frame_Parser.headerType.general).equals(_header.getType().trim())) {
            //System.out.println(TAG + "getFrameVariant: " + "frameVariant.aq_data");
            return PASSAT_Frame_Parser.frameVariant.aq_data;
        }

        if (PASSAT_Frame_Parser.headerDestinationMap.get(PASSAT_Frame_Parser.headerDestination.acquisition).equals(_header.getSource().trim())
                && PASSAT_Frame_Parser.headerDestinationMap.get(PASSAT_Frame_Parser.headerDestination.client).equals(_header.getTarget().trim())
                && PASSAT_Frame_Parser.headerCommandMap.get(PASSAT_Frame_Parser.headerCommand.exit).equals(_header.getCommand().trim())
                && PASSAT_Frame_Parser.headerCmdClassMap.get(PASSAT_Frame_Parser.headerCmdClass.control).equals(_header.getCmdclass().trim())
                && PASSAT_Frame_Parser.headerTypeMap.get(PASSAT_Frame_Parser.headerType.response).equals(_header.getType().trim())) {
            return PASSAT_Frame_Parser.frameVariant.aq_exit_res;
        }

        if (PASSAT_Frame_Parser.headerDestinationMap.get(PASSAT_Frame_Parser.headerDestination.hardware_manager).equals(_header.getSource().trim())
                && PASSAT_Frame_Parser.headerDestinationMap.get(PASSAT_Frame_Parser.headerDestination.client).equals(_header.getTarget().trim())
                && PASSAT_Frame_Parser.headerCommandMap.get(PASSAT_Frame_Parser.headerCommand.info).equals(_header.getCommand().trim())
                && PASSAT_Frame_Parser.headerCmdClassMap.get(PASSAT_Frame_Parser.headerCmdClass.employ).equals(_header.getCmdclass().trim())
                && PASSAT_Frame_Parser.headerTypeMap.get(PASSAT_Frame_Parser.headerType.response).equals(_header.getType().trim())) {
            return PASSAT_Frame_Parser.frameVariant.hw_info_res;
        }

        System.out.println(TAG + "getFrameVariant: " + "frameVariant.unknown");
        return PASSAT_Frame_Parser.frameVariant.unknown;
    }
}
