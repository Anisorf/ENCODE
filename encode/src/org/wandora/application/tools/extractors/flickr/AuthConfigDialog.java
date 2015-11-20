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
 * AuthConfigDialog.java
 *
 * Created on 28. huhtikuuta 2008, 12:45
 */

package org.wandora.application.tools.extractors.flickr;

import java.awt.Color;
import javax.swing.JLabel;
import org.wandora.application.Wandora;
import org.wandora.application.gui.simple.*;
import org.wandora.application.gui.*;

/**
 *
 * @author  anttirt
 */
public class AuthConfigDialog extends javax.swing.JDialog {
    
    private Wandora admin;
    private FlickrState flickrState;
    private JLabel[] curLabels;
    
    /** Creates new form AuthConfigDialog */
    public AuthConfigDialog(java.awt.Frame parent, boolean modal, FlickrState state) {
        super(parent, modal);
        flickrState = state;
        initComponents();
        curLabels = new JLabel[] { gotNone, gotRead, gotWrite, gotDelete };
        
        int currentAuthLevel = FlickrState.getAuthLevel(state.PermissionLevel);
        Color validGreen = new Color(30, 220, 30);
        
        for(int i = 0; i <= currentAuthLevel; ++i)
        {
           curLabels[i].setForeground(validGreen);
        }
        
        fldUsername.setText(state.LastUserName);
        fldFrob.setText(state.Frob);
        fldToken.setText(state.Token);
        
        this.setSize(650, 280);
        
        
        if(parent instanceof Wandora)
        {
            admin = (Wandora)parent;
            admin.centerWindow(this);
        }
    }
    
    private boolean cancelled;
    
    public boolean wasCancelled() {
        return cancelled;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        wandoraLabel2 = new org.wandora.application.gui.simple.SimpleLabel();
        authPanel = new javax.swing.JPanel();
        wandoraLabel3 = new org.wandora.application.gui.simple.SimpleLabel();
        jPanel3 = new javax.swing.JPanel();
        fldUsername = new javax.swing.JTextField();
        fldToken = new javax.swing.JTextField();
        fldFrob = new javax.swing.JTextField();
        wandoraLabel6 = new org.wandora.application.gui.simple.SimpleLabel();
        wandoraLabel5 = new org.wandora.application.gui.simple.SimpleLabel();
        wandoraLabel4 = new org.wandora.application.gui.simple.SimpleLabel();
        wandoraLabel7 = new org.wandora.application.gui.simple.SimpleLabel();
        jPanel1 = new javax.swing.JPanel();
        gotNone = new org.wandora.application.gui.simple.SimpleLabel();
        gotRead = new org.wandora.application.gui.simple.SimpleLabel();
        gotDelete = new org.wandora.application.gui.simple.SimpleLabel();
        gotWrite = new org.wandora.application.gui.simple.SimpleLabel();
        jPanel4 = new javax.swing.JPanel();
        btnClearInfo = new org.wandora.application.gui.simple.SimpleButton();
        btnValidate = new org.wandora.application.gui.simple.SimpleButton();
        buttonPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnClose = new org.wandora.application.gui.simple.SimpleButton();

        wandoraLabel2.setText("wandoraLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Flickr authentication status");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        authPanel.setLayout(new java.awt.GridBagLayout());

        wandoraLabel3.setText("<html><body><p>This dialog lets you inspect and modify the Flickr authentication status of the current running Wandora instance. If you have saved the frob and token from a previous session, you can enter them here to avoid having to re-authenticate at the Flickr website.</p></body></html>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        authPanel.add(wandoraLabel3, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel3.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        jPanel3.add(fldUsername, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel3.add(fldToken, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel3.add(fldFrob, gridBagConstraints);

        wandoraLabel6.setText("Current username");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        jPanel3.add(wandoraLabel6, gridBagConstraints);

        wandoraLabel5.setText("Token");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel3.add(wandoraLabel5, gridBagConstraints);

        wandoraLabel4.setText("Frob");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel3.add(wandoraLabel4, gridBagConstraints);

        wandoraLabel7.setText("Current rights");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        jPanel3.add(wandoraLabel7, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        gotNone.setForeground(new java.awt.Color(102, 102, 102));
        gotNone.setText("none");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel1.add(gotNone, gridBagConstraints);

        gotRead.setForeground(new java.awt.Color(102, 102, 102));
        gotRead.setText("read");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel1.add(gotRead, gridBagConstraints);

        gotDelete.setForeground(new java.awt.Color(102, 102, 102));
        gotDelete.setText("delete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel1.add(gotDelete, gridBagConstraints);

        gotWrite.setForeground(new java.awt.Color(102, 102, 102));
        gotWrite.setText("write");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel1.add(gotWrite, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 5);
        jPanel3.add(jPanel1, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        btnClearInfo.setText("Clear information");
        btnClearInfo.setPreferredSize(new java.awt.Dimension(115, 21));
        btnClearInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearInfoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel4.add(btnClearInfo, gridBagConstraints);

        btnValidate.setText("Validate new frob and token");
        btnValidate.setPreferredSize(new java.awt.Dimension(171, 21));
        btnValidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValidateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        jPanel4.add(btnValidate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        jPanel3.add(jPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        authPanel.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(authPanel, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        buttonPanel.add(jPanel5, gridBagConstraints);

        btnClose.setText("Close");
        btnClose.setPreferredSize(new java.awt.Dimension(70, 23));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        buttonPanel.add(btnClose, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 10, 10);
        getContentPane().add(buttonPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnValidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidateActionPerformed
        flickrState.Frob = fldFrob.getText();
        flickrState.Token = fldToken.getText();
        try
        {
            //WandoraOptionPane.showMessageDialog(admin, "Validating...", "Validating...", WandoraOptionPane.INFORMATION_MESSAGE);
            if(flickrState.validToken(FlickrState.PermRead))
            {
                WandoraOptionPane.showMessageDialog(admin, "Validating done", "Successfully validated", WandoraOptionPane.INFORMATION_MESSAGE);
                                
                fldUsername.setText(flickrState.LastUserName);
                fldFrob.setText(flickrState.Frob);
                fldToken.setText(flickrState.Token);
                int currentAuthLevel = FlickrState.getAuthLevel(flickrState.PermissionLevel);
                Color validGreen = new Color(30, 220, 30);

                for(int i = 0; i <= currentAuthLevel; ++i)
                {
                   curLabels[i].setForeground(validGreen);
                }
            }
            else
            {
                WandoraOptionPane.showMessageDialog(this, "Validating failed", "Validating failed!", WandoraOptionPane.INFORMATION_MESSAGE);
                flickrState.Frob = null;
                flickrState.Token = null;
            }
        }
        catch(FlickrExtractor.RequestFailure e)
        {
            WandoraOptionPane.showMessageDialog(this, e.getMessage(), "Validating failed!", WandoraOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnValidateActionPerformed

    private void btnClearInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearInfoActionPerformed
        fldUsername.setText("");
        fldFrob.setText("");
        fldToken.setText("");
        flickrState.Frob = null;
        flickrState.Token = null;
        flickrState.LastUserName = null;
        // 212 208 200
        
        curLabels[0].setForeground(new Color(30, 220, 30));
        for(int i = 1; i < 4; ++i)
            curLabels[i].setForeground(new Color(102, 102, 102));
    }//GEN-LAST:event_btnClearInfoActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        cancelled = true;
        setVisible(false);
}//GEN-LAST:event_btnCloseActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AuthConfigDialog dialog = new AuthConfigDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
        */
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel authPanel;
    private org.wandora.application.gui.simple.SimpleButton btnClearInfo;
    private org.wandora.application.gui.simple.SimpleButton btnClose;
    private org.wandora.application.gui.simple.SimpleButton btnValidate;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JTextField fldFrob;
    private javax.swing.JTextField fldToken;
    private javax.swing.JTextField fldUsername;
    private javax.swing.JLabel gotDelete;
    private javax.swing.JLabel gotNone;
    private javax.swing.JLabel gotRead;
    private javax.swing.JLabel gotWrite;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private org.wandora.application.gui.simple.SimpleLabel wandoraLabel2;
    private org.wandora.application.gui.simple.SimpleLabel wandoraLabel3;
    private org.wandora.application.gui.simple.SimpleLabel wandoraLabel4;
    private org.wandora.application.gui.simple.SimpleLabel wandoraLabel5;
    private org.wandora.application.gui.simple.SimpleLabel wandoraLabel6;
    private org.wandora.application.gui.simple.SimpleLabel wandoraLabel7;
    // End of variables declaration//GEN-END:variables
    
}
