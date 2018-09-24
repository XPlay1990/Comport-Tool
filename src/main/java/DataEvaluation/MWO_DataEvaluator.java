/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package DataEvaluation;

import Graphs.Graph;
import HelpClasses.ValuesList;
import Logs.TxtLog;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author jan.adamczyk
 */
public class MWO_DataEvaluator extends DataEvaluator_Abstract {

    private ValuesList data;

    private ExecutorService executor;
    private final Graph graph;
    private TxtLog txtLogger;

    @Override
    public void run() {
        //TimeStamp for Tool-Latency
        LocalTime timeStamp = getTimeStamp();

        //Process data
//        processData();

        //set Graph-data
        graph.setDataToProcess(data);
        //build logline
        txtLogger.newLogLine(timeStamp.toString() + "\t\t" + data);

        try {
            //print graph && log data to txt
            startThreadAndWaitForCompletition(txtLogger, executor);
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(MWO_DataEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            SwingUtilities.invokeAndWait(graph);
        } catch (InterruptedException | InvocationTargetException ex) {
            Logger.getLogger(MWO_DataEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }

        updateFrameLatencys(timeStamp);
    }

    /**
     *
     * @param data
     */
    @Override
    public void setData(ValuesList data) {
        this.data = data;
    }

    /**
     *
     */
    @Override
    public void processData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //Insert Code if there is something to process
    }

    /**
     *
     * @param graph
     * @param hw_Interface
     */
    public MWO_DataEvaluator(Graph graph, String hw_Interface) {
        this.graph = graph;
        this.executor = Executors.newSingleThreadExecutor();
        try {
            txtLogger = new TxtLog(hw_Interface);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MWO_DataEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private LocalTime getTimeStamp() {
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime zdt = ZonedDateTime.now(zone);
        return zdt.toLocalTime();
    }

    /**
     *
     * @param eventReceived_Time
     */
    private void updateFrameLatencys(LocalTime eventReceived_Time) {
//        try {
//            embeddedLatency = (int) comportReceivedLine_Time.until(eventReceived_Time, ChronoUnit.MILLIS);
//            if (embeddedLatency > maxEmbeddedLatency) {
//                maxEmbeddedLatency = embeddedLatency;
//            }
        int toolLatency = (int) eventReceived_Time.until(getTimeStamp(), ChronoUnit.MILLIS);
        setChanged();
        notifyObservers(toolLatency);
//        comportReceivedLine_Time = eventReceived_Time;
    }
}
