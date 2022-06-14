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
import java.awt.Desktop;
import java.net.URL;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import def_lib.MixLibrary;
import def_lib.ModelBuilder;
import def_lib.SuperUserMenu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.io.FilenameUtils;
import java.io.Serializable;
import java.net.URI;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import org.apache.commons.io.FileUtils;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import static mixregui.advancedOptions.disaggregateEnabled;
import static mixregui.stageTwoRegs.stageTwoLevelOne;
import static mixregui.stageTwoRegs.stageTwoLevelTwo;

/**
 * Main class for the program that is used to manipulate regressors
 *
 * @author adityaponnada
 */
public class mixregGUI extends javax.swing.JFrame implements Serializable {

    public static mixregGUI mxr;

    // Declarations from old java
    public File file;
    public File file_stageTwo;

    public static String sessionFolderNameBuilt;

    static String[] variableNamesCombo_stageOne;

    static int RLE;
    static boolean notIncludeStageTwo;
    static boolean outComeBoolean;
    static boolean isRandomScale = false;
    static String dataFileNameRef;
    static String dataFileNameRef_stageTwo;
    final ImageIcon icon;
    final ImageIcon bigIcon;
    static int iconPositionX;
    static int iconPositionY;
    String missingValue = "0";
    static int revealHiddenTabs;
    boolean validDataset = true;
    boolean validDataset_stageTwo = true;
    boolean isNewModalConfigSubmitted;
    boolean isUpdateStage2ConfigClicked;
    boolean isStageOneSubmitted;
    boolean isStageTwoSubmitted;

//    public static DefinitionHelper defFile;
    public static MixLibrary defFile;
    public static ModelBuilder modelBuilder;

    public static MixRegGuiStates MXRStates;

    public static SystemLogger logger;
    public String sessionFolderName;
    public static String logFilePath;

    public int getRLE() {
        return RLE;
    }

    //get the variable names from the 1st data file
    public static String[] getVariableNames_stageOne() {

        return variableNamesCombo_stageOne;
    }

    //get the variable names from the 2nd data file
    public static String[] getVariableNames_stageTwo() {

        return variableNamesCombo_stageTwo;
    }

    //get data file name when created
    public String getDataFileName(int stageNum) {
        String dataFileName;
        switch (stageNum) {
            case 1:
                dataFileName = dataFileNameRef;
                break;
            case 2:
                if ((includeStageTwoDataYes.isSelected() == true) && (!dataFileNameRef_stageTwo.isEmpty())) {
                    dataFileName = dataFileNameRef_stageTwo;
                } else {
                    dataFileName = dataFileNameRef;
                }

                break;
            default:
                dataFileName = dataFileNameRef;
                break;
        }
        return dataFileName;
    }

    //get title from the text box
    public String getTitle() {
        return titleField.getText();
    }

    public String getFilePath() {
        return filePath.getText();
    }

    public String getFilePath_stageTwo() {
        return filePath_stageTwo.getText();
    }

    public boolean getMissingValuePresent() {
        return missingValuePresent.isSelected();
    }

    public boolean getMissingValueAbsent() {
        return missingValueAbsent.isSelected();
    }

    public String getNewModelMissingValueCode() {
        return newModelMissingValueCode.getText();
    }

    public boolean getStageOneContinuousRadio() {
        return stageOneContinuousRadio.isSelected();
    }

    public boolean getStageOneDichotomousRadio() {
        return stageOneDichotomousRadio.isSelected();
    }

    public boolean getStageOneOrdinalRadio() {
        return stageOneOrdinalRadio.isSelected();
    }

    public boolean getStageOneProbit() {
        return StageOneProbitRadio.isSelected();
    }

    public boolean getStageOneLogistic() {
        return StageOneLogisticRadio.isSelected();
    }

    public boolean getOneRLERadio() {
        return oneRLERadio.isSelected();
    }

    public boolean getMoreThanOneRLERadio() {
        return moreThanOneRLERadio.isSelected();
    }

    public boolean getRandomScaleSelectionYes() {
        return randomScaleSelectionYes.isSelected();
    }

    public boolean getRandomScaleSelectionNo() {
        return randomScaleSelectionNo.isSelected();
    }

    public boolean getIncludeStageTwoYes() {
        return includeStageTwoYes.isSelected();
    }

    public boolean getIncludeStageTwoNo() {
        return includeStageTwoNo.isSelected();
    }

    public boolean getIncludeStageTwoDataYes() {
        return includeStageTwoDataYes.isSelected();
    }

    public boolean getIncludeStageTwoDataNo() {
        return includeStageTwoDataNo.isSelected();
    }

    public boolean getStageTwoSingleLevel() {
        return stageTwoSingleLevel.isSelected();
    }

    public boolean getStageTwoMultiLevel() {
        return stageTwoMultiLevel.isSelected();
    }

    public boolean getStageTwoContinuousRadio() {
        return stageTwoContinuousRadio.isSelected();
    }

    public boolean getStageTwoDichotomousRadio() {
        return stageTwoDichotomousRadio.isSelected();
    }

    public boolean getCountRadio() {
        return stageTwoCountRadio.isSelected();
    }

    public boolean getMultinomialRadio() {
        return stageTwoMultinomialRadio.isSelected();
    }

    public String getSeedTextBox() {
        return seedTextBox.getText();
    }

//check if the outcome type is selected as continuos or dichotomous
    public boolean isOutcomeContinous() {

        boolean selection = true;

        if (stageTwoContinuousRadio.isSelected() == true) {

            selection = true;
            System.out.println("Outcome selected at Newmodel: " + String.valueOf(selection));
        } else {

            selection = false;
            System.out.println("Outcome selected at Newmodel: " + String.valueOf(selection));
        }

        System.out.println("Outcome selected at Newmodel: " + String.valueOf(selection));
        return selection;
    }

    public boolean includeStageTwoNo() {
        System.out.println("In isOutcomeNone NewModel");

        boolean selection = false;

        if (includeStageTwoNo.isSelected() == true) {
            selection = true;
            System.out.println("In isOutcomeNone true");
        } else if (includeStageTwoYes.isSelected() == false) {

            selection = false;
        }
        return selection;
    }

    public boolean getNotIncludeStageTwo() {
        return includeStageTwoNo.isSelected();

    }

    public MixLibrary getDefFile() {

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

    public String extractDatFilePath(File file) {

        String csvPath = file.getAbsolutePath();
        String datPath = FilenameUtils.getFullPath(csvPath)
                + defFile.getUtcDirPath()
                + FilenameUtils.getBaseName(csvPath) + ".dat";
        return datPath;
    }

    public void getDataFromCSV() throws FileNotFoundException, IOException {

        Object[] columnnames;

        CSVReader CSVFileReader = new CSVReader(new FileReader(file));
        List myEntries = CSVFileReader.readAll();
        columnnames = (String[]) myEntries.get(0);

        // validation: check if dataset name include space
        String filename = file.getName();
        if (filename.contains(" ")) {
            validDataset = false;
            JOptionPane.showMessageDialog(null, "The filename of .csv file can not include space. Please try to use underscore instead.",
                    "Dataset Naming Error", JOptionPane.INFORMATION_MESSAGE);
        } else {
            validDataset = true;
        }
//        validDataset = true;

        // validation: check first row should be column names (every column name contains letters)
        if (validDataset) {
            for (int i = 0; i < columnnames.length; i++) {
                String colname = (String) columnnames[i];
                // check if colname contains just numbers
                if (colname.matches("[0-9]+")) {
                    validDataset = false;
                    JOptionPane.showMessageDialog(null, "The first row of .csv file should be column names in letters.",
                            "Dataset Error", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                validDataset = true;
            }
        }

        outerloop:
        if (validDataset) {
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
                        if (thiscellvalue.length() == 0) {
                            validDataset = false;
                            JOptionPane.showMessageDialog(null, "The .csv file should contain no blanks. Please insert a number for all missing values (e.g., -999)."
                                    + "\n" + "Missing value codes should be numeric only.",
                                    "Dataset Error", JOptionPane.INFORMATION_MESSAGE);
                            break outerloop;
                        }
                        if (!isNumeric(thiscellvalue)) {
                            validDataset = false;
                            JOptionPane.showMessageDialog(null, "The .csv file should contain only numeric values, except for the headers in the first row."
                                    + "\n" + "Missing value codes should be numeric only.",
                                    "Dataset Error", JOptionPane.INFORMATION_MESSAGE);
                            break outerloop;
                        }
                        tableModel.setValueAt(thiscellvalue, x - 1, columnnumber);
                        columnnumber++;
                    }
                }
            }
            validDataset = true;

            dataTable.setModel(tableModel);
            dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            dataTable.setAutoscrolls(true);
        }

    }

    public void printFileName() {
        String path = file.getName();

        printedFileName.setText(path);

    }

    public String randomLocationEffects() {

        String randLocString = "";

        if (oneRLERadio.isSelected()) {
            randLocString = "Intercept Only";

        } else if (moreThanOneRLERadio.isSelected()) {
            randLocString = "Intercept + Slope(s)";

        }

        return randLocString;
    }

    public String stageOneOutcomeTypeString() {

        String outcomeTypeText = "";

        if (stageOneContinuousRadio.isSelected()) {
            outcomeTypeText = "Continuous";
        } else if (stageOneDichotomousRadio.isSelected()) {
            outcomeTypeText = "Dichotomous";
        } else if (stageOneOrdinalRadio.isSelected()) {
            outcomeTypeText = "Ordinal";
        }
        return outcomeTypeText;

    }

    public String stageTwoOutcomeTypeString() {

        String outcomeTypeText = "";

        if (stageTwoContinuousRadio.isSelected()) {
            outcomeTypeText = "Continuous";
        } else if (stageTwoDichotomousRadio.isSelected()) {
            outcomeTypeText = "Dichot/Ord";
        } else if (stageTwoCountRadio.isSelected()) {
            outcomeTypeText = "Count";
        } else if (stageTwoMultinomialRadio.isSelected()) {
            outcomeTypeText = "Multinomial";
        } else if (includeStageTwoNo.isSelected()) {
            outcomeTypeText = "None";
        }
        return outcomeTypeText;

    }

    public String stageTwoModelTypeString() {

        String modelTypeText = "";

        if (stageTwoSingleLevel.isSelected()) {

            modelTypeText = "Single-level";
        } else if (stageTwoMultiLevel.isSelected()) {

            modelTypeText = "Multi-level";
        }
        return modelTypeText;

    }

    public String numResamplingString() {

        String numResamplingText;
        numResamplingText = advancedOptions_view.getResamplingRate();
        return numResamplingText;

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
        if (buttonGroup3.getSelection() == null & includeStageTwoYes.isSelected()) {
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

        if (missingValuePresent.isSelected() && newModelMissingValueCode.getText().trim().length() == 0) {
            allFieldsEntered = false;
            JOptionPane.showMessageDialog(null, "Please don't leave the missing code value as blank", "Missing information!", JOptionPane.INFORMATION_MESSAGE, icon);
            System.out.println("FIELD VALIDATE: " + "Missing value blank");
        }

        if (newModelMissingValueCode.getText().equals("0") || newModelMissingValueCode.getText().equals("00") || newModelMissingValueCode.getText().equals("000")) {
            allFieldsEntered = false;
            JOptionPane.showMessageDialog(null, "Invalid missing value code, 0 implies there are no missing values. Please use some other value. E.g., -999", "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }
        System.out.println("FIELD VALIDATE: " + "about to exit");
        return allFieldsEntered;
    }

    private String generateSeed() {
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

    // RLE_selected represents number of random location effects selected in new model
    int RLE_selected;

    int levelOneRegSize = 0;
    int levelTwoRegSize = 0;
    int stageTwoLevelOneRegSize = 0;
    int stageTwoLevelTwoRegSize = 0;
    int levelOneDisaggSize = 0;

    int SUPERUSER_KEY = 0;

    String[] variableNamesCombo;

    static String[] variableNamesCombo_stageTwo;

    DefaultComboBoxModel<String> IDList;

    DefaultComboBoxModel<String> StageOneList;

    DefaultComboBoxModel<String> StageTwoList;

    DefaultListModel<String> savedVariablesStageOne;

    ArrayList<ArrayList<JCheckBox>> levelOneBoxes;

    ArrayList<ArrayList<JCheckBox>> levelTwoBoxes;

    ArrayList<ArrayList<JCheckBox>> stageTwoBoxes;

    ArrayList<ArrayList<JCheckBox>> stageTwoLevelOneGridBoxes;

    ArrayList<ArrayList<JCheckBox>> stageTwoLevelTwoGridBoxes;

    ArrayList<ArrayList<JCheckBox>> disaggVarianceBoxes;

    public static int IDpos;
    public static int IDposStageTwo;
    public static int stageOnePos;
    public static int stageTwoPos;

    boolean scaleChecked = false;
    boolean randomChecked = false;
    boolean isIDChanged = false;
    boolean isStageOneOutcomeChanged = false;
    boolean isStageTwoOutcomeChanged = false;

    boolean suppressed = true;
    boolean stageTwoNotIncluded = false;
    boolean addStageOneCHecked = false;
    boolean addStageTwoChecked = false;

    ArrayList<String> levelOneSelected;
    ArrayList<String> levelTwoSelected;
    ArrayList<String> stageTwoSelected;
    ArrayList<String> stageTwoLevelOneSelected;
    ArrayList<String> stageTwoLevelTwoSelected;

    static ActionListener actionListener;

    int stageOneClicked = 0;

    JFileChooser fileChooser = new JFileChooser();
    int selectedModel;
    String defFilePath;

    String[] dataValues;

    int outComeType;

    static String outPutStageTwo;

    private void initiateStageOneTabLayout() {
//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        // newModel = new NewModel();
//        advancedOptions_view = new advancedOptions();
        //instructions = new InstructionsGUI();
        variableNamesCombo = getVariableNames_stageOne();
        stageTwoNotIncluded = getNotIncludeStageTwo();

        IDList = new DefaultComboBoxModel<String>();
        StageOneList = new DefaultComboBoxModel<String>();
        StageTwoList = new DefaultComboBoxModel<String>();
        NoAssociationRadio.setSelected(true);
        stage_1_regs = new stageOneRegs();

        RLE_selected = getRLE();

//        stageOneTabs.setEnabledAt(2, false);
        enbaleInteractionCheckBox.setVisible(true);
        enbaleInteractionCheckBox.setSelected(false);
        enbaleInteractionCheckBox.setEnabled(true);

        //Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/mixLogo.png"));
        //setIconImage(image);
        if (RLE_selected == MixLibrary.STAGE_ONE_RLE_SLOPE) {
            associationLabel.setText("Association of random location & scale?"); //Switch to this when RLE_selected > 1
            NoAssociationRadio.setText("Yes");
            LinearAssociationRadio.setText("No");
            QuadraticAssociationRadio.setVisible(false);

            if (isRandomScale) {
                NoAssociationRadio.setEnabled(false);
                LinearAssociationRadio.setEnabled(false);
                associationLabel.setEnabled(false);
                NoAssociationRadio.setSelected(true);
            } else {
                LinearAssociationRadio.setSelected(true);
            }

            // if random location effects are more than one, change the table column names
            level2_BSVar.setVisible(false);
            level1_BSVar.setText("Random Slope");
            level1_BSVar.setToolTipText("Select the checkbox and allows for \n"
                    + "extra random slope effects in the \n"
                    + "[Mean model].");
//            level1_WSVar.setText("Scale");
//            level2_WSVar.setText("Scale");
            level1_WSVar.setText("WS Variance");
            level2_WSVar.setText("WS Variance");

        }

        stageOneLevelOnePanel.setLayout(new BorderLayout());
        stageOneLevelTwoPanel.setLayout(new BorderLayout());
        stageTwoLevelOnePanel.setLayout(new BorderLayout());
        stageTwoLevelTwoPanel.setLayout(new BorderLayout());

        System.out.println("Right before");
        if (includeStageTwoNo() == true) {
            System.out.println("In isOutcomeNone MixReg");
            startStageTwo.setText("Run Stage 1");
            System.out.println(startStageTwo.getText());
            //stageOneTabs.set
//            stageOneTabs.setEnabledAt(2, false);
        } else if (includeStageTwoNo() == false) {
            startStageTwo.setText("Configure Stage 2");
        }
        System.out.println("Right after");

        try {
            if (defFile.getAdvancedUseRandomScale() == "1") {
                NoAssociationRadio.setEnabled(false);
                LinearAssociationRadio.setEnabled(false);
                QuadraticAssociationRadio.setEnabled(false);
            }
        } catch (Exception eoe) {
            System.out.println("No Random Scale Option Set");
            SystemLogger.LOGGER.log(Level.SEVERE, eoe.toString() + "{0}", SystemLogger.getLineNum());
        }
    }

    private void setFirstTabStatus(boolean turnOn) {
        dataFileLabel.setVisible(turnOn);
        filePath.setVisible(turnOn);
        fileBrowseButton.setVisible(turnOn);

        titleViewLabel.setVisible(turnOn);
        titleField.setVisible(turnOn);

        DatasetLabel.setVisible(turnOn);
        missingViewLabel.setVisible(turnOn);
        missingValueAbsent.setVisible(turnOn);
        missingValuePresent.setVisible(turnOn);

        missingCodeViewLabel.setVisible(turnOn);
        newModelMissingValueCode.setVisible(turnOn);

        stageOneOutcomeViewLabel.setVisible(turnOn);
        stageOneContinuousRadio.setVisible(turnOn);
        stageOneDichotomousRadio.setVisible(turnOn);
        stageOneOrdinalRadio.setVisible(turnOn);
        stageOneOutcomeHelpButton.setVisible(turnOn);

        StageOneModelTypeLabel.setVisible(turnOn);
        StageOneProbitRadio.setVisible(turnOn);
        StageOneLogisticRadio.setVisible(turnOn);

        rleViewLabel.setVisible(turnOn);
        oneRLERadio.setVisible(turnOn);
        moreThanOneRLERadio.setVisible(turnOn);

        randomScaleViewLabel.setVisible(turnOn);
        randomScaleSelectionYes.setVisible(turnOn);
        randomScaleSelectionNo.setVisible(turnOn);

        includeStageTwoLabel.setVisible(turnOn);
        includeStageTwoYes.setVisible(turnOn);
        includeStageTwoNo.setVisible(turnOn);
        stageTwoDescription.setVisible(turnOn);

        includeStageTwoDataLabel.setVisible(turnOn);
        includeStageTwoDataYes.setVisible(turnOn);
        includeStageTwoDataNo.setVisible(turnOn);

        DataFileStageTwoLabel.setVisible(turnOn);
        filePath_stageTwo.setVisible(turnOn);
        fileBrowseButtonStageTwoData.setVisible(turnOn);

        stageTwoModelTypeLabel.setVisible(turnOn);
        stageTwoSingleLevel.setVisible(turnOn);
        stageTwoMultiLevel.setVisible(turnOn);

        stageTwoOutcomeTypeLabel.setVisible(turnOn);
        stageTwoContinuousRadio.setVisible(turnOn);
        stageTwoDichotomousRadio.setVisible(turnOn);
        stageTwoCountRadio.setVisible(turnOn);
        stageTwoMultinomialRadio.setVisible(turnOn);

        setSeedLabel.setVisible(turnOn);
        seedTextBox.setVisible(turnOn);
        seedHelpButton.setVisible(turnOn);

        newModel_resetButton.setVisible(turnOn);
        newModelSubmit.setVisible(turnOn);
        updateStage2ConfigButton.setVisible(turnOn);

        stageOneModelGiantLabel.setVisible(turnOn);
        stageTwoModelGiantLabel.setVisible(turnOn);

        jSeparator16.setVisible(turnOn);
        jSeparator12.setVisible(turnOn);
        jSeparator8.setVisible(turnOn);

        showHiddenBigIconLabel(!turnOn);

        guiStatesLoadButtonModalConfig.setVisible(turnOn);
        guiStatesSaveButtonModalConfig.setVisible(turnOn);

        datasetHelpButton.setVisible(turnOn);
        datasetMissingValuesHelpButton.setVisible(turnOn);
        stageOneRLEHelpButton.setVisible(turnOn);
        stageOneRSHelpButton.setVisible(turnOn);
        stageTwoModelTypeHelpButton.setVisible(turnOn);
        stageTwoOutcomeTypeHelpButton.setVisible(turnOn);
    }

    /**
     * Creates new form mixregGUI
     */
    public mixregGUI() {
        initComponents();
        this.setTitle("MixWILD-2.0.6");
        // adjust the frame size to fit screen resolution
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, stageOneTabs.getWidth(), (int) Math.round(screenSize.height / 1.5));
//        setBounds(0, 0, stageOneTabs.getWidth(), 700);
        setVisible(true);

        // set tooltip display time
        int delayTimeDesired = 10000; //10 seconds
        javax.swing.ToolTipManager.sharedInstance().setDismissDelay(delayTimeDesired);

        MXRStates = new MixRegGuiStates();
        advancedOptions_view = new advancedOptions();

        icon = new ImageIcon(getClass().getResource("/resources/MixWildLogoTiny.png"));
        bigIcon = new ImageIcon(getClass().getResource("/resources/mixwild_logo-red_large.png"));

        stageOneContinuousRadio.setSelected(true);
        stageOneDichotomousRadio.setEnabled(true);
        stageOneOrdinalRadio.setEnabled(true);

        stageTwoCountRadio.setEnabled(false);
        stageTwoMultinomialRadio.setEnabled(false);

        stageTwoSingleLevel.setEnabled(true);
//        stageTwoSingleLevel.setSelected(true);
        stageTwoMultiLevel.setEnabled(true);

        //updateMixRegGUI();
        //this.setResizable(false);
        // hide components for user operating in order
        setFirstTabStatus(false);

        // hide tabs
//        stageOneTabs.setEnabledAt(1, false);
//        stageOneTabs.setEnabledAt(2, false);
//        stageOneTabs.setEnabledAt(3, false);
//        stageOneTabs.setEnabledAt(4, false);
//        stageOneTabs.setEnabledAt(5, false);
//        stageOneTabs.setEnabledAt(6, false);
//        stageOneTabs.setEnabledAt(7, false);
        stageOneTabs.remove(jPanel1);
        stageOneTabs.remove(jPanel12);
        stageOneTabs.remove(jPanel3);
        stageOneTabs.remove(jPanel4);
        stageOneTabs.remove(jPanel2);
        stageOneTabs.remove(jPanel6);
        stageOneTabs.remove(jPanel16);
        stageOneTabs.remove(jPanel14);

        stageOneTabs.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                System.out.println("Select Tab: " + stageOneTabs.getTitleAt(stageOneTabs.getSelectedIndex()));
                SystemLogger.LOGGER.log(Level.FINE, "Select Tab: " + stageOneTabs.getTitleAt(stageOneTabs.getSelectedIndex()));
            }
        });

        //updateMixRegGUI();
        //this.setResizable(false);
        // TODO: Fix superuser menu code
        //superUserMenu.setVisible(SUPERUSER_KEY > 2);
//       IDpos = IDvariableCombo.getSelectedIndex();
//       stageOnePos = StageOneVariableCombo.getSelectedIndex();
//       stageTwoPos = StageTwoOutcomeCombo.getSelectedIndex();
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                e.getWindow().dispose();
                System.out.println("Program Closed!");
                SystemLogger.LOGGER.log(Level.INFO, "Program Closed");
            }
        });
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
        includeStageTwoGroup = new javax.swing.ButtonGroup();
        randomScaleSelectionGroup = new javax.swing.ButtonGroup();
        stageOneOutcomeGroup = new javax.swing.ButtonGroup();
        stageTwoLevelGroup = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        stageTwoDataButtonGroup = new javax.swing.ButtonGroup();
        parentPanel = new javax.swing.JPanel();
        stageOneTabs = new javax.swing.JTabbedPane();
        jPanel13 = new javax.swing.JPanel();
        fileBrowseButton = new javax.swing.JButton();
        filePath = new javax.swing.JTextField();
        dataFileLabel = new javax.swing.JLabel();
        titleViewLabel = new javax.swing.JLabel();
        titleField = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();
        rleViewLabel = new javax.swing.JLabel();
        oneRLERadio = new javax.swing.JRadioButton();
        moreThanOneRLERadio = new javax.swing.JRadioButton();
        randomScaleViewLabel = new javax.swing.JLabel();
        stageTwoOutcomeTypeLabel = new javax.swing.JLabel();
        stageTwoContinuousRadio = new javax.swing.JRadioButton();
        stageTwoDichotomousRadio = new javax.swing.JRadioButton();
        missingValueAbsent = new javax.swing.JRadioButton();
        missingValuePresent = new javax.swing.JRadioButton();
        missingViewLabel = new javax.swing.JLabel();
        missingCodeViewLabel = new javax.swing.JLabel();
        newModelMissingValueCode = new javax.swing.JTextField();
        seedTextBox = new javax.swing.JTextField();
        setSeedLabel = new javax.swing.JLabel();
        newModel_resetButton = new javax.swing.JButton();
        newModelSubmit = new javax.swing.JButton();
        stageOneModelGiantLabel = new javax.swing.JLabel();
        stageTwoModelGiantLabel = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        stageOneOutcomeViewLabel = new javax.swing.JLabel();
        stageOneContinuousRadio = new javax.swing.JRadioButton();
        stageOneDichotomousRadio = new javax.swing.JRadioButton();
        stageOneOrdinalRadio = new javax.swing.JRadioButton();
        randomScaleSelectionYes = new javax.swing.JRadioButton();
        randomScaleSelectionNo = new javax.swing.JRadioButton();
        includeStageTwoLabel = new javax.swing.JLabel();
        includeStageTwoYes = new javax.swing.JRadioButton();
        includeStageTwoNo = new javax.swing.JRadioButton();
        stageTwoModelTypeLabel = new javax.swing.JLabel();
        stageTwoSingleLevel = new javax.swing.JRadioButton();
        stageTwoMultiLevel = new javax.swing.JRadioButton();
        stageTwoMultinomialRadio = new javax.swing.JRadioButton();
        stageTwoCountRadio = new javax.swing.JRadioButton();
        hiddenBigIconLabel = new javax.swing.JLabel();
        guiStatesLoadButtonModalConfig = new javax.swing.JButton();
        guiStatesSaveButtonModalConfig = new javax.swing.JButton();
        loadModelByBrowseButton = new javax.swing.JButton();
        updateStage2ConfigButton = new javax.swing.JButton();
        newDataSetButton = new javax.swing.JButton();
        DatasetLabel = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        datasetHelpButton = new javax.swing.JLabel();
        seedHelpButton = new javax.swing.JLabel();
        datasetMissingValuesHelpButton = new javax.swing.JLabel();
        stageOneOutcomeHelpButton = new javax.swing.JLabel();
        stageOneRLEHelpButton = new javax.swing.JLabel();
        stageOneRSHelpButton = new javax.swing.JLabel();
        stageTwoDescription = new javax.swing.JLabel();
        stageTwoModelTypeHelpButton = new javax.swing.JLabel();
        stageTwoOutcomeTypeHelpButton = new javax.swing.JLabel();
        StageOneModelTypeLabel = new javax.swing.JLabel();
        StageOneLogisticRadio = new javax.swing.JRadioButton();
        StageOneProbitRadio = new javax.swing.JRadioButton();
        includeStageTwoDataLabel = new javax.swing.JLabel();
        DataFileStageTwoLabel = new javax.swing.JLabel();
        includeStageTwoDataYes = new javax.swing.JRadioButton();
        includeStageTwoDataNo = new javax.swing.JRadioButton();
        fileBrowseButtonStageTwoData = new javax.swing.JButton();
        filePath_stageTwo = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        clearStageOneButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        level1_MeanReg = new javax.swing.JLabel();
        level1_WSVar = new javax.swing.JLabel();
        level2_MeanReg = new javax.swing.JLabel();
        level2_BSVar = new javax.swing.JLabel();
        level2_WSVar = new javax.swing.JLabel();
        level1_BSVar = new javax.swing.JLabel();
        stageOneLevelOnePanel = new javax.swing.JPanel();
        levelOneGrid = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        stageOneLevelTwoPanel = new javax.swing.JPanel();
        levelTwoGrid = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        StageOneOutcomeCombo = new javax.swing.JComboBox<>();
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
        jSeparator18 = new javax.swing.JSeparator();
        randomLocationEffectsLabel1 = new javax.swing.JLabel();
        stageTwoOutcomePrintLabel1 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        startStageTwo = new javax.swing.JButton();
        guiStatesSaveButtonStageOne = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        addStageTwoReg = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        stageTwoLevelTwoPanel = new javax.swing.JPanel();
        stageTwoRegsGridLvl2 = new javax.swing.JPanel();
        runTabTwoStageOneTwo = new javax.swing.JButton();
        enbaleInteractionCheckBox = new javax.swing.JCheckBox();
        StageTwoOutcomeCombo = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        clearStageTwoButton = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 2), new java.awt.Dimension(0, 2), new java.awt.Dimension(32767, 2));
        jSeparator14 = new javax.swing.JSeparator();
        jSeparator15 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        outcomeCatButton = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        outCategoryDisplay = new javax.swing.JTextPane();
        guiStatesSaveButtonStageTwo = new javax.swing.JButton();
        stageTwoLevelOnePanel = new javax.swing.JPanel();
        stageTwoRegsGridLvl1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        stageOneOutcomeStageTwoConfigLabel = new javax.swing.JLabel();
        stageOneModelStageTwoConfigLabel = new javax.swing.JLabel();
        stageTwoOutcomeStageTwoConfigLabel = new javax.swing.JLabel();
        stageTwoModelTypeStageTwoConfigLabel = new javax.swing.JLabel();
        numResamplingStageTwoConfigLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator17 = new javax.swing.JSeparator();
        jLabel33 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        stageOneModelStageTwoConfigLabel1 = new javax.swing.JLabel();
        stageOneOutcomeStageTwoConfigLabel1 = new javax.swing.JLabel();
        stageTwoModelTypeStageTwoConfigLabel1 = new javax.swing.JLabel();
        stageTwoOutcomeStageTwoConfigLabel1 = new javax.swing.JLabel();
        numResamplingStageTwoConfigLabel1 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        IDStageTwoVariableCombo = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        stageOneOutput = new javax.swing.JTextArea();
        saveStage1OutButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        openStage1OutButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        stageTwoOutput = new javax.swing.JTextArea();
        saveStage2OutButton = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        javax.swing.JButton openStage2OutButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
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
        jPanel16 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        printedFileName_stageTwo = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        dataTable_stageTwo = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        userGuideDownload = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        exampleDataDownload = new javax.swing.JButton();
        online_support_help_label = new javax.swing.JLabel();
        online_support_button = new javax.swing.JButton();

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
        setTitle("MixWILD");
        setMinimumSize(new java.awt.Dimension(1150, 670));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        parentPanel.setLayout(new java.awt.CardLayout());
        getContentPane().add(parentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1224, -1));

        stageOneTabs.setToolTipText("");
        stageOneTabs.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        stageOneTabs.setFocusable(false);
        stageOneTabs.setFont(new java.awt.Font("Dialog", 1, 13)); // NOI18N
        stageOneTabs.setMinimumSize(new java.awt.Dimension(1000, 700));
        stageOneTabs.setPreferredSize(new java.awt.Dimension(1200, 700));

        jPanel13.setMinimumSize(new java.awt.Dimension(1000, 700));
        jPanel13.setPreferredSize(new java.awt.Dimension(1300, 700));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fileBrowseButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        fileBrowseButton.setText("Change Dataset");
        fileBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileBrowseButtonActionPerformed(evt);
            }
        });
        jPanel13.add(fileBrowseButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 40, 145, 30));

        filePath.setEditable(false);
        filePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filePathActionPerformed(evt);
            }
        });
        jPanel13.add(filePath, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 45, 254, -1));

        dataFileLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        dataFileLabel.setText("CSV file path: ");
        dataFileLabel.setToolTipText("");
        jPanel13.add(dataFileLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 45, -1, -1));

        titleViewLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        titleViewLabel.setText("Title (optional):");
        jPanel13.add(titleViewLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, -1, -1));

        titleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titleFieldActionPerformed(evt);
            }
        });
        jPanel13.add(titleField, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 80, 254, -1));
        jPanel13.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 559, 770, 11));

        rleViewLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        rleViewLabel.setText("Specify random location effects:");
        rleViewLabel.setToolTipText("");
        jPanel13.add(rleViewLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 264, -1, -1));

        buttonGroup2.add(oneRLERadio);
        oneRLERadio.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        oneRLERadio.setText("Intercept only");
        oneRLERadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oneRLERadioActionPerformed(evt);
            }
        });
        jPanel13.add(oneRLERadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 260, -1, -1));

        buttonGroup2.add(moreThanOneRLERadio);
        moreThanOneRLERadio.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        moreThanOneRLERadio.setText("Intercept and slope(s)");
        moreThanOneRLERadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moreThanOneRLERadioActionPerformed(evt);
            }
        });
        jPanel13.add(moreThanOneRLERadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(704, 260, -1, -1));

        randomScaleViewLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        randomScaleViewLabel.setText("Include estimates of random scale:");
        randomScaleViewLabel.setToolTipText("");
        jPanel13.add(randomScaleViewLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 298, -1, -1));

        stageTwoOutcomeTypeLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        stageTwoOutcomeTypeLabel.setText("Stage 2 outcome:");
        stageTwoOutcomeTypeLabel.setToolTipText("");
        jPanel13.add(stageTwoOutcomeTypeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 489, -1, -1));

        buttonGroup3.add(stageTwoContinuousRadio);
        stageTwoContinuousRadio.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        stageTwoContinuousRadio.setText("Continuous");
        stageTwoContinuousRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageTwoContinuousRadioActionPerformed(evt);
            }
        });
        jPanel13.add(stageTwoContinuousRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 487, -1, -1));

        buttonGroup3.add(stageTwoDichotomousRadio);
        stageTwoDichotomousRadio.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        stageTwoDichotomousRadio.setText("Dichotomous/Ordinal");
        stageTwoDichotomousRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageTwoDichotomousRadioActionPerformed(evt);
            }
        });
        jPanel13.add(stageTwoDichotomousRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(691, 487, -1, -1));

        buttonGroup4.add(missingValueAbsent);
        missingValueAbsent.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        missingValueAbsent.setText("No");
        missingValueAbsent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                missingValueAbsentActionPerformed(evt);
            }
        });
        jPanel13.add(missingValueAbsent, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 114, -1, 20));

        buttonGroup4.add(missingValuePresent);
        missingValuePresent.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        missingValuePresent.setText("Yes");
        missingValuePresent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                missingValuePresentActionPerformed(evt);
            }
        });
        jPanel13.add(missingValuePresent, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 114, 60, 20));

        missingViewLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        missingViewLabel.setText("Does your data contain missing values?");
        missingViewLabel.setToolTipText("");
        jPanel13.add(missingViewLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 114, -1, -1));

        missingCodeViewLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        missingCodeViewLabel.setText("What is your missing data coded as?");
        missingCodeViewLabel.setToolTipText("");
        jPanel13.add(missingCodeViewLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 145, -1, -1));

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
        jPanel13.add(newModelMissingValueCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 145, 80, -1));

        seedTextBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seedTextBoxActionPerformed(evt);
            }
        });
        jPanel13.add(seedTextBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 524, 80, -1));

        setSeedLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        setSeedLabel.setText("Set a seed for Stage 2 resampling (optional):");
        setSeedLabel.setToolTipText("");
        jPanel13.add(setSeedLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 524, 320, -1));

        newModel_resetButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        newModel_resetButton.setText("Reset");
        newModel_resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newModel_resetButtonActionPerformed(evt);
            }
        });
        jPanel13.add(newModel_resetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 580, 120, 35));

        newModelSubmit.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        newModelSubmit.setText("Continue");
        newModelSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newModelSubmitActionPerformed(evt);
            }
        });
        jPanel13.add(newModelSubmit, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 580, 120, 35));

        stageOneModelGiantLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        stageOneModelGiantLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        stageOneModelGiantLabel.setText("Stage 1 Model");
        jPanel13.add(stageOneModelGiantLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 230, -1, -1));

        stageTwoModelGiantLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        stageTwoModelGiantLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        stageTwoModelGiantLabel.setText("Stage 2 Model");
        jPanel13.add(stageTwoModelGiantLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 367, -1, -1));
        jPanel13.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, 770, 12));

        stageOneOutcomeViewLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        stageOneOutcomeViewLabel.setText("Stage 1 outcome:");
        stageOneOutcomeViewLabel.setToolTipText("");
        jPanel13.add(stageOneOutcomeViewLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 196, -1, -1));

        stageOneOutcomeGroup.add(stageOneContinuousRadio);
        stageOneContinuousRadio.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        stageOneContinuousRadio.setText("Continuous");
        stageOneContinuousRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageOneContinuousRadioActionPerformed(evt);
            }
        });
        jPanel13.add(stageOneContinuousRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 192, -1, -1));

        stageOneOutcomeGroup.add(stageOneDichotomousRadio);
        stageOneDichotomousRadio.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        stageOneDichotomousRadio.setText("Dichotomous");
        stageOneDichotomousRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageOneDichotomousRadioActionPerformed(evt);
            }
        });
        jPanel13.add(stageOneDichotomousRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(691, 192, -1, -1));

        stageOneOutcomeGroup.add(stageOneOrdinalRadio);
        stageOneOrdinalRadio.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        stageOneOrdinalRadio.setText("Ordinal");
        stageOneOrdinalRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageOneOrdinalRadioActionPerformed(evt);
            }
        });
        jPanel13.add(stageOneOrdinalRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(802, 192, -1, -1));

        randomScaleSelectionGroup.add(randomScaleSelectionYes);
        randomScaleSelectionYes.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        randomScaleSelectionYes.setText("Yes");
        randomScaleSelectionYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomScaleSelectionYesActionPerformed(evt);
            }
        });
        jPanel13.add(randomScaleSelectionYes, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 294, -1, -1));

        randomScaleSelectionGroup.add(randomScaleSelectionNo);
        randomScaleSelectionNo.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        randomScaleSelectionNo.setText("No");
        randomScaleSelectionNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomScaleSelectionNoActionPerformed(evt);
            }
        });
        jPanel13.add(randomScaleSelectionNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 294, -1, -1));

        includeStageTwoLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        includeStageTwoLabel.setText("Include Stage 2 model:");
        includeStageTwoLabel.setToolTipText("");
        jPanel13.add(includeStageTwoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 349, -1, -1));

        includeStageTwoGroup.add(includeStageTwoYes);
        includeStageTwoYes.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        includeStageTwoYes.setText("Yes");
        includeStageTwoYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                includeStageTwoYesActionPerformed(evt);
            }
        });
        jPanel13.add(includeStageTwoYes, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 345, -1, -1));

        includeStageTwoGroup.add(includeStageTwoNo);
        includeStageTwoNo.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        includeStageTwoNo.setText("No");
        includeStageTwoNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                includeStageTwoNoActionPerformed(evt);
            }
        });
        jPanel13.add(includeStageTwoNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(643, 345, -1, -1));

        stageTwoModelTypeLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        stageTwoModelTypeLabel.setText("Stage 2 model type:");
        stageTwoModelTypeLabel.setToolTipText("");
        jPanel13.add(stageTwoModelTypeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 454, -1, -1));

        stageTwoLevelGroup.add(stageTwoSingleLevel);
        stageTwoSingleLevel.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        stageTwoSingleLevel.setText("Single level");
        stageTwoSingleLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageTwoSingleLevelActionPerformed(evt);
            }
        });
        jPanel13.add(stageTwoSingleLevel, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 450, -1, -1));

        stageTwoLevelGroup.add(stageTwoMultiLevel);
        stageTwoMultiLevel.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        stageTwoMultiLevel.setText("Multilevel");
        stageTwoMultiLevel.setToolTipText("");
        stageTwoMultiLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageTwoMultiLevelActionPerformed(evt);
            }
        });
        jPanel13.add(stageTwoMultiLevel, new org.netbeans.lib.awtextra.AbsoluteConstraints(691, 450, -1, -1));

        buttonGroup3.add(stageTwoMultinomialRadio);
        stageTwoMultinomialRadio.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        stageTwoMultinomialRadio.setText("Multinomial");
        stageTwoMultinomialRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageTwoMultinomialRadioActionPerformed(evt);
            }
        });
        jPanel13.add(stageTwoMultinomialRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(919, 487, -1, -1));

        buttonGroup3.add(stageTwoCountRadio);
        stageTwoCountRadio.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        stageTwoCountRadio.setText("Count");
        stageTwoCountRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageTwoCountRadioActionPerformed(evt);
            }
        });
        jPanel13.add(stageTwoCountRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(852, 487, -1, -1));

        hiddenBigIconLabel.setFocusable(false);
        hiddenBigIconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hiddenBigIconLabelMouseClicked(evt);
            }
        });
        jPanel13.add(hiddenBigIconLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(264, 22, -1, -1));

        guiStatesLoadButtonModalConfig.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        guiStatesLoadButtonModalConfig.setText("Load Model");
        guiStatesLoadButtonModalConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guiStatesLoadButtonModalConfigActionPerformed(evt);
            }
        });
        jPanel13.add(guiStatesLoadButtonModalConfig, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 580, 120, 35));

        guiStatesSaveButtonModalConfig.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        guiStatesSaveButtonModalConfig.setText("Save Model");
        guiStatesSaveButtonModalConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guiStatesSaveButtonModalConfigActionPerformed(evt);
            }
        });
        jPanel13.add(guiStatesSaveButtonModalConfig, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 580, 120, 35));

        loadModelByBrowseButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        loadModelByBrowseButton.setText("Start with Previous Model");
        loadModelByBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadModelByBrowseButtonActionPerformed(evt);
            }
        });
        jPanel13.add(loadModelByBrowseButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 258, -1, -1));

        updateStage2ConfigButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        updateStage2ConfigButton.setText("Update Stage 2");
        updateStage2ConfigButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateStage2ConfigButtonActionPerformed(evt);
            }
        });
        jPanel13.add(updateStage2ConfigButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 580, -1, 35));

        newDataSetButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        newDataSetButton.setText("Start with New CSV File");
        newDataSetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newDataSetButtonActionPerformed(evt);
            }
        });
        jPanel13.add(newDataSetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 258, -1, -1));

        DatasetLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        DatasetLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DatasetLabel.setText("Dataset");
        jPanel13.add(DatasetLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 97, -1, -1));
        jPanel13.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 12, 175, -1));

        jLabel34.setText("  ");
        jPanel13.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(262, 183, 164, -1));
        jPanel13.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 333, 770, 11));

        datasetHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon4 - Copy.png"))); // NOI18N
        datasetHelpButton.setToolTipText("<html><pre>Is your dataset MixWILD friendly?\n    1) You should always use a .csv file.\n    2) You should ensure that missing values are not blanks.\n    3) Missing value codes should be numeric only.</p>\n    4) Make sure your missing value code is the same as your dataset.\n    5) Please ensure that the data is sorted by IDs.\n    6) The first row in the .csv file should be column names.<pre>");
        datasetHelpButton.setMaximumSize(new java.awt.Dimension(16, 16));
        datasetHelpButton.setMinimumSize(new java.awt.Dimension(16, 16));
        datasetHelpButton.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel13.add(datasetHelpButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 46, -1, -1));

        seedHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon4 - Copy.png"))); // NOI18N
        seedHelpButton.setToolTipText("<html><pre>A seed is a number used to initialize a random number generator. Different seeds produce \ndifferent sequences of random numbers.In the context of two-stage models, a seed is helpful for\nreplicating models with identical results.<pre>");
        seedHelpButton.setMaximumSize(new java.awt.Dimension(16, 16));
        seedHelpButton.setMinimumSize(new java.awt.Dimension(16, 16));
        seedHelpButton.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel13.add(seedHelpButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 526, -1, -1));

        datasetMissingValuesHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon4 - Copy.png"))); // NOI18N
        datasetMissingValuesHelpButton.setToolTipText("<html><pre>Click on missing values if there are any in your dataset;\nspecify the missing value code in the box (e.g., '-999').<pre>");
        datasetMissingValuesHelpButton.setMaximumSize(new java.awt.Dimension(16, 16));
        datasetMissingValuesHelpButton.setMinimumSize(new java.awt.Dimension(16, 16));
        datasetMissingValuesHelpButton.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel13.add(datasetMissingValuesHelpButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 116, -1, -1));

        stageOneOutcomeHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon4 - Copy.png"))); // NOI18N
        stageOneOutcomeHelpButton.setToolTipText("<html><pre>(To be implemented in MixWILD 2.0) Currently, you are restricted to continuous Stage 1 outcomes.\nDichotomous and ordinal outcomes will run ordered logistic regressions at Stage 1.\nNote that random scale is not available for dichotomous outcomes.<pre>");
        stageOneOutcomeHelpButton.setMaximumSize(new java.awt.Dimension(16, 16));
        stageOneOutcomeHelpButton.setMinimumSize(new java.awt.Dimension(16, 16));
        stageOneOutcomeHelpButton.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel13.add(stageOneOutcomeHelpButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 198, -1, -1));

        stageOneRLEHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon4 - Copy.png"))); // NOI18N
        stageOneRLEHelpButton.setToolTipText("<html><pre>\nSelect “Intercept only”, and the model assumes the mean of the response does not differ between\nsubjects as a result of some covariate and engages the MixRegLS model, allowing users to\nspecify covariates for WS and BS variances. \n\nSelect “Intercept and slope(s)”, this will be MixRegMLS model. Adding slopes can have more than 2 random location effects, but estimation time is increased with each additional random effect.<pre>");
        stageOneRLEHelpButton.setMaximumSize(new java.awt.Dimension(16, 16));
        stageOneRLEHelpButton.setMinimumSize(new java.awt.Dimension(16, 16));
        stageOneRLEHelpButton.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel13.add(stageOneRLEHelpButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 266, -1, -1));

        stageOneRSHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon4 - Copy.png"))); // NOI18N
        stageOneRSHelpButton.setToolTipText("<html><pre>Random scale parameters allow subjects to have individual estimates of the \nwithin-subject variance, and this is the distinguishing feature of a mixed-eefects locatio nscale model.\nFor random scale models, a linear or quadratic association is also possible.<pre>");
        stageOneRSHelpButton.setMaximumSize(new java.awt.Dimension(16, 16));
        stageOneRSHelpButton.setMinimumSize(new java.awt.Dimension(16, 16));
        stageOneRSHelpButton.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel13.add(stageOneRSHelpButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 300, -1, -1));

        stageTwoDescription.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon4 - Copy.png"))); // NOI18N
        stageTwoDescription.setToolTipText("<html><pre>(To be implemented in MixWILD 2.0) Currently, you are restricted to single level models and\ncontinuous and dichotomous/ordinal Stage 2 outcomes. Multilevel models allow for additional estimation at\nStage 2 using random intercept mixed effects model in place of the standard single level model, similar to\nStage 1. Continuous outcomes will run a linear regression at Stage 2. Dichotomous and ordinal outcomes \nwill run an ordered logistic regression at Stage 2.<pre>");
        stageTwoDescription.setMaximumSize(new java.awt.Dimension(16, 16));
        stageTwoDescription.setMinimumSize(new java.awt.Dimension(16, 16));
        stageTwoDescription.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel13.add(stageTwoDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 351, -1, -1));

        stageTwoModelTypeHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon4 - Copy.png"))); // NOI18N
        stageTwoModelTypeHelpButton.setToolTipText("<html><pre>Multilevel models allow for additional estimation at stage 2 \nusing random intercept mixed effect model.<pre>");
        stageTwoModelTypeHelpButton.setMaximumSize(new java.awt.Dimension(16, 16));
        stageTwoModelTypeHelpButton.setMinimumSize(new java.awt.Dimension(16, 16));
        stageTwoModelTypeHelpButton.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel13.add(stageTwoModelTypeHelpButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 456, -1, -1));

        stageTwoOutcomeTypeHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon4 - Copy.png"))); // NOI18N
        stageTwoOutcomeTypeHelpButton.setToolTipText("<html><pre>This stage 2 outcome can be a subject-level or 2-level outcome, and can be of four different outcome types:\ncontinuous (normal), dichotomous/ordinal, count, or nominal.<pre>");
        stageTwoOutcomeTypeHelpButton.setMaximumSize(new java.awt.Dimension(16, 16));
        stageTwoOutcomeTypeHelpButton.setMinimumSize(new java.awt.Dimension(16, 16));
        stageTwoOutcomeTypeHelpButton.setPreferredSize(new java.awt.Dimension(16, 16));
        jPanel13.add(stageTwoOutcomeTypeHelpButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 491, -1, -1));

        StageOneModelTypeLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        StageOneModelTypeLabel.setText("Stage 1 regression type: ");
        jPanel13.add(StageOneModelTypeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 230, -1, -1));

        buttonGroup5.add(StageOneLogisticRadio);
        StageOneLogisticRadio.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        StageOneLogisticRadio.setText("Logistic");
        StageOneLogisticRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StageOneLogisticRadioActionPerformed(evt);
            }
        });
        jPanel13.add(StageOneLogisticRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(691, 226, -1, -1));

        buttonGroup5.add(StageOneProbitRadio);
        StageOneProbitRadio.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        StageOneProbitRadio.setText("Probit");
        StageOneProbitRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StageOneProbitRadioActionPerformed(evt);
            }
        });
        jPanel13.add(StageOneProbitRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 226, -1, -1));

        includeStageTwoDataLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        includeStageTwoDataLabel.setText("Include separate Stage 2 data file:");
        includeStageTwoDataLabel.setToolTipText("");
        jPanel13.add(includeStageTwoDataLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 384, -1, -1));

        DataFileStageTwoLabel.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        DataFileStageTwoLabel.setText("Stage 2 CSV file path:");
        DataFileStageTwoLabel.setToolTipText("");
        jPanel13.add(DataFileStageTwoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 419, -1, -1));

        stageTwoDataButtonGroup.add(includeStageTwoDataYes);
        includeStageTwoDataYes.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        includeStageTwoDataYes.setText("Yes");
        includeStageTwoDataYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                includeStageTwoDataYesActionPerformed(evt);
            }
        });
        jPanel13.add(includeStageTwoDataYes, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 380, -1, -1));

        stageTwoDataButtonGroup.add(includeStageTwoDataNo);
        includeStageTwoDataNo.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        includeStageTwoDataNo.setText("No");
        includeStageTwoDataNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                includeStageTwoDataNoActionPerformed(evt);
            }
        });
        jPanel13.add(includeStageTwoDataNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(643, 380, -1, -1));

        fileBrowseButtonStageTwoData.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        fileBrowseButtonStageTwoData.setText("Import Dataset");
        fileBrowseButtonStageTwoData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileBrowseButtonStageTwoDataActionPerformed(evt);
            }
        });
        jPanel13.add(fileBrowseButtonStageTwoData, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 414, 145, 30));

        filePath_stageTwo.setEditable(false);
        filePath_stageTwo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filePath_stageTwoActionPerformed(evt);
            }
        });
        jPanel13.add(filePath_stageTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 419, 254, -1));

        stageOneTabs.addTab("Model Configuration", jPanel13);

        jPanel1.setMinimumSize(new java.awt.Dimension(1000, 700));
        jPanel1.setPreferredSize(new java.awt.Dimension(1300, 700));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        clearStageOneButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        clearStageOneButton.setText("Clear Stage 1");
        clearStageOneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearStageOneButtonActionPerformed(evt);
            }
        });
        jPanel1.add(clearStageOneButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 530, 140, 35));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setText("Stage 1 Regressors");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 18, -1, -1));

        level1_MeanReg.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        level1_MeanReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        level1_MeanReg.setText("Mean");
        level1_MeanReg.setToolTipText("Select the regressors in [Mean  Model] to predict the mean value of the outcome variable ");
        level1_MeanReg.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(level1_MeanReg, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 70, 140, -1));
        level1_MeanReg.getAccessibleContext().setAccessibleName("");

        level1_WSVar.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        level1_WSVar.setText("WS Variance");
        level1_WSVar.setToolTipText("Select the regressors to predict  the within-subject variance of the  outcome variable");
        jPanel1.add(level1_WSVar, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 70, -1, -1));

        level2_MeanReg.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        level2_MeanReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        level2_MeanReg.setText("Mean");
        level2_MeanReg.setToolTipText("Select the regressors in [Mean  Model] to predict the mean value of the outcome variable ");
        level2_MeanReg.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(level2_MeanReg, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 280, 140, -1));

        level2_BSVar.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        level2_BSVar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        level2_BSVar.setText("BS Variance");
        level2_BSVar.setToolTipText("Select the regressors to predict  the between-subject variance of  the outcome variable");
        jPanel1.add(level2_BSVar, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 280, 110, -1));

        level2_WSVar.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        level2_WSVar.setText("WS Variance");
        level2_WSVar.setToolTipText("Select the regressors to predict  the within-subject variance of the  outcome variable");
        jPanel1.add(level2_WSVar, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 280, -1, -1));

        level1_BSVar.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        level1_BSVar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        level1_BSVar.setText("BS Variance");
        level1_BSVar.setToolTipText("Select the regressors to predict  the between-subject variance of  the outcome variable");
        level1_BSVar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(level1_BSVar, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 70, 110, -1));

        stageOneLevelOnePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Level-1"));

        levelOneGrid.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout stageOneLevelOnePanelLayout = new javax.swing.GroupLayout(stageOneLevelOnePanel);
        stageOneLevelOnePanel.setLayout(stageOneLevelOnePanelLayout);
        stageOneLevelOnePanelLayout.setHorizontalGroup(
            stageOneLevelOnePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(levelOneGrid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        stageOneLevelOnePanelLayout.setVerticalGroup(
            stageOneLevelOnePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(levelOneGrid, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
        );

        levelOneGrid.getAccessibleContext().setAccessibleName("Level-1");

        jPanel1.add(stageOneLevelOnePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 90, 540, 170));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, 540, 10));
        jPanel1.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 50, -1, 140));

        stageOneLevelTwoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Level-2"));

        levelTwoGrid.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout stageOneLevelTwoPanelLayout = new javax.swing.GroupLayout(stageOneLevelTwoPanel);
        stageOneLevelTwoPanel.setLayout(stageOneLevelTwoPanelLayout);
        stageOneLevelTwoPanelLayout.setHorizontalGroup(
            stageOneLevelTwoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(levelTwoGrid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        stageOneLevelTwoPanelLayout.setVerticalGroup(
            stageOneLevelTwoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(levelTwoGrid, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
        );

        jPanel1.add(stageOneLevelTwoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 300, 540, 180));

        StageOneOutcomeCombo.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        StageOneOutcomeCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        StageOneOutcomeCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                StageOneOutcomeComboItemStateChanged(evt);
            }
        });
        StageOneOutcomeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StageOneOutcomeComboActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("Stage 1 Outcome:");

        IDvariableCombo.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
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

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("ID Variable:");

        addStageOneButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        addStageOneButton.setText("Configure Stage 1 Regressors ...");
        addStageOneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStageOneButtonActionPerformed(evt);
            }
        });

        advancedOptionsButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        advancedOptionsButton.setText("Options ...");
        advancedOptionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advancedOptionsButtonActionPerformed(evt);
            }
        });

        associationLabel.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        associationLabel.setText("<html>Specify the relationship between the <br>mean and WS variance.<br></html>");

        buttonGroup1.add(NoAssociationRadio);
        NoAssociationRadio.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        NoAssociationRadio.setText("No Association");
        NoAssociationRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoAssociationRadioActionPerformed(evt);
            }
        });

        buttonGroup1.add(LinearAssociationRadio);
        LinearAssociationRadio.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        LinearAssociationRadio.setText("Linear Association");
        LinearAssociationRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LinearAssociationRadioActionPerformed(evt);
            }
        });

        buttonGroup1.add(QuadraticAssociationRadio);
        QuadraticAssociationRadio.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        QuadraticAssociationRadio.setText("Quadratic Association");
        QuadraticAssociationRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuadraticAssociationRadioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout associationPanelLayout = new javax.swing.GroupLayout(associationPanel);
        associationPanel.setLayout(associationPanelLayout);
        associationPanelLayout.setHorizontalGroup(
            associationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(associationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(associationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(associationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addGroup(associationPanelLayout.createSequentialGroup()
                        .addGroup(associationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(NoAssociationRadio)
                            .addComponent(LinearAssociationRadio)
                            .addComponent(QuadraticAssociationRadio))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        associationPanelLayout.setVerticalGroup(
            associationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(associationPanelLayout.createSequentialGroup()
                .addComponent(associationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(NoAssociationRadio)
                .addGap(12, 12, 12)
                .addComponent(LinearAssociationRadio)
                .addGap(12, 12, 12)
                .addComponent(QuadraticAssociationRadio)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(randomLocationEffectsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 34, -1, -1));
        jPanel7.add(stageTwoOutcomePrintLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 55, -1, -1));

        jLabel21.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel21.setText("Selected Model Configuration");
        jPanel7.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 12, 212, -1));
        jPanel7.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 28, 165, 6));

        randomLocationEffectsLabel1.setText("Stage 1 model:");
        jPanel7.add(randomLocationEffectsLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 34, -1, -1));

        stageTwoOutcomePrintLabel1.setText("State 1 outcome:");
        jPanel7.add(stageTwoOutcomePrintLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 55, -1, -1));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(IDvariableCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(StageOneOutcomeCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addStageOneButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSeparator10)
                                .addComponent(jLabel2)
                                .addComponent(jSeparator9)
                                .addComponent(jLabel1)
                                .addComponent(jSeparator7)
                                .addComponent(advancedOptionsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSeparator11))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(associationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(IDvariableCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(StageOneOutcomeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(addStageOneButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(advancedOptionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(associationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 310, 560));
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 530, -1, -1));

        startStageTwo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        startStageTwo.setText("Configure Stage 2");
        startStageTwo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startStageTwoActionPerformed(evt);
            }
        });
        jPanel1.add(startStageTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 530, 160, 35));

        guiStatesSaveButtonStageOne.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        guiStatesSaveButtonStageOne.setText("Save Model");
        guiStatesSaveButtonStageOne.setMaximumSize(new java.awt.Dimension(99, 25));
        guiStatesSaveButtonStageOne.setMinimumSize(new java.awt.Dimension(99, 25));
        guiStatesSaveButtonStageOne.setPreferredSize(new java.awt.Dimension(99, 25));
        guiStatesSaveButtonStageOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guiStatesSaveButtonStageOneActionPerformed(evt);
            }
        });
        jPanel1.add(guiStatesSaveButtonStageOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 530, 140, 35));

        stageOneTabs.addTab("Stage 1 Configuration", jPanel1);

        jPanel12.setName(""); // NOI18N
        jPanel12.setPreferredSize(new java.awt.Dimension(1000, 700));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel14.setText("Regressor(s)");
        jLabel14.setToolTipText("Check the box(s) of the regressor(s) to add the main effect in the model");
        jPanel12.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 100, 100, -1));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setText("Stage 2 Interactions");
        jPanel12.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 18, 190, 20));

        jLabel15.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel15.setText("Random Location");
        jLabel15.setToolTipText("Check the box(s) of the regressor(s) to  add the interaction effect by random location (intercept + slope(s)) in the model");
        jPanel12.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(655, 100, 140, -1));

        addStageTwoReg.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        addStageTwoReg.setText("Configure Stage 2 Regressors ...");
        addStageTwoReg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStageTwoRegActionPerformed(evt);
            }
        });
        jPanel12.add(addStageTwoReg, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 295, 260, 40));

        jLabel17.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel17.setText("Random Scale");
        jLabel17.setToolTipText("Check the box(s) of the regressor(s) to add the interaction effect by random  scale in the model");
        jPanel12.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(799, 100, 110, 20));

        jLabel18.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel18.setText("Location X Scale");
        jLabel18.setToolTipText("Check the box(s) of the regressor(s) to add the interaction effect by random location and scale in the model");
        jPanel12.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 100, 130, 20));

        stageTwoLevelTwoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Level-2"));

        stageTwoRegsGridLvl2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        stageTwoRegsGridLvl2.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout stageTwoLevelTwoPanelLayout = new javax.swing.GroupLayout(stageTwoLevelTwoPanel);
        stageTwoLevelTwoPanel.setLayout(stageTwoLevelTwoPanelLayout);
        stageTwoLevelTwoPanelLayout.setHorizontalGroup(
            stageTwoLevelTwoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(stageTwoRegsGridLvl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        stageTwoLevelTwoPanelLayout.setVerticalGroup(
            stageTwoLevelTwoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(stageTwoRegsGridLvl2, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
        );

        stageTwoRegsGridLvl2.getAccessibleContext().setAccessibleParent(stageTwoRegsGridLvl2);

        jPanel12.add(stageTwoLevelTwoPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 330, 610, 170));
        stageTwoLevelTwoPanel.getAccessibleContext().setAccessibleName("Level-2 Stage-2");
        stageTwoLevelTwoPanel.getAccessibleContext().setAccessibleDescription("");

        runTabTwoStageOneTwo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        runTabTwoStageOneTwo.setText("Run Stage 1 and 2");
        runTabTwoStageOneTwo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runTabTwoStageOneTwoActionPerformed(evt);
            }
        });
        jPanel12.add(runTabTwoStageOneTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 530, 160, 35));

        enbaleInteractionCheckBox.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        enbaleInteractionCheckBox.setText("Enable 2-way Location X Scale Interaction");
        enbaleInteractionCheckBox.setToolTipText("<html><pre>The interaction(s) of location by scale are automatically specified in the default Stage 2 model, \nbut this option can be disabled by checking this box, which limits the model to show the main effects of random effects only <pre>");
        enbaleInteractionCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enbaleInteractionCheckBoxActionPerformed(evt);
            }
        });
        jPanel12.add(enbaleInteractionCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 60, -1, -1));

        StageTwoOutcomeCombo.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        StageTwoOutcomeCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        StageTwoOutcomeCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                StageTwoOutcomeComboItemStateChanged(evt);
            }
        });
        StageTwoOutcomeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StageTwoOutcomeComboActionPerformed(evt);
            }
        });
        jPanel12.add(StageTwoOutcomeCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 250, 260, 30));

        jLabel22.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel22.setText("Stage 2 Outcome:");
        jPanel12.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, 210, -1));

        clearStageTwoButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        clearStageTwoButton.setText("Clear Stage 2");
        clearStageTwoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearStageTwoButtonActionPerformed(evt);
            }
        });
        jPanel12.add(clearStageTwoButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 530, 140, 35));
        jPanel12.add(filler1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 570, -1, 80));
        jPanel12.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 50, 610, 10));
        jPanel12.add(jSeparator15, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 660, -1, -1));
        jPanel12.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 262, 220, 0));

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        outcomeCatButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
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
                    .addComponent(outcomeCatButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(outcomeCatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 350, 260, 200));

        guiStatesSaveButtonStageTwo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        guiStatesSaveButtonStageTwo.setText("Save Model");
        guiStatesSaveButtonStageTwo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guiStatesSaveButtonStageTwoActionPerformed(evt);
            }
        });
        jPanel12.add(guiStatesSaveButtonStageTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 530, 130, 35));

        stageTwoLevelOnePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Level-1"));

        stageTwoRegsGridLvl1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        stageTwoRegsGridLvl1.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout stageTwoLevelOnePanelLayout = new javax.swing.GroupLayout(stageTwoLevelOnePanel);
        stageTwoLevelOnePanel.setLayout(stageTwoLevelOnePanelLayout);
        stageTwoLevelOnePanelLayout.setHorizontalGroup(
            stageTwoLevelOnePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(stageTwoRegsGridLvl1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        stageTwoLevelOnePanelLayout.setVerticalGroup(
            stageTwoLevelOnePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(stageTwoRegsGridLvl1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
        );

        jPanel12.add(stageTwoLevelOnePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 120, 610, 170));

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel3.setText("Selected Model Configuration");
        jPanel12.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 25, -1, -1));
        jLabel3.getAccessibleContext().setAccessibleName("");

        jPanel12.add(stageOneOutcomeStageTwoConfigLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 70, -1, -1));
        jPanel12.add(stageOneModelStageTwoConfigLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, -1, -1));
        jPanel12.add(stageTwoOutcomeStageTwoConfigLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 110, -1, -1));
        jPanel12.add(stageTwoModelTypeStageTwoConfigLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(205, 90, -1, -1));
        jPanel12.add(numResamplingStageTwoConfigLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 130, -1, -1));
        jPanel12.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, 260, 10));
        jPanel12.add(jSeparator17, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 41, 165, 10));

        jLabel33.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel33.setText("Regressor(s)");
        jLabel33.setToolTipText("Check the box(s) of the regressor(s) to add the main effect in the model");
        jPanel12.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 310, 110, -1));

        jLabel35.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel35.setText("Random Location");
        jLabel35.setToolTipText("Check the box(s) of the regressor(s) to  add the interaction effect by random location (intercept + slope(s)) in the model");
        jPanel12.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(655, 310, 140, -1));

        jLabel36.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel36.setText("Random Scale");
        jLabel36.setToolTipText("Check the box(s) of the regressor(s) to add the interaction effect by random  scale in the model");
        jPanel12.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(799, 310, 110, 20));

        jLabel37.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel37.setText("Location X Scale");
        jLabel37.setToolTipText("Check the box(s) of the regressor(s) to add the interaction effect by random location and scale in the model");
        jPanel12.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 310, 130, 20));

        stageOneModelStageTwoConfigLabel1.setText("Stage 1 model:");
        jPanel12.add(stageOneModelStageTwoConfigLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, -1));

        stageOneOutcomeStageTwoConfigLabel1.setText("Stage 1 outcome:");
        jPanel12.add(stageOneOutcomeStageTwoConfigLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, -1, -1));

        stageTwoModelTypeStageTwoConfigLabel1.setText("Stage 2 model type:");
        jPanel12.add(stageTwoModelTypeStageTwoConfigLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, -1, -1));

        stageTwoOutcomeStageTwoConfigLabel1.setText("Stage 2 outcome:");
        jPanel12.add(stageTwoOutcomeStageTwoConfigLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, -1, -1));

        numResamplingStageTwoConfigLabel1.setText("Number of resamples (stage 2):");
        jPanel12.add(numResamplingStageTwoConfigLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, -1, -1));
        jPanel12.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 155, 260, 10));

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setText("ID Variable:");
        jPanel12.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, -1, -1));

        IDStageTwoVariableCombo.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        IDStageTwoVariableCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        IDStageTwoVariableCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                IDStageTwoVariableComboItemStateChanged(evt);
            }
        });
        IDStageTwoVariableCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDStageTwoVariableComboActionPerformed(evt);
            }
        });
        jPanel12.add(IDStageTwoVariableCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, 260, 30));

        jLabel32.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel32.setText("X");
        jPanel12.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 310, -1, -1));

        jLabel38.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel38.setText("X");
        jPanel12.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 100, -1, -1));

        stageOneTabs.addTab("Stage 2 Configuration", jPanel12);

        jPanel3.setPreferredSize(new java.awt.Dimension(1000, 700));

        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        stageOneOutput.setColumns(20);
        stageOneOutput.setFont(new java.awt.Font("Courier New", 0, 11)); // NOI18N
        stageOneOutput.setRows(5);
        jScrollPane2.setViewportView(stageOneOutput);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                .addContainerGap())
        );

        saveStage1OutButton.setText("Save Results As ...");
        saveStage1OutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveStage1OutButtonActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setText("Results from stage 1 analysis");

        openStage1OutButton.setText("Open Results In Editor");
        openStage1OutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openStage1OutButtonActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel8.setText("*For examples of results interpretation, please check the user guide.");
        jLabel8.setToolTipText("");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addGap(308, 308, 308)
                            .addComponent(openStage1OutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(saveStage1OutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel7))
                .addContainerGap(354, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(openStage1OutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveStage1OutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(80, Short.MAX_VALUE))
        );

        stageOneTabs.addTab("Stage 1 Results", jPanel3);

        jPanel4.setPreferredSize(new java.awt.Dimension(1000, 700));

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel11.setPreferredSize(new java.awt.Dimension(762, 462));

        stageTwoOutput.setColumns(20);
        stageTwoOutput.setFont(new java.awt.Font("Courier New", 0, 11)); // NOI18N
        stageTwoOutput.setRows(5);
        jScrollPane1.setViewportView(stageTwoOutput);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                .addContainerGap())
        );

        saveStage2OutButton.setText("Save Results As ...");
        saveStage2OutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveStage2OutButtonActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel11.setText("Results from stage 2 analysis");
        jLabel11.setToolTipText("");

        openStage2OutButton.setText("Open Results In Editor");
        openStage2OutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openStage2OutButtonActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel9.setText("*For examples of results interpretation, please check the user guide.");
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(openStage2OutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(saveStage2OutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11)
                        .addComponent(jLabel9)))
                .addContainerGap(355, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(7, 7, 7)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(openStage2OutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveStage2OutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80))
        );

        stageOneTabs.addTab("Stage 2 Results", jPanel4);

        jPanel2.setPreferredSize(new java.awt.Dimension(1300, 700));
        jPanel2.setRequestFocusEnabled(false);
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setPreferredSize(new java.awt.Dimension(1000, 800));

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
                .addContainerGap(461, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1210, 660));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/mixLogo.png"))); // NOI18N
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 550, -1, 40));

        stageOneTabs.addTab("View Model", jPanel2);

        jPanel6.setMaximumSize(new java.awt.Dimension(1200, 700));
        jPanel6.setName(""); // NOI18N
        jPanel6.setPreferredSize(new java.awt.Dimension(1300, 700));

        dataTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(dataTable);

        jLabel20.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel20.setText("Imported data file:");

        printedFileName.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        printedFileName.setText("filename");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 903, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(31, 31, 31)
                        .addComponent(printedFileName)))
                .addContainerGap(208, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(printedFileName))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(113, Short.MAX_VALUE))
        );

        stageOneTabs.addTab("View Data", jPanel6);

        jLabel29.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel29.setText("Imported data file:");

        printedFileName_stageTwo.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        printedFileName_stageTwo.setText("filename");

        dataTable_stageTwo.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(dataTable_stageTwo);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 903, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addGap(31, 31, 31)
                        .addComponent(printedFileName_stageTwo)))
                .addContainerGap(208, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(printedFileName_stageTwo))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(113, Short.MAX_VALUE))
        );

        stageOneTabs.addTab("View Stage 2 Data", jPanel16);

        jLabel19.setText("Stage Two Outcome:");

        jLabel24.setText("Selected Outcome");

        jLabel25.setText("Stage One Outcome:");

        jLabel26.setText("Selected Outcome");

        jLabel27.setText("Selected Model Type at S1 with Model at S2");

        jLabel28.setText("Number of Samples:");

        jTextField1.setText("Selected Samples");

        jLabel30.setText("Cutoff:");

        jTextField2.setText("0.00000");

        jButton3.setText("?");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel30)
                        .addGap(30, 30, 30)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel27)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(864, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel24))
                .addGap(59, 59, 59)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addContainerGap(463, Short.MAX_VALUE))
        );

        stageOneTabs.addTab("Postestimation", jPanel14);

        jPanel15.setPreferredSize(new java.awt.Dimension(1295, 700));

        userGuideDownload.setText("Download MixWild User Guide");
        userGuideDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userGuideDownloadActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel31.setText("Supplement Resources");

        exampleDataDownload.setText("Download MixWILD Example Dataset");
        exampleDataDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exampleDataDownloadActionPerformed(evt);
            }
        });

        online_support_help_label.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        online_support_help_label.setText("Online Support");

        online_support_button.setText("Open Github Discussion Group");
        online_support_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                online_support_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(online_support_button)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(online_support_help_label)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(userGuideDownload)
                            .addComponent(exampleDataDownload))
                        .addGap(868, 868, 868))))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(userGuideDownload)
                .addGap(18, 18, 18)
                .addComponent(exampleDataDownload)
                .addGap(18, 18, 18)
                .addComponent(online_support_help_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(online_support_button)
                .addGap(1413, 1413, 1413))
        );

        stageOneTabs.addTab("Help", jPanel15);

        getContentPane().add(stageOneTabs, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveStage2OutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveStage2OutButtonActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "saveStage2OutButtonActionPerformed");
        try {
            // TODO add your handling code here:

            saveStageTwoOutput();
        } catch (IOException ex) {
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
        }
    }//GEN-LAST:event_saveStage2OutButtonActionPerformed

    private void saveStage1OutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveStage1OutButtonActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "jButton8ActionPerformed");
        saveStageOneOutput();
    }//GEN-LAST:event_saveStage1OutButtonActionPerformed

    private void outcomeCatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outcomeCatButtonActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "outcomeCatButtonActionPerformed");
        //get the categories of the stage two outcome variable
        System.out.println("Inside click for categories");

        ArrayList<String> ColumnsCustom = new ArrayList<>();
        ArrayList<String> UniqueList = new ArrayList<>();

        String dataFileName = getDataFileName(2);
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

                int index = StageTwoOutcomeCombo.getSelectedIndex();
                ColumnsCustom.add(Columns[index]);

            }

//            System.out.println("COLUMN:");
//            for (int k = 0; k < ColumnsCustom.size(); k++) {
//                System.out.println(ColumnsCustom.get(k));
//            }
            //            if (defFile.getAdvancedMissingValue().contains(".")){
            //            String strippedMissingVal = defFile.getAdvancedMissingValue().substring(0,defFile.getAdvancedMissingValue().indexOf('.'));
            //            }
            //
            //count the unique ones
            for (int x = 0; x < ColumnsCustom.size(); x++) {
                if (UniqueList.contains(ColumnsCustom.get(x))) {
                    //do nothing
                } else if (ColumnsCustom.get(x).equals(defFile.getAdvancedMissingValueCode()) && !ColumnsCustom.get(x).equals("0")) { //compare if the category is a missing value, then don't consider it as a category
                    //do nothing

                } else {
                    UniqueList.add(ColumnsCustom.get(x));
                }

            }

            //sort UniqueList First
            ArrayList<Double> UniqueIntegers = new ArrayList<>();

            for (int x = 0; x < UniqueList.size(); x++) {

                UniqueIntegers.add(Double.valueOf(UniqueList.get(x)));

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
            SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
            e.printStackTrace();
            Logger.getLogger(getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        } catch (IOException e) {
            SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
                    e.printStackTrace();
                }
            }
        }

    }//GEN-LAST:event_outcomeCatButtonActionPerformed

    private void clearStageTwoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearStageTwoButtonActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "jButton1ActionPerformed");
        SystemLogger.LOGGER.log(Level.INFO, "Clear Stage Two");
        clearStageTwoLevelOneGrid();
        clearStageTwoLevelTwoGrid();
        enbaleInteractionCheckBox.setEnabled(true);
        enbaleInteractionCheckBox.setSelected(false);

    }//GEN-LAST:event_clearStageTwoButtonActionPerformed

    private void StageTwoOutcomeComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_StageTwoOutcomeComboItemStateChanged
        // TODO add your handling code here:
        stageTwoPos = StageTwoOutcomeCombo.getSelectedIndex();
        System.out.println("STAGE TWO OUTCOME CHANGED: " + String.valueOf(stageTwoPos));
        isStageTwoOutcomeChanged = true;
    }//GEN-LAST:event_StageTwoOutcomeComboItemStateChanged

    private void enbaleInteractionCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enbaleInteractionCheckBoxActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "suppressIntCheckBoxActionPerformed");
        update_trigger_enableInteractionCheckBox();
    }//GEN-LAST:event_enbaleInteractionCheckBoxActionPerformed

    private void runTabTwoStageOneTwoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runTabTwoStageOneTwoActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "runTabTwoStageOneTwoActionPerformed");
        SystemLogger.LOGGER.log(Level.INFO, "Run Stage One and Two");
        isStageTwoSubmitted = true;
        update_trigger_runTabTwoStageOneTwo();

    }//GEN-LAST:event_runTabTwoStageOneTwoActionPerformed

    private void addStageTwoRegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStageTwoRegActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "addStageTwoRegActionPerformed");

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
    }//GEN-LAST:event_addStageTwoRegActionPerformed

    private void startStageTwoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startStageTwoActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "startStageTwoActionPerformed");
        SystemLogger.LOGGER.log(Level.INFO, "Submit stage one configurations");
        isStageOneSubmitted = true;
        update_trigger_StartStageTwo();
    }//GEN-LAST:event_startStageTwoActionPerformed

    private void LinearAssociationRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LinearAssociationRadioActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "LinearAssociationRadioActionPerformed");
    }//GEN-LAST:event_LinearAssociationRadioActionPerformed

    private void NoAssociationRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoAssociationRadioActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "NoAssociationRadioActionPerformed");
    }//GEN-LAST:event_NoAssociationRadioActionPerformed

    private void advancedOptionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advancedOptionsButtonActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "advancedOptionsButtonActionPerformed");
        //advancedOptions_view = new advancedOptions();

        advancedOptions_view.setVisible(true);
    }//GEN-LAST:event_advancedOptionsButtonActionPerformed

    private void addStageOneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStageOneButtonActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "addStageOneButtonActionPerformed");
        stageOneClicked = 1;

        IDpos = IDvariableCombo.getSelectedIndex();
        stageOnePos = StageOneOutcomeCombo.getSelectedIndex();
        stageTwoPos = StageTwoOutcomeCombo.getSelectedIndex();

        if (stage_1_regs.isVisible()) { //if it is already open and visible in the background

            // stage_1_regs.set
            stage_1_regs.setFocusable(true);
            stageOneClicked = 1;
            addStageTwoReg.setEnabled(true);

        } else {
            //stage_1_regs.revalidate();
            //stage_1_regs.repaint();
            // stage_1_regs.removeAll();

            stageOneClicked = 1;
            addStageTwoReg.setEnabled(true);

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
//                stage_1_regs.updateAllVariables();
            } else {
                stage_1_regs.setVisible(true);
                stage_1_regs.updateAllVariables();
            }
        }

        addStageOneCHecked = true;

    }//GEN-LAST:event_addStageOneButtonActionPerformed

    private void IDvariableComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDvariableComboActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "IDvariableComboActionPerformed");
    }//GEN-LAST:event_IDvariableComboActionPerformed

    private void IDvariableComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_IDvariableComboItemStateChanged
        // TODO add your handling code here:

        IDpos = IDvariableCombo.getSelectedIndex();
        System.out.println("ID CHANGED: " + String.valueOf(IDpos));
        isIDChanged = true;
    }//GEN-LAST:event_IDvariableComboItemStateChanged

    private void StageOneOutcomeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StageOneOutcomeComboActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "StageOneOutcomeComboActionPerformed");
    }//GEN-LAST:event_StageOneOutcomeComboActionPerformed

    private void StageOneOutcomeComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_StageOneOutcomeComboItemStateChanged
        // TODO add your handling code here:
        stageOnePos = StageOneOutcomeCombo.getSelectedIndex();
        System.out.println("STAGE ONE OUTCOME CHANGED: " + String.valueOf(stageOnePos));
        isStageOneOutcomeChanged = true;
    }//GEN-LAST:event_StageOneOutcomeComboItemStateChanged

    private void clearStageOneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearStageOneButtonActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "resetButtonActionPerformed");
        SystemLogger.LOGGER.log(Level.INFO, "Clear Stage One");

        IDvariableCombo.setSelectedIndex(0);
        StageOneOutcomeCombo.setSelectedIndex(1);
        StageTwoOutcomeCombo.setSelectedIndex(2);

        buttonGroup1.clearSelection();

        //addStageTwoTabTwo.setEnabled(false);
        stageOneLevelTwoPanel.removeAll();
        stageOneLevelTwoPanel.revalidate();
        stageOneLevelTwoPanel.repaint();

        stageOneLevelOnePanel.removeAll();
        stageOneLevelOnePanel.revalidate();
        stageOneLevelOnePanel.repaint();

        stage_1_regs.updateAllVariables();

        stage_1_regs.levelOneList.clear();
        stage_1_regs.levelTwoList.clear();
        updateStageOneLevelTwoGrid(stage_1_regs.levelTwoList);
        updateStageOneLevelOneGrid(stage_1_regs.levelOneList);

    }//GEN-LAST:event_clearStageOneButtonActionPerformed

    private void userGuideDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userGuideDownloadActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "userGuideDownloadActionPerformed");

        // user open filechooser and select save path
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("MixWild_User_Guide.pdf"));
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File dest = fileChooser.getSelectedFile();

            // copy file from resources to user selected save path
            InputStream stream = null;
            try {
                URL inputUrl = getClass().getResource("/resources/UserGuide.pdf");
                FileUtils.copyURLToFile(inputUrl, dest);
            } catch (IOException e) {
                SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Exception e) {
                        SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
                        e.printStackTrace();
                    }
                }
            }
        }
    }//GEN-LAST:event_userGuideDownloadActionPerformed

    private void exampleDataDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exampleDataDownloadActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "exampleDataDownloadActionPerformed");

        // user open filechooser and select save path
        JFileChooser filechooser_sample = new JFileChooser();
        filechooser_sample.setSelectedFile(new File("Mixwild_example_data.csv"));
        int option = filechooser_sample.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File dest = filechooser_sample.getSelectedFile();

            // copy file from resources to user selected save path
            InputStream stream = null;
            try {
                URL inputUrl = getClass().getResource("/resources/ExampleData.csv");
                FileUtils.copyURLToFile(inputUrl, dest);
            } catch (IOException e) {
                SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
                    }
                }
            }
        }
    }//GEN-LAST:event_exampleDataDownloadActionPerformed

    private void guiStatesSaveButtonStageOneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guiStatesSaveButtonStageOneActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "guiStatesSaveButtonStageOneActionPerformed");
        SystemLogger.LOGGER.log(Level.INFO, "Saving GUI states");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        MXRStates.writeAllStates(this);
    }//GEN-LAST:event_guiStatesSaveButtonStageOneActionPerformed

    private void guiStatesSaveButtonStageTwoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guiStatesSaveButtonStageTwoActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "guiStatesSaveButtonStageTwoActionPerformed");
        SystemLogger.LOGGER.log(Level.INFO, "Saving GUI states");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        MXRStates.writeAllStates(this);
    }//GEN-LAST:event_guiStatesSaveButtonStageTwoActionPerformed

    private void QuadraticAssociationRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuadraticAssociationRadioActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "QuadraticAssociationRadioActionPerformed");
    }//GEN-LAST:event_QuadraticAssociationRadioActionPerformed

    private void StageTwoOutcomeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StageTwoOutcomeComboActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "StageTwoOutcomeComboActionPerformed");
    }//GEN-LAST:event_StageTwoOutcomeComboActionPerformed

    private void newDataSetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newDataSetButtonActionPerformed
        importDataSet();
        SystemLogger.LOGGER.log(Level.FINE, "newDataSetButtonActionPerformed");

        if (validDataset) {
            MXRStates = new MixRegGuiStates(this, advancedOptions_view);
            updateGuiView(MXRStates);

            newDataSetButton.setVisible(false);
            loadModelByBrowseButton.setVisible(false);

            jSeparator8.setVisible(true);
            jLabel34.setVisible(true);
            loadModelByBrowseButton.setEnabled(false);
            loadModelByBrowseButton.setVisible(false);

            showHiddenBigIconLabel(false);
            dataFileLabel.setVisible(true);
            filePath.setVisible(true);
            fileBrowseButton.setVisible(true);
            datasetHelpButton.setVisible(true);
        }
    }//GEN-LAST:event_newDataSetButtonActionPerformed

    private void updateStage2ConfigButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateStage2ConfigButtonActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "updateStage2ConfigButtonActionPerformed");
        isUpdateStage2ConfigClicked = true;

        includeStageTwoYes.setEnabled(true);
        includeStageTwoNo.setEnabled(true);

        isNewModalConfigSubmitted = false;
        newModelSubmit.setVisible(true);
        newModelSubmit.setEnabled(true);
        updateStage2ConfigButton.setVisible(false);

        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_updateStage2ConfigButtonActionPerformed

    private void loadModelByBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadModelByBrowseButtonActionPerformed

        // choose saved progress file
        MXRStates = new MixRegGuiStates();
        boolean read_success = MXRStates.readAllStates(this);

        if (read_success) {
            SystemLogger.LOGGER.log(Level.FINE, "Load Model Success");
            // update view with saved states
            updateGuiView(MXRStates);
            SystemLogger.LOGGER.log(Level.FINE, "Load GUI View Success");
            // check if dataset loading is success
            if (checkTabExistinJTabbedPane(stageOneTabs, "View Data")) {
                // hide and show fields
                newDataSetButton.setVisible(false);
                loadModelByBrowseButton.setVisible(false);
                loadModelByBrowseButton.setEnabled(false);

                dataFileLabel.setVisible(true);
                filePath.setVisible(true);
                fileBrowseButton.setVisible(true);
                // select the first tab
                stageOneTabs.setSelectedIndex(0);
            }

            // reregister logger
            logFilePath = MXRStates.logFilePath;
            loadLogger(logFilePath);
        }

        SystemLogger.LOGGER.log(Level.FINE, "loadModelByBrowseButtonActionPerformed");
    }//GEN-LAST:event_loadModelByBrowseButtonActionPerformed

    private void guiStatesSaveButtonModalConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guiStatesSaveButtonModalConfigActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "guiStatesSaveButtonModalConfigActionPerformed");
        if (sessionFolderName != null) {
            SystemLogger.LOGGER.log(Level.INFO, "Saving GUI states");
        }
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        MXRStates.writeAllStates(this);
    }//GEN-LAST:event_guiStatesSaveButtonModalConfigActionPerformed

    private void guiStatesLoadButtonModalConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guiStatesLoadButtonModalConfigActionPerformed
        if (sessionFolderName != null) {
            SystemLogger.LOGGER.log(Level.INFO, "Loading GUI states");
        }

        MXRStates = new MixRegGuiStates();
        MXRStates.readAllStates(this);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_guiStatesLoadButtonModalConfigActionPerformed

    private void hiddenBigIconLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hiddenBigIconLabelMouseClicked
        // TODO add your handling code here:
        revealHiddenTabs++;
        if (revealHiddenTabs > 4) {
            stageOneTabs.setEnabledAt(7, true);
            stageOneTabs.setEnabledAt(8, true);
            superUserMenuLaunch = new SuperUserMenu();
            superUserMenuLaunch.setLocationRelativeTo(mxr);
            superUserMenuLaunch.setVisible(true);
        }
    }//GEN-LAST:event_hiddenBigIconLabelMouseClicked

    private void stageTwoCountRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageTwoCountRadioActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "stageTwoCountRadioActionPerformed");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_stageTwoCountRadioActionPerformed

    private void stageTwoMultinomialRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageTwoMultinomialRadioActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "stageTwoMultinomialRadioActionPerformed");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_stageTwoMultinomialRadioActionPerformed

    private void stageTwoMultiLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageTwoMultiLevelActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "stageTwoMultiLevelActionPerformed");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_stageTwoMultiLevelActionPerformed

    private void stageTwoSingleLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageTwoSingleLevelActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "stageTwoSingleLevelActionPerformed");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_stageTwoSingleLevelActionPerformed

    private void includeStageTwoNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_includeStageTwoNoActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "includeStageTwoNoActionPerformed");
        this.includeStageTwoNo.setSelected(true);
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_includeStageTwoNoActionPerformed

    private void includeStageTwoYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_includeStageTwoYesActionPerformed

        SystemLogger.LOGGER.log(Level.FINE, "includeStageTwoYesActionPerformed");
        this.includeStageTwoYes.setSelected(true);
        String seedVal = generateSeed();
        seedTextBox.setText(seedVal);

        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_includeStageTwoYesActionPerformed

    private void randomScaleSelectionNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_randomScaleSelectionNoActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "randomScaleSelectionNoActionPerformed");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_randomScaleSelectionNoActionPerformed

    private void randomScaleSelectionYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_randomScaleSelectionYesActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "randomScaleSelectionYesActionPerformed");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_randomScaleSelectionYesActionPerformed

    private void stageOneOrdinalRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageOneOrdinalRadioActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "stageOneOrdinalRadioActionPerformed");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_stageOneOrdinalRadioActionPerformed

    private void stageOneDichotomousRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageOneDichotomousRadioActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "stageOneDichotomousRadioActionPerformed");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_stageOneDichotomousRadioActionPerformed

    private void stageOneContinuousRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageOneContinuousRadioActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "stageOneContinuousRadioActionPerformed");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_stageOneContinuousRadioActionPerformed

    private void newModelSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newModelSubmitActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "newModelSubmitActionPerformed");
        isNewModalConfigSubmitted = true;
        updateGuiView_trigger_NewModelSubmit();
        SystemLogger.LOGGER.log(Level.INFO, "Submit new model");
        isUpdateStage2ConfigClicked = false;
    }//GEN-LAST:event_newModelSubmitActionPerformed

    private void newModel_resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newModel_resetButtonActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "newModel_resetButtonActionPerformed");

        if (sessionFolderName != null) {
            SystemLogger.LOGGER.log(Level.INFO, "Reset MixWild");
        }
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

        variableNamesCombo_stageOne = null;
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

    private void seedTextBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seedTextBoxActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "seedTextBoxActionPerformed : {0}", seedTextBox.getText());
    }//GEN-LAST:event_seedTextBoxActionPerformed

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

    private void newModelMissingValueCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newModelMissingValueCodeActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "newModelMissingValueCodeActionPerformed : {0}", newModelMissingValueCode.getText());
    }//GEN-LAST:event_newModelMissingValueCodeActionPerformed

    private void missingValuePresentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_missingValuePresentActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "missingValuePresentActionPerformed");

        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_missingValuePresentActionPerformed

    private void missingValueAbsentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_missingValueAbsentActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "missingValueAbsentActionPerformed");

        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_missingValueAbsentActionPerformed

    private void stageTwoDichotomousRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageTwoDichotomousRadioActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "stageTwoDichotomousRadioActionPerformed");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
        /*if (includeStageTwoNo.isSelected()){
            //Do nothing
        } else {
            setSeedLabel.setVisible(true);
            seedTextBox.setVisible(true);
            seedHelpButton.setVisible(true);
            String seedVal = generateSeed();
            seedTextBox.setText(seedVal);
        }*/
    }//GEN-LAST:event_stageTwoDichotomousRadioActionPerformed

    private void stageTwoContinuousRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageTwoContinuousRadioActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "stageTwoContinuousRadioActionPerformed");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
        /*if (includeStageTwoNo.isSelected()){
            //Do nothing

        } else {
            setSeedLabel.setVisible(true);
            seedTextBox.setVisible(true);
            seedHelpButton.setVisible(true);
            String seedVal = generateSeed();
            seedTextBox.setText(seedVal);
        }*/
    }//GEN-LAST:event_stageTwoContinuousRadioActionPerformed

    private void moreThanOneRLERadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moreThanOneRLERadioActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "moreThanOneRLERadioActionPerformed");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_moreThanOneRLERadioActionPerformed

    private void oneRLERadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oneRLERadioActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "oneRLERadioActionPerformed");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_oneRLERadioActionPerformed

    private void titleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titleFieldActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "titleFieldActionPerformed" + " : " + titleField.getText());
    }//GEN-LAST:event_titleFieldActionPerformed

    private void filePathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filePathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filePathActionPerformed

    private void fileBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileBrowseButtonActionPerformed

        SystemLogger.LOGGER.log(Level.FINE, "fileBrowseButtonActionPerformed");
        importDataSet();
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);

        //        loadModelByBrowseButton.setEnabled(false);
        //        loadModelByBrowseButton.setVisible(false);
        //        goBackButton.setVisible(false);
    }//GEN-LAST:event_fileBrowseButtonActionPerformed

    private void StageOneProbitRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StageOneProbitRadioActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "StageOneProbitRadioActionPerformed");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_StageOneProbitRadioActionPerformed

    private void StageOneLogisticRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StageOneLogisticRadioActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "StageOneLogisticRadioActionPerformed");
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_StageOneLogisticRadioActionPerformed

    private void includeStageTwoDataYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_includeStageTwoDataYesActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "includeStageTwoDataYesActionPerformed");
        this.includeStageTwoDataYes.setSelected(true);
        // check if stage 2 dataset same as stage 1 dataset

        JOptionPane.showMessageDialog(this, "Both files need to be sorted by the same ID variable.",
                "Notice", JOptionPane.INFORMATION_MESSAGE);

        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_includeStageTwoDataYesActionPerformed

    private void includeStageTwoDataNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_includeStageTwoDataNoActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "includeStageTwoDataNoActionPerformed");
        this.includeStageTwoDataNo.setSelected(true);
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_includeStageTwoDataNoActionPerformed

    private void fileBrowseButtonStageTwoDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileBrowseButtonStageTwoDataActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "fileBrowseButtonStageTwoDataActionPerformed : {0}", filePath_stageTwo.getText());
        importDataSetStageTwo();
        MXRStates = new MixRegGuiStates(this, advancedOptions_view);
        updateGuiView(MXRStates);
    }//GEN-LAST:event_fileBrowseButtonStageTwoDataActionPerformed

    private void filePath_stageTwoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filePath_stageTwoActionPerformed

    }//GEN-LAST:event_filePath_stageTwoActionPerformed

    private void IDStageTwoVariableComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_IDStageTwoVariableComboItemStateChanged

    }//GEN-LAST:event_IDStageTwoVariableComboItemStateChanged

    private void IDStageTwoVariableComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDStageTwoVariableComboActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "IDStageTwoVariableComboActionPerformed");
        IDposStageTwo = IDStageTwoVariableCombo.getSelectedIndex();
    }//GEN-LAST:event_IDStageTwoVariableComboActionPerformed

    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void online_support_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_online_support_buttonActionPerformed
        openWebpage("https://github.com/reach-lab/MixWildGUI/discussions");
    }//GEN-LAST:event_online_support_buttonActionPerformed

    public void openTextFileInEditor(File file) throws IOException {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            String cmd = "rundll32 url.dll,FileProtocolHandler " + file.getCanonicalPath();
            Runtime.getRuntime().exec(cmd);
        } else {
            Desktop.getDesktop().edit(file);
        }
    }


    private void openStage1OutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openStage1OutButtonActionPerformed
        String fileName = mixregGUI.defFile.getSharedDataFilename();
        String outputFilePath = FilenameUtils.removeExtension(fileName) + "_Output_stage1.out";
        File outputfile = new File(outputFilePath);
        try {
            openTextFileInEditor(outputfile);
        } catch (IOException ex) {
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_openStage1OutButtonActionPerformed

    private void openStage2OutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openStage2OutButtonActionPerformed
        String fileName = mixregGUI.defFile.getSharedDataFilename();
        String outputFilePath = FilenameUtils.removeExtension(fileName) + "_Output_stage2.out";
        File outputfile = new File(outputFilePath);
        try {
            openTextFileInEditor(outputfile);
        } catch (IOException ex) {
            Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_openStage2OutButtonActionPerformed

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        int helpTabIdx = stageOneTabs.indexOfTab("Help");
        stageOneTabs.setSelectedIndex(helpTabIdx);
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        int helpTabIdx = stageOneTabs.indexOfTab("Help");
        stageOneTabs.setSelectedIndex(helpTabIdx);
    }//GEN-LAST:event_jLabel9MouseClicked

    // **********************update********************
    private void updateGuiView(MixRegGuiStates mxrStates) {
        // this method is to update Gui View with Gui state data saved in MixRegGuiStates
        // Three different kinds of methods are defined:
        //     1. verify: examine necessary verification 
        //     2. all basic states: update all basic states that may or may not trigger other state change        
        //     3. trigger: update states that are triggered by method Two (e.g. visible and enabled states)       

        // Verify: Data file path exists or not
        boolean FilepathValid, FilepathValid_stageTwo;
        FilepathValid = updateGuiView_verify_FilePath(mxrStates);
        FilepathValid_stageTwo = updateGuiView_verify_FilePath_stageTwo(mxrStates);

        if (FilepathValid && FilepathValid_stageTwo) {
            // update browse triggered fields
            updateGuiView_trigger_browse(true);
            // update missing value trigger fields
            if (mxrStates.missingValuePresent) {
                missingValuePresent.setSelected(true);
                newModelMissingValueCode.setText(mxrStates.newModelMissingValueCode);
            } else if (mxrStates.missingValueAbsent) {
                missingValueAbsent.setSelected(true);
            }
            updateGuiView_trigger_missingvaluefield();
            // update Data View tab
            updateGuiView_trigger_dataview();
            // Update basic GUI States: Model Configuration Tab
            updateGuiView_TabOneStates(mxrStates);
            // Trigger: stage 1 outcome
            updateGuiView_trigger_stageOneOutcome();
            // Trigger: stage 1 config completed
            updateGuiView_trigger_stageOneConfig();
            // Trigger: Include Stage 2 or not
            updateGuiView_trigger_IncludeStageTwo();
            // Trigger: Include Stage 2 Data file or not
            updateGuiView_trigger_IncludeStageTwoData();
            // Triger: Click to modify stage 2 config or not
            updateGuiView_trigger_updateStage2Config();
            // Trigger: New model submitted or not
            isNewModalConfigSubmitted = mxrStates.isNewModalConfigSubmitted;
            if (isNewModalConfigSubmitted == true) {
                updateGuiView_trigger_NewModelSubmit();
            }
        } else {
            if (checkTabExistinJTabbedPane(stageOneTabs, "View Data")) {
                stageOneTabs.remove(jPanel6);
            }
            return;
        }

        if (isNewModalConfigSubmitted == true) {
            if (!isUpdateStage2ConfigClicked) {
                update_StageOneStates(mxrStates);
            }
            update_StageTwoStates(mxrStates);
        }

        //hide load button temprarily
        guiStatesLoadButtonModalConfig.setVisible(
                false);

    }

    private boolean updateGuiView_verify_FilePath(MixRegGuiStates mxrStates) {
        dataFileNameRef = mxrStates.filepath;
        filePath.setText(mxrStates.filepath);
        file = mxrStates.file;
//        file = new File(mxrStates.filepath);

        if (file.exists()) {
//            setFirstTabStatus(true);
            if (!checkTabExistinJTabbedPane(stageOneTabs, "View Data") && !checkTabExistinJTabbedPane(stageOneTabs, "View Stage 1 Data")) {
                int helpTabIdx = stageOneTabs.indexOfTab("Help");
                stageOneTabs.insertTab("View Data", null, jPanel6, null, helpTabIdx);
            }
            return true;
        } else {
            setFirstTabStatus(false);
//            stageOneTabs.setEnabledAt(6, false);
            JOptionPane.showMessageDialog(null, "To load the configuration, please place the originial dataset using this path:"
                    + "\r\n" + filePath.getText() + "\r\n\r\n" + "OR" + "\r\n\r\n" + "To start a new analysis, please choose a new dataset",
                    "Error: Wrong File Path", JOptionPane.INFORMATION_MESSAGE, icon);
            return false;
        }
    }

    private boolean updateGuiView_verify_FilePath_stageTwo(MixRegGuiStates mxrStates) {
        if (mxrStates.includeStageTwoDataYes) {
            includeStageTwoDataYes.setSelected(true);

            dataFileNameRef_stageTwo = mxrStates.filepath_stageTwo;
            filePath_stageTwo.setText(mxrStates.filepath_stageTwo);
            file_stageTwo = mxrStates.file_stageTwo;

//        file = new File(mxrStates.filepath);
            if (dataFileNameRef_stageTwo.isEmpty()) {
                return true;
            } else {
                if (file_stageTwo.exists()) {

                    if (!checkTabExistinJTabbedPane(stageOneTabs, "View Stage 2 Data")) {
                        int viewDataStageOneIdx = stageOneTabs.indexOfTab("View Data");
                        stageOneTabs.setTitleAt(viewDataStageOneIdx, "View Stage 1 Data");
                        int helpTabIdx = stageOneTabs.indexOfTab("Help");
                        stageOneTabs.insertTab("View Stage 2 Data", null, jPanel16, null, helpTabIdx);

                    }
                    // update Stage 2 Data View tab
                    updateGuiView_trigger_dataview_stageTwo();
                    return true;
                } else {

                    JOptionPane.showMessageDialog(null, "To load the configuration, please place the stage 2 dataset using this path:"
                            + "\r\n" + filePath_stageTwo.getText() + "\r\n\r\n" + "OR" + "\r\n\r\n" + "To start a new analysis, please choose a new dataset",
                            "Error: Wrong File Path", JOptionPane.INFORMATION_MESSAGE, icon);
                    return false;
                }

            }

        } else if (mxrStates.includeStageTwoDataNo) {
            includeStageTwoDataNo.setSelected(true);
            return true;
        } else {
            return true;
        }

    }

    private void updateGuiView_TabOneStates(MixRegGuiStates mxrStates) {
        titleField.setText(mxrStates.title);
        sessionFolderName = mxrStates.sessionFolderName;

        if (mxrStates.stageOneContinuousRadio) {
            stageOneContinuousRadio.setSelected(true);
        } else if (mxrStates.stageOneDichotomousRadio) {
            stageOneDichotomousRadio.setSelected(true);
        } else if (mxrStates.stageOneOrdinalRadio) {
            stageOneOrdinalRadio.setSelected(true);
        }

        if (mxrStates.stageOneProbitRadio) {
            StageOneProbitRadio.setSelected(true);
        } else if (mxrStates.stageOneLogisticRadio) {
            StageOneLogisticRadio.setSelected(true);
        }

        if (mxrStates.oneRLERadio) {
            oneRLERadio.setSelected(true);
        } else if (mxrStates.moreThanOneRLERadio) {
            moreThanOneRLERadio.setSelected(true);
        }

        if (mxrStates.randomScaleSelectionYes) {
            randomScaleSelectionYes.setSelected(true);
        } else if (mxrStates.randomScaleSelectionNo) {
            randomScaleSelectionNo.setSelected(true);
        }

        if (mxrStates.includeStageTwoYes) {
            includeStageTwoYes.setSelected(true);
        } else if (mxrStates.includeStageTwoNo) {
            includeStageTwoNo.setSelected(true);
        }

        if (mxrStates.includeStageTwoDataYes) {
            includeStageTwoDataYes.setSelected(true);
        } else if (mxrStates.includeStageTwoDataNo) {
            includeStageTwoDataNo.setSelected(true);
        }

        if (mxrStates.stageTwoSingleLevel) {
            stageTwoSingleLevel.setSelected(true);
        } else if (mxrStates.stageTwoMultiLevel) {
            stageTwoMultiLevel.setSelected(true);
        }

        if (mxrStates.stageTwoContinuousRadio) {
            stageTwoContinuousRadio.setSelected(true);
        } else if (mxrStates.stageTwoDichotomousRadio) {
            stageTwoDichotomousRadio.setSelected(true);
        } else if (mxrStates.stageTwoCountRadio) {
            stageTwoCountRadio.setSelected(true);
        } else if (mxrStates.stageTwoMultinomialRadio) {
            stageTwoMultinomialRadio.setSelected(true);
        }

        seedTextBox.setText(mxrStates.seedTextBox);
    }

    private void updateGuiView_trigger_MissingValue() {
        if (missingValueAbsent.isSelected()) {
            newModelMissingValueCode.setEnabled(false);
            newModelMissingValueCode.setText("");
        }
    }

    private void updateGuiView_trigger_IncludeStageTwo() {
        if (includeStageTwoNo.isSelected()) {
            setSeedLabel.setVisible(false);
            seedTextBox.setVisible(false);
            seedHelpButton.setVisible(false);
            seedTextBox.setText("0");
            stageTwoSingleLevel.setVisible(false);
            stageTwoMultiLevel.setVisible(false);
            includeStageTwoDataLabel.setVisible(false);
            includeStageTwoDataYes.setVisible(false);
            includeStageTwoDataNo.setVisible(false);
            DataFileStageTwoLabel.setVisible(false);
            filePath_stageTwo.setVisible(false);
            fileBrowseButtonStageTwoData.setVisible(false);
            stageTwoContinuousRadio.setVisible(false);
            stageTwoDichotomousRadio.setVisible(false);
            stageTwoCountRadio.setVisible(false);
            stageTwoMultinomialRadio.setVisible(false);
            stageTwoModelTypeLabel.setVisible(false);
            stageTwoOutcomeTypeLabel.setVisible(false);
            stageTwoModelGiantLabel.setVisible(false);
            stageTwoModelTypeHelpButton.setVisible(false);
            stageTwoOutcomeTypeHelpButton.setVisible(false);

            newModelSubmit.setVisible(true);
            newModelSubmit.setEnabled(true);

            includeStageTwoDataNo.setSelected(true);

        } else if (includeStageTwoYes.isSelected()) {
            setSeedLabel.setVisible(true);
            seedTextBox.setVisible(true);
            seedHelpButton.setVisible(true);
//            String seedVal = generateSeed();
//            seedTextBox.setText(seedVal);
            seedTextBox.setEnabled(true);
            stageTwoSingleLevel.setVisible(true);
            stageTwoSingleLevel.setEnabled(true);
            stageTwoMultiLevel.setVisible(true);
            stageTwoMultiLevel.setEnabled(true);
            includeStageTwoDataLabel.setVisible(true);
            includeStageTwoDataLabel.setEnabled(true);
            includeStageTwoDataYes.setVisible(true);
            includeStageTwoDataYes.setEnabled(true);
            includeStageTwoDataNo.setVisible(true);
            includeStageTwoDataNo.setEnabled(true);
            DataFileStageTwoLabel.setVisible(true);
            DataFileStageTwoLabel.setEnabled(true);
            filePath_stageTwo.setVisible(true);
            filePath_stageTwo.setEnabled(false);
            fileBrowseButtonStageTwoData.setVisible(true);
            fileBrowseButtonStageTwoData.setEnabled(false);
            stageTwoContinuousRadio.setVisible(true);
            stageTwoContinuousRadio.setEnabled(true);
            stageTwoDichotomousRadio.setVisible(true);
            stageTwoDichotomousRadio.setEnabled(true);
            stageTwoCountRadio.setVisible(true);
            stageTwoCountRadio.setEnabled(true);
            stageTwoMultinomialRadio.setVisible(true);
            stageTwoMultinomialRadio.setEnabled(true);
            stageTwoModelTypeLabel.setVisible(true);
            stageTwoOutcomeTypeLabel.setVisible(true);
            stageTwoModelGiantLabel.setVisible(true);
            stageTwoModelTypeHelpButton.setVisible(true);
            stageTwoOutcomeTypeHelpButton.setVisible(true);

            // continue button shows when all button groups have been selected
            if ((stageTwoLevelGroup.getSelection() != null) && (buttonGroup3.getSelection() != null) && (stageTwoDataButtonGroup.getSelection() != null)) {
                // continue button won't show when select Yes for stage 2 data but haven't import it.
                if ((includeStageTwoDataYes.isSelected() == false) || (!dataFileNameRef_stageTwo.isEmpty())) {
                    newModelSubmit.setVisible(true);
                    newModelSubmit.setEnabled(true);
                } else {
                    if (!isUpdateStage2ConfigClicked) {
                        JOptionPane.showMessageDialog(null, "Don't forget to import stage 2 data.",
                                "Data missing", JOptionPane.INFORMATION_MESSAGE);
                    }
                    newModelSubmit.setVisible(false);
                    newModelSubmit.setEnabled(false);
                }
            } else {
//                newModelSubmit.setVisible(false);
                newModelSubmit.setEnabled(false);
            }

//            updateStage2ConfigButton.setVisible(true);
        }
    }

    private void update_StageOneStates(MixRegGuiStates mxrStates) {
        IDpos = mxrStates.IDpos;
        IDvariableCombo.setSelectedIndex(IDpos);

        stageOnePos = mxrStates.stageOnePos;
        StageOneOutcomeCombo.setSelectedIndex(stageOnePos);

        stageTwoPos = mxrStates.stageTwoPos;
        StageTwoOutcomeCombo.setSelectedIndex(stageTwoPos);

        stageOneClicked = mxrStates.stageOneClicked;
        addStageOneCHecked = mxrStates.addStageOneCHecked;
        stageOneRegs.varList = mxrStates.varList;
        stageOneRegs.levelOneList = mxrStates.levelOneList;
        stageOneRegs.levelTwoList = mxrStates.levelTwoList;
        update_trigger_StageOneRegConfig();
        stage_1_regs.getAllVariablesList().removeAll();
        stage_1_regs.getAllVariablesList().setModel(mxrStates.varList);
        stage_1_regs.getAllVariablesList().setSelectedIndex(0);
        stage_1_regs.getStageOneLevelOneList().removeAll();
        stage_1_regs.getStageOneLevelOneList().setModel(mxrStates.levelOneList);
        stage_1_regs.getStageOneLevelTwoList().removeAll();
        stage_1_regs.getStageOneLevelTwoList().setModel(mxrStates.levelTwoList);

        stageOneRegs.isSubmitClicked = mxrStates.isStageOneRegSubmitClicked;
        if (stageOneRegs.isSubmitClicked == true) {
            stage_1_regs.getEnabledStageOneSubmitButton(true);
            //update boxes

//            levelOneBoxes = mxrStates.levelOneBoxes;
//            disaggVarianceBoxes = mxrStates.disaggVarianceBoxes;
//            levelTwoBoxes = mxrStates.levelTwoBoxes;
            if (mxrStates.levelOneList.getSize() > 0) {
                update_StageOneLevelOneBoxes(stageOneRegs.levelOneList, mxrStates.StageOneLevelOneBoxesSelection, mxrStates.disaggVarianceBoxesSelection);
            }
            if (mxrStates.levelTwoList.getSize() > 0) {
                update_StageOneLevelTwoBoxes(stageOneRegs.levelTwoList, mxrStates.StageOneLevelTwoBoxesSelection);
            }
        }

        //update advanced options
        advancedOptions_view.setMeanSubmodelCheckBox(mxrStates.meanSubmodelCheckBox);
        advancedOptions_view.setBSVarianceCheckBox(mxrStates.BSVarianceCheckBox);
        advancedOptions_view.setWSVarianceCheckBox(mxrStates.WSVarianceCheckBox);
        advancedOptions_view.setCenterRegressorsCheckBox(mxrStates.centerRegressorsCheckBox);
        advancedOptions_view.setDiscardSubjectsCheckBox(mxrStates.discardSubjectsCheckBox);
        advancedOptions_view.setResampleCheckBox(mxrStates.resampleCheckBox);
        advancedOptions_view.setAdaptiveQuadritureCheckBox(mxrStates.adaptiveQuadritureCheckBox);
        advancedOptions_view.setRun32BitCheckBox(mxrStates.run32BitCheckBox);
        advancedOptions_view.setConvergenceCriteria(mxrStates.convergenceCriteria);
        advancedOptions_view.setQuadriturePoints(mxrStates.quadriturePoints);
        advancedOptions_view.setMaximumIterations(mxrStates.maximumIterations);
        advancedOptions_view.setRidgeSpinner(mxrStates.ridgeSpinner);
        advancedOptions_view.setResampleSpinner(mxrStates.resampleSpinner);
        advancedOptions_view.update_trigger_AdvancedOptionsSubmit();
        advancedOptions_view.update_trigger_resampleCheckBox();
        advancedOptions_view.update_trigger_run32BitCheckBox();
        advancedOptions_view.update_enableDisaggregate();
        NoAssociationRadio.setSelected(mxrStates.NoAssociationRadio);
        LinearAssociationRadio.setSelected(mxrStates.LinearAssociationRadio);
        QuadraticAssociationRadio.setSelected(mxrStates.QuadraticAssociationRadio);

        isStageOneSubmitted = mxrStates.isStageOneSubmitted;
        levelTwoSelected = mxrStates.levelTwoSelected;
        if (isStageOneSubmitted == true) {
            update_trigger_StartStageTwo();
        }

    }

// **********************update********************
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        SystemLogger.LOGGER.log(Level.INFO, "Program Start");
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Oyaha".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mixregGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mixregGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mixregGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mixregGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JLabel DataFileStageTwoLabel;
    private javax.swing.JLabel DatasetLabel;
    private javax.swing.JComboBox<String> IDStageTwoVariableCombo;
    private javax.swing.JComboBox<String> IDvariableCombo;
    private javax.swing.JRadioButton LinearAssociationRadio;
    private javax.swing.JRadioButton NoAssociationRadio;
    private javax.swing.JRadioButton QuadraticAssociationRadio;
    private javax.swing.JRadioButton StageOneLogisticRadio;
    private javax.swing.JLabel StageOneModelTypeLabel;
    private javax.swing.JComboBox<String> StageOneOutcomeCombo;
    private javax.swing.JRadioButton StageOneProbitRadio;
    private javax.swing.JComboBox<String> StageTwoOutcomeCombo;
    private javax.swing.JButton addStageOneButton;
    private javax.swing.JButton addStageTwoReg;
    private javax.swing.JButton advancedOptionsButton;
    private javax.swing.JLabel associationLabel;
    private javax.swing.JPanel associationPanel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JButton clearStageOneButton;
    private javax.swing.JButton clearStageTwoButton;
    private javax.swing.JLabel dataFileLabel;
    public static javax.swing.JTable dataTable;
    public static javax.swing.JTable dataTable_stageTwo;
    private javax.swing.JLabel datasetHelpButton;
    private javax.swing.JLabel datasetMissingValuesHelpButton;
    private javax.swing.JCheckBox enbaleInteractionCheckBox;
    private javax.swing.JTextArea equationArea;
    private javax.swing.JButton exampleDataDownload;
    private javax.swing.JButton fileBrowseButton;
    private javax.swing.JButton fileBrowseButtonStageTwoData;
    private javax.swing.JTextField filePath;
    private javax.swing.JTextField filePath_stageTwo;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton guiStatesLoadButtonModalConfig;
    private javax.swing.JButton guiStatesSaveButtonModalConfig;
    private javax.swing.JButton guiStatesSaveButtonStageOne;
    private javax.swing.JButton guiStatesSaveButtonStageTwo;
    private javax.swing.JLabel hiddenBigIconLabel;
    private javax.swing.JLabel includeStageTwoDataLabel;
    private javax.swing.JRadioButton includeStageTwoDataNo;
    private javax.swing.JRadioButton includeStageTwoDataYes;
    private javax.swing.ButtonGroup includeStageTwoGroup;
    private javax.swing.JLabel includeStageTwoLabel;
    private javax.swing.JRadioButton includeStageTwoNo;
    private javax.swing.JRadioButton includeStageTwoYes;
    private javax.swing.JButton jButton3;
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
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
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
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
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
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel level1_BSVar;
    private javax.swing.JLabel level1_MeanReg;
    private javax.swing.JLabel level1_WSVar;
    private javax.swing.JLabel level2_BSVar;
    private javax.swing.JLabel level2_MeanReg;
    private javax.swing.JLabel level2_WSVar;
    private javax.swing.JPanel levelOneGrid;
    private javax.swing.JPanel levelTwoGrid;
    private javax.swing.JButton loadModelByBrowseButton;
    private javax.swing.JLabel missingCodeViewLabel;
    private javax.swing.JRadioButton missingValueAbsent;
    private javax.swing.JRadioButton missingValuePresent;
    private javax.swing.JLabel missingViewLabel;
    private javax.swing.JRadioButton moreThanOneRLERadio;
    private javax.swing.JButton newDataSetButton;
    private javax.swing.JTextField newModelMissingValueCode;
    private javax.swing.JButton newModelSubmit;
    private javax.swing.JButton newModel_resetButton;
    private javax.swing.JLabel numResamplingStageTwoConfigLabel;
    private javax.swing.JLabel numResamplingStageTwoConfigLabel1;
    private javax.swing.JRadioButton oneRLERadio;
    private javax.swing.JButton online_support_button;
    private javax.swing.JLabel online_support_help_label;
    private javax.swing.JButton openStage1OutButton;
    private javax.swing.JTextPane outCategoryDisplay;
    private javax.swing.JButton outcomeCatButton;
    private javax.swing.JPanel parentPanel;
    public static javax.swing.JLabel printedFileName;
    public static javax.swing.JLabel printedFileName_stageTwo;
    public static javax.swing.JLabel randomLocationEffectsLabel;
    public static javax.swing.JLabel randomLocationEffectsLabel1;
    private javax.swing.ButtonGroup randomScaleSelectionGroup;
    private javax.swing.JRadioButton randomScaleSelectionNo;
    private javax.swing.JRadioButton randomScaleSelectionYes;
    private javax.swing.JLabel randomScaleViewLabel;
    private javax.swing.JLabel rleViewLabel;
    private javax.swing.JButton runTabTwoStageOneTwo;
    private javax.swing.JButton saveStage1OutButton;
    private javax.swing.JButton saveStage2OutButton;
    private javax.swing.JLabel seedHelpButton;
    private javax.swing.JTextField seedTextBox;
    private javax.swing.JLabel setSeedLabel;
    private javax.swing.JRadioButton stageOneContinuousRadio;
    private javax.swing.JRadioButton stageOneDichotomousRadio;
    private javax.swing.JPanel stageOneLevelOnePanel;
    private javax.swing.JPanel stageOneLevelTwoPanel;
    private javax.swing.JLabel stageOneModelGiantLabel;
    private javax.swing.JLabel stageOneModelStageTwoConfigLabel;
    private javax.swing.JLabel stageOneModelStageTwoConfigLabel1;
    private javax.swing.JRadioButton stageOneOrdinalRadio;
    private javax.swing.ButtonGroup stageOneOutcomeGroup;
    private javax.swing.JLabel stageOneOutcomeHelpButton;
    private javax.swing.JLabel stageOneOutcomeStageTwoConfigLabel;
    private javax.swing.JLabel stageOneOutcomeStageTwoConfigLabel1;
    private javax.swing.JLabel stageOneOutcomeViewLabel;
    public static javax.swing.JTextArea stageOneOutput;
    private javax.swing.JLabel stageOneRLEHelpButton;
    private javax.swing.JLabel stageOneRSHelpButton;
    private javax.swing.JTabbedPane stageOneTabs;
    private javax.swing.JRadioButton stageTwoContinuousRadio;
    private javax.swing.JRadioButton stageTwoCountRadio;
    private javax.swing.ButtonGroup stageTwoDataButtonGroup;
    private javax.swing.JLabel stageTwoDescription;
    private javax.swing.JRadioButton stageTwoDichotomousRadio;
    private javax.swing.ButtonGroup stageTwoLevelGroup;
    private javax.swing.JPanel stageTwoLevelOnePanel;
    private javax.swing.JPanel stageTwoLevelTwoPanel;
    private javax.swing.JLabel stageTwoModelGiantLabel;
    private javax.swing.JLabel stageTwoModelTypeHelpButton;
    private javax.swing.JLabel stageTwoModelTypeLabel;
    private javax.swing.JLabel stageTwoModelTypeStageTwoConfigLabel;
    private javax.swing.JLabel stageTwoModelTypeStageTwoConfigLabel1;
    private javax.swing.JRadioButton stageTwoMultiLevel;
    private javax.swing.JRadioButton stageTwoMultinomialRadio;
    public static javax.swing.JLabel stageTwoOutcomePrintLabel;
    public static javax.swing.JLabel stageTwoOutcomePrintLabel1;
    private javax.swing.JLabel stageTwoOutcomeStageTwoConfigLabel;
    private javax.swing.JLabel stageTwoOutcomeStageTwoConfigLabel1;
    private javax.swing.JLabel stageTwoOutcomeTypeHelpButton;
    private javax.swing.JLabel stageTwoOutcomeTypeLabel;
    public static javax.swing.JTextArea stageTwoOutput;
    private javax.swing.JPanel stageTwoRegsGridLvl1;
    private javax.swing.JPanel stageTwoRegsGridLvl2;
    private javax.swing.JRadioButton stageTwoSingleLevel;
    private javax.swing.JButton startStageTwo;
    private javax.swing.JTextField titleField;
    private javax.swing.JLabel titleViewLabel;
    private javax.swing.JButton updateStage2ConfigButton;
    private javax.swing.JButton userGuideDownload;
    // End of variables declaration//GEN-END:variables

    public void initiateStageOneTab() {
        IDvariableCombo.setSelectedIndex(0);
        StageOneOutcomeCombo.setSelectedIndex(1);
        StageTwoOutcomeCombo.setSelectedIndex(2);

        buttonGroup1.clearSelection();

        //addStageTwoTabTwo.setEnabled(false);
        stageOneLevelTwoPanel.removeAll();
        stageOneLevelTwoPanel.revalidate();
        stageOneLevelTwoPanel.repaint();

        stageOneLevelOnePanel.removeAll();
        stageOneLevelOnePanel.revalidate();
        stageOneLevelOnePanel.repaint();

        parentPanel.removeAll();
        parentPanel.add(stageOneTabs);
        parentPanel.repaint();
        parentPanel.revalidate();
    }

    //Updates IDs and outcome variables list
    public void initiateStageOneComboBoxes() {

        for (int j = 0; j < variableNamesCombo.length; j++) {
            IDList.addElement(variableNamesCombo[j]);
            StageOneList.addElement(variableNamesCombo[j]);
        }

        IDvariableCombo.setModel(IDList);
        IDvariableCombo.setSelectedIndex(0);

        StageOneOutcomeCombo.setModel(StageOneList);
        StageOneOutcomeCombo.setSelectedIndex(1);

    }

    public void initiateStageTwoComboBoxes() {
        if (getIncludeStageTwoYes() == true) {
            // 2nd dataset imported: Update stage 2 combo with 2nd dataset variables
            if (getIncludeStageTwoDataYes() == true) {
                IDList = new DefaultComboBoxModel<>();
                StageTwoList = new DefaultComboBoxModel<String>();

                Scanner inputStream;
                try {
                    // Read variable names from row 1
                    inputStream = new Scanner(file_stageTwo);
                    String variableNames = inputStream.next();
                    variableNamesCombo_stageTwo = variableNames.split(",");
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                }

                for (String variableNamesComboItem : variableNamesCombo_stageTwo) {
                    IDList.addElement(variableNamesComboItem);
                    StageTwoList.addElement(variableNamesComboItem);
                }

                IDStageTwoVariableCombo.setModel(IDList);
                IDStageTwoVariableCombo.setSelectedIndex(0);

                StageTwoOutcomeCombo.setModel(StageTwoList);
                StageTwoOutcomeCombo.setSelectedIndex(1);
            } else {
                // No 2nd dataset imported: Use variables from 1st dataset
                variableNamesCombo_stageTwo = variableNamesCombo;

                StageTwoList = new DefaultComboBoxModel<String>();
                for (String variableNamesComboItem : variableNamesCombo_stageTwo) {
                    StageTwoList.addElement(variableNamesComboItem);
                }
                StageTwoOutcomeCombo.setModel(StageTwoList);
                StageTwoOutcomeCombo.setSelectedIndex(2);
            }
        }
    }

    //Open a web link from the software
    public static void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
            e.printStackTrace();
        }
    }

//    //get a list of variables remaininag from stage 1 
//    public DefaultListModel<String> getSavedVariables() {
//
//        int index = StageTwoOutcomeCombo.getSelectedIndex();
//
//        //DefaultListModel<String> tempModel = stage_1_regs.getListModel();
//        DefaultListModel<String> tempModel = stage_1_regs.getListModel();
//
//        tempModel.removeElement(StageTwoOutcomeCombo.getSelectedItem());
//
//        savedVariablesStageOne = tempModel;
//
//        return savedVariablesStageOne;
//
//    }
    //get ID variable selected by the user
    public int getIDVariable() {
        // String ID;

        int pos = IDvariableCombo.getSelectedIndex();

        return pos;
    }

    //Get the position of ID variable in the data file
    public static int getIDFieldPosition(int stage) {
        switch (stage) {
            case 1:
                return IDpos;
            case 2:
                // no new stage 2 dataset imported
                if (IDposStageTwo == -1) {
                    return IDpos;
                } else {
                    return IDposStageTwo;
                }
            default:
                return -2;
        }

    }

    //get Stage One DV variable selected by the user
    public String getStageOneDV() {
        String StageOneDV;

        StageOneDV = StageOneOutcomeCombo.getItemAt(StageOneOutcomeCombo.getSelectedIndex());

        return StageOneDV;

    }

    //Get stage 1 dv position in data file
    public static int getStageOneDVFieldPosition() {

        return stageOnePos;
    }

//    //get Stage Two variable selected by the user
//    public String getStageTwoDV() {
//        String StageTwoDV;
//
//        StageTwoDV = StageTwoOutcomeCombo.getItemAt(StageTwoOutcomeCombo.getSelectedIndex());
//
//        return StageTwoDV;
//    }
    //Get stage 2 dv position in data file
    public static int getStageTwoDVFieldPosition() {

        return stageTwoPos;
    }

    public boolean getNoAssociationRadio() {
        return NoAssociationRadio.isSelected();
    }

    public boolean getLinearAssociationRadio() {
        return LinearAssociationRadio.isSelected();
    }

    public boolean getQuadraticAssociationRadio() {
        return QuadraticAssociationRadio.isSelected();
    }

    public boolean getEnableInteractionCheckBox() {
        return enbaleInteractionCheckBox.isSelected();
    }

    //Update level 1 table with regressors
    public void updateStageOneLevelOneGrid(DefaultListModel<String> defaultListModel) {

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

        constraints.insets = new Insets(3, 10, 5, 0);
        separatorConstraint.insets = new Insets(0, 0, 0, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        levelOneBoxes = new ArrayList<>();
        disaggVarianceBoxes = new ArrayList<>();

        for (int j = 0; j < regSize; j++) {
            constraints.gridx = 0;
            constraints.anchor = GridBagConstraints.LINE_END;
            levelOneSelected.add(defaultListModel.getElementAt(j));
            JLabel variableText = new JLabel(levelOneSelected.get(j));
//            variableText.setBorder(new LineBorder(Color.BLACK));
            variableText.setPreferredSize(new Dimension(80, 20));
            levelOneGrid.add(variableText, constraints);

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
                        try {
                            if (selected) {
                                System.out.println("Checkbox selected");
                                disaggVarianceBoxes.get(row).get(column).setEnabled(true);
                                disaggVarianceBoxes.get(row).get(column).setSelected(false);
//                            System.out.println(disaggVarianceBoxes.size());
                            } else {
                                disaggVarianceBoxes.get(row).get(column).setEnabled(false);
                                disaggVarianceBoxes.get(row).get(column).setSelected(false);
                            }
                        } catch (java.lang.IndexOutOfBoundsException indexE) {

                        }

                    }
                });

            }

            if (disaggregateEnabled == true) {
                constraints.gridy++;
                constraints.gridx = 0;
                constraints.anchor = GridBagConstraints.LINE_END;

                levelOneGrid.add(new JLabel("  - Disaggregate"), constraints);
                disaggVarianceBoxes.add(j, new ArrayList<JCheckBox>());

                for (int k = 0; k < 3; k++) {
                    constraints.gridx++;
                    constraints.anchor = GridBagConstraints.CENTER;

                    disaggVarianceBoxes.get(j).add(k, new JCheckBox());

                    levelOneGrid.add(disaggVarianceBoxes.get(j).get(k), constraints);
                    disaggVarianceBoxes.get(j).get(k).setEnabled(false);

                }
                separatorConstraint.gridy = separatorConstraint.gridy + 1;
            }

            constraints.gridy++;
            //constraints.gridx = 0;
            separatorConstraint.gridy = separatorConstraint.gridy + 2;
            //System.out.println("before seperator");
            levelOneGrid.add(new JSeparator(JSeparator.HORIZONTAL), separatorConstraint);
            //System.out.println("after seperator");
            constraints.gridy++;

        }

        stageOneLevelOnePanel.removeAll();
        stageOneLevelOnePanel.revalidate();
        stageOneLevelOnePanel.repaint();

        stageOneLevelOnePanel.add(scrollpanel);
        revalidate();

    }

    //Update level 2 table with regressors
    public void updateStageOneLevelTwoGrid(DefaultListModel<String> defaultListModel) {

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

        constraints.insets = new Insets(3, 10, 5, 0);
        separatorConstraint.insets = new Insets(0, 0, 0, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        levelTwoBoxes = new ArrayList<ArrayList<JCheckBox>>();
        //disaggVarianceBoxes = new ArrayList<ArrayList<JCheckBox>>();

        for (int j = 0; j < regSize; j++) {
            constraints.gridx = 0;
            constraints.anchor = GridBagConstraints.LINE_END;
            levelTwoSelected.add(defaultListModel.getElementAt(j));
            JLabel variableText = new JLabel(levelTwoSelected.get(j));
//            variableText.setBorder(new LineBorder(Color.BLACK));
            variableText.setPreferredSize(new Dimension(80, 20));
            levelTwoGrid.add(variableText, constraints);

            levelTwoBoxes.add(j, new ArrayList<JCheckBox>());

            for (int k = 0; k < 3; k++) {

                constraints.gridx++;
                constraints.anchor = GridBagConstraints.CENTER;
                levelTwoBoxes.get(j).add(k, new JCheckBox());
                levelTwoGrid.add(levelTwoBoxes.get(j).get(k), constraints);
            }

            if (RLE_selected == MixLibrary.STAGE_ONE_RLE_SLOPE) {
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

        stageOneLevelTwoPanel.removeAll();
        stageOneLevelTwoPanel.revalidate();
        stageOneLevelTwoPanel.repaint();

        stageOneLevelTwoPanel.add(scrollpanel);
        revalidate();

    }

    //Update stage 2 table with selected regressors
    public void updateStageTwoLevelTwoGrid(DefaultListModel<String> defaultListModel) {

        JScrollPane scrollpanel = new JScrollPane(stageTwoRegsGridLvl2);
        stageTwoLevelTwoSelected = new ArrayList<String>();

        int regSize = defaultListModel.getSize();
        stageTwoLevelTwoRegSize = regSize;

        stageTwoRegsGridLvl2.removeAll();

        if (suppressed) {
            jLabel37.setVisible(false);
        } else {
            jLabel37.setVisible(true);
        }

        stageTwoRegsGridLvl2.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.NORTHEAST;

        GridBagConstraints separatorConstraint = new GridBagConstraints();
        separatorConstraint.weightx = 1.0;
        separatorConstraint.fill = GridBagConstraints.HORIZONTAL;
        separatorConstraint.gridwidth = GridBagConstraints.REMAINDER;
        separatorConstraint.gridx = 0;

        constraints.insets = new Insets(3, 5, 5, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        separatorConstraint.insets = new Insets(0, 0, 0, 0);
        constraints.weightx = 1;

        stageTwoLevelTwoGridBoxes = new ArrayList<ArrayList<JCheckBox>>();
        //disaggVarianceBoxes = new ArrayList<ArrayList<JCheckBox>>();

        for (int j = 0; j < regSize; j++) {
            int row = j;
            constraints.gridx = 1;
            constraints.anchor = GridBagConstraints.FIRST_LINE_START;
            stageTwoLevelTwoSelected.add(defaultListModel.getElementAt(j));
            JLabel variableText = new JLabel(stageTwoLevelTwoSelected.get(j));
            variableText.setPreferredSize(new Dimension(60, 20));
            stageTwoRegsGridLvl2.add(variableText, constraints);

            stageTwoLevelTwoGridBoxes.add(j, new ArrayList<JCheckBox>());

            for (int k = 0; k < 4; k++) {

                if (k == 1) {
                    constraints.gridx = constraints.gridx + 5;
                } else {
                    constraints.gridx++;
                }

                constraints.anchor = GridBagConstraints.CENTER;

                if (k == 3) {
                    if (suppressed) {
                        JLabel placeHolder3 = new JLabel(" ");
                        placeHolder3.setPreferredSize(new Dimension(20, 20));
                        stageTwoRegsGridLvl2.add(placeHolder3, constraints);
                        stageTwoLevelTwoGridBoxes.get(j).add(k, new JCheckBox());
                        stageTwoLevelTwoGridBoxes.get(j).get(k).setEnabled(false);
                    } else {
                        stageTwoLevelTwoGridBoxes.get(j).add(k, new JCheckBox());
                        stageTwoLevelTwoGridBoxes.get(j).get(k).setEnabled(true);
                    }
                } else {
                    stageTwoLevelTwoGridBoxes.get(j).add(k, new JCheckBox());
                    if (suppressed) {
                        stageTwoLevelTwoGridBoxes.get(j).get(k).setEnabled(false);
                    } else {
                        stageTwoLevelTwoGridBoxes.get(j).get(k).setEnabled(true);
                    }
                }

                if (k == 0) {
                    stageTwoLevelTwoGridBoxes.get(j).get(k).setSelected(true);
                    stageTwoLevelTwoGridBoxes.get(j).get(k).setVisible(false);
                    constraints.gridx++;

                    if (isRandomScale) {
                        JLabel placeHolder1 = new JLabel(" ");
                        placeHolder1.setPreferredSize(new Dimension(60, 20));
                        stageTwoRegsGridLvl2.add(placeHolder1, constraints);
                    }
                }

                if (k == 3) {
                    if (suppressed) {
                        // do nothing
                    } else {
                        stageTwoRegsGridLvl2.add(stageTwoLevelTwoGridBoxes.get(j).get(k), constraints);
                    }
                } else {
                    stageTwoRegsGridLvl2.add(stageTwoLevelTwoGridBoxes.get(j).get(k), constraints);
                }

            }

            constraints.gridy++;

            separatorConstraint.gridy = separatorConstraint.gridy + 2;

            stageTwoRegsGridLvl2.add(new JSeparator(JSeparator.HORIZONTAL), separatorConstraint);
            constraints.gridy++;

            stageTwoLevelTwoGridBoxes.get(row).get(1).setEnabled(true);
            stageTwoLevelTwoGridBoxes.get(row).get(2).setEnabled(true);

            if (!isRandomScale) {
                stageTwoLevelTwoGridBoxes.get(row).get(2).setVisible(false);
            }

//            stageTwoLevelTwoGridBoxes.get(j).get(0).addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                    AbstractButton abstractButton = (AbstractButton) e.getSource();
//                    boolean selected = abstractButton.getModel().isSelected();
//                    if (selected) {
//                        System.out.println("Checkbox selected");
//                        //disaggVarianceBoxes.get(row).get(column).setEnabled(true);
//                        stageTwoLevelTwoGridBoxes.get(row).get(1).setEnabled(true);
//                        stageTwoLevelTwoGridBoxes.get(row).get(1).setSelected(false);
//                        stageTwoLevelTwoGridBoxes.get(row).get(2).setEnabled(true);
//                        stageTwoLevelTwoGridBoxes.get(row).get(2).setSelected(false);
//                        randomChecked = false;
//                        scaleChecked = false;
////                        System.out.println(disaggVarianceBoxes.size());
//                    } else {
//                        //disaggVarianceBoxes.get(row).get(column).setEnabled(false);
//                        stageTwoLevelTwoGridBoxes.get(row).get(1).setEnabled(false);
//                        stageTwoLevelTwoGridBoxes.get(row).get(1).setSelected(false);
//                        stageTwoLevelTwoGridBoxes.get(row).get(2).setEnabled(false);
//                        stageTwoLevelTwoGridBoxes.get(row).get(2).setSelected(false);
//                        stageTwoLevelTwoGridBoxes.get(row).get(3).setSelected(false);
//                        stageTwoLevelTwoGridBoxes.get(row).get(3).setEnabled(false);
//                        randomChecked = false;
//                        scaleChecked = false;
////                        suppressIntCheckBox.setEnabled(false);
////                        suppressIntCheckBox.setSelected(false);
//
//                    }
//
//                }
//            });
            stageTwoLevelTwoGridBoxes.get(j).get(1).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();
                    if (selected) {
                        scaleChecked = true;
                        if (randomChecked == true) {
                            if (!suppressed) {
                                stageTwoLevelTwoGridBoxes.get(row).get(3).setEnabled(true);
                                stageTwoLevelTwoGridBoxes.get(row).get(3).setSelected(false);
                            }
                        }

                    } else {
                        scaleChecked = false;
                        if (!suppressed) {
                            stageTwoLevelTwoGridBoxes.get(row).get(3).setEnabled(false);
                            stageTwoLevelTwoGridBoxes.get(row).get(3).setSelected(false);
                        }
                    }
                }
            });

            stageTwoLevelTwoGridBoxes.get(j).get(2).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();

                    if (selected) {
                        randomChecked = true;

                        if (scaleChecked == true) {
                            if (!suppressed) {
                                stageTwoLevelTwoGridBoxes.get(row).get(3).setEnabled(true);
                                stageTwoLevelTwoGridBoxes.get(row).get(3).setSelected(false);
                            }
                        }

                    } else {
                        randomChecked = false;
                        if (!suppressed) {
                            stageTwoLevelTwoGridBoxes.get(row).get(3).setEnabled(false);
                            stageTwoLevelTwoGridBoxes.get(row).get(3).setSelected(false);
                        }

                    }
                }
            });

            stageTwoLevelTwoGridBoxes.get(j).get(3).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();

                    randomChecked = false;
                    scaleChecked = false;

                    enbaleInteractionCheckBox.setEnabled(true);

                }
            });

        }

        stageTwoLevelTwoPanel.removeAll();
        stageTwoLevelTwoPanel.add(scrollpanel);

        stageTwoLevelTwoPanel.revalidate();
        stageTwoLevelTwoPanel.repaint();

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
        if (disaggregateEnabled == true) {
            for (int p = 0; p < levelOneRegSize; p++) {

                if (disaggVarianceBoxes.get(p).get(0).isSelected()) {
                    levelOneDisagg = levelOneDisagg + 1;
                }

            }
        }

        return levelOneDisagg;
    }

    public int countLevelOneDicompBS() {
        int levelOneDisagg = 0;
        if (disaggregateEnabled == true) {
            for (int p = 0; p < levelOneRegSize; p++) {

                if (disaggVarianceBoxes.get(p).get(1).isSelected()) {
                    levelOneDisagg = levelOneDisagg + 1;
                }

            }
        }

        return levelOneDisagg;
    }

    public int countLevelOneDicompWS() {
        int levelOneDisagg = 0;
        if (disaggregateEnabled == true) {
            for (int p = 0; p < levelOneRegSize; p++) {

                if (disaggVarianceBoxes.get(p).get(2).isSelected()) {
                    levelOneDisagg = levelOneDisagg + 1;
                }

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

        for (int p = 0; p < stageTwoLevelOneRegSize; p++) {

            if (stageTwoLevelOneGridBoxes.get(p).get(0).isSelected()) {

                stageTwoBeta = stageTwoBeta + 1;
            }
        }

        for (int p = 0; p < stageTwoLevelTwoRegSize; p++) {

            if (stageTwoLevelTwoGridBoxes.get(p).get(0).isSelected()) {

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

        for (int p = 0; p < stageTwoLevelOneRegSize; p++) {

            if (stageTwoLevelOneGridBoxes.get(p).get(1).isSelected()) {

                stageTwoAlpha = stageTwoAlpha + 1;
            }
        }

        for (int p = 0; p < stageTwoLevelTwoRegSize; p++) {

            if (stageTwoLevelTwoGridBoxes.get(p).get(1).isSelected()) {

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

        for (int p = 0; p < stageTwoLevelOneRegSize; p++) {

            if (stageTwoLevelOneGridBoxes.get(p).get(2).isSelected()) {

                stageTwoTau = stageTwoTau + 1;
            }
        }

        for (int p = 0; p < stageTwoLevelTwoRegSize; p++) {

            if (stageTwoLevelTwoGridBoxes.get(p).get(2).isSelected()) {

                stageTwoTau = stageTwoTau + 1;
            }
        }

        return stageTwoTau;

    }

    public int countStageTwoInteractions() {

        int stageTwoInter = 0;

        for (int p = 0; p < stageTwoLevelOneRegSize; p++) {

            if (stageTwoLevelOneGridBoxes.get(p).get(3).isSelected()) {

                stageTwoInter = stageTwoInter + 1;
            }
        }

        for (int p = 0; p < stageTwoLevelTwoRegSize; p++) {

            if (stageTwoLevelTwoGridBoxes.get(p).get(3).isSelected()) {

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
        boolean disaggVarianceBoxesSelected;
        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < levelOneRegSize; p++) {
            disaggVarianceBoxesSelected = false;
            if (disaggregateEnabled == true) {
                disaggVarianceBoxesSelected = disaggVarianceBoxes.get(p).get(0).isSelected();
            }
            if (levelOneBoxes.get(p).get(0).isSelected() && !disaggVarianceBoxesSelected) {
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
//        variableNamesCombo_stageTwo = new String[levelTwoRegSize];
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
        boolean disaggVarianceBoxesSelected;

        for (int p = 0; p < levelOneRegSize; p++) {
            disaggVarianceBoxesSelected = false;
            if (disaggregateEnabled == true) {
                disaggVarianceBoxesSelected = disaggVarianceBoxes.get(p).get(0).isSelected();
            }
            if (levelOneBoxes.get(p).get(0).isSelected() && disaggVarianceBoxesSelected) {
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
        boolean disaggVarianceBoxesSelected;
        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < levelOneRegSize; p++) {
            disaggVarianceBoxesSelected = false;
            if (disaggregateEnabled == true) {
                disaggVarianceBoxesSelected = disaggVarianceBoxes.get(p).get(1).isSelected();
            }
            if (levelOneBoxes.get(p).get(1).isSelected() && !disaggVarianceBoxesSelected) {
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
        boolean disaggVarianceBoxesSelected;

        for (int p = 0; p < levelOneRegSize; p++) {
            disaggVarianceBoxesSelected = false;
            if (disaggregateEnabled == true) {
                disaggVarianceBoxesSelected = disaggVarianceBoxes.get(p).get(1).isSelected();
            }
            if (levelOneBoxes.get(p).get(1).isSelected() && disaggVarianceBoxesSelected) {
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
        boolean disaggVarianceBoxesSelected;

        for (int p = 0; p < levelOneRegSize; p++) {
            disaggVarianceBoxesSelected = false;
            if (disaggregateEnabled == true) {
                disaggVarianceBoxesSelected = disaggVarianceBoxes.get(p).get(2).isSelected();
            }

            if (levelOneBoxes.get(p).get(2).isSelected() && !disaggVarianceBoxesSelected) {
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
        boolean disaggVarianceBoxesSelected;
        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < levelOneRegSize; p++) {
            disaggVarianceBoxesSelected = false;
            if (disaggregateEnabled == true) {
                disaggVarianceBoxesSelected = disaggVarianceBoxes.get(p).get(2).isSelected();
            }
            if (levelOneBoxes.get(p).get(2).isSelected() && disaggVarianceBoxesSelected) {
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
        boolean disaggVarianceBoxesSelected;

        for (int p = 0; p < levelOneRegSize; p++) {
            disaggVarianceBoxesSelected = false;
            if (disaggregateEnabled == true) {
                disaggVarianceBoxesSelected = disaggVarianceBoxes.get(p).get(0).isSelected();
            }
            if (levelOneBoxes.get(p).get(0).isSelected() && !disaggVarianceBoxesSelected) {

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
        boolean disaggVarianceBoxesSelected;
        for (int p = 0; p < levelOneRegSize; p++) {
            disaggVarianceBoxesSelected = false;
            if (disaggregateEnabled == true) {
                disaggVarianceBoxesSelected = disaggVarianceBoxes.get(p).get(1).isSelected();
            }
            if (levelOneBoxes.get(p).get(1).isSelected() && !disaggVarianceBoxesSelected) {
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
        boolean disaggVarianceBoxesSelected;
        for (int p = 0; p < levelOneRegSize; p++) {
            disaggVarianceBoxesSelected = false;
            if (disaggregateEnabled == true) {
                disaggVarianceBoxesSelected = disaggVarianceBoxes.get(p).get(2).isSelected();
            }
            if (levelOneBoxes.get(p).get(2).isSelected() && !disaggVarianceBoxesSelected) {
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
        boolean disaggVarianceBoxesSelected;

        for (int p = 0; p < levelOneRegSize; p++) {
            disaggVarianceBoxesSelected = false;
            if (disaggregateEnabled == true) {
                disaggVarianceBoxesSelected = disaggVarianceBoxes.get(p).get(0).isSelected();
            }

            if (levelOneBoxes.get(p).get(0).isSelected() && disaggVarianceBoxesSelected) {

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
        boolean disaggVarianceBoxesSelected;

        for (int p = 0; p < levelOneRegSize; p++) {
            disaggVarianceBoxesSelected = false;
            if (disaggregateEnabled == true) {
                disaggVarianceBoxesSelected = disaggVarianceBoxes.get(p).get(1).isSelected();
            }
            if (levelOneBoxes.get(p).get(1).isSelected() && disaggVarianceBoxesSelected) {

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
        boolean disaggVarianceBoxesSelected;

        for (int p = 0; p < levelOneRegSize; p++) {
            disaggVarianceBoxesSelected = false;
            if (disaggregateEnabled == true) {
                disaggVarianceBoxesSelected = disaggVarianceBoxes.get(p).get(2).isSelected();
            }
            if (levelOneBoxes.get(p).get(2).isSelected() && disaggVarianceBoxesSelected) {

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

        outcome = StageOneOutcomeCombo.getSelectedItem().toString();
        System.out.println("Stage-Two/MixRegGUI/Outcome-variable-Label: " + outcome);

        return outcome;

    }

    public int getStageOneOutcome() {
        int stageOneOutcome = MixLibrary.STAGE_ONE_OUTCOME_MIXREG;

        if (getStageOneContinuousRadio()) {
            stageOneOutcome = MixLibrary.STAGE_ONE_OUTCOME_MIXREG;
        } else if (getStageOneDichotomousRadio() || getStageOneOrdinalRadio()) {
            stageOneOutcome = MixLibrary.STAGE_ONE_OUTCOME_MIXOR;
        }

        return stageOneOutcome;
    }

    public String getStageOneRegressionType() {
        String stageOneRegressonType = "1";

        if (getStageOneProbit()) {
            stageOneRegressonType = "0";
        } else if (getStageOneLogistic()) {
            stageOneRegressonType = "1";
        }

        return stageOneRegressonType;
    }

    public String getStageTwoOutcomePosition() {
        String position;
        int pos;

        String outcome = StageTwoOutcomeCombo.getSelectedItem().toString();
        pos = StageTwoOutcomeCombo.getSelectedIndex();

        position = String.valueOf(pos + 1);

        return position;

    }

    public String getStageTwoOutcomeLabel() {
        String position;
        int pos;

        String outcome = StageTwoOutcomeCombo.getSelectedItem().toString();
        pos = StageTwoOutcomeCombo.getSelectedIndex();

        position = String.valueOf(pos + 1);

        return outcome;

    }

    public String[] getFixedFieldRegressors_StageTwo() {
        System.out.println("*********************************");
        System.out.println("Fixed From Stage Two (positions)");

        String fieldLabel;

        String[] regressorLabels = new String[stageTwoLevelOneRegSize + stageTwoLevelTwoRegSize];
        int index = 0;

        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < stageTwoLevelOneRegSize; p++) {

            if (stageTwoLevelOneGridBoxes.get(p).get(0).isSelected()) {
                regressorLabels[index] = stageTwoLevelOneSelected.get(p);
                fieldLabel = stageTwoLevelOneSelected.get(p);
                System.out.println("Stage-Two/mixRegGUI/Level 1 Regressor-Fields-(Fixed): " + regressorLabels[index]);
                index++;

                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo_stageTwo.length; q++) {

                    if (variableNamesCombo_stageTwo[q].equals(fieldLabel)) {
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

        for (int p = 0; p < stageTwoLevelTwoRegSize; p++) {

            if (stageTwoLevelTwoGridBoxes.get(p).get(0).isSelected()) {
                regressorLabels[index] = stageTwoLevelTwoSelected.get(p);
                fieldLabel = stageTwoLevelTwoSelected.get(p);
                System.out.println("Stage-Two/mixRegGUI/Level 2 Regressor-Fields-(Fixed): " + regressorLabels[index]);
                index++;

                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo_stageTwo.length; q++) {

                    if (variableNamesCombo_stageTwo[q].equals(fieldLabel)) {
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

        String[] regressorLabels = new String[stageTwoLevelOneRegSize + stageTwoLevelTwoRegSize];
        int index = 0;

        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < stageTwoLevelOneRegSize; p++) {

            if (stageTwoLevelOneGridBoxes.get(p).get(1).isSelected()) {
                regressorLabels[index] = stageTwoLevelOneSelected.get(p);
                fieldLabel = stageTwoLevelOneSelected.get(p);
                System.out.println("Stage-Two/mixRegGUI/Level 1 Regressor-Fields-(LocRan): " + regressorLabels[index]);
                index++;

                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo_stageTwo.length; q++) {

                    if (variableNamesCombo_stageTwo[q].equals(fieldLabel)) {
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

        for (int p = 0; p < stageTwoLevelTwoRegSize; p++) {

            if (stageTwoLevelTwoGridBoxes.get(p).get(1).isSelected()) {
                regressorLabels[index] = stageTwoLevelTwoSelected.get(p);
                fieldLabel = stageTwoLevelTwoSelected.get(p);
                System.out.println("Stage-Two/mixRegGUI/Level 2 Regressor-Fields-(LocRan): " + regressorLabels[index]);
                index++;

                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo_stageTwo.length; q++) {

                    if (variableNamesCombo_stageTwo[q].equals(fieldLabel)) {
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

        String[] regressorLabels = new String[stageTwoLevelOneRegSize + stageTwoLevelTwoRegSize];
        int index = 0;

        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < stageTwoLevelOneRegSize; p++) {

            if (stageTwoLevelOneGridBoxes.get(p).get(2).isSelected()) {
                regressorLabels[index] = stageTwoLevelOneSelected.get(p);
                fieldLabel = stageTwoLevelOneSelected.get(p);
                System.out.println("Stage-Two/mixRegGUI/Regressor Fields (Scale): " + regressorLabels[index]);
                index++;

                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo_stageTwo.length; q++) {

                    if (variableNamesCombo_stageTwo[q].equals(fieldLabel)) {
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

        for (int p = 0; p < stageTwoLevelTwoRegSize; p++) {

            if (stageTwoLevelTwoGridBoxes.get(p).get(2).isSelected()) {
                regressorLabels[index] = stageTwoLevelTwoSelected.get(p);
                fieldLabel = stageTwoLevelTwoSelected.get(p);
                System.out.println("Stage-Two/mixRegGUI/Regressor Fields (Scale): " + regressorLabels[index]);
                index++;

                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo_stageTwo.length; q++) {

                    if (variableNamesCombo_stageTwo[q].equals(fieldLabel)) {
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

        for (int p = 0; p < stageTwoLevelOneRegSize; p++) {

            if (stageTwoLevelOneGridBoxes.get(p).get(0).isSelected()) {
                regressorLabels.add(stageTwoLevelOneSelected.get(p));
                fieldLabel = stageTwoLevelOneSelected.get(p);
                System.out.println("Stage-Two/mixRegGUI/Level 1 Regressor-Fields-(FIXED): " + regressorLabels.get(index));
                index++;

            }
        }

        for (int p = 0; p < stageTwoLevelTwoRegSize; p++) {

            if (stageTwoLevelTwoGridBoxes.get(p).get(0).isSelected()) {
                regressorLabels.add(stageTwoLevelTwoSelected.get(p));
                fieldLabel = stageTwoLevelTwoSelected.get(p);
                System.out.println("Stage-Two/mixRegGUI/Level 2 Regressor-Fields-(FIXED): " + regressorLabels.get(index));
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

        for (int p = 0; p < stageTwoLevelOneRegSize; p++) {

            if (stageTwoLevelOneGridBoxes.get(p).get(1).isSelected()) {
                regressorLabels.add(stageTwoLevelOneSelected.get(p));
                fieldLabel = stageTwoLevelOneSelected.get(p);
                System.out.println("STAGE-TWO/MIXREGGUI/Level 1 Regressor-Fields-(LOC RAN): " + regressorLabels.get(index));
                index++;

            }
        }

        for (int p = 0; p < stageTwoLevelTwoRegSize; p++) {

            if (stageTwoLevelTwoGridBoxes.get(p).get(1).isSelected()) {
                regressorLabels.add(stageTwoLevelTwoSelected.get(p));
                fieldLabel = stageTwoLevelTwoSelected.get(p);
                System.out.println("STAGE-TWO/MIXREGGUI/Level 2 Regressor-Fields-(LOC RAN): " + regressorLabels.get(index));
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

        for (int p = 0; p < stageTwoLevelOneRegSize; p++) {

            if (stageTwoLevelOneGridBoxes.get(p).get(2).isSelected()) {
                regressorLabels.add(stageTwoLevelOneSelected.get(p));
                fieldLabel = stageTwoLevelOneSelected.get(p);
                System.out.println("Stage-Two/mixRegGUI/Level 1 Regressor-Fields-(SCALE): " + regressorLabels.get(index));
                index++;
            }
        }

        for (int p = 0; p < stageTwoLevelTwoRegSize; p++) {

            if (stageTwoLevelTwoGridBoxes.get(p).get(2).isSelected()) {
                regressorLabels.add(stageTwoLevelTwoSelected.get(p));
                fieldLabel = stageTwoLevelTwoSelected.get(p);
                System.out.println("Stage-Two/mixRegGUI/Level 2 Regressor-Fields-(SCALE): " + regressorLabels.get(index));
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

        String[] regressorLabels = new String[stageTwoLevelOneRegSize + stageTwoLevelTwoRegSize];
        int index = 0;

        ArrayList<String> position = new ArrayList<>();

        for (int p = 0; p < stageTwoLevelOneRegSize; p++) {

            if (stageTwoLevelOneGridBoxes.get(p).get(3).isSelected()) {
                regressorLabels[index] = stageTwoLevelOneSelected.get(p);
                fieldLabel = stageTwoLevelOneSelected.get(p);
                System.out.println("Stage-two/mixRegGUI/Level 1 Regressor-Fields-(INTERACTION): " + regressorLabels[index]);
                index++;

                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo_stageTwo.length; q++) {

                    if (variableNamesCombo_stageTwo[q].equals(fieldLabel)) {
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

        for (int p = 0; p < stageTwoLevelTwoRegSize; p++) {

            if (stageTwoLevelTwoGridBoxes.get(p).get(3).isSelected()) {
                regressorLabels[index] = stageTwoLevelTwoSelected.get(p);
                fieldLabel = stageTwoLevelTwoSelected.get(p);
                System.out.println("Stage-two/mixRegGUI/Level 2 Regressor-Fields-(INTERACTION): " + regressorLabels[index]);
                index++;

                int posIndex = 0;

                for (int q = 0; q < variableNamesCombo_stageTwo.length; q++) {

                    if (variableNamesCombo_stageTwo[q].equals(fieldLabel)) {
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

        for (int p = 0; p < stageTwoLevelOneRegSize; p++) {

            if (stageTwoLevelOneGridBoxes.get(p).get(3).isSelected()) {
                regressorLabels.add(stageTwoLevelOneSelected.get(p));
                fieldLabel = stageTwoLevelOneSelected.get(p);
                System.out.println("STAGE-TWO/MIXREGGUI/Level 1 Regressor-Fields-(INTERACTIONS)= " + regressorLabels.get(index));
                index++;
            }
        }

        for (int p = 0; p < stageTwoLevelTwoRegSize; p++) {

            if (stageTwoLevelTwoGridBoxes.get(p).get(3).isSelected()) {
                regressorLabels.add(stageTwoLevelTwoSelected.get(p));
                fieldLabel = stageTwoLevelTwoSelected.get(p);
                System.out.println("STAGE-TWO/MIXREGGUI/Level 2 Regressor-Fields-(INTERACTIONS)= " + regressorLabels.get(index));
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

    public void produceStageTwoOutput(File filename) throws FileNotFoundException, IOException {

        String outputFileName = FilenameUtils.removeExtension(getDataFileName(2)) + "_output_1" + ".out";
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
                SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
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
                SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(this, "Stage 1 output could not be Saved!",
                        "Error!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    // TODO: Where is this used?
//    public void runMixedModels() {
//
//        String absoluteJavaPath = System.getProperty("user.dir");
//        String defFileName = executableModel(selectedModel);
//        try {
//            try {
//                copyExecutable(defFilePath, selectedModel); //get the def file path after it is saved
//                Process p = Runtime.getRuntime().exec("cmd /c dir && cd " + defFilePath + " && dir && "
//                        + defFileName); // does it save it in the same directory //@Eldin: This is where it is copying it twice.
//                //@Eldin: This is where we may want to keep the terminal open in the background.
//
//                p.waitFor();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//                String line = reader.readLine();
//                while (line != null) {
//                    System.out.println(line);
//                    line = reader.readLine();
//                }
//            } catch (FileNotFoundException fnfe1) {
//                SystemLogger.LOGGER.log(Level.SEVERE, fnfe1.toString() + "{0}", SystemLogger.getLineNum());
//                System.out.println("File not found Exception");
//            } catch (IOException e1) {
//                SystemLogger.LOGGER.log(Level.SEVERE, e1.toString() + "{0}", SystemLogger.getLineNum());
//                System.out.println("IO Exception");
//            }
//
//            try {
//                Process p = Runtime.getRuntime().exec("cmd /c dir && cd " + defFilePath + " && del /f " + defFileName);
//                p.waitFor();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//                String line = reader.readLine();
//                while (line != null) {
//                    System.out.println(line);
//                    line = reader.readLine();
//                }
//            } catch (FileNotFoundException fnfe1) {
//                SystemLogger.LOGGER.log(Level.SEVERE, fnfe1.toString() + "{0}", SystemLogger.getLineNum());
//                System.out.println("File not found Exception 2");
//            } catch (IOException e1) {
//                SystemLogger.LOGGER.log(Level.SEVERE, e1.toString() + "{0}", SystemLogger.getLineNum());
//                System.out.println("IO Exception 2 ");
//            }
//
//            JOptionPane.showMessageDialog(null, defFilePath);
//
//        } catch (Exception ex) {
//            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Failed");
//        }
//
//    }
    // TODO: Old, update
//    private String executableModel(int modelSelection) {
//        switch (modelSelection) {
//            case DefinitionHelper.MIXREGLS_MIXREG_KEY:
//                return "mixregls_mixreg.exe";
//            case DefinitionHelper.MIXREGLS_MIXOR_KEY:
//                return "mixregls_mixor.exe";
//            case DefinitionHelper.MIXREGMLS_MIXREG_KEY:
//                return "mixregmls_mixreg.exe";
//            case DefinitionHelper.MIXREGMLS_MIXOR_KEY:
//                return "mixregmls_mixor.exe";
//
//            default:
//                return "mixregls_mixreg.exe";
//        }
//    }
    // TODO: What is this? Not compatible with macOS
//    private void copyExecutable(String absoluteDirectoryPath, int modelSelection) throws FileNotFoundException, IOException {
//        String modelPath;
//        String executableName = executableModel(modelSelection);
//        switch (modelSelection) {
//            case DefinitionHelper.MIXREGLS_MIXREG_KEY:
//                modelPath = "resources/Windows/mixregls_mixreg.exe";
//                break;
//            case DefinitionHelper.MIXREGLS_MIXOR_KEY:
//                modelPath = "resources/Windows/mixregls_mixor.exe";
//                break;
//            case DefinitionHelper.MIXREGMLS_MIXREG_KEY:
//                modelPath = "resources/Windows/mixregmls_mixreg.exe";
//                break;
//            case DefinitionHelper.MIXREGMLS_MIXOR_KEY:
//                modelPath = "resources/Windows/mixregmls_mixor.exe";
//                break;
//            default:
//                modelPath = "resources/Windows/mixregls_mixreg.exe";
//                break;
//        }
//        InputStream stream = getClass().getClassLoader().getResourceAsStream(modelPath);
//
//        OutputStream outputStream
//                = new FileOutputStream(new File(absoluteDirectoryPath + executableName));
//
//        int read;
//        byte[] bytes = new byte[4096];
//
//        while ((read = stream.read(bytes)) > 0) {
//            outputStream.write(bytes, 0, read);
//        }
//        stream.close();
//        outputStream.close();
//    }
    public void runMixRegLS_mixor() {

    }

    public int getStagetwoOutcomeCats() {
        ArrayList<String> ColumnsCustom = new ArrayList<>();
        ArrayList<String> UniqueList = new ArrayList<>();

        String dataFileName = getDataFileName(2);
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
                int index = StageTwoOutcomeCombo.getSelectedIndex();
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
                } else if (ColumnsCustom.get(x).equals(defFile.getAdvancedMissingValueCode()) && !ColumnsCustom.get(x).equals("0")) { //compare if the category is a missing value, then don't consider it as a category
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
            SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
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

        String dataFileName = getDataFileName(2);
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
                int index = StageTwoOutcomeCombo.getSelectedIndex();
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
                } else if (ColumnsCustom.get(x).equals(defFile.getAdvancedMissingValueCode()) && !ColumnsCustom.get(x).equals("0")) { //compare if the category is a missing value, then don't consider it as a category
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
            SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
            e.printStackTrace();
            Logger.getLogger(getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        } catch (IOException e) {
            SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
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

    public void importDataSet() {
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
            filePath.setText("");

            try {
                getDataFromCSV();
                printFileName();
                System.out.println("NEW MODEL DATA READ");
                if (validDataset) {
                    filePath.setText(fileName);
                    stageOneTabs.insertTab("View Data", null, jPanel6, null, 1);
                }
            } catch (IOException ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            // build session folder
            try {
                sessionFolderNameBuilt = ModelBuilder.buildFolder(file);
            } catch (IOException ex) {
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            }

            // create new logger
            try {
                createNewLogger(file);
            } catch (IOException ex) {
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            }

        } else {
            System.out.println("File access cancelled by user.");
            try {
                System.out.println(file.getAbsolutePath());
            } catch (NullPointerException ex) {
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            }
        }
    }

    private void updateGuiView_trigger_NewModelSubmit() {
        System.out.println("Model submitted" + " called");

        if (validateFields() == true) {
            System.out.print("condition is true");
            updateGuiView_trigger_NewModelSubmit_TabChange();
            // stage 2 tab 
            updateStage2ConfigButton.setVisible(true);

            // save button setVisible
            guiStatesSaveButtonModalConfig.setVisible(true);
            guiStatesSaveButtonStageOne.setVisible(true);
            guiStatesSaveButtonStageTwo.setVisible(true);

            if (includeStageTwoNo() == true) {
                startStageTwo.setText("Run Stage 1");
            } else if (includeStageTwoNo() == false) {
                startStageTwo.setText("Configure Stage 2");
            }

            if (oneRLERadio.isSelected() == true) {
                RLE = MixLibrary.STAGE_ONE_RLE_LOCATION;
            } else if (moreThanOneRLERadio.isSelected() == true) {
                RLE = MixLibrary.STAGE_ONE_RLE_SLOPE;
            } else {
                isNewModalConfigSubmitted = false;
            }

//            defFile = new DefinitionHelper(RLE, !isOutcomeContinous());
//            modelBuilder = new ModelBuilder(defFile);
            // Include stage 2 or not
            if (getIncludeStageTwoYes() == true) {
                defFile = new MixLibrary(getStageOneOutcome(), RLE, getRandomScaleSelection(), getStageTwoModelType(), getStageTwoOutcomeType(), getStageTwoDataIncluded());
            }
            if (getIncludeStageTwoNo() == true) {
                defFile = new MixLibrary(getStageOneOutcome(), RLE, getRandomScaleSelection());
            }

            System.out.println("RLE: " + String.valueOf(RLE));

//            defFile.modelSelector(RLE, isOutcomeContinous());
            if (missingValuePresent.isSelected()) {

                System.out.println("NEW MODEL | MISSING VALUE = " + newModelMissingValueCode.getText());

                try {
                    System.out.println("NEW MODEL | MISSING VALUE = " + newModelMissingValueCode.getText());
                    defFile.setAdvancedMissingValueCode(String.valueOf(newModelMissingValueCode.getText()));

                    System.out.println("From defHelper | Missing Value: " + defFile.getAdvancedMissingValueCode());
                } catch (Exception ex) {
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE);
                }

            } else if (missingValueAbsent.isSelected()) {
                try {
                    defFile.setAdvancedMissingValueCode(String.valueOf(missingValue));
                    System.out.println("From defHelper | Missing Value: " + defFile.getAdvancedMissingValueCode());
                } catch (Exception ex) {
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE);
                }
                //do nothing
            }

            try {
                //convert csv to .dat file ...
                //defFile.
                if (sessionFolderName == null) {
                    defFile.csvToDatConverter(file);
                    if (getIncludeStageTwoDataYes()) {
                        defFile.csvToDatConverterSecondDataset(file_stageTwo);
                    }
                    sessionFolderName = defFile.getUtcDirPath();

                } else {
                    defFile.setUtcDirPath(sessionFolderName);

                }

            } catch (IOException ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE);
            }

//            System.out.println("MODEL SELECTOR: " + String.valueOf(defFile.getSelectedModel()));
            if (filePath.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Please upload a .csv file to start your analysis", "Caution!", JOptionPane.INFORMATION_MESSAGE);
            } else {

                try {

                    // Read file contents
                    Scanner inputStream = new Scanner(file);

                    // Read variable names from row 1
                    String variableNames = inputStream.next();
                    variableNamesCombo_stageOne = variableNames.split(",");
                    System.out.println("Variables are: " + Arrays.toString(variableNamesCombo_stageOne));
                    // save all variables in an array
                    defFile.setSharedDataFilename(extractDatFilePath(file));
                    defFile.setAdvancedVariableCount(String.valueOf(variableNamesCombo_stageOne.length));
                    System.out.println("From defHelper | Variable count: " + defFile.getAdvancedVariableCount());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                } catch (Exception ex) {
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                }

                // Read random location effects from new Model
                //RLE = (Integer) randomLocationEffects.getValue();
                notIncludeStageTwo = includeStageTwoNo();
//                outComeBoolean = isOutcomeContinous();
                System.out.println("NoneVar: " + String.valueOf(notIncludeStageTwo));
                System.out.println(String.valueOf(isOutcomeContinous()));
                System.out.println("IsOutcomeNone: " + String.valueOf(includeStageTwoNo()));
                // set Values in def helper
                defFile.setSharedModelTitle(getTitle());
                System.out.println("From defHelper | Title: " + defFile.getSharedModelTitle());
                //defFile.setModelSubtitle(getSubTitle());
                System.out.println("From defHelper | Subtitle: " + defFile.getSharedModelSubtitle());

                //Check if the randome scale is checked or not
                if (randomScaleSelectionYes.isSelected()) {
                    isRandomScale = true;

                } else if (randomScaleSelectionNo.isSelected()) {
                    isRandomScale = false;
                } else {
                    // randomScaleCheckBox
                }

                defFile.setSharedModelSubtitle("Created with MixWILD GUI");
                System.out.println("From defHelper | Subtitle: " + defFile.getSharedModelSubtitle());

                //set advanced options defaults and assign values to defition library
                try {
                    defFile.setAdvancedMeanIntercept(String.valueOf(0));
                    System.out.println("From defHelper | Mean SubModel Checked?: " + defFile.getAdvancedMeanIntercept());
                    //tryCount = 1;

                } catch (Exception ex) {
                    //catchCount = 1;
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    Logger
                            .getLogger(advancedOptions.class
                                    .getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedRandomIntercept(String.valueOf(0));
//                    defFile.setModelBetweenInt(String.valueOf(0));
                    System.out.println("From defHelper | BS SubModel Checked?: " + defFile.getAdvancedRandomIntercept());
//                    System.out.println("From defHelper | BS SubModel Checked?: " + defFile.getModelBetweenInt());

                } catch (Exception ex) {
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    Logger
                            .getLogger(advancedOptions.class
                                    .getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedScaleIntercept(String.valueOf(0));
//                    defFile.setModelWithinInt(String.valueOf(0));
                    System.out.println("From defHelper | WS SubModel Checked?: " + defFile.getAdvancedScaleIntercept());
//                    System.out.println("From defHelper | WS SubModel Checked?: " + defFile.getModelWithinInt());
                    //tryCount = 1;

                } catch (Exception ex) {
                    //catchCount = 1;
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    Logger
                            .getLogger(advancedOptions.class
                                    .getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedAdaptiveQuad(String.valueOf(1));
                    System.out.println("From defHelper | Adaptive Quadriture Checked?: " + defFile.getAdvancedAdaptiveQuad());
                    //tryCount = 1;

                } catch (Exception ex) {
                    //catchCount = 1;
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    Logger
                            .getLogger(advancedOptions.class
                                    .getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedConvergenceCriteria(String.valueOf(0.00001));
                    System.out.println("From defHelper | Convergence: " + defFile.getAdvancedConvergenceCriteria());
                    //tryCount = 1;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    Logger
                            .getLogger(advancedOptions.class
                                    .getName()).log(Level.SEVERE, null, ex);
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    //catchCount = 1;
                }
                try {
                    defFile.setAdvancedQuadPoints(String.valueOf(11));
                    System.out.println("From defHelper | Quadriture Points: " + defFile.getAdvancedQuadPoints());
                    //tryCount = 1;

                } catch (Exception ex) {
                    //catchCount = 1;
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    Logger
                            .getLogger(advancedOptions.class
                                    .getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedMaxIterations(String.valueOf(200));
                    System.out.println("From defHelper | Maximum Iteraions: " + defFile.getAdvancedMaxIterations());
                    //tryCount = 1;

                } catch (Exception ex) {
                    //catchCount = 1;
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    Logger
                            .getLogger(advancedOptions.class
                                    .getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedInitialRidge(String.valueOf(0.15));
                    System.out.println("From defHelper | Ridge: " + defFile.getAdvancedInitialRidge());
                    //tryCount = 1;

                } catch (Exception ex) {
                    //catchCount = 1;
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    Logger
                            .getLogger(advancedOptions.class
                                    .getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedRandomScaleCutoff("0");
                    System.out.println("CUT OFF: " + defFile.getAdvancedRandomScaleCutoff());
                } catch (Exception ex) {
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedDiscardNoVariance("0");
                    System.out.println("DISCARD SUBJECTS: " + defFile.getAdvancedDiscardNoVariance());

                } catch (Exception ex) {
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    Logger
                            .getLogger(advancedOptions.class
                                    .getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedCenterScaleVariables(String.valueOf(0));
                    System.out.println("From defHelper | Scale Regressor: " + defFile.getAdvancedCenterScaleVariables());

                } catch (Exception ex) {
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    Logger
                            .getLogger(advancedOptions.class
                                    .getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    defFile.setAdvancedResampleCount("500");
                    System.out.println("From defHelper | Resample count: " + defFile.getAdvancedResampleCount());

                } catch (Exception ex) {
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    Logger
                            .getLogger(advancedOptions.class
                                    .getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

                try {
                    defFile.setAdvancedResamplingSeed(seedTextBox.getText());
                    System.out.println("From defHelper | SEED: " + defFile.getAdvancedResamplingSeed());
                } catch (Exception ex) {
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

                defFile.setSharedOutputPrefix(extractDatFileName() + "_Output");
                System.out.println("From defHelper | Output file name: " + defFile.getSharedOutputPrefix());

                if (randomScaleSelectionYes.isSelected()) {
                    try {
                        defFile.setAdvancedUseRandomScale("0");
                        System.out.println("IS RANDOM SCALE INCLUDED: " + defFile.getAdvancedUseRandomScale());
                    } catch (Exception ex) {
                        SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                        Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    }

                } else {

                    try {
                        defFile.setAdvancedUseRandomScale("1");
                        System.out.println("IS RANDOM SCALE INCLUDED: " + defFile.getAdvancedUseRandomScale());
                    } catch (Exception ex) {
                        SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                        Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    }

                }

                if (includeStageTwoNo()) {
                    try {
                        // defFile.setAdvancedUseStageTwo("1");
                        System.out.println("DROP SECOND STAGE?: " + defFile.getAdvancedUseStageTwo());
                    } catch (Exception ex) {
                        SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                        Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    }

                } else {

                    try {
                        // defFile.setAdvancedUseStageTwo("0");
                        System.out.println("DROP SECOND STAGE?: " + defFile.getAdvancedUseStageTwo());
                    } catch (Exception ex) {
                        SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                        Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    }

                }

                setVisible(true);
//                    randomLocationEffectsLabel.setText("Random location effects: " + randomLocationEffects());
                Color darkGreen = new Color(0, 100, 0);
                randomLocationEffectsLabel.setText(randomLocationEffects());
                randomLocationEffectsLabel.setForeground(darkGreen);
                stageTwoOutcomePrintLabel.setText(stageOneOutcomeTypeString());
                stageTwoOutcomePrintLabel.setForeground(darkGreen);

                // if the update stage 2 not clicked, which means first run of model configuration
                if (!isUpdateStage2ConfigClicked) {
                    initiateStageOneTab();
                    //Update ID, stage one and stage two variable comboboxes
                    initiateStageOneTabLayout();
                    initiateStageOneComboBoxes();
                }

                // Initiation for stage 2 configuration
                initiateStageTwoComboBoxes();
                stage_2_regs = new stageTwoRegs();
                update_trigger_stageTwoOutcomeCat();
                update_trigger_stageTwoIDCombo();
                if (getIncludeStageTwoDataYes() == true) {
                    try {
                        defFile.setAdvancedMultipleDataFiles("1");
                        defFile.setSharedDataFilename_stageTwo(extractDatFilePath(file_stageTwo));
                        defFile.setStageTwoNewDataVariableCount(String.valueOf(variableNamesCombo_stageTwo.length));
                        defFile.setStageTwoNewDataIDField(String.valueOf(IDStageTwoVariableCombo.getSelectedIndex() + 1));

                    } catch (Exception ex) {
                        Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                        SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    }
                } else {
                    IDposStageTwo = -1;
                }

                // View Update for Model Configuration Tab 
                stageOneTabs.setSelectedIndex(1);
                newModelSubmit.setEnabled(false);
                fileBrowseButton.setEnabled(false);
                fileBrowseButton.setVisible(false);
                filePath.setEnabled(false);
                titleField.setEnabled(false);
                oneRLERadio.setEnabled(false);
                moreThanOneRLERadio.setEnabled(false);
                //randomScaleCheckBox.setEnabled(false);
                randomScaleSelectionYes.setEnabled(false);
                randomScaleSelectionNo.setEnabled(false);
                stageTwoContinuousRadio.setEnabled(false);
                stageTwoDichotomousRadio.setEnabled(false);
                stageTwoCountRadio.setEnabled(false);
                stageTwoMultinomialRadio.setEnabled(false);
                includeStageTwoNo.setEnabled(false);
                includeStageTwoYes.setEnabled(false);
                //noneRadio.setEnabled(false);
                missingValuePresent.setEnabled(false);
                missingValueAbsent.setEnabled(false);
                newModelMissingValueCode.setEnabled(false);
                seedTextBox.setEnabled(false);
                seedHelpButton.setVisible(false);
                stageOneContinuousRadio.setEnabled(false);
                stageOneDichotomousRadio.setEnabled(false);
                stageOneOrdinalRadio.setEnabled(false);
                StageOneProbitRadio.setEnabled(false);
                StageOneLogisticRadio.setEnabled(false);
                stageTwoSingleLevel.setEnabled(false);
                stageTwoMultiLevel.setEnabled(false);
//                includeStageTwoDataLabel.setEnabled(false);
                includeStageTwoDataYes.setEnabled(false);
                includeStageTwoDataNo.setEnabled(false);
//                DataFileStageTwoLabel.setEnabled(false);
                filePath_stageTwo.setEnabled(false);
                fileBrowseButtonStageTwoData.setEnabled(false);

                // Disabled stage 2 level 1 regressor box and table 
                // if single level is selected in model configuration 
                if (getStageTwoSingleLevel() == true) {
                    stage_2_regs.setEnabledStageTwoLevelOneAddButton(false);
                    stage_2_regs.setEnabledStageTwoLevelOneRemoveButton(false);
                    stage_2_regs.setEnabledStageTwoLevelOneRegTitle(false);
                    stage_2_regs.setEnabledStageTwoLevelOneRegVariables(false);
                    stageTwoLevelOnePanel.setEnabled(false);
                    stageTwoRegsGridLvl1.setEnabled(false);
                    clearStageTwoLevelOneGrid();
                } else if (getStageTwoMultiLevel() == true) {
                    stage_2_regs.setEnabledStageTwoLevelOneAddButton(true);
                    stage_2_regs.setEnabledStageTwoLevelOneRemoveButton(true);
                    stage_2_regs.setEnabledStageTwoLevelOneRegTitle(true);
                    stage_2_regs.setEnabledStageTwoLevelOneRegVariables(true);
                    stageTwoLevelOnePanel.setEnabled(true);
                    stageTwoRegsGridLvl1.setEnabled(true);
                }

                updateGuiView_trigger_randomScaleSelection();
            }
        } else {

            System.out.println("VALIDATION OF FIELDS: " + String.valueOf(false));

        } //To change body of generated methods, choose Tools | Templates.
    }

    private void update_trigger_StartStageTwo() {

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
        String[] idOutcome = {String.valueOf(IDvariableCombo.getSelectedIndex() + 1), String.valueOf(StageOneOutcomeCombo.getSelectedIndex() + 1)};

        if (getStageOneOutcome() == MixLibrary.STAGE_ONE_OUTCOME_MIXOR) {
            try {
                tryCount = 1;
                ArrayList<Double> uniqueValues = getStageOneOrTwoCategoricalOutcomeUniqueList(1);
                Integer categoryNum = uniqueValues.size();
                defFile.setAdvancedStageOneOutcomeValueCount(categoryNum.toString());
                System.out.println("From defHelper | number of categories for the ordinal stage 1 outcome: " + Arrays.toString(defFile.getSharedIdAndStageOneOutcomeFields()));
//                if (getStageOneOrdinalRadio()) {
                defFile.setMixorModelStageOneOutcomeLevels(listToString(uniqueValues));
                System.out.println("From defHelper | Values for the ordinal stage 1 outcome: " + Arrays.toString(defFile.getSharedIdAndStageOneOutcomeFields()));
//                }
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;
                defFile.setAdvancedLogisticProbitRegression(getStageOneRegressionType());
                System.out.println("From defHelper | Stage One Outcome regression type: " + defFile.getAdvancedLogisticProbitRegression());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
        }

        try {
            tryCount = 1;
            defFile.setSharedIdAndStageOneOutcomeFields(idOutcome);
            System.out.println("From defHelper | ID and Outcome indices: " + Arrays.toString(defFile.getSharedIdAndStageOneOutcomeFields()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            tryCount = 1;
            defFile.setSharedModelStageOneOutcomeLabel(getOutcomeLabel());
            System.out.println("From defHelper | Outcome variable Stage One LABEL: " + defFile.getSharedModelStageOneOutcomeLabel());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        // RLE_selected is the number of random location effects selected by the users
        if (RLE_selected == MixLibrary.STAGE_ONE_RLE_LOCATION) {

            try {
                tryCount = 1;
                int MeanCount = countLevelOneBeta() + countLevelTwoBeta() - countLevelOneDicompMean(); //check this ======================
                // count total mean regressors in level one and level two
                defFile.setAdvancedMeanRegressorCount(String.valueOf(MeanCount));
                System.out.println("From mixRegGUI | Stage 1 Model Mean Count: " + String.valueOf(MeanCount));
                System.out.println("From defHelper | Stage 1 Model Mean Count: " + defFile.getAdvancedMeanRegressorCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;
                int betweenCount = countLevelOneAlpha() + countLevelTwoAlpha() - countLevelOneDicompBS();
                defFile.setAdvancedRandomRegressorCount(String.valueOf(betweenCount));
                System.out.println("From defHelper | Model Between Count: " + defFile.getAdvancedRandomRegressorCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;
                int withinCount = countLevelOneTau() + countLevelTwoTau() - countLevelOneDicompWS();
                defFile.setAdvancedScaleRegressorCount(String.valueOf(withinCount));
                System.out.println("From defHelper | Model Within Count: " + defFile.getAdvancedScaleRegressorCount());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            //Number of disaggregate means
            try {
                tryCount = 1;
                defFile.setAdvancedDecomposeMeanRegressorCount(String.valueOf(countLevelOneDicompMean()));
                System.out.println("From defHelper | Stage 1 Decomp Model Mean Count: " + defFile.getAdvancedDecomposeMeanRegressorCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            //Number of disaggregate BS Variance
            try {
                tryCount = 1;
                defFile.setAdvancedDecomposeRandomRegressorCount(String.valueOf(countLevelOneDicompBS()));
                System.out.println("From defHelper | Stage 1 BS Variance Disagg. Regressor Count: " + defFile.getAdvancedDecomposeRandomRegressorCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            //Number of disaggregate WS Variance
            try {
                tryCount = 1;
                defFile.setAdvancedDecomposeScaleRegressorCount(String.valueOf(countLevelOneDicompWS()));
                System.out.println("From defHelper | Stage 1 WS Variance Disagg Regressor Count: " + defFile.getAdvancedDecomposeScaleRegressorCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            // ---- Check if the association radio buttons have been selected (Advanced effect of mean) ----
            //count field array sizes
            if (NoAssociationRadio.isSelected()) {

                try {
                    defFile.setAdvancedRandomScaleAssociation(String.valueOf(0));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (No Association): " + defFile.getAdvancedRandomScaleAssociation());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (LinearAssociationRadio.isSelected()) {
                try {
                    defFile.setAdvancedRandomScaleAssociation(String.valueOf(1));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (Linear Association): " + defFile.getAdvancedRandomScaleAssociation());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (QuadraticAssociationRadio.isSelected()) {
                try {
                    defFile.setAdvancedRandomScaleAssociation(String.valueOf(2));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (Quadratic Association): " + defFile.getAdvancedRandomScaleAssociation());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } // field array counting ends

            // get selected regressor labels and read them into defFile
            try {
                defFile.setSharedModelMeanRegressorLabels(ModelMeansLabelsArray());
//                System.out.println("From defHelper | Stage 1 MEAN REGRESSOR LABELS): " + Arrays.toString(defFile.getSharedModelMeanRegressorLabels()));
//                defFile.setSharedModelMeanRegressorLabelsLevelOne(getModelMeanLabelsLevelOne());
//                System.out.println("From defHelper | LEVEL 1 MEAN REGRESSOR LABELS): " + Arrays.toString(defFile.getSharedModelMeanRegressorLabelsLevelOne()));
//                defFile.setSharedModelMeanRegressorLabelsLevelTwo(getModelMeanLabelsLevelTwo());
//                System.out.println("From defHelper | LEVEL 2 MEAN REGRESSOR LABELS): " + Arrays.toString(defFile.getSharedModelMeanRegressorLabelsLevelTwo()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setSharedModelRandomRegressorLabels(ModelBSLabelsArray());
//                System.out.println("From defHelper | Stage 1 BS REGRESSOR LABELS): " + Arrays.toString(defFile.getSharedModelRandomRegressorLabels()));
//                defFile.setSharedModelRandomRegressorLabelsLevelOne(getModelBSLabelsLevelOne());
//                System.out.println("From defHelper | LEVEL 1 BS REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelBSRegressorsLevelOne()));
//                defFile.setSharedModelRandomRegressorLabelsLevelTwo(getModelBSLabelsLevelTwo());
//                System.out.println("From defHelper | LEVEL 2 BS REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelBSRegressorsLevelTwo()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setSharedModelScaleRegressorLabels(ModelWSLabelsArray());
                System.out.println("From defHelper | Stage 1 WS REGRESSOR LABELS): " + Arrays.toString(defFile.getSharedModelScaleRegressorLabels()));
                defFile.setLabelModelWSRegressorsLevelOne(getModelWSLabelsLevelOne());
                System.out.println("From defHelper | LEVEL 1 WS REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelWSRegressorsLevelOne()));
                defFile.setLabelModelWSRegressorsLevelTwo(getModelWSLabelsLevelTwo());
                System.out.println("From defHelper | LEVEL 2 WS REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelWSRegressorsLevelTwo()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            // Reads the variable names of variables that have been selected as mean regressors
            try {
                defFile.setSharedModelMeanRegressorFields(fieldModelMeanArray());
                System.out.println("From defHelper | #Stage One Mean Regressors: " + defFile.getSharedModelMeanRegressorFields().length);
                System.out.println("From defHelper | Stage One Mean Regressors Selected: " + Arrays.toString(defFile.getSharedModelMeanRegressorFields()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            // Reads the variable names of variables that have been selected as BS Variances
            try {
                defFile.setSharedModelRandomRegressorFields(fieldModelBSArray());
                System.out.println("From defHelper | #Stage One BS Regressors: " + defFile.getSharedModelRandomRegressorFields().length);
                System.out.println("From defHelper | Stage One BS Var. Regressors Selected: " + Arrays.toString(defFile.getSharedModelRandomRegressorFields()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            // Reads the variable names of variables that have been selected as WS Variances
            try {
                defFile.setSharedModelScaleRegressorFields(fieldModelWSArray());
                System.out.println("From defHelper | #Stage One WS Regressors: " + defFile.getSharedModelScaleRegressorFields().length);
                System.out.println("From defHelper | Stage One WS Var. Regressors Selected: " + Arrays.toString(defFile.getSharedModelScaleRegressorFields()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setSharedModelDecomposeMeanRegressorFields(getMeanDecompFieldRegressorLabels_levelOne());
                System.out.println("From defHelper | #Stage One Mean + Disagg. Regressors: " + defFile.getSharedModelDecomposeMeanRegressorFields().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setSharedModelDecomposeRandomRegressorFields(getBSDecompFieldRegressorLabels_levelOne());
                System.out.println("From defHelper | #Stage One BS + Disagg. Regressors: " + defFile.getSharedModelDecomposeRandomRegressorFields().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setSharedModelDecomposeScaleRegressorFields(getWSDecompFieldRegressorLabels_levelOne());
                System.out.println("From defHelper | #Stage One WS + Disagg. Regressors: " + defFile.getSharedModelDecomposeScaleRegressorFields().length);
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setSharedModelDecomposeMeanRegressorLabels(getDecompMeanLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + Mean Labels: " + Arrays.toString(defFile.getSharedModelDecomposeMeanRegressorLabels()));
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setSharedModelDecomposeRandomRegressorLabels(getDecompBSLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + BS Labels: " + Arrays.toString(defFile.getSharedModelDecomposeRandomRegressorLabels()));
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setSharedModelDecomposeScaleRegressorLabels(getDecompWSLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + WS Labels: " + Arrays.toString(defFile.getSharedModelDecomposeScaleRegressorLabels()));
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            //******************************************************************
        } else if (RLE_selected == MixLibrary.STAGE_ONE_RLE_SLOPE) {

            try {
                tryCount = 1;
                int MeanCount = countLevelOneBeta() + countLevelTwoBeta() - countLevelOneDicompMean();

                // count total mean regressors in level one and level two
                defFile.setAdvancedMeanRegressorCount(String.valueOf(MeanCount));
                System.out.println("From defHelper | Stage 1 Model Mean Count: " + defFile.getAdvancedMeanRegressorCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                int LocRanCount = countLevelOneAlpha() + countLevelTwoAlpha() - countLevelOneDicompBS();
                // count total random location regressors in level one and level two
                defFile.setAdvancedRandomRegressorCount(String.valueOf(LocRanCount));
                System.out.println("From defHelper | Stage 1 Model Loc Ran Count: " + defFile.getAdvancedRandomRegressorCount().toString());
                tryCount = 1;
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;
                int ScaleCount = countLevelOneTau() + countLevelTwoTau() - countLevelOneDicompWS();
                // count total scale regressors in level one and level two
                defFile.setAdvancedScaleRegressorCount(String.valueOf(ScaleCount));
                System.out.println("From defHelper | Stage 1 Model Scale Count: " + defFile.getAdvancedScaleRegressorCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;

                //Disagg means count
                defFile.setAdvancedDecomposeMeanRegressorCount(String.valueOf(countLevelOneDicompMean()));
                System.out.println("From defHelper | Stage 1 Decomp Model Mean Count: " + defFile.getAdvancedDecomposeMeanRegressorCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;
                //Disagg Random Location count
                defFile.setAdvancedDecomposeRandomRegressorCount(String.valueOf(countLevelOneDicompBS()));
                System.out.println("From defHelper | Stage 1 Decomp Model Loc Random Count: " + defFile.getAdvancedDecomposeRandomRegressorCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                tryCount = 1;
                //Disagg scale count
                defFile.setAdvancedDecomposeScaleRegressorCount(String.valueOf(countLevelOneDicompWS()));
                System.out.println("From defHelper | Stage 1 Decomp Scale Count: " + defFile.getAdvancedDecomposeScaleRegressorCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            //Check if the effect of mean on WS variances options have been selected
            if (NoAssociationRadio.isSelected()) {

                try {
                    defFile.setAdvancedRandomScaleAssociation(String.valueOf(1));
                    System.out.println("From defHelper | Stage 1 Association of random location & scale?: " + defFile.getAdvancedRandomScaleAssociation());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (LinearAssociationRadio.isSelected()) {
                try {
                    defFile.setAdvancedRandomScaleAssociation(String.valueOf(0));
                    System.out.println("From defHelper | Stage 1 Association of random location & scale?: " + defFile.getAdvancedRandomScaleAssociation());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
            }

            try {
                defFile.setSharedModelMeanRegressorLabels(ModelMeansLabelsArray());
                System.out.println("From defHelper | Stage 1 MEAN REGRESSOR LABELS): " + Arrays.toString(defFile.getSharedModelMeanRegressorLabels()));
//                defFile.setSharedModelMeanRegressorLabelsLevelOne(getModelMeanLabelsLevelOne());
//                System.out.println("From defHelper | LEVEL 1 MEAN REGRESSOR LABELS): " + Arrays.toString(defFile.getSharedModelMeanRegressorLabelsLevelOne()));
//                defFile.setSharedModelMeanRegressorLabelsLevelTwo(getModelMeanLabelsLevelTwo());
//                System.out.println("From defHelper | LEVEL 2 MEAN REGRESSOR LABELS): " + Arrays.toString(defFile.getSharedModelMeanRegressorLabelsLevelTwo()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setSharedModelRandomRegressorLabels(ModelBSLabelsArray());
                System.out.println("From defHelper | Stage 1 LocRan REGRESSOR LABELS): " + Arrays.toString(defFile.getSharedModelRandomRegressorLabels()));
                defFile.setLabelModelLocRanRegressorsLevelOne(getModelBSLabelsLevelOne());
                System.out.println("From defHelper | LEVEL 1 LocRan REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelLocRanRegressorsLevelOne()));
                defFile.setLabelModelLocRanRegressorsLevelTwo(getModelBSLabelsLevelTwo());
                System.out.println("From defHelper | LEVEL 2 LocRan REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelLocRanRegressorsLevelTwo()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setSharedModelScaleRegressorLabels(ModelWSLabelsArray());
                System.out.println("From defHelper | Stage 1 Scale REGRESSOR LABELS): " + Arrays.toString(defFile.getSharedModelScaleRegressorLabels()));
                defFile.setLabelModelScaleRegressorsLevelOne(getModelWSLabelsLevelOne());
                System.out.println("From defHelper | LEVEL 1 Scale REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelScaleRegressorsLevelOne()));
                defFile.setLabelModelScaleRegressorsLevelTwo(getModelWSLabelsLevelTwo());
                System.out.println("From defHelper | LEVEL 2 Scale REGRESSOR LABELS): " + Arrays.toString(defFile.getLabelModelScaleRegressorsLevelTwo()));
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            // count field labels
            try {
                // get variable names from selected mean regressors
                defFile.setSharedModelMeanRegressorFields(fieldModelMeanArray());
                System.out.println("From defHelper | #Stage One Mean Regressors: " + defFile.getSharedModelMeanRegressorFields().length); //check this ===============
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                // get variable names from selected random location regressors
                defFile.setSharedModelRandomRegressorFields(fieldModelBSArray());
                System.out.println("From defHelper | #Stage One BS(RanLoc) Regressors: " + defFile.getSharedModelRandomRegressorFields().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                // get variable names from selected scale regressors
                defFile.setSharedModelScaleRegressorFields(fieldModelWSArray());
                System.out.println("From defHelper | #Stage One WS(Scale) Regressors: " + defFile.getSharedModelScaleRegressorFields().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setSharedModelDecomposeMeanRegressorFields(getMeanDecompFieldRegressorLabels_levelOne());
                System.out.println("From defHelper | #Stage One Mean + Disagg. Regressors: " + defFile.getSharedModelDecomposeMeanRegressorFields().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

            try {
                defFile.setSharedModelDecomposeRandomRegressorFields(getBSDecompFieldRegressorLabels_levelOne());
                //defFile.setSharedModelDecomposeRandomRegressorFields(fieldModelBSArray());
                System.out.println("From defHelper | #Stage One BS(RanLoc) + Disagg. Regressors: " + defFile.getSharedModelDecomposeRandomRegressorFields().length);
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
            try {
                defFile.setSharedModelDecomposeScaleRegressorFields(getWSDecompFieldRegressorLabels_levelOne());
                System.out.println("From defHelper | #Stage One WS(Scale) + Disagg. Regressors: " + defFile.getSharedModelDecomposeScaleRegressorFields().length);
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
            try {
                defFile.setSharedModelDecomposeMeanRegressorLabels(getDecompMeanLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + Mean Labels: " + Arrays.toString(defFile.getSharedModelDecomposeMeanRegressorLabels()));
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
            try {
                defFile.setSharedModelDecomposeRandomRegressorLabels(getDecompBSLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + LocRan Labels: " + Arrays.toString(defFile.getSharedModelDecomposeRandomRegressorLabels()));
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
            try {
                defFile.setSharedModelDecomposeScaleRegressorLabels(getDecompWSLabelsLevelOne());
                System.out.println("From defHelper | Model Decomp + Scale Labels: " + Arrays.toString(defFile.getSharedModelDecomposeScaleRegressorLabels()));
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
        }
//        System.out.print("1!!!!!!!!!!!!!!!!!!!!!");
//        System.out.print(defFile.getAdvancedResampleCount());
//        advancedOptions_view.update_trigger_AdvancedOptionsSubmit();
//        System.out.print("2!!!!!!!!!!!!!!!!!!!!!");
//        System.out.print(defFile.getAdvancedResampleCount());

        if (getNotIncludeStageTwo() == true) {

            if (!checkTabExistinJTabbedPane(stageOneTabs, "View Model")) {
                int viewModelTabIdx = stageOneTabs.indexOfTab("View Data");
                stageOneTabs.insertTab("View Model", null, jPanel2, null, viewModelTabIdx);
            }
            if (!checkTabExistinJTabbedPane(stageOneTabs, "Stage 1 Results")) {
                int stage1ResultTabIdx = stageOneTabs.indexOfTab("View Model");
                stageOneTabs.insertTab("Stage 1 Results", null, jPanel3, null, stage1ResultTabIdx);
            }
            if (catchCount == 0) {
                int defTry = 0;
                int defCatch = 0;
                try {
                    List<String> defFileOutput;

                    defFile.writeStageOneOnlyDefFileToFolder(stageOneTabs.getSize());

                    //defFileOutput = defFile.buildStageOneOnlyDefinitonList();
                    System.out.println("From defHelper | Stage 1 def file created successfully!");

                } catch (Exception ex) {
                    defCatch = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    System.out.println("From defHelper | Stage 1 def file failed!");
                }

                if (defCatch == 0) {
                    stageOneTabs.setSelectedIndex(3);
//                    stageOneTabs.setEnabledAt(5, false);
                }

            } else {

                //stageOneTabs.setSelectedIndex(1);
                //System.out.println("outcome not true!!!!");
            }

            stageOneTabs.setSelectedIndex(2);

        } else {
            stageOneTabs.setSelectedIndex(2);
            stageOneTabs.setEnabledAt(2, true);
            System.out.println("outcome not none!!!!");
        }

//        try {
//            produceStageOneOutput();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
//            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
//        }
//        if (stageTwoNotIncluded == true) {
//            stageOneTabs.setEnabledAt(2, false);
//            stageOneTabs.setEnabledAt(4, false);
//        }
        Color darkGreen = new Color(0, 100, 0);
        stageOneModelStageTwoConfigLabel.setText(randomLocationEffects());
        stageOneModelStageTwoConfigLabel.setForeground(darkGreen);
        stageOneOutcomeStageTwoConfigLabel.setText(stageOneOutcomeTypeString());
        stageOneOutcomeStageTwoConfigLabel.setForeground(darkGreen);
        stageTwoOutcomeStageTwoConfigLabel.setText(stageTwoOutcomeTypeString());
        stageTwoOutcomeStageTwoConfigLabel.setForeground(darkGreen);
        stageTwoModelTypeStageTwoConfigLabel.setText(stageTwoModelTypeString());
        stageTwoModelTypeStageTwoConfigLabel.setForeground(darkGreen);
        numResamplingStageTwoConfigLabel.setText(numResamplingString());
        numResamplingStageTwoConfigLabel.setForeground(darkGreen);
    }

    private void update_trigger_StageOneRegConfig() {
        if (stageOneClicked == 1) {
            addStageTwoReg.setEnabled(true);
            if (addStageOneCHecked == true) {
                stage_1_regs.updateStageOneAgain();
            } else {
                stage_1_regs.updateAllVariables();
            }

        }
    }

    private void update_StageOneLevelOneBoxes(DefaultListModel<String> defaultListModel,
            boolean[][] StageOneLevelOneBoxesSelection,
            boolean[][] disaggVarianceBoxesSelection) {

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

        constraints.insets = new Insets(3, 10, 5, 0);
        separatorConstraint.insets = new Insets(0, 0, 0, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        levelOneBoxes = new ArrayList<>();
        disaggVarianceBoxes = new ArrayList<>();

        for (int j = 0; j < regSize; j++) {
            constraints.gridx = 0;
            constraints.anchor = GridBagConstraints.LINE_END;
//            constraints.fill = GridBagConstraints.HORIZONTAL;
            levelOneSelected.add(defaultListModel.getElementAt(j));
            JLabel variableText = new JLabel(levelOneSelected.get(j));
//            variableText.setBorder(new LineBorder(Color.BLACK));
            variableText.setPreferredSize(new Dimension(80, 20));
            levelOneGrid.add(variableText, constraints);

            levelOneBoxes.add(j, new ArrayList<JCheckBox>());

            for (int k = 0; k < 3; k++) {
                int row = j;
                int column = k;

                constraints.gridx++;
                constraints.anchor = GridBagConstraints.CENTER;
                levelOneBoxes.get(j).add(k, new JCheckBox());

                if (StageOneLevelOneBoxesSelection[j][k] == true) {
                    levelOneBoxes.get(j).get(k).setSelected(true);
//                    disaggVarianceBoxes.get(j).get(k).setEnabled(true);
                }
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

            levelOneGrid.add(new JLabel("  - Disaggregate"), constraints);
            disaggVarianceBoxes.add(j, new ArrayList<JCheckBox>());

            for (int k = 0; k < 3; k++) {
                constraints.gridx++;
                constraints.anchor = GridBagConstraints.CENTER;

                disaggVarianceBoxes.get(j).add(k, new JCheckBox());
                if (disaggVarianceBoxesSelection[j][k] == true) {
                    disaggVarianceBoxes.get(j).get(k).setSelected(true);
                }
                levelOneGrid.add(disaggVarianceBoxes.get(j).get(k), constraints);
                disaggVarianceBoxes.get(j).get(k).setEnabled(false);

                if (levelOneBoxes.get(j).get(k).isSelected() == true) {
                    disaggVarianceBoxes.get(j).get(k).setEnabled(true);
                }
                separatorConstraint.gridy = separatorConstraint.gridy + 1;
            }

            constraints.gridy++;
            //constraints.gridx = 0;
            separatorConstraint.gridy = separatorConstraint.gridy + 2;
            //System.out.println("before seperator");
            levelOneGrid.add(new JSeparator(JSeparator.HORIZONTAL), separatorConstraint);
            //System.out.println("after seperator");
            constraints.gridy++;

        }

        stageOneLevelOnePanel.removeAll();

        stageOneLevelOnePanel.revalidate();

        stageOneLevelOnePanel.repaint();

        stageOneLevelOnePanel.add(scrollpanel);

        revalidate();

    }

    private void update_StageOneLevelTwoBoxes(DefaultListModel<String> defaultListModel, boolean[][] StageOneLevelTwoBoxesSelection) {

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
//        constraints.weightx = 1.0;
        // constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTH;
        //constraints.gridwidth = 4;

        GridBagConstraints separatorConstraint = new GridBagConstraints();
        separatorConstraint.weightx = 1.0;
        separatorConstraint.fill = GridBagConstraints.HORIZONTAL;
        separatorConstraint.gridwidth = GridBagConstraints.REMAINDER;
        separatorConstraint.gridx = 0;

        constraints.insets = new Insets(3, 10, 5, 0);
        separatorConstraint.insets = new Insets(0, 0, 0, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        levelTwoBoxes = new ArrayList<ArrayList<JCheckBox>>();
        //disaggVarianceBoxes = new ArrayList<ArrayList<JCheckBox>>();

        for (int j = 0; j < regSize; j++) {
            constraints.gridx = 0;
            constraints.anchor = GridBagConstraints.LINE_END;
            levelTwoSelected.add(defaultListModel.getElementAt(j));
            JLabel variableText = new JLabel(levelTwoSelected.get(j));
//            variableText.setBorder(new LineBorder(Color.BLACK));
            variableText.setPreferredSize(new Dimension(80, 20));
            levelTwoGrid.add(variableText, constraints);
            //levelTwoGrid.add(new JLabel(defaultListModel.getElementAt(j)), constraints);

            levelTwoBoxes.add(j, new ArrayList<JCheckBox>());

            for (int k = 0; k < 3; k++) {

                constraints.gridx++;
                constraints.anchor = GridBagConstraints.CENTER;
                levelTwoBoxes.get(j).add(k, new JCheckBox());
                // hoho
                if (StageOneLevelTwoBoxesSelection[j][k] == true) {
                    levelTwoBoxes.get(j).get(k).setSelected(true);
//                    System.out.print("********"+j+k+"********");
                }

                levelTwoGrid.add(levelTwoBoxes.get(j).get(k), constraints);
            }

            if (RLE_selected == MixLibrary.STAGE_ONE_RLE_SLOPE) {
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

        stageOneLevelTwoPanel.removeAll();
        stageOneLevelTwoPanel.revalidate();
        stageOneLevelTwoPanel.repaint();

        stageOneLevelTwoPanel.add(scrollpanel);
        revalidate();

    }

    private void update_trigger_runTabTwoStageOneTwo() {
        // validate ID variable of second dataset
        boolean validation_pass = validate_stageTwoDataset_ID();
        if (!validation_pass) {
            //error message
            return;
        }

        // TODO add your handling code here:
        // tryCount counts the number of successful DefinitionHelper function calls
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
        if (getStageTwoOutcomeType() == MixLibrary.STAGE_TWO_OUTCOME_ORDINAL || getStageTwoOutcomeType() == MixLibrary.STAGE_TWO_OUTCOME_NOMINAL) {

            try {
                ArrayList<Double> uniqueCategoryList = getStageOneOrTwoCategoricalOutcomeUniqueList(2);
                Integer categoryNum = uniqueCategoryList.size();
                defFile.setStageTwoOutcomeCategoryNum(categoryNum.toString());
                System.out.println("From defHelper | Number of categories of categorical STAGE TWO Outcome: " + defFile.getStageTwoOutcomeField());

                defFile.setStageTwoCategoricalOutcomeUniqueList(listToString(uniqueCategoryList));
                System.out.println("From defHelper | Category values of categorical STAGE TWO Outcome: " + defFile.getStageTwoOutcomeField());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }
        }

        try {
            defFile.setStageTwoOutcomeField(getStageTwoOutcomePosition());
            System.out.println("From defHelper | Outcome variable Position STAGE TWO: " + defFile.getStageTwoOutcomeField());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setStageTwoOutcomeLabel(getStageTwoOutcomeLabel());
            System.out.println("From defHelper | Outcome variable label STAGE TWO: " + defFile.getStageTwoOutcomeLabel());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        // *********************************************************************
        // RLE_selected is the number of random location effects selected by the users
        if (RLE_selected == MixLibrary.STAGE_ONE_RLE_LOCATION) {

            if (NoAssociationRadio.isSelected()) {

                try {
                    defFile.setAdvancedRandomScaleAssociation(String.valueOf(0));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (No Association): " + defFile.getAdvancedRandomScaleAssociation());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (LinearAssociationRadio.isSelected()) {
                try {
                    defFile.setAdvancedRandomScaleAssociation(String.valueOf(1));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (Linear Association): " + defFile.getAdvancedRandomScaleAssociation());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (QuadraticAssociationRadio.isSelected()) {
                try {
                    defFile.setAdvancedRandomScaleAssociation(String.valueOf(2));
                    System.out.println("From defHelper | Stage 1 Advanced effects of mean on WS variance (Quadratic Association): " + defFile.getAdvancedRandomScaleAssociation());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } // field array counting ends

        } else if (RLE_selected == MixLibrary.STAGE_ONE_RLE_SLOPE) {

            //Check if the effect of mean on WS variances options have been selected
            if (NoAssociationRadio.isSelected()) {

                try {
                    defFile.setAdvancedRandomScaleAssociation(String.valueOf(1));
                    System.out.println("From defHelper | Stage 1 Association of random location & scale?: " + defFile.getAdvancedRandomScaleAssociation());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

            } else if (LinearAssociationRadio.isSelected()) {
                try {
                    defFile.setAdvancedRandomScaleAssociation(String.valueOf(0));
                    System.out.println("From defHelper | Stage 1 Association of random location & scale?: " + defFile.getAdvancedRandomScaleAssociation());
                } catch (Exception ex) {
                    catchCount = 1;
                    Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
            }
        }

        try {
            defFile.setAdvancedStageTwoFixedRegressorCount(String.valueOf(countStageTwoBeta()));
            System.out.println("From defHelper | STAGE TWO FIXED COUNT: " + defFile.getAdvancedStageTwoFixedRegressorCount());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setAdvancedStageTwoThetaRegressorCount(String.valueOf(countStageTwoAlpha()));
            System.out.println("From defHelper | STAGE TWO LOC. RANDOM COUNT: " + defFile.getAdvancedStageTwoThetaRegressorCount().toString());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setAdvancedStageTwoOmegaRegressorCount(String.valueOf(countStageTwoTau()));
            System.out.println("From defHelper | STAGE TWO SCALE COUNT: " + defFile.getAdvancedStageTwoOmegaRegressorCount().toString());
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        if (getEnableInteractionCheckBox() == false) {

            try {
                defFile.setAdvancedStageTwoInteractionRegressorCount("-1");
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            }
        } else {

            try {
                defFile.setAdvancedStageTwoInteractionRegressorCount(String.valueOf(countStageTwoInteractions()));
                System.out.println("From defHelper | STAGE TWO INTERACTIONS COUNT: " + defFile.getAdvancedStageTwoInteractionRegressorCount().toString());
            } catch (Exception ex) {
                catchCount = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

        }
        try {
            defFile.setStageTwoInteractionFields(getInteractionFieldRegressors_StageTwo());
            System.out.println("From defHelper | STAGE TWO  INTERACTIONS REGRESSOR Positions: " + Arrays.toString(defFile.getStageTwoInteractionFields()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setStageTwoInteractionLabels(getModelInteractionLabelsStageTwo());
            System.out.println("From defHelper | STAGE TWO  INTERACTIONS REGRESSORS: " + Arrays.toString(defFile.getStageTwoInteractionLabels()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setStageTwoFixedFields(getFixedFieldRegressors_StageTwo());
            System.out.println("From defHelper | STAGE TWO  FIXED REGRESSOR Positions: " + Arrays.toString(defFile.getStageTwoFixedFields()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setStageTwoThetaFields(getRanLocFieldRegressors_StageTwo());
            System.out.println("From defHelper | STAGE TWO  LOC RAN REGRESSOR Positions: " + Arrays.toString(defFile.getStageTwoThetaFields()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setStageTwoOmegaFields(getScaleFieldRegressors_StageTwo());
            System.out.println("From defHelper | STAGE TWO  SCALE REGRESSOR Positions: " + Arrays.toString(defFile.getStageTwoOmegaFields()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setStageTwoFixedLabels(getModelFixedLabelsStageTwo());
            System.out.println("From defHelper | STAGE TWO  MEAN REGRESSORS: " + Arrays.toString(defFile.getStageTwoFixedLabels()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setStageTwoThetaLabels(getModelLocRanLabelsStageTwo());
            System.out.println("From defHelper | STAGE TWO  LOC RAN REGRESSORS: " + Arrays.toString(defFile.getStageTwoThetaLabels()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            defFile.setStageTwoOmegaLabels(getModelScaleLabelsStageTwo());
            System.out.println("From defHelper | STAGE TWO  SCALE REGRESSORS: " + Arrays.toString(defFile.getStageTwoOmegaLabels()));
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(stageOneTabs, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        if (getIncludeStageTwoDataYes() == true) {
            try {
//                defFile.setAdvancedMultipleDataFiles("1");
//                defFile.setSharedDataFilename_stageTwo(extractDatFilePath(file_stageTwo));
//                defFile.setStageTwoNewDataVariableCount(String.valueOf(variableNamesCombo_stageTwo.length));
                defFile.setStageTwoNewDataIDField(String.valueOf(IDStageTwoVariableCombo.getSelectedIndex() + 1));

            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            }
        }
//        if (outComeType == false) {
//            try {
//                defFile.setStageTwoOutcomeCatCount(String.valueOf(getStagetwoOutcomeCats()));
//                System.out.println("From defHelper | STAGE TWO OUTCOME CATEGORY NUMBERS: " + defFile.getStageTwoOutcomeCatCount());
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
//                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//            try {
//                defFile.setStageTwoOutcomeCatLabel(getStageTwoOutcomeValues());
//                System.out.println("From defHelper | STAGE TWO OUTCOME CATEGORY VALUES: " + Arrays.toString(defFile.getStageTwoOutcomeCatLabel()));
//            } catch (Exception ex) {
//                catchCount = 1;
//                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
//                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
//                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//            }
//
//        } else {
//
//            // do nothing ...
//        }

        if (catchCount == 0) {
            int defTry = 0;
            int defCatch = 0;
            try {
                List<String> defFileOutput;

                defFile.writeDefFileToFolder(stageOneTabs.getSize());
                defFileOutput = defFile.buildDefinitionList();
                System.out.println("From defHelper | Stage 1&2 def file created successfully!");

            } catch (Exception ex) {

                defCatch = 1;
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                // Error Jframe is set to be always on top
                JFrame jf = new JFrame();
                jf.setAlwaysOnTop(true);
                JOptionPane.showMessageDialog(jf, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
//                JOptionPane.setAlwaysOnTop(alwaysOnTop);
                System.out.println("From defHelper | Stage 1 def file failed!");
            }

            if (!checkTabExistinJTabbedPane(stageOneTabs, "View Model")) {
                int viewModelTabIdx;
                if (checkTabExistinJTabbedPane(stageOneTabs, "View Data")) {
                    viewModelTabIdx = stageOneTabs.indexOfTab("View Data");
                    stageOneTabs.insertTab("View Model", null, jPanel2, null, viewModelTabIdx);
                } else if (checkTabExistinJTabbedPane(stageOneTabs, "View Stage 1 Data")) {
                    viewModelTabIdx = stageOneTabs.indexOfTab("View Stage 1 Data");
                    stageOneTabs.insertTab("View Model", null, jPanel2, null, viewModelTabIdx);
                }
            }
            if (!checkTabExistinJTabbedPane(stageOneTabs, "Stage 2 Results")) {
                int stage2TabIdx = stageOneTabs.indexOfTab("View Model");
                stageOneTabs.insertTab("Stage 2 Results", null, jPanel4, null, stage2TabIdx);
            }
            if (!checkTabExistinJTabbedPane(stageOneTabs, "Stage 1 Results")) {
                int stage1ResultTabIdx = stageOneTabs.indexOfTab("Stage 2 Results");
                stageOneTabs.insertTab("Stage 1 Results", null, jPanel3, null, stage1ResultTabIdx);
            }

            // jump to "stage 1 result" tab
            int stageOneTabIdx = stageOneTabs.indexOfTab("Stage 1 Results");
            stageOneTabs.setSelectedIndex(stageOneTabIdx);

        } else {
            // do nothing
        }

    }

    private void update_StageTwoStates(MixRegGuiStates mxrStates) {

        // stage 2 outcome (done in update_StageTwoStates)
        // Configure stage 2 regressors
        stageTwoRegs.stageTwoListModel = mxrStates.stageTwoListModel;
        stageTwoRegs.stageTwoLevelOne = mxrStates.stageTwoLevelOne;
        stageTwoRegs.stageTwoLevelTwo = mxrStates.stageTwoLevelTwo;
        stage_2_regs.getStageTwoAllVariables().removeAll();
        stage_2_regs.getStageTwoAllVariables().setModel(mxrStates.stageTwoListModel);
        stage_2_regs.getStageTwoAllVariables().setSelectedIndex(0);
        stage_2_regs.getStageTwoLevelOneVariables().removeAll();
        stage_2_regs.getStageTwoLevelOneVariables().setModel(mxrStates.stageTwoLevelOne);
        stage_2_regs.getStageTwoLevelTwoVariables().removeAll();
        stage_2_regs.getStageTwoLevelTwoVariables().setModel(mxrStates.stageTwoLevelTwo);

        // boxes
        stageTwoRegs.isStageTwoSubmitClicked = mxrStates.isStageTwoSubmitClicked;
        if (stageTwoRegs.isStageTwoSubmitClicked == true) {
            stage_2_regs.setEnabledStageTwoSubmitButton(true);
            update_StageTwoLevelOneBoxes(stageTwoRegs.stageTwoLevelOne, mxrStates.stageTwoLevelOneGridBoxesSelection);
            update_StageTwoLevelTwoBoxes(stageTwoRegs.stageTwoLevelTwo, mxrStates.stageTwoLevelTwoGridBoxesSelection);
            enbaleInteractionCheckBox.setSelected(mxrStates.suppressIntCheckBox);
            update_trigger_enableInteractionCheckBox();
        }

        // suppress scale X random Interaction
        // run stage 1 and 2
//
//        isStageOneSubmitted = mxrStates.isStageOneSubmitted;
//        if (isStageOneSubmitted == true) {
//            update_trigger_StartStageTwo();
//        }
    }

    private void update_StageTwoLevelTwoBoxes(DefaultListModel<String> defaultListModel, boolean[][] stageTwoGridBoxesSelection) {

        JScrollPane scrollpanel = new JScrollPane(stageTwoRegsGridLvl2);
        stageTwoLevelTwoSelected = new ArrayList<String>();

        int regSize = defaultListModel.getSize();
        stageTwoLevelTwoRegSize = regSize;

        stageTwoRegsGridLvl2.removeAll();

        if (suppressed) {
            jLabel37.setVisible(false);
        } else {
            jLabel37.setVisible(true);
        }

        stageTwoRegsGridLvl2.setLayout(new GridBagLayout());
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

        constraints.insets = new Insets(3, 5, 5, 0);
        separatorConstraint.insets = new Insets(0, 0, 0, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        stageTwoLevelTwoGridBoxes = new ArrayList<ArrayList<JCheckBox>>();
        //disaggVarianceBoxes = new ArrayList<ArrayList<JCheckBox>>();

        for (int j = 0; j < regSize; j++) {
            int row = j;
            constraints.gridx = 1;
            constraints.anchor = GridBagConstraints.FIRST_LINE_START;
            stageTwoLevelTwoSelected.add(defaultListModel.getElementAt(j));
            JLabel variableText = new JLabel(stageTwoLevelTwoSelected.get(j));
//            variableText.setBorder(new LineBorder(Color.BLACK));
            variableText.setPreferredSize(new Dimension(60, 20));
            stageTwoRegsGridLvl2.add(variableText, constraints);
//            stageTwoRegsGridLvl2.add(new JLabel(stageTwoLevelTwoSelected.get(j)), constraints);

            //stageTwoGrid.add(new JLabel(defaultListModel.getElementAt(j)), constraints);
            stageTwoLevelTwoGridBoxes.add(j, new ArrayList<JCheckBox>());

            for (int k = 0; k < 4; k++) {

                if (k == 1) {
                    constraints.gridx = constraints.gridx + 5;
                } else {
                    constraints.gridx++;
                }

                constraints.anchor = GridBagConstraints.CENTER;

                if (k == 3) {
                    if (suppressed) {
                        JLabel placeHolder3 = new JLabel(" ");
                        placeHolder3.setPreferredSize(new Dimension(20, 20));
                        stageTwoRegsGridLvl2.add(placeHolder3, constraints);
                        stageTwoLevelTwoGridBoxes.get(j).add(k, new JCheckBox());
                        stageTwoLevelTwoGridBoxes.get(j).get(k).setEnabled(false);
                    } else {
                        stageTwoLevelTwoGridBoxes.get(j).add(k, new JCheckBox());
                        stageTwoLevelTwoGridBoxes.get(j).get(k).setEnabled(true);
                    }
                } else {
                    stageTwoLevelTwoGridBoxes.get(j).add(k, new JCheckBox());
                    if (suppressed) {
                        stageTwoLevelTwoGridBoxes.get(j).get(k).setEnabled(false);
                    } else {
                        stageTwoLevelTwoGridBoxes.get(j).get(k).setEnabled(true);
                    }
                }
                if (stageTwoGridBoxesSelection[j][k] == true) {
                    stageTwoLevelTwoGridBoxes.get(j).get(k).setSelected(true);
                }
                if (k == 0) {
                    stageTwoLevelTwoGridBoxes.get(j).get(k).setSelected(true);
                    stageTwoLevelTwoGridBoxes.get(j).get(k).setVisible(false);
                    constraints.gridx++;

                    if (isRandomScale) {
                        JLabel placeHolder1 = new JLabel(" ");
                        placeHolder1.setPreferredSize(new Dimension(60, 20));
                        stageTwoRegsGridLvl2.add(placeHolder1, constraints);
                    }
                }

                if (k == 3) {
                    if (suppressed) {
                        // do nothing
                    } else {
                        stageTwoRegsGridLvl2.add(stageTwoLevelTwoGridBoxes.get(j).get(k), constraints);
                    }
                } else {
                    stageTwoRegsGridLvl2.add(stageTwoLevelTwoGridBoxes.get(j).get(k), constraints);
                }

            }

            constraints.gridy++;

            separatorConstraint.gridy = separatorConstraint.gridy + 2;

            stageTwoRegsGridLvl2.add(new JSeparator(JSeparator.HORIZONTAL), separatorConstraint);
            //System.out.println("after seperator");
            constraints.gridy++;

            if (!isRandomScale) {
                stageTwoLevelTwoGridBoxes.get(row).get(2).setVisible(false);
            }

//            stageTwoLevelTwoGridBoxes.get(row).get(1).setEnabled(true);
//            stageTwoLevelTwoGridBoxes.get(row).get(2).setEnabled(true);
//            stageTwoLevelTwoGridBoxes.get(row).get(3).setEnabled(false);
//            stageTwoLevelTwoGridBoxes.get(j).get(0).addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                    AbstractButton abstractButton = (AbstractButton) e.getSource();
//                    boolean selected = abstractButton.getModel().isSelected();
//                    if (selected) {
//                        System.out.println("Checkbox selected");
//                        //disaggVarianceBoxes.get(row).get(column).setEnabled(true);
//                        stageTwoLevelTwoGridBoxes.get(row).get(1).setEnabled(true);
//                        stageTwoLevelTwoGridBoxes.get(row).get(1).setSelected(false);
//                        stageTwoLevelTwoGridBoxes.get(row).get(2).setEnabled(true);
//                        stageTwoLevelTwoGridBoxes.get(row).get(2).setSelected(false);
//                        randomChecked = false;
//                        scaleChecked = false;
////                        System.out.println(disaggVarianceBoxes.size());
//                    } else {
//                        //disaggVarianceBoxes.get(row).get(column).setEnabled(false);
//                        stageTwoLevelTwoGridBoxes.get(row).get(1).setEnabled(false);
//                        stageTwoLevelTwoGridBoxes.get(row).get(1).setSelected(false);
//                        stageTwoLevelTwoGridBoxes.get(row).get(2).setEnabled(false);
//                        stageTwoLevelTwoGridBoxes.get(row).get(2).setSelected(false);
//                        stageTwoLevelTwoGridBoxes.get(row).get(3).setSelected(false);
//                        stageTwoLevelTwoGridBoxes.get(row).get(3).setEnabled(false);
//                        randomChecked = false;
//                        scaleChecked = false;
//                        enbaleInteractionCheckBox.setEnabled(false);
//                        enbaleInteractionCheckBox.setSelected(false);
//
//                    }
//
//                }
//            });
            stageTwoLevelTwoGridBoxes.get(j).get(1).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();
                    if (selected) {
                        scaleChecked = true;
                        if (randomChecked == true) {
                            if (!suppressed) {
                                stageTwoLevelTwoGridBoxes.get(row).get(3).setEnabled(true);
                                stageTwoLevelTwoGridBoxes.get(row).get(3).setSelected(false);
                            }
                        }

                    } else {
                        scaleChecked = false;
                        if (!suppressed) {
                            stageTwoLevelTwoGridBoxes.get(row).get(3).setEnabled(false);
                            stageTwoLevelTwoGridBoxes.get(row).get(3).setSelected(false);
                        }
                    }
                }
            });

            stageTwoLevelTwoGridBoxes.get(j).get(2).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();

                    if (selected) {
                        randomChecked = true;

                        if (scaleChecked == true) {
                            if (!suppressed) {
                                stageTwoLevelTwoGridBoxes.get(row).get(3).setEnabled(true);
                                stageTwoLevelTwoGridBoxes.get(row).get(3).setSelected(false);
                            }
                        }

                    } else {
                        randomChecked = false;
                        if (!suppressed) {
                            stageTwoLevelTwoGridBoxes.get(row).get(3).setEnabled(false);
                            stageTwoLevelTwoGridBoxes.get(row).get(3).setSelected(false);
                        }

                    }
                }
            });

            stageTwoLevelTwoGridBoxes.get(j).get(3).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();

                    randomChecked = false;
                    scaleChecked = false;

                    enbaleInteractionCheckBox.setEnabled(true);

                }
            });

        }

        stageTwoLevelTwoPanel.removeAll();
        stageTwoLevelTwoPanel.add(scrollpanel);
        stageTwoLevelTwoPanel.revalidate();
        stageTwoLevelTwoPanel.repaint();

        revalidate();

    }

    private void update_StageTwoLevelOneBoxes(DefaultListModel<String> defaultListModel, boolean[][] stageTwoLevelOneGridBoxesSelection) {

        JScrollPane scrollpanel = new JScrollPane(stageTwoRegsGridLvl1);
        stageTwoLevelOneSelected = new ArrayList<String>();

        int regSize = defaultListModel.getSize();
        stageTwoLevelOneRegSize = regSize;

        stageTwoRegsGridLvl1.removeAll();

        if (suppressed) {
            jLabel18.setVisible(false);
        } else {
            jLabel18.setVisible(true);
        }

        stageTwoRegsGridLvl1.setLayout(new GridBagLayout());
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

        constraints.insets = new Insets(3, 5, 5, 0);
        separatorConstraint.insets = new Insets(0, 0, 0, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        stageTwoLevelOneGridBoxes = new ArrayList<ArrayList<JCheckBox>>();
        //disaggVarianceBoxes = new ArrayList<ArrayList<JCheckBox>>();

        for (int j = 0; j < regSize; j++) {
            int row = j;
            constraints.gridx = 1;
            constraints.anchor = GridBagConstraints.FIRST_LINE_START;
            stageTwoLevelOneSelected.add(defaultListModel.getElementAt(j));
            JLabel variableText = new JLabel(stageTwoLevelOneSelected.get(j));
//            variableText.setBorder(new LineBorder(Color.BLACK));
            variableText.setPreferredSize(new Dimension(60, 20));
            stageTwoRegsGridLvl1.add(variableText, constraints);
//            stageTwoRegsGridLvl1.add(new JLabel(stageTwoLevelOneSelected.get(j)), constraints);

            //stageTwoGrid.add(new JLabel(defaultListModel.getElementAt(j)), constraints);
            stageTwoLevelOneGridBoxes.add(j, new ArrayList<JCheckBox>());

            for (int k = 0; k < 4; k++) {

                if (k == 1) {
                    constraints.gridx = constraints.gridx + 5;
                } else {
                    constraints.gridx++;
                }

                constraints.anchor = GridBagConstraints.CENTER;

                if (k == 3) {
                    if (suppressed) {
                        JLabel placeHolder3 = new JLabel(" ");
                        placeHolder3.setPreferredSize(new Dimension(20, 20));
                        stageTwoRegsGridLvl1.add(placeHolder3, constraints);
                        stageTwoLevelOneGridBoxes.get(j).add(k, new JCheckBox());
                        stageTwoLevelOneGridBoxes.get(j).get(k).setEnabled(false);
                    } else {
                        stageTwoLevelOneGridBoxes.get(j).add(k, new JCheckBox());
                        stageTwoLevelOneGridBoxes.get(j).get(k).setEnabled(true);
                    }
                } else {
                    stageTwoLevelOneGridBoxes.get(j).add(k, new JCheckBox());
                    if (suppressed) {
                        stageTwoLevelOneGridBoxes.get(j).get(k).setEnabled(false);
                    } else {
                        stageTwoLevelOneGridBoxes.get(j).get(k).setEnabled(true);
                    }
                }
                if (stageTwoLevelOneGridBoxesSelection[j][k] == true) {
                    stageTwoLevelOneGridBoxes.get(j).get(k).setSelected(true);
                }
                if (k == 0) {
                    stageTwoLevelOneGridBoxes.get(j).get(k).setSelected(true);
                    stageTwoLevelOneGridBoxes.get(j).get(k).setVisible(false);
                    constraints.gridx++;

                    if (isRandomScale) {
                        JLabel placeHolder1 = new JLabel(" ");
                        placeHolder1.setPreferredSize(new Dimension(60, 20));
                        stageTwoRegsGridLvl1.add(placeHolder1, constraints);
                    }
                }

                if (k == 3) {
                    if (suppressed) {
                        // do nothing
                    } else {
                        stageTwoRegsGridLvl1.add(stageTwoLevelOneGridBoxes.get(j).get(k), constraints);
                    }
                } else {
                    stageTwoRegsGridLvl1.add(stageTwoLevelOneGridBoxes.get(j).get(k), constraints);
                }

            }

            constraints.gridy++;

            separatorConstraint.gridy = separatorConstraint.gridy + 2;

            stageTwoRegsGridLvl1.add(new JSeparator(JSeparator.HORIZONTAL), separatorConstraint);
            constraints.gridy++;

            if (!isRandomScale) {
                stageTwoLevelOneGridBoxes.get(row).get(2).setVisible(false);
            }

            stageTwoLevelOneGridBoxes.get(j).get(1).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();
                    if (selected) {
                        scaleChecked = true;
                        if (randomChecked == true) {
                            if (!suppressed) {
                                stageTwoLevelOneGridBoxes.get(row).get(3).setEnabled(true);
                                stageTwoLevelOneGridBoxes.get(row).get(3).setSelected(false);
                            }
                        }

                    } else {
                        scaleChecked = false;
                        if (!suppressed) {
                            stageTwoLevelOneGridBoxes.get(row).get(3).setEnabled(false);
                            stageTwoLevelOneGridBoxes.get(row).get(3).setSelected(false);
                        }
                    }
                }
            });

            stageTwoLevelOneGridBoxes.get(j).get(2).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();

                    if (selected) {
                        randomChecked = true;

                        if (scaleChecked == true) {
                            if (!suppressed) {
                                stageTwoLevelOneGridBoxes.get(row).get(3).setEnabled(true);
                                stageTwoLevelOneGridBoxes.get(row).get(3).setSelected(false);
                            }
                        }

                    } else {
                        randomChecked = false;
                        if (!suppressed) {
                            stageTwoLevelOneGridBoxes.get(row).get(3).setEnabled(false);
                            stageTwoLevelOneGridBoxes.get(row).get(3).setSelected(false);
                        }

                    }
                }
            });

            stageTwoLevelOneGridBoxes.get(j).get(3).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();

                    randomChecked = false;
                    scaleChecked = false;

                    enbaleInteractionCheckBox.setEnabled(true);

                }
            });

        }

        stageTwoLevelOnePanel.removeAll();
        stageTwoLevelOnePanel.add(scrollpanel);
        stageTwoLevelOnePanel.revalidate();
        stageTwoLevelOnePanel.repaint();

        revalidate();

    }

    private void remove_last_checkbox_from_panel(javax.swing.JPanel panelName) {
        //Get the components in the panel
        Component[] componentList = panelName.getComponents();

        //Loop through the components
        for (Component c : componentList) {

            //Find the components you want to remove
            if (c instanceof JLabel) {
                JLabel c_label = (JLabel) c;
                String text = c_label.getText();
                if (text == "  ") {
                    //Remove it
                    panelName.remove(c);
                }

            }
        }

        //IMPORTANT
        panelName.revalidate();
        panelName.repaint();

    }

    private void update_trigger_enableInteractionCheckBox() {
        if (!enbaleInteractionCheckBox.isSelected()) {

            suppressed = true;

            jLabel18.setVisible(false);
            jLabel37.setVisible(false);

            // reset table
            updateStageTwoLevelOneGrid(stageTwoLevelOne);
            updateStageTwoLevelTwoGrid(stageTwoLevelTwo);

//            GridBagConstraints constraints = new GridBagConstraints();
//
//            constraints.anchor = GridBagConstraints.CENTER;
//            constraints.gridx = 10;
//            constraints.gridy = 0;
//            constraints.weightx = 1;
//            constraints.insets = new Insets(3, 5, 5, 0);
//            constraints.fill = GridBagConstraints.HORIZONTAL;
//
//            JLabel placeHolder = new JLabel("  ");
//            placeHolder.setPreferredSize(new Dimension(60, 20));
//            for (int p = 0; p < stageTwoLevelOneRegSize; p++) {
//                stageTwoLevelOneGridBoxes.get(p).get(3).setSelected(false);
//                stageTwoLevelOneGridBoxes.get(p).get(3).setEnabled(false);
//                stageTwoLevelOneGridBoxes.get(p).get(3).setVisible(false);
//                stageTwoRegsGridLvl1.add(placeHolder, constraints);
//                constraints.gridy++;
//                constraints.gridy++;
//            }
//            for (int p = 0; p < stageTwoLevelTwoRegSize; p++) {
//                stageTwoLevelTwoGridBoxes.get(p).get(3).setSelected(false);
//                stageTwoLevelTwoGridBoxes.get(p).get(3).setEnabled(false);
//                stageTwoLevelTwoGridBoxes.get(p).get(3).setVisible(false);
//                stageTwoRegsGridLvl2.add(placeHolder, constraints);
//                constraints.gridy++;
//                constraints.gridy++;
//            }
            try {
                defFile.setAdvancedStageTwoInteractionRegressorCount("-1");
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            }

            try {
                defFile.setStageTwoInteractionFields(new String[0]);
                defFile.setStageTwoInteractionLabels(new String[0]);

            } catch (Exception ex) {
                Logger.getLogger(mixregGUI.class
                        .getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            }

        } else {
            suppressed = false;

            try {
                defFile.setAdvancedStageTwoInteractionRegressorCount("0");
            } catch (Exception ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            }

            jLabel18.setVisible(true);
            jLabel37.setVisible(true);

            // reset table
            updateStageTwoLevelOneGrid(stageTwoLevelOne);
            updateStageTwoLevelTwoGrid(stageTwoLevelTwo);

            for (int p = 0; p < stageTwoLevelOneRegSize; p++) {
//                stageTwoLevelOneGridBoxes.get(p).get(3).setVisible(true);
                if (stageTwoLevelOneGridBoxes.get(p).get(1).isSelected() && stageTwoLevelOneGridBoxes.get(p).get(2).isSelected()) {
                    stageTwoLevelOneGridBoxes.get(p).get(3).setEnabled(true);
//                    remove_last_checkbox_from_panel(stageTwoRegsGridLvl1);

                }

            }

            for (int p = 0; p < stageTwoLevelTwoRegSize; p++) {
//                stageTwoLevelTwoGridBoxes.get(p).get(3).setVisible(true);
                if (stageTwoLevelTwoGridBoxes.get(p).get(1).isSelected() && stageTwoLevelTwoGridBoxes.get(p).get(2).isSelected()) {
                    stageTwoLevelTwoGridBoxes.get(p).get(3).setEnabled(true);
//                    remove_last_checkbox_from_panel(stageTwoRegsGridLvl2);
                }

            }

//            JScrollPane scrollpanel = new JScrollPane(stageTwoRegsGridLvl1);
//            stageTwoLevelOnePanel.removeAll();
//            stageTwoLevelOnePanel.add(scrollpanel);
//            stageTwoLevelOnePanel.revalidate();
//            stageTwoLevelOnePanel.repaint();
//
//            JScrollPane scrollpanel2 = new JScrollPane(stageTwoRegsGridLvl2);
//            stageTwoLevelTwoPanel.removeAll();
//            stageTwoLevelTwoPanel.add(scrollpanel2);
//            stageTwoLevelTwoPanel.revalidate();
//            stageTwoLevelTwoPanel.repaint();
        }

    }

    private void updateGuiView_trigger_dataview() {
        try {
            getDataFromCSV();
            printFileName();
            System.out.println("NEW MODEL DATA READ");
        } catch (IOException ex) {
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }
    }

    private void updateGuiView_trigger_dataview_stageTwo() {
        try {
            getDataFromCSV_stageTwo();
            printFileName_stageTwo();
            System.out.println("NEW stage 2 DATA READ");
        } catch (IOException ex) {
            Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }
    }

    void updateStageTwoLevelOneGrid(DefaultListModel<String> defaultListModel) {

        JScrollPane scrollpanel = new JScrollPane(stageTwoRegsGridLvl1);
        stageTwoLevelOneSelected = new ArrayList<String>();

        int regSize = defaultListModel.getSize();
        stageTwoLevelOneRegSize = regSize;

        stageTwoRegsGridLvl1.removeAll();

        if (suppressed) {
            jLabel18.setVisible(false);
        } else {
            jLabel18.setVisible(true);
        }

        stageTwoRegsGridLvl1.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.NORTHEAST;

        GridBagConstraints separatorConstraint = new GridBagConstraints();
        separatorConstraint.weightx = 1.0;
        separatorConstraint.fill = GridBagConstraints.HORIZONTAL;
        separatorConstraint.gridwidth = GridBagConstraints.REMAINDER;
        separatorConstraint.gridx = 0;

        constraints.insets = new Insets(3, 5, 5, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        separatorConstraint.insets = new Insets(0, 0, 0, 0);
        constraints.weightx = 1;

        stageTwoLevelOneGridBoxes = new ArrayList<ArrayList<JCheckBox>>();

        for (int j = 0; j < regSize; j++) {
            int row = j;
            constraints.gridx = 1;
            constraints.anchor = GridBagConstraints.FIRST_LINE_START;
            stageTwoLevelOneSelected.add(defaultListModel.getElementAt(j));
            JLabel variableText = new JLabel(stageTwoLevelOneSelected.get(j));
            variableText.setPreferredSize(new Dimension(60, 20));
            stageTwoRegsGridLvl1.add(variableText, constraints);

            stageTwoLevelOneGridBoxes.add(j, new ArrayList<JCheckBox>());

            for (int k = 0; k < 4; k++) {

                if (k == 1) {
                    constraints.gridx = constraints.gridx + 5;
                } else {
                    constraints.gridx++;
                }

                constraints.anchor = GridBagConstraints.CENTER;

                if (k == 3) {
                    if (suppressed) {
                        JLabel placeHolder3 = new JLabel(" ");
                        placeHolder3.setPreferredSize(new Dimension(20, 20));
                        stageTwoRegsGridLvl1.add(placeHolder3, constraints);
                        stageTwoLevelOneGridBoxes.get(j).add(k, new JCheckBox());
                        stageTwoLevelOneGridBoxes.get(j).get(k).setEnabled(false);
                    } else {
                        stageTwoLevelOneGridBoxes.get(j).add(k, new JCheckBox());
                        stageTwoLevelOneGridBoxes.get(j).get(k).setEnabled(true);
                    }
                } else {
                    stageTwoLevelOneGridBoxes.get(j).add(k, new JCheckBox());
                    if (suppressed) {
                        stageTwoLevelOneGridBoxes.get(j).get(k).setEnabled(false);
                    } else {
                        stageTwoLevelOneGridBoxes.get(j).get(k).setEnabled(true);
                    }
                }

                if (k == 0) {
                    stageTwoLevelOneGridBoxes.get(j).get(k).setSelected(true);
                    stageTwoLevelOneGridBoxes.get(j).get(k).setVisible(false);
                    constraints.gridx++;

                    if (isRandomScale) {
                        JLabel placeHolder1 = new JLabel(" ");
                        placeHolder1.setPreferredSize(new Dimension(60, 20));
                        stageTwoRegsGridLvl1.add(placeHolder1, constraints);
                    }
                }

                if (k == 3) {
                    if (suppressed) {
                        // do nothing
                    } else {
                        stageTwoRegsGridLvl1.add(stageTwoLevelOneGridBoxes.get(j).get(k), constraints);
                    }
                } else {
                    stageTwoRegsGridLvl1.add(stageTwoLevelOneGridBoxes.get(j).get(k), constraints);
                }

            }

            constraints.gridy++;

            separatorConstraint.gridy = separatorConstraint.gridy + 2;

            stageTwoRegsGridLvl1.add(new JSeparator(JSeparator.HORIZONTAL), separatorConstraint);
            constraints.gridy++;

            stageTwoLevelOneGridBoxes.get(row).get(1).setEnabled(true);
            stageTwoLevelOneGridBoxes.get(row).get(2).setEnabled(true);

            if (!isRandomScale) {
                stageTwoLevelOneGridBoxes.get(row).get(2).setVisible(false);
            }

            stageTwoLevelOneGridBoxes.get(j).get(1).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();
                    if (selected) {
                        scaleChecked = true;
                        if (randomChecked == true) {
                            if (!suppressed) {
                                stageTwoLevelOneGridBoxes.get(row).get(3).setEnabled(true);
                                stageTwoLevelOneGridBoxes.get(row).get(3).setSelected(false);
                            }
                        }

                    } else {
                        scaleChecked = false;
                        if (!suppressed) {
                            stageTwoLevelOneGridBoxes.get(row).get(3).setEnabled(false);
                            stageTwoLevelOneGridBoxes.get(row).get(3).setSelected(false);
                        }
                    }
                }
            });

            stageTwoLevelOneGridBoxes.get(j).get(2).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();

                    if (selected) {
                        randomChecked = true;

                        if (scaleChecked == true) {
                            if (!suppressed) {
                                stageTwoLevelOneGridBoxes.get(row).get(3).setEnabled(true);
                                stageTwoLevelOneGridBoxes.get(row).get(3).setSelected(false);
                            }
                        }

                    } else {
                        randomChecked = false;
                        if (!suppressed) {
                            stageTwoLevelOneGridBoxes.get(row).get(3).setEnabled(false);
                            stageTwoLevelOneGridBoxes.get(row).get(3).setSelected(false);
                        }

                    }
                }
            });

            stageTwoLevelOneGridBoxes.get(j).get(3).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    AbstractButton abstractButton = (AbstractButton) e.getSource();
                    boolean selected = abstractButton.getModel().isSelected();

                    randomChecked = false;
                    scaleChecked = false;

                    enbaleInteractionCheckBox.setEnabled(true);

                }
            });

        }

        stageTwoLevelOnePanel.removeAll();
        stageTwoLevelOnePanel.add(scrollpanel);
        stageTwoLevelOnePanel.revalidate();
        stageTwoLevelOnePanel.repaint();

    }

    public int getRandomScaleSelection() {
        int randomScaleSelection = MixLibrary.STAGE_ONE_SCALE_NO;

        if (getRandomScaleSelectionYes() == true) {
            randomScaleSelection = MixLibrary.STAGE_ONE_SCALE_YES;
        } else {
            randomScaleSelection = MixLibrary.STAGE_ONE_SCALE_NO;
        }

        return randomScaleSelection;
    }

    public int getStageTwoModelType() {
        int stageTwoModelType = MixLibrary.STAGE_TWO_MODEL_TYPE_SINGLE;

        if (getStageTwoSingleLevel() == true) {
            stageTwoModelType = MixLibrary.STAGE_TWO_MODEL_TYPE_SINGLE;
        } else if (getStageTwoMultiLevel() == true) {
            stageTwoModelType = MixLibrary.STAGE_TWO_MODEL_TYPE_MULTILEVEL;
        }

        return stageTwoModelType;

    }

    public int getStageTwoOutcomeType() {
        int stageTwoOutcomeType = MixLibrary.STAGE_TWO_OUTCOME_NONE;

        if (getStageTwoContinuousRadio() == true) {
            stageTwoOutcomeType = MixLibrary.STAGE_TWO_OUTCOME_NORMAL;
        } else if (getStageTwoDichotomousRadio() == true) {
            stageTwoOutcomeType = MixLibrary.STAGE_TWO_OUTCOME_ORDINAL;
        } else if (getCountRadio() == true) {
            stageTwoOutcomeType = MixLibrary.STAGE_TWO_OUTCOME_COUNT;
        } else if (getMultinomialRadio() == true) {
            stageTwoOutcomeType = MixLibrary.STAGE_TWO_OUTCOME_NOMINAL;
        }

        return stageTwoOutcomeType;

    }

    public boolean getStageTwoDataIncluded() {
        boolean included = false;
        if (getIncludeStageTwoDataYes() == true) {
            included = true;
        }
        return included;
    }

    private void updateGuiView_trigger_updateStage2Config() {
        boolean turnOn = true;

        if (isUpdateStage2ConfigClicked == true) {
            turnOn = false;
            randomScaleSelectionYes.setEnabled(turnOn);
            randomScaleSelectionNo.setEnabled(turnOn);
        }
        titleField.setEnabled(turnOn);
        missingValueAbsent.setEnabled(turnOn);
        missingValuePresent.setEnabled(turnOn);
        newModelMissingValueCode.setEnabled(turnOn);
        stageOneContinuousRadio.setEnabled(turnOn);
        stageOneDichotomousRadio.setEnabled(turnOn);
        stageOneOrdinalRadio.setEnabled(turnOn);
        StageOneProbitRadio.setEnabled(turnOn);
        StageOneLogisticRadio.setEnabled(turnOn);
        oneRLERadio.setEnabled(turnOn);
        moreThanOneRLERadio.setEnabled(turnOn);

        guiStatesSaveButtonModalConfig.setVisible(turnOn);
        guiStatesSaveButtonStageOne.setVisible(turnOn);
        guiStatesSaveButtonStageTwo.setVisible(turnOn);
    }

    private boolean checkTabExistinJTabbedPane(JTabbedPane tabPane, String view_Model) {
        int count = tabPane.getTabCount();
        boolean exist = false;
        for (int i = 0; i < count; i++) {
            if (view_Model == tabPane.getTitleAt(i)) {
                exist = true;
            }
        }

        return exist;
    }

    private void updateGuiView_trigger_NewModelSubmit_TabChange() {
        // add tabs
//        stageOneTabs.insertTab("Postestimation", null, jPanel14, null, 1);

        if (!checkTabExistinJTabbedPane(stageOneTabs, "Stage 1 Configuration")) {
            stageOneTabs.insertTab("Stage 1 Configuration", null, jPanel1, null, 1);
        }

        int stage2TabIdx = stageOneTabs.indexOfTab("Stage 1 Configuration");
        if (!checkTabExistinJTabbedPane(stageOneTabs, "Stage 2 Configuration") && (includeStageTwoNo() == false)) {
            stageOneTabs.insertTab("Stage 2 Configuration", null, jPanel12, null, stage2TabIdx + 1);
            stageOneTabs.setEnabledAt(stage2TabIdx + 1, false);
        }

        // remove tabs
        if (checkTabExistinJTabbedPane(stageOneTabs, "Stage 2 Configuration") && (includeStageTwoNo() == true)) {
            stageOneTabs.remove(jPanel12);
        }
        if (checkTabExistinJTabbedPane(stageOneTabs, "Stage 2 Results") && (includeStageTwoNo() == true)) {
            stageOneTabs.remove(jPanel4);
        }
    }

    private void updateGuiView_trigger_browse(boolean turnOn) {
        titleViewLabel.setVisible(turnOn);
        titleField.setVisible(turnOn);

        DatasetLabel.setVisible(turnOn);
        missingViewLabel.setVisible(turnOn);
        missingValueAbsent.setVisible(turnOn);
        missingValuePresent.setVisible(turnOn);
        datasetHelpButton.setVisible(turnOn);
        datasetMissingValuesHelpButton.setVisible(turnOn);

        if (turnOn) {
            hiddenBigIconLabel.setIcon(null);
        }

        guiStatesSaveButtonModalConfig.setVisible(turnOn);
        newModel_resetButton.setVisible(turnOn);

    }

    private void updateGuiView_trigger_missingvaluefield() {

        if (missingValuePresent.isSelected()) {
            missingCodeViewLabel.setVisible(true);
            newModelMissingValueCode.setVisible(true);
//            newModelMissingValueCode.setText("-999");
            newModelMissingValueCode.selectAll();

            stageOneOutcomeViewLabel.setVisible(true);
            stageOneContinuousRadio.setVisible(true);
            stageOneDichotomousRadio.setVisible(true);
            stageOneOrdinalRadio.setVisible(true);
            stageOneOutcomeHelpButton.setVisible(true);

//            StageOneModelTypeLabel.setVisible(true);
//            StageOneProbitRadio.setVisible(true);
//            StageOneLogisticRadio.setVisible(true);
            rleViewLabel.setVisible(true);
            oneRLERadio.setVisible(true);
            moreThanOneRLERadio.setVisible(true);
            stageOneRLEHelpButton.setVisible(true);
            stageOneRSHelpButton.setVisible(true);

            randomScaleViewLabel.setVisible(true);
            randomScaleSelectionYes.setVisible(true);
            randomScaleSelectionNo.setVisible(true);

            jSeparator16.setVisible(true);
            jSeparator12.setVisible(true);

            stageOneModelGiantLabel.setVisible(true);
        } else if (missingValueAbsent.isSelected()) {
            missingCodeViewLabel.setVisible(false);
            newModelMissingValueCode.setVisible(false);
            newModelMissingValueCode.setText("");

            stageOneOutcomeViewLabel.setVisible(true);
            stageOneContinuousRadio.setVisible(true);
            stageOneDichotomousRadio.setVisible(true);
            stageOneOrdinalRadio.setVisible(true);
            stageOneOutcomeHelpButton.setVisible(true);

//            StageOneModelTypeLabel.setVisible(true);
//            StageOneProbitRadio.setVisible(true);
//            StageOneLogisticRadio.setVisible(true);
            rleViewLabel.setVisible(true);
            oneRLERadio.setVisible(true);
            moreThanOneRLERadio.setVisible(true);
            stageOneRLEHelpButton.setVisible(true);
            stageOneRSHelpButton.setVisible(true);

            randomScaleViewLabel.setVisible(true);
            randomScaleSelectionYes.setVisible(true);
            randomScaleSelectionNo.setVisible(true);

            jSeparator16.setVisible(true);
            jSeparator12.setVisible(true);

            stageOneModelGiantLabel.setVisible(true);
        }

    }

    private void updateGuiView_trigger_stageOneConfig() {

        if (getStageOneOutcome() == MixLibrary.STAGE_ONE_OUTCOME_MIXOR) {
            if ((stageOneOutcomeGroup.getSelection() != null) && (buttonGroup5.getSelection() != null) && (buttonGroup2.getSelection() != null) && (randomScaleSelectionGroup.getSelection() != null)) {
                includeStageTwoLabel.setVisible(true);
                includeStageTwoYes.setVisible(true);
                includeStageTwoNo.setVisible(true);
                stageTwoDescription.setVisible(true);
                stageTwoModelGiantLabel.setVisible(true);
            }
        } else {
            if ((stageOneOutcomeGroup.getSelection() != null) && (buttonGroup2.getSelection() != null) && (randomScaleSelectionGroup.getSelection() != null)) {
                includeStageTwoLabel.setVisible(true);
                includeStageTwoYes.setVisible(true);
                includeStageTwoNo.setVisible(true);
                stageTwoDescription.setVisible(true);
                stageTwoModelGiantLabel.setVisible(true);
            }

        }

    }

    private void showHiddenBigIconLabel(boolean turnOn) {
        if (turnOn) {
            hiddenBigIconLabel.setIcon(bigIcon);
        } else {
            hiddenBigIconLabel.setIcon(null);

        }
    }

    private boolean isNumeric(String thiscellvalue) {
        return thiscellvalue.matches("-?\\d+(\\.\\d+)?");
    }

    private void clearStageTwoLevelOneGrid() {
        stageTwoLevelOnePanel.removeAll();
        stageTwoLevelOnePanel.revalidate();
        stageTwoLevelOnePanel.repaint();

        stage_2_regs.stageTwoLevelOne.clear();
        updateStageTwoLevelOneGrid(stage_2_regs.stageTwoLevelOne);
    }

    private void clearStageTwoLevelTwoGrid() {
        stageTwoLevelTwoPanel.removeAll();
        stageTwoLevelTwoPanel.revalidate();
        stageTwoLevelTwoPanel.repaint();

        stage_2_regs.stageTwoLevelTwo.clear();
        updateStageTwoLevelTwoGrid(stage_2_regs.stageTwoLevelTwo);
    }

    private void updateGuiView_trigger_stageOneOutcome() {
        if (getStageOneDichotomousRadio()) {
            randomScaleSelectionYes.setEnabled(false);
            randomScaleSelectionYes.setSelected(false);
            randomScaleSelectionNo.setEnabled(false);
            randomScaleSelectionNo.setSelected(true);
        }
        if (getStageOneContinuousRadio() || getStageOneOrdinalRadio()) {
            randomScaleSelectionYes.setEnabled(true);
            randomScaleSelectionYes.setSelected(false);
            randomScaleSelectionNo.setEnabled(true);
            randomScaleSelectionNo.setSelected(false);
        }
        if (getStageOneDichotomousRadio() || getStageOneOrdinalRadio()) {
            StageOneModelTypeLabel.setVisible(true);
            StageOneProbitRadio.setVisible(true);
            StageOneLogisticRadio.setVisible(true);
        }
        if (getStageOneContinuousRadio()) {
            StageOneModelTypeLabel.setVisible(false);
            StageOneProbitRadio.setVisible(false);
            StageOneLogisticRadio.setVisible(false);
        }
    }

    private ArrayList<Double> getStageOneOrTwoCategoricalOutcomeUniqueList(Integer stageNum) {
        ArrayList<String> ColumnsCustom = new ArrayList<>();
        ArrayList<String> UniqueList = new ArrayList<>();

        String dataFileName = getDataFileName(stageNum);
        File file = new File(dataFileName);
        //        //first get the column
        BufferedReader br = null;
        String line = "";
        String commaSplitter = ",";
        //
        ArrayList<Double> UniqueValues = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(dataFileName));
            line = br.readLine(); //consumes the first row
            int index;
            switch (stageNum) {
                case 1:
                    index = StageOneOutcomeCombo.getSelectedIndex();
                    break;
                case 2:
                    index = StageTwoOutcomeCombo.getSelectedIndex();
                    break;
                default:
                    index = StageOneOutcomeCombo.getSelectedIndex();
                    break;
            }
            while ((line = br.readLine()) != null) {
                String[] Columns = line.split(commaSplitter);

//                int index = StageTwoOutcomeCombo.getSelectedIndex();
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
                } else if (ColumnsCustom.get(x).equals(defFile.getAdvancedMissingValueCode()) && !ColumnsCustom.get(x).equals("0")) { //compare if the category is a missing value, then don't consider it as a category
                    //do nothing

                } else {
                    UniqueList.add(ColumnsCustom.get(x));
                }

            }

            //sort UniqueList First
            for (int x = 0; x < UniqueList.size(); x++) {
//                UniqueValues.add(Integer.valueOf(UniqueList.get(x)));
                UniqueValues.add(Double.valueOf(UniqueList.get(x)));

            }
            Collections.sort(UniqueValues);

        } catch (FileNotFoundException e) {
            SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
            e.printStackTrace();
            Logger.getLogger(getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        } catch (IOException e) {
            SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
                    e.printStackTrace();
                }
            }

        }
        return UniqueValues;

    }

    private static String listToString(List<Double> numbers) {
        StringBuilder buffer = new StringBuilder();
        for (Double nextNumber : numbers) {
            buffer.append(nextNumber).append(" ");
        }
        return buffer.toString();
    }

    private void updateGuiView_trigger_IncludeStageTwoData() {
        if (includeStageTwoDataNo.isSelected()) {
//            DataFileStageTwoLabel.setEnabled(false);
            filePath_stageTwo.setEnabled(false);
            fileBrowseButtonStageTwoData.setEnabled(false);
            if (checkTabExistinJTabbedPane(stageOneTabs, "View Stage 2 Data")) {
                stageOneTabs.remove(jPanel16);
            }
            if (checkTabExistinJTabbedPane(stageOneTabs, "View Stage 1 Data")) {
                int viewDataStageOneIdx = stageOneTabs.indexOfTab("View Stage 1 Data");
                stageOneTabs.setTitleAt(viewDataStageOneIdx, "View Data");
            }
            filePath_stageTwo.setText("");
        } else if (includeStageTwoDataYes.isSelected()) {
//            DataFileStageTwoLabel.setVisible(true);
            DataFileStageTwoLabel.setEnabled(true);
//            filePath_stageTwo.setVisible(true);
            filePath_stageTwo.setEnabled(true);
//            fileBrowseButtonStageTwoData.setVisible(true);
            fileBrowseButtonStageTwoData.setEnabled(true);

        }
    }

    private void importDataSetStageTwo() {
        //only csv files are displayed in filechooser 
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Data files", "csv");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // File file = fileChooser.getSelectedFile();
            // What to do with the file, e.g. display it in a TextArea
            //textarea.read( new FileReader( file.getAbsolutePath() ), null );
            //Select file from the file object
            file_stageTwo = fileChooser.getSelectedFile();
            //get file path to display on the text box
            String fileName = file_stageTwo.getAbsolutePath();

            dataFileNameRef_stageTwo = fileName;

            // check if stage 2 dataset same as stage 1 dataset
            if (dataFileNameRef.equals(dataFileNameRef_stageTwo)) {
                JOptionPane.showMessageDialog(this, "Please use a different dataset from the initial dataset!",
                        "Caution!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            filePath_stageTwo.setText("");

            try {
                getDataFromCSV_stageTwo();
                printFileName_stageTwo();
                System.out.println("NEW STAGE TWO DATA READ");
                if (validDataset_stageTwo) {
                    filePath_stageTwo.setText(fileName);
                    int viewDataStageOneIdx = stageOneTabs.indexOfTab("View Data");
                    stageOneTabs.setTitleAt(viewDataStageOneIdx, "View Stage 1 Data");
                    int helpTabIdx = stageOneTabs.indexOfTab("Help");
                    stageOneTabs.insertTab("View Stage 2 Data", null, jPanel16, null, helpTabIdx);

                }
            } catch (IOException ex) {
                Logger.getLogger(getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

        } else {
            System.out.println("File access to stage 2 dataset cancelled by user.");
            try {
                System.out.println(file_stageTwo.getAbsolutePath());
            } catch (NullPointerException ex) {
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            }
        }
    }

    private void getDataFromCSV_stageTwo() throws FileNotFoundException, IOException {

        Object[] columnnames;

        CSVReader CSVFileReader = new CSVReader(new FileReader(file_stageTwo));
        List myEntries = CSVFileReader.readAll();
        columnnames = (String[]) myEntries.get(0);

        // validation: check if dataset name include space
        String filename = file_stageTwo.getName();
        if (filename.contains(" ")) {
            validDataset_stageTwo = false;
            JOptionPane.showMessageDialog(null, "The filename of .csv file can not include space. Please try to use underscore instead.",
                    "Dataset Naming Error", JOptionPane.INFORMATION_MESSAGE);
        } else {
            validDataset_stageTwo = true;
        }

        if (validDataset_stageTwo) {
            // validation1: check first row should be column names (every column name contains letters)
            for (int i = 0; i < columnnames.length; i++) {
                String colname = (String) columnnames[i];
                // check if colname contains just numbers
                if (colname.matches("[0-9]+")) {
                    validDataset_stageTwo = false;
                    JOptionPane.showMessageDialog(null, "The first row of .csv file should be column names in letters.",
                            "Stage Two Dataset Error", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                validDataset_stageTwo = true;
            }
        }

        DefaultTableModel tableModel = null;

        // validation2: no blanks or letters in cells
        outerloop:
        if (validDataset_stageTwo) {
            tableModel = new DefaultTableModel(columnnames, myEntries.size() - 1) {

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
                        if (thiscellvalue.length() == 0) {
                            validDataset_stageTwo = false;
                            JOptionPane.showMessageDialog(null, "The .csv file should contain no blanks. Please insert a number for all missing values (e.g., -999)."
                                    + "\n" + "Missing value codes should be numeric only.",
                                    "Dataset Error", JOptionPane.INFORMATION_MESSAGE);
                            SystemLogger.LOGGER.log(Level.SEVERE, "Dataset Blanks Error", SystemLogger.getLineNum());
                            break outerloop;
                        }
                        if (!isNumeric(thiscellvalue)) {
                            validDataset_stageTwo = false;
                            JOptionPane.showMessageDialog(null, "The .csv file should contain only numeric values, except for the headers in the first row."
                                    + "\n" + "Missing value codes should be numeric only.",
                                    "Dataset Error", JOptionPane.INFORMATION_MESSAGE);
                            SystemLogger.LOGGER.log(Level.SEVERE, "Dataset Non-numeric Values Error", SystemLogger.getLineNum());
                            break outerloop;
                        }
                        tableModel.setValueAt(thiscellvalue, x - 1, columnnumber);
                        columnnumber++;
                    }
                }
            }

        }

        if (validDataset_stageTwo = true) {
            dataTable_stageTwo.setModel(tableModel);
            dataTable_stageTwo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            dataTable_stageTwo.setAutoscrolls(true);
        }

    }

    private void printFileName_stageTwo() {
        String path = file_stageTwo.getName();

        printedFileName_stageTwo.setText(path);
    }

    private void update_trigger_stageTwoIDCombo() {
        boolean turnOn = false;
        if (includeStageTwoDataYes.isSelected() == true) {
            turnOn = true;
        }
        jLabel6.setVisible(turnOn);
        IDStageTwoVariableCombo.setVisible(turnOn);
    }

    private void update_trigger_stageTwoOutcomeCat() {
        outComeType = getStageTwoOutcomeType();
        if ((outComeType == MixLibrary.STAGE_TWO_OUTCOME_ORDINAL) || (outComeType == MixLibrary.STAGE_TWO_OUTCOME_NOMINAL)) {

            outcomeCatButton.setEnabled(true);
            outcomeCatButton.setVisible(true);
            //outComeText.setVisible(true);
            outCategoryDisplay.setVisible(true);
            //outComeText.setEditable(false);
            outCategoryDisplay.setEnabled(true);
            System.out.println("outCatButton Enabled: " + String.valueOf(isOutcomeContinous()));
            jPanel5.setVisible(true);
            jPanel5.setEnabled(true);

        } else {
            outcomeCatButton.setVisible(false);
            outcomeCatButton.setEnabled(false);
            jPanel5.setVisible(false);
            jPanel5.setEnabled(false);
        }
    }

    private boolean validate_stageTwoDataset_ID() {
        boolean validation_pass = true;
        if ((includeStageTwoDataYes.isSelected() == true) && (!dataFileNameRef_stageTwo.isEmpty())) {
            ArrayList<String> uniqueListIDStageOne = get_unique_list_from_dataset_column(1);
            ArrayList<String> uniqueListIDStageTwo = get_unique_list_from_dataset_column(2);
            // step1: check number of unique IDs
            int uniqueListIDStageOneSize = uniqueListIDStageOne.size();
            int uniqueListIDStageTwoSize = uniqueListIDStageTwo.size();

            if (uniqueListIDStageOneSize > uniqueListIDStageTwoSize) {
                // error message
                JOptionPane.showMessageDialog(this, "Stage 1 dataset has more unique IDs than stage 2 dataset. Please upload two datasets with the same amount of unique IDs.",
                        "Error", JOptionPane.INFORMATION_MESSAGE);
                validation_pass = false;
                SystemLogger.LOGGER.log(Level.SEVERE, "Stage 1 dataset has more unique IDs than stage 2 dataset.", SystemLogger.getLineNum());
            } else if (uniqueListIDStageOneSize < uniqueListIDStageTwoSize) {
                // error message
                JOptionPane.showMessageDialog(this, "Stage 2 dataset has more unique IDs than stage 1 dataset. Please upload two datasets with the same amount of unique IDs.",
                        "Error", JOptionPane.INFORMATION_MESSAGE);
                validation_pass = false;
                SystemLogger.LOGGER.log(Level.SEVERE, "Stage 2 dataset has more unique IDs than stage 1 dataset.", SystemLogger.getLineNum());
            } else {
                // if the two ID unique lists have the same length
                // step2: check order and elements
                for (int x = 0; x < uniqueListIDStageOneSize; x++) {
                    if (!uniqueListIDStageOne.get(x).equals(uniqueListIDStageTwo.get(x))) {
                        if (uniqueListIDStageTwo.contains(uniqueListIDStageOne.get(x))) {
                            // order problem message
                            JOptionPane.showMessageDialog(this, "Stage 1 and stage 2 datasets have different order of IDs. Please upload two datasets with the same order of IDs.",
                                    "Error", JOptionPane.INFORMATION_MESSAGE);
                            SystemLogger.LOGGER.log(Level.SEVERE, "Stage 1 and stage 2 datasets have different order of IDs.", SystemLogger.getLineNum());
                        } else {
                            // element problem message
                            JOptionPane.showMessageDialog(this, "Stage 1 dataset has ID that stage 2 datasets doesn't have. Please upload two datasets with the same unique IDs.",
                                    "Error", JOptionPane.INFORMATION_MESSAGE);
                            SystemLogger.LOGGER.log(Level.SEVERE, "Stage 1 dataset has ID that stage 2 datasets doesn't have.", SystemLogger.getLineNum());
                        }
                        validation_pass = false;
                        break;
                    } else {
                        // do nothing
                    }
                }
            }

        }

        return validation_pass;
    }

    private ArrayList<String> get_unique_list_from_dataset_column(int datasetNum) {
        ArrayList<String> ColumnsCustom = new ArrayList<>();
        ArrayList<String> UniqueList = new ArrayList<>();

        String dataFileName = getDataFileName(datasetNum);

        //        //first get the column
        BufferedReader br = null;
        String line = "";
        String commaSplitter = ",";

        try {
            br = new BufferedReader(new FileReader(dataFileName));
            line = br.readLine(); //consumes the first row
            // determine the ID variable position
            int IDVaraibleIndex;
            switch (datasetNum) {
                case 1:
                    IDVaraibleIndex = IDvariableCombo.getSelectedIndex();
                    break;
                case 2:
                    IDVaraibleIndex = IDStageTwoVariableCombo.getSelectedIndex();
                    break;
                default:
                    IDVaraibleIndex = IDStageTwoVariableCombo.getSelectedIndex();
            }

            while ((line = br.readLine()) != null) {
                String[] Columns = line.split(commaSplitter);
                ColumnsCustom.add(Columns[IDVaraibleIndex]);

            }

            //count the unique ones
            for (int x = 0; x < ColumnsCustom.size(); x++) {
                if (UniqueList.contains(ColumnsCustom.get(x))) {
                    //do nothing
                } else {
                    UniqueList.add(ColumnsCustom.get(x));
                }

            }

        } catch (FileNotFoundException e) {
            SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
            e.printStackTrace();
            Logger.getLogger(getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        } catch (IOException e) {
            SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
                    e.printStackTrace();
                }
            }
        }

        return UniqueList;
    }

    private void createNewLogger(File csvFile) throws IOException {
        String absolutePath = csvFile.getAbsolutePath();
        String folderPath = FilenameUtils.getFullPath(absolutePath);
        logFilePath = folderPath + "MixWILD Logs/" + sessionFolderNameBuilt + "/";
        File dirGen = new File(logFilePath);
        // check if file exists
        boolean dirExist = Files.exists(dirGen.toPath());
        if (!dirExist) {
            boolean genTrue = dirGen.mkdirs();
            if (!genTrue) {
                throw new IOException("Cannot generate log directory, please check folder permissions");
            }

        }

        // create logger after session folder created
//        SystemLogger.logPath = FilenameUtils.getFullPath(dataFileNameRef) + sessionFolderName;
        SystemLogger.logPath = logFilePath;
        logger = new SystemLogger();
    }

    private void loadLogger(String logFile) {
        SystemLogger.logPath = logFile;
        logger = new SystemLogger();
    }

    private void updateGuiView_trigger_randomScaleSelection() {
        if (!isRandomScale) {
            jLabel17.setVisible(false);
            jLabel36.setVisible(false);
            jLabel18.setVisible(false);
            jLabel37.setVisible(false);
            enbaleInteractionCheckBox.setVisible(false);
        } else {
            // do nothing
        }
    }

}
