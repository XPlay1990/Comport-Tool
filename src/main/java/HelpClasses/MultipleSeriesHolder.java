/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package HelpClasses;

import java.util.ArrayList;

/**
 *
 * @author Jan.Adamczyk
 */
public class MultipleSeriesHolder extends ArrayList<SeriesHolder> {

    public boolean add(SeriesHolder e, int maxLength) {
        while (this.size() > (maxLength - 1)) {
            this.remove(0);
        }
        return super.add(e); //To change body of generated methods, choose Tools | Templates.
    }
}
