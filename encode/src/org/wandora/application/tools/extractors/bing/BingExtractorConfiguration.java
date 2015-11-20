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
 * BingConfigurationDialog.java
 *
 * Created on 19.12.2009, 20:01:41
 */

package org.wandora.application.tools.extractors.bing;


import javax.swing.*;
import org.wandora.application.*;
import org.wandora.application.gui.simple.*;
import org.wandora.utils.*;

/**
 *
 * @author akivela
 */
public class BingExtractorConfiguration extends javax.swing.JDialog {
    private WandoraTool tool = null;
    private Wandora wandora = null;
    private Options options = null;



    /** Creates new form BingConfigurationDialog */
    public BingExtractorConfiguration(Wandora wandora, Options options, WandoraTool tool) {
        super(wandora, true);
        System.out.println("tool: "+tool);
        this.tool = tool;
        this.wandora = wandora;
        this.options = options;
        initComponents();
        setSize(300, 150);
        setTitle("Bing extractor configuration");
        if(wandora != null) wandora.centerWindow(this);
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

        jPanel1 = new javax.swing.JPanel();
        forgetKeyButton = new SimpleButton();
        cancelButton = new SimpleButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        forgetKeyButton.setText("Forget authentication");
        forgetKeyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forgetKeyButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel1.add(forgetKeyButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        jPanel1.add(cancelButton, new java.awt.GridBagConstraints());

        getContentPane().add(jPanel1, new java.awt.GridBagConstraints());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void forgetKeyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forgetKeyButtonActionPerformed
        AbstractBingExtractor.forgetAuth();
        this.setVisible(false);
    }//GEN-LAST:event_forgetKeyButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton forgetKeyButton;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

}
