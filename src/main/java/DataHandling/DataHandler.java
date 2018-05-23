/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package DataHandling;

import Frame.Schema.PASSAT_Frame;

/**
 *
 * @author jan.adamczyk
 */
public interface DataHandler {

    /**
     *
     * @param frame
     */
    public void setFrame(PASSAT_Frame frame);

    /**
     *
     */
    public void processData();
}
