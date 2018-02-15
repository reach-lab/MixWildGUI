package def_lib;

import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private static final String KEY_EPSILON = "\u03b5";
    private static final String KEY_I = "\u1d62";
    private static final String KEY_IJ = "\u1d62" + "\u2c7c";
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
        String prefix = "Y" + KEY_IJ + " = ";
        String constant = KEY_BETA + KEY_IJ + KEY_PLUS;
        String bsvar = "u" + KEY_I + KEY_PLUS;
        String wsvar = KEY_EPSILON + KEY_IJ;
        if(buildFile.getModelFixedInt().matches("0")){
            constant="";
        }
        String regressorsOne = "";
        String regressorsTwo = "";
        String[] meanArrayOne = buildFile.getLabelModelMeanRegressorsLevelOne();
        String[] meanArrayTwo = buildFile.getLabelModelMeanRegressorsLevelTwo();
        try {
            for(int i = 0; i < meanArrayOne.length; i++){
                regressorsOne += meanArrayOne[i].toUpperCase() + KEY_IJ + KEY_PLUS;    
            }
        }
        catch (NullPointerException npe){
            
        }
        try {
            for(int i = 0; i < meanArrayTwo.length; i++){
                regressorsOne += meanArrayTwo[i].toUpperCase() + KEY_I + KEY_PLUS;    
            }
        }
        catch(NullPointerException npe) {
           
        }
           
        String regressors = regressorsOne + regressorsTwo;
        String[] decompMeanArray = buildFile.getLabelDecompMeanRegressors();
        for(int i = 0; i < decompMeanArray.length; i++){
            regressors += decompMeanArray[i].toUpperCase() + "_BS"+ KEY_BETA + KEY_I + KEY_PLUS + decompMeanArray[i].toUpperCase() + "_WS" + KEY_BETA + KEY_IJ + KEY_PLUS;    
        }
        
        return prefix + constant + regressors + bsvar + wsvar;
    }
    
    public static void saveWildDefinitionFile(String filePath, DefinitionHelper defLib) throws FileNotFoundException, IOException {
        FileOutputStream fos2 = new FileOutputStream(filePath);
        ObjectOutputStream out2= new ObjectOutputStream(fos2);
        out2.writeObject(defLib);  
        out2.flush();
        out2.close();
        fos2.close();
    }
    
    
    public static void saveWildFile(DefinitionHelper defLib) throws FileNotFoundException, IOException {
        FileInputStream in = new FileInputStream(defLib.getDataFilename());
        String fileNamePath = FilenameUtils.getFullPath(defLib.getDataFilename());

        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fileNamePath + defLib.getOutputPrefix() + ".mwa"));
        out.putNextEntry(new ZipEntry(FilenameUtils.getName(defLib.getDataFilename()))); 

        byte[] b = new byte[1024];
        int count;

        while ((count = in.read(b)) > 0) {
            out.write(b, 0, count);
        }
        out.closeEntry();
        in.close();
        
        saveWildDefinitionFile(fileNamePath + defLib.getOutputPrefix() + ".mwd", defLib);
        
        FileInputStream in2 = new FileInputStream(fileNamePath + defLib.getOutputPrefix() + ".mwd");
        out.putNextEntry(new ZipEntry(defLib.getOutputPrefix() + ".mwd"));
        byte[] b2 = new byte[1024];
        int count2;
        
        while ((count2 = in2.read(b2)) > 0) {
            out.write(b2, 0, count2);
        }
        out.close();
        in2.close();   
    }
    
    public static DefinitionHelper loadWildFile (String filePath) throws IOException, ClassNotFoundException {
        ObjectInputStream ino =new ObjectInputStream(new FileInputStream(filePath));  
        DefinitionHelper s;
        s = (DefinitionHelper)ino.readObject(); 
        return s;
    }
    
    public static String buildFolder(File csvFile) throws IOException {
        String absolutePath = csvFile.getAbsolutePath();
        String folderPath = FilenameUtils.getFullPath(absolutePath);
        String dirName = Long.toString(Instant.now().getEpochSecond());
        String newPath = folderPath + "MIXWILD" + dirName + "/";
        File dirGen = new File(newPath);
        System.out.println(newPath);
        boolean genTrue = dirGen.mkdirs();
        if(!genTrue){
            throw new IOException("Cannot generate temporary work directory, please check folder permissions");
        }
        return "MIXWILD"  + dirName + "/";
    }
    
    public static void archiveFolder(DefinitionHelper defLib) {
        
    }
    
    public static void accessFolderArchive(File mwaFile) {
        
    }
    
} 
