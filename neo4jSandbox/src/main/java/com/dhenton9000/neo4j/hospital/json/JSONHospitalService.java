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
    
    /**
     * Build a division object from a point in the tree using the start
     * points division label. For example, build the entire tree starting
     * from the node 'Midwest'. It will include providers
     * 
     * @param startDivisionLabel
     * @return an assembled division object
     */
    Division buildDivison(String startDivisionLabel);
    
    
    /**
     * For a given live node compute its label. This takes into account that 
     * labels for Divisions are stored in one place, for providers in another
     * @param currentNode
     * @return 
     */
    String getDisplayMessage(Node currentNode);

    /**
     * Lookup a division node by its label. 
     * @param nodeName
     * @return null if nothing found
     */
    Node getDivisionNode(String nodeName);

    /**
     * given a proper JSON string, return the division object
     * @param jsonString
     * @return
     * @throws IOException 
     */
    Division stringToStructure(String jsonString) throws IOException;
    /**
     * given a proper division object get the JSON string
     * @param root
     * @return
     * @throws IOException 
     */
    String structureToString(Division root) throws IOException;
    
    Node createAndAttachDivisionNode(Node parent, String nodeLabel);
    Node createAndAttachProviderNode(Node parent, String nodeLabel);
    /**
     * Given a division attach this whole tree to the root node of the
     * neo4j graph. The id property can be empty
     * @param d the graph as as division, this is only a sample, ids will
     * be filled in when attached
     * @return the root Division with the id now filled in
     */
    public Division attachFullTree(Division d);
    
}
