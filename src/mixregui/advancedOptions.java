/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mixregui;

import def_lib.DefinitionHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

// Group the first three advanced options
// rename based on stage 1 column names (keep RLE in mind)

/**
 *
 * @author adityaponnada
 */
public class advancedOptions extends javax.swing.JFrame {

    DefinitionHelper defFile3;
    final ImageIcon icon;
    
    

    /**
     * Creates new form advancedOptions
     */
    public advancedOptions() {
        initComponents();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        icon = new ImageIcon(getClass().getResource("/resources/mixLogo.png"));
        meanSubmodelCheckBox.setToolTipText("Check mean submodel");
        BSVarianceCheckBox.setToolTipText("Check mean submodel");
        WSVarianceCheckBox.setToolTipText("Tool tip here");
        convergenceCriteria.setToolTipText("Tool tip here");
        quadriturePoints.setToolTipText("Tool tip here");
        adaptiveQuadritureCheckBox.setToolTipText("Tool tip here");
        maximumIterations.setToolTipText("Tool tip here");
        //standardizedCoeff.setToolTipText("Tool tip here");
        ridgeSpinner.setToolTipText("Tool tip here");
        centerRegressorsCheckBox.setToolTipText("Tool tip here");
        
        meanSubmodelCheckBox.setSelected(true);
        BSVarianceCheckBox.setSelected(true);
        WSVarianceCheckBox.setSelected(true);
        adaptiveQuadritureCheckBox.setSelected(true);
        
        if (NewModel.NoneVar == true) {
            
            resampleSpinner.setEnabled(false);
        
        } else {
        
            resampleSpinner.setEnabled(true);
        
        }
       // missingValueCode.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        meanSubmodelCheckBox = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        BSVarianceCheckBox = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        WSVarianceCheckBox = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        adaptiveQuadritureCheckBox = new javax.swing.JCheckBox();
        convergenceCriteria = new javax.swing.JSpinner();
        quadriturePoints = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        maximumIterations = new javax.swing.JSpinner();
        ridgeSpinner = new javax.swing.JSpinner();
        jLabel15 = new javax.swing.JLabel();
        centerRegressorsCheckBox = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        resampleSpinner = new javax.swing.JSpinner();
        jButton1 = new javax.swing.JButton();
        advancedOptions_resetButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        advancedOptionsCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Advanced Options ...");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Mean Intercept:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 7, -1, -1));

        meanSubmodelCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                meanSubmodelCheckBoxActionPerformed(evt);
            }
        });
        jPanel1.add(meanSubmodelCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(225, 7, -1, -1));

        jLabel2.setText("BS Variance Intercept:  ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 36, -1, -1));

        BSVarianceCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BSVarianceCheckBoxActionPerformed(evt);
            }
        });
        jPanel1.add(BSVarianceCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(225, 36, -1, -1));

        jLabel3.setText("WS Variance Intercept:  ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 65, -1, -1));

        WSVarianceCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WSVarianceCheckBoxActionPerformed(evt);
            }
        });
        jPanel1.add(WSVarianceCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(225, 65, -1, -1));

        jLabel4.setText("Convergence Criteria:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 94, -1, -1));

        jLabel5.setText("Quadriture Points:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 128, -1, -1));

        jLabel6.setText("Adaptive Quadriture:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 162, -1, -1));

        adaptiveQuadritureCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adaptiveQuadritureCheckBoxActionPerformed(evt);
            }
        });
        jPanel1.add(adaptiveQuadritureCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(225, 160, -1, -1));

        convergenceCriteria.setModel(new javax.swing.SpinnerNumberModel(0.001d, 0.0d, 1.0d, 0.001d));
        jPanel1.add(convergenceCriteria, new org.netbeans.lib.awtextra.AbsoluteConstraints(184, 94, 69, -1));

        quadriturePoints.setModel(new javax.swing.SpinnerNumberModel(11, 1, 255, 1));
        jPanel1.add(quadriturePoints, new org.netbeans.lib.awtextra.AbsoluteConstraints(184, 128, 69, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 21, 260, 201));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setText("Maximum Iterations:");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 12, -1, -1));

        jLabel11.setText("Ridge:");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        maximumIterations.setModel(new javax.swing.SpinnerNumberModel(100, 1, null, 1));
        jPanel2.add(maximumIterations, new org.netbeans.lib.awtextra.AbsoluteConstraints(182, 7, 69, -1));

        ridgeSpinner.setModel(new javax.swing.SpinnerNumberModel(0.15d, 0.0d, 1.0d, 0.01d));
        jPanel2.add(ridgeSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, 68, -1));

        jLabel15.setText("Standardize All Regressors?");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        centerRegressorsCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                centerRegressorsCheckBoxActionPerformed(evt);
            }
        });
        jPanel2.add(centerRegressorsCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, -1, -1));

        jLabel9.setText("Resample?");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        resampleSpinner.setModel(new javax.swing.SpinnerNumberModel(100, 1, 10000, 1));
        jPanel2.add(resampleSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 60, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(294, 21, 260, 201));

        jButton1.setText("Submit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 230, -1, -1));

        advancedOptions_resetButton.setText("Reset");
        advancedOptions_resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advancedOptions_resetButtonActionPerformed(evt);
            }
        });
        getContentPane().add(advancedOptions_resetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 230, 70, -1));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/mixLogo.png"))); // NOI18N
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 231, -1, 29));

        advancedOptionsCancel.setText("Cancel");
        advancedOptionsCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advancedOptionsCancelActionPerformed(evt);
            }
        });
        getContentPane().add(advancedOptionsCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 230, 70, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void meanSubmodelCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_meanSubmodelCheckBoxActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_meanSubmodelCheckBoxActionPerformed

    private void BSVarianceCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BSVarianceCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BSVarianceCheckBoxActionPerformed

    private void WSVarianceCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WSVarianceCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_WSVarianceCheckBoxActionPerformed

    private void adaptiveQuadritureCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adaptiveQuadritureCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_adaptiveQuadritureCheckBoxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //defFile3 = NewModel.defFile;
        int tryCount = 0;
        int catchCount = 0;

        //defFile
        try {
            NewModel.defFile.setAdvancedConvergence(String.valueOf(convergenceCriteria.getValue()));
            System.out.println("From defHelper | Convergence: " + NewModel.defFile.getAdvancedConvergence());
            tryCount = 1;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
            Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
            catchCount = 1;
        }

        //set quadriture points
        try {
            NewModel.defFile.setAdvancedQuadPoints(String.valueOf(quadriturePoints.getValue()));
            System.out.println("From defHelper | Quadriture Points: " + NewModel.defFile.getAdvancedQuadPoints());
            tryCount = 1;
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            NewModel.defFile.setAdvancedMaxIteration(String.valueOf(getMaximumIterations()));
            System.out.println("From defHelper | Maximum Iteraions: " + NewModel.defFile.getAdvancedMaxIteration());
            tryCount = 1;
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

       

        try {
            NewModel.defFile.setModelFixedInt(String.valueOf(isMeanSubModelChecked()));
            System.out.println("From defHelper | Mean SubModel Checked?: " + NewModel.defFile.getModelFixedInt());
            tryCount = 1;
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            NewModel.defFile.setModelRandomInt(String.valueOf(isBSVarianceChecked()));
            System.out.println("From defHelper | BS SubModel Checked?: " + NewModel.defFile.getModelRandomInt());
            tryCount = 1;
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            NewModel.defFile.setModelScaleInt(String.valueOf(isWSVarianceChecked()));
            System.out.println("From defHelper | WS SubModel Checked?: " + NewModel.defFile.getModelScaleInt());
            tryCount = 1;
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            NewModel.defFile.setAdvancedAdaptiveQuad(String.valueOf(isAdaptiveQuadritureChecked()));
            System.out.println("From defHelper | Adaptive Quadriture Checked?: " + NewModel.defFile.getAdvancedAdaptiveQuad());
            tryCount = 1;
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        //NewModel.defFile.setOutputPrefix("Output_" + getOutPutFileName());
        //System.out.println("From defHelper | Output file name: " + NewModel.defFile.getOutputPrefix());

        try {
            NewModel.defFile.setAdvancedRidge(String.valueOf(getRidge()));
            System.out.println("From defHelper | Ridge: " + NewModel.defFile.getAdvancedRidge());
            tryCount = 1;
        } catch (Exception ex) {
            catchCount = 1;
            Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        try {
            NewModel.defFile.setAdvancedCenterScale(String.valueOf(isCenterRegressorChecked()));
            System.out.println("From defHelper | Scale Regressor: " + NewModel.defFile.getAdvancedCenterScale());
            tryCount = 1;
        } catch (Exception ex) {
            Logger.getLogger(advancedOptions.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

        if (catchCount == 0) {
            //do nothing
            this.dispose();
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void centerRegressorsCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_centerRegressorsCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_centerRegressorsCheckBoxActionPerformed

    private void advancedOptions_resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advancedOptions_resetButtonActionPerformed
        // TODO add your handling code here:

        meanSubmodelCheckBox.setSelected(false);
        BSVarianceCheckBox.setSelected(false);
        WSVarianceCheckBox.setSelected(false);

        convergenceCriteria.setValue(0.001);
        quadriturePoints.setValue(11);

        adaptiveQuadritureCheckBox.setSelected(false);
        centerRegressorsCheckBox.setSelected(false);
        

        maximumIterations.setValue(100);
       // missingValuesCheckBox.setSelected(false);
        //standardizedCoeff.setSelected(false);
        ridgeSpinner.setValue(0.15);
        resampleSpinner.setValue(100);

    }//GEN-LAST:event_advancedOptions_resetButtonActionPerformed

    private void advancedOptionsCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advancedOptionsCancelActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_advancedOptionsCancelActionPerformed

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
            java.util.logging.Logger.getLogger(advancedOptions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(advancedOptions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(advancedOptions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(advancedOptions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new advancedOptions().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox BSVarianceCheckBox;
    private javax.swing.JCheckBox WSVarianceCheckBox;
    private javax.swing.JCheckBox adaptiveQuadritureCheckBox;
    private javax.swing.JButton advancedOptionsCancel;
    private javax.swing.JButton advancedOptions_resetButton;
    private javax.swing.JCheckBox centerRegressorsCheckBox;
    private javax.swing.JSpinner convergenceCriteria;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSpinner maximumIterations;
    private javax.swing.JCheckBox meanSubmodelCheckBox;
    private javax.swing.JSpinner quadriturePoints;
    private javax.swing.JSpinner resampleSpinner;
    private javax.swing.JSpinner ridgeSpinner;
    // End of variables declaration//GEN-END:variables

//check if mean sub model is checked in advanced options    
    public int isMeanSubModelChecked() {

        int checked = 0;

        if (meanSubmodelCheckBox.isSelected() == true) {
            checked = 1;
        } else {
            checked = 0;
        }

        return checked;
    }

// check if BS variance is checked
    public int isBSVarianceChecked() {

        int checked = 0;

        if (BSVarianceCheckBox.isSelected() == true) {
            checked = 1;
        } else {
            checked = 0;
        }

        return checked;
    }

// check if BS variance is checked
    public int isWSVarianceChecked() {

        int checked = 0;

        if (WSVarianceCheckBox.isSelected() == true) {
            checked = 1;
        } else {
            checked = 0;
        }

        return checked;
    }

// get the convergence criteria
    public Double getConvergenceCriteria() {

        return (Double) convergenceCriteria.getValue();

    }

// get Quadriture points
    public Double getQuadriturePoints() {

        return (Double) quadriturePoints.getValue();
    }

// check if adaptive quadriture is checked
    public int isAdaptiveQuadritureChecked() {
        int checked = 0;

        if (adaptiveQuadritureCheckBox.isSelected() == true) {
            checked = 0;
        } else {

            checked = 1;
        }

        return checked;
    }

// get the convergence criteria
    public Integer getMaximumIterations() {

        return (Integer) maximumIterations.getValue();

    }



//check if standardized coefficients is checked
   /* public int isStandardizedCoefChecked() {
        int checked = 0;

        if (standardizedCoeff.isSelected() == true) {
            checked = 1;
        } else {

            checked = 0;
        }

        return checked;
    }*/

// get the ridge value
    public Double getRidge() {

        return (Double) ridgeSpinner.getValue();

    }

    public int isCenterRegressorChecked() {
        int value = 0;

        if (centerRegressorsCheckBox.isSelected()) {
            value = 1;
        } else {
            value = 0;
        }

        return value;
    }

    public String getOutPutFileName() {

      /*  String outPut;

        outPut = outputTextField.getText().toString();

        return outPut;*/
      
      return "";
    }
    
//    public int isResamplingChecked() {
//
//        int checked = 0;
//
//        if (resampleCheckBox.isSelected() == true) {
//            checked = 1;
//        } else {
//            checked = 0;
//        }
//
//        return checked;
//    }
    
    public Integer getResamplingRate() {

        return (Integer) resampleSpinner.getValue();

    }

}
