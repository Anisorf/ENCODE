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
 * OccurrenceTableSingleType.java
 *
 * Created on August 17, 2004, 12:07 PM
 */

package org.wandora.application.gui;



import org.wandora.application.gui.texteditor.OccurrenceTextEditor;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.wandora.application.*;
import org.wandora.application.contexts.ApplicationContext;
import org.wandora.application.gui.simple.*;
import org.wandora.application.gui.topicstringify.TopicToString;
import org.wandora.application.tools.occurrences.*;
import org.wandora.application.tools.occurrences.DeleteOccurrence;
import org.wandora.topicmap.*;
import static org.wandora.topicmap.TMBox.LANGUAGE_SI;
import org.wandora.utils.*;
import org.wandora.utils.language.GoogleTranslateBox;
import org.wandora.utils.language.MicrosoftTranslateBox;
import org.wandora.utils.language.WatsonTranslateBox;
import org.wandora.utils.swing.*;


/**
 *
 * @author  olli, akivela
 */
public class OccurrenceTableSingleType extends SimpleTable implements OccurrenceTable, MouseListener, Clipboardable {
    
    public String tableType = VIEW_SCHEMA;
    
    private Topic topic;
    private Topic[] langs;
    private Topic type;
    private String[] data;
    private Color[] colors;
    private TableSorter sorter;
    private Wandora wandora;    

    private String[] originalData;
    private int defaultRowHeight = 1;
    
    private Object[] popupStruct;
    private MouseEvent mouseEvent;
    
    
    /** Creates a new instance of OccurrenceTableSingleType */
    public OccurrenceTableSingleType(Topic topic, Topic type, Options options, Wandora wandora) throws TopicMapException {
        this.wandora=wandora;
        this.topic=topic;
        TopicMap tm = wandora.getTopicMap();
        
        try {
            Options opts = options;
            if(opts == null) opts = wandora.getOptions();
            if(opts != null) {
                tableType = opts.get(VIEW_OPTIONS_KEY);
                if(tableType == null || tableType.length() == 0) tableType = VIEW_SCHEMA;
                
                defaultRowHeight = opts.getInt(ROW_HEIGHT_OPTIONS_KEY);
                if(defaultRowHeight < 1) defaultRowHeight = 1;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        this.type = type;

        HashSet<Topic> langSet = new LinkedHashSet();
        if(VIEW_USED.equalsIgnoreCase(tableType) || VIEW_USED_AND_SCHEMA.equalsIgnoreCase(tableType)) {
            Hashtable<Topic,String> occs = null;
            Topic langTopic = null;
            occs = topic.getData(type);
            for(Enumeration<Topic> keys = occs.keys(); keys.hasMoreElements(); ) {
                langTopic = keys.nextElement();
                langSet.add(langTopic);
            }
        }
        if(VIEW_SCHEMA.equalsIgnoreCase(tableType) || VIEW_USED_AND_SCHEMA.equalsIgnoreCase(tableType)) {
            Collection<Topic> langTopics = tm.getTopicsOfType(LANGUAGE_SI);
            langSet.addAll( langTopics );
        }
        
        langs=langSet.toArray( new Topic[langSet.size()] );
        data=new String[langs.length];
        originalData=new String[langs.length];
        colors=new Color[langs.length];

        for(int j=0;j<langs.length;j++){
            if(langs[j] != null) {
                data[j]=topic.getData(type,langs[j]);
                if(data[j]==null) data[j]="";
                originalData[j]=data[j];
                colors[j]=wandora.topicHilights.getOccurrenceColor(topic,type,langs[j]);
            }
        }

        this.setColumnSelectionAllowed(false);
        this.setRowSelectionAllowed(false);
        sorter=new TableSorter(new DataTableModel());
        
        final TableCellRenderer oldRenderer=this.getTableHeader().getDefaultRenderer();
        this.getTableHeader().setDefaultRenderer(new TableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus, int row, int column){
                Component c=oldRenderer.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                column=convertColumnIndexToModel(column);
                return c;
            }
        });
        
        this.setAutoCreateColumnsFromModel(false);
        this.setModel(sorter);
        TableColumn column=new TableColumn(0,40,new TopicCellRenderer(),new TopicCellEditor());
        this.addColumn(column);
        column = new TableColumn(1,500,new DataCellRenderer(),new DataCellEditor());
        this.addColumn(column);
        sorter.setTableHeader(this.getTableHeader());

        this.setRowHeight(getDefaultRowHeightInPixels());
        updateRowHeights();
        
        popupStruct = WandoraMenuManager.getOccurrenceTableMenu(this, options);
        JPopupMenu popup = UIBox.makePopupMenu(popupStruct, wandora);
        this.setComponentPopupMenu(popup);
        this.addMouseListener(this);
        
        this.setDragEnabled(true);
        this.setTransferHandler(new OccurrencesTableTransferHandler());
        this.setDropMode(DropMode.ON);
        this.createDefaultTableSelectionModel();
    }
    
    
    protected int getDefaultRowHeightInPixels() {
        return 2+defaultRowHeight*16;
    }
    
    
    protected void updateRowHeights() {
        for(int i=0; i<data.length; i++) {
            try {
                String occurrenceData = data[i];
                if(DataURL.isDataURL(occurrenceData)) {
                    Component preview = UIBox.getPreview(new DataURL(occurrenceData));
                    if(preview != null) {
                        setRowHeight(i, Math.max(getDefaultRowHeightInPixels(), preview.getHeight()+10));
                    }
                }
                else {
                    setRowHeight(i, getDefaultRowHeightInPixels());
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    @Override
    public boolean applyChanges(Topic t, Wandora parent) throws TopicMapException {
        boolean changed=false;
        for(int j=0; j<langs.length; j++){
            String text=data[j];
            if(text != null) {
                // String orig=topic.getData(types[i],langs[j]);
                String orig=originalData[j];
                if(orig==null) orig="";
                if(!orig.equals(text)) {
                    changed=true;
                    try {
                        if(text.length()==0) {
                            topic.removeData(type,langs[j]);
                        }
                        else {
                            topic.setData(type, langs[j], text );
                        }
                    }
                    catch(Exception e) {
                        wandora.handleError(e);
                    }
                }
            }
        }
        return changed;
    }
    
    
    @Override
    public Topic getTopic() {
        return topic;
    }
    
    
    @Override
    public String getToolTipText(java.awt.event.MouseEvent e) {
        try {
            Object o = getValueAt(e);
            if(o != null) {
                String tooltipText = o.toString();
                if(!DataURL.isDataURL(tooltipText)) {
                    if(tooltipText != null && tooltipText.length() > 5) {
                        if(tooltipText.length() > 200) tooltipText = tooltipText.substring(0,199)+"...";
                        return Textbox.makeHTMLParagraph(tooltipText, 40);
                    }
                }
            }
        }
        catch(Exception ex) {
            // Sometimes swing throws index out of bounds exceptions if the table has not initialized yet.
        }
        return null;
    }
    
    
    
    @Override
    public int getRowHeightOption() {
        return defaultRowHeight;
    }
    
    
    
    
    @Override
    public Object getOccurrenceTableType() {
        return tableType;
    }

        

  
    
    
    
    @Override
    public String getPointedOccurrence() {
        try {
            Point p = getTablePoint(mouseEvent);
            int realCol=convertColumnIndexToModel(p.y);
            int realRow=sorter.modelIndex(p.x);
            if(realRow >= 0 && realCol >= 0 && realRow < langs.length && realCol < 2) {
                return topic.getData(type, langs[realRow]);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    @Override
    public Topic getPointedOccurrenceType() {
        return type;
    }
    
    
    
    
    @Override
    public Topic getPointedOccurrenceLang() {
        try {
            Point p = getTablePoint(mouseEvent);
            int realRow=sorter.modelIndex(p.x);
            if(realRow >= 0 && realRow < langs.length) {
                return langs[realRow];
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    
    
    
    @Override
    public void cut() {
        try {
            copy();
            Point p = getTablePoint(mouseEvent);
            int realCol=convertColumnIndexToModel(p.y);
            int realRow=sorter.modelIndex(p.x);
            if(realRow >= 0 && realCol >= 0 && realRow < langs.length && realCol < 2) {
                topic.removeData(type, langs[realRow]);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            if(wandora != null) wandora.handleError(e);
        }
    }
    
    
    
    
    
    
    @Override
    public void paste() {
        try {
            Point p = getTablePoint(mouseEvent);
            int realCol=convertColumnIndexToModel(p.y);
            int realRow=sorter.modelIndex(p.x);
            if(realRow >= 0 && realCol >= 0 && realRow < langs.length && realCol < 2) {
                topic.setData(type, langs[realRow], ClipboardBox.getClipboard());
            }
        }
        catch(Exception e) {
            if(wandora != null) wandora.handleError(e);
        }
    }
    
    
    
    @Override
    public void append() {
        try {
            Point p = getTablePoint(mouseEvent);
            int realCol=convertColumnIndexToModel(p.y);
            int realRow=sorter.modelIndex(p.x);
            if(realRow >= 0 && realCol >= 0 && realRow < langs.length && realCol < 2) {
                String oldOccurrence = topic.getData(type, langs[realRow]);
                if(oldOccurrence == null) oldOccurrence = "";
                topic.setData(type, langs[realRow], oldOccurrence + ClipboardBox.getClipboard());
            }
        }
        catch(Exception e) {
            if(wandora != null) wandora.handleError(e);
        }
    }
    
    
    
    @Override
    public void copy() {
        ClipboardBox.setClipboard(getCopyString());
    }
    
    
    
    @Override
    public String getCopyString() {
        try {
            Point p = getTablePoint(mouseEvent);
            int realCol=convertColumnIndexToModel(p.y);
            int realRow=sorter.modelIndex(p.x);
            if(realRow >= 0 && realCol >= 0 && realRow < langs.length && realCol < 2) {
                return topic.getData(type, langs[realRow]);
            }
        }
        catch(Exception e) {}
        return "";
    }

    
    
    @Override
    public void delete() {
        try {
            Point p = getTablePoint(mouseEvent);
            int realCol=convertColumnIndexToModel(p.y);
            int realRow=sorter.modelIndex(p.x);
            if(realRow >= 0 && realCol >= 0 && realRow < langs.length && realCol < 2) {
                topic.removeData(type, langs[realRow]);
            }
        }
        catch(Exception e) {
            if(wandora != null) wandora.handleError(e);
        }
    }
    
    
    
    @Override
    public void changeType() {
        try {
            Point p = getTablePoint(mouseEvent);
            int realCol=convertColumnIndexToModel(p.y);
            int realRow=sorter.modelIndex(p.x);
            if(realRow >= 0 && realCol >= 0 && realRow < langs.length && realCol < 2) {
                Topic selectedScope = langs[realRow];
                if(type != null && !type.isRemoved()) {
                    Topic newType = wandora.showTopicFinder("Select new occurrence type");
                    if(newType != null && !newType.isRemoved()) {
                        Hashtable<Topic,String> os = topic.getData(type);
                        if(os != null && !os.isEmpty()) {
                            for(Topic scope : os.keySet()) {
                                if(scope != null && !scope.isRemoved()) {
                                    if(selectedScope == null) {
                                        String occurrenceText = os.get(scope);
                                        topic.setData(newType, scope, occurrenceText);
                                    }
                                    else if(selectedScope.mergesWithTopic(scope)) {
                                        String occurrenceText = os.get(scope);
                                        topic.setData(newType, scope, occurrenceText);
                                    }
                                }
                            }
                            if(selectedScope == null) {
                                topic.removeData(type);
                            }
                            else {
                                topic.removeData(type, selectedScope);
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e) {
            if(wandora != null) wandora.handleError(e);
        }
    }
    
    
    
    @Override
    public void duplicateType() {
        try {
            Point p = getTablePoint(mouseEvent);
            int realCol=convertColumnIndexToModel(p.y);
            int realRow=sorter.modelIndex(p.x);
            if(realRow >= 0 && realCol >= 0 && realRow < langs.length && realCol < 2) {
                Topic selectedScope = langs[realRow];
                if(type != null && !type.isRemoved()) {
                    Topic newType = wandora.showTopicFinder("Select new occurrence type");
                    if(newType != null && !newType.isRemoved()) {
                        Hashtable<Topic,String> os = topic.getData(type);
                        if(os != null && !os.isEmpty()) {
                            for(Topic scope : os.keySet()) {
                                if(scope != null && !scope.isRemoved()) {
                                    if(selectedScope == null) {
                                        String occurrenceText = os.get(scope);
                                        topic.setData(newType, scope, occurrenceText);
                                    }
                                    else if(selectedScope.mergesWithTopic(scope)) {
                                        String occurrenceText = os.get(scope);
                                        topic.setData(newType, scope, occurrenceText);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e) {
            if(wandora != null) wandora.handleError(e);
        }
    }
    
    
    
    @Override
    public void openURLOccurrence() {
        try {
            String errorMessage = null;
            Point p = getTablePoint(mouseEvent);
            int realCol=convertColumnIndexToModel(p.y);
            int realRow=sorter.modelIndex(p.x);
            if(realRow >= 0 && realCol >= 0 && realRow < langs.length && realCol < 2) {
                String occurrence = topic.getData(type, langs[realRow]);
                if(occurrence != null) {
                    occurrence = occurrence.trim();
                    if(occurrence.length() > 0) {
                        try {
                            if(!DataURL.isDataURL(occurrence)) {
                                Desktop desktop = Desktop.getDesktop();
                                desktop.browse(new URI(occurrence));
                            }
                            else {
                                errorMessage = "Due to security restrictions Wandora can't open data-urls with desktop browser. "+
                                               "Copy data-url to clipboard and paste it to browser's address field.";
                            }
                        }
                        catch(IOException ioe) {
                            errorMessage = "IOException occurred while starting external browser for occurrence: "+ioe.getMessage();
                        }
                        catch(Exception e) {
                            if(occurrence.length() > 80) occurrence = occurrence.substring(0, 80)+"...";
                            errorMessage = "Exception '"+e.getMessage()+"' occurred while starting external browser for occurrence. Check if the occurrence text is a valid URL.";
                            e.printStackTrace();
                        }
                    }
                }
            }
            if(errorMessage != null) {
                WandoraOptionPane.showMessageDialog(Wandora.getWandora(), errorMessage, "Error while opening URL");
            }
        }
        catch(Exception e) {
            if(wandora != null) wandora.handleError(e);
        }
    }
    
    
    
    @Override
    public void downloadURLOccurrence() {
        try {
            Point p = getTablePoint(mouseEvent);
            int realCol=convertColumnIndexToModel(p.y);
            int realRow=sorter.modelIndex(p.x);
            if(realRow >= 0 && realCol >= 0 && realRow < langs.length && realCol < 2) {
                String occurrence = topic.getData(type, langs[realRow]);
                if(occurrence != null) {
                    occurrence = occurrence.trim();
                    if(occurrence.length() > 0) {
                        try {
                            URL url = new URL(occurrence);
                            int a = WandoraOptionPane.showConfirmDialog(wandora, "Make data-url instead of text occurrence?", "Make data-url", WandoraOptionPane.INFORMATION_MESSAGE);
                            if(a == WandoraOptionPane.YES_OPTION) {
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("HEAD");
                                connection.connect();
                                String contentType = connection.getContentType();
                                connection.disconnect();
                                
                                InputStream is = url.openStream();
                                
                                byte[] dataBytes = IOUtils.toByteArray(is);
                                DataURL dataURL = new DataURL(contentType, dataBytes);
                                topic.setData(type, langs[realCol-1], dataURL.toExternalForm());
                            }
                            else {
                                String occurrenceContent = IObox.doUrl(url);
                                topic.setData(type, langs[realCol-1], occurrenceContent);
                            }
                        }
                        catch(MalformedURLException mue) {
                            WandoraOptionPane.showMessageDialog(wandora, "The occurrence is invalid URL. Can't download.", "Invalid URL exception", WandoraOptionPane.ERROR_MESSAGE);
                        }
                        catch(IOException ioe) {
                            WandoraOptionPane.showMessageDialog(wandora, "Can't read the occurrence URL. IOException occurred ('"+ioe.getMessage()+"').", "Can't read the URL", WandoraOptionPane.ERROR_MESSAGE);
                        }
                        catch(TopicMapException tme) {
                            WandoraOptionPane.showMessageDialog(wandora, "Can't set the occurrence because of a topic map exception. Topic map doesn't function as intended", "Topic map exception occurred", WandoraOptionPane.ERROR_MESSAGE);
                        }
                        catch(Exception e) {
                            WandoraOptionPane.showMessageDialog(wandora, "Exception '"+e.getMessage()+"' occurred while downloading URL '"+occurrence+"'.", "Error downloading URL", WandoraOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        catch(Exception e) {
            if(wandora != null) wandora.handleError(e);
        }
    }
    
    
    @Override
    public void spread() {
        try {
            Point p = getTablePoint(mouseEvent);
            boolean overrideAll = false;
            if(mouseEvent != null && mouseEvent.isAltDown()) overrideAll = true;
            int realCol=convertColumnIndexToModel(p.y);
            int realRow=sorter.modelIndex(p.x);
            if(realRow >= 0 && realCol >= 0 && realRow < langs.length && realCol < 2) {
                String occurrenceText = topic.getData(type, langs[realRow]);
                for(int i=0; i<langs.length; i++) {
                    if(i != realRow) {
                        int override = WandoraOptionPane.YES_OPTION;
                        if(!overrideAll && topic.getData(type, langs[i]) != null) {
                            override = WandoraOptionPane.showConfirmDialog(wandora, "Override existing "+langs[i].getDisplayName()+" occurrence?", "Override existing occurrence?", WandoraOptionPane.YES_TO_ALL_NO_CANCEL_OPTION);
                            if(override == WandoraOptionPane.YES_TO_ALL_OPTION) overrideAll = true;
                            if(override == WandoraOptionPane.CANCEL_OPTION) return;
                        }
                        if(override == WandoraOptionPane.YES_OPTION || override == WandoraOptionPane.YES_TO_ALL_OPTION) {
                            topic.setData(type, langs[i], occurrenceText);
                        }
                    }
                }
            }
        }
        catch(Exception e) {
            if(wandora != null) wandora.handleError(e);
        }
    }


    @Override
    public void translate(int translationService) {
        try {
            Point p = getTablePoint(mouseEvent);
            boolean markTranslation = true;
            boolean overrideAll = false;
            if(mouseEvent != null && mouseEvent.isShiftDown()) markTranslation = false;
            if(mouseEvent != null && mouseEvent.isAltDown()) overrideAll = true;
            int realCol=convertColumnIndexToModel(p.y);
            int realRow=sorter.modelIndex(p.x);
            if(realRow >= 0 && realCol >= 0 && realRow < langs.length && realCol < 2) {
                Topic sourceLangTopic = langs[realRow];
                String occurrenceText = topic.getData(type, sourceLangTopic);
                for(int i=0; i<langs.length; i++) {
                    if(i != realCol-1) {
                        Topic targetLangTopic = langs[i];
                        String translatedOccurrenceText = null;
                        if(translationService == GOOGLE_TRANSLATE) {
                            translatedOccurrenceText = GoogleTranslateBox.translate(occurrenceText, sourceLangTopic, targetLangTopic, markTranslation);
                        }
                        else if(translationService == MICROSOFT_TRANSLATE) {
                            translatedOccurrenceText = MicrosoftTranslateBox.translate(occurrenceText, sourceLangTopic, targetLangTopic, markTranslation);
                        }
                        else if(translationService == WATSON_TRANSLATE) {
                            translatedOccurrenceText = WatsonTranslateBox.translate(occurrenceText, sourceLangTopic, targetLangTopic, markTranslation);
                        }
                        if(translatedOccurrenceText != null) {
                            int override = WandoraOptionPane.YES_OPTION;
                            if(!overrideAll && topic.getData(type, targetLangTopic) != null) {
                                override = WandoraOptionPane.showConfirmDialog(wandora, "Override existing "+targetLangTopic.getDisplayName()+" occurrence?", "Override existing occurrence?", WandoraOptionPane.YES_TO_ALL_NO_CANCEL_OPTION);
                                if(override == WandoraOptionPane.YES_TO_ALL_OPTION) overrideAll = true;
                                if(override == WandoraOptionPane.CANCEL_OPTION) return;
                            }
                            if(override == WandoraOptionPane.YES_OPTION || override == WandoraOptionPane.YES_TO_ALL_OPTION) {
                                topic.setData(type, targetLangTopic, translatedOccurrenceText);
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            if(wandora != null) wandora.handleError(e);
        }
    }


    
    // -------------------------------------------------------------------------
    
    
    public Point getTablePoint() {
        return getTablePoint(mouseEvent);
    }
    public Point getTablePoint(java.awt.event.MouseEvent e) {
        try {
            java.awt.Point p=e.getPoint();
            int row=rowAtPoint(p);
            int col=columnAtPoint(p);
            //int realCol=convertColumnIndexToModel(col);
            return new Point(row, col);
        }
        catch (Exception ex) {
            wandora.handleError(ex);
            return null;
        }
    }

    public Object getValueAt(MouseEvent e) {
        return getValueAt(getTablePoint(e));
    }
    public Object getValueAt(Point p) {
        return getValueAt(p.x, p.y);
    }
    @Override
    public Object getValueAt(int x, int y) {
        try { return getModel().getValueAt(x, convertColumnIndexToModel(y)); }
        catch (Exception e) { e.printStackTrace(); }
        return null;
    }
    
    
    // -------------------------------------------------------------------------
    
    
        
    @Override
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        this.mouseEvent = mouseEvent;
        //System.out.println("Mouse clicked!");
    }
    
    @Override
    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {

    }
    
    @Override
    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
    }
    
    @Override
    public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
        this.mouseEvent = mouseEvent;
    }
    
    @Override
    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
        this.mouseEvent = mouseEvent;
    }
    
    
    
    
    
    
    
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    
    
    
    private class DataCellRenderer implements TableCellRenderer {
        private SimpleTextPane occurrenceTextField = null;
        private SimpleLabel occurrenceInfoLabel = null;
        private JPanel occurrencePanel = null;
        
        private Color noOccurrenceColor = UIConstants.noContentBackgroundColor;
        private Color occurrenceInfoTextColor = new Color(150,150,150);
        private Color dataOccurrenceColor = new Color(240,255,250);
        
        public DataCellRenderer(){
            occurrenceTextField = new SimpleTextPane();
            occurrenceTextField.setOpaque(true);
            occurrenceTextField.setMargin(new Insets(0,0,0,0));
            Font f = occurrenceTextField.getFont();
            occurrenceTextField.setFont(new Font(f.getName(),Font.PLAIN,f.getSize()));
            occurrenceTextField.setBackground(Color.WHITE);
            occurrenceTextField.putClientProperty("html.disable", Boolean.TRUE);
            
            occurrenceInfoLabel = new SimpleLabel();
            occurrenceInfoLabel.setOpaque(true);
            occurrenceInfoLabel.setBackground(Color.WHITE);
            occurrenceInfoLabel.setPreferredSize(new Dimension(60,16));
            occurrenceInfoLabel.setAlignmentY(SimpleLabel.TOP_ALIGNMENT);
            occurrenceInfoLabel.setAlignmentX(SimpleLabel.RIGHT_ALIGNMENT);
            occurrenceInfoLabel.setHorizontalTextPosition(SimpleLabel.RIGHT);
            occurrenceInfoLabel.setHorizontalAlignment(SimpleLabel.RIGHT);
            occurrenceInfoLabel.setVerticalAlignment(SimpleLabel.TOP);
            occurrenceInfoLabel.setFocusable(false);
            
            BorderLayout layout = new BorderLayout();

            occurrencePanel = new JPanel();
            
            occurrencePanel.setLayout(layout);
            occurrencePanel.add(occurrenceTextField, BorderLayout.CENTER);
            occurrencePanel.add(occurrenceInfoLabel, BorderLayout.EAST);
        }
        
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(value == null) value = "";
            String occurrenceText = value.toString();
            String viewedOccurrenceText = occurrenceText;
            boolean isDataOccurrence = false;
            if(DataURL.isDataURL(viewedOccurrenceText)) {
                try {
                    Component preview = UIBox.getPreview(new DataURL(viewedOccurrenceText));
                    if(preview != null) {
                        return preview;
                    }
                }
                catch(MalformedURLException mue) {
                    System.out.println("Warning: Malformed data-url found.");
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                isDataOccurrence = true;
                viewedOccurrenceText = viewedOccurrenceText.substring(0, Math.max(5, viewedOccurrenceText.indexOf(',')));
            }
            else {
                if(viewedOccurrenceText.length() > 9999) {
                    viewedOccurrenceText = viewedOccurrenceText.substring(0, 9999)+"...";
                }
            }
            occurrenceTextField.setText(viewedOccurrenceText);
            String occurrenceInfoText = (occurrenceText.length() > 0 ? " "+occurrenceText.length()+" " : "");
            occurrenceInfoLabel.setText(occurrenceInfoText);
            Color c=colors[row];
            if(c != null) {
                occurrenceTextField.setForeground(c);
                occurrenceInfoLabel.setForeground(occurrenceInfoTextColor);
            }
            else {
                occurrenceTextField.setForeground(Color.BLACK);
                occurrenceInfoLabel.setForeground(occurrenceInfoTextColor);
            }
            if(occurrenceText.length() == 0) {
                occurrenceTextField.setBackground(noOccurrenceColor);
                occurrenceInfoLabel.setBackground(noOccurrenceColor);
            }
            else if(isDataOccurrence) {
                occurrenceTextField.setBackground(dataOccurrenceColor);
                occurrenceInfoLabel.setBackground(dataOccurrenceColor);
            }
            else {
                occurrenceTextField.setBackground(Color.WHITE);
                occurrenceInfoLabel.setBackground(Color.WHITE);
            }
            return occurrencePanel;
        }
    }
        
    
    
    
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    
    
    private class DataCellEditor extends AbstractCellEditor implements TableCellEditor, java.awt.event.MouseListener {        
        private Topic scope;
        private SimpleTextPane label;
        private String editedText;
        int realCol,realRow;
        

        
        
        public DataCellEditor(){
            label=new SimpleTextPane();
            label.setMargin(new Insets(0,0,0,0));
            Font f=label.getFont();
            label.setFont(new Font(f.getName(),Font.PLAIN,f.getSize()));
            label.addMouseListener(this);
            label.putClientProperty("html.disable", Boolean.TRUE);
        }
        
        
        
        
        @Override
        public Object getCellEditorValue() {
            return editedText;
        }
        
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if(value == null) value="";
            
            realCol=convertColumnIndexToModel(column);
            realRow=sorter.modelIndex(row);
            scope=langs[realRow];
            
            String viewedOccurrenceText = value.toString();
            if(viewedOccurrenceText.length() > 9999) {
                viewedOccurrenceText = viewedOccurrenceText.substring(0, 9999)+"...";
            }
            label.setText(viewedOccurrenceText);
            
            editedText=value.toString();
            return label;
        }
        
        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            if(label.contains(e.getPoint())){
                OccurrenceTextEditor ed = new OccurrenceTextEditor(wandora, true, editedText, topic, type, scope);
                String typeName = wandora.getTopicGUIName(type);
                String topicName = wandora.getTopicGUIName(topic);
                ed.setTitle("Edit occurrence text of '"+typeName+"' attached to '"+topicName+"'");
                ed.setVisible(true);
                
                if(ed.acceptChanges()) {
                    if(ed.getText() != null) {
                        editedText=ed.getText();
                        data[realRow]=editedText;
                    }
                    try {
                        boolean changed = applyChanges(topic, wandora);
                        if(changed) {
                            wandora.doRefresh();
                        }
                    }
                    catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            fireEditingStopped();
        }
        
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {}
        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {}
        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {}
        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {}

    }
    
    
    
    
    
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    
    
    
    private class TopicCellRenderer implements TableCellRenderer {
        private JLabel label;
        
        public TopicCellRenderer(){
            label=new JLabel("");
            label.setOpaque(true);
            Font f=label.getFont();
            label.setFont(new Font(f.getName(),Font.PLAIN,f.getSize()));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            int realRow = sorter.modelIndex(row);
            Topic scope = langs[realRow];
            label.setText(TopicToString.toString(scope));
            
            Color c=wandora.topicHilights.get(scope);
            if(c==null) c=wandora.topicHilights.getLayerColor(scope);
            if(c!=null) label.setForeground(c);
            else label.setForeground(Color.BLACK);
            if(column > 0) {
                label.setBackground(Color.WHITE);
            }
            return label;
        }
    }
    
    
    private class TopicCellEditor extends AbstractCellEditor implements TableCellEditor, java.awt.event.MouseListener {        
        private Topic scope;
        private JLabel label;
        
        public TopicCellEditor(){
            label= new JLabel();
            label.setOpaque(true);
            Font f=label.getFont();
            label.setFont(new Font(f.getName(),Font.PLAIN,f.getSize()));
            label.addMouseListener(this);
        }
        
        @Override
        public Object getCellEditorValue() {
            return TopicToString.toString(topic);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            int realRow = sorter.modelIndex(row);
            scope = langs[realRow];
            label.setText(TopicToString.toString(scope));
            Color c=wandora.topicHilights.get(scope);
            if(c==null) c=wandora.topicHilights.getLayerColor(scope);
            if(c!=null) label.setForeground(c);
            else label.setForeground(Color.BLACK);
            if(column > 0) {
                label.setBackground(Color.WHITE);
            }
            return label;
        }
        
        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            fireEditingStopped();
            if(label.contains(e.getPoint()) && scope!=null)
                wandora.openTopic(scope);
        }
        
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {}
        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {}
        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {}
        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {}
        
    }
    
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    
    
    private class DataTableModel extends AbstractTableModel {
        
        
        @Override
        public int getColumnCount() {
            return 2;
        }
        
        @Override
        public int getRowCount() {
            if(langs == null) return 0;
            return langs.length;
        }
        
        
        
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(columnIndex==0) {
                return langs[rowIndex];
            }
            else {
                return data[rowIndex];
            }
        }
        
        
        
        
        @Override
        public String getColumnName(int columnIndex){
            if(columnIndex==0) {
                return "Occurrence scope";
            }
            else if(columnIndex==1) {
                return "Occurrence data";
            }
            else {
                return "";
            }
        }
        
        
        @Override
        public boolean isCellEditable(int row,int col) {
            return true;
        }
        
    }
    
   
    
    
    // -------------------------------------------------------------------------
    
    
    
    private class OccurrencesTableTransferHandler extends TransferHandler {

        @Override
        public boolean canImport(TransferSupport support) {
            if(!support.isDrop()) return false;
            return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor) ||
                   support.isDataFlavorSupported(DataFlavor.stringFlavor) ||
                   support.isDataFlavorSupported(DnDHelper.topicDataFlavor);
        }

        
        
        @Override
        protected Transferable createTransferable(JComponent c) {
            String occurrenceText = null;
            try {
                Point p = getTablePoint(mouseEvent);
                int realCol=convertColumnIndexToModel(p.y);
                int realRow=sorter.modelIndex(p.x);
                if(realRow >= 0 && realCol > 0 && realRow < langs.length && realCol < 2) {
                    Topic sourceLangTopic = langs[realRow];
                    occurrenceText = topic.getData(type, sourceLangTopic);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            
            if(occurrenceText == null) {
                return null;
            }
            else {
                return new StringSelection(occurrenceText);
            }
        }

        @Override
        public int getSourceActions(JComponent c) {
            return TransferHandler.COPY_OR_MOVE;
        }

        @Override
        public boolean importData(TransferSupport support) {
            if(!support.isDrop()) return false;
            try {
                Transferable transferable = support.getTransferable();
                Point p = support.getDropLocation().getDropPoint();
                int realRow=rowAtPoint(p);
                int realCol=columnAtPoint(p);
                if(realRow >= 0 && realCol >= 0 && realRow < langs.length && realCol < 2) {
                    if(transferable.isDataFlavorSupported(DnDHelper.topicDataFlavor)) {
                        TopicMap tm=wandora.getTopicMap();
                        ArrayList<Topic> topics=DnDHelper.getTopicList(support, tm, true);
                        if(topics==null) return false;
                        
                        // DROP ON TYPE COLUMN ==> DUPLICATE/CHANGE TYPE
                        if(realCol == 0 && realRow == 0) {
                            if(type != null && !type.isRemoved()) {
                                Hashtable<Topic,String> os = topic.getData(type);
                                if(os != null && !os.isEmpty()) {
                                    for(Topic scope : os.keySet()) {
                                        if(scope != null && !scope.isRemoved()) {
                                            String occurrenceText = os.get(scope);
                                            for(Topic newType : topics) {
                                                if(newType != null && !newType.isRemoved()) {
                                                    topic.setData(newType, scope, occurrenceText);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                                
                        else if(realCol > 0) {
                            Topic scope = langs[realRow];
                            
                            if(type != null && scope != null && !type.isRemoved() && !scope.isRemoved()) {
                                for(Topic t : topics) {
                                    if(t != null && !t.isRemoved()) {
                                        String occurrenceText = t.getData(type, scope);
                                        if(occurrenceText != null) {
                                            topic.setData(type, scope, occurrenceText);
                                        }
                                    }
                                }
                            }
                        }
                        
                    }
                    else if(transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        try {
                            java.util.List<File> fileList = (java.util.List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                            if(fileList != null && !fileList.isEmpty()) {
                                
                                int a = WandoraOptionPane.showConfirmDialog(wandora, "Make data-url occurrence?", "Make data-url occurrence?", WandoraOptionPane.QUESTION_MESSAGE);
                                boolean makeDataURLOccurrence = (a == WandoraOptionPane.YES_OPTION ? true : false);
                                
                                for( File occurrenceFile : fileList ) {
                                    if(makeDataURLOccurrence) {
                                        DataURL dataUrl = new DataURL(occurrenceFile);
                                        topic.setData(type, langs[realRow], dataUrl.toExternalForm());
                                    }
                                    else {
                                        String content = null;

                                        String filename = occurrenceFile.getPath().toLowerCase();
                                        String suffix = filename.substring(Math.max(filename.lastIndexOf(".")+1, 0));

                                        // --- handle rtf files ---
                                        if("rtf".equals(suffix)) {
                                            content = Textbox.RTF2PlainText(new FileInputStream(occurrenceFile));
                                        }

                                        // --- handle pdf files ---
                                        else if("pdf".equals(suffix)) {
                                            try {
                                                PDDocument doc = PDDocument.load(occurrenceFile);
                                                PDFTextStripper stripper = new PDFTextStripper();
                                                content = stripper.getText(doc);
                                                doc.close();
                                            }
                                            catch(Exception e) {
                                                System.out.println("No PDF support!");
                                            }
                                        }

                                        // --- handle MS office files ---
                                        else if("doc".equals(suffix) ||
                                           "ppt".equals(suffix) ||
                                           "xsl".equals(suffix) ||
                                           "vsd".equals(suffix) 
                                           ) {
                                                content = MSOfficeBox.getText(occurrenceFile);
                                        }

                                        else if("docx".equals(suffix)) {
                                            content = MSOfficeBox.getDocxText(occurrenceFile);
                                        }
                                        
                                        else if("txt".equals(suffix)) {
                                            FileReader inputReader = new FileReader(occurrenceFile);
                                            content = IObox.loadFile(inputReader);
                                        }

                                        // --- handle everything else ---
                                        if(content == null) {
                                            FileReader inputReader = new FileReader(occurrenceFile);
                                            content = IObox.loadFile(inputReader);
                                        }
                                        
                                        if(content != null) {
                                            topic.setData(type, langs[realRow], content);
                                        }

                                    }
                                }
                            }
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else if(transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        try {
                            Object occurrenceText = support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                            if(occurrenceText != null) {
                                topic.setData(type, langs[realRow], occurrenceText.toString());
                            }
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    
                    wandora.doRefresh();
                }
            }
            catch(TopicMapException tme){tme.printStackTrace();}
            catch(Exception ce){}
            return false;
        }

    }
    
    
}
