/* 
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Logs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;

/**
 * Class to Handle Txt-File Logging of the Data
 *
 * @author Jan.Adamczyk
 */
public final class TxtLog implements Runnable {

    private final File file;
    private final PrintWriter writer;
    private String logLine;
    private boolean isFinished = false;

    @Override
    public void run() {
        writeOut(logLine);
    }

    /**
     *
     * @param hw_Interface
     * @throws FileNotFoundException
     */
    public TxtLog(String hw_Interface) throws FileNotFoundException {
        String dateTime = LocalDateTime.now().toString().replaceAll(":", "-");
        dateTime = dateTime.replaceAll("T", "_Time_");
        this.file = new File("ComportTool_Logfile_" + dateTime + "_" + hw_Interface.replaceAll(" ", "_") + ".txt");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        writer = new PrintWriter(file);
    }

    /**
     *
     * @param line
     */
    public void writeOut(String line) {
        writer.println(line);
        writer.flush();
    }

    /**
     *
     */
    public void closeStream() {
        writer.flush();
        writer.close();
    }

    /**
     *
     */
    public void setFinished() {
        isFinished = true;
    }

    /**
     *
     * @param s
     */
    public void addToLogLine(String s) {
        logLine = logLine + s;
    }

    /**
     *
     * @param s
     */
    public void newLogLine(String s) {
        logLine = s;
    }
}
