/* 
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Protocol;

import java.util.ArrayList;

/**
 * handles the initial input received by the comport, before the data are
 * processed
 *
 * @author Jan Adamczyk
 */
public class InputStreamMatrix extends ArrayList<Stream_Evaluation> {

    /**
     *
     */
    public InputStreamMatrix() {
        super();
    }

    /**
     *
     * @param line
     * @param element
     */
    public void addLine(int line, Integer element) {
        while (line >= this.size()) {
            this.add(new Stream_Evaluation());
        }
        this.get(line).add(element);
    }

    /**
     *
     * @param line
     * @param column
     * @param element
     */
    public void addLineValues(int line, int column, Integer element) {
        while (line >= this.size()) {
            this.add(new Stream_Evaluation());
        }

        ArrayList<Integer> values = this.get(line);
        while (column >= values.size()) {
            values.add(null);
        }
        values.set(column, element);
    }

    /**
     *
     * @param columnNumber
     * @return
     */
    public ArrayList<Integer> getColumn(int columnNumber) {
        ArrayList<Integer> columnList = new ArrayList<>();
        this.forEach((aThi) -> {
            columnList.add(aThi.get(columnNumber));
        });
        return columnList;
    }

    /**
     *
     * @param rowNumber
     * @return
     */
    public ArrayList<Integer> getRow(int rowNumber) {
        return (this.get(rowNumber));
    }

    /**
     *
     * @param rowNumber
     * @return
     */
    public ArrayList<Integer> getNoDateRow(int rowNumber) {
        for (int dateColumns = 0; dateColumns < 3; dateColumns++) {
            this.get(rowNumber).remove(0);
        }
        return (this.get(rowNumber));
    }

    /**
     *
     * @return
     */
    public int getRowNumber() {
        return (this.size());
    }

    /**
     *
     * @return
     */
    public int getColumnNumber() {
        return this.getRow(0).size();
    }

    /**
     *
     * @return
     */
    public ArrayList<Integer> getLast() {
        return this.get(this.size() - 1);
    }

    /**
     *
     */
    public void removeFirst() {
        if (this.size() >= 1) {
            this.remove(0);
        }
    }

    /**
     *
     * @return
     */
    public Stream_Evaluation removeETBsAtFirstlist() {
        Stream_Evaluation evaluate = null;
        try {
            evaluate = this.get(0).removeETBs();
        } catch (Exception e) {
            //TODO: catch specific exception
        }
        return evaluate;
    }

    /**
     *
     * @param element
     */
    public void addLast(Integer element) {
        if (!this.isEmpty()) {
            this.getLast().add(element);
        }
    }
}
