/**
 * 
 */
package com.JSPICE.SElement;

import com.JSPICE.SMath.Complex;

/**
 * @author 1sand0s
 *
 */
public abstract class VSource extends SElement {
    protected double voltage;

    @Override
    public double getValue() {
        return voltage;
    }

    @Override
    public void setValue(double value) {
        voltage = value;
    }

    @Override
    public Complex getVoltage(Complex[] result) {
        return new Complex(voltage, 0);
    }

    @Override
    public Complex getCurrent(Complex[] result,
			      double frequency) {
        return null;
    }
}
