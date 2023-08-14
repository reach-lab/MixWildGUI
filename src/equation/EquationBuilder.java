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
import static mixregui.mixregGUI.MXRStates;

public class EquationBuilder {

    public static String[] getStageOneModelVariables() {
//        String latex2 = "PA_i_j = \\beta_0 + \\beta_1 Day\\_c_i_j + \\nu_i + \\epsilon_i_j";
//        String latex3 = "\\int_0^{+\\infty} e^{-x^2}\\mathrm{d}x = \\frac\\sqrt{\\pi}2  \\sum_{n=0}^{\\infty}\\frac{1}{n^2}=\\frac{\\pi^2}6";
        String[] stageOneModelVarArray = new String[20];
        String latex1 = "Y1: PA";
        String latex2 = "X1: Day_c";

        stageOneModelVarArray[0] = latex1;
        stageOneModelVarArray[1] = latex2;

        return stageOneModelVarArray;
    }

    public static String[] getStageOneModelLatex() {
//        String latex2 = "PA_i_j = \\beta_0 + \\beta_1 Day\\_c_i_j + \\nu_i + \\epsilon_i_j";
//        String latex3 = "\\int_0^{+\\infty} e^{-x^2}\\mathrm{d}x = \\frac\\sqrt{\\pi}2  \\sum_{n=0}^{\\infty}\\frac{1}{n^2}=\\frac{\\pi^2}6";
        String[] stageOneModelLatexArray = new String[3];
        String latex1 = "Y_1_i_j = \\beta_0 + \\beta_1 X_1_i_j + \\nu_i + \\epsilon_i_j";
        String latex2 = "{\\sigma_{\\nu_i_j}^2} = \\exp(\\alpha_0 + \\alpha_1 X_1_i_j)";
        String latex3 = "{\\sigma_{\\epsilon_i_j}^2} = \\exp(\\tau_0 + \\tau_1 X_1_i_j + \\tau_\\nu \\nu_i + \\omega_i)";

        stageOneModelLatexArray[0] = latex1;
        stageOneModelLatexArray[1] = latex2;
        stageOneModelLatexArray[2] = latex3;

        return stageOneModelLatexArray;
    }
}
