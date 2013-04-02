/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.hospital;

import static com.dhenton9000.neo4j.hospital.HospitalDbMaker.*;
import com.dhenton9000.neo4j.hospital.json.Division;
import com.dhenton9000.neo4j.hospital.json.JSONHospitalServiceImpl;
import com.dhenton9000.neo4j.starter.neo4j.sandbox.BaseNeo4jTest;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */

public class JsonHospitalTests extends HospitalTestBase {

    private final Logger logger = LoggerFactory.getLogger(HospitalTests.class);
    private static JSONHospitalServiceImpl jsonService = new JSONHospitalServiceImpl();
    //  private static String tString = "";

    @BeforeClass
    public static void beforeClass() {
        prepareStaticHospitalTestDatabase();
        jsonService.setNeo4jDb(staticgraphDb);

    }

    @AfterClass
    public static void closeTheDatabase() {
        staticgraphDb.shutdown();
    }

    @Test
    public void JSONToObject() throws Exception {
        Division root =
                jsonService.buildDivison(PROGRAM_NAME);
        String temp = jsonService.structureToString(root);
        Division d2 = jsonService.stringToStructure(temp);
        assertEquals(d2.getChildren().get(2).getLabel(), root.getChildren().get(2).getLabel());
        logger.info("\n"+temp);
    }
}
