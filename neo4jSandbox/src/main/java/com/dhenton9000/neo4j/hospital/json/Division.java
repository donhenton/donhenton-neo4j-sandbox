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
//@JsonTypeName("")
public class Division  {
    
    
    private String label;
    private Long id;
    private List<Division> children =  new ArrayList<Division>();

    /**
     * @return the label
     */
    
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
    
     
    public List<Division> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    
    public void setChildren(List<Division> children) {
        this.children = children;
    }

//    @Override
//    public NODE_TYPE getNodeType() {
//        return NODE_TYPE.DIVISIONS;
//    }
    
    
    
    
}
