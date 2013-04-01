/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital.json;

import java.io.IOException;
import org.neo4j.graphdb.Node;

/**
 *
 * @author dhenton
 */
public interface JSONHospitalService {

    /**
     * build the Division representation in the passed in division object
     * from the given Node. The node should be initialized
     * @param item
     * @param parent 
     */
    
    
    Division buildDivison(String startDivisionLabel);

    String getDisplayMessage(Node currentNode);

    Node getDivisionNode(String nodeName);

    Division stringToStructure(String jsonString) throws IOException;

    String structureToString(Division root) throws IOException;
    
}
