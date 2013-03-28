/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox.inventory;

import com.dhenton9000.neo4j.starter.neo4j.sandbox.BaseNeo4jTest;
import static com.dhenton9000.neo4j.starter.neo4j.sandbox.inventory.HospitalDbMaker.*;
import java.util.Iterator;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class HospitalTests extends BaseNeo4jTest {

    private final Logger logger = LoggerFactory.getLogger(HospitalTests.class);
    private static final int STATE_COUNT = 47;

    @BeforeClass
    public static void beforeClass() {
        prepareEmbeddedDatabase(DB_LOCATION);
    }

    @AfterClass
    public static void closeTheDatabase() {
        staticgraphDb.shutdown();
    }

    @Test
    public void testStateCount() {
        Index<Node> indexTypes = staticgraphDb.index().forNodes(TYPE_INDEX);
        IndexHits<Node> states =
                indexTypes.get(NODE_TYPE.TYPE.toString(),
                NODE_TYPE.STATE_DIVISIONS.toString());
        assertEquals(STATE_COUNT, states.size());

    }

    @Test
    public void testGetDivision() {
        String nodeName = "D012";
        Node dItem = getDivisionNode(nodeName);
        assertNotNull(dItem);
        assertEquals(nodeName, dItem.getProperty(DIVISION_DISPLAY_PROPERTY));
    }

    @Test
    public void testGetNewYorkProviders() {
        String nodeName = "New York";
        Node dItem = getDivisionNode(nodeName);
        assertNotNull(dItem);
        assertEquals(nodeName, dItem.getProperty(DIVISION_DISPLAY_PROPERTY));
        logger.debug("" + dItem.getProperty(DIVISION_DISPLAY_PROPERTY));
        TraversalDescription providers =
                Traversal.description()
                .relationships(RelationshipTypes.IS_DIVIDED_INTO, Direction.OUTGOING)           
                .depthFirst()
                .evaluator(new Evaluator() {
            @Override
            public Evaluation evaluate(Path path) {
                 
                return Evaluation.INCLUDE_AND_CONTINUE;


            }
        });


        Iterable<Node> nodeList = providers.traverse(dItem).nodes();

        Iterator<Node> iter = nodeList.iterator();
        while (iter.hasNext()) {
            Node t = iter.next();
            logger.debug("type " + t.getProperty(NODE_TYPE.TYPE.toString(), "NULL"));
           // logger.debug("display " + t.getProperty(PROVIDER_DISPLAY_PROPERTY, "NULL"));


        }

    }

    
    /*
     start n=node:division_display_index(division_display_property="New York") match n-[?]->a-[?]->b where b.TYPE = "PROVIDERS" return b;
     start n=node:division_display_index(division_display_property= 'New York') return n;
start n=node:division_display_index(division_display_property= 'Northeast') match n -[:IS_DIVIDED_INTO*]-> o return o;
    
    ClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);

	WebResource resource = jerseyClient.resource(employeeListServiceInternalEndpoint);
		ClientResponse response = resource.path(companyId.toString()).path(employeeLastName).accept(MediaType.APPLICATION_XML_TYPE).get(ClientResponse.class);
		if (response.getClientResponseStatus().getStatusCode() == Status.OK.getStatusCode()) {
			MyJaxbClassList entity = response.getEntity(MyJaxbClass.class);
			List<MyJaxbClass> results = new ArrayList<MyJaxbClass>(entity.getThings());
			return results;
		} else {
			return Collections.EMPTY_LIST;
		}
    
    
    */
    
    
    
    private Node getDivisionNode(String nodeName) {
        Index<Node> indexDivisionsDisplay =
                staticgraphDb.index().forNodes(DIVISION_DISPLAY_INDEX);
        Node dItem =
                indexDivisionsDisplay.get(DIVISION_DISPLAY_PROPERTY, nodeName).getSingle();
        return dItem;
    }
}
