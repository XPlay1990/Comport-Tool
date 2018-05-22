/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jan.adamczyk
 */
public class SocketHandler implements Runnable {

    BufferedReader reader;
    PrintWriter writer;

    @Override
    public void run() {
        while (true) {
            try {
                String input;
                while ((input = reader.readLine()) != null) {
                    //TODO:CODE
                }
            } catch (IOException ex) {
                Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param ip
     * @param port
     */
    public SocketHandler(String ip, int port) {
        try {
            Socket socket = new Socket(ip, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param o
     */
    public void writeToSocket(Object o) {
        writer.write(o.toString());
        writer.flush();
    }
}
