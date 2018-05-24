/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Socket;

import Frame.Frame_Handler;
import Frame.PASSAT_Frame_Parser;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jan.adamczyk
 */
public class SocketReader implements Runnable {

    BufferedReader reader;
    ExecutorService executor;
    Frame_Handler frame_Handler;
    PASSAT_Frame_Parser parser;

    @Override
    public void run() {
        String input;
        while (true) {
            try {
                while ((input = reader.readLine()) != null) {
                    startFrameHandler(input);
                }
            } catch (IOException ex) {
                Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *  fills and starts FrameHandler and waits for completition
     * @author jan.adamczyk
     */
    private void startFrameHandler(String input) {
        frame_Handler.setJsonString(input);

        Future<?> future = executor.submit(frame_Handler);

        try {
            future.get();   //Wait for thread to finish successfull
        } catch (ExecutionException | InterruptedException ex) {
            Logger.getLogger(SocketReader.class.getName()).log(Level.SEVERE, null, ex);
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
