/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.starter.neo4j.sandbox;

import static org.apache.commons.lang3.StringEscapeUtils.ESCAPE_XML;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dhenton
 */
public class EscapeTests {
    
   @Test
    public void testEscape()
    {
       String start = "Love & Monsters";
       String expect = "Love &amp; Monsters";
       String test = ESCAPE_XML.translate(start);
       
       assertEquals("escape error",expect,test);
       
       
    }
    
    
}
