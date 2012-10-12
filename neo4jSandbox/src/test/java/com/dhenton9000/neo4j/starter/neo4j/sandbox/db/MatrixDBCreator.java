/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox.db;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import static com.dhenton9000.neo4j.starter.neo4j.sandbox.GraphMLDemo.MAXTRIX_DB_LOCATION;
/**
 *
 * @author dhenton
 */
public class MatrixDBCreator extends AbstractEmbeddedDBCreator {


    public enum RelTypes implements RelationshipType {

        NEO_NODE,
        KNOWS,
        CODED_BY
    }

    @Override
    public void createNodespace() {
        Transaction tx = getGraphDb().beginTx();
        try {
            Node thomas = getGraphDb().createNode();
            thomas.setProperty("Text", "Thomas Anderson");
            thomas.setProperty("age", 29);

            // connect Neo/Thomas to the reference node
            Node referenceNode = getGraphDb().getReferenceNode();
            referenceNode.createRelationshipTo(thomas, RelTypes.NEO_NODE);

            Node trinity = getGraphDb().createNode();
            trinity.setProperty("Text", "Trinity");
            Relationship rel = thomas.createRelationshipTo(trinity,
                    RelTypes.KNOWS);
            rel.setProperty("age", "3 days");
            Node morpheus = getGraphDb().createNode();
            morpheus.setProperty("Text", "Morpheus");
            morpheus.setProperty("rank", "Captain");
            morpheus.setProperty("occupation", "Total badass");
            thomas.createRelationshipTo(morpheus, RelTypes.KNOWS);
            rel = morpheus.createRelationshipTo(trinity, RelTypes.KNOWS);
            rel.setProperty("age", "12 years");
            Node cypher = getGraphDb().createNode();
            cypher.setProperty("Text", "Cypher");
            cypher.setProperty("last Text", "Reagan");
            trinity.createRelationshipTo(cypher, RelTypes.KNOWS);
            rel = morpheus.createRelationshipTo(cypher, RelTypes.KNOWS);
            rel.setProperty("disclosure", "public");
            Node smith = getGraphDb().createNode();
            smith.setProperty("Text", "Agent Smith");
            smith.setProperty("version", "1.0b");
            smith.setProperty("language", "C++");
            rel = cypher.createRelationshipTo(smith, RelTypes.KNOWS);
            rel.setProperty("disclosure", "secret");
            rel.setProperty("age", "6 months");
            Node architect = getGraphDb().createNode();
            architect.setProperty("Text", "The Architect");
            smith.createRelationshipTo(architect, RelTypes.CODED_BY);

            tx.success();
        } finally {
            tx.finish();
        }
    }

    public static void main(String[] args) {
        MatrixDBCreator mc = new MatrixDBCreator();
        mc.setUp(MAXTRIX_DB_LOCATION);

    }
}
