/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.hospital.json;

import com.dhenton9000.neo4j.hospital.json.JSONHospitalService.NODE_TYPE;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 *
 * @author dhenton
 */
@JsonTypeName("division")
public class Division implements HospitalNode {
    
    
    private String label;
    private Long id;
    private List<HospitalNode> children =  new ArrayList<HospitalNode>();

    /**
     * @return the label
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    
    /**
     * @return the children
     */
    @Override
     
    public List<HospitalNode> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    @Override
    public void setChildren(List<HospitalNode> children) {
        this.children = children;
    }

//    @Override
//    public NODE_TYPE getNodeType() {
//        return NODE_TYPE.DIVISIONS;
//    }
    
    
    
    
}
