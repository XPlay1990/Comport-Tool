/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Socket;

import DataEvaluation.AQ_Response_Evaluator;
import DataEvaluation.DataEvaluator_Abstract;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import DataEvaluation.DataEvaluator_Interface;
import HelpClasses.Threading.ThreadStarter_Abstract;

/**
 *
 * @author jan.adamczyk
 */
public class SocketHandler extends ThreadStarter_Abstract {

    private DataEvaluator_Interface dataHandler;

    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    private SocketReader socketReader;
    private SocketWriter socketWriter;
    private ExecutorService writeExecutor;

    /**
     *
     * @param ip
     * @param port
     * @throws java.net.ConnectException
     */
    public SocketHandler(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);

        prepareWriterThread();
        createReaderThread();
    }

    /**
     *
     */
    public void disconnect() {
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

        //Nonblocking Threading
        writeExecutor.execute(socketWriter);
//        try {
//            writeExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException ex) {
//            //write took too long
//            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    /**
     *
     * @param dataEvaluator
     */
    public synchronized void initGraphComponents(DataEvaluator_Abstract dataEvaluator) {
        socketReader.initGraphComponents(dataEvaluator);
    }
    
    public synchronized void initGraphComponents(DataEvaluator_Abstract dataEvaluator,
            AQ_Response_Evaluator aq_info_implementer) {
        socketReader.initGraphComponents(dataEvaluator,aq_info_implementer);
    }
    
    public synchronized void initGraphComponents(AQ_Response_Evaluator aq_info_implementer) {
        socketReader.initGraphComponents(aq_info_implementer);
    }
    

    public void stopInputAndWait() {
        socketReader.stopInput();

        //wait for running frame-evaluation threads to finish
        socketReader.isInputting();
    }

    public void startInput() {
        socketReader.startInput();
    }
}
