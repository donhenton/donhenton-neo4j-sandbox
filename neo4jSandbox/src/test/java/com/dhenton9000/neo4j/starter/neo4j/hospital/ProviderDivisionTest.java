/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.hospital;

import com.dhenton9000.neo4j.hospital.json.Division;
import com.dhenton9000.neo4j.hospital.json.HospitalNode;
import com.dhenton9000.neo4j.hospital.json.JSONHospitalService;
import com.dhenton9000.neo4j.hospital.json.JSONHospitalServiceImpl;
import com.dhenton9000.neo4j.hospital.json.Provider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.impl.util.StringLogger;

/**
 * uses simple tree to test writing an entire tree to an impermanent neo4j db
 *
 * @author dhenton
 */
public class ProviderDivisionTest extends HospitalTestBase {
    public static final String PROVIDER_LABEL = "Provider1";

    private final Logger logger = LoggerFactory.getLogger(ProviderDivisionTest.class);
    private JSONHospitalServiceImpl jService = new JSONHospitalServiceImpl();
    private ObjectMapper mapper = new ObjectMapper();

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
    public void testJSONForSample() throws IOException
    {
        String t = jService.structureToString(getSampleRoot());
        logger.debug("t\n"+t);
        HospitalNode root = jService.stringToStructure(t);
        HospitalNode h = root.getChildren().get(0);
        assertEquals(PROVIDER_LABEL,h.getLabel());
        Provider p = (Provider) h;
        assertNotNull(p);
        assertEquals(PROVIDER_LABEL,p.getLabel());
    }
    
   
    private Division getSampleRoot() {

        ArrayList<HospitalNode> children = new ArrayList<HospitalNode>();
        Division d = null;
        Division root = new Division();
        root.setLabel("Alpha");
        root.setChildren(children);
        d = new Division();
        d.setLabel("Manny");
        HospitalNode h = new Provider();
        h.setLabel(PROVIDER_LABEL);
        children.add(h);
        
        
        return root;

    }

    
}
