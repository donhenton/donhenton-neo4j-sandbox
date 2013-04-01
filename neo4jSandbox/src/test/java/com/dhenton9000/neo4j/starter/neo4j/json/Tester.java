package com.dhenton9000.neo4j.starter.neo4j.json;

import com.dhenton9000.neo4j.starter.neo4j.sandbox.MatrixTests;
import com.dhenton9000.neo4j.starter.neo4j.sandbox.hospital.json.Division;
import com.dhenton9000.utils.xml.XMLUtils;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import junit.framework.Assert;


import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tester {

    private final Logger logger = LoggerFactory.getLogger(Tester.class);
    private Book book = new Book();
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void init() {
        ArrayList<String> revs = new ArrayList<String>();

        revs.add("Manny");
        revs.add("Moe");
        revs.add("Jack");
        book.setReviewers(revs);
    }

    //@Test
    public void canSerializeBook() {

        Assert.assertTrue("Cannot serialize Book class", mapper.canSerialize(Book.class));
    }

    //@Test
    public void BookToJson() throws JsonGenerationException, JsonMappingException, IOException {
        String temp = mapper.defaultPrettyPrintingWriter().writeValueAsString(book);

        if (logger.isInfoEnabled()) {
            logger.info(temp);
        }

        Assert.assertNotNull("Writing Book as JSON string resulted in null.", temp);
    }

    @Test
    public void testLoadJSON() throws Exception {
        // InputStream in = this.getClass().getClassLoader().getResourceAsStream("json_tree.json");

        // mapper.readValue(in, Division.class);


        Division root = getSampleRoot();
        String temp = mapper.defaultPrettyPrintingWriter().writeValueAsString(root);
        logger.info("\n" + temp);

    }

    private Division getSampleRoot() {

        ArrayList<Division> children = new ArrayList<Division>();
        Division d = null;
        Division root = new Division();
        root.setLabel("Alpha");
        root.setId(new Long(1));

        d = new Division();
        d.setLabel("Manny");
        d.setId(new Long(100));
        children.add(d);

        d = new Division();
        d.setLabel("Moe");
        d.setId(new Long(101));
        children.add(d);

        ArrayList<Division> d2 = new ArrayList<Division>();
        d.setChildren(d2);

        d = new Division();
        d.setLabel("Huey");
        d.setId(new Long(201));
        d2.add(d);
        d = new Division();
        d.setLabel("Dewey");
        d.setId(new Long(202));
        d2.add(d);
        d = new Division();
        d.setLabel("Louie");
        d.setId(new Long(203));
        d2.add(d);

        d = new Division();
        d.setLabel("Jack");
        d.setId(new Long(102));
        children.add(d);


        root.setChildren(children);

        return root;


    }
}