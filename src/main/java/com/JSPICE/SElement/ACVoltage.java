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
public class ACVoltage extends VSource {

    public ACVoltage() {
        denomination = ComponentDenominations.V;
        voltage = 0;
        terminals = new Terminals(2,
                new ComponentTerminals[] { ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE });
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

        B[posNode][iSourceIndex].add(new Complex(1, 0));
        B[negNode][iSourceIndex].add(new Complex(-1, 0));

        C[iSourceIndex][posNode].add(new Complex(1, 0));
        C[iSourceIndex][negNode].add(new Complex(-1, 0));

        /* AC sources turned off during DC analysis */
        z[G.length + iSourceIndex - 1][0].add(new Complex(voltage * 0, 0));
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

        B[posNode][iSourceIndex].add(new Complex(1, 0));
        B[negNode][iSourceIndex].add(new Complex(-1, 0));

        C[iSourceIndex][posNode].add(new Complex(1, 0));
        C[iSourceIndex][negNode].add(new Complex(-1, 0));

        z[G.length + iSourceIndex - 1][0].add(new Complex(voltage, 0));
    }

    @Override
    public void stampMatrixTransient(Complex[][] G,
				     Complex[][] B,
				     Complex[][] C,
				     Complex[][] D,
				     Complex[][] z,
				     Complex[][] result,
				     int iSourceIndex,
				     double time,
				     double deltaT) {
	/* AC sources are not allowed for transient simulation */
    }
}
