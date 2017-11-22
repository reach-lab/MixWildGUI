/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package def_lib;

import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
import static mixregui.NewModel.defFile;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Eldin
 * 
 */
public class SuperUserMenu extends javax.swing.JFrame {

    
    DefinitionHelper defLib;
    JFileChooser fileChooser = new JFileChooser();
    int selectedModel;
    String defFilePath;
    
    /**
     * Creates new form SuperUserMenu
     */
    public SuperUserMenu() {
        initComponents();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        runButton = new javax.swing.JButton();
        MixRegLS_Mixreg = new javax.swing.JButton();
        MixregLS_Mixor = new javax.swing.JButton();
        MixRegMLS_Mixreg = new javax.swing.JButton();
        MixRegMLS_Mixor = new javax.swing.JButton();
        LoadButton = new javax.swing.JButton();
        ResetButton = new javax.swing.JButton();
        viewDefButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        csvToDat = new javax.swing.JButton();
        PrintMeanModel = new javax.swing.JButton();
        runMac = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        runButton.setText("Run");
        runButton.setEnabled(false);
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });

        MixRegLS_Mixreg.setText("MixRegLS Mixreg");
        MixRegLS_Mixreg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MixRegLS_MixregActionPerformed(evt);
            }
        });

        MixregLS_Mixor.setText("MixregLS Mixor");
        MixregLS_Mixor.setToolTipText("");
        MixregLS_Mixor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MixregLS_MixorActionPerformed(evt);
            }
        });

        MixRegMLS_Mixreg.setText("MixRegMLS Mixreg");
        MixRegMLS_Mixreg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MixRegMLS_MixregActionPerformed(evt);
            }
        });

        MixRegMLS_Mixor.setText("MixRegMLS Mixor");
        MixRegMLS_Mixor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MixRegMLS_MixorActionPerformed(evt);
            }
        });

        LoadButton.setText("Load Definition File");
        LoadButton.setToolTipText("");
        LoadButton.setEnabled(false);
        LoadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadButtonActionPerformed(evt);
            }
        });

        ResetButton.setText("Reset");
        ResetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetButtonActionPerformed(evt);
            }
        });

        viewDefButton.setText("View Definition File");
        viewDefButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewDefButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Superuser Mode");

        jLabel2.setText("Welcome to Superuser, a menu for advanced functions and UI override.");

        csvToDat.setText("Convert CSV to DAT");
        csvToDat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvToDatActionPerformed(evt);
            }
        });

        PrintMeanModel.setText("Print Mean Model");
        PrintMeanModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrintMeanModelActionPerformed(evt);
            }
        });

        runMac.setText("Run Mac Command");
        runMac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runMacActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(LoadButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(PrintMeanModel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(viewDefButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(runButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(ResetButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(csvToDat)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(MixregLS_Mixor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(MixRegLS_Mixreg, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(MixRegMLS_Mixreg, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                    .addComponent(MixRegMLS_Mixor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(runMac, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(csvToDat)
                    .addComponent(runMac))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MixRegLS_Mixreg, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MixRegMLS_Mixor, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MixregLS_Mixor, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MixRegMLS_Mixreg, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ResetButton)
                    .addComponent(PrintMeanModel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(runButton)
                    .addComponent(LoadButton)
                    .addComponent(viewDefButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
       // String absoluteDefFilePath = "C:/Users/Eldin/Desktop/";
        String absoluteJavaPath = System.getProperty( "user.dir" );
        String defFileName = executableModel(selectedModel);
        
        try {          
               copyExecutable(defFilePath, selectedModel);
               Process p=Runtime.getRuntime().exec("cmd /c dir && cd " + defFilePath + " && dir && "
                        + defFileName); // does it save it in the same directory
               
               Thread runCMD = new Thread(new Runnable(){
                   public void run() {
                       try{
                            InputStreamReader isr = new InputStreamReader(p.getInputStream());
                            BufferedReader br = new BufferedReader(isr);
                            String line=null;  // UI magic should run in here @adityaponnada
                            while ( (line = br.readLine()) != null)
                                System.out.println("MIXWILD:" + line);    
                            } catch (IOException ioe)
                              {
                                ioe.printStackTrace();  
                              }                       
                   }
                   
               });
               
               runCMD.start(); 
               
               int exitVal = p.waitFor();
               System.out.println("ExitValue: " + exitVal); // Non-zero is an error //@Eldin: should we add a condition that the p2 process will run only when the exit value is zero?
               Process p2=Runtime.getRuntime().exec("cmd /c dir && cd " + defFilePath + " && del /f " + defFileName); //What is p2 doing? just deleting right?

        }
        catch (Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed");
        }
    }//GEN-LAST:event_runButtonActionPerformed

    private void MixRegLS_MixregActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MixRegLS_MixregActionPerformed
        int selectedModel = DefinitionHelper.MIXREGLS_MIXREG_KEY;
        defLib = new DefinitionHelper(1,false);
        runButton.setEnabled(true);
        MixRegLS_Mixreg.setEnabled(false);
        MixregLS_Mixor.setEnabled(false);
        MixRegMLS_Mixreg.setEnabled(false);
        MixRegMLS_Mixor.setEnabled(false);
        LoadButton.setEnabled(true);
    }//GEN-LAST:event_MixRegLS_MixregActionPerformed

    private void MixRegMLS_MixorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MixRegMLS_MixorActionPerformed
        int selectedModel = DefinitionHelper.MIXREGMLS_MIXOR_KEY;
        defLib = new DefinitionHelper(3,true);
        runButton.setEnabled(true);
        MixRegLS_Mixreg.setEnabled(false);
        MixregLS_Mixor.setEnabled(false);
        MixRegMLS_Mixreg.setEnabled(false);
        MixRegMLS_Mixor.setEnabled(false);
        LoadButton.setEnabled(true);
        
    }//GEN-LAST:event_MixRegMLS_MixorActionPerformed

    private void MixregLS_MixorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MixregLS_MixorActionPerformed
        int selectedModel = DefinitionHelper.MIXREGLS_MIXOR_KEY;
        defLib = new DefinitionHelper(1,true);
        runButton.setEnabled(true);
        MixRegLS_Mixreg.setEnabled(false);
        MixregLS_Mixor.setEnabled(false);
        MixRegMLS_Mixreg.setEnabled(false);
        MixRegMLS_Mixor.setEnabled(false);
        LoadButton.setEnabled(true);
 
    }//GEN-LAST:event_MixregLS_MixorActionPerformed

    private void MixRegMLS_MixregActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MixRegMLS_MixregActionPerformed
        int selectedModel = DefinitionHelper.MIXREGMLS_MIXREG_KEY;
        defLib = new DefinitionHelper(3,false);
        runButton.setEnabled(true);
        MixRegLS_Mixreg.setEnabled(false);
        MixregLS_Mixor.setEnabled(false);
        MixRegMLS_Mixreg.setEnabled(false);
        MixRegMLS_Mixor.setEnabled(false);
        LoadButton.setEnabled(true);

    }//GEN-LAST:event_MixRegMLS_MixregActionPerformed

    private void LoadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadButtonActionPerformed
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Data files","def");
        
        fileChooser.setFileFilter(filter);
        
        fileOpen();
        
        //Select file from the file object
        File file = fileChooser.getSelectedFile();
        List<String> varnames = new ArrayList<String>(Arrays.asList(new String[]{"test","test","test"}));
        try {
            defLib.readDefinitionFile(file, varnames);
            JFrame myFrame = new JFrame("JEditorPane Test");
            myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            myFrame.setSize(300,200);

            JEditorPane myPane = new JEditorPane();
            myPane.setContentType("text/plain");
            myPane.setText(String.join("\n",defLib.buildStageOneDefinitonList()).replace("[", "").replace("]", ""));

            myFrame.setContentPane(myPane);
            myFrame.setVisible(true); 
            Document defDoc = myPane.getDocument();
            int length = defDoc.getLength();
            File newDefFile = new File("tester");
            OutputStream os = new BufferedOutputStream(
              new FileOutputStream(newDefFile + ".def"));
            Writer w = new OutputStreamWriter(os);
            myPane.write(w);
            w.close();
            //.
        }
        catch (FileNotFoundException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        catch (IOException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
            ex.printStackTrace();
        }
        
    }//GEN-LAST:event_LoadButtonActionPerformed

    private void ResetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetButtonActionPerformed
        defLib = null;
        runButton.setEnabled(false);
        MixRegLS_Mixreg.setEnabled(true);
        MixregLS_Mixor.setEnabled(true);
        MixRegMLS_Mixreg.setEnabled(true);
        MixRegMLS_Mixor.setEnabled(true);
        LoadButton.setEnabled(false);
    }//GEN-LAST:event_ResetButtonActionPerformed

    private void viewDefButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewDefButtonActionPerformed
        try{
        JFrame myFrame = new JFrame("JEditorPane Test");
            myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            myFrame.setSize(300,200);

            JEditorPane myPane = new JEditorPane();
            myPane.setContentType("text/plain");
            try{
            myPane.setText(String.join("\n",defLib.debugStageOneDefinitonList()).replace("[", "").replace("]", ""));
            }
            catch(Exception e){
            myPane.setText(String.join("\n",defFile.debugStageOneDefinitonList()).replace("[", "").replace("]", ""));
            }
            myFrame.setContentPane(myPane);
            myFrame.setVisible(true); 
            Document defDoc = myPane.getDocument();
            int length = defDoc.getLength();
            File newDefFile = new File("tester");
            OutputStream os = new BufferedOutputStream(
              new FileOutputStream(newDefFile + ".def"));
            Writer w = new OutputStreamWriter(os);
            myPane.write(w);
            w.close();
        }
        catch(Exception exception){
            exception.printStackTrace();
        }
    }//GEN-LAST:event_viewDefButtonActionPerformed

    private void csvToDatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvToDatActionPerformed
       FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files","csv");
        
        fileChooser.setFileFilter(filter);
        
        fileOpen();
        
        //Select file from the file object
        File file = fileChooser.getSelectedFile();
        try {
            DefinitionHelper.csvToDatConverter(file);
            JOptionPane.showMessageDialog(null, "Success!");
            
        } catch (IOException ex) {
            Logger.getLogger(SuperUserMenu.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Something went wrong, please try again. Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_csvToDatActionPerformed

    private void PrintMeanModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrintMeanModelActionPerformed
        ModelBuilder newMeanModel = new ModelBuilder(defFile);
        JOptionPane.showMessageDialog(null, newMeanModel.meanEquation());
    }//GEN-LAST:event_PrintMeanModelActionPerformed

    private void runMacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runMacActionPerformed
        try {                                       
            System.out.print("ELDIN: In CHMOD conversion");
            System.out.print("ELDIN: Binary is:" + "mixregls_random_mixor");
            /*Process p=Runtime.getRuntime().exec("/bin/bash -c \"cd " + "/Users/eldin/Downloads/2stage_exes_and_docs-7/" +
                    " ; chmod u+x mix_random ; chmod u+x mixreg ; chmod u+x repeat_mixreg" +
                    " ; chmod u+x mixor ; chmod u+x repeat_mixor" +
                    " ; chmod u+x " + "mixregls_random_mixor" + "\"");*/
            String[] commands = {"echo Hello"};
            for(String command : commands){
                Process p=Runtime.getRuntime().exec(command);
                try {
                    p.waitFor();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line=reader.readLine();
                    while(line!=null)
                    {
                        System.out.println(line);
                        line=reader.readLine();
                    }
                    } catch (InterruptedException ex) {
                    Logger.getLogger(DefinitionHelper.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (IOException ex) {
                            Logger.getLogger(SuperUserMenu.class.getName()).log(Level.SEVERE, null, ex);
            } 
                    
    }//GEN-LAST:event_runMacActionPerformed

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
            java.util.logging.Logger.getLogger(SuperUserMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SuperUserMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SuperUserMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SuperUserMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SuperUserMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton LoadButton;
    private javax.swing.JButton MixRegLS_Mixreg;
    private javax.swing.JButton MixRegMLS_Mixor;
    private javax.swing.JButton MixRegMLS_Mixreg;
    private javax.swing.JButton MixregLS_Mixor;
    private javax.swing.JButton PrintMeanModel;
    private javax.swing.JButton ResetButton;
    private javax.swing.JButton csvToDat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton runButton;
    private javax.swing.JButton runMac;
    private javax.swing.JButton viewDefButton;
    // End of variables declaration//GEN-END:variables

    private void fileOpen() {
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            defFilePath = filePath.substring(0,filePath.lastIndexOf(File.separator)) + "/";
            System.out.println(file.getAbsolutePath());
        } else {
            System.out.println("File access cancelled by user.");
        }
    }
    
    private String executableModel(int modelSelection){
        switch(modelSelection){
            case DefinitionHelper.MIXREGLS_MIXREG_KEY:
                return "mixregls_random_mixreg.exe";
            case DefinitionHelper.MIXREGLS_MIXOR_KEY:
                return "mixregls_random_mixor.exe";
            case DefinitionHelper.MIXREGMLS_MIXREG_KEY:
                return "mixregmls_random_mixreg.exe";
            case DefinitionHelper.MIXREGMLS_MIXOR_KEY:
                return "mixregmls_random_mixor.exe";
           
            default:
                return "mixregls__random_mixreg.exe";
        }
    }
    
    private void copyExecutable(String absoluteDirectoryPath, int modelSelection) throws FileNotFoundException, IOException{
        String modelPath;
        String MIX_RANDOM = "resources/WindowsNew/mix_random.exe";
        String FIRST;
        String REPEAT;
        switch(modelSelection){
            case DefinitionHelper.MIXREGLS_MIXREG_KEY:
                modelPath = "resources/WindowsNew/mixregls_random_mixreg.exe";
                REPEAT = "resources/WindowsNew/repeat_mixreg.exe";
                FIRST = "resources/WindowsNew/mixreg.exe";
                break;
            case DefinitionHelper.MIXREGLS_MIXOR_KEY:
                modelPath = "resources/WindowsNew/mixregls_random_mixor.exe";
                REPEAT = "resources/WindowsNew/repeat_mixor.exe";
                FIRST = "resources/WindowsNew/mixor.exe";
                break;
            case DefinitionHelper.MIXREGMLS_MIXREG_KEY:
                modelPath = "resources/WindowsNew/mixregmls_random_mixreg.exe";
                REPEAT = "resources/WindowsNew/repeat_mixreg.exe";
                FIRST = "resources/WindowsNew/mixreg.exe";
                break;
            case DefinitionHelper.MIXREGMLS_MIXOR_KEY:
                modelPath = "resources/WindowsNew/mixregmls_random_mixor.exe";
                REPEAT = "resources/WindowsNew/repeat_mixor.exe";
                FIRST = "resources/WindowsNew/mixor.exe";
                break;
            default:
                modelPath = "resources/WindowsNew/mixregls_random_mixreg.exe";
                REPEAT = "resources/WindowsNew/repeat_mixreg.exe";
                FIRST = "resources/WindowsNew/mixreg.exe";
                break;
        }
        String[] exeArray = {modelPath, MIX_RANDOM, FIRST, REPEAT}; 
        for(String exe: exeArray){
            InputStream stream = getClass().getClassLoader().getResourceAsStream(exe);
            OutputStream outputStream = 
                    new FileOutputStream(new File(absoluteDirectoryPath + FilenameUtils.getName(exe)));

            int read;
            byte[] bytes = new byte[4096];

            while ((read = stream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, read);
            }
            stream.close();
            outputStream.close();
            }   
    }
}
