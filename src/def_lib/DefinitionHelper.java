package def_lib;

import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
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
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
import static mixregui.NewModel.defFile;

/****
 * Exposed Methods:
 * readDefinitionFile: takes File as parameter, assigns all variables and then attempts to validate file
 * buildStageOneDefinitionList: returns ArrayList<String> that can be passed to a FileWriter class, validates first
 * @note: Both classes should pass Exceptions to the view and interrupt until user fixes error  
 *  
 * @author Eldin Dzubur
 */
public class DefinitionHelper {

    /**
     * Private Class Keys
     */
    private static final boolean MIX_INTEGER = Boolean.TRUE;
    private static final boolean MIX_STRING = Boolean.FALSE;
    /**
     * Public Class Specific Keys
     */
    public static final int MIXREGLS_MIXREG_KEY = 1;
    public static final int MIXREGLS_MIXOR_KEY = 2;
    public static final int MIXREGMLS_MIXREG_KEY = 3;
    public static final int MIXREGMLS_MIXOR_KEY = 4;

    public static final int INIT_PARAMETER_KEY = 1;
    public static final int S1_ADVANCED_KEY = 2;
    public static final int S1_MODEL_KEY = 3;
    public static final int S2_ADVANCED_KEY = 4;
    public static final int S2_MODEL_KEY = 5;

    private int randomLocationEffects = 1;
    private boolean stageTwoBinary = Boolean.FALSE;
    
    JFrame myFrame;
    JEditorPane myPane;
    
    int selectedModel;
    String defFilePath;
    /**
     * Initial Definition Parameters
     */
    private String modelTitle;
    private String modelSubtitle;
    private String dataFilename;
    private String outputPrefix;
    /**
     * Stage 1 Advanced Options
     */
    private String dataVariableCount;
    private String modelMeanCount = "0";
    private String modelLocRanCount = "0";
    private String modelScaleCount = "0";
    private String modelFixedInt;
    private String modelRandomInt;
    private String modelScaleInt;
    private String decompMeanCount = "0";
    private String decompLocRanCount = "0";
    private String decompScaleCount = "0";
    private String advancedConvergence;
    private String advancedQuadPoints;
    private String advancedAdaptiveQuad;
    private String advancedMaxIteration;
    private String advancedMissingValue;
    private String advancedCenterScale;
    private String advancedRidge;
    private String modelBetweenCount = "0";
    private String modelWithinCount = "0";
    private String modelBetweenInt = "0";
    private String modelWithinInt = "0";
    private String decompBSCount = "0";
    private String decompWSCount = "0";
    private String advancedEffectMeanWS;
    /**
     * Stage 1 Model Specification
     */
    private String[] idOutcome;
    private String[] fieldModelMeanRegressors; // stage 1 under mean - disagg. 
    private String[] fieldModelBSRegressors; // random intercept
    private String[] fieldModelWSRegressors; // random scale
    private String[] fieldModelLocRanRegressors;
    private String[] fieldModelScaleRegressors;
    private String[] fieldDecompMeanRegressors; 
    private String[] fieldDecompBSRegressors;
    private String[] fieldDecompWSRegressors;
    private String[] fieldDecompLocRanRegressors;
    private String[] fieldDecompScaleRegressors;
    private String labelModelOutcome;
    private String[] labelModelMeanRegressors;
    private String[] labelModelLocRanRegressors;
    private String[] labelModelScaleRegressors;
    private String[] labelModelBSRegressors;
    private String[] labelModelWSRegressors;
    private String[] labelDecompMeanRegressors;
    private String[] labelDecompLocRanRegressors;
    private String[] labelDecompScaleRegressors;
    private String[] labelDecompBSRegressors;
    private String[] labelDecompWSRegressors;
    /**
     * Stage 2 Advanced Options
     */
    private String stageTwoFixedCount = "0";
    private String stageTwoLocRanInteractions = "0";
    private String stageTwoScaleInteractions = "0";
    private String stageTwoIntOfInteraction = "0";
    private String stageTwoOutcomeCatCount;
    /**
     * Stage 2 ModelS Specification
     */
    private String stageTwoOutcomeField;
    private String[] stageTwoOutcomeCatLabel;
    private String[] stageTwoFixedFields; // main effect
    private String[] stageTwoLocRanIntFields; //random
    private String[] stageTwoScaleIntFields; //scale
    private String[] stageTwoFirstIntFields; //scale * random
    private String stageTwoOutcomeLabel;
    private String[] stageTwoFixedLabels;
    private String[] stageTwoLocRanIntLabels;
    private String[] stageTwoScaleIntLabels;
    private String[] stageTwoFirstIntLabels;
    
    

    /**
     *
     * @param randomLocationEffects: number of random location effects
     * @param stageTwoBinary : whether or not the stage two outcome is
     * dichotomous or ordinal
     */
    public DefinitionHelper(int randomLocationEffects, boolean stageTwoBinary) {
        this.randomLocationEffects = randomLocationEffects;
        this.stageTwoBinary = stageTwoBinary;
        if (this.randomLocationEffects > 1) {
            this.modelLocRanCount = Integer.toString(randomLocationEffects);
        }
    }
//
    public int getRandomLocationEffects() {
        return randomLocationEffects;
    }

    public boolean isStageTwoBinary() {
        return stageTwoBinary;
    }

    /**
     *
     * @return sequenceDecision: integer key determining stage 1+stage 2 model
     * UI views
     */
    public int sequenceDecision() {
        if(stageTwoBinary){
            if(randomLocationEffects<2){return MIXREGLS_MIXOR_KEY;}
            else{return MIXREGMLS_MIXOR_KEY;}
        }
        else{
            if(randomLocationEffects<2){return MIXREGLS_MIXREG_KEY;}
            else{return MIXREGMLS_MIXREG_KEY;}
        }
    }
    /**
     *
     * @param defFile the file, as specified by the filepicker method
     * @param varNames the list of variable names, derived from data file
     * try/catch these exceptions in order
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception display error message for this exception to user, do
     * not execute program until this is resolved
     */
    public void readDefinitionFile(File defFile, List<String> varNames) throws FileNotFoundException, IOException, Exception {
        System.out.println("Beginning new model, type is: " + sequenceDecision());
        try (BufferedReader br = new BufferedReader(new FileReader(defFile))) {
            String defLine;
            int row = 0;
            List<String> defSummary = new ArrayList<>();

            int fileSize = 0;
            while ((defLine = br.readLine()) != null ) {
                //if(defLine.length() > 0){
                System.out.println(defLine);
                    fileSize++;
                //}
                defSummary.add(defLine.trim());
            }
            if (stageTwoBinary & fileSize != 31) {
                throw new Exception("Invalid definition file length, likely you forgot line spacing");  // TODO: Create a new class that extends from Exception for invalid def files
            } else if (!stageTwoBinary & fileSize != 30) {
                throw new Exception("Invalid definition file length, likely you forgot line spacing");
            }

            assignDefinitionVariables(defSummary);
        }
    }
    private boolean validateFieldLabels(String countVariable, String[] fieldLabelLine) throws Exception {
        int field = -1;
        int labels = 0;
        try {
            field = Integer.parseInt(countVariable);
        } catch (Exception ex) {
            throw new Exception("Unassigned count variable for one or more options or regressors");
        }
        try {
            labels = fieldLabelLine.length;
        } catch (Exception ex) {
            throw new Exception("Unassigned field or label series for one or more sets of options or regressors");
        }
        return field == labels;
    }
    private void exportValidatorStageOne() throws Exception {
         if(!validateFieldLabels(getModelMeanCount(),getFieldModelMeanRegressors())){
             throw new Exception("Fatal model error: number of MEAN regressors does not equal MEAN fields");
         }
         if(!validateFieldLabels(getModelMeanCount(),getLabelModelMeanRegressors())){
             throw new Exception("Fatal model error: number of MEAN regressors does not equal MEAN labels");
         }
         if(sequenceDecision() == MIXREGLS_MIXOR_KEY || sequenceDecision() == MIXREGLS_MIXREG_KEY) {
            if(!validateFieldLabels(getModelBetweenCount(),getFieldModelBSRegressors())){
                throw new Exception("Fatal model error: number of BS regressors does not equal BS fields");
            }
            if(!validateFieldLabels(getModelBetweenCount(),getLabelModelBSRegressors())){
                throw new Exception("Fatal model error: number of BS regressors does not equal BS labels");
            }
            if(!validateFieldLabels(getModelWithinCount(),getFieldModelWSRegressors())){
                throw new Exception("Fatal model error: number of WS regressors does not equal WS fields");
            }
            if(!validateFieldLabels(getModelWithinCount(),getLabelModelWSRegressors())){
                throw new Exception("Fatal model error: number of WS regressors does not equal WS labels");
         }
         }
         else {
            if(!validateFieldLabels(getModelLocRanCount(),getFieldModelLocRanRegressors())){
                throw new Exception("Fatal model error: number of LOCATION RANDOM regressors does not equal LOCATION RANDOM fields");
            }
            if(!validateFieldLabels(getModelLocRanCount(),getLabelModelLocRanRegressors())){
                throw new Exception("Fatal model error: number of LOCATION RANDOM regressors does not equal LOCATION RANDOM labels");
            }
            if(!validateFieldLabels(getModelScaleCount(),getFieldModelScaleRegressors())){
                throw new Exception("Fatal model error: number of SCALE regressors does not equal SCALE fields");
            }
            if(!validateFieldLabels(getModelScaleCount(),getLabelModelScaleRegressors())){
                throw new Exception("Fatal model error: number of SCALE regressors does not equal SCALE labels");
            }
         }
         if(!validateFieldLabels(getModelMeanCount(),getFieldModelMeanRegressors())){
             throw new Exception("Fatal model error: number of MEAN regressors does not equal MEAN fields");
         }
         
         if(!validateFieldLabels(getDecompMeanCount(),getLabelDecompMeanRegressors())){
             throw new Exception("Fatal variance decomposition error: number of MEAN regressors does not equal MEAN labels");
         }
         if(sequenceDecision() == MIXREGLS_MIXOR_KEY || sequenceDecision() == MIXREGLS_MIXREG_KEY) {
            if(!validateFieldLabels(getDecompBSCount(),getFieldDecompBSRegressors())){
                throw new Exception("Fatal variance decomposition error: number of BS regressors does not equal BS fields");
            }
            if(!validateFieldLabels(getDecompBSCount(),getLabelDecompBSRegressors())){
                throw new Exception("Fatal variance decomposition error: number of BS regressors does not equal BS labels");
            }
            if(!validateFieldLabels(getDecompWSCount(),getFieldDecompWSRegressors())){
                throw new Exception("Fatal variance decomposition error: number of WS regressors does not equal WS fields");
            }
            if(!validateFieldLabels(getDecompWSCount(),getLabelDecompWSRegressors())){
                throw new Exception("Fatal variance decomposition error: number of WS regressors does not equal WS labels");
            }
         }
         else {
            if(!validateFieldLabels(getDecompLocRanCount(),getFieldDecompLocRanRegressors())){
                throw new Exception("Fatal variance decomposition error: number of LOCATION RANDOM regressors does not equal LOCATION RANDOM fields");
            }
            if(!validateFieldLabels(getDecompLocRanCount(),getLabelDecompLocRanRegressors())){
                throw new Exception("Fatal variance decomposition error: number of LOCATION RANDOM regressors does not equal LOCATION RANDOM labels");
            }
            if(!validateFieldLabels(getDecompScaleCount(),getFieldDecompScaleRegressors())){
                throw new Exception("Fatal variance decomposition error: number of SCALE regressors does not equal SCALE fields");
            }
            if(!validateFieldLabels(getDecompScaleCount(),getLabelDecompScaleRegressors())){
                throw new Exception("Fatal variance decomposition error: number of SCALE regressors does not equal SCALE labels");
            }
         }   
         if(!validateFieldLabels(getStageTwoFixedCount(),getStageTwoFixedLabels())){
             throw new Exception("Fatal stage two label error: number of FIXED regressors does not equal FIXED labels");
         }
         if(!validateFieldLabels(getStageTwoLocRanInteractions(),getStageTwoLocRanIntLabels())){
             throw new Exception("Fatal stage two label error: number of LOCATION RANDOM INTERACTIONS does not equal LOCATION RANDOM INTERACTIONS labels");
         }
         if(!validateFieldLabels(getStageTwoScaleInteractions(),getStageTwoScaleIntLabels())){
             throw new Exception("Fatal stage two label error: number of SCALE RANDOM INTERACTIONS does not equal SCALE RANDOM INTERACTIONS labels");
         }
         if(!validateFieldLabels(getStageTwoIntOfInteraction(),getStageTwoFirstIntLabels())){
             throw new Exception("Fatal stage two label error: number of regressors THREE-WAY INTERACTION regressors to does not equal THREE-WAY INTERACTION labels");
         }
         if(!validateFieldLabels(getStageTwoFixedCount(),getStageTwoFixedFields())){
             throw new Exception("Fatal stage two field error: number of FIXED regressors does not equal FIXED fields");
         }
         if(!validateFieldLabels(getStageTwoLocRanInteractions(),getStageTwoLocRanIntFields())){
             throw new Exception("Fatal stage two field error: number of LOCATION RANDOM INTERACTIONS does not equal LOCATION RANDOM INTERACTIONS fields");
         }
         if(!validateFieldLabels(getStageTwoScaleInteractions(),getStageTwoScaleIntFields())){
             throw new Exception("Fatal stage two field error: number of SCALE RANDOM INTERACTIONS does not equal SCALE RANDOM INTERACTIONS fields");
         }
         if(!validateFieldLabels(getStageTwoIntOfInteraction(),getStageTwoFirstIntFields())){
             throw new Exception("Fatal stage two field error: number of regressors THREE-WAY INTERACTION regressors to does not equal THREE-WAY INTERACTION fields");
         }
    }
    
    /**
     * debug version of stage one def builder
     * @return
     */
    public List<String> debugStageOneDefinitonList() throws Exception {
        List<String> newDefinitionFile = new ArrayList();
        newDefinitionFile.add(getModelTitle());
        newDefinitionFile.add(getModelSubtitle());
        newDefinitionFile.add(getDataFilename());
        newDefinitionFile.add(getOutputPrefix());
        String[] advancedOptionsOne = advancedVariableBuild(1);
        newDefinitionFile.add(Arrays.toString(advancedOptionsOne).replaceAll(",", " "));
        newDefinitionFile.add(Arrays.toString(getIdOutcome()).replaceAll(",", " "));
        newDefinitionFile.add(Arrays.toString(getFieldModelMeanRegressors()).replaceAll(",", " "));
        String[] advancedOptionsTwo = advancedVariableBuild(2);


        switch (sequenceDecision()) {
            case MIXREGLS_MIXREG_KEY:
                newDefinitionFile.add(Arrays.toString(getFieldModelBSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldModelWSRegressors()).replaceAll(",", " "));
                 newDefinitionFile.add(Arrays.toString(getFieldDecompMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldDecompBSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldDecompWSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(getLabelModelOutcome());
                  newDefinitionFile.add(Arrays.toString(getLabelModelMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelModelBSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelModelWSRegressors()).replaceAll(",", " "));
                 newDefinitionFile.add(Arrays.toString(getLabelDecompMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompBSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompWSRegressors()).replaceAll(",", " "));
                
                newDefinitionFile.add(Arrays.toString(advancedOptionsTwo).replaceAll(",", " ")); 
                newDefinitionFile.add(getStageTwoOutcomeField());
                newDefinitionFile.add(Arrays.toString(getStageTwoFixedFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoLocRanIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoScaleIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoFirstIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(getStageTwoOutcomeLabel());
                newDefinitionFile.add(Arrays.toString(getStageTwoFixedLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoLocRanIntLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoScaleIntLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoFirstIntLabels()).replaceAll(",", " "));
                break;
            case MIXREGLS_MIXOR_KEY:
                newDefinitionFile.add(Arrays.toString(getFieldModelBSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldModelWSRegressors()).replaceAll(",", " "));
                 newDefinitionFile.add(Arrays.toString(getFieldDecompMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldDecompBSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldDecompWSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(getLabelModelOutcome());
                 newDefinitionFile.add(Arrays.toString(getLabelModelMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelModelBSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelModelWSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompBSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompWSRegressors()).replaceAll(",", " "));
                
                newDefinitionFile.add(Arrays.toString(advancedOptionsTwo).replaceAll(",", " ")); 
                newDefinitionFile.add(getStageTwoOutcomeField());
                newDefinitionFile.add(Arrays.toString(getStageTwoFixedFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoLocRanIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoScaleIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoFirstIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(getStageTwoOutcomeLabel());
                newDefinitionFile.add(Arrays.toString(getStageTwoFixedLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoLocRanIntLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoScaleIntLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoFirstIntLabels()).replaceAll(",", " "));
                break;
            case MIXREGMLS_MIXREG_KEY:
                newDefinitionFile.add(Arrays.toString(getFieldModelLocRanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldModelScaleRegressors()).replaceAll(",", " "));
                 newDefinitionFile.add(Arrays.toString(getFieldDecompMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldDecompLocRanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldDecompScaleRegressors()).replaceAll(",", " "));
                 newDefinitionFile.add(getLabelModelOutcome());
                  newDefinitionFile.add(Arrays.toString(getLabelModelMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelModelLocRanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelModelScaleRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompLocRanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompScaleRegressors()).replaceAll(",", " "));
                
                newDefinitionFile.add(Arrays.toString(advancedOptionsTwo).replaceAll(",", " ")); 
                newDefinitionFile.add(getStageTwoOutcomeField());
                newDefinitionFile.add(Arrays.toString(getStageTwoFixedFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoLocRanIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoScaleIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoFirstIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(getStageTwoOutcomeLabel());
                newDefinitionFile.add(Arrays.toString(getStageTwoFixedLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoLocRanIntLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoScaleIntLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoFirstIntLabels()).replaceAll(",", " "));
                break;
            case MIXREGMLS_MIXOR_KEY:
                newDefinitionFile.add(Arrays.toString(getFieldModelLocRanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldModelScaleRegressors()).replaceAll(",", " "));
                 newDefinitionFile.add(Arrays.toString(getFieldDecompMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldDecompLocRanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldDecompScaleRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(getLabelModelOutcome());
                 newDefinitionFile.add(Arrays.toString(getLabelModelMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelModelLocRanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelModelScaleRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompLocRanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompScaleRegressors()).replaceAll(",", " "));
                
                newDefinitionFile.add(Arrays.toString(advancedOptionsTwo).replaceAll(",", " ")); 
                newDefinitionFile.add(getStageTwoOutcomeField());
                newDefinitionFile.add(Arrays.toString(getStageTwoFixedFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoLocRanIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoScaleIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoFirstIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(getStageTwoOutcomeLabel());
                newDefinitionFile.add(Arrays.toString(getStageTwoFixedLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoLocRanIntLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoScaleIntLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoFirstIntLabels()).replaceAll(",", " "));
                break;
            default:
            //TODO: Log this error     
        }

       
        return newDefinitionFile;
    }
    
    /**
     *
     * @return
     */
    public List<String> buildStageOneDefinitonList() throws Exception { //does this create the def file
        List<String> newDefinitionFile = new ArrayList();
        newDefinitionFile.add(getModelTitle());
        newDefinitionFile.add(getModelSubtitle());
        newDefinitionFile.add(getDataFilename());
        newDefinitionFile.add(getOutputPrefix());
        String[] advancedOptionsOne = advancedVariableBuild(1);
        newDefinitionFile.add(Arrays.toString(advancedOptionsOne).replaceAll(",", " "));
        newDefinitionFile.add(Arrays.toString(getIdOutcome()).replaceAll(",", " "));
        newDefinitionFile.add(Arrays.toString(getFieldModelMeanRegressors()).replaceAll(",", " "));
        String[] advancedOptionsTwo = advancedVariableBuild(2);


        switch (sequenceDecision()) {
            case MIXREGLS_MIXREG_KEY:
                newDefinitionFile.add(Arrays.toString(getFieldModelBSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldModelWSRegressors()).replaceAll(",", " "));
                 newDefinitionFile.add(Arrays.toString(getFieldDecompMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldDecompBSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldDecompWSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(getLabelModelOutcome());
                  newDefinitionFile.add(Arrays.toString(getLabelModelMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelModelBSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelModelWSRegressors()).replaceAll(",", " "));
                 newDefinitionFile.add(Arrays.toString(getLabelDecompMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompBSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompWSRegressors()).replaceAll(",", " "));
                
                newDefinitionFile.add(Arrays.toString(advancedOptionsTwo).replaceAll(",", " ")); 
                newDefinitionFile.add(getStageTwoOutcomeField());
                newDefinitionFile.add(Arrays.toString(getStageTwoFixedFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoLocRanIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoScaleIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoFirstIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(getStageTwoOutcomeLabel());
                newDefinitionFile.add(Arrays.toString(getStageTwoFixedLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoLocRanIntLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoScaleIntLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoFirstIntLabels()).replaceAll(",", " "));
                break;
            case MIXREGLS_MIXOR_KEY:
                newDefinitionFile.add(Arrays.toString(getFieldModelBSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldModelWSRegressors()).replaceAll(",", " "));
                 newDefinitionFile.add(Arrays.toString(getFieldDecompMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldDecompBSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldDecompWSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(getLabelModelOutcome());
                 newDefinitionFile.add(Arrays.toString(getLabelModelMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelModelBSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelModelWSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompBSRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompWSRegressors()).replaceAll(",", " "));
                
                newDefinitionFile.add(Arrays.toString(advancedOptionsTwo).replaceAll(",", " ")); 
                newDefinitionFile.add(getStageTwoOutcomeField());
                newDefinitionFile.add(Arrays.toString(getStageTwoFixedFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoLocRanIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoScaleIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoFirstIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(getStageTwoOutcomeLabel());
                newDefinitionFile.add(Arrays.toString(getStageTwoFixedLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoLocRanIntLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoScaleIntLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoFirstIntLabels()).replaceAll(",", " "));
                break;
            case MIXREGMLS_MIXREG_KEY:
                newDefinitionFile.add(Arrays.toString(getFieldModelLocRanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldModelScaleRegressors()).replaceAll(",", " "));
                 newDefinitionFile.add(Arrays.toString(getFieldDecompMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldDecompLocRanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldDecompScaleRegressors()).replaceAll(",", " "));
                 newDefinitionFile.add(getLabelModelOutcome());
                  newDefinitionFile.add(Arrays.toString(getLabelModelMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelModelLocRanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelModelScaleRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompLocRanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompScaleRegressors()).replaceAll(",", " "));
                
                newDefinitionFile.add(Arrays.toString(advancedOptionsTwo).replaceAll(",", " ")); 
                newDefinitionFile.add(getStageTwoOutcomeField());
                newDefinitionFile.add(Arrays.toString(getStageTwoFixedFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoLocRanIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoScaleIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoFirstIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(getStageTwoOutcomeLabel());
                newDefinitionFile.add(Arrays.toString(getStageTwoFixedLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoLocRanIntLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoScaleIntLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoFirstIntLabels()).replaceAll(",", " "));
                break;
            case MIXREGMLS_MIXOR_KEY:
                newDefinitionFile.add(Arrays.toString(getFieldModelLocRanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldModelScaleRegressors()).replaceAll(",", " "));
                 newDefinitionFile.add(Arrays.toString(getFieldDecompMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldDecompLocRanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getFieldDecompScaleRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(getLabelModelOutcome());
                 newDefinitionFile.add(Arrays.toString(getLabelModelMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelModelLocRanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelModelScaleRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompMeanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompLocRanRegressors()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getLabelDecompScaleRegressors()).replaceAll(",", " "));
                
                newDefinitionFile.add(Arrays.toString(advancedOptionsTwo).replaceAll(",", " ")); 
                newDefinitionFile.add(getStageTwoOutcomeField());
                newDefinitionFile.add(Arrays.toString(getStageTwoFixedFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoLocRanIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoScaleIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoFirstIntFields()).replaceAll(",", " "));
                newDefinitionFile.add(getStageTwoOutcomeLabel());
                newDefinitionFile.add(Arrays.toString(getStageTwoFixedLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoLocRanIntLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoScaleIntLabels()).replaceAll(",", " "));
                newDefinitionFile.add(Arrays.toString(getStageTwoFirstIntLabels()).replaceAll(",", " "));
                break;
            default:
            //TODO: Log this error     
        }

        exportValidatorStageOne();
        return newDefinitionFile;
    }

    /**
     *
     * @param readDefinitionFile the definition file as a List<String>
     * @throws Exception display error message for this exception to user, do
     * not execute program until this is resolved
     */
    private void assignDefinitionVariables(List<String> readDefinitionFile) throws Exception {
        setModelTitle(readDefinitionFile.get(0));
        setModelSubtitle(readDefinitionFile.get(1));
        setDataFilename(readDefinitionFile.get(2));
        setOutputPrefix(readDefinitionFile.get(3));
        advancedVariableAssignment(1,readDefinitionFile.get(4).split("\\s+"));
        
        setIdOutcome(readDefinitionFile.get(5).split("\\s+"));
        setFieldModelMeanRegressors(readDefinitionFile.get(6).split("\\s+"));
        setFieldDecompMeanRegressors(readDefinitionFile.get(9).split("\\s+"));
        setLabelModelOutcome(readDefinitionFile.get(12));
        setLabelModelMeanRegressors(readDefinitionFile.get(13).split("\\s+"));
        setLabelDecompMeanRegressors(readDefinitionFile.get(16).split("\\s+")); 
        advancedVariableAssignment(2,readDefinitionFile.get(19).split("\\s+")); 
        setStageTwoOutcomeField(readDefinitionFile.get(20));
        switch (sequenceDecision()) {
            case MIXREGLS_MIXREG_KEY:
                setFieldModelBSRegressors(readDefinitionFile.get(7).split("\\s+"));
                setFieldModelWSRegressors(readDefinitionFile.get(8).split("\\s+"));
                setFieldDecompBSRegressors(readDefinitionFile.get(10).split("\\s+"));
                setFieldDecompWSRegressors(readDefinitionFile.get(11).split("\\s+"));
                setLabelModelBSRegressors(readDefinitionFile.get(14).split("\\s+"));
                setLabelModelWSRegressors(readDefinitionFile.get(15).split("\\s+"));
                setLabelDecompBSRegressors(readDefinitionFile.get(17).split("\\s+"));
                setLabelDecompWSRegressors(readDefinitionFile.get(18).split("\\s+"));
                setStageTwoFixedFields(readDefinitionFile.get(21).split("\\s+"));
                setStageTwoLocRanIntFields(readDefinitionFile.get(22).split("\\s+"));
                setStageTwoScaleIntFields(readDefinitionFile.get(23).split("\\s+"));
                setStageTwoFirstIntFields(readDefinitionFile.get(24).split("\\s+"));
                setStageTwoOutcomeLabel(readDefinitionFile.get(25));
                setStageTwoFixedLabels(readDefinitionFile.get(26).split("\\s+"));
                setStageTwoLocRanIntLabels(readDefinitionFile.get(27).split("\\s+"));
                setStageTwoScaleIntLabels(readDefinitionFile.get(28).split("\\s+"));
                setStageTwoFirstIntLabels(readDefinitionFile.get(29).split("\\s+"));
                break;
            case MIXREGLS_MIXOR_KEY:
                setFieldModelBSRegressors(readDefinitionFile.get(7).split("\\s+"));
                setFieldModelWSRegressors(readDefinitionFile.get(8).split("\\s+"));
                setFieldDecompBSRegressors(readDefinitionFile.get(10).split("\\s+"));
                setFieldDecompWSRegressors(readDefinitionFile.get(11).split("\\s+"));
                setLabelModelBSRegressors(readDefinitionFile.get(14).split("\\s+"));
                setLabelModelWSRegressors(readDefinitionFile.get(15).split("\\s+"));
                setLabelDecompBSRegressors(readDefinitionFile.get(17).split("\\s+"));
                setLabelDecompWSRegressors(readDefinitionFile.get(18).split("\\s+"));
                setStageTwoFixedFields(readDefinitionFile.get(22).split("\\s+"));
                setStageTwoLocRanIntFields(readDefinitionFile.get(23).split("\\s+"));
                setStageTwoScaleIntFields(readDefinitionFile.get(24).split("\\s+"));
                setStageTwoFirstIntFields(readDefinitionFile.get(25).split("\\s+"));
                setStageTwoOutcomeLabel(readDefinitionFile.get(26));
                setStageTwoFixedLabels(readDefinitionFile.get(27).split("\\s+"));
                setStageTwoLocRanIntLabels(readDefinitionFile.get(28).split("\\s+"));
                setStageTwoScaleIntLabels(readDefinitionFile.get(29).split("\\s+"));
                setStageTwoFirstIntLabels(readDefinitionFile.get(30).split("\\s+"));
                break;
            case MIXREGMLS_MIXREG_KEY:
                setFieldModelLocRanRegressors(readDefinitionFile.get(7).split("\\s+"));
                setFieldModelScaleRegressors(readDefinitionFile.get(8).split("\\s+"));
                setFieldDecompLocRanRegressors(readDefinitionFile.get(10).split("\\s+"));
                setFieldDecompScaleRegressors(readDefinitionFile.get(11).split("\\s+"));
                setLabelModelLocRanRegressors(readDefinitionFile.get(14).split("\\s+"));
                setLabelModelScaleRegressors(readDefinitionFile.get(15).split("\\s+"));
                setLabelDecompLocRanRegressors(readDefinitionFile.get(17).split("\\s+"));
                setLabelDecompScaleRegressors(readDefinitionFile.get(18).split("\\s+"));
                setStageTwoFixedFields(readDefinitionFile.get(21).split("\\s+"));
                setStageTwoLocRanIntFields(readDefinitionFile.get(22).split("\\s+"));
                setStageTwoScaleIntFields(readDefinitionFile.get(23).split("\\s+"));
                setStageTwoFirstIntFields(readDefinitionFile.get(24).split("\\s+"));
                setStageTwoOutcomeLabel(readDefinitionFile.get(25));
                setStageTwoFixedLabels(readDefinitionFile.get(26).split("\\s+"));
                setStageTwoLocRanIntLabels(readDefinitionFile.get(27).split("\\s+"));
                setStageTwoScaleIntLabels(readDefinitionFile.get(28).split("\\s+"));
                setStageTwoFirstIntLabels(readDefinitionFile.get(29).split("\\s+"));
                break;
            case MIXREGMLS_MIXOR_KEY:
                setFieldModelLocRanRegressors(readDefinitionFile.get(7).split("\\s+"));
                setFieldModelScaleRegressors(readDefinitionFile.get(8).split("\\s+"));
                setFieldDecompLocRanRegressors(readDefinitionFile.get(10).split("\\s+"));
                setFieldDecompScaleRegressors(readDefinitionFile.get(11).split("\\s+"));
                setLabelModelLocRanRegressors(readDefinitionFile.get(14).split("\\s+"));
                setLabelModelScaleRegressors(readDefinitionFile.get(15).split("\\s+"));
                setLabelDecompLocRanRegressors(readDefinitionFile.get(17).split("\\s+"));
                setLabelDecompScaleRegressors(readDefinitionFile.get(18).split("\\s+"));
                setStageTwoFixedFields(readDefinitionFile.get(22).split("\\s+"));
                setStageTwoLocRanIntFields(readDefinitionFile.get(23).split("\\s+"));
                setStageTwoScaleIntFields(readDefinitionFile.get(24).split("\\s+"));
                setStageTwoFirstIntFields(readDefinitionFile.get(25).split("\\s+"));
                setStageTwoOutcomeLabel(readDefinitionFile.get(26));

                setStageTwoFixedLabels(readDefinitionFile.get(27).split("\\s+"));
                setStageTwoLocRanIntLabels(readDefinitionFile.get(28).split("\\s+"));
                setStageTwoScaleIntLabels(readDefinitionFile.get(29).split("\\s+"));
                setStageTwoFirstIntLabels(readDefinitionFile.get(30).split("\\s+"));              

                break;
            default:
            //TODO: Log this error     
        }
        exportValidatorStageOne();
    }
    /**
     *
     * @param stage: stage 1 or stage 2, internal call only
     * @return
     */
    private String[] advancedVariableBuild(int stage) {
        List<String> advancedVars = new ArrayList();
        if (stage == 1) {
            switch (sequenceDecision()) {
                case MIXREGLS_MIXREG_KEY:
                    advancedVars.add(getDataVariableCount());
                    advancedVars.add(getModelMeanCount());
                    advancedVars.add(getModelBetweenCount());
                    advancedVars.add(getModelWithinCount());
                    advancedVars.add(getModelFixedInt());
                    advancedVars.add(getModelBetweenInt());
                    advancedVars.add(getModelWithinInt());
                    advancedVars.add(getDecompMeanCount());
                    advancedVars.add(getDecompBSCount());
                    advancedVars.add(getDecompWSCount());
                    advancedVars.add(getAdvancedConvergence());
                    advancedVars.add(getAdvancedQuadPoints());
                    advancedVars.add(getAdvancedAdaptiveQuad());
                    advancedVars.add(getAdvancedMaxIteration());
                    advancedVars.add(getAdvancedMissingValue());
                    advancedVars.add(getAdvancedCenterScale());
                    advancedVars.add(getAdvancedEffectMeanWS());
                    advancedVars.add(getAdvancedRidge());
                    break;
                case MIXREGLS_MIXOR_KEY:
                    advancedVars.add(getDataVariableCount());
                    advancedVars.add(getModelMeanCount());
                    advancedVars.add(getModelBetweenCount());
                    advancedVars.add(getModelWithinCount());
                    advancedVars.add(getModelFixedInt());
                    advancedVars.add(getModelBetweenInt());
                    advancedVars.add(getModelWithinInt());
                    advancedVars.add(getDecompMeanCount());
                    advancedVars.add(getDecompBSCount());
                    advancedVars.add(getDecompWSCount());
                    advancedVars.add(getAdvancedConvergence());
                    advancedVars.add(getAdvancedQuadPoints());
                    advancedVars.add(getAdvancedAdaptiveQuad());
                    advancedVars.add(getAdvancedMaxIteration());
                    advancedVars.add(getAdvancedMissingValue());
                    advancedVars.add(getAdvancedCenterScale());
                    advancedVars.add(getAdvancedEffectMeanWS());
                    advancedVars.add(getAdvancedRidge());
                    break;
                case MIXREGMLS_MIXREG_KEY:
                    advancedVars.add(getDataVariableCount());
                    advancedVars.add(getModelMeanCount());
                    advancedVars.add(getModelLocRanCount());
                    advancedVars.add(getModelScaleCount());
                    advancedVars.add(getModelFixedInt());
                    advancedVars.add(getModelRandomInt());
                    advancedVars.add(getModelScaleInt());
                    advancedVars.add(getDecompMeanCount());
                    advancedVars.add(getDecompLocRanCount());
                    advancedVars.add(getDecompScaleCount());
                    advancedVars.add(getAdvancedConvergence());
                    advancedVars.add(getAdvancedQuadPoints());
                    advancedVars.add(getAdvancedAdaptiveQuad());
                    advancedVars.add(getAdvancedMaxIteration());
                    advancedVars.add(getAdvancedMissingValue());
                    advancedVars.add(getAdvancedCenterScale());
                    advancedVars.add(getAdvancedEffectMeanWS());
                    advancedVars.add(getAdvancedRidge());
                    break;
                case MIXREGMLS_MIXOR_KEY:
                    advancedVars.add(getDataVariableCount());
                    advancedVars.add(getModelMeanCount());
                    advancedVars.add(getModelLocRanCount());
                    advancedVars.add(getModelScaleCount());
                    advancedVars.add(getModelFixedInt());
                    advancedVars.add(getModelRandomInt());
                    advancedVars.add(getModelScaleInt());
                    advancedVars.add(getDecompMeanCount());
                    advancedVars.add(getDecompLocRanCount());
                    advancedVars.add(getDecompScaleCount());
                    advancedVars.add(getAdvancedConvergence());
                    advancedVars.add(getAdvancedQuadPoints());
                    advancedVars.add(getAdvancedAdaptiveQuad());
                    advancedVars.add(getAdvancedMaxIteration());
                    advancedVars.add(getAdvancedMissingValue());
                    advancedVars.add(getAdvancedCenterScale());
                    advancedVars.add(getAdvancedEffectMeanWS());
                    advancedVars.add(getAdvancedRidge());
                    break;
                default:
                //TODO: Log this error 
            }
        } else {
            switch (sequenceDecision()) {
                case MIXREGLS_MIXREG_KEY:
                    advancedVars.add(getStageTwoFixedCount());
                    advancedVars.add(getStageTwoLocRanInteractions());
                    advancedVars.add(getStageTwoScaleInteractions());
                    advancedVars.add(getStageTwoIntOfInteraction());
                    break;
                case MIXREGLS_MIXOR_KEY:
                    advancedVars.add(getStageTwoFixedCount());
                    advancedVars.add(getStageTwoLocRanInteractions());
                    advancedVars.add(getStageTwoScaleInteractions());
                    advancedVars.add(getStageTwoIntOfInteraction());
                    advancedVars.add(getStageTwoOutcomeCatCount());
                    break;
                case MIXREGMLS_MIXREG_KEY:
                    advancedVars.add(getStageTwoFixedCount());
                    advancedVars.add(getStageTwoLocRanInteractions());
                    advancedVars.add(getStageTwoScaleInteractions());
                    advancedVars.add(getStageTwoIntOfInteraction());
                    break;
                case MIXREGMLS_MIXOR_KEY:
                    advancedVars.add(getStageTwoFixedCount());
                    advancedVars.add(getStageTwoLocRanInteractions());
                    advancedVars.add(getStageTwoScaleInteractions());
                    advancedVars.add(getStageTwoIntOfInteraction());
                    advancedVars.add(getStageTwoOutcomeCatCount());
                    break;
                default:
                //TODO: Log this error 
            }
        }
        String[] returnVars = new String[advancedVars.size()];
        int iter = 0;
        for (String iterate : advancedVars) {
            returnVars[iter] = iterate;
            iter++;
        };
        return returnVars;
    }

    private void advancedVariableAssignment(int stage, String[] advancedVars) throws Exception {
        if (stage == 1) {
            setDataVariableCount(advancedVars[0]);
            setModelMeanCount(advancedVars[1]);
            setModelFixedInt(advancedVars[4]);
            setDecompMeanCount(advancedVars[7]);
            setAdvancedConvergence(advancedVars[10]);
            setAdvancedQuadPoints(advancedVars[11]);
            setAdvancedAdaptiveQuad(advancedVars[12]);
            setAdvancedMaxIteration(advancedVars[13]);
            setAdvancedMissingValue(advancedVars[14]);
            setAdvancedCenterScale(advancedVars[15]);

            switch (sequenceDecision()) {
                case MIXREGLS_MIXREG_KEY:
                    setModelBetweenCount(advancedVars[2]);
                    setModelWithinCount(advancedVars[3]);
                    setModelBetweenInt(advancedVars[5]);
                    setModelWithinInt(advancedVars[6]);
                    setDecompBSCount(advancedVars[8]);
                    setDecompWSCount(advancedVars[9]);
                    setAdvancedEffectMeanWS(advancedVars[16]);
                    setAdvancedRidge(advancedVars[17]);
                    break;
                case MIXREGLS_MIXOR_KEY:
                    setModelBetweenCount(advancedVars[2]);
                    setModelWithinCount(advancedVars[3]);
                    setModelBetweenInt(advancedVars[5]);
                    setModelWithinInt(advancedVars[6]);
                    setDecompBSCount(advancedVars[8]);
                    setDecompWSCount(advancedVars[9]);
                    setAdvancedEffectMeanWS(advancedVars[16]);
                    setAdvancedRidge(advancedVars[17]);
                    break;
                case MIXREGMLS_MIXREG_KEY:
                    setModelLocRanCount(advancedVars[2]);
                    setModelScaleCount(advancedVars[3]);
                    setModelRandomInt(advancedVars[5]);
                    setModelScaleInt(advancedVars[6]);
                    setDecompLocRanCount(advancedVars[8]);
                    setDecompScaleCount(advancedVars[9]);
                    setAdvancedEffectMeanWS(advancedVars[16]);
                    setAdvancedRidge(advancedVars[17]);
                    break;
                case MIXREGMLS_MIXOR_KEY:
                    setModelLocRanCount(advancedVars[2]);
                    setModelScaleCount(advancedVars[3]);
                    setModelRandomInt(advancedVars[5]);
                    setModelScaleInt(advancedVars[6]);
                    setDecompLocRanCount(advancedVars[8]);
                    setDecompScaleCount(advancedVars[9]);
                    setAdvancedEffectMeanWS(advancedVars[16]);
                    setAdvancedRidge(advancedVars[17]);
                    break;
                default:
                //TODO: Log this error 
            }
        } else {
            setStageTwoFixedCount(advancedVars[0]);
            setStageTwoLocRanInteractions(advancedVars[1]);
            setStageTwoScaleInteractions(advancedVars[2]);
            setStageTwoIntOfInteraction(advancedVars[3]);

            switch (sequenceDecision()) {
                case MIXREGLS_MIXREG_KEY:
                    break;
                case MIXREGLS_MIXOR_KEY:
                    setStageTwoOutcomeCatCount(advancedVars[4]);
                    break;
                case MIXREGMLS_MIXREG_KEY:
                    break;
                case MIXREGMLS_MIXOR_KEY:
                    setStageTwoOutcomeCatCount(advancedVars[4]);
                    break;

            default:
               break;
            }
        }
    }

    /**
     *
     * @param validationMessage line name to throw in Exception message
     * @param lineMessage line number to throw in Exception message
     * @param validationString String variable that will be tested as Integer
     * @param minValue minimum value expressed as integer
     * @param maxValue maximum value expressed as integer
     * @param isInteger is validationString an integer (TRUE) or a string
     * (FALSE)
     * @return only returns true, otherwise throws Exception
     * @throws Exception inherited exception
     */
    private boolean setValidator(String validationMessage, String lineMessage,
            String validationString, int minValue, int maxValue, boolean isInteger) throws Exception {
        if (isInteger) {
            try {
                if (Integer.parseInt(validationString) >= minValue && Integer.parseInt(validationString) <= maxValue) {
                    return Boolean.TRUE;
                } else {
                    throw new Exception("Invalid " + validationMessage + " in .dat file specified, line " + lineMessage);
                }
            } catch (NumberFormatException nfe) {
                throw new Exception("Invalid character for " + validationMessage + " in .dat file specified, line " + lineMessage);
            }
        } else {
            if (validationString.length() >= minValue && validationString.length() <= maxValue) {
                return Boolean.TRUE;
            } else {
                throw new Exception("Invalid string for " + validationMessage + " in .dat file specified, line " + lineMessage);
            }
        }
    }

    /**
     * inherits parameters of setValidator, loops until all true, otherwise
     * throws Exception
     *
     * @param validationMessage
     * @param lineMessage
     * @param validationString String array to test
     * @param minValue
     * @param maxValue #param isInteger
     * @return returns true
     * @throws Exception inherited Exception from setValidator
     */
    private boolean loopSetValidator(String validationMessage, String lineMessage,
            String[] validationString, int minValue, int maxValue, boolean isInteger) throws Exception {
        int loopCounter = 0;
        for (String testString : validationString) {
            if (!setValidator(validationMessage, lineMessage, testString, minValue, maxValue, isInteger)) {
                return Boolean.FALSE;
            } else {
                loopCounter++;
            }
        }
        if (validationString.length == loopCounter) {
            return Boolean.TRUE;
        } else {
            throw new Exception("Inconsistent spacing on line " + lineMessage + " for " + validationMessage);
        }
    }

    //read model title from NewModel.java
    //done
    public String getModelTitle() {
        return modelTitle;
    }

    // read model title in this function
    //done
    public void setModelTitle(String modelTitle) {
        if (modelTitle.length() > 72) {
            this.modelTitle = modelTitle.substring(0, 71);
        } else {
            this.modelTitle = modelTitle;
        }
    }

    public String getModelSubtitle() {
        return modelSubtitle;
    }

    // read subtitle in this function
    //done
    public void setModelSubtitle(String modelSubtitle) {
        if (modelSubtitle.length() > 72) {
            this.modelSubtitle = modelSubtitle.substring(0, 71);
        } else {
            this.modelSubtitle = modelSubtitle;
        }
    }

    // print file name path
    //done
    public String getDataFilename() {
        return dataFilename;
    }

    // read fileName
    //done
    public void setDataFilename(String dataFilename) throws Exception {
        if (dataFilename.endsWith(".dat") || dataFilename.endsWith(".csv")) {
            this.dataFilename = dataFilename;
        } else {
            throw new Exception("Filename is not a valid .dat or .csv file, line 3");
        }
    }

    public String getOutputPrefix() {
        return outputPrefix;
    }

    // check what is this function needed for?
    // done
    public void setOutputPrefix(String outputPrefix) {
        if (outputPrefix.length() > 72) {
            this.outputPrefix = outputPrefix.substring(0, 200);
        } else {
            this.outputPrefix = outputPrefix;
        }
        this.outputPrefix = outputPrefix;
    }

    public String getDataVariableCount() {
        return dataVariableCount;
    }

    // read variable array size into this one
    //done
    public void setDataVariableCount(String dataVariableCount) throws Exception {
        if (setValidator("number of variables", "5", dataVariableCount, 2, 255, MIX_INTEGER)) {
            this.dataVariableCount = dataVariableCount;
        }
    }

    public String getModelMeanCount() {
        return modelMeanCount;
    }

    // what should this do?
    //todo // mean level 1 and level 2?
    public void setModelMeanCount(String modelMeanCount) throws Exception {
        if (setValidator("number of mean regressors", "5", modelMeanCount, 0, 255, MIX_INTEGER)) {
            this.modelMeanCount = modelMeanCount;
        }
    }

    public String getModelLocRanCount() {
        return modelLocRanCount;
    }

    // read RLE into this one
    //done
    public void setModelLocRanCount(String modelLocRanCount) throws Exception {
        if (setValidator("number of location random effects", "5", modelLocRanCount, 0, 255, MIX_INTEGER)) {
            this.modelLocRanCount = modelLocRanCount;
        }
    }

    public String getModelScaleCount() {
        return modelScaleCount;
    }

    // What does this do?
    //done
    public void setModelScaleCount(String modelScaleCount) throws Exception {
        if (setValidator("number of scale regressors", "5", modelScaleCount, 0, 255, MIX_INTEGER)) {
            this.modelScaleCount = modelScaleCount;
        }
    }

    public String getModelFixedInt() {
        return modelFixedInt;
    }

    // what does this do?
    //done
    public void setModelFixedInt(String modelFixedInt) throws Exception {
        if (setValidator("fixed intercept", "5", modelFixedInt, 0, 1, MIX_INTEGER)) {
            this.modelFixedInt = modelFixedInt;
        }
    }

    public String getModelRandomInt() {
        return modelRandomInt;
    }

    // what does this do?
    //done
    public void setModelRandomInt(String modelRandomInt) throws Exception {
        if (setValidator("random intercept", "5", modelRandomInt, 0, 1, MIX_INTEGER)) {
            this.modelRandomInt = modelRandomInt;
        }
    }

    public String getModelScaleInt() {
        return modelScaleInt;
    }

    // What does this do?
    //done
    public void setModelScaleInt(String modelScaleInt) throws Exception {
        if (setValidator("scale intercept", "5", modelScaleInt, 0, 1, MIX_INTEGER)) {
            this.modelScaleInt = modelScaleInt;
        }
    }

    public String getDecompMeanCount() {
        return decompMeanCount;
    }

    //level one plus level two disagg variances
    public void setDecompMeanCount(String decompMeanCount) throws Exception {
        if (setValidator("number of mean regressors for BS/WS decomposition", "5", decompMeanCount, 0, 255, MIX_INTEGER)) {
            this.decompMeanCount = decompMeanCount;
        }
    }

    public String getDecompLocRanCount() {
        return decompLocRanCount;
    }

    // level one BS
    //done
    public void setDecompLocRanCount(String decompLocRanCount) throws Exception {
        if (setValidator("number of location random effects for BS/WS decomposition", "5", decompLocRanCount, 0, 255, MIX_INTEGER)) {
            this.decompLocRanCount = decompLocRanCount;
        }
    }

    public String getDecompScaleCount() {
        return decompScaleCount;
    }

    // level one WS
    //done
    public void setDecompScaleCount(String decompScaleCount) throws Exception {
        if (setValidator("number of scale regressors for BS/WS decomposition", "5", decompScaleCount, 0, 255, MIX_INTEGER)) {
            this.decompScaleCount = decompScaleCount;
        }
    }

    public String getAdvancedConvergence() {
        return advancedConvergence;
    }

    // read from advanced options
    //done
    public void setAdvancedConvergence(String advancedConvergence) throws Exception {
        try {
            if (Double.parseDouble(advancedConvergence) >= 0 && Double.parseDouble(advancedConvergence) <= 1) {
                this.advancedConvergence = advancedConvergence;
            } else {
                throw new Exception("Invalid convergence criteria in .dat file specified, line 5");
            }
        } catch (NumberFormatException nfe) {
            throw new Exception("Invalid character for convergence criteria in .dat file specified, line 5");
        }
    }

    public String getAdvancedQuadPoints() {
        return advancedQuadPoints;
    }

    // read from advanced options
    //done
    public void setAdvancedQuadPoints(String advancedQuadPoints) throws Exception {
        if (setValidator("number of quadrature points", "5", advancedQuadPoints, 1, 255, MIX_INTEGER)) {
            this.advancedQuadPoints = advancedQuadPoints;
        }
    }

    public String getAdvancedAdaptiveQuad() {
        return advancedAdaptiveQuad;
    }

    // read from advanced options
    //done
    public void setAdvancedAdaptiveQuad(String advancedAdaptiveQuad) throws Exception {
        if (setValidator("adaptive quadrature", "5", advancedAdaptiveQuad, 0, 1, MIX_INTEGER)) {
            this.advancedAdaptiveQuad = advancedAdaptiveQuad;
        }
    }

    public String getAdvancedMaxIteration() {
        return advancedMaxIteration;
    }

    // read from advanced options
    //done
    public void setAdvancedMaxIteration(String advancedMaxIteration) throws Exception {
        if (setValidator("maximum iterations", "5", advancedMaxIteration, 1, Integer.MAX_VALUE, MIX_INTEGER)) {
            this.advancedMaxIteration = advancedMaxIteration;
        }
    }

    public String getAdvancedMissingValue() {
        return advancedMissingValue;
    }

    // read from advanced options
    //done
    public void setAdvancedMissingValue(String advancedMissingValue) throws Exception {

        System.out.print("Missing Value: " + advancedMissingValue);
        if(!advancedMissingValue.contains(".")){
            try{
                if(setValidator("missing value (integer)", "5", advancedMissingValue, Integer.MIN_VALUE, Integer.MAX_VALUE, MIX_INTEGER)){
                    this.advancedMissingValue = advancedMissingValue;
                }
            }
            catch(NumberFormatException nfe) {
                throw new Exception("Invalid character for missing value in .dat file specified, line 5");
            }
        }
        else{
            try { 
                if(Double.parseDouble(advancedMissingValue)>=(-Double.MAX_VALUE) && Double.parseDouble(advancedMissingValue)<=Double.MAX_VALUE){
                    this.advancedMissingValue = advancedMissingValue;
                }
                else {throw new Exception("Invalid missing value (double) in .dat file specified, line 5");}
            }
            catch(NumberFormatException nfe) {
                throw new Exception("Invalid character for missing value in .dat file specified, line 5");
            }
 
        }
    }

    public String getAdvancedCenterScale() {
        return advancedCenterScale;
    }

    //done
    public void setAdvancedCenterScale(String advancedCenterScale) throws Exception {
        if (setValidator("scale centering", "5", advancedCenterScale, 0, 1, MIX_INTEGER)) {
            this.advancedCenterScale = advancedCenterScale;
        }
    }

    public String getAdvancedRidge() {
        return advancedRidge;
    }

    // read from advanced options
    //done
    public void setAdvancedRidge(String advancedRidge) throws Exception {
        try {
            if (Double.parseDouble(advancedRidge) >= 0 && Double.parseDouble(advancedRidge) <= 1) {
                this.advancedRidge = advancedRidge;
            } else {
                throw new Exception("Invalid initial ridge value in .dat file specified, line 5");
            }
        } catch (NumberFormatException nfe) {
            throw new Exception("Invalid character for initial ridge value in .dat file specified, line 5");
        }
    }

    public String getModelBetweenCount() {
        return modelBetweenCount;
    }

    // TOTAL LEVEL TWO REGS
    //done
    public void setModelBetweenCount(String modelBetweenCount) throws Exception {
        if (setValidator("number of between-subject variance regressors", "5", modelBetweenCount, 0, 255, MIX_INTEGER)) {
            this.modelBetweenCount = modelBetweenCount;
        }
    }

    public String getModelWithinCount() {
        return modelWithinCount;
    }

    // TOTAL LEVEL ONE REGS
    //done
    public void setModelWithinCount(String modelWithinCount) throws Exception {
        if (setValidator("number of within-subject variance regressors", "5", modelWithinCount, 0, 255, MIX_INTEGER)) {
            this.modelWithinCount = modelWithinCount;
        }
    }

    // to do
    //done
    public String getModelBetweenInt() {
        return modelBetweenInt;
    }

    // done
    public void setModelBetweenInt(String modelBetweenInt) throws Exception {
        if (setValidator("between-subject variance intercept", "5", modelBetweenInt, 0, 1, MIX_INTEGER)) {
            this.modelBetweenInt = modelBetweenInt;
        }
    }

    // to do
    public String getModelWithinInt() {
        return modelWithinInt;
    }

    // done
    public void setModelWithinInt(String modelWithinInt) throws Exception {
        if (setValidator("within-subject variance intercept", "5", modelWithinInt, 0, 1, MIX_INTEGER)) {
            this.modelWithinInt = modelWithinInt;
        }
    }

    public String getDecompBSCount() {
        return decompBSCount;
    }

    // done 
    public void setDecompBSCount(String decompBSCount) throws Exception {
        if (setValidator("number of between-subject variance regressors for BS/WS decomposition", "5", decompBSCount, 0, 255, MIX_INTEGER)) {
            this.decompBSCount = decompBSCount;
        }
    }

    public String getDecompWSCount() {
        return decompWSCount;
    }

    // tested
    // done
    public void setDecompWSCount(String decompWSCount) throws Exception {
        if (setValidator("number of within-subject variance regressors for BS/WS decomposition", "5", decompWSCount, 0, 255, MIX_INTEGER)) {
            this.decompWSCount = decompWSCount;
        }
    }

    public String getAdvancedEffectMeanWS() {
        return advancedEffectMeanWS;
    }

    // to do?
    // tested?
    public void setAdvancedEffectMeanWS(String advancedEffectMeanWS) throws Exception {
        if(randomLocationEffects>1){
            if (setValidator("association of random location & scale", "5", advancedEffectMeanWS, 0, 1, MIX_INTEGER)) {
                this.advancedEffectMeanWS = advancedEffectMeanWS;
            }
        }
        else {
            if (setValidator("effect of mean on WS variance", "5", advancedEffectMeanWS, 0, 2, MIX_INTEGER)) {
                this.advancedEffectMeanWS = advancedEffectMeanWS;
            }
        }
    }

    public String[] getIdOutcome() {
        return idOutcome;
    }

    // set ID and Outcome variable index numbers in the data set
    // done
    public void setIdOutcome(String[] idOutcome) throws Exception {
 
        System.out.print("ID and Outcome: " + Arrays.toString(idOutcome));
        if(setValidator("id location", "6", idOutcome[0], 0, 255, MIX_INTEGER)){
            if(setValidator("outcome location", "6", idOutcome[1], 0, 255, MIX_INTEGER)){
 
                this.idOutcome = idOutcome;
            }
        }
    }

    public String[] getFieldModelMeanRegressors() {
        System.out.println("DEF HELPER MEAN FIELDS (GET): " + Arrays.toString(fieldModelMeanRegressors));
        return fieldModelMeanRegressors;
    }

    //fix
    public void setFieldModelMeanRegressors(String[] fieldModelMeanRegressors) throws Exception {
        
        System.out.println("DEF HELPER MEAN FIELDS:  " + Arrays.toString(fieldModelMeanRegressors));
        System.out.println("The valueeee"+ getModelMeanCount().toString());
        if (getModelMeanCount().equals("0")){
            
            this.fieldModelMeanRegressors = new String[0];
            System.out.println("DEF HELPER MEAN FIELDS (IF):  " + Arrays.toString(this.fieldModelMeanRegressors));
        }
        else if (loopSetValidator("model mean regressor fields", "7", fieldModelMeanRegressors, 0, 255, MIX_INTEGER)) {
            this.fieldModelMeanRegressors = fieldModelMeanRegressors;
            System.out.println("DEF HELPER MEAN FIELDS (ELSE): " + Arrays.toString(this.fieldModelMeanRegressors));
        }
    }

    public String[] getFieldModelBSRegressors() {
        
        return fieldModelBSRegressors;
    }

    // fix
    public void setFieldModelBSRegressors(String[] fieldModelBSRegressors) throws Exception {
        if (getModelBetweenCount().matches("0")){
            this.fieldModelBSRegressors = new String[0];
        }
        else if(loopSetValidator("model BS variance regressor fields", "8", fieldModelBSRegressors, 0, 255, MIX_INTEGER)) {
            this.fieldModelBSRegressors = fieldModelBSRegressors;
        }
    }

    public String[] getFieldModelWSRegressors() {
        return fieldModelWSRegressors;
    }

    // fix
    public void setFieldModelWSRegressors(String[] fieldModelWSRegressors) throws Exception {
        if (getModelWithinCount().matches("0")){
            this.fieldModelWSRegressors = new String[0];
        }
        else if(loopSetValidator("model WS variance regressor fields", "9", fieldModelWSRegressors, 0, 255, MIX_INTEGER)) {
            this.fieldModelWSRegressors = fieldModelWSRegressors;
        }
    }

    public String[] getFieldModelLocRanRegressors() {
        return fieldModelLocRanRegressors;
    }

    // fix
    public void setFieldModelLocRanRegressors(String[] fieldModelLocRanRegressors) throws Exception {
        if (getModelLocRanCount().matches("0")){
            this.fieldModelLocRanRegressors = new String[0];
        }
        else if(loopSetValidator("model random regressor fields", "8", fieldModelLocRanRegressors, 0, 255, MIX_INTEGER)) {
            this.fieldModelLocRanRegressors = fieldModelLocRanRegressors;
        }
    }

    public String[] getFieldModelScaleRegressors() {
        return fieldModelScaleRegressors;
    }

    // fix
    public void setFieldModelScaleRegressors(String[] fieldModelScaleRegressors) throws Exception {
        if (getModelScaleCount().matches("0")){
            this.fieldModelScaleRegressors = new String[0];
        }
        else if (loopSetValidator("model scale regressor fields", "9", fieldModelScaleRegressors, 0, 255, MIX_INTEGER)) {
            this.fieldModelScaleRegressors = fieldModelScaleRegressors;
        }
    }

    public String[] getFieldDecompMeanRegressors() {
        return fieldDecompMeanRegressors;
    }

    // done
    public void setFieldDecompMeanRegressors(String[] fieldDecompMeanRegressors) throws Exception {
        if (getDecompMeanCount().matches("0")){
            this.fieldDecompMeanRegressors = new String[0];
        }
        else if (loopSetValidator("model mean regressor for BS/WS decomposition fields", "10", fieldDecompMeanRegressors, 0, 255, MIX_INTEGER)) {
            this.fieldDecompMeanRegressors = fieldDecompMeanRegressors;
        }
    }

    public String[] getFieldDecompBSRegressors() {
        return fieldDecompBSRegressors;
    }

    // done
    public void setFieldDecompBSRegressors(String[] fieldDecompBSRegressors) throws Exception {
        if (getDecompBSCount().matches("0")){
            this.fieldDecompBSRegressors = new String[0];
        }
        else if (loopSetValidator("model BS variance regressor for BS/WS decomposition fields", "11", fieldDecompBSRegressors, 0, 255, MIX_INTEGER)) {
            this.fieldDecompBSRegressors = fieldDecompBSRegressors;
        }
    }

    public String[] getFieldDecompWSRegressors() {
        return fieldDecompWSRegressors;
    }

    // done
    public void setFieldDecompWSRegressors(String[] fieldDecompWSRegressors) throws Exception {
        if (getDecompWSCount().matches("0")){
            this.fieldDecompWSRegressors = new String[0];
        }
        else if (loopSetValidator("model WS variance regressor for BS/WS decomposition fields", "12", fieldDecompWSRegressors, 0, 255, MIX_INTEGER)) {
            this.fieldDecompWSRegressors = fieldDecompWSRegressors;
        }
    }

    public String[] getFieldDecompLocRanRegressors() {
        return fieldDecompLocRanRegressors;
    }

    // done
    public void setFieldDecompLocRanRegressors(String[] fieldDecompLocRanRegressors) throws Exception {
        if (getDecompLocRanCount().matches("0")){
            this.fieldDecompLocRanRegressors = new String[0];
        }
        else if (loopSetValidator("model random regressor for BS/WS decomposition fields", "11", fieldDecompLocRanRegressors, 0, 255, MIX_INTEGER)) {
            this.fieldDecompLocRanRegressors = fieldDecompLocRanRegressors;
        }
    }
    //done
    public String[] getFieldDecompScaleRegressors() {
        return fieldDecompScaleRegressors;
    }

    // done
    public void setFieldDecompScaleRegressors(String[] fieldDecompScaleRegressors) throws Exception {
        if (getDecompScaleCount().matches("0")){
            this.fieldDecompScaleRegressors = new String[0];
        }
        else if (loopSetValidator("model scale regressor for BS/WS decomposition fields", "12", fieldDecompScaleRegressors, 0, 255, MIX_INTEGER)) {
            this.fieldDecompScaleRegressors = fieldDecompScaleRegressors;
        }
    }

    public String getLabelModelOutcome() {
        return labelModelOutcome;
    }
    //done
    public void setLabelModelOutcome(String labelModelOutcome) throws Exception {
        if (setValidator("model scale regressor for BS/WS decomposition fields", "13", labelModelOutcome, 1, 255, MIX_STRING)) {
            this.labelModelOutcome = labelModelOutcome;
        }
    }

    // done
    public String[] getLabelModelMeanRegressors() {
        return labelModelMeanRegressors;
    }
    // done
    public void setLabelModelMeanRegressors(String[] labelModelMeanRegressors) throws Exception {
        System.out.println("The Valueeeee again: " + getModelMeanCount());
        if (getModelMeanCount().matches("0")){
            
            this.labelModelMeanRegressors = new String[0];
        }
        else if (loopSetValidator("model mean regressor labels", "14", labelModelMeanRegressors, 1, 255, MIX_STRING)) {
            this.labelModelMeanRegressors = labelModelMeanRegressors;
        }
    }

    public String[] getLabelModelLocRanRegressors() {
        return labelModelLocRanRegressors;
    }

    // done
    public void setLabelModelLocRanRegressors(String[] labelModelLocRanRegressors) throws Exception {
        if (getModelLocRanCount().matches("0")){
            this.labelModelLocRanRegressors = new String[0];
        }
        else if (loopSetValidator("model random regressor labels", "15", labelModelLocRanRegressors, 1, 255, MIX_STRING)) {
            this.labelModelLocRanRegressors = labelModelLocRanRegressors;
        }
    }

    public String[] getLabelModelScaleRegressors() {
        return labelModelScaleRegressors;
    }

    // done
    public void setLabelModelScaleRegressors(String[] labelModelScaleRegressors) throws Exception {
        if (getModelScaleCount().matches("0")){
            this.labelModelScaleRegressors = new String[0];
        }
        else if (loopSetValidator("model scale regressor labels", "16", labelModelScaleRegressors, 1, 255, MIX_STRING)) {
            this.labelModelScaleRegressors = labelModelScaleRegressors;
        }
    }

    public String[] getLabelModelBSRegressors() {
        return labelModelBSRegressors;
    }

    // done
    public void setLabelModelBSRegressors(String[] labelModelBSRegressors) throws Exception {
        if (getModelBetweenCount().matches("0")){
            this.labelModelBSRegressors = new String[0];
        }
        else if (loopSetValidator("model BS variance regressor labels", "15", labelModelBSRegressors, 1, 255, MIX_STRING)) {
            this.labelModelBSRegressors = labelModelBSRegressors;
        }
    }

    public String[] getLabelModelWSRegressors() {
        return labelModelWSRegressors;
    }

    // done
    public void setLabelModelWSRegressors(String[] labelModelWSRegressors) throws Exception {
        if (getModelWithinCount().matches("0")){
            this.labelModelWSRegressors = new String[0];
        }
        else if (loopSetValidator("model WS variance regressor labels", "16", labelModelWSRegressors, 1, 255, MIX_STRING)) {
            this.labelModelWSRegressors = labelModelWSRegressors;
        }
    }

    public String[] getLabelDecompMeanRegressors() {
        return labelDecompMeanRegressors;
    }

    // done
    public void setLabelDecompMeanRegressors(String[] labelDecompMeanRegressors) throws Exception {
        if (getDecompMeanCount().matches("0")){
            this.labelDecompMeanRegressors = new String[0];
        }
        else if (loopSetValidator("model mean regressor for BS/WS decomposition labels", "17", labelDecompMeanRegressors, 1, 255, MIX_STRING)) {
            this.labelDecompMeanRegressors = labelDecompMeanRegressors;
        }
    }

    public String[] getLabelDecompLocRanRegressors() {
        return labelDecompLocRanRegressors;
    }

    // done
    public void setLabelDecompLocRanRegressors(String[] labelDecompLocRanRegressors) throws Exception {
        if (getDecompLocRanCount().matches("0")){
            this.labelDecompLocRanRegressors = new String[0];
        }
        else if (loopSetValidator("model random regressor for BS/WS decomposition labels", "18", labelDecompLocRanRegressors, 1, 255, MIX_STRING)) {
            this.labelDecompLocRanRegressors = labelDecompLocRanRegressors;
        }
    }

    public String[] getLabelDecompScaleRegressors() {
        return labelDecompScaleRegressors;
    }

    // done
    public void setLabelDecompScaleRegressors(String[] labelDecompScaleRegressors) throws Exception {
        if (getDecompScaleCount().matches("0")){
            this.labelDecompScaleRegressors = new String[0];
        }
        else if (loopSetValidator("model scale regressor for BS/WS decomposition labels", "19", labelDecompScaleRegressors, 1, 255, MIX_STRING)) {
            this.labelDecompScaleRegressors = labelDecompScaleRegressors;
        }
    }

    public String[] getLabelDecompBSRegressors() {
        return labelDecompBSRegressors;
    }

    // done
    public void setLabelDecompBSRegressors(String[] labelDecompBSRegressors) throws Exception {
        if (getDecompBSCount().matches("0")){
            this.labelDecompBSRegressors = new String[0];
        } 
        else if (loopSetValidator("model BS variance regressor for BS/WS decomposition labels", "18", labelDecompBSRegressors, 1, 255, MIX_STRING)) {
            this.labelDecompBSRegressors = labelDecompBSRegressors;
        }
    }

    public String[] getLabelDecompWSRegressors() {
        return labelDecompWSRegressors;
    }

    // done
    public void setLabelDecompWSRegressors(String[] labelDecompWSRegressors) throws Exception {
        if (getDecompWSCount().matches("0")){
            this.labelDecompWSRegressors = new String[0];
        } 
        else if (loopSetValidator("model WS variance regressor for BS/WS decomposition labels", "19", labelDecompWSRegressors, 1, 255, MIX_STRING)) {
            this.labelDecompWSRegressors = labelDecompWSRegressors;
        }
    }
    
    public String getStageTwoFixedCount() {
        return stageTwoFixedCount;
    }

    // done
    public void setStageTwoFixedCount(String stageTwoFixedCount) throws Exception {
        if (setValidator("number of fixed regressors in stage 2", "20", stageTwoFixedCount, 0, 255, MIX_INTEGER)) {
            this.stageTwoFixedCount = stageTwoFixedCount;
        }
    }

    public String getStageTwoLocRanInteractions() {
        return stageTwoLocRanInteractions;
    }

    // done
    public void setStageTwoLocRanInteractions(String stageTwoLocRanInteractions) throws Exception {
        if (setValidator("number of interactions with location random effects in stage 2", "20", stageTwoLocRanInteractions, 0, 255, MIX_INTEGER)) {
            this.stageTwoLocRanInteractions = stageTwoLocRanInteractions;
        }
    }

    public String getStageTwoScaleInteractions() {
        return stageTwoScaleInteractions;
    }

    // done
    public void setStageTwoScaleInteractions(String stageTwoScaleInteractions) throws Exception {
        if (setValidator("number of interactions with scale random effects in stage 2", "20", stageTwoScaleInteractions, 0, 255, MIX_INTEGER)) {
            this.stageTwoScaleInteractions = stageTwoScaleInteractions;
        }
    }

    public String getStageTwoIntOfInteraction() {
        return stageTwoIntOfInteraction;
    }

    // to do
    public void setStageTwoIntOfInteraction(String stageTwoIntOfInteraction) throws Exception {
        if (setValidator("number of interactions with interaction of location and scale random effects in stage 2", "20", stageTwoIntOfInteraction, -1, 255, MIX_INTEGER)) {
            this.stageTwoIntOfInteraction = stageTwoIntOfInteraction;
        }
    }

    public String getStageTwoOutcomeCatCount() {
        return stageTwoOutcomeCatCount;
    }

    // todo
    public void setStageTwoOutcomeCatCount(String stageTwoOutcomeCatCount) throws Exception {
        if (setValidator("number of categories for the outcome in stage 2", "20", stageTwoOutcomeCatCount, 2, 255, MIX_INTEGER)) {
            this.stageTwoOutcomeCatCount = stageTwoOutcomeCatCount;
        }
    }

    public String getStageTwoOutcomeField() {
        return stageTwoOutcomeField;
    }

    // done
    public void setStageTwoOutcomeField(String stageTwoOutcomeField) throws Exception {
        if (setValidator("outcome field in stage 2", "21", stageTwoOutcomeField, 0, 255, MIX_INTEGER)) {
            this.stageTwoOutcomeField = stageTwoOutcomeField;
        }
    }

    public String[] getStageTwoOutcomeCatLabel() {
        return stageTwoOutcomeCatLabel;
    }

    //todo
    public void setStageTwoOutcomeCatLabel(String[] stageTwoOutcomeCatLabel) throws Exception {
        if (loopSetValidator("numeric categories of outcome variable", "21", stageTwoOutcomeCatLabel, 0, 255, MIX_INTEGER)) {
            this.stageTwoOutcomeCatLabel = stageTwoOutcomeCatLabel;
        }
    }

    public String[] getStageTwoFixedFields() {
        return stageTwoFixedFields;
    }

    
    //done
    public void setStageTwoFixedFields(String[] stageTwoFixedFields) throws Exception {
        if (getStageTwoFixedCount().matches("0")){
            this.stageTwoFixedFields = new String[0];
        } 
        else if (loopSetValidator("fields of fixed regressors", "22(mixreg)/23((mixor)", stageTwoFixedFields, 1, 255, MIX_INTEGER)) {
            this.stageTwoFixedFields = stageTwoFixedFields;
        }
    }

    public String[] getStageTwoLocRanIntFields() {
        return stageTwoLocRanIntFields;
    }

    // done
    public void setStageTwoLocRanIntFields(String[] stageTwoLocRanIntFields) throws Exception {
        if (getStageTwoLocRanInteractions().matches("0")){
            this.stageTwoLocRanIntFields = new String[0];
        } 
        else if (loopSetValidator("fields of regressors to interact with location random effects", "23(mixreg)/24((mixor)", stageTwoLocRanIntFields, 1, 255, MIX_INTEGER)) {
            this.stageTwoLocRanIntFields = stageTwoLocRanIntFields;
        }
    }

    public String[] getStageTwoScaleIntFields() {
        return stageTwoScaleIntFields;
    }

    // done
    public void setStageTwoScaleIntFields(String[] stageTwoScaleIntFields) throws Exception {
        if (getStageTwoScaleInteractions().matches("0")){
            this.stageTwoScaleIntFields = new String[0];
        } 
        else if (loopSetValidator("fields of regressors to interact with scale random effects", "24(mixreg)/25((mixor)", stageTwoScaleIntFields, 1, 255, MIX_INTEGER)) {
            this.stageTwoScaleIntFields = stageTwoScaleIntFields;
        }
    }

    public String[] getStageTwoFirstIntFields() {
        return stageTwoFirstIntFields;
    }

    public void setStageTwoFirstIntFields(String[] stageTwoFirstIntFields) throws Exception {
        if (getStageTwoIntOfInteraction().matches("0")){
            this.stageTwoFirstIntFields = new String[0];
        } 
        else if  (loopSetValidator("fields of regressors to interact with the interaction of the location random effects", "25(mixreg)/26((mixor)", stageTwoFirstIntFields, 1, 255, MIX_INTEGER)) {
            this.stageTwoFirstIntFields = stageTwoFirstIntFields;
        }
    }

    public String getStageTwoOutcomeLabel() {
        return stageTwoOutcomeLabel;
    }

    // done
    public void setStageTwoOutcomeLabel(String stageTwoOutcomeLabel) throws Exception {
        if (setValidator("label of stage two outcome", "26(mixreg)/27((mixor)", stageTwoOutcomeLabel, 1, 255, MIX_STRING)) {
            this.stageTwoOutcomeLabel = stageTwoOutcomeLabel;
        }
    }

    public String[] getStageTwoFixedLabels() {
        return stageTwoFixedLabels;
    }

    // done
    public void setStageTwoFixedLabels(String[] stageTwoFixedLabels) throws Exception {
        if (getStageTwoFixedCount().matches("0")){
            this.stageTwoFixedLabels = new String[0];
        } 
        else if (loopSetValidator("label of stage two fixed regressors", "27(mixreg)/28((mixor)", stageTwoFixedLabels, 1, 255, MIX_STRING)) {
            this.stageTwoFixedLabels = stageTwoFixedLabels;
        }
    }

    public String[] getStageTwoLocRanIntLabels() {
        return stageTwoLocRanIntLabels;
    }

    // done
    public void setStageTwoLocRanIntLabels(String[] stageTwoLocRanIntLabels) throws Exception {
        if (getStageTwoLocRanInteractions().matches("0")){
            this.stageTwoLocRanIntLabels = new String[0];
        } 
        else if (loopSetValidator("labels of stage two regressors to interact with location random effect", "28(mixreg)/29((mixor)", stageTwoLocRanIntLabels, 1, 255, MIX_STRING)) {
            this.stageTwoLocRanIntLabels = stageTwoLocRanIntLabels;
        }
    }

    public String[] getStageTwoScaleIntLabels() {
        return stageTwoScaleIntLabels;
    }

    // done
    public void setStageTwoScaleIntLabels(String[] stageTwoScaleIntLabels) throws Exception {
        if (getStageTwoScaleInteractions().matches("0")){
            this.stageTwoScaleIntLabels = new String[0];
        } 
        else if  (loopSetValidator("labels of stage two regressors to interact with scale effect", "29(mixreg)/30((mixor)", stageTwoScaleIntLabels, 1, 255, MIX_STRING)) {
            this.stageTwoScaleIntLabels = stageTwoScaleIntLabels;
        }
    }

    public String[] getStageTwoFirstIntLabels() {
        return stageTwoFirstIntLabels;
    }

    public void setStageTwoFirstIntLabels(String[] stageTwoFirstIntLabels) throws Exception {
        if (getStageTwoIntOfInteraction().matches("0")){
            this.stageTwoFirstIntLabels = new String[0];
        } 
        else if (loopSetValidator("labels of stage two regressors to interact with the interaction of the location random effect", "30(mixreg)/31(mixor)", stageTwoFirstIntLabels, 1, 255, MIX_STRING)) {
            this.stageTwoFirstIntLabels = stageTwoFirstIntLabels;
        }
    }
    
    
    public void writeDefFileToFolder(){
        
        try{
            myFrame = new JFrame("Definition File Preview");
        
            GridLayout defFileGrid = new GridLayout(0,2);
            
            FlowLayout defFileFlow = new FlowLayout();
        
            myFrame.setLayout(defFileFlow);
            defFileFlow.setAlignment(FlowLayout.TRAILING);
            myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            myFrame.setSize(550,550);
            
           
            myPane = new JEditorPane();
            myPane.setSize(500, 500);
            myPane.setContentType("text/plain");
            myPane.setFont(new Font("Monospaced", 0, 12));
            try{
            myPane.setText(String.join("\n",debugStageOneDefinitonList()).replace("[", "").replace("]", ""));
            }
            catch(Exception e){
            myPane.setText(String.join("\n",debugStageOneDefinitonList()).replace("[", "").replace("]", ""));
            }
            
            JButton proceedButton = new JButton("Proceed");
            JButton saveDefFile = new JButton("Save Def File");
            
            myFrame.add(myPane);
            myFrame.add(proceedButton);
            myFrame.add(saveDefFile);
            myFrame.setComponentOrientation(ComponentOrientation.UNKNOWN);
            
            proceedButton.addActionListener(new ActionListener() {
                
                public void actionPerformed(ActionEvent e){
                    
                        
                    runModels();

                    //select the program here
                    // then read the output
                    
                    
                    myFrame.dispose();
                }
            
            });

            saveDefFile.addActionListener(new ActionListener() {
                
                public void actionPerformed(ActionEvent e){
                    try {
                        saveDefFileLocally();
                    } catch (IOException ex) {
                        Logger.getLogger(DefinitionHelper.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            
            });
            
            myFrame.setVisible(true); 
            Document defDoc = myPane.getDocument();
            int length = defDoc.getLength();
            
            File newDefFile = new File("MIXREGLS_MIXREG_KEY");
            
            if (selectedModel == DefinitionHelper.MIXREGLS_MIXREG_KEY){
                newDefFile = new File("MIXREGLS_MIXREG");
            } else if (selectedModel == DefinitionHelper.MIXREGLS_MIXOR_KEY){
            
                newDefFile = new File("MIXREGLS_MIXOR");
            } else if (selectedModel == DefinitionHelper.MIXREGMLS_MIXREG_KEY){
                
                newDefFile = new File("MIXREGMLS_MIXREG");
            } else if (selectedModel == DefinitionHelper.MIXREGMLS_MIXOR_KEY) {
            
                newDefFile = new File("MIXREGMLS_MIXOR");
            }
            
           // File newDefFile = new File("tester");
            OutputStream os = new BufferedOutputStream(
              new FileOutputStream(newDefFile + ".def"));
            Writer w = new OutputStreamWriter(os);
            myPane.write(w);
            defFilePath = newDefFile.getAbsolutePath();
            System.out.println("PATH-NAME: " + defFilePath);
            w.close();
        }
        catch(Exception exception){
            exception.printStackTrace();
        }
    
    
    }
    
    public void saveDefFileLocally() throws IOException{
        
      FileFilter filter = new FileNameExtensionFilter("TEXT FILE","txt");
    
      JFileChooser saver = new JFileChooser("./");
        saver.setFileFilter(filter);
        int returnVal = saver.showSaveDialog(myFrame);
        File file = saver.getSelectedFile();
        BufferedWriter writer = null;
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            try
            {
            writer = new BufferedWriter( new FileWriter( file.getName()+".txt"));
            writer.write( myPane.getText());
            writer.close( );
            JOptionPane.showMessageDialog(myFrame, "The Message was Saved Successfully!",
                        "Success!", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (IOException e)
            {
            JOptionPane.showMessageDialog(myFrame, "The Text could not be Saved!",
                        "Error!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    public void runModels(){
    
    String absoluteJavaPath = System.getProperty( "user.dir" );
        String defFileName = executableModel(selectedModel);
        
        try {          
            try 
            { 
                copyExecutable(defFilePath, selectedModel);
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
            //JOptionPane.showMessageDialog(null, ex.getMessage() + "Failed");
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE);
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
    
    public void modelSelector(int randomLocEffects, boolean outcomeContinious){
        
        System.out.println("inside MODEL SELECTOR");
        
        if(randomLocEffects == 1 && outcomeContinious == true){
            
            selectedModel = MIXREGLS_MIXREG_KEY;
            
            System.out.println("MODEL SELECTED: " + "DEFFILE" + String.valueOf(selectedModel));
        
        } else if (randomLocEffects == 1 && outcomeContinious == false) {
            
            selectedModel = MIXREGLS_MIXOR_KEY;
            
            System.out.println("MODEL SELECTED: " + "DEFFILE" + String.valueOf(selectedModel));
        
        } else if (randomLocEffects > 1 && outcomeContinious == true){
            
            selectedModel = MIXREGMLS_MIXREG_KEY;
            
            System.out.println("MODEL SELECTED: " + "DEFFILE" + String.valueOf(selectedModel));
        
        } else if (randomLocEffects > 1 && outcomeContinious == false){
            
            selectedModel = MIXREGMLS_MIXOR_KEY;
            
            System.out.println("MODEL SELECTED: " + "DEFFILE" + String.valueOf(selectedModel));
        }
        
        //return selectedModel;
      
    }
    
    public int getSelectedModel(){
    
    return selectedModel;
    
    }
    
}
