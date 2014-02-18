/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class StarterTest {

    private static GraphDatabaseService graphDb = null;
    private static final String ROOTNODE_USERNAME = "<init>";

    //  private Index<Node> nodeIndex;
    private static final String USERNAME_KEY = "username";
    private static final Logger logger = LoggerFactory.getLogger(IndexTest.class);
    private static Node usersReferenceNode = null;

    /**
     * @return the graphDb
     */
    public static GraphDatabaseService getGraphDb() {
        return graphDb;
    }

    private static enum RelTypes implements RelationshipType {

        USERS_REFERENCE,
        USER
    }

    @BeforeClass
    public static void beforeClass() {
        
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        IndexDefinition indexDefinition = null;
        try (Transaction tx = getGraphDb().beginTx()) {
            Schema schema = getGraphDb().schema();
            indexDefinition = schema.indexFor(DynamicLabel.label(USERNAME_KEY))
                    .on(USERNAME_KEY)
                    .create();
            tx.success();

        } catch (Exception err) {
            logger.error("error " + err.getClass().getName() + " " + err.getMessage());

            // destroyTestDatabase();
            // cleanUpDone = true;
        }
        try (Transaction tx = getGraphDb().beginTx()) {
            // nodeIndex = getGraphDb().index().forNodes("nodes");
            // Create users sub reference node
            usersReferenceNode = getGraphDb().createNode();
            usersReferenceNode.setProperty(USERNAME_KEY, ROOTNODE_USERNAME);
            getGraphDb().getNodeById(0).createRelationshipTo(
                    usersReferenceNode, RelTypes.USERS_REFERENCE);
            // Create some users and index their names with the IndexService
            for (int id = 0; id < 100; id++) {
                Node userNode   = getGraphDb().createNode();
                userNode.setProperty(USERNAME_KEY, idToUserName(id));
                usersReferenceNode.createRelationshipTo(userNode,
                        RelTypes.USER);
            }
            // END SNIPPET: addUsers
            logger.debug("users created");

            tx.success();
        } catch (Exception err) {
            logger.error("error " + err.getClass().getName() + " " + err.getMessage());

        }
    }
  

    @AfterClass
    public static void afterClass() {
        getGraphDb().shutdown();
    }

    @Test
    public void findUser() {

        int idToFind = 45;
        Node foundUser = findAUser(idToFind);

        logger.debug("The username of user " + idToFind + " is "
                + foundUser.getProperty(USERNAME_KEY));
        assertNotNull(foundUser);
        assertEquals(idToUserName(idToFind), foundUser.getProperty(USERNAME_KEY));

    }

    @Test
    public void testRelationShips() {
        int idToFind = 45;
        Node foundUser = findAUser(idToFind);
        Iterable<Relationship> relations = foundUser.getRelationships();
        int cc = 0;
        for (Relationship r : relations) {
            //logger.debug(r + " type: " + r.getType() + " start node "
            //        + r.getStartNode().getProperty(USERNAME_KEY, null) + " end " + r.getEndNode().getProperty(USERNAME_KEY, null));
            cc++;
        }
        relations = foundUser.getRelationships();
        Relationship r = relations.iterator().next();
        assertEquals(r.getStartNode().getProperty(USERNAME_KEY, null), ROOTNODE_USERNAME);
        assertEquals(usersReferenceNode, r.getStartNode());
        assertEquals(1, cc);
        assertEquals(foundUser, r.getEndNode());

    }

    
    
    
    
    @Test
    public void getAJob() {
        assertTrue(true);
    }

    private Node createAndIndexUser(final String username) {
        Node node = getGraphDb().createNode();
        node.setProperty(USERNAME_KEY, username);
        //nodeIndex.add(node, USERNAME_KEY, username);
        return node;
    }

    private static String idToUserName(final int id) {
        return "user" + id + "@neo4j.org";
    }

    private Node findAUser(int idToFind) {

        Node foundUser = null;
        try (Transaction tx = getGraphDb().beginTx();
                ResourceIterator<Node> users = getGraphDb()
                .findNodesByLabelAndProperty(null, "username", idToUserName(idToFind))
                .iterator()) {

            if (users.hasNext()) {
                foundUser = users.next();
            }
            users.close();
        }
        return foundUser;
    }
}
