/* 
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Graphs;

import Config.Tool_Config.Graph_Config;
import HelpClasses.MultipleSeriesHolder;
import HelpClasses.SeriesNameAndData;
import HelpClasses.ValuesList;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.ImageIcon;
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
public final class JFreeChart_2DLine_Graph extends ApplicationFrame implements Graph {

    private static final String TITLE = "TouchFoilSeries";
    private static final float FATNESS = 3.0f;
    private final XYSeriesCollection dataset;
    private ValueAxis domain;

    private ValueAxis valueAxis;
    private ValueAxis valueAxis2;

    private final JFreeChart jfreeChart;
    private XYPlot plot;
    private ChartpanelUnzoomFixx chartPanel;
    private MultipleSeriesHolder seriesHolder;

    //Config
    private final Graph_Config config;
    private HashMap<String, Integer> channelNameToNumberMapping;
    private ArrayList<Integer> activeChannelList;
    private int seriesMaxLength = 2500;
    private Integer xAxisRange = 500;
    private int lowerY = 0;
    private int upperY = 0;

    private boolean pauseState = false;
    private boolean offsetState = false;
    private Integer actualIndex = 0;
    private ArrayList<Integer> offsetList = new ArrayList<>();

    public void run() {
        try {
            if (!pauseState) {
                dataset.setNotify(false);

                addValuesToSeries(seriesHolder);
                seriesHolder = new MultipleSeriesHolder();
                dataset.setNotify(true);
            }
        } catch (NullPointerException e) {
            System.out.println(e + ": DataSet was not existing yet");
        }
    }

    /**
     *
     * @param valuesList
     */
    @Override
    public void setDataToProcess(ValuesList valuesList) {
        valuesList.stream().map((dataList) -> {
            if (!offsetState) {
                offsetList = new ArrayList<>();
            }
            ArrayList<SeriesNameAndData> seriesNameandData = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                if (offsetState) {
                    dataList.set(i, (dataList.get(i) - offsetList.get(i)));
                } else {
                    offsetList.add(i, dataList.get(i));
                }
                if (activeChannelList.contains(i)) {
                    SeriesNameAndData seriesNameAndData = new SeriesNameAndData(config.getChannelNumberToNameMapping().get(i), dataList.get(i));
                    seriesNameandData.add(seriesNameAndData);
                }
            }
            return seriesNameandData;
        }).forEachOrdered((seriesNameandData) -> {
            seriesHolder.add(seriesNameandData);
        });
    }

    /**
     *
     * @param cfg
     * @param hw_Interface
     */
    public JFreeChart_2DLine_Graph(Graph_Config cfg, String hw_Interface) {
        super(hw_Interface);
        setApplicationIcon();

        //init config
        this.config = cfg;
        initCfg(cfg);

        //create Graph
        dataset = new XYSeriesCollection();
        jfreeChart = createChart(dataset);
        this.seriesHolder = new MultipleSeriesHolder();

        ArrayList<String> activeChannelNames = new ArrayList<>();
        activeChannelList.forEach((i) -> {
            activeChannelNames.add(config.getChannelNumberToNameMapping().get(i));
        });

        addChannels(activeChannelNames);

        setyAxisRange(upperY, lowerY);
        if (cfg.isAutoRange()) {
            setyAxisAutorange(true);
        }

        this.setMinimumSize(new Dimension(500, 500));
        this.pack();
        this.validate();
        this.repaint();
        this.setVisible(true);
    }

    private void initCfg(Graph_Config cfg) {
        this.channelNameToNumberMapping = config.getChannelNameToNumberMapping();
        this.activeChannelList = config.getActiveChannelList();

        this.xAxisRange = cfg.getX_Values_Shown();
        this.seriesMaxLength = cfg.getMaximum_x_Values_Shown();
        this.lowerY = cfg.getMin_y();
        this.upperY = cfg.getMax_y();
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
        chart.setAntiAlias(config.isAntiAliasing());
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
                channelNumber = channelNameToNumberMapping.get(key);
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
     * @param seriesHolder
     */
    public synchronized void addValuesToSeries(MultipleSeriesHolder seriesHolder) {
        seriesHolder.stream().map((valueList) -> {
            try {
                valueList.forEach((value) -> {
                    XYSeries series = dataset.getSeries(value.getNAME());
                    series.add((Number) actualIndex, value.getDATA());
                });
            } catch (UnknownKeyException ex) {
                //Series was active in Comporthandler but not in Graph -> probably deactivated while Pausing
            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(null, ex);
            }
            return valueList;
        }).forEachOrdered((_item) -> {
            actualIndex++;
        });
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
        resetYRange();
        valueAxis.setAutoRange(bool);
        valueAxis2.setAutoRange(bool);
//        Range range = valueAxis.getRange();
//        valueAxis2.setRange(range);
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
     * @param channelNames
     */
    @Override
    public void removeChannels(ArrayList<String> channelNames) {
        channelNames.forEach((name) -> {
            try {
                XYSeries series = dataset.getSeries(name);
//                series.clear();
                dataset.removeSeries(series);
                activeChannelList.remove(channelNameToNumberMapping.get(series.getKey().toString()));
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
    @Override
    public void addChannels(ArrayList<String> channelNames) {
        channelNames.forEach((channelName) -> {
            XYSeries series = new XYSeries(channelName, false, false);
            series.setMaximumItemCount(seriesMaxLength);
            dataset.addSeries(series);
            plot.getRenderer().setSeriesStroke(dataset.getSeriesIndex(channelName), new BasicStroke(FATNESS));
        });
        setAllSeriesColor();
    }

    @Override
    public void setPause(boolean state) {
        pauseState = state;
    }

    @Override
    public void setOffset(boolean state) {
        offsetState = state;
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

    /**
     *
     * @return
     */
    public int getSeriesMaxLength() {
        return seriesMaxLength;
    }
}