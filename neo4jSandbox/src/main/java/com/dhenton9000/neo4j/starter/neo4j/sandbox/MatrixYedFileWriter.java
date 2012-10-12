/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;

/**
 *
 * @author dhenton
 */
public class MatrixYedFileWriter extends AbstractYedFileWriter {
   public static final String VERTEX_LABEL_PROPERTY = "Text";
    
    public MatrixYedFileWriter(Graph graph)
    {
        super(graph);
    }
    
    @Override
    public String computeVertexLabel(Vertex vertex)
            
    {
        return (String) vertex.getProperty(VERTEX_LABEL_PROPERTY);
    }

}
