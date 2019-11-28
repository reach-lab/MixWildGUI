/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mixregui;

import def_lib.StateObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    // init default
    MixRegGuiStates() {
    }

    // init a snapshot of mixregGui states
    MixRegGuiStates(mixregGUI mxr) {
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
    }

    public void writeAllStates(mixregGUI mxr) {
        HashMap<String, StateObject> hmapStates = this.createStatesHashMap();

        // user open filechooser and select save path
        JFileChooser fileChooser_save = new JFileChooser();
        fileChooser_save.setSelectedFile(new File("GuiStates.ser"));
        int option = fileChooser_save.showSaveDialog(mxr);
        if (option == JFileChooser.APPROVE_OPTION) {
            File save_filename = fileChooser_save.getSelectedFile();

            try {
                FileOutputStream fos = new FileOutputStream(save_filename);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(hmapStates);
                oos.close();
                fos.close();
                System.out.printf("Gui states are saved in GuiStates.ser");
            } catch (IOException ioe) {
            }
        }
    }

    public void readAllStates(mixregGUI mxr) {
        HashMap<String, StateObject> hmapStates = null;

        JFileChooser fileChooser_load = new JFileChooser();
        fileChooser_load.setSelectedFile(new File("GuiStates.ser"));
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
            } catch (FileNotFoundException ex) {
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(mixregGUI.class.getName()).log(Level.SEVERE, null, ex);
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

    }

    public HashMap<String, StateObject> createStatesHashMap() {
        HashMap<String, StateObject> hashmap = new HashMap<>();
        StateObject po0 = new StateObject("filepath", 0, filepath, true);
        StateObject po1 = new StateObject("titleField", 0, title, true);
        StateObject po2 = new StateObject("missingValuePresent", 0, "", missingValuePresent);
        StateObject po3 = new StateObject("missingValueAbsent", 0, "", missingValueAbsent);
        StateObject po4 = new StateObject("newModelMissingValueCode", 0, newModelMissingValueCode, true);
        StateObject po5 = new StateObject("stageOneContinuousRadio", 0, "", stageOneContinuousRadio);
        StateObject po6 = new StateObject("stageOneDichotomousRadio", 0, "", stageOneDichotomousRadio);
        StateObject po7 = new StateObject("stageOneOrdinalRadio", 0, "", stageOneOrdinalRadio);
        StateObject po8 = new StateObject("oneRLERadio", 0, "", oneRLERadio);
        StateObject po9 = new StateObject("moreThanOneRLERadio", 0, "", moreThanOneRLERadio);
        StateObject po10 = new StateObject("randomScaleSelectionYes", 0, "", randomScaleSelectionYes);
        StateObject po11 = new StateObject("randomScaleSelectionNo", 0, "", randomScaleSelectionNo);
        StateObject po12 = new StateObject("includeStageTwoYes", 0, "", includeStageTwoYes);
        StateObject po13 = new StateObject("includeStageTwoNo", 0, "", includeStageTwoNo);
        StateObject po14 = new StateObject("stageTwoSingleLevel", 0, "", stageTwoSingleLevel);
        StateObject po15 = new StateObject("stageTwoMultiLevel", 0, "", stageTwoMultiLevel);
        StateObject po16 = new StateObject("continuousRadio", 0, "", continuousRadio);
        StateObject po17 = new StateObject("dichotomousRadio", 0, "", dichotomousRadio);
        StateObject po18 = new StateObject("countRadio", 0, "", countRadio);
        StateObject po19 = new StateObject("multinomialRadio", 0, "", multinomialRadio);
        StateObject po20 = new StateObject("seedTextBox", 0, seedTextBox, true);
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
        return hashmap;
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
}
