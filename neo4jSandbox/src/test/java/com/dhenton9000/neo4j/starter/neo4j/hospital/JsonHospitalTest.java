/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.hospital;

import static com.dhenton9000.neo4j.hospital.HospitalDbMaker.*;
import com.dhenton9000.neo4j.hospital.json.Division;
import com.dhenton9000.neo4j.hospital.json.JSONHospitalServiceImpl;
import java.util.Iterator;
import java.util.Map;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.Iterable;
import scala.collection.immutable.List;

/**
 * tests the hospital tree to and from JSON
 * @author dhenton
 */

public class JsonHospitalTest extends HospitalTestBase {

    private final Logger logger = LoggerFactory.getLogger(JsonHospitalTest.class);
    private static JSONHospitalServiceImpl jsonService = new JSONHospitalServiceImpl();
    //  private static String tString = "";

    @BeforeClass
    public static void beforeClass() {
        prepareStaticHospitalTestDatabase();
        jsonService.setNeo4jDb(staticgraphDb);

    }

    @AfterClass
    public static void closeTheDatabase() {
        staticgraphDb.shutdown();
    }

    @Test
    public void JSONToObjectForHospitalDb() throws Exception {
        Division root =
                jsonService.buildDivison(PROGRAM_NAME);
        String temp = jsonService.structureToString(root);
        Division d2 = jsonService.stringToStructure(temp);
        assertEquals(d2.getChildren().get(2).getLabel(), root.getChildren().get(2).getLabel());
       // logger.info("\n"+temp);
    }
    
    @Test
    public void checkOnProviders() throws Exception
    {
          ExecutionEngine engine = new ExecutionEngine(staticgraphDb);
          String q = "start n=node:type_index(TYPE='PROVIDERS') return n.provider_display_property";
          final ExecutionResult executionResult = engine.execute(q);
          assertTrue(executionResult.size()>400);
        List<String> z = executionResult.columns();
        Iterable<scala.collection.immutable.Map<String, Object>> itemMap = executionResult.toIterable();
      //  assertEquals(4,itemMap.size());
        // assertTrue(executionResult.javaIterator().hasNext());
        //Map<String, Object> itemMap =  assertEquals("POO4",itemMap.
                 
    }
}
