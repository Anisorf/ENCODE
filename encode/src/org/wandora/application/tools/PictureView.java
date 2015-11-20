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
 * PictureView.java
 *
 * Created on July 19, 2004, 2:38 PM
 */

package org.wandora.application.tools;
import java.awt.*;
import java.awt.image.*;

/**
 *
 * @author  olli
 */
public class PictureView extends javax.swing.JDialog {
    private BufferedImage image;
    /** Creates new form PictureView */
    public PictureView(java.awt.Dialog parent, boolean modal,BufferedImage image) {
        super(parent, modal);
        this.image=image;
        initComponents();
        this.picturePanel.setMinimumSize(new java.awt.Dimension(image.getWidth(),image.getHeight()));
        this.picturePanel.setPreferredSize(new java.awt.Dimension(image.getWidth(),image.getHeight()));
//        this.picturePanel.setSize(image.getWidth(),image.getHeight());
        org.wandora.utils.swing.GuiTools.centerWindow(this,parent);
        this.invalidate();        
    }
    public PictureView(java.awt.Frame parent, boolean modal,BufferedImage image) {
        super(parent, modal);
        this.image=image;
        initComponents();
        this.picturePanel.setMinimumSize(new java.awt.Dimension(image.getWidth(),image.getHeight()));
        this.picturePanel.setPreferredSize(new java.awt.Dimension(image.getWidth(),image.getHeight()));
//        this.picturePanel.setSize(image.getWidth(),image.getHeight());
        this.setLocation(parent.getLocation().x+parent.getWidth()/2-this.getWidth()/2, 
                         parent.getLocation().y+parent.getHeight()/2-this.getHeight()/2);
        this.invalidate();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jButton = new javax.swing.JButton();
        jScrollPane = new javax.swing.JScrollPane();
        picturePanel = new javax.swing.JPanel() {
            public void paint(Graphics g){
                super.paint(g);
                g.drawImage(image,0,0,image.getWidth(),image.getHeight(),null);
            }
        };

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jButton.setText("Close");
        jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        getContentPane().add(jButton, gridBagConstraints);

        jScrollPane.setViewportView(picturePanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane, gridBagConstraints);

        setBounds(0, 0, 567, 560);
    }//GEN-END:initComponents

    private void jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JPanel picturePanel;
    // End of variables declaration//GEN-END:variables
    
}