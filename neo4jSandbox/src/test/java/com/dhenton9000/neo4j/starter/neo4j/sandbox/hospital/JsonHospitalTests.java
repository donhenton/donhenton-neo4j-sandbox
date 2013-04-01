/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital;

import com.dhenton9000.neo4j.starter.neo4j.sandbox.BaseNeo4jTest;
import static com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital.HospitalDbMaker.*;
import com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital.HospitalDbMaker.NODE_TYPE;
import com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital.json.Division;
import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class JsonHospitalTests extends BaseNeo4jTest {

    private final Logger logger = LoggerFactory.getLogger(HospitalTests.class);
    private ObjectMapper mapper = new ObjectMapper();
    //  private static String tString = "";

    @BeforeClass
    public static void beforeClass() {
        prepareEmbeddedDatabase(DB_LOCATION);
        // tString = "";
    }

    @AfterClass
    public static void closeTheDatabase() {
        staticgraphDb.shutdown();
    }

    @Test
    public void testBuildJSON() throws IOException {

        Division root = new Division();
        Node dItem = getDivisionNode(PROGRAM_NAME);
        String nextItem = (String) dItem.getProperty(DIVISION_DISPLAY_PROPERTY);
        root.setLabel(nextItem);
        root.setId(dItem.getId());
        buildJSON(dItem, root);
        String temp = mapper.defaultPrettyPrintingWriter().writeValueAsString(root);
       // logger.info("\n" + temp);


    }
    
    
    @Test
    public void JSONToObject() throws Exception
    {
        Division root = new Division();
        Node dItem = getDivisionNode(PROGRAM_NAME);
        String nextItem = (String) dItem.getProperty(DIVISION_DISPLAY_PROPERTY);
        root.setLabel(nextItem);
        root.setId(dItem.getId());
        buildJSON(dItem, root);
        String temp = mapper.defaultPrettyPrintingWriter().writeValueAsString(root);
        
        Division d2 = mapper.readValue(temp, Division.class);
        
        assertEquals(d2.getChildren().get(2).getLabel(),root.getChildren().get(2).getLabel());
        
        
        
        
    }

    private void buildJSON(Node item, Division parent) {
        String nextItem = getDisplayMessage(item);
        Iterable<Relationship> rels =
                //  item.getRelationships(Direction.OUTGOING,
                //  RelationshipTypes.IS_DIVIDED_INTO);

                item.getRelationships(Direction.OUTGOING);
        parent.setLabel(nextItem);

        if (rels.iterator().hasNext()) {
            for (Relationship r : rels) {
                Division div = new Division();
                Node currentNode = r.getEndNode();
                String lVar = getDisplayMessage(currentNode);
                div.setLabel(lVar);
                div.setId(currentNode.getId());
                parent.getChildren().add(div);
                buildJSON(currentNode, div);
            }


        }




    }

    private String getDisplayMessage(Node currentNode) {
        NODE_TYPE type = getNodeType(currentNode);
        String lVar = "";
        switch (type) {
            case DIVISIONS:

                lVar = (String) currentNode.getProperty(DIVISION_DISPLAY_PROPERTY);
                break;

            case PROVIDERS:
                lVar = (String) currentNode.getProperty(PROVIDER_DISPLAY_PROPERTY);
                break;

            default:
                throw new RuntimeException(" got fault " + currentNode.toString());
        }
        return lVar;
    }

    private NODE_TYPE getNodeType(Node node) {
        String t = (String) node.getProperty(NODE_TYPE.TYPE.toString());
        NODE_TYPE res = NODE_TYPE.valueOf(t);
        if (res == null) {
            throw new RuntimeException("got node type error " + t);
        }
        return res;
    }

    private Node getDivisionNode(String nodeName) {
        Index<Node> indexDivisionsDisplay =
                staticgraphDb.index().forNodes(DIVISION_DISPLAY_INDEX);
        Node dItem =
                indexDivisionsDisplay.get(DIVISION_DISPLAY_PROPERTY, nodeName).getSingle();
        return dItem;
    }
}
