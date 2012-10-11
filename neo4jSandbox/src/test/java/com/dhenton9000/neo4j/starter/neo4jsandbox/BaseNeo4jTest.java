/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4jsandbox;

import java.util.Map;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.ImpermanentGraphDatabase;
import org.neo4j.test.TestGraphDatabaseFactory;

/**
 *
 * @author dhenton
 */
public class BaseNeo4jTest {

    private GraphDatabaseService graphDb;

    
    /**
     * this would be called in the @Before
     */
    protected void prepareTestDatabase() {
         
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabaseBuilder().newGraphDatabase();
    }

    
   
   /**
     * this would be called in the @Before, in this case we can set 
     * properties
     * 
        Map<String, String> config = new HashMap<String, String>();
        config.put( "neostore.nodestore.db.mapped_memory", "10M" );
        config.put( "string_block_size", "60" );
        config.put( "array_block_size", "300" ); 
     * 
     * 
     */
    protected void prepareTestDatabase(Map<String,String> config) {
         
        graphDb = new ImpermanentGraphDatabase( config );
    }

    
    
    /**
     *   this would be called in the @After method
     */
    protected void destroyTestDatabase() {
        getGraphDb().shutdown();
    }

    /**
     * @return the graphDb
     */
    protected GraphDatabaseService getGraphDb() {
        return graphDb;
    }
}



