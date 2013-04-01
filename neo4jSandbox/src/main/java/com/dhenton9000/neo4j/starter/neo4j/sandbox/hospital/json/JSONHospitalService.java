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

    void buildJSON(Node item, Division parent);

    String getDisplayMessage(Node currentNode);

    Node getDivisionNode(String nodeName);

    Division stringToStructure(String jsonString) throws IOException;

    String structureToString(Division root) throws IOException;
    
}
