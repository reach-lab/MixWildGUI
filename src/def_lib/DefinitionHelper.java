package def_lib;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefinitionHelper {
    /**
     * Class Specific Parameters
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

    // TODO: Deprecate
    private List<String> initParameters = new ArrayList<>();
    private List<String> stageOneVariables = new ArrayList<>();
    private List<String> stageTwoVariables = new ArrayList<>();
    private List<String> advancedOptions = new ArrayList<>();
    
    private int randomLocationEffects = 1;
    private boolean stageTwoBinary = Boolean.FALSE;
    
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
    private String modelMeanCount;
    private String modelLocRanCount;
    private String modelScaleCount;
    private String modelFixedInt;
    private String modelRandomInt;
    private String modelScaleInt;
    private String decompMeanCount;
    private String decompLocRanCount;
    private String decompScaleCount;
    private String advancedConvergence;
    private String advancedQuadPoints;
    private String advancedAdaptiveQuad;
    private String advancedMaxIteration;
    private String advancedMissingValue;
    private String advancedCenterScale;
    private String advancedRidge;           
    private String modelBetweenCount;
    private String modelWithinCount;            
    private String modelBetweenInt;
    private String modelWithinInt;    
    private String decompBSCount;
    private String decompWSCount;          
    private String advancedEffectMeanWS;

    /**
     * Stage 1 Model Specification
     */
    private String[] idOutcome;
    private String[] fieldModelMeanRegressors;
    private String[] fieldModelBSRegressors;
    private String[] fieldModelWSRegressors;
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
    private String stageTwoFixedCount;
    private String stageTwoLocRanInteractions;
    private String stageTwoScaleInteractions;
    private String stageTwoIntOfInteraction;
    private String stageTwoOutcomeCatCount;
    
    /**
     * Stage 2 ModelS Specification
     */
    private String stageTwoOutcomeField;
    private String[] stageTwoOutcomeCatLabel;
    private String[] stageTwoFixedFields;
    private String[] stageTwoLocRanIntFields;
    private String[] stageTwoScaleIntFields;
    private String[] stageTwoFirstIntFields;
    private String stageTwoOutcomeLabel;
    private String[] stageTwoFixedLabels;
    private String[] stageTwoLocRanIntLabels;
    private String[] stageTwoScaleIntLabels;
    private String[] stageTwoFirstIntLabels;

    /**
     * 
     * @param randomLocationEffects: number of random location effects
     * @param stageTwoBinary : whether or not the stage two outcome is dichotomous or ordinal
     */
    public DefinitionHelper(int randomLocationEffects, boolean stageTwoBinary) {
       this.randomLocationEffects = randomLocationEffects;
       this.stageTwoBinary = stageTwoBinary;
       if(this.randomLocationEffects>1){
           this.modelLocRanCount = Integer.toString(randomLocationEffects);
       }
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
    
    
    /**
     * 
     * @param defFile the file, as specified by the filepicker method
     * @param varNames the list of  variable names, derived from data file
     * try/catch these exceptions in order
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception display error message for this exception to user, do not execute program until this is resolved
     */
    public void readDefinitionFile(File defFile, List<String> varNames) throws FileNotFoundException, IOException, Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(defFile))) {
            String defLine;
            int row = 0;
            List<String> defSummary = new ArrayList<>();
            
            int fileSize = 0;
            while ((defLine = br.readLine()) != null) {
                if(defLine.length() > 0){fileSize++;}
                defSummary.add(defLine);
            }
                       
            if(stageTwoBinary & fileSize != 31){
                throw new Exception("Invalid definition file length");  // TODO: Create a new class that extends from Exception for invalid def files
            }
            else if(!stageTwoBinary & fileSize != 30){
                throw new Exception("Invalid definition file length"); 
            }         
            
            assignDefinitionVariables(defSummary);
        }
    }
    
    /**
     * 
     * @param readDefinitionFile the definition file as a List<String>
     * @throws Exception display error message for this exception to user, do not execute program until this is resolved
     */
    private void assignDefinitionVariables(List<String> readDefinitionFile) throws Exception {
        setModelTitle(readDefinitionFile.get(0));
        setModelSubtitle(readDefinitionFile.get(1));
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
    
    /**
     * 
     * @param validationMessage line name to throw in Exception message
     * @param lineMessage line number to throw in Exception message
     * @param validationInteger String variable that will be tested as Integer
     * @param minValue minimum value expressed as integer
     * @param maxValue maximum value expressed as integer
     * @return only returns true, otherwise throws Exception
     * @throws Exception inherited exception
     */
    private boolean setValidator(String validationMessage, String lineMessage, String validationInteger, int minValue, int maxValue) throws Exception {
        try { 
            if(Integer.parseInt(validationInteger)>= minValue && Integer.parseInt(validationInteger) <= maxValue){
                return Boolean.TRUE;
            }
            else {throw new Exception("Invalid " + validationMessage + " in .dat file specified, line " + lineMessage);}
        }
        catch(NumberFormatException nfe) {
            throw new Exception("Invalid character for " + validationMessage + " in .dat file specified, line " + lineMessage);
        }
    }
    
    /**
     * inherits parameters of setValidator, loops until all true, otherwise throws Exception
     * @param validationMessage
     * @param lineMessage
     * @param validationInteger String array to test
     * @param minValue
     * @param maxValue
     * @return returns true
     * @throws Exception inherited Exception from setValidator
     */
    private boolean loopSetValidator(String validationMessage, String lineMessage, String[] validationInteger, int minValue, int maxValue) throws Exception {
        for(String testInteger: validationInteger){
            if(!setValidator(validationMessage, lineMessage, testInteger, minValue, maxValue)){
              return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
    
    public String getModelTitle() {
        return modelTitle;
    }

    public void setModelTitle(String modelTitle) {
        if(modelTitle.length()>72){this.modelTitle = modelTitle.substring(0, 71);}
        else{this.modelTitle = modelTitle;}
    }

    public String getModelSubtitle() {
        return modelSubtitle;
    }

    public void setModelSubtitle(String modelSubtitle) {
        if(modelSubtitle.length()>72){this.modelSubtitle = modelSubtitle.substring(0, 71);}
        else{this.modelSubtitle = modelSubtitle;}
    }

    public String getDataFilename() {
        return dataFilename;
    }

    public void setDataFilename(String dataFilename) throws Exception {
        if(dataFilename.endsWith(".dat") || dataFilename.endsWith(".csv")){
           this.dataFilename = dataFilename;
        }
        else {throw new Exception("Filename is not a valid .dat or .csv file, line 3");}
    }

    public String getOutputPrefix() {
        return outputPrefix;
    }

    public void setOutputPrefix(String outputPrefix) {
        if(outputPrefix.length()>72){this.outputPrefix = outputPrefix.substring(0, 200);}
        else{this.outputPrefix = outputPrefix;}
        this.outputPrefix = outputPrefix;
    }

    public String getDataVariableCount() {
        return dataVariableCount;
    }

    public void setDataVariableCount(String dataVariableCount) throws Exception {
        if(setValidator("number of variables", "5", dataVariableCount, 2, 255)){
            this.dataVariableCount = dataVariableCount;
        }
    }

    public String getModelMeanCount() {
        return modelMeanCount;
    }

    public void setModelMeanCount(String modelMeanCount) throws Exception {
        if(setValidator("number of mean regressors", "5", modelMeanCount, 1, 255)){
            this.modelMeanCount = modelMeanCount;
        }
    }

    public String getModelLocRanCount() {
        return modelLocRanCount;
    }

    public void setModelLocRanCount(String modelLocRanCount) throws Exception {
        if(setValidator("number of location random effects", "5", modelLocRanCount, 0, 255)){
            this.modelLocRanCount = modelLocRanCount;
        }
    }
    
    public String getModelScaleCount() {
        return modelScaleCount;
    }

    public void setModelScaleCount(String modelScaleCount) throws Exception {
        if(setValidator("number of scale regressors", "5", modelScaleCount, 0, 255)){
            this.modelScaleCount = modelScaleCount;
        }
    }

    public String getModelFixedInt() {
        return modelFixedInt;
    }

    public void setModelFixedInt(String modelFixedInt) throws Exception {
        if(setValidator("fixed intercept", "5", modelFixedInt, 0, 1)){
            this.modelFixedInt = modelFixedInt;
        }
    }

    public String getModelRandomInt() {
        return modelRandomInt;
    }

    public void setModelRandomInt(String modelRandomInt) throws Exception {
        if(setValidator("random intercept", "5", modelRandomInt, 0, 1)){
            this.modelRandomInt = modelRandomInt;
        }
    }

    public String getModelScaleInt() {
        return modelScaleInt;
    }

    public void setModelScaleInt(String modelScaleInt) throws Exception {
        if(setValidator("scale intercept", "5", modelScaleInt, 0, 1)){
            this.modelScaleInt = modelScaleInt;
        }
    }

    public String getDecompMeanCount() {
        return decompMeanCount;
    }

    public void setDecompMeanCount(String decompMeanCount) throws Exception {
        if(setValidator("number of mean regressors for BS/WS decomposition", "5", decompMeanCount, 0, 255)){
            this.decompMeanCount = decompMeanCount;
        }
    }

    public String getDecompLocRanCount() {
        return decompLocRanCount;
    }

    public void setDecompLocRanCount(String decompLocRanCount) throws Exception {
        if(setValidator("number of location random effects for BS/WS decomposition", "5", decompLocRanCount, 0, 255)){
            this.decompLocRanCount = decompLocRanCount;
        }
    }

    public String getDecompScaleCount() {
        return decompScaleCount;
    }

    public void setDecompScaleCount(String decompScaleCount) throws Exception {
        if(setValidator("number of scale regressors for BS/WS decomposition", "5", decompScaleCount, 0, 255)){
            this.decompScaleCount = decompScaleCount;
        }
    }

    public String getAdvancedConvergence() {
        return advancedConvergence;
    }

    public void setAdvancedConvergence(String advancedConvergence) throws Exception {
        try { 
            if(Double.parseDouble(advancedConvergence)>=0 && Double.parseDouble(advancedConvergence)<=1){
                this.advancedConvergence = advancedConvergence;
            }
            else {throw new Exception("Invalid convergence criteria in .dat file specified, line 5");}
        }
        catch(NumberFormatException nfe) {
            throw new Exception("Invalid character for convergence criteria in .dat file specified, line 5");
        }
    }

    public String getAdvancedQuadPoints() {
        return advancedQuadPoints;
    }

    public void setAdvancedQuadPoints(String advancedQuadPoints) throws Exception {
        if(setValidator("number of quadrature points", "5", advancedQuadPoints, 1, 255)){
            this.advancedQuadPoints = advancedQuadPoints;
        }
    }

    public String getAdvancedAdaptiveQuad() {
        return advancedAdaptiveQuad;
    }

    public void setAdvancedAdaptiveQuad(String advancedAdaptiveQuad) throws Exception {
        if(setValidator("adaptive quadrature", "5", advancedAdaptiveQuad, 0, 1)){
            this.advancedAdaptiveQuad = advancedAdaptiveQuad;
        }
    }

    public String getAdvancedMaxIteration() {
        return advancedMaxIteration;
    }

    public void setAdvancedMaxIteration(String advancedMaxIteration) throws Exception {
        if(setValidator("maximum iterations", "5", advancedMaxIteration, 1, Integer.MAX_VALUE)){
            this.advancedMaxIteration = advancedMaxIteration;
        }
    }

    public String getAdvancedMissingValue() {
        return advancedMissingValue;
    }

    public void setAdvancedMissingValue(String advancedMissingValue) throws Exception {
        if(setValidator("missing value", "5", advancedMissingValue, Integer.MIN_VALUE, Integer.MAX_VALUE)){
            this.advancedMissingValue = advancedMissingValue;
        }
    }

    public String getAdvancedCenterScale() {
        return advancedCenterScale;
    }

    public void setAdvancedCenterScale(String advancedCenterScale) throws Exception {
        if(setValidator("scale centering", "5", advancedCenterScale, 0, 1)){
            this.advancedCenterScale = advancedCenterScale;
        }
    }

    public String getAdvancedRidge() {
        return advancedRidge;
    }

    public void setAdvancedRidge(String advancedRidge) throws Exception {
        try { 
            if(Double.parseDouble(advancedRidge)>=0 && Double.parseDouble(advancedRidge)<=1){
                this.advancedRidge = advancedRidge;
            }
            else {throw new Exception("Invalid initial ridge value in .dat file specified, line 5");}
        }
        catch(NumberFormatException nfe) {
            throw new Exception("Invalid character for initial ridge value in .dat file specified, line 5");
        }
    }

    public String getModelBetweenCount() {
        return modelBetweenCount;
    }

    public void setModelBetweenCount(String modelBetweenCount) throws Exception {
        if(setValidator("number of between-subject variance regressors", "5", modelBetweenCount, 0, 255)){
            this.modelBetweenCount = modelBetweenCount;
        }
    }

    public String getModelWithinCount() {
        return modelWithinCount;
    }

    public void setModelWithinCount(String modelWithinCount) throws Exception {
        if(setValidator("number of within-subject variance regressors", "5", modelWithinCount, 0, 255)){
            this.modelWithinCount = modelWithinCount;
        }
    }

    public String getModelBetweenInt() {
        return modelBetweenInt;
    }

    public void setModelBetweenInt(String modelBetweenInt) throws Exception {
        if(setValidator("between-subject variance intercept", "5", modelBetweenInt, 0, 1)){
            this.modelBetweenInt = modelBetweenInt;
        }
    }

    public String getModelWithinInt() {
        return modelWithinInt;
    }

    public void setModelWithinInt(String modelWithinInt) throws Exception {
        if(setValidator("within-subject variance intercept", "5", modelWithinInt, 0, 1)){
            this.modelWithinInt = modelWithinInt;
        }
    }

    public String getDecompBSCount() {
        return decompBSCount;
    }

    public void setDecompBSCount(String decompBSCount) throws Exception {
        if(setValidator("number of between-subject variance regressors for BS/WS decomposition", "5", decompBSCount, 0, 255)){
            this.decompBSCount = decompBSCount;
        }
    }

    public String getDecompWSCount() {
        return decompWSCount;
    }

    public void setDecompWSCount(String decompWSCount) throws Exception {
        if(setValidator("number of within-subject variance regressors for BS/WS decomposition", "5", decompWSCount, 0, 255)){
            this.decompWSCount = decompWSCount;
        }
    }

    public String getAdvancedEffectMeanWS() {
        return advancedEffectMeanWS;
    }

    public void setAdvancedEffectMeanWS(String advancedEffectMeanWS) throws Exception {
        if(setValidator("effect of mean on WS variance", "5", decompWSCount, 0, 2)){
            this.decompWSCount = decompWSCount;
        }
    }

    public String[] getIdOutcome() {
        return idOutcome;
    }

    public void setIdOutcome(String[] idOutcome) throws Exception {
        if(setValidator("id location", "6", idOutcome[0], 0, 255)){
            if(setValidator("outcome location", "6", idOutcome[1], 0, 255)){
                this.idOutcome = idOutcome;
            }
        }
    }

    public String[] getFieldModelMeanRegressors() {
        return fieldModelMeanRegressors;
    }

    public void setFieldModelMeanRegressors(String[] fieldModelMeanRegressors) throws Exception {
        if(loopSetValidator("model mean regressor fields", "7", fieldModelMeanRegressors, 0, 255)){
            this.fieldModelMeanRegressors = fieldModelMeanRegressors;
        }
    }

    public String[] getFieldModelBSRegressors() {
        return fieldModelBSRegressors;
    }

    public void setFieldModelBSRegressors(String[] fieldModelBSRegressors) {
        this.fieldModelBSRegressors = fieldModelBSRegressors;
    }

    public String[] getFieldModelWSRegressors() {
        return fieldModelWSRegressors;
    }

    public void setFieldModelWSRegressors(String[] fieldModelWSRegressors) {
        this.fieldModelWSRegressors = fieldModelWSRegressors;
    }

    public String[] getFieldModelLocRanRegressors() {
        return fieldModelLocRanRegressors;
    }

    public void setFieldModelLocRanRegressors(String[] fieldModelLocRanRegressors) {
        this.fieldModelLocRanRegressors = fieldModelLocRanRegressors;
    }

    public String[] getFieldModelScaleRegressors() {
        return fieldModelScaleRegressors;
    }

    public void setFieldModelScaleRegressors(String[] fieldModelScaleRegressors) {
        this.fieldModelScaleRegressors = fieldModelScaleRegressors;
    }

    public String[] getFieldDecompMeanRegressors() {
        return fieldDecompMeanRegressors;
    }

    public void setFieldDecompMeanRegressors(String[] fieldDecompMeanRegressors) {
        this.fieldDecompMeanRegressors = fieldDecompMeanRegressors;
    }

    public String[] getFieldDecompBSRegressors() {
        return fieldDecompBSRegressors;
    }

    public void setFieldDecompBSRegressors(String[] fieldDecompBSRegressors) {
        this.fieldDecompBSRegressors = fieldDecompBSRegressors;
    }

    public String[] getFieldDecompWSRegressors() {
        return fieldDecompWSRegressors;
    }

    public void setFieldDecompWSRegressors(String[] fieldDecompWSRegressors) {
        this.fieldDecompWSRegressors = fieldDecompWSRegressors;
    }

    public String[] getFieldDecompLocRanRegressors() {
        return fieldDecompLocRanRegressors;
    }

    public void setFieldDecompLocRanRegressors(String[] fieldDecompLocRanRegressors) {
        this.fieldDecompLocRanRegressors = fieldDecompLocRanRegressors;
    }

    public String[] getFieldDecompScaleRegressors() {
        return fieldDecompScaleRegressors;
    }

    public void setFieldDecompScaleRegressors(String[] fieldDecompScaleRegressors) {
        this.fieldDecompScaleRegressors = fieldDecompScaleRegressors;
    }

    public String getLabelModelOutcome() {
        return labelModelOutcome;
    }

    public void setLabelModelOutcome(String labelModelOutcome) {
        this.labelModelOutcome = labelModelOutcome;
    }

    public String[] getLabelModelMeanRegressors() {
        return labelModelMeanRegressors;
    }

    public void setLabelModelMeanRegressors(String[] labelModelMeanRegressors) {
        this.labelModelMeanRegressors = labelModelMeanRegressors;
    }

    public String[] getLabelModelLocRanRegressors() {
        return labelModelLocRanRegressors;
    }

    public void setLabelModelLocRanRegressors(String[] labelModelLocRanRegressors) {
        this.labelModelLocRanRegressors = labelModelLocRanRegressors;
    }

    public String[] getLabelModelScaleRegressors() {
        return labelModelScaleRegressors;
    }

    public void setLabelModelScaleRegressors(String[] labelModelScaleRegressors) {
        this.labelModelScaleRegressors = labelModelScaleRegressors;
    }

    public String[] getLabelModelBSRegressors() {
        return labelModelBSRegressors;
    }

    public void setLabelModelBSRegressors(String[] labelModelBSRegressors) {
        this.labelModelBSRegressors = labelModelBSRegressors;
    }

    public String[] getLabelModelWSRegressors() {
        return labelModelWSRegressors;
    }

    public void setLabelModelWSRegressors(String[] labelModelWSRegressors) {
        this.labelModelWSRegressors = labelModelWSRegressors;
    }

    public String[] getLabelDecompMeanRegressors() {
        return labelDecompMeanRegressors;
    }

    public void setLabelDecompMeanRegressors(String[] labelDecompMeanRegressors) {
        this.labelDecompMeanRegressors = labelDecompMeanRegressors;
    }

    public String[] getLabelDecompLocRanRegressors() {
        return labelDecompLocRanRegressors;
    }

    public void setLabelDecompLocRanRegressors(String[] labelDecompLocRanRegressors) {
        this.labelDecompLocRanRegressors = labelDecompLocRanRegressors;
    }

    public String[] getLabelDecompScaleRegressors() {
        return labelDecompScaleRegressors;
    }

    public void setLabelDecompScaleRegressors(String[] labelDecompScaleRegressors) {
        this.labelDecompScaleRegressors = labelDecompScaleRegressors;
    }

    public String[] getLabelDecompBSRegressors() {
        return labelDecompBSRegressors;
    }

    public void setLabelDecompBSRegressors(String[] labelDecompBSRegressors) {
        this.labelDecompBSRegressors = labelDecompBSRegressors;
    }

    public String[] getLabelDecompWSRegressors() {
        return labelDecompWSRegressors;
    }

    public void setLabelDecompWSRegressors(String[] labelDecompWSRegressors) {
        this.labelDecompWSRegressors = labelDecompWSRegressors;
    }

    public String getStageTwoFixedCount() {
        return stageTwoFixedCount;
    }

    public void setStageTwoFixedCount(String stageTwoFixedCount) {
        this.stageTwoFixedCount = stageTwoFixedCount;
    }

    public String getStageTwoLocRanInteractions() {
        return stageTwoLocRanInteractions;
    }

    public void setStageTwoLocRanInteractions(String stageTwoLocRanInteractions) {
        this.stageTwoLocRanInteractions = stageTwoLocRanInteractions;
    }

    public String getStageTwoScaleInteractions() {
        return stageTwoScaleInteractions;
    }

    public void setStageTwoScaleInteractions(String stageTwoScaleInteractions) {
        this.stageTwoScaleInteractions = stageTwoScaleInteractions;
    }

    public String getStageTwoIntOfInteraction() {
        return stageTwoIntOfInteraction;
    }

    public void setStageTwoIntOfInteraction(String stageTwoIntOfInteraction) {
        this.stageTwoIntOfInteraction = stageTwoIntOfInteraction;
    }

    public String getStageTwoOutcomeCatCount() {
        return stageTwoOutcomeCatCount;
    }

    public void setStageTwoOutcomeCatCount(String stageTwoOutcomeCatCount) {
        this.stageTwoOutcomeCatCount = stageTwoOutcomeCatCount;
    }

    public String getStageTwoOutcomeField() {
        return stageTwoOutcomeField;
    }

    public void setStageTwoOutcomeField(String stageTwoOutcomeField) {
        this.stageTwoOutcomeField = stageTwoOutcomeField;
    }

    public String[] getStageTwoOutcomeCatLabel() {
        return stageTwoOutcomeCatLabel;
    }

    public void setStageTwoOutcomeCatLabel(String[] stageTwoOutcomeCatLabel) {
        this.stageTwoOutcomeCatLabel = stageTwoOutcomeCatLabel;
    }

    public String[] getStageTwoFixedFields() {
        return stageTwoFixedFields;
    }

    public void setStageTwoFixedFields(String[] stageTwoFixedFields) {
        this.stageTwoFixedFields = stageTwoFixedFields;
    }

    public String[] getStageTwoLocRanIntFields() {
        return stageTwoLocRanIntFields;
    }

    public void setStageTwoLocRanIntFields(String[] stageTwoLocRanIntFields) {
        this.stageTwoLocRanIntFields = stageTwoLocRanIntFields;
    }

    public String[] getStageTwoScaleIntFields() {
        return stageTwoScaleIntFields;
    }

    public void setStageTwoScaleIntFields(String[] stageTwoScaleIntFields) {
        this.stageTwoScaleIntFields = stageTwoScaleIntFields;
    }

    public String[] getStageTwoFirstIntFields() {
        return stageTwoFirstIntFields;
    }

    public void setStageTwoFirstIntFields(String[] stageTwoFirstIntFields) {
        this.stageTwoFirstIntFields = stageTwoFirstIntFields;
    }

    public String getStageTwoOutcomeLabel() {
        return stageTwoOutcomeLabel;
    }

    public void setStageTwoOutcomeLabel(String stageTwoOutcomeLabel) {
        this.stageTwoOutcomeLabel = stageTwoOutcomeLabel;
    }

    public String[] getStageTwoFixedLabels() {
        return stageTwoFixedLabels;
    }

    public void setStageTwoFixedLabels(String[] stageTwoFixedLabels) {
        this.stageTwoFixedLabels = stageTwoFixedLabels;
    }

    public String[] getStageTwoLocRanIntLabels() {
        return stageTwoLocRanIntLabels;
    }

    public void setStageTwoLocRanIntLabels(String[] stageTwoLocRanIntLabels) {
        this.stageTwoLocRanIntLabels = stageTwoLocRanIntLabels;
    }

    public String[] getStageTwoScaleIntLabels() {
        return stageTwoScaleIntLabels;
    }

    public void setStageTwoScaleIntLabels(String[] stageTwoScaleIntLabels) {
        this.stageTwoScaleIntLabels = stageTwoScaleIntLabels;
    }

    public String[] getStageTwoFirstIntLabels() {
        return stageTwoFirstIntLabels;
    }

    public void setStageTwoFirstIntLabels(String[] stageTwoFirstIntLabels) {
        this.stageTwoFirstIntLabels = stageTwoFirstIntLabels;
    }

}