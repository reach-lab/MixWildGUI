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
import static mixregui.mixregGUI.MXRStates;

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

    public static String[] getStageOneModelLatex(String stageOneOutcomeLabel, String[] stageOneRegLabelList, String[] stageOneRegEquationNameList, int RLE, int RSE, int association, String[] meanModelVarLabels, String[] BSModelVarLabels, String[] WSModelVarLabels, String[] BWModelVarLabels, String[] ScaleRandomModelVarLabels) {
//        String latex2 = "PA_i_j = \\beta_0 + \\beta_1 Day\\_c_i_j + \\nu_i + \\epsilon_i_j";
//        String latex3 = "\\int_0^{+\\infty} e^{-x^2}\\mathrm{d}x = \\frac\\sqrt{\\pi}2  \\sum_{n=0}^{\\infty}\\frac{1}{n^2}=\\frac{\\pi^2}6";
        String[] stageOneModelLatexArray = new String[3];

        // mean model
        String MeanModelLatex = "Y_1_i_j = \\beta_0"; //" + \\beta_1 X_1_i_j + \\nu_i + \\epsilon_i_j";
        for (int i = 0; i < meanModelVarLabels.length; i++) {
            String regLabel = meanModelVarLabels[i];
            String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
            MeanModelLatex = MeanModelLatex + " + \\beta_" + Integer.toString(i + 1) + " " + equationVarName + "_i_j";
//            if (RLE == 1)
        }
        MeanModelLatex = MeanModelLatex + " + \\nu_i + \\epsilon_i_j";

        
        // BS model
        String BSModelLatex = "{\\sigma_{\\nu_i_j}^2} = \\exp(\\alpha_0";
        for (int i = 0; i < BSModelVarLabels.length; i++) {
            String regLabel = BSModelVarLabels[i];
            String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
            BSModelLatex = BSModelLatex + " + \\alpha_" + Integer.toString(i + 1) + " " + equationVarName + "_i_j";
        }
        BSModelLatex = BSModelLatex + ")";
        
        
        // WS model
        String WSModelLatex = "{\\sigma_{\\epsilon_i_j}^2} = \\exp(\\tau_0";
        for (int i = 0; i < WSModelVarLabels.length; i++) {
            String regLabel = WSModelVarLabels[i];
            String equationVarName = stageOneRegEquationNameList[Arrays.asList(stageOneRegLabelList).indexOf(regLabel)];
            WSModelLatex = WSModelLatex + " + \\tau_" + Integer.toString(i + 1) + " " + equationVarName + "_i_j";
        }
        if (RSE == 0){
            WSModelLatex = WSModelLatex + ")";
        } else if (RSE > 0){
            if (association == 0){
                WSModelLatex = WSModelLatex + " + \\omega_i)";
            } else if (association == 1){
                WSModelLatex = WSModelLatex + " + \\tau_\\nu \\nu_i + \\omega_i)";
            } else if (association == 2){
                WSModelLatex = WSModelLatex + "+ \\tau_\\nu \\nu_i^2 + \\omega_i)";
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
}
