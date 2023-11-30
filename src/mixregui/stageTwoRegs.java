/*
 * MixWild, a program to model subject-level slope and variance on continuous or ordinal outcomes
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
package mixregui;

import def_lib.MixLibrary;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import mixregui.SystemLogger;
import java.util.logging.Level;
//import static mixregui.stageOneRegs.varList;
//import static mixregui.stageOneRegs.variableNamesList;

/**
 * Use this class to add stage two regressors in the model
 *
 * @author adityaponnada
 */
public class stageTwoRegs extends javax.swing.JFrame {

    //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    stageOneRegs stageOne;
    static String[] variableNamesList_stageTwo;
    static DefaultListModel<String> stageTwoListModel;
    static DefaultListModel<String> stageTwoLevelOne;
    static DefaultListModel<String> stageTwoLevelTwo;
    static boolean isStageTwoSubmitClicked = false;
    final ImageIcon icon;

    /**
     * Creates new form stageTwoRegs
     */
    public stageTwoRegs() {
        initComponents();
//        this.setResizable(false);
//        if(mixregGUI.defFile.getStageTwoModelType() == MixLibrary.STAGE_TWO_MODEL_TYPE_SINGLE){
//            jLabel4.setVisible(false);
//            jScrollPane3.setVisible(false);
//            stageTwoLevelOneAddButton.setVisible(false);
//            stageTwoLevelOneRemoveButton.setVisible(false);
//        }
        //create list models

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        variableNamesList_stageTwo = mixregGUI.getVariableNames_stageTwo();
//        System.out.println("Preparing variables of size: " + variableNamesList_stageTwo.length);

        //newModel2 = new NewModel();
        icon = new ImageIcon(getClass().getResource("/resources/mixLogo.png"));

        if (!isStageTwoSubmitClicked) {
            stageTwoListModel = new DefaultListModel<String>();
            stageTwoLevelOne = new DefaultListModel<String>();
            stageTwoLevelTwo = new DefaultListModel<String>();

        } else {
            StageTwoAllVariables.setModel(stageTwoListModel);
            StageTwoLevelOneVariables.setModel(stageTwoLevelOne);
            StageTwoLevelTwoVariables.setModel(stageTwoLevelTwo);

        }

        StageTwoAllVariables.setToolTipText("List of all available variables");
        jLabel1.setToolTipText("List of all available variables");
        jLabel2.setToolTipText("Variables consistent within individuals, such as demographics");
        StageTwoLevelTwoVariables.setToolTipText("Variables consistent within individuals, such as demographics");

        stageTwoSubmitButton.setEnabled(false);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        stageTwoLevelTwoAddButton = new javax.swing.JButton();
        stageTwoLevelTwoRemoveButton = new javax.swing.JButton();
        stageTwoResetButton = new javax.swing.JButton();
        stageTwoSubmitButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        StageTwoAllVariables = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        StageTwoLevelTwoVariables = new javax.swing.JList<>();
        stageTwoCancel = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        StageTwoLevelOneVariables = new javax.swing.JList<>();
        jLabel4 = new javax.swing.JLabel();
        stageTwoLevelOneAddButton = new javax.swing.JButton();
        stageTwoLevelOneRemoveButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Add Stage 2 regressors");
        setMinimumSize(new java.awt.Dimension(600, 510));
        setPreferredSize(new java.awt.Dimension(600, 540));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("Variables");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("Level 2 (Time Invariant)");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 230, -1, -1));

        stageTwoLevelTwoAddButton.setText("Add");
        stageTwoLevelTwoAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageTwoLevelTwoAddButtonActionPerformed(evt);
            }
        });
        getContentPane().add(stageTwoLevelTwoAddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 310, 89, -1));

        stageTwoLevelTwoRemoveButton.setText("Remove");
        stageTwoLevelTwoRemoveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageTwoLevelTwoRemoveButtonActionPerformed(evt);
            }
        });
        getContentPane().add(stageTwoLevelTwoRemoveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 340, 89, -1));

        stageTwoResetButton.setText("Reset");
        stageTwoResetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageTwoResetButtonActionPerformed(evt);
            }
        });
        getContentPane().add(stageTwoResetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(365, 460, 96, -1));

        stageTwoSubmitButton.setText("Submit");
        stageTwoSubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageTwoSubmitButtonActionPerformed(evt);
            }
        });
        getContentPane().add(stageTwoSubmitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(465, 460, 96, -1));

        jScrollPane1.setViewportView(StageTwoAllVariables);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 55, 179, 372));

        jScrollPane2.setViewportView(StageTwoLevelTwoVariables);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 257, 220, 170));

        stageTwoCancel.setText("Cancel");
        stageTwoCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageTwoCancelActionPerformed(evt);
            }
        });
        getContentPane().add(stageTwoCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 460, 96, -1));

        jScrollPane3.setViewportView(StageTwoLevelOneVariables);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 60, 220, 160));

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setText("Level 1 (Time Variant)");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 30, -1, -1));

        stageTwoLevelOneAddButton.setText("Add");
        stageTwoLevelOneAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageTwoLevelOneAddButtonActionPerformed(evt);
            }
        });
        getContentPane().add(stageTwoLevelOneAddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 110, 89, -1));

        stageTwoLevelOneRemoveButton.setText("Remove");
        stageTwoLevelOneRemoveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageTwoLevelOneRemoveButtonActionPerformed(evt);
            }
        });
        getContentPane().add(stageTwoLevelOneRemoveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 140, 89, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/icon4 - Copy.png"))); // NOI18N
        jLabel5.setToolTipText("<html><pre>Please select additional covariates to include in the stage-two model.\nNote: If you wish to re-run the second stage model without running stage 1, you will only be able to remove regressors. <pre>");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 32, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void stageTwoSubmitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageTwoSubmitButtonActionPerformed
        //dispose closes the window
        //mixregGUI.
        SystemLogger.LOGGER.log(Level.FINE, "stageTwoSubmitButtonActionPerformed");
        isStageTwoSubmitClicked = true;

        //mixregGUIStageTwo = newModel2.getMixReg();
        // mixregGUIStageTwo.updateStageTwoGrid_version2(stageTwoLevelTwo);
        //mixregGUIStageTwo.setSele

        mixregGUI.mxr.updateStageTwoLevelTwoGrid(stageTwoLevelTwo);
        mixregGUI.mxr.updateStageTwoLevelOneGrid(stageTwoLevelOne);
        this.dispose();
    }//GEN-LAST:event_stageTwoSubmitButtonActionPerformed

    private void stageTwoLevelTwoAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageTwoLevelTwoAddButtonActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "stageTwoLevelTwoAddButtonActionPerformed");
        //Add items to the model and then copy it to the UI list
        if (!StageTwoAllVariables.isSelectionEmpty()) {

            stageTwoLevelTwo.addElement(StageTwoAllVariables.getSelectedValue());
            StageTwoLevelTwoVariables.setModel(stageTwoLevelTwo);
            stageTwoListModel.remove(StageTwoAllVariables.getSelectedIndex());

            for (int k = 0; k < stageTwoListModel.size(); k++) {
                System.out.println("VarList: " + String.valueOf(stageTwoListModel.getElementAt(k)));

            }
            stageTwoSubmitButton.setEnabled(true);

        } else {
            JOptionPane.showMessageDialog(null, "Please select a variable for stage two.", "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }


    }//GEN-LAST:event_stageTwoLevelTwoAddButtonActionPerformed

    private void stageTwoLevelTwoRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageTwoLevelTwoRemoveButtonActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "stageTwoLevelTwoRemoveButtonActionPerformed");
        //Remove an item from the model
        if (!StageTwoLevelTwoVariables.isSelectionEmpty()) {
            stageTwoSubmitButton.setEnabled(true);

            if (!stageTwoListModel.contains(StageTwoLevelTwoVariables.getSelectedValue())) {

                stageTwoListModel.addElement(StageTwoLevelTwoVariables.getSelectedValue());

            }

            stageTwoLevelTwo.remove(StageTwoLevelTwoVariables.getSelectedIndex());

        } else {
            JOptionPane.showMessageDialog(null, "Please select a variable from stage two.", "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);

        }

    }//GEN-LAST:event_stageTwoLevelTwoRemoveButtonActionPerformed

    private void stageTwoResetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageTwoResetButtonActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "stageTwoResetButtonActionPerformed");
        stageTwoLevelOne.clear();
        stageTwoLevelTwo.clear();
    }//GEN-LAST:event_stageTwoResetButtonActionPerformed

    private void stageTwoCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageTwoCancelActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "stageTwoCancelActionPerformed");        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_stageTwoCancelActionPerformed

    private void stageTwoLevelOneAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageTwoLevelOneAddButtonActionPerformed
        //Add items to the model and then copy it to the UI list
        SystemLogger.LOGGER.log(Level.FINE, "stageTwoLevelOneAddButtonActionPerformed");
        if (!StageTwoAllVariables.isSelectionEmpty()) {

            stageTwoLevelOne.addElement(StageTwoAllVariables.getSelectedValue());
            StageTwoLevelOneVariables.setModel(stageTwoLevelOne);
            stageTwoListModel.remove(StageTwoAllVariables.getSelectedIndex());

            stageTwoSubmitButton.setEnabled(true);

        } else {
            JOptionPane.showMessageDialog(null, "Please select a variable for stage two.", "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }
    }//GEN-LAST:event_stageTwoLevelOneAddButtonActionPerformed

    private void stageTwoLevelOneRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageTwoLevelOneRemoveButtonActionPerformed
        SystemLogger.LOGGER.log(Level.FINE, "stageTwoLevelOneRemoveButtonActionPerformed");
        //Remove an item from the model
        if (!StageTwoLevelOneVariables.isSelectionEmpty()) {
            stageTwoSubmitButton.setEnabled(true);

            if (!stageTwoListModel.contains(StageTwoLevelOneVariables.getSelectedValue())) {

                stageTwoListModel.addElement(StageTwoLevelOneVariables.getSelectedValue());

            }

            stageTwoLevelOne.remove(StageTwoLevelOneVariables.getSelectedIndex());

        } else {
            JOptionPane.showMessageDialog(null, "Please select a variable from stage two.", "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);

        }
    }//GEN-LAST:event_stageTwoLevelOneRemoveButtonActionPerformed

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
            java.util.logging.Logger.getLogger(stageTwoRegs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(stageTwoRegs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(stageTwoRegs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(stageTwoRegs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new stageTwoRegs().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> StageTwoAllVariables;
    private javax.swing.JList<String> StageTwoLevelOneVariables;
    private javax.swing.JList<String> StageTwoLevelTwoVariables;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton stageTwoCancel;
    private javax.swing.JButton stageTwoLevelOneAddButton;
    private javax.swing.JButton stageTwoLevelOneRemoveButton;
    private javax.swing.JButton stageTwoLevelTwoAddButton;
    private javax.swing.JButton stageTwoLevelTwoRemoveButton;
    private javax.swing.JButton stageTwoResetButton;
    private javax.swing.JButton stageTwoSubmitButton;
    // End of variables declaration//GEN-END:variables

    public void updateStageTwoVariables(DefaultListModel<String> defaultListModel) {

        //updates variables in stage two list (to add regressors)
        //Add model to stage two variables, to display items  
        StageTwoAllVariables.setModel(defaultListModel);

        StageTwoAllVariables.setSelectedIndex(2);
    }

    public void updateStageTwoWithoutStageOne() {
        int idIndex = mixregGUI.getIDFieldPosition(2);
        int stageTwoIndex = mixregGUI.getStageTwoDVFieldPosition();

        stageTwoListModel.removeAllElements();

        for (int j = 0; j < variableNamesList_stageTwo.length; j++) {
            if (j == idIndex || j == stageTwoIndex) {
                //do nothing               
            } else {
                stageTwoListModel.addElement(variableNamesList_stageTwo[j]);
            }
        }

        StageTwoAllVariables.setModel(stageTwoListModel);
        StageTwoAllVariables.setSelectedIndex(1);

    }

    public void updateStageTwoAgain() {

        int idIndex = mixregGUI.getIDFieldPosition(2);
        int stageTwoIndex = mixregGUI.getStageTwoDVFieldPosition();

        stageTwoListModel.removeAllElements();

        for (int j = 0; j < variableNamesList_stageTwo.length; j++) {
            if (j == idIndex || j == stageTwoIndex) {
                //do nothing               
            } else {
                stageTwoListModel.addElement(variableNamesList_stageTwo[j]);
            }
        }

        ListModel<String> stageTwoLevelOneListModel = (ListModel<String>) StageTwoLevelOneVariables.getModel();
        ListModel<String> stageTwoLevelTwoListModel = (ListModel<String>) StageTwoLevelTwoVariables.getModel();
        for (int i = 0; i < stageTwoLevelOneListModel.getSize(); i++) {
            Object item = stageTwoLevelOneListModel.getElementAt(i);
            stageTwoListModel.removeElement(item);
        }
        for (int i = 0; i < stageTwoLevelTwoListModel.getSize(); i++) {
            Object item = stageTwoLevelTwoListModel.getElementAt(i);
            stageTwoListModel.removeElement(item);
        }

        StageTwoAllVariables.setModel(stageTwoListModel);
        StageTwoAllVariables.setSelectedIndex(1);

    }

    public javax.swing.JList<String> getStageTwoAllVariables() {
        return StageTwoAllVariables;
    }

    public javax.swing.JList<String> getStageTwoLevelOneVariables() {
        return StageTwoLevelOneVariables;
    }

    public javax.swing.JList<String> getStageTwoLevelTwoVariables() {
        return StageTwoLevelTwoVariables;
    }

    public void setEnabledStageTwoSubmitButton(boolean turnon) {
        stageTwoSubmitButton.setEnabled(turnon);
    }

    public void setEnabledStageTwoLevelOneAddButton(boolean turnon) {
        stageTwoLevelOneAddButton.setEnabled(turnon);
    }

    public void setEnabledStageTwoLevelOneRemoveButton(boolean turnon) {
        stageTwoLevelOneRemoveButton.setEnabled(turnon);
    }

    public void setEnabledStageTwoLevelOneRegTitle(boolean turnon) {
        jLabel4.setEnabled(turnon);
    }

    public void setEnabledStageTwoLevelOneRegVariables(boolean turnon) {
        StageTwoLevelOneVariables.setEnabled(turnon);
    }
}
