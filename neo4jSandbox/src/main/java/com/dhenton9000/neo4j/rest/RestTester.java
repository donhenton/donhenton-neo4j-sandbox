/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.rest;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.kernel.impl.util.StringLogger;
import org.neo4j.rest.graphdb.RestAPIFacade;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstration class for the rest API wrapper. The wrapper treats a remote
 * database as if it were local
 * @author dhenton
 */
public class RestTester {

    private final Logger log = LoggerFactory.getLogger(RestTester.class);
    public void doTest() {
        GraphDatabaseService gds = new RestGraphDatabase("http://localhost:7474/db/data");
        RestAPIFacade f = new RestAPIFacade("http://localhost:7474/db/data");
        StringLogger logger = StringLogger.logger(new File("messages.log"));
        Node n = gds.getNodeById(34);
        log.info("node is "+n.getProperty("division_display_property"));
        
        
        Map<String, String> mMap = getInitialNodes(gds,logger);
        for (String k:mMap.keySet())
        {
            log.info("Key: "+k+" --> "+mMap.get(k));
        }
        
        

    }
    
    
    public Map<String, String> getInitialNodes(GraphDatabaseService gds,StringLogger logger) {
        HashMap<String, String> items = new HashMap<String, String>();
        String q = "start n=node(0) match n-[IS_DIVIDED_INTO*1]->b "
                + "return b.division_display_property "
                + "as name, ID(b) as id";

        ExecutionEngine engine = new ExecutionEngine(gds,logger);

        log.info(" q\n" + q);
        final ExecutionResult executionResult = engine.execute(q);

        Iterator<Map<String, Object>> columnsData = executionResult.javaIterator();


        while (columnsData.hasNext()) {
            Map<String, Object> z = columnsData.next();
            String nameString = (String) z.get("name");
            Long key = (Long) z.get("id");
            items.put(key.toString(), nameString);
        }

        if (items.isEmpty()) {
            return null;
        } else {
            return items;
        }
    }
    
    

    public static void main(String[] args) {
        (new RestTester()).doTest();
    }
}
