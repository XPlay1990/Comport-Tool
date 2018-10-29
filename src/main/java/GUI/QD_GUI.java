/* 
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package GUI;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import Config.Config_JSON;
import Config.Tool_Config.Graph_Config;
import Config.Tool_Config.Server_Config;
import Config.Tool_Config.Tool_Config;
import DataEvaluation.AQ_Response_Evaluator;
import DataEvaluation.DataEvaluator_Abstract;
import DataEvaluation.MWO_DataEvaluator;
import Frame.Frame_Handler;
import Frame.PASSAT_Frame_Parser;
import Frame.generated_PASSAT_DATA_Frame.Configuration;
import Frame.generated_PASSAT_DATA_Frame.Data;
import Frame.generated_PASSAT_DATA_Frame.Hardware;
import Frame.generated_PASSAT_DATA_Frame.Header;
import Frame.generated_PASSAT_DATA_Frame.Ifconfig;
import Frame.generated_PASSAT_DATA_Frame.Passat_Data_Frame;
import Frame.generated_PASSAT_DATA_Frame.Protocol;
import Frame.generated_PASSAT_DATA_Frame.Target;
import Frame.generated_PASSAT_DATA_Frame.TargetElement;
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
import java.util.List;
import java.util.Map;
import java.util.Observer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

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

    ArrayList<Integer> activeChannelList;

    private SocketHandler socketHandler;
    private DataEvaluator_Abstract dataEvaluator;

    private AQ_Response_Evaluator aq_info_dataImplementer;
    DefaultTableModel table_aq_uuid_model;
    //ArrayList<String> ar1;// = new ArrayList();

    private Graph graph;

    private String[] portNames;
    private boolean isPaused = false;
    private HashMap<String, Integer> channelNameToNumberMapping;
    private HashMap<Integer, String> channelNumberToNameMapping;
    ArrayList<String> allChannelNames;
    private Map<String, TargetElement> runningAcquisitionsMap;// = new HashMap<PASSAT_Frame_Parser.headerDestination , String>()

    private final String serverSelect_CardLayout = "serverSelect_CardLayout";
    private final String toolConnect_CardLayout = "toolConnect_CardLayout";
    private final String channelSelect_CardLayout = "channelSelect_CardLayout";

    private static final Logger ERROR_LOGGER = LogManager.getLogger(QD_GUI.class.getName());

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
        if (serverConfig.getAutoConnectToServer()) {
            connectServer_Button.doClick();
        }

        table_aq_uuid_model = new DefaultTableModel();

        String[] columnNames = {"AQ UUID", "Interface", "Protocol", "Created", "AQ Name"};
        Object[][] table_data = {{"Create new acquisition ..."}/*{"UUID", "COM2", "MWO"},*/};
        table_aq_uuid_model = new DefaultTableModel(table_data, columnNames);
        table_AcquisitionsInfo.setDefaultEditor(Object.class, null);
        table_AcquisitionsInfo.setModel(table_aq_uuid_model);
        runningAcquisitionsMap = new HashMap<>();

        this.validate();
        this.repaint();
    }

    private void initConfig() {
        getConfig();

        //use config to initialize program
        initGUI();
    }

    private void getConfig() {
        ERROR_LOGGER.info("Loading config");
        try {
            config = Config_JSON.main(config_FileName);
            ERROR_LOGGER.info("Loading successful");
        } catch (FileNotFoundException ex) {
            config = new Config_JSON();
            ERROR_LOGGER.info("Config did not exist, creating new");
        } catch (JsonSyntaxException ex) {
            JOptionPane.showMessageDialog(this, config_FileName + " corrupted. Creating " + config_Fallback_FileName);
            ERROR_LOGGER.info("Config corrupted, creating new");
            config = new Config_JSON();
            config_FileName = config_Fallback_FileName;
        }
        Tool_Config toolConfig = config.getTool_Config();

        this.serverConfig = toolConfig.getServer_Config();
        this.graphConfig = toolConfig.getGraph_Config();
        this.activeChannelList = graphConfig.getActiveChannelList();
    }

    private void saveConfigOnClose() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveConfigToFile();
                ERROR_LOGGER.info("Closing Program");
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

        //TEST_FRAME_AQJOIN_REQ x = new TEST_FRAME_AQJOIN_REQ();
        //x.toJson();
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
        serverSelect_ComboBox = new javax.swing.JComboBox<String>();
        jLabelQDIcon1 = new javax.swing.JLabel();
        autoConnect_Checkbox = new javax.swing.JCheckBox();
        connectServer_Button = new javax.swing.JButton();
        toolConnect = new javax.swing.JPanel();
        jPanelConnectButtonPanel = new javax.swing.JPanel();
        jTextFieldBaudrate = new javax.swing.JTextField();
        jTextFieldDataBits = new javax.swing.JTextField();
        jTextFieldStopBits = new javax.swing.JTextField();
        jTextFieldAcqName = new javax.swing.JTextField();
        jLabelBaudrate = new javax.swing.JLabel();
        jLabelDataBits = new javax.swing.JLabel();
        jLabelStopBits = new javax.swing.JLabel();
        jLabelAcqName = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_AcquisitionsInfo = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jPanelLogoPanel = new javax.swing.JPanel();
        jLabelQDIcon = new javax.swing.JLabel();
        jTextFieldNewChannelNumber = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        disconnectServer_Button = new javax.swing.JButton();
        comboBoxPortChooser = new javax.swing.JComboBox<String>();
        refresh_AQ_Button = new javax.swing.JButton();
        connectAQ_Button = new javax.swing.JButton();
        get_hardware_Button = new javax.swing.JButton();
        channelSelect = new javax.swing.JPanel();
        jPanelControlPanel = new javax.swing.JPanel();
        channelPanel = new javax.swing.JPanel();
        jScrollPaneInactiveChannels = new javax.swing.JScrollPane();
        jListInactiveChannels = new javax.swing.JList<String>();
        jScrollPaneActiveChannels = new javax.swing.JScrollPane();
        jListActiveChannels = new javax.swing.JList<String>();
        ChannelRemove = new GUI.JButtonArrow();
        ChannelAdd = new GUI.JButtonArrow();
        jLabelInactiveChannels = new javax.swing.JLabel();
        jLabelActiveChannels = new javax.swing.JLabel();
        channelControlPanel = new javax.swing.JPanel();
        jTextFieldNewChannelName = new javax.swing.JTextField();
        jButtonSetChannelName = new javax.swing.JButton();
        jLabelNewChannelName = new javax.swing.JLabel();
        jLabelOldChannelName = new javax.swing.JLabel();
        jComboBoxOldChannelName = new javax.swing.JComboBox<String>();
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
        jComboBoxValuesShown = new javax.swing.JComboBox<String>();
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
        setResizable(false);

        mainPanel.setLayout(new java.awt.CardLayout());

        serverSelect_ComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Server" }));

        jLabelQDIcon1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Pictures/logo.jpg"))); // NOI18N

        autoConnect_Checkbox.setText("AutoConnect");
        autoConnect_Checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoConnect_CheckboxActionPerformed(evt);
            }
        });

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
                        .addComponent(serverSelect_ComboBox, 0, 753, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 704, Short.MAX_VALUE)
                .addComponent(connectServer_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        mainPanel.add(serverSelect_Panel, "serverSelect_CardLayout");

        jTextFieldBaudrate.setText("400000");

        jTextFieldDataBits.setText("8");

        jTextFieldStopBits.setText("1");

        jTextFieldAcqName.setText("new Acqusition");
        jTextFieldAcqName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldAcqNameActionPerformed(evt);
            }
        });

        jLabelBaudrate.setText("Baudrate:");

        jLabelDataBits.setText("DataBits:");

        jLabelStopBits.setText("StopBits:");

        jLabelAcqName.setText("Acqusition name:");

        table_AcquisitionsInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table_AcquisitionsInfo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_AcquisitionsInfoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table_AcquisitionsInfo);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Available acquisitions");

        javax.swing.GroupLayout jPanelConnectButtonPanelLayout = new javax.swing.GroupLayout(jPanelConnectButtonPanel);
        jPanelConnectButtonPanel.setLayout(jPanelConnectButtonPanelLayout);
        jPanelConnectButtonPanelLayout.setHorizontalGroup(
            jPanelConnectButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 729, Short.MAX_VALUE)
            .addGroup(jPanelConnectButtonPanelLayout.createSequentialGroup()
                .addGroup(jPanelConnectButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelConnectButtonPanelLayout.createSequentialGroup()
                        .addGroup(jPanelConnectButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldBaudrate)
                            .addComponent(jLabelBaudrate, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelConnectButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelDataBits, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldDataBits, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelConnectButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelStopBits, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                            .addComponent(jTextFieldStopBits))
                        .addGap(26, 26, 26)
                        .addGroup(jPanelConnectButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldAcqName, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelAcqName, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel2))
                .addContainerGap(223, Short.MAX_VALUE))
        );
        jPanelConnectButtonPanelLayout.setVerticalGroup(
            jPanelConnectButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConnectButtonPanelLayout.createSequentialGroup()
                .addGroup(jPanelConnectButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelBaudrate, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDataBits, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelStopBits, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAcqName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelConnectButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldBaudrate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDataBits, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldStopBits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldAcqName, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 747, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(35, 35, 35)
                .addComponent(jTextFieldNewChannelNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        disconnectServer_Button.setText("Disconnect Server");
        disconnectServer_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectServer_ButtonActionPerformed(evt);
            }
        });

        comboBoxPortChooser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Choose Port" }));

        refresh_AQ_Button.setText("Refresh AQ List");
        refresh_AQ_Button.setMaximumSize(new java.awt.Dimension(99, 23));
        refresh_AQ_Button.setMinimumSize(new java.awt.Dimension(99, 23));
        refresh_AQ_Button.setPreferredSize(new java.awt.Dimension(99, 23));
        refresh_AQ_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_AQ_ButtonActionPerformed(evt);
            }
        });

        connectAQ_Button.setText("Connect AQ");
        connectAQ_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectAQ_ButtonActionPerformed(evt);
            }
        });

        get_hardware_Button.setText("Get Hardware");
        get_hardware_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                get_hardware_ButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout toolConnectLayout = new javax.swing.GroupLayout(toolConnect);
        toolConnect.setLayout(toolConnectLayout);
        toolConnectLayout.setHorizontalGroup(
            toolConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolConnectLayout.createSequentialGroup()
                .addGroup(toolConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(toolConnectLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(toolConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelConnectButtonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboBoxPortChooser, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(12, 12, 12))
                    .addGroup(toolConnectLayout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(refresh_AQ_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(get_hardware_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(connectAQ_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(disconnectServer_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGroup(toolConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(connectAQ_Button, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(toolConnectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(get_hardware_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(refresh_AQ_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(disconnectServer_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanelLogoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        mainPanel.add(toolConnect, "toolConnect_CardLayout");

        jListInactiveChannels.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jListInactiveChannels.setAutoscrolls(false);
        jScrollPaneInactiveChannels.setViewportView(jListInactiveChannels);

        jListActiveChannels.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
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
                        .addGroup(channelControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelOldChannelName, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                            .addComponent(jComboBoxOldChannelName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(channelControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(channelControlPanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabelNewChannelName, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(channelControlPanelLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jTextFieldNewChannelName)
                                .addGap(5, 5, 5))))
                    .addComponent(jButtonSetChannelName, javax.swing.GroupLayout.Alignment.TRAILING))
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

        javax.swing.GroupLayout jPanelControlPanelLayout = new javax.swing.GroupLayout(jPanelControlPanel);
        jPanelControlPanel.setLayout(jPanelControlPanelLayout);
        jPanelControlPanelLayout.setHorizontalGroup(
            jPanelControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelControlPanelLayout.createSequentialGroup()
                .addComponent(channelPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanelControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(channelControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(264, Short.MAX_VALUE))
        );
        jPanelControlPanelLayout.setVerticalGroup(
            jPanelControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(channelPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(channelControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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

        jComboBoxValuesShown.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"50", "100", "250", "500", "1000", "2500", "5000" }));
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
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
                                    .addComponent(ToolLatencyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(ControlButtonsLayout.createSequentialGroup()
                                .addGroup(ControlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldMinY, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelMinY)
                                    .addComponent(jButtonResetY)
                                    .addComponent(jButtonDisconnect))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addContainerGap(453, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout channelSelectLayout = new javax.swing.GroupLayout(channelSelect);
        channelSelect.setLayout(channelSelectLayout);
        channelSelectLayout.setHorizontalGroup(
            channelSelectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(channelSelectLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ControlButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(38, 38, 38)
                .addComponent(jPanelControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        channelSelectLayout.setVerticalGroup(
            channelSelectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(channelSelectLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(channelSelectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(channelSelectLayout.createSequentialGroup()
                        .addComponent(ControlButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(channelSelectLayout.createSequentialGroup()
                        .addComponent(jPanelControlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(5, 5, 5))))
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

    private void jButtonDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisconnectActionPerformed
        disconnectAQ();
    }//GEN-LAST:event_jButtonDisconnectActionPerformed

    private void disconnectAQ() {
        allChannelOff();
        setCardLayout(toolConnect_CardLayout);
//        init();
        graph.dispose();
        graph = null;
        dataEvaluator = null;
        socketHandler.stopInputAndWait();
        socketHandler.initGraphComponents(dataEvaluator, aq_info_dataImplementer);
        socketHandler.startInput();
        this.validate();
        this.repaint();
    }

    private void jButtonPlayPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlayPauseActionPerformed
        togglePause();
    }//GEN-LAST:event_jButtonPlayPauseActionPerformed

    private void jButtonSetMinMaxYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSetMinMaxYActionPerformed
        try {
            int upper = Integer.valueOf(jTextFieldMaxY.getText());
            int lower = Integer.valueOf(jTextFieldMinY.getText());
            graph.setyAxisRange(lower, upper);
            graphConfig.setMax_y(upper);
            graphConfig.setMin_y(lower);
            graphConfig.setAutoRange(false);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }//GEN-LAST:event_jButtonSetMinMaxYActionPerformed

    private void jButtonResetYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetYActionPerformed
        graph.setyAxisAutorange(true);
        graphConfig.setAutoRange(true);
    }//GEN-LAST:event_jButtonResetYActionPerformed

    private void jComboBoxValuesShownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxValuesShownActionPerformed
        String selectedItem = (String) jComboBoxValuesShown.getSelectedItem();
        try {
            Integer valueOf = Integer.valueOf(selectedItem);
            graph.setxAxisRange(valueOf);
            graphConfig.setX_Values_Shown(valueOf);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }//GEN-LAST:event_jComboBoxValuesShownActionPerformed


    private void jToggleButtonOffsetRawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonOffsetRawActionPerformed
        boolean offsetState;
        if (jToggleButtonOffsetRaw.isSelected()) {
            jToggleButtonOffsetRaw.setText("Show Rawdata");
            offsetState = true;
        } else {
            jToggleButtonOffsetRaw.setText("Set Offset");
            offsetState = false;
        }
        socketHandler.stopInputAndWait();
        graph.setOffset(offsetState);
        socketHandler.startInput();
    }//GEN-LAST:event_jToggleButtonOffsetRawActionPerformed

    private void jToggleButtonAntiAliasingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonAntiAliasingActionPerformed
        if (jToggleButtonAntiAliasing.isSelected()) {
            jToggleButtonAntiAliasing.setText("enable");
            graphConfig.setAntiAliasing(false);
            graph.setAntiAlias(false);
        } else {
            jToggleButtonAntiAliasing.setText("disable");
            graphConfig.setAntiAliasing(true);
            graph.setAntiAlias(true);
        }
    }//GEN-LAST:event_jToggleButtonAntiAliasingActionPerformed

    private void connectServer_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectServer_ButtonActionPerformed
        String selectedItem = serverSelect_ComboBox.getSelectedItem().toString();
        boolean autoConnect = autoConnect_Checkbox.isSelected();

        //Update Config
        try {
            String defaultServer = selectedItem;
            serverConfig.setDefaultServer(defaultServer);
        } catch (NullPointerException e) {
            //nothing was selected yet
        }
        serverConfig.setAutoConnectToServer(autoConnect);

        //Connect to Server
        ERROR_LOGGER.info("Selected-Server: " + selectedItem + ", " + "AutoConnect: " + autoConnect);
        ERROR_LOGGER.info("Connecting to Server...");
        try {
            socketHandler = new SocketHandler(serverConfig.getServerList().get(selectedItem), serverConfig.getPort());
            refreshAndSetToolConnect();

            ERROR_LOGGER.info("Connected to Server");
        } catch (IOException ex) {
            ERROR_LOGGER.error("Server connect failed");
            JOptionPane.showMessageDialog(this, "Connection to " + selectedItem + " failed!");
        }
    }//GEN-LAST:event_connectServer_ButtonActionPerformed

    private void autoConnect_CheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoConnect_CheckboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_autoConnect_CheckboxActionPerformed

    private void get_hardware_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_get_hardware_ButtonActionPerformed
        // TODO add your handling code here:
        get_available_Hardware();
    }//GEN-LAST:event_get_hardware_ButtonActionPerformed

    private void connectAQ_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectAQ_ButtonActionPerformed

        //Check if join was successful
        //        if(joinmessage_received){
        //init GUI
        String aq_uuid = table_aq_uuid_model.getValueAt(
                table_AcquisitionsInfo.getSelectedRow(), 0
        ).toString();
        connectAcquisition(aq_uuid);

        /*
            String selectedHW_Interface = comboBoxPortChooser.getSelectedItem().toString();
            int selectedBaudrate = Integer.valueOf(jTextFieldBaudrate.getText());
            int selectedDataBits = Integer.valueOf(jTextFieldDataBits.getText());
            int selectedStopBits = Integer.valueOf(jTextFieldStopBits.getText());
            int channelCount = Integer.valueOf(jTextFieldNewChannelNumber.getText());
            initChannelMapping();
            initChannelLists();
            //init Graph Components
            initGraphComponents(selectedHW_Interface);
         */
    }//GEN-LAST:event_connectAQ_ButtonActionPerformed

    private void refresh_AQ_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_AQ_ButtonActionPerformed
        // TODO add your handling code here:

        refreshAcquisitionsList();
    }//GEN-LAST:event_refresh_AQ_ButtonActionPerformed

    private void disconnectServer_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectServer_ButtonActionPerformed
        socketHandler.disconnect();
        socketHandler = null;
        setCardLayout(serverSelect_CardLayout);
    }//GEN-LAST:event_disconnectServer_ButtonActionPerformed

    private void table_AcquisitionsInfoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_AcquisitionsInfoMouseClicked

        if (evt.getClickCount() == 2) {
            String aq_uuid = table_aq_uuid_model.getValueAt(
                    table_AcquisitionsInfo.getSelectedRow(), 0
            ).toString();
            if (!aq_uuid.startsWith("Create")) {
                connectAcquisition(aq_uuid);
            } else {
                System.out.println("table_AcquisitionsInfoMouseClicked... create new AQ JOIN Request");

                Passat_Data_Frame aq_join_frame = new Passat_Data_Frame();
                Header header = new Header();
                Data data = new Data();
                List<Target> targets = new ArrayList<>();
                Target target = new Target();
                TargetElement targetElement = new TargetElement();
                Hardware hardware = new Hardware();
                Configuration configuration = new Configuration();
                Ifconfig ifconfig = new Ifconfig();
                Protocol protocol = new Protocol();

                // config for serial port
                ifconfig.setBitrate(Integer.parseInt(jTextFieldBaudrate.getText().trim()));
                ifconfig.setDataBits(Integer.parseInt(jTextFieldDataBits.getText().trim()));
                ifconfig.setStopBits(Integer.parseInt(jTextFieldStopBits.getText().trim()));
                ifconfig.setParityBits("n");
                ifconfig.setPortname(comboBoxPortChooser.getSelectedItem().toString().trim());
                //
                protocol.setProtId(1);
                protocol.setProtName("mwo");
                protocol.setProtVersion("1");

                hardware.setIftype("serial");
                hardware.setIfconfig(ifconfig);
                configuration.setHardware(hardware);
                configuration.setProtocol(protocol);
                // header config
                header.setSource("client");
                header.setTarget("acquisition");
                header.setCmdclass("control");
                header.setCommand("join");
                header.setType("request");

                targetElement.setConfiguration(configuration);
                targetElement.setCreated("");
                targetElement.setTargetName(jTextFieldAcqName.getText().trim());//("my first acquisition");
                targetElement.setTargetUuid("");
                target.setTargetElement(targetElement);
                targets.add(target);
                data.setTargets(targets);

                aq_join_frame.setHeader(header);
                aq_join_frame.setData(data);
                socketHandler.stopInputAndWait();
                socketHandler.writeToSocket(aq_join_frame.toJson_string());
                socketHandler.startInput();
                //System.out.println(aq_join_frame.toJson_string());
            }
        }
    }//GEN-LAST:event_table_AcquisitionsInfoMouseClicked

    private void jButtonSetChannelNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSetChannelNameActionPerformed
        renameChannel();
    }//GEN-LAST:event_jButtonSetChannelNameActionPerformed

    private void jTextFieldNewChannelNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNewChannelNameKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButtonSetChannelName.doClick();
        }
    }//GEN-LAST:event_jTextFieldNewChannelNameKeyPressed

    private void ChannelAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChannelAddActionPerformed
        setChannelsToActive();
    }//GEN-LAST:event_ChannelAddActionPerformed

    private void ChannelRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChannelRemoveActionPerformed
        setChannelsToInactive();
    }//GEN-LAST:event_ChannelRemoveActionPerformed

    private void jTextFieldAcqNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldAcqNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldAcqNameActionPerformed

    private void get_available_Hardware() {
        socketHandler.stopInputAndWait();
        if (aq_info_dataImplementer == null) {
            aq_info_dataImplementer = new AQ_Response_Evaluator();
            aq_info_dataImplementer.addObserver(this);
            //give aq_info_dataImplementer access to dataevaluator
            socketHandler.initGraphComponents(aq_info_dataImplementer);
        }
        Passat_Data_Frame passat_Frame = new Passat_Data_Frame();
        TargetElement te = new TargetElement();
        List<Target> targets = new ArrayList<Target>();
        Target target = new Target();
        Data data = new Data();
        Configuration configuration = new Configuration();
        Header header = new Header();
        header.setTarget(PASSAT_Frame_Parser.headerDestinationMap.get(PASSAT_Frame_Parser.headerDestination.hardware_manager));
        header.setSource(PASSAT_Frame_Parser.headerDestinationMap.get(PASSAT_Frame_Parser.headerDestination.client));
        header.setCommand(PASSAT_Frame_Parser.headerCommandMap.get(PASSAT_Frame_Parser.headerCommand.info));
        header.setCmdclass(PASSAT_Frame_Parser.headerCmdClassMap.get(PASSAT_Frame_Parser.headerCmdClass.employ));
        header.setType(PASSAT_Frame_Parser.headerTypeMap.get(PASSAT_Frame_Parser.headerType.request));
        passat_Frame.setHeader(header);

        te.setTargetName("");
        te.setTargetUuid("");
        te.setCreated("");
        te.setConfiguration(configuration);
        target.setTargetElement(te);
        targets.add(target);
        data.setTargets(targets);
        passat_Frame.setData(data);
        socketHandler.startInput();
        socketHandler.writeToSocket(passat_Frame.toJson_string());
    }

    private void start_AcquisitionGUI() {
        String selectedHW_Interface = comboBoxPortChooser.getSelectedItem().toString();
        initChannelMapping();
        initChannelLists();
        //init Graph Components
        initGraphComponents(selectedHW_Interface);
    }

    private void connectAcquisition(String aq_uuid) {
        String join_aq1 = "{ \"header\": { \"source\": \"client\", \"target\": \"acquisition\", \"type\": \"request\", \"cmdclass\": \"control\", \"command\": \"join\" }, \"data\": { \"targets\": [ { \"target_element\": { \"target_name\": \"\", \"target_uuid\": \"9ba57abb-d66b-4f48-8c93-a8785483720e\", \"created\": \"YYYY-MM-DDTHH:mm:ss.sssZ\", \"configuration\": { \"hardware\": { \"iftype\": \"serial\", \"ifconfig\": { \"portname\": \"ttyUSB0\", \"bitrate\": 11500, \"data_bits\": 8, \"stop_bits\": 1, \"parity_bits\": \"n\" } }, \"protocol\": { \"prot_name\": \"mwo_1\", \"prot_id\": 1, \"prot_version\": \"1.2\" } } } } ] } }";
        //socketHandler.writeToSocket(join_aq1);
        System.out.println("##################### connectAcquisition " + aq_uuid + " #####################");
        Passat_Data_Frame passat_Frame = new Passat_Data_Frame();
        TargetElement te = runningAcquisitionsMap.get(aq_uuid);
        List<Target> targets = new ArrayList<Target>();
        Target target = new Target();
        Data data = new Data();
        Header header = new Header();
        header.setTarget(PASSAT_Frame_Parser.headerDestinationMap.get(PASSAT_Frame_Parser.headerDestination.acquisition));
        header.setSource(PASSAT_Frame_Parser.headerDestinationMap.get(PASSAT_Frame_Parser.headerDestination.client));
        header.setCommand(PASSAT_Frame_Parser.headerCommandMap.get(PASSAT_Frame_Parser.headerCommand.join));
        header.setCmdclass(PASSAT_Frame_Parser.headerCmdClassMap.get(PASSAT_Frame_Parser.headerCmdClass.control));
        header.setType(PASSAT_Frame_Parser.headerTypeMap.get(PASSAT_Frame_Parser.headerType.request));
        passat_Frame.setHeader(header);
        if (te != null) {
            target.setTargetElement(te);
            targets.add(target);
            data.setTargets(targets);
            passat_Frame.setData(data);
            socketHandler.writeToSocket(passat_Frame.toJson_string());
        } else {
            te = new TargetElement();
            Configuration configuration = new Configuration();
            Protocol protocol = new Protocol();
            target.setTargetElement(te);
        }

        //socketHandler.writeToSocket(join_aq1);
        //runn
        //passat_Frame.setData(data);
    }

    private void refreshAcquisitionsList() {
        socketHandler.stopInputAndWait();
        if (aq_info_dataImplementer == null) {
            aq_info_dataImplementer = new AQ_Response_Evaluator();
            aq_info_dataImplementer.addObserver(this);
            //give aq_info_dataImplementer access to dataevaluator
            socketHandler.initGraphComponents(aq_info_dataImplementer);
        }

        String aq_info = "{\"header\": { \"source\": \"client\", \"target\": \"acquisition\", \"type\": \"request\", \"cmdclass\": \"employ\", \"command\": \"info\" },\"data\": {\"targets\": [    {\"target_element\": {\"target_name\": \"name that Aquisition should have after creation, if name is empty, the name will be generated by aquisition automaticaly\",\"target_uuid\": \"\",\"created\": \"YYYY-MM-DDTHH:mm:ss.sssZ\"}}]}}";

        socketHandler.startInput();
        socketHandler.writeToSocket(aq_info);
    }

    /**
     *
     */
    private void initChannelLists() {
        DefaultListModel listModelActiveChannels = new DefaultListModel();
        jListActiveChannels.setModel(listModelActiveChannels);

        allChannelNames = new ArrayList<>();

        DefaultListModel activeChannels = new DefaultListModel();
        DefaultListModel inactiveChannels = new DefaultListModel();

        channelNumberToNameMapping.entrySet().stream().map((entity) -> {
            if (activeChannelList.contains(entity.getKey())) {
                activeChannels.addElement(entity.getValue());
            } else {
                inactiveChannels.addElement(entity.getValue());
            }
            return entity;
        }).forEachOrdered((entity) -> {
            allChannelNames.add(entity.getValue());
        });

        jListActiveChannels.setModel(activeChannels);
        jListInactiveChannels.setModel(inactiveChannels);

        reinitOldChannelName(allChannelNames);

        jScrollPaneActiveChannels.setWheelScrollingEnabled(true);
        jScrollPaneActiveChannels.setPreferredSize(new Dimension(150, 250));
        jScrollPaneInactiveChannels.setWheelScrollingEnabled(true);
        jScrollPaneInactiveChannels.setPreferredSize(new Dimension(150, 250));
        jScrollPaneActiveChannels.validate();
        jScrollPaneInactiveChannels.repaint();
        jScrollPaneActiveChannels.validate();
        jScrollPaneInactiveChannels.repaint();
        this.pack();
        channelPanel.validate();
        channelPanel.repaint();
    }

    private void initGraphComponents(String selectedHW_Interface) {
        socketHandler.stopInputAndWait();

        graph = new JFreeChart_2DLine_Graph(graphConfig, selectedHW_Interface);

        if (dataEvaluator == null) {
            dataEvaluator = new MWO_DataEvaluator(graph, selectedHW_Interface);
            dataEvaluator.addObserver(this);
        }

        /*
        if(aq_info_dataImplementer == null) {
            aq_info_dataImplementer = new AQ_Response_Evaluator();
            aq_info_dataImplementer.addObserver(this);
            
            System.out.println("QD_GUI.java:" + "initGraphComponents");
        }
         */
        //give framehandler access to dataevaluator
        socketHandler.initGraphComponents(dataEvaluator);

        this.jComboBoxValuesShown.setSelectedItem(Integer.toString(graphConfig.getX_Values_Shown()));
        this.jToggleButtonChannelOnOff.setSelected(true);
        setCardLayout(channelSelect_CardLayout);

        socketHandler.startInput();
    }

    private void initChannelMapping() {
        channelNumberToNameMapping = graphConfig.getChannelNumberToNameMapping();
        channelNameToNumberMapping = graphConfig.getChannelNameToNumberMapping();
    }

    private void setChannelsToActive() {
        socketHandler.stopInputAndWait();
        ArrayList<String> channelsToActive = new ArrayList<>();
        channelsToActive.addAll(jListInactiveChannels.getSelectedValuesList());

        DefaultListModel inactiveModel = (DefaultListModel) jListInactiveChannels.getModel();
        DefaultListModel activeModel = (DefaultListModel) jListActiveChannels.getModel();
        channelsToActive.stream().map((channelName) -> {
            inactiveModel.removeElement(channelName);
            return channelName;
        }).forEachOrdered((channelName) -> {
            activeModel.addElement(channelName);
        });

        //update Graph
        graph.addChannels(channelsToActive);

        //update Config
        channelsToActive.forEach((channelName) -> {
            activeChannelList.add(channelNameToNumberMapping.get(channelName));
        });

        sortChannelLists();
        socketHandler.startInput();
    }

    private void setChannelsToInactive() {
        socketHandler.stopInputAndWait();
        ArrayList<String> channelsToInactive = new ArrayList<>();
        channelsToInactive.addAll(jListActiveChannels.getSelectedValuesList());

        DefaultListModel inactiveModel = (DefaultListModel) jListInactiveChannels.getModel();
        DefaultListModel activeModel = (DefaultListModel) jListActiveChannels.getModel();
        channelsToInactive.stream().map((channelName) -> {
            inactiveModel.addElement(channelName);
            return channelName;
        }).forEachOrdered((channelName) -> {
            activeModel.removeElement(channelName);
        });

        //update Graph
        graph.removeChannels(channelsToInactive);

        //update Config
        channelsToInactive.forEach((channelName) -> {
            activeChannelList.remove(channelNameToNumberMapping.get(channelName));
        });

        sortChannelLists();
        socketHandler.startInput();
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
            handle_Graph_Pause();
        } else {
            isPaused = true;
            setPlayPauseIcon("play");
            handle_Graph_Pause();
        }
    }

    private void handle_Graph_Pause() {
        socketHandler.stopInputAndWait();
        graph.setPause(isPaused);
        socketHandler.startInput();
    }

    private void renameChannel() {
        String oldName = (String) jComboBoxOldChannelName.getSelectedItem();
        String newChannelName = jTextFieldNewChannelName.getText();
        if (!channelNameToNumberMapping.containsKey(newChannelName)) {

            Integer index = this.channelNameToNumberMapping.get(oldName);
            this.channelNameToNumberMapping.remove(oldName);
            this.channelNameToNumberMapping.put(newChannelName, index);
            this.channelNumberToNameMapping.put(index, newChannelName);

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

            graph.changeSeriesName(oldName, newChannelName);
        } else {
            JOptionPane.showMessageDialog(null, "Name already taken!");
        }
    }

    /**
     * Method for Hotkey
     */
    public void klickPausePlay() {
        jButtonPlayPause.doClick();
    }

    /**
     * Method for Hotkey
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
        //initHW_Interface_List();
//        refreshAcquisitionsList();
//        get_available_Hardware();
        //set last used HW if possible && create configuration of hw
        setCardLayout(toolConnect_CardLayout);
        start_AcquisitionGUI();
    }

    private void refreshAQ_List(List<Target> target) {
        //DefaultListModel<String> model = new DefaultListModel<String>();
        runningAcquisitionsMap.clear();

        for (int i = table_aq_uuid_model.getRowCount() - 1; i > 0; i--) {
            table_aq_uuid_model.removeRow(i);
        }
        target.forEach((te) -> {
            //System.out.println("AQ_Response_Evaluator: " + te.getTargetElement().getTargetUuid() );
            runningAcquisitionsMap.put(te.getTargetElement().getTargetUuid(), te.getTargetElement());
            //System.out.println("getTargetUuid(): " + te.getTargetElement().getTargetUuid() );
            /**/
            table_aq_uuid_model.addRow(
                    new Object[]{
                        te.getTargetElement().getTargetUuid(),
                        te.getTargetElement().getConfiguration().getHardware().getIfconfig().getPortname(),
                        te.getTargetElement().getConfiguration().getProtocol().getProtName(),
                        te.getTargetElement().getCreated(),
                        te.getTargetElement().getTargetName()
                    });

        });

        table_AcquisitionsInfo.setModel(table_aq_uuid_model);
    }

    private void initHW_Interface_List_obsolete() {
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

    private void initHW_Interface_List(List<Target> targets) {
        //TODO: Temporary, until RST fixes HW-Request/Response
        ArrayList<String> portnameList = new ArrayList<>();
        targets.forEach((var) -> {
            //System.out.println(var.getTargetElement().getConfiguration().getHardware().getIftype());
            if (var.getTargetElement().getConfiguration().getHardware().getIftype().equals("serial")) {
                portnameList.add(var.getTargetElement().getConfiguration().getHardware().getIfconfig().getPortname());
            } else if (var.getTargetElement().getConfiguration().getHardware().getIftype().equals("ethernet")) {
                portnameList.add(var.getTargetElement().getConfiguration().getHardware().getIfconfig().getPortname());
            }
        });

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
    private javax.swing.JButton get_hardware_Button;
    private javax.swing.JButton jButtonDisconnect;
    private javax.swing.JButton jButtonPlayPause;
    private javax.swing.JButton jButtonResetY;
    private javax.swing.JButton jButtonSetChannelName;
    private javax.swing.JButton jButtonSetMinMaxY;
    private javax.swing.JComboBox<String> jComboBoxOldChannelName;
    private javax.swing.JComboBox<String> jComboBoxValuesShown;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelAcqName;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPaneActiveChannels;
    private javax.swing.JScrollPane jScrollPaneInactiveChannels;
    private javax.swing.JTextField jTextFieldAcqName;
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
    private javax.swing.JButton refresh_AQ_Button;
    private javax.swing.JComboBox<String> serverSelect_ComboBox;
    private javax.swing.JPanel serverSelect_Panel;
    private javax.swing.JTable table_AcquisitionsInfo;
    private javax.swing.JPanel toolConnect;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(java.util.Observable o, Object arg) {
        this.jTextFieldToolLatency.setText(arg.toString());

        if (o instanceof AQ_Response_Evaluator) {
            Frame_Handler fh = new Frame_Handler();
            Passat_Data_Frame passat_Frame = (Passat_Data_Frame) arg;
            PASSAT_Frame_Parser.frameVariant _frameVariant = fh.getFrameVariant(passat_Frame.getHeader());

            switch (_frameVariant) {
                case aq_join_res:
                    System.out.println("QD_GUI " + "aq_join_res frame received.");
//                    System.out.println(passat_Frame.toJson_string());
                    refreshAcquisitionsList();
                    String selectedHW_Interface = comboBoxPortChooser.getSelectedItem().toString();
                    initChannelMapping();
                    initChannelLists();
                    //init Graph Components
                    initGraphComponents(selectedHW_Interface);
                    break;
                case aq_info_res:
                    System.out.println("QD_GUI " + "aq_info_res frame received.");
                    refreshAQ_List(passat_Frame.getData().getTargets());
                    break;
                case aq_exit_res:
                    break;
                case hw_info_res:
                    System.out.println("QD_GUI " + "hw_info_res frame received.");
                    initHW_Interface_List(passat_Frame.getData().getTargets());
                    break;
                default:
                    break;
            }
        }
        /*
        if (o instanceof AQ_Response_Evaluator) {
            Data data = (Data) arg;
            
            System.out.println("update from AQ_Response_Evaluator: " + arg.toString());
            data.getTargets().forEach((te) -> {
                System.out.println("AQ_Response_Evaluator: " + te.getTargetElement().getTargetUuid() );
            });
            
            
            refreshAQ_List(data.getTargets());
        }
         */
    }
}
