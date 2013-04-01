/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.hospital.json;

import java.io.IOException;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

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
    public static final String DIVISION_DISPLAY_PROPERTY = "division_display_property";
    public static final String PROVIDER_DISPLAY_PROPERTY = "provider_display_property";
    public static final String DIVISION_DISPLAY_INDEX = "division_display_index";
    public static final String PROVIDER_DISPLAY_INDEX = "provider_display_index";
    public static final String PROVIDER_DB_KEY = "provider_db_key";
    public static final String TYPE_INDEX = "type_index";
    
     public enum RelationshipTypes implements RelationshipType {

        IS_DIVIDED_INTO,
        DERIVES_SERVICE_FROM
    }

    public enum NODE_TYPE {

        TYPE, DIVISIONS, PROVIDERS,  DISTRICTS
    }
    
    
    Division buildDivison(String startDivisionLabel);

    String getDisplayMessage(Node currentNode);

    Node getDivisionNode(String nodeName);

    Division stringToStructure(String jsonString) throws IOException;

    String structureToString(Division root) throws IOException;
    
    Node createAndAttachDivisionNode(Node parent, String nodeLabel);
    Node createAndAttachProviderNode(Node parent, String nodeLabel);
    
}
