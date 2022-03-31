/**
 * 
 */
package com.JSPICE.SPICESolver;

import com.JSPICE.SMath.Complex;
import com.JSPICE.SElement.SElement;
import com.JSPICE.Util.ComponentTerminals;

/**
 * @author 1sand0s
 *
 */
public class DCSpiceResult extends AbstractSpiceResult {
    double x[];

    @Override
    public void updateResult(Complex x[][]){
	this.x = new double[x.length];

	for(int j = 0; j < x.length; j++)
	    this.x[j] = x[j][0].getReal();
    }
    
    public double getElementVoltage(SElement element,
				    ComponentTerminals terminal1,
				    ComponentTerminals terminal2){
	int terminalIndex1 = element.getTerminalIndex(terminal1);
	int terminalIndex2 = element.getTerminalIndex(terminal2);

	return (x[terminalIndex1] - x[terminalIndex2]);
    }

    public double getElementCurrent(SElement element){
	return 0.0;
    }

    @Override
    public boolean resultMatch(AbstractSpiceResult result1, double tol){
	if(!(result1 instanceof DCSpiceResult))
	    return false;

	DCSpiceResult result = (DCSpiceResult) result1;
	
	if(result.x.length != x.length)
	    return false;

	for(int j = 0; j < x.length; j++){
	    if(!(x[j] == 0 && result.x[j] == 0)){
		if(!(Math.abs(result.x[j] - x[j]) / Math.abs(result.x[j] + x[j]) < tol)){
		    return false;
		}
	    }
	}
	return true;
    }
}

