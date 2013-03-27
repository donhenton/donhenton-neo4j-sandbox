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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class HospitalTests extends BaseNeo4jTest {

    private final Logger logger = LoggerFactory.getLogger(HospitalTests.class);

    @BeforeClass
    public static void beforeClass() {
        prepareEmbeddedDatabase(DB_LOCATION);
    }

    @AfterClass
    public static void closeTheDatabase() {
        staticgraphDb.shutdown();
    }
    
    
    @Test
    public void testThings()
    {
        assertTrue(true);
    }
    
}
