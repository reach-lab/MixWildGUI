package def_lib;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefinitionHelper {
    public static final int MIXREGLS_MIXREG_KEY = 1;
    public static final int MIXREGLS_MIXOR_KEY = 2;
    public static final int MIXREGMLS_MIXREG_KEY = 3;
    public static final int MIXREGMLS_MIXOR_KEY = 4;
    
    private List<String> initParameters = new ArrayList<>();
    private List<String> stageOneVariables = new ArrayList<>();
    private List<String> stageTwoVariables = new ArrayList<>();
    private List<String> advancedOptions = new ArrayList<>();
    
    private int randomLocationEffects = 1;
    private boolean stageTwoBinary = Boolean.FALSE;
    /**
     * Initial Definition Parameters
     */
    public String modelTitle;
    public String modelSubtitle;
    public String dataFilename;
    public String outputPrefix;
    
    /**
     * Stage 1 Advanced Options
     */
    public String dataVariableCount;
    public String modelMeanCount;
    public String modelLocRanCount;
    public String modelScaleCount;
    public String modelFixedInt;
    public String modelRandomInt;
    public String modelScaleInt;
    public String decompMeanCount;
    public String decompLocRanCount;
    public String decompScaleCount;
    public String advancedConvergence;
    public String advancedQuadPoints;
    public String advancedAdaptiveQuad;
    public String advancedMaxIteration;
    public String advancedMissingValue;
    public String advancedCenterScale;
    public String advancedRidge;           
    public String modelBetweenCount;
    public String modelWithinCount;            
    public String modelBetweenInt;
    public String modelWithinInt;    
    public String decompBSCount;
    public String decompWSCount;          
    public String advancedEffectMeanWS;

    /**
     * Stage 1 Model Specification
     */
    public String[] idOutcome;
    public String[] fieldModelMeanRegressors;
    public String[] fieldModelBSRegressors;
    public String[] fieldModelWSRegressors;
    public String[] fieldModelLocRanRegressors;
    public String[] fieldModelScaleRegressors;
    public String[] fieldDecompMeanRegressors;
    public String[] fieldDecompBSRegressors;
    public String[] fieldDecompWSRegressors;
    public String[] fieldDecompLocRanRegressors;
    public String[] fieldDecompScaleRegressors;
    public String labelModelOutcome;
    public String[] labelModelMeanRegressors;
    public String[] labelModelLocRanRegressors;
    public String[] labelModelScaleRegressors;
    public String[] labelModelBSRegressors;
    public String[] labelModelWSRegressors;
    public String[] labelDecompMeanRegressors;
    public String[] labelDecompLocRanRegressors;
    public String[] labelDecompScaleRegressors;
    public String[] labelDecompBSRegressors;
    public String[] labelDecompWSRegressors;

    /**
     * Stage 2 Advanced Options
     */
    public String stageTwoFixedCount;
    public String stageTwoLocRanInteractions;
    public String stageTwoScaleInteractions;
    public String stageTwoIntOfInteraction;
    public String stageTwoOutcomeCatCount;
    
    /**
     * Stage 2 ModelS Specification
     */
    public String stageTwoOutcomeField;
    public String[] stageTwoOutcomeCatLabel;
    public String[] stageTwoFixedFields;
    public String[] stageTwoLocRanIntFields;
    public String[] stageTwoScaleIntFields;
    public String[] stageTwoFirstIntFields;
    public String stageTwoOutcomeLabel;
    public String[] stageTwoFixedLabels;
    public String[] stageTwoLocRanIntLabels;
    public String[] stageTwoScaleIntLabels;
    public String[] stageTwoFirstIntLabels;

    /**
     * 
     * @param randomLocationEffects: number of random location effects
     * @param stageTwoBinary : whether or not the stage two outcome is dichotomous or ordinal
     */
    public DefinitionHelper(int randomLocationEffects, boolean stageTwoBinary) {
       this.randomLocationEffects = randomLocationEffects;
       this.stageTwoBinary = stageTwoBinary;
    }

    public int getRandomLocationEffects() {
        return randomLocationEffects;
    }

    public boolean isStageTwoBinary() {
        return stageTwoBinary;
    }

    /** 
     * 
     * @return sequenceDecision: integer key determining stage 1+stage 2 model UI views
     */
    public int sequenceDecision() {
        if(stageTwoBinary){
            if(randomLocationEffects<2){return MIXREGLS_MIXOR_KEY;}
            else{return MIXREGLS_MIXREG_KEY;}
        }
        else{
            if(randomLocationEffects<2){return MIXREGMLS_MIXOR_KEY;}
            else{return MIXREGMLS_MIXREG_KEY;}
        }
    }
    
    public void readDefinitionFile(File defFile, List<String> varNames) throws FileNotFoundException, IOException, Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(defFile))) {
            String defLine;
            int row = 0;
            List<String> defSummary = new ArrayList<>();
            
            while ((defLine = br.readLine()) != null) {
                defSummary.add(defLine);
            }
            
            int fileSize = defSummary.size();
            
            if(stageTwoBinary & fileSize != 31){
                throw new Exception("Invalid definition file");  // TODO: Create a new class that extends from Exception for invalid def files
            }
            else if(!stageTwoBinary & fileSize != 30){
                throw new Exception("Invalid definition file"); 
            }         
            
            assignDefinitionVariables(defSummary);
        }
    }
    
    private void assignDefinitionVariables(List<String> readDefinitionFile) {
        modelTitle = readDefinitionFile.get(0);
        modelSubtitle = readDefinitionFile.get(1);
        dataFilename = readDefinitionFile.get(2);
        outputPrefix = readDefinitionFile.get(3);
        
        advancedVariableAssignment(1,readDefinitionFile.get(4).split(" "));
        
        idOutcome = readDefinitionFile.get(5).split(" ");
        fieldModelMeanRegressors = readDefinitionFile.get(6).split(" ");
        fieldDecompMeanRegressors = readDefinitionFile.get(9).split(" ");
        labelModelOutcome = readDefinitionFile.get(12);
        labelModelMeanRegressors = readDefinitionFile.get(13).split(" ");
        labelDecompMeanRegressors = readDefinitionFile.get(16).split(" "); 
        advancedVariableAssignment(2,readDefinitionFile.get(19).split(" ")); 
        stageTwoOutcomeField = readDefinitionFile.get(20);
        switch(sequenceDecision()){
            case MIXREGLS_MIXREG_KEY:
                fieldModelBSRegressors = readDefinitionFile.get(7).split(" ");
                fieldModelWSRegressors = readDefinitionFile.get(8).split(" ");
                fieldDecompBSRegressors = readDefinitionFile.get(10).split(" ");
                fieldDecompWSRegressors = readDefinitionFile.get(11).split(" ");
                labelModelBSRegressors = readDefinitionFile.get(14).split(" ");
                labelModelWSRegressors = readDefinitionFile.get(15).split(" ");
                labelDecompBSRegressors = readDefinitionFile.get(17).split(" ");
                labelDecompWSRegressors = readDefinitionFile.get(18).split(" ");
                stageTwoFixedFields = readDefinitionFile.get(21).split(" ");
                stageTwoLocRanIntFields = readDefinitionFile.get(22).split(" ");
                stageTwoScaleIntFields = readDefinitionFile.get(23).split(" ");
                stageTwoFirstIntFields = readDefinitionFile.get(24).split(" ");
                stageTwoOutcomeLabel = readDefinitionFile.get(25);
                stageTwoFixedLabels = readDefinitionFile.get(26).split(" ");
                stageTwoLocRanIntLabels = readDefinitionFile.get(27).split(" ");
                stageTwoScaleIntLabels = readDefinitionFile.get(28).split(" ");
                stageTwoFirstIntLabels = readDefinitionFile.get(29).split(" ");
                break;
            case MIXREGLS_MIXOR_KEY:
                fieldModelBSRegressors = readDefinitionFile.get(7).split(" ");
                fieldModelWSRegressors = readDefinitionFile.get(8).split(" ");
                fieldDecompBSRegressors = readDefinitionFile.get(10).split(" ");
                fieldDecompWSRegressors = readDefinitionFile.get(11).split(" ");
                labelModelBSRegressors = readDefinitionFile.get(14).split(" ");
                labelModelWSRegressors = readDefinitionFile.get(15).split(" ");
                labelDecompBSRegressors = readDefinitionFile.get(17).split(" ");
                labelDecompWSRegressors = readDefinitionFile.get(18).split(" ");
                stageTwoFixedFields = readDefinitionFile.get(22).split(" ");
                stageTwoLocRanIntFields = readDefinitionFile.get(23).split(" ");
                stageTwoScaleIntFields = readDefinitionFile.get(24).split(" ");
                stageTwoFirstIntFields = readDefinitionFile.get(25).split(" ");
                stageTwoOutcomeLabel = readDefinitionFile.get(26);
                stageTwoFixedLabels = readDefinitionFile.get(27).split(" ");
                stageTwoLocRanIntLabels = readDefinitionFile.get(28).split(" ");
                stageTwoScaleIntLabels = readDefinitionFile.get(29).split(" ");
                stageTwoFirstIntLabels = readDefinitionFile.get(30).split(" ");
                break;
            case MIXREGMLS_MIXREG_KEY:
                fieldModelLocRanRegressors = readDefinitionFile.get(7).split(" ");
                fieldModelScaleRegressors = readDefinitionFile.get(8).split(" ");
                fieldDecompLocRanRegressors = readDefinitionFile.get(10).split(" ");
                fieldDecompScaleRegressors = readDefinitionFile.get(11).split(" ");
                labelModelLocRanRegressors = readDefinitionFile.get(14).split(" ");
                labelModelScaleRegressors = readDefinitionFile.get(15).split(" ");
                labelDecompLocRanRegressors = readDefinitionFile.get(17).split(" ");
                labelDecompScaleRegressors = readDefinitionFile.get(18).split(" ");
                stageTwoFixedFields = readDefinitionFile.get(21).split(" ");
                stageTwoLocRanIntFields = readDefinitionFile.get(22).split(" ");
                stageTwoScaleIntFields = readDefinitionFile.get(23).split(" ");
                stageTwoFirstIntFields = readDefinitionFile.get(24).split(" ");
                stageTwoOutcomeLabel = readDefinitionFile.get(25);
                stageTwoFixedLabels = readDefinitionFile.get(26).split(" ");
                stageTwoLocRanIntLabels = readDefinitionFile.get(27).split(" ");
                stageTwoScaleIntLabels = readDefinitionFile.get(28).split(" ");
                stageTwoFirstIntLabels = readDefinitionFile.get(29).split(" ");
                break;
            case MIXREGMLS_MIXOR_KEY:
                fieldModelLocRanRegressors = readDefinitionFile.get(7).split(" ");
                fieldModelScaleRegressors = readDefinitionFile.get(8).split(" ");
                fieldDecompLocRanRegressors = readDefinitionFile.get(10).split(" ");
                fieldDecompScaleRegressors = readDefinitionFile.get(11).split(" ");
                labelModelLocRanRegressors = readDefinitionFile.get(14).split(" ");
                labelModelScaleRegressors = readDefinitionFile.get(15).split(" ");
                labelDecompLocRanRegressors = readDefinitionFile.get(17).split(" ");
                labelDecompScaleRegressors = readDefinitionFile.get(18).split(" ");
                stageTwoFixedFields = readDefinitionFile.get(22).split(" ");
                stageTwoLocRanIntFields = readDefinitionFile.get(23).split(" ");
                stageTwoScaleIntFields = readDefinitionFile.get(24).split(" ");
                stageTwoFirstIntFields = readDefinitionFile.get(25).split(" ");
                stageTwoOutcomeLabel = readDefinitionFile.get(26);
                stageTwoFixedLabels = readDefinitionFile.get(27).split(" ");
                stageTwoLocRanIntLabels = readDefinitionFile.get(28).split(" ");
                stageTwoScaleIntLabels = readDefinitionFile.get(29).split(" ");
                stageTwoFirstIntLabels = readDefinitionFile.get(30).split(" ");              
                break;
            default:
                //TODO: Log this error     
        }
    }
    
    private void advancedVariableAssignment(int stage, String[] advancedVars){
        if(stage==1){
            dataVariableCount = advancedVars[0];
            modelMeanCount = advancedVars[1];
            modelFixedInt = advancedVars[4]; 
            decompMeanCount = advancedVars[7];
            advancedConvergence = advancedVars[10];
            advancedQuadPoints = advancedVars[11];
            advancedAdaptiveQuad = advancedVars[12];
            advancedMaxIteration = advancedVars[13];
            advancedMissingValue = advancedVars[14];
            advancedCenterScale = advancedVars[15];

            switch(sequenceDecision()){
                case MIXREGLS_MIXREG_KEY:
                    modelBetweenCount = advancedVars[2];
                    modelWithinCount = advancedVars[3];
                    modelBetweenInt = advancedVars[5];
                    modelWithinInt = advancedVars[6];
                    decompBSCount = advancedVars[8];
                    decompWSCount = advancedVars[9];
                    advancedEffectMeanWS = advancedVars[16];
                    advancedRidge = advancedVars[17];
                    break;
                case MIXREGLS_MIXOR_KEY:
                    modelBetweenCount = advancedVars[2];
                    modelWithinCount = advancedVars[3];
                    modelBetweenInt = advancedVars[5];
                    modelWithinInt = advancedVars[6];
                    decompBSCount = advancedVars[8];
                    decompWSCount = advancedVars[9];
                    advancedEffectMeanWS = advancedVars[16];
                    advancedRidge = advancedVars[17];
                    break;
                case MIXREGMLS_MIXREG_KEY:
                    modelLocRanCount = advancedVars[2];
                    modelScaleCount = advancedVars[3];
                    modelRandomInt = advancedVars[5];
                    modelScaleInt = advancedVars[6];
                    decompLocRanCount = advancedVars[8];
                    decompScaleCount = advancedVars[9];
                    advancedRidge = advancedVars[16];
                    break;
                case MIXREGMLS_MIXOR_KEY:
                    modelLocRanCount = advancedVars[2];
                    modelScaleCount = advancedVars[3];
                    modelRandomInt = advancedVars[5];
                    modelScaleInt = advancedVars[6];
                    decompLocRanCount = advancedVars[8];
                    decompScaleCount = advancedVars[9];
                    advancedRidge = advancedVars[16];
                    break;
            default:
                //TODO: Log this error 
            }
        }
        else{
            stageTwoFixedCount = advancedVars[0];
            stageTwoLocRanInteractions = advancedVars[1];
            stageTwoScaleInteractions = advancedVars[2];
            stageTwoIntOfInteraction = advancedVars[3];
            
            switch(sequenceDecision()){
                case MIXREGLS_MIXREG_KEY:                  
                    break;
                case MIXREGLS_MIXOR_KEY:
                    stageTwoOutcomeCatCount = advancedVars[4];                  
                    break;
                case MIXREGMLS_MIXREG_KEY:
                    break;
                case MIXREGMLS_MIXOR_KEY:
                    stageTwoOutcomeCatCount = advancedVars[4];               
                    break;
            default:
                //TODO: Log this error 
            }
        }
    }

}