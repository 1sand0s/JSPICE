/**
 * 
 */
package com.JSPICE.SElement;

import com.JSPICE.Util.ComponentTerminals;

/**
 * @author 1sand0s
 *
 */
public class Node {
    private ComponentTerminals name;
    private int index;
    
    /**
     * @param name : Terminal name
     */
    public Node(ComponentTerminals name) {
        this.name = name;
    }
    
    /**
    * regular
    * @author 1sand0s
    * @param index : global index of the node
    * @since 1.0.0
    * @version 1.0.0
    */
    public void setIndex(int index) {
        this.index = index;
    }
    
    /**
    * regular
    * @author 1sand0s
    * @return int : index associated with the node
    * @since 1.0.0
    * @version 1.0.0
    */
    public int getIndex() {
        return this.index;
    }
    
    /**
    * regular
    * @author 1sand0s
    * @return ComponentTerminals : node name
    * @since 1.0.0
    * @version 1.0.0
    */
    public ComponentTerminals getName() {
        return this.name;
    }
}
