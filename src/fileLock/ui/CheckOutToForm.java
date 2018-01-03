package fileLock.ui;

import java.awt.event.*;
import com.intellij.openapi.diff.impl.incrementalMerge.Change;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vfs.VirtualFile;
import fileLock.bo.ChangeList;
import fileLock.bo.CodeLine;
import fileLock.config.CodeLineManager;
import fileLock.config.FileMapping;
import fileLock.config.Utils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Fri Jul 29 12:21:32 CST 2016
 */



/**
 * @author Bin Li
 */
public class CheckOutToForm extends JFrame {
    private VirtualFile m_selectedFile;
    private Map<String, List> m_clMap;
    public CheckOutToForm(VirtualFile file_) {
        m_selectedFile = file_;
        m_clMap = new HashMap<String, List>();
        initComponents();
        initData();
    }

    private void cmbCLsActionPerformed(ActionEvent e) {

    }

    private void cmbCLsItemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED){
            Object obj = e.getItem();
            if (obj instanceof ChangeList){
                ChangeList cl = (ChangeList)obj;
                lstFiles.setListData(cl.getFiles().toArray());
                txtDesc.setText(cl.getCLDesc());
            }
            else if(obj.toString().equals("Define New")){
                lstFiles.setListData(new String[]{});
                txtDesc.setText("");
            }
//            String clDesc = e.getItem().toString();
//            updateList(clDesc);
        }
    }

    private void updateList(String clDesc){
        if(!clDesc.equals("Define New")){
            List<String> clFiles = m_clMap.get(clDesc);
            if (clFiles != null){
                lstFiles.setListData(clFiles.toArray());
            }
            else{
                lstFiles.setListData(new String[]{});
            }
        }
        else {
            lstFiles.setListData(new String[]{});
            //lstFiles.removeAll();
        }
    }

    private void btnOkActionPerformed(ActionEvent e) {
        String path = m_selectedFile.getPath();
        Object obj = cmbCLs.getSelectedItem();
//        if(CodeLineManager.getCurrentCodeLine().getIsUnderSvn()) {
//
//            File file = new File(path);
//            if (!file.canWrite()) {
//                file.setWritable(true);
//                m_selectedFile.refresh(false, false);
//
//                String clDesc = obj.toString();
//                if (clDesc.equals("Define New")) {
//                    String ret = JOptionPane.showInputDialog(null, "Change list description:", "Please input new change list description", JOptionPane.QUESTION_MESSAGE);
//                    if (ret.equals("Ok")) {
//
//                    }
//                } else {
//
//                }
//            }
//        } else {
        boolean checkout = false;
        if (obj instanceof ChangeList){
            ChangeList cl = (ChangeList)obj;
            cl.checkoutFile(path);
            checkout = cl.save();
        } else if(obj.toString().equals("Define New")){
            ChangeList newCL = new ChangeList();
            newCL.initNew();
            newCL.setCLDesc(txtDesc.getText());
            newCL.setDate(Calendar.getInstance().getTimeInMillis());
            newCL.setCodeLine(CodeLineManager.getCurrentCodeLine().getCodeLineNo());
            newCL.checkoutFile(path);
            checkout = newCL.save();
        }
        if(checkout){
            File file = new File(path);
            file.setWritable(true);
            m_selectedFile.refresh(false, false);
        }
        //}




        this.dispose();
    }

    private void btnCancelActionPerformed(ActionEvent e) {
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - bill li
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        cmbCLs = new JComboBox();
        label2 = new JLabel();
        btnCancel = new JButton();
        btnOk = new JButton();
        scrollPane1 = new JScrollPane();
        lstFiles = new JList();
        label3 = new JLabel();
        scrollPane2 = new JScrollPane();
        txtDesc = new JTextArea();

        //======== this ========
        setTitle("Check out to...");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));

            // JFormDesigner evaluation mark
            dialogPane.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                    "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), dialogPane.getBorder())); dialogPane.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- label1 ----
                label1.setText("Change list");

                //---- cmbCLs ----
                cmbCLs.addActionListener(e -> cmbCLsActionPerformed(e));
                cmbCLs.addItemListener(e -> cmbCLsItemStateChanged(e));

                //---- label2 ----
                label2.setText("Files");

                //---- btnCancel ----
                btnCancel.setText("Cancel");
                btnCancel.addActionListener(e -> btnCancelActionPerformed(e));

                //---- btnOk ----
                btnOk.setText("Ok");
                btnOk.addActionListener(e -> btnOkActionPerformed(e));

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(lstFiles);
                }

                //---- label3 ----
                label3.setText("Description");

                //======== scrollPane2 ========
                {
                    scrollPane2.setViewportView(txtDesc);
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addGroup(contentPanelLayout.createParallelGroup()
                                        .addGroup(contentPanelLayout.createSequentialGroup()
                                            .addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(btnCancel)
                                            .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(contentPanelLayout.createSequentialGroup()
                                            .addGroup(contentPanelLayout.createParallelGroup()
                                                .addComponent(label1, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label2, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(contentPanelLayout.createParallelGroup()
                                                .addComponent(cmbCLs)
                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                    .addGap(0, 0, Short.MAX_VALUE)
                                                    .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 523, GroupLayout.PREFERRED_SIZE)))))
                                    .addGap(6, 6, 6))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(label3)
                                    .addGap(18, 18, 18)
                                    .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 520, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap())))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(label1)
                                .addComponent(cmbCLs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addComponent(label2)
                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addGap(0, 0, Short.MAX_VALUE)
                                    .addComponent(label3)
                                    .addGap(152, 152, 152))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCancel)
                                        .addComponent(btnOk))
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.WEST);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - bill li
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JComboBox cmbCLs;
    private JLabel label2;
    private JButton btnCancel;
    private JButton btnOk;
    private JScrollPane scrollPane1;
    private JList lstFiles;
    private JLabel label3;
    private JScrollPane scrollPane2;
    private JTextArea txtDesc;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private void initData(){
//        if(CodeLineManager.getCurrentCodeLine().getIsUnderSvn()){
//            initDataFromSvn();
//        } else {
            initDataFromLocal();
        //}
    }

    private void initDataFromLocal(){
        CodeLine codeLine = CodeLineManager.getCurrentCodeLine();
        List<ChangeList> cls = codeLine.getAllChangeList();
        for(ChangeList cl : cls){
            cmbCLs.addItem(cl);
        }
        cmbCLs.addItem("Define New");
        cmbCLs.addItemListener(e -> cmbCLsItemStateChanged(e));
    }

    private void initDataFromSvn(){
        String cmd = "cmd.exe /c D: && cd \"" + CodeLineManager.getCurrentCodeLine().getFileMap().getSVNTrunkPath() + "\" && svn st | find /V \"?\"";
        String clsStr = Utils.exeCmd(cmd);
        System.out.print("clsStr:" + clsStr);
        String[] lines = clsStr.split("\n");
        List<String> clFiles = new ArrayList<>();
        String clDesc = "(no changelist)";
        for(int i = 0; i < lines.length; i++){
            if (lines[i].length() == 0)
                continue;
            if (lines[i].startsWith("---")){
                int idx1 = lines[i].indexOf('\'');
                cmbCLs.addItem(clDesc);
                m_clMap.put(clDesc, clFiles);
                clDesc = lines[i].substring(idx1 + 1, lines[i].length() - 2);
                clFiles = new ArrayList<>();
            }
            else{
                String file = lines[i].substring(8, lines[i].length() - 2);
                clFiles.add(file);
            }
        }

        cmbCLs.addItem("Define New");
        cmbCLs.addItemListener(e -> cmbCLsItemStateChanged(e));
        updateList(cmbCLs.getSelectedItem().toString());
    }
}
