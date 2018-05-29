/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Socket;

import DataEvaluation.DataEvaluator_Abstract;
import Frame.Frame_Handler;
import HelpClasses.Threading.ThreadStarter_Abstract;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jan.adamczyk
 */
public class SocketReader extends ThreadStarter_Abstract implements Runnable {

    private final BufferedReader reader;
    private final ExecutorService executor;
    private final Frame_Handler frame_Handler;
    private Future<?> future;

    private boolean inputAllowed = true;
    boolean isInputting = false;

    @Override
    public void run() {
        ArrayList<String> inputList = new ArrayList<>();
        String input;
        try {
            while (true) {
                while ((input = reader.readLine()) != null) {
                    inputList.add(input);
                    if ((future == null || future.isDone() == true) && inputAllowed) {
                        isInputting = true;
                        frame_Handler.setJsonString(inputList);
                        future = executor.submit(frame_Handler);

                        inputList = new ArrayList<>();
                        isInputting = false;
                    }
                }
            }
        } catch (IOException ex) {
            //Socket closed!
            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param reader
     */
    public SocketReader(BufferedReader reader) {
        this.reader = reader;
        this.executor = Executors.newSingleThreadExecutor();
        this.frame_Handler = new Frame_Handler();
    }

    /**
     *
     * @param dataEvaluator
     */
    public synchronized void initGraphComponents(DataEvaluator_Abstract dataEvaluator) {
        frame_Handler.initGraphComponents(dataEvaluator);
    }

    public void stopInput() {
        inputAllowed = false;
    }

    public void startInput() {
        inputAllowed = true;
    }

    public void isInputting() {
//        if (future != null) {
//            try {
//                future.get();
//            } catch (InterruptedException | ExecutionException ex) {
//                Logger.getLogger(SocketReader.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        while (isInputting) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException ex) {
                Logger.getLogger(SocketReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
