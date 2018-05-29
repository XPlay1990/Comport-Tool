/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package DataEvaluation;

import HelpClasses.ValuesList;

/**
 *
 * @author jan.adamczyk
 */
public interface DataEvaluator_Interface extends Runnable {

    /**
     *
     * @param data
     */
    public void setData(ValuesList data);

    /**
     *
     */
    public void processData();
}
