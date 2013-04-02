/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.hospital.json;

import java.util.List;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;

/**
 *
 * @author dhenton
 */

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="type")
  @JsonSubTypes({

        @JsonSubTypes.Type(value=Division.class),
        @JsonSubTypes.Type(value=Provider.class)

    }) 
public interface HospitalNode {

    String getLabel();
    List<HospitalNode> getChildren();
    void setChildren(List<HospitalNode> children);
    public Long getId();
    public void setId(Long id);
    //public String getNodeType();
    //public void setNodeType(String t);
   
     
}
