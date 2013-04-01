/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dhenton9000.neo4j.starter.neo4j.sandbox.BaseNeo4jTest;
import static com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital.HospitalDbMaker.*;
import com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital.json.Division;
import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;

/**
 *
 * @author dhenton
 */
public class JsonHospitalTests extends BaseNeo4jTest {

    private final Logger logger = LoggerFactory.getLogger(HospitalTests.class);
    private static final int STATE_COUNT = 47;
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
        buildJSON(dItem, root);
        String temp = mapper.defaultPrettyPrintingWriter().writeValueAsString(root);
        logger.info("\n" + temp);


    }

    private void buildJSON(Node item, Division parent) {
        String nextItem = (String) item.getProperty(DIVISION_DISPLAY_PROPERTY);
        Iterable<Relationship> rels =
                item.getRelationships(Direction.OUTGOING,
                RelationshipTypes.IS_DIVIDED_INTO);

        
        parent.setLabel(nextItem);

        if (rels.iterator().hasNext()) {
            for (Relationship r : rels) {
                Division div = new Division();
                String lVar = (String) r.getEndNode().getProperty(DIVISION_DISPLAY_PROPERTY);
                div.setLabel(lVar);
                div.setId(r.getEndNode().getId());
                parent.getChildren().add(div);
                buildJSON(r.getEndNode(), div);
            }
            

        }
       



    }

    private Node getDivisionNode(String nodeName) {
        Index<Node> indexDivisionsDisplay =
                staticgraphDb.index().forNodes(DIVISION_DISPLAY_INDEX);
        Node dItem =
                indexDivisionsDisplay.get(DIVISION_DISPLAY_PROPERTY, nodeName).getSingle();
        return dItem;
    }
}
