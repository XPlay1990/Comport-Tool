/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Socket;

import Comport.ComportHandler;
import DataHandling.Comport_DataHandler;
import Frame.PASSAT_Frame_Parser;
import Frame.Schema.PASSAT_Frame;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jan.adamczyk
 */
public class SocketReader implements Runnable {

    BufferedReader reader;
    ExecutorService executor;
    Comport_DataHandler dataHandler;
    PASSAT_Frame_Parser parser;

    @Override
    public void run() {
        String input;
        while (true) {
            try {
                while ((input = reader.readLine()) != null) {
                    //TODO:CODE
                    PASSAT_Frame passat_frame = parser.parseJSONStringtoPASSAT(input);

                    dataHandler.setFrame(passat_frame);
                    executor.execute(dataHandler);
                }
            } catch (IOException ex) {
                Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param reader
     */
    public SocketReader(BufferedReader reader) {
        this.reader = reader;

        executor = Executors.newSingleThreadExecutor();
        this.parser = new PASSAT_Frame_Parser();
    }
}
