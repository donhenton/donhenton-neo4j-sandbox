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
public interface HospitalNode {

    String getLabel();

    List<HospitalNode> getChildren();

    void setChildren(List<HospitalNode> children);

    public Long getId();

    public void setId(Long id);
    
    public NODE_TYPE getNodeType();
}
