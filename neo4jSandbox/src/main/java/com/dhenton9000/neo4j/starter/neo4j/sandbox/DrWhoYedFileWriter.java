/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.apache.commons.lang3.StringEscapeUtils.ESCAPE_XML;


//import   org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author dhenton
 */
public class DrWhoYedFileWriter extends AbstractYedFileWriter {
   public static final String VERTEX_LABEL_PROPERTY = "Text";
   private final Logger logger = LoggerFactory.getLogger(DrWhoYedFileWriter.class);
    
    
    public DrWhoYedFileWriter(Graph graph)
    {
        super(graph);
    }
    
    @Override
    public String computeVertexLabel(Vertex vertex)
            
    {
        String info = "\nid: "+ vertex.getId()+"\n";
        Set<String> keylist = vertex.getPropertyKeys();
        String label = "null";
        if (keylist.size() > 0)
        {
          label =   keylist.iterator().next().toString();
        }
        
         
        
//        for(String t:keylist)
//        {
//            info   += "\tkey --> "+t+"\n";
//        }
        
     //   logger.debug(info);
        
        return ESCAPE_XML.translate((String) vertex.getProperty(label));
    }

}
