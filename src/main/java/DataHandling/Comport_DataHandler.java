/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package DataHandling;

import Frame.Schema.PASSAT_Frame;
import java.util.Observable;

/**
 *
 * @author jan.adamczyk
 */
public class Comport_DataHandler extends Observable implements Runnable, DataHandler {
    
    PASSAT_Frame frame;

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param frame
     */
    @Override
    public void setFrame(PASSAT_Frame frame) {
        this.frame = frame;
    }

    /**
     *
     */
    @Override
    public void processData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
