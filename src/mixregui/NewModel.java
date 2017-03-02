/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mixregui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import def_lib.DefinitionHelper;
import javax.swing.JFrame;

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
    static mixregGUI mxr;
    
    public static def_lib.DefinitionHelper defFile;
     
    
    

    /**
     * Creates new form NewModel
     */
    public NewModel() {
        initComponents();
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
       fileChooser = new JFileChooser();
      // defFile = new def_lib.DefinitionHelper();
      
      
       
       titleField.setEnabled(false);
       subtitleField.setEnabled(false);
       continuousRadio.setEnabled(false);
       dichotomousRadio.setEnabled(false);
       randomLocationEffects.setEnabled(false);
       newModelSubmit.setEnabled(false);
       
       dataFileLabel.setToolTipText("Insert a data file in .csv format");
       fileBrowseButton.setToolTipText("Insert a data file in .csv format");
       
       filePath.setToolTipText("Insert a data file in .csv format");
       titleField.setToolTipText("Insert title for the model");
       subtitleField.setToolTipText("Insert subtitle for the model");
       randomLocationEffects.setToolTipText("Select the number of random location effects. Minimum value is 1");
      
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
        dataFileLabel = new javax.swing.JLabel();
        filePath = new javax.swing.JTextField();
        fileBrowseButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        titleField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        subtitleField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        randomLocationEffects = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        continuousRadio = new javax.swing.JRadioButton();
        dichotomousRadio = new javax.swing.JRadioButton();
        newModelSubmit = new javax.swing.JButton();
        newModelCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Model for Stage 1 Analysis");

        dataFileLabel.setText("Data File: ");

        filePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filePathActionPerformed(evt);
            }
        });

        fileBrowseButton.setText("Browse");
        fileBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileBrowseButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Title:");

        titleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titleFieldActionPerformed(evt);
            }
        });

        jLabel3.setText("Subtitle:");

        subtitleField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subtitleFieldActionPerformed(evt);
            }
        });

        jLabel4.setText("Random Location Effects:");

        randomLocationEffects.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        jLabel5.setText("Stage 2 Outcome Type:");

        buttonGroup1.add(continuousRadio);
        continuousRadio.setText("Continuous");
        continuousRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continuousRadioActionPerformed(evt);
            }
        });

        buttonGroup1.add(dichotomousRadio);
        dichotomousRadio.setText("Dichotomous/Ordinal");

        newModelSubmit.setText("Submit");
        newModelSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newModelSubmitActionPerformed(evt);
            }
        });

        newModelCancel.setText("Cancel");
        newModelCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newModelCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(newModelCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newModelSubmit)
                        .addGap(64, 64, 64))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(31, 31, 31)
                                .addComponent(continuousRadio)
                                .addGap(18, 18, 18)
                                .addComponent(dichotomousRadio))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(26, 26, 26)
                                .addComponent(randomLocationEffects, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dataFileLabel)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(filePath, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(fileBrowseButton))
                                    .addComponent(titleField)
                                    .addComponent(subtitleField))))
                        .addContainerGap(55, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dataFileLabel)
                    .addComponent(filePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileBrowseButton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(subtitleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(randomLocationEffects, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(continuousRadio)
                    .addComponent(dichotomousRadio))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newModelSubmit)
                    .addComponent(newModelCancel))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void filePathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filePathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filePathActionPerformed

    private void fileBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileBrowseButtonActionPerformed
        //JFileChooser fileChooser = new JFileChooser();
        //fileChooser.showOpenDialog(null);
        
        fileOpen();
        
        //Select file from the file object
        file = fileChooser.getSelectedFile();
        
        //get file path to display on the text box
        String fileName = file.getAbsolutePath();
        
        filePath.setText(fileName);
        
        //enable other buttons here:
        titleField.setEnabled(true);
        subtitleField.setEnabled(true);
        randomLocationEffects.setEnabled(true);
        continuousRadio.setEnabled(true);
        dichotomousRadio.setEnabled(true);
        randomLocationEffects.setEnabled(true);
        newModelSubmit.setEnabled(true);
        
    }//GEN-LAST:event_fileBrowseButtonActionPerformed

    private void subtitleFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subtitleFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_subtitleFieldActionPerformed

    private void continuousRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continuousRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_continuousRadioActionPerformed

    private void newModelSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newModelSubmitActionPerformed
        
        defFile = new DefinitionHelper(RLE, isOutcomeContinous());
        
        if (filePath.getText().toString().equals("")){
        
        JOptionPane.showMessageDialog(null, "Please upload a datafile to start your analysis", "Caution!", JOptionPane.INFORMATION_MESSAGE);
        } else {
        
        try {
            
            // Read file contents
            Scanner inputStream = new Scanner(file);
            
           
            // Read variable names from row 1
            String variableNames = inputStream.next();
            
            variableArray = variableNames.split(",");
            
           // save all variables in an array
            
            String[] varTemp = getVariableNames();
            
           defFile.setDataFilename(file.getAbsolutePath());
           
           defFile.setDataVariableCount(String.valueOf(variableArray.length));
           System.out.println(defFile.getDataVariableCount());
        
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewModel.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (Exception ex) {
                Logger.getLogger(NewModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        
       
        // Read random location effects from new Model
        RLE = (Integer) randomLocationEffects.getValue();
        
        System.out.println(String.valueOf(isOutcomeContinous()));
        
       // set Values in def helper
       defFile.setModelTitle(getTitle());
       System.out.println(defFile.getModelTitle());
       
       defFile.setModelSubtitle(getSubTitle());
       System.out.println(defFile.getModelSubtitle());
        
       mxr = new mixregGUI();
       mxr.isSubmitClicked();
       mxr.setVisible(true);
       //Update ID, stage one and stage two variable comboboxes
       mxr.updateComboBoxes();
      
       
        this.dispose();
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

    
    //Open data file
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
    private javax.swing.JRadioButton continuousRadio;
    private javax.swing.JLabel dataFileLabel;
    private javax.swing.JRadioButton dichotomousRadio;
    private javax.swing.JButton fileBrowseButton;
    private javax.swing.JTextField filePath;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JButton newModelCancel;
    private javax.swing.JButton newModelSubmit;
    private javax.swing.JSpinner randomLocationEffects;
    private javax.swing.JTextField subtitleField;
    private javax.swing.JTextField titleField;
    // End of variables declaration//GEN-END:variables



//get the number of random location effects    
public int getRLE(){
    return RLE;
    }


//get the variable names from the data file
public String[] getVariableNames(){

return variableArray;
}

//get the instance of the model mixReg declared in newModel
public mixregGUI getMixReg(){

return mxr;
}


//get title from the text box
public String getTitle(){

    String titleString = titleField.getText().toString();

    return titleString;
    
}

//get subtitle from the text box
public String getSubTitle(){

       String SubTitleString = subtitleField.getText().toString();

       return SubTitleString;
    
}



//check if the outcome type is selected as continuos or dichotomous
public boolean isOutcomeContinous(){
    
    boolean selection = true;
    
    if (continuousRadio.isSelected() == true){
    
    selection = true;
            }
    else if (dichotomousRadio.isSelected() == true){
    
    selection = false;
    }

    return selection;
}


public DefinitionHelper getDefFile(){

return defFile;
}

}
