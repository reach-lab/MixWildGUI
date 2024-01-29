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

    public static String[] getStageOneModelVariables(String stageOneOutcomeLabel, String[] stageOneRegNameList, String[] stageOneRegEquationNameList) {
//        String latex2 = "PA_i_j = \\beta_0 + \\beta_1 Day\\_c_i_j + \\nu_i + \\epsilon_i_j";
//        String latex3 = "\\int_0^{+\\infty} e^{-x^2}\\mathrm{d}x = \\frac\\sqrt{\\pi}2  \\sum_{n=0}^{\\infty}\\frac{1}{n^2}=\\frac{\\pi^2}6";
        String[] stageOneModelVarArray = new String[stageOneRegNameList.length + 1];
        String stageOneOutcomeLatex = "Y_1: " + stageOneOutcomeLabel;
        stageOneModelVarArray[0] = stageOneOutcomeLatex;
        for (int i = 0; i < stageOneRegEquationNameList.length; i++) {
            stageOneModelVarArray[i + 1] = stageOneRegEquationNameList[i] + ": " + stageOneRegNameList[i];
        }

        return stageOneModelVarArray;
    }

    public static String[] getStageOneModelLatex(String stageOneOutcomeLabel, String[] stageOneRegLabelList, String[] stageOneRegEquationNameList, int RLE, int RSE, int association, String[] meanModelVarLabels, String[] BSModelVarLabels, String[] WSModelVarLabels, String[] BWModelVarLabels, String[] ScaleRandomModelVarLabels, DefaultListModel<String> stageOneLvlOneList, DefaultListModel<String> stageOneLvlTwoList, DefaultListModel<String> stageOneLvlThreeList) {
//        String latex2 = "PA_i_j = \\beta_0 + \\beta_1 Day\\_c_i_j + \\nu_i + \\epsilon_i_j";
//        String latex3 = "\\int_0^{+\\infty} e^{-x^2}\\mathrm{d}x = \\frac\\sqrt{\\pi}2  \\sum_{n=0}^{\\infty}\\frac{1}{n^2}=\\frac{\\pi^2}6";
        String[] stageOneModelLatexArray = new String[3];

        // mean model
        String MeanModelLatex = "Y_1_i_j = \\beta_0"; //" + \\beta_1 X_1_i_j + \\nu_i + \\epsilon_i_j";
        for (int i = 0; i < meanModelVarLabels.length; i++) {
            String regLabel = meanModelVarLabels[i];
            String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
            String subscript = getSubscriptStageOneRegressor(regLabel, stageOneLvlOneList, stageOneLvlTwoList, stageOneLvlThreeList);
            MeanModelLatex = MeanModelLatex + " + \\beta_" + Integer.toString(i + 1) + " " + equationVarName + subscript;
        }

        if (RLE == 1) {
            // add random slope for variable from BSModelVarLabels
            for (int i = 0; i < BSModelVarLabels.length; i++) {
                String regLabel = BSModelVarLabels[i];
                String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                String subscript = getSubscriptStageOneRegressor(regLabel, stageOneLvlOneList, stageOneLvlTwoList, stageOneLvlThreeList);
                MeanModelLatex = MeanModelLatex + " + $\\nu_{" + Integer.toString(i + 1) + "_i}$" + " " + equationVarName + subscript;
            }
        }
        MeanModelLatex = MeanModelLatex + " + $\\nu_{0_i}$ + \\epsilon_i_j";

        // BS model
        String BSModelLatex = null;
        if (RLE == 0) { // If randome location effect has random slope in mean model, no BSV model.
            BSModelLatex = "{\\sigma_{\\nu_i_j}^2} = \\exp(\\alpha_0";
            for (int i = 0; i < BSModelVarLabels.length; i++) {
                String regLabel = BSModelVarLabels[i];
                String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                String subscript = getSubscriptStageOneRegressor(regLabel, stageOneLvlOneList, stageOneLvlTwoList, stageOneLvlThreeList);
                BSModelLatex = BSModelLatex + " + \\alpha_" + Integer.toString(i + 1) + " " + equationVarName + subscript;
            }
            BSModelLatex = BSModelLatex + ")";
        } else {
            // pass
        }

        // WS model
        String WSModelLatex = "{\\sigma_{\\epsilon_i_j}^2} = \\exp(\\tau_0";
        for (int i = 0; i < WSModelVarLabels.length; i++) {
            String regLabel = WSModelVarLabels[i];
            String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
            String subscript = getSubscriptStageOneRegressor(regLabel, stageOneLvlOneList, stageOneLvlTwoList, stageOneLvlThreeList);
            WSModelLatex = WSModelLatex + " + \\tau_" + Integer.toString(i + 1) + " " + equationVarName + subscript;
        }

        if (RSE == 2) {
            // add random slope for variable from ScaleRandomModelVarLabels
            for (int i = 0; i < ScaleRandomModelVarLabels.length; i++) {
                String regLabel = ScaleRandomModelVarLabels[i];
                String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
                String subscript = getSubscriptStageOneRegressor(regLabel, stageOneLvlOneList, stageOneLvlTwoList, stageOneLvlThreeList);
                WSModelLatex = WSModelLatex + " + $\\omega_{" + Integer.toString(i + 1) + "_i}$" + " " + equationVarName + subscript;
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
        WSModelLatex = WSModelLatex + ")";

        stageOneModelLatexArray[0] = MeanModelLatex;
        stageOneModelLatexArray[1] = BSModelLatex;
        stageOneModelLatexArray[2] = WSModelLatex;

        return stageOneModelLatexArray;
    }

    private static String getSubscriptStageOneRegressor(String regLabel, DefaultListModel<String> stageOneLvlOneList, DefaultListModel<String> stageOneLvlTwoList, DefaultListModel<String> stageOneLvlThreeList) {
        int regressorLevel = 1;
        String subscript = "";

        if (stageOneLvlOneList.contains(regLabel)) {
            regressorLevel = 1;
        } else if (stageOneLvlTwoList.contains(regLabel)) {
            regressorLevel = 2;
        } else if (stageOneLvlThreeList.contains(regLabel)) {
            regressorLevel = 3;
        } else {
            //pass
        }

        if (regressorLevel == 1) {
            subscript = "_i_j";
        } else if (regressorLevel == 2) {
            subscript = "_i";
        } else {
            //pass
        }

        return subscript;
    }
}
