/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package DataEvaluation;

import Graphs.JFreeChart_2DLine_Graph;
import Logs.TxtLog;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jan.adamczyk
 */
public class MWO_DataEvaluator extends DataEvaluator_Abstract {

    private ArrayList<Integer> data;
    private Integer dataCounter;
    private JFreeChart_2DLine_Graph graph;
    private TxtLog txtLogger;
    ExecutorService executor;

    @Override
    public void run() {
        //set Graph-data
//        graph.setDataToProcess(data, seriesNameAndData);

        //build logline
        txtLogger.newLogLine("t");

        //print graph
        executor.execute(graph);
        //log data to txt
        executor.execute(txtLogger);
        try {
            executor.awaitTermination(200, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(MWO_DataEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

        dataCounter++;
    }

    /**
     *
     * @param data
     */
    public void setData(ArrayList<Integer> data) {
        this.data = data;
    }

    /**
     *
     */
    @Override
    public void processData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     */
    public MWO_DataEvaluator() {
        executor = Executors.newFixedThreadPool(2);
        try {
            txtLogger = new TxtLog("dummyName");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MWO_DataEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
