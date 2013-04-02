package com.dhenton9000.neo4j.starter.neo4j.json;

import com.dhenton9000.neo4j.hospital.json.Division;
import com.dhenton9000.neo4j.hospital.json.HospitalNode;
import java.io.IOException;
import java.util.ArrayList;
import junit.framework.Assert;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
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

    @Test
    public void canSerializeBook() {

        Assert.assertTrue("Cannot serialize Book class", mapper.canSerialize(Book.class));
    }

    @Test
    public void BookToJson() throws JsonGenerationException, JsonMappingException, IOException {
        String temp = mapper.defaultPrettyPrintingWriter().writeValueAsString(book);

        if (logger.isInfoEnabled()) {
            logger.info(temp);
        }

        Assert.assertNotNull("Writing Book as JSON string resulted in null.", temp);
    }

   

   
}