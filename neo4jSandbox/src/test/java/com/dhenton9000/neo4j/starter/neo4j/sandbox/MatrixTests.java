/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox;

import com.dhenton9000.neo4j.starter.neo4j.sandbox.db.MatrixDBCreator.RelTypes;
import static com.dhenton9000.neo4j.starter.neo4j.sandbox.GraphMLDemo.MAXTRIX_DB_LOCATION;
 
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class MatrixTests extends BaseNeo4jTest {
   
    private final Logger logger = LoggerFactory.getLogger(MatrixTests.class);
    @BeforeClass
    public static void beforeClass()
    {
        prepareEmbeddedDatabase(MAXTRIX_DB_LOCATION);
    }
    
    
     @Test
    public void testGraphMLOutput() throws IOException
   {
        // this code must NOT have the database running
    
        
       //GraphMLWriter.outputGraph(graph, new FileOutputStream("mygraph.xml"));
        
       // GraphMLWriter writer = new GraphMLWriter(staticgraphDb.get);
//writer.setNormalize(true);
//writer.outputGraph(out);
    }
    
    
    @Test
    public void testNeoNode()
    {
       Node t = getNeoNode();
       assertNotNull(t);
       assertNotNull(t.getProperty("Text",null));
       assertEquals(t.getProperty("Text",null),"Thomas Anderson");
    }
    
    
    private Node getNeoNode() {
        return staticgraphDb.getReferenceNode().getSingleRelationship(RelTypes.NEO_NODE, Direction.OUTGOING).getEndNode();
    }

    public String printNeoFriends() {
        Node neoNode = getNeoNode();
        // START SNIPPET: friends-usage
        int numberOfFriends = 0;
        String output = neoNode.getProperty("name") + "'s friends:\n";
        Traverser friendsTraverser = getFriends(neoNode);
        for (Path friendPath : friendsTraverser) {
            output += "At depth " + friendPath.length() + " => "
                    + friendPath.endNode().getProperty("name") + "\n";
            numberOfFriends++;
        }
        output += "Number of friends found: " + numberOfFriends + "\n";
        // END SNIPPET: friends-usage
        return output;
    }

    // START SNIPPET: get-friends
    private static Traverser getFriends(
            final Node person) {
        TraversalDescription td = Traversal.description().breadthFirst().relationships(RelTypes.KNOWS, Direction.OUTGOING).evaluator(Evaluators.excludeStartPosition());
        return td.traverse(person);
    }
    // END SNIPPET: get-friends

    public String printMatrixHackers() {
        // START SNIPPET: find--hackers-usage
        String output = "Hackers:\n";
        int numberOfHackers = 0;
        Traverser traverser = findHackers(getNeoNode());
        for (Path hackerPath : traverser) {
            output += "At depth " + hackerPath.length() + " => "
                    + hackerPath.endNode().getProperty("name") + "\n";
            numberOfHackers++;
        }
        output += "Number of hackers found: " + numberOfHackers + "\n";
        // END SNIPPET: find--hackers-usage
        return output;
    }

    // START SNIPPET: find-hackers
    private static Traverser findHackers(final Node startNode) {
        TraversalDescription td = Traversal.description().breadthFirst().relationships(RelTypes.CODED_BY, Direction.OUTGOING).relationships(RelTypes.KNOWS, Direction.OUTGOING).evaluator(
                Evaluators.includeWhereLastRelationshipTypeIs(RelTypes.CODED_BY));
        return td.traverse(startNode);
    }
    // END SNIPPET: find-hackers
}
