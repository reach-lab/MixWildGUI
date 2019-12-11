/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mixregui;

import javax.swing.DefaultListModel;
import static mixregui.mixregGUI.mxr;

/**
 *
 * @author eldin
 */
public class confirmReset extends javax.swing.JDialog {

    /**
     * Creates new form confirmReset
     */
    public confirmReset(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("Reset");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Are you sure you wish to reset your model?");

        jLabel2.setText("All results will no longer be accessible using the user interface.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(88, 88, 88))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(44, 44, 44))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mixregGUI.mxr.dispose();

                //Reset UI
                mixregGUI.variableArray = null;
                mixregGUI.mxr = null;
                mixregGUI.RLE = 0;
                mixregGUI.NoneVar = false;
                mixregGUI.outComeBoolean = false;
                mixregGUI.isRandomScale = false;
                mixregGUI.dataFileNameRef = null;
                mixregGUI.defFile = null;
                mixregGUI.modelBuilder = null;

                mixregGUI.IDpos = 0;
                mixregGUI.stageOnePos = 0;
                mixregGUI.stageTwoPos = 0;

                mixregGUI.outPutStageTwo = null;

                // Stage One Reset
                stageOneRegs.variableNamesList = null;
                stageOneRegs.levelOneList = null;
                stageOneRegs.levelTwoList = null;
                stageOneRegs.isSubmitClicked = false;

                // Stage Two Reset
                stageTwoRegs.stageTwoListModel = null;
                stageTwoRegs.stageTwoLevelTwo = null;
                stageTwoRegs.isStageTwoSubmitClicked = false;

                mixregGUI.mxr = new mixregGUI();
                mixregGUI.mxr.setVisible(true);
            }
        });
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(confirmReset.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(confirmReset.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(confirmReset.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(confirmReset.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                confirmReset dialog = new confirmReset(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables

    public static void resetUI() {
        mixregGUI.mxr.dispose();

        //Reset UI
        mixregGUI.variableArray = null;
        mixregGUI.mxr = null;
        mixregGUI.RLE = 0;
        mixregGUI.NoneVar = false;
        mixregGUI.outComeBoolean = false;
        mixregGUI.isRandomScale = false;
        mixregGUI.dataFileNameRef = null;
        mixregGUI.defFile = null;
        mixregGUI.modelBuilder = null;

        mixregGUI.IDpos = 0;
        mixregGUI.stageOnePos = 0;
        mixregGUI.stageTwoPos = 0;

        mixregGUI.outPutStageTwo = null;

        // Stage One Reset
        stageOneRegs.variableNamesList = null;
        stageOneRegs.levelOneList = null;
        stageOneRegs.levelTwoList = null;
        stageOneRegs.isSubmitClicked = false;

        // Stage Two Reset
        stageTwoRegs.stageTwoListModel = null;
        stageTwoRegs.stageTwoLevelTwo = null;
        stageTwoRegs.isStageTwoSubmitClicked = false;

        mixregGUI.mxr = new mixregGUI();
        mixregGUI.mxr.setVisible(true);
    }

}
