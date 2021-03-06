/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;

/**
 *
 * the graph structure is :
 *
 * Root | usersReferenceNode | | | | | | user1 user2 user4 user5 user6 .....
 *
 * @author dhenton
 */
public class IndexTest extends BaseNeo4jTest {

    private static final String ROOTNODE_USERNAME = "<init>";

    //  private Index<Node> nodeIndex;
    private static final String USERNAME_KEY = "username";
    private final Logger logger = LoggerFactory.getLogger(IndexTest.class);
    private Node usersReferenceNode;

    private static enum RelTypes implements RelationshipType {

        USERS_REFERENCE,
        USER
    }

    @Before
    public void before() {
        this.prepareTestDatabase();
        setUpGraph();

    }

    @After
    public void after() {
        this.destroyTestDatabase();
    }

    private void setUpGraph() {


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
                Node userNode = createAndIndexUser(idToUserName(id));
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

    @Ignore
    public void findUser() {

        int idToFind = 45;
        Node foundUser = findAUser(idToFind);

        logger.debug("The username of user " + idToFind + " is "
                + foundUser.getProperty(USERNAME_KEY));
        assertNotNull(foundUser);
        assertEquals(idToUserName(idToFind), foundUser.getProperty(USERNAME_KEY));

    }

    @Ignore
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

    private String idToUserName(final int id) {
        return "user" + id + "@neo4j.org";
    }

    /**
     * add to the nodeIndex store. the index store will index the item node in
     * the index USERNAME_KEY, with a value of username the nodeIndex is a
     * collecton of indices for key/value pairs each key is a <b>separate
     * index</b> the nodeIndex is a collection of indices
     *
     * @param username
     * @return
     */
    private Node createAndIndexUser(final String username) {
        Node node = getGraphDb().createNode();
        node.setProperty(USERNAME_KEY, username);
        //nodeIndex.add(node, USERNAME_KEY, username);
        return node;
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
