/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mixregui;

import def_lib.DefinitionHelper;
import java.awt.Desktop;
import java.net.URL;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import def_lib.DefinitionHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * d
 *
 * @author adityaponnada
 */
public class mixregGUI extends javax.swing.JFrame {

    NewModel newModel;
    //InstructionsGUI instructions;
    advancedOptions advancedOptions_view;
    stageOneRegs stage_1_regs;
    stageTwoRegs stage_2_regs;
    def_lib.SuperUserMenu superUserMenuLaunch;

    // i represents number of random location effects selected in new model
    int i;

    int levelOneRegSize, levelTwoRegSize, stageTwoRegSize, levelOneDisaggSize;

    String[] variableNamesCombo;

    DefaultComboBoxModel<String> IDList;

    DefaultComboBoxModel<String> StageOneList;

    DefaultComboBoxModel<String> StageTwoList;

    DefaultListModel<String> savedVariablesStageOne;

    DefaultComboBoxModel<String> regressorsLevelOne;

    DefaultComboBoxModel<String> regressorsLevelTwo;

    ArrayList<ArrayList<JCheckBox>> levelOneBoxes;

    ArrayList<ArrayList<JCheckBox>> levelTwoBoxes;

    ArrayList<ArrayList<JCheckBox>> stageTwoBoxes;

    ArrayList<ArrayList<JCheckBox>> stageTwoGridBoxes;

    ArrayList<ArrayList<JCheckBox>> disaggVarianceBoxes;
    
    boolean scaleChecked = false;
    boolean randomChecked = false;

    boolean suppressed = false;
    boolean outcomeNone = false;

    ArrayList<String> levelOneSelected;
    ArrayList<String> levelTwoSelected;
    ArrayList<String> stageTwoSelected;
    ArrayList<String> stageTwoSelected_tab2;

    final ImageIcon icon;

    static ActionListener actionListener;

    int stageOneClicked = 0;
    
    JFileChooser fileChooser = new JFileChooser();
    int selectedModel;
    String defFilePath;

    /**
     * Creates new form mixregGUI
     */
    public mixregGUI() {
        initComponents();
        newModel = new NewModel();
        //instructions = new InstructionsGUI();
        variableNamesCombo = newModel.getVariableNames();
        outcomeNone = newModel.getNoneVar();

        IDList = new DefaultComboBoxModel<String>();
        StageOneList = new DefaultComboBoxModel<String>();
        StageTwoList = new DefaultComboBoxModel<String>();
        NoAssociationRadio.setSelected(true);

        i = newModel.getRLE();
        System.out.println(String.valueOf(i));

        //Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/mixLogo.png"));
        //setIconImage(image);
        icon = new ImageIcon(getClass().getResource("/resources/mixLogo.png"));

        if (i > 1) {

            NoAssociationRadio.setText("Yes");

            LinearAssociationRadio.setText("No");
            QuadraticAssociationRadio.setVisible(false);

            associationLabel.setText("Association of random location & scale?");

            // if random location effects are more than one, change the table column names
            level1_BSVar.setText("Loc. eff.");
            level1_WSVar.setText("Scale");
            level2_BSVar.setText("Loc. eff.");
            level2_WSVar.setText("Scale");

        }

        levelOnePanel.setLayout(new BorderLayout());
        levelTwoPanel.setLayout(new BorderLayout());

        stageTwoPanel.setLayout(new BorderLayout());
        
        System.out.println("Right before");
        if (outcomeNone == true){
            System.out.println("In isOutcomeNone MixReg");
            startStageTwo.setText("Run Stage 1");
            System.out.println(startStageTwo.getText());
            //stageOneTabs.set
            stageOneTabs.setEnabledAt(1, false);
        }
        System.out.println("Right after");

        //to enable stage two regressor buttos. avoids accidental clicks
        if (stageOneClicked == 0) {

            addStageTwoTabTwo.setEnabled(false);

        } else {

            addStageTwoTabTwo.setEnabled(true);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jSeparator5 = new javax.swing.JSeparator();
        parentPanel = new javax.swing.JPanel();
        imageView = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        stageOneTabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        resetButton = new javax.swing.JButton();
        goBackMxrButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        level1_MeanReg = new javax.swing.JLabel();
        level1_WSVar = new javax.swing.JLabel();
        level2_MeanReg = new javax.swing.JLabel();
        level2_BSVar = new javax.swing.JLabel();
        level2_WSVar = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        level1_BSVar = new javax.swing.JLabel();
        levelOnePanel = new javax.swing.JPanel();
        levelOneGrid = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        levelTwoPanel = new javax.swing.JPanel();
        levelTwoGrid = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        StageOneVariableCombo = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        IDvariableCombo = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        addStageOneButton = new javax.swing.JButton();
        advancedOptionsButton = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        associationPanel = new javax.swing.JPanel();
        associationLabel = new javax.swing.JLabel();
        NoAssociationRadio = new javax.swing.JRadioButton();
        LinearAssociationRadio = new javax.swing.JRadioButton();
        QuadraticAssociationRadio = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        startStageTwo = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        addStageTwoTabTwo = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        stageTwoPanel = new javax.swing.JPanel();
        stageTwoRegsGrid = new javax.swing.JPanel();
        runTabTwoStageOneTwo = new javax.swing.JButton();
        suppressIntCheckBox = new javax.swing.JCheckBox();
        stageTwoOutcome = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 2), new java.awt.Dimension(0, 2), new java.awt.Dimension(32767, 2));
        jSeparator14 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        stageOneOutput = new javax.swing.JTextArea();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        stageTwoOutput = new javax.swing.JTextArea();
        saveStage2OutButton = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newModelMenu = new javax.swing.JMenuItem();
        modifyStageOneMenu = new javax.swing.JMenuItem();
        modifyStageTwoMenu = new javax.swing.JMenuItem();
        superUserMenu = new javax.swing.JMenuItem();
        exitMenu = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        diagramMenu = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mix Suite");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        parentPanel.setLayout(new java.awt.CardLayout());

        imageView.setBackground(new java.awt.Color(255, 255, 255));
        imageView.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/mixWild.png"))); // NOI18N
        imageView.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 180, 680, 400));

        parentPanel.add(imageView, "card3");

        stageOneTabs.setFocusTraversalKeysEnabled(false);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        jPanel1.add(resetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 510, 135, 40));

        goBackMxrButton.setText("Go Back");
        goBackMxrButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goBackMxrButtonActionPerformed(evt);
            }
        });
        jPanel1.add(goBackMxrButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 510, 147, 40));

        jLabel4.setText("Stage 1 Regressors");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 10, -1, -1));

        level1_MeanReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        level1_MeanReg.setText("Mean");
        level1_MeanReg.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(level1_MeanReg, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 60, -1, -1));
        level1_MeanReg.getAccessibleContext().setAccessibleName("");

        level1_WSVar.setText("Random Scale");
        jPanel1.add(level1_WSVar, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 60, -1, -1));

        level2_MeanReg.setText("Mean");
        jPanel1.add(level2_MeanReg, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 60, -1, -1));

        level2_BSVar.setText("Random Location");
        jPanel1.add(level2_BSVar, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 60, -1, -1));

        level2_WSVar.setText("Random Scale");
        jPanel1.add(level2_WSVar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 60, -1, -1));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 560, 1190, 20));

        level1_BSVar.setText("Random Intercept");
        jPanel1.add(level1_BSVar, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 60, -1, -1));

        levelOnePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Level-1"));

        levelOneGrid.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout levelOnePanelLayout = new javax.swing.GroupLayout(levelOnePanel);
        levelOnePanel.setLayout(levelOnePanelLayout);
        levelOnePanelLayout.setHorizontalGroup(
            levelOnePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(levelOneGrid, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
        );
        levelOnePanelLayout.setVerticalGroup(
            levelOnePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(levelOneGrid, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
        );

        levelOneGrid.getAccessibleContext().setAccessibleName("Level-1");

        jPanel1.add(levelOnePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, 440, 430));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 30, 540, -1));
        jPanel1.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 50, -1, 140));

        levelTwoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Level-2"));

        levelTwoGrid.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout levelTwoPanelLayout = new javax.swing.GroupLayout(levelTwoPanel);
        levelTwoPanel.setLayout(levelTwoPanelLayout);
        levelTwoPanelLayout.setHorizontalGroup(
            levelTwoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(levelTwoGrid, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
        );
        levelTwoPanelLayout.setVerticalGroup(
            levelTwoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(levelTwoGrid, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
        );

        jPanel1.add(levelTwoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 70, 420, 430));

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        StageOneVariableCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        StageOneVariableCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StageOneVariableComboActionPerformed(evt);
            }
        });
        jPanel8.add(StageOneVariableCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 230, -1));

        jLabel2.setText("Stage 1 Outcome:");
        jPanel8.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, -1, -1));

        IDvariableCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        IDvariableCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDvariableComboActionPerformed(evt);
            }
        });
        jPanel8.add(IDvariableCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 230, -1));

        jLabel1.setText("ID Variable:");
        jPanel8.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, -1, -1));
        jPanel8.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 270, -1));
        jPanel8.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 270, -1));

        addStageOneButton.setText("Add Stage 1 Regressors ...");
        addStageOneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStageOneButtonActionPerformed(evt);
            }
        });
        jPanel8.add(addStageOneButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 225, 35));

        advancedOptionsButton.setText("Options ...");
        advancedOptionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advancedOptionsButtonActionPerformed(evt);
            }
        });
        jPanel8.add(advancedOptionsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 225, 37));
        jPanel8.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 280, -1));

        associationPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        associationLabel.setText("<html>Specify the relationship between the <br>random location and within subject variance.<br>(i.e. relation between variance and slope) </html>");
        associationPanel.add(associationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 290, 70));

        buttonGroup1.add(NoAssociationRadio);
        NoAssociationRadio.setText("No Association");
        NoAssociationRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoAssociationRadioActionPerformed(evt);
            }
        });
        associationPanel.add(NoAssociationRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        buttonGroup1.add(LinearAssociationRadio);
        LinearAssociationRadio.setText("Linear Association");
        LinearAssociationRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LinearAssociationRadioActionPerformed(evt);
            }
        });
        associationPanel.add(LinearAssociationRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        buttonGroup1.add(QuadraticAssociationRadio);
        QuadraticAssociationRadio.setText("Quadratic Association");
        associationPanel.add(QuadraticAssociationRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        jPanel8.add(associationPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 290, 180));

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 310, 480));
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 530, -1, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/mixLogo.png"))); // NOI18N
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 510, -1, 30));

        startStageTwo.setText("Modify Stage 2");
        startStageTwo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startStageTwoActionPerformed(evt);
            }
        });
        jPanel1.add(startStageTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(1063, 510, 140, 40));

        stageOneTabs.addTab("Stage 1 Configuration", jPanel1);

        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setText(" Main Effects");
        jPanel12.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 60, -1, -1));

        jLabel12.setText("Stage 2 Interactions");
        jPanel12.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 20, -1, 20));

        jLabel15.setText("Scale");
        jPanel12.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 60, -1, -1));

        addStageTwoTabTwo.setText("Modify Regressors");
        addStageTwoTabTwo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStageTwoTabTwoActionPerformed(evt);
            }
        });
        jPanel12.add(addStageTwoTabTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 190, 40));

        jLabel17.setText("Random");
        jPanel12.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 60, -1, -1));

        jLabel18.setText("Scale X Random");
        jPanel12.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 60, 110, -1));

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel12.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 80, 20, 370));

        stageTwoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Stage-2 Regressors"));

        stageTwoRegsGrid.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout stageTwoPanelLayout = new javax.swing.GroupLayout(stageTwoPanel);
        stageTwoPanel.setLayout(stageTwoPanelLayout);
        stageTwoPanelLayout.setHorizontalGroup(
            stageTwoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(stageTwoRegsGrid, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE)
        );
        stageTwoPanelLayout.setVerticalGroup(
            stageTwoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(stageTwoRegsGrid, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
        );

        stageTwoRegsGrid.getAccessibleContext().setAccessibleParent(jPanel12);

        jPanel12.add(stageTwoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 70, 720, 380));

        runTabTwoStageOneTwo.setText("Run Stage 1 and 2");
        runTabTwoStageOneTwo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runTabTwoStageOneTwoActionPerformed(evt);
            }
        });
        jPanel12.add(runTabTwoStageOneTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 470, 160, 40));

        suppressIntCheckBox.setText("Suppress All Interactions");
        suppressIntCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppressIntCheckBoxActionPerformed(evt);
            }
        });
        jPanel12.add(suppressIntCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 220, -1, -1));

        stageTwoOutcome.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel12.add(stageTwoOutcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 190, 30));

        jLabel22.setText("Stage 2 Outcome:");
        jPanel12.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, -1, -1));
        jPanel12.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 190, -1));
        jPanel12.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 200, 190, -1));

        jButton1.setText("Reset");
        jPanel12.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 470, 140, 40));
        jPanel12.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 520, 740, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/mixLogo.png"))); // NOI18N
        jPanel12.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 470, -1, 30));
        jPanel12.add(filler1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 570, -1, 80));
        jPanel12.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 40, 320, 10));

        stageOneTabs.addTab("Stage 2 Configuration", jPanel12);

        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        stageOneOutput.setColumns(20);
        stageOneOutput.setFont(new java.awt.Font("Monospaced", 0, 13)); // NOI18N
        stageOneOutput.setRows(5);
        jScrollPane2.setViewportView(stageOneOutput);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 697, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jButton8.setText("Save Output As ...");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Save Stage 1+2 Def File As ...");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/mixLogo.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(181, 181, 181)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(297, 297, 297)
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(223, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(345, Short.MAX_VALUE))
        );

        stageOneTabs.addTab("Stage 1 Results", jPanel3);

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        stageTwoOutput.setColumns(20);
        stageTwoOutput.setFont(new java.awt.Font("Monospaced", 0, 13)); // NOI18N
        stageTwoOutput.setRows(5);
        jScrollPane1.setViewportView(stageTwoOutput);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(125, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(192, 24, 940, -1));

        saveStage2OutButton.setText("Save Output As ...");
        saveStage2OutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveStage2OutButtonActionPerformed(evt);
            }
        });
        jPanel4.add(saveStage2OutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 390, 227, 39));

        jButton11.setText("Save Stage 2 Def File As ...");
        jPanel4.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 390, 232, 39));

        jButton12.setText("Save Stage 1 and 2 Def File As ...");
        jPanel4.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 390, -1, 39));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/mixLogo.png"))); // NOI18N
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 390, -1, 39));

        stageOneTabs.addTab("Stage 2 Results", jPanel4);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/mixreg.png"))); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 854, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 850, 450));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/mixLogo.png"))); // NOI18N
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 550, -1, 40));

        stageOneTabs.addTab("View Model", jPanel2);

        parentPanel.add(stageOneTabs, "card2");

        getContentPane().add(parentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1224, -1));

        fileMenu.setText("File");

        newModelMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newModelMenu.setText("New Model");
        newModelMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newModelMenuActionPerformed(evt);
            }
        });
        fileMenu.add(newModelMenu);

        modifyStageOneMenu.setText("Modify Stage 1 and 2");
        modifyStageOneMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyStageOneMenuActionPerformed(evt);
            }
        });
        fileMenu.add(modifyStageOneMenu);

        modifyStageTwoMenu.setText("Modify Stage 2 Only");
        fileMenu.add(modifyStageTwoMenu);

        superUserMenu.setText("Super User Mode");
        superUserMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                superUserMenuActionPerformed(evt);
            }
        });
        fileMenu.add(superUserMenu);

        exitMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        exitMenu.setText("Exit");
        exitMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenu);

        jMenuBar1.add(fileMenu);

        helpMenu.setText("Help");

        diagramMenu.setText("Diagram");
        diagramMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diagramMenuActionPerformed(evt);
            }
        });
        helpMenu.add(diagramMenu);

        jMenuItem6.setText("Template");
        helpMenu.add(jMenuItem6);

        jMenuItem7.setText("Citations");
        helpMenu.add(jMenuItem7);

        jMenuItem8.setText("Documentation");
        helpMenu.add(jMenuItem8);

        jMenuItem9.setText("About");
        helpMenu.add(jMenuItem9);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newModelMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newModelMenuActionPerformed

        // opens new model window
        newModel.setVisible(true);
       // instructions.setVisible(true);
        

    }//GEN-LAST:event_newModelMenuActionPerformed

    private void exitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuActionPerformed

    private void modifyStageOneMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyStageOneMenuActionPerformed
        isSubmitClicked();
    }//GEN-LAST:event_modifyStageOneMenuActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void diagramMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diagramMenuActionPerformed
        //opens webpage with the url
        openWebpage("http://myquitadmin.usc.edu/mixsuite.php");

    }//GEN-LAST:event_diagramMenuActionPerformed

    private void goBackMxrButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goBackMxrButtonActionPerformed

        newModel.setVisible(true);
    }//GEN-LAST:event_goBackMxrButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // TODO add your handling code here:

        IDvariableCombo.setSelectedIndex(0);
        StageOneVariableCombo.setSelectedIndex(1);
        stageTwoOutcome.setSelectedIndex(2);

        buttonGroup1.clearSelection();

        addStageTwoTabTwo.setEnabled(false);


    }//GEN-LAST:event_resetButtonActionPerformed

    private void NoAssociationRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoAssociationRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoAssociationRadioActionPerformed

    private void superUserMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_superUserMenuActionPerformed
        // TODO add your handling code here:
        superUserMenuLaunch = new def_lib.SuperUserMenu();

        superUserMenuLaunch.setVisible(true);
    }//GEN-LAST:event_superUserMenuActionPerformed

    private void addStageTwoTabTwoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStageTwoTabTwoActionPerformed
        // TODO add your handling code here:

        stage_2_regs = new stageTwoRegs();
        //stageOneTabs.setSelectedIndex(1);

        stage_2_regs.setVisible(true);
        stage_2_regs.updateStageTwoVariables(getSavedVariables());
    }//GEN-LAST:event_addStageTwoTabTwoActionPerformed

    private void suppressIntCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppressIntCheckBoxActionPerformed
        // TODO add your handling code here:

        if (suppressed == false) {

            for (int p = 0; p < stageTwoRegSize; p++) {

                stageTwoGridBoxes.get(p).get(3).setSelected(false);
                stageTwoGridBoxes.get(p).get(3).setEnabled(false);
            }

        } else {
            for (int p = 0; p < stageTwoRegSize; p++) {

                stageTwoGridBoxes.get(p).get(3).setSelected(false);
                stageTwoGridBoxes.get(p).get(3).setEnabled(true);

            }

        }

        suppressed = true;
    }//GEN-LAST:event_suppressIntCheckBoxActionPerformed

    private void runTabTwoStageOneTwoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runTabTwoStageOneTwoActionPerformed
        // TODO add your handling code here:
        // tryCount counts the number of successful definitionhelper function calls
        //catchCount counts number of exceptions in reading values to derHelper. 
        //Prevents UI from moving forward in case of an exception
        int tryCount = 0;
        int catchCount = 0;

        // *********************************************************************
        // Test printing statements counting mean regressors
        System.out.println("Total selected mean regressors in level one: " + String.valueOf(countLevelOneBeta()));
        System.out.println("Total selected BS Variances in level one: " + String.valueOf(countLevelOneAlpha()));
        System.out.println("Total selected WS Variances in level one: " + String.valueOf(countLevelOneTau()));
        System.out.println("Total selected disagg. variance in level one: " + String.valueOf(countLevelOneDicompMean()));

        System.out.println("Total selected mean regressors in level two: " + String.valueOf(countLevelTwoBeta()));
        System.out.println("Total selected BS variances in level two: " + String.valueOf(countLevelTwoAlpha()));
        System.out.println("Total selected WS variances in level two: " + String.valueOf(countLevelTwoTau()));

        System.out.println("Total selected mean regressors in stage two: " + String.valueOf(countStageTwoBeta()));
        System.out.println("Total selected BS Variances in stage two: " + String.valueOf(countStageTwoAlpha()));
        System.out.println("Total selected WS Variances in stage two: " + String.valueOf(countStageTwoTau()));

        //**********************************************************************
        // Reads selected ID variable and outcome variable from the first two comboboxes
        //String[] idOutcome = {String.valueOf(IDvariableCombo.getSelectedIndex() + 1), String.valueOf(StageOneVariableCombo.getSelectedIndex() + 1)};

       /* try {
            tryCount = 1;
            NewModel.defFile.setIdOutcome(idOutcome);
            System.out.println("From defHelper | ID and Outcome indices: " + Arrays.toString(NewModel.defFile.getIdOutcome()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }*/

//        try {
//            tryCount = 1;
//            NewModel.defFile.setLabelModelOutcome(getOutcomeLabel());
//            System.out.println("From defHelper | Outcome variable Stage One LABEL: " + NewModel.defFile.getLabelModelOutcome());
//        } catch (Exception ex) {
//            catchCount = 1;
//            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//        }
        
        // =======

        try {
            NewModel.defFile.setStageTwoOutcomeField(getStageTwoOutcomePosition());
            System.out.println("From defHelper | Outcome variable Position STAGE TWO: " + NewModel.defFile.getStageTwoOutcomeField());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            NewModel.defFile.setStageTwoOutcomeLabel(getStageTwoOutcomeLabel());
            System.out.println("From defHelper | Outcome variable label STAGE TWO: " + NewModel.defFile.getStageTwoOutcomeLabel());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        // *********************************************************************
        // i is the number of random location effects selected by the users
        if (i == 1) {

            //Number of disaggregate means
//            try {
//                tryCount = 1;
//                NewModel.defFile.setDecompMeanCount(String.valueOf(countLevelOneDicompMean()));
//                System.out.println("From defHelper | Stage 1 Decomp Model Mean Count: " + NewModel.defFile.getDecompMeanCount().toString());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            //Number of disaggregate BS Variance
//            try {
//                tryCount = 1;
//                NewModel.defFile.setDecompBSCount(String.valueOf(countLevelOneDicompBS()));
//                System.out.println("From defHelper | Stage 1 BS Variance Disagg. Regressor Count: " + NewModel.defFile.getDecompBSCount().toString());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            //Number of disaggregate WS Variance
//            try {
//                tryCount = 1;
//                NewModel.defFile.setDecompWSCount(String.valueOf(countLevelOneDicompWS()));
//                System.out.println("From defHelper | Stage 1 WS Variance Disagg Regressor Count: " + NewModel.defFile.getDecompBSCount().toString());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }

            // ---- Check if the association radio buttons have been selected (Advanced effect of mean) ----
            //count field array sizes     
            if (NoAssociationRadio.isSelected()) {

                try {
                    NewModel.defFile.setAdvancedEffectMeanWS(String.valueOf(0));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (No Association): " + NewModel.defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (LinearAssociationRadio.isSelected()) {
                try {
                    NewModel.defFile.setAdvancedEffectMeanWS(String.valueOf(1));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (Linear Association): " + NewModel.defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (QuadraticAssociationRadio.isSelected()) {
                try {
                    NewModel.defFile.setAdvancedEffectMeanWS(String.valueOf(2));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (Quadratic Association): " + NewModel.defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } // field array counting ends

            // get selected regressor labels and read them into defFile
//            try {
//                NewModel.defFile.setLabelModelMeanRegressors(ModelMeansLabelsArray());
//                System.out.println("From defHelper | Stage 1 MEAN REGRESSOR LABELS): " + Arrays.toString(NewModel.defFile.getLabelModelMeanRegressors()));
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setLabelModelBSRegressors(ModelBSLabelsArray());
//                System.out.println("From defHelper | Stage 1 BS REGRESSOR LABELS): " + Arrays.toString(NewModel.defFile.getLabelModelBSRegressors()));
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setLabelModelWSRegressors(ModelWSLabelsArray());
//                System.out.println("From defHelper | Stage 1 WS REGRESSOR LABELS): " + Arrays.toString(NewModel.defFile.getLabelModelWSRegressors()));
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            // Reads the variable names of variables that have been selected as mean regressors
//            try {
//                NewModel.defFile.setFieldModelMeanRegressors(fieldModelMeanArray());
//                System.out.println("From defHelper | #Stage One Mean Regressors: " + NewModel.defFile.getFieldModelMeanRegressors().length);
//                System.out.println("From defHelper | Stage One Mean Regressors Selected: " + Arrays.toString(NewModel.defFile.getFieldModelMeanRegressors()));
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            // Reads the variable names of variables that have been selected as BS Variances
//            try {
//                NewModel.defFile.setFieldModelBSRegressors(fieldModelBSArray());
//                System.out.println("From defHelper | #Stage One BS Regressors: " + NewModel.defFile.getFieldModelBSRegressors().length);
//                System.out.println("From defHelper | Stage One BS Var. Regressors Selected: " + Arrays.toString(NewModel.defFile.getFieldModelBSRegressors()));
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            // Reads the variable names of variables that have been selected as WS Variances
//            try {
//                NewModel.defFile.setFieldModelWSRegressors(fieldModelWSArray());
//                System.out.println("From defHelper | #Stage One WS Regressors: " + NewModel.defFile.getFieldModelWSRegressors().length);
//                System.out.println("From defHelper | Stage One WS Var. Regressors Selected: " + Arrays.toString(NewModel.defFile.getFieldModelWSRegressors()));
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setFieldDecompMeanRegressors(getMeanDecompFieldRegressorLabels_levelOne());
//                System.out.println("From defHelper | #Stage One Mean + Disagg. Regressors: " + NewModel.defFile.getFieldDecompMeanRegressors().length);
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setFieldDecompBSRegressors(getBSDecompFieldRegressorLabels_levelOne());
//                System.out.println("From defHelper | #Stage One BS + Disagg. Regressors: " + NewModel.defFile.getFieldDecompBSRegressors().length);
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setFieldDecompWSRegressors(getWSDecompFieldRegressorLabels_levelOne());
//                System.out.println("From defHelper | #Stage One WS + Disagg. Regressors: " + NewModel.defFile.getFieldDecompWSRegressors().length);
//            } catch (Exception ex) {
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                tryCount = 1;
//                int MeanCount = countLevelOneBeta() + countLevelTwoBeta() - countLevelOneDicompMean();
//                // count total mean regressors in level one and level two
//                NewModel.defFile.setModelMeanCount(String.valueOf(MeanCount));
//                System.out.println("From defHelper | Stage 1 Model Mean Count: " + NewModel.defFile.getModelMeanCount().toString());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                tryCount = 1;
//                int betweenCount = countLevelOneAlpha() + countLevelTwoAlpha() - countLevelOneDicompBS();
//                NewModel.defFile.setModelBetweenCount(String.valueOf(betweenCount));
//                System.out.println("From defHelper | Model Between Count: " + NewModel.defFile.getModelBetweenCount().toString());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                tryCount = 1;
//                int withinCount = countLevelOneTau() + countLevelTwoTau() - countLevelOneDicompWS();
//                NewModel.defFile.setModelWithinCount(String.valueOf(withinCount));
//                System.out.println("From defHelper | Model Within Count: " + NewModel.defFile.getModelBetweenCount().toString());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setLabelDecompMeanRegressors(getDecompMeanLabelsLevelOne());
//                System.out.println("From defHelper | Model Decomp + Mean Labels: " + Arrays.toString(NewModel.defFile.getLabelDecompMeanRegressors()));
//
//            } catch (Exception ex) {
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setLabelDecompBSRegressors(getDecompBSLabelsLevelOne());
//                System.out.println("From defHelper | Model Decomp + BS Labels: " + Arrays.toString(NewModel.defFile.getLabelDecompBSRegressors()));
//
//            } catch (Exception ex) {
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setLabelDecompWSRegressors(getDecompWSLabelsLevelOne());
//                System.out.println("From defHelper | Model Decomp + WS Labels: " + Arrays.toString(NewModel.defFile.getLabelDecompWSRegressors()));
//
//            } catch (Exception ex) {
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }

            //******************************************************************
        } else if (i > 1) {

            //Check if the effect of mean on WS variances options have been selected
            if (NoAssociationRadio.isSelected()) {

                try {
                    NewModel.defFile.setAdvancedEffectMeanWS(String.valueOf(0));
                    System.out.println("From defHelper | Stage 1 Association of random location & scale?: " + NewModel.defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (LinearAssociationRadio.isSelected()) {
                try {
                    NewModel.defFile.setAdvancedEffectMeanWS(String.valueOf(1));
                    System.out.println("From defHelper | Stage 1 Association of random location & scale?: " + NewModel.defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
            }

//            try {
//                tryCount = 1;
//
//                //Disagg means count 
//                NewModel.defFile.setDecompMeanCount(String.valueOf(countLevelOneDicompMean()));
//                System.out.println("From defHelper | Stage 1 Decomp Model Mean Count: " + NewModel.defFile.getDecompMeanCount().toString());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                tryCount = 1;
//                //Disagg Random Location count 
//                NewModel.defFile.setDecompLocRanCount(String.valueOf(countLevelOneDicompBS()));
//                System.out.println("From defHelper | Stage 1 Decomp Model Loc Random Count: " + NewModel.defFile.getDecompMeanCount().toString());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                tryCount = 1;
//                //Disagg scale count 
//                NewModel.defFile.setDecompScaleCount(String.valueOf(countLevelOneDicompWS()));
//                System.out.println("From defHelper | Stage 1 Decomp Scale Count: " + NewModel.defFile.getDecompScaleCount().toString());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setLabelModelMeanRegressors(ModelMeansLabelsArray());
//                System.out.println("From defHelper | Stage 1 MEAN REGRESSOR LABELS): " + Arrays.toString(NewModel.defFile.getLabelModelMeanRegressors()));
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setLabelModelLocRanRegressors(ModelBSLabelsArray());
//                System.out.println("From defHelper | Stage 1 LocRan REGRESSOR LABELS): " + Arrays.toString(NewModel.defFile.getLabelModelLocRanRegressors()));
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setLabelModelScaleRegressors(ModelWSLabelsArray());
//                System.out.println("From defHelper | Stage 1 Scale REGRESSOR LABELS): " + Arrays.toString(NewModel.defFile.getLabelModelScaleRegressors()));
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            // count field labels
//            try {
//                // get variable names from selected mean regressors
//                NewModel.defFile.setFieldModelMeanRegressors(fieldModelMeanArray());
//                System.out.println("From defHelper | #Stage One Mean Regressors: " + NewModel.defFile.getFieldModelMeanRegressors().length);
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                // get variable names from selected random location regressors
//                NewModel.defFile.setFieldModelLocRanRegressors(fieldModelBSArray());
//                System.out.println("From defHelper | #Stage One BS(RanLoc) Regressors: " + NewModel.defFile.getFieldModelLocRanRegressors().length);
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                // get variable names from selected scale regressors
//                NewModel.defFile.setFieldModelScaleRegressors(fieldModelWSArray());
//                System.out.println("From defHelper | #Stage One WS(Scale) Regressors: " + NewModel.defFile.getFieldModelScaleRegressors().length);
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setFieldDecompMeanRegressors(getMeanDecompFieldRegressorLabels_levelOne());
//                System.out.println("From defHelper | #Stage One Mean + Disagg. Regressors: " + NewModel.defFile.getFieldDecompMeanRegressors().length);
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setFieldDecompLocRanRegressors(getBSFieldRegressorLabels_levelOne());
//                System.out.println("From defHelper | #Stage One BS(RanLoc) + Disagg. Regressors: " + NewModel.defFile.getFieldDecompLocRanRegressors().length);
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setFieldDecompScaleRegressors(getWSDecompFieldRegressorLabels_levelOne());
//                System.out.println("From defHelper | #Stage One WS(Scale) + Disagg. Regressors: " + NewModel.defFile.getFieldDecompScaleRegressors().length);
//            } catch (Exception ex) {
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setLabelDecompMeanRegressors(getDecompMeanLabelsLevelOne());
//                System.out.println("From defHelper | Model Decomp + Mean Labels: " + Arrays.toString(NewModel.defFile.getLabelDecompMeanRegressors()));
//            } catch (Exception ex) {
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setLabelDecompLocRanRegressors(getDecompBSLabelsLevelOne());
//                System.out.println("From defHelper | Model Decomp + LocRan Labels: " + Arrays.toString(NewModel.defFile.getLabelDecompLocRanRegressors()));
//            } catch (Exception ex) {
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setLabelDecompScaleRegressors(getDecompWSLabelsLevelOne());
//                System.out.println("From defHelper | Model Decomp + Scale Labels: " + Arrays.toString(NewModel.defFile.getLabelDecompScaleRegressors()));
//            } catch (Exception ex) {
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                tryCount = 1;
//                int MeanCount = countLevelOneBeta() + countLevelTwoBeta() - countLevelOneDicompMean();
//
//                // count total mean regressors in level one and level two
//                NewModel.defFile.setModelMeanCount(String.valueOf(MeanCount));
//                System.out.println("From defHelper | Stage 1 Model Mean Count: " + NewModel.defFile.getModelMeanCount().toString());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                int LocRanCount = countLevelOneAlpha() + countLevelTwoAlpha() - countLevelOneDicompBS();
//                // count total random location regressors in level one and level two
//                NewModel.defFile.setModelLocRanCount(String.valueOf(LocRanCount));
//                System.out.println("From defHelper | Stage 1 Model Loc Ran Count: " + NewModel.defFile.getModelLocRanCount().toString());
//                tryCount = 1;
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                tryCount = 1;
//                int ScaleCount = countLevelOneTau() + countLevelTwoTau() - countLevelOneDicompWS();
//                // count total scale regressors in level one and level two
//                NewModel.defFile.setModelScaleCount(String.valueOf(ScaleCount));
//                System.out.println("From defHelper | Stage 1 Model Scale Count: " + NewModel.defFile.getModelLocRanCount().toString());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }

        }
        // =p=p=p=p=

        try {
            NewModel.defFile.setStageTwoFixedCount(String.valueOf(countStageTwoBeta()));
            System.out.println("From defHelper | STAGE TWO FIXED COUNT: " + NewModel.defFile.getStageTwoFixedCount().toString());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            NewModel.defFile.setStageTwoLocRanInteractions(String.valueOf(countStageTwoAlpha()));
            System.out.println("From defHelper | STAGE TWO LOC. RANDOM COUNT: " + NewModel.defFile.getStageTwoLocRanInteractions().toString());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            NewModel.defFile.setStageTwoScaleInteractions(String.valueOf(countStageTwoTau()));
            System.out.println("From defHelper | STAGE TWO SCALE COUNT: " + NewModel.defFile.getStageTwoScaleInteractions().toString());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }
        
        if (suppressed == false) {
            
            try {
                NewModel.defFile.setStageTwoIntOfInteraction(String.valueOf(countStageTwoInteractions()));
                System.out.println("From defHelper | STAGE TWO INTERACTIONS COUNT: " + NewModel.defFile.getStageTwoIntOfInteraction().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
            
        }

        try {
            NewModel.defFile.setStageTwoFixedFields(getFixedFieldRegressors_StageTwo());
            System.out.println("From defHelper | STAGE TWO  FIXED REGRESSOR Positions: " + Arrays.toString(NewModel.defFile.getStageTwoFixedFields()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            NewModel.defFile.setStageTwoLocRanIntFields(getRanLocFieldRegressors_StageTwo());
            System.out.println("From defHelper | STAGE TWO  LOC RAN REGRESSOR Positions: " + Arrays.toString(NewModel.defFile.getStageTwoLocRanIntFields()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            NewModel.defFile.setStageTwoScaleIntFields(getScaleFieldRegressors_StageTwo());
            System.out.println("From defHelper | STAGE TWO  SCALE REGRESSOR Positions: " + Arrays.toString(NewModel.defFile.getStageTwoScaleIntFields()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        
        if (suppressed == false) {
            
            try {
                NewModel.defFile.setStageTwoFirstIntFields(getInteractionFieldRegressors_StageTwo());
                System.out.println("From defHelper | STAGE TWO  INTERACTIONS REGRESSOR Positions: " + Arrays.toString(NewModel.defFile.getStageTwoFirstIntFields()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setStageTwoFirstIntLabels(getModelInteractionLabelsStageTwo());
                System.out.println("From defHelper | STAGE TWO  INTERACTIONS REGRESSORS: " + Arrays.toString(NewModel.defFile.getStageTwoFirstIntLabels()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
        }

        try {
            NewModel.defFile.setStageTwoFixedLabels(getModelFixedLabelsStageTwo());
            System.out.println("From defHelper | STAGE TWO  MEAN REGRESSORS: " + Arrays.toString(NewModel.defFile.getStageTwoFixedLabels()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            NewModel.defFile.setStageTwoLocRanIntLabels(getModelLocRanLabelsStageTwo());
            System.out.println("From defHelper | STAGE TWO  LOC RAN REGRESSORS: " + Arrays.toString(NewModel.defFile.getStageTwoLocRanIntLabels()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            NewModel.defFile.setStageTwoScaleIntLabels(getModelScaleLabelsStageTwo());
            System.out.println("From defHelper | STAGE TWO  SCALE REGRESSORS: " + Arrays.toString(NewModel.defFile.getStageTwoScaleIntLabels()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        if (catchCount == 0) {
            int defTry = 0;
            int defCatch = 0;
            try {
                List<String> defFileOutput;

                NewModel.defFile.writeDefFileToFolder();
                defFileOutput = NewModel.defFile.buildStageOneDefinitonList();
                System.out.println("From defHelper | Stage 1 def file created successfully!");

            } catch (Exception ex) {
                defCatch = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                System.out.println("From defHelper | Stage 1 def file failed!");
            }

            if (defCatch == 0) {
                stageOneTabs.setSelectedIndex(3);
            }

        } else {

            // do nothing
        }
        
        try {
            produceStageTwoOutput();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            produceStageOneOutput();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_runTabTwoStageOneTwoActionPerformed

    private void startStageTwoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startStageTwoActionPerformed
        // TODO add your handling code here:
        
        
        
            int tryCount = 0;
            int catchCount = 0;

        // *********************************************************************
        // Test printing statements counting mean regressors
        System.out.println("Total selected mean regressors in level one: " + String.valueOf(countLevelOneBeta()));
        System.out.println("Total selected BS Variances in level one: " + String.valueOf(countLevelOneAlpha()));
        System.out.println("Total selected WS Variances in level one: " + String.valueOf(countLevelOneTau()));
        System.out.println("Total selected disagg. variance in level one: " + String.valueOf(countLevelOneDicompMean()));

        System.out.println("Total selected mean regressors in level two: " + String.valueOf(countLevelTwoBeta()));
        System.out.println("Total selected BS variances in level two: " + String.valueOf(countLevelTwoAlpha()));
        System.out.println("Total selected WS variances in level two: " + String.valueOf(countLevelTwoTau()));
        
        // Reads selected ID variable and outcome variable from the first two comboboxes
        String[] idOutcome = {String.valueOf(IDvariableCombo.getSelectedIndex() + 1), String.valueOf(StageOneVariableCombo.getSelectedIndex() + 1)};

        try {
            tryCount = 1;
            NewModel.defFile.setIdOutcome(idOutcome);
            System.out.println("From defHelper | ID and Outcome indices: " + Arrays.toString(NewModel.defFile.getIdOutcome()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            tryCount = 1;
            NewModel.defFile.setLabelModelOutcome(getOutcomeLabel());
            System.out.println("From defHelper | Outcome variable Stage One LABEL: " + NewModel.defFile.getLabelModelOutcome());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }
        
        // i is the number of random location effects selected by the users
        if (i == 1) {
            
            
            try {
                tryCount = 1;
                int MeanCount = countLevelOneBeta() + countLevelTwoBeta() - countLevelOneDicompMean(); //check this ======================
                // count total mean regressors in level one and level two
                NewModel.defFile.setModelMeanCount(String.valueOf(MeanCount));
                System.out.println("From mixRegGUI | Stage 1 Model Mean Count: " + String.valueOf(MeanCount));
                System.out.println("From defHelper | Stage 1 Model Mean Count: " + NewModel.defFile.getModelMeanCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;
                int betweenCount = countLevelOneAlpha() + countLevelTwoAlpha() - countLevelOneDicompBS();
                NewModel.defFile.setModelBetweenCount(String.valueOf(betweenCount));
                System.out.println("From defHelper | Model Between Count: " + NewModel.defFile.getModelBetweenCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;
                int withinCount = countLevelOneTau() + countLevelTwoTau() - countLevelOneDicompWS();
                NewModel.defFile.setModelWithinCount(String.valueOf(withinCount));
                System.out.println("From defHelper | Model Within Count: " + NewModel.defFile.getModelWithinCount());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            //Number of disaggregate means
            try {
                tryCount = 1;
                NewModel.defFile.setDecompMeanCount(String.valueOf(countLevelOneDicompMean()));
                System.out.println("From defHelper | Stage 1 Decomp Model Mean Count: " + NewModel.defFile.getDecompMeanCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            //Number of disaggregate BS Variance
            try {
                tryCount = 1;
                NewModel.defFile.setDecompBSCount(String.valueOf(countLevelOneDicompBS()));
                System.out.println("From defHelper | Stage 1 BS Variance Disagg. Regressor Count: " + NewModel.defFile.getDecompBSCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            //Number of disaggregate WS Variance
            try {
                tryCount = 1;
                NewModel.defFile.setDecompWSCount(String.valueOf(countLevelOneDicompWS()));
                System.out.println("From defHelper | Stage 1 WS Variance Disagg Regressor Count: " + NewModel.defFile.getDecompWSCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            // ---- Check if the association radio buttons have been selected (Advanced effect of mean) ----
            //count field array sizes     
            if (NoAssociationRadio.isSelected()) {

                try {
                    NewModel.defFile.setAdvancedEffectMeanWS(String.valueOf(0));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (No Association): " + NewModel.defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (LinearAssociationRadio.isSelected()) {
                try {
                    NewModel.defFile.setAdvancedEffectMeanWS(String.valueOf(1));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (Linear Association): " + NewModel.defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (QuadraticAssociationRadio.isSelected()) {
                try {
                    NewModel.defFile.setAdvancedEffectMeanWS(String.valueOf(2));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (Quadratic Association): " + NewModel.defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } // field array counting ends

            // get selected regressor labels and read them into defFile
            try {
                NewModel.defFile.setLabelModelMeanRegressors(ModelMeansLabelsArray());
                System.out.println("From defHelper | Stage 1 MEAN REGRESSOR LABELS): " + Arrays.toString(NewModel.defFile.getLabelModelMeanRegressors()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setLabelModelBSRegressors(ModelBSLabelsArray());
                System.out.println("From defHelper | Stage 1 BS REGRESSOR LABELS): " + Arrays.toString(NewModel.defFile.getLabelModelBSRegressors()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setLabelModelWSRegressors(ModelWSLabelsArray());
                System.out.println("From defHelper | Stage 1 WS REGRESSOR LABELS): " + Arrays.toString(NewModel.defFile.getLabelModelWSRegressors()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            // Reads the variable names of variables that have been selected as mean regressors
            try {
                NewModel.defFile.setFieldModelMeanRegressors(fieldModelMeanArray());
                System.out.println("From defHelper | #Stage One Mean Regressors: " + NewModel.defFile.getFieldModelMeanRegressors().length);
                System.out.println("From defHelper | Stage One Mean Regressors Selected: " + Arrays.toString(NewModel.defFile.getFieldModelMeanRegressors()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            // Reads the variable names of variables that have been selected as BS Variances
            try {
                NewModel.defFile.setFieldModelBSRegressors(fieldModelBSArray());
                System.out.println("From defHelper | #Stage One BS Regressors: " + NewModel.defFile.getFieldModelBSRegressors().length);
                System.out.println("From defHelper | Stage One BS Var. Regressors Selected: " + Arrays.toString(NewModel.defFile.getFieldModelBSRegressors()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            // Reads the variable names of variables that have been selected as WS Variances
            try {
                NewModel.defFile.setFieldModelWSRegressors(fieldModelWSArray());
                System.out.println("From defHelper | #Stage One WS Regressors: " + NewModel.defFile.getFieldModelWSRegressors().length);
                System.out.println("From defHelper | Stage One WS Var. Regressors Selected: " + Arrays.toString(NewModel.defFile.getFieldModelWSRegressors()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setFieldDecompMeanRegressors(getMeanDecompFieldRegressorLabels_levelOne());
                System.out.println("From defHelper | #Stage One Mean + Disagg. Regressors: " + NewModel.defFile.getFieldDecompMeanRegressors().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setFieldDecompBSRegressors(getBSDecompFieldRegressorLabels_levelOne());
                System.out.println("From defHelper | #Stage One BS + Disagg. Regressors: " + NewModel.defFile.getFieldDecompBSRegressors().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setFieldDecompWSRegressors(getWSDecompFieldRegressorLabels_levelOne());
                System.out.println("From defHelper | #Stage One WS + Disagg. Regressors: " + NewModel.defFile.getFieldDecompWSRegressors().length);
            } catch (Exception ex) {
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setLabelDecompMeanRegressors(getDecompMeanLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + Mean Labels: " + Arrays.toString(NewModel.defFile.getLabelDecompMeanRegressors()));

            } catch (Exception ex) {
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setLabelDecompBSRegressors(getDecompBSLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + BS Labels: " + Arrays.toString(NewModel.defFile.getLabelDecompBSRegressors()));

            } catch (Exception ex) {
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setLabelDecompWSRegressors(getDecompWSLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + WS Labels: " + Arrays.toString(NewModel.defFile.getLabelDecompWSRegressors()));

            } catch (Exception ex) {
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            //******************************************************************
        } else if (i > 1) {
            
            try {
                tryCount = 1;
                int MeanCount = countLevelOneBeta() + countLevelTwoBeta() - countLevelOneDicompMean();

                // count total mean regressors in level one and level two
                NewModel.defFile.setModelMeanCount(String.valueOf(MeanCount));
                System.out.println("From defHelper | Stage 1 Model Mean Count: " + NewModel.defFile.getModelMeanCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                int LocRanCount = countLevelOneAlpha() + countLevelTwoAlpha() - countLevelOneDicompBS();
                // count total random location regressors in level one and level two
                NewModel.defFile.setModelLocRanCount(String.valueOf(LocRanCount));
                System.out.println("From defHelper | Stage 1 Model Loc Ran Count: " + NewModel.defFile.getModelLocRanCount().toString());
                tryCount = 1;
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;
                int ScaleCount = countLevelOneTau() + countLevelTwoTau() - countLevelOneDicompWS();
                // count total scale regressors in level one and level two
                NewModel.defFile.setModelScaleCount(String.valueOf(ScaleCount));
                System.out.println("From defHelper | Stage 1 Model Scale Count: " + NewModel.defFile.getModelScaleCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
            
            try {
                tryCount = 1;

                //Disagg means count 
                NewModel.defFile.setDecompMeanCount(String.valueOf(countLevelOneDicompMean()));
                System.out.println("From defHelper | Stage 1 Decomp Model Mean Count: " + NewModel.defFile.getDecompMeanCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;
                //Disagg Random Location count 
                NewModel.defFile.setDecompLocRanCount(String.valueOf(countLevelOneDicompBS()));
                System.out.println("From defHelper | Stage 1 Decomp Model Loc Random Count: " + NewModel.defFile.getDecompLocRanCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;
                //Disagg scale count 
                NewModel.defFile.setDecompScaleCount(String.valueOf(countLevelOneDicompWS()));
                System.out.println("From defHelper | Stage 1 Decomp Scale Count: " + NewModel.defFile.getDecompScaleCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
            
            //Check if the effect of mean on WS variances options have been selected
            if (NoAssociationRadio.isSelected()) {

                try {
                    NewModel.defFile.setAdvancedEffectMeanWS(String.valueOf(0));
                    System.out.println("From defHelper | Stage 1 Association of random location & scale?: " + NewModel.defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (LinearAssociationRadio.isSelected()) {
                try {
                    NewModel.defFile.setAdvancedEffectMeanWS(String.valueOf(1));
                    System.out.println("From defHelper | Stage 1 Association of random location & scale?: " + NewModel.defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
            }
            
            try {
                NewModel.defFile.setLabelModelMeanRegressors(ModelMeansLabelsArray());
                System.out.println("From defHelper | Stage 1 MEAN REGRESSOR LABELS): " + Arrays.toString(NewModel.defFile.getLabelModelMeanRegressors()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setLabelModelLocRanRegressors(ModelBSLabelsArray());
                System.out.println("From defHelper | Stage 1 LocRan REGRESSOR LABELS): " + Arrays.toString(NewModel.defFile.getLabelModelLocRanRegressors()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setLabelModelScaleRegressors(ModelWSLabelsArray());
                System.out.println("From defHelper | Stage 1 Scale REGRESSOR LABELS): " + Arrays.toString(NewModel.defFile.getLabelModelScaleRegressors()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            // count field labels
            try {
                // get variable names from selected mean regressors
                NewModel.defFile.setFieldModelMeanRegressors(fieldModelMeanArray());
                System.out.println("From defHelper | #Stage One Mean Regressors: " + NewModel.defFile.getFieldModelMeanRegressors().length); //check this ===============
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                // get variable names from selected random location regressors
                NewModel.defFile.setFieldModelLocRanRegressors(fieldModelBSArray());
                System.out.println("From defHelper | #Stage One BS(RanLoc) Regressors: " + NewModel.defFile.getFieldModelLocRanRegressors().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                // get variable names from selected scale regressors
                NewModel.defFile.setFieldModelScaleRegressors(fieldModelWSArray());
                System.out.println("From defHelper | #Stage One WS(Scale) Regressors: " + NewModel.defFile.getFieldModelScaleRegressors().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setFieldDecompMeanRegressors(getMeanDecompFieldRegressorLabels_levelOne());
                System.out.println("From defHelper | #Stage One Mean + Disagg. Regressors: " + NewModel.defFile.getFieldDecompMeanRegressors().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setFieldDecompLocRanRegressors(getBSDecompFieldRegressorLabels_levelOne());
                //NewModel.defFile.setFieldDecompLocRanRegressors(fieldModelBSArray());
                System.out.println("From defHelper | #Stage One BS(RanLoc) + Disagg. Regressors: " + NewModel.defFile.getFieldDecompLocRanRegressors().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setFieldDecompScaleRegressors(getWSDecompFieldRegressorLabels_levelOne());
                System.out.println("From defHelper | #Stage One WS(Scale) + Disagg. Regressors: " + NewModel.defFile.getFieldDecompScaleRegressors().length);
            } catch (Exception ex) {
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setLabelDecompMeanRegressors(getDecompMeanLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + Mean Labels: " + Arrays.toString(NewModel.defFile.getLabelDecompMeanRegressors()));
            } catch (Exception ex) {
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setLabelDecompLocRanRegressors(getDecompBSLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + LocRan Labels: " + Arrays.toString(NewModel.defFile.getLabelDecompLocRanRegressors()));
            } catch (Exception ex) {
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                NewModel.defFile.setLabelDecompScaleRegressors(getDecompWSLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + Scale Labels: " + Arrays.toString(NewModel.defFile.getLabelDecompScaleRegressors()));
            } catch (Exception ex) {
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

        }
//            //Check if the effect of mean on WS variances options have been selected
//            if (NoAssociationRadio.isSelected()) {
//
//                try {
//                    NewModel.defFile.setAdvancedEffectMeanWS(String.valueOf(0));
//                    System.out.println("From defHelper | Stage 1 Association of random location & scale?: " + NewModel.defFile.getAdvancedEffectMeanWS());
//                } catch (Exception ex) {
//                    catchCount = 1;
//                    Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//                }
//
//            } else if (LinearAssociationRadio.isSelected()) {
//                try {
//                    NewModel.defFile.setAdvancedEffectMeanWS(String.valueOf(1));
//                    System.out.println("From defHelper | Stage 1 Association of random location & scale?: " + NewModel.defFile.getAdvancedEffectMeanWS());
//                } catch (Exception ex) {
//                    catchCount = 1;
//                    Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//                }
//            }

//            try {
//                tryCount = 1;
//
//                //Disagg means count 
//                NewModel.defFile.setDecompMeanCount(String.valueOf(countLevelOneDicompMean()));
//                System.out.println("From defHelper | Stage 1 Decomp Model Mean Count: " + NewModel.defFile.getDecompMeanCount().toString());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                tryCount = 1;
//                //Disagg Random Location count 
//                NewModel.defFile.setDecompLocRanCount(String.valueOf(countLevelOneDicompBS()));
//                System.out.println("From defHelper | Stage 1 Decomp Model Loc Random Count: " + NewModel.defFile.getDecompMeanCount().toString());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                tryCount = 1;
//                //Disagg scale count 
//                NewModel.defFile.setDecompScaleCount(String.valueOf(countLevelOneDicompWS()));
//                System.out.println("From defHelper | Stage 1 Decomp Scale Count: " + NewModel.defFile.getDecompScaleCount().toString());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }

//            try {
//                NewModel.defFile.setLabelModelMeanRegressors(ModelMeansLabelsArray());
//                System.out.println("From defHelper | Stage 1 MEAN REGRESSOR LABELS): " + Arrays.toString(NewModel.defFile.getLabelModelMeanRegressors()));
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setLabelModelLocRanRegressors(ModelBSLabelsArray());
//                System.out.println("From defHelper | Stage 1 LocRan REGRESSOR LABELS): " + Arrays.toString(NewModel.defFile.getLabelModelLocRanRegressors()));
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setLabelModelScaleRegressors(ModelWSLabelsArray());
//                System.out.println("From defHelper | Stage 1 Scale REGRESSOR LABELS): " + Arrays.toString(NewModel.defFile.getLabelModelScaleRegressors()));
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            // count field labels
//            try {
//                // get variable names from selected mean regressors
//                NewModel.defFile.setFieldModelMeanRegressors(fieldModelMeanArray());
//                System.out.println("From defHelper | #Stage One Mean Regressors: " + NewModel.defFile.getFieldModelMeanRegressors().length); //check this ===============
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                // get variable names from selected random location regressors
//                NewModel.defFile.setFieldModelLocRanRegressors(fieldModelBSArray());
//                System.out.println("From defHelper | #Stage One BS(RanLoc) Regressors: " + NewModel.defFile.getFieldModelLocRanRegressors().length);
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                // get variable names from selected scale regressors
//                NewModel.defFile.setFieldModelScaleRegressors(fieldModelWSArray());
//                System.out.println("From defHelper | #Stage One WS(Scale) Regressors: " + NewModel.defFile.getFieldModelScaleRegressors().length);
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setFieldDecompMeanRegressors(getMeanDecompFieldRegressorLabels_levelOne());
//                System.out.println("From defHelper | #Stage One Mean + Disagg. Regressors: " + NewModel.defFile.getFieldDecompMeanRegressors().length);
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setFieldDecompLocRanRegressors(getBSFieldRegressorLabels_levelOne());
//                System.out.println("From defHelper | #Stage One BS(RanLoc) + Disagg. Regressors: " + NewModel.defFile.getFieldDecompLocRanRegressors().length);
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setFieldDecompScaleRegressors(getWSDecompFieldRegressorLabels_levelOne());
//                System.out.println("From defHelper | #Stage One WS(Scale) + Disagg. Regressors: " + NewModel.defFile.getFieldDecompScaleRegressors().length);
//            } catch (Exception ex) {
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setLabelDecompMeanRegressors(getDecompMeanLabelsLevelOne());
//                System.out.println("From defHelper | Model Decomp + Mean Labels: " + Arrays.toString(NewModel.defFile.getLabelDecompMeanRegressors()));
//            } catch (Exception ex) {
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setLabelDecompLocRanRegressors(getDecompBSLabelsLevelOne());
//                System.out.println("From defHelper | Model Decomp + LocRan Labels: " + Arrays.toString(NewModel.defFile.getLabelDecompLocRanRegressors()));
//            } catch (Exception ex) {
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                NewModel.defFile.setLabelDecompScaleRegressors(getDecompWSLabelsLevelOne());
//                System.out.println("From defHelper | Model Decomp + Scale Labels: " + Arrays.toString(NewModel.defFile.getLabelDecompScaleRegressors()));
//            } catch (Exception ex) {
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }

//            try {
//                tryCount = 1;
//                int MeanCount = countLevelOneBeta() + countLevelTwoBeta() - countLevelOneDicompMean();
//
//                // count total mean regressors in level one and level two
//                NewModel.defFile.setModelMeanCount(String.valueOf(MeanCount));
//                System.out.println("From defHelper | Stage 1 Model Mean Count: " + NewModel.defFile.getModelMeanCount().toString());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                int LocRanCount = countLevelOneAlpha() + countLevelTwoAlpha() - countLevelOneDicompBS();
//                // count total random location regressors in level one and level two
//                NewModel.defFile.setModelLocRanCount(String.valueOf(LocRanCount));
//                System.out.println("From defHelper | Stage 1 Model Loc Ran Count: " + NewModel.defFile.getModelLocRanCount().toString());
//                tryCount = 1;
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                tryCount = 1;
//                int ScaleCount = countLevelOneTau() + countLevelTwoTau() - countLevelOneDicompWS();
//                // count total scale regressors in level one and level two
//                NewModel.defFile.setModelScaleCount(String.valueOf(ScaleCount));
//                System.out.println("From defHelper | Stage 1 Model Scale Count: " + NewModel.defFile.getModelLocRanCount().toString());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//        }
        
        if (outcomeNone == true){
            
            if (catchCount == 0) {
            int defTry = 0;
            int defCatch = 0;
            try {
                List<String> defFileOutput;
                
                NewModel.defFile.writeDefFileToFolder();

                defFileOutput = NewModel.defFile.buildStageOneDefinitonList();

                System.out.println("From defHelper | Stage 1 def file created successfully!");

            } catch (Exception ex) {
                defCatch = 1;
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                System.out.println("From defHelper | Stage 1 def file failed!");
            }

            if (defCatch == 0) {
                stageOneTabs.setSelectedIndex(3);
            }

        } else {

            //stageOneTabs.setSelectedIndex(1);
            //System.out.println("outcome not true!!!!");
        }
        
        } else {
            
            stageOneTabs.setSelectedIndex(1);
            System.out.println("outcome not none!!!!");
        
        }
        
        
        try {
            produceStageOneOutput();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_startStageTwoActionPerformed

    private void advancedOptionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advancedOptionsButtonActionPerformed
        // TODO add your handling code here:
        advancedOptions_view = new advancedOptions();

        advancedOptions_view.setVisible(true);
    }//GEN-LAST:event_advancedOptionsButtonActionPerformed

    private void addStageOneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStageOneButtonActionPerformed
        stageOneClicked = 1;
        addStageTwoTabTwo.setEnabled(true);
        stage_1_regs = new stageOneRegs();
        stage_1_regs.setVisible(true);
        stage_1_regs.updateAllVariables();
    }//GEN-LAST:event_addStageOneButtonActionPerformed

    private void IDvariableComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDvariableComboActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_IDvariableComboActionPerformed

    private void StageOneVariableComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StageOneVariableComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StageOneVariableComboActionPerformed

    private void LinearAssociationRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LinearAssociationRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LinearAssociationRadioActionPerformed

    private void saveStage2OutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveStage2OutButtonActionPerformed
        try {
            // TODO add your handling code here:

            saveStageTwoOutput();
        } catch (IOException ex) {
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveStage2OutButtonActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        saveStageOneOutput();
    }//GEN-LAST:event_jButton8ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Oyaha".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mixregGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mixregGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mixregGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mixregGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //imageViewr-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mixregGUI().setVisible(true);

            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> IDvariableCombo;
    private javax.swing.JRadioButton LinearAssociationRadio;
    private javax.swing.JRadioButton NoAssociationRadio;
    private javax.swing.JRadioButton QuadraticAssociationRadio;
    private javax.swing.JComboBox<String> StageOneVariableCombo;
    private javax.swing.JButton addStageOneButton;
    private javax.swing.JButton addStageTwoTabTwo;
    private javax.swing.JButton advancedOptionsButton;
    private javax.swing.JLabel associationLabel;
    private javax.swing.JPanel associationPanel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JMenuItem diagramMenu;
    private javax.swing.JMenuItem exitMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton goBackMxrButton;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JPanel imageView;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel level1_BSVar;
    private javax.swing.JLabel level1_MeanReg;
    private javax.swing.JLabel level1_WSVar;
    private javax.swing.JLabel level2_BSVar;
    private javax.swing.JLabel level2_MeanReg;
    private javax.swing.JLabel level2_WSVar;
    private javax.swing.JPanel levelOneGrid;
    private javax.swing.JPanel levelOnePanel;
    private javax.swing.JPanel levelTwoGrid;
    private javax.swing.JPanel levelTwoPanel;
    private javax.swing.JMenuItem modifyStageOneMenu;
    private javax.swing.JMenuItem modifyStageTwoMenu;
    private javax.swing.JMenuItem newModelMenu;
    private javax.swing.JPanel parentPanel;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton runTabTwoStageOneTwo;
    private javax.swing.JButton saveStage2OutButton;
    private javax.swing.JTextArea stageOneOutput;
    private javax.swing.JTabbedPane stageOneTabs;
    private javax.swing.JComboBox<String> stageTwoOutcome;
    private javax.swing.JTextArea stageTwoOutput;
    private javax.swing.JPanel stageTwoPanel;
    private javax.swing.JPanel stageTwoRegsGrid;
    private javax.swing.JButton startStageTwo;
    private javax.swing.JMenuItem superUserMenu;
    private javax.swing.JCheckBox suppressIntCheckBox;
    // End of variables declaration//GEN-END:variables

    public void isSubmitClicked() {

        parentPanel.removeAll();
        parentPanel.add(stageOneTabs);
        parentPanel.repaint();
        parentPanel.revalidate();
    }

    public void updateComboBoxes() {

        for (int j = 0; j < variableNamesCombo.length; j++) {
            IDList.addElement(variableNamesCombo[j]);
            StageOneList.addElement(variableNamesCombo[j]);
            StageTwoList.addElement(variableNamesCombo[j]);
        }

        IDvariableCombo.setModel(IDList);
        IDvariableCombo.setSelectedIndex(0);

        StageOneVariableCombo.setModel(StageOneList);
        StageOneVariableCombo.setSelectedIndex(1);

        stageTwoOutcome.setModel(StageTwoList);
        stageTwoOutcome.setSelectedIndex(2);

    }

    public static void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DefaultListModel<String> getSavedVariables() {

        savedVariablesStageOne = stage_1_regs.getListModel();

        return savedVariablesStageOne;

    }

    //get ID variable selected by the user
    public String getIDVariable() {
        String ID;

        ID = IDvariableCombo.getItemAt(IDvariableCombo.getSelectedIndex());

        return ID;

    }

    //get Stage One DV variable selected by the user
    public String getStageOneDV() {
        String StageOneDV;

        StageOneDV = StageOneVariableCombo.getItemAt(StageOneVariableCombo.getSelectedIndex());

        return StageOneDV;

    }

    //get Stage Two variable selected by the user
    public String getStageTwoDV() {
        String StageTwoDV;

        StageTwoDV = stageTwoOutcome.getItemAt(stageTwoOutcome.getSelectedIndex());

        return StageTwoDV;

    }

    public void updateLevelOneGrid_version2(DefaultListModel<String> defaultListModel) {

        levelOneSelected = new ArrayList<String>();

        JScrollPane scrollpanel = new JScrollPane(levelOneGrid);

        int regSize = defaultListModel.getSize();
        levelOneRegSize = regSize;
        levelOneDisaggSize = regSize;

        levelOneGrid.removeAll();

        levelOneGrid.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        //constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.NORTH;
        //constraints.gridwidth = 4;

        GridBagConstraints separatorConstraint = new GridBagConstraints();
        separatorConstraint.weightx = 1.0;
        separatorConstraint.fill = GridBagConstraints.HORIZONTAL;
        separatorConstraint.gridwidth = GridBagConstraints.REMAINDER;
        separatorConstraint.gridx = 0;

        constraints.insets = new Insets(3, 0, 5, 25);
        separatorConstraint.insets = new Insets(0, 0, 0, 0);
        //constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        levelOneBoxes = new ArrayList<>();
        disaggVarianceBoxes = new ArrayList<>();

        for (int j = 0; j < regSize; j++) {
            constraints.gridx = 0;
            constraints.anchor = GridBagConstraints.LINE_END;
            levelOneSelected.add(defaultListModel.getElementAt(j));
            levelOneGrid.add(new JLabel(levelOneSelected.get(j)), constraints);

            levelOneBoxes.add(j, new ArrayList<JCheckBox>());

            for (int k = 0; k < 3; k++) {
                int row = j;
                int column = k;

                constraints.gridx++;
                constraints.anchor = GridBagConstraints.CENTER;
                levelOneBoxes.get(j).add(k, new JCheckBox());
                levelOneGrid.add(levelOneBoxes.get(j).get(k), constraints);
                levelOneBoxes.get(j).get(k).addActionListener(actionListener);
                levelOneBoxes.get(j).get(k).addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        AbstractButton abstractButton = (AbstractButton) e.getSource();
                        boolean selected = abstractButton.getModel().isSelected();
                        if (selected) {
                            System.out.println("Checkbox selected");
                            disaggVarianceBoxes.get(row).get(column).setEnabled(true);
                            disaggVarianceBoxes.get(row).get(column).setSelected(false);
                            System.out.println(disaggVarianceBoxes.size());
                        } else {
                            disaggVarianceBoxes.get(row).get(column).setEnabled(false);
                            disaggVarianceBoxes.get(row).get(column).setSelected(false);
                        }

                    }
                });

            }

            constraints.gridy++;
            constraints.gridx = 0;
            constraints.anchor = GridBagConstraints.LINE_END;

            levelOneGrid.add(new JLabel("Disaggregate?"), constraints);
            disaggVarianceBoxes.add(j, new ArrayList<JCheckBox>());

            for (int k = 0; k < 3; k++) {
                constraints.gridx++;
                constraints.anchor = GridBagConstraints.CENTER;

                disaggVarianceBoxes.get(j).add(k, new JCheckBox());
                levelOneGrid.add(disaggVarianceBoxes.get(j).get(k), constraints);
                disaggVarianceBoxes.get(j).get(k).setEnabled(false);

            }

            constraints.gridy++;
            //constraints.gridx = 0;
            separatorConstraint.gridy = separatorConstraint.gridy + 3;
            //System.out.println("before seperator");
            levelOneGrid.add(new JSeparator(JSeparator.HORIZONTAL), separatorConstraint);
            //System.out.println("after seperator");
            constraints.gridy++;

        }

        
        levelOnePanel.removeAll();
        levelOnePanel.revalidate();
        levelOnePanel.repaint();
        
        levelOnePanel.add(scrollpanel);
        revalidate();

    }

    public void updateLevelTwoGrid_version2(DefaultListModel<String> defaultListModel) {

        //levelTwoGrid.setVisible(true);
        JScrollPane scrollpanel = new JScrollPane(levelTwoGrid);
        levelTwoSelected = new ArrayList<String>();

        int regSize = defaultListModel.getSize();
        levelTwoRegSize = regSize;

        levelTwoGrid.removeAll();

        levelTwoGrid.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        // constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTH;
        //constraints.gridwidth = 4;

        GridBagConstraints separatorConstraint = new GridBagConstraints();
        separatorConstraint.weightx = 1.0;
        separatorConstraint.fill = GridBagConstraints.HORIZONTAL;
        separatorConstraint.gridwidth = GridBagConstraints.REMAINDER;
        separatorConstraint.gridx = 0;

        constraints.insets = new Insets(3, 0, 5, 25);
        separatorConstraint.insets = new Insets(0, 0, 0, 0);
        //constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        levelTwoBoxes = new ArrayList<ArrayList<JCheckBox>>();
        //disaggVarianceBoxes = new ArrayList<ArrayList<JCheckBox>>();

        for (int j = 0; j < regSize; j++) {
            constraints.gridx = 0;
            constraints.anchor = GridBagConstraints.LINE_END;
            levelTwoSelected.add(defaultListModel.getElementAt(j));
            levelTwoGrid.add(new JLabel(levelTwoSelected.get(j)), constraints);
            //levelTwoGrid.add(new JLabel(defaultListModel.getElementAt(j)), constraints);

            levelTwoBoxes.add(j, new ArrayList<JCheckBox>());

            for (int k = 0; k < 3; k++) {

                constraints.gridx++;
                constraints.anchor = GridBagConstraints.CENTER;
                levelTwoBoxes.get(j).add(k, new JCheckBox());
                levelTwoGrid.add(levelTwoBoxes.get(j).get(k), constraints);
            }
            
            if (NewModel.isRandomScale){
                
                    levelTwoBoxes.get(j).get(1).setEnabled(false);
                
                }

            constraints.gridy++;

            separatorConstraint.gridy = separatorConstraint.gridy + 2;
            // System.out.println("before seperator");
            levelTwoGrid.add(new JSeparator(JSeparator.HORIZONTAL), separatorConstraint);
            // System.out.println("after seperator");
            constraints.gridy++;

        }

        levelTwoPanel.removeAll();
        levelTwoPanel.revalidate();
        levelTwoPanel.repaint();
        
        levelTwoPanel.add(scrollpanel);
        revalidate();

    }

    public void updateStageTwoGrid_tab2(DefaultListModel<String> defaultListModel) {

        JScrollPane scrollpanel = new JScrollPane(stageTwoRegsGrid);
        stageTwoSelected_tab2 = new ArrayList<String>();

        int regSize = defaultListModel.getSize();
        stageTwoRegSize = regSize;

        stageTwoRegsGrid.removeAll();

        stageTwoRegsGrid.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        // constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTHEAST;
        //constraints.gridwidth = 4;

        GridBagConstraints separatorConstraint = new GridBagConstraints();
        separatorConstraint.weightx = 1.0;
        separatorConstraint.fill = GridBagConstraints.HORIZONTAL;
        separatorConstraint.gridwidth = GridBagConstraints.REMAINDER;
        separatorConstraint.gridx = 0;

        constraints.insets = new Insets(3, 0, 5, 25);
        separatorConstraint.insets = new Insets(0, 0, 0, 0);
        constraints.weightx = 1;

        stageTwoGridBoxes = new ArrayList<ArrayList<JCheckBox>>();
        //disaggVarianceBoxes = new ArrayList<ArrayList<JCheckBox>>();

        for (int j = 0; j < regSize; j++) {
            int row = j;
            constraints.gridx = 1;
            constraints.anchor = GridBagConstraints.FIRST_LINE_START;
            stageTwoSelected_tab2.add(defaultListModel.getElementAt(j));
            stageTwoRegsGrid.add(new JLabel(stageTwoSelected_tab2.get(j)), constraints);

            //stageTwoGrid.add(new JLabel(defaultListModel.getElementAt(j)), constraints);
            stageTwoGridBoxes.add(j, new ArrayList<JCheckBox>());

            for (int k = 0; k < 4; k++) {
                
                
                if (k == 1){
                
                constraints.gridx = constraints.gridx + 5;
                    
                } else {
                constraints.gridx++;
                }

                
                constraints.anchor = GridBagConstraints.CENTER;
                stageTwoGridBoxes.get(j).add(k, new JCheckBox());

                stageTwoRegsGrid.add(stageTwoGridBoxes.get(j).get(k), constraints);  
            }

            constraints.gridy++;

            separatorConstraint.gridy = separatorConstraint.gridy + 2;

            stageTwoRegsGrid.add(new JSeparator(JSeparator.HORIZONTAL), separatorConstraint);
            //System.out.println("after seperator");
            constraints.gridy++;
            
            stageTwoGridBoxes.get(row).get(1).setEnabled(false);
            stageTwoGridBoxes.get(row).get(2).setEnabled(false);
            stageTwoGridBoxes.get(row).get(3).setEnabled(false);
            
            
            stageTwoGridBoxes.get(j).get(0).addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        AbstractButton abstractButton = (AbstractButton) e.getSource();
                        boolean selected = abstractButton.getModel().isSelected();
                        if (selected) {
                            System.out.println("Checkbox selected");
                            //disaggVarianceBoxes.get(row).get(column).setEnabled(true);
                            stageTwoGridBoxes.get(row).get(1).setEnabled(true);
                            stageTwoGridBoxes.get(row).get(1).setSelected(false);
                            stageTwoGridBoxes.get(row).get(2).setEnabled(true);
                            stageTwoGridBoxes.get(row).get(2).setSelected(false);
                            System.out.println(disaggVarianceBoxes.size());
                        } else {
                            //disaggVarianceBoxes.get(row).get(column).setEnabled(false);
                            stageTwoGridBoxes.get(row).get(1).setEnabled(false);
                            stageTwoGridBoxes.get(row).get(1).setSelected(false);
                            stageTwoGridBoxes.get(row).get(2).setEnabled(false);
                            stageTwoGridBoxes.get(row).get(2).setSelected(false);
                            stageTwoGridBoxes.get(row).get(3).setSelected(false);
                            stageTwoGridBoxes.get(row).get(3).setEnabled(false);
                        }

                    }
                });
                
                stageTwoGridBoxes.get(j).get(1).addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        AbstractButton abstractButton = (AbstractButton) e.getSource();
                        boolean selected = abstractButton.getModel().isSelected();
                        if (selected){
                            scaleChecked = true;
                            
                            if (randomChecked == true){
                            stageTwoGridBoxes.get(row).get(3).setEnabled(true);
                            stageTwoGridBoxes.get(row).get(3).setSelected(false);
                            }
                            
                            
                        } else {
                            scaleChecked = false;
                            stageTwoGridBoxes.get(row).get(3).setEnabled(false);
                            stageTwoGridBoxes.get(row).get(3).setSelected(false);
                        }
                        
                       /* if (selected) {
                            System.out.println("Checkbox selected");
                            //disaggVarianceBoxes.get(row).get(column).setEnabled(true);
                            stageTwoGridBoxes.get(row).get(1).setEnabled(true);
                            stageTwoGridBoxes.get(row).get(2).setEnabled(true);
                            System.out.println(disaggVarianceBoxes.size());
                        } else {
                            //disaggVarianceBoxes.get(row).get(column).setEnabled(false);
                            stageTwoGridBoxes.get(row).get(1).setEnabled(false);
                            stageTwoGridBoxes.get(row).get(2).setEnabled(false);
                        }*/

                    }
                });
                
                stageTwoGridBoxes.get(j).get(2).addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        AbstractButton abstractButton = (AbstractButton) e.getSource();
                        boolean selected = abstractButton.getModel().isSelected();
                        
                        if (selected){
                            randomChecked = true;
                            
                            if (scaleChecked == true){
                            stageTwoGridBoxes.get(row).get(3).setEnabled(true);
                            stageTwoGridBoxes.get(row).get(3).setSelected(false);
                            }
                            
                        } else {
                            randomChecked = false;
                            stageTwoGridBoxes.get(row).get(3).setEnabled(false);
                            stageTwoGridBoxes.get(row).get(3).setSelected(false);
                        
                        }
                        
                       /* if (selected) {
                            System.out.println("Checkbox selected");
                            //disaggVarianceBoxes.get(row).get(column).setEnabled(true);
                            stageTwoGridBoxes.get(row).get(1).setEnabled(true);
                            stageTwoGridBoxes.get(row).get(2).setEnabled(true);
                            System.out.println(disaggVarianceBoxes.size());
                        } else {
                            //disaggVarianceBoxes.get(row).get(column).setEnabled(false);
                            stageTwoGridBoxes.get(row).get(1).setEnabled(false);
                            stageTwoGridBoxes.get(row).get(2).setEnabled(false);
                        }*/

                    }
                });

        }

        stageTwoPanel.removeAll();
        stageTwoPanel.revalidate();
        stageTwoPanel.repaint();
        
        stageTwoPanel.add(scrollpanel);
        revalidate();

    }
    
    public void updateStageTwoGrid_tab2v2(DefaultListModel<String> defaultListModel) {
    
    //todo: create two grid layouts here or try with a sep
    }

    public int countLevelOneBeta() {

        int levelOneBeta = 0;

        for (int p = 0; p < levelOneRegSize; p++) {

            if (levelOneBoxes.get(p).get(0).isSelected()) {

                levelOneBeta = levelOneBeta + 1;
            }
        }

        return levelOneBeta;

    }

    public int countLevelOneDicompMean() {
        int levelOneDisagg = 0;

        for (int p = 0; p < levelOneRegSize; p++) {

            if (disaggVarianceBoxes.get(p).get(0).isSelected()) {
                levelOneDisagg = levelOneDisagg + 1;
            }

        }

        return levelOneDisagg;
    }

    public int countLevelOneDicompBS() {
        int levelOneDisagg = 0;

        for (int p = 0; p < levelOneRegSize; p++) {

            if (disaggVarianceBoxes.get(p).get(1).isSelected()) {
                levelOneDisagg = levelOneDisagg + 1;
            }

        }

        return levelOneDisagg;
    }

    public int countLevelOneDicompWS() {
        int levelOneDisagg = 0;

        for (int p = 0; p < levelOneRegSize; p++) {

            if (disaggVarianceBoxes.get(p).get(2).isSelected()) {
                levelOneDisagg = levelOneDisagg + 1;
            }

        }

        return levelOneDisagg;
    }

    public int countLevelTwoBeta() {

        int levelTwoBeta = 0;

        for (int p = 0; p < levelTwoRegSize; p++) {

            if (levelTwoBoxes.get(p).get(0).isSelected()) {

                levelTwoBeta = levelTwoBeta + 1;
            }
        }

        return levelTwoBeta;

    }

    //setStageTwoFixedCount()
    public int countStageTwoBeta() {

        int stageTwoBeta = 0;

        for (int p = 0; p < stageTwoRegSize; p++) {

            if (stageTwoGridBoxes.get(p).get(0).isSelected()) {

                stageTwoBeta = stageTwoBeta + 1;
            }
        }

        return stageTwoBeta;

    }

    public int countLevelOneAlpha() {

        int levelOneAlpha = 0;

        for (int p = 0; p < levelOneRegSize; p++) {

            if (levelOneBoxes.get(p).get(1).isSelected()) {

                levelOneAlpha = levelOneAlpha + 1;
            }
        }

        return levelOneAlpha;

    }

    public int countLevelTwoAlpha() {

        int levelTwoAlpha = 0;

        for (int p = 0; p < levelTwoRegSize; p++) {

            if (levelTwoBoxes.get(p).get(1).isSelected()) {

                levelTwoAlpha = levelTwoAlpha + 1;
            }
        }

        return levelTwoAlpha;

    }

    //setStageTwoLocRanInteractions
    public int countStageTwoAlpha() {

        int stageTwoAlpha = 0;

        for (int p = 0; p < stageTwoRegSize; p++) {

            if (stageTwoGridBoxes.get(p).get(1).isSelected()) {

                stageTwoAlpha = stageTwoAlpha + 1;
            }
        }

        return stageTwoAlpha;

    }

    public int countLevelOneTau() {

        int levelOneTau = 0;

        for (int p = 0; p < levelOneRegSize; p++) {

            if (levelOneBoxes.get(p).get(2).isSelected()) {

                levelOneTau = levelOneTau + 1;
            }
        }

        return levelOneTau;

    }

    public int countLevelTwoTau() {

        int levelTwoTau = 0;

        for (int p = 0; p < levelTwoRegSize; p++) {

            if (levelTwoBoxes.get(p).get(2).isSelected()) {

                levelTwoTau = levelTwoTau + 1;
            }
        }

        return levelTwoTau;

    }

    //setStageTwoScaleInteractions
    public int countStageTwoTau() {

        int stageTwoTau = 0;

        for (int p = 0; p < stageTwoRegSize; p++) {

            if (stageTwoGridBoxes.get(p).get(2).isSelected()) {

                stageTwoTau = stageTwoTau + 1;
            }
        }

        return stageTwoTau;

    }

    public int countStageTwoInteractions() {

        int stageTwoInter = 0;

        for (int p = 0; p < stageTwoRegSize; p++) {

            if (stageTwoGridBoxes.get(p).get(3).isSelected()) {

                stageTwoInter = stageTwoInter + 1;
            }
        }

        return stageTwoInter;

    }
    
    public void disableLevelTwoRandomLocation(){
    
    
    
    
    }

    public String[] getMeanFieldRegressorLabels_levelOne() {
        System.out.println("*********************************");
        System.out.println("Mean-positions From level 1 (positions)");

        String fieldLabel;

        String[] regressorLabels = new String[levelOneRegSize];
        int index = 0;

        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < levelOneRegSize; p++) {

            if (levelOneBoxes.get(p).get(0).isSelected() && !disaggVarianceBoxes.get(p).get(0).isSelected()) {
                regressorLabels[index] = levelOneSelected.get(p);
                fieldLabel = levelOneSelected.get(p);
                System.out.println("From inside mixRegGUI | Level One Regressor Fields (Mean): " + regressorLabels[index]);
                index++;

                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo.length; q++) {

                    if (variableNamesCombo[q].equals(fieldLabel)) {
                        //position[index] = String.valueOf(q + 1);
                        position.add(posIndex, String.valueOf(q + 1));
                        System.out.println("Regressor position test: " + String.valueOf(q + 1));
                        System.out.println("From inside mixRegGUI | Position of this regressor: " + position.get(posIndex));
                        System.out.println("Position array: " + position);
                        posIndex++;

                    }

                }
            }

        }
        System.out.println("Position Aray Size here: " + String.valueOf(position.size()));

        String[] positionArray = new String[position.size()];

        for (int pos = 0; pos < positionArray.length; pos++) {
            positionArray[pos] = position.get(pos);
            System.out.println("positionArrayElements: " + positionArray[pos]);

        }

        System.out.println("Converted array size | position: " + String.valueOf(positionArray.length));
        System.out.println("Converted array elements | positions: " + Arrays.toString(positionArray));
        System.out.println("*********************************");

        return positionArray;

    }

    public String[] getMeanFieldRegressorLabels_levelTwo() {
        System.out.println("*********************************");
        System.out.println("Mean-positions From level 2 (positions)");

        String fieldLabel;

        String[] regressorLabels = new String[levelTwoRegSize];
        int index = 0;

        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < levelTwoRegSize; p++) {

            if (levelTwoBoxes.get(p).get(0).isSelected()) {
                regressorLabels[index] = levelTwoSelected.get(p);
                fieldLabel = levelTwoSelected.get(p);
                System.out.println("From inside mixRegGUI | Level Two Regressor Fields (Mean) level2: " + regressorLabels[index]);
                index++;

                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo.length; q++) {

                    if (variableNamesCombo[q].equals(fieldLabel)) {
                        //position[index] = String.valueOf(q + 1);
                        position.add(posIndex, String.valueOf(q + 1));
                        System.out.println("Regressor position test: " + String.valueOf(q + 1));
                        System.out.println("From inside mixRegGUI | Position of this regressor in level2: " + position.get(posIndex));
                        System.out.println("Position array: " + position);
                        posIndex++;

                    }

                }
            }

        }
        System.out.println("Position Aray Size here: " + String.valueOf(position.size()));

        String[] positionArray = new String[position.size()];

        for (int pos = 0; pos < positionArray.length; pos++) {
            positionArray[pos] = position.get(pos);
            System.out.println("positionArrayElements: " + positionArray[pos]);

        }

        System.out.println("Converted array size | position: " + String.valueOf(positionArray.length));
        System.out.println("Converted array elements | positions: " + Arrays.toString(positionArray));
        System.out.println("*********************************");

        return positionArray;
    }

    public String[] fieldModelMeanArray() {
        System.out.println("*********************************");
        System.out.println("Mean-positions From lstage 1 (positions)");

        int arraySize = getMeanFieldRegressorLabels_levelOne().length + getMeanFieldRegressorLabels_levelTwo().length;
        String[] meanModel = new String[arraySize];

        for (int pos = 0; pos < arraySize; pos++) {
            if (pos >= 0 && pos < getMeanFieldRegressorLabels_levelOne().length) {
                meanModel[pos] = getMeanFieldRegressorLabels_levelOne()[pos];

            } else if (pos >= getMeanFieldRegressorLabels_levelOne().length && pos < arraySize) {

                meanModel[pos] = getMeanFieldRegressorLabels_levelTwo()[pos - getMeanFieldRegressorLabels_levelOne().length];
            }

        }

        System.out.println("Inside mixRegGUI | Mean-nModel STAGE 1: " + Arrays.toString(meanModel));
        System.out.println("Inside mixRegGui | Mean-Model STAGE 1 Size: " + meanModel.length);
        System.out.println("*********************************");

        return meanModel;
    }

    public String[] getMeanDecompFieldRegressorLabels_levelOne() {

        System.out.println("*********************************");
        System.out.println("Mean+Disagg.-positions From level 1 (positions)");

        String fieldLabel;

        String[] regressorLabels = new String[levelOneRegSize];
        int index = 0;

        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < levelOneRegSize; p++) {

            if (levelOneBoxes.get(p).get(0).isSelected() && disaggVarianceBoxes.get(p).get(0).isSelected()) {
                regressorLabels[index] = levelOneSelected.get(p);
                fieldLabel = levelOneSelected.get(p);
                System.out.println("From inside mixRegGUI | Level One Regressor Fields (Mean + Disagg.): " + regressorLabels[index]);
                index++;

                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo.length; q++) {

                    if (variableNamesCombo[q].equals(fieldLabel)) {
                        //position[index] = String.valueOf(q + 1);
                        position.add(posIndex, String.valueOf(q + 1));
                        System.out.println("Regressor position test: " + String.valueOf(q + 1));
                        System.out.println("From inside mixRegGUI | Position of this regressor: " + position.get(posIndex));
                        System.out.println("Position array: " + position);
                        posIndex++;

                    }

                }
            }

        }
        System.out.println("Position Aray Size here: " + String.valueOf(position.size()));

        String[] positionArray = new String[position.size()];

        for (int pos = 0; pos < positionArray.length; pos++) {
            positionArray[pos] = position.get(pos);
            System.out.println("positionArrayElements: " + positionArray[pos]);

        }

        System.out.println("Converted array size | position: " + String.valueOf(positionArray.length));
        System.out.println("Converted array elements | positions: " + Arrays.toString(positionArray));
        System.out.println("*********************************");

        return positionArray;

    }

    public String[] getBSFieldRegressorLabels_levelOne() {
        System.out.println("*********************************");
        System.out.println("BS-positions From level 1 (positions)");

        String fieldLabel;

        String[] regressorLabels = new String[levelOneRegSize];
        int index = 0;
        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < levelOneRegSize; p++) {

            if (levelOneBoxes.get(p).get(1).isSelected() && !disaggVarianceBoxes.get(p).get(1).isSelected()) {
                regressorLabels[index] = levelOneSelected.get(p);
                fieldLabel = levelOneSelected.get(p);
                System.out.println("From inside mixRegGUI | Level One Regressor Fields (BS + Disagg.): " + regressorLabels[index]);
                index++;
                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo.length; q++) {

                    if (variableNamesCombo[q].equals(fieldLabel)) {
                        position.add(posIndex, String.valueOf(q + 1));
                        System.out.println("Regressor position test: " + String.valueOf(q + 1));
                        System.out.println("From inside mixRegGUI | Position of this regressor: " + position.get(posIndex));
                        System.out.println("Position array: " + position);
                        posIndex++;

                    }
                }
            }
        }
        System.out.println("Position Aray Size here: " + String.valueOf(position.size()));

        String[] positionArray = new String[position.size()];

        for (int pos = 0; pos < positionArray.length; pos++) {
            positionArray[pos] = position.get(pos);
            System.out.println("positionArrayElements: " + positionArray[pos]);

        }

        System.out.println("Converted array size | position: " + String.valueOf(positionArray.length));
        System.out.println("Converted array elements | positions: " + Arrays.toString(positionArray));
        System.out.println("*********************************");

        return positionArray;

    }

    public String[] getBSFieldRegressorLabels_levelTwo() {

        System.out.println("*********************************");
        System.out.println("BS-positions From level 2 (positions)");

        String fieldLabel;

        String[] regressorLabels = new String[levelTwoRegSize];
        int index = 0;
        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < levelTwoRegSize; p++) {

            if (levelTwoBoxes.get(p).get(1).isSelected()) {
                regressorLabels[index] = levelTwoSelected.get(p);
                fieldLabel = levelTwoSelected.get(p);
                System.out.println("From inside mixRegGUI | Level two Regressor Fields level 2 (BS + Disagg.): " + regressorLabels[index]);
                index++;
                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo.length; q++) {

                    if (variableNamesCombo[q].equals(fieldLabel)) {
                        position.add(posIndex, String.valueOf(q + 1));
                        System.out.println("Regressor position test: " + String.valueOf(q + 1));
                        System.out.println("From inside mixRegGUI | Position of this regressor: " + position.get(posIndex));
                        System.out.println("Position array: " + position);
                        posIndex++;

                    }
                }
            }
        }
        System.out.println("Position Aray Size here: " + String.valueOf(position.size()));

        String[] positionArray = new String[position.size()];

        for (int pos = 0; pos < positionArray.length; pos++) {
            positionArray[pos] = position.get(pos);
            System.out.println("positionArrayElements: " + positionArray[pos]);

        }

        System.out.println("Converted array size | position: " + String.valueOf(positionArray.length));
        System.out.println("Converted array elements | positions: " + Arrays.toString(positionArray));
        System.out.println("*********************************");

        return positionArray;

    }

    public String[] fieldModelBSArray() {

        System.out.println("*********************************");
        System.out.println("BS-positions From stage 1 (positions)");

        int arraySize = getBSFieldRegressorLabels_levelOne().length + getBSFieldRegressorLabels_levelTwo().length;
        String[] meanModel = new String[arraySize];

        for (int pos = 0; pos < arraySize; pos++) {
            if (pos >= 0 && pos < getBSFieldRegressorLabels_levelOne().length) {
                meanModel[pos] = getBSFieldRegressorLabels_levelOne()[pos];

            } else if (pos >= getBSFieldRegressorLabels_levelOne().length && pos < arraySize) {

                meanModel[pos] = getBSFieldRegressorLabels_levelTwo()[pos - getBSFieldRegressorLabels_levelOne().length];
            }

        }

        System.out.println("Inside mixRegGUI | BS-Model STAGE 1: " + Arrays.toString(meanModel));
        System.out.println("Inside mixRegGui | BS-Model STAGE 1 Size: " + meanModel.length);
        System.out.println("*********************************");

        return meanModel;
    }

    public String[] getBSDecompFieldRegressorLabels_levelOne() {

        System.out.println("*********************************");
        System.out.println("BS+Disagg-positions From level 1 (positions)");

        String fieldLabel;

        String[] regressorLabels = new String[levelOneRegSize];
        int index = 0;
        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < levelOneRegSize; p++) {

            if (levelOneBoxes.get(p).get(1).isSelected() && disaggVarianceBoxes.get(p).get(1).isSelected()) {
                regressorLabels[index] = levelOneSelected.get(p);
                fieldLabel = levelOneSelected.get(p);
                System.out.println("From inside mixRegGUI | Level One Regressor Fields (BS): " + regressorLabels[index]);
                index++;
                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo.length; q++) {

                    if (variableNamesCombo[q].equals(fieldLabel)) {
                        position.add(posIndex, String.valueOf(q + 1));
                        System.out.println("Regressor position test: " + String.valueOf(q + 1));
                        System.out.println("From inside mixRegGUI | Position of this regressor: " + position.get(posIndex));
                        System.out.println("Position array: " + position);
                        posIndex++;

                    }
                }
            }
        }
        System.out.println("Position Aray Size here: " + String.valueOf(position.size()));

        String[] positionArray = new String[position.size()];

        for (int pos = 0; pos < positionArray.length; pos++) {
            positionArray[pos] = position.get(pos);
            System.out.println("positionArrayElements: " + positionArray[pos]);

        }

        System.out.println("Converted array size | position: " + String.valueOf(positionArray.length));
        System.out.println("Converted array elements | positions: " + Arrays.toString(positionArray));
        System.out.println("*********************************");

        return positionArray;

    }

    public String[] getWSFieldRegressorLabels_levelOne() {
        System.out.println("*********************************");
        System.out.println("WS-positions From level 1 (positions)");

        String[] regressorLabels = new String[levelOneRegSize];
        String fieldLabel;

        int index = 0;
        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < levelOneRegSize; p++) {

            if (levelOneBoxes.get(p).get(2).isSelected() && !disaggVarianceBoxes.get(p).get(2).isSelected()) {
                regressorLabels[index] = levelOneSelected.get(p);
                fieldLabel = levelOneSelected.get(p);
                System.out.println("From inside mixRegGUI | Level one Regressor Fields (WS): " + regressorLabels[index]);
                index++;
                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo.length; q++) {

                    if (variableNamesCombo[q].equals(fieldLabel)) {
                        position.add(posIndex, String.valueOf(q + 1));
                        System.out.println("Regressor position test: " + String.valueOf(q + 1));
                        System.out.println("From inside mixRegGUI | Position of this regressor: " + position.get(posIndex));
                        System.out.println("Position array: " + position);
                        posIndex++;

                    }
                }
            }

        }

        System.out.println("Position Aray Size here: " + String.valueOf(position.size()));

        String[] positionArray = new String[position.size()];

        for (int pos = 0; pos < positionArray.length; pos++) {
            positionArray[pos] = position.get(pos);
            System.out.println("positionArrayElements: " + positionArray[pos]);

        }

        System.out.println("Converted array size | position: " + String.valueOf(positionArray.length));
        System.out.println("Converted array elements | positions: " + Arrays.toString(positionArray));
        System.out.println("*********************************");

        return positionArray;

    }

    public String[] getWSFieldRegressorLabels_levelTwo() {
        System.out.println("*********************************");
        System.out.println("WS-positions From level 2 (positions)");

        String[] regressorLabels = new String[levelTwoRegSize];
        String fieldLabel;

        int index = 0;
        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < levelTwoRegSize; p++) {

            if (levelTwoBoxes.get(p).get(2).isSelected()) {
                regressorLabels[index] = levelTwoSelected.get(p);
                fieldLabel = levelTwoSelected.get(p);
                System.out.println("From inside mixRegGUI | Level 2 Regressor Fields (WS): " + regressorLabels[index]);
                index++;
                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo.length; q++) {

                    if (variableNamesCombo[q].equals(fieldLabel)) {
                        position.add(posIndex, String.valueOf(q + 1));
                        System.out.println("Regressor position test: " + String.valueOf(q + 1));
                        System.out.println("From inside mixRegGUI | Position of this regressor: " + position.get(posIndex));
                        System.out.println("Position array: " + position);
                        posIndex++;

                    }
                }
            }

        }

        System.out.println("Position Aray Size here: " + String.valueOf(position.size()));

        String[] positionArray = new String[position.size()];

        for (int pos = 0; pos < positionArray.length; pos++) {
            positionArray[pos] = position.get(pos);
            System.out.println("positionArrayElements: " + positionArray[pos]);

        }

        System.out.println("Converted array size | position: " + String.valueOf(positionArray.length));
        System.out.println("Converted array elements | positions: " + Arrays.toString(positionArray));
        System.out.println("*********************************");

        return positionArray;

    }

    public String[] fieldModelWSArray() {
        System.out.println("*********************************");
        System.out.println("WS-positions From stage 1 (positions)");

        int arraySize = getWSFieldRegressorLabels_levelOne().length + getWSFieldRegressorLabels_levelTwo().length;
        String[] meanModel = new String[arraySize];

        for (int pos = 0; pos < arraySize; pos++) {
            if (pos >= 0 && pos < getWSFieldRegressorLabels_levelOne().length) {
                meanModel[pos] = getWSFieldRegressorLabels_levelOne()[pos];

            } else if (pos >= getWSFieldRegressorLabels_levelOne().length && pos < arraySize) {

                meanModel[pos] = getWSFieldRegressorLabels_levelTwo()[pos - getWSFieldRegressorLabels_levelOne().length];
            }

        }

        System.out.println("Inside mixRegGUI | WS-Model STAGE 1: " + Arrays.toString(meanModel));
        System.out.println("Inside mixRegGui | WS-Model STAGE 1 Size: " + meanModel.length);
        System.out.println("*********************************");

        return meanModel;
    }

    public String[] getWSDecompFieldRegressorLabels_levelOne() {

        System.out.println("*********************************");
        System.out.println("WS+Disagg-positions From level 1 (positions)");
        String[] regressorLabels = new String[levelOneRegSize];
        String fieldLabel;

        int index = 0;
        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < levelOneRegSize; p++) {

            if (levelOneBoxes.get(p).get(2).isSelected() && disaggVarianceBoxes.get(p).get(2).isSelected()) {
                regressorLabels[index] = levelOneSelected.get(p);
                fieldLabel = levelOneSelected.get(p);
                System.out.println("From inside mixRegGUI | Level One Regressor Fields (WS): " + regressorLabels[index]);
                index++;
                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo.length; q++) {

                    if (variableNamesCombo[q].equals(fieldLabel)) {
                        position.add(posIndex, String.valueOf(q + 1));
                        System.out.println("Regressor position test: " + String.valueOf(q + 1));
                        System.out.println("From inside mixRegGUI | Position of this regressor: " + position.get(posIndex));
                        System.out.println("Position array: " + position);
                        posIndex++;

                    }
                }
            }

        }

        System.out.println("Position Aray Size here: " + String.valueOf(position.size()));

        String[] positionArray = new String[position.size()];

        for (int pos = 0; pos < positionArray.length; pos++) {
            positionArray[pos] = position.get(pos);
            System.out.println("positionArrayElements: " + positionArray[pos]);

        }

        System.out.println("Converted array size | position: " + String.valueOf(positionArray.length));
        System.out.println("Converted array elements | positions: " + Arrays.toString(positionArray));
        System.out.println("*********************************");

        return positionArray;

    }

    public String[] getModelMeanLabelsLevelOne() {
        System.out.println("*********************************");
        System.out.println("Means-Labels From level 1 (Labels)");

        String fieldLabel;

        ArrayList<String> regressorLabels = new ArrayList<String>();

        int index = 0;

        for (int p = 0; p < levelOneRegSize; p++) {

            if (levelOneBoxes.get(p).get(0).isSelected() && !disaggVarianceBoxes.get(p).get(0).isSelected()) {

                regressorLabels.add(levelOneSelected.get(p));
                fieldLabel = levelOneSelected.get(p);
                System.out.println("From inside mixRegGUI | LEVEL ONE Regressor Fields (Mean): " + regressorLabels.get(index));
                index++;
            }
        }

        String[] regLabels = new String[regressorLabels.size()];

        for (int pos = 0; pos < regLabels.length; pos++) {
            regLabels[pos] = regressorLabels.get(pos);
            System.out.println("Reg_LABEL: " + regLabels[pos]);

        }

        System.out.println("From inside mixRegGUI | LEVEL ONE MEAN REGRESSORS: " + Arrays.toString(regLabels));
        System.out.println("*********************************");
        return regLabels;
    }

    public String[] getModelMeanLabelsLevelTwo() {
        System.out.println("*********************************");
        System.out.println("Means-Labels From level 2 (Labels)");

        String fieldLabel;

        ArrayList<String> regressorLabels = new ArrayList<String>();
        int index = 0;

        for (int p = 0; p < levelTwoRegSize; p++) {

            if (levelTwoBoxes.get(p).get(0).isSelected()) {
                regressorLabels.add(levelTwoSelected.get(p));
                fieldLabel = levelTwoSelected.get(p);
                System.out.println("From inside mixRegGUI | LEVEL TWO Regressor Fields (Mean): " + regressorLabels.get(index));
                index++;

            }
        }

        String[] regLabels = new String[regressorLabels.size()];

        for (int pos = 0; pos < regLabels.length; pos++) {
            regLabels[pos] = regressorLabels.get(pos);
            System.out.println("Reg_LABEL: " + regLabels[pos]);

        }

        System.out.println("From inside mixRegGUI | LEVEL TWO MEAN REGRESSORS: " + Arrays.toString(regLabels));
        System.out.println("*********************************");
        return regLabels;
    }

    public String[] ModelMeansLabelsArray() {
        System.out.println("*********************************");
        System.out.println("Means-Labels From stage 1 (Labels)");

        int arraySize = getModelMeanLabelsLevelOne().length + getModelMeanLabelsLevelTwo().length;
        String[] meanModel = new String[arraySize];

        for (int pos = 0; pos < arraySize; pos++) {
            if (pos >= 0 && pos < getModelMeanLabelsLevelOne().length) {
                meanModel[pos] = getModelMeanLabelsLevelOne()[pos];

            } else if (pos >= getModelMeanLabelsLevelOne().length && pos < arraySize) {

                meanModel[pos] = getModelMeanLabelsLevelTwo()[pos - getModelMeanLabelsLevelOne().length];
            }

        }

        System.out.println("Inside mixRegGUI | Means-Model STAGE 1 labels: " + Arrays.toString(meanModel));
        System.out.println("*********************************");

        return meanModel;
    }

    public String[] getModelBSLabelsLevelOne() {
        System.out.println("*********************************");
        System.out.println("BS-Labels From level 1 (Labels)");

        String fieldLabel;

        ArrayList<String> regressorLabels = new ArrayList<String>();
        int index = 0;

        for (int p = 0; p < levelOneRegSize; p++) {

            if (levelOneBoxes.get(p).get(1).isSelected() && !disaggVarianceBoxes.get(p).get(1).isSelected()) {
                regressorLabels.add(levelOneSelected.get(p));
                fieldLabel = levelOneSelected.get(p);
                System.out.println("From inside mixRegGUI | LEVEL ONE Regressor Fields (BS): " + regressorLabels.get(index));
                index++;

            }
        }

        String[] regLabels = new String[regressorLabels.size()];

        for (int pos = 0; pos < regLabels.length; pos++) {
            regLabels[pos] = regressorLabels.get(pos);
            System.out.println("Reg_LABEL: " + regLabels[pos]);

        }

        System.out.println("From inside mixRegGUI | LEVEL ONE BS REGRESSORS: " + Arrays.toString(regLabels));
        System.out.println("*********************************");
        return regLabels;
    }

    public String[] getModelBSLabelsLevelTwo() {
        System.out.println("*********************************");
        System.out.println("BS-Labels From level 2 (Labels)");

        ArrayList<String> regressorLabels = new ArrayList<String>();
        int index = 0;

        for (int p = 0; p < levelTwoRegSize; p++) {

            if (levelTwoBoxes.get(p).get(1).isSelected()) {
                regressorLabels.add(levelTwoSelected.get(p));

                System.out.println("From inside mixRegGUI | LEVEL TWO Regressor Fields (BS): " + regressorLabels.get(index));
                index++;

            }
        }

        String[] regLabels = new String[regressorLabels.size()];

        for (int pos = 0; pos < regLabels.length; pos++) {
            regLabels[pos] = regressorLabels.get(pos);
            System.out.println("Reg_LABEL: " + regLabels[pos]);

        }

        System.out.println("From inside mixRegGUI | LEVEL TWO BS REGRESSORS: " + Arrays.toString(regLabels));
        System.out.println("*********************************");
        return regLabels;
    }

    public String[] ModelBSLabelsArray() {
        System.out.println("*********************************");
        System.out.println("BS-Labels From stage 1 (Labels)");

        int arraySize = getModelBSLabelsLevelOne().length + getModelBSLabelsLevelTwo().length;
        String[] meanModel = new String[arraySize];

        for (int pos = 0; pos < arraySize; pos++) {
            if (pos >= 0 && pos < getModelBSLabelsLevelOne().length) {
                meanModel[pos] = getModelBSLabelsLevelOne()[pos];

            } else if (pos >= getModelBSLabelsLevelOne().length && pos < arraySize) {

                meanModel[pos] = getModelBSLabelsLevelTwo()[pos - getModelBSLabelsLevelOne().length];
            }

        }

        System.out.println("Inside mixRegGUI | BS-Model STAGE 1 labels: " + Arrays.toString(meanModel));
        System.out.println("*********************************");

        return meanModel;
    }

    public String[] getModelWSLabelsLevelOne() {
        System.out.println("*********************************");
        System.out.println("WS-Labels From Level one (Labels)");

        ArrayList<String> regressorLabels = new ArrayList<String>();
        int index = 0;

        for (int p = 0; p < levelOneRegSize; p++) {

            if (levelOneBoxes.get(p).get(2).isSelected() && !disaggVarianceBoxes.get(p).get(2).isSelected()) {
                regressorLabels.add(levelOneSelected.get(p));
                System.out.println("From inside mixRegGUI | LEVEL ONE Regressor Fields (WS): " + regressorLabels.get(index));
                index++;

            }
        }

        String[] regLabels = new String[regressorLabels.size()];

        for (int pos = 0; pos < regLabels.length; pos++) {
            regLabels[pos] = regressorLabels.get(pos);
            System.out.println("Reg_LABEL: " + regLabels[pos]);

        }

        System.out.println("From inside mixRegGUI | LEVEL ONE WS REGRESSORS: " + Arrays.toString(regLabels));
        System.out.println("*********************************");
        return regLabels;
    }

    public String[] getModelWSLabelsLevelTwo() {
        System.out.println("*********************************");
        System.out.println("WS-Labels From Level two (Labels)");

        ArrayList<String> regressorLabels = new ArrayList<String>();
        int index = 0;

        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < levelTwoRegSize; p++) {

            if (levelTwoBoxes.get(p).get(2).isSelected()) {
                regressorLabels.add(levelOneSelected.get(p));
                System.out.println("From inside mixRegGUI | LEVEL TWO Regressor Fields (WS): " + regressorLabels.get(index));
                index++;

            }
        }

        String[] regLabels = new String[regressorLabels.size()];

        for (int pos = 0; pos < regLabels.length; pos++) {
            regLabels[pos] = regressorLabels.get(pos);
            System.out.println("Reg_LABEL: " + regLabels[pos]);

        }

        System.out.println("From inside mixRegGUI | LEVEL TWO WS REGRESSORS: " + Arrays.toString(regLabels));
        System.out.println("*********************************");
        return regLabels;
    }

    public String[] ModelWSLabelsArray() {
        System.out.println("*********************************");
        System.out.println("WS Labels From stage one (Labels)");

        int arraySize = getModelWSLabelsLevelOne().length + getModelWSLabelsLevelTwo().length;
        String[] meanModel = new String[arraySize];

        for (int pos = 0; pos < arraySize; pos++) {
            if (pos >= 0 && pos < getModelWSLabelsLevelOne().length) {
                meanModel[pos] = getModelWSLabelsLevelOne()[pos];

            } else if (pos >= getModelWSLabelsLevelOne().length && pos < arraySize) {

                meanModel[pos] = getModelWSLabelsLevelTwo()[pos - getModelWSLabelsLevelOne().length];
            }

        }

        System.out.println("Inside mixRegGUI | WS-Model STAGE 1 labels: " + Arrays.toString(meanModel));
        System.out.println("*********************************");

        return meanModel;
    }

    public String[] getDecompMeanLabelsLevelOne() {
        System.out.println("*********************************");
        System.out.println("Mean+Disagg. Labels From Level one (Labels)");

        ArrayList<String> regressorLabels = new ArrayList<String>();

        int index = 0;

        for (int p = 0; p < levelOneRegSize; p++) {

            if (levelOneBoxes.get(p).get(0).isSelected() && disaggVarianceBoxes.get(p).get(0).isSelected()) {

                regressorLabels.add(levelOneSelected.get(p));
                System.out.println("From inside mixRegGUI | LEVEL ONE Regressor Fields (Mean + Disagg): " + regressorLabels.get(index));
                index++;
            }
        }

        String[] regLabels = new String[regressorLabels.size()];

        for (int pos = 0; pos < regLabels.length; pos++) {
            regLabels[pos] = regressorLabels.get(pos);
            System.out.println("Reg_LABEL: " + regLabels[pos]);

        }

        System.out.println("From inside mixRegGUI | LEVEL ONE MEAN + DISAGG REGRESSORS: " + Arrays.toString(regLabels));
        System.out.println("*********************************");
        return regLabels;
    }

    public String[] getDecompBSLabelsLevelOne() {
        System.out.println("*********************************");
        System.out.println("BS+Disagg. Labels From Level one (Labels)");

        ArrayList<String> regressorLabels = new ArrayList<String>();

        int index = 0;

        for (int p = 0; p < levelOneRegSize; p++) {

            if (levelOneBoxes.get(p).get(1).isSelected() && disaggVarianceBoxes.get(p).get(1).isSelected()) {

                regressorLabels.add(levelOneSelected.get(p));
                System.out.println("From inside mixRegGUI | LEVEL ONE Regressor Fields (Mean + Disagg.): " + regressorLabels.get(index));
                index++;
            }
        }

        String[] regLabels = new String[regressorLabels.size()];

        for (int pos = 0; pos < regLabels.length; pos++) {
            regLabels[pos] = regressorLabels.get(pos);
            System.out.println("Reg_LABEL: " + regLabels[pos]);

        }

        System.out.println("From inside mixRegGUI | LEVEL ONE BS + DISAGG REGRESSORS: " + Arrays.toString(regLabels));
        System.out.println("*********************************");
        return regLabels;
    }

    public String[] getDecompWSLabelsLevelOne() {
        System.out.println("*********************************");
        System.out.println("WS + Disagg. Labels From level One (Labels)");

        ArrayList<String> regressorLabels = new ArrayList<String>();

        int index = 0;

        for (int p = 0; p < levelOneRegSize; p++) {

            if (levelOneBoxes.get(p).get(2).isSelected() && disaggVarianceBoxes.get(p).get(2).isSelected()) {

                regressorLabels.add(levelOneSelected.get(p));
                System.out.println("Stage-Two/mixRegGUI/Regressor-Fields-(Mean + Disagg.): " + regressorLabels.get(index));
                index++;
            }
        }

        String[] regLabels = new String[regressorLabels.size()];

        for (int pos = 0; pos < regLabels.length; pos++) {
            regLabels[pos] = regressorLabels.get(pos);
            System.out.println("Reg_LABEL: " + regLabels[pos]);

        }

        System.out.println("LEVEL-ONE/MIXREGGUI/WS + DISAGG-REGRESSORS= " + Arrays.toString(regLabels));
        System.out.println("*********************************");
        return regLabels;
    }

    public String getOutcomeLabel() {
        String outcome;

        outcome = StageOneVariableCombo.getSelectedItem().toString();
        System.out.println("Stage-Two/MixRegGUI/Outcome-variable-Label: " + outcome);

        return outcome;

    }

    public String getStageTwoOutcomePosition() {
        String position;
        int pos;

        String outcome = stageTwoOutcome.getSelectedItem().toString();
        pos = stageTwoOutcome.getSelectedIndex();

        position = String.valueOf(pos + 1);

        return position;

    }

    public String getStageTwoOutcomeLabel() {
        String position;
        int pos;

        String outcome = stageTwoOutcome.getSelectedItem().toString();
        pos = stageTwoOutcome.getSelectedIndex();

        position = String.valueOf(pos + 1);

        return outcome;

    }

    public String[] getFixedFieldRegressors_StageTwo() {
        System.out.println("*********************************");
        System.out.println("Fixed From Stage Two (positions)");

        String fieldLabel;

        String[] regressorLabels = new String[stageTwoRegSize];
        int index = 0;

        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < stageTwoRegSize; p++) {

            if (stageTwoGridBoxes.get(p).get(0).isSelected()) {
                regressorLabels[index] = stageTwoSelected_tab2.get(p);
                fieldLabel = stageTwoSelected_tab2.get(p);
                System.out.println("Stage-Two/mixRegGUI/Regressor-Fields-(Fixed): " + regressorLabels[index]);
                index++;

                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo.length; q++) {

                    if (variableNamesCombo[q].equals(fieldLabel)) {
                        //position[index] = String.valueOf(q + 1);
                        position.add(posIndex, String.valueOf(q + 1));
                        System.out.println("Regressor position test: " + String.valueOf(q + 1));
                        System.out.println("From inside mixRegGUI | Position of this regressor: " + position.get(posIndex));
                        System.out.println("Position array: " + position);
                        posIndex++;

                    }

                }
            }

        }
        System.out.println("Position Aray Size here: " + String.valueOf(position.size()));

        String[] positionArray = new String[position.size()];

        for (int pos = 0; pos < positionArray.length; pos++) {
            positionArray[pos] = position.get(pos);
            System.out.println("positionArrayElements: " + positionArray[pos]);

        }

        System.out.println("Converted array size | position: " + String.valueOf(positionArray.length));
        System.out.println("Converted array elements | positions: " + Arrays.toString(positionArray));
        System.out.println("*********************************");

        return positionArray;

    }

    public String[] getRanLocFieldRegressors_StageTwo() {
        System.out.println("*********************************");
        System.out.println("Loc Ran  From Stage Two (position)");

        String fieldLabel;

        String[] regressorLabels = new String[stageTwoRegSize];
        int index = 0;

        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < stageTwoRegSize; p++) {

            if (stageTwoGridBoxes.get(p).get(1).isSelected()) {
                regressorLabels[index] = stageTwoSelected_tab2.get(p);
                fieldLabel = stageTwoSelected_tab2.get(p);
                System.out.println("Stage-Two/mixRegGUI/Regressor-Fields-(LocRan): " + regressorLabels[index]);
                index++;

                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo.length; q++) {

                    if (variableNamesCombo[q].equals(fieldLabel)) {
                        //position[index] = String.valueOf(q + 1);
                        position.add(posIndex, String.valueOf(q + 1));
                        System.out.println("Regressor position test: " + String.valueOf(q + 1));
                        System.out.println("From inside mixRegGUI | Position of this regressor: " + position.get(posIndex));
                        System.out.println("Position array: " + position);
                        posIndex++;

                    }

                }
            }

        }
        System.out.println("Position Aray Size here: " + String.valueOf(position.size()));

        String[] positionArray = new String[position.size()];

        for (int pos = 0; pos < positionArray.length; pos++) {
            positionArray[pos] = position.get(pos);
            System.out.println("positionArrayElements: " + positionArray[pos]);

        }

        System.out.println("Converted array size | position: " + String.valueOf(positionArray.length));
        System.out.println("Converted array elements | positions: " + Arrays.toString(positionArray));
        System.out.println("*********************************");

        return positionArray;

    }

    public String[] getScaleFieldRegressors_StageTwo() {
        System.out.println("*********************************");
        System.out.println("Scale From Stage Two (Positions)");

        String fieldLabel;

        String[] regressorLabels = new String[stageTwoRegSize];
        int index = 0;

        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < stageTwoRegSize; p++) {

            if (stageTwoGridBoxes.get(p).get(2).isSelected()) {
                regressorLabels[index] = stageTwoSelected_tab2.get(p);
                fieldLabel = stageTwoSelected_tab2.get(p);
                System.out.println("Stage-Two/mixRegGUI/Regressor Fields (Scale): " + regressorLabels[index]);
                index++;

                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo.length; q++) {

                    if (variableNamesCombo[q].equals(fieldLabel)) {
                        //position[index] = String.valueOf(q + 1);
                        position.add(posIndex, String.valueOf(q + 1));
                        System.out.println("Regressor position test: " + String.valueOf(q + 1));
                        System.out.println("Stage-two/mixRegGUI/Position of this regressor: " + position.get(posIndex));
                        System.out.println("Position array: " + position);
                        posIndex++;

                    }

                }
            }

        }
        System.out.println("Position Aray Size here: " + String.valueOf(position.size()));

        String[] positionArray = new String[position.size()];

        for (int pos = 0; pos < positionArray.length; pos++) {
            positionArray[pos] = position.get(pos);
            System.out.println("positionArrayElements: " + positionArray[pos]);

        }

        System.out.println("Converted array size | position: " + String.valueOf(positionArray.length));
        System.out.println("Converted array elements | positions: " + Arrays.toString(positionArray));
        System.out.println("*********************************");

        return positionArray;

    }

    public String[] getModelFixedLabelsStageTwo() {
        System.out.println("*********************************");
        System.out.println("Fixed Labels From Stage Two (labels)");

        String fieldLabel;

        ArrayList<String> regressorLabels = new ArrayList<String>();
        int index = 0;

        for (int p = 0; p < stageTwoRegSize; p++) {

            if (stageTwoGridBoxes.get(p).get(0).isSelected()) {
                regressorLabels.add(stageTwoSelected_tab2.get(p));
                fieldLabel = stageTwoSelected_tab2.get(p);
                System.out.println("Stage-Two/mixRegGUI/Regressor-Fields-(FIXED): " + regressorLabels.get(index));
                index++;

            }
        }

        String[] regLabels = new String[regressorLabels.size()];

        for (int pos = 0; pos < regLabels.length; pos++) {
            regLabels[pos] = regressorLabels.get(pos);
            System.out.println("Reg_LABEL: " + regLabels[pos]);

        }

        System.out.println("STAGE-TWO/MIXREGGUI/MEAN-REGRESSORS= " + Arrays.toString(regLabels));
        System.out.println("*********************************");
        return regLabels;
    }

    public String[] getModelLocRanLabelsStageTwo() {
        System.out.println("*********************************");
        System.out.println("Loc Ran Labels From Stage Two (Labels)");

        String fieldLabel;

        ArrayList<String> regressorLabels = new ArrayList<String>();
        int index = 0;

        for (int p = 0; p < stageTwoRegSize; p++) {

            if (stageTwoGridBoxes.get(p).get(1).isSelected()) {
                regressorLabels.add(stageTwoSelected_tab2.get(p));
                fieldLabel = stageTwoSelected_tab2.get(p);
                System.out.println("STAGE-TWO/MIXREGGUI/Regressor-Fields-(LOC RAN): " + regressorLabels.get(index));
                index++;

            }
        }

        String[] regLabels = new String[regressorLabels.size()];

        for (int pos = 0; pos < regLabels.length; pos++) {
            regLabels[pos] = regressorLabels.get(pos);
            System.out.println("Reg_LABEL: " + regLabels[pos]);

        }

        System.out.println("STAGE-TWO/MXREGGUI/LOC-RAN-REGRESSORS= " + Arrays.toString(regLabels));
        System.out.println("*********************************");
        return regLabels;
    }

    public String[] getModelScaleLabelsStageTwo() {
        System.out.println("*********************************");
        System.out.println("Scale Labels From Stage Two (Labels)");

        String fieldLabel;

        ArrayList<String> regressorLabels = new ArrayList<String>();
        int index = 0;

        for (int p = 0; p < stageTwoRegSize; p++) {

            if (stageTwoGridBoxes.get(p).get(2).isSelected()) {
                regressorLabels.add(stageTwoSelected_tab2.get(p));
                fieldLabel = stageTwoSelected_tab2.get(p);
                System.out.println("Stage-Two/mixRegGUI/Regressor-Fields-(SCALE): " + regressorLabels.get(index));
                index++;

            }
        }

        String[] regLabels = new String[regressorLabels.size()];

        for (int pos = 0; pos < regLabels.length; pos++) {
            regLabels[pos] = regressorLabels.get(pos);
            System.out.println("Reg_LABEL: " + regLabels[pos]);

        }

        System.out.println("STAGE-TWO/MIXREGGUI/SCALE-REGRESSORS= " + Arrays.toString(regLabels));
        System.out.println("*********************************");
        return regLabels;
    }

    public String[] getInteractionFieldRegressors_StageTwo() {
        System.out.println("*********************************");
        System.out.println("Interactions From Stage Two (Positions)");

        String fieldLabel;

        String[] regressorLabels = new String[stageTwoRegSize];
        int index = 0;

        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < stageTwoRegSize; p++) {

            if (stageTwoGridBoxes.get(p).get(3).isSelected()) {
                regressorLabels[index] = stageTwoSelected_tab2.get(p);
                fieldLabel = stageTwoSelected_tab2.get(p);
                System.out.println("Stage-two/mixRegGUI/Regressor-Fields-(INTERACTION): " + regressorLabels[index]);
                index++;

                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo.length; q++) {

                    if (variableNamesCombo[q].equals(fieldLabel)) {
                        //position[index] = String.valueOf(q + 1);
                        position.add(posIndex, String.valueOf(q + 1));
                        System.out.println("Stage-Two/Regressor-position-test: " + String.valueOf(q + 1));
                        System.out.println("Stage-Two/mixRegGUI | Position of this regressor: " + position.get(posIndex));
                        System.out.println("Stage-Two/Position array: " + position);
                        posIndex++;

                    }

                }
            }

        }
        System.out.println("Stage-Two/Position Aray Size here: " + String.valueOf(position.size()));

        String[] positionArray = new String[position.size()];

        for (int pos = 0; pos < positionArray.length; pos++) {
            positionArray[pos] = position.get(pos);
            System.out.println("Stage-Two/positionArrayElements: " + positionArray[pos]);

        }

        System.out.println("Stage-Two/Converted array size | position: " + String.valueOf(positionArray.length));
        System.out.println("Stage-Two/Converted array elements | positions: " + Arrays.toString(positionArray));
        System.out.println("*********************************");

        return positionArray;

    }

    public String[] getModelInteractionLabelsStageTwo() {
        System.out.println("*********************************");
        System.out.println("Interaction Labels From Stage Two (Labels)");

        String fieldLabel;

        ArrayList<String> regressorLabels = new ArrayList<String>();
        int index = 0;

        for (int p = 0; p < stageTwoRegSize; p++) {

            if (stageTwoGridBoxes.get(p).get(3).isSelected()) {
                regressorLabels.add(stageTwoSelected_tab2.get(p));
                fieldLabel = stageTwoSelected_tab2.get(p);
                System.out.println("STAGE-TWO/MIXREGGUI/Regressor-Fields-(INTERACTIONS)= " + regressorLabels.get(index));
                index++;
            }
        }

        String[] regLabels = new String[regressorLabels.size()];

        for (int pos = 0; pos < regLabels.length; pos++) {
            regLabels[pos] = regressorLabels.get(pos);
            System.out.println("Reg_LABEL: " + regLabels[pos]);

        }

        System.out.println("STAGE-TWO/MIXREGGUI/INTERACTIONS-REGRESSORS= " + Arrays.toString(regLabels));
        System.out.println("*********************************");
        return regLabels;
    }
    
    public void produceStageTwoOutput() throws FileNotFoundException{
    
        //FileReader reader = new FileReader("/Users/adityaponnada/NetBeansProjects/mixregMLS/src/resources/mixREGLS51.OUT");
        
        FileReader reader = new FileReader("/Users/adityaponnada/NetBeansProjects/mixregMLS/src/resources/mixREGLS51.OUT");
        
        try {
            stageTwoOutput.read(reader, "stageTwoOutput");
        } catch (IOException ex) {
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void produceStageOneOutput() throws FileNotFoundException{
    
        //FileReader reader = new FileReader("/Users/adityaponnada/NetBeansProjects/mixregMLS/src/resources/mixREGLS51.OUT");
        
        FileReader reader = new FileReader("/Users/adityaponnada/NetBeansProjects/mixregMLS/src/resources/mixREGLS51.OUT");
        
        try {
            stageOneOutput.read(reader, "stageOneOutput");
        } catch (IOException ex) {
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void saveStageTwoOutput() throws IOException{
        
      FileFilter filter = new FileNameExtensionFilter("TEXT FILE","txt");
    
      JFileChooser saver = new JFileChooser("./");
        saver.setFileFilter(filter);
        int returnVal = saver.showSaveDialog(this);
        File file = saver.getSelectedFile();
        BufferedWriter writer = null;
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            try
            {
            writer = new BufferedWriter( new FileWriter( file.getName()+".txt"));
            writer.write( stageTwoOutput.getText());
            writer.close( );
            JOptionPane.showMessageDialog(this, "The Message was Saved Successfully!",
                        "Success!", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (IOException e)
            {
            JOptionPane.showMessageDialog(this, "The Text could not be Saved!",
                        "Error!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    public void saveStageOneOutput(){
        FileFilter filter = new FileNameExtensionFilter("TEXT FILE","txt");
    
      JFileChooser saver = new JFileChooser("./");
        saver.setFileFilter(filter);
        int returnVal = saver.showSaveDialog(this);
        File file = saver.getSelectedFile();
        BufferedWriter writer = null;
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            try
            {
            writer = new BufferedWriter( new FileWriter( file.getName()+".txt"));
            writer.write( stageOneOutput.getText());
            writer.close( );
            JOptionPane.showMessageDialog(this, "The Message was Saved Successfully!",
                        "Success!", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (IOException e)
            {
            JOptionPane.showMessageDialog(this, "The Text could not be Saved!",
                        "Error!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    public void runMixedModels(){
    
        String absoluteJavaPath = System.getProperty( "user.dir" );
        String defFileName = executableModel(selectedModel);
        try {          
            try 
            { 
                copyExecutable(defFilePath, selectedModel); //get the def file path after it is saved
                Process p=Runtime.getRuntime().exec("cmd /c dir && cd " + defFilePath + " && dir && "
                        + defFileName); // does it save it in the same directory
                
                p.waitFor(); 
                BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
                String line=reader.readLine(); 
                while(line!=null) 
                { 
                System.out.println(line); 
                line=reader.readLine(); 
                } 
             } 
            catch(FileNotFoundException fnfe1){
             System.out.println("File not found Exception"); 
            }
            catch(IOException e1) {
              System.out.println("IO Exception"); 
            } 
            
            try 
            { 
                Process p=Runtime.getRuntime().exec("cmd /c dir && cd " + defFilePath + " && del /f " + defFileName);
                p.waitFor(); 
                BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
                String line=reader.readLine(); 
                while(line!=null) 
                { 
                System.out.println(line); 
                line=reader.readLine(); 
                } 
             } 
            catch(FileNotFoundException fnfe1){
             System.out.println("File not found Exception 2"); 
            }
            catch(IOException e1) {
              System.out.println("IO Exception 2 "); 
            }
            
            JOptionPane.showMessageDialog(null, defFilePath);
            
        }
        catch (Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed");
        }
 
    }
    
    
    private String executableModel(int modelSelection){
        switch(modelSelection){
            case DefinitionHelper.MIXREGLS_MIXREG_KEY:
                return "mixregls_mixreg.exe";
            case DefinitionHelper.MIXREGLS_MIXOR_KEY:
                return "mixregls_mixor.exe";
            case DefinitionHelper.MIXREGMLS_MIXREG_KEY:
                return "mixregmls_mixreg.exe";
            case DefinitionHelper.MIXREGMLS_MIXOR_KEY:
                return "mixregmls_mixor.exe";
           
            default:
                return "mixregls_mixreg.exe";
        }
    }
    
    
    private void copyExecutable(String absoluteDirectoryPath, int modelSelection) throws FileNotFoundException, IOException{
        String modelPath;
        String executableName = executableModel(modelSelection);
        switch(modelSelection){
            case DefinitionHelper.MIXREGLS_MIXREG_KEY:
                modelPath = "resources/Windows/mixregls_mixreg.exe";
                break;
            case DefinitionHelper.MIXREGLS_MIXOR_KEY:
                modelPath = "resources/Windows/mixregls_mixor.exe";
                break;
            case DefinitionHelper.MIXREGMLS_MIXREG_KEY:
                modelPath = "resources/Windows/mixregmls_mixreg.exe";
                break;
            case DefinitionHelper.MIXREGMLS_MIXOR_KEY:
                modelPath = "resources/Windows/mixregmls_mixor.exe";
                break;
            default:
                modelPath = "resources/Windows/mixregls_mixreg.exe";
                break;
        }
        InputStream stream = getClass().getClassLoader().getResourceAsStream(modelPath);
        
        
        OutputStream outputStream = 
                new FileOutputStream(new File(absoluteDirectoryPath + executableName));

        int read;
        byte[] bytes = new byte[4096];

        while ((read = stream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, read);
        }
        stream.close();
        outputStream.close();
    }
    
    
    public void runMixRegLS_mixor(){
        
        // if then select def file type
        
        int selectedModel = DefinitionHelper.MIXREGLS_MIXREG_KEY;
        /*defLib = new DefinitionHelper(1,false);
        runButton.setEnabled(true);
        MixRegLS_Mixreg.setEnabled(false);
        MixregLS_Mixor.setEnabled(false);
        MixRegMLS_Mixreg.setEnabled(false);
        MixRegMLS_Mixor.setEnabled(false);
        LoadButton.setEnabled(true);*/
        
        
    
    
    
    }
    
    public void runMixRegLS_mixreg(){
    
    
    
    }
    
    public void runMixRegMLS_mixor(){
    
    
    
    }
    
    public void runMixRegMLS_mixreg(){
    
    
    
    }
    
    
    

}
