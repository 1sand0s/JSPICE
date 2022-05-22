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
public abstract class ISource extends SElement {
    protected double current;
    protected double frequency;
    protected double phase;

    public ISource() {
        denomination = ComponentDenominations.I;
        current = 0;
	frequency = 0;
	phase = 0;
        terminals = new Terminals(2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE);
    }

    
    @Override
    public double getValue() {
        return current;
    }

    @Override
    public void setValue(double value) {
        current = value;
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

    /**
     * @brief Sets the frequency of the source (Valid for AC and 
     *        SinusoidCurrent source)
     * 
     * @author 1sand0s
     * @return double : frequency The frequency of the source
     * @since 1.0.0
     * @version 1.0.0
     */
    public void setFrequency(double frequency){
	this.frequency = frequency;
    }

    /**
     * @brief Sets the phase of the source (Valid for AC and 
     *        SinusoidCurrent source)
     * 
     * @author 1sand0s
     * @return double : phase The phase of the source
     * @since 1.0.0
     * @version 1.0.0
     */    
    public void setPhase(double phase){
	this.phase = phase;
    }
}
