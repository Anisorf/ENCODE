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
 *
 */

package org.wandora.application.gui.topicpanels.dockingpanel;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.ListModel;
import org.wandora.application.Wandora;
import org.wandora.application.gui.simple.SimpleButton;
import org.wandora.application.gui.simple.SimpleCheckBox;
import org.wandora.application.gui.topicpanels.TopicPanel;

/**
 *
 * @author akivela
 */


public class SelectTopicPanelPanel extends javax.swing.JPanel {
    
    private JDialog myDialog = null;
    private ArrayList<TopicPanel> topicPanels = null;
    private Wandora wandora = null;
    private boolean wasAccepted = false;
    
    private static TopicPanel lastSelectedTopicPanel = null;
    private static boolean lastRememberSelection = false;
    
    
    /**
     * Creates new form SelectTopicPanelPanel
     */
    public SelectTopicPanelPanel() {
        initComponents();
    }

    
    
    
    public void openInDialog(ArrayList<TopicPanel> topicPanels, Wandora wandora) {
        this.topicPanels = topicPanels;
        this.wandora = wandora;
        
        int index = 0;
        int defaultIndex = -1;
        DefaultListModel topicPanelListModel = new DefaultListModel();
        for(TopicPanel tp : topicPanels) {
            topicPanelListModel.addElement(tp.getName()+" w "+tp.getTitle());
            if(lastSelectedTopicPanel != null && tp.equals(lastSelectedTopicPanel)) {
                defaultIndex = index;
            }
            index++;
        }
        
        topicPanelList.setModel(topicPanelListModel);
        topicPanelList.setSelectedIndex(defaultIndex);
        
        rememberCheckBox.setSelected(lastRememberSelection);
        
        wasAccepted = false;
        
        myDialog = new JDialog(wandora, true);
        myDialog.setTitle("Select topic panel");
        myDialog.add(this);
        myDialog.setSize(500, 250);
        wandora.centerWindow(myDialog);
        myDialog.setVisible(true);
    }
    
    
    
    public boolean wasAccepted() {
        return wasAccepted;
    }
    
    
    public TopicPanel getSelectedTopicPanel() {
        int i = topicPanelList.getSelectedIndex();
        TopicPanel selectedTopicPanel = null;
        if(i >= 0 && i < topicPanels.size()) {
            selectedTopicPanel = topicPanels.get(i);
            lastSelectedTopicPanel = selectedTopicPanel;
        }
        return selectedTopicPanel;
    }
    
    
    public boolean getRememberSelection() {
        lastRememberSelection = rememberCheckBox.isSelected();
        return rememberCheckBox.isSelected();
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

        jPanel1 = new javax.swing.JPanel();
        selectorsPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        topicPanelList = new javax.swing.JList();
        buttonPanel = new javax.swing.JPanel();
        rememberCheckBox = new SimpleCheckBox();
        fillerPanel = new javax.swing.JPanel();
        selectButton = new SimpleButton();
        cancelButton = new SimpleButton();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        selectorsPanel.setLayout(new java.awt.GridBagLayout());

        topicPanelList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        topicPanelList.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jScrollPane1.setViewportView(topicPanelList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        selectorsPanel.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(selectorsPanel, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        rememberCheckBox.setText("remember selection");
        buttonPanel.add(rememberCheckBox, new java.awt.GridBagConstraints());

        fillerPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        buttonPanel.add(fillerPanel, gridBagConstraints);

        selectButton.setText("Select");
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        buttonPanel.add(selectButton, gridBagConstraints);

        cancelButton.setText("Cancel");
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
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel1.add(buttonPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        wasAccepted = false;
        if(myDialog != null) {
            myDialog.setVisible(false);
        }
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        int i = topicPanelList.getSelectedIndex();
        if(i >= 0 && i < topicPanels.size()) {
            wasAccepted = true;
        }
        if(myDialog != null) {
            myDialog.setVisible(false);
        }
    }//GEN-LAST:event_selectButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel fillerPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox rememberCheckBox;
    private javax.swing.JButton selectButton;
    private javax.swing.JPanel selectorsPanel;
    private javax.swing.JList topicPanelList;
    // End of variables declaration//GEN-END:variables



    // -------------------------------------------------------------------------
    
    
    


}
