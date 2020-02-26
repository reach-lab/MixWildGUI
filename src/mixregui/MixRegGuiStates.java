/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mixregui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

/**
 *
 * @author jixin
 */
public class MixRegGuiStates {

    // gui states of New Model Configuration Tab
    public String filepath;
    public String title;
    public boolean missingValuePresent;
    public boolean missingValueAbsent;
    public String newModelMissingValueCode;
    public boolean stageOneContinuousRadio;
    public boolean stageOneDichotomousRadio;
    public boolean stageOneOrdinalRadio;
    public boolean oneRLERadio;
    public boolean moreThanOneRLERadio;
    public boolean randomScaleSelectionYes;
    public boolean randomScaleSelectionNo;
    public boolean includeStageTwoYes;
    public boolean includeStageTwoNo;
    public boolean stageTwoSingleLevel;
    public boolean stageTwoMultiLevel;
    public boolean continuousRadio;
    public boolean dichotomousRadio;
    public boolean countRadio;
    public boolean multinomialRadio;
    public String seedTextBox;
    public boolean isNewModalConfigSubmitted;

    // gui states of Stage One
    public boolean isStageOneSubmitted;
    public boolean isStageTwoSubmitted;
    public int IDpos;
    public int stageOnePos;
    public int stageTwoPos;
    public int stageOneClicked;
    public boolean addStageOneCHecked;
    public DefaultListModel<String> varList;
    public DefaultListModel<String> levelOneList;
    public DefaultListModel<String> levelTwoList;
    public String varListString;
    public String levelOneListString;
    public String levelTwoListString;
    public boolean isStageOneRegSubmitClicked;
    public boolean[][] StageOneLevelOneBoxesSelection;
    public boolean[][] disaggVarianceBoxesSelection;
    public boolean[][] StageOneLevelTwoBoxesSelection;

    // advanced options
    public boolean meanSubmodelCheckBox;
    public boolean BSVarianceCheckBox;
    public boolean WSVarianceCheckBox;
    public boolean centerRegressorsCheckBox;
    public boolean discardSubjectsCheckBox;
    public boolean resampleCheckBox;
    public boolean adaptiveQuadritureCheckBox;
    public boolean run32BitCheckBox;
    public String convergenceCriteria;
    public int quadriturePoints;
    public int maximumIterations;
    public double ridgeSpinner;
    public int resampleSpinner;

    // Association
    public boolean NoAssociationRadio;
    public boolean LinearAssociationRadio;
    public boolean QuadraticAssociationRadio;

    // gui states of Stage Two
    public DefaultListModel<String> stageTwoListModel;
    public DefaultListModel<String> stageTwoLevelTwo;
    public String stageTwoListModelString;
    public String stageTwoLevelTwoString;
    public boolean isStageTwoSubmitClicked;
    public boolean[][] stageTwoGridBoxesSelection;
    public boolean suppressIntCheckBox;

    public ArrayList<String> levelTwoSelected;

    public String sessionFolderName;

    // init default
    MixRegGuiStates() {
    }

    // init a snapshot of mixregGui states
    MixRegGuiStates(mixregGUI mxr, advancedOptions ao) {
//        System.out.print(mxr.file.getAbsolutePath());
        this.filepath = mxr.file.getAbsolutePath();
        this.title = mxr.getTitle();
        this.missingValuePresent = mxr.getMissingValuePresent();
        this.missingValueAbsent = mxr.getMissingValueAbsent();
        this.newModelMissingValueCode = mxr.getNewModelMissingValueCode();
        this.stageOneContinuousRadio = mxr.getStageOneContinuousRadio();
        this.stageOneDichotomousRadio = mxr.getStageOneDichotomousRadio();
        this.stageOneOrdinalRadio = mxr.getStageOneOrdinalRadio();
        this.oneRLERadio = mxr.getOneRLERadio();
        this.moreThanOneRLERadio = mxr.getMoreThanOneRLERadio();
        this.randomScaleSelectionYes = mxr.getRandomScaleSelectionYes();
        this.randomScaleSelectionNo = mxr.getRandomScaleSelectionNo();
        this.includeStageTwoYes = mxr.getIncludeStageTwoYes();
        this.includeStageTwoNo = mxr.getIncludeStageTwoNo();
        this.stageTwoSingleLevel = mxr.getStageTwoSingleLevel();
        this.stageTwoMultiLevel = mxr.getStageTwoMultiLevel();
        this.continuousRadio = mxr.getContinuousRadio();
        this.dichotomousRadio = mxr.getDichotomousRadio();
        this.countRadio = mxr.getCountRadio();
        this.multinomialRadio = mxr.getMultinomialRadio();
        this.seedTextBox = mxr.getSeedTextBox();
        this.isNewModalConfigSubmitted = mxr.isNewModalConfigSubmitted;
        this.IDpos = mixregGUI.IDpos;
        this.stageOnePos = mixregGUI.stageOnePos;
        this.stageTwoPos = mixregGUI.stageTwoPos;
        this.stageOneClicked = mxr.stageOneClicked;

        this.varListString = saveDefaultListModel(stageOneRegs.varList);
        this.levelOneListString = saveDefaultListModel(stageOneRegs.levelOneList);
        this.levelTwoListString = saveDefaultListModel(stageOneRegs.levelTwoList);

        this.addStageOneCHecked = mxr.addStageOneCHecked;
        this.isStageOneRegSubmitClicked = stageOneRegs.isSubmitClicked;

        this.StageOneLevelOneBoxesSelection = getSelectionBoxes(mxr.levelOneBoxes);
        this.disaggVarianceBoxesSelection = getSelectionBoxes(mxr.disaggVarianceBoxes);
        this.StageOneLevelTwoBoxesSelection = getSelectionBoxes(mxr.levelTwoBoxes);

        this.meanSubmodelCheckBox = ao.isMeanSubmodelCheckBoxChecked();
        this.BSVarianceCheckBox = ao.isBSVarianceCheckBoxChecked();
        this.WSVarianceCheckBox = ao.isWSVarianceCheckBoxChecked();
        this.centerRegressorsCheckBox = ao.isCenterRegressorsCheckBoxChecked();
        this.discardSubjectsCheckBox = ao.isDiscardSubjectsCheckBoxChecked();
        this.resampleCheckBox = ao.isResampleCheckBoxChecked();
        this.adaptiveQuadritureCheckBox = ao.isAdaptiveQuadritureCheckBoxChecked();
        this.run32BitCheckBox = ao.isRun32BitChecked();
        this.convergenceCriteria = ao.getConvergenceCriteria();
        this.quadriturePoints = ao.getQuadriturePoints();
        this.maximumIterations = ao.getMaximumIterations();
        this.ridgeSpinner = ao.getRidge();
        this.resampleSpinner = ao.getResampleSpinner();
        this.NoAssociationRadio = mxr.getNoAssociationRadio();
        this.LinearAssociationRadio = mxr.getLinearAssociationRadio();
        this.QuadraticAssociationRadio = mxr.getQuadraticAssociationRadio();
        this.isStageOneSubmitted = mxr.isStageOneSubmitted;
        this.isStageTwoSubmitted = mxr.isStageTwoSubmitted;

        this.stageTwoListModelString = saveDefaultListModel(stageTwoRegs.stageTwoListModel);
        this.stageTwoLevelTwoString = saveDefaultListModel(stageTwoRegs.stageTwoLevelTwo);
        this.isStageTwoSubmitClicked = stageTwoRegs.isStageTwoSubmitClicked;
        this.stageTwoGridBoxesSelection = getSelectionBoxes(mxr.stageTwoLevelTwoGridBoxes);
        this.suppressIntCheckBox = mxr.getSuppressIntCheckBox();
        this.levelTwoSelected = mxr.levelTwoSelected;
        this.sessionFolderName = mxr.sessionFolderName;
    }

    public void writeAllStates(mixregGUI mxr) {
        HashMap<String, StateObject> hmapStates = this.createStatesHashMap();

        // user open filechooser and select save path
        JFileChooser fileChooser_save = new JFileChooser();
        fileChooser_save.setSelectedFile(new File("configuration.mw"));
        int option = fileChooser_save.showSaveDialog(mxr);
        if (option == JFileChooser.APPROVE_OPTION) {
            File save_filename = fileChooser_save.getSelectedFile();

            try {
                FileOutputStream fos = new FileOutputStream(save_filename);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(hmapStates);
                oos.close();
                fos.close();
                System.out.printf("Gui states are saved in configuration.mw");
            } catch (IOException ioe) {
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ioe);
                SystemLogger.LOGGER.log(Level.SEVERE, ioe.toString() + "{0}", SystemLogger.getLineNum());
            }
        }
    }

    public void readAllStates(mixregGUI mxr) throws NullPointerException {
        HashMap<String, StateObject> hmapStates = null;

        JFileChooser fileChooser_load = new JFileChooser();
        fileChooser_load.setSelectedFile(new File("configuration.mw"));
        int option = fileChooser_load.showOpenDialog(mxr);
        if (option == JFileChooser.APPROVE_OPTION) {
            File load_filename = fileChooser_load.getSelectedFile();
            try {
                FileInputStream fis = new FileInputStream(load_filename);
                ObjectInputStream ois = new ObjectInputStream(fis);
                hmapStates = (HashMap) ois.readObject();
                ois.close();
                fis.close();
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found");
                SystemLogger.LOGGER.log(Level.SEVERE, c.toString() + "{0}", SystemLogger.getLineNum());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            } catch (IOException ex) {
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
                SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            }
        }

        // gui states of New Model Configuration Tab
        filepath = hmapStates.get("filepath").getString();
        title = hmapStates.get("titleField").getString();
        missingValuePresent = hmapStates.get("missingValuePresent").getBoolean();
        missingValueAbsent = hmapStates.get("missingValueAbsent").getBoolean();
        newModelMissingValueCode = hmapStates.get("newModelMissingValueCode").getString();
        stageOneContinuousRadio = hmapStates.get("stageOneContinuousRadio").getBoolean();
        stageOneDichotomousRadio = hmapStates.get("stageOneDichotomousRadio").getBoolean();
        stageOneOrdinalRadio = hmapStates.get("stageOneOrdinalRadio").getBoolean();
        oneRLERadio = hmapStates.get("oneRLERadio").getBoolean();
        moreThanOneRLERadio = hmapStates.get("moreThanOneRLERadio").getBoolean();
        randomScaleSelectionYes = hmapStates.get("randomScaleSelectionYes").getBoolean();
        randomScaleSelectionNo = hmapStates.get("randomScaleSelectionNo").getBoolean();
        includeStageTwoYes = hmapStates.get("includeStageTwoYes").getBoolean();
        includeStageTwoNo = hmapStates.get("includeStageTwoNo").getBoolean();
        stageTwoSingleLevel = hmapStates.get("stageTwoSingleLevel").getBoolean();
        stageTwoMultiLevel = hmapStates.get("stageTwoMultiLevel").getBoolean();
        continuousRadio = hmapStates.get("continuousRadio").getBoolean();
        dichotomousRadio = hmapStates.get("dichotomousRadio").getBoolean();
        countRadio = hmapStates.get("countRadio").getBoolean();
        multinomialRadio = hmapStates.get("multinomialRadio").getBoolean();
        seedTextBox = hmapStates.get("seedTextBox").getString();
        isNewModalConfigSubmitted = hmapStates.get("isNewModalConfigSubmitted").getBoolean();
        IDpos = hmapStates.get("IDpos").getInt();
        stageOnePos = hmapStates.get("stageOnePos").getInt();
        stageTwoPos = hmapStates.get("stageTwoPos").getInt();
        stageOneClicked = hmapStates.get("stageOneClicked").getInt();
        addStageOneCHecked = hmapStates.get("addStageOneCHecked").getBoolean();

        varListString = hmapStates.get("varList").getString();
        varList = loadDefaultListModel(varListString);
        levelOneListString = hmapStates.get("levelOneList").getString();
        levelOneList = loadDefaultListModel(levelOneListString);
        levelTwoListString = hmapStates.get("levelTwoList").getString();
        levelTwoList = loadDefaultListModel(levelTwoListString);

        isStageOneRegSubmitClicked = hmapStates.get("isStageOneRegSubmitClicked").getBoolean();
//        levelOneBoxes = hmapStates.get("levelOneBoxes").getBox();
//        disaggVarianceBoxes = hmapStates.get("disaggVarianceBoxes").getBox();
//        levelTwoBoxes = hmapStates.get("levelTwoBoxes").getBox();
        StageOneLevelOneBoxesSelection = hmapStates.get("StageOneLevelOneBoxesSelection").getBox();
        disaggVarianceBoxesSelection = hmapStates.get("disaggVarianceBoxesSelection").getBox();
        StageOneLevelTwoBoxesSelection = hmapStates.get("StageOneLevelTwoBoxesSelection").getBox();

        meanSubmodelCheckBox = hmapStates.get("meanSubmodelCheckBox").getBoolean();
        BSVarianceCheckBox = hmapStates.get("BSVarianceCheckBox").getBoolean();
        WSVarianceCheckBox = hmapStates.get("WSVarianceCheckBox").getBoolean();
        centerRegressorsCheckBox = hmapStates.get("centerRegressorsCheckBox").getBoolean();
        discardSubjectsCheckBox = hmapStates.get("discardSubjectsCheckBox").getBoolean();
        resampleCheckBox = hmapStates.get("resampleCheckBox").getBoolean();
        adaptiveQuadritureCheckBox = hmapStates.get("adaptiveQuadritureCheckBox").getBoolean();
        run32BitCheckBox = hmapStates.get("run32BitCheckBox").getBoolean();
        convergenceCriteria = hmapStates.get("convergenceCriteria").getString();
        quadriturePoints = hmapStates.get("quadriturePoints").getInt();
        maximumIterations = hmapStates.get("maximumIterations").getInt();
        ridgeSpinner = hmapStates.get("ridgeSpinner").getDouble();
        resampleSpinner = hmapStates.get("resampleSpinner").getInt();
        NoAssociationRadio = hmapStates.get("NoAssociationRadio").getBoolean();
        LinearAssociationRadio = hmapStates.get("LinearAssociationRadio").getBoolean();
        QuadraticAssociationRadio = hmapStates.get("QuadraticAssociationRadio").getBoolean();
        isStageOneSubmitted = hmapStates.get("isStageOneSubmitted").getBoolean();
        isStageTwoSubmitted = hmapStates.get("isStageTwoSubmitted").getBoolean();

//        stageTwoListModel = hmapStates.get("stageTwoListModel").getStringList();
        stageTwoListModelString = hmapStates.get("stageTwoListModel").getString();
        stageTwoListModel = loadDefaultListModel(stageTwoListModelString);

//        stageTwoLevelTwo = hmapStates.get("stageTwoLevelTwo").getStringList();
        stageTwoLevelTwoString = hmapStates.get("stageTwoLevelTwo").getString();
        stageTwoLevelTwo = loadDefaultListModel(stageTwoLevelTwoString);

        isStageTwoSubmitClicked = hmapStates.get("isStageTwoSubmitClicked").getBoolean();
        stageTwoGridBoxesSelection = hmapStates.get("stageTwoGridBoxesSelection").getBox();

        suppressIntCheckBox = hmapStates.get("suppressIntCheckBox").getBoolean();
        levelTwoSelected = hmapStates.get("levelTwoSelected").getStringArrayList();

        sessionFolderName = hmapStates.get("sessionFolderName").getString();

    }

    public HashMap<String, StateObject> createStatesHashMap() {
        HashMap<String, StateObject> hashmap = new HashMap<>();
        StateObject po0 = new StateObject("filepath", filepath);
        StateObject po1 = new StateObject("titleField", title);
        StateObject po2 = new StateObject("missingValuePresent", missingValuePresent);
        StateObject po3 = new StateObject("missingValueAbsent", missingValueAbsent);
        StateObject po4 = new StateObject("newModelMissingValueCode", newModelMissingValueCode);
        StateObject po5 = new StateObject("stageOneContinuousRadio", stageOneContinuousRadio);
        StateObject po6 = new StateObject("stageOneDichotomousRadio", stageOneDichotomousRadio);
        StateObject po7 = new StateObject("stageOneOrdinalRadio", stageOneOrdinalRadio);
        StateObject po8 = new StateObject("oneRLERadio", oneRLERadio);
        StateObject po9 = new StateObject("moreThanOneRLERadio", moreThanOneRLERadio);
        StateObject po10 = new StateObject("randomScaleSelectionYes", randomScaleSelectionYes);
        StateObject po11 = new StateObject("randomScaleSelectionNo", randomScaleSelectionNo);
        StateObject po12 = new StateObject("includeStageTwoYes", includeStageTwoYes);
        StateObject po13 = new StateObject("includeStageTwoNo", includeStageTwoNo);
        StateObject po14 = new StateObject("stageTwoSingleLevel", stageTwoSingleLevel);
        StateObject po15 = new StateObject("stageTwoMultiLevel", stageTwoMultiLevel);
        StateObject po16 = new StateObject("continuousRadio", continuousRadio);
        StateObject po17 = new StateObject("dichotomousRadio", dichotomousRadio);
        StateObject po18 = new StateObject("countRadio", countRadio);
        StateObject po19 = new StateObject("multinomialRadio", multinomialRadio);
        StateObject po20 = new StateObject("seedTextBox", seedTextBox);
        StateObject po21 = new StateObject("isNewModalConfigSubmitted", isNewModalConfigSubmitted);
        StateObject po22 = new StateObject("IDpos", IDpos);
        StateObject po23 = new StateObject("stageOnePos", stageOnePos);
        StateObject po24 = new StateObject("stageTwoPos", stageTwoPos);
        StateObject po25 = new StateObject("varList", varListString);
        StateObject po26 = new StateObject("levelOneList", levelOneListString);
        StateObject po27 = new StateObject("levelTwoList", levelTwoListString);
        StateObject po28 = new StateObject("addStageOneCHecked", addStageOneCHecked);
        StateObject po29 = new StateObject("stageOneClicked", stageOneClicked);
        StateObject po30 = new StateObject("isStageOneRegSubmitClicked", isStageOneRegSubmitClicked);
        StateObject po31 = new StateObject("StageOneLevelOneBoxesSelection", StageOneLevelOneBoxesSelection);
        StateObject po32 = new StateObject("disaggVarianceBoxesSelection", disaggVarianceBoxesSelection);
        StateObject po33 = new StateObject("StageOneLevelTwoBoxesSelection", StageOneLevelTwoBoxesSelection);
        StateObject po34 = new StateObject("meanSubmodelCheckBox", meanSubmodelCheckBox);
        StateObject po35 = new StateObject("BSVarianceCheckBox", BSVarianceCheckBox);
        StateObject po36 = new StateObject("WSVarianceCheckBox", WSVarianceCheckBox);
        StateObject po37 = new StateObject("centerRegressorsCheckBox", centerRegressorsCheckBox);
        StateObject po38 = new StateObject("discardSubjectsCheckBox", discardSubjectsCheckBox);
        StateObject po39 = new StateObject("resampleCheckBox", resampleCheckBox);
        StateObject po40 = new StateObject("adaptiveQuadritureCheckBox", adaptiveQuadritureCheckBox);
        StateObject po41 = new StateObject("run32BitCheckBox", run32BitCheckBox);
        StateObject po42 = new StateObject("convergenceCriteria", convergenceCriteria);
        StateObject po43 = new StateObject("quadriturePoints", quadriturePoints);
        StateObject po44 = new StateObject("maximumIterations", maximumIterations);
        StateObject po45 = new StateObject("ridgeSpinner", ridgeSpinner);
        StateObject po46 = new StateObject("resampleSpinner", resampleSpinner);
        StateObject po47 = new StateObject("NoAssociationRadio", NoAssociationRadio);
        StateObject po48 = new StateObject("LinearAssociationRadio", LinearAssociationRadio);
        StateObject po49 = new StateObject("QuadraticAssociationRadio", QuadraticAssociationRadio);
        StateObject po50 = new StateObject("isStageOneSubmitted", isStageOneSubmitted);
        StateObject po51 = new StateObject("isStageTwoSubmitted", isStageTwoSubmitted);
        StateObject po52 = new StateObject("stageTwoListModel", stageTwoListModelString);
        StateObject po53 = new StateObject("stageTwoLevelTwo", stageTwoLevelTwoString);
        StateObject po54 = new StateObject("isStageTwoSubmitClicked", isStageTwoSubmitClicked);
        StateObject po55 = new StateObject("stageTwoGridBoxesSelection", stageTwoGridBoxesSelection);
        StateObject po56 = new StateObject("suppressIntCheckBox", suppressIntCheckBox);
        StateObject po57 = new StateObject("levelTwoSelected", levelTwoSelected, 0);
        StateObject po58 = new StateObject("sessionFolderName", sessionFolderName);

        hashmap.put(po0.getKey(), po0);
        hashmap.put(po1.getKey(), po1);
        hashmap.put(po2.getKey(), po2);
        hashmap.put(po3.getKey(), po3);
        hashmap.put(po4.getKey(), po4);
        hashmap.put(po5.getKey(), po5);
        hashmap.put(po6.getKey(), po6);
        hashmap.put(po7.getKey(), po7);
        hashmap.put(po8.getKey(), po8);
        hashmap.put(po9.getKey(), po9);
        hashmap.put(po10.getKey(), po10);
        hashmap.put(po11.getKey(), po11);
        hashmap.put(po12.getKey(), po12);
        hashmap.put(po13.getKey(), po13);
        hashmap.put(po14.getKey(), po14);
        hashmap.put(po15.getKey(), po15);
        hashmap.put(po16.getKey(), po16);
        hashmap.put(po17.getKey(), po17);
        hashmap.put(po18.getKey(), po18);
        hashmap.put(po19.getKey(), po19);
        hashmap.put(po20.getKey(), po20);
        hashmap.put(po21.getKey(), po21);
        hashmap.put(po22.getKey(), po22);
        hashmap.put(po23.getKey(), po23);
        hashmap.put(po24.getKey(), po24);
        hashmap.put(po25.getKey(), po25);
        hashmap.put(po26.getKey(), po26);
        hashmap.put(po27.getKey(), po27);
        hashmap.put(po28.getKey(), po28);
        hashmap.put(po29.getKey(), po29);
        hashmap.put(po30.getKey(), po30);
        hashmap.put(po31.getKey(), po31);
        hashmap.put(po32.getKey(), po32);
        hashmap.put(po33.getKey(), po33);
        hashmap.put(po34.getKey(), po34);
        hashmap.put(po35.getKey(), po35);
        hashmap.put(po36.getKey(), po36);
        hashmap.put(po37.getKey(), po37);
        hashmap.put(po38.getKey(), po38);
        hashmap.put(po39.getKey(), po39);
        hashmap.put(po40.getKey(), po40);
        hashmap.put(po41.getKey(), po41);
        hashmap.put(po42.getKey(), po42);
        hashmap.put(po43.getKey(), po43);
        hashmap.put(po44.getKey(), po44);
        hashmap.put(po45.getKey(), po45);
        hashmap.put(po46.getKey(), po46);
        hashmap.put(po47.getKey(), po47);
        hashmap.put(po48.getKey(), po48);
        hashmap.put(po49.getKey(), po49);
        hashmap.put(po50.getKey(), po50);
        hashmap.put(po51.getKey(), po51);
        hashmap.put(po52.getKey(), po52);
        hashmap.put(po53.getKey(), po53);
        hashmap.put(po54.getKey(), po54);
        hashmap.put(po55.getKey(), po55);
        hashmap.put(po56.getKey(), po56);
        hashmap.put(po57.getKey(), po57);
        hashmap.put(po58.getKey(), po58);
        return hashmap;
    }

    private String saveDefaultListModel(DefaultListModel<String> list) {
        String oneString = "";

        if (list != null) {
            for (int i = 0; i < list.getSize(); i++) {
                String item = list.elementAt(i);
                if (i == list.getSize() - 1) {
                    oneString = oneString + item;
                } else {
                    oneString = oneString + item + ",";
                }
            }
        }
        return oneString;
    }

    private DefaultListModel<String> loadDefaultListModel(String savedString) {
        DefaultListModel<String> result = new DefaultListModel<>();

        if (savedString.length() > 0) {
            String[] arr = savedString.split(",");

            for (String item : arr) {
                result.addElement(item);
            }
        }
        return result;
    }
//    public boolean getMissingValuePresent() {
//        return missingvaluePresent;
//    }
//
//    public void setMissingValuePresent(boolean turnon) {
//        this.missingvaluePresent = turnon;
//    }
//    
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }

    private boolean[][] getSelectionBoxes(ArrayList<ArrayList<JCheckBox>> Boxes) {

        if (Boxes == null) {
            return null;
        } else {
            int rows = Boxes.size();
            boolean[][] result = null;
            if (rows != 0) {
                int cols = Boxes.get(0).size();
                result = new boolean[rows][cols];

                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        result[i][j] = Boxes.get(i).get(j).isSelected();
                    }
                }
            } else {
                return null;
            }
            return result;
        }
    }
}
