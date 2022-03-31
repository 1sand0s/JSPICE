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
    }
    
    @Override
    public void solve() {
	solve(circuitElements,
	      wires);
    }
    
    @Override
    public void solve(ArrayList<SElement> circuitElements,
		      ArrayList<Wire> wires) {
        int vSourceIndex = 0;
	
        G = new Complex[wires.size()][wires.size()];
        B = new Complex[wires.size()][iVSource + iISource];
        C = new Complex[iVSource + iISource][wires.size()];
        D = new Complex[iVSource + iISource][iVSource + iISource];
        z = new Complex[wires.size() + iVSource + iISource - 1][numHarmonics];

        numberNodes();

        ComplexMatrixOperations.initializeMatrices(G);
        ComplexMatrixOperations.initializeMatrices(B);
        ComplexMatrixOperations.initializeMatrices(C);
        ComplexMatrixOperations.initializeMatrices(D);
        ComplexMatrixOperations.initializeMatrices(z);

        for (int j = 0; j < circuitElements.size(); j++) {
            SElement element = circuitElements.get(j);

            element.stampMatrixDC(G, B, C, D, z, vSourceIndex); 

            if (element instanceof VSource)
                vSourceIndex++;
        }

	/* Exclude Row and Column corresponding to GND node to prevent singular matrix */
        Complex A[][] = constructMNAMatrix(G, B, C, D);

        x = ComplexMatrixOperations.computeLinearEquation(A, z);
	x = addGNDToResult(x);
    }
}
