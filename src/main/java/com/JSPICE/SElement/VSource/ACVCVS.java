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
public class ACVCVS extends CVS {
    
    public ACVCVS() {
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

	/* Short circuit AC dependent source during DC Analysis*/
	int posNode = terminals.getTerminal(ComponentTerminals.POS_NODE);
	int negNode = terminals.getTerminal(ComponentTerminals.NEG_NODE);
	
        B[posNode][iSourceIndex].add(new Complex(1, 0));
        B[negNode][iSourceIndex].add(new Complex(-1, 0));

        C[iSourceIndex][posNode].add(new Complex(1, 0));
        C[iSourceIndex][negNode].add(new Complex(-1, 0));

        z[G.length + iSourceIndex][0].add(new Complex(0, 0));
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
	C[iSourceIndex][dependentPositiveRef.getNodeIndex()].add(new Complex(-gain, 0));
        C[iSourceIndex][dependentNegativeRef.getNodeIndex()].add(new Complex(gain, 0));

        z[G.length + iSourceIndex][0].add(new Complex(0, 0));
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
        /* Short circuit AC dependent source during transient Analysis*/
	stampMatrixDC(G, B, C, D, z, result, iSourceIndex);
    }   
}
