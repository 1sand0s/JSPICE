/**
 * 
 */
package com.JSPICE.SPICESolver;

import java.util.ArrayList;

import com.JSPICE.SMath.Complex;
import com.JSPICE.SElement.SElement;
import com.JSPICE.Util.ComponentTerminals;

/**
 * @author 1sand0s
 *
 */
public class TransientSpiceResult extends AbstractSpiceResult {
    ArrayList<double[]> x;

    TransientSpiceResult(){
	x = new ArrayList<double[]>();
    }

    @Override
    public void updateResult(Complex x[][]){
	double x1[] = new double[x.length];
	
	for(int j = 0; j < x.length; j++)
	    x1[j] = x[j][0].getReal();
	this.x.add(x1);
    }

    @Override
    public void clearResult(){
	x.clear();
    }
    
    public double getElementVoltage(int index,
				    SElement element,
				    ComponentTerminals terminal1,
				    ComponentTerminals terminal2){
	int terminalIndex1 = element.getTerminalIndex(terminal1);
	int terminalIndex2 = element.getTerminalIndex(terminal2);

	return (x.get(index)[terminalIndex1] - x.get(index)[terminalIndex2]);
    }
    
    public double getElementCurrent(int index,
				    SElement element){
	return 0.0;
    }

    @Override
    public boolean resultMatch(AbstractSpiceResult result1, double tol){
	if(!(result1 instanceof TransientSpiceResult))
	    return false;

	TransientSpiceResult result = (TransientSpiceResult) result1;
	
	if(result.x.size() != x.size())
	    return false;
	
	for(int j = 0; j < x.size(); j++){
	    if(x.get(j).length != result.x.get(j).length)
		return false;

	    for(int k = 0; k < x.get(j).length; k++){
		if(!(x.get(j)[k] == 0 && result.x.get(j)[k] == 0)){
		    if(!(Math.abs(result.x.get(j)[k] - x.get(j)[k]) / Math.abs(result.x.get(j)[k] + x.get(j)[k]) < tol)){
			return false;
		    }
		}
	    }
	}
	return true;
    }

    @Override
    public boolean resultMatch(int j, AbstractSpiceResult result1, double tol){
	if(!(result1 instanceof TransientSpiceResult))
	    return false;

	TransientSpiceResult result = (TransientSpiceResult) result1;
	
        if(x.get(j).length != result.x.get(0).length)
	    return false;
	
	for(int k = 0; k < x.get(j).length; k++){
	    if(!(x.get(j)[k] == 0 && result.x.get(0)[k] == 0)){
		if(!(Math.abs(result.x.get(0)[k] - x.get(j)[k]) / Math.abs(result.x.get(0)[k] + x.get(j)[k]) < tol)){
		    return false;
		}
	    }
	}
	return true;
    }
}
