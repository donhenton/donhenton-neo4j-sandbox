/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4jsandbox;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dhenton
 */
public class SimpleTest extends BaseNeo4jTest {
    
    @Before
    public void before()
    {
        this.prepareTestDatabase();
    }
    
    @After
    public void after()
    {
        this.destroyTestDatabase();
    }
    
    
    @Test
    public void testDbCreate()
    {
        assertNotNull(this.getGraphDb());
    }
    
    
}
