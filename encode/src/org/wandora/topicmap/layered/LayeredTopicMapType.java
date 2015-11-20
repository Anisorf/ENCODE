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
 *
 * LayeredTopicMapType.java
 *
 * Created on 17. maaliskuuta 2006, 14:50
 *
 */

package org.wandora.topicmap.layered;
import org.wandora.application.Wandora;
import org.wandora.topicmap.*;
import org.wandora.utils.Options;
import java.io.*;
import org.wandora.topicmap.undowrapper.UndoTopicMap;
/**
 *
 * @author olli
 */
public class LayeredTopicMapType implements TopicMapType {
    
    public static boolean USE_UNDO_WRAPPED_TOPICMAPS = true;
    
    
    /** Creates a new instance of LayeredTopicMapType */
    public LayeredTopicMapType() {
    }

    
    @Override
    public void packageTopicMap(TopicMap tm, PackageOutput out, String path, TopicMapLogger logger) throws IOException,TopicMapException {
        LayerStack ls=(LayerStack)tm;
        Options options=new Options();
        int lcounter=0;
        String pathpre="";
        if(path.length()>0) pathpre=path+"/";
        Layer selectedLayer = ls.getSelectedLayer();
        for(Layer l : ls.getLayers()) {
            options.put("layer"+lcounter+".name",l.getName());
            TopicMap ltm = getWrappedTopicMap(l.getTopicMap());
            
            options.put("layer"+lcounter+".type",ltm.getClass().getName());
            options.put("layer"+lcounter+".visiblity", l.isVisible() ? "true" : "false");
            options.put("layer"+lcounter+".readonly", l.isReadOnly() ? "true" : "false");

            if(selectedLayer == l) options.put("layer"+lcounter+".selected", "true");
            lcounter++;
        }
        out.nextEntry(pathpre+"options.xml");
        // save options before everything else because we will need it before other files
        options.save(new OutputStreamWriter(out.getOutputStream()));
        
        lcounter=0;
        for(Layer l : ls.getLayers()) {
            TopicMap ltm = getWrappedTopicMap(l.getTopicMap());
            TopicMapType tmtype=TopicMapTypeManager.getType(ltm);
            logger.log("Saving layer '" + l.getName() + "'.");
            tmtype.packageTopicMap(ltm,out,pathpre+"layer"+lcounter, logger);
            lcounter++;
        }
    }

    
    
    private TopicMap getWrappedTopicMap(TopicMap tm) {
        if(tm != null) {
            if(tm.getClass().equals(UndoTopicMap.class)) {
                tm = ((UndoTopicMap) tm).getWrappedTopicMap();
            }
        }
        return tm;
    }
    
    
    
    @Override
    public TopicMap createTopicMap(Object params) {
        return new LayerStack();
    }
    
    @Override
    public TopicMap modifyTopicMap(TopicMap tm,Object params) throws TopicMapException {
        return tm;
    }

    
    @Override
    public TopicMap unpackageTopicMap(TopicMap topicmap, PackageInput in, String path, TopicMapLogger logger,Wandora wandora) throws IOException,TopicMapException {
        if(!(topicmap instanceof LayerStack)) return topicmap;
        if(topicmap == null) topicmap = new LayerStack();
        
        String pathpre="";
        if(path.length()>0) pathpre=path+"/";
        if(!in.gotoEntry(pathpre+"options.xml")){
            logger.log("Couldn't find options.xml in package.");
            logger.log("Aborting load.");
            return null;
        }
        Options options=new Options();
        options.parseOptions(new BufferedReader(new InputStreamReader(in.getInputStream())));
        
        LayerStack ls = (LayerStack) topicmap;
        Layer selectedLayer = null;

        int counter=0;
        while(true) {
            String layerName=options.get("layer"+counter+".name");
            if(layerName==null) break;
            
            String proposedLayerName = layerName;
            int layerCount = 1;
            while(ls.getLayer(proposedLayerName) != null) {
                proposedLayerName = layerName + " ("+layerCount+")";
                layerCount++;
            }
            layerName = proposedLayerName;

            String typeClass=options.get("layer"+counter+".type");
            
            // ENSURING BACKWARD COMPATABILITY!
            if("com.gripstudios.applications.wandora.topicmap.memory.TopicMapImpl".equals(typeClass)) {
                typeClass = "org.wandora.topicmap.memory.TopicMapImpl";
            }
            
            try {
                Class c=Class.forName(typeClass);
                TopicMapType type=TopicMapTypeManager.getType((Class<? extends TopicMap>)c);
                logger.log("Loading layer '" + layerName + "'.");
                TopicMap tm=type.unpackageTopicMap(in,pathpre+"layer"+counter, logger,wandora);
                Layer l = new Layer(tm,layerName,ls); 
                ls.addLayer(l);
                
                String layerVisibility = options.get("layer"+counter+".visiblity");
                String layerReadonly = options.get("layer"+counter+".readonly");
                String isSelected = options.get("layer"+counter+".selected");
                if("true".equalsIgnoreCase(layerVisibility)) l.setVisible(true);
                else l.setVisible(false);
                if("true".equalsIgnoreCase(layerReadonly)) l.setReadOnly(true);
                else l.setReadOnly(false);
                if("true".equalsIgnoreCase(isSelected)) selectedLayer = l;
            }
            catch(ClassNotFoundException cnfe){
                logger.log("Can't find class '"+typeClass+"', skipping layer.");
            }
            
            counter++;
        }
        if(selectedLayer != null) {
            ls.selectLayer(selectedLayer);
        }
        return ls;
    }
    
    
    
    @Override
    public TopicMap unpackageTopicMap(PackageInput in, String path, TopicMapLogger logger, Wandora wandora) throws IOException,TopicMapException {
        String pathpre="";
        if(path.length()>0) pathpre=path+"/";
        if(!in.gotoEntry(pathpre+"options.xml")){
            if(logger != null) {
                logger.log("Couldn't find options.xml in package.");
                logger.log("Aborting load.");
            }
            else {
                System.out.println("Couldn't find options.xml in package.");
                System.out.println("Aborting load.");
            }
            return null;
        }
        Options options=new Options();
        options.parseOptions(new BufferedReader(new InputStreamReader(in.getInputStream())));
        
        LayerStack ls=new LayerStack();
        Layer selectedLayer = null;
        if(logger == null) logger = ls;
        
        int counter=0;
        while(true && counter < 9999) {
            String layerName=options.get("layer"+counter+".name");
            if(layerName==null) break;
            
            String typeClass=options.get("layer"+counter+".type");
            // ENSURING BACKWARD COMPATABILITY!
            if("com.gripstudios.applications.wandora.topicmap.memory.TopicMapImpl".equals(typeClass)) {
                typeClass = "org.wandora.topicmap.memory.TopicMapImpl";
            }
            try {
                Class c=Class.forName(typeClass);
                TopicMapType type=TopicMapTypeManager.getType((Class<? extends TopicMap>)c);
                logger.log("Loading layer '" + layerName + "'.");
                TopicMap tm=type.unpackageTopicMap(in,pathpre+"layer"+counter, logger,wandora);
                
                Layer l = new Layer(tm,layerName,ls); 
                ls.addLayer(l);
                
                String layerVisibility = options.get("layer"+counter+".visiblity");
                String layerReadonly = options.get("layer"+counter+".readonly");
                String isSelected = options.get("layer"+counter+".selected");
                if("true".equalsIgnoreCase(layerVisibility)) l.setVisible(true);
                else l.setVisible(false);
                if("true".equalsIgnoreCase(layerReadonly)) l.setReadOnly(true);
                else l.setReadOnly(false);
                if("true".equalsIgnoreCase(isSelected)) selectedLayer = l;
            }
            catch(ClassNotFoundException cnfe){
                logger.log("Can't find topic map class '"+typeClass+"', skipping layer.");
            }
            counter++;
        }
        if(selectedLayer != null) {
            ls.selectLayer(selectedLayer);
        }
        return ls;
    }

    @Override
    public String getTypeName() {
        return "Layered";
    }
    
    @Override
    public String toString(){return getTypeName();}

    @Override
    public TopicMapConfigurationPanel getConfigurationPanel(Wandora admin, Options options) {
        return new TopicMapConfigurationPanel(){
            @Override
            public Object getParameters() {
                return new Object();
            }
        };
    }
    @Override
    public TopicMapConfigurationPanel getModifyConfigurationPanel(Wandora admin, Options options,TopicMap tm) {
        return null;
    }
    @Override
    public javax.swing.JMenuItem[] getTopicMapMenu(TopicMap tm,Wandora admin){
        return null;
    }
    @Override
    public String getTypeIcon(){
        return "gui/icons/layerinfo/layer_type_layered.png";
    }
    
}
