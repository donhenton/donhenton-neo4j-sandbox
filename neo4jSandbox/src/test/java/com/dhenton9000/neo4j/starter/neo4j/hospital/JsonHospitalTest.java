/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.hospital;

import static com.dhenton9000.neo4j.hospital.HospitalDbMaker.*;
import com.dhenton9000.neo4j.hospital.json.Division;
import com.dhenton9000.neo4j.hospital.json.JSONHospitalServiceImpl;
import java.util.Iterator;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.neo4j.helpers.collection.IteratorUtil.asIterable;
import org.neo4j.kernel.impl.util.StringLogger;
import org.neo4j.kernel.logging.LogbackService.Slf4jStringLogger;
 
 

/**
 * tests the hospital tree to and from JSON
 *
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
    public void checkOnProviders() throws Exception {
        String cLabel = "item";
        String nodeResult = "";
        ExecutionEngine engine = new ExecutionEngine(staticgraphDb,StringLogger.SYSTEM);
        String q = "start n=node:type_index(TYPE='PROVIDERS') return n.provider_display_property as "+ cLabel +" order by n.provider_display_property";
        logger.info(" q\n"+q);
        final ExecutionResult executionResult = engine.execute(q);
        assertTrue(executionResult.size() > 400);
        
        
       // logger.info(executionResult.dumpToString());
        String[] var = new String[600];
        executionResult.copyToArray(var);
        logger.debug("var "+var[3]);
        
        List<String> columns = executionResult.javaColumns();
        assertEquals(1,columns.size());
        assertEquals(cLabel,columns.get(0));
        
        Iterator<Object> columnAs = executionResult.javaColumnAs(cLabel);
         logger.info("iter 1 "+columnAs);
        Iterable<Object> zz = asIterable(columnAs);
        Iterator iter = zz.iterator();
        logger.info("iter 2 "+iter);
        while (iter.hasNext())
        {
            logger.info(""+iter.next());
        }
 
    }
}
