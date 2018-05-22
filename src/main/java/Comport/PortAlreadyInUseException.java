/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Comport;

/**
 *
 * @author jan.adamczyk
 */
public class PortAlreadyInUseException extends Exception {

    /**
     *
     * @param message
     */
    public PortAlreadyInUseException(String message) {
        super(message);
    }
    
}
