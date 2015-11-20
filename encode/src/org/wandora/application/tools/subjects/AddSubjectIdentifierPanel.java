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
 *
 */



package org.wandora.application.tools.subjects;

import java.io.File;
import javax.swing.JDialog;
import org.wandora.application.Wandora;
import org.wandora.application.gui.UIConstants;
import org.wandora.application.gui.simple.SimpleButton;
import org.wandora.application.gui.simple.SimpleField;
import org.wandora.application.gui.simple.SimpleFileChooser;
import org.wandora.application.gui.simple.SimpleLabel;
import org.wandora.application.gui.simple.SimpleURIField;

/**
 *
 * @author akivela
 */


public class AddSubjectIdentifierPanel extends javax.swing.JPanel {

    private JDialog dialog = null;
    private boolean wasAccepted = false;
    private Wandora wandora = null;
    
    
    /**
     * Creates new form AddSubjectIdentifierPanel
     */
    public AddSubjectIdentifierPanel() {
        initComponents();
    }

    
    
    public String open(Wandora wandora, String label, String title) {
        wasAccepted = false;
        this.wandora = wandora;
        dialog = new JDialog(wandora, true);
        
        dialog.setTitle(title);
        locatorLabel.setText(label);
        dialog.add(this);
        dialog.setSize(500, 170);
        wandora.centerWindow(dialog);
        dialog.setVisible(true);
        
        if(wasAccepted) return locatorField.getText();
        else return null;
    }
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        formPanel = new javax.swing.JPanel();
        locatorLabel = new SimpleLabel();
        locatorField = new SimpleURIField();
        buttonPanel = new javax.swing.JPanel();
        pickFileButton = new SimpleButton();
        buttonFillerPanel = new javax.swing.JPanel();
        okButton = new SimpleButton();
        cancelButton = new SimpleButton();

        setLayout(new java.awt.GridBagLayout());

        formPanel.setLayout(new java.awt.GridBagLayout());

        locatorLabel.setText("Enter subject ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 0);
        formPanel.add(locatorLabel, gridBagConstraints);

        locatorField.setMinimumSize(new java.awt.Dimension(6, 21));
        locatorField.setPreferredSize(new java.awt.Dimension(6, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        formPanel.add(locatorField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(formPanel, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        pickFileButton.setLabel("Select file");
        pickFileButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
        pickFileButton.setMinimumSize(new java.awt.Dimension(70, 23));
        pickFileButton.setPreferredSize(new java.awt.Dimension(70, 23));
        pickFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pickFileButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(pickFileButton, new java.awt.GridBagConstraints());

        buttonFillerPanel.setPreferredSize(new java.awt.Dimension(253, 10));

        javax.swing.GroupLayout buttonFillerPanelLayout = new javax.swing.GroupLayout(buttonFillerPanel);
        buttonFillerPanel.setLayout(buttonFillerPanelLayout);
        buttonFillerPanelLayout.setHorizontalGroup(
            buttonFillerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        buttonFillerPanelLayout.setVerticalGroup(
            buttonFillerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        buttonPanel.add(buttonFillerPanel, gridBagConstraints);

        okButton.setText("OK");
        okButton.setMargin(new java.awt.Insets(2, 8, 2, 8));
        okButton.setMinimumSize(new java.awt.Dimension(70, 23));
        okButton.setPreferredSize(new java.awt.Dimension(70, 23));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        buttonPanel.add(okButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.setMargin(new java.awt.Insets(2, 8, 2, 8));
        cancelButton.setMinimumSize(new java.awt.Dimension(70, 23));
        cancelButton.setPreferredSize(new java.awt.Dimension(70, 23));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(cancelButton, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(buttonPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        wasAccepted = true;
        if(dialog != null) dialog.setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        wasAccepted = false;
        if(dialog != null) dialog.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    
    private void pickFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pickFileButtonActionPerformed
        SimpleFileChooser chooser = UIConstants.getFileChooser();
        chooser.setDialogTitle("Select file");
        if(chooser.open(wandora, "Select")==SimpleFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            if(f != null) {
                locatorField.setText(f.toURI().toString());
            }
        }
    }//GEN-LAST:event_pickFileButtonActionPerformed

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonFillerPanel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel formPanel;
    private javax.swing.JTextField locatorField;
    private javax.swing.JLabel locatorLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JButton pickFileButton;
    // End of variables declaration//GEN-END:variables
}
