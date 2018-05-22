/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Config.HW_Config;

/**
 *
 * @author jan.adamczyk
 */
public class Comport_Config {

    //default values before configread
    private String portname = "COM1";
    private int baudrate = 400000;
    private int dataBits = 8;
    private int stopBits = 1;
    private int shownChannels = 60;

    /**
     *
     * @return
     */
    public String getPortname() {
        return portname;
    }

    /**
     *
     * @param portname
     */
    public void setPortname(String portname) {
        this.portname = portname;
    }

    /**
     *
     * @return
     */
    public int getBaudrate() {
        return baudrate;
    }

    /**
     *
     * @param baudrate
     */
    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    /**
     *
     * @return
     */
    public int getDataBits() {
        return dataBits;
    }

    /**
     *
     * @param dataBits
     */
    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    /**
     *
     * @return
     */
    public int getStopBits() {
        return stopBits;
    }

    /**
     *
     * @param stopBits
     */
    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    /**
     *
     * @return
     */
    public int getShownChannels() {
        return shownChannels;
    }

    /**
     *
     * @param shownChannels
     */
    public void setShownChannels(int shownChannels) {
        this.shownChannels = shownChannels;
    }
    
    
}
