package def_lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FilenameUtils;


/**
 * This class should be used to generate the model build after a file is run.
 * @author eldin
 */
public class ModelBuilder {
    
    private DefinitionHelper buildFile;
    
    private static final String KEY_ALPHA = "\u03b1";
    private static final String KEY_BETA = "\u03b2";
    private static final String KEY_EPSILON = "\03b5";
    private static final String KEY_PLUS = " + ";
    
    
    public ModelBuilder(DefinitionHelper defFile){
        this.buildFile = defFile;
        switch(buildFile.sequenceDecision()){
            case DefinitionHelper.MIXREGLS_MIXREG_KEY:  
                break;
            case DefinitionHelper.MIXREGLS_MIXOR_KEY:
                break;
            case DefinitionHelper.MIXREGMLS_MIXREG_KEY:
                break;
            case DefinitionHelper.MIXREGMLS_MIXOR_KEY:
                break;
            default:
                break;
        }
    }
    
    public String meanEquation(){
        String prefix = "yij = ";
        String constant = KEY_BETA + " + ";
        String bsvar = "ui + ";
        String wsvar = KEY_EPSILON + "ij";
        if(buildFile.getModelFixedInt().matches("0")){
            constant="";
        }
        String regressors = "";
        String[] meanArray = buildFile.getLabelModelMeanRegressors();
        for(int i = 0; i < meanArray.length; i++){
            regressors += meanArray[i].toUpperCase() + KEY_PLUS;    
        }
        String[] decompMeanArray = buildFile.getLabelDecompMeanRegressors();
        for(int i = 0; i < decompMeanArray.length; i++){
            regressors += decompMeanArray[i].toUpperCase() + "_BS"+ KEY_BETA + "i" + KEY_PLUS + decompMeanArray[i].toUpperCase() + "_WS" + KEY_BETA + "ij" + KEY_PLUS;    
        }
        return prefix + constant + regressors + bsvar + wsvar;
    }
    
    public void saveWildFile() throws FileNotFoundException, IOException {
        FileInputStream in = new FileInputStream(buildFile.getDataFilename());
        String fileNamePath = FilenameUtils.getFullPath(buildFile.getDataFilename());

        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fileNamePath + buildFile.getOutputPrefix() + ".mwa"));
        out.putNextEntry(new ZipEntry(FilenameUtils.getName(buildFile.getDataFilename()))); 

        byte[] b = new byte[1024];
        int count;

        while ((count = in.read(b)) > 0) {
            out.write(b, 0, count);
        }
        out.closeEntry();
        in.close();
        
        //Gson gson = new GsonBuilder().create();
        //String jsonString = gson.toJson(buildFile);
        FileOutputStream fos2 = new FileOutputStream(fileNamePath + buildFile.getOutputPrefix() + ".mwd");
        ObjectOutputStream out2= new ObjectOutputStream(fos2);
        out2.writeObject(buildFile);  
        out2.flush();
        out2.close();
        
        FileInputStream in2 = new FileInputStream(fileNamePath + buildFile.getOutputPrefix() + ".mwd");
        out.putNextEntry(new ZipEntry(buildFile.getOutputPrefix() + ".mwd"));
        byte[] b2 = new byte[1024];
        int count2;
        
        while ((count2 = in2.read(b2)) > 0) {
            out.write(b2, 0, count2);
        }
        out.close();
        in2.close();
    }
    
}
     
    
    
    
