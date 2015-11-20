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

package org.wandora.application.gui.search;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import org.wandora.application.Wandora;
import org.wandora.application.gui.TopicSelector;
import org.wandora.application.gui.UIBox;
import org.wandora.application.gui.WandoraOptionPane;
import org.wandora.application.gui.simple.SimpleButton;
import org.wandora.application.gui.simple.SimpleCheckBox;
import org.wandora.application.gui.simple.SimpleComboBox;
import org.wandora.application.gui.simple.SimpleLabel;
import org.wandora.application.gui.simple.SimpleScrollPane;
import org.wandora.application.gui.simple.SimpleTextPane;
import org.wandora.application.gui.simple.SimpleTextPaneResizeable;
import org.wandora.application.gui.table.MixedTopicTable;
import org.wandora.topicmap.TMQLRunner;
import org.wandora.topicmap.Topic;
import org.wandora.topicmap.TopicMap;
import org.wandora.topicmap.TopicMapException;
import org.wandora.utils.Options;
import org.wandora.utils.Tuples;

/**
 *
 * @author akivela
 */


public class TMQLPanel extends javax.swing.JPanel implements TopicSelector {

    
    private Wandora wandora = null;
    private String TMQL_QUERY_OPTION_KEY = "tmqlQueries";
    private ArrayList<Tuples.T2<String,String>> storedTmqlQueries = new ArrayList<Tuples.T2<String,String>>();
    private MixedTopicTable resultsTable = null;
    private SimpleLabel message = null;
    
    /**
     * Creates new form TMQLPanel
     */
    public TMQLPanel() {
        wandora = Wandora.getWandora();
        initComponents();
        runtimeComboBox.setEditable(false);
        message = new SimpleLabel();
        message.setHorizontalAlignment(SimpleLabel.CENTER);
        message.setIcon(UIBox.getIcon("gui/icons/warn.png"));
        tmqlTextPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        clearResultsButton.setEnabled(false);
        ((TMQLTextPane) tmqlTextPane).setHorizontallyResizeable(false);
        readStoredTmqlQueries();
    }


    
    public void removeResultScrollPanesMouseListeners() {
        MouseWheelListener[] mouseWheelListeners = resultScrollPane.getMouseWheelListeners();
        for(MouseWheelListener listener : mouseWheelListeners) {
            resultScrollPane.removeMouseWheelListener(listener);
        }
    }
    
    
    
    private void readStoredTmqlQueries() {
        storedTmqlQueries = new ArrayList<Tuples.T2<String,String>>();
        if(wandora != null) {
            Options options = wandora.getOptions();
            if(options != null) {
                int queryCount = 0;
                String query = null;
                String queryName = options.get(TMQL_QUERY_OPTION_KEY+".query["+queryCount+"].name");
                while(queryName != null && queryName.length() > 0) {
                    query = options.get(TMQL_QUERY_OPTION_KEY+".query["+queryCount+"].query");
                    storedTmqlQueries.add( new Tuples.T2(queryName, query) );
                    queryCount++;
                    queryName = options.get(TMQL_QUERY_OPTION_KEY+".query["+queryCount+"].name");
                }
                updateTmqlComboBox();
            }
        }
    }


    private void writeTmqlQueries() {
        if(wandora != null) {
            Options options = wandora.getOptions();
            if(options != null) {
                options.removeAll(TMQL_QUERY_OPTION_KEY);
                int queryCount = 0;
                for( Tuples.T2<String,String> storedQuery : storedTmqlQueries ) {
                    if(storedQuery != null) {
                        options.put(TMQL_QUERY_OPTION_KEY+".query["+queryCount+"].name", storedQuery.e1);
                        options.put(TMQL_QUERY_OPTION_KEY+".query["+queryCount+"].query", storedQuery.e2);
                        queryCount++;
                    }
                }
            }
        }
    }


    public void updateTmqlComboBox() {
        tmqlComboBox.removeAllItems();
        String name = "";
        String query = "";
        for( Tuples.T2<String,String> storedQuery : storedTmqlQueries ) {
            if(storedQuery != null) {
                name = storedQuery.e1;
                query = storedQuery.e2;
                tmqlComboBox.addItem(name);
            }
        }
        tmqlComboBox.setSelectedItem(name);
        tmqlTextPane.setText(query);
    }
    
    public void addTmqlQuery(){
        String queryName = WandoraOptionPane.showInputDialog(wandora, "Give name for the tmql query?", "", "Name of the tmql query");
        if(queryName != null && queryName.length() > 0) {
            String query = tmqlTextPane.getText();
            storedTmqlQueries.add( new Tuples.T2(queryName, query) );
            writeTmqlQueries();
            updateTmqlComboBox();
        }        
    }
    public void deleteTmqlQuery(){
        int index = tmqlComboBox.getSelectedIndex();
        if(index < storedTmqlQueries.size() && index >= 0) {
            String name = storedTmqlQueries.get(index).e1;
            int a = WandoraOptionPane.showConfirmDialog(wandora, "Would you like to remove tmql query '"+name+"'?", "Delete tmql query?");
            if(a == WandoraOptionPane.YES_OPTION) {
                storedTmqlQueries.remove(index);
                writeTmqlQueries();
                updateTmqlComboBox();
            }
        }        
    }
    public void selectTmqlQuery(){
        int index = tmqlComboBox.getSelectedIndex();
        if(index < storedTmqlQueries.size() && index >= 0) {
            Tuples.T2<String,String> query = storedTmqlQueries.get(index);
            tmqlTextPane.setText(query.e2);
        }        
    }
    
    


    
    public void refresh() {
        if(resultsTable != null) {
            ((DefaultTableModel) resultsTable.getModel()).fireTableDataChanged();
        }
        tmqlResultPanel.revalidate();
        revalidate();
        repaint();
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

        tmqlPanel = new javax.swing.JPanel();
        selectQueryPanel1 = new javax.swing.JPanel();
        tmqlComboBox = new SimpleComboBox();
        addTmqlButton = new SimpleButton();
        delTmqlButton = new SimpleButton();
        tmqlScrollPane = new javax.swing.JScrollPane();
        tmqlTextPane = new TMQLTextPane();
        tmqlOptionPanel = new javax.swing.JPanel();
        runtimeLabel = new javax.swing.JLabel();
        runtimeComboBox = new SimpleComboBox();
        tmqlButtonPanel = new javax.swing.JPanel();
        runButton = new SimpleButton();
        clearResultsButton = new SimpleButton();
        tmqlResultContainerPanel = new javax.swing.JPanel();
        resultScrollPane = new SimpleScrollPane();
        tmqlResultPanel = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        tmqlPanel.setName(""); // NOI18N
        tmqlPanel.setLayout(new java.awt.GridBagLayout());

        selectQueryPanel1.setLayout(new java.awt.GridBagLayout());

        tmqlComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        tmqlComboBox.setPreferredSize(new java.awt.Dimension(57, 21));
        tmqlComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tmqlComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        selectQueryPanel1.add(tmqlComboBox, gridBagConstraints);

        addTmqlButton.setText("Add");
        addTmqlButton.setMargin(new java.awt.Insets(1, 4, 1, 4));
        addTmqlButton.setPreferredSize(new java.awt.Dimension(50, 21));
        addTmqlButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTmqlButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        selectQueryPanel1.add(addTmqlButton, gridBagConstraints);

        delTmqlButton.setText("Del");
        delTmqlButton.setMargin(new java.awt.Insets(1, 4, 1, 4));
        delTmqlButton.setPreferredSize(new java.awt.Dimension(50, 21));
        delTmqlButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delTmqlButtonActionPerformed(evt);
            }
        });
        selectQueryPanel1.add(delTmqlButton, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        tmqlPanel.add(selectQueryPanel1, gridBagConstraints);

        tmqlScrollPane.setMinimumSize(new java.awt.Dimension(23, 100));
        tmqlScrollPane.setPreferredSize(new java.awt.Dimension(2, 150));
        tmqlScrollPane.setViewportView(tmqlTextPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        tmqlPanel.add(tmqlScrollPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(tmqlPanel, gridBagConstraints);

        tmqlOptionPanel.setLayout(new java.awt.GridBagLayout());

        runtimeLabel.setText("Use runtime");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 4);
        tmqlOptionPanel.add(runtimeLabel, gridBagConstraints);

        runtimeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TMQL-2007" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8);
        tmqlOptionPanel.add(runtimeComboBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
        add(tmqlOptionPanel, gridBagConstraints);

        tmqlButtonPanel.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        tmqlButtonPanel.setMinimumSize(new java.awt.Dimension(91, 40));
        tmqlButtonPanel.setPreferredSize(new java.awt.Dimension(91, 40));
        tmqlButtonPanel.setLayout(new java.awt.GridBagLayout());

        runButton.setText("Run query");
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        tmqlButtonPanel.add(runButton, gridBagConstraints);

        clearResultsButton.setText("Clear results");
        clearResultsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearResultsButtonActionPerformed(evt);
            }
        });
        tmqlButtonPanel.add(clearResultsButton, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(tmqlButtonPanel, gridBagConstraints);

        tmqlResultContainerPanel.setLayout(new java.awt.BorderLayout());

        resultScrollPane.setBorder(null);

        tmqlResultPanel.setLayout(new java.awt.BorderLayout());
        resultScrollPane.setViewportView(tmqlResultPanel);

        tmqlResultContainerPanel.add(resultScrollPane, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(tmqlResultContainerPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void tmqlComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tmqlComboBoxActionPerformed
        if((evt.getModifiers() | ActionEvent.MOUSE_EVENT_MASK) != 0) {
            selectTmqlQuery();
        }
    }//GEN-LAST:event_tmqlComboBoxActionPerformed

    private void addTmqlButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTmqlButtonActionPerformed
        if((evt.getModifiers() | ActionEvent.MOUSE_EVENT_MASK) != 0) {
            addTmqlQuery();
        }
    }//GEN-LAST:event_addTmqlButtonActionPerformed

    private void delTmqlButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delTmqlButtonActionPerformed
        if((evt.getModifiers() | ActionEvent.MOUSE_EVENT_MASK) != 0) {
            deleteTmqlQuery();
        }
    }//GEN-LAST:event_delTmqlButtonActionPerformed

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
        try {
            tmqlResultPanel.removeAll();
            clearResultsButton.setEnabled(true);
            
            resultsTable = null;
            TopicMap topicMap = wandora.getTopicMap();
            String query = tmqlTextPane.getText();
            String runtime = runtimeComboBox.getSelectedItem().toString();

            TMQLRunner.TMQLResult res = TMQLRunner.runTMQLCatchException(topicMap, query, runtime);
            if(res.isException()) {
                throw res.getException();
            }
            else {
                if(res.getData() != null && res.getData().length > 0) {
                    resultsTable = new MixedTopicTable(wandora);
                    Object[][] data = res.getData();
                    Object[] columns = Arrays.copyOf(res.getColumns(), res.getNumColumns(), Object[].class);
                    resultsTable.initialize(data,columns);
                }
            }
            
            if(resultsTable != null) {
                tmqlResultPanel.add(resultsTable, BorderLayout.NORTH);
            }
            else {
                message.setText("No search results!");
                tmqlResultPanel.add(message, BorderLayout.CENTER);
            }
            tmqlComboBox.setEditable(false);
            revalidate();
            repaint();
        }
        catch(Exception e){
            message.setText("Error!");
            tmqlResultPanel.add(message, BorderLayout.CENTER);
            revalidate();
            repaint();
            wandora.handleError(e);
        }
        catch(Throwable t) {
            message.setText("Error!");
            tmqlResultPanel.add(message, BorderLayout.CENTER);
            revalidate();
            repaint();
            wandora.handleError(t);
        }
    }//GEN-LAST:event_runButtonActionPerformed

    private void clearResultsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearResultsButtonActionPerformed
        clearResultsButton.setEnabled(false);
        tmqlResultPanel.removeAll();
        resultsTable = null;
        revalidate();
        repaint();
    }//GEN-LAST:event_clearResultsButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addTmqlButton;
    private javax.swing.JButton clearResultsButton;
    private javax.swing.JButton delTmqlButton;
    private javax.swing.JScrollPane resultScrollPane;
    private javax.swing.JButton runButton;
    private javax.swing.JComboBox runtimeComboBox;
    private javax.swing.JLabel runtimeLabel;
    private javax.swing.JPanel selectQueryPanel1;
    private javax.swing.JPanel tmqlButtonPanel;
    private javax.swing.JComboBox tmqlComboBox;
    private javax.swing.JPanel tmqlOptionPanel;
    private javax.swing.JPanel tmqlPanel;
    private javax.swing.JPanel tmqlResultContainerPanel;
    private javax.swing.JPanel tmqlResultPanel;
    private javax.swing.JScrollPane tmqlScrollPane;
    private javax.swing.JTextPane tmqlTextPane;
    // End of variables declaration//GEN-END:variables


    
    
    // -------------------------------------------------------------------------
    // ------------------------------------------------------- TMQLTextPane ----
    // -------------------------------------------------------------------------

    
    
    private class TMQLTextPane extends SimpleTextPaneResizeable {
    
        private int tmqlPanelWidth = 100;
        private int tmqlPanelHeight = tmqlPanel.getHeight();
        
        
        @Override
        public void mouseDragged(MouseEvent e) {
            Point p = e.getPoint();
            if(mousePressedInTriangle) {
                inTheTriangleZone = true;
                int yDiff = (mousePressedPoint.y - p.y);
                newSize = new Dimension(100, sizeAtPress.height - yDiff);

                JScrollPane sp = getScrollPane();

                if(scrollPane != null) {
                    sp.getViewport().setSize(newSize);
                    sp.getViewport().setPreferredSize(newSize);
                    sp.getViewport().setMinimumSize(newSize);

                    sp.setSize(newSize);
                    sp.setPreferredSize(newSize);
                    sp.setMinimumSize(newSize);
                }

                tmqlPanel.setSize(tmqlPanelWidth, tmqlPanelHeight - yDiff);
                tmqlPanel.revalidate();
                tmqlPanel.repaint();
            }
        }
        
        
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            if(mousePressedInTriangle) {
                Point p = e.getPoint();
                tmqlPanelHeight = tmqlPanel.getHeight();
            }
        }
        
    }

    
    

    // ------------------------------------------------------- TopicSelector ---
    
    @Override
    public Topic getSelectedTopic() {
        if(resultsTable != null) {
            Topic[] topics = resultsTable.getSelectedTopics();
            if(topics != null && topics.length > 0) {
                return topics[0];
            }
        }
        return null;
    }


    @Override
    public Topic[] getSelectedTopics() {
        if(resultsTable != null) {
            resultsTable.getSelectedTopics();
        }
        return null;
    }
    

    @Override
    public java.awt.Component getPanel() {
        return this;
    }
    
    
    @Override
    public String getSelectorName() {
        return "TMQL";
    }
    
    @Override
    public void init() {
        
    }
    
    @Override
    public void cleanup() {
        
    }

}
