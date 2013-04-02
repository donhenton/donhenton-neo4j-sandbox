/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.hospital.json;

import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
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
    public Division attachFullTree(Division d) {
        Transaction tx = getNeo4jDb().beginTx();
        try {
            Node graphRefNode = getNeo4jDb().getReferenceNode();
            String nodeLabel = d.getLabel();
            Node rootNode = createAndAttachDivisionNode(graphRefNode, nodeLabel);
            d.setId(rootNode.getId());
            for (HospitalNode dChild : d.getChildren()) {
                attachSubTree(dChild, rootNode);
            }

            tx.success();
        } finally {
            tx.finish();
        }
        return d;
    }

    private void attachSubTree(HospitalNode subD, Node parent) {
        String nodeLabel = subD.getLabel();
        Node rootNode = createAndAttachDivisionNode(parent, nodeLabel);
        subD.setId(rootNode.getId());
        for (HospitalNode dChild : subD.getChildren()) {
            attachSubTree(dChild, rootNode);
        }
    }

    @Override
    public Node createAndAttachDivisionNode(Node parent, String nodeLabel) {
        Node currentNode = getNeo4jDb().createNode();
        parent.createRelationshipTo(currentNode, RelationshipTypes.IS_DIVIDED_INTO);
        getTypeIndex().add(currentNode, NODE_TYPE.TYPE.toString(), NODE_TYPE.DIVISIONS.toString());
        currentNode.setProperty(NODE_TYPE.TYPE.toString(), NODE_TYPE.DIVISIONS.toString());
        getDivisionIndex().add(currentNode, DIVISION_DISPLAY_PROPERTY, nodeLabel);
        currentNode.setProperty(DIVISION_DISPLAY_PROPERTY, nodeLabel);
        return currentNode;
    }

    @Override
    public Node createAndAttachProviderNode(Node parent, String nodeLabel) {
        Node currentNode = getNeo4jDb().createNode();
        parent.createRelationshipTo(currentNode, RelationshipTypes.DERIVES_SERVICE_FROM);
        getTypeIndex().add(currentNode, NODE_TYPE.TYPE.toString(), NODE_TYPE.PROVIDERS.toString());
        currentNode.setProperty(NODE_TYPE.TYPE.toString(), NODE_TYPE.PROVIDERS.toString());
        getDivisionIndex().add(currentNode, PROVIDER_DISPLAY_PROPERTY, nodeLabel);
        currentNode.setProperty(PROVIDER_DISPLAY_PROPERTY, nodeLabel);
        return currentNode;
    }

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
    public Division buildDivison(String startDivisionLabel) {
        Division root = new Division();
        Node dItem = getDivisionNode(startDivisionLabel);
        String nextItem = (String) dItem.getProperty(DIVISION_DISPLAY_PROPERTY);
        root.setLabel(nextItem);
        root.setId(dItem.getId());
        buildDivisionElement(dItem, root);
        return root;
    }

    private void buildDivisionElement(Node item, Division parent) {
        String nextItem = getDisplayMessage(item);
        Iterable<Relationship> rels =
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
                buildDivisionElement(currentNode, div);
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

    private NODE_TYPE getNodeType(Node node) {
        String t = (String) node.getProperty(NODE_TYPE.TYPE.toString());
        NODE_TYPE res = NODE_TYPE.valueOf(t);
        if (res == null) {
            throw new RuntimeException("got node type error " + t);
        }
        return res;
    }

    @Override
    public Node getDivisionNode(String nodeName) {

        Node dItem =
                getDivisionIndex().get(DIVISION_DISPLAY_PROPERTY, nodeName).getSingle();
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

    private Index<Node> getDivisionIndex() {
        return getIndex(DIVISION_DISPLAY_INDEX);
    }

    private Index<Node> getProviderIndex() {
        return getIndex(PROVIDER_DISPLAY_INDEX);
    }

    private Index<Node> getTypeIndex() {
        return getIndex(TYPE_INDEX);
    }

    private Index<Node> getIndex(String indexNodeLabel) {
        return getNeo4jDb().index().forNodes(indexNodeLabel);
    }
}
