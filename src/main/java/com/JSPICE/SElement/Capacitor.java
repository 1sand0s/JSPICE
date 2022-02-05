package com.JSPICE.SElement;

import com.JSPICE.Util.ComponentTerminals;
import com.JSPICE.SMath.Complex;
import com.JSPICE.Util.ComponentDenominations;

/**
 * @author 1sand0s
 *
 */
public class Capacitor extends SElement {

    private double capacitance;
    private double epsilonLoss = 1e9;

    public Capacitor() {
        denomination = ComponentDenominations.C;
        capacitance = 0;
        terminals = new Terminals(2,
                new ComponentTerminals[] { ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE });
    }

    @Override
    public double getValue() {
        return capacitance;
    }

    @Override
    public void setValue(double value) {
        capacitance = value;
    }

    @Override
    public double getVoltage(double[] result) {
        int[] nodes = terminals.getTerminals();

        return (result[nodes[0]] - result[nodes[1]]);
    }

    @Override
    public double getCurrent(double[] result) {
        return (getVoltage(result) * capacitance);
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

        /* Stamp high impedance dielectric loss during DC*/
        G[posNode][posNode].add(new Complex(1 / epsilonLoss, 0));
        G[negNode][negNode].add(new Complex(1 / epsilonLoss, 0));
        G[posNode][negNode].add(new Complex(-1 / epsilonLoss, 0));
        G[negNode][posNode].add(new Complex(-1 / epsilonLoss, 0));
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

        /* Stamp capacitance reactance during AC */
        G[posNode][posNode].add(new Complex(0, -(capacitance * frequency * 2 * Math.PI)));
        G[negNode][negNode].add(new Complex(0, -(capacitance * frequency * 2 * Math.PI)));
        G[posNode][negNode].add(new Complex(0, (capacitance * frequency * 2 * Math.PI)));
        G[negNode][posNode].add(new Complex(0, (capacitance * frequency * 2 * Math.PI)));
    }
}
