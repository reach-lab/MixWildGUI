/*
 * MixWild, a program to model subject-level slope and variance on continuous or ordinal outcomes
    Copyright (C) 2018 Genevieve Dunton & Donald Hedeker

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

	Principal Investigators:
	Genevieve Dunton, PhD MPH
	University of Southern California
	dunton@usc.edu
	
	Donald Hedeker, PhD
	University of Chicago
	DHedeker@health.bsd.uchicago.edu
 */
package mixregui;

import com.opencsv.CSVReader;
import def_lib.DefinitionHelper;
import java.awt.Desktop;
import java.net.URL;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import def_lib.DefinitionHelper;
import def_lib.ModelBuilder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
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
import javax.swing.JTable;
import javax.swing.SwingConstants;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.io.FilenameUtils;

/**
 * Main class for the program that is used 
 * to manipulate regressors
 *
 * @author adityaponnada
 */
public class mixregGUI extends javax.swing.JFrame {
    public static mixregGUI mxr;
    
    // Declarations from old java
    File file;
    
    static String[] variableArray;
    
    static int RLE;
    static boolean NoneVar;
    static boolean outComeBoolean;
    static boolean isRandomScale = false;
    static String dataFileNameRef;
    final ImageIcon icon;
    String missingValue = "0";
    
    public static DefinitionHelper defFile;
    public static ModelBuilder modelBuilder;
    
    public int getRLE() {
        return RLE;
    }

    //get the variable names from the data file
    public static String[] getVariableNames() {

        return variableArray;
    }

    //get data file name when created
    public static String getDataFileName() {

        return dataFileNameRef;
    }

  
    //get title from the text box
    public String getTitle() {

        String titleString = titleField.getText().toString();

        return titleString;

    }

//check if the outcome type is selected as continuos or dichotomous
    public boolean isOutcomeContinous() {

        boolean selection = true;

        if (continuousRadio.isSelected() == true) {

            selection = true;
            System.out.println("Outcome selected at Newmodel: " + String.valueOf(selection));
        } else if (dichotomousRadio.isSelected() == true) {

            selection = false;
            System.out.println("Outcome selected at Newmodel: " + String.valueOf(selection));
        }

        System.out.println("Outcome selected at Newmodel: " + String.valueOf(selection));
        return selection;
    }

    public boolean isOutcomeNone() {
        System.out.println("In isOutcomeNone NewModel");

        boolean selection = false;

        if (noneRadio.isSelected() == true) {
            selection = true;
            System.out.println("In isOutcomeNone true");
        } else if (noneRadio.isSelected() == false) {

            selection = false;
        }
        return selection;
    }

    public boolean getNoneVar() {
        return NoneVar;

    }

    public boolean getOutComeType() {

        return outComeBoolean;
    }

    public DefinitionHelper getDefFile() {

        return defFile;
    }

    public String extractDatFileName() {

        String fileLoc = file.getAbsolutePath();
        String fileName = fileLoc.substring(fileLoc.lastIndexOf(File.separator) + 1);
        int iend = fileName.indexOf(".");

        if (iend != -1) {
            fileName = fileName.substring(0, iend);
        }

        return fileName;
    }

    public String extractDatFilePath() {

        String csvPath = file.getAbsolutePath();
        String datPath = FilenameUtils.getFullPath(csvPath) +
                 defFile.getUtcDirPath() +
                FilenameUtils.getBaseName(csvPath) + ".dat";
        return datPath;
    }

    public void getDataFromCSV() throws FileNotFoundException, IOException {

        Object[] columnnames;

        CSVReader CSVFileReader = new CSVReader(new FileReader(file));
        List myEntries = CSVFileReader.readAll();
        columnnames = (String[]) myEntries.get(0);
        DefaultTableModel tableModel = new DefaultTableModel(columnnames, myEntries.size() - 1) {

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        int rowcount = tableModel.getRowCount();

        for (int x = 0; x < rowcount + 1; x++) {
            int columnnumber = 0;
            // if x = 0 this is the first row...skip it... data used for columnnames
            if (x > 0) {
                for (String thiscellvalue : (String[]) myEntries.get(x)) {
                    tableModel.setValueAt(thiscellvalue, x - 1, columnnumber);
                    columnnumber++;
                }
            }
        }

        dataTable.setModel(tableModel);
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dataTable.setAutoscrolls(true);
    }

    public void printFileName() {
        String path = file.getName();

        printedFileName.setText(path);

    }

    public String randomLocationEffects() {

        String randLocString = "";

        if (oneRLERadio.isSelected()) {
            randLocString = "Intercept";

        } else if (moreThanOneRLERadio.isSelected()) {
            randLocString = "Intercept + Slope";

        }

        return randLocString;
    }

    public String outComeTypeString() {

        String outcomeTypeText = "";

        if (continuousRadio.isSelected()) {

            outcomeTypeText = "Continuous";
        } else if (dichotomousRadio.isSelected()) {

            outcomeTypeText = "Dichotomous";
        } else if (noneRadio.isSelected()) {

            outcomeTypeText = "None";
        }
        return outcomeTypeText;

    }
    
    private boolean validateFields() {
        boolean allFieldsEntered = true;
        System.out.println("FIELD VALIDATE: " + "Called");

        if (filePath.getText().trim().length() == 0) {
            allFieldsEntered = false;
            JOptionPane.showMessageDialog(null, "Please select a .csv file for your analysis", "Missing information!", JOptionPane.INFORMATION_MESSAGE, icon);
            System.out.println("FIELD VALIDATE: " + "File path missing");
        }
        //radio buttons for random location effects
        if (buttonGroup2.getSelection() == null) {
            allFieldsEntered = false;
            JOptionPane.showMessageDialog(null, "Please select random location effects", "Missing information!", JOptionPane.INFORMATION_MESSAGE, icon);
            System.out.println("FIELD VALIDATE: " + "RLE not selected");
        }
        //for stage 2 outcome type
        if (buttonGroup3.getSelection() == null) {
            allFieldsEntered = false;
            JOptionPane.showMessageDialog(null, "Please select a stage 2 outcome type", "Missing information!", JOptionPane.INFORMATION_MESSAGE, icon);
            System.out.println("FIELD VALIDATE: " + "Stage 2 outcome missing");
        }
        //For missing values, yes or no.
        if (buttonGroup4.getSelection() == null) {
            allFieldsEntered = false;
            JOptionPane.showMessageDialog(null, "Please confirm if your data has missing values", "Missing information!", JOptionPane.INFORMATION_MESSAGE, icon);
            System.out.println("FIELD VALIDATE: " + "Missing value missing");
        }

        if (newModelMissingValueCode.isEnabled() && newModelMissingValueCode.getText().trim().length() == 0) {
            allFieldsEntered = false;
            JOptionPane.showMessageDialog(null, "Please don't leave the missing code value as blank", "Missing information!", JOptionPane.INFORMATION_MESSAGE, icon);
            System.out.println("FIELD VALIDATE: " + "Missing value blank");
        }
        System.out.println("FIELD VALIDATE: " + "about to exit");
        return allFieldsEntered;
    }
    
    private String generateSeed(){
        String seed;
        
        Random rnd = new Random();
        
        int rndSeed = rnd.nextInt(65535) + 1;
        
        seed = String.valueOf(rndSeed);
        
        return seed;
    }
    
    // Original declarations
   // NewModel newModel;
    advancedOptions advancedOptions_view;
    stageOneRegs stage_1_regs;
    stageTwoRegs stage_2_regs;
    def_lib.SuperUserMenu superUserMenuLaunch;

    // i represents number of random location effects selected in new model
    int i;

    int levelOneRegSize = 0;
    int levelTwoRegSize = 0;
    int stageTwoRegSize = 0;
    int levelOneDisaggSize = 0;
    
    int SUPERUSER_KEY = 0;

    String[] variableNamesCombo;

    DefaultComboBoxModel<String> IDList;

    DefaultComboBoxModel<String> StageOneList;

    DefaultComboBoxModel<String> StageTwoList;

    DefaultListModel<String> savedVariablesStageOne;

    ArrayList<ArrayList<JCheckBox>> levelOneBoxes;

    ArrayList<ArrayList<JCheckBox>> levelTwoBoxes;

    ArrayList<ArrayList<JCheckBox>> stageTwoBoxes;

    ArrayList<ArrayList<JCheckBox>> stageTwoGridBoxes;

    ArrayList<ArrayList<JCheckBox>> disaggVarianceBoxes;

    public static int IDpos;
    public static int stageOnePos;
    public static int stageTwoPos;

    boolean scaleChecked = false;
    boolean randomChecked = false;
    boolean isIDChanged = false;
    boolean isStageOneOutcomeChanged = false;
    boolean isStageTwoOutcomeChanged = false;

    boolean suppressed = false;
    boolean outcomeNone = false;
    boolean addStageOneCHecked = false;
    boolean addStageTwoChecked = false;

    ArrayList<String> levelOneSelected;
    ArrayList<String> levelTwoSelected;
    ArrayList<String> stageTwoSelected;
    ArrayList<String> stageTwoSelected_tab2;

    static ActionListener actionListener;

    int stageOneClicked = 0;

    JFileChooser fileChooser = new JFileChooser();
    int selectedModel;
    String defFilePath;

    String[] dataValues;

    boolean outComeType;

    static String outPutStageTwo;
    
    
    
    
    private void updateMixRegGUI() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        // newModel = new NewModel();
        advancedOptions_view = new advancedOptions();
        //instructions = new InstructionsGUI();
        variableNamesCombo = getVariableNames();
        outcomeNone = getNoneVar();
        outComeType = getOutComeType();
        

        IDList = new DefaultComboBoxModel<String>();
        StageOneList = new DefaultComboBoxModel<String>();
        StageTwoList = new DefaultComboBoxModel<String>();
        NoAssociationRadio.setSelected(true);
        stage_1_regs = new stageOneRegs();
        stage_2_regs = new stageTwoRegs();
        
        i = getRLE();
        System.out.println(String.valueOf(i));

        stageOneTabs.setEnabledAt(2, false);
        suppressIntCheckBox.setVisible(true);
        suppressIntCheckBox.setSelected(false);
        suppressIntCheckBox.setEnabled(true);

        //Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/mixLogo.png"));
        //setIconImage(image);


        if (i > 1) {

            NoAssociationRadio.setText("Yes");

            LinearAssociationRadio.setText("No");
            QuadraticAssociationRadio.setVisible(false);

            associationLabel.setText("Association of random location & scale?"); //Switch to this when i > 1
            LinearAssociationRadio.setSelected(true);

            // if random location effects are more than one, change the table column names
            level2_BSVar.setVisible(false);
            level1_BSVar.setText("Random Slope");
            level1_WSVar.setText("Scale");
            //level2_BSVar.setText("Loc. eff.");
            level2_WSVar.setText("Scale");

        }

        levelOnePanel.setLayout(new BorderLayout());
        levelTwoPanel.setLayout(new BorderLayout());

        stageTwoPanel.setLayout(new BorderLayout());

        System.out.println("Right before");
        if (outcomeNone == true) {
            System.out.println("In isOutcomeNone MixReg");
            startStageTwo.setText("Run Stage 1");
            System.out.println(startStageTwo.getText());
            //stageOneTabs.set
            stageOneTabs.setEnabledAt(2, false);
        }
        System.out.println("Right after");

        if (outComeType == false) {

            outcomeCatButton.setEnabled(true);
            outcomeCatButton.setVisible(true);
            //outComeText.setEnabled(true);
            outCategoryDisplay.setEnabled(true);
            //outComeText.setVisible(true);
            outCategoryDisplay.setVisible(true);
            //outComeText.setEditable(false);
            outCategoryDisplay.setEnabled(true);
            System.out.println("outCatButton Enabled: " + String.valueOf(isOutcomeContinous()));
            jPanel5.setEnabled(true);

        } else if (outComeType == true) {

            outcomeCatButton.setVisible(false);
            //outComeText.setVisible(false);
            System.out.println("outCatButton Enabled: " + String.valueOf(isOutcomeContinous()));
            jPanel5.setVisible(false);

        }
        
        try{
            if(defFile.getAdvancedRandomScaleNotIncluded() == "1"){
                NoAssociationRadio.setEnabled(false);
                LinearAssociationRadio.setEnabled(false);
                QuadraticAssociationRadio.setEnabled(false);
            } 
        }
        catch(Exception eoe){
            System.out.println("No Random Scale Option Set");
        }
    }

    /**
     * Creates new form mixregGUI
     */
    public mixregGUI() {
        initComponents();
        icon = new ImageIcon(getClass().getResource("/resources/MixWildLogoTiny.png"));
        //updateMixRegGUI();
        //this.setResizable(false);

        
        // TODO: Fix superuser menu code
        //superUserMenu.setVisible(SUPERUSER_KEY > 2);
        

//       IDpos = IDvariableCombo.getSelectedIndex();
//       stageOnePos = StageOneVariableCombo.getSelectedIndex();
//       stageTwoPos = stageTwoOutcome.getSelectedIndex();
//        
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
        jSeparator13 = new javax.swing.JSeparator();
        jScrollPane6 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        parentPanel = new javax.swing.JPanel();
        stageOneTabs = new javax.swing.JTabbedPane();
        jPanel13 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        fileBrowseButton = new javax.swing.JButton();
        filePath = new javax.swing.JTextField();
        dataFileLabel = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        titleField = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel24 = new javax.swing.JLabel();
        oneRLERadio = new javax.swing.JRadioButton();
        moreThanOneRLERadio = new javax.swing.JRadioButton();
        jLabel25 = new javax.swing.JLabel();
        randomScaleCheckBox = new javax.swing.JCheckBox();
        jLabel26 = new javax.swing.JLabel();
        continuousRadio = new javax.swing.JRadioButton();
        dichotomousRadio = new javax.swing.JRadioButton();
        noneRadio = new javax.swing.JRadioButton();
        missingValueAbsent = new javax.swing.JRadioButton();
        missingValuePresent = new javax.swing.JRadioButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        newModelMissingValueCode = new javax.swing.JTextField();
        seedTextBox = new javax.swing.JTextField();
        setSeedLabel = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        newModel_resetButton = new javax.swing.JButton();
        newModelSubmit = new javax.swing.JButton();
        seedHelpButton = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        resetButton = new javax.swing.JButton();
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
        jPanel7 = new javax.swing.JPanel();
        randomLocationEffectsLabel = new javax.swing.JLabel();
        stageTwoOutcomePrintLabel = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
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
        jButton1 = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 2), new java.awt.Dimension(0, 2), new java.awt.Dimension(32767, 2));
        jSeparator14 = new javax.swing.JSeparator();
        jSeparator15 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        outcomeCatButton = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        outCategoryDisplay = new javax.swing.JTextPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        stageOneOutput = new javax.swing.JTextArea();
        jButton8 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        stageTwoOutput = new javax.swing.JTextArea();
        saveStage2OutButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        equationArea = new javax.swing.JTextArea();
        jLabel23 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        printedFileName = new javax.swing.JLabel();

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

        jScrollPane6.setViewportView(jEditorPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mix Suite");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        parentPanel.setLayout(new java.awt.CardLayout());

        jLabel16.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel16.setText("Is your dataset Mix{WILD} friendly?");

        jButton2.setText("Check here...");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        fileBrowseButton.setText("Browse");
        fileBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileBrowseButtonActionPerformed(evt);
            }
        });

        filePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filePathActionPerformed(evt);
            }
        });

        dataFileLabel.setText("Data File: ");

        jLabel19.setText("Title:");

        titleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titleFieldActionPerformed(evt);
            }
        });

        jLabel24.setText("Specify random location effects:");

        buttonGroup2.add(oneRLERadio);
        oneRLERadio.setText("Intercept only");
        oneRLERadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oneRLERadioActionPerformed(evt);
            }
        });

        buttonGroup2.add(moreThanOneRLERadio);
        moreThanOneRLERadio.setText("Intercept and Slope(s)");
        moreThanOneRLERadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moreThanOneRLERadioActionPerformed(evt);
            }
        });

        jLabel25.setText("Include estimates of random scale:");

        randomScaleCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomScaleCheckBoxActionPerformed(evt);
            }
        });

        jLabel26.setText("Stage 2 Outcome:");

        buttonGroup3.add(continuousRadio);
        continuousRadio.setText("Continuous");
        continuousRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continuousRadioActionPerformed(evt);
            }
        });

        buttonGroup3.add(dichotomousRadio);
        dichotomousRadio.setText("Dichotomous/Ordinal");
        dichotomousRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dichotomousRadioActionPerformed(evt);
            }
        });

        buttonGroup3.add(noneRadio);
        noneRadio.setText("None");
        noneRadio.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                noneRadioStateChanged(evt);
            }
        });
        noneRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noneRadioActionPerformed(evt);
            }
        });

        buttonGroup4.add(missingValueAbsent);
        missingValueAbsent.setText("No");
        missingValueAbsent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                missingValueAbsentActionPerformed(evt);
            }
        });

        buttonGroup4.add(missingValuePresent);
        missingValuePresent.setText("Yes");
        missingValuePresent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                missingValuePresentActionPerformed(evt);
            }
        });

        jLabel27.setText("Does your data contain missing values?");

        jLabel28.setText("What is your missing data coded as?");

        newModelMissingValueCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newModelMissingValueCodeActionPerformed(evt);
            }
        });
        newModelMissingValueCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                newModelMissingValueCodeKeyTyped(evt);
            }
        });

        setSeedLabel.setText("(Optional) Set a seed for Stage 2 resampling:");

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/MixWildLogoTiny.png"))); // NOI18N

        newModel_resetButton.setText("Reset");
        newModel_resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newModel_resetButtonActionPerformed(evt);
            }
        });

        newModelSubmit.setText("Submit");
        newModelSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newModelSubmitActionPerformed(evt);
            }
        });

        seedHelpButton.setText("?");
        seedHelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seedHelpButtonActionPerformed(evt);
            }
        });

        jLabel30.setText("Stage 1 Model");

        jLabel31.setText("Stage 2 Model");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(231, 231, 231)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addComponent(jLabel19)
                                        .addGap(38, 38, 38)
                                        .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addComponent(dataFileLabel)
                                        .addGap(7, 7, 7)
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel13Layout.createSequentialGroup()
                                                .addComponent(jLabel16)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel13Layout.createSequentialGroup()
                                                .addComponent(filePath, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, 0)
                                                .addComponent(fileBrowseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addComponent(jLabel27)
                                        .addGap(11, 11, 11)
                                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(newModelMissingValueCode, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel13Layout.createSequentialGroup()
                                                .addComponent(missingValuePresent, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(missingValueAbsent))))))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(206, 206, 206)
                                .addComponent(jLabel29)
                                .addGap(150, 150, 150)
                                .addComponent(newModel_resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(newModelSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                                        .addComponent(jLabel28)
                                        .addGap(263, 263, 263))
                                    .addComponent(jSeparator8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(49, 49, 49))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel13Layout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31))
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(85, 85, 85)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addComponent(jLabel25)
                                        .addGap(18, 18, 18)
                                        .addComponent(randomScaleCheckBox))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addComponent(jLabel24)
                                        .addGap(18, 18, 18)
                                        .addComponent(oneRLERadio, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(moreThanOneRLERadio))
                                    .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(74, 74, 74)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addComponent(setSeedLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(seedTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(seedHelpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addComponent(jLabel26)
                                        .addGap(9, 9, 9)
                                        .addComponent(continuousRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(dichotomousRadio)
                                        .addGap(0, 0, 0)
                                        .addComponent(noneRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(44, 44, 44)))
                .addGap(337, 337, 337))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(62, 62, 62)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(dataFileLabel))
                    .addComponent(filePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileBrowseButton))
                .addGap(21, 21, 21)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(missingValuePresent, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(missingValueAbsent, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newModelMissingValueCode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(moreThanOneRLERadio)
                                .addComponent(oneRLERadio))
                            .addComponent(jLabel24))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(randomScaleCheckBox)
                            .addComponent(jLabel25)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel30)
                        .addGap(30, 30, 30)))
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(noneRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(jLabel31))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(continuousRadio)
                            .addComponent(dichotomousRadio))))
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(setSeedLabel)
                    .addComponent(seedTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(seedHelpButton))
                .addGap(135, 135, 135)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newModel_resetButton)
                    .addComponent(newModelSubmit))
                .addContainerGap(163, Short.MAX_VALUE))
        );

        stageOneTabs.addTab("Model Configuration", jPanel13);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        resetButton.setText("Clear Stage 1");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        jPanel1.add(resetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 560, 135, 40));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setText("Stage 1 Regressors");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 20, -1, -1));

        level1_MeanReg.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        level1_MeanReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        level1_MeanReg.setText("Mean");
        level1_MeanReg.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(level1_MeanReg, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 70, 40, -1));
        level1_MeanReg.getAccessibleContext().setAccessibleName("");

        level1_WSVar.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        level1_WSVar.setText("WS Variance");
        jPanel1.add(level1_WSVar, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 70, -1, -1));

        level2_MeanReg.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        level2_MeanReg.setText("Mean");
        jPanel1.add(level2_MeanReg, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 70, 40, -1));

        level2_BSVar.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        level2_BSVar.setText("BS Variance");
        jPanel1.add(level2_BSVar, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 70, 80, -1));

        level2_WSVar.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        level2_WSVar.setText("WS Variance");
        jPanel1.add(level2_WSVar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 70, -1, -1));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 610, 1190, 10));

        level1_BSVar.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        level1_BSVar.setText("BS Variance");
        level1_BSVar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(level1_BSVar, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 70, 80, -1));

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
            .addComponent(levelOneGrid, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
        );

        levelOneGrid.getAccessibleContext().setAccessibleName("Level-1");

        jPanel1.add(levelOnePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 90, 440, 450));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 50, 540, 10));
        jPanel1.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 50, -1, 140));

        levelTwoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Level-2"));

        levelTwoGrid.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout levelTwoPanelLayout = new javax.swing.GroupLayout(levelTwoPanel);
        levelTwoPanel.setLayout(levelTwoPanelLayout);
        levelTwoPanelLayout.setHorizontalGroup(
            levelTwoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(levelTwoGrid, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
        );
        levelTwoPanelLayout.setVerticalGroup(
            levelTwoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(levelTwoPanelLayout.createSequentialGroup()
                .addComponent(levelTwoGrid, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(levelTwoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 90, 420, 450));

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        StageOneVariableCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        StageOneVariableCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                StageOneVariableComboItemStateChanged(evt);
            }
        });
        StageOneVariableCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StageOneVariableComboActionPerformed(evt);
            }
        });
        jPanel8.add(StageOneVariableCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 230, -1));

        jLabel2.setText("Stage 1 Outcome:");
        jPanel8.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, -1));

        IDvariableCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        IDvariableCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                IDvariableComboItemStateChanged(evt);
            }
        });
        IDvariableCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDvariableComboActionPerformed(evt);
            }
        });
        jPanel8.add(IDvariableCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 230, -1));

        jLabel1.setText("ID Variable:");
        jPanel8.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, -1, -1));
        jPanel8.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, 270, 10));
        jPanel8.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 270, 10));

        addStageOneButton.setText("Configure Stage 1 Regressors ...");
        addStageOneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStageOneButtonActionPerformed(evt);
            }
        });
        jPanel8.add(addStageOneButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 225, 35));

        advancedOptionsButton.setText("Options ...");
        advancedOptionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advancedOptionsButtonActionPerformed(evt);
            }
        });
        jPanel8.add(advancedOptionsButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 225, 37));
        jPanel8.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 280, 10));

        associationLabel.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        associationLabel.setText("<html>Specify the relationship between the <br>mean and WS variance.<br></html>");

        buttonGroup1.add(NoAssociationRadio);
        NoAssociationRadio.setText("No Association");
        NoAssociationRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoAssociationRadioActionPerformed(evt);
            }
        });

        buttonGroup1.add(LinearAssociationRadio);
        LinearAssociationRadio.setText("Linear Association");
        LinearAssociationRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LinearAssociationRadioActionPerformed(evt);
            }
        });

        buttonGroup1.add(QuadraticAssociationRadio);
        QuadraticAssociationRadio.setText("Quadratic Association");

        javax.swing.GroupLayout associationPanelLayout = new javax.swing.GroupLayout(associationPanel);
        associationPanel.setLayout(associationPanelLayout);
        associationPanelLayout.setHorizontalGroup(
            associationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(associationPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(associationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(associationLabel)
                    .addGroup(associationPanelLayout.createSequentialGroup()
                        .addGroup(associationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(NoAssociationRadio)
                            .addComponent(LinearAssociationRadio)
                            .addComponent(QuadraticAssociationRadio))
                        .addContainerGap())))
        );
        associationPanelLayout.setVerticalGroup(
            associationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(associationPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(associationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(NoAssociationRadio)
                .addGap(12, 12, 12)
                .addComponent(LinearAssociationRadio)
                .addGap(12, 12, 12)
                .addComponent(QuadraticAssociationRadio))
        );

        jPanel8.add(associationPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 290, 160));

        randomLocationEffectsLabel.setText("Selected Model");

        stageTwoOutcomePrintLabel.setText("State 2 outcome");

        jLabel21.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel21.setText("Selected model configuration:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stageTwoOutcomePrintLabel)
                    .addComponent(randomLocationEffectsLabel)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(randomLocationEffectsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stageTwoOutcomePrintLabel)
                .addContainerGap())
        );

        jPanel8.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, -10, 290, 80));
        jPanel8.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 280, 10));

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 310, 510));
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 530, -1, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/MixWildLogoTiny.png"))); // NOI18N
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 550, -1, 30));

        startStageTwo.setText("Configure Stage 2");
        startStageTwo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startStageTwoActionPerformed(evt);
            }
        });
        jPanel1.add(startStageTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 560, 140, 40));

        stageOneTabs.addTab("Stage 1 Configuration", jPanel1);

        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel14.setText(" Main Effects");
        jPanel12.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 90, -1, -1));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel12.setText("Stage 2 Interactions");
        jPanel12.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 40, 160, 20));

        jLabel15.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel15.setText("Random Location");
        jPanel12.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 90, -1, -1));

        addStageTwoTabTwo.setText("Configure Stage 2 Regressors ...");
        addStageTwoTabTwo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStageTwoTabTwoActionPerformed(evt);
            }
        });
        jPanel12.add(addStageTwoTabTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, 230, 40));

        jLabel17.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel17.setText("Random Scale");
        jPanel12.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 90, -1, 20));

        jLabel18.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel18.setText("Location X Scale");
        jPanel12.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 90, 110, 20));

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel12.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 130, 10, 360));

        stageTwoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Stage-2 Regressors"));

        stageTwoRegsGrid.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout stageTwoPanelLayout = new javax.swing.GroupLayout(stageTwoPanel);
        stageTwoPanel.setLayout(stageTwoPanelLayout);
        stageTwoPanelLayout.setHorizontalGroup(
            stageTwoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stageTwoPanelLayout.createSequentialGroup()
                .addGap(250, 250, 250)
                .addComponent(stageTwoRegsGrid, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addGap(48, 48, 48))
        );
        stageTwoPanelLayout.setVerticalGroup(
            stageTwoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stageTwoPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(stageTwoRegsGrid, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                .addContainerGap())
        );

        stageTwoRegsGrid.getAccessibleContext().setAccessibleParent(jPanel12);

        jPanel12.add(stageTwoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 110, 720, 390));

        runTabTwoStageOneTwo.setText("Run Stage 1 and 2");
        runTabTwoStageOneTwo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runTabTwoStageOneTwoActionPerformed(evt);
            }
        });
        jPanel12.add(runTabTwoStageOneTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 510, 160, 40));

        suppressIntCheckBox.setText("Suppress Scale X Random Interaction");
        suppressIntCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppressIntCheckBoxActionPerformed(evt);
            }
        });
        jPanel12.add(suppressIntCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 520, 240, -1));

        stageTwoOutcome.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        stageTwoOutcome.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                stageTwoOutcomeItemStateChanged(evt);
            }
        });
        jPanel12.add(stageTwoOutcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 230, 30));

        jLabel22.setText("Stage 2 Outcome:");
        jPanel12.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 110, -1));

        jButton1.setText("Clear Stage 2");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 510, 150, 40));
        jPanel12.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 570, 740, 10));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/MixWildLogoTiny.png"))); // NOI18N
        jPanel12.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 520, -1, 30));
        jPanel12.add(filler1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 570, -1, 80));
        jPanel12.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 70, 320, 10));
        jPanel12.add(jSeparator15, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 660, -1, -1));
        jPanel12.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 262, 220, 0));

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        outcomeCatButton.setText("Check outcome categories");
        outcomeCatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outcomeCatButtonActionPerformed(evt);
            }
        });

        jScrollPane5.setViewportView(outCategoryDisplay);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(outcomeCatButton, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                    .addComponent(jScrollPane5))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(outcomeCatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel12.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 250, 240, 250));

        stageOneTabs.addTab("Stage 2 Configuration", jPanel12);

        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        stageOneOutput.setColumns(20);
        stageOneOutput.setRows(5);
        jScrollPane2.setViewportView(stageOneOutput);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton8.setText("Save Results As ...");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/MixWildLogoTiny.png"))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setText("Results from stage 1 analysis");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(181, 181, 181)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(226, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(117, 117, 117))
        );

        stageOneTabs.addTab("Stage 1 Results", jPanel3);

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        stageTwoOutput.setColumns(20);
        stageTwoOutput.setRows(5);
        jScrollPane1.setViewportView(stageTwoOutput);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 807, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                .addContainerGap())
        );

        saveStage2OutButton.setText("Save Results As ...");
        saveStage2OutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveStage2OutButtonActionPerformed(evt);
            }
        });

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/MixWildLogoTiny.png"))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel11.setText("Results from stage 2 analysis");
        jLabel11.setToolTipText("");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(186, 186, 186)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(saveStage2OutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 820, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveStage2OutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        stageOneTabs.addTab("Stage 2 Results", jPanel4);

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel13.setText("Your resulting model equation");

        equationArea.setColumns(20);
        equationArea.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        equationArea.setRows(5);
        jScrollPane8.setViewportView(equationArea);

        jLabel23.setText("You can copy this model equation directly into Latex, Word or any other text editor.");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(162, Short.MAX_VALUE)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 885, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(163, Short.MAX_VALUE))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(367, 367, 367)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addGap(486, 486, 486))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel23)
                .addContainerGap(445, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1210, 660));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/mixLogo.png"))); // NOI18N
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 550, -1, 40));

        stageOneTabs.addTab("View Model", jPanel2);

        dataTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(dataTable);

        jLabel20.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel20.setText("Imported data file:");

        printedFileName.setText("filename");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(31, 31, 31)
                        .addComponent(printedFileName))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1098, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(printedFileName))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 583, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(250, Short.MAX_VALUE))
        );

        stageOneTabs.addTab("View Data", jPanel6);

        parentPanel.add(stageOneTabs, "card2");

        getContentPane().add(parentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1224, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveStage2OutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveStage2OutButtonActionPerformed
        try {
            // TODO add your handling code here:

            saveStageTwoOutput();
        } catch (IOException ex) {
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveStage2OutButtonActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        saveStageOneOutput();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void outcomeCatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outcomeCatButtonActionPerformed
        // TODO add your handling code here:
        //get the categories of the stage two outcome variable
        System.out.println("Inside click for categories");

        ArrayList<String> ColumnsCustom = new ArrayList<>();
        ArrayList<String> UniqueList = new ArrayList<>();

        String dataFileName = getDataFileName();
        File file = new File(dataFileName);
        //        //first get the column
        BufferedReader br = null;
        String line = "";
        String commaSplitter = ",";
        //

        try {
            br = new BufferedReader(new FileReader(dataFileName));
            line = br.readLine(); //consumes the first row
            while ((line = br.readLine()) != null) {
                String[] Columns = line.split(commaSplitter);

                int index = stageTwoOutcome.getSelectedIndex();
                ColumnsCustom.add(Columns[index]);

            }

            System.out.println("COLUMN:");
            for (int k = 0; k < ColumnsCustom.size(); k++) {

                System.out.println(ColumnsCustom.get(k));
            }

            //            if (defFile.getAdvancedMissingValue().contains(".")){
                //            String strippedMissingVal = defFile.getAdvancedMissingValue().substring(0,defFile.getAdvancedMissingValue().indexOf('.'));
                //            }
            //
            //count the unique ones
            for (int x = 0; x < ColumnsCustom.size(); x++) {
                if (UniqueList.contains(ColumnsCustom.get(x))) {
                    //do nothing
                } else if (ColumnsCustom.get(x).equals(defFile.getAdvancedMissingValue()) && !ColumnsCustom.get(x).equals("0")) { //compare if the category is a missing value, then don't consider it as a category
                    //do nothing

                } else {
                    UniqueList.add(ColumnsCustom.get(x));
                }

            }

            //sort UniqueList First
            ArrayList<Integer> UniqueIntegers = new ArrayList<>();

            for (int x = 0; x < UniqueList.size(); x++) {

                UniqueIntegers.add(Integer.valueOf(UniqueList.get(x)));

            }

            Collections.sort(UniqueIntegers);

            System.out.println("Number of unique categories: " + String.valueOf(UniqueList.size()));

            outCategoryDisplay.setText(UniqueList.size() + " Categories:\n");
            //            for (int index = 0; index < UniqueList.size(); index++) {
                //                //numberOfCategories.setT
                //                //numberOfCategories.setText(numberOfCategories.getText() +"<html><br></html>" + String.valueOf(index + 1) + ":" + UniqueList.get(index) + "<html><br></html>");
                //                outCategoryDisplay.setText(outCategoryDisplay.getText() + String.valueOf(index + 1) + ") " + UniqueList.get(index) + "\n");
                //
                //            }

            for (int index = 0; index < UniqueIntegers.size(); index++) {
                //numberOfCategories.setT
                //numberOfCategories.setText(numberOfCategories.getText() +"<html><br></html>" + String.valueOf(index + 1) + ":" + UniqueList.get(index) + "<html><br></html>");
                outCategoryDisplay.setText(outCategoryDisplay.getText() + String.valueOf(index + 1) + ") " + String.valueOf(UniqueIntegers.get(index)) + "\n");

            }

            String[] outcomeCats = new String[UniqueIntegers.size()];

            for (int pos = 0; pos < outcomeCats.length; pos++) {
                outcomeCats[pos] = String.valueOf(UniqueIntegers.get(pos));
                System.out.println("STAGE 2 TESTING OUTCOME CATEGORIES: " + outcomeCats[pos]);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Logger.getLogger(getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }//GEN-LAST:event_outcomeCatButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        stageTwoPanel.removeAll();
        stageTwoPanel.revalidate();
        stageTwoPanel.repaint();
        
        stage_2_regs.stageTwoLevelTwo.clear();
        updateStageTwoGrid_tab2(stage_2_regs.stageTwoLevelTwo);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void stageTwoOutcomeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_stageTwoOutcomeItemStateChanged
        // TODO add your handling code here:
        stageTwoPos = stageTwoOutcome.getSelectedIndex();
        System.out.println("STAGE TWO OUTCOME CHANGED: " + String.valueOf(stageTwoPos));
        isStageTwoOutcomeChanged = true;
    }//GEN-LAST:event_stageTwoOutcomeItemStateChanged

    private void suppressIntCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppressIntCheckBoxActionPerformed
        // TODO add your handling code here:

        if (suppressIntCheckBox.isSelected()) {

            suppressed = true;

            try {
                defFile.setStageTwoIntOfInteraction("-1");
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            }

            for (int p = 0; p < stageTwoRegSize; p++) {

                stageTwoGridBoxes.get(p).get(3).setSelected(false);
                stageTwoGridBoxes.get(p).get(3).setEnabled(false);
            }
            try {
                defFile.setStageTwoFirstIntFields(new String[0]);
                defFile.setStageTwoFirstIntLabels(new String[0]);
            } catch (Exception ex) {
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            suppressed = false;

            try {
                defFile.setStageTwoIntOfInteraction("0");
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            }

            for (int p = 0; p < stageTwoRegSize; p++) {

                if (stageTwoGridBoxes.get(p).get(1).isSelected() && stageTwoGridBoxes.get(p).get(2).isSelected()){
                    stageTwoGridBoxes.get(p).get(3).setSelected(false);
                    stageTwoGridBoxes.get(p).get(3).setEnabled(true);
                }

            }

        }

        //suppressed = true;
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
        try {
            defFile.setStageTwoOutcomeField(getStageTwoOutcomePosition());
            System.out.println("From defHelper | Outcome variable Position STAGE TWO: " + defFile.getStageTwoOutcomeField());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setStageTwoOutcomeLabel(getStageTwoOutcomeLabel());
            System.out.println("From defHelper | Outcome variable label STAGE TWO: " + defFile.getStageTwoOutcomeLabel());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        // *********************************************************************
        // i is the number of random location effects selected by the users
        if (i == 1) {

            if (NoAssociationRadio.isSelected()) {

                try {
                    defFile.setAdvancedEffectMeanWS(String.valueOf(0));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (No Association): " + defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (LinearAssociationRadio.isSelected()) {
                try {
                    defFile.setAdvancedEffectMeanWS(String.valueOf(1));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (Linear Association): " + defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (QuadraticAssociationRadio.isSelected()) {
                try {
                    defFile.setAdvancedEffectMeanWS(String.valueOf(2));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (Quadratic Association): " + defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } // field array counting ends

        } else if (i > 1) {

            //Check if the effect of mean on WS variances options have been selected
            if (NoAssociationRadio.isSelected()) {

                try {
                    defFile.setAdvancedEffectMeanWS(String.valueOf(1));
                    System.out.println("From defHelper | Stage 1 Association of random location & scale?: " + defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (LinearAssociationRadio.isSelected()) {
                try {
                    defFile.setAdvancedEffectMeanWS(String.valueOf(0));
                    System.out.println("From defHelper | Stage 1 Association of random location & scale?: " + defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
            }
        }

        try {
            defFile.setStageTwoFixedCount(String.valueOf(countStageTwoBeta()));
            System.out.println("From defHelper | STAGE TWO FIXED COUNT: " + defFile.getStageTwoFixedCount().toString());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setStageTwoLocRanInteractions(String.valueOf(countStageTwoAlpha()));
            System.out.println("From defHelper | STAGE TWO LOC. RANDOM COUNT: " + defFile.getStageTwoLocRanInteractions().toString());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setStageTwoScaleInteractions(String.valueOf(countStageTwoTau()));
            System.out.println("From defHelper | STAGE TWO SCALE COUNT: " + defFile.getStageTwoScaleInteractions().toString());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        if (suppressed == false) {

            try {
                defFile.setStageTwoIntOfInteraction(String.valueOf(countStageTwoInteractions()));
                System.out.println("From defHelper | STAGE TWO INTERACTIONS COUNT: " + defFile.getStageTwoIntOfInteraction().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

        }

        try {
            defFile.setStageTwoFixedFields(getFixedFieldRegressors_StageTwo());
            System.out.println("From defHelper | STAGE TWO  FIXED REGRESSOR Positions: " + Arrays.toString(defFile.getStageTwoFixedFields()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setStageTwoLocRanIntFields(getRanLocFieldRegressors_StageTwo());
            System.out.println("From defHelper | STAGE TWO  LOC RAN REGRESSOR Positions: " + Arrays.toString(defFile.getStageTwoLocRanIntFields()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setStageTwoScaleIntFields(getScaleFieldRegressors_StageTwo());
            System.out.println("From defHelper | STAGE TWO  SCALE REGRESSOR Positions: " + Arrays.toString(defFile.getStageTwoScaleIntFields()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        if (suppressed == false) {

            try {
                defFile.setStageTwoFirstIntFields(getInteractionFieldRegressors_StageTwo());
                System.out.println("From defHelper | STAGE TWO  INTERACTIONS REGRESSOR Positions: " + Arrays.toString(defFile.getStageTwoFirstIntFields()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setStageTwoFirstIntLabels(getModelInteractionLabelsStageTwo());
                System.out.println("From defHelper | STAGE TWO  INTERACTIONS REGRESSORS: " + Arrays.toString(defFile.getStageTwoFirstIntLabels()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
        }

        try {
            defFile.setStageTwoFixedLabels(getModelFixedLabelsStageTwo());
            System.out.println("From defHelper | STAGE TWO  MEAN REGRESSORS: " + Arrays.toString(defFile.getStageTwoFixedLabels()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setStageTwoLocRanIntLabels(getModelLocRanLabelsStageTwo());
            System.out.println("From defHelper | STAGE TWO  LOC RAN REGRESSORS: " + Arrays.toString(defFile.getStageTwoLocRanIntLabels()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setStageTwoScaleIntLabels(getModelScaleLabelsStageTwo());
            System.out.println("From defHelper | STAGE TWO  SCALE REGRESSORS: " + Arrays.toString(defFile.getStageTwoScaleIntLabels()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }
        if (outComeType == false) {
            try {
                defFile.setStageTwoOutcomeCatCount(String.valueOf(getStagetwoOutcomeCats()));
                System.out.println("From defHelper | STAGE TWO OUTCOME CATEGORY NUMBERS: " + defFile.getStageTwoOutcomeCatCount());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setStageTwoOutcomeCatLabel(getStageTwoOutcomeValues());
                System.out.println("From defHelper | STAGE TWO OUTCOME CATEGORY VALUES: " + Arrays.toString(defFile.getStageTwoOutcomeCatLabel()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

        } else {

            // do nothing ...
        }

        if (catchCount == 0) {
            int defTry = 0;
            int defCatch = 0;
            try {
                List<String> defFileOutput;

                defFile.writeDefFileToFolder();
                defFileOutput = defFile.buildStageOneDefinitonList();
                System.out.println("From defHelper | Stage 1 def file created successfully!");
                //modelBuilder(defFile);
                modelBuilder = new ModelBuilder(defFile);
                //                modelEquationTextArea.setText(modelBuilder.meanEquation());
                //                testEq.setText(modelBuilder.meanEquation());
                equationArea.setText(modelBuilder.meanEquation());

                //modelBuilder.saveWildFile(defFile);
                //                modelBuilder.meanEquation();
            } catch (Exception ex) {
                defCatch = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                System.out.println("From defHelper | Stage 1 def file failed!");
            }

            if (defCatch == 0) {
                stageOneTabs.setSelectedIndex(3);//todo: get output as soon as it is ready
            }

        } else {

            // do nothing
        }

    }//GEN-LAST:event_runTabTwoStageOneTwoActionPerformed

    private void addStageTwoTabTwoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStageTwoTabTwoActionPerformed
        // TODO add your handling code here:

        if (stage_2_regs.isVisible()) { //if it is already open and visible in the background

            // stage_1_regs.set
            stage_2_regs.setFocusable(true);

        } else {
            //stage_1_regs.revalidate();
            //stage_1_regs.repaint();
            // stage_1_regs.removeAll();

            if (addStageTwoChecked == true) {

                stage_2_regs.setVisible(true);
                stage_2_regs.updateStageTwoAgain();
                //            stage_2_regs.updateStageTwoWithoutStageOne();
            } else {
                stage_2_regs.setVisible(true);
                stage_2_regs.updateStageTwoWithoutStageOne();
            }
        }

        addStageTwoChecked = true;

        //        stage_2_regs = new stageTwoRegs();
        //stageOneTabs.setSelectedIndex(1);
        //        stage_2_regs.setVisible(true);
        //        //stage_2_regs.updateStageTwoVariables(getSavedVariables());
        //        stage_2_regs.updateStageTwoWithoutStageOne();
    }//GEN-LAST:event_addStageTwoTabTwoActionPerformed

    private void startStageTwoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startStageTwoActionPerformed
        // TODO add your handling code here:

        if (outcomeNone == false) {
            stageOneTabs.setEnabledAt(2, true);
        } else {

            stageOneTabs.setEnabledAt(2, false);
        }

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
            defFile.setIdOutcome(idOutcome);
            System.out.println("From defHelper | ID and Outcome indices: " + Arrays.toString(defFile.getIdOutcome()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            tryCount = 1;
            defFile.setLabelModelOutcome(getOutcomeLabel());
            System.out.println("From defHelper | Outcome variable Stage One LABEL: " + defFile.getLabelModelOutcome());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        // i is the number of random location effects selected by the users
        if (i == 1) {

            try {
                tryCount = 1;
                int MeanCount = countLevelOneBeta() + countLevelTwoBeta() - countLevelOneDicompMean(); //check this ======================
                // count total mean regressors in level one and level two
                defFile.setModelMeanCount(String.valueOf(MeanCount));
                System.out.println("From mixRegGUI | Stage 1 Model Mean Count: " + String.valueOf(MeanCount));
                System.out.println("From defHelper | Stage 1 Model Mean Count: " + defFile.getModelMeanCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;
                int betweenCount = countLevelOneAlpha() + countLevelTwoAlpha() - countLevelOneDicompBS();
                defFile.setModelBetweenCount(String.valueOf(betweenCount));
                System.out.println("From defHelper | Model Between Count: " + defFile.getModelBetweenCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;
                int withinCount = countLevelOneTau() + countLevelTwoTau() - countLevelOneDicompWS();
                defFile.setModelWithinCount(String.valueOf(withinCount));
                System.out.println("From defHelper | Model Within Count: " + defFile.getModelWithinCount());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            //Number of disaggregate means
            try {
                tryCount = 1;
                defFile.setDecompMeanCount(String.valueOf(countLevelOneDicompMean()));
                System.out.println("From defHelper | Stage 1 Decomp Model Mean Count: " + defFile.getDecompMeanCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            //Number of disaggregate BS Variance
            try {
                tryCount = 1;
                defFile.setDecompBSCount(String.valueOf(countLevelOneDicompBS()));
                System.out.println("From defHelper | Stage 1 BS Variance Disagg. Regressor Count: " + defFile.getDecompBSCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            //Number of disaggregate WS Variance
            try {
                tryCount = 1;
                defFile.setDecompWSCount(String.valueOf(countLevelOneDicompWS()));
                System.out.println("From defHelper | Stage 1 WS Variance Disagg Regressor Count: " + defFile.getDecompWSCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            // ---- Check if the association radio buttons have been selected (Advanced effect of mean) ----
            //count field array sizes
            if (NoAssociationRadio.isSelected()) {

                try {
                    defFile.setAdvancedEffectMeanWS(String.valueOf(0));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (No Association): " + defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (LinearAssociationRadio.isSelected()) {
                try {
                    defFile.setAdvancedEffectMeanWS(String.valueOf(1));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (Linear Association): " + defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (QuadraticAssociationRadio.isSelected()) {
                try {
                    defFile.setAdvancedEffectMeanWS(String.valueOf(2));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (Quadratic Association): " + defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } // field array counting ends

            // get selected regressor labels and read them into defFile
            try {
                defFile.setLabelModelMeanRegressors(ModelMeansLabelsArray());
                System.out.println("From defHelper | Stage 1 MEAN REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelMeanRegressors()));
                defFile.setLabelModelMeanRegressorsLevelOne(getModelMeanLabelsLevelOne());
                System.out.println("From defHelper | LEVEL 1 MEAN REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelMeanRegressorsLevelOne()));
                defFile.setLabelModelMeanRegressorsLevelTwo(getModelMeanLabelsLevelTwo());
                System.out.println("From defHelper | LEVEL 2 MEAN REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelMeanRegressorsLevelTwo()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setLabelModelBSRegressors(ModelBSLabelsArray());
                System.out.println("From defHelper | Stage 1 BS REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelBSRegressors()));
                defFile.setLabelModelBSRegressorsLevelOne(getModelBSLabelsLevelOne());
                System.out.println("From defHelper | LEVEL 1 BS REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelBSRegressorsLevelOne()));
                defFile.setLabelModelBSRegressorsLevelTwo(getModelBSLabelsLevelTwo());
                System.out.println("From defHelper | LEVEL 2 BS REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelBSRegressorsLevelTwo()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setLabelModelWSRegressors(ModelWSLabelsArray());
                System.out.println("From defHelper | Stage 1 WS REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelWSRegressors()));
                defFile.setLabelModelWSRegressorsLevelOne(getModelWSLabelsLevelOne());
                System.out.println("From defHelper | LEVEL 1 WS REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelWSRegressorsLevelOne()));
                defFile.setLabelModelWSRegressorsLevelTwo(getModelWSLabelsLevelTwo());
                System.out.println("From defHelper | LEVEL 2 WS REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelWSRegressorsLevelTwo()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            // Reads the variable names of variables that have been selected as mean regressors
            try {
                defFile.setFieldModelMeanRegressors(fieldModelMeanArray());
                System.out.println("From defHelper | #Stage One Mean Regressors: " + defFile.getFieldModelMeanRegressors().length);
                System.out.println("From defHelper | Stage One Mean Regressors Selected: " + Arrays.toString(defFile.getFieldModelMeanRegressors()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            // Reads the variable names of variables that have been selected as BS Variances
            try {
                defFile.setFieldModelBSRegressors(fieldModelBSArray());
                System.out.println("From defHelper | #Stage One BS Regressors: " + defFile.getFieldModelBSRegressors().length);
                System.out.println("From defHelper | Stage One BS Var. Regressors Selected: " + Arrays.toString(defFile.getFieldModelBSRegressors()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            // Reads the variable names of variables that have been selected as WS Variances
            try {
                defFile.setFieldModelWSRegressors(fieldModelWSArray());
                System.out.println("From defHelper | #Stage One WS Regressors: " + defFile.getFieldModelWSRegressors().length);
                System.out.println("From defHelper | Stage One WS Var. Regressors Selected: " + Arrays.toString(defFile.getFieldModelWSRegressors()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setFieldDecompMeanRegressors(getMeanDecompFieldRegressorLabels_levelOne());
                System.out.println("From defHelper | #Stage One Mean + Disagg. Regressors: " + defFile.getFieldDecompMeanRegressors().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setFieldDecompBSRegressors(getBSDecompFieldRegressorLabels_levelOne());
                System.out.println("From defHelper | #Stage One BS + Disagg. Regressors: " + defFile.getFieldDecompBSRegressors().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setFieldDecompWSRegressors(getWSDecompFieldRegressorLabels_levelOne());
                System.out.println("From defHelper | #Stage One WS + Disagg. Regressors: " + defFile.getFieldDecompWSRegressors().length);
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setLabelDecompMeanRegressors(getDecompMeanLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + Mean Labels: " + Arrays.toString(defFile.getLabelDecompMeanRegressors()));
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setLabelDecompBSRegressors(getDecompBSLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + BS Labels: " + Arrays.toString(defFile.getLabelDecompBSRegressors()));
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setLabelDecompWSRegressors(getDecompWSLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + WS Labels: " + Arrays.toString(defFile.getLabelDecompWSRegressors()));
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            //******************************************************************
        } else if (i > 1) {

            try {
                tryCount = 1;
                int MeanCount = countLevelOneBeta() + countLevelTwoBeta() - countLevelOneDicompMean();

                // count total mean regressors in level one and level two
                defFile.setModelMeanCount(String.valueOf(MeanCount));
                System.out.println("From defHelper | Stage 1 Model Mean Count: " + defFile.getModelMeanCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                int LocRanCount = countLevelOneAlpha() + countLevelTwoAlpha() - countLevelOneDicompBS();
                // count total random location regressors in level one and level two
                defFile.setModelLocRanCount(String.valueOf(LocRanCount));
                System.out.println("From defHelper | Stage 1 Model Loc Ran Count: " + defFile.getModelLocRanCount().toString());
                tryCount = 1;
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;
                int ScaleCount = countLevelOneTau() + countLevelTwoTau() - countLevelOneDicompWS();
                // count total scale regressors in level one and level two
                defFile.setModelScaleCount(String.valueOf(ScaleCount));
                System.out.println("From defHelper | Stage 1 Model Scale Count: " + defFile.getModelScaleCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;

                //Disagg means count
                defFile.setDecompMeanCount(String.valueOf(countLevelOneDicompMean()));
                System.out.println("From defHelper | Stage 1 Decomp Model Mean Count: " + defFile.getDecompMeanCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;
                //Disagg Random Location count
                defFile.setDecompLocRanCount(String.valueOf(countLevelOneDicompBS()));
                System.out.println("From defHelper | Stage 1 Decomp Model Loc Random Count: " + defFile.getDecompLocRanCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;
                //Disagg scale count
                defFile.setDecompScaleCount(String.valueOf(countLevelOneDicompWS()));
                System.out.println("From defHelper | Stage 1 Decomp Scale Count: " + defFile.getDecompScaleCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            //Check if the effect of mean on WS variances options have been selected
            if (NoAssociationRadio.isSelected()) {

                try {
                    defFile.setAdvancedEffectMeanWS(String.valueOf(1));
                    System.out.println("From defHelper | Stage 1 Association of random location & scale?: " + defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (LinearAssociationRadio.isSelected()) {
                try {
                    defFile.setAdvancedEffectMeanWS(String.valueOf(0));
                    System.out.println("From defHelper | Stage 1 Association of random location & scale?: " + defFile.getAdvancedEffectMeanWS());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
            }

            try {
                defFile.setLabelModelMeanRegressors(ModelMeansLabelsArray());
                System.out.println("From defHelper | Stage 1 MEAN REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelMeanRegressors()));
                defFile.setLabelModelMeanRegressorsLevelOne(getModelMeanLabelsLevelOne());
                System.out.println("From defHelper | LEVEL 1 MEAN REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelMeanRegressorsLevelOne()));
                defFile.setLabelModelMeanRegressorsLevelTwo(getModelMeanLabelsLevelTwo());
                System.out.println("From defHelper | LEVEL 2 MEAN REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelMeanRegressorsLevelTwo()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setLabelModelLocRanRegressors(ModelBSLabelsArray());
                System.out.println("From defHelper | Stage 1 LocRan REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelLocRanRegressors()));
                defFile.setLabelModelLocRanRegressorsLevelOne(getModelBSLabelsLevelOne());
                System.out.println("From defHelper | LEVEL 1 LocRan REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelLocRanRegressorsLevelOne()));
                defFile.setLabelModelLocRanRegressorsLevelTwo(getModelBSLabelsLevelTwo());
                System.out.println("From defHelper | LEVEL 2 LocRan REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelLocRanRegressorsLevelTwo()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setLabelModelScaleRegressors(ModelWSLabelsArray());
                System.out.println("From defHelper | Stage 1 Scale REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelScaleRegressors()));
                defFile.setLabelModelScaleRegressorsLevelOne(getModelWSLabelsLevelOne());
                System.out.println("From defHelper | LEVEL 1 Scale REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelScaleRegressorsLevelOne()));
                defFile.setLabelModelScaleRegressorsLevelTwo(getModelWSLabelsLevelTwo());
                System.out.println("From defHelper | LEVEL 2 Scale REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelScaleRegressorsLevelTwo()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            // count field labels
            try {
                // get variable names from selected mean regressors
                defFile.setFieldModelMeanRegressors(fieldModelMeanArray());
                System.out.println("From defHelper | #Stage One Mean Regressors: " + defFile.getFieldModelMeanRegressors().length); //check this ===============
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                // get variable names from selected random location regressors
                defFile.setFieldModelLocRanRegressors(fieldModelBSArray());
                System.out.println("From defHelper | #Stage One BS(RanLoc) Regressors: " + defFile.getFieldModelLocRanRegressors().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                // get variable names from selected scale regressors
                defFile.setFieldModelScaleRegressors(fieldModelWSArray());
                System.out.println("From defHelper | #Stage One WS(Scale) Regressors: " + defFile.getFieldModelScaleRegressors().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setFieldDecompMeanRegressors(getMeanDecompFieldRegressorLabels_levelOne());
                System.out.println("From defHelper | #Stage One Mean + Disagg. Regressors: " + defFile.getFieldDecompMeanRegressors().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setFieldDecompLocRanRegressors(getBSDecompFieldRegressorLabels_levelOne());
                //defFile.setFieldDecompLocRanRegressors(fieldModelBSArray());
                System.out.println("From defHelper | #Stage One BS(RanLoc) + Disagg. Regressors: " + defFile.getFieldDecompLocRanRegressors().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
            try {
                defFile.setFieldDecompScaleRegressors(getWSDecompFieldRegressorLabels_levelOne());
                System.out.println("From defHelper | #Stage One WS(Scale) + Disagg. Regressors: " + defFile.getFieldDecompScaleRegressors().length);
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
            try {
                defFile.setLabelDecompMeanRegressors(getDecompMeanLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + Mean Labels: " + Arrays.toString(defFile.getLabelDecompMeanRegressors()));
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
            try {
                defFile.setLabelDecompLocRanRegressors(getDecompBSLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + LocRan Labels: " + Arrays.toString(defFile.getLabelDecompLocRanRegressors()));
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
            try {
                defFile.setLabelDecompScaleRegressors(getDecompWSLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + Scale Labels: " + Arrays.toString(defFile.getLabelDecompScaleRegressors()));
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
        }

        if (outcomeNone == true) {

            if (catchCount == 0) {
                int defTry = 0;
                int defCatch = 0;
                try {
                    List<String> defFileOutput;

                    defFile.writeStageOneOnlyDefFileToFolder();

                    //defFileOutput = defFile.buildStageOneOnlyDefinitonList();
                    System.out.println("From defHelper | Stage 1 def file created successfully!");

                } catch (Exception ex) {
                    defCatch = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    System.out.println("From defHelper | Stage 1 def file failed!");
                }

                if (defCatch == 0) {
                    stageOneTabs.setSelectedIndex(3);
                    stageOneTabs.setEnabledAt(5, false);
                }

            } else {

                //stageOneTabs.setSelectedIndex(1);
                //System.out.println("outcome not true!!!!");
            }

        } else {
            stageOneTabs.setSelectedIndex(2);
            System.out.println("outcome not none!!!!");
        }

        try {
            produceStageOneOutput();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_startStageTwoActionPerformed

    private void LinearAssociationRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LinearAssociationRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LinearAssociationRadioActionPerformed

    private void NoAssociationRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoAssociationRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoAssociationRadioActionPerformed

    private void advancedOptionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advancedOptionsButtonActionPerformed
        // TODO add your handling code here:
        //advancedOptions_view = new advancedOptions();

        advancedOptions_view.setVisible(true);
    }//GEN-LAST:event_advancedOptionsButtonActionPerformed

    private void addStageOneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStageOneButtonActionPerformed

        IDpos = IDvariableCombo.getSelectedIndex();
        stageOnePos = StageOneVariableCombo.getSelectedIndex();
        stageTwoPos = stageTwoOutcome.getSelectedIndex();

        if (stage_1_regs.isVisible()) { //if it is already open and visible in the background

            // stage_1_regs.set
            stage_1_regs.setFocusable(true);
            stageOneClicked = 1;
            addStageTwoTabTwo.setEnabled(true);

        } else {
            //stage_1_regs.revalidate();
            //stage_1_regs.repaint();
            // stage_1_regs.removeAll();

            stageOneClicked = 1;
            addStageTwoTabTwo.setEnabled(true);

            //            if (levelOneRegSize == 0 && levelTwoRegSize ==0){
                //
                //            //refresh as normal
                //            } else {
                //
                //
                //            }
            if (addStageOneCHecked == true) {

                stage_1_regs.setVisible(true);
                stage_1_regs.updateStageOneAgain();
            } else {
                stage_1_regs.setVisible(true);
                stage_1_regs.updateAllVariables();
            }
        }

        addStageOneCHecked = true;

    }//GEN-LAST:event_addStageOneButtonActionPerformed

    private void IDvariableComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDvariableComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDvariableComboActionPerformed

    private void IDvariableComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_IDvariableComboItemStateChanged
        // TODO add your handling code here:

        IDpos = IDvariableCombo.getSelectedIndex();
        System.out.println("ID CHANGED: " + String.valueOf(IDpos));
        isIDChanged = true;
    }//GEN-LAST:event_IDvariableComboItemStateChanged

    private void StageOneVariableComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StageOneVariableComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StageOneVariableComboActionPerformed

    private void StageOneVariableComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_StageOneVariableComboItemStateChanged
        // TODO add your handling code here:
        stageOnePos = StageOneVariableCombo.getSelectedIndex();
        System.out.println("STAGE ONE OUTCOME CHANGED: " + String.valueOf(stageOnePos));
        isStageOneOutcomeChanged = true;
    }//GEN-LAST:event_StageOneVariableComboItemStateChanged

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // TODO add your handling code here:
       
        IDvariableCombo.setSelectedIndex(0);
        StageOneVariableCombo.setSelectedIndex(1);
        stageTwoOutcome.setSelectedIndex(2);
        
        buttonGroup1.clearSelection();
        
        //addStageTwoTabTwo.setEnabled(false);

        levelTwoPanel.removeAll();
        levelTwoPanel.revalidate();
        levelTwoPanel.repaint();

        levelOnePanel.removeAll();
        levelOnePanel.revalidate();
        levelOnePanel.repaint();
        
        stage_1_regs.updateAllVariables();

        stage_1_regs.levelOneList.clear();
        stage_1_regs.levelTwoList.clear();
        updateLevelTwoGrid_version2(stage_1_regs.levelTwoList);
        updateLevelOneGrid_version2(stage_1_regs.levelOneList);
        
    }//GEN-LAST:event_resetButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        InstructionsGUI instructions = new InstructionsGUI();
        instructions.setLocationRelativeTo(mxr);
        instructions.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void fileBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileBrowseButtonActionPerformed
        //JFileChooser fileChooser = new JFileChooser();
        //fileChooser.showOpenDialog(null);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Data files", "csv");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // File file = fileChooser.getSelectedFile();
            // What to do with the file, e.g. display it in a TextArea
            //textarea.read( new FileReader( file.getAbsolutePath() ), null );
            //Select file from the file object
            file = fileChooser.getSelectedFile();
            //get file path to display on the text box
            String fileName = file.getAbsolutePath();
            dataFileNameRef = fileName;
            filePath.setText(fileName);
            //enable other buttons here:
            titleField.setEnabled(true);
            //subtitleField.setEnabled(true);
            //randomLocationEffects.setEnabled(true);
            oneRLERadio.setEnabled(true);
            moreThanOneRLERadio.setEnabled(true);
            continuousRadio.setEnabled(true);
            dichotomousRadio.setEnabled(true);
            // randomLocationEffects.setEnabled(true);
            newModelSubmit.setEnabled(true);
            // newModelMissingValues.setEnabled(true);
            missingValuePresent.setEnabled(true);
            missingValueAbsent.setEnabled(true);
            // newModelMissingValueCode.setEnabled(true);
            noneRadio.setEnabled(true);
            newModel_resetButton.setEnabled(true);
            randomScaleCheckBox.setEnabled(true);
            randomScaleCheckBox.setSelected(true);
            // newModelMissingValueCode.selectAll();
            System.out.println(file.getAbsolutePath());
        } else {
            System.out.println("File access cancelled by user.");
        }

    }//GEN-LAST:event_fileBrowseButtonActionPerformed

    private void filePathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filePathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filePathActionPerformed

    private void titleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titleFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_titleFieldActionPerformed

    private void randomScaleCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_randomScaleCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_randomScaleCheckBoxActionPerformed

    private void continuousRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continuousRadioActionPerformed
        // TODO add your handling code here:
        if (noneRadio.isSelected()){
            //Do nothing

        } else {
            setSeedLabel.setVisible(true);
            seedTextBox.setVisible(true);
            seedHelpButton.setVisible(true);
            String seedVal = generateSeed();
            seedTextBox.setText(seedVal);
        }
    }//GEN-LAST:event_continuousRadioActionPerformed

    private void dichotomousRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dichotomousRadioActionPerformed
        // TODO add your handling code here:
        if (noneRadio.isSelected()){
            //Do nothing
        } else {
            setSeedLabel.setVisible(true);
            seedTextBox.setVisible(true);
            seedHelpButton.setVisible(true);
            String seedVal = generateSeed();
            seedTextBox.setText(seedVal);
        }
    }//GEN-LAST:event_dichotomousRadioActionPerformed

    private void noneRadioStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_noneRadioStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_noneRadioStateChanged

    private void noneRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noneRadioActionPerformed
        // TODO add your handling code here:
        if (noneRadio.isSelected()){
            //Do nothing
            setSeedLabel.setVisible(false);
            seedTextBox.setVisible(false);
            seedHelpButton.setVisible(false);
            seedTextBox.setText("0");
        } else {
            setSeedLabel.setVisible(true);
            seedTextBox.setVisible(true);
            seedHelpButton.setVisible(true);
            String seedVal = generateSeed();
            seedTextBox.setText(seedVal);
        }

    }//GEN-LAST:event_noneRadioActionPerformed

    private void missingValueAbsentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_missingValueAbsentActionPerformed
        // TODO add your handling code here:
        if (missingValueAbsent.isSelected()) {
            newModelMissingValueCode.setEnabled(false);
            newModelMissingValueCode.setText("");
        }
    }//GEN-LAST:event_missingValueAbsentActionPerformed

    private void missingValuePresentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_missingValuePresentActionPerformed
        // TODO add your handling code here:
        if (missingValuePresent.isSelected()) {
            newModelMissingValueCode.setEnabled(true);
            newModelMissingValueCode.setText("-999");
            newModelMissingValueCode.selectAll();
        }
    }//GEN-LAST:event_missingValuePresentActionPerformed

    private void newModelMissingValueCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newModelMissingValueCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newModelMissingValueCodeActionPerformed

    private void newModelMissingValueCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newModelMissingValueCodeKeyTyped
        // TODO add your handling code here:

        char vchar = evt.getKeyChar();
        //        if (!((Character.isDigit(vchar)) || (vchar == KeyEvent.VK_BACKSPACE) || (vchar == KeyEvent.VK_DELETE) || (vchar == KeyEvent.VK_MINUS))) {
            //            evt.consume();
            //        }
        if (!((Character.isDigit(vchar)) || (vchar == KeyEvent.VK_BACK_SPACE) || (vchar == KeyEvent.VK_DELETE) || (vchar == KeyEvent.VK_MINUS) || (vchar == '.'))) {
            evt.consume();
        }
    }//GEN-LAST:event_newModelMissingValueCodeKeyTyped

    private void newModel_resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newModel_resetButtonActionPerformed
        // TODO add your handling code here:
        confirmReset resetBox = new confirmReset(mxr, false);
        resetBox.setLocationRelativeTo(mxr);
        resetBox.setVisible(true);

             /*   
        filePath.setText("");
        titleField.setText("");
        titleField.setEnabled(false);
        buttonGroup2.clearSelection();
        buttonGroup3.clearSelection();
        buttonGroup4.clearSelection();
        randomScaleCheckBox.setSelected(false);
        newModelMissingValueCode.setText("");
        seedTextBox.setText("");
        
        variableArray = null;
        isRandomScale = false;
        dataFileNameRef = null;
        missingValue = "0";
        defFile = null;
        modelBuilder = null;
        
        outPutStageTwo = null;
        
        resetButtonActionPerformed(evt);
        jButton1ActionPerformed(evt);
        */
        //updateMixRegGUI();
    }//GEN-LAST:event_newModel_resetButtonActionPerformed

    private void newModelSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newModelSubmitActionPerformed

        // System.getProperty("os.name");
        //        String osName = System.getProperty("os.name");
        //        System.out.println("YOUR OPERATING SYSTEM IS: " + osName);
        //        if (osName.contains("Windows")){
            //            System.out.println("YES THE OS IS WINDOWS");
            //        } else {
            //            System.out.println("NO THE OS IS NOT WINDOWS");
            //        }
        System.out.println("Model submitted" + " called");

        if (validateFields() == true) {
            System.out.print("condition is true");
            if (oneRLERadio.isSelected() == true) {
                RLE = 1;
            } else if (moreThanOneRLERadio.isSelected() == true) {
                RLE = 2;
            }

            defFile = new DefinitionHelper(RLE, !isOutcomeContinous());
            // modelBuilder = new ModelBuilder(defFile);
            System.out.println("RLE: " + String.valueOf(RLE));

            defFile.modelSelector(RLE, isOutcomeContinous());

            if (missingValuePresent.isSelected()) {

                System.out.println("NEW MODEL | MISSING VALUE = " + newModelMissingValueCode.getText());

                try {
                    System.out.println("NEW MODEL | MISSING VALUE = " + newModelMissingValueCode.getText());
                    defFile.setAdvancedMissingValue(String.valueOf(newModelMissingValueCode.getText()));

                    System.out.println("From defHelper | Missing Value: " + defFile.getAdvancedMissingValue());
                } catch (Exception ex) {
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE);
                }

            } else if (missingValueAbsent.isSelected()) {
                try {
                    defFile.setAdvancedMissingValue(String.valueOf(missingValue));
                    System.out.println("From defHelper | Missing Value: " + defFile.getAdvancedMissingValue());
                } catch (Exception ex) {
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE);
                }
                //do nothing
            }

            try {
                //convert csv to .dat file ...
                //defFile.
                defFile.csvToDatConverter(file);
            } catch (IOException ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE);
            }

            System.out.println("MODEL SELECTOR: " + String.valueOf(defFile.getSelectedModel()));

            if (filePath.getText().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "Please upload a .csv file to start your analysis", "Caution!", JOptionPane.INFORMATION_MESSAGE);
            } else {

                try {

                    // Read file contents
                    Scanner inputStream = new Scanner(file);

                    // Read variable names from row 1
                    String variableNames = inputStream.next();
                    variableArray = variableNames.split(",");   
                    System.out.println("Variables are: " + Arrays.toString(variableArray));
                    // save all variables in an array
                    String[] varTemp = getVariableNames();
                    defFile.setDataFilename(extractDatFilePath());
                    defFile.setDataVariableCount(String.valueOf(variableArray.length));
                    System.out.println("From defHelper | Variable count: " + defFile.getDataVariableCount());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                }

                // Read random location effects from new Model
                //RLE = (Integer) randomLocationEffects.getValue();
                NoneVar = isOutcomeNone();
                outComeBoolean = isOutcomeContinous();
                System.out.println("NoneVar: " + String.valueOf(NoneVar));
                System.out.println(String.valueOf(isOutcomeContinous()));
                System.out.println("IsOutcomeNone: " + String.valueOf(isOutcomeNone()));
                // set Values in def helper
                defFile.setModelTitle(getTitle());
                System.out.println("From defHelper | Title: " + defFile.getModelTitle());
                //defFile.setModelSubtitle(getSubTitle());
                System.out.println("From defHelper | Subtitle: " + defFile.getModelSubtitle());

                //Check if the randome scale is checked or not
                if (randomScaleCheckBox.isSelected()) {
                    isRandomScale = true;
                } else {
                    isRandomScale = false;
                }

                defFile.setModelSubtitle("Created with MixWILD GUI");
                System.out.println("From defHelper | Subtitle: " + defFile.getModelSubtitle());

                //set advanced options defaults and assign values to defition library
                try {
                    defFile.setModelFixedInt(String.valueOf(0));
                    System.out.println("From defHelper | Mean SubModel Checked?: " + defFile.getModelFixedInt());
                    //tryCount = 1;
                } catch (Exception ex) {
                    //catchCount = 1;
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setModelRandomInt(String.valueOf(0));
                    defFile.setModelBetweenInt(String.valueOf(0));
                    System.out.println("From defHelper | BS SubModel Checked?: " + defFile.getModelRandomInt());
                    System.out.println("From defHelper | BS SubModel Checked?: " + defFile.getModelBetweenInt());
                } catch (Exception ex) {
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setModelScaleInt(String.valueOf(0));
                    defFile.setModelWithinInt(String.valueOf(0));
                    System.out.println("From defHelper | WS SubModel Checked?: " + defFile.getModelScaleInt());
                    System.out.println("From defHelper | WS SubModel Checked?: " + defFile.getModelWithinInt());
                    //tryCount = 1;
                } catch (Exception ex) {
                    //catchCount = 1;
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedAdaptiveQuad(String.valueOf(1));
                    System.out.println("From defHelper | Adaptive Quadriture Checked?: " + defFile.getAdvancedAdaptiveQuad());
                    //tryCount = 1;
                } catch (Exception ex) {
                    //catchCount = 1;
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedConvergence(String.valueOf(0.00001));
                    System.out.println("From defHelper | Convergence: " + defFile.getAdvancedConvergence());
                    //tryCount = 1;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    //catchCount = 1;
                }
                try {
                    defFile.setAdvancedQuadPoints(String.valueOf(11));
                    System.out.println("From defHelper | Quadriture Points: " + defFile.getAdvancedQuadPoints());
                    //tryCount = 1;
                } catch (Exception ex) {
                    //catchCount = 1;
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedMaxIteration(String.valueOf(200));
                    System.out.println("From defHelper | Maximum Iteraions: " + defFile.getAdvancedMaxIteration());
                    //tryCount = 1;
                } catch (Exception ex) {
                    //catchCount = 1;
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedRidge(String.valueOf(0.15));
                    System.out.println("From defHelper | Ridge: " + defFile.getAdvancedRidge());
                    //tryCount = 1;
                } catch (Exception ex) {
                    //catchCount = 1;
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedCutoffLower("0");
                    System.out.println("CUT OFF: " + defFile.getAdvancedCutoffLower());
                } catch (Exception ex) {
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedDiscardSubjects("0");
                    System.out.println("DISCARD SUBJECTS: " + defFile.getAdvancedDiscardSubjects());

                } catch (Exception ex) {
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedCenterScale(String.valueOf(0));
                    System.out.println("From defHelper | Scale Regressor: " + defFile.getAdvancedCenterScale());
                } catch (Exception ex) {
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedResampleCount("500");
                    System.out.println("From defHelper | Resample count: " + defFile.getAdvancedResampleCount());
                } catch (Exception ex) {
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

                try {
                    defFile.setSeedForStageTwo(seedTextBox.getText());
                    System.out.println("From defHelper | SEED: " + defFile.getSeedForStageTwo());
                } catch (Exception ex) {
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

                defFile.setOutputPrefix(extractDatFileName() + "_Output");
                System.out.println("From defHelper | Output file name: " + defFile.getOutputPrefix());

                if (randomScaleCheckBox.isSelected()) {
                    try {
                        defFile.setAdvancedRandomScaleNotIncluded("0");
                        System.out.println("IS RANDOM SCALE INCLUDED: " + defFile.getAdvancedRandomScaleNotIncluded());
                    } catch (Exception ex) {
                        Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    }

                } else {

                    try {
                        defFile.setAdvancedRandomScaleNotIncluded("1");
                        System.out.println("IS RANDOM SCALE INCLUDED: " + defFile.getAdvancedRandomScaleNotIncluded());
                    } catch (Exception ex) {
                        Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    }

                }

                if (isOutcomeNone()) {
                    try {
                        defFile.setAdvancedDropSecondStage("1");
                        System.out.println("DROP SECOND STAGE?: " + defFile.getAdvancedDropSecondStage());
                    } catch (Exception ex) {
                        Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    }

                } else {

                    try {
                        defFile.setAdvancedDropSecondStage("0");
                        System.out.println("DROP SECOND STAGE?: " + defFile.getAdvancedDropSecondStage());
                    } catch (Exception ex) {
                        Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    }

                }

                //set conditions here:
                if (newModelMissingValueCode.getText().equals("0") || newModelMissingValueCode.getText().equals("00") || newModelMissingValueCode.getText().equals("000")) {
                    //show message alert here:
                    JOptionPane.showMessageDialog(null, "Invalid missing value code, 0 implies there are no missing values. Please use some other value. E.g., -999", "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                } else {
                    // TODO: fix this stuff xyz
                    isSubmitClicked();
                    setVisible(true);
                    randomLocationEffectsLabel.setText("Random location effects: " + randomLocationEffects());
                    stageTwoOutcomePrintLabel.setText("Stage 2 outcome: " + outComeTypeString());
                    //Update ID, stage one and stage two variable comboboxes
                    updateMixRegGUI();
                    updateComboBoxes();
                    stageOneTabs.setSelectedIndex(1);
                    newModelSubmit.setEnabled(false);
                    newModelSubmit.setVisible(false);
                    fileBrowseButton.setEnabled(false);
                    fileBrowseButton.setVisible(false);
                    filePath.setEnabled(false);
                    titleField.setEnabled(false);
                    oneRLERadio.setEnabled(false);
                    moreThanOneRLERadio.setEnabled(false);
                    randomScaleCheckBox.setEnabled(false);
                    continuousRadio.setEnabled(false);
                    dichotomousRadio.setEnabled(false);
                    noneRadio.setEnabled(false);
                    missingValuePresent.setEnabled(false);
                    missingValueAbsent.setEnabled(false);
                    newModelMissingValueCode.setEnabled(false);
                    seedTextBox.setEnabled(false);
                    seedHelpButton.setVisible(false);
                    
                }

            }

            try {
                getDataFromCSV();
                printFileName();
                System.out.println("NEW MODEL DATA READ");
            } catch (IOException ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

        } else {

            System.out.println("VALIDATION OF FIELDS: " + String.valueOf(false));

        }
    }//GEN-LAST:event_newModelSubmitActionPerformed

    private void moreThanOneRLERadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moreThanOneRLERadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_moreThanOneRLERadioActionPerformed

    private void oneRLERadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oneRLERadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_oneRLERadioActionPerformed

    private void seedHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seedHelpButtonActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null, "A seed is a number used to initialize a random number generator. Different seeds produce different sequences of random numbers."
                + "\n" + "In the context of two-stage models, a seed is helpful for replicating models with identical results.", "What is a seed?", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_seedHelpButtonActionPerformed

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
                mxr = new mixregGUI();
                mxr.setLocationRelativeTo(null);
                mxr.setVisible(true);
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
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.JRadioButton continuousRadio;
    private javax.swing.JLabel dataFileLabel;
    public static javax.swing.JTable dataTable;
    private javax.swing.JRadioButton dichotomousRadio;
    private javax.swing.JTextArea equationArea;
    private javax.swing.JButton fileBrowseButton;
    private javax.swing.JTextField filePath;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton8;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
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
    private javax.swing.JRadioButton missingValueAbsent;
    private javax.swing.JRadioButton missingValuePresent;
    private javax.swing.JRadioButton moreThanOneRLERadio;
    private javax.swing.JTextField newModelMissingValueCode;
    private javax.swing.JButton newModelSubmit;
    private javax.swing.JButton newModel_resetButton;
    private javax.swing.JRadioButton noneRadio;
    private javax.swing.JRadioButton oneRLERadio;
    private javax.swing.JTextPane outCategoryDisplay;
    private javax.swing.JButton outcomeCatButton;
    private javax.swing.JPanel parentPanel;
    public static javax.swing.JLabel printedFileName;
    public static javax.swing.JLabel randomLocationEffectsLabel;
    private javax.swing.JCheckBox randomScaleCheckBox;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton runTabTwoStageOneTwo;
    private javax.swing.JButton saveStage2OutButton;
    private javax.swing.JButton seedHelpButton;
    private javax.swing.JTextField seedTextBox;
    private javax.swing.JLabel setSeedLabel;
    public static javax.swing.JTextArea stageOneOutput;
    private javax.swing.JTabbedPane stageOneTabs;
    private javax.swing.JComboBox<String> stageTwoOutcome;
    public static javax.swing.JLabel stageTwoOutcomePrintLabel;
    public static javax.swing.JTextArea stageTwoOutput;
    private javax.swing.JPanel stageTwoPanel;
    private javax.swing.JPanel stageTwoRegsGrid;
    private javax.swing.JButton startStageTwo;
    private javax.swing.JCheckBox suppressIntCheckBox;
    private javax.swing.JTextField titleField;
    // End of variables declaration//GEN-END:variables

    public void isSubmitClicked() {
        IDvariableCombo.setSelectedIndex(0);
        StageOneVariableCombo.setSelectedIndex(1);
        stageTwoOutcome.setSelectedIndex(2);

        buttonGroup1.clearSelection();
        
        //addStageTwoTabTwo.setEnabled(false);

        levelTwoPanel.removeAll();
        levelTwoPanel.revalidate();
        levelTwoPanel.repaint();

        levelOnePanel.removeAll();
        levelOnePanel.revalidate();
        levelOnePanel.repaint();

        parentPanel.removeAll();
        parentPanel.add(stageOneTabs);
        parentPanel.repaint();
        parentPanel.revalidate();
    }

    //Updates IDs and outcome variables list
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

    //Open a web link from the software
    public static void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get a list of variables remaininag from stage 1 
    public DefaultListModel<String> getSavedVariables() {

        int index = stageTwoOutcome.getSelectedIndex();

        //DefaultListModel<String> tempModel = stage_1_regs.getListModel();
        DefaultListModel<String> tempModel = stage_1_regs.getListModel();

        tempModel.removeElement(stageTwoOutcome.getSelectedItem());

        savedVariablesStageOne = tempModel;

        return savedVariablesStageOne;

    }

    //get ID variable selected by the user
    public int getIDVariable() {
        // String ID;

        int pos = IDvariableCombo.getSelectedIndex();

        return pos;
    }

    //Get the position of ID variable in the data file
    public static int getIDFieldPosition() {

        return IDpos;
    }

    //get Stage One DV variable selected by the user
    public String getStageOneDV() {
        String StageOneDV;

        StageOneDV = StageOneVariableCombo.getItemAt(StageOneVariableCombo.getSelectedIndex());

        return StageOneDV;

    }

    //Get stage 1 dv position in data file
    public static int getStageOneDVFieldPosition() {

        return stageOnePos;
    }

    //get Stage Two variable selected by the user
    public String getStageTwoDV() {
        String StageTwoDV;

        StageTwoDV = stageTwoOutcome.getItemAt(stageTwoOutcome.getSelectedIndex());

        return StageTwoDV;
    }

    //Get stage 2 dv position in data file
    public static int getStageTwoDVFieldPosition() {

        return stageTwoPos;
    }

    //Update level 1 table with regressors
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

    //Update level 2 table with regressors
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

            if (isRandomScale) {

                levelTwoBoxes.get(j).get(2).setEnabled(true);

            } else {

                levelTwoBoxes.get(j).get(2).setEnabled(false);

            }

            if (i > 1) {
                levelTwoBoxes.get(j).get(1).setVisible(false);

            } else {

                levelTwoBoxes.get(j).get(1).setVisible(true);
                levelTwoBoxes.get(j).get(1).setEnabled(true);

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

    //Update stage 2 table with selected regressors
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

                if (k == 1) {

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
                        randomChecked = false;
                        scaleChecked = false;
//                        System.out.println(disaggVarianceBoxes.size());
                    } else {
                        //disaggVarianceBoxes.get(row).get(column).setEnabled(false);
                        stageTwoGridBoxes.get(row).get(1).setEnabled(false);
                        stageTwoGridBoxes.get(row).get(1).setSelected(false);
                        stageTwoGridBoxes.get(row).get(2).setEnabled(false);
                        stageTwoGridBoxes.get(row).get(2).setSelected(false);
                        stageTwoGridBoxes.get(row).get(3).setSelected(false);
                        stageTwoGridBoxes.get(row).get(3).setEnabled(false);
                        randomChecked = false;
                        scaleChecked = false;
                        suppressIntCheckBox.setEnabled(false);
                        suppressIntCheckBox.setSelected(false);
                        
                    }

                }
            });

            stageTwoGridBoxes.get(j).get(1).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();
                    if (selected) {
                        scaleChecked = true;
                        if (randomChecked == true) {
                            if(!suppressed){
                                stageTwoGridBoxes.get(row).get(3).setEnabled(true);
                                stageTwoGridBoxes.get(row).get(3).setSelected(false);
                            }
                        }

                    } else {
                        scaleChecked = false;
                        if(!suppressed){
                            stageTwoGridBoxes.get(row).get(3).setEnabled(false);
                            stageTwoGridBoxes.get(row).get(3).setSelected(false);
                        }
                    }
                }
            });

            stageTwoGridBoxes.get(j).get(2).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();

                    if (selected) {
                        randomChecked = true;

                        if (scaleChecked == true) {
                            if(!suppressed){
                                stageTwoGridBoxes.get(row).get(3).setEnabled(true);
                                stageTwoGridBoxes.get(row).get(3).setSelected(false);
                            }
                        }

                    } else {
                        randomChecked = false;
                        if(!suppressed){
                            stageTwoGridBoxes.get(row).get(3).setEnabled(false);
                            stageTwoGridBoxes.get(row).get(3).setSelected(false);
                        }

                    }
                }
            });
            
        stageTwoGridBoxes.get(j).get(3).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();
                    
                    randomChecked = false;
                    scaleChecked = false;

                   suppressIntCheckBox.setEnabled(true);
                    
                    
                }
            });
            

        }

        stageTwoPanel.removeAll();
        stageTwoPanel.revalidate();
        stageTwoPanel.repaint();

        stageTwoPanel.add(scrollpanel);
        revalidate();

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

    public void disableLevelTwoRandomLocation() {

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
                        position.add(String.valueOf(q + 1));
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
                        position.add(String.valueOf(q + 1));
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
                        System.out.println("The current position to add in ArrayList is: " + String.valueOf(posIndex));
                        position.add(String.valueOf(q + 1));
                        System.out.println("Regressor position test: " + String.valueOf(q + 1));
                        System.out.println("From inside mixRegGUI | Position of this regressor: " + position.get(posIndex) + "@" + String.valueOf(posIndex));
                        System.out.println("Position array: " + position);
                        posIndex++;

                    }

                }
            }

        }
        System.out.println("Position Aray Size here: " + String.valueOf(position.size()));
        System.out.println("Position ArrayList of Means: " + Arrays.toString(position.toArray()));

        for (int testPos = 0; testPos < position.size(); testPos++) {
            System.out.println("This ArrayList contains: " + position.get(testPos));
        }

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
                        position.add(String.valueOf(q + 1));
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
                        position.add(String.valueOf(q + 1));
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
                        position.add(String.valueOf(q + 1));
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
                        position.add(String.valueOf(q + 1));
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
                        position.add(String.valueOf(q + 1));
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
                        position.add(String.valueOf(q + 1));
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
                regressorLabels.add(levelTwoSelected.get(p));
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
                        position.add(String.valueOf(q + 1));
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
                        position.add(String.valueOf(q + 1));
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
                        position.add(String.valueOf(q + 1));
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
                        position.add(String.valueOf(q + 1));
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

    public static void produceStageTwoOutput(File filename) throws FileNotFoundException, IOException {

        String outputFileName = FilenameUtils.removeExtension(getDataFileName()) + "_output_1" + ".out";
        //read file here
        FileReader reader = new FileReader(outputFileName);

    }

    public void produceStageOneOutput() throws FileNotFoundException {

    }

    public void saveStageTwoOutput() throws IOException {

        FileFilter filter = new FileNameExtensionFilter("TEXT FILE", "txt");

        JFileChooser saver = new JFileChooser("./");
        saver.setFileFilter(filter);
        int returnVal = saver.showSaveDialog(this);
        File file = saver.getSelectedFile();
        BufferedWriter writer = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(stageTwoOutput.getText());
                writer.close();
                JOptionPane.showMessageDialog(this, "Stage 2 output was Saved Successfully!",
                        "Success!", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Stage 2 output could not be Saved!",
                        "Error!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void saveStageOneOutput() {
        FileFilter filter = new FileNameExtensionFilter("TEXT FILE", "txt");

        JFileChooser saver = new JFileChooser("./");
        saver.setFileFilter(filter);
        int returnVal = saver.showSaveDialog(this);
        File file = saver.getSelectedFile();
        BufferedWriter writer = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(stageOneOutput.getText());
                writer.close();
                JOptionPane.showMessageDialog(this, "Stage 1 output was Saved Successfully!",
                        "Success!", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Stage 1 output could not be Saved!",
                        "Error!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void runMixedModels() {

        String absoluteJavaPath = System.getProperty("user.dir");
        String defFileName = executableModel(selectedModel);
        try {
            try {
                copyExecutable(defFilePath, selectedModel); //get the def file path after it is saved
                Process p = Runtime.getRuntime().exec("cmd /c dir && cd " + defFilePath + " && dir && "
                        + defFileName); // does it save it in the same directory //@Eldin: This is where it is copying it twice.
                //@Eldin: This is where we may want to keep the terminal open in the background.

                p.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = reader.readLine();
                while (line != null) {
                    System.out.println(line);
                    line = reader.readLine();
                }
            } catch (FileNotFoundException fnfe1) {
                System.out.println("File not found Exception");
            } catch (IOException e1) {
                System.out.println("IO Exception");
            }

            try {
                Process p = Runtime.getRuntime().exec("cmd /c dir && cd " + defFilePath + " && del /f " + defFileName);
                p.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = reader.readLine();
                while (line != null) {
                    System.out.println(line);
                    line = reader.readLine();
                }
            } catch (FileNotFoundException fnfe1) {
                System.out.println("File not found Exception 2");
            } catch (IOException e1) {
                System.out.println("IO Exception 2 ");
            }

            JOptionPane.showMessageDialog(null, defFilePath);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed");
        }

    }

    private String executableModel(int modelSelection) {
        switch (modelSelection) {
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

    private void copyExecutable(String absoluteDirectoryPath, int modelSelection) throws FileNotFoundException, IOException {
        String modelPath;
        String executableName = executableModel(modelSelection);
        switch (modelSelection) {
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

        OutputStream outputStream
                = new FileOutputStream(new File(absoluteDirectoryPath + executableName));

        int read;
        byte[] bytes = new byte[4096];

        while ((read = stream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, read);
        }
        stream.close();
        outputStream.close();
    }

    public void runMixRegLS_mixor() {

    }

    public int getStagetwoOutcomeCats() {
        ArrayList<String> ColumnsCustom = new ArrayList<>();
        ArrayList<String> UniqueList = new ArrayList<>();

        String dataFileName = getDataFileName();
        File file = new File(dataFileName);
        //first get the column
        BufferedReader br = null;
        String line = "";
        String commaSplitter = ",";
        //
        try {
            br = new BufferedReader(new FileReader(dataFileName));
            line = br.readLine(); //consumes the first row
            while ((line = br.readLine()) != null) {
                String[] Columns = line.split(commaSplitter);
                int index = stageTwoOutcome.getSelectedIndex();
                ColumnsCustom.add(Columns[index]);
            }
            System.out.println("COLUMN:");
            for (int k = 0; k < ColumnsCustom.size(); k++) {

                System.out.println(ColumnsCustom.get(k));
            }
            //count the unique ones
            for (int x = 0; x < ColumnsCustom.size(); x++) {
                if (UniqueList.contains(ColumnsCustom.get(x))) {
                    //do nothing
                } else if (ColumnsCustom.get(x).equals(defFile.getAdvancedMissingValue()) && !ColumnsCustom.get(x).equals("0")) { //compare if the category is a missing value, then don't consider it as a category
                    //do nothing

                } else {
                    UniqueList.add(ColumnsCustom.get(x));
                }
            }
            System.out.println("Number of unique categories: " + String.valueOf(UniqueList.size()));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Logger.getLogger(getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return UniqueList.size();
    }

    public String[] getStageTwoOutcomeValues() {

        System.out.println("Inside getStageTwoOutcomeValues");

        ArrayList<String> ColumnsCustom = new ArrayList<>();
        ArrayList<String> UniqueList = new ArrayList<>();
        ArrayList<Integer> UniqueIntegers = new ArrayList<>();

        String dataFileName = getDataFileName();
        File file = new File(dataFileName);
        //first get the column
        BufferedReader br = null;
        String line = "";
        String commaSplitter = ",";
        //
        try {
            br = new BufferedReader(new FileReader(dataFileName));
            line = br.readLine(); //consumes the first row
            while ((line = br.readLine()) != null) {
                String[] Columns = line.split(commaSplitter);
                int index = stageTwoOutcome.getSelectedIndex();
                ColumnsCustom.add(Columns[index]);
            }
            //System.out.println("COLUMN:");
            for (int k = 0; k < ColumnsCustom.size(); k++) {
                System.out.println(ColumnsCustom.get(k));
            }
            //count the unique ones
            for (int x = 0; x < ColumnsCustom.size(); x++) {
                if (UniqueList.contains(ColumnsCustom.get(x))) {
                    //do nothing
                } else if (ColumnsCustom.get(x).equals(defFile.getAdvancedMissingValue()) && !ColumnsCustom.get(x).equals("0")) { //compare if the category is a missing value, then don't consider it as a category
                    //do nothing

                } else {
                    UniqueList.add(ColumnsCustom.get(x));
                }
            }

            for (int x = 0; x < UniqueList.size(); x++) {

                UniqueIntegers.add(Integer.valueOf(UniqueList.get(x)));

            }

            Collections.sort(UniqueIntegers);

            System.out.println("Number of unique categories: " + String.valueOf(UniqueList.size()));

            outCategoryDisplay.setText(UniqueList.size() + " Categories:\n");
//            for (int index = 0; index < UniqueList.size(); index++) {
//                //numberOfCategories.setT
//                //numberOfCategories.setText(numberOfCategories.getText() +"<html><br></html>" + String.valueOf(index + 1) + ":" + UniqueList.get(index) + "<html><br></html>");
//                outCategoryDisplay.setText(outCategoryDisplay.getText() + String.valueOf(index + 1) + ") " + UniqueList.get(index) + "\n");
//
//            }

            for (int index = 0; index < UniqueIntegers.size(); index++) {
                //numberOfCategories.setT
                //numberOfCategories.setText(numberOfCategories.getText() +"<html><br></html>" + String.valueOf(index + 1) + ":" + UniqueList.get(index) + "<html><br></html>");
                outCategoryDisplay.setText(outCategoryDisplay.getText() + String.valueOf(index + 1) + ") " + String.valueOf(UniqueIntegers.get(index)) + "\n");

            }

            // System.out.println("Number of unique categories: " + String.valueOf(UniqueList.size()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Logger.getLogger(getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] outcomeCats = new String[UniqueIntegers.size()];

        for (int pos = 0; pos < outcomeCats.length; pos++) {
            outcomeCats[pos] = String.valueOf(UniqueIntegers.get(pos));
            System.out.println("STAGE 2 OUTCOME CATEGORIES: " + outcomeCats[pos]);
        }
        System.out.println("Before return getStageTwoOutcomeValues");
        return outcomeCats;
    }

}
