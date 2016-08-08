package fileLock.ui;

import java.awt.event.*;
import com.intellij.openapi.diff.impl.incrementalMerge.Change;
import fileLock.bo.ChangeList;
import fileLock.bo.CodeLine;

import java.awt.*;
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
    public CheckOutToForm() {
        initComponents();
        initData();
    }

    private void cmbCLsActionPerformed(ActionEvent e) {

    }

    private void cmbCLsItemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED){
            Object obj = e.getItem();
            if (obj instanceof ChangeList){
                ChangeList cl = (ChangeList)e.getItem();
                txtDesc.setText(cl.getCLDesc());
            }
            else if(obj.toString().equals("Define New")) {
                txtDesc.setText("");
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Bin Li
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        cmbCLs = new JComboBox();
        label2 = new JLabel();
        txtDesc = new JTextArea();
        btnCancel = new JButton();
        btnOk = new JButton();

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
                label2.setText("Description");

                //---- btnCancel ----
                btnCancel.setText("Cancel");

                //---- btnOk ----
                btnOk.setText("Ok");

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(label1, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cmbCLs, GroupLayout.PREFERRED_SIZE, 306, GroupLayout.PREFERRED_SIZE))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(label2, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtDesc, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnCancel)))
                            .addGap(0, 8, Short.MAX_VALUE))
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
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(label2)
                                    .addGap(0, 153, Short.MAX_VALUE))
                                .addComponent(txtDesc, GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(btnOk)
                                .addComponent(btnCancel)))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Bin Li
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JComboBox cmbCLs;
    private JLabel label2;
    private JTextArea txtDesc;
    private JButton btnCancel;
    private JButton btnOk;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private void initData(){
        CodeLine codeLine = CodeLine.getCurrentCodeLine();
        List<ChangeList> cls = codeLine.getAllChangeList();
        for(ChangeList cl : cls){
            cmbCLs.addItem(cl);
        }
        cmbCLs.addItem("Define New");
    }
}
