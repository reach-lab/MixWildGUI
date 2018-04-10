/*
 * MixWild, a program to model subject-level slope and variance on continuous or ordinal outcomes
    Copyright (C) 2018 (not sure what name goes here?)

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
package mixregui;

import com.opencsv.CSVReader;
import com.sun.glass.events.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import def_lib.DefinitionHelper;
import def_lib.ModelBuilder;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileReader;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author adityaponnada
 */
public class NewModel extends javax.swing.JFrame {

    //Object declarations
    JFileChooser fileChooser;
    File file;
    static String[] variableArray;
    static int RLE;
    static boolean NoneVar;
    static boolean outComeBoolean;
    static mixregGUI mxr;
    InstructionsGUI instructions;
    static boolean isRandomScale = false;
    static String dataFileNameRef;
    final ImageIcon icon;
    String missingValue = "0";

    /**
     * Declare libraries
     */
    public static DefinitionHelper defFile;
    public static ModelBuilder modelBuilder;

    /**
     * Creates new form NewModel
     */
    public NewModel() {
        initComponents();
        this.setResizable(false);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        fileChooser = new JFileChooser();
        instructions = new InstructionsGUI();
        icon = new ImageIcon(getClass().getResource("/resources/mixLogo.png"));
        // defFile = new def_lib.DefinitionHelper();

        //subtitle ---> ouput prefix + date and time
        //Set fields disabled till a data file is loaded
        titleField.setEnabled(false);
        newModel_resetButton.setEnabled(false);
        // subtitleField.setEnabled(false);
        continuousRadio.setEnabled(false);
        dichotomousRadio.setEnabled(false);
        //randomLocationEffects.setEnabled(false);
        newModelSubmit.setEnabled(false);
        //newModelMissingValueCode.setText("-999");
        // newModelMissingValueCode.setEnabled(false);
        // newModelMissingValues.setEnabled(false);
        // newModelMissingValues.setSelected(true);
        missingValuePresent.setEnabled(false);
        missingValueAbsent.setEnabled(false);
        noneRadio.setEnabled(false);
        randomScaleCheckBox.setEnabled(false);
        oneRLERadio.setEnabled(false);
        moreThanOneRLERadio.setEnabled(false);

        dataFileLabel.setToolTipText("Insert a data file in .csv format");
        fileBrowseButton.setToolTipText("Insert a data file in .csv format");

        filePath.setToolTipText("Insert a data file in .csv format");
        titleField.setToolTipText("Insert title for the model");
        jLabel2.setToolTipText("Insert title for the model");
        jLabel4.setToolTipText("Select which variables define subject  mean (location) in model");
        oneRLERadio.setToolTipText("Random subject intercept defines the subject mean (location) in the model");
        moreThanOneRLERadio.setToolTipText("Random subject intercept and random subject slope(s) "
                + "define the subject mean (location) in the model");
        jLabel10.setToolTipText("Select whether or not the model will include subject variability (scale)");
        randomScaleCheckBox.setToolTipText("Select whether or not the model will include subject variability (scale)");
        jLabel5.setToolTipText("Select the type of outcome variable in the second-stage model; alternately, you may run the first stage only");
        continuousRadio.setToolTipText("Second stage outcome will be a continuous variable");
        dichotomousRadio.setToolTipText("Second stage outcome will be a dichotomous or ordinal variable");
        noneRadio.setToolTipText("The program will not produce a second stage model");
        jLabel3.setToolTipText("Select whether or not explicitly defined missing values are present in data");
        missingValuePresent.setToolTipText("Missing values are present and explicitly defined");
        missingValueAbsent.setToolTipText("There are no missing values in the data");
        jLabel6.setToolTipText("Specify missing value code");
        newModelMissingValueCode.setToolTipText("Specify missing value code");
        // subtitleField.setToolTipText("Insert subtitle for the model");
        //randomLocationEffects.setToolTipText("Select the number of random location effects. Minimum value is 1");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        dataFileLabel = new javax.swing.JLabel();
        filePath = new javax.swing.JTextField();
        fileBrowseButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        titleField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        continuousRadio = new javax.swing.JRadioButton();
        dichotomousRadio = new javax.swing.JRadioButton();
        newModelSubmit = new javax.swing.JButton();
        newModelCancel = new javax.swing.JButton();
        newModel_resetButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        newModelMissingValueCode = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        noneRadio = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        randomScaleCheckBox = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        oneRLERadio = new javax.swing.JRadioButton();
        moreThanOneRLERadio = new javax.swing.JRadioButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        missingValuePresent = new javax.swing.JRadioButton();
        missingValueAbsent = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Model for Stage 1 Analysis");
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dataFileLabel.setText("Data File: ");
        getContentPane().add(dataFileLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        filePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filePathActionPerformed(evt);
            }
        });
        getContentPane().add(filePath, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 330, -1));

        fileBrowseButton.setText("Browse");
        fileBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileBrowseButtonActionPerformed(evt);
            }
        });
        getContentPane().add(fileBrowseButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 70, 90, -1));

        jLabel2.setText("Title:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        titleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titleFieldActionPerformed(evt);
            }
        });
        getContentPane().add(titleField, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 330, -1));

        jLabel4.setText("Random Location Effects:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, -1, -1));

        jLabel5.setText("Stage 2 Outcome:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, -1, 40));

        buttonGroup1.add(continuousRadio);
        continuousRadio.setText("Continuous");
        continuousRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continuousRadioActionPerformed(evt);
            }
        });
        getContentPane().add(continuousRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 110, -1));

        buttonGroup1.add(dichotomousRadio);
        dichotomousRadio.setText("Dichotomous/Ordinal");
        dichotomousRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dichotomousRadioActionPerformed(evt);
            }
        });
        getContentPane().add(dichotomousRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 250, 170, -1));

        newModelSubmit.setText("Submit");
        newModelSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newModelSubmitActionPerformed(evt);
            }
        });
        getContentPane().add(newModelSubmit, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 440, 90, -1));

        newModelCancel.setText("Cancel");
        newModelCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newModelCancelActionPerformed(evt);
            }
        });
        getContentPane().add(newModelCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 440, 90, -1));

        newModel_resetButton.setText("Reset");
        newModel_resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newModel_resetButtonActionPerformed(evt);
            }
        });
        getContentPane().add(newModel_resetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 440, 90, -1));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(538, 334, -1, -1));

        jLabel6.setText("Missing value code:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, -1, 50));

        newModelMissingValueCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newModelMissingValueCodeActionPerformed(evt);
            }
        });
        newModelMissingValueCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                newModelMissingValueCodeKeyTyped(evt);
            }
        });
        getContentPane().add(newModelMissingValueCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 340, 77, 30));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/mixLogo.png"))); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, -1, 31));

        buttonGroup1.add(noneRadio);
        noneRadio.setText("None");
        noneRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noneRadioActionPerformed(evt);
            }
        });
        getContentPane().add(noneRadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 240, 70, 40));

        jButton1.setText("Check here...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 110, -1));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel9.setText("Is your dataset Mix{WILD} friendly?");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, -1, 30));

        jLabel10.setText("Random Scale?");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, -1, -1));

        randomScaleCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomScaleCheckBoxActionPerformed(evt);
            }
        });
        getContentPane().add(randomScaleCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 210, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 520, -1));
        getContentPane().add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 144, 524, 10));

        buttonGroup2.add(oneRLERadio);
        oneRLERadio.setText("Intercept");
        getContentPane().add(oneRLERadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 170, 90, -1));

        buttonGroup2.add(moreThanOneRLERadio);
        moreThanOneRLERadio.setText("Intercept + Slope(s)");
        getContentPane().add(moreThanOneRLERadio, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 170, -1, -1));
        getContentPane().add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 416, 516, 10));

        jLabel3.setText("Contains missing values?");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        buttonGroup4.add(missingValuePresent);
        missingValuePresent.setText("Yes");
        missingValuePresent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                missingValuePresentActionPerformed(evt);
            }
        });
        getContentPane().add(missingValuePresent, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 300, 60, 20));

        buttonGroup4.add(missingValueAbsent);
        missingValueAbsent.setText("No");
        missingValueAbsent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                missingValueAbsentActionPerformed(evt);
            }
        });
        getContentPane().add(missingValueAbsent, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 300, -1, 20));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void filePathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filePathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filePathActionPerformed

    private void fileBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileBrowseButtonActionPerformed
        //JFileChooser fileChooser = new JFileChooser();
        //fileChooser.showOpenDialog(null);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Data files", "csv");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // File file = fileChooser.getSelectedFile();
            // What to do with the file, e.g. display it in a TextArea
            //textarea.read( new FileReader( file.getAbsolutePath() ), null );
            //Select file from the file object
            file = fileChooser.getSelectedFile();
            //get file path to display on the text box
            String fileName = file.getAbsolutePath();
            dataFileNameRef = fileName;
            filePath.setText(fileName);
            //enable other buttons here:
            titleField.setEnabled(true);
            //subtitleField.setEnabled(true);
            //randomLocationEffects.setEnabled(true);
            oneRLERadio.setEnabled(true);
            moreThanOneRLERadio.setEnabled(true);
            continuousRadio.setEnabled(true);
            dichotomousRadio.setEnabled(true);
            // randomLocationEffects.setEnabled(true);
            newModelSubmit.setEnabled(true);
            // newModelMissingValues.setEnabled(true);
            missingValuePresent.setEnabled(true);
            missingValueAbsent.setEnabled(true);
            // newModelMissingValueCode.setEnabled(true);
            noneRadio.setEnabled(true);
            newModel_resetButton.setEnabled(true);
            randomScaleCheckBox.setEnabled(true);
            randomScaleCheckBox.setSelected(true);
            // newModelMissingValueCode.selectAll();
            System.out.println(file.getAbsolutePath());
        } else {
            System.out.println("File access cancelled by user.");
        }


    }//GEN-LAST:event_fileBrowseButtonActionPerformed

    private void continuousRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continuousRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_continuousRadioActionPerformed

    private void newModelSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newModelSubmitActionPerformed

        // System.getProperty("os.name");
//        String osName = System.getProperty("os.name");
//        System.out.println("YOUR OPERATING SYSTEM IS: " + osName);
//        if (osName.contains("Windows")){
//            System.out.println("YES THE OS IS WINDOWS");
//        } else {
//            System.out.println("NO THE OS IS NOT WINDOWS");
//        }
        System.out.println("Model submitted" + " called");

        if (validateFields() == true) {
            System.out.print("condition is true");
            if (oneRLERadio.isSelected() == true) {
                RLE = 1;
            } else if (moreThanOneRLERadio.isSelected() == true) {
                RLE = 2;
            }

            defFile = new DefinitionHelper(RLE, !isOutcomeContinous());
            // modelBuilder = new ModelBuilder(defFile);
            System.out.println("RLE: " + String.valueOf(RLE));

            defFile.modelSelector(RLE, isOutcomeContinous());

            if (missingValuePresent.isSelected()) {

                System.out.println("NEW MODEL | MISSING VALUE = " + newModelMissingValueCode.getText());

                try {
                    System.out.println("NEW MODEL | MISSING VALUE = " + newModelMissingValueCode.getText());
                    defFile.setAdvancedMissingValue(String.valueOf(newModelMissingValueCode.getText()));

                    System.out.println("From defHelper | Missing Value: " + defFile.getAdvancedMissingValue());
                } catch (Exception ex) {
                    Logger.getLogger(NewModel.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE);
                }

            } else if (missingValueAbsent.isSelected()) {
                try {
                    defFile.setAdvancedMissingValue(String.valueOf(missingValue));
                    System.out.println("From defHelper | Missing Value: " + defFile.getAdvancedMissingValue());
                } catch (Exception ex) {
                    Logger.getLogger(NewModel.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE);
                }
                //do nothing
            }

            try {
                //convert csv to .dat file ...
                //defFile.
                defFile.csvToDatConverter(file);
            } catch (IOException ex) {
                Logger.getLogger(NewModel.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE);
            }

            System.out.println("MODEL SELECTOR: " + String.valueOf(defFile.getSelectedModel()));

            if (filePath.getText().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "Please upload a .csv file to start your analysis", "Caution!", JOptionPane.INFORMATION_MESSAGE);
            } else {

                try {

                    // Read file contents
                    Scanner inputStream = new Scanner(file);

                    // Read variable names from row 1
                    String variableNames = inputStream.next();
                    variableArray = variableNames.split(",");
                    // save all variables in an array
                    String[] varTemp = getVariableNames();
                    defFile.setDataFilename(extractDatFilePath());
                    defFile.setDataVariableCount(String.valueOf(variableArray.length));
                    System.out.println("From defHelper | Variable count: " + defFile.getDataVariableCount());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(NewModel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(NewModel.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Read random location effects from new Model
                //RLE = (Integer) randomLocationEffects.getValue();
                NoneVar = isOutcomeNone();
                outComeBoolean = isOutcomeContinous();
                System.out.println("NoneVar: " + String.valueOf(NoneVar));
                System.out.println(String.valueOf(isOutcomeContinous()));
                System.out.println("IsOutcomeNone: " + String.valueOf(isOutcomeNone()));
                // set Values in def helper
                defFile.setModelTitle(getTitle());
                System.out.println("From defHelper | Title: " + defFile.getModelTitle());
                //defFile.setModelSubtitle(getSubTitle());
                System.out.println("From defHelper | Subtitle: " + defFile.getModelSubtitle());

                //Check if the randome scale is checked or not
                if (randomScaleCheckBox.isSelected()) {
                    isRandomScale = true;
                } else {
                    isRandomScale = false;
                }

                //       mxr = new mixregGUI();
                //       mxr.isSubmitClicked();
                //       mxr.setVisible(true);
                //       //Update ID, stage one and stage two variable comboboxes
                //       mxr.updateComboBoxes();
                NewModel.defFile.setModelSubtitle("Created with MixWILD GUI");
                System.out.println("From defHelper | Subtitle: " + NewModel.defFile.getModelSubtitle());

                //set advanced options defaults and assign values to defition library
                try {
                    NewModel.defFile.setModelFixedInt(String.valueOf(0));
                    System.out.println("From defHelper | Mean SubModel Checked?: " + NewModel.defFile.getModelFixedInt());
                    //tryCount = 1;
                } catch (Exception ex) {
                    //catchCount = 1;
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    NewModel.defFile.setModelRandomInt(String.valueOf(0));
                    NewModel.defFile.setModelBetweenInt(String.valueOf(0));
                    System.out.println("From defHelper | BS SubModel Checked?: " + NewModel.defFile.getModelRandomInt());
                    System.out.println("From defHelper | BS SubModel Checked?: " + NewModel.defFile.getModelBetweenInt());
                } catch (Exception ex) {
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    NewModel.defFile.setModelScaleInt(String.valueOf(0));
                    NewModel.defFile.setModelWithinInt(String.valueOf(0));
                    System.out.println("From defHelper | WS SubModel Checked?: " + NewModel.defFile.getModelScaleInt());
                    System.out.println("From defHelper | WS SubModel Checked?: " + NewModel.defFile.getModelWithinInt());
                    //tryCount = 1;
                } catch (Exception ex) {
                    //catchCount = 1;
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    NewModel.defFile.setAdvancedAdaptiveQuad(String.valueOf(1));
                    System.out.println("From defHelper | Adaptive Quadriture Checked?: " + NewModel.defFile.getAdvancedAdaptiveQuad());
                    //tryCount = 1;
                } catch (Exception ex) {
                    //catchCount = 1;
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    NewModel.defFile.setAdvancedConvergence(String.valueOf(0.00001));
                    System.out.println("From defHelper | Convergence: " + NewModel.defFile.getAdvancedConvergence());
                    //tryCount = 1;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    //catchCount = 1;
                }
                try {
                    NewModel.defFile.setAdvancedQuadPoints(String.valueOf(11));
                    System.out.println("From defHelper | Quadriture Points: " + NewModel.defFile.getAdvancedQuadPoints());
                    //tryCount = 1;
                } catch (Exception ex) {
                    //catchCount = 1;
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    NewModel.defFile.setAdvancedMaxIteration(String.valueOf(200));
                    System.out.println("From defHelper | Maximum Iteraions: " + NewModel.defFile.getAdvancedMaxIteration());
                    //tryCount = 1;
                } catch (Exception ex) {
                    //catchCount = 1;
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    NewModel.defFile.setAdvancedRidge(String.valueOf(0.15));
                    System.out.println("From defHelper | Ridge: " + NewModel.defFile.getAdvancedRidge());
                    //tryCount = 1;
                } catch (Exception ex) {
                    //catchCount = 1;
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    NewModel.defFile.setAdvancedCutoffLower("0");
                    System.out.println("CUT OFF: " + NewModel.defFile.getAdvancedCutoffLower());
                } catch (Exception ex) {
                    Logger.getLogger(NewModel.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    NewModel.defFile.setAdvancedDiscardSubjects("0");
                    System.out.println("DISCARD SUBJECTS: " + NewModel.defFile.getAdvancedDiscardSubjects());

                } catch (Exception ex) {
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    NewModel.defFile.setAdvancedCenterScale(String.valueOf(0));
                    System.out.println("From defHelper | Scale Regressor: " + NewModel.defFile.getAdvancedCenterScale());
                } catch (Exception ex) {
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }
                try {
                    NewModel.defFile.setAdvancedResampleCount("500");
                    System.out.println("From defHelper | Resample count: " + NewModel.defFile.getAdvancedResampleCount());
                } catch (Exception ex) {
                    Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                }

                NewModel.defFile.setOutputPrefix(extractDatFileName() + "_Output");
                System.out.println("From defHelper | Output file name: " + NewModel.defFile.getOutputPrefix());

                if (randomScaleCheckBox.isSelected()) {
                    try {
                        NewModel.defFile.setAdvancedRandomScaleNotIncluded("0");
                        System.out.println("IS RANDOM SCALE INCLUDED: " + NewModel.defFile.getAdvancedRandomScaleNotIncluded());
                    } catch (Exception ex) {
                        Logger.getLogger(NewModel.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    }

                } else {

                    try {
                        NewModel.defFile.setAdvancedRandomScaleNotIncluded("1");
                        System.out.println("IS RANDOM SCALE INCLUDED: " + NewModel.defFile.getAdvancedRandomScaleNotIncluded());
                    } catch (Exception ex) {
                        Logger.getLogger(NewModel.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    }

                }

                if (isOutcomeNone()) {
                    try {
                        NewModel.defFile.setAdvancedDropSecondStage("1");
                        System.out.println("DROP SECOND STAGE?: " + NewModel.defFile.getAdvancedDropSecondStage());
                    } catch (Exception ex) {
                        Logger.getLogger(NewModel.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    }

                } else {

                    try {
                        NewModel.defFile.setAdvancedDropSecondStage("0");
                        System.out.println("DROP SECOND STAGE?: " + NewModel.defFile.getAdvancedDropSecondStage());
                    } catch (Exception ex) {
                        Logger.getLogger(NewModel.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                    }

                }

                //set conditions here:
                if (newModelMissingValueCode.getText().equals("0") || newModelMissingValueCode.getText().equals("00") || newModelMissingValueCode.getText().equals("000")) {
                    //show message alert here:
                    JOptionPane.showMessageDialog(null, "Invalid missing value code, 0 implies there are no missing values. Please use some other value. E.g., -999", "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
                } else {
                    mxr = new mixregGUI();
                    mxr.isSubmitClicked();
                    mxr.setVisible(true);
                    mxr.randomLocationEffectsLabel.setText("Random location effects: " + randomLocationEffects());
                    mxr.stageTwoOutcomePrintLabel.setText("Stage 2 outcome: " + outComeTypeString());
                    //Update ID, stage one and stage two variable comboboxes
                    mxr.updateComboBoxes();
                    this.dispose();
                }

            }

            try {
                getDataFromCSV();
                printFileName();
                System.out.println("NEW MODEL DATA READ");
            } catch (IOException ex) {
                Logger.getLogger(NewModel.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            }

        } else {

            System.out.println("VALIDATION OF FIELDS: " + String.valueOf(false));

        }

    }//GEN-LAST:event_newModelSubmitActionPerformed

    private void newModelCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newModelCancelActionPerformed
        // TODO add your handling code here:
        //Close(NewModel);
        this.dispose();
    }//GEN-LAST:event_newModelCancelActionPerformed

    private void titleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titleFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_titleFieldActionPerformed

    private void newModel_resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newModel_resetButtonActionPerformed
        // TODO add your handling code here:

        filePath.setText("");
        titleField.setText("");
        titleField.setEnabled(false);
        // subtitleField.setEditable(false);
        // subtitleField.setText("");
        //randomLocationEffects.setValue(1);
        //randomLocationEffects.setEnabled(false);
        continuousRadio.setEnabled(false);
        dichotomousRadio.setEnabled(false);
        //randomLocationEffects.setEnabled(false);
        newModelSubmit.setEnabled(false);
        // newModelMissingValues.setEnabled(false);
        newModelMissingValueCode.setEnabled(false);
        noneRadio.setSelected(false);
        noneRadio.setEnabled(false);
        randomScaleCheckBox.setSelected(false);
        randomScaleCheckBox.setEnabled(false);

    }//GEN-LAST:event_newModel_resetButtonActionPerformed

    private void newModelMissingValueCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newModelMissingValueCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newModelMissingValueCodeActionPerformed

    private void noneRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noneRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_noneRadioActionPerformed

    private void newModelMissingValueCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newModelMissingValueCodeKeyTyped
        // TODO add your handling code here:

        char vchar = evt.getKeyChar();
//        if (!((Character.isDigit(vchar)) || (vchar == KeyEvent.VK_BACKSPACE) || (vchar == KeyEvent.VK_DELETE) || (vchar == KeyEvent.VK_MINUS))) {
//            evt.consume();
//        }
        if (!((Character.isDigit(vchar)) || (vchar == KeyEvent.VK_BACKSPACE) || (vchar == KeyEvent.VK_DELETE) || (vchar == KeyEvent.VK_MINUS) || (vchar == '.'))) {
            evt.consume();
        }

    }//GEN-LAST:event_newModelMissingValueCodeKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        instructions.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void randomScaleCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_randomScaleCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_randomScaleCheckBoxActionPerformed

    private void dichotomousRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dichotomousRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dichotomousRadioActionPerformed

    private void missingValuePresentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_missingValuePresentActionPerformed
        // TODO add your handling code here:
        if (missingValuePresent.isSelected()) {
            newModelMissingValueCode.setEnabled(true);
            newModelMissingValueCode.setText("-999");
            newModelMissingValueCode.selectAll();
        }

    }//GEN-LAST:event_missingValuePresentActionPerformed

    private void missingValueAbsentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_missingValueAbsentActionPerformed
        // TODO add your handling code here:
        if (missingValueAbsent.isSelected()) {
            newModelMissingValueCode.setEnabled(false);
            newModelMissingValueCode.setText("");
        }

    }//GEN-LAST:event_missingValueAbsentActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewModel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewModel().setVisible(true);
            }
        });
    }

    //Open data file - unused
    private void fileOpen() {
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // What to do with the file, e.g. display it in a TextArea
            //textarea.read( new FileReader( file.getAbsolutePath() ), null );

            System.out.println(file.getAbsolutePath());
        } else {
            System.out.println("File access cancelled by user.");
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JRadioButton continuousRadio;
    private javax.swing.JLabel dataFileLabel;
    private javax.swing.JRadioButton dichotomousRadio;
    private javax.swing.JButton fileBrowseButton;
    private javax.swing.JTextField filePath;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JRadioButton missingValueAbsent;
    private javax.swing.JRadioButton missingValuePresent;
    private javax.swing.JRadioButton moreThanOneRLERadio;
    private javax.swing.JButton newModelCancel;
    private javax.swing.JTextField newModelMissingValueCode;
    private javax.swing.JButton newModelSubmit;
    private javax.swing.JButton newModel_resetButton;
    private javax.swing.JRadioButton noneRadio;
    private javax.swing.JRadioButton oneRLERadio;
    private javax.swing.JCheckBox randomScaleCheckBox;
    private javax.swing.JTextField titleField;
    // End of variables declaration//GEN-END:variables

    //get the number of random location effects    
    public int getRLE() {
        return RLE;
    }

    //get the variable names from the data file
    public static String[] getVariableNames() {

        return variableArray;
    }

    //get data file name when created
    public static String getDataFileName() {

        return dataFileNameRef;
    }

    //get the instance of the model mixReg declared in newModel
    public mixregGUI getMixReg() {

        return mxr;
    }

    //get title from the text box
    public String getTitle() {

        String titleString = titleField.getText().toString();

        return titleString;

    }

//check if the outcome type is selected as continuos or dichotomous
    public boolean isOutcomeContinous() {

        boolean selection = true;

        if (continuousRadio.isSelected() == true) {

            selection = true;
            System.out.println("Outcome selected at Newmodel: " + String.valueOf(selection));
        } else if (dichotomousRadio.isSelected() == true) {

            selection = false;
            System.out.println("Outcome selected at Newmodel: " + String.valueOf(selection));
        }

        System.out.println("Outcome selected at Newmodel: " + String.valueOf(selection));
        return selection;
    }

    public boolean isOutcomeNone() {
        System.out.println("In isOutcomeNone NewModel");

        boolean selection = false;

        if (noneRadio.isSelected() == true) {
            selection = true;
            System.out.println("In isOutcomeNone true");
        } else if (noneRadio.isSelected() == false) {

            selection = false;
        }
        return selection;
    }

    public boolean getNoneVar() {
        return NoneVar;

    }

    public boolean getOutComeType() {

        return outComeBoolean;
    }

    public DefinitionHelper getDefFile() {

        return defFile;
    }

    public String extractDatFileName() {

        String fileLoc = file.getAbsolutePath();
        String fileName = fileLoc.substring(fileLoc.lastIndexOf(File.separator) + 1);
        int iend = fileName.indexOf(".");

        if (iend != -1) {
            fileName = fileName.substring(0, iend);
        }

        return fileName;
    }

    public String extractDatFilePath() {

        String csvPath = file.getAbsolutePath();
        String datPath = FilenameUtils.getFullPath(csvPath) +
                 defFile.getUtcDirPath() +
                FilenameUtils.getBaseName(csvPath) + ".dat";
        return datPath;
    }

    public void getDataFromCSV() throws FileNotFoundException, IOException {

        Object[] columnnames;

        CSVReader CSVFileReader = new CSVReader(new FileReader(file));
        List myEntries = CSVFileReader.readAll();
        columnnames = (String[]) myEntries.get(0);
        DefaultTableModel tableModel = new DefaultTableModel(columnnames, myEntries.size() - 1) {

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        int rowcount = tableModel.getRowCount();

        for (int x = 0; x < rowcount + 1; x++) {
            int columnnumber = 0;
            // if x = 0 this is the first row...skip it... data used for columnnames
            if (x > 0) {
                for (String thiscellvalue : (String[]) myEntries.get(x)) {
                    tableModel.setValueAt(thiscellvalue, x - 1, columnnumber);
                    columnnumber++;
                }
            }
        }

        mixregGUI.dataTable.setModel(tableModel);
        mixregGUI.dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mixregGUI.dataTable.setAutoscrolls(true);
    }

    public void printFileName() {
        String path = file.getName();

        mixregGUI.printedFileName.setText(path);

    }

    private boolean validateFields() {
        boolean allFieldsEntered = true;
        System.out.println("FIELD VALIDATE: " + "Called");

        if (filePath.getText().trim().length() == 0) {
            allFieldsEntered = false;
            JOptionPane.showMessageDialog(null, "Please select a .csv file for your analysis", "Missing information!", JOptionPane.INFORMATION_MESSAGE, icon);
            System.out.println("FIELD VALIDATE: " + "File path missing");
        }
        //radio buttons for random location effects
        if (buttonGroup2.getSelection() == null) {
            allFieldsEntered = false;
            JOptionPane.showMessageDialog(null, "Please select random location effects", "Missing information!", JOptionPane.INFORMATION_MESSAGE, icon);
            System.out.println("FIELD VALIDATE: " + "RLE not selected");
        }
        //for stage 2 outcome type
        if (buttonGroup1.getSelection() == null) {
            allFieldsEntered = false;
            JOptionPane.showMessageDialog(null, "Please select a stage 2 outcome type", "Missing information!", JOptionPane.INFORMATION_MESSAGE, icon);
            System.out.println("FIELD VALIDATE: " + "Stage 2 outcome missing");
        }
        //For missing values, yes or no.
        if (buttonGroup4.getSelection() == null) {
            allFieldsEntered = false;
            JOptionPane.showMessageDialog(null, "Please confirm if your data has missing values", "Missing information!", JOptionPane.INFORMATION_MESSAGE, icon);
            System.out.println("FIELD VALIDATE: " + "Missing value missing");
        }

        if (newModelMissingValueCode.isEnabled() && newModelMissingValueCode.getText().trim().length() == 0) {
            allFieldsEntered = false;
            JOptionPane.showMessageDialog(null, "Please don't leave the missing code value as blank", "Missing information!", JOptionPane.INFORMATION_MESSAGE, icon);
            System.out.println("FIELD VALIDATE: " + "Missing value blank");
        }
        System.out.println("FIELD VALIDATE: " + "about to exit");
        return allFieldsEntered;
    }

    public String randomLocationEffects() {

        String randLocString = "";

        if (oneRLERadio.isSelected()) {
            randLocString = "Intercept";

        } else if (moreThanOneRLERadio.isSelected()) {
            randLocString = "Intercept + Slope";

        }

        return randLocString;
    }

    public String outComeTypeString() {

        String outcomeTypeText = "";

        if (continuousRadio.isSelected()) {

            outcomeTypeText = "Continuous";
        } else if (dichotomousRadio.isSelected()) {

            outcomeTypeText = "Dichotomous";
        } else if (noneRadio.isSelected()) {

            outcomeTypeText = "None";
        }
        return outcomeTypeText;

    }

}
