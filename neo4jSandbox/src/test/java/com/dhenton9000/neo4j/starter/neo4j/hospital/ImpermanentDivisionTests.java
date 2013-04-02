/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.hospital;

import com.dhenton9000.neo4j.hospital.json.Division;
import com.dhenton9000.neo4j.hospital.json.HospitalNode;
import com.dhenton9000.neo4j.hospital.json.JSONHospitalServiceImpl;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 *
 * @author dhenton
 */
public class ImpermanentDivisionTests extends HospitalTestBase {

    private final Logger logger = LoggerFactory.getLogger(HospitalTests.class);
    private JSONHospitalServiceImpl jService = new JSONHospitalServiceImpl();

    @Before
    public void before() {
        this.prepareTestDatabase();
        jService.setNeo4jDb(this.getGraphDb());

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
    public void testCreateNeo4jGraphFromDivision()
    {
        String label = "Test";
        Division d = new Division();
        d.setLabel(label);
        d = jService.attachFullTree(d);
        assertEquals(label,d.getLabel());
        assertEquals(new Long(1L),new Long(d.getId()));
        assertEquals(0,d.getChildren().size());
    }
    
     @Test
    public void testCreateNeo4jGraphFromSample()
    {
        String label = "Alpha";
        Division d = getSampleRoot();
        d.setLabel(label);
        d = jService.attachFullTree(d);
        assertEquals(label,d.getLabel());
        assertEquals(new Long(1L),new Long(d.getId()));
        assertEquals(3,d.getChildren().size());
        
        HospitalNode moeDiv = d.getChildren().get(1);
        assertEquals(new Long(3L),new Long(moeDiv.getId()));
        assertEquals("Moe",moeDiv.getLabel());
        assertEquals(3,moeDiv.getChildren().size());
        HospitalNode hueyDiv = moeDiv.getChildren().get(0);
        assertEquals("Huey",hueyDiv.getLabel());
        assertEquals(new Long(4L),new Long(hueyDiv.getId()));
        
        
    }
    
        private Division getSampleRoot() {

        ArrayList<HospitalNode> children = new ArrayList<HospitalNode>();
        Division d = null;
        Division root = new Division();
        root.setLabel("Alpha");
         

        d = new Division();
        d.setLabel("Manny");
        
        children.add(d);

        d = new Division();
        d.setLabel("Moe");
        children.add(d);

        ArrayList<HospitalNode> d2 = new ArrayList<HospitalNode>();
        d.setChildren(d2);

        d = new Division();
        d.setLabel("Huey");
        d2.add(d);
        d = new Division();
        d.setLabel("Dewey");
        d2.add(d);
        d = new Division();
        d.setLabel("Louie");
        d2.add(d);

        d = new Division();
        d.setLabel("Jack");
        children.add(d);
        root.setChildren(children);
        return root;

    }
}
