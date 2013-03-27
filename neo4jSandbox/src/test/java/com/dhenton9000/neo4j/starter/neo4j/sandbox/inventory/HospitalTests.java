/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox.inventory;

import com.dhenton9000.neo4j.starter.neo4j.sandbox.BaseNeo4jTest;
import static com.dhenton9000.neo4j.starter.neo4j.sandbox.inventory.HospitalDbMaker.*;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class HospitalTests extends BaseNeo4jTest {

    private final Logger logger = LoggerFactory.getLogger(HospitalTests.class);
    private static final int STATE_COUNT = 47;
    
    @BeforeClass
    public static void beforeClass() {
        prepareEmbeddedDatabase(DB_LOCATION);
    }

    @AfterClass
    public static void closeTheDatabase() {
        staticgraphDb.shutdown();
    }
    
    
    @Test
    public void testStateCount()
    {
        Index<Node> indexTypes = staticgraphDb.index().forNodes(TYPE_INDEX);
        IndexHits<Node> states = 
                indexTypes.get(NODE_TYPE.TYPE.toString(), 
                NODE_TYPE.STATE_DIVISIONS.toString());
        assertEquals(STATE_COUNT,states.size());
         
    }
    
    @Test
    public void testGetDivision()
    {
        String nodeName = "D012";
        Node dItem =   getDivisionNode(nodeName); 
        assertNotNull(dItem);
        assertEquals(nodeName,dItem.getProperty(DIVISION_DISPLAY_PROPERTY));
    }

    private Node getDivisionNode(String nodeName) {
        Index<Node> indexDivisionsDisplay = 
                staticgraphDb.index().forNodes(DIVISION_DISPLAY_INDEX);
        Node dItem = 
                indexDivisionsDisplay.get(DIVISION_DISPLAY_PROPERTY, nodeName).getSingle();
        return dItem;
    }
    
    
}
