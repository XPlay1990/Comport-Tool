/* 
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Graphs;

import HelpClasses.SeriesNameAndData;
import HelpClasses.SeriesHolder;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ui.ApplicationFrame;

/**
 * creates a live-graph of the received data
 *
 * @author Jan.Adamczyk
 */
public final class AnimatedGraph extends ApplicationFrame implements Runnable {

    private static final String TITLE = "TouchFoilSeries";
    private static final float FATNESS = 3.0f;
    private final XYSeriesCollection dataset;
    private Integer xAxisRange = 500;
    private ValueAxis domain;
    private int lowerY = 0;
    private int upperY = 0;
    private ValueAxis valueAxis;
    private ValueAxis valueAxis2;
    private final int seriesMaxLength = 2500;
    private final JFreeChart jfreeChart;
    private XYPlot plot;
    private HashMap<String, Integer> channelNameNumberAssigment;
    private ChartpanelUnzoomFixx chartPanel;
//    private final ArrayList<JCheckBox> checkboxes;

    private ArrayList<Integer> x_indexList;
    private ArrayList<SeriesHolder> seriesNameandData;

    public void run() {
        try {
            dataset.setNotify(false);
            for (int index = 0; index < x_indexList.size(); index++) {
                addValuesToSeries(x_indexList.get(index), seriesNameandData.get(index));
            }
            dataset.setNotify(true);
        } catch (NullPointerException e) {
            System.out.println(e + ": DataSet was not existing yet");
        }
    }

    /**
     *
     * @param indexList
     * @param seriesNameAndData
     */
    public void setDataToProcess(ArrayList<Integer> indexList, ArrayList<SeriesHolder> seriesNameAndData) {
        this.x_indexList = indexList;
        this.seriesNameandData = seriesNameAndData;
    }

    /**
     *
     * @param channelNameNumberAssigment
     */
    public AnimatedGraph(HashMap<String, Integer> channelNameNumberAssigment, String comport) {
        super(comport);
        setApplicationIcon();
        this.channelNameNumberAssigment = channelNameNumberAssigment;
        dataset = new XYSeriesCollection();
        jfreeChart = createChart(dataset);
        this.validate();
        this.repaint();
    }

    private JFreeChart createChart(final XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(TITLE, "", "TouchValue", dataset, PlotOrientation.VERTICAL, true, true, false);
        plot = chart.getXYPlot();
        domain = plot.getDomainAxis();
        domain.setFixedAutoRange(xAxisRange);
        valueAxis = plot.getRangeAxis();
        valueAxis.setAutoRange(true);
        try {
            valueAxis2 = (ValueAxis) valueAxis.clone();
        } catch (CloneNotSupportedException ex) {
        }
        valueAxis2.setAutoRange(true);

        plot.setRangeAxis(1, valueAxis2);
        plot.setRangeAxisLocation(0, AxisLocation.TOP_OR_RIGHT);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_LEFT);
        plot.mapDatasetToRangeAxes(0, Arrays.asList(0, 1));

        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.BLACK);
        plot.setDomainGridlineStroke(new BasicStroke(0));
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setRangeGridlineStroke(new BasicStroke(0));

//        plot.setDomainGridlinesVisible(false);
//        plot.setRangeGridlinesVisible(false);
        chart.setBackgroundPaint(Color.GRAY);
        chartPanel = new ChartpanelUnzoomFixx(chart, lowerY, upperY);
        this.add(chartPanel, BorderLayout.CENTER);
        chart.setAntiAlias(true);
        return chart;
    }

    /**
     *
     * @param bool
     */
    public synchronized void setAntiAlias(Boolean bool) {
        jfreeChart.setAntiAlias(bool);
    }

    /**
     *
     */
    public void removeAllSeries() {
        dataset.removeAllSeries();
    }

    /**
     *
     * @param names
     */
    public void removeAllSeries(ArrayList<String> names) {
        names.forEach((name) -> {
            try {
                XYSeries series = dataset.getSeries(name);
                series.clear();
                dataset.removeSeries(series);
                setAllSeriesColor();
            } catch (UnknownKeyException e) {
                //Series isnt in List
            }
        });
    }

    /**
     *
     * @param channelNames
     */
    public void addAllSeries(ArrayList<String> channelNames) {
        channelNames.forEach((channelName) -> {
            XYSeries series = new XYSeries(channelName, false, false);
            series.setMaximumItemCount(seriesMaxLength);
            dataset.addSeries(series);
            plot.getRenderer().setSeriesStroke(dataset.getSeriesIndex(channelName), new BasicStroke(FATNESS));
        });
        setAllSeriesColor();
    }

    /**
     *
     */
    public void setAllSeriesColor() {
        int index = 0;
        Color color;

        Iterator iterator = dataset.getSeries().iterator();
        while (iterator.hasNext()) {
            XYSeries next = (XYSeries) iterator.next();
            String key = (String) next.getKey();
            int channelNumber = 0;
            try {
                channelNumber = channelNameNumberAssigment.get(key);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            switch (channelNumber % 17) {
                case 0:
                    color = ChartColor.red;
                    break;
                case 1:
                    color = ChartColor.blue;
                    break;
                case 2:
                    color = ChartColor.green;
                    break;
                case 3:
                    color = ChartColor.magenta;
                    break;
                case 4:
                    color = ChartColor.yellow;
                    break;
                case 5:
                    color = ChartColor.cyan;
                    break;
                case 6:
                    color = ChartColor.LIGHT_RED;
                    break;
                case 7:
                    color = ChartColor.LIGHT_BLUE;
                    break;
                case 8:
                    color = ChartColor.LIGHT_GREEN;
                    break;
                case 9:
                    color = ChartColor.LIGHT_MAGENTA;
                    break;
                case 10:
                    color = ChartColor.LIGHT_YELLOW;
                    break;
                case 11:
                    color = ChartColor.LIGHT_CYAN;
                    break;
                case 12:
                    color = ChartColor.DARK_RED;
                    break;
                case 13:
                    color = ChartColor.DARK_BLUE;
                    break;
                case 14:
                    color = ChartColor.DARK_GREEN;
                    break;
                case 15:
                    color = ChartColor.DARK_MAGENTA;
                    break;
                case 16:
                    color = ChartColor.DARK_YELLOW;
                    break;
                default:
                    color = ChartColor.red;
                    break;
            }
            plot.getRenderer().setSeriesPaint(index, color);
            index++;
        }
    }

    /**
     *
     * @param index
     * @param valueList
     */
    public synchronized void addValuesToSeries(int index, ArrayList<SeriesNameAndData> valueList) {
        try {
            valueList.forEach((value) -> {
                XYSeries series = dataset.getSeries(value.getNAME());
                series.add(index, value.getDATA());
            });
        } catch (UnknownKeyException ex) {
            //Series was active in Comporthandler but not in Graph -> probably deactivated while Pausing
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    /**
     *
     * @param oldName
     * @param newName
     */
    public void changeSeriesName(String oldName, String newName) {
        try {
            XYSeries series = dataset.getSeries(oldName);
            series.setKey(newName);
        } catch (UnknownKeyException ex) {
//            JOptionPane.showMessageDialog(null, ex);
        }
    }

    /**
     *
     * @param xAxisRange
     */
    public void setxAxisRange(Integer xAxisRange) {
        try {
            this.xAxisRange = xAxisRange;
            domain.setFixedAutoRange(xAxisRange);
        } catch (NullPointerException e) {

        }
    }

    /**
     *
     * @param lower
     * @param upper
     */
    public void setyAxisRange(int lower, int upper) {
        this.lowerY = lower;
        this.upperY = upper;
        chartPanel.setYValues(lowerY, upperY);
        setYRange();
    }

    /**
     *
     */
    public void resetYRange() {
        this.lowerY = 0;
        this.upperY = 0;
        chartPanel.setYValues(lowerY, upperY);
        setYRange();
    }

    private void setYRange() {
        try {
            valueAxis.setRange(lowerY, upperY);
            valueAxis2.setRange(lowerY, upperY);
        } catch (IllegalArgumentException e) {
            valueAxis.setAutoRange(true);
            valueAxis2.setAutoRange(true);
        }
    }

    /**
     *
     * @param bool
     */
    public void setyAxisAutorange(boolean bool) {
        valueAxis.setAutoRange(bool);
        valueAxis2.setAutoRange(bool);
        Range range = valueAxis.getRange();
        valueAxis2.setRange(range);
    }

    /**
     *
     * @return
     */
    public XYSeriesCollection getDataset() {
        return dataset;
    }

    /**
     *
     * @param channelNameNumberAssigment
     */
    public void setHashMap(HashMap<String, Integer> channelNameNumberAssigment) {
        this.channelNameNumberAssigment = channelNameNumberAssigment;
    }

    private class ChartpanelUnzoomFixx extends ChartPanel {

        private int maxYValue;
        private int minYValue;

        public ChartpanelUnzoomFixx(JFreeChart chart, int minYValue, int maxYValue) {
            super(chart);
            this.minYValue = minYValue;
            this.maxYValue = maxYValue;
        }

        public void setYValues(int lower, int upper) {
            this.maxYValue = upper;
            this.minYValue = lower;
        }

        @Override
        public void restoreAutoBounds() {
            super.restoreAutoDomainBounds();

            try {
                valueAxis.setAutoRange(false);
                valueAxis2.setAutoRange(false);
                valueAxis.setRange(minYValue, maxYValue);
                Range range = valueAxis.getRange();
                valueAxis2.setRange(range);
            } catch (IllegalArgumentException e) {
                try {
                    super.restoreAutoRangeBounds();
                } catch (IndexOutOfBoundsException ex) {

                }
            }
        }
    }

    String time() {
//        return Instant.now().toString();
        LocalTime toLocalTime = getTime();
        return toLocalTime.toString();
    }

    LocalTime getTime() {
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime zdt = ZonedDateTime.now(zone);
        return zdt.toLocalTime();
    }

    private void setApplicationIcon() {
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/Pictures/logo.jpg"));
        Image img = icon.getImage();

        this.setIconImage(img);
    }

    public int getSeriesMaxLength() {
        return seriesMaxLength;
    }
}
