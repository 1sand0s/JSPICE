/**
 * 
 */
package com.JSPICE.SPICESolver;

import com.JSPICE.SMath.Complex;
import com.JSPICE.SMath.ComplexMatrixOperations;
import com.JSPICE.SElement.SElement;
import com.JSPICE.Util.ComponentTerminals;

/**
 * @author 1sand0s
 *
 */
public class ACSpiceResult extends AbstractSpiceResult {
    Complex x[];

    @Override
    public void updateResult(Complex x[][]){
	this.x = new Complex[x.length];
	
	for(int j = 0; j < x.length; j++){
	    this.x[j] = new Complex(x[j][0]);
	}
    }

    public Complex getElementVoltage(int index,
				     SElement element,
				     ComponentTerminals terminal1,
				     ComponentTerminals terminal2){
	int terminalIndex1 = element.getTerminalIndex(terminal1);
	int terminalIndex2 = element.getTerminalIndex(terminal2);
	
	return (ComplexMatrixOperations.Sub(x[terminalIndex1],
					    x[terminalIndex2]));
    }

    public Complex getElementCurrent(int index, SElement element){
	return null;
    }

    @Override
    public boolean resultMatch(AbstractSpiceResult result1, double tol){
	if(!(result1 instanceof ACSpiceResult))
	    return false;

	ACSpiceResult result = (ACSpiceResult) result1;
	
	if(result.x.length != x.length)
	    return false;
	
	for(int j = 0; j < x.length; j++){
	    if(!(x[j].getReal() == 0 && result.x[j].getReal() == 0)){
		if(!(Math.abs(result.x[j].getReal() - x[j].getReal()) / Math.abs(result.x[j].getReal() + x[j].getReal()) < tol)){
		    return false;
		}
	    }
	    
	    if(!(x[j].getImaginary() == 0 && result.x[j].getImaginary() == 0)){
		if(!(Math.abs(result.x[j].getImaginary() - x[j].getImaginary()) / Math.abs(result.x[j].getImaginary() + x[j].getImaginary()) < tol)){
		    return false;
		}
	    }
	}
	return true;
    }
}
