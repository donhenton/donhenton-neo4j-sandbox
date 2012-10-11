/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox.db;

import java.io.File;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 *
 * @author dhenton
 */
public abstract class AbstractEmbeddedDBCreator {

    private GraphDatabaseService graphDb;

    /**
     * run this in the derived classes main method
     *
     * @param dbLocation
     */
    public void setUp(String dbLocation) {
        deleteFileOrDirectory(new File(dbLocation));
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbLocation);
        registerShutdownHook();
        createNodespace();
    }

    /**
     * this is the code for setting up the database
     */
    public abstract void createNodespace();

    /**
     * delete a directory and all subdirectories
     *
     * @param file
     */
    private void deleteFileOrDirectory(final File file) {
        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteFileOrDirectory(child);
            }
        } else {
            file.delete();
        }
    }

    /**
     * clean up on shutdown
     */
    private void registerShutdownHook() {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running example before it's completed)
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                getGraphDb().shutdown();
            }
        });
    }

    /**
     * @return the graphDb
     */
    public GraphDatabaseService getGraphDb() {
        return graphDb;
    }
}
