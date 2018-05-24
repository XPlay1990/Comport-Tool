/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package DataHandling;

import java.util.ArrayList;

/**
 *
 * @author jan.adamczyk
 */
public interface DataHandler {

    /**
     *
     * @param data
     */
    public void setData(ArrayList<Integer> data);

    /**
     *
     */
    public void processData();
}
