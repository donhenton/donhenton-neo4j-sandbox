/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.hospital.json;

import com.dhenton9000.neo4j.hospital.json.JSONHospitalService.NODE_TYPE;
import java.util.List;

/**
 *
 * @author dhenton
 */
//@JsonTypeName("provider")
public class Provider extends Division {

    @Override
    public String getNodeType() {
       return NODE_TYPE.PROVIDERS.toString();
    }

    

    
    
    
     
    
}
