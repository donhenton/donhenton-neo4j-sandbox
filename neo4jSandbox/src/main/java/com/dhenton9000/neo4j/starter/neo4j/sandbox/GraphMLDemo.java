/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.pgm.util.io.graphml.GraphMLWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Don
 */
public class GraphMLDemo {

    public static final String MAXTRIX_DB_LOCATION = "target/matrixDB";
 private final Logger logger = LoggerFactory.getLogger(GraphMLDemo.class);
    public static void main(String[] args) {
        GraphMLDemo gd = new GraphMLDemo();
        try {
            gd.doDrWhoDemo();
        } catch (IOException ex) {
        }
    }

    private void doDemo() throws IOException {
        Graph graph = new Neo4jGraph(MAXTRIX_DB_LOCATION);
        GraphMLWriter.outputGraph(graph, new FileOutputStream("mygraph-unlabeled.graphml"));

    }
    
     private void doYedDemo() throws IOException {
        Graph graph = new Neo4jGraph(MAXTRIX_DB_LOCATION);
        YedFileWriter yedWriter = new YedFileWriter(graph,"Text");
        yedWriter.outputGraph("yedwriter.graphml");
        

    }
     
      private void doDrWhoDemo() throws IOException {
        Graph graph = new Neo4jGraph("target/drwho");
        YedFileWriter yedWriter = new YedFileWriter(graph,"character");
        yedWriter.outputGraph("drwho.graphml");
        

    }
    
}
