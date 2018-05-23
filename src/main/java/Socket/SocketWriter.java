/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Socket;

import java.io.PrintWriter;

/**
 *
 * @author jan.adamczyk
 */
public class SocketWriter implements Runnable {

    private final PrintWriter writer;
    private String dataToWrite;

    @Override
    public void run() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param writer
     */
    public SocketWriter(PrintWriter writer) {
        this.writer = writer;
    }

    /**
     *
     * @param dataToWrite
     */
    public void setDataToWrite(String dataToWrite) {
        this.dataToWrite = dataToWrite;
    }
}
