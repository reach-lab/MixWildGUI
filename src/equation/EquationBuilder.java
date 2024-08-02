/*
Build latex from user configuration
1. refresh button, extract user configuration from MixRegGUIStates.java
2. public methods to generate stage 1 and 2 model formula latex

 */
package equation;

/**
 *
 * @author Jixin
 */
import java.util.Arrays;
import javax.swing.DefaultListModel;
import static mixregui.mixregGUI.MXRStates;
import mixregui.stageOneRegs;

public class EquationBuilder {

    public static String[] getStageOneModelVariables(String stageOneOrTwoOutcomeLabel, String[] stageOneOrTwoRegNameList, String[] stageOneOrTwoRegEquationNameList) {
//        String latex2 = "PA_i_j = \\beta_0 + \\beta_1 Day\\_c_i_j + \\nu_i + \\epsilon_i_j";
//        String latex3 = "\\int_0^{+\\infty} e^{-x^2}\\mathrm{d}x = \\frac\\sqrt{\\pi}2  \\sum_{n=0}^{\\infty}\\frac{1}{n^2}=\\frac{\\pi^2}6";
        String[] stageOneModelVarArray = new String[stageOneOrTwoRegNameList.length + 1];
        String stageOneOutcomeLatex = "Y_1: " + stageOneOrTwoOutcomeLabel;
        stageOneModelVarArray[0] = stageOneOutcomeLatex;
        for (int i = 0; i < stageOneOrTwoRegEquationNameList.length; i++) {
            stageOneModelVarArray[i + 1] = stageOneOrTwoRegEquationNameList[i] + ": " + stageOneOrTwoRegNameList[i];
        }

        return stageOneModelVarArray;
    }

    public static String[] getStageTwoModelVariables(String stageOneOrTwoOutcomeLabel, String[] stageOneOrTwoRegNameList, String[] stageOneOrTwoRegEquationNameList) {
//        String latex2 = "PA_i_j = \\beta_0 + \\beta_1 Day\\_c_i_j + \\nu_i + \\epsilon_i_j";
//        String latex3 = "\\int_0^{+\\infty} e^{-x^2}\\mathrm{d}x = \\frac\\sqrt{\\pi}2  \\sum_{n=0}^{\\infty}\\frac{1}{n^2}=\\frac{\\pi^2}6";
        String[] stageOneModelVarArray = new String[stageOneOrTwoRegNameList.length + 1];
        String stageOneOutcomeLatex = "Y_2: " + stageOneOrTwoOutcomeLabel;
        stageOneModelVarArray[0] = stageOneOutcomeLatex;
        for (int i = 0; i < stageOneOrTwoRegEquationNameList.length; i++) {
            stageOneModelVarArray[i + 1] = stageOneOrTwoRegEquationNameList[i] + ": " + stageOneOrTwoRegNameList[i];
        }

        return stageOneModelVarArray;
    }

    public static String[] getStageOneModelLatex(boolean stageOneLevelThree, String stageOneOutcomeLabel, String[] stageOneRegLabelList, String[] stageOneRegTableNameList, String[] stageOneRegEquationNameList, int RLE, int RSE, int association, String[] meanModelVarLabels, String[] meanModelDisaggVarLabels, String[] BSModelVarLabels, String[] BSModelDisaggVarLabels, String[] WSModelVarLabels, String[] WSModelDisaggVarLabels, String[] BWModelVarLabels, String[] BWModelDisaggVarLabels, String[] ScaleRandomModelVarLabels, String[] ScaleRandomDisaggModelVarLabels, DefaultListModel<String> stageOneLvlOneList, DefaultListModel<String> stageOneLvlTwoList, DefaultListModel<String> stageOneLvlThreeList) {
//        String latex2 = "PA_i_j = \\beta_0 + \\beta_1 Day\\_c_i_j + \\nu_i + \\epsilon_i_j";
//        String latex3 = "\\int_0^{+\\infty} e^{-x^2}\\mathrm{d}x = \\frac\\sqrt{\\pi}2  \\sum_{n=0}^{\\infty}\\frac{1}{n^2}=\\frac{\\pi^2}6";
        String[] stageOneModelLatexArray = new String[3];

        // mean model
        String MeanModelLatex = "Y_1_i_j = \\beta_0"; //" + \\beta_1 X_1_i_j + \\nu_i + \\epsilon_i_j";
        for (int i = 0; i < meanModelVarLabels.length + meanModelDisaggVarLabels.length; i++) {
            String regLabel;
            if (i < meanModelVarLabels.length) {
                regLabel = meanModelVarLabels[i];
                String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                MeanModelLatex = MeanModelLatex + " + \\beta_" + Integer.toString(i + 1) + " " + equationVarName;
            } else { // disaggregate
                regLabel = meanModelDisaggVarLabels[i];
                String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                String tableVarName = stageOneRegTableNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                MeanModelLatex = MeanModelLatex + " + \\beta_" + Integer.toString(i + 1) + " (" + equationVarName + " - " + tableVarName + "_i)";
            }

        }

        if (stageOneLevelThree == false) {
            MeanModelLatex = MeanModelLatex + " + $\\nu_{0_i}$";
        } else {
            MeanModelLatex = MeanModelLatex + " + $\\nu_{0_i_j}$ + $\\nu_{0_i}$";
        }

        if (RLE == 1) {
            // add random slope for variable from BSModelVarLabels
            for (int i = 0; i < BSModelVarLabels.length + BSModelDisaggVarLabels.length; i++) {
                String regLabel;
                if (i < BSModelVarLabels.length) {
                    regLabel = BSModelVarLabels[i];
                    String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                    MeanModelLatex = MeanModelLatex + " + $\\nu_{" + Integer.toString(i + 1) + "_i}$" + " " + equationVarName;
                } else {
                    regLabel = BSModelDisaggVarLabels[i];
                    String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                    String tableVarName = stageOneRegTableNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                    MeanModelLatex = MeanModelLatex + " + $\\nu_{" + Integer.toString(i + 1) + "_i}$" + " (" + equationVarName + " - " + tableVarName + "_i)";
                }

            }
        }

        if (stageOneLevelThree == false) {
            MeanModelLatex = MeanModelLatex + " + \\epsilon_i_j";
        } else {
            MeanModelLatex = MeanModelLatex + " + \\epsilon_i_j_k";
        }

        // BS model
        String BSModelLatex = null;
        if (RLE == 0) { // If randome location effect has random slope in mean model, no BSV model.
            BSModelLatex = "{\\sigma_{\\nu_i_j}^2} = \\exp(\\alpha_0";
            for (int i = 0; i < BSModelVarLabels.length + BSModelDisaggVarLabels.length; i++) {
                String regLabel;
                if (i < BSModelVarLabels.length) {
                    regLabel = BSModelVarLabels[i];
                    String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                    BSModelLatex = BSModelLatex + " + \\alpha_" + Integer.toString(i + 1) + " " + equationVarName;
                } else {
                    regLabel = BSModelDisaggVarLabels[i];
                    String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                    String tableVarName = stageOneRegTableNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                    BSModelLatex = BSModelLatex + " + \\alpha_" + Integer.toString(i + 1) + " (" + equationVarName + " - " + tableVarName + "_i)";
                }

            }
            BSModelLatex = BSModelLatex + ")";
        } else {
            // pass
        }

        // WS model
        String WSModelLatex = "{\\sigma_{\\epsilon_i_j}^2} = \\exp(\\tau_0";
        for (int i = 0; i < WSModelVarLabels.length + WSModelDisaggVarLabels.length; i++) {

            String regLabel;
            if (i < WSModelVarLabels.length) {
                regLabel = WSModelVarLabels[i];
                String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                WSModelLatex = WSModelLatex + " + \\tau_" + Integer.toString(i + 1) + " " + equationVarName;
            } else {
                regLabel = WSModelDisaggVarLabels[i];
                String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                String tableVarName = stageOneRegTableNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                WSModelLatex = WSModelLatex + " + \\tau_" + Integer.toString(i + 1) + " (" + equationVarName + " - " + tableVarName + "_i)";
            }

        }

        if (RSE == 0) {
            // pass
        } else if (RSE > 0) {
            if (association == 0) {
                WSModelLatex = WSModelLatex + " + $\\omega_{0_i}$";
            } else if (association == 1) {
                WSModelLatex = WSModelLatex + " + \\tau_\\nu \\nu_i + $\\omega_{0_i}$";
            } else if (association == 2) {
                WSModelLatex = WSModelLatex + " + \\tau_\\nu \\nu_i + \\tau_\\nu \\nu_i^2 + $\\omega_{0_i}$";
            } else {
                // pass
            }
        }

        if (RSE == 2) {
            // add random slope for variable from ScaleRandomModelVarLabels
            for (int i = 0; i < ScaleRandomModelVarLabels.length + ScaleRandomDisaggModelVarLabels.length; i++) {

                String regLabel;
                if (i < ScaleRandomModelVarLabels.length) {
                    regLabel = ScaleRandomModelVarLabels[i];
                    String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                    WSModelLatex = WSModelLatex + " + $\\omega_{" + Integer.toString(i + 1) + "_i}$" + " " + equationVarName;
                } else {
                    regLabel = ScaleRandomDisaggModelVarLabels[i];
                    String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                    String tableVarName = stageOneRegTableNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                    WSModelLatex = WSModelLatex + " + $\\omega_{" + Integer.toString(i + 1) + "_i}$" + " (" + equationVarName + " - " + tableVarName + "_i)";
                }

            }
        }

        WSModelLatex = WSModelLatex + ")";

        stageOneModelLatexArray[0] = MeanModelLatex;
        stageOneModelLatexArray[1] = BSModelLatex;
        stageOneModelLatexArray[2] = WSModelLatex;

        return stageOneModelLatexArray;
    }

    public static String[] getStageTwoModelLatex(int stageTwoOutcomeLevel, int stageTwoOutcomeType, String[] stageTwoRegLabelList, String[] stageTwoRegEquationNameList, int RLE, int RSE, String[] stageTwoRegressorLabels, String[] randomLocationInteractionLabels, String[] randomScaleInteractionLabels, String[] randomLocationScaleInteractionLabels, boolean twoWayRandomLocationScaleInteraction) {
//        String latex2 = "PA_i_j = \\beta_0 + \\beta_1 Day\\_c_i_j + \\nu_i + \\epsilon_i_j";
//        String latex3 = "\\int_0^{+\\infty} e^{-x^2}\\mathrm{d}x = \\frac\\sqrt{\\pi}2  \\sum_{n=0}^{\\infty}\\frac{1}{n^2}=\\frac{\\pi^2}6";
        //" + \\beta_1 X_1_i_j + \\nu_i + \\epsilon_i_j";
        String[] stageOneModelLatexArray = new String[1];

        // Stage two model: regressors (lvl1+lvl2), interaction, random effects, error term
        String modelLatex;
        String outcomeVarLatex = "Y_2";
        if (stageTwoOutcomeLevel == 0) {
            outcomeVarLatex = outcomeVarLatex + "_i";
        } else {
            outcomeVarLatex = outcomeVarLatex + "_i_j";
        }

        if (stageTwoOutcomeType == 1) { // continuous
            modelLatex = "Y_1_i_j = \\beta_0";
        } else if (stageTwoOutcomeType == 2 || stageTwoOutcomeType == 4) { //binary or ordinal, multinomial
            modelLatex = "logit(" + outcomeVarLatex + ") = \\beta_0";
        } else { // count
            modelLatex = "log(" + outcomeVarLatex + ") = \\beta_0";
        }

        // Regressors
        int index = 1;
        for (int i = 0; i < stageTwoRegressorLabels.length; i++) {
            String regLabel;
            regLabel = stageTwoRegressorLabels[i];
            String equationVarName = stageTwoRegEquationNameList[Arrays.asList(stageTwoRegLabelList).indexOf(regLabel)];
            modelLatex = modelLatex + " + \\beta_" + Integer.toString(index) + " " + equationVarName;
            index++;
        }

        // Interaction
        for (int i = 0; i < randomLocationInteractionLabels.length; i++) {
            String regLabel;
            regLabel = randomLocationInteractionLabels[i];
            String equationVarName = stageTwoRegEquationNameList[Arrays.asList(stageTwoRegLabelList).indexOf(regLabel)];
            modelLatex = modelLatex + " + \\beta_" + Integer.toString(index) + " " + equationVarName + " \\nu_i";
            index++;
        }

        for (int i = 0; i < randomScaleInteractionLabels.length; i++) {
            String regLabel;
            regLabel = randomScaleInteractionLabels[i];
            String equationVarName = stageTwoRegEquationNameList[Arrays.asList(stageTwoRegLabelList).indexOf(regLabel)];
            modelLatex = modelLatex + " + \\beta_" + Integer.toString(index) + " " + equationVarName + " \\omega_i";
            index++;
        }

        for (int i = 0; i < randomLocationScaleInteractionLabels.length; i++) {
            String regLabel;
            regLabel = randomLocationScaleInteractionLabels[i];

            String equationVarName = stageTwoRegEquationNameList[Arrays.asList(stageTwoRegLabelList).indexOf(regLabel)];
            modelLatex = modelLatex + " + \\beta_" + Integer.toString(index) + " " + equationVarName + " \\nu_i  \\omega_i";
            index++;
        }

        //Random effects
        modelLatex = modelLatex + " + \\nu_i";

        if (RSE > 0) {
            modelLatex = modelLatex + " + \\omega_i";
            if (twoWayRandomLocationScaleInteraction) {
                modelLatex = modelLatex + " + \\nu_i \\omega_i";
            }
        } else {
            // pass
        }

        // Error terms
        if (stageTwoOutcomeLevel == 0) {
            modelLatex = modelLatex + " + \\epsilon_i";
        } else {
            modelLatex = modelLatex + " + \\epsilon_i_j";
        }

        stageOneModelLatexArray[0] = modelLatex;

        return stageOneModelLatexArray;
    }

}
