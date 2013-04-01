/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital.json;

import com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital.HospitalDbMaker;
import static com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital.HospitalDbMaker.*;
import com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital.HospitalDbMaker.NODE_TYPE;
import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;

/**
 * Service class for processing JSON for the Provider structures
 *
 * @author dhenton
 */
public class JSONHospitalServiceImpl implements JSONHospitalService {

    private GraphDatabaseService neo4jDb;
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public String structureToString(Division root) throws IOException {
        String temp = mapper.defaultPrettyPrintingWriter().writeValueAsString(root);
        return temp;
    }

    @Override
    public Division stringToStructure(String jsonString) throws IOException {
        return mapper.readValue(jsonString, Division.class);
    }

    @Override
    public void buildJSON(Node item, Division parent) {
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

    @Override
    public String getDisplayMessage(Node currentNode) {
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

    private HospitalDbMaker.NODE_TYPE getNodeType(Node node) {
        String t = (String) node.getProperty(HospitalDbMaker.NODE_TYPE.TYPE.toString());
        HospitalDbMaker.NODE_TYPE res = HospitalDbMaker.NODE_TYPE.valueOf(t);
        if (res == null) {
            throw new RuntimeException("got node type error " + t);
        }
        return res;
    }

    @Override
    public Node getDivisionNode(String nodeName) {
        Index<Node> indexDivisionsDisplay =
                getNeo4jDb().index().forNodes(DIVISION_DISPLAY_INDEX);
        Node dItem =
                indexDivisionsDisplay.get(DIVISION_DISPLAY_PROPERTY, nodeName).getSingle();
        return dItem;
    }

    /**
     * @return the neo4jDb
     */
    public GraphDatabaseService getNeo4jDb() {
        return neo4jDb;
    }

    /**
     * @param neo4jDb the neo4jDb to set
     */
    public void setNeo4jDb(GraphDatabaseService neo4jDb) {
        this.neo4jDb = neo4jDb;
    }
}
