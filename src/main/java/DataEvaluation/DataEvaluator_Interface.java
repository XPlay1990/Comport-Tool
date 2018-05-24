/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package DataEvaluation;

import java.util.ArrayList;

/**
 *
 * @author jan.adamczyk
 */
public interface DataEvaluator_Interface {

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
