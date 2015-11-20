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
package org.wandora.application.gui.topicpanels.queryeditorpanel;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.ListModel;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.wandora.application.Wandora;
import org.wandora.application.gui.UIBox;
import org.wandora.application.gui.WandoraOptionPane;
import org.wandora.application.gui.topicpanels.queryeditorpanel.DirectiveEditor.DirectiveParameters;
import org.wandora.utils.JsonMapper;
import org.wandora.utils.Options;

/**
 *
 * @author olli
 */


public class QueryLibraryPanel extends javax.swing.JPanel {

    protected String openedName;
    
    protected final ArrayList<StoredQuery> storedQueries=new ArrayList<StoredQuery>();
    
    public static class StoredQueries {
        public StoredQuery[] queries;
        public StoredQueries(){}
        public StoredQueries(StoredQuery[] queries){this.queries=queries;}
        public StoredQueries(ArrayList<StoredQuery> queries){this.queries=queries.toArray(new StoredQuery[queries.size()]);}
        @JsonIgnore
        public StoredQueries duplicate(){
            StoredQueries ret=new StoredQueries();
            ret.queries=new StoredQuery[queries.length];
            for(int i=0;i<queries.length;i++){
                ret.queries[i]=queries[i].duplicate();
            }
            return ret;
        }
    }
    
    public static class StoredQuery {
        public String name;
        public ArrayList<DirectiveParameters> directiveParameters;
        public String rootDirective;
        
        public StoredQuery() {
        }

        public StoredQuery(String name, ArrayList<DirectiveParameters> directiveParameters, String rootDirective) {
            this.name = name;
            this.directiveParameters = directiveParameters;
            this.rootDirective = rootDirective;
        }
        
        @Override
        public String toString(){return name;}
        
        @JsonIgnore
        public DirectiveParameters getRootParameters(){
            for(DirectiveParameters p : directiveParameters){
                if(p.id!=null && p.id.equals(rootDirective)) return p;
            }
            return null;
        }
        
        @JsonIgnore
        public StoredQuery duplicate(){
            ArrayList<DirectiveParameters> newParams=new ArrayList<>();
            for(DirectiveParameters p : directiveParameters) newParams.add(p.duplicate());
            return new StoredQuery(name,newParams,rootDirective);
        }
    }
    
    /**
     * Creates new form QueryLibraryPanel
     */
    public QueryLibraryPanel() {
        initComponents();
        
        Object[] buttonStruct = {
            "Open",
            UIBox.getIcon(0xF07C),
            new java.awt.event.ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    openClicked();
                }
            },
            "Save",
            UIBox.getIcon(0xF0C7),
            new java.awt.event.ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveClicked();
                }
            },
            "Delete",
            UIBox.getIcon(0xF014),
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    deleteClicked();
                }
            }
        };
        JComponent buttonContainer = UIBox.makeButtonContainer(buttonStruct, Wandora.getWandora());
        buttonPanel.add(buttonContainer);
     
        readQueries(Wandora.getWandora().getOptions());
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

        toolBar = new javax.swing.JToolBar();
        buttonPanel = new javax.swing.JPanel();
        fillerPanel = new javax.swing.JPanel();
        innerFillerPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        queryList = new javax.swing.JList();

        setLayout(new java.awt.GridBagLayout());

        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        toolBar.add(buttonPanel);

        fillerPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        fillerPanel.add(innerFillerPanel, gridBagConstraints);

        toolBar.add(fillerPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(toolBar, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Query name");
        jPanel1.add(jLabel1, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(nameField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jPanel1, gridBagConstraints);

        queryList.setModel(new DefaultListModel());
        jScrollPane2.setViewportView(queryList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    public QueryEditorComponent findGraph(){
        Container c=this;
        while(c!=null && !(c instanceof QueryEditorDockPanel)){
            c=c.getParent();
        }
        
        if(c==null) return null;
        return ((QueryEditorDockPanel)c).getQueryEditor();
    }
    
    public void readQueries(Options options){
        StoredQuery[] queries=null;
        String queriesJson=options.get(OPTIONS_KEY);
        if(queriesJson!=null){
            JsonMapper mapper=new JsonMapper();
            try{
                StoredQueries wrapper=(StoredQueries)mapper.readValue(queriesJson,StoredQueries.class);
                queries=wrapper.queries;
            }
            catch(IOException ioe){Wandora.getWandora().handleError(ioe);}
        }
        else queries=new StoredQuery[0];
        
        if(queries!=null){
            synchronized(storedQueries){
                storedQueries.clear();
                storedQueries.addAll(Arrays.asList(queries));

                DefaultListModel model=(DefaultListModel)queryList.getModel();            
                model.clear();
                for(StoredQuery q : storedQueries){
                    model.addElement(q);
                }
                
            }
        }

    }
    
    private static final String OPTIONS_KEY="options.queryeditor.storedqueries";
    public void writeQueries(Options options){
        JsonMapper mapper=new JsonMapper();
        String json=null;
        synchronized(storedQueries){
            json=mapper.writeValue(new StoredQueries(storedQueries));
        }
        options.put(OPTIONS_KEY,json);
        
         
    }
    
    
    public void save(String name){
        QueryEditorComponent graph=findGraph();
        StoredQuery query=graph.getStoredQuery();
        if(query==null) return;
        query.name=name;
        
        // cycle through serialisation and deserialisation to make everything consistent
        JsonMapper mapper=new JsonMapper();
        String json=mapper.writeValue(query);
        try{
            query=mapper.readValue(json, StoredQuery.class);
        }
        catch(IOException e){Wandora.getWandora().handleError(e); return;}
        
        
        synchronized(storedQueries){
            boolean saved=false;
            for(int i=0;i<storedQueries.size();i++){
                StoredQuery q=storedQueries.get(i);
                if(q.name!=null && q.name.equals(name)){
                    saved=true;
                    storedQueries.set(i, query);
                    break;
                }
            }
            if(!saved){
                storedQueries.add(query);
                DefaultListModel model=(DefaultListModel)queryList.getModel();            
                model.addElement(query);
            }
        }
        
        writeQueries(Wandora.getWandora().getOptions());
        
    }
    
    
    public void openQuery(StoredQuery query){
        nameField.setText(query.name);
        QueryEditorComponent graph=findGraph();        
        
        graph.clearQuery();
        graph.openStoredQuery(query.duplicate());
        
    }
    
    public void open(String name){
        synchronized(storedQueries) {
            for(StoredQuery q : storedQueries){
                if(q.name.equals(name)){
                    openQuery(q);
                    return;
                }
            }
            WandoraOptionPane.showMessageDialog(Wandora.getWandora(), "Query not found.");            
        }
    }
    
    public void deleteClicked(){
        Object o=queryList.getSelectedValue();
        if(o==null) return;
        String openName=o.toString();
        
        if(WandoraOptionPane.showConfirmDialog(Wandora.getWandora(), "Do you want to delete query "+openName)
                    == WandoraOptionPane.YES_OPTION ){
            
            
            synchronized(storedQueries) {
                for(StoredQuery q : storedQueries){
                    if(q.name.equals(openName)){
                        storedQueries.remove(q);
                        
                        DefaultListModel model=(DefaultListModel)queryList.getModel();            
                        model.removeElement(o);
                        
                        writeQueries(Wandora.getWandora().getOptions());
                        break;
                    }
                }
            }
        }
    }
    
    public void saveClicked(){
        String name=nameField.getText().trim();
        if(name.length()==0){
            WandoraOptionPane.showMessageDialog(Wandora.getWandora(), "Name is empty.");
            return;
        }
        else {
            ListModel listModel=queryList.getModel();
            for(int i=0;i<listModel.getSize();i++){
                Object o=listModel.getElementAt(i);
                if(name.equals(o)){
                    if(openedName==null || !openedName.equals(name)){
                        // TODO: confirm overwrite
                    }
                    break;
                }
            }
        }
        
        openedName=name;
        
        save(name);
    }
    
    
    public void openClicked(){
        // TODO: save confirm?
        
        Object o=queryList.getSelectedValue();
        if(o==null) return;
        
        openedName=o.toString();
        
        open(openedName);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel fillerPanel;
    private javax.swing.JPanel innerFillerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField nameField;
    private javax.swing.JList queryList;
    private javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables
}
