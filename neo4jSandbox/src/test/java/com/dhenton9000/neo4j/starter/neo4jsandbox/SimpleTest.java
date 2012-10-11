/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4jsandbox;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.neo4j.graphdb.*;

/**
 *
 * @author dhenton
 */
public class SimpleTest extends BaseNeo4jTest {

    private static final String INODE = "inode";
    private static final String NAME = "name";
    private static final long SYMLINK_INODE_VALUE = 111111111;

    public enum FileTreeTypes implements RelationshipType {

        CHILD,
        SYMLINK
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

    @Test
    public void testDbCreate() {
        assertNotNull(this.getGraphDb());
    }
    
    @Test
    public void testFileStructure()
    {
            
            Node rootNode = getGraphDb().getReferenceNode();
            String  path = "dir1/dir12/shortcut";
            Node target = getNodeFromPath( rootNode, path, true );
            assertNotNull(target);
            assertEquals("symlink inode value",target.getProperty( INODE ),SYMLINK_INODE_VALUE  );

    }
    
    

    private Node createSubdir(final Node parent, final String name,
            final long inode) {
        Node dirNode = getGraphDb().createNode();
        dirNode.setProperty(INODE, inode);
        Relationship rel = parent.createRelationshipTo(dirNode,
                TreeTraverse.FileTreeTypes.CHILD);
        rel.setProperty(NAME, name);
        return dirNode;
    }

    /**
     * Create a symlink.
     *
     * @param parent parent directory
     * @param target directory the symlink points to
     * @param name name of the symlink
     * @return the symlink itself
     */
    private Relationship createSymlink(final Node parent,
            final Node target, final String name) {
        Relationship rel = parent.createRelationshipTo(target,
                TreeTraverse.FileTreeTypes.SYMLINK);
        rel.setProperty(NAME, name);
        return rel;
    }

    private void setUpGraph() {
        Transaction tx = getGraphDb().beginTx();
        try {
            Node rootNode = getGraphDb().getReferenceNode();

            // create a path like this:
            // /dir1/dir12/dir123/dir1231

            Node dir1 = createSubdir(rootNode, "dir1", 100001);
            Node dir12 = createSubdir(dir1, "dir12", 100002);
            Node dir123 = createSubdir(dir12, "dir123", 100003);
            Node dir1231 = createSubdir(dir123, "dir1231", SYMLINK_INODE_VALUE);

            // create a symlink like this:
            // /dir1/dir12/shortcut
            // which points to dir1231
            createSymlink(dir12, dir1231, "shortcut");

            tx.success();
        } finally {
            tx.finish();
        }
    }
    
       /**
     * Follow CHILD relationships along the path and return the target node.
     * 
     * @param startNode the root node to start from
     * @param path the pat to the target node
     * @return the target node
     */
    private static Node getNodeFromPath( final Node startNode, final String path )
    {
        Node currentNode = startNode;
        for ( String name : path.split( "/" ) )
        {
            boolean foundName = false;
            for ( Relationship rel : currentNode.getRelationships(
                    TreeTraverse.FileTreeTypes.CHILD, Direction.OUTGOING ) )
            {
                if ( name.equals( rel.getProperty( NAME, null ) ) )
                {
                    currentNode = rel.getEndNode();
                    foundName = true;
                    break;
                }
            }
            if ( !foundName )
            {
                throw new IllegalArgumentException( "No such path" );
            }
        }
        return currentNode;
    }

    /**
     * Follow CHILD relationships along the path and return the target node.
     * 
     * @param startNode the root node to start from
     * @param path the pat to the target node
     * @param includeSymlinks follows symlinks when true
     * @return the target node
     */
    private static Node getNodeFromPath( final Node startNode,
            final String path, final boolean includeSymlinks )
    {
        Node currentNode = startNode;
        for ( String name : path.split( "/" ) )
        {
            boolean foundName = false;
            Iterable<Relationship> rels;
            if ( includeSymlinks )
            {
                // simply follow any outgoing relationships
                rels = currentNode.getRelationships( Direction.OUTGOING );
            }
            else
            {
                // only follow outgoing CHILD relationships
                rels = currentNode.getRelationships( TreeTraverse.FileTreeTypes.CHILD,
                        Direction.OUTGOING );
            }
            for ( Relationship rel : rels )
            {
                if ( name.equals( rel.getProperty( NAME, null ) ) )
                {
                    currentNode = rel.getEndNode();
                    foundName = true;
                    break;
                }
            }
            if ( !foundName )
            {
                throw new IllegalArgumentException( "No such path" );
            }
        }
        return currentNode;
    }
    
    
}
