/*
 * Created by JFormDesigner on Thu Aug 04 12:32:41 CST 2016
 */

package fileLock.ui;

import fileLock.bo.ChangeList;
import fileLock.bo.CodeLine;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * @author Bin Li
 */
public class ViewAllChangeListForm extends JFrame {
    public ViewAllChangeListForm() {
        initComponents();
        initData();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Bin Li
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        rootNode = new DefaultMutableTreeNode("All CodeLines");
        clTree = new JTree(rootNode);
        label1 = new JLabel();
        panel1 = new JPanel();
        label2 = new JLabel();
        txtProPath = new JTextField();
        label3 = new JLabel();
        scrollPane2 = new JScrollPane();
        txtDesc = new JTextArea();
        label4 = new JLabel();
        scrollPane3 = new JScrollPane();
        txtFiles = new JTextArea();
        label5 = new JLabel();
        txtTime = new JTextField();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("All Change Lists");
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

                //======== scrollPane1 ========
                {

                    //---- clTree ----
                    //clTree.setRootVisible(false);
                    scrollPane1.setViewportView(clTree);
                }

                //---- label1 ----
                label1.setText("All change lists:");

                //======== panel1 ========
                {

                    //---- label2 ----
                    label2.setText("Project path:");

                    //---- txtProPath ----
                    txtProPath.setEditable(false);

                    //---- label3 ----
                    label3.setText("Description:");

                    //======== scrollPane2 ========
                    {

                        //---- txtDesc ----
                        txtDesc.setEditable(false);
                        scrollPane2.setViewportView(txtDesc);
                    }

                    //---- label4 ----
                    label4.setText("Files:");

                    //======== scrollPane3 ========
                    {

                        //---- txtFiles ----
                        txtFiles.setEditable(false);
                        scrollPane3.setViewportView(txtFiles);
                    }

                    //---- label5 ----
                    label5.setText("Create time:");

                    //---- txtTime ----
                    txtTime.setEditable(false);

                    GroupLayout panel1Layout = new GroupLayout(panel1);
                    panel1.setLayout(panel1Layout);
                    panel1Layout.setHorizontalGroup(
                        panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(label3)
                                        .addGap(18, 18, 18)
                                        .addComponent(scrollPane2))
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(label4, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(label2, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(panel1Layout.createParallelGroup()
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtProPath, GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE))
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addGap(14, 14, 14)
                                                .addComponent(scrollPane3, GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE))))
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(label5)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                                        .addComponent(txtTime, GroupLayout.PREFERRED_SIZE, 583, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
                    );
                    panel1Layout.setVerticalGroup(
                        panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addComponent(label2)
                                    .addComponent(txtProPath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(label4)
                                        .addGap(0, 67, Short.MAX_VALUE))
                                    .addComponent(scrollPane3, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label3))
                                .addGap(11, 11, 11)
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label5)
                                    .addComponent(txtTime, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                    );
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(label1)
                                    .addGap(0, 588, Short.MAX_VALUE))
                                .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
                                .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                            .addComponent(label1)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
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
    private JScrollPane scrollPane1;
    private DefaultMutableTreeNode rootNode;
    private JTree clTree;
    private JLabel label1;
    private JPanel panel1;
    private JLabel label2;
    private JTextField txtProPath;
    private JLabel label3;
    private JScrollPane scrollPane2;
    private JTextArea txtDesc;
    private JLabel label4;
    private JScrollPane scrollPane3;
    private JTextArea txtFiles;
    private JLabel label5;
    private JTextField txtTime;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private DefaultTreeModel m_model;
    private void initData(){
        CodeLine codeLine = CodeLine.getCurrentCodeLine();

        m_model = (DefaultTreeModel)clTree.getModel();
        int stIndex = 0;
        DefaultMutableTreeNode codeLineNode = new DefaultMutableTreeNode(codeLine.getProjectPath());
        //m_model.insertNodeInto(node, rootNode, stIndex);
        List<ChangeList> cls = codeLine.getAllChangeList();
        for (ChangeList cl : cls){
            DefaultMutableTreeNode clNode = new DefaultMutableTreeNode(cl.toString());
            for (String str : cl.getFiles()){
                DefaultMutableTreeNode clFileNode = new DefaultMutableTreeNode(str);
                clNode.add(clFileNode);
            }
            codeLineNode.add(clNode);
        }
        rootNode.add(codeLineNode);
        clTree.updateUI();
//        DefaultMutableTreeNode temp = new DefaultMutableTreeNode(clTree.getModel().getRoot());
//        temp.removeAllChildren();
    }
}
