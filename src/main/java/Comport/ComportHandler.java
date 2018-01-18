/* 
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Comport;

import Graphs.AnimatedGraph;
import HelpClasses.IndexHolder;
import HelpClasses.MultipleSeriesHolder;
import HelpClasses.SeriesHolder;
import HelpClasses.SeriesNameAndData;
import Protocol.Stream_Evaluation;
import Protocol.InputStreamMatrix;
import Logs.TxtLog;
import Protocol.Stream_Evaluation.ExceptionChar;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
//import jssc.SerialPort;
//import jssc.SerialPortEvent;
//import jssc.SerialPortEventListener;
//import jssc.SerialPortException;
import org.jfree.ui.RefineryUtilities;

import com.fazecast.jSerialComm.*;

/**
 * handles connection to the serialport and receiving of data
 *
 * @author jan.Adamczyk
 */
public class ComportHandler extends java.util.Observable implements SerialPortDataListener, Runnable {

    private InputStreamMatrix inputStreamMatrix;
    private SerialPort serialPort;
    private Integer channelcount = 0;
    private final AnimatedGraph animated;
    private final ArrayList<Integer> activeChannelList;
    private boolean isInserting = false;
    private boolean isPaused = false;
    private boolean startReading = true;

    private MultipleSeriesHolder multipleSeriesHolder;
    private IndexHolder x_indexHolder;

    private final String comport;
    private final int baudrate;
    private final int dataBits;
    private final int stopBits;
    private TxtLog txtLogger;
    private String txtLogline;

    private HashMap<String, Integer> channelNameNumberAssignment;
    private HashMap<Integer, String> reversedHashMap;
    private final HashMap<Integer, Integer> offsetMap;
    private final HashMap<Integer, Integer> assignedOffset;
    private LocalTime comportReceivedLine_Time;
    private int receivedIncorrectLines = 0;
    private int embeddedLatency;
    private int maxEmbeddedLatency;
    private int toolLatency;
    private boolean disconnect = false;

    @Override
    public void run() {
        try {
            connect(comport, baudrate, dataBits, stopBits);

            txtLogger = new TxtLog(txtLogline);

            animated.setVisible(true);
        } catch (FileNotFoundException ex) {
            Thread t = new Thread(() -> {
                animated.dispose();
                disconnect = true;
                setChanged();
                notifyObservers(true);
                JOptionPane.showMessageDialog(null, ex);
            });
            t.start();
        }
    }

    /**
     *
     * @param comport
     * @param baudrate
     * @param dataBits
     * @param stopBits
     * @param channelCount
     * @param channelNameNumberAssignment
     * @param reversedHashMap
     */
    public ComportHandler(String comport, int baudrate, int dataBits, int stopBits, int channelCount, HashMap<String, Integer> channelNameNumberAssignment, HashMap<Integer, String> reversedHashMap) {
        this.comport = comport;
        this.baudrate = baudrate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.activeChannelList = new ArrayList<>();
        this.reversedHashMap = reversedHashMap;
        this.channelNameNumberAssignment = channelNameNumberAssignment;
//        this.channelNameNumberAssignment = channelNameNumberAssignment;

        //create Animated-Graph-Window
        animated = new AnimatedGraph(channelNameNumberAssignment);
        animated.pack();
        animated.setMinimumSize(new Dimension(500, 500));
        RefineryUtilities.centerFrameOnScreen(animated);

        this.offsetMap = new HashMap<>();
        this.assignedOffset = new HashMap<>();
        for (int index = 0; index < channelCount; index++) {
            this.assignedOffset.put(index, 0);
        }

        multipleSeriesHolder = new MultipleSeriesHolder();
        x_indexHolder = new IndexHolder();
    }

    /**
     *
     * @param comport
     * @param baudrate
     * @param dataBits
     * @param stopBits
     * @throws jssc.SerialPortException
     */
    private void connect(String comport, int baudrate, int dataBits, int stopBits) {
        this.inputStreamMatrix = new InputStreamMatrix();

        //read Port
        serialPort = SerialPort.getCommPort(comport);

        serialPort.openPort();//Open serial port
        serialPort.setComPortParameters(baudrate, dataBits, stopBits, 0);//Set params.
//        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN
//                | SerialPort.FLOWCONTROL_RTSCTS_OUT);

        inputStream = new SerialInputStream(serialPort);
        serialPort.addDataListener(this);
    }

    public void disconnect() {
        serialPort.closePort();
        animated.dispose();
    }

    /**
     *
     * @param event
     */
    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
            return;
        } else {
            try {
                if (startReading) {
                    isInserting = true;
//                    if (inputStream.available() > 10) { // breaks embeddedlatency if streamlength too small
                    while (serialPort.bytesAvailable() > 0) {
                        int lastByte;
                        lastByte = this.serialPort.re

                        if (lastByte == ExceptionChar.EXC_ENQ.getValue()) {
                            //Start of transmission
                            inputStreamMatrix.add(new Stream_Evaluation());
                        } else if (lastByte == ExceptionChar.EXC_EOT.getValue()) {
                            //End of transmission -> evaluation
                            LocalTime eventReceived_Time = getTime();

                            Iterator<Stream_Evaluation> iterator = inputStreamMatrix.iterator();
                            while (iterator.hasNext()) {
                                Stream_Evaluation etbsRemoved = iterator.next().removeETBs();
                                iterator.remove();

                                ArrayList<Integer> bytesFolded = new ArrayList<>();
                                //insert into animatedgraph if correct
                                try {
                                    if (etbsRemoved.checkCorrectness(bytesFolded)) {
                                        int channelNumber = 0;
                                        String txtLogLine = (LocalDate.now().toString() + "\t") + (LocalTime.now().toString() + "\t\t");

                                        //retreive data
                                        SeriesHolder seriesNameAndData = new SeriesHolder();
                                        for (int value : bytesFolded) {
                                            offsetMap.put(channelNumber, value);
                                            if (activeChannelList.contains(channelNumber)) {
                                                seriesNameAndData.add(new SeriesNameAndData(reversedHashMap.get(channelNumber), value - (assignedOffset.get(channelNumber))));
                                            }
                                            txtLogLine += (value + "\t");
                                            channelNumber++;
                                        }

                                        //using the retreived data
                                        txtLogger.newLogLine(txtLogLine);
                                        Thread txtloggerThread = new Thread(txtLogger);
                                        txtloggerThread.start();
                                        int graphMaxLength = animated.getSeriesMaxLength();
                                        multipleSeriesHolder.add(seriesNameAndData, graphMaxLength);
                                        x_indexHolder.add(channelcount, graphMaxLength);
                                        if (!isPaused) {
                                            animated.setDataToProcess(x_indexHolder, multipleSeriesHolder);
                                            Thread animatedThread = new Thread(animated);
                                            animatedThread.setPriority(Thread.NORM_PRIORITY + 3);
                                            animatedThread.start();
                                            animatedThread.join();
                                            multipleSeriesHolder = new MultipleSeriesHolder();
                                            x_indexHolder = new IndexHolder();
                                        }
                                        txtloggerThread.join();

                                        updateFrameLatencys(eventReceived_Time);

                                        channelcount++;
                                    } else {
                                        //stream was corrupted
                                        receivedIncorrectLines++;
                                        if (receivedIncorrectLines == 250) {
                                            JOptionPane.showMessageDialog(null, "Already received 250 corrupt lines!");
                                        }
                                    }
                                } catch (NullPointerException e) {
                                    //List was Empty when end of stream reached
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(ComportHandler.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } else {
                            //add data to List
                            inputStreamMatrix.addLast(lastByte);
                            //TODO: if empty -> errorcount++;
                        }
                    }
//                    }
                    isInserting = false;
                }
            } catch (HeadlessException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    /**
     *
     */
    public void sendCommand() {

    }

    private LocalTime getTime() {
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime zdt = ZonedDateTime.now(zone);
        return zdt.toLocalTime();
    }

    /**
     *
     * @param eventReceived_Time
     */
    public void updateFrameLatencys(LocalTime eventReceived_Time) {
        try {
            embeddedLatency = (int) comportReceivedLine_Time.until(eventReceived_Time, ChronoUnit.MILLIS);
            if (embeddedLatency > maxEmbeddedLatency) {
                maxEmbeddedLatency = embeddedLatency;
            }
            toolLatency = (int) eventReceived_Time.until(getTime(), ChronoUnit.MILLIS);
            setChanged();
            notifyObservers();
            comportReceivedLine_Time = eventReceived_Time;
        } catch (NullPointerException e) {
            comportReceivedLine_Time = getTime();
        }
    }

    /**
     *
     * @return
     */
    public int getEmbeddedLatency() {
        return embeddedLatency;
    }

    /**
     *
     * @return
     */
    public int getMaxEmbeddedLatency() {
        return maxEmbeddedLatency;
    }

    /**
     *
     * @return
     */
    public int getToolLatency() {
        return toolLatency;
    }

    /**
     *
     * @return
     */
    public int getReceivedIncorrectLines() {
        return receivedIncorrectLines;
    }

    /**
     *
     * @param channelNumber
     */
    public void addToActiveChannelList(int channelNumber) {
        activeChannelList.add(channelNumber);
    }

    /**
     *
     * @param channelNumber
     */
    public void removeFromActiveChannelList(int channelNumber) {
        activeChannelList.remove((Object) channelNumber);
    }

    /**
     *
     * @param channelNameNumberAssigment
     * @param reversedHashMap
     */
    public void setHashMaps(HashMap<String, Integer> channelNameNumberAssigment, HashMap<Integer, String> reversedHashMap) {
        this.channelNameNumberAssignment = channelNameNumberAssigment;
        this.reversedHashMap = reversedHashMap;
        setAnimatedHashmap();
    }

    private void setAnimatedHashmap() {
        animated.setHashMap(channelNameNumberAssignment);
    }

    /**
     *
     * @param seriesNames
     */
    public void addToGraph(ArrayList<String> seriesNames) {
        animated.addAllSeries(seriesNames);
    }

    /**
     *
     * @param seriesNames
     */
    public void removeFromGraph(ArrayList<String> seriesNames) {
        animated.removeAllSeries(seriesNames);
    }

    /**
     *
     * @param oldName
     * @param NewName
     */
    public void changeSeriesname(String oldName, String NewName) {
        animated.changeSeriesName(oldName, NewName);
    }

    /**
     *
     * @param lower
     * @param upper
     */
    public void setGraphYRange(int lower, int upper) {
        animated.setyAxisRange(lower, upper);
    }

    /**
     *
     */
    public void resetGraphYRange() {
        animated.resetYRange();
    }

    /**
     *
     * @param range
     */
    public void setGraphXRange(int range) {
        animated.setxAxisRange(range);
    }

    /**
     *
     */
    public void setOffset() {
        assignedOffset.putAll(offsetMap);
        animated.removeAllSeries();
        ArrayList<String> activeChannels = new ArrayList<>();
        activeChannelList.forEach((index) -> {
            activeChannels.add(reversedHashMap.get(index));
        });
        animated.addAllSeries(activeChannels);
    }

    /**
     *
     */
    public void resetOffset() {
        assignedOffset.replaceAll((k, v) -> 0);
        animated.removeAllSeries();
        ArrayList<String> activeChannels = new ArrayList<>();
        activeChannelList.forEach((index) -> {
            activeChannels.add(reversedHashMap.get(index));
        });
        animated.addAllSeries(activeChannels);
    }

    /**
     *
     * @param state
     */
    public void setAntialiasing(boolean state) {
        animated.setAntiAlias(state);
    }

    /**
     *
     */
    @SuppressWarnings("empty-statement")
    public void stopInputAndWait() {
        stopInput();
        while (isInserting);
    }

    /**
     *
     */
    public synchronized void stopInput() {
        startReading = false;
    }

    /**
     *
     */
    public void startReadingComPort() {
        this.startReading = true;
    }

    /**
     *
     * @param isPaused
     */
    public void setIsPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public boolean isDisconnect() {
        return disconnect;
    }
}
