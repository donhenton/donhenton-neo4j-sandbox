/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.neo4j.hospital.json;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonTypeName;

/**
 *
 * @author dhenton
 */
@JsonTypeName("division")
public class Division implements HospitalNode {
    
    
    private String label;
    private String type;
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
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    @Override
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

     
 
    @Override
    public String getNodeType() {
        return JSONHospitalService.NODE_TYPE.DIVISIONS.toString();
    }

    @Override
    public void setNodeType(String t) {
        
    }

    

    
    
    
    
    
}
