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
 * 
 * LinkedTopicMapConfiguration.java
 *
 * Created on 9. toukokuuta 2008, 12:46
 */

package org.wandora.topicmap.linked;
import org.wandora.application.Wandora;
import org.wandora.topicmap.*;
import org.wandora.topicmap.layered.*;
import org.wandora.application.gui.simple.*;
import java.util.*;

/**
 *
 * @author  olli
 */
public class LinkedTopicMapConfiguration extends TopicMapConfigurationPanel {

    protected Wandora wandora;
    protected TopicMap tm;
    /** Creates new form LinketTopicMapConfiguration */
    public LinkedTopicMapConfiguration(Wandora wandora) {
        this(wandora,null);
    }
    public LinkedTopicMapConfiguration(Wandora wandora,TopicMap tm) {
        this.wandora=wandora;
        this.tm=tm;
        initComponents();
        fillComboBox();
    }
    
    protected void fillComboBox(){
        linkedMapComboBox.removeAllItems();
        fillComboBox(wandora.getTopicMap(),"");
    }
    protected void fillComboBox(ContainerTopicMap container,String prefix){
        for(Layer l : container.getLayers()){
            if(tm!=l.getTopicMap())
                linkedMapComboBox.addItem(prefix+l.getName());
            if(l.getTopicMap() instanceof ContainerTopicMap){
                fillComboBox((ContainerTopicMap)l.getTopicMap(),prefix+"  ");
            }
        }
    }
    
    public String getSelectedLayerName(){
        Object o=linkedMapComboBox.getSelectedItem();
        if(o==null) return null;
        return o.toString().trim();
    }
    
    public void setSelectedLayer(String name){
        int size=linkedMapComboBox.getModel().getSize();
        for(int i=0;i<size;i++){
            Object o=linkedMapComboBox.getModel().getElementAt(i);
            if(o.toString().trim().equals(name)) {
                linkedMapComboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    @Override
    public Object getParameters() {
        return getSelectedLayerName();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new SimpleLabel();
        linkedMapComboBox = new SimpleComboBox();
        linkedMapComboBox.setEditable(false);
        jPanel1 = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Linked topic map");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 0);
        add(jLabel1, gridBagConstraints);

        linkedMapComboBox.setPreferredSize(new java.awt.Dimension(29, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 5);
        add(linkedMapComboBox, gridBagConstraints);

        jPanel1.setMinimumSize(new java.awt.Dimension(1, 1));
        jPanel1.setPreferredSize(new java.awt.Dimension(1, 1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox linkedMapComboBox;
    // End of variables declaration//GEN-END:variables

}