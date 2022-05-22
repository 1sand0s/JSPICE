package com.JSPICE.SElement.VSource;

import com.JSPICE.SMath.Complex;
import com.JSPICE.Util.ComponentDenominations;
import com.JSPICE.Util.ComponentTerminals;
import com.JSPICE.SElement.SElement;
import com.JSPICE.SElement.Wire;
import com.JSPICE.SElement.Terminals;

/**
 * @author 1sand0s
 *
 */
public class SinusoidVoltage extends VSource {
    
    public SinusoidVoltage() {
        super();
    }

    @Override
    public void stampMatrixDC(Complex[][] G,
                              Complex[][] B,
                              Complex[][] C,
                              Complex[][] D,
                              Complex[][] z,
			      Complex[][] result,
                              int iSourceIndex) {
        int posNode = terminals.getTerminal(ComponentTerminals.POS_NODE);
        int negNode = terminals.getTerminal(ComponentTerminals.NEG_NODE);

        B[posNode][iSourceIndex].add(new Complex(1, 0));
        B[negNode][iSourceIndex].add(new Complex(-1, 0));

        C[iSourceIndex][posNode].add(new Complex(1, 0));
        C[iSourceIndex][negNode].add(new Complex(-1, 0));

	/* Transient Sources turned off during DC analysis*/
        z[G.length + iSourceIndex][0].add(new Complex(voltage * 0, 0));
    }

    @Override
    public void stampMatrixAC(Complex[][] G,
                              Complex[][] B,
                              Complex[][] C,
                              Complex[][] D,
                              Complex[][] z,
			      Complex[][] result,
                              int iSourceIndex,
                              double frequency) {
        int posNode = terminals.getTerminal(ComponentTerminals.POS_NODE);
        int negNode = terminals.getTerminal(ComponentTerminals.NEG_NODE);

        B[posNode][iSourceIndex].add(new Complex(1, 0));
        B[negNode][iSourceIndex].add(new Complex(-1, 0));
	
        C[iSourceIndex][posNode].add(new Complex(1, 0));
        C[iSourceIndex][negNode].add(new Complex(-1, 0));

	/* r/_phi form to a + jb form conversion 
	 * Stamp the amplitude and phase at time 0 for AC analysis
	 */
	double real = voltage * Math.cos(phase);
	double imag = voltage * Math.sin(phase);
        z[G.length + iSourceIndex][0].add(new Complex(real, imag));
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
	int posNode = terminals.getTerminal(ComponentTerminals.POS_NODE);
        int negNode = terminals.getTerminal(ComponentTerminals.NEG_NODE);
	
        B[posNode][iSourceIndex].add(new Complex(1, 0));
        B[negNode][iSourceIndex].add(new Complex(-1, 0));
	
        C[iSourceIndex][posNode].add(new Complex(1, 0));
        C[iSourceIndex][negNode].add(new Complex(-1, 0));

        z[G.length + iSourceIndex][0].add(new Complex(voltage * Math.sin(2 * Math.PI * frequency * time + phase), 0));
    }   
}

