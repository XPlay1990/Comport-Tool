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

    public String getPortname() {
        return portname;
    }

    public void setPortname(String portname) {
        this.portname = portname;
    }

    public int getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public int getShownChannels() {
        return shownChannels;
    }

    public void setShownChannels(int shownChannels) {
        this.shownChannels = shownChannels;
    }
    
    
}
