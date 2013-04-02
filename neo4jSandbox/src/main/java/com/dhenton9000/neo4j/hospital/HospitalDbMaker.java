/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.hospital;

import static com.dhenton9000.neo4j.hospital.json.JSONHospitalService.*;
import com.dhenton9000.neo4j.hospital.json.JSONHospitalServiceImpl;
import com.dhenton9000.neo4j.utils.DatabaseHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
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
    public static final String PROGRAM_NAME = "Blue Cross";
    private GraphDatabaseService graphDb;
    public static final String DB_LOCATION = "/home/dhenton/neo4j/mydata/hospital.db";
    private JSONHospitalServiceImpl jService = new JSONHospitalServiceImpl();
    public static final HashMap<String, List<String>> divisionMap = new HashMap<String, List<String>>();
    String[] division1 = {"Maine", "New Hampshire", "Vermont",
        "Massachusetts", "Rhode Island", "Connecticut"};
    String[] division2 = {"New York", "Pennsylvania", "New Jersey"};
    String[] division3 = {"Wisconsin", "Michigan", "Illinois", "Indiana", "Ohio"};
    String[] division4 = {"Missouri", "North Dakota", "South Dakota",
        "Nebraska", "Kansas", "Minnesota", "Iowa"};
    String[] division5 = {"Delaware", "Maryland", "District of Columbia", "Virginia",
        "West Virginia", "North Carolina", "South Carolina", "Georgia", "Florida"};
    String[] division6 = {"Kentucky", "Tennessee", "Mississippi", "Alabama",
        "Arkansas", "Louisiana", "Oklahoma"};
    String[] division7 = {"Idaho", "Montana", "Wyoming", "Nevada", "Utah",
        "Colorado", "Arizona", "New Mexico"};
    String[] division8 = {"Alaska", "Washington", "Oregon", "California", "Hawaii"};
    String[] divisionName = {"New England", "Mid Atlantic",
        "East North Central", "West North Central", "East South Central",
        "West South Central", "Mountain", "Pacific"};
    // private Index<Node> indexDivisionsDisplay;
    //  private Index<Node> indexProvidersDisplay;
    //  private Index<Node> indexTypes;
    public int districtNumber = 0;
    public int providerNumber = 0;
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
    private void doDistricts() {


        for (Node state : stateArray) {
            addDistrictsAndProviders(state);
        }


    }

    private void addDistrictsAndProviders(Node rootNode) {

        Double d = Math.random() * 5d;
        int numDist = d.intValue() + 2;

        for (int i = 0; i < numDist; i++) {
            districtNumber++;

            String label = "D" + String.format("%03d", districtNumber);
            Node districtNode =
                    jService.createAndAttachDivisionNode(rootNode, label);
            addProviders(districtNode);
        }

    }

    private void addProviders(Node rootNode) {
        Double d = Math.random() * 3d;
        int numProviders = d.intValue() + 2;
        for (int i = 0; i < numProviders; i++) {
            providerNumber++;;

            String label = "P" + String.format("%03d", providerNumber);
            jService.createAndAttachProviderNode(rootNode, label);
          //  providerNode.setProperty(PROVIDER_DISPLAY_PROPERTY, label);



        }
    }

    private void doRegions() {


        String[] labels = {"Northeast", "Midwest", "South", "West"};
        Node refNode = graphDb.getReferenceNode();
        Node rootNode = jService.createAndAttachDivisionNode(refNode, PROGRAM_NAME);

        for (String label : labels) {
            jService.createAndAttachDivisionNode(rootNode, label);

        }


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

        Node regionNode = jService.getDivisionNode(region);

        //  Node regionNode = indexDivisionsDisplay.get(DIVISION_DISPLAY_PROPERTY, region).getSingle();
        for (int i = start; i < stop; i++) {
            List<String> items = divisionMap.get(divisionName[i]);
            // Node divNode = graphDb.createNode();
            String divLabel = divisionName[i];
            Node divNode = jService.createAndAttachDivisionNode(regionNode, divLabel);
            for (String stateLabel : items) {
                Node stateNode = jService.createAndAttachDivisionNode(divNode, stateLabel);
                stateArray.add(stateNode);
            }
        }


    }

    public void doDBCreate() throws Exception {

        Transaction tx = graphDb.beginTx();
        jService.setNeo4jDb(graphDb);


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


    }

    public void doShutdown() {
        graphDb.shutdown();

    }

    public void setNeo4jDb(GraphDatabaseService g) {
        graphDb = g;
    }

    public static void main(String[] args) {
        HospitalDbMaker hospitialDbCreator = new HospitalDbMaker();
        DatabaseHelper dbHelper = new DatabaseHelper();

        try {
            EmbeddedGraphDatabase g = dbHelper.createDatabase(DB_LOCATION, true);
            hospitialDbCreator.setNeo4jDb(g);
            hospitialDbCreator.doDBCreate();
            hospitialDbCreator.doShutdown();
        } catch (Exception ex) {
            logger.error("Problem ", ex);
        }
    }
}
