/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package DataHandling;

import Graphs.AnimatedGraph;
import Logs.TxtLog;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jan.adamczyk
 */
public class Comport_DataHandler extends Observable implements Runnable, DataHandler {

    private ArrayList<Integer> data;
    private Integer dataCounter;
    private AnimatedGraph graph;
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
            Logger.getLogger(Comport_DataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        dataCounter++;
    }

    /**
     *
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
    public Comport_DataHandler() {
        executor = Executors.newFixedThreadPool(2);
    }
}
