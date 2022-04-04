/**
 * 
 */
package com.JSPICE.SPICESolver;

import com.JSPICE.SElement.*;
import com.JSPICE.SMath.ComplexMatrixOperations;
import com.JSPICE.SMath.Complex;

import java.util.ArrayList;

/**
 * @author 1sand0s
 *
 */
public class DCSpiceSolver extends AbstractSpiceSolver {

    /**
     * 
     */
    public DCSpiceSolver() {
        circuitElements = new ArrayList<SElement>();
        wires = new ArrayList<Wire>();
	result = new DCSpiceResult();
	/* Default tolerance for testing convergence of Newton-Raphson */
	tol = 1e-5;
    }
    
    @Override
    public void solve() {
	solve(circuitElements,
	      wires);
    }
    
    @Override
    public void solve(ArrayList<SElement> circuitElements,
		      ArrayList<Wire> wires) {

	G = new Complex[wires.size()][wires.size()];
        B = new Complex[wires.size()][iVSource + iISource];
        C = new Complex[iVSource + iISource][wires.size()];
        D = new Complex[iVSource + iISource][iVSource + iISource];
        z = new Complex[wires.size() + iVSource + iISource][numHarmonics];
	x = new Complex[wires.size() + iVSource + iISource][numHarmonics];

	/* Number Circuit nodes */
        numberNodes();

	/* Initial guess for all node voltages and branch currents are 0's*/
	ComplexMatrixOperations.initializeMatrices(x);

	/* Holds result after each Newton-Raphson iteration */
	Complex xSolved[][];
	
	do{
	    xSolved = x;
	    int vSourceIndex = 0;

	    /* Re-initialize all matrices to 0's
	     * [+] TODO : Optimize since only 'x' changes between 
	     *            Newton-Raphson iterations
	     */
	    ComplexMatrixOperations.initializeMatrices(G);
	    ComplexMatrixOperations.initializeMatrices(B);
	    ComplexMatrixOperations.initializeMatrices(C);
	    ComplexMatrixOperations.initializeMatrices(D);
	    ComplexMatrixOperations.initializeMatrices(z);

	    /* Stamp each circuit element into the MNA matrix */
	    for (int j = 0; j < circuitElements.size(); j++) {
		SElement element = circuitElements.get(j);
		
		element.stampMatrixDC(G, B, C, D, z, x, vSourceIndex); 
		
		if (element instanceof VSource)
		    vSourceIndex++;
	    }
	    
	    /* Exclude Row and Column corresponding to GND node to prevent singular matrix */
	    Complex A[][] = constructMNAMatrix(G, B, C, D);

	    x = ComplexMatrixOperations.computeLinearEquation(A, removeGNDFromResult(z));
	    x = addGNDToResult(x);	
	    
	} while(!ComplexMatrixOperations.compareMatrices(x, xSolved, tol));
	result.updateResult(x);
    }
}
