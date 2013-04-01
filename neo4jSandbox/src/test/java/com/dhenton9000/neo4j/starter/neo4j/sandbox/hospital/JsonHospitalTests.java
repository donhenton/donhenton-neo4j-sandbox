/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital;

import com.dhenton9000.neo4j.starter.neo4j.sandbox.BaseNeo4jTest;
import static com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital.HospitalDbMaker.*;
import com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital.HospitalDbMaker.NODE_TYPE;
import com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital.json.Division;
import com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital.json.JSONHospitalService;
import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class JsonHospitalTests extends BaseNeo4jTest {

    private final Logger logger = LoggerFactory.getLogger(HospitalTests.class);
    private static JSONHospitalService jsonService = new JSONHospitalService();
    //  private static String tString = "";

    @BeforeClass
    public static void beforeClass() {
        prepareEmbeddedDatabase(DB_LOCATION);
        jsonService.setNeo4jDb(staticgraphDb);
        // tString = "";
    }

    @AfterClass
    public static void closeTheDatabase() {
        staticgraphDb.shutdown();
    }

    @Test
    public void testBuildJSON() throws IOException {

        Division root = new Division();
        Node dItem = jsonService.getDivisionNode(PROGRAM_NAME);
        String nextItem = (String) dItem.getProperty(DIVISION_DISPLAY_PROPERTY);
        root.setLabel(nextItem);
        root.setId(dItem.getId());
        jsonService.buildJSON(dItem, root);
       
       // logger.info("\n" + temp);


    }
    
    
    @Test
    public void JSONToObject() throws Exception
    {
        Division root = new Division();
        Node dItem = jsonService.getDivisionNode(PROGRAM_NAME);
        String nextItem = (String) dItem.getProperty(DIVISION_DISPLAY_PROPERTY);
        root.setLabel(nextItem);
        root.setId(dItem.getId());
        jsonService.buildJSON(dItem, root);
        String temp = jsonService.structureToString(root);
        Division d2 = jsonService.stringToStructure(temp);
        assertEquals(d2.getChildren().get(2).getLabel(),root.getChildren().get(2).getLabel());
        
        
        
        
    }

}
