/*
MixWild, a program to model subject-level slope and variance on continuous or ordinal outcomes
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
package def_lib;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import mixregui.SystemLogger;
import org.apache.commons.io.FilenameUtils;

/**
 * ****
 * Exposed Methods: // readDefinitionFile: takes File as parameter, assigns all
 * variables and then attempts to validate file buildStageOneDefinitionList:
 * returns ArrayList<String> that can be passed to a FileWriter class, validates
 * first
 *
 * @note: Both classes should pass Exceptions to the view and interrupt until
 * user fixes error
 *
 * @author Eldin Dzubur.
 */
public class MixLibrary implements Serializable {

    /**
     * MIXWILD V2.0 Keys
     */
    /**
     * Private Class Keys
     */
    private static final boolean MIX_INTEGER = Boolean.TRUE;
    private static final boolean MIX_STRING = Boolean.FALSE;
    
    /**
     * Stage One Outcome Keys
     */
    public static final int STAGE_ONE_OUTCOME_MIXREG = 1;
    public static final int STAGE_ONE_OUTCOME_MIXOR = 2;
    
    /**
     * Stage One Random Location Keys
     */
    public static final int STAGE_ONE_RLE_NONE = 1;
    public static final int STAGE_ONE_RLE_LOCATION = 2;
    public static final int STAGE_ONE_RLE_SLOPE = 3;
    
    /**
     * Stage One Random Scale Keys
     */
    public static final int STAGE_ONE_SCALE_NO = 0;
    public static final int STAGE_ONE_SCALE_YES = 1;
    
    /**
     * Stage Two Model Type Keys
     */
    public static final int STAGE_TWO_MODEL_TYPE_NONE = 0;
    public static final int STAGE_TWO_MODEL_TYPE_SINGLE = 1;
    public static final int STAGE_TWO_MODEL_TYPE_MULTILEVEL = 2;
    
    /**
     * Stage Two Outcome Keys
     */
    public static final int STAGE_TWO_OUTCOME_NONE = 0;
    public static final int STAGE_TWO_OUTCOME_NORMAL = 1;
    public static final int STAGE_TWO_OUTCOME_ORDINAL = 2;
    public static final int STAGE_TWO_OUTCOME_COUNT = 3;
    public static final int STAGE_TWO_OUTCOME_NOMINAL = 4;
    
    /**
     * MixWILD V2.0 Initialization Parameters
     */
    
    private int stageOneOutcome;
    private int stageOneRandomLocationEffects;
    private int stageOneRandomScale;
    private int stageTwoModelType;
    private int stageTwoOutcomeType;
   
    
    //auxiliary fields
    private String utcDirPath;
    public Boolean win32;
    

    
    
    /**
     * MixWILD V2.0 Initialization of DefinitionHelper without Stage Two
     * 
     * @param stageOneOutcome STAGE_ONE_OUTCOME_MIXREG or STAGE_ONE_OUTCOME_MIXREG
     * @param stageOneRandomLocationEffects STAGE_ONE_RLE_NONE or STAGE_ONE_RLE_LOCATION or STAGE_ONE_RLE_SLOPE
     * @param stageOneRandomScale STAGE_ONE_SCALE_NO or STAGE_ONE_SCALE_YES
     */
    public MixLibrary(int stageOneOutcome, int stageOneRandomLocationEffects, 
            int stageOneRandomScale) {
        this.stageOneOutcome = stageOneOutcome;
        this.stageOneRandomLocationEffects = stageOneRandomLocationEffects;
        this.stageOneRandomScale = stageOneRandomScale;
        this.stageTwoModelType = STAGE_TWO_MODEL_TYPE_NONE;
        this.stageTwoOutcomeType = STAGE_TWO_OUTCOME_NONE;
    }

    
    /**
     * MixWILD V2.0 Initialization of DefinitionHelper with Stage Two
     * 
     * @param stageOneOutcome STAGE_ONE_OUTCOME_MIXREG or STAGE_ONE_OUTCOME_MIXREG
     * @param stageOneRandomLocationEffects STAGE_ONE_RLE_NONE or STAGE_ONE_RLE_LOCATION or STAGE_ONE_RLE_SLOPE
     * @param stageOneRandomScale STAGE_ONE_SCALE_NO or STAGE_ONE_SCALE_YES
     * @param stageTwoModelType STAGE_TWO_MODEL_TYPE_SINGLE or STAGE_TWO_MODEL_TYPE_MULTILEVEL
     * @param stageTwoOutcomeType  STAGE_TWO_OUTCOME_NORMAL or STAGE_TWO_OUTCOME_ORDINAL or STAGE_TWO_OUTCOME_COUNT or STAGE_TWO_OUTCOME_NOMINAL
     */
    public MixLibrary(int stageOneOutcome, int stageOneRandomLocationEffects, 
            int stageOneRandomScale, int stageTwoModelType, int stageTwoOutcomeType) {
        this.stageOneOutcome = stageOneOutcome;
        this.stageOneRandomLocationEffects = stageOneRandomLocationEffects;
        this.stageOneRandomScale = stageOneRandomScale;
        this.stageTwoModelType = stageTwoModelType;
        this.stageTwoOutcomeType = stageTwoOutcomeType;
    }
    
    
    /** 
     * MixWILD V2.0 SHARED Parameters
     */
    private String sharedModelTitle; // LINE 1
    private String sharedModelSubtitle; // LINE 2
    private String sharedDataFilename; // LINE 3
    private String sharedOutputPrefix; // LINE 4
    private String[] sharedAdvancedOptions; // LINE 5  -  SEE LINE 190 FOR DETAILS
    
    private String[] sharedIdAndStageOneOutcomeFields; // LINE 7 for MIXOR, LINE 6 for MIXREG
    
    private String[] sharedModelMeanRegressorFields; // LINE 8 for MIXOR, LINE 7 for MIXREG
    private String[] sharedModelRandomRegressorFields; // LINE 9 for MIXOR, LINE 8 for MIXREG
    private String[] sharedModelScaleRegressorFields; // LINE 10 for MIXOR, LINE 9 for MIXREG
    
    private String[] sharedModelDecomposeMeanRegressorFields; // LINE 11 for MIXOR, LINE 10 for MIXREG
    private String[] sharedModelDecomposeRandomRegressorFields; // LINE 12 for MIXOR, LINE 11 for MIXREG
    private String[] sharedModelDecomposeScaleRegressorFields; // LINE 13 for MIXOR, LINE 12 for MIXREG
    
    private String sharedModelStageOneOutcomeLabel; // LINE 15 for MIXOR, LINE 13 for MIXREG
    
    private String[] sharedModelMeanRegressorLabels; // LINE 16 for MIXOR, LINE 14 for MIXREG
    private String[] sharedModelRandomRegressorLabels; // LINE 17 for MIXOR, LINE 15 for MIXREG
    private String[] sharedModelScaleRegressorLabels; // LINE 18 for MIXOR, LINE 16 for MIXREG
    
    private String[] sharedModelDecomposeMeanRegressorLabels; // LINE 19 for MIXOR, LINE 17 for MIXREG
    private String[] sharedModelDecomposeRandomRegressorLabels; // LINE 20 for MIXOR, LINE 18 for MIXREG
    private String[] sharedModelDecomposeScaleRegressorLabels; // LINE 21 for MIXOR, LINE 19 for MIXREG
    
    /**
     * MixWILD V2.0 MIXOR Parameters
     */
    private String[] mixorModelCovarianceThresholdParameters; // LINE 6 for MIXOR
    private String mixorModelCovarianceParameter = "0";
    private String mixorModelThresholdParameter = "-1";
    
    private String[] mixorModelStageOneOutcomeLevels; // LINE 14 for MIXOR
    
    /**
     * MixWILD V2.0 Stage Two Parameters
     * Append at LINE 20 for MIXREG and LINE 22 for MIXOR
     */
    private String[] stageTwoRegressorCounts; //  SEE LINE 220 FOR DETAILS
    private String stageTwoOutcomeField;
    
    private String[] stageTwoFixedFields; // FIXEX
    private String[] stageTwoThetaFields; // INTERACTION WITH LOCATION
    private String[] stageTwoOmegaFields; // INTERACTION WITH SCALE
    private String[] stageTwoInteractionFields; // INTERACTION WITH INTERACTION OF LOCATION*SCALE
    
    private String stageTwoOutcomeLabel;
    
    private String[] stageTwoFixedLabels; // FIXEX
    private String[] stageTwoThetaLabels; // INTERACTION WITH LOCATION
    private String[] stageTwoOmegaLabels; // INTERACTION WITH SCALE
    private String[] stageTwoInteractionLabels; // INTERACTION WITH INTERACTION OF LOCATION*SCALE
    
    /**
     * MixWILD V2.0 MIXREG Advanced Parameters
     */
    
    private String advancedVariableCount; // NVAR
    private String advancedMeanRegressorCount; // P
    private String advancedRandomRegressorCount; // R
    private String advancedScaleRegressorCount; // S
    
    private String advancedStageOneOutcomeValueCount; // NULL if MIXREG - MAXJ
    
    private String advancedMeanIntercept; // NULL if MIXOR - PNINT
    private String advancedRandomIntercept; // RNINT
    private String advancedScaleIntercept; // NULL if MIXOR - SNINT
    
    private String advancedDecomposeMeanRegressorCount; // P1
    private String advancedDecomposeRandomRegressorCount; // R1
    private String advancedDecomposeScaleRegressorCount; // S1
    
    private String advancedConvergenceCriteria; //CONV
    private String advancedQuadPoints; // QP
    private String advancedAdaptiveQuad; // AQ
    private String advancedMaxIterations; // MAXIT
    private String advancedMissingValueCode; // YMISS
    private String advancedCenterScaleVariables; // NULL if MIXOR - NCENT
    private String advancedRandomScaleAssociation; // ALWAYS 0 for MIXOR - NCOV
    private String advancedInitialRidge; // RIDGEIN
    private String advancedLogisticProbitRegression; // NULL if MIXREG - NFN 
    private String advancedDiscardNoVariance; // NULL if MIXOR - DISCARD0
    private String advancedUseMLS; // ALWAYS 1 for MIXOR - MLS
    private String advancedCovarianceMatrix; // NULL if MIXOR - CHOL
    private String advancedResampleCount; // NREPS
    private String advancedRandomScaleCutoff; // NULL if MIXOR - CUTOFF
    private String advancedUseRandomScale; // NORS
    private String advancedResamplingSeed; // MYSEED
    
    private String advancedUseStageTwo; // STAGE2
    private String advancedStageTwoMultilevel; // MULTI2ND
    private String advancedMultipleDataFiles; // SEPFILE
    
    /**
     * MixWILD V2.0 MIXREG Stage 2 Advanced Parameters
     */
    private String advancedStageTwoFixedRegressorCount;         // COUNT OF FIXEX
    private String advancedStageTwoThetaRegressorCount;         // COUNT OF INTERACTION WITH LOCATION 
    private String advancedStageTwoOmegaRegressorCount;         // COUNT OF INTERACTION WITH SCALE 
    private String advancedStageTwoInteractionRegressorCount;   // COUNT OF INTERACTION WITH LOC*SCA
    
    /**
     * Stage 1 GUI Serializable Specifications
     */
    private String[] labelModelMeanRegressorsLevelOne;
    private String[] labelModelLocRanRegressorsLevelOne;
    private String[] labelModelScaleRegressorsLevelOne;
    private String[] labelModelBSRegressorsLevelOne;
    private String[] labelModelWSRegressorsLevelOne;
    private String[] labelModelMeanRegressorsLevelTwo;
    private String[] labelModelLocRanRegressorsLevelTwo;
    private String[] labelModelScaleRegressorsLevelTwo;
    private String[] labelModelBSRegressorsLevelTwo;
    private String[] labelModelWSRegressorsLevelTwo;
    /**
     * Builds the advanced variable arrays for Stage 1 and Stage 2, 
     * as noted above.
     * 
     * @param stageToBuild 1 OR 2
     * @return array of advanced variable fields to pass to writer
     */
    private String[] advancedVariableBuild(int stageToBuild) {
        List<String> advancedVariable = new ArrayList();
        if(stageToBuild == 1){
            if (stageOneOutcome == STAGE_ONE_OUTCOME_MIXREG) { 
                advancedVariable.add(getAdvancedVariableCount());
                advancedVariable.add(getAdvancedMeanRegressorCount());
                advancedVariable.add(getAdvancedRandomRegressorCount());
                advancedVariable.add(getAdvancedScaleRegressorCount());

                advancedVariable.add(getAdvancedMeanIntercept()); 
                advancedVariable.add(getAdvancedRandomIntercept()); 
                advancedVariable.add(getAdvancedScaleIntercept()); 

                advancedVariable.add(getAdvancedDecomposeMeanRegressorCount());  
                advancedVariable.add(getAdvancedDecomposeRandomRegressorCount());  
                advancedVariable.add(getAdvancedDecomposeScaleRegressorCount());  

                advancedVariable.add(getAdvancedConvergenceCriteria());  
                advancedVariable.add(getAdvancedQuadPoints());  
                advancedVariable.add(getAdvancedAdaptiveQuad()); 
                advancedVariable.add(getAdvancedMaxIterations()); 
                advancedVariable.add(getAdvancedMissingValueCode()); 
                advancedVariable.add(getAdvancedCenterScaleVariables()); 

                advancedVariable.add(getAdvancedRandomScaleAssociation()); 
                advancedVariable.add(getAdvancedInitialRidge()); 
                advancedVariable.add(getAdvancedDiscardNoVariance()); 
                advancedVariable.add(getAdvancedUseMLS()); 
                advancedVariable.add(getAdvancedCovarianceMatrix()); 
                advancedVariable.add(getAdvancedResampleCount()); 
                advancedVariable.add(getAdvancedRandomScaleCutoff()); 
                advancedVariable.add(getAdvancedUseRandomScale()); 
                advancedVariable.add(getAdvancedResamplingSeed()); 

                advancedVariable.add(getAdvancedUseStageTwo()); 
                advancedVariable.add(getAdvancedStageTwoMultilevel()); 
                advancedVariable.add(getAdvancedMultipleDataFiles()); 
            }

            if (stageOneOutcome == STAGE_ONE_OUTCOME_MIXOR) {
                advancedVariable.add(getAdvancedVariableCount());
                advancedVariable.add(getAdvancedStageOneOutcomeValueCount()); 

                advancedVariable.add(getAdvancedMeanRegressorCount());
                advancedVariable.add(getAdvancedRandomRegressorCount());
                advancedVariable.add(getAdvancedScaleRegressorCount());
                advancedVariable.add(getAdvancedDecomposeMeanRegressorCount());  
                advancedVariable.add(getAdvancedDecomposeRandomRegressorCount());  
                advancedVariable.add(getAdvancedDecomposeScaleRegressorCount());  

                advancedVariable.add(getAdvancedRandomIntercept()); 

                advancedVariable.add(getAdvancedConvergenceCriteria());  
                advancedVariable.add(getAdvancedQuadPoints());  
                advancedVariable.add(getAdvancedAdaptiveQuad()); 
                advancedVariable.add(getAdvancedMaxIterations()); 
                advancedVariable.add(getAdvancedMissingValueCode()); 

                advancedVariable.add(getAdvancedInitialRidge()); 
                advancedVariable.add(getAdvancedLogisticProbitRegression()); 
                advancedVariable.add(getAdvancedUseRandomScale()); 

                advancedVariable.add(getAdvancedResamplingSeed()); 
                advancedVariable.add(getAdvancedUseStageTwo()); 
                advancedVariable.add(getAdvancedStageTwoMultilevel()); 
                advancedVariable.add(getAdvancedResampleCount());
                advancedVariable.add(getAdvancedMultipleDataFiles()); 
                advancedVariable.add(getAdvancedUseMLS());
                advancedVariable.add(getAdvancedRandomScaleAssociation()); 
            }
        }
        
        if(stageToBuild == 2){
            advancedVariable.add(getAdvancedStageTwoFixedRegressorCount());
            advancedVariable.add(getAdvancedStageTwoThetaRegressorCount());
            advancedVariable.add(getAdvancedStageTwoOmegaRegressorCount());
            advancedVariable.add(getAdvancedStageTwoInteractionRegressorCount());
        }

        String[] returnVars = new String[advancedVariable.size()];
        int iter = 0;
        for (String iterate : advancedVariable) {
            returnVars[iter] = iterate;
            iter++;
        };
        return returnVars;
    }
    
    /**
     *  Creates the definition file list
     *  See comments for line numbers referencing MIXOR/MIXREG
     * 
     * @return a List object to be written as the def file
     * @throws Exception error message showing why the definition is invalid
     */
    public List<String> buildStageOneDefinitonList() throws Exception { 
        List<String> newDefinitionFile = new ArrayList();
        
        newDefinitionFile.add(getSharedModelTitle()); // LINE 1
        newDefinitionFile.add(getSharedModelSubtitle()); // LINE 2
        newDefinitionFile.add("\"" + FilenameUtils.getName(getSharedDataFilename()) + "\""); // LINE 3
        newDefinitionFile.add(getSharedOutputPrefix()); // LINE 4
        newDefinitionFile.add(Arrays.toString(getSharedAdvancedOptions()).replaceAll(",", " ")); // LINE 5
        
        if(getStageOneOutcome() == STAGE_ONE_OUTCOME_MIXOR){
            newDefinitionFile.add(Arrays.toString(getMixorModelCovarianceThresholdParameters()).replaceAll(",", " ")); // LINE 6/-
        }
        
        newDefinitionFile.add(Arrays.toString(getSharedIdAndStageOneOutcomeFields()).replaceAll(",", " ")); // LINE 7/6
        
        newDefinitionFile.add(Arrays.toString(getSharedModelMeanRegressorFields()).replaceAll(",", " ")); // LINE 8/7
        newDefinitionFile.add(Arrays.toString(getSharedModelRandomRegressorFields()).replaceAll(",", " ")); // LINE 9/8
        newDefinitionFile.add(Arrays.toString(getSharedModelScaleRegressorFields()).replaceAll(",", " ")); // LINE 10/9
        
        newDefinitionFile.add(Arrays.toString(getSharedModelDecomposeMeanRegressorFields()).replaceAll(",", " ")); // LINE 11/10
        newDefinitionFile.add(Arrays.toString(getSharedModelDecomposeRandomRegressorFields()).replaceAll(",", " ")); // LINE 12/11
        newDefinitionFile.add(Arrays.toString(getSharedModelDecomposeScaleRegressorFields()).replaceAll(",", " ")); // LINE 13/12
        
        if(getStageOneOutcome() == STAGE_ONE_OUTCOME_MIXOR){
            newDefinitionFile.add(Arrays.toString(getMixorModelStageOneOutcomeLevels()).replaceAll(",", " ")); // LINE 14/-
        }
        
        newDefinitionFile.add(getSharedModelStageOneOutcomeLabel()); // LINE 15/13
        
        newDefinitionFile.add(Arrays.toString(getSharedModelMeanRegressorLabels()).replaceAll(",", " ")); // LINE 16/14
        newDefinitionFile.add(Arrays.toString(getSharedModelRandomRegressorLabels()).replaceAll(",", " ")); // LINE 17/15
        newDefinitionFile.add(Arrays.toString(getSharedModelScaleRegressorLabels()).replaceAll(",", " ")); // LINE 18/16
        
        newDefinitionFile.add(Arrays.toString(getSharedModelDecomposeMeanRegressorLabels()).replaceAll(",", " ")); // LINE 19/17
        newDefinitionFile.add(Arrays.toString(getSharedModelDecomposeRandomRegressorLabels()).replaceAll(",", " ")); // LINE 20/18
        newDefinitionFile.add(Arrays.toString(getSharedModelDecomposeScaleRegressorLabels()).replaceAll(",", " ")); // LINE 21/19

        /**
         * Appending Stage 2 (Optional)
         */
        if(stageTwoModelType != STAGE_TWO_MODEL_TYPE_NONE){
            newDefinitionFile.add(Arrays.toString(getStageTwoRegressorCounts()).replaceAll(",", " "));
            newDefinitionFile.add(getStageTwoOutcomeField());
            
            newDefinitionFile.add(Arrays.toString(getStageTwoFixedFields()).replaceAll(",", " "));
            newDefinitionFile.add(Arrays.toString(getStageTwoThetaFields()).replaceAll(",", " "));
            newDefinitionFile.add(Arrays.toString(getStageTwoOmegaFields()).replaceAll(",", " "));
            newDefinitionFile.add(Arrays.toString(getStageTwoInteractionFields()).replaceAll(",", " "));
            
            newDefinitionFile.add(getStageTwoOutcomeLabel());
            
            newDefinitionFile.add(Arrays.toString(getStageTwoFixedLabels()).replaceAll(",", " "));
            newDefinitionFile.add(Arrays.toString(getStageTwoThetaLabels()).replaceAll(",", " "));
            newDefinitionFile.add(Arrays.toString(getStageTwoOmegaLabels()).replaceAll(",", " "));
            newDefinitionFile.add(Arrays.toString(getStageTwoInteractionLabels()).replaceAll(",", " "));
        } 
      
        exportValidator();
        return newDefinitionFile;
    }
    
    /**
     *  Validates that the field/label counts are equivalent to the number of fields or labels.
     * @param countVariable the field or label count variable
     * @param fieldLabelLine the actual field or label array
     * @return boolean, does the field/label count equal the number of elements in the array
     * @throws Exception 
     */
    private boolean validateFieldLabels(String countVariable, String[] fieldLabelLine) throws Exception {
        int field = -1;
        int labels = 0;
        try {
            field = Integer.parseInt(countVariable);
        } catch (Exception ex) {
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString()+ "{0}", SystemLogger.getLineNum());
            throw new Exception("Unassigned count variable for one or more options or regressors");

        }
        try {
            labels = fieldLabelLine.length;
        } catch (Exception ex) {
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString()+ "{0}", SystemLogger.getLineNum());
            throw new Exception("Unassigned field or label series for one or more sets of options or regressors");
        }
        return field == labels;
    }
    
    /**
     * Checks to see if random location scale interactions in stage two are included
     * @return a boolean
     * @throws Exception if the interaction value is not an integer 
     */
    private boolean isAdvancedStageTwoInteractionIncluded() throws Exception {
        int stageTwoInteractionInteger;
        try {
            stageTwoInteractionInteger = Integer.parseInt(getAdvancedStageTwoInteractionRegressorCount());
        } catch(NumberFormatException nfe){
            SystemLogger.LOGGER.log(Level.SEVERE, nfe.toString()+ "{0}", SystemLogger.getLineNum());
            throw new Exception("Improperly assigned series for Stage Two interaction count");
        }
        return stageTwoInteractionInteger != -1;
    }
    
    /**
     * Validates all fields in the definition file
     * @throws Exception if one or more fields are not accurate
     */
    private void exportValidator() throws Exception {
        /**
         *  Validating Stage 1 General Variables
         */
        if (!validateFieldLabels(getAdvancedMeanRegressorCount(), getSharedModelMeanRegressorFields())) {
            throw new Exception("Fatal model error: number of MEAN regressors does not equal MEAN fields");
        }
        if (!validateFieldLabels(getAdvancedMeanRegressorCount(), getSharedModelMeanRegressorLabels())) {
            throw new Exception("Fatal model error: number of MEAN regressors does not equal MEAN labels");
        }
        
        if (!validateFieldLabels(getAdvancedRandomRegressorCount(), getSharedModelRandomRegressorFields())) {
            throw new Exception("Fatal model error: number of RANDOM regressors does not equal RANDOM fields");
        }
        if (!validateFieldLabels(getAdvancedRandomRegressorCount(), getSharedModelRandomRegressorLabels())) {
            throw new Exception("Fatal model error: number of RANDOM regressors does not equal RANDOM labels");
        }
        
        if (!validateFieldLabels(getAdvancedScaleRegressorCount(), getSharedModelScaleRegressorFields())) {
            throw new Exception("Fatal model error: number of SCALE regressors does not equal SCALE fields");
        }
        if (!validateFieldLabels(getAdvancedScaleRegressorCount(), getSharedModelScaleRegressorLabels())) {
            throw new Exception("Fatal model error: number of SCALE regressors does not equal SCALE labels");
        }
        
        /**
         *  Validating Stage 1 Decomposition Variables
         */
        if (!validateFieldLabels(getAdvancedDecomposeMeanRegressorCount(), getSharedModelDecomposeMeanRegressorFields())) {
            throw new Exception("Fatal variance decomposition error: number of MEAN regressors does not equal MEAN fields");
        }
        if (!validateFieldLabels(getAdvancedDecomposeMeanRegressorCount(), getSharedModelDecomposeMeanRegressorLabels())) {
            throw new Exception("Fatal variance decomposition error: number of MEAN regressors does not equal MEAN labels");
        }
        
        if (!validateFieldLabels(getAdvancedDecomposeRandomRegressorCount(), getSharedModelDecomposeRandomRegressorFields())) {
            throw new Exception("Fatal variance decomposition error: number of RANDOM regressors does not equal RANDOM fields");
        }
        if (!validateFieldLabels(getAdvancedDecomposeRandomRegressorCount(), getSharedModelDecomposeRandomRegressorLabels())) {
            throw new Exception("Fatal variance decomposition error: number of RANDOM regressors does not equal RANDOM labels");
        }
        
        if (!validateFieldLabels(getAdvancedDecomposeScaleRegressorCount(), getSharedModelDecomposeScaleRegressorFields())) {
            throw new Exception("Fatal variance decomposition error: number of SCALE regressors does not equal SCALE fields");
        }
        if (!validateFieldLabels(getAdvancedDecomposeScaleRegressorCount(), getSharedModelDecomposeScaleRegressorLabels())) {
            throw new Exception("Fatal variance decomposition error: number of SCALE regressors does not equal SCALE labels");
        }
        
        /**
         * Validating Stage 2 (Optional)
         */
        if(stageTwoModelType != STAGE_TWO_MODEL_TYPE_NONE){
            if (!validateFieldLabels(getAdvancedStageTwoFixedRegressorCount(), getStageTwoFixedFields())) {
                throw new Exception("Fatal stage two error: number of FIXED regressors does not equal FIXED fields");
            }        
            if (!validateFieldLabels(getAdvancedStageTwoFixedRegressorCount(), getStageTwoFixedLabels())) {
                throw new Exception("Fatal stage two error: number of FIXED regressors does not equal FIXED labels");
            }   
            
            if (!validateFieldLabels(getAdvancedStageTwoThetaRegressorCount(), getStageTwoThetaFields())) {
                throw new Exception("Fatal stage two error: number of THETA regressors does not equal THETA fields");
            }        
            if (!validateFieldLabels(getAdvancedStageTwoThetaRegressorCount(), getStageTwoThetaLabels())) {
                throw new Exception("Fatal stage two error: number of THETA regressors does not equal THETA labels");
            }   
            
            if (!validateFieldLabels(getAdvancedStageTwoOmegaRegressorCount(), getStageTwoOmegaFields())) {
                throw new Exception("Fatal stage two error: number of OMEGA regressors does not equal OMEGA fields");
            }        
            if (!validateFieldLabels(getAdvancedStageTwoOmegaRegressorCount(), getStageTwoOmegaLabels())) {
                throw new Exception("Fatal stage two error: number of OMEGA regressors does not equal OMEGA labels");
            }
            
            /**
             *  Additional step here because the Interaction field can be -1.
             */
            if (!isAdvancedStageTwoInteractionIncluded() && !validateFieldLabels("0", getStageTwoInteractionFields())) {
                throw new Exception("Fatal stage two error: number of INTERACTION regressors does not equal INTERACTION fields");
            }        
            if (!isAdvancedStageTwoInteractionIncluded() && !validateFieldLabels("0", getStageTwoInteractionLabels())) {
                throw new Exception("Fatal stage two error: number of INTERACTION regressors does not equal INTERACTION labels");
            }
            
            if (isAdvancedStageTwoInteractionIncluded() && !validateFieldLabels(getAdvancedStageTwoInteractionRegressorCount(), getStageTwoInteractionFields())) {
                throw new Exception("Fatal stage two error: number of INTERACTION regressors does not equal INTERACTION fields");
            }        
            if (isAdvancedStageTwoInteractionIncluded() && !validateFieldLabels(getAdvancedStageTwoInteractionRegressorCount(), getStageTwoInteractionLabels())) {
                throw new Exception("Fatal stage two error: number of INTERACTION regressors does not equal INTERACTION labels");
            }
        }
    }
    
    /**
     * validates setters in library
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
                SystemLogger.LOGGER.log(Level.SEVERE, nfe.toString()+ "{0}", SystemLogger.getLineNum());
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


    public int getStageOneOutcome() {
        return stageOneOutcome;
    }

    public void setStageOneOutcome(int stageOneOutcome) {
        this.stageOneOutcome = stageOneOutcome;
    }

    public int getStageOneRandomLocationEffects() {
        return stageOneRandomLocationEffects;
    }

    public void setStageOneRandomLocationEffects(int stageOneRandomLocationEffects) {
        this.stageOneRandomLocationEffects = stageOneRandomLocationEffects;
    }

    public boolean isStageOneRandomScale() {
        if (stageOneRandomScale == 1){
            return true;
        } else if (stageOneRandomScale == 0){
            return false;
        }
        return false;
    }

    public void setStageOneRandomScale(int stageOneRandomScale) {
        this.stageOneRandomScale = stageOneRandomScale;
    }

    public int getStageTwoModelType() {
        return stageTwoModelType;
    }

    public void setStageTwoModelType(int stageTwoModelType) {
        this.stageTwoModelType = stageTwoModelType;
    }

    public int getStageTwoOutcomeType() {
        return stageTwoOutcomeType;
    }

    public void setStageTwoOutcomeType(int stageTwoOutcomeType) {
        this.stageTwoOutcomeType = stageTwoOutcomeType;
    }

    public String getSharedModelTitle() {
        return sharedModelTitle;
    }

    public void setSharedModelTitle(String sharedModelTitle) {
        if (sharedModelTitle.length() > 72) {
            this.sharedModelTitle = sharedModelTitle.substring(0, 71);
        } else {
            this.sharedModelTitle = sharedModelTitle;
        }
    }

    public String getSharedModelSubtitle() {
        return sharedModelSubtitle;
    }

    public void setSharedModelSubtitle(String sharedModelSubtitle) {
        if (sharedModelSubtitle.length() > 72) {
            this.sharedModelSubtitle = sharedModelSubtitle.substring(0, 71);
        } else {
            this.sharedModelSubtitle = sharedModelSubtitle;
        }
    }

    public String getSharedDataFilename() {
        return sharedDataFilename;
    }

    public void setSharedDataFilename(String sharedDataFilename) throws Exception {
        if (sharedDataFilename.endsWith(".dat") || sharedDataFilename.endsWith(".csv")) {
            this.sharedDataFilename = sharedDataFilename.replace(" ", "_");
        } else {
            throw new Exception("Data file name is not a valid .dat or .csv file");
        }
    }

    public String getSharedOutputPrefix() {
        return sharedOutputPrefix;
    }

    public void setSharedOutputPrefix(String sharedOutputPrefix) {
        if (sharedOutputPrefix.length() > 72) {
            this.sharedOutputPrefix = sharedOutputPrefix.substring(0, 200).replace(" ", "_");
        } else {
            this.sharedOutputPrefix = sharedOutputPrefix.replace(" ", "_");;
        }
        this.sharedOutputPrefix = sharedOutputPrefix.replace(" ", "_");;
    }

    public String[] getSharedAdvancedOptions() {
        if(sharedAdvancedOptions == null){
            setSharedAdvancedOptions();
        }
        return sharedAdvancedOptions;
    }

    public void setSharedAdvancedOptions() {
        this.sharedAdvancedOptions = advancedVariableBuild(1); // = sharedAdvancedOptions;
    }

    public String[] getSharedIdAndStageOneOutcomeFields() {
        return sharedIdAndStageOneOutcomeFields;
    }

    public void setSharedIdAndStageOneOutcomeFields(String[] sharedIdAndStageOneOutcomeFields) {
        this.sharedIdAndStageOneOutcomeFields = sharedIdAndStageOneOutcomeFields;
    }

    public String[] getSharedModelMeanRegressorFields() {
        return sharedModelMeanRegressorFields;
    }

    public void setSharedModelMeanRegressorFields(String[] sharedModelMeanRegressorFields) {
        this.sharedModelMeanRegressorFields = sharedModelMeanRegressorFields;
    }

    public String[] getSharedModelRandomRegressorFields() {
        return sharedModelRandomRegressorFields;
    }

    public void setSharedModelRandomRegressorFields(String[] sharedModelRandomRegressorFields) {
        this.sharedModelRandomRegressorFields = sharedModelRandomRegressorFields;
    }

    public String[] getSharedModelScaleRegressorFields() {
        return sharedModelScaleRegressorFields;
    }

    public void setSharedModelScaleRegressorFields(String[] sharedModelScaleRegressorFields) {
        this.sharedModelScaleRegressorFields = sharedModelScaleRegressorFields;
    }

    public String[] getSharedModelDecomposeMeanRegressorFields() {
        return sharedModelDecomposeMeanRegressorFields;
    }

    public void setSharedModelDecomposeMeanRegressorFields(String[] sharedModelDecomposeMeanRegressorFields) {
        this.sharedModelDecomposeMeanRegressorFields = sharedModelDecomposeMeanRegressorFields;
    }

    public String[] getSharedModelDecomposeRandomRegressorFields() {
        return sharedModelDecomposeRandomRegressorFields;
    }

    public void setSharedModelDecomposeRandomRegressorFields(String[] sharedModelDecomposeRandomRegressorFields) {
        this.sharedModelDecomposeRandomRegressorFields = sharedModelDecomposeRandomRegressorFields;
    }

    public String[] getSharedModelDecomposeScaleRegressorFields() {
        return sharedModelDecomposeScaleRegressorFields;
    }

    public void setSharedModelDecomposeScaleRegressorFields(String[] sharedModelDecomposeScaleRegressorFields) {
        this.sharedModelDecomposeScaleRegressorFields = sharedModelDecomposeScaleRegressorFields;
    }

    public String getSharedModelStageOneOutcomeLabel() {
        return sharedModelStageOneOutcomeLabel;
    }

    public void setSharedModelStageOneOutcomeLabel(String sharedModelStageOneOutcomeLabel) {
        this.sharedModelStageOneOutcomeLabel = sharedModelStageOneOutcomeLabel;
    }

    public String[] getSharedModelMeanRegressorLabels() {
        return sharedModelMeanRegressorLabels;
    }

    public void setSharedModelMeanRegressorLabels(String[] sharedModelMeanRegressorLabels) {
        this.sharedModelMeanRegressorLabels = sharedModelMeanRegressorLabels;
    }

    public String[] getSharedModelRandomRegressorLabels() {
        return sharedModelRandomRegressorLabels;
    }

    public void setSharedModelRandomRegressorLabels(String[] sharedModelRandomRegressorLabels) {
        this.sharedModelRandomRegressorLabels = sharedModelRandomRegressorLabels;
    }

    public String[] getSharedModelScaleRegressorLabels() {
        return sharedModelScaleRegressorLabels;
    }

    public void setSharedModelScaleRegressorLabels(String[] sharedModelScaleRegressorLabels) {
        this.sharedModelScaleRegressorLabels = sharedModelScaleRegressorLabels;
    }

    public String[] getSharedModelDecomposeMeanRegressorLabels() {
        return sharedModelDecomposeMeanRegressorLabels;
    }

    public void setSharedModelDecomposeMeanRegressorLabels(String[] sharedModelDecomposeMeanRegressorLabels) {
        this.sharedModelDecomposeMeanRegressorLabels = sharedModelDecomposeMeanRegressorLabels;
    }

    public String[] getSharedModelDecomposeRandomRegressorLabels() {
        return sharedModelDecomposeRandomRegressorLabels;
    }

    public void setSharedModelDecomposeRandomRegressorLabels(String[] sharedModelDecomposeRandomRegressorLabels) {
        this.sharedModelDecomposeRandomRegressorLabels = sharedModelDecomposeRandomRegressorLabels;
    }

    public String[] getSharedModelDecomposeScaleRegressorLabels() {
        return sharedModelDecomposeScaleRegressorLabels;
    }

    public void setSharedModelDecomposeScaleRegressorLabels(String[] sharedModelDecomposeScaleRegressorLabels) {
        this.sharedModelDecomposeScaleRegressorLabels = sharedModelDecomposeScaleRegressorLabels;
    }

    public String[] getMixorModelCovarianceThresholdParameters() {
        if(mixorModelCovarianceThresholdParameters == null){
            setMixorModelCovarianceThresholdParameters();
        }
        return mixorModelCovarianceThresholdParameters;
    }

    public void setMixorModelCovarianceThresholdParameters() {
        this.mixorModelCovarianceThresholdParameters = 
                new String[]{getMixorModelCovarianceParameter(),getMixorModelThresholdParameter()};
    }

    public String[] getMixorModelStageOneOutcomeLevels() {
        return mixorModelStageOneOutcomeLevels;
    }

    public void setMixorModelStageOneOutcomeLevels(String[] mixorModelStageOneOutcomeLevels) {
        this.mixorModelStageOneOutcomeLevels = mixorModelStageOneOutcomeLevels;
    }

    public String[] getStageTwoRegressorCounts() {
        if(stageTwoRegressorCounts == null){
            setStageTwoRegressorCounts();
        }
        return stageTwoRegressorCounts;
    }

    public void setStageTwoRegressorCounts() {
        this.stageTwoRegressorCounts = advancedVariableBuild(2);
    }

    public String getStageTwoOutcomeField() {
        return stageTwoOutcomeField;
    }

    public void setStageTwoOutcomeField(String stageTwoOutcomeField) {
        this.stageTwoOutcomeField = stageTwoOutcomeField;
    }

    public String[] getStageTwoFixedFields() {
        return stageTwoFixedFields;
    }

    public void setStageTwoFixedFields(String[] stageTwoFixedFields) {
        this.stageTwoFixedFields = stageTwoFixedFields;
    }

    public String[] getStageTwoThetaFields() {
        return stageTwoThetaFields;
    }

    public void setStageTwoThetaFields(String[] stageTwoThetaFields) {
        this.stageTwoThetaFields = stageTwoThetaFields;
    }

    public String[] getStageTwoOmegaFields() {
        return stageTwoOmegaFields;
    }

    public void setStageTwoOmegaFields(String[] stageTwoOmegaFields) {
        this.stageTwoOmegaFields = stageTwoOmegaFields;
    }

    public String[] getStageTwoInteractionFields() {
        return stageTwoInteractionFields;
    }

    public void setStageTwoInteractionFields(String[] stageTwoInteractionFields) {
        this.stageTwoInteractionFields = stageTwoInteractionFields;
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

    public String[] getStageTwoThetaLabels() {
        return stageTwoThetaLabels;
    }

    public void setStageTwoThetaLabels(String[] stageTwoThetaLabels) {
        this.stageTwoThetaLabels = stageTwoThetaLabels;
    }

    public String[] getStageTwoOmegaLabels() {
        return stageTwoOmegaLabels;
    }

    public void setStageTwoOmegaLabels(String[] stageTwoOmegaLabels) {
        this.stageTwoOmegaLabels = stageTwoOmegaLabels;
    }

    public String[] getStageTwoInteractionLabels() {
        return stageTwoInteractionLabels;
    }

    public void setStageTwoInteractionLabels(String[] stageTwoInteractionLabels) {
        this.stageTwoInteractionLabels = stageTwoInteractionLabels;
    }

    public String getAdvancedVariableCount() {
        return advancedVariableCount;
    }

    public void setAdvancedVariableCount(String advancedVariableCount) throws Exception {
        if (setValidator("number of variables", "5", advancedVariableCount, 2, 255, MIX_INTEGER)) {
            this.advancedVariableCount = advancedVariableCount;
        }
    }

    public String getAdvancedMeanRegressorCount() {
        return advancedMeanRegressorCount;
    }

    public void setAdvancedMeanRegressorCount(String advancedMeanRegressorCount) {
        this.advancedMeanRegressorCount = advancedMeanRegressorCount;
    }

    public String getAdvancedRandomRegressorCount() {
        return advancedRandomRegressorCount;
    }

    public void setAdvancedRandomRegressorCount(String advancedRandomRegressorCount) {
        this.advancedRandomRegressorCount = advancedRandomRegressorCount;
    }

    public String getAdvancedScaleRegressorCount() {
        return advancedScaleRegressorCount;
    }

    public void setAdvancedScaleRegressorCount(String advancedScaleRegressorCount) {
        this.advancedScaleRegressorCount = advancedScaleRegressorCount;
    }

    public String getAdvancedStageOneOutcomeValueCount() {
        return advancedStageOneOutcomeValueCount;
    }

    public void setAdvancedStageOneOutcomeValueCount(String advancedStageOneOutcomeValueCount) {
        this.advancedStageOneOutcomeValueCount = advancedStageOneOutcomeValueCount;
    }

    public String getAdvancedMeanIntercept() {
        return advancedMeanIntercept;
    }

    public void setAdvancedMeanIntercept(String advancedMeanIntercept) {
        this.advancedMeanIntercept = advancedMeanIntercept;
    }

    public String getAdvancedRandomIntercept() {
        return advancedRandomIntercept;
    }

    public void setAdvancedRandomIntercept(String advancedRandomIntercept) {
        this.advancedRandomIntercept = advancedRandomIntercept;
    }

    public String getAdvancedScaleIntercept() {
        return advancedScaleIntercept;
    }

    public void setAdvancedScaleIntercept(String advancedScaleIntercept) {
        this.advancedScaleIntercept = advancedScaleIntercept;
    }

    public String getAdvancedDecomposeMeanRegressorCount() {
        return advancedDecomposeMeanRegressorCount;
    }

    public void setAdvancedDecomposeMeanRegressorCount(String advancedDecomposeMeanRegressorCount) {
        this.advancedDecomposeMeanRegressorCount = advancedDecomposeMeanRegressorCount;
    }

    public String getAdvancedDecomposeRandomRegressorCount() {
        return advancedDecomposeRandomRegressorCount;
    }

    public void setAdvancedDecomposeRandomRegressorCount(String advancedDecomposeRandomRegressorCount) {
        this.advancedDecomposeRandomRegressorCount = advancedDecomposeRandomRegressorCount;
    }

    public String getAdvancedDecomposeScaleRegressorCount() {
        return advancedDecomposeScaleRegressorCount;
    }

    public void setAdvancedDecomposeScaleRegressorCount(String advancedDecomposeScaleRegressorCount) {
        this.advancedDecomposeScaleRegressorCount = advancedDecomposeScaleRegressorCount;
    }

    public String getAdvancedConvergenceCriteria() {
        return advancedConvergenceCriteria;
    }

    public void setAdvancedConvergenceCriteria(String advancedConvergenceCriteria) {
        this.advancedConvergenceCriteria = advancedConvergenceCriteria;
    }

    public String getAdvancedQuadPoints() {
        return advancedQuadPoints;
    }

    public void setAdvancedQuadPoints(String advancedQuadPoints) {
        this.advancedQuadPoints = advancedQuadPoints;
    }

    public String getAdvancedAdaptiveQuad() {
        return advancedAdaptiveQuad;
    }

    public void setAdvancedAdaptiveQuad(String advancedAdaptiveQuad) {
        this.advancedAdaptiveQuad = advancedAdaptiveQuad;
    }

    public String getAdvancedMaxIterations() {
        return advancedMaxIterations;
    }

    public void setAdvancedMaxIterations(String advancedMaxIterations) {
        this.advancedMaxIterations = advancedMaxIterations;
    }

    public String getAdvancedMissingValueCode() {
        return advancedMissingValueCode;
    }

    public void setAdvancedMissingValueCode(String advancedMissingValueCode) {
        this.advancedMissingValueCode = advancedMissingValueCode;
    }

    public String getAdvancedCenterScaleVariables() {
        return advancedCenterScaleVariables;
    }

    public void setAdvancedCenterScaleVariables(String advancedCenterScaleVariables) {
        this.advancedCenterScaleVariables = advancedCenterScaleVariables;
    }

    public String getAdvancedRandomScaleAssociation() {
        return advancedRandomScaleAssociation;
    }

    public void setAdvancedRandomScaleAssociation(String advancedRandomScaleAssociation) {
        this.advancedRandomScaleAssociation = advancedRandomScaleAssociation;
    }

    public String getAdvancedInitialRidge() {
        return advancedInitialRidge;
    }

    public void setAdvancedInitialRidge(String advancedInitialRidge) {
        this.advancedInitialRidge = advancedInitialRidge;
    }

    public String getAdvancedLogisticProbitRegression() {
        return advancedLogisticProbitRegression;
    }

    public void setAdvancedLogisticProbitRegression(String advancedLogisticProbitRegression) {
        this.advancedLogisticProbitRegression = advancedLogisticProbitRegression;
    }

    public String getAdvancedDiscardNoVariance() {
        return advancedDiscardNoVariance;
    }

    public void setAdvancedDiscardNoVariance(String advancedDiscardNoVariance) {
        this.advancedDiscardNoVariance = advancedDiscardNoVariance;
    }

    public String getAdvancedUseMLS() {
        return advancedUseMLS;
    }

    public void setAdvancedUseMLS(String advancedUseMLS) {
        this.advancedUseMLS = advancedUseMLS;
    }

    public String getAdvancedCovarianceMatrix() {
        return advancedCovarianceMatrix;
    }

    public void setAdvancedCovarianceMatrix(String advancedCovarianceMatrix) {
        this.advancedCovarianceMatrix = advancedCovarianceMatrix;
    }

    public String getAdvancedResampleCount() {
        return advancedResampleCount;
    }

    public void setAdvancedResampleCount(String advancedResampleCount) {
        this.advancedResampleCount = advancedResampleCount;
    }

    public String getAdvancedRandomScaleCutoff() {
        return advancedRandomScaleCutoff;
    }

    public void setAdvancedRandomScaleCutoff(String advancedRandomScaleCutoff) {
        this.advancedRandomScaleCutoff = advancedRandomScaleCutoff;
    }

    public String getAdvancedUseRandomScale() {
        return advancedUseRandomScale;
    }

    public void setAdvancedUseRandomScale(String advancedUseRandomScale) {
        this.advancedUseRandomScale = advancedUseRandomScale;
    }

    public String getAdvancedResamplingSeed() {
        return advancedResamplingSeed;
    }

    public void setAdvancedResamplingSeed(String advancedResamplingSeed) {
        this.advancedResamplingSeed = advancedResamplingSeed;
    }

    public String getAdvancedUseStageTwo() {
        return advancedUseStageTwo;
    }

    public void setAdvancedUseStageTwo(String advancedUseStageTwo) {
        this.advancedUseStageTwo = advancedUseStageTwo;
    }

    public String getAdvancedStageTwoMultilevel() {
        return advancedStageTwoMultilevel;
    }

    public void setAdvancedStageTwoMultilevel(String advancedStageTwoMultilevel) {
        this.advancedStageTwoMultilevel = advancedStageTwoMultilevel;
    }

    public String getAdvancedMultipleDataFiles() {
        return advancedMultipleDataFiles;
    }

    public void setAdvancedMultipleDataFiles(String advancedMultipleDataFiles) {
        this.advancedMultipleDataFiles = advancedMultipleDataFiles;
    }

    public String getAdvancedStageTwoFixedRegressorCount() {
        return advancedStageTwoFixedRegressorCount;
    }

    public void setAdvancedStageTwoFixedRegressorCount(String advancedStageTwoFixedRegressorCount) {
        this.advancedStageTwoFixedRegressorCount = advancedStageTwoFixedRegressorCount;
    }

    public String getAdvancedStageTwoThetaRegressorCount() {
        return advancedStageTwoThetaRegressorCount;
    }

    public void setAdvancedStageTwoThetaRegressorCount(String advancedStageTwoThetaRegressorCount) {
        this.advancedStageTwoThetaRegressorCount = advancedStageTwoThetaRegressorCount;
    }

    public String getAdvancedStageTwoOmegaRegressorCount() {
        return advancedStageTwoOmegaRegressorCount;
    }

    public void setAdvancedStageTwoOmegaRegressorCount(String advancedStageTwoOmegaRegressorCount) {
        this.advancedStageTwoOmegaRegressorCount = advancedStageTwoOmegaRegressorCount;
    }

    public String getAdvancedStageTwoInteractionRegressorCount() {
        return advancedStageTwoInteractionRegressorCount;
    }

    public void setAdvancedStageTwoInteractionRegressorCount(String advancedStageTwoInteractionRegressorCount) {
        this.advancedStageTwoInteractionRegressorCount = advancedStageTwoInteractionRegressorCount;
    }

    public String getMixorModelCovarianceParameter() {
        return mixorModelCovarianceParameter;
    }

    public void setMixorModelCovarianceParameter(String mixorModelCovarianceParameter) {
        this.mixorModelCovarianceParameter = mixorModelCovarianceParameter;
    }

    public String getMixorModelThresholdParameter() {
        return mixorModelThresholdParameter;
    }

    public void setMixorModelThresholdParameter(String mixorModelThresholdParameter) {
        this.mixorModelThresholdParameter = mixorModelThresholdParameter;
    }
    
        //auxiliary functions
    public void setUtcDirPath(File csvFileLocation) throws IOException {
        String utcDirPath = ModelBuilder.buildFolder(csvFileLocation);
        this.utcDirPath = utcDirPath;
    }
    
    public void setUtcDirPath(String folderAbsolutePath) {
        this.utcDirPath = folderAbsolutePath;
    }
    
    public String getUtcDirPath() {
        return utcDirPath;
    }
        
    public void csvToDatConverter(File csvFileToConvert) throws IOException {
        String fileName = csvFileToConvert.getAbsolutePath();
        String fileNameShort = FilenameUtils.removeExtension(fileName);
        String baseName = FilenameUtils.getBaseName(fileName);
        String filePath = FilenameUtils.getFullPath(fileName);
        // TODO: Deprecate
        //String filePath = fileName.substring(0, fileName.lastIndexOf(File.separator)) + "/"; //subset the string.

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            setUtcDirPath(csvFileToConvert);
            List<String[]> csvRows = reader.readAll();
            reader.close();
            System.out.println(Arrays.toString(csvRows.get(0)) + " to be removed");
            csvRows.remove(0); // TODO: make sure this isn't removing data
            System.out.println("New:" + Arrays.toString(csvRows.get(0)));

            CSVWriter writer = new CSVWriter(new FileWriter(filePath + utcDirPath + baseName.replace(" ", "_") + ".dat"), ' ', CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.RFC4180_LINE_END);
            writer.writeAll(csvRows);
            writer.close();
        }
    }

    public String[] getLabelModelMeanRegressorsLevelOne() {
        return labelModelMeanRegressorsLevelOne;
    }

    public void setLabelModelMeanRegressorsLevelOne(String[] labelModelMeanRegressorsLevelOne) {
        this.labelModelMeanRegressorsLevelOne = labelModelMeanRegressorsLevelOne;
    }

    public String[] getLabelModelLocRanRegressorsLevelOne() {
        return labelModelLocRanRegressorsLevelOne;
    }

    public void setLabelModelLocRanRegressorsLevelOne(String[] labelModelLocRanRegressorsLevelOne) {
        this.labelModelLocRanRegressorsLevelOne = labelModelLocRanRegressorsLevelOne;
    }

    public String[] getLabelModelScaleRegressorsLevelOne() {
        return labelModelScaleRegressorsLevelOne;
    }

    public void setLabelModelScaleRegressorsLevelOne(String[] labelModelScaleRegressorsLevelOne) {
        this.labelModelScaleRegressorsLevelOne = labelModelScaleRegressorsLevelOne;
    }

    public String[] getLabelModelBSRegressorsLevelOne() {
        return labelModelBSRegressorsLevelOne;
    }

    public void setLabelModelBSRegressorsLevelOne(String[] labelModelBSRegressorsLevelOne) {
        this.labelModelBSRegressorsLevelOne = labelModelBSRegressorsLevelOne;
    }

    public String[] getLabelModelWSRegressorsLevelOne() {
        return labelModelWSRegressorsLevelOne;
    }

    public void setLabelModelWSRegressorsLevelOne(String[] labelModelWSRegressorsLevelOne) {
        this.labelModelWSRegressorsLevelOne = labelModelWSRegressorsLevelOne;
    }

    public String[] getLabelModelMeanRegressorsLevelTwo() {
        return labelModelMeanRegressorsLevelTwo;
    }

    public void setLabelModelMeanRegressorsLevelTwo(String[] labelModelMeanRegressorsLevelTwo) {
        this.labelModelMeanRegressorsLevelTwo = labelModelMeanRegressorsLevelTwo;
    }

    public String[] getLabelModelLocRanRegressorsLevelTwo() {
        return labelModelLocRanRegressorsLevelTwo;
    }

    public void setLabelModelLocRanRegressorsLevelTwo(String[] labelModelLocRanRegressorsLevelTwo) {
        this.labelModelLocRanRegressorsLevelTwo = labelModelLocRanRegressorsLevelTwo;
    }

    public String[] getLabelModelScaleRegressorsLevelTwo() {
        return labelModelScaleRegressorsLevelTwo;
    }

    public void setLabelModelScaleRegressorsLevelTwo(String[] labelModelScaleRegressorsLevelTwo) {
        this.labelModelScaleRegressorsLevelTwo = labelModelScaleRegressorsLevelTwo;
    }

    public String[] getLabelModelBSRegressorsLevelTwo() {
        return labelModelBSRegressorsLevelTwo;
    }

    public void setLabelModelBSRegressorsLevelTwo(String[] labelModelBSRegressorsLevelTwo) {
        this.labelModelBSRegressorsLevelTwo = labelModelBSRegressorsLevelTwo;
    }

    public String[] getLabelModelWSRegressorsLevelTwo() {
        return labelModelWSRegressorsLevelTwo;
    }

    public void setLabelModelWSRegressorsLevelTwo(String[] labelModelWSRegressorsLevelTwo) {
        this.labelModelWSRegressorsLevelTwo = labelModelWSRegressorsLevelTwo;
    }
    
    public void writeStageOneOnlyDefFileToFolder() {}
    
    public void writeDefFileToFolder() {}

    CharSequence[] debugStageOneDefinitonList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
