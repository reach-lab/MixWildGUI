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
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import mixregui.SystemLogger;
import org.apache.commons.io.FilenameUtils;

/**
 * This class should be used to generate the model build after a file is run.
 *
 * @author eldin
 */
public class ModelBuilder {

    private MixLibrary buildFile;

    private static final String KEY_ALPHA = "\u03b1";
    private static final String KEY_BETA = "\u03b2";
    private static final String KEY_EPSILON = "\u03b5";
    private static final String KEY_I = "\u1d62";
    private static final String KEY_IJ = "\u1d62" + "\u2c7c";
    private static final String KEY_PLUS = " + ";

    public ModelBuilder(MixLibrary defFile) {
        this.buildFile = defFile;
//        switch (buildFile.sequenceDecision()) {
//            case DefinitionHelper.MIXREGLS_MIXREG_KEY:
//                break;
//            case DefinitionHelper.MIXREGLS_MIXOR_KEY:
//                break;
//            case DefinitionHelper.MIXREGMLS_MIXREG_KEY:
//                break;
//            case DefinitionHelper.MIXREGMLS_MIXOR_KEY:
//                break;
//            default:
//                break;
//        }
    }

    public String meanEquation() {
        String prefix = "Y" + KEY_IJ + " = ";
        String constant = KEY_BETA + KEY_IJ + KEY_PLUS;
        String bsvar = "u" + KEY_I + KEY_PLUS;
        String wsvar = KEY_EPSILON + KEY_IJ;
        if (buildFile.getAdvancedMeanIntercept().matches("0")) {
            constant = "";
        }
        String regressorsOne = "";
        String regressorsTwo = "";
        String[] meanArrayOne = buildFile.getLabelModelMeanRegressorsLevelOne();
        String[] meanArrayTwo = buildFile.getLabelModelMeanRegressorsLevelTwo();
        try {
            for (int i = 0; i < meanArrayOne.length; i++) {
                regressorsOne += meanArrayOne[i].toUpperCase() + KEY_IJ + KEY_PLUS;
            }
        } catch (NullPointerException npe) {
            SystemLogger.LOGGER.log(Level.SEVERE, npe.toString()+ "{0}", SystemLogger.getLineNum());

        }
        try {
            for (int i = 0; i < meanArrayTwo.length; i++) {
                regressorsOne += meanArrayTwo[i].toUpperCase() + KEY_I + KEY_PLUS;
            }
        } catch (NullPointerException npe) {
            SystemLogger.LOGGER.log(Level.SEVERE, npe.toString()+ "{0}", SystemLogger.getLineNum());

        }

        String regressors = regressorsOne + regressorsTwo;
        String[] decompMeanArray = buildFile.getSharedModelDecomposeMeanRegressorLabels();
        for (int i = 0; i < decompMeanArray.length; i++) {
            regressors += decompMeanArray[i].toUpperCase() + "_BS" + KEY_BETA + KEY_I + KEY_PLUS + decompMeanArray[i].toUpperCase() + "_WS" + KEY_BETA + KEY_IJ + KEY_PLUS;
        }

        return prefix + constant + regressors + bsvar + wsvar;
    }

    public static void saveWildDefinitionFile(String filePath, MixLibrary defLib) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(defLib);
        oos.close();
        fos.close();
    }

    /*
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
     */
    public static MixLibrary loadWildFile(String filePath) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filePath);
        ObjectInputStream ino = new ObjectInputStream(fis);
        MixLibrary s;
        s = (MixLibrary) ino.readObject();
        ino.close();
        fis.close();
        return s;
    }

    public static String buildFolder(File csvFile) throws IOException {
        String absolutePath = csvFile.getAbsolutePath();
        String folderPath = FilenameUtils.getFullPath(absolutePath);
//        String dirName = Long.toString(Instant.now().getEpochSecond());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMddHHmm");
        Date date = new Date(System.currentTimeMillis());
        String dirName = formatter.format(date);
        String newPath = folderPath + "MIXWILD" + dirName + "/";
        File dirGen = new File(newPath);
        System.out.println(newPath);
        boolean genTrue = dirGen.mkdirs();
        if (!genTrue) {
            throw new IOException("Cannot generate temporary work directory, please check folder permissions");
        }
        return "MIXWILD" + dirName + "/";
    }

    public static void archiveFolder(MixLibrary defLib, String absoluteWorkingDirectory) throws IOException {
        System.out.println(defLib.getUtcDirPath());
        System.out.println(absoluteWorkingDirectory);
        String folderPath = absoluteWorkingDirectory + defLib.getUtcDirPath();
        byte[] buffer = new byte[1024];
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        String outputZip = absoluteWorkingDirectory + defLib.getUtcDirPath().replace("/", "") + ".mwa";
        List<String> fileList = new ArrayList<String>();
        saveWildDefinitionFile(folderPath + "model.mwd", defLib);
        createZipEntryList(new File(folderPath), fileList, folderPath);

        fos = new FileOutputStream(outputZip);
        zos = new ZipOutputStream(fos);

        System.out.println("Input folderpath: " + folderPath);
        System.out.println("Output filename: " + outputZip);

        for (String file : fileList) {
            System.out.println("File in: " + file);
            ZipEntry ze = new ZipEntry(file);
            zos.putNextEntry(ze);

            FileInputStream in
                    = new FileInputStream(folderPath + "/" + file);

            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            in.close();
        }

        zos.closeEntry();
        zos.close();

        System.out.println("Complete: Save");

    }

    public static MixLibrary accessFolderArchive(File mwaFile) throws IOException, ClassNotFoundException {
        String folderPath = FilenameUtils.getFullPath(mwaFile.getAbsolutePath());
//        String dirName = Long.toString(Instant.now().getEpochSecond());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMddHHmm");
        Date date = new Date(System.currentTimeMillis());
        String dirName = formatter.format(date);
        String outputPath = folderPath + "MIXWILD" + dirName + "/";
        byte[] buffer = new byte[1024];

        File folder = new File(outputPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        FileInputStream fis = new FileInputStream(mwaFile.getAbsolutePath());
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry ze = zis.getNextEntry();

        while (ze != null) {
            String fileName = ze.getName();
            File newFile = new File(outputPath + "/" + fileName);
            System.out.println("Unzipped: " + newFile.getAbsoluteFile());

            new File(newFile.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            ze = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
        fis.close();

        MixLibrary returnDefLib = loadWildFile(outputPath + "model.mwd");
        returnDefLib.setUtcDirPath(mwaFile.getAbsolutePath());

        System.out.println("Complete: Open");
        return returnDefLib;
    }

    private static String createZipEntry(String file, String source) {
        return file.substring(source.length(), file.length());
    }

    private static void createZipEntryList(File input, List fileList, String source) {
        if (input.isFile()) {
            fileList.add(createZipEntry(input.getAbsoluteFile().toString(), source));
        }

        if (input.isDirectory()) {
            String[] subInput = input.list();
            for (String filename : subInput) {
                createZipEntryList(new File(input, filename), fileList, source);
            }
        }

    }

}
