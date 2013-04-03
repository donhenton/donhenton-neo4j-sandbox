/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.hospital;

import com.dhenton9000.neo4j.hospital.HospitalDbMaker;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.test.ImpermanentGraphDatabase;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class HospitalTestBase {

    private GraphDatabaseService graphDb;
    protected static GraphDatabaseService staticgraphDb;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HospitalTestBase.class);

    protected static void prepareEmbeddedDatabase(String dbLocation) {
        staticgraphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbLocation);
        registerShutdownHook();
    }

    public static File createTempDatabaseDir() {

        File d;
        try {
            d = File.createTempFile("hospital-test", "dir");
            logger.debug(String.format("Created a new Neo4j database at [%s]", d.getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!d.delete()) {
            throw new RuntimeException("temp config directory pre-delete failed");
        }
        if (!d.mkdirs()) {
            throw new RuntimeException("temp config directory not created");
        }
        d.deleteOnExit();
        return d;
    }

    /**
     * this would be called in the
     *
     * @Before
     */
    protected void prepareTestDatabase() {

        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabaseBuilder().newGraphDatabase();
    }

    /**
     * this would be called in the
     *
     * @Before, in this case we can set properties
     *
     * Map<String, String> config = new HashMap<String, String>(); config.put(
     * "neostore.nodestore.db.mapped_memory", "10M" ); config.put(
     * "string_block_size", "60" ); config.put( "array_block_size", "300" );
     *
     *
     */
    protected void prepareTestDatabase(Map<String, String> config) {

        graphDb = new ImpermanentGraphDatabase(config);
    }

    protected static void prepareStaticHospitalTestDatabase() {

        prepareEmbeddedDatabase(createTempDatabaseDir().getAbsolutePath());
        registerShutdownHook();
        HospitalDbMaker hMaker = new HospitalDbMaker();
        hMaker.setNeo4jDb(staticgraphDb);
        try {
            hMaker.doDBCreate();
        } catch (Exception ex) {
            throw new RuntimeException("database create problem "
                    + ex.getClass() + " " + ex.getMessage());
        }

    }

    /**
     * this would be called in the
     *
     * @After method
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

    /**
     * This will connect to an already primed and loaded embedded folder area
     *
     * @param dbFileFolder
     */
    protected void connectToEmbedded(String dbFileFolder) {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbFileFolder);
        registerShutdownHook();
    }

    private static void registerShutdownHook() {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running example before it's completed)
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                staticgraphDb.shutdown();
            }
        });
    }
}
