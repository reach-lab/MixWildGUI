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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private boolean stageOneRandomScale;
    private int stageTwoModelType;
    private int stageTwoOutcomeType;
   
    
    /**
     * MixWILD V2.0 Initialization of DefinitionHelper without Stage Two
     * 
     * @param stageOneOutcome STAGE_ONE_OUTCOME_MIXREG or STAGE_ONE_OUTCOME_MIXREG
     * @param stageOneRandomLocationEffects STAGE_ONE_RLE_NONE or STAGE_ONE_RLE_LOCATION or STAGE_ONE_RLE_SLOPE
     * @param stageOneRandomScale STAGE_ONE_SCALE_NO or STAGE_ONE_SCALE_YES
     */
    public MixLibrary(int stageOneOutcome, int stageOneRandomLocationEffects, 
            boolean stageOneRandomScale) {
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
            boolean stageOneRandomScale, int stageTwoModelType, int stageTwoOutcomeType) {
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
    private String[] sharedAdvancedOptions; // LINE 5
    
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
    private String[] stageTwoRegressorCounts; 
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
    
    
    private String[] advancedVariableBuild() {
        List<String> advancedVariable = new ArrayList();
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
        
        String[] returnVars = new String[advancedVariable.size()];
        int iter = 0;
        for (String iterate : advancedVariable) {
            returnVars[iter] = iterate;
            iter++;
        };
        return(returnVars);
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
        return stageOneRandomScale;
    }

    public void setStageOneRandomScale(boolean stageOneRandomScale) {
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
        this.sharedModelTitle = sharedModelTitle;
    }

    public String getSharedModelSubtitle() {
        return sharedModelSubtitle;
    }

    public void setSharedModelSubtitle(String sharedModelSubtitle) {
        this.sharedModelSubtitle = sharedModelSubtitle;
    }

    public String getSharedDataFilename() {
        return sharedDataFilename;
    }

    public void setSharedDataFilename(String sharedDataFilename) {
        this.sharedDataFilename = sharedDataFilename;
    }

    public String getSharedOutputPrefix() {
        return sharedOutputPrefix;
    }

    public void setSharedOutputPrefix(String sharedOutputPrefix) {
        this.sharedOutputPrefix = sharedOutputPrefix;
    }

    public String[] getSharedAdvancedOptions() {
        return sharedAdvancedOptions;
    }

    public void setSharedAdvancedOptions() {
        this.sharedAdvancedOptions = advancedVariableBuild(); // = sharedAdvancedOptions;
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
        return mixorModelCovarianceThresholdParameters;
    }

    public void setMixorModelCovarianceThresholdParameters(String[] mixorModelCovarianceThresholdParameters) {
        this.mixorModelCovarianceThresholdParameters = mixorModelCovarianceThresholdParameters;
    }

    public String[] getMixorModelStageOneOutcomeLevels() {
        return mixorModelStageOneOutcomeLevels;
    }

    public void setMixorModelStageOneOutcomeLevels(String[] mixorModelStageOneOutcomeLevels) {
        this.mixorModelStageOneOutcomeLevels = mixorModelStageOneOutcomeLevels;
    }

    public String[] getStageTwoRegressorCounts() {
        return stageTwoRegressorCounts;
    }

    public void setStageTwoRegressorCounts(String[] stageTwoRegressorCounts) {
        this.stageTwoRegressorCounts = stageTwoRegressorCounts;
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

    public void setAdvancedVariableCount(String advancedVariableCount) {
        this.advancedVariableCount = advancedVariableCount;
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
    
    
    
    
    

}
