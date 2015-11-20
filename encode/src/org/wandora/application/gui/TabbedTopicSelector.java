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
 * TabbedTopicSelector.java
 *
 * Created on 17. helmikuuta 2006, 13:09
 */

package org.wandora.application.gui;




import org.wandora.application.gui.search.SelectTopicPanel;
import org.wandora.application.gui.search.SearchPanel;
import org.wandora.application.gui.simple.*;
import org.wandora.topicmap.Topic;
import org.wandora.utils.swing.GuiTools;
import java.awt.*;
import java.util.*;



/**
 *
 * @author  olli
 */
public class TabbedTopicSelector extends javax.swing.JPanel implements TopicSelector {
    
    private ArrayList<TopicSelector> selectors;
    private boolean wasCancelled=true;
    private boolean cleared=false;
    
    
    /** Creates new form TabbedTopicSelector */
    public TabbedTopicSelector() {
        selectors=new ArrayList<TopicSelector>();
        initComponents();
        clearButton.setVisible(false);
    }
    
    public void insertTab(TopicSelector selector, int index){
        selectors.add(index, selector);
        tabbedPane.insertTab(selector.getSelectorName(),null,selector.getPanel(),null,index);
    }
    
    public void addTab(TopicSelector selector){
        selectors.add(selector);
        tabbedPane.addTab(selector.getSelectorName(),selector.getPanel());
    }

    @Override
    public Topic[] getSelectedTopics() {
        if(cleared) return new Topic[0];
        TopicSelector selector=selectors.get(tabbedPane.getSelectedIndex());
        return selector.getSelectedTopics();
    }

    @Override
    public Topic getSelectedTopic() {
        if(cleared) return null;
        TopicSelector selector=selectors.get(tabbedPane.getSelectedIndex());
        return selector.getSelectedTopic();
    }

    @Override
    public Component getPanel() {
        return this;
    }
    
    @Override
    public String getSelectorName(){
        return "Tabbed selector";
    }
    
    public void setClearVisible(boolean b){
        clearButton.setVisible(b);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tabbedPane = new SimpleTabbedPane();
        buttonPanel = new javax.swing.JPanel();
        rememberCheckBox = new SimpleCheckBox();
        jPanel1 = new javax.swing.JPanel();
        clearButton = new SimpleButton();
        selectButton = new SimpleButton();
        cancelButton = new SimpleButton();

        setLayout(new java.awt.GridBagLayout());

        tabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabbedPaneMouseReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(tabbedPane, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        rememberCheckBox.setText("Remember");
        buttonPanel.add(rememberCheckBox, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        buttonPanel.add(jPanel1, gridBagConstraints);

        clearButton.setLabel("Select none");
        clearButton.setMargin(new java.awt.Insets(2, 1, 2, 1));
        clearButton.setMaximumSize(new java.awt.Dimension(80, 23));
        clearButton.setMinimumSize(new java.awt.Dimension(80, 23));
        clearButton.setPreferredSize(new java.awt.Dimension(80, 23));
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        buttonPanel.add(clearButton, gridBagConstraints);

        selectButton.setText("Select");
        selectButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        selectButton.setMaximumSize(new java.awt.Dimension(70, 23));
        selectButton.setMinimumSize(new java.awt.Dimension(70, 23));
        selectButton.setPreferredSize(new java.awt.Dimension(70, 23));
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        buttonPanel.add(selectButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cancelButton.setMaximumSize(new java.awt.Dimension(70, 23));
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(buttonPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        wasCancelled=false;
        cleared=true;
        java.awt.Window w=GuiTools.getWindow(this);
        if(w!=null) w.setVisible(false);
    }//GEN-LAST:event_clearButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        wasCancelled=true;
        cleared=false;
        java.awt.Window w=GuiTools.getWindow(this);
        if(w!=null) w.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        wasCancelled=false;
        java.awt.Window w=GuiTools.getWindow(this);
        if(w!=null) w.setVisible(false);        
    }//GEN-LAST:event_selectButtonActionPerformed

    private void tabbedPaneMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabbedPaneMouseReleased
        Component currentTab = tabbedPane.getSelectedComponent();
        if(currentTab != null) {
            if(currentTab instanceof SearchPanel) {
                ((SearchPanel) currentTab).requestSearchFieldFocus();
            }
            else if(currentTab instanceof SelectTopicPanel) {
                ((SelectTopicPanel) currentTab).requestSearchFieldFocus();
            }
        }
    }//GEN-LAST:event_tabbedPaneMouseReleased
    
    public boolean wasCancelled(){
        return wasCancelled;
    }
    
    @Override
    public void init(){
        // tabbedPane.setSelectedIndex(0);
        for(TopicSelector s : selectors) s.init();
    }
    
    @Override
    public void cleanup(){
        for(TopicSelector s : selectors) s.cleanup();        
    }
    
    
    public boolean remember() {
        return rememberCheckBox.isSelected();
    }
    
    public void setRemember(boolean remember) {
        rememberCheckBox.setSelected(remember);
    }
    
    public Component getSelectedSelector() {
        return tabbedPane.getSelectedComponent();
    }
    
    public void setSelectedSelector(Component s) {
        tabbedPane.setSelectedComponent(s);
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JCheckBox rememberCheckBox;
    private javax.swing.JButton selectButton;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
    
}
