package com.JSPICE.SElement;

import com.JSPICE.SMath.Complex;
import com.JSPICE.Util.ComponentDenominations;
import com.JSPICE.Util.ComponentTerminals;

/**
 * @author 1sand0s
 *
 */
public class Inductor extends SElement {

    private double inductance;
    private double gMinResistance = 1e-3;

    public Inductor() {
        denomination = ComponentDenominations.L;
        inductance = 0;
        terminals = new Terminals(2,
                new ComponentTerminals[] { ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE });
    }

    @Override
    public double getValue() {
        return inductance;
    }

    @Override
    public void setValue(double value) {
        inductance = value;
    }

    @Override
    public double getVoltage(double[] result) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getCurrent(double[] result) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void stampMatrixDC(Complex[][] G,
                              Complex[][] B,
                              Complex[][] C,
                              Complex[][] D,
                              Complex[][] z,
                              int iSourceIndex) {
        int posNode = terminals.getTerminal(ComponentTerminals.POS_NODE);
        int negNode = terminals.getTerminal(ComponentTerminals.NEG_NODE);

        /* Stamp low impedance during DC (prevents singular matrices) */
        G[posNode][posNode].add(new Complex(1 / gMinResistance, 0));
        G[negNode][negNode].add(new Complex(1 / gMinResistance, 0));
        G[posNode][negNode].add(new Complex(-1 / gMinResistance, 0));
        G[negNode][posNode].add(new Complex(-1 / gMinResistance, 0));
    }

    @Override
    public void stampMatrixAC(Complex[][] G,
                              Complex[][] B,
                              Complex[][] C,
                              Complex[][] D,
                              Complex[][] z,
                              int iSourceIndex,
                              double frequency) {
        int posNode = terminals.getTerminal(ComponentTerminals.POS_NODE);
        int negNode = terminals.getTerminal(ComponentTerminals.NEG_NODE);

        /* Stamp inductive reactance during AC */
        G[posNode][posNode].add(new Complex(0, 1 / (inductance * frequency * 2 * Math.PI)));
        G[negNode][negNode].add(new Complex(0, 1 / (inductance * frequency * 2 * Math.PI)));
        G[posNode][negNode].add(new Complex(0, -1 / (inductance * frequency * 2 * Math.PI)));
        G[negNode][posNode].add(new Complex(0, -1 / (inductance * frequency * 2 * Math.PI)));
    }
}
