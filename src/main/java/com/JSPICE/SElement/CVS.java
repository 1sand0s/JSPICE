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
	super();
       	gain = 0;
	dependentPositiveRef = null;
	dependentNegativeRef = null;
    }

    /**
     * @brief Sets the voltage gain of the dependent voltage source
     * 
     * @author 1sand0s
     * @param double : gain The gain of the dependent voltage source
     * @since 1.0.0
     * @version 1.0.0
     */
    public void setGain(double gain){
	this.gain = gain;
    }

    /**
     * @brief Sets the positive reference node for the dependent source
     * 
     * @author 1sand0s
     * @param Wire : dependentPositiveRef The circuit node corresponding 
     *                                    to the positive ref of the 
     *                                    dependent source
     * @since 1.0.0
     * @version 1.0.0
     */
    public void setPositiveReference(Wire dependentPositiveRef){
	this.dependentPositiveRef = dependentPositiveRef;
    }

    /**
     * @brief Sets the negative reference node for the dependent source
     * 
     * @author 1sand0s
     * @param Wire : dependentNegativeRef The circuit node corresponding 
     *                                    to the negative ref of the 
     *                                    dependent source
     * @since 1.0.0
     * @version 1.0.0
     */
    public void setNegativeReference(Wire dependentNegativeRef){
	this.dependentNegativeRef = dependentNegativeRef;
    }
}
