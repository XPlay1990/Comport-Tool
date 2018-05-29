/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Socket;

import DataEvaluation.DataEvaluator_Abstract;
import Frame.Frame_Handler;
import HelpClasses.Threading.ThreadStarter_Abstract;
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
public class SocketReader extends ThreadStarter_Abstract implements Runnable {

    private final BufferedReader reader;
    private final ExecutorService executor;
    private final Frame_Handler frame_Handler;

    @Override
    public void run() {
        String input;
        try {
            while (true) {

                while ((input = reader.readLine()) != null) {
                    startFrameHandler(input);
//                    System.out.println(input);
                }

            }
        } catch (IOException ex) {
            //Socket closed!
            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * fills and starts FrameHandler and waits for completition
     *
     * @author jan.adamczyk
     */
    private void startFrameHandler(String input) {
        frame_Handler.setJsonString(input);

        startThreadAndWaitForCompletition(frame_Handler, executor);
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
}
