/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import java.util.ArrayList;

/**
 *
 * @author michael.machate
 */
public class Node_Red_Frame {

    private ArrayList<Integer> data;
    private Integer frequency;

    /**
     *
     * @param data
     * @param frequency
     */
    public Node_Red_Frame(ArrayList<Integer> data, Integer frequency) {
        this.data = data;
        this.frequency = frequency;
    }

    /**
     *
     * @return
     */
    public ArrayList<Integer> getData() {
        return data;
    }

    /**
     *
     * @param data
     */
    public void setData(ArrayList<Integer> data) {
        this.data = data;
    }

    /**
     *
     * @return
     */
    public Integer getFrequency() {
        return frequency;
    }

    /**
     *
     * @param Frequency
     */
    public void setFrequency(Integer Frequency) {
        this.frequency = Frequency;
    }
}
