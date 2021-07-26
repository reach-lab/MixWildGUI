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
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
import mixregui.SystemLogger;
import mixregui.mixregGUI;
import org.apache.commons.io.FileUtils;
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
    public int terminalVal;
    String definitionFilepath;
    JTextArea progressPane;

    /**
     * Stage One Outcome Keys
     */
    public static final int STAGE_ONE_OUTCOME_MIXREG = 1;
    public static final int STAGE_ONE_OUTCOME_MIXOR = 2;

    /**
     * Stage One Random Location Keys
     */
    // public static final int STAGE_ONE_RLE_NONE = 1;
    public static final int STAGE_ONE_RLE_LOCATION = 0;
    public static final int STAGE_ONE_RLE_SLOPE = 1;

    /**
     * Stage One Random Scale Keys
     */
    public static final int STAGE_ONE_SCALE_NO = 0;
    public static final int STAGE_ONE_SCALE_YES = 1;

    /**
     * Stage Two Outcome Keys
     */
    public static final int STAGE_TWO_OUTCOME_NONE = 0;
    public static final int STAGE_TWO_OUTCOME_NORMAL = 1;
    public static final int STAGE_TWO_OUTCOME_ORDINAL = 2;
    public static final int STAGE_TWO_OUTCOME_COUNT = 3;
    public static final int STAGE_TWO_OUTCOME_NOMINAL = 4;

    /**
     * Stage Two Model Type Keys
     */
    public static final int STAGE_TWO_MODEL_TYPE_SINGLE = 0;
    public static final int STAGE_TWO_MODEL_TYPE_MULTILEVEL = 1;

    /**
     * MixWILD V2.0 Initialization Parameters
     */
    private int stageOneOutcome;
    private int stageOneRandomLocationEffects;
    private int stageOneRandomScale;
    private int stageTwoModelType;
    private int stageTwoOutcomeType;
    private boolean stageTwoNewDataIncluded;

    //auxiliary fields
    private String utcDirPath;
    public Boolean win32 = false;
    private JFrame myFrame;
    private JEditorPane myPane;
    private ProgressStatus progressStatus;
    JFrame progressWindow;
    private File newDefFile;

    /**
     * MixWILD V2.0 Initialization of DefinitionHelper without Stage Two
     *
     * @param stageOneOutcome STAGE_ONE_OUTCOME_MIXREG or
     * STAGE_ONE_OUTCOME_MIXREG
     * @param stageOneRandomLocationEffects STAGE_ONE_RLE_NONE or
     * STAGE_ONE_RLE_LOCATION or STAGE_ONE_RLE_SLOPE
     * @param stageOneRandomScale STAGE_ONE_SCALE_NO or STAGE_ONE_SCALE_YES
     */
    public MixLibrary(int stageOneOutcome, int stageOneRandomLocationEffects,
            int stageOneRandomScale) {
        this.stageOneOutcome = stageOneOutcome;
        this.stageOneRandomLocationEffects = stageOneRandomLocationEffects;
        this.stageOneRandomScale = stageOneRandomScale;
        this.stageTwoModelType = STAGE_TWO_MODEL_TYPE_SINGLE;
        this.setStageTwoModelType(STAGE_TWO_MODEL_TYPE_SINGLE);
        this.stageTwoOutcomeType = STAGE_TWO_OUTCOME_NONE;
        this.setStageTwoOutcomeType(STAGE_TWO_OUTCOME_NONE);
    }

    /**
     * MixWILD V2.0 Initialization of DefinitionHelper with Stage Two
     *
     * @param stageOneOutcome STAGE_ONE_OUTCOME_MIXREG or
     * STAGE_ONE_OUTCOME_MIXREG
     * @param stageOneRandomLocationEffects STAGE_ONE_RLE_NONE or
     * STAGE_ONE_RLE_LOCATION or STAGE_ONE_RLE_SLOPE
     * @param stageOneRandomScale STAGE_ONE_SCALE_NO or STAGE_ONE_SCALE_YES
     * @param stageTwoModelType STAGE_TWO_MODEL_TYPE_SINGLE or
     * STAGE_TWO_MODEL_TYPE_MULTILEVEL
     * @param stageTwoOutcomeType STAGE_TWO_OUTCOME_NORMAL or
     * STAGE_TWO_OUTCOME_ORDINAL or STAGE_TWO_OUTCOME_COUNT or
     * STAGE_TWO_OUTCOME_NOMINAL
     */
    public MixLibrary(int stageOneOutcome, int stageOneRandomLocationEffects,
            int stageOneRandomScale, int stageTwoModelType, int stageTwoOutcomeType, boolean stageTwoNewDataIncluded) {
        this.stageOneOutcome = stageOneOutcome;
        this.stageOneRandomLocationEffects = stageOneRandomLocationEffects;
        this.stageOneRandomScale = stageOneRandomScale;
        this.stageTwoModelType = stageTwoModelType;
        this.stageTwoOutcomeType = stageTwoOutcomeType;
        this.stageTwoNewDataIncluded = stageTwoNewDataIncluded;
    }

    /**
     * MixWILD V2.0 SHARED Parameters
     */
    private String sharedModelTitle; // LINE 1
    private String sharedModelSubtitle; // LINE 2
    private String sharedDataFilename; // LINE 3
    private String sharedDataFilename_stageTwo;
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

    private String mixorModelStageOneOutcomeLevels; // LINE 14 for MIXOR

    /**
     * MixWILD V2.0 Stage Two Parameters Append at LINE 20 for MIXREG and LINE
     * 22 for MIXOR
     */
    private String[] stageTwoRegressorCounts; //  SEE LINE 220 FOR DETAILS
    private String stageTwoOutcomeField;
    private String stageTwoOutcomeCategoryNum;
    private String stageTwoCategoricalOutcomeUniqueList;

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
     * MixWILD V2.0 Stage Two Parameters for additional data in stage two
     */
    private String[] stageTwoNewDataFeatures;
    private String stageTwoNewDataVariableCount;
    private String stageTwoNewDataIDField;

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
     * Builds the advanced variable arrays for Stage 1 and Stage 2, as noted
     * above.
     *
     * @param stageToBuild 1 OR 2
     * @return array of advanced variable fields to pass to writer
     */
    private String[] advancedVariableBuild(int stageToBuild) {
        List<String> advancedVariable = new ArrayList();
        if (stageToBuild == 1) {
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
//                System.out.print("^^^^^^^^^^^^^^^");
//                System.out.print(getAdvancedResampleCount());
                advancedVariable.add(getAdvancedRandomScaleCutoff());
                advancedVariable.add(getAdvancedUseRandomScale());
                advancedVariable.add(getAdvancedResamplingSeed());

                advancedVariable.add(getAdvancedUseStageTwo());
                advancedVariable.add(getAdvancedStageTwoMultilevel());
                advancedVariable.add(getAdvancedMultipleDataFiles());
            }

            if (stageOneOutcome == STAGE_ONE_OUTCOME_MIXOR) {
                advancedVariable.add(getAdvancedVariableCount());  //1
                advancedVariable.add(getAdvancedStageOneOutcomeValueCount());  //2

                advancedVariable.add(getAdvancedMeanRegressorCount());  //3
                advancedVariable.add(getAdvancedRandomRegressorCount());  //4
                advancedVariable.add(getAdvancedScaleRegressorCount());  //5
                advancedVariable.add(getAdvancedDecomposeMeanRegressorCount());  //6
                advancedVariable.add(getAdvancedDecomposeRandomRegressorCount());  //7
                advancedVariable.add(getAdvancedDecomposeScaleRegressorCount());  //8

                advancedVariable.add(getAdvancedRandomIntercept());  //9

                advancedVariable.add(getAdvancedConvergenceCriteria());  //10
                advancedVariable.add(getAdvancedQuadPoints());  //11
                advancedVariable.add(getAdvancedAdaptiveQuad());  //12
                advancedVariable.add(getAdvancedMaxIterations());  //13
                advancedVariable.add(getAdvancedMissingValueCode());  //14

                advancedVariable.add(getAdvancedInitialRidge());  //15
                advancedVariable.add(getAdvancedLogisticProbitRegression());  //16
                advancedVariable.add(getAdvancedUseRandomScale());  //17

                advancedVariable.add(getAdvancedResamplingSeed());  //18
                advancedVariable.add(getAdvancedUseStageTwo());  //19
                advancedVariable.add(getAdvancedStageTwoMultilevel());  //20
                advancedVariable.add(getAdvancedResampleCount());  //21
                advancedVariable.add(getAdvancedMultipleDataFiles());  //22
                advancedVariable.add(getAdvancedUseMLS());  //23
                advancedVariable.add(getAdvancedRandomScaleAssociation());  //24
            }
        }

        if (stageToBuild == 2) {
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

    private String[] stageTwoNewDataFeaturesBuild() {
        List<String> stageTwoNewDataVariables = new ArrayList();

        stageTwoNewDataVariables.add(getStageTwoNewDataVariableCount());
        stageTwoNewDataVariables.add(getStageTwoNewDataIDField());

        String[] returnVars = new String[stageTwoNewDataVariables.size()];
        int iter = 0;
        for (String iterate : stageTwoNewDataVariables) {
            returnVars[iter] = iterate;
            iter++;
        };
        return returnVars;
    }

    /**
     * Creates the definition file list See comments for line numbers
     * referencing MIXOR/MIXREG
     *
     * @return a List object to be written as the def file
     * @throws Exception error message showing why the definition is invalid
     */
    public List<String> buildDefinitionList() throws Exception {
        List<String> newDefinitionFile = new ArrayList();

        newDefinitionFile.add(getSharedModelTitle()); // LINE 1
        newDefinitionFile.add(getSharedModelSubtitle()); // LINE 2
        // newDefinitionFile.add("\"" + FilenameUtils.getName(getSharedDataFilename()) + "\""); // LINE 3
        newDefinitionFile.add(FilenameUtils.getName(getSharedDataFilename())); // LINE 3
        newDefinitionFile.add(getSharedOutputPrefix()); // LINE 4
        newDefinitionFile.add(Arrays.toString(getSharedAdvancedOptions()).replaceAll(",", " ")); // LINE 5
//        System.out.print(Arrays.toString(getSharedAdvancedOptions()).replaceAll(",", " "));
//        System.out.print("\n");
        if (getStageOneOutcome() == STAGE_ONE_OUTCOME_MIXOR) {
            newDefinitionFile.add(Arrays.toString(getMixorModelCovarianceThresholdParameters()).replaceAll(",", " ")); // LINE 6/-
        }

        newDefinitionFile.add(Arrays.toString(getSharedIdAndStageOneOutcomeFields()).replaceAll(",", " ")); // LINE 7/6

        newDefinitionFile.add(Arrays.toString(getSharedModelMeanRegressorFields()).replaceAll(",", " ")); // LINE 8/7
        newDefinitionFile.add(Arrays.toString(getSharedModelRandomRegressorFields()).replaceAll(",", " ")); // LINE 9/8
        newDefinitionFile.add(Arrays.toString(getSharedModelScaleRegressorFields()).replaceAll(",", " ")); // LINE 10/9

        newDefinitionFile.add(Arrays.toString(getSharedModelDecomposeMeanRegressorFields()).replaceAll(",", " ")); // LINE 11/10
        newDefinitionFile.add(Arrays.toString(getSharedModelDecomposeRandomRegressorFields()).replaceAll(",", " ")); // LINE 12/11
        newDefinitionFile.add(Arrays.toString(getSharedModelDecomposeScaleRegressorFields()).replaceAll(",", " ")); // LINE 13/12

        if (getStageOneOutcome() == STAGE_ONE_OUTCOME_MIXOR) {
            newDefinitionFile.add(getMixorModelStageOneOutcomeLevels()); // LINE 14/-
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
        if (stageTwoOutcomeType != STAGE_TWO_OUTCOME_NONE) {
            newDefinitionFile.add(Arrays.toString(getStageTwoRegressorCounts()).replaceAll(",", " ")); // LINE 22/20

            // if stage 2 outcome is ordinal / multinomial, add additional two lines
            if (stageTwoOutcomeType == STAGE_TWO_OUTCOME_ORDINAL || stageTwoOutcomeType == STAGE_TWO_OUTCOME_NOMINAL) {
                newDefinitionFile.add(getStageTwoOutcomeCategoryNum());           // LINE 23/21
                newDefinitionFile.add(getStageTwoCategoricalOutcomeUniqueList()); // LINE 24/22
            }

            // if include additonal stage 2 data
            if (stageTwoNewDataIncluded == true) {
                // stage 2 dataset path
                newDefinitionFile.add(FilenameUtils.getName(getSharedDataFilename_stageTwo()));

                //  stage 2 number of variables, fileds of ID variable
                newDefinitionFile.add(Arrays.toString(getStageTwoNewDataFeatures()).replaceAll(",", " "));

            }

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
     * Validates that the field/label counts are equivalent to the number of
     * fields or labels.
     *
     * @param countVariable the field or label count variable
     * @param fieldLabelLine the actual field or label array
     * @return boolean, does the field/label count equal the number of elements
     * in the array
     * @throws Exception
     */
    private boolean validateFieldLabels(String countVariable, String[] fieldLabelLine) throws Exception {
        int field = -1;
        int labels = 0;
        try {
            field = Integer.parseInt(countVariable);
        } catch (Exception ex) {
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            throw new Exception("Unassigned count variable for one or more options or regressors");

        }
        try {
            labels = fieldLabelLine.length;
        } catch (Exception ex) {
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
            throw new Exception("Unassigned field or label series for one or more sets of options or regressors");
        }
        return field == labels;
    }

    /**
     * Checks to see if random location scale interactions in stage two are
     * included
     *
     * @return a boolean
     * @throws Exception if the interaction value is not an integer
     */
    private boolean isAdvancedStageTwoInteractionIncluded() throws Exception {
        int stageTwoInteractionInteger;
        try {
            stageTwoInteractionInteger = Integer.parseInt(getAdvancedStageTwoInteractionRegressorCount());
        } catch (NumberFormatException nfe) {
            SystemLogger.LOGGER.log(Level.SEVERE, nfe.toString() + "{0}", SystemLogger.getLineNum());
            throw new Exception("Improperly assigned series for Stage Two interaction count");
        }
        return stageTwoInteractionInteger != -1;
    }

    /**
     * Validates all fields in the definition file
     *
     * @throws Exception if one or more fields are not accurate
     */
    private void exportValidator() throws Exception {
        /**
         * Validating Stage 1 General Variables
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
         * Validating Stage 1 Decomposition Variables
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
        if (stageTwoOutcomeType != STAGE_TWO_OUTCOME_NONE) {
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
             * Additional step here because the Interaction field can be -1.
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
                SystemLogger.LOGGER.log(Level.SEVERE, nfe.toString() + "{0}", SystemLogger.getLineNum());
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
        if (stageOneRandomScale == 1) {
            return true;
        } else if (stageOneRandomScale == 0) {
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

    public String getSharedDataFilename_stageTwo() {
        return sharedDataFilename_stageTwo;
    }

    public void setSharedDataFilename_stageTwo(String sharedDataFilename_stageTwo) throws Exception {
        if (sharedDataFilename_stageTwo.endsWith(".dat") || sharedDataFilename_stageTwo.endsWith(".csv")) {
            this.sharedDataFilename_stageTwo = sharedDataFilename_stageTwo.replace(" ", "_");
        } else {
            throw new Exception("Stage 2 data file name is not a valid .dat or .csv file");
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
        // do not set if == null, it will prevent modifications to the def file
        setSharedAdvancedOptions();
        return sharedAdvancedOptions;
    }

    public void setSharedAdvancedOptions() {
        this.sharedAdvancedOptions = advancedVariableBuild(1); // = sharedAdvancedOptions;
    }

    public String[] getStageTwoNewDataFeatures() {
        // do not set if == null, it will prevent modifications to the def file
        setStageTwoNewDataFeatures();
        return stageTwoNewDataFeatures;
    }

    public void setStageTwoNewDataFeatures() {
        this.stageTwoNewDataFeatures = stageTwoNewDataFeaturesBuild(); // = sharedAdvancedOptions;
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
        if (mixorModelCovarianceThresholdParameters == null) {
            setMixorModelCovarianceThresholdParameters();
        }
        return mixorModelCovarianceThresholdParameters;
    }

    public void setMixorModelCovarianceThresholdParameters() {
        this.mixorModelCovarianceThresholdParameters
                = new String[]{getMixorModelCovarianceParameter(), getMixorModelThresholdParameter()};
    }

    public String getMixorModelStageOneOutcomeLevels() {
        return mixorModelStageOneOutcomeLevels;
    }

    public void setMixorModelStageOneOutcomeLevels(String mixorModelStageOneOutcomeLevels) {
        this.mixorModelStageOneOutcomeLevels = mixorModelStageOneOutcomeLevels;
    }

    public String[] getStageTwoRegressorCounts() {
        setStageTwoRegressorCounts();
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

    public String getStageTwoOutcomeCategoryNum() {
        return stageTwoOutcomeCategoryNum;
    }

    public void setStageTwoOutcomeCategoryNum(String stageTwoOutcomeCategoryNum) {
        this.stageTwoOutcomeCategoryNum = stageTwoOutcomeCategoryNum;
    }

    public String getStageTwoCategoricalOutcomeUniqueList() {
        return stageTwoCategoricalOutcomeUniqueList;
    }

    public void setStageTwoCategoricalOutcomeUniqueList(String stageTwoCategoricalOutcomeUniqueList) {
        this.stageTwoCategoricalOutcomeUniqueList = stageTwoCategoricalOutcomeUniqueList;
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

    public String getStageTwoNewDataVariableCount() {
        return stageTwoNewDataVariableCount;
    }

    public void setStageTwoNewDataVariableCount(String stageTwoNewDataVariableCount) throws Exception {
        this.stageTwoNewDataVariableCount = stageTwoNewDataVariableCount;

    }

    public String getStageTwoNewDataIDField() {
        return stageTwoNewDataIDField;
    }

    public void setStageTwoNewDataIDField(String stageTwoNewDataVariableCount) throws Exception {
        this.stageTwoNewDataIDField = stageTwoNewDataVariableCount;

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
        // temporary setting
//        advancedLogisticProbitRegression = "1";
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
        if (advancedUseMLS == null) {
            advancedUseMLS = Integer.toString(stageOneRandomLocationEffects);
        }
        return advancedUseMLS;
    }

    public void setAdvancedUseMLS(String advancedUseMLS) {
        this.advancedUseMLS = advancedUseMLS;
    }

    public String getAdvancedCovarianceMatrix() {
        if (advancedCovarianceMatrix == null) {
            advancedCovarianceMatrix = "0";
        }
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
        if (advancedUseStageTwo == null) {
            this.advancedUseStageTwo = Integer.toString(stageTwoOutcomeType);
        }
        return advancedUseStageTwo;
    }

    public void setAdvancedUseStageTwo(String advancedUseStageTwo) {
        this.advancedUseStageTwo = advancedUseStageTwo;
    }

    public String getAdvancedStageTwoMultilevel() {
        advancedStageTwoMultilevel = "0";
        if (this.stageTwoModelType == STAGE_TWO_MODEL_TYPE_SINGLE) {
            advancedStageTwoMultilevel = "0";
        } else if (this.stageTwoModelType == STAGE_TWO_MODEL_TYPE_MULTILEVEL) {
            advancedStageTwoMultilevel = "1";
        }
        return advancedStageTwoMultilevel;
    }

    public void setAdvancedStageTwoMultilevel(String advancedStageTwoMultilevel) {
        this.advancedStageTwoMultilevel = advancedStageTwoMultilevel;
    }

    public String getAdvancedMultipleDataFiles() {
        if (advancedMultipleDataFiles == null) {
            advancedMultipleDataFiles = "0";
        }
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
        // build session folder
//        String utcDirPath = ModelBuilder.buildFolder(csvFileLocation);
        String utcDirPath = mixregGUI.sessionFolderNameBuilt;
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
    
        public void csvToDatConverterSecondDataset(File csvFileToConvert) throws IOException {
        String fileName = csvFileToConvert.getAbsolutePath();
        String fileNameShort = FilenameUtils.removeExtension(fileName);
        String baseName = FilenameUtils.getBaseName(fileName);
        String filePath = FilenameUtils.getFullPath(fileName);
        // TODO: Deprecate
        //String filePath = fileName.substring(0, fileName.lastIndexOf(File.separator)) + "/"; //subset the string.

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            List<String[]> csvRows = reader.readAll();
            reader.close();

            csvRows.remove(0); // TODO: make sure this isn't removing data

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

    /**
     * Ancillary classes
     */
    class ProgressStatus extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {

            //wait for the execution here
            runMixRegModels();
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void done() {
            progressWindow.dispose();
            System.out.println("THE PROCESS IS COMPLETE");

        }

    }

    public void writeStageOneOnlyDefFileToFolder(Dimension windowPanelDim) {

        try {
            myFrame = new JFrame("Definition File Preview");

            FlowLayout defFileFlow = new FlowLayout();

            myFrame.setLayout(defFileFlow);
            defFileFlow.setAlignment(FlowLayout.TRAILING);
            myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            myFrame.setSize(windowPanelDim.width / 2, windowPanelDim.height - 50);
            myFrame.setResizable(false);
            myPane = new JEditorPane();
            myPane.setSize(windowPanelDim.width / 2 - 20, windowPanelDim.height - 50);
            myPane.setContentType("text/plain");
            myPane.setFont(new Font("Monospaced", 0, 12));
            myPane.setLayout(new BorderLayout(windowPanelDim.width / 2 - 20, windowPanelDim.height - 50));
            myPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            String newline = System.getProperty("line.separator");
            try {
                myPane.setText(String.join(newline, buildDefinitionList()).replace("[", "").replace("]", ""));
            } catch (Exception e) {
                SystemLogger.LOGGER.log(Level.SEVERE, e.toString());
                //myPane.setText(String.join(newline, buildDefinitionList()()).replace("[", "").replace("]", ""));
            }

            JButton proceedButton = new JButton("Proceed");
            JButton saveDefFile = new JButton("Save Def File");
            proceedButton.setPreferredSize(new Dimension(120, 30));
            saveDefFile.setPreferredSize(new Dimension(120, 30));

            JPanel container = new JPanel();
            BoxLayout boxlayout = new BoxLayout(container, BoxLayout.Y_AXIS);
            container.setLayout(boxlayout);
            container.add(myPane);

            JPanel buttonContainer = new JPanel();
            buttonContainer.setLayout(new FlowLayout());

            buttonContainer.add(proceedButton);
            buttonContainer.add(saveDefFile);
            container.add(buttonContainer);

            myFrame.setComponentOrientation(ComponentOrientation.UNKNOWN);
            myFrame.setResizable(true);
            JScrollPane scrPane = new JScrollPane(container);
            myFrame.getContentPane().add(scrPane, BorderLayout.CENTER);
            myFrame.pack();

            proceedButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    SystemLogger.LOGGER.log(Level.FINE, "Stage 1 Proceed performed");
                    try {
                        // modelSelector();
                        // System.out.println("SELECTED MODEL: " + );

                        //runMixRegModels(); // run the updated functions of executing Don's code
                        progressStatus = new ProgressStatus();
                        progressStatus.execute();
                    } catch (Exception ex) {
                        Logger.getLogger(MixLibrary.class.getName()).log(Level.SEVERE, null, ex);
                        SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    }

                    myFrame.dispose();
                }

            });

            saveDefFile.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        saveDefFileLocally();
                    } catch (IOException ex) {
                        Logger.getLogger(MixLibrary.class.getName()).log(Level.SEVERE, null, ex);
                        SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    }
                }

            });

            myFrame.setVisible(true);
            myFrame.setAlwaysOnTop(true);

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            myFrame.setLocation(dim.width / 2 - myFrame.getSize().width / 2, dim.height / 2 - myFrame.getSize().height / 2);

            Document defDoc = myPane.getDocument();
            int length = defDoc.getLength();

            String dataFileSample = new File(this.getSharedDataFilename()).getAbsolutePath();
            String newDefFilePrefix = dataFileSample.substring(0, dataFileSample.lastIndexOf(File.separator)) + "/";
            if (getStageOneOutcome() == STAGE_ONE_OUTCOME_MIXOR) {
                newDefFile = new File(newDefFilePrefix + "mixors_random_mixblank");
            } else {
                newDefFile = new File(newDefFilePrefix + "lsboth_random_mixblank");
            }
            //newDefFile = new File(newDefFilePrefix + "MixWild");
//            
//            if (selectedModel == DefinitionHelper.MIXREGLS_MIXREG_KEY) {
//                newDefFile = new File(newDefFilePrefix + "MIXREGLS_RANDOM_MIXREG");
//            } else if (selectedModel == DefinitionHelper.MIXREGLS_MIXOR_KEY) {
//
//                newDefFile = new File(newDefFilePrefix + "MIXREGLS_RANDOM_MIXOR");
//            } else if (selectedModel == DefinitionHelper.MIXREGMLS_MIXREG_KEY) {
//
//                newDefFile = new File(newDefFilePrefix + "MIXREGMLS_RANDOM_MIXREG");
//            } else if (selectedModel == DefinitionHelper.MIXREGMLS_MIXOR_KEY) {
//
//                newDefFile = new File(newDefFilePrefix + "MIXREGMLS_RANDOM_MIXOR");
//            }

            FileWriter out = new FileWriter(newDefFile + ".def");
            out.write(myPane.getText());
            out.close();
        } catch (Exception exception) {
            SystemLogger.LOGGER.log(Level.SEVERE, exception.toString() + "{0}", SystemLogger.getLineNum());
            exception.printStackTrace();
        }
    }

    public void saveDefFileLocally() throws IOException {
        FileFilter filter = new FileNameExtensionFilter("TEXT FILE", "txt");

        JFileChooser saver = new JFileChooser("./");
        saver.setFileFilter(filter);
        int returnVal = saver.showSaveDialog(myFrame);
        File file = saver.getSelectedFile();
        BufferedWriter writer = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                // writer = new BufferedWriter( new FileWriter( file.getName()+".txt"));
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(myPane.getText());
                writer.close();
                JOptionPane.showMessageDialog(myFrame, "The .def file was Saved Successfully!",
                        "Success!", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
                JOptionPane.showMessageDialog(myFrame, "The .def file could not be Saved!",
                        "Error!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void writeDefFileToFolder(Dimension windowPanelDim) {

        try {

            myFrame = new JFrame("Definition File Preview");

            FlowLayout defFileFlow = new FlowLayout();
//            myFrame.setLayout(defFileFlow);
//            defFileFlow.setAlignment(FlowLayout.TRAILING);
            myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            myFrame.setSize(windowPanelDim.width / 2, windowPanelDim.height - 50);
            myFrame.setResizable(false);

            myPane = new JEditorPane();
            myPane.setSize(windowPanelDim.width / 2 - 30, windowPanelDim.height - 50);
            myPane.setContentType("text/plain");
            myPane.setFont(new Font("Monospaced", 0, 12));
            myPane.setLayout(new BorderLayout(windowPanelDim.width / 2 - 30, windowPanelDim.height - 50));
            myPane.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
            String newline = System.getProperty("line.separator");
            try {
                myPane.setText(String.join(newline, buildDefinitionList()).replace("[", "").replace("]", ""));
            } catch (Exception e) {
                SystemLogger.LOGGER.log(Level.SEVERE, e.toString() + "{0}", SystemLogger.getLineNum());
                //myPane.setText(String.join(newline, buildDefinitionList()).replace("[", "").replace("]", ""));
            }

            JButton proceedButton = new JButton("Proceed");
            JButton saveDefFile = new JButton("Save Def File");
            proceedButton.setPreferredSize(new Dimension(120, 30));
            saveDefFile.setPreferredSize(new Dimension(120, 30));

            JPanel container = new JPanel();
            BoxLayout boxlayout = new BoxLayout(container, BoxLayout.Y_AXIS);
            container.setLayout(boxlayout);
            container.add(myPane);

            JPanel buttonContainer = new JPanel();
            buttonContainer.setLayout(new FlowLayout());

            buttonContainer.add(proceedButton);
            buttonContainer.add(saveDefFile);
            container.add(buttonContainer);

            myFrame.setComponentOrientation(ComponentOrientation.UNKNOWN);
            myFrame.setResizable(true);
            JScrollPane scrPane = new JScrollPane(container);
            myFrame.getContentPane().add(scrPane, BorderLayout.CENTER);
            myFrame.pack();

            proceedButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    SystemLogger.LOGGER.log(Level.INFO, "Stage 2 Proceed performed");

                    try {
                        // modelSelector();
                        // System.out.println("SELECTED MODEL: " + );

                        //runMixRegModels(); // run the updated functions of executing Don's code
                        progressStatus = new ProgressStatus();
                        progressStatus.execute();
                    } catch (Exception ex) {
                        Logger.getLogger(MixLibrary.class.getName()).log(Level.SEVERE, null, ex);
                        SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    }

                    myFrame.dispose();
                }

            });

            saveDefFile.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    SystemLogger.LOGGER.log(Level.INFO, "Save Definition File");

                    try {
                        saveDefFileLocally();
                    } catch (IOException ex) {
                        Logger.getLogger(MixLibrary.class.getName()).log(Level.SEVERE, null, ex);
                        SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                    }
                }

            });

            myFrame.setVisible(true);
            myFrame.setAlwaysOnTop(true);

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            myFrame.setLocation(dim.width / 2 - myFrame.getSize().width / 2, dim.height / 2 - myFrame.getSize().height / 2);

            Document defDoc = myPane.getDocument();
            int length = defDoc.getLength();

            //File newDefFile = new File("MIXREGLS_MIXREG_KEY");
            String dataFileSample = new File(this.getSharedDataFilename()).getAbsolutePath();
            String newDefFilePrefix = dataFileSample.substring(0, dataFileSample.lastIndexOf(File.separator)) + "/";
            // this one is run for stage 2
            if (getStageOneOutcome() == STAGE_ONE_OUTCOME_MIXOR) {
                newDefFile = new File(newDefFilePrefix + "mixors_random_mixblank");
            } else {
                newDefFile = new File(newDefFilePrefix + "lsboth_random_mixblank");
            }
//            if (selectedModel == DefinitionHelper.MIXREGLS_MIXREG_KEY) {
//                newDefFile = new File(newDefFilePrefix + "MIXREGLS_RANDOM_MIXREG");
//            } else if (selectedModel == DefinitionHelper.MIXREGLS_MIXOR_KEY) {
//
//                newDefFile = new File(newDefFilePrefix + "MIXREGLS_RANDOM_MIXOR");
//            } else if (selectedModel == DefinitionHelper.MIXREGMLS_MIXREG_KEY) {
//
//                newDefFile = new File(newDefFilePrefix + "MIXREGMLS_RANDOM_MIXREG");
//            } else if (selectedModel == DefinitionHelper.MIXREGMLS_MIXOR_KEY) {
//
//                newDefFile = new File(newDefFilePrefix + "MIXREGMLS_RANDOM_MIXOR");
//            }

            FileWriter out = new FileWriter(newDefFile + ".def");
            out.write(myPane.getText());
            out.close();
        } catch (Exception exception) {
            SystemLogger.LOGGER.log(Level.SEVERE, exception.toString());
            exception.printStackTrace();
        }
    }

    public static void modelingProgressLogging(String line) {

        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(SystemLogger.logPath + "modelingProgress.txt", true));
            writer.write(line);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(MixLibrary.class.getName()).log(Level.SEVERE, null, ex);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
        }

    }

    public void runMixRegModels() {

        //@Eldin: This is the part where it may be throwing exceptions. Why do you have "/" at the end?
        String filePath = newDefFile.getAbsolutePath();
        System.out.println("THE DEF FILE IS: " + filePath);
        modelingProgressLogging("THE DEF FILE IS: " + filePath);
        definitionFilepath = filePath.substring(0, filePath.lastIndexOf(File.separator)) + "/";
        System.out.println("THE DEF FILE PATH IS: " + definitionFilepath);
        modelingProgressLogging("HE DEF FILE PATH IS: " + definitionFilepath);
        ////// selectedModel = getSelectedModel();
        String absoluteJavaPath = System.getProperty("user.dir");

        ////// String defFileName = executableModel(selectedModel);
        boolean isWindows = getOSName().contains("windows");
        String defFileName;

        String system_bit_extension = "";
        if (!win32) {
            system_bit_extension = "64";
        }
        if (stageOneOutcome == STAGE_ONE_OUTCOME_MIXOR) {
            defFileName = "mixors_random_mixblank" + system_bit_extension;
        } else {
            defFileName = "lsboth_random_mixblank" + system_bit_extension;
        }
        if (isWindows) {
            defFileName = defFileName + ".exe";
        }

        progressWindow = new JFrame("Please wait ...");
        modelingProgressLogging("Please wait ...");

        FlowLayout defFileFlow = new FlowLayout();
        progressWindow.setLayout(defFileFlow);
        defFileFlow.setAlignment(FlowLayout.TRAILING);
        progressWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        progressWindow.setSize(630, 680);
        progressWindow.setResizable(false);

        progressPane = new JTextArea(30, 80);
        DefaultCaret caret = (DefaultCaret) progressPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);

        progressPane.setLayout(new BorderLayout(500, 500));
        progressPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        progressPane.setFont(new Font("Monospaced", 0, 12));
        progressPane.setText("Please wait while we crunch some numbers .." + "\n");
        modelingProgressLogging("Please wait while we crunch some numbers .." + "\n");
        JScrollPane scroller = new JScrollPane(progressPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        progressWindow.add(scroller);
        scroller.setBounds(0, 0, 500, 500);

        JButton cancelButton = new JButton("Cancel Analysis");
        progressWindow.add(cancelButton);
        progressWindow.setComponentOrientation(ComponentOrientation.UNKNOWN);
        progressWindow.setVisible(true);
        //progressWindow.setAlwaysOnTop(true);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        progressWindow.setLocation(dim.width / 2 - progressWindow.getSize().width / 2, dim.height / 2 - progressWindow.getSize().height / 2);

        try {

            //@Eldin: this where we can switch between mixreg binaries
//                if (getOSName().contains("windows")) {
//                
//                
//                } else if (getOSName().contains("mac")) {
//                
//                
//                }
            copyExecutable(definitionFilepath);
            Process p;
            String macOSCommand = "\"" + definitionFilepath + defFileName + "\"";
            // debug
            SystemLogger.LOGGER.log(Level.CONFIG, getOSName(), SystemLogger.getLineNum());

            if (getOSName().contains("windows")) {
                System.out.print("$$$$$$$$$$$$$: " + definitionFilepath);
                // the file path is not in the C drive
                if (!"C".equals(definitionFilepath.split(":")[0])) {
                    String command = "cmd /c dir && cd /d" + "\"" + definitionFilepath + "\"" + " && dir && "
                            + defFileName;
                    p = Runtime.getRuntime().exec(command);
                    // debug
                    SystemLogger.LOGGER.log(Level.CONFIG, "cmd /c dir && cd /d" + "\"" + definitionFilepath + "\"" + " && dir && "
                            + defFileName, SystemLogger.getLineNum());
                } else {
                    String command = "cmd /c dir && cd " + "\"" + definitionFilepath + "\"" + " && dir && "
                            + defFileName;
                    p = Runtime.getRuntime().exec(command);
                    // debug
                    SystemLogger.LOGGER.log(Level.CONFIG, "cmd /c dir && cd " + "\"" + definitionFilepath + "\"" + " && dir && "
                            + defFileName, SystemLogger.getLineNum());
                }
                //

            } else {
                ProcessBuilder pb = new ProcessBuilder(
                        "bash",
                        "-c",
                        macOSCommand);
                pb.directory(new File(definitionFilepath));
                pb.redirectErrorStream(true);
                p = pb.start();
            }

            Thread runCMD = new Thread(new Runnable() {
                public void run() {
                    System.out.println("Inside the thread for: " + macOSCommand);
                    try {
                        InputStreamReader isr = new InputStreamReader(p.getInputStream());
                        // InputStreamReader esr = new InputStreamReader(p.getErrorStream());
                        BufferedReader br = new BufferedReader(isr);
                        // BufferedReader ebr = new BufferedReader(esr);
                        String line = null;  // UI magic should run in here @adityapona
                        while ((line = br.readLine()) != null) {
                            System.out.println("MIXWILD:" + line);
                            updateProgressPane("MIXWILD:" + line + "\n");
                            //progressPane.append();
                            modelingProgressLogging("MIXWILD:" + line + "\n");
                        }
                    } catch (IOException ioe) {
                        SystemLogger.LOGGER.log(Level.SEVERE, ioe.toString() + "{0}", SystemLogger.getLineNum());
                        ioe.printStackTrace();
                    }
                }
            });
            runCMD.start();

            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    p.destroy();
                    progressWindow.dispose();
                }
            });
            int exitVal = p.waitFor();
            System.out.println("ExitValue: " + exitVal); // Non-zero is an error
            updateProgressPane("MIXWILD::Exit Value: " + String.valueOf(exitVal) + "\n"); //should append all the text after a new line to the text area
            modelingProgressLogging("ExitValue: " + exitVal);
            modelingProgressLogging("MIXWILD::Exit Value: " + String.valueOf(exitVal) + "\n");

            if (exitVal == 0) {
                //send the out to StageTwoOutPu from here
                // FileReader reader = new FileReader(absoluteJavaPath + ".out file name");
                terminalVal = exitVal;
                Process p2;
                if (getOSName().contains("windows")) {
                    String[] executable_array = {"lsboth_random_mixblank", "mixors_random_mixblank", "mixno", "mixreg", "mixors", "mixpreg", "stage2only", "lsboth_random_mixblank64", "mixors_random_mixblank64", "stage2only64"};
                    for (int i = 0; i < executable_array.length; i++) {
                        String executableFile = executable_array[i];
                        String command = "cmd /c dir && cd " + "\"" + definitionFilepath + "\"" + " && del /f " + "\"" + executableFile + ".exe" + "\"";
                        p2 = Runtime.getRuntime().exec(command); //delete the file when everything works great.
                    }
//                    String command = "cmd /c dir && cd " + "\"" + definitionFilepath + "\"" + " && del /f " + "\"" + defFileName + "\"";
//                    p2 = Runtime.getRuntime().exec(command); //delete the file when everything works great.
                } else {
                    ProcessBuilder pb = new ProcessBuilder(
                            "bash",
                            "-c",
                            "rm " + "\"" + definitionFilepath + defFileName + "\"");
                    pb.redirectErrorStream(true);
                    p2 = pb.start();
                }

                readStageOneOutputfile();
                readStageTwoOutputfile();

                progressWindow.dispose(); //should close the window when done after this line

                // FileReader reader = new FileReader(absoluteJavaPath + ".out file name");
            } else {
                JOptionPane.showMessageDialog(progressWindow, "Failed to build model. Please revisit your regressors and try again. For more information, checkout help docs.", "Execution failed!", JOptionPane.INFORMATION_MESSAGE);
                modelingProgressLogging("Failed to build model. Please revisit your regressors and try again. For more information, checkout help docs.");
                Process p2;
                if (getOSName().contains("windows")) {
                    p2 = Runtime.getRuntime().exec("cmd /c dir && cd " + "\"" + definitionFilepath + "\"" + " && del /f " + "\"" + defFileName + "\"");

                } else {
                    ProcessBuilder pb = new ProcessBuilder(
                            "bash",
                            "-c",
                            "rm " + "\"" + definitionFilepath + defFileName + "\"");
                    pb.redirectErrorStream(true);
                    p2 = pb.start();

                }

                terminalVal = exitVal;
                //progressWindow.dispose();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(progressWindow, "Failed");
            JOptionPane.showMessageDialog(progressWindow, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE);
            SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
        }
    }

    public int getExitVal() {

        return terminalVal;

    }

    public void readStageOneOutputfile() throws FileNotFoundException, IOException {
        mixregGUI.stageOneOutput.setText("");
        String fileName = mixregGUI.defFile.getSharedDataFilename();
        String outputFilePath = FilenameUtils.removeExtension(fileName) + "_Output_stage1.out";
        File file = new File(outputFilePath);
        BufferedReader br = null;
        String line = "";

        br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null) {
            //System.out.println(line);
            mixregGUI.stageOneOutput.append(line + "\n");

        }

        br.close();

    }

    public void readStageTwoOutputfile() throws FileNotFoundException, IOException {

        mixregGUI.stageTwoOutput.setText("");
        if (mixregGUI.defFile.getAdvancedUseStageTwo().equals("0")) {
            //do nothing
        } else {
            String fileName;
//            if (!stageTwoNewDataIncluded) {
//                fileName = mixregGUI.defFile.getSharedDataFilename();
//            } else {
//                fileName = mixregGUI.defFile.getSharedDataFilename_stageTwo();
//            }
            fileName = mixregGUI.defFile.getSharedDataFilename();
            String outputFilePath = FilenameUtils.removeExtension(fileName) + "_Output_stage2.out";
            File file = new File(outputFilePath);
            String line = "";

            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                mixregGUI.stageTwoOutput.append(line + "\n");
            }

            br.close();

        }

    }

    public String getOSName() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName;
    }

    private void copyExecutable(String absoluteDirectoryPath) throws FileNotFoundException, IOException {
        String modelPath;

        String LSBOTH_PRE = "lsboth_random_mixblank";
        String MIXORS_PRE = "mixors_random_mixblank";
        String MIXNO = "mixno";
        String MIXREG = "mixreg";
        String MIXORS = "mixors";
        String MIXPREG = "mixpreg";
        String STAGETWO_ONLY = "stage2only";
        if (win32) {
            STAGETWO_ONLY = "stage2only";
        }

        if (getOSName().contains("windows")) {
            System.out.print(win32);
            if (win32) {
                LSBOTH_PRE = "resources/Windows32/" + LSBOTH_PRE + ".exe";
                MIXORS_PRE = "resources/Windows32/" + MIXORS_PRE + ".exe";
                MIXNO = "resources/Windows32/" + MIXNO + ".exe";
                MIXREG = "resources/Windows32/" + MIXREG + ".exe";
                MIXORS = "resources/Windows32/" + MIXORS + ".exe";
                MIXPREG = "resources/Windows32/" + MIXPREG + ".exe";
                STAGETWO_ONLY = "resources/Windows32/" + STAGETWO_ONLY + ".exe";
            } else {
                System.out.print("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvva");
                LSBOTH_PRE = "resources/Windows64/" + LSBOTH_PRE + "64" + ".exe";
                MIXORS_PRE = "resources/Windows64/" + MIXORS_PRE + "64" + ".exe";
                MIXNO = "resources/Windows64/" + MIXNO + ".exe";
                MIXREG = "resources/Windows64/" + MIXREG + ".exe";
                MIXORS = "resources/Windows64/" + MIXORS + ".exe";
                MIXPREG = "resources/Windows64/" + MIXPREG + ".exe";
                STAGETWO_ONLY = "resources/Windows64/" + STAGETWO_ONLY + "64" + ".exe";
            }
        } else {
            LSBOTH_PRE = "resources/macOS/" + LSBOTH_PRE;
            MIXORS_PRE = "resources/macOS/" + MIXORS_PRE;
            MIXNO = "resources/macOS/" + MIXNO;
            MIXREG = "resources/macOS/" + MIXREG;
            MIXORS = "resources/macOS/" + MIXORS;
            MIXPREG = "resources/macOS/" + MIXPREG;
            STAGETWO_ONLY = "resources/macOS/" + STAGETWO_ONLY;
        }

        String exeArray[] = {LSBOTH_PRE, MIXORS_PRE, MIXNO, MIXREG, MIXORS, MIXPREG, STAGETWO_ONLY};

        for (String exe : exeArray) {
            System.out.println("Working on Exes");
            InputStream stream = getClass().getClassLoader().getResourceAsStream(exe);
            OutputStream outputStream
                    = new FileOutputStream(new File(absoluteDirectoryPath + FilenameUtils.getName(exe)));

            int read;
            byte[] bytes = new byte[4096];

            while ((read = stream.read(bytes)) > 0) {
                //System.out.println("Working on output stream");
                outputStream.write(bytes, 0, read);
            }
            stream.close();
            outputStream.close();
        }

        if (!getOSName().contains("windows")) {
            String[] commands = {"chmod u+x " + "\"" + definitionFilepath + FilenameUtils.getName(LSBOTH_PRE) + "\"",
                "chmod u+x " + "\"" + definitionFilepath + FilenameUtils.getName(MIXORS_PRE) + "\"",
                "chmod u+x " + "\"" + definitionFilepath + FilenameUtils.getName(MIXNO) + "\"",
                "chmod u+x " + "\"" + definitionFilepath + FilenameUtils.getName(MIXPREG) + "\"",
                "chmod u+x " + "\"" + definitionFilepath + FilenameUtils.getName(MIXREG) + "\"",
                "chmod u+x " + "\"" + definitionFilepath + FilenameUtils.getName(MIXORS) + "\"",
                "chmod u+x " + "\"" + definitionFilepath + FilenameUtils.getName(STAGETWO_ONLY) + "\""};
            for (String command : commands) {
                ProcessBuilder pb1 = new ProcessBuilder(
                        "bash",
                        "-c",
                        command);
                System.out.print(Arrays.toString(pb1.command().toArray()));
                pb1.redirectErrorStream(true);
                Process p0 = pb1.start();
                try {
                    System.out.println("WAITING FOR SYSTEM RESPONSE");
                    p0.waitFor();
                } catch (InterruptedException ex) {
                    System.out.println("WAIT FAILED!");
                    Logger.getLogger(MixLibrary.class.getName()).log(Level.SEVERE, null, ex);
                    SystemLogger.LOGGER.log(Level.SEVERE, ex.toString() + "{0}", SystemLogger.getLineNum());
                }
            }

        }
        System.out.println("Copying done!");
    }

    public void updateProgressPane(String input) {
        SwingUtilities.invokeLater(
                new Runnable() {
            @Override
            public void run() {
                progressPane.append(input);
            }

        });
    }
}
