/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.hospital;

import static com.dhenton9000.neo4j.hospital.json.JSONHospitalService.*;
import com.dhenton9000.neo4j.hospital.json.JSONHospitalService.RelationshipTypes;
import java.util.Iterator;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.index.Index;
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

public class HospitalTests extends HospitalTestBase {

    private final Logger logger = LoggerFactory.getLogger(HospitalTests.class);
    private static final int STATE_COUNT = 47;
    //  private static String tString = "";

    @BeforeClass
    public static void beforeClass() {
        prepareStaticHospitalTestDatabase();
        // tString = "";
    }

    @AfterClass
    public static void closeTheDatabase() {
        staticgraphDb.shutdown();
    }

//TODO use a cypher query with fixed depth where depth is at the state level
//    @Test
//    public void testStateCount() {
//        Index<Node> indexTypes = staticgraphDb.index().forNodes(TYPE_INDEX);
//        IndexHits<Node> states =
//                indexTypes.get(NODE_TYPE.TYPE.toString(),
//                NODE_TYPE.STATE_DIVISIONS.toString());
//        assertEquals(STATE_COUNT, states.size());
//
//    }

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
            //    logger.debug("type " + t.getProperty(NODE_TYPE.TYPE.toString(), "NULL"));
            // logger.debug("display " + t.getProperty(PROVIDER_DISPLAY_PROPERTY, "NULL"));


        }

    }
/*
    @Ignore
    public void testBuildTree() {
        String nodeName = "Midwest";
        // Node dItem = getDivisionNode(nodeName);
        Node dItem = staticgraphDb.getNodeById(1);

        assertNotNull(dItem);
        Iterable<Relationship> rels =
                dItem.getRelationships(Direction.OUTGOING,
                RelationshipTypes.IS_DIVIDED_INTO);
        ArrayList<String> items = new ArrayList<String>();
        buildTree(dItem, items, "");
        logger.debug("\n" + items.toString() + "\n");

    }

    private void buildTree(Node item, ArrayList<String> list, String marker) {
        String nextItem = (String) item.getProperty(DIVISION_DISPLAY_PROPERTY);
        // logger.debug("item in " + nextItem);

        Iterable<Relationship> rels =
                item.getRelationships(Direction.OUTGOING,
                RelationshipTypes.IS_DIVIDED_INTO);
        list.add(marker + nextItem);
        if (rels.iterator().hasNext()) {
            // logger.debug(tString);
            // logger.debug(" -> " + nextItem);
            for (Relationship r : rels) {
                buildTree(r.getEndNode(), list, marker + "-");
            }

        }

    }

    @Test
    public void testBuildJSON() {

        StringBuffer j = new StringBuffer();
        Node dItem = getDivisionNode(PROGRAM_NAME);
        buildJSON(dItem, j, 0);
        // assertEquals("fredted", j.toString());
        logger.debug("\n\n" + j.toString() + "\n");

    }

    private void buildJSON(Node item, StringBuffer jsonString, int level) {
        String nextItem = (String) item.getProperty(DIVISION_DISPLAY_PROPERTY);
        Iterable<Relationship> rels =
                item.getRelationships(Direction.OUTGOING,
                RelationshipTypes.IS_DIVIDED_INTO);

        //level++;
        jsonString.append("\n" + getIndent(level) + "{ label: '" + nextItem + "',");
        if (rels.iterator().hasNext()) {
            // logger.debug(tString);
            // logger.debug(" -> " + nextItem);
            level ++;
            jsonString.append( "\n"+getIndent(level)+"children: [");

            for (Relationship r : rels) {
                buildJSON(r.getEndNode(), jsonString,  level);
            }
            jsonString.append("\n" + getIndent(level) + "]\n");

        }
        jsonString.append(getIndent(level-1) +"},");



    }

    private String getIndent(int n) {
        return new String(new char[n]).replace("\0", "   ");
    }

    @Test
    public void testDisplayTraverse() {
        String nodeName = "West";
        Node dItem = getDivisionNode(nodeName);

        TraversalDescription td = Traversal.description()
                .breadthFirst()
                .relationships(RelationshipTypes.IS_DIVIDED_INTO, Direction.OUTGOING)
                .evaluator(Evaluators.excludeStartPosition());

        Traverser t = td.traverse(dItem);

        for (Path p : t) {
            //  logger.debug("p "+p.endNode().getProperty(DIVISION_DISPLAY_PROPERTY));
        }

    }
*/
    /*
     * treewalking: http://stackoverflow.com/questions/9080929/modeling-an-ordered-tree-with-neo4j
     * 
     
     /reusable traversal description
     final private TraversalDescription AST_TRAVERSAL = Traversal.description()
     .depthFirst()
     .expand(new OrderedByTypeExpander()
     .add(RelType.FIRST_CHILD, Direction.OUTGOING)
     .add(RelType.NEXT_SIBLING, Direction.OUTGOING));
     and then when I actually needed to walk the tree I could just do

     for(Path path : AST_TRAVERSAL.traverse(astRoot)){
     //do stuff here
     }
      
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
