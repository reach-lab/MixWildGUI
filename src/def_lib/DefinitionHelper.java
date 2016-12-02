package def_lib;

import java.util.ArrayList;

public class DefinitionHelper {
    public static final int MIXREGLS_MIXREG_KEY = 1;
    public static final int MIXREGLS_MIXOR_KEY = 2;
    public static final int MIXREGMLS_MIXREG_KEY = 3;
    public static final int MIXREGMLS_MIXOR_KEY = 4;
    
    private ArrayList initParameters = new ArrayList( );
    private ArrayList stageOneVariables = new ArrayList( );
    private ArrayList stageTwoVariables = new ArrayList( );
    private ArrayList advancedOptions = new ArrayList( );
    
    private int randomLocationEffects = 0;
    private boolean stageTwoBinary = Boolean.FALSE;

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
}