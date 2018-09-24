/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataEvaluation;

import Frame.generated_PASSAT_DATA_Frame.Data;
import Frame.generated_PASSAT_DATA_Frame.Passat_Data_Frame;
import HelpClasses.ValuesList;
import Logs.TxtLog;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Observable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author ruslan.steinbeck
 */
public class AQ_Response_Evaluator extends Observable {

    //private ValuesList data;
    private DefaultListModel listModel;
    private String aq_uuid;
    
    private ExecutorService executor;
    private TxtLog txtLogger;


    /**
     *
     * @param 
     */
    public AQ_Response_Evaluator(String aq_uuid) {
        if(listModel != null && listModel.size() != 0) {
            listModel = new DefaultListModel();
        }
        int size = listModel.size();
        this.aq_uuid = aq_uuid;
        listModel.addElement(aq_uuid);
        this.executor = Executors.newSingleThreadExecutor();
    }
    
    public AQ_Response_Evaluator() {
        if(listModel != null && listModel.size() != 0) {
            listModel = new DefaultListModel();
        }
        this.executor = Executors.newSingleThreadExecutor();
    }

    private void add_UUID(String aq_uuid) {
        listModel.addElement(aq_uuid);
    }

    /**
     *
     * @param eventReceived_Time
     */
    public void updateAcquisitionsList() {
        setChanged();
        notifyObservers(this.aq_uuid);
    }
    
    public void updateAcquisitionsList(String aq_uuid) {
        setChanged();
        notifyObservers(aq_uuid);
    }
    public void updateAcquisitionsList(Data data) {
        setChanged();
        notifyObservers(data);
    }
    public void updateAcquisitionsList(Passat_Data_Frame passat_frame) {
        setChanged();
        notifyObservers(passat_frame);
    }
    
    
    
}
