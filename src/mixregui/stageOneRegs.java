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

import java.awt.Dimension;
import java.awt.List;
import java.awt.Toolkit;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

/**
 * Use this to add or remove stage 1 regressors into the model
 *
 * @author adityaponnada
 */
public class stageOneRegs extends javax.swing.JFrame {

    // declare required jFrame
    // NewModel newModel2;
    stageTwoRegs stageTwo;
    static String[] variableNamesList;
    public static DefaultListModel<String> varList;
    static DefaultListModel<String> levelOneList;
    static DefaultListModel<String> levelTwoList;
    public static boolean isSubmitClicked = false;
    final ImageIcon icon;

    /**
     * Creates new form stageOneRegs
     */
    public stageOneRegs() {
        initComponents();
        this.setResizable(false);

        //newModel2 = new NewModel();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        //get variable names from the data set
        //variableNamesList = newModel2.getVariableNames();
        variableNamesList = mixregGUI.getVariableNames();
        System.out.println("Preparing variables of size: " + variableNamesList.length);

        icon = new ImageIcon(getClass().getResource("/resources/mixLogo.png"));

        if (!isSubmitClicked) {
            varList = new DefaultListModel<String>();
            levelOneList = new DefaultListModel<String>();
            levelTwoList = new DefaultListModel<String>();

        } else {
            AllVariablesList.removeAll();
            AllVariablesList.setModel(varList);
            AllVariablesList.setSelectedIndex(0);
            StageOneLevelOneList.removeAll();
            StageOneLevelOneList.setModel(levelOneList);
            StageOneLevelTwoList.removeAll();
            StageOneLevelTwoList.setModel(levelTwoList);
        }

        stageOneSubmitButton.setEnabled(false);
        // updateAllVariables();
        AllVariablesList.setToolTipText("List of all available variables detected in data");
        jLabel1.setToolTipText("List of all available variables detected in data");
        StageOneLevelOneList.setToolTipText("Variables that vary within-individuals, including time");
        jLabel2.setToolTipText("Variables that vary within-individuals, including time");
        StageOneLevelTwoList.setToolTipText("Variables consistent within individuals, such as demographics");
        jLabel3.setToolTipText("Variables consistent within individuals, such as demographics");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label1 = new java.awt.Label();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        stageOneSubmitButton = new javax.swing.JButton();
        stageOneResetButton = new javax.swing.JButton();
        removeLevelButton = new javax.swing.JButton();
        levelOneAddButton = new javax.swing.JButton();
        removeLevelTwoButton = new javax.swing.JButton();
        addLevelTwoButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        AllVariablesList = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        StageOneLevelOneList = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        StageOneLevelTwoList = new javax.swing.JList<>();
        stageOneCancel = new javax.swing.JButton();

        label1.setText("label1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Add Stage 1 Regressors");
        setMinimumSize(new java.awt.Dimension(640, 510));
        setPreferredSize(new java.awt.Dimension(680, 560));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("Variables");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 30, -1, -1));

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("Level-1 (Time Varying)");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, -1, -1));

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setText("Level-2 (Time Invariant)");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 255, -1, -1));

        stageOneSubmitButton.setText("Submit");
        stageOneSubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageOneSubmitButtonActionPerformed(evt);
            }
        });
        getContentPane().add(stageOneSubmitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(545, 470, 95, -1));

        stageOneResetButton.setText("Reset");
        stageOneResetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageOneResetButtonActionPerformed(evt);
            }
        });
        getContentPane().add(stageOneResetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(445, 470, 95, -1));

        removeLevelButton.setText("Remove");
        removeLevelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeLevelButtonActionPerformed(evt);
            }
        });
        getContentPane().add(removeLevelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(248, 140, 90, -1));

        levelOneAddButton.setText("Add");
        levelOneAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                levelOneAddButtonActionPerformed(evt);
            }
        });
        getContentPane().add(levelOneAddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(248, 110, 90, -1));

        removeLevelTwoButton.setText("Remove");
        removeLevelTwoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeLevelTwoButtonActionPerformed(evt);
            }
        });
        getContentPane().add(removeLevelTwoButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(248, 360, 90, -1));

        addLevelTwoButton.setText("Add");
        addLevelTwoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLevelTwoButtonActionPerformed(evt);
            }
        });
        getContentPane().add(addLevelTwoButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(248, 330, 90, -1));

        jScrollPane1.setViewportView(AllVariablesList);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 59, 198, 388));

        jScrollPane2.setViewportView(StageOneLevelOneList);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 59, 287, 164));

        jScrollPane3.setViewportView(StageOneLevelTwoList);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(353, 284, 287, 163));

        stageOneCancel.setText("Cancel");
        stageOneCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stageOneCancelActionPerformed(evt);
            }
        });
        getContentPane().add(stageOneCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(345, 470, 95, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void stageOneSubmitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageOneSubmitButtonActionPerformed

        for (int k = 0; k < varList.size(); k++) {
            System.out.println("VarList Submitted: " + String.valueOf(varList.getElementAt(k)));

        }

        stageTwo = new stageTwoRegs();
        isSubmitClicked = true;

        //get the instance of the mixReg model declared
        //mixregStageOne = mxr;
        //update regressors on stage one regressors window
        // mixregStageOne.updateRegressors(getSelectedLevelOneVars(), getSelectedLevelTwoVars());
        //mixregStageOne.updateLevelOneRegGrid(levelOneList);
        mixregGUI.mxr.updateStageOneLevelOneGrid(levelOneList);
        mixregGUI.mxr.updateStageOneLevelTwoGrid(levelTwoList);

        this.dispose();
    }//GEN-LAST:event_stageOneSubmitButtonActionPerformed

    private void levelOneAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_levelOneAddButtonActionPerformed

        if (!AllVariablesList.isSelectionEmpty()) {
            levelOneList.addElement(AllVariablesList.getSelectedValue());
            StageOneLevelOneList.setModel(levelOneList);
            //remove the variable once it is added to levelOne regressors
            varList.remove(AllVariablesList.getSelectedIndex());

            for (int k = 0; k < varList.size(); k++) {
                System.out.println("VarList: " + String.valueOf(varList.getElementAt(k)));

            }

            stageOneSubmitButton.setEnabled(true);
        } else {

            JOptionPane.showMessageDialog(null, "Please select a variable for level one.", "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);

        }


    }//GEN-LAST:event_levelOneAddButtonActionPerformed

    private void addLevelTwoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLevelTwoButtonActionPerformed

        if (!AllVariablesList.isSelectionEmpty()) {
            stageOneSubmitButton.setEnabled(true);

            levelTwoList.addElement(AllVariablesList.getSelectedValue());
            StageOneLevelTwoList.setModel(levelTwoList);

            varList.remove(AllVariablesList.getSelectedIndex());

        } else {
            JOptionPane.showMessageDialog(null, "Please select a variable for level two.", "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }


    }//GEN-LAST:event_addLevelTwoButtonActionPerformed

    private void removeLevelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeLevelButtonActionPerformed

        if (!StageOneLevelOneList.isSelectionEmpty()) {

            stageOneSubmitButton.setEnabled(true);

            //add an if condition here
            if (!varList.contains(StageOneLevelOneList.getSelectedValue())) {

                varList.addElement(StageOneLevelOneList.getSelectedValue());

            }

            AllVariablesList.setModel(varList);

            levelOneList.remove(StageOneLevelOneList.getSelectedIndex());

        } else {
            JOptionPane.showMessageDialog(null, "Please select a variable from level one.", "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);

        }


    }//GEN-LAST:event_removeLevelButtonActionPerformed

    private void removeLevelTwoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeLevelTwoButtonActionPerformed

        if (!StageOneLevelTwoList.isSelectionEmpty()) {
            stageOneSubmitButton.setEnabled(true);

            if (!varList.contains(StageOneLevelTwoList.getSelectedValue())) {

                varList.addElement(StageOneLevelTwoList.getSelectedValue());

            }

            //varList.addElement(StageOneLevelTwoList.getSelectedValue());
            AllVariablesList.setModel(varList);

            levelTwoList.remove(StageOneLevelTwoList.getSelectedIndex());
        } else {

            JOptionPane.showMessageDialog(null, "Please select a variable from level two.", "Caution!", JOptionPane.INFORMATION_MESSAGE, icon);
        }

    }//GEN-LAST:event_removeLevelTwoButtonActionPerformed

    private void stageOneResetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageOneResetButtonActionPerformed

        //clear lists on reset
        updateAllVariables();

        levelOneList.clear();
        levelTwoList.clear();
    }//GEN-LAST:event_stageOneResetButtonActionPerformed

    private void stageOneCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stageOneCancelActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_stageOneCancelActionPerformed

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
            java.util.logging.Logger.getLogger(stageOneRegs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(stageOneRegs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(stageOneRegs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(stageOneRegs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new stageOneRegs().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> AllVariablesList;
    private javax.swing.JList<String> StageOneLevelOneList;
    private javax.swing.JList<String> StageOneLevelTwoList;
    private javax.swing.JButton addLevelTwoButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private java.awt.Label label1;
    private javax.swing.JButton levelOneAddButton;
    private javax.swing.JButton removeLevelButton;
    private javax.swing.JButton removeLevelTwoButton;
    private javax.swing.JButton stageOneCancel;
    private javax.swing.JButton stageOneResetButton;
    private javax.swing.JButton stageOneSubmitButton;
    // End of variables declaration//GEN-END:variables

    public void updateAllVariables() {

        // mixregGUI.g
        int idIndex = mixregGUI.getIDFieldPosition();
        System.out.println("STAGE ONE | THE ID IS: " + String.valueOf(idIndex));
        int stageOneIndex = mixregGUI.getStageOneDVFieldPosition();
        System.out.println("STAGE ONE | THE OUTCOME IS: " + String.valueOf(stageOneIndex));

        varList.removeAllElements();

        for (int j = 0; j < variableNamesList.length; j++) {
            if (j == idIndex || j == stageOneIndex) {
                //do nothing               
            } else {
                varList.addElement(variableNamesList[j]);
            }
        }

        AllVariablesList.setModel(varList);
        AllVariablesList.setSelectedIndex(0);
    }

    public void updateStageOneAgain() {
        int idIndex = mixregGUI.getIDFieldPosition();
        int stageOneIndex = mixregGUI.getStageOneDVFieldPosition();

        varList.removeAllElements();

        for (int j = 0; j < variableNamesList.length; j++) {
            if (j == idIndex || j == stageOneIndex) {
                //do nothing               
            } else {
                varList.addElement(variableNamesList[j]);
            }
        }

        ListModel<String> stageOneLevelOneListModel = (ListModel<String>) getStageOneLevelOneList().getModel();
        ListModel<String> stageOneLevelTwoListModel = (ListModel<String>) getStageOneLevelTwoList().getModel();
        for (int i = 0; i < stageOneLevelOneListModel.getSize(); i++) {
            Object item = stageOneLevelOneListModel.getElementAt(i);
            varList.removeElement(item);
        }
        for (int i = 0; i < stageOneLevelTwoListModel.getSize(); i++) {
            Object item = stageOneLevelTwoListModel.getElementAt(i);
            varList.removeElement(item);
        }

        AllVariablesList.setModel(varList);
        AllVariablesList.setSelectedIndex(0);

    }

    public DefaultListModel<String> getListModel() {

        System.out.println("inside getListModel()");

        return varList;
    }

    public DefaultComboBoxModel<String> getSelectedLevelOneVars() {

        DefaultComboBoxModel<String> levelOneCombo = new DefaultComboBoxModel();

        for (int j = 0; j < levelOneList.getSize(); j++) {

            levelOneCombo.addElement(levelOneList.getElementAt(j));

        }

        return levelOneCombo;

    }

    public DefaultComboBoxModel<String> getSelectedLevelTwoVars() {

        DefaultComboBoxModel<String> levelTwoCombo = new DefaultComboBoxModel();

        for (int j = 0; j < levelTwoList.getSize(); j++) {

            levelTwoCombo.addElement(levelTwoList.getElementAt(j));

        }
        return levelTwoCombo;

    }

    public javax.swing.JList<String> getAllVariablesList() {
        return AllVariablesList;
    }

    public javax.swing.JList<String> getStageOneLevelOneList() {
        return StageOneLevelOneList;
    }

    public javax.swing.JList<String> getStageOneLevelTwoList() {
        return StageOneLevelTwoList;
    }

    public void getEnabledStageOneSubmitButton(boolean turnon) {
        stageOneSubmitButton.setEnabled(turnon);
    }
}
