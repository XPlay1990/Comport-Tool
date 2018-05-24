/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Socket;

import DataHandling.DataHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jan.adamczyk
 */
public class SocketHandler {

    DataHandler dataHandler;

    Socket socket;
    BufferedReader reader;
    PrintWriter writer;

    SocketReader socketReader;
    SocketWriter socketWriter;
    ExecutorService writeExecutor;

    /**
     *
     * @param ip
     * @param port
     */
    public SocketHandler(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            prepareWriterThread();
            createReaderThread();
        } catch (IOException ex) {
            //socket could not be opened
            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void disconnect(){
        try {
            socket.close();
        } catch (IOException ex) {
            //socket was already closed
            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createReaderThread() {
        socketReader = new SocketReader(reader);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(socketReader);
        executor.shutdown();
    }

    private void prepareWriterThread() {
        socketWriter = new SocketWriter(writer);
        writeExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     *
     * @param frame
     */
    public void writeToSocket(String frame) {
        socketWriter.setDataToWrite(frame);
        writeExecutor.execute(socketWriter);
        try {
            writeExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            //write took too long
            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
