package com.JSPICE.SElement;

import com.JSPICE.SMath.Complex;
import com.JSPICE.Util.ComponentDenominations;
import com.JSPICE.Util.ComponentTerminals;

/**
 * @author 1sand0s
 *
 */
public abstract class OpAmp extends CVS {
    public OpAmp(){
	super();
	terminals = new Terminals(3, ComponentTerminals.OPAMP_INVERTING, ComponentTerminals.OPAMP_NONINVERTING, ComponentTerminals.OPAMP_OUTPUT);
	denomination = ComponentDenominations.U; 	
    }

    @Override
    public double getValue() {
        throw new UnsupportedOperationException("Error : getValue() not implemented for instance of " + this.getClass().toString());
    }

    @Override
    public void setValue(double value) {
        throw new UnsupportedOperationException("Error : getValue() not implemented for instance of " + this.getClass().toString());
    }
    
    @Override
    public void stampMatrixDC(Complex[][] G,
                                       Complex[][] B,
                                       Complex[][] C,
                                       Complex[][] D,
                                       Complex[][] z,
				       Complex[][] result,
                                       int iSourceIndex){
	int invertingNode = terminals.getTerminal(ComponentTerminals.OPAMP_INVERTING);
        int nonInvertingNode = terminals.getTerminal(ComponentTerminals.OPAMP_NONINVERTING);
	int outputNode = terminals.getTerminal(ComponentTerminals.OPAMP_OUTPUT);
	
        B[nonInvertingNode][iSourceIndex].add(new Complex(1, 0));
        B[invertingNode][iSourceIndex].add(new Complex(-1, 0));
	
        C[iSourceIndex][nonInvertingNode].add(new Complex(1, 0));
        C[iSourceIndex][invertingNode].add(new Complex(-1, 0));
	C[iSourceIndex][dependentPositiveRef.getNodeIndex()].add(new Complex(-gain, 0));
        C[iSourceIndex][dependentNegativeRef.getNodeIndex()].add(new Complex(gain, 0));
	
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
                                       double frequency){
	stampMatrixDC(G, B, C, D, z, result, iSourceIndex);
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
					      double deltaT){
	stampMatrixDC(G, B, C, D, z, result, iSourceIndex);
    }
}
