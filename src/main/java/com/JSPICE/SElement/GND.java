package com.JSPICE.SElement;

import com.JSPICE.SMath.Complex;
import com.JSPICE.Util.ComponentDenominations;
import com.JSPICE.Util.ComponentTerminals;

/**
 * @author 1sand0s
 *
 */
public class GND extends SElement {

    public GND() {
        denomination = ComponentDenominations.G;
        terminals = new Terminals(1, new ComponentTerminals[] { ComponentTerminals.GND });
    }

    @Override
    public double getValue() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setValue(double value) {
        // TODO Auto-generated method stub

    }

    @Override
    public Complex[] getVoltage(Complex[][] result) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Complex[] getCurrent(Complex[][] result,
				double frequency) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void stampMatrixDC(Complex[][] G,
                              Complex[][] B,
                              Complex[][] C,
                              Complex[][] D,
                              Complex[][] z,
                              int iSourceIndex) {
    }

    @Override
    public void stampMatrixAC(Complex[][] G,
                              Complex[][] B,
                              Complex[][] C,
                              Complex[][] D,
                              Complex[][] z,
                              int iSourceIndex,
                              double frequency) {
    }
}
