/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package DataEvaluation;

import Graphs.Graph;
import Logs.TxtLog;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jan.adamczyk
 */
public class MWO_DataEvaluator extends DataEvaluator_Abstract {

    private ArrayList<Integer> data;
    private Integer dataCounter;

    private ExecutorService executor;
    private final Graph graph;
    private TxtLog txtLogger;
    private final List<Runnable> runnables;

    @Override
    public void run() {
        //set Graph-data
//        graph.setDataToProcess(data, seriesNameAndData);

        //build logline
        txtLogger.newLogLine("t");

        //print graph && log data to txt
        startThreadsAndWaitForCompletition(runnables, executor);

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
     * @param graph
     */
    public MWO_DataEvaluator(Graph graph) {
        this.graph = graph;
        executor = Executors.newFixedThreadPool(2);
        try {
            txtLogger = new TxtLog("dummyName");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MWO_DataEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
        runnables = new ArrayList<>();
        runnables.add(graph);
        runnables.add(txtLogger);
    }
}
