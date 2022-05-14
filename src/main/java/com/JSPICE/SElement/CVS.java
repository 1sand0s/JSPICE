package com.JSPICE.SElement;

import com.JSPICE.SMath.Complex;
import com.JSPICE.Util.ComponentDenominations;
import com.JSPICE.Util.ComponentTerminals;

/**
 * @author 1sand0s
 *
 */
public abstract class CVS extends VSource {

    protected double gain;
    protected Wire dependentPositiveRef;
    protected Wire dependentNegativeRef;
    
    public CVS() {
        denomination = ComponentDenominations.V;
       	gain = 0;
	dependentPositiveRef = null;
	dependentNegativeRef = null;
        terminals = new Terminals(2,
				  new ComponentTerminals[] { ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE });
    }

    public void setGain(double gain){
	this.gain = gain;
    }

    public void setPositiveReference(Wire dependentPositiveRef){
	this.dependentPositiveRef = dependentPositiveRef;
    }

    public void setNegativeReference(Wire dependentNegativeRef){
	this.dependentNegativeRef = dependentNegativeRef;
    }
}
