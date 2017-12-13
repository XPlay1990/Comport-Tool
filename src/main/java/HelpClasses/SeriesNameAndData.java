/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package HelpClasses;

/**
 *
 * @author Jan.Adamczyk
 */
public class SeriesNameAndData {
    private final String NAME;
    private final int DATA;

    /**
     *
     * @param Name
     * @param Data
     */
    public SeriesNameAndData(String Name, int Data) {
        this.DATA = Data;
        this.NAME = Name;
    }

    /**
     *
     * @return
     */
    public String getNAME() {
        return this.NAME;
    }

    /**
     *
     * @return
     */
    public int getDATA() {
        return DATA;
    }
}
