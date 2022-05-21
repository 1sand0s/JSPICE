/**
 * 
 */
package com.JSPICE.SElement;

import com.JSPICE.SMath.Complex;
import com.JSPICE.Util.ComponentDenominations;
import com.JSPICE.Util.ComponentTerminals;

/**
 * @author 1sand0s
 *
 */
public abstract class VSource extends SElement {
    protected double voltage;
    protected double frequency;

    public VSource() {
        denomination = ComponentDenominations.V;
        voltage = 0;
	frequency = 0;
        terminals = new Terminals(2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE);
    }

    
    @Override
    public double getValue() {
        return voltage;
    }

    @Override
    public void setValue(double value) {
        voltage = value;
    }

    @Override
    public Complex[] getVoltage(Complex[][] result) {
        return null;
    }
    
    @Override
    public Complex[] getCurrent(Complex[][] result,
				double frequency) {
        return null;
    }

    public void setFrequency(double frequency){
	this.frequency = frequency;
    }
}
