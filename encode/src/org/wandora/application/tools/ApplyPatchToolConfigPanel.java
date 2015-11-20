/*
 * WANDORA
 * Knowledge Extraction, Management, and Publishing Application
 * http://wandora.org
 * 
 * Copyright (C) 2004-2015 Wandora Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.wandora.application.tools;
import org.wandora.application.gui.filechooser.TopicMapFileChooser;
import org.wandora.*;
import org.wandora.topicmap.diff.*;
import org.wandora.topicmap.*;
import org.wandora.topicmap.layered.*;
import org.wandora.application.*;
import org.wandora.application.contexts.*;
import org.wandora.application.gui.*;
import org.wandora.application.gui.simple.*;

import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author  olli
 */
public class ApplyPatchToolConfigPanel extends javax.swing.JPanel {

    public static final int MODE_FILE=1;
    public static final int MODE_LAYER=2;
    public static final int MODE_PROJECT=3;
    public static final int MODE_NONE=99;
    
    protected Wandora admin;
    
    protected JDialog parentDialog;
    
    protected boolean cancelled=true;
    
    /** Creates new form ApplyPatchToolConfigPanel */
    public ApplyPatchToolConfigPanel(Wandora admin,JDialog parentDialog) {
        initComponents();
        this.admin=admin;
        this.parentDialog=parentDialog;
        List<Layer> layers=admin.getTopicMap().getLayers();
        for(Layer l : layers){
            layerComboBox1.addItem(l.getName());
        }
        mapGroupChanged();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        mapGroup = new javax.swing.ButtonGroup();
        map1ProjectButton = new SimpleRadioButton();
        jPanel1 = new javax.swing.JPanel();
        map1FileButton = new SimpleRadioButton();
        map1LayerButton = new SimpleRadioButton();
        layerComboBox1 = new SimpleComboBox();
        layerComboBox1.setEditable(false);
        fileTextField1 = new SimpleField();
        fileButton1 = new SimpleButton();
        jPanel2 = new javax.swing.JPanel();
        patchTextField = new SimpleField();
        patchButton = new SimpleButton();
        jLabel1 = new SimpleLabel();
        reverseCheckBox = new SimpleCheckBox();
        buttonsPanel = new javax.swing.JPanel();
        okButton = new SimpleButton();
        cancelButton = new SimpleButton();

        mapGroup.add(map1ProjectButton);
        map1ProjectButton.setText("Layer stack");
        map1ProjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                map1ProjectButtonActionPerformed(evt);
            }
        });

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Topic map", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, UIConstants.plainFont));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        mapGroup.add(map1FileButton);
        map1FileButton.setSelected(true);
        map1FileButton.setText("File");
        map1FileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                map1FileButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        jPanel1.add(map1FileButton, gridBagConstraints);

        mapGroup.add(map1LayerButton);
        map1LayerButton.setText("Layer");
        map1LayerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                map1LayerButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        jPanel1.add(map1LayerButton, gridBagConstraints);

        layerComboBox1.setPreferredSize(new java.awt.Dimension(29, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        jPanel1.add(layerComboBox1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        jPanel1.add(fileTextField1, gridBagConstraints);

        fileButton1.setText("Browse");
        fileButton1.setMargin(new java.awt.Insets(0, 7, 0, 7));
        fileButton1.setPreferredSize(new java.awt.Dimension(69, 20));
        fileButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        jPanel1.add(fileButton1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 3, 5);
        add(jPanel1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Patch", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, UIConstants.plainFont));
        jPanel2.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        jPanel2.add(patchTextField, gridBagConstraints);

        patchButton.setText("Browse");
        patchButton.setMargin(new java.awt.Insets(0, 7, 0, 7));
        patchButton.setPreferredSize(new java.awt.Dimension(69, 20));
        patchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patchButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        jPanel2.add(patchButton, gridBagConstraints);

        jLabel1.setText("Patch file");
        jLabel1.setPreferredSize(new java.awt.Dimension(76, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 3);
        jPanel2.add(jLabel1, gridBagConstraints);

        reverseCheckBox.setText("Reverse patch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        jPanel2.add(reverseCheckBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 3, 5);
        add(jPanel2, gridBagConstraints);

        buttonsPanel.setLayout(new java.awt.GridBagLayout());

        okButton.setText("Patch");
        okButton.setMargin(new java.awt.Insets(2, 5, 2, 5));
        okButton.setMaximumSize(new java.awt.Dimension(70, 23));
        okButton.setMinimumSize(new java.awt.Dimension(70, 23));
        okButton.setPreferredSize(new java.awt.Dimension(4970, 23));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(okButton, new java.awt.GridBagConstraints());

        cancelButton.setText("Cancel");
        cancelButton.setMargin(new java.awt.Insets(2, 5, 2, 5));
        cancelButton.setMaximumSize(new java.awt.Dimension(70, 23));
        cancelButton.setMinimumSize(new java.awt.Dimension(70, 23));
        cancelButton.setPreferredSize(new java.awt.Dimension(70, 23));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        buttonsPanel.add(cancelButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 5, 3);
        add(buttonsPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    public int getMapMode(){
        if(map1FileButton.isSelected()) return MODE_FILE;
        else if(map1LayerButton.isSelected()) return MODE_LAYER;
        else if(map1ProjectButton.isSelected()) return MODE_PROJECT;
        else return MODE_NONE;
    }
    public String getMapValue(){
        if(map1FileButton.isSelected()) return fileTextField1.getText();
        else if(map1LayerButton.isSelected()) return layerComboBox1.getSelectedItem().toString();
        else return null;
    }
    
    public String getPatchFile(){
        return patchTextField.getText();
    }
    public boolean getPatchReverse(){
        return reverseCheckBox.isSelected();
    }
    
    private void mapGroupChanged(){
        fileTextField1.setEnabled(map1FileButton.isSelected());
        fileButton1.setEnabled(map1FileButton.isSelected());
        layerComboBox1.setEnabled(map1LayerButton.isSelected());
    }

    private String browseTopicMap(){
        TopicMapFileChooser chooser;
        String currentDirectory = admin.options.get("current.directory");
        if(currentDirectory != null) chooser = new TopicMapFileChooser(currentDirectory);
        else chooser=new TopicMapFileChooser();
        if(chooser.open(admin, "Select")==TopicMapFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();        
            return file.getAbsolutePath();
        }
        else return null;
    }
    private String browseFile(){
        SimpleFileChooser chooser=UIConstants.getFileChooser();
        if(chooser.showDialog(admin, "Select")==SimpleFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();        
            return file.getAbsolutePath();
        }
        else return null;
    }

    public boolean wasCancelled(){
        return cancelled;
    }

    private void map1FileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_map1FileButtonActionPerformed
        mapGroupChanged();
    }//GEN-LAST:event_map1FileButtonActionPerformed

    private void map1LayerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_map1LayerButtonActionPerformed
        mapGroupChanged();
    }//GEN-LAST:event_map1LayerButtonActionPerformed

    private void map1ProjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_map1ProjectButtonActionPerformed
        mapGroupChanged();
    }//GEN-LAST:event_map1ProjectButtonActionPerformed

    private void fileButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileButton1ActionPerformed
        String f=browseTopicMap();
        if(f!=null) fileTextField1.setText(f);
    }//GEN-LAST:event_fileButton1ActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        cancelled=false;
        if(parentDialog!=null) parentDialog.setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        cancelled=true;
        if(parentDialog!=null) parentDialog.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void patchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patchButtonActionPerformed
        String f=browseFile();
        if(f!=null) patchTextField.setText(f);
}//GEN-LAST:event_patchButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton fileButton1;
    private javax.swing.JTextField fileTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox layerComboBox1;
    private javax.swing.JRadioButton map1FileButton;
    private javax.swing.JRadioButton map1LayerButton;
    private javax.swing.JRadioButton map1ProjectButton;
    private javax.swing.ButtonGroup mapGroup;
    private javax.swing.JButton okButton;
    private javax.swing.JButton patchButton;
    private javax.swing.JTextField patchTextField;
    private javax.swing.JCheckBox reverseCheckBox;
    // End of variables declaration//GEN-END:variables
    
}
