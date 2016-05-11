/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wandora.application.tools.lessonplan;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.wandora.utils.swing.GuiTools;
import org.wandora.utils.Tuples.*;
import org.wandora.topicmap.*;
import static org.wandora.utils.Tuples.t2;
import org.wandora.application.Wandora;
import org.wandora.application.WandoraTool;
import org.wandora.application.contexts.AssociationContext;
import org.wandora.application.contexts.Context;
import org.wandora.application.gui.topicstringify.TopicToString;
import org.wandora.application.tools.*;
import org.wandora.topicmap.layered.Layer;
import org.wandora.topicmap.layered.LayeredTopic;
import org.wandora.topicmap.layered.LayerStack;
import org.wandora.topicmap.layered.LayeredAssociation;
import org.wandora.topicmap.layered.LayeredTopicMapType;
import org.wandora.topicmap.memory.TopicMapImpl;
import org.wandora.topicmap.undowrapper.UndoAssociation;
import org.wandora.topicmap.undowrapper.UndoTopicMap;
import org.wandora.topicmap.undowrapper.UndoTopic;
import org.wandora.utils.ClipboardBox;
import org.wandora.application.tools.lessonplan.Permute;

/**
 *
 * @author Anisorf
 */
public class LearningPaths extends AbstractWandoraTool implements WandoraTool{
    public static final String SI = "http://www.eiffe-l.org/encode/si/lesson/";
    public static final String SCHEMA_SI = SI + "schema/";
    public static final String SCHEMA_TERM_ASSOCIATION_TYPE = "association-type";
    TopicMap topoMap =  new org.wandora.topicmap.memory.TopicMapImpl();
    
    
    private ArrayList<Topic> topicsPruned = new ArrayList<Topic>();
    
    private ArrayList<Topic> topics;
    private HashMap<String, LinkedList<Topic>>  adjList = new HashMap<String, LinkedList<Topic>>();
    private Topic primaryNotion, learningOutcome;
    private Topic role_prerequisite = null;
    private Topic role_subsidiary = null;
    private Topic aType;
    private Topic aPrecedence;
    private static Topic lessonplanT = null;
    private Layer lessonPlanLayer  = null;
    
    private Queue<Topic> queueIn = new LinkedList<Topic>();
    private Queue<Topic> queueOut = new LinkedList<Topic>();
    private HashMap<String, Topic> remainingT = new HashMap<>();
    private Queue<Topic> queueInN ;
    private Queue<Topic> queueOutN ;
    private HashMap<String, Topic> remainingTN ;
    
    private HashMap<String, Integer> indegreeN;
    private HashMap<String, Integer> indegree = new HashMap<String, Integer>();
    
    private HashMap<Integer, LinkedList<Topic>> alltoporders = new HashMap<Integer, LinkedList<Topic>>();
    private int countOrders;
   
    public static String LIST_GRAPH_SI = "http://www.eiffe-l.org/encode/si/core/lesson-plan/";
    public static String siPattern = "http://www.eiffe-l.org/encode/si/core/lesson-plan/lesson-plan_n_";
    public String basenameLP = "Lesson Plan ";
    public static boolean connectWithWandoraClass = true;
   
    private HashMap<String, Integer> marked = new HashMap<String, Integer>(); 
    
    private HashMap<Locator,Integer> level = new HashMap<Locator,Integer>();
    
    public LearningPaths() {
    }
    
    @Override
    public String getName() {
        return "Linearizing graph";
    }
    
    @Override
    public String getDescription() {
        return "Generates lesson plans as new layers. First a user choose the Primary Notion and the Learning Outcome, "
                + "than a modified topological sort generates paths, i.e. lesson plans, which are past to the new layers.";
    }
    
    @Override
    public boolean requiresRefresh() {
        return true;
    }
    
    @Override
    public void execute(Wandora wandora, Context context) throws TopicMapException {
        
        wandora.clipboardtm.clearTopicMapIndexes();
        TopicMap topicmap = solveContextTopicMap(wandora, context);
        /*Hashtable<Locator, Topic> subjectIdentifierIndex = ((TopicMapImpl)topicmap).getSubjectIdentifierIndex();
        Hashtable<String, Topic> nameIndex = ((TopicMapImpl)topicmap).getNameIndex();*/
        GenericOptionsDialog god=new GenericOptionsDialog(wandora,
            "Lesson Plan Generator",
            "Finds a suitable learning paths through the map, from the " +
            "selected Primary Notion through the Learning Outcome. " ,
            true,new String[][]{
            new String[]{"Name for the Lesson Plan","string",basenameLP,"Name for the created Lesson Plan topic."},
            
            new String[]{"Select Primary Notion topic","topic",null,"Select the starting topic for the Lesson Plan"},
            new String[]{"Select Learning Outcome topic","topic",null,"Select the ending topic for the Lesson Plan"},   
        },wandora);
        
        god.setSize(750, 300);
        GuiTools.centerWindow(god,wandora);
        god.setVisible(true);
        if(god.wasCancelled()) return;    
        Map<String,String> values=god.getValues(); 
        try {
            topics = (ArrayList)topicmap.getTopicsOfType("http://www.eiffe-l.org/encode/si/core/primary_notion"); //primary notion topics
            ArrayList<Topic> sNtopics = (ArrayList)topicmap.getTopicsOfType("http://www.eiffe-l.org/encode/si/core/secondary_notion"); //secondari notion topics
            topics.addAll(sNtopics); //primary e secondary notion topics
            
            primaryNotion = topicmap.getTopic(values.get("Select Primary Notion topic"));
            learningOutcome = topicmap.getTopic(values.get("Select Learning Outcome topic"));
            
            basenameLP = values.get("Name for the Lesson Plan");
            connectWithWandoraClass = "true".equalsIgnoreCase(values.get("Connect topics with Wandora class"));
            aType = topicmap.getTopic("http://www.eiffe-l.org/encode/si/core/is_req");
            role_prerequisite = topicmap.getTopic("http://www.eiffe-l.org/encode/si/core/prerequisite");
            role_subsidiary = topicmap.getTopic("http://www.eiffe-l.org/encode/si/core/subsidiary");
            aPrecedence = topicmap.getTopic("http://www.eiffe-l.org/encode/si/core/lesson-plan/precedence");
                     //wandora.getTopicMap().getLayer(basenameLP).getContainer()
                    //topicmap.getTopic("http://www.eiffe-l.org/encode/si/core/lesson-plan/precedence");
           
           /*Pruning the map keeping only the topics that have a path to LO*/
            this.topicsPruned.add(learningOutcome);
            pruning(learningOutcome);
            Collections.reverse(topicsPruned);
            System.out.println("Primary Notion = "+primaryNotion);
            System.out.println("Learning Outcome ="+learningOutcome);
            for(Topic t : topicsPruned){
                System.out.print(t.getBaseName()+"-"+t.getVisited().toString()+", ");
            }
            /**
             * Create the AdjacencyList from the pruned topics
             */
            for(Topic element: topicsPruned){  
                
                    remainingT.put(element.getBaseName(), element);
                    Collection<Association> assoTopic = element.getAssociations(aType, role_prerequisite);
                    Collection<Association> assoInD = element.getAssociations(aType, role_subsidiary);
                    int count_neighbours = 0;
                    if (assoInD!=null){
                        for(Association a: assoInD){
                            
                            if(a!=null)
                            {   String aname = a.getPlayer(role_subsidiary).getBaseName();
                                boolean visplayer = false;
                                for(Topic tp: topicsPruned) {
                                    if(tp.getBaseName() != null && tp.getBaseName().equals(aname)) {
                                        visplayer = tp.getVisited();
                                        
                                    }
                                }
                                if(visplayer){++count_neighbours;}
                            }    
                        }
                        indegree.put(element.getBaseName(), (Integer)count_neighbours);
                    }
                    Iterator<Association> iterAssoT = assoTopic.iterator();
                    Association association = null;
                    LinkedList<Topic> neighbours = new LinkedList<>();
                    /**
                     * Adjacency list
                     */
                    if(iterAssoT.hasNext()==true){
                        while(iterAssoT.hasNext()) {
                            association = iterAssoT.next();
                            Topic neighbour = association.getPlayer(role_subsidiary);
                            String neighname = neighbour.getBaseName();
                            boolean visited = false;
                            for(Topic tp: topicsPruned) {
                                if(tp.getBaseName() != null && tp.getBaseName().equals(neighname)) {
                                    visited = tp.getVisited();
                                    System.out.println(neighname+", ");
                                }
                            } 
                            if(visited){/*add neighbour only if that neighbour can reach the LO*/
                                neighbours.add(neighbour);
                                adjList.put(element.getBaseName(), neighbours);
                            }
                        }
                    }
                    else { adjList.put(element.getBaseName(), null);}
                    System.out.println("indegree "+element.getBaseName()+" = " + indegree.get(element.getBaseName()));
                    
            } 
          
            /**
             * Inizializza queueIn
             */
            for(String s : indegree.keySet()){
                
                if(indegree.get(s)==0){
                    Topic zeroT = topicmap.getTopicWithBaseName(s);
                    /* Topic zeroT = inputDAG.get(j).getFirst(); 
                    mi ritorna il primo elemento della lista dei
                    topic connessi col topic base che a me serve */
                    queueIn.add(zeroT);
                    System.out.println("queueIn Add = " +zeroT.getBaseName());
                }
            }
        
            
            /**
             * Finds one topo order
            **/
            while(queueIn.size()!=0){
                Topic dequeue = queueIn.peek();
                queueOut.add(dequeue);
                System.out.println("queueOut Add = "+dequeue.getBaseName());
                if(adjList.get(dequeue.getBaseName())!=null){
                    for(Topic t : adjList.get(dequeue.getBaseName())){
                        String tName = t.getBaseName(); 
                        System.out.println("InDegree["+tName+"] -- ="+indegree.get(tName));
                        int x = indegree.get(tName).intValue();
                        x--;
                        indegree.replace(tName, x);
                        System.out.println("InDegree["+tName+"] -- ="+indegree.get(tName));
                    
                        if(indegree.get(tName)==0){
                            queueIn.add(t);
                        }
                    }
                }
                
                queueIn.remove();
            }
            
            System.out.println("Topological order = ");
            for(Topic pathT : queueOut){
                System.out.print(pathT.getBaseName()+", ");
            }
            if(queueOut.isEmpty())
            {System.out.println("There is a cycle in the graph <=> no topological order");}
            
            
            
            LinkedList<Topic> lessonplan = new LinkedList();
           
            queueOut.stream().forEach((t) -> {
                lessonplan.add(t);
            });
            /*Iterator<Topic> lessonIter = lessonplan.listIterator();
            while(lessonIter.hasNext()){
                
                topoMap.copyTopicIn(lessonIter.next(), true);
                
            }*/
            
            /**
             * Find longhest path 
             */
            // Inizialized level
            Iterator<Topic> lIter = lessonplan.iterator();
            while(lIter.hasNext()){
                Topic t = lIter.next();
                Locator locator = t.getFirstSubjectIdentifier();
                level.put(locator,0);
            }
            longest_path(lessonplan,level, lessonplan.getLast()); 
             
            HashMap<Integer,ArrayList<Locator>> levelTopics = new HashMap<Integer,ArrayList<Locator>>();
           
            for (Map.Entry<Locator, Integer> entry : level.entrySet()){
                Locator key = entry.getKey();
                Integer value = entry.getValue();
                Topic t = topicmap.getTopic(key);
                if(levelTopics.containsKey(value)){
                    ArrayList<Locator> list1 = new ArrayList<Locator>();
                    list1 = levelTopics.get(value);
                    list1.add(key);
                    levelTopics.put(value,list1);
                }
                else
                {
                    ArrayList<Locator> list2 = new ArrayList<Locator>();
                    list2.add(key);
                    levelTopics.put(value,list2);
                }
            }
            GenericTree orderTree = new GenericTree();
            GenericTreeNode<List<Locator>> root = new GenericTreeNode<List<Locator>>();
            root.setData(levelTopics.get(0));
            orderTree.setRoot(root);
            GenericTreeNode<List<Locator>> nodePrevious = new GenericTreeNode<List<Locator>>();
            int y=0;
            List<GenericTreeNode<List<Locator>>> children = new ArrayList<GenericTreeNode<List<Locator>>>();
            /**
             * Creazione del albero quale traversamento root-leaf rapresenta i possibili ordinamenti:
             * Per ogni livello trovare prima tutte le possibili permutazioni e il set di nodi leaf 
             * (inizialmente = root), per ogni leaf aggiungere come child ogniuna delle permutazioni
             * dalla lista di liste
             */
            for(int z=1; z<levelTopics.size(); z++){
                ArrayList<Locator> l = levelTopics.get(z);
                List<List<Locator>> permute = new ArrayList<List<Locator>>();
                permute = Permute.permute(l);
                Set<GenericTreeNode<List<Locator>>> leafs = new HashSet<GenericTreeNode<List<Locator>>>();
                leafs = root.getAllLeafNodes();
                for(GenericTreeNode<List<Locator>> leaf:leafs){
                    for(List<Locator> p:permute){
                        GenericTreeNode<List<Locator>> node2 = new GenericTreeNode<List<Locator>>();
                        node2.setData(p);
                        leaf.addChild(node2);
                        
                    }
                }
                System.out.println("Level "+z+":");
            }
            List<GenericTreeNode<List<Locator>>> paths = root.getPaths();
            int z=0;
            String pn = primaryNotion.getBaseName();
            String sn = learningOutcome.getBaseName();
            Topic default_role_1 = topicmap.getTopic("http://wandora.org/si/core/default-role-1");
            Topic default_role_2 = topicmap.getTopic("http://wandora.org/si/core/default-role-2");
            Topic t_first = null;
            Topic t_next = null;
            Topic t_prev = null;
            Association a = null;
            for(GenericTreeNode<List<Locator>> p:paths){
                z++;
                System.out.println("");
                System.out.println(" Ordinamento numero "+z);
                String nome = " ";
                TopicMap newTM = new TopicMapImpl();
                createNewLayer(newTM, basenameLP, wandora, z);
                List<GenericTreeNode<List<Locator>>> nodo = p.children;
                Topic previous = null;
                for(GenericTreeNode<List<Locator>> n:nodo){
                    List<Locator> list = n.data;
                    for(Locator l:list){
                        Topic current = topicmap.getTopic(l);
                        newTM.copyTopicIn(current, true);
                        if(previous!=null){
                            a = wandora.getTopicMap().getLayer(basenameLP).getContainer().createAssociation(aPrecedence);
                            Topic t_prevL = wandora.getTopicMap().getLayer(basenameLP).getContainer().getTopicWithBaseName(previous.getBaseName());
                            Topic t_currentL = wandora.getTopicMap().getLayer(basenameLP).getContainer().getTopicWithBaseName(current.getBaseName());
                            a.addPlayer(t_prevL, default_role_1);
                            a.addPlayer(t_currentL, default_role_2);    
                        }
                        previous = current;
                
                        String current_name = current.getBaseName();
                        System.out.print(current_name+", " );  
                        nome+=current_name +" ,";    
                        
                    }    
                }  log("Ordinamento "+z+": " + nome);
            }
            /**
             * Creates new Layer for the Lesson Plan 
             */
            
           
            /**
            * A new layer and a precedence association is created in order to connect the topics in a topological order inside 
            * the lesson plan layer
            
            createNewLayer(topoMap, basenameLP, wandora);
            
            Iterator<Topic> lessonIter2 = lessonplan.listIterator();
            while(lessonIter2.hasNext()){
                a = wandora.getTopicMap().getLayer(basenameLP).getContainer().createAssociation(aPrecedence);
                Topic tOrderNext = lessonIter2.next();
                t_next =  wandora.getTopicMap().getLayer(basenameLP).getTopicMap().getTopicWithBaseName(tOrderNext.getBaseName());       
                if(t_prev!=null){ 
                            Topic t_prevL =  wandora.getTopicMap().getLayer(basenameLP).getContainer().getTopicWithBaseName(t_prev.getBaseName());
                            Topic t_nextL =  wandora.getTopicMap().getLayer(basenameLP).getContainer().getTopicWithBaseName(t_next.getBaseName());
                          
                            a.addPlayer(t_prevL, default_role_1);
                            a.addPlayer(t_nextL, default_role_2);
                        }
                        else {
                            t_first = lessonplan.getFirst();
                            t_prev = t_first;       
                        }
                        t_prev =  t_next;     
            } 
            */
               
        }
        catch(Exception e) {
            singleLog(e);
            System.out.println("Error in execution of tool Lesson Plan");
            return;
        }   
    }   
    
private ArrayList<Topic> pruning(Topic t) throws TopicMapException{
    Collection<Association> assosLO = t.getAssociations(aType, role_subsidiary);
    Iterator<Association> loIter = assosLO.iterator();
    t.setVisited(true);
    Association asLO = null;
    if(!loIter.hasNext() ){
        System.out.println("pruning no other reachable nodes");
    }
    else if(loIter.hasNext() && (t.getVisited()==true)){
      
            while(loIter.hasNext()){
                asLO = loIter.next();
                asLO.getType().isOfType(aType);
                Topic reachable = asLO.getPlayer(role_prerequisite);
                if(reachable!=null && reachable.getVisited()==null){
                    topicsPruned.add(reachable);
                    System.out.println("Added as reachable: "+reachable.getBaseName());
                    pruning(reachable);
                }   
            }  
        }
    
    return (topicsPruned);
}
    
    
private HashMap<String, Integer> newInDegree (){
    HashMap<String, Integer> indegreeN = new HashMap<>(); 
    for(Topic topic: topicsPruned){
      Collection<Association> assoInD;
        try {
            assoInD = topic.getAssociations(aType, role_subsidiary);
            int size = 0;
            for(Association a: assoInD){
                if(a.getPlayer(role_subsidiary).getVisited())
                {size++;}    
            }
            indegreeN.put(topic.getBaseName(), size);
        } catch (TopicMapException ex) {
            Logger.getLogger(LearningPaths.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    return indegreeN;
}
    
    private HashMap<Integer, LinkedList<Topic>> alltopsorts(Queue<Topic> queueOut, Queue<Topic> queueIn, HashMap<String, Topic> remainingT, HashMap<String, Integer> indegree) {
        if (queueIn.isEmpty()) {
            if ((queueOut.size() == adjList.size()) && remainingT.isEmpty()) {
                System.out.println("Topological solution if1: ");
                countOrders++;
                alltoporders.put((Integer) countOrders, (LinkedList<Topic>) queueOut);
                for (Topic t : queueOut) {
                    try {
                        System.out.print(t.getBaseName() + " ,");
                    } catch (TopicMapException ex) {
                        Logger.getLogger(LearningPaths.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //return queueOut;
            } else if (remainingT.isEmpty() && (queueOut.size() < adjList.size())) {
                System.out.println("Topological partial solution: ");
                for (Topic t : queueOut) {
                    try {
                        System.out.print(t.getBaseName() + " ,");
                    } catch (TopicMapException ex) {
                        Logger.getLogger(LearningPaths.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                //return queueOut;
            }
            /*else if(!remainingT.isEmpty()){
             System.out.println("Cycle!!!!");
             }*/
        } else {
            for (Topic dequeue : queueIn) {
                queueInN = new LinkedList<Topic>();
                for (Topic t : queueIn) {
                    queueInN.add(t);
                }
                queueOutN = new LinkedList<Topic>();
                for (Topic t : queueOut) {
                    queueOutN.add(t);
                }
                remainingTN = new HashMap<>();
                for (String key : remainingT.keySet()) {
                    Topic value = remainingT.get(key);
                    remainingTN.put(key, value);
                }
                indegreeN = new HashMap<>();
                for (String s : indegree.keySet()) {
                    Integer value = indegree.get(s);
                    indegreeN.put(s, value);
                }

                queueOutN.add(dequeue);
                try {
                    queueInN.remove(dequeue);
                    System.out.println("queueOut Add = " + dequeue.getBaseName());
                    if (adjList.get(dequeue.getBaseName()) != null) { // se ho vicini al nodo dequeue
                        for (Topic t : adjList.get(dequeue.getBaseName())) {
                            String tName = t.getBaseName();
                            int x = indegreeN.get(tName).intValue();
                            x--;
                            indegreeN.replace(tName, x);

                            if (indegreeN.get(tName) == 0) {
                                queueInN.add(t);
                            }
                        }
                    }
                    remainingTN.remove(dequeue.getBaseName(), dequeue);
                    alltopsorts(queueOutN, queueInN, remainingTN, indegreeN);
                } catch (Exception e) {
                }

            }
        }
        return alltoporders;
    }

    public static Topic getOrCreateTopic(TopicMap topoMap, String si, String basenameLP) {
        Topic topic = null;
        try {
            topic = topoMap.getTopic(si);
            if (topic == null) {
                topic = topoMap.createTopic();
                topic.addSubjectIdentifier(new Locator(si));
                if (basenameLP != null) {
                    topic.setBaseName(basenameLP);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return topic;
    }

    protected void createNewLayer(TopicMap topoMap, String streamName, Wandora wandora, int x) throws TopicMapException {
        if (topoMap != null) {
            if (wandora != null) {
                String s = streamName.replaceAll("\\d","");
                s+=x;
                basenameLP = s;
                LayerStack layerStack = wandora.getTopicMap();
                log("A new layer will be created for the Lesson Plan with name: '" + basenameLP + "'.");
                lessonPlanLayer = new Layer(topoMap, basenameLP, layerStack);
                lessonPlanLayer.setColor(000555);
                layerStack.addLayer(lessonPlanLayer);
                wandora.layerTree.resetLayers();
                wandora.layerTree.selectLayer(lessonPlanLayer);
                
                
            }
        }
        /*
        lessonplanT = getOrCreateTopic(wandora.getTopicMap(), "http://www.eiffe-l.org/encode/si/".concat(basenameLP), basenameLP);
        if (lessonplanT != null) {

            makeSubclassOfWandoraClass(lessonplanT, wandora.getTopicMap());
        }
        */

    }
    public void makeSubclassOfWandoraClass(Topic t, TopicMap tm) throws TopicMapException {
        if(tm != null && t != null) {
            Topic wandoraClass = tm.getTopic(TMBox.WANDORACLASS_SI);
            Topic superclassSubclass = tm.getTopic(XTMPSI.SUPERCLASS_SUBCLASS);
            Topic superclass = tm.getTopic(XTMPSI.SUPERCLASS);
            Topic subclass = tm.getTopic(XTMPSI.SUBCLASS);
            if(wandoraClass != null &&
                    superclassSubclass != null &&
                    superclass != null &&
                    subclass != null) {
                
                Association a = tm.createAssociation(superclassSubclass);
                a.addPlayer(t, subclass);
                a.addPlayer(wandoraClass, superclass);
            }
        }
    }
    public static Topic createInstanceTopic(TopicMap tm, Topic t) throws TopicMapException {
        if(t!= null) {
            makeSubclassOf(tm, t, lessonplanT);
        }
        return t;       
    }
    private static void makeSubclassOf(TopicMap tm, Topic t, Topic superclass) throws TopicMapException {
        Topic supersubClassTopic = getOrCreateTopic(tm, XTMPSI.SUPERCLASS_SUBCLASS, "superclass-subclass");
        Topic subclassTopic = getOrCreateTopic(tm, XTMPSI.SUBCLASS, "subclass");
        Topic superclassTopic = getOrCreateTopic(tm, XTMPSI.SUPERCLASS, "superclass");
        Association ta = tm.createAssociation(supersubClassTopic);
        ta.addPlayer(t, subclassTopic);
        ta.addPlayer(superclass, superclassTopic);
    }

    private void longest_path( LinkedList<Topic> lessonplan,HashMap<Locator,Integer> calclevel, Topic topic) throws TopicMapException {
        Locator key1 = (Locator) lessonplan.getFirst().getFirstSubjectIdentifier();  
        HashMap<Locator,Integer> levelmax=null;
        if(topic.getFirstSubjectIdentifier().equals(key1)){
            level.put(key1,0);  
        }
        else{
           
            Collection<Association> prerequisites = topic.getAssociations(aType, role_subsidiary);
            List<Topic> predecessors = new ArrayList<Topic>();
            for(Association a: prerequisites){
                Topic t = a.getPlayer(role_prerequisite); 
                Locator keyT = t.getFirstSubjectIdentifier();
                Integer i = level.get(keyT);
                i++;
                level.put(keyT, i);
                predecessors.add(t);
                
            }  
            int maxPredecessors = 0;
           
            for (Topic p:predecessors){
              longest_path(lessonplan, level, p); 
              Locator keyP = p.getFirstSubjectIdentifier(); 
              maxPredecessors  = Math.max(maxPredecessors , level.get(keyP));
            }
            //maxPredecessors = Collections.max(predecessors);
            Locator keyT = topic.getFirstSubjectIdentifier();
            level.put(keyT, maxPredecessors+1);    
        } 
       // return levelmax;
    }
    
}
