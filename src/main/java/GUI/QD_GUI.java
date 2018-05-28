/* 
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package GUI;

import Config.Config_JSON;
import Config.Tool_Config.Graph_Config;
import Config.Tool_Config.Server_Config;
import Config.Tool_Config.Tool_Config;
import DataEvaluation.DataEvaluator_Abstract;
import DataEvaluation.MWO_DataEvaluator;
import Frame.newpackage.TEST_FRAME_AQJOIN_REQ;
import Graphs.Graph;
import Graphs.JFreeChart_2DLine_Graph;
import Socket.SocketHandler;
import HelpClasses.Sorting.AlphanumComparator;
import com.google.gson.JsonSyntaxException;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * GUI class to access the tool
 *
 * @author jan.Adamczyk
 */
public final class QD_GUI extends javax.swing.JFrame implements Observer {

    private Config_JSON config;
    private Server_Config serverConfig;
    private Graph_Config graphConfig;
    private String config_FileName = "Config.json";
    private final String config_Fallback_FileName = "Fallback_Config.json";

    private SocketHandler socketHandler;
    private DataEvaluator_Abstract dataEvaluator;
    private Graph graph;

    private String[] portNames;
    private boolean isPaused = false;
    private HashMap<String, Integer> channelNameNumberAssignment;
    private HashMap<Integer, String> reversedHashMap;
    ArrayList<String> allChannelNames;

    private final String serverSelect_CardLayout = "serverSelect_CardLayout";
    private final String toolConnect_CardLayout = "toolConnect_CardLayout";
    private final String channelSelect_CardLayout = "channelSelect_CardLayout";

    /**
     * Creates new form Frame
     *
     */
    public QD_GUI() {
        init();
    }

    /**
     * init all Components
     */
    private void init() {
        initComponents();
        setPlayPauseIcon("pause");
        setApplicationIcon();

        initConfig();
        saveConfigOnClose();

        this.validate();
        this.repaint();
    }

    private void initConfig() {
        getConfig();

        //use config to initialize program
        initGUI();
    }

    private void getConfig() {
        try {
            config = Config_JSON.main(config_FileName);
        } catch (FileNotFoundException ex) {
            config = new Config_JSON();
        } catch (JsonSyntaxException ex) {
            JOptionPane.showMessageDialog(this, config_FileName + " corrupted. Creating " + config_Fallback_FileName);
            config = new Config_JSON();
            config_FileName = config_Fallback_FileName;
        }
        Tool_Config toolConfig = config.getTool_Config();

        this.serverConfig = toolConfig.getServer_Config();
        this.graphConfig = toolConfig.getGraph_Config();
    }

    private void saveConfigOnClose() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveConfigToFile();
                System.exit(0);
            }
        });
    }

    private void saveConfigToFile() {
        try {
            config.toJSON(config_FileName);
        } catch (IOException ex) {
            int response = JOptionPane.showConfirmDialog(this, "Retry?", "Could not save Config-File",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            switch (response) {
                case JOptionPane.YES_OPTION:
                    saveConfigToFile();
                    break;
                case JOptionPane.NO_OPTION:
                case JOptionPane.CANCEL_OPTION:
                    break;
            }
        }

        TEST_FRAME_AQJOIN_REQ x = new TEST_FRAME_AQJOIN_REQ();
        x.toJson();
    }

    private void initGUI() {
        //Init ServerList
        HashMap<String, String> serverHashMap = serverConfig.getServerList();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        serverHashMap.entrySet().forEach((entry) -> {
//            model.addElement(entry.getKey() + " (" + entry.getValue() + ")");
            model.addElement(entry.getKey());
        });
        this.serverSelect_ComboBox.setModel(model);

        //Init selected Server
        serverSelect_ComboBox.setSelectedItem(serverConfig.getDefaultServer());

        //Init autoConnect to Server
        autoConnect_Checkbox.setSelected(serverConfig.getAutoConnectToServer());

    }

    private void setApplicationIcon() {
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/Pictures/logo.jpg"));
        Image img = icon.getImage();

        this.setIconImage(img);
    }

//    private void initConfig() {
//        SerialPort[] commPorts = SerialPort.getCommPorts();
//        ArrayList<String> portnameList = new ArrayList<>();
//        for (SerialPort comport : commPorts) {
//            portnameList.add(comport.getDescriptivePortName());
//        }
//        boolean contains = portnameList.contains(portname);
//        refreshAndSetToolConnect();
//        if (contains) {
//            comboBoxPortChooser.setSelectedItem(portname);
//        }
//
//        jTextFieldBaudrate.setText(String.valueOf(baudrate));
//        jTextFieldDataBits.setText(String.valueOf(dataBits));
//        jTextFieldStopBits.setText(String.valueOf(stopBits));
//        jTextFieldNewChannelNumber.setText(String.valueOf(shownChannels));
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        serverSelect_Panel = new javax.swing.JPanel();
        serverSelect_ComboBox = new javax.swing.JComboBox<>();
        jLabelQDIcon1 = new javax.swing.JLabel();
        autoConnect_Checkbox = new javax.swing.JCheckBox();
        connectServer_Button = new javax.swing.JButton();
        toolConnect = new javax.swing.JPanel();
        jPanelConnectButtonPanel = new javax.swing.JPanel();
        jTextFieldBaudrate = new javax.swing.JTextField();
        jTextFieldDataBits = new javax.swing.JTextField();
        jTextFieldStopBits = new javax.swing.JTextField();
        jLabelBaudrate = new javax.swing.JLabel();
        jLabelDataBits = new javax.swing.JLabel();
        jLabelStopBits = new javax.swing.JLabel();
        jPanelLogoPanel = new javax.swing.JPanel();
        jLabelQDIcon = new javax.swing.JLabel();
        jTextFieldNewChannelNumber = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        connectAQ_Button = new javax.swing.JButton();
        disconnectServer_Button = new javax.swing.JButton();
        comboBoxPortChooser = new javax.swing.JComboBox<>();
        channelSelect = new javax.swing.JPanel();
        jPanelControlPanel = new javax.swing.JPanel();
        channelPanel = new javax.swing.JPanel();
        jScrollPaneInactiveChannels = new javax.swing.JScrollPane();
        jListInactiveChannels = new javax.swing.JList<>();
        jScrollPaneActiveChannels = new javax.swing.JScrollPane();
        jListActiveChannels = new javax.swing.JList<>();
        ChannelRemove = new GUI.JButtonArrow();
        ChannelAdd = new GUI.JButtonArrow();
        jLabelInactiveChannels = new javax.swing.JLabel();
        jLabelActiveChannels = new javax.swing.JLabel();
        channelControlPanel = new javax.swing.JPanel();
        jTextFieldNewChannelName = new javax.swing.JTextField();
        jButtonSetChannelName = new javax.swing.JButton();
        jLabelNewChannelName = new javax.swing.JLabel();
        jLabelOldChannelName = new javax.swing.JLabel();
        jComboBoxOldChannelName = new javax.swing.JComboBox<>();
        ErrorPanel = new javax.swing.JPanel();
        jLabelCommErrors = new javax.swing.JLabel();
        jTextFieldErrorCount = new javax.swing.JTextField();
        ControlButtons = new javax.swing.JPanel();
        jButtonDisconnect = new javax.swing.JButton();
        jButtonPlayPause = new javax.swing.JButton();
        jButtonSetMinMaxY = new javax.swing.JButton();
        jToggleButtonOffsetRaw = new javax.swing.JToggleButton();
        jTextFieldMinY = new javax.swing.JTextField();
        jLabelMinY = new javax.swing.JLabel();
        jTextFieldMaxY = new javax.swing.JTextField();
        jLabelMaxY = new javax.swing.JLabel();
        jComboBoxValuesShown = new javax.swing.JComboBox<>();
        jLabelValueNumber = new javax.swing.JLabel();
        jToggleButtonChannelOnOff = new javax.swing.JToggleButton();
        EmbeddedLatencyPanel = new javax.swing.JPanel();
        jLabelEmbeddedLatency = new javax.swing.JLabel();
        jTextFieldEmbeddedLatency = new javax.swing.JTextField();
        maxEmbeddedLatency = new javax.swing.JTextField();
        ToolLatencyPanel = new javax.swing.JPanel();
        jLabelToolLatency = new javax.swing.JLabel();
        jTextFieldToolLatency = new javax.swing.JTextField();
        jLabelAntiAliasing = new javax.swing.JLabel();
        jToggleButtonAntiAliasing = new javax.swing.JToggleButton();
        jButtonResetY = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setLayout(new java.awt.CardLayout());

        serverSelect_ComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Server" }));

        jLabelQDIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pictures/logo.jpg"))); // NOI18N

        autoConnect_Checkbox.setText("AutoConnect");

        connectServer_Button.setText("Connect");
        connectServer_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectServer_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout serverSelect_PanelLayout = new javax.swing.GroupLayout(serverSelect_Panel);
        serverSelect_Panel.setLayout(serverSelect_PanelLayout);
        serverSelect_PanelLayout.setHorizontalGroup(
            serverSelect_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serverSelect_PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(serverSelect_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(connectServer_Button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(serverSelect_PanelLayout.createSequentialGroup()
                        .addComponent(serverSelect_ComboBox, 0, 476, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelQDIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(serverSelect_PanelLayout.createSequentialGroup()
                        .addComponent(autoConnect_Checkbox)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        serverSelect_PanelLayout.setVerticalGroup(
            serverSelect_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serverSelect_PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(serverSelect_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(serverSelect_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelQDIcon1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(autoConnect_Checkbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 390, Short.MAX_VALUE)
                .addComponent(connectServer_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        mainPanel.add(serverSelect_Panel, "serverSelect_CardLayout");

        jTextFieldBaudrate.setText("400000");

        jTextFieldDataBits.setText("8");

        jTextFieldStopBits.setText("1");

        jLabelBaudrate.setText("Baudrate:");

        jLabelDataBits.setText("DataBits:");

        jLabelStopBits.setText("StopBits:");

        javax.swing.GroupLayout jPanelConnectButtonPanelLayout = new javax.swing.GroupLayout(jPanelConnectButtonPanel);
        jPanelConnectButtonPanel.setLayout(jPanelConnectButtonPanelLayout);
        jPanelConnectButtonPanelLayout.setHorizontalGroup(
            jPanelConnectButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConnectButtonPanelLayout.createSequentialGroup()
                .addGroup(jPanelConnectButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelBaudrate, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                    .addComponent(jTextFieldBaudrate, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                    .addComponent(jLabelDataBits, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                    .addComponent(jTextFieldDataBits, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                    .addComponent(jLabelStopBits, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                    .addComponent(jTextFieldStopBits, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                .addContainerGap(253, Short.MAX_VALUE))
        );

        jPanelConnectButtonPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabelBaudrate, jLabelDataBits, jLabelStopBits, jTextFieldBaudrate, jTextFieldDataBits, jTextFieldStopBits});

        jPanelConnectButtonPanelLayout.setVerticalGroup(
            jPanelConnectButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConnectButtonPanelLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(jLabelBaudrate, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldBaudrate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelDataBits, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldDataBits, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelStopBits, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldStopBits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
        );

        jPanelConnectButtonPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabelBaudrate, jLabelDataBits, jLabelStopBits, jTextFieldBaudrate, jTextFieldDataBits, jTextFieldStopBits});

        jLabelQDIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pictures/logo.jpg"))); // NOI18N

        jTextFieldNewChannelNumber.setText("60");

        jLabel1.setText("Shown Channels");

        javax.swing.GroupLayout jPanelLogoPanelLayout = new javax.swing.GroupLayout(jPanelLogoPanel);
        jPanelLogoPanel.setLayout(jPanelLogoPanelLayout);
        jPanelLogoPanelLayout.setHorizontalGroup(
            jPanelLogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLogoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldNewChannelNumber)
                    .addGroup(jPanelLogoPanelLayout.createSequentialGroup()
                        .addGroup(jPanelLogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelQDIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelLogoPanelLayout.setVerticalGroup(
            jPanelLogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLogoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelQDIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(10, 10, 10)
                .addComponent(jTextFieldNewChannelNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        connectAQ_Button.setText("Connect AQ");
        connectAQ_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectAQ_ButtonActionPerformed(evt);
            }
        });

        disconnectServer_Button.setText("Disconnect Server");
        disconnectServer_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectServer_ButtonActionPerformed(evt);
            }
        });

        comboBoxPortChooser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Choose Port" }));

        javax.swing.GroupLayout toolConnectLayout = new javax.swing.GroupLayout(toolConnect);
        toolConnect.setLayout(toolConnectLayout);
        toolConnectLayout.setHorizontalGroup(
            toolConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolConnectLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(toolConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(toolConnectLayout.createSequentialGroup()
                        .addGroup(toolConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelConnectButtonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboBoxPortChooser, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(12, 12, 12))
                    .addGroup(toolConnectLayout.createSequentialGroup()
                        .addComponent(connectAQ_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(disconnectServer_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanelLogoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        toolConnectLayout.setVerticalGroup(
            toolConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolConnectLayout.createSequentialGroup()
                .addGroup(toolConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(toolConnectLayout.createSequentialGroup()
                        .addComponent(comboBoxPortChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelConnectButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(toolConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(connectAQ_Button, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                            .addComponent(disconnectServer_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanelLogoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        toolConnectLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {comboBoxPortChooser, connectAQ_Button});

        mainPanel.add(toolConnect, "toolConnect_CardLayout");

        jListInactiveChannels.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jListInactiveChannels.setAutoscrolls(false);
        jScrollPaneInactiveChannels.setViewportView(jListInactiveChannels);

        jListActiveChannels.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPaneActiveChannels.setViewportView(jListActiveChannels);

        ChannelRemove.setBackground(new java.awt.Color(107, 106, 104));
        ChannelRemove.setText("jButtonArrowRemoveFromActiveChannels");
        ChannelRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChannelRemoveActionPerformed(evt);
            }
        });

        ChannelAdd.setBackground(new java.awt.Color(107, 106, 104));
        ChannelAdd.setActionCommand("jButtonArrowToActiveChannels");
        ChannelAdd.setDirection(3);
        ChannelAdd.setLabel("Add to active Channels");
        ChannelAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChannelAddActionPerformed(evt);
            }
        });

        jLabelInactiveChannels.setText("Inactive");

        jLabelActiveChannels.setText("Active");

        javax.swing.GroupLayout channelPanelLayout = new javax.swing.GroupLayout(channelPanel);
        channelPanel.setLayout(channelPanelLayout);
        channelPanelLayout.setHorizontalGroup(
            channelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(channelPanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(channelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelInactiveChannels)
                    .addComponent(jScrollPaneInactiveChannels, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(channelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ChannelRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChannelAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(channelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneActiveChannels, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelActiveChannels))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        channelPanelLayout.setVerticalGroup(
            channelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, channelPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(channelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelInactiveChannels)
                    .addComponent(jLabelActiveChannels))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(channelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, channelPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPaneActiveChannels, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, channelPanelLayout.createSequentialGroup()
                        .addComponent(ChannelRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ChannelAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(101, 101, 101))
                    .addGroup(channelPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPaneInactiveChannels)
                        .addContainerGap())))
        );

        jTextFieldNewChannelName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldNewChannelNameKeyPressed(evt);
            }
        });

        jButtonSetChannelName.setText("Set Ch.-Name");
        jButtonSetChannelName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSetChannelNameActionPerformed(evt);
            }
        });

        jLabelNewChannelName.setText("New Name:");

        jLabelOldChannelName.setText("Old Name:");

        jComboBoxOldChannelName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { }));

        ErrorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ErrorPanel.setForeground(new java.awt.Color(107, 106, 104));

        jLabelCommErrors.setBackground(new java.awt.Color(255, 255, 0));
        jLabelCommErrors.setText("Comm. Errors:");
        jLabelCommErrors.setOpaque(true);

        jTextFieldErrorCount.setEditable(false);
        jTextFieldErrorCount.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldErrorCount.setText("0");
        jTextFieldErrorCount.setToolTipText("Number of detected broken communications");
        jTextFieldErrorCount.setFocusable(false);

        javax.swing.GroupLayout ErrorPanelLayout = new javax.swing.GroupLayout(ErrorPanel);
        ErrorPanel.setLayout(ErrorPanelLayout);
        ErrorPanelLayout.setHorizontalGroup(
            ErrorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ErrorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ErrorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ErrorPanelLayout.createSequentialGroup()
                        .addComponent(jLabelCommErrors, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(3, 3, 3))
                    .addGroup(ErrorPanelLayout.createSequentialGroup()
                        .addComponent(jTextFieldErrorCount, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        ErrorPanelLayout.setVerticalGroup(
            ErrorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ErrorPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelCommErrors)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldErrorCount, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout channelControlPanelLayout = new javax.swing.GroupLayout(channelControlPanel);
        channelControlPanel.setLayout(channelControlPanelLayout);
        channelControlPanelLayout.setHorizontalGroup(
            channelControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, channelControlPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ErrorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(channelControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(channelControlPanelLayout.createSequentialGroup()
                        .addGroup(channelControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelOldChannelName, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxOldChannelName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(channelControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldNewChannelName, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelNewChannelName, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(channelControlPanelLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jButtonSetChannelName)))
                .addContainerGap())
        );
        channelControlPanelLayout.setVerticalGroup(
            channelControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, channelControlPanelLayout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addGroup(channelControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ErrorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(channelControlPanelLayout.createSequentialGroup()
                        .addGroup(channelControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelNewChannelName)
                            .addComponent(jLabelOldChannelName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(channelControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldNewChannelName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxOldChannelName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSetChannelName)))
                .addGap(23, 23, 23))
        );

        jButtonDisconnect.setText("Disconnect");
        jButtonDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisconnectActionPerformed(evt);
            }
        });

        jButtonPlayPause.setMaximumSize(new java.awt.Dimension(339, 265));
        jButtonPlayPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlayPauseActionPerformed(evt);
            }
        });

        jButtonSetMinMaxY.setText("Set_Y");
        jButtonSetMinMaxY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSetMinMaxYActionPerformed(evt);
            }
        });

        jToggleButtonOffsetRaw.setText("Set Offset");
        jToggleButtonOffsetRaw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonOffsetRawActionPerformed(evt);
            }
        });

        jTextFieldMinY.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldMinYKeyPressed(evt);
            }
        });

        jLabelMinY.setText("MinY:");

        jTextFieldMaxY.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldMaxYKeyPressed(evt);
            }
        });

        jLabelMaxY.setText("MaxY:");

        jComboBoxValuesShown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"50", "100", "250", "500", "1000", "2500" }));
        jComboBoxValuesShown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxValuesShownActionPerformed(evt);
            }
        });

        jLabelValueNumber.setText("x-Values shown");

        jToggleButtonChannelOnOff.setText("All on");
        jToggleButtonChannelOnOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonChannelOnOffActionPerformed(evt);
            }
        });

        EmbeddedLatencyPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        EmbeddedLatencyPanel.setForeground(new java.awt.Color(107, 106, 104));

        jLabelEmbeddedLatency.setBackground(new java.awt.Color(160, 160, 160));
        jLabelEmbeddedLatency.setText("Embedded Latency");
        jLabelEmbeddedLatency.setOpaque(true);

        jTextFieldEmbeddedLatency.setEditable(false);
        jTextFieldEmbeddedLatency.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldEmbeddedLatency.setText("0");
        jTextFieldEmbeddedLatency.setToolTipText("Timedifference between two received EOT Bytes");
        jTextFieldEmbeddedLatency.setFocusable(false);

        maxEmbeddedLatency.setEditable(false);
        maxEmbeddedLatency.setBackground(new java.awt.Color(255, 255, 255));
        maxEmbeddedLatency.setText("0");
        maxEmbeddedLatency.setToolTipText("Max Embedded Latency (might be corrupted due to PC workload) ");
        maxEmbeddedLatency.setFocusable(false);

        javax.swing.GroupLayout EmbeddedLatencyPanelLayout = new javax.swing.GroupLayout(EmbeddedLatencyPanel);
        EmbeddedLatencyPanel.setLayout(EmbeddedLatencyPanelLayout);
        EmbeddedLatencyPanelLayout.setHorizontalGroup(
            EmbeddedLatencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmbeddedLatencyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(EmbeddedLatencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EmbeddedLatencyPanelLayout.createSequentialGroup()
                        .addComponent(jLabelEmbeddedLatency, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(3, 3, 3))
                    .addGroup(EmbeddedLatencyPanelLayout.createSequentialGroup()
                        .addGroup(EmbeddedLatencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(maxEmbeddedLatency, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldEmbeddedLatency, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        EmbeddedLatencyPanelLayout.setVerticalGroup(
            EmbeddedLatencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmbeddedLatencyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelEmbeddedLatency)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldEmbeddedLatency, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(maxEmbeddedLatency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        ToolLatencyPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ToolLatencyPanel.setForeground(new java.awt.Color(107, 106, 104));

        jLabelToolLatency.setBackground(new java.awt.Color(160, 160, 160));
        jLabelToolLatency.setText("Tool Latency");
        jLabelToolLatency.setOpaque(true);

        jTextFieldToolLatency.setEditable(false);
        jTextFieldToolLatency.setBackground(new java.awt.Color(255, 255, 255));
        jTextFieldToolLatency.setText("0");
        jTextFieldToolLatency.setToolTipText("Timedifference between EOT Byte and finish drawing the graph");
        jTextFieldToolLatency.setFocusable(false);

        javax.swing.GroupLayout ToolLatencyPanelLayout = new javax.swing.GroupLayout(ToolLatencyPanel);
        ToolLatencyPanel.setLayout(ToolLatencyPanelLayout);
        ToolLatencyPanelLayout.setHorizontalGroup(
            ToolLatencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ToolLatencyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ToolLatencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ToolLatencyPanelLayout.createSequentialGroup()
                        .addComponent(jLabelToolLatency, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(3, 3, 3))
                    .addGroup(ToolLatencyPanelLayout.createSequentialGroup()
                        .addComponent(jTextFieldToolLatency, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        ToolLatencyPanelLayout.setVerticalGroup(
            ToolLatencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ToolLatencyPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelToolLatency)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldToolLatency, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabelAntiAliasing.setText("Antialiasing");

        jToggleButtonAntiAliasing.setText("disable");
        jToggleButtonAntiAliasing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonAntiAliasingActionPerformed(evt);
            }
        });

        jButtonResetY.setText("Reset_Y");
        jButtonResetY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetYActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ControlButtonsLayout = new javax.swing.GroupLayout(ControlButtons);
        ControlButtons.setLayout(ControlButtonsLayout);
        ControlButtonsLayout.setHorizontalGroup(
            ControlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ControlButtonsLayout.createSequentialGroup()
                .addGroup(ControlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToggleButtonChannelOnOff, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(ControlButtonsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(ControlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ControlButtonsLayout.createSequentialGroup()
                                .addGroup(ControlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelMaxY)
                                    .addComponent(jTextFieldMaxY, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(ControlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBoxValuesShown, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelValueNumber, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ControlButtonsLayout.createSequentialGroup()
                                .addComponent(jButtonSetMinMaxY, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                                .addComponent(jToggleButtonOffsetRaw, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ControlButtonsLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(ControlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelAntiAliasing)
                                    .addComponent(jToggleButtonAntiAliasing)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ControlButtonsLayout.createSequentialGroup()
                                .addComponent(jButtonPlayPause, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addGroup(ControlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(EmbeddedLatencyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(ToolLatencyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(ControlButtonsLayout.createSequentialGroup()
                                .addGroup(ControlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldMinY, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelMinY)
                                    .addComponent(jButtonDisconnect)
                                    .addComponent(jButtonResetY))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        ControlButtonsLayout.setVerticalGroup(
            ControlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ControlButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToggleButtonChannelOnOff, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ControlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelMaxY)
                    .addComponent(jLabelValueNumber))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ControlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldMaxY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxValuesShown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelMinY)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldMinY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ControlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSetMinMaxY)
                    .addComponent(jToggleButtonOffsetRaw))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonResetY)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelAntiAliasing)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButtonAntiAliasing)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ControlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ControlButtonsLayout.createSequentialGroup()
                        .addComponent(jButtonPlayPause, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonDisconnect, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ControlButtonsLayout.createSequentialGroup()
                        .addComponent(ToolLatencyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(EmbeddedLatencyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelControlPanelLayout = new javax.swing.GroupLayout(jPanelControlPanel);
        jPanelControlPanel.setLayout(jPanelControlPanelLayout);
        jPanelControlPanelLayout.setHorizontalGroup(
            jPanelControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ControlButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(channelPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(channelControlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelControlPanelLayout.setVerticalGroup(
            jPanelControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelControlPanelLayout.createSequentialGroup()
                .addComponent(channelPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(channelControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(ControlButtons, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout channelSelectLayout = new javax.swing.GroupLayout(channelSelect);
        channelSelect.setLayout(channelSelectLayout);
        channelSelectLayout.setHorizontalGroup(
            channelSelectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(channelSelectLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        channelSelectLayout.setVerticalGroup(
            channelSelectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(channelSelectLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelControlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );

        mainPanel.add(channelSelect, "channelSelect_CardLayout");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButtonChannelOnOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonChannelOnOffActionPerformed
        if (jToggleButtonChannelOnOff.isSelected()) {
            allChannelOff();
        } else {
            allChannelOn();
        }
    }//GEN-LAST:event_jToggleButtonChannelOnOffActionPerformed

    private void jTextFieldMaxYKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldMaxYKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextFieldMinY.requestFocusInWindow();
        }
    }//GEN-LAST:event_jTextFieldMaxYKeyPressed

    private void jTextFieldMinYKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldMinYKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonSetMinMaxY.doClick();
        }
    }//GEN-LAST:event_jTextFieldMinYKeyPressed

    private void jTextFieldNewChannelNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNewChannelNameKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonSetChannelName.doClick();
        }
    }//GEN-LAST:event_jTextFieldNewChannelNameKeyPressed

    private void jButtonDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisconnectActionPerformed
        disconnect();
    }//GEN-LAST:event_jButtonDisconnectActionPerformed

    private void disconnect() {
        allChannelOff();
        setCardLayout("toolConnect");
//        init();
        this.validate();
        this.repaint();
    }

    private void jButtonPlayPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlayPauseActionPerformed
        togglePause();
    }//GEN-LAST:event_jButtonPlayPauseActionPerformed

    private void ChannelRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChannelRemoveActionPerformed
        setChannelsToInactive();
    }//GEN-LAST:event_ChannelRemoveActionPerformed

    private void ChannelAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChannelAddActionPerformed
        setChannelsToActive();
    }//GEN-LAST:event_ChannelAddActionPerformed

    private void jButtonSetMinMaxYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSetMinMaxYActionPerformed
        try {
            int upper = Integer.valueOf(jTextFieldMaxY.getText());
            int lower = Integer.valueOf(jTextFieldMinY.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_jButtonSetMinMaxYActionPerformed

    private void jButtonResetYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetYActionPerformed

    }//GEN-LAST:event_jButtonResetYActionPerformed

    private void jComboBoxValuesShownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxValuesShownActionPerformed
        String selectedItem = (String) jComboBoxValuesShown.getSelectedItem();
    }//GEN-LAST:event_jComboBoxValuesShownActionPerformed

    private void jButtonSetChannelNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSetChannelNameActionPerformed
        String oldName = (String) jComboBoxOldChannelName.getSelectedItem();
        String newChannelName = jTextFieldNewChannelName.getText();
        if (!channelNameNumberAssignment.containsKey(newChannelName)) {

            Integer index = this.channelNameNumberAssignment.get(oldName);
            this.channelNameNumberAssignment.remove(oldName);
            this.channelNameNumberAssignment.put(newChannelName, index);
            this.reversedHashMap.put(index, newChannelName);

            DefaultListModel activeModel = (DefaultListModel) jListActiveChannels.getModel();
            DefaultListModel inactiveModel = (DefaultListModel) jListInactiveChannels.getModel();
            if (activeModel.contains(oldName)) {
                activeModel.removeElement(oldName);
                activeModel.addElement(newChannelName);
                sortChannelLists();
            } else if (inactiveModel.contains(oldName)) {
                inactiveModel.removeElement(oldName);
                inactiveModel.addElement(newChannelName);
                sortChannelLists();
            } else {
                JOptionPane.showMessageDialog(null, "Name already taken!");
                return;
            }

            allChannelNames.set(allChannelNames.indexOf(oldName), newChannelName);
            allChannelNames.sort(new AlphanumComparator());
            reinitOldChannelName(allChannelNames);

        } else {
            JOptionPane.showMessageDialog(null, "Name already taken!");
        }
    }//GEN-LAST:event_jButtonSetChannelNameActionPerformed

    private void jToggleButtonOffsetRawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonOffsetRawActionPerformed
        if (jToggleButtonOffsetRaw.isSelected()) {

            jToggleButtonOffsetRaw.setText("Show Rawdata");
        } else {

            jToggleButtonOffsetRaw.setText("Set Offset");
        }
    }//GEN-LAST:event_jToggleButtonOffsetRawActionPerformed

    private void jToggleButtonAntiAliasingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonAntiAliasingActionPerformed
        if (jToggleButtonAntiAliasing.isSelected()) {
            jToggleButtonAntiAliasing.setText("enable");
        } else {
            jToggleButtonAntiAliasing.setText("disable");
        }
    }//GEN-LAST:event_jToggleButtonAntiAliasingActionPerformed

    private void disconnectServer_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectServer_ButtonActionPerformed
        socketHandler.disconnect();
        socketHandler = null;
        setCardLayout(serverSelect_CardLayout);
    }//GEN-LAST:event_disconnectServer_ButtonActionPerformed

    private void connectAQ_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectAQ_ButtonActionPerformed
        String selectedHW_Interface = comboBoxPortChooser.getSelectedItem().toString();
        int selectedBaudrate = Integer.valueOf(jTextFieldBaudrate.getText());
        int selectedDataBits = Integer.valueOf(jTextFieldDataBits.getText());
        int selectedStopBits = Integer.valueOf(jTextFieldStopBits.getText());
        int channelCount = Integer.valueOf(jTextFieldNewChannelNumber.getText());

        //Check if join was successful
//        if(joinmessage_received){
        //create Graph and dataevaluator
        graph = new JFreeChart_2DLine_Graph(channelNameNumberAssignment, graphConfig, selectedHW_Interface);
        dataEvaluator = new MWO_DataEvaluator(graph, selectedHW_Interface);
        dataEvaluator.addObserver(this);

        //give framehandler access to dataevaluator
        socketHandler.initGraphComponents(dataEvaluator);
//        frame_handler.set

          //TODO: create reversed-map
//        initChannelLists();

        this.jComboBoxValuesShown.setSelectedItem(Integer.toString(graphConfig.getX_Values_Shown()));
        this.jToggleButtonChannelOnOff.setSelected(true);
        setCardLayout(channelSelect_CardLayout);
        //        }
    }//GEN-LAST:event_connectAQ_ButtonActionPerformed

    private void connectServer_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectServer_ButtonActionPerformed
        String selectedItem = serverSelect_ComboBox.getSelectedItem().toString();

        //Update Config
        try {
            String defaultServer = selectedItem;
            serverConfig.setDefaultServer(defaultServer);
        } catch (NullPointerException e) {
            //nothing was selected yet
        }
        serverConfig.setAutoConnectToServer(autoConnect_Checkbox.isSelected());

        //Connect to Server
        try {
            socketHandler = new SocketHandler(serverConfig.getServerList().get(selectedItem), serverConfig.getPort());

            refreshAndSetToolConnect();

        } catch (IOException ex) {
            Logger.getLogger(QD_GUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Connection to " + selectedItem + " failed!");
        }
    }//GEN-LAST:event_connectServer_ButtonActionPerformed

    /**
     *
     */
    public void initChannelLists() {
        channelNameNumberAssignment = new HashMap<>();
        reversedHashMap = new HashMap<>();

        DefaultListModel listModelActiveChannels = new DefaultListModel();
        jListActiveChannels.setModel(listModelActiveChannels);

        int shownChannelNumber = Integer.valueOf(jTextFieldNewChannelNumber.getText());

        allChannelNames = new ArrayList<>();

        DefaultListModel allChannels = new DefaultListModel();
        for (int i = 0; i < shownChannelNumber; i++) {
            String channelName = "Channel_" + i;
            allChannelNames.add(channelName);
            allChannels.addElement(channelName);
            channelNameNumberAssignment.put(channelName, i);
            reversedHashMap.put(i, channelName);
        }

        allChannelNames.sort(new AlphanumComparator());
        reinitOldChannelName(allChannelNames);

        jListInactiveChannels.setModel(allChannels);

        jScrollPaneActiveChannels.setWheelScrollingEnabled(true);
        jScrollPaneActiveChannels.setPreferredSize(new Dimension(150, 250));
        jScrollPaneInactiveChannels.setWheelScrollingEnabled(true);
        jScrollPaneInactiveChannels.setPreferredSize(new Dimension(150, 250));
        jScrollPaneActiveChannels.validate();
        jScrollPaneInactiveChannels.repaint();
        jScrollPaneActiveChannels.validate();
        jScrollPaneInactiveChannels.repaint();
        this.setSize(this.getPreferredSize());
        channelPanel.validate();
        channelPanel.repaint();
    }

    private void setChannelsToActive() {
        ArrayList<String> selectedValuesList = new ArrayList<>();
        selectedValuesList.addAll(jListInactiveChannels.getSelectedValuesList());
        selectedValuesList.stream().map((listItem) -> {
            DefaultListModel activeModel = (DefaultListModel) jListActiveChannels.getModel();
            activeModel.addElement(listItem);
            return listItem;
        }).map((listItem) -> {
            DefaultListModel inactiveModel = (DefaultListModel) jListInactiveChannels.getModel();
            inactiveModel.removeElement(listItem);
            return listItem;
        }).forEachOrdered((listItem) -> {
        });
        sortChannelLists();
    }

    private void setChannelsToInactive() {
        ArrayList<String> selectedValuesList = new ArrayList<>();
        selectedValuesList.addAll(jListActiveChannels.getSelectedValuesList());

        selectedValuesList.stream().map((listItem) -> {
            DefaultListModel activeModel = (DefaultListModel) jListInactiveChannels.getModel();
            activeModel.addElement(listItem);
            return listItem;
        }).map((listItem) -> {
            DefaultListModel inactiveModel = (DefaultListModel) jListActiveChannels.getModel();
            inactiveModel.removeElement(listItem);
            return listItem;
        }).forEachOrdered((listItem) -> {
        });
        sortChannelLists();
    }

    private void setCardLayout(String cardName) {
        CardLayout card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel, cardName);
        this.validate();
        this.repaint();
    }

    private void togglePause() {
        if (isPaused) {
            isPaused = false;
            setPlayPauseIcon("pause");
        } else {
            isPaused = true;
            setPlayPauseIcon("play");
        }
    }

    /**
     *
     */
    public void klickPausePlay() {
        jButtonPlayPause.doClick();
    }

    /**
     *
     */
    public void klickAllOnOff() {
        jToggleButtonChannelOnOff.doClick();
    }

    /**
     *
     */
    public void allChannelOn() {
        jToggleButtonChannelOnOff.setText("All off");
        jToggleButtonChannelOnOff.setSelected(false);

        int end = jListInactiveChannels.getModel().getSize() - 1;
        if (end >= 0) {
            jListInactiveChannels.setSelectionInterval(0, end);
        }
        ChannelAdd.doClick();
    }

    /**
     *
     */
    public void allChannelOff() {
        jToggleButtonChannelOnOff.setText("All on");
        jToggleButtonChannelOnOff.setSelected(true);

        int end = jListActiveChannels.getModel().getSize() - 1;
        if (end >= 0) {
            jListActiveChannels.setSelectionInterval(0, end);
        }
        ChannelRemove.doClick();
    }

    private void setPlayPauseIcon(String name) {
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/Pictures/" + name + ".png"));
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newimg);
        jButtonPlayPause.setIcon(icon);
    }

    private void refreshAndSetToolConnect() {
        //get HW && config-fields from Server

        //init HW-List
        initHW_Interface_List();

        //set last used HW if possible && create configuration of hw
        setCardLayout(toolConnect_CardLayout);
    }

    private void initHW_Interface_List() {
        //TODO: Temporary, until RST fixes HW-Request/Response
        ArrayList<String> portnameList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            portnameList.add("Com" + (i + 1));
        }

        this.portNames = portnameList.toArray(new String[portnameList.size()]);

        if (portNames.length > 0) {
            comboBoxPortChooser.setModel(new javax.swing.DefaultComboBoxModel<>(portNames));
        } else {
            comboBoxPortChooser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"No active Port found"}));
        }
    }

    /**
     *
     */
    public void sortChannelLists() {
        //sort activeList
        DefaultListModel activeModel = (DefaultListModel) jListActiveChannels.getModel();

        ArrayList<String> tempSortListActiveChannels = new ArrayList<>();
        for (int i = 0; i < activeModel.size(); i++) {
            tempSortListActiveChannels.add((String) activeModel.getElementAt(i));
        }
        tempSortListActiveChannels.sort(new AlphanumComparator());
        DefaultListModel sortedActiveModel = new DefaultListModel();
        tempSortListActiveChannels.forEach((_item) -> {
            sortedActiveModel.addElement(_item);
        });

        jListActiveChannels.setModel(sortedActiveModel);

        //sort inactiveList
        DefaultListModel inactiveModel = (DefaultListModel) jListInactiveChannels.getModel();

        ArrayList<String> tempSortListInactiveChannels = new ArrayList<>();
        for (int i = 0; i < inactiveModel.size(); i++) {
            tempSortListInactiveChannels.add((String) inactiveModel.getElementAt(i));
        }
        tempSortListInactiveChannels.sort(new AlphanumComparator());
        DefaultListModel sortedInactiveModel = new DefaultListModel();
        tempSortListInactiveChannels.forEach((_item) -> {
            sortedInactiveModel.addElement(_item);
        });

        jListInactiveChannels.setModel(sortedInactiveModel);
    }

    /**
     *
     * @param channelNames
     */
    public void reinitOldChannelName(ArrayList<String> channelNames) {
        jComboBoxOldChannelName.removeAllItems();
        channelNames.forEach((item) -> {
            jComboBoxOldChannelName.addItem(item);
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private GUI.JButtonArrow ChannelAdd;
    private GUI.JButtonArrow ChannelRemove;
    private javax.swing.JPanel ControlButtons;
    private javax.swing.JPanel EmbeddedLatencyPanel;
    private javax.swing.JPanel ErrorPanel;
    private javax.swing.JPanel ToolLatencyPanel;
    private javax.swing.JCheckBox autoConnect_Checkbox;
    private javax.swing.JPanel channelControlPanel;
    private javax.swing.JPanel channelPanel;
    private javax.swing.JPanel channelSelect;
    private javax.swing.JComboBox<String> comboBoxPortChooser;
    private javax.swing.JButton connectAQ_Button;
    private javax.swing.JButton connectServer_Button;
    private javax.swing.JButton disconnectServer_Button;
    private javax.swing.JButton jButtonDisconnect;
    private javax.swing.JButton jButtonPlayPause;
    private javax.swing.JButton jButtonResetY;
    private javax.swing.JButton jButtonSetChannelName;
    private javax.swing.JButton jButtonSetMinMaxY;
    private javax.swing.JComboBox<String> jComboBoxOldChannelName;
    private javax.swing.JComboBox<String> jComboBoxValuesShown;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelActiveChannels;
    private javax.swing.JLabel jLabelAntiAliasing;
    private javax.swing.JLabel jLabelBaudrate;
    private javax.swing.JLabel jLabelCommErrors;
    private javax.swing.JLabel jLabelDataBits;
    private javax.swing.JLabel jLabelEmbeddedLatency;
    private javax.swing.JLabel jLabelInactiveChannels;
    private javax.swing.JLabel jLabelMaxY;
    private javax.swing.JLabel jLabelMinY;
    private javax.swing.JLabel jLabelNewChannelName;
    private javax.swing.JLabel jLabelOldChannelName;
    private javax.swing.JLabel jLabelQDIcon;
    private javax.swing.JLabel jLabelQDIcon1;
    private javax.swing.JLabel jLabelStopBits;
    private javax.swing.JLabel jLabelToolLatency;
    private javax.swing.JLabel jLabelValueNumber;
    private javax.swing.JList<String> jListActiveChannels;
    private javax.swing.JList<String> jListInactiveChannels;
    private javax.swing.JPanel jPanelConnectButtonPanel;
    private javax.swing.JPanel jPanelControlPanel;
    private javax.swing.JPanel jPanelLogoPanel;
    private javax.swing.JScrollPane jScrollPaneActiveChannels;
    private javax.swing.JScrollPane jScrollPaneInactiveChannels;
    private javax.swing.JTextField jTextFieldBaudrate;
    private javax.swing.JTextField jTextFieldDataBits;
    private javax.swing.JTextField jTextFieldEmbeddedLatency;
    private javax.swing.JTextField jTextFieldErrorCount;
    private javax.swing.JTextField jTextFieldMaxY;
    private javax.swing.JTextField jTextFieldMinY;
    private javax.swing.JTextField jTextFieldNewChannelName;
    private javax.swing.JTextField jTextFieldNewChannelNumber;
    private javax.swing.JTextField jTextFieldStopBits;
    private javax.swing.JTextField jTextFieldToolLatency;
    private javax.swing.JToggleButton jToggleButtonAntiAliasing;
    private javax.swing.JToggleButton jToggleButtonChannelOnOff;
    private javax.swing.JToggleButton jToggleButtonOffsetRaw;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField maxEmbeddedLatency;
    private javax.swing.JComboBox<String> serverSelect_ComboBox;
    private javax.swing.JPanel serverSelect_Panel;
    private javax.swing.JPanel toolConnect;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(java.util.Observable o, Object arg) {
        this.jTextFieldToolLatency.setText(arg.toString());
    }
}
