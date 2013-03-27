/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox.inventory;

import com.dhenton9000.neo4j.utils.DatabaseHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.actors.threadpool.Arrays;

/**
 *
 * @author dhenton
 */
public class HospitalDbMaker {

    private static final Logger logger = LoggerFactory.getLogger(HospitalDbMaker.class);
    public static final String STATE_ROOT_LABEL = "STATES";
    private EmbeddedGraphDatabase graphDb;
    public static final String DB_LOCATION = "/home/dhenton/neo4j/mydata/hospital.db";
    //public static final String DIVISION_INDEX_NAME = "division_index";
    //public static final String DIVISION_INDEX_LABEL = "division";
    public static final String DIVISION_DISPLAY_PROPERTY = "division_display_property";
    public static final String PROVIDER_DISPLAY_PROPERTY = "provider_display_property";
    public static final String DIVISION_DISPLAY_INDEX = "division_display_index";
    public static final String PROVIDER_DISPLAY_INDEX = "provider_display_index";
    public static final String PROVIDER_DB_KEY = "provider_db_key";
    public static final String TYPE_INDEX = "type_index";
    public static final HashMap<String, List<String>> divisionMap = new HashMap<String, List<String>>();
    String[] division1 = {"Maine", "New Hampshire", "Vermont",
        "Massachusetts", "Rhode Island", "Connecticut"};
    String[] division2 = {"New York", "Pennsylvania", "New Jersey"};
    String[] division3 = {"Wisconsin", "Michigan", "Illinois", "Indiana", "Ohio"};
    String[] division4 = {"Missouri", "North Dakota", "South Dakota",
        "Nebraska", "Kansas", "Minnesota", "Iowa"};
    String[] division5 = {"Delaware", "Maryland", "District of Columbia", "Virginia",
        "West Virginia", "North Carolina", "South Carolina", "Georgia", "Florida"};
    String[] division6 = {"Kentucky", "Tennessee", "Mississippi", "Alabama"};
    String[] division7 = {"Idaho", "Montana", "Wyoming", "Nevada", "Utah",
        "Colorado", "Arizona", "New Mexico"};
    String[] division8 = {"Alaska", "Washington", "Oregon", "California", "Hawaii"};
    String[] divisionName = {"New England", "Mid Atlantic",
        "East North Central", "West North Central", "East South Central",
        "West South Central", "Mountain", "Pacific"};
    private Index<Node> indexDivisionsDisplay;
    private Index<Node> indexProvidersDisplay;
    private Index<Node> indexTypes;
    public int districtNumber = 0;
    public int providerNumber = 0;

    public enum RelationshipTypes implements RelationshipType {

        IS_DIVIDED_INTO,
        IS_PART_OF,
        PROVIDES_SERVICE_TO,
        DERIVES_SERVICE_FROM
    }

    public enum NODE_TYPE {

        TYPE, DIVISIONS, PROVIDERS, STATE_DIVISIONS, DISTRICTS
    }
    private List<Node> stateArray = new ArrayList<Node>();

    /*
    
     Region 1 (Northeast)
     Division 1 (New England) Maine, New Hampshire, Vermont, Massachusetts, Rhode Island, Connecticut
     Division 2 (Mid-Atlantic) New York, Pennsylvania, New Jersey
     Region 2 (Midwest) 
     Division 3 (East North Central) Wisconsin, Michigan, Illinois, Indiana, Ohio
     Division 4 (West North Central) Missouri, North Dakota, South Dakota, Nebraska, Kansas, Minnesota, Iowa
     Region 3 (South)
     Division 5 (South Atlantic) Delaware, Maryland, District of Columbia, Virginia, West Virginia, North Carolina, South Carolina, Georgia, Florida
     Division 6 (East South Central) Kentucky, Tennessee, Mississippi, Alabama
     Division 7 (West South Central) Oklahoma, Texas, Arkansas, Louisiana
     Region 4 (West)
     Division 8 (Mountain) Idaho, Montana, Wyoming, Nevada, Utah, Colorado, Arizona, New Mexico
     Division 9 (Pacific) Alaska, Washington, Oregon, California, Hawaii
    
    
     */
    /*
     private void doDistricts() {
     TraversalDescription states =
     Traversal.description()
     .relationships(RelationshipTypes.IS_STATE, Direction.BOTH)
     .breadthFirst()
     .evaluator(new Evaluator() {
     @Override
     public Evaluation evaluate(Path path) {
     if (path.endNode().getProperty(DIVISION_DISPLAY_PROPERTY).equals(STATE_ROOT_LABEL)) 
     {
     return Evaluation.EXCLUDE_AND_CONTINUE;
     }
     else
     {
     return Evaluation.INCLUDE_AND_CONTINUE;
     }
                

     }
     });
     Iterable<Node> nodeList = states.traverse(stateRootNode).nodes();
        
     Iterator<Node> iter = nodeList.iterator();
     while (iter.hasNext()) {
     Node t = iter.next();
     logger.debug("xx " + t.getProperty(DIVISION_DISPLAY_PROPERTY), "NA");
     addDistricts(t);
     }


     }
     */
    private void doDistricts() {
        IndexHits<Node> stateList = indexTypes.get(NODE_TYPE.TYPE.toString(), NODE_TYPE.STATE_DIVISIONS);

        for (Node state : stateList) {
            addDistricts(state);
        }


    }

    private void addDistricts(Node rootNode) {

        Double d = Math.random() * 5d;
        int numDist = d.intValue() + 2;

        for (int i = 0; i < numDist; i++) {
            districtNumber++;
            Node districtNode = graphDb.createNode();
            String label = "D" + String.format("%03d", districtNumber);
            districtNode.setProperty(DIVISION_DISPLAY_PROPERTY, label);
            indexDivisionsDisplay.add(districtNode, DIVISION_DISPLAY_PROPERTY, label);
            indexTypes.add(districtNode, NODE_TYPE.TYPE.toString(), NODE_TYPE.DISTRICTS.toString());
            rootNode.createRelationshipTo(districtNode, RelationshipTypes.IS_DIVIDED_INTO);
            districtNode.createRelationshipTo(rootNode, RelationshipTypes.IS_PART_OF);
            districtNode.setProperty( NODE_TYPE.TYPE.toString(), NODE_TYPE.DIVISIONS.toString());
            addProviders(districtNode);
        }

    }

    private void addProviders(Node rootNode) {
        Double d = Math.random() * 3d;
        int numProviders = d.intValue() + 2;
        for (int i = 0; i < numProviders; i++) {
            providerNumber++;;
            Node providerNode = graphDb.createNode();
            String label = "P" + String.format("%03d", providerNumber);
            providerNode.setProperty(PROVIDER_DISPLAY_PROPERTY, label);
            indexTypes.add(providerNode, NODE_TYPE.TYPE.toString(), NODE_TYPE.PROVIDERS.toString());
            rootNode.createRelationshipTo(providerNode, RelationshipTypes.DERIVES_SERVICE_FROM);
            providerNode.createRelationshipTo(rootNode, RelationshipTypes.PROVIDES_SERVICE_TO);
            indexProvidersDisplay.add(providerNode, PROVIDER_DISPLAY_PROPERTY, label);
            providerNode.setProperty(PROVIDER_DB_KEY, providerNumber);
            providerNode.setProperty(NODE_TYPE.TYPE.toString(), NODE_TYPE.PROVIDERS.toString());
            indexProvidersDisplay.add(providerNode, PROVIDER_DB_KEY, providerNumber);


        }
    }

    private void doRegions() {


        String[] labels = {"Northeast", "Midwest", "South", "West"};
        Node rootNode = graphDb.getReferenceNode();
        for (String label : labels) {
            //logger.debug("adding " + label);

            Node divNode = graphDb.createNode();
            divNode.setProperty(DIVISION_DISPLAY_PROPERTY, label);
            indexDivisionsDisplay.add(divNode, DIVISION_DISPLAY_PROPERTY, label);
            indexTypes.add(divNode, NODE_TYPE.TYPE.toString(), NODE_TYPE.DIVISIONS.toString());
            rootNode.createRelationshipTo(divNode, RelationshipTypes.IS_DIVIDED_INTO);
            divNode.createRelationshipTo(rootNode, RelationshipTypes.IS_PART_OF);
            divNode.setProperty( NODE_TYPE.TYPE.toString(), NODE_TYPE.DIVISIONS.toString());
        }

        // stateRootNode = graphDb.createNode();
        // stateRootNode.setProperty(DIVISION_DISPLAY_PROPERTY, STATE_ROOT_LABEL);
        // rootNode.createRelationshipTo(stateRootNode, RelationshipTypes.STATE_ROOT);

    }

    private void setUpDivisions() {


        // Northeast
        divisionMap.put(divisionName[0], Arrays.asList(division1));
        divisionMap.put(divisionName[1], Arrays.asList(division2));
        // Midwest
        divisionMap.put(divisionName[2], Arrays.asList(division3));
        divisionMap.put(divisionName[3], Arrays.asList(division4));
        //South
        divisionMap.put(divisionName[4], Arrays.asList(division5));
        divisionMap.put(divisionName[5], Arrays.asList(division6));
        //West
        divisionMap.put(divisionName[6], Arrays.asList(division7));
        divisionMap.put(divisionName[7], Arrays.asList(division8));

    }

    private void doRegionAndAddStates(String region, int start, int stop) {

        Node regionNode = indexDivisionsDisplay.get(DIVISION_DISPLAY_PROPERTY, region).getSingle();
        for (int i = start; i < stop; i++) {
            List<String> items = divisionMap.get(divisionName[i]);
            Node divNode = graphDb.createNode();
            String divLabel = divisionName[i];
            divNode.setProperty(DIVISION_DISPLAY_PROPERTY, divLabel);
            regionNode.createRelationshipTo(divNode, RelationshipTypes.IS_DIVIDED_INTO);
            divNode.createRelationshipTo(regionNode, RelationshipTypes.IS_PART_OF);
            divNode.setProperty( NODE_TYPE.TYPE.toString(), NODE_TYPE.DIVISIONS.toString());
            indexTypes.add(divNode, NODE_TYPE.TYPE.toString(), NODE_TYPE.DIVISIONS.toString());
            for (String label : items) {
                Node stateNode = graphDb.createNode();
                stateNode.setProperty(DIVISION_DISPLAY_PROPERTY, label);
                // indexDivisions.add(stateNode, DIVISION_INDEX_LABEL, divLabel);
                indexDivisionsDisplay.add(stateNode, DIVISION_DISPLAY_PROPERTY, label);
                indexTypes.add(stateNode, NODE_TYPE.TYPE.toString(), NODE_TYPE.DIVISIONS.toString());
                indexTypes.add(stateNode, NODE_TYPE.TYPE.toString(), NODE_TYPE.STATE_DIVISIONS.toString());
                divNode.createRelationshipTo(stateNode, RelationshipTypes.IS_DIVIDED_INTO);
                stateNode.createRelationshipTo(divNode, RelationshipTypes.IS_PART_OF);
                stateNode.setProperty( NODE_TYPE.TYPE.toString(), NODE_TYPE.DIVISIONS.toString());
                stateArray.add(stateNode);
                

            }
        }


    }

    public void doDBCreate() throws Exception {
        DatabaseHelper dbHelper = new DatabaseHelper();
        graphDb = dbHelper.createDatabase(DB_LOCATION, true);
        Transaction tx = graphDb.beginTx();
        indexDivisionsDisplay = graphDb.index().forNodes(DIVISION_DISPLAY_INDEX);
        indexProvidersDisplay = graphDb.index().forNodes(PROVIDER_DISPLAY_INDEX);
        indexTypes = graphDb.index().forNodes(TYPE_INDEX);


        try {

            setUpDivisions();
            doRegions();
            doRegionAndAddStates("Northeast", 0, 2);
            doRegionAndAddStates("Midwest", 2, 4);
            doRegionAndAddStates("South", 4, 6);
            doRegionAndAddStates("West", 6, 8);
            doDistricts();

            tx.success();
        } finally {
            tx.finish();
        }


        //  dbHelper.dumpGraphToConsole(graphDb);
        graphDb.shutdown();

    }

    public static void main(String[] args) {
        HospitalDbMaker hospitialDbCreator = new HospitalDbMaker();
        try {
            hospitialDbCreator.doDBCreate();
        } catch (Exception ex) {
            logger.error("Problem ", ex);
        }
    }
}
