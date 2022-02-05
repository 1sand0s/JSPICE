/**
 * 
 */
package com.JSPICE.SElement;

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
    public double getVoltage(double[] result) {
        return voltage;
    }

    @Override
    public double getCurrent(double[] result) {
        return 0;
    }
}
