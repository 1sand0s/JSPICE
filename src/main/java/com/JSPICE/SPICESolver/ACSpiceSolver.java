/**
 * 
 */
package com.JSPICE.SPICESolver;

import java.util.ArrayList;

import com.JSPICE.SElement.*;
import com.JSPICE.SMath.Complex;
import com.JSPICE.SMath.ComplexMatrixOperations;

/**
 * @author 1sand0s
 *
 */
public class ACSpiceSolver extends AbstractSpiceSolver {

    private double frequency = 0;

    /**
     * 
     */
    public ACSpiceSolver() {
        circuitElements = new ArrayList<SElement>();
        wires = new ArrayList<Wire>();
    }

    @Override
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    @Override
    public void solve() {
        int vSourceIndex = 0;

        G = new Complex[wires.size()][wires.size()];
        B = new Complex[wires.size()][iVSource + iISource];
        C = new Complex[iVSource + iISource][wires.size()];
        D = new Complex[iVSource + iISource][iVSource + iISource];
        z = new Complex[wires.size() + iVSource + iISource - 1][1];

        numberNodes();

        ComplexMatrixOperations.initializeMatrices(G);
        ComplexMatrixOperations.initializeMatrices(B);
        ComplexMatrixOperations.initializeMatrices(C);
        ComplexMatrixOperations.initializeMatrices(D);
        ComplexMatrixOperations.initializeMatrices(z);

        for (int j = 0; j < circuitElements.size(); j++) {
            SElement element = circuitElements.get(j);
            element.stampMatrixAC(G, B, C, D, z, vSourceIndex, frequency);

            if (element instanceof VSource)
                vSourceIndex++;
        }
        Complex A[][] = ComplexMatrixOperations.matrixConcatenator(G, B, C, D);
        Complex xTemp[][] = ComplexMatrixOperations.computeLinearEquation(A, z);
       
	x = new Complex[wires.size() + iVSource + iISource][1];
	x[0][0] = new Complex(0, 0);
	for(int j = 0; j < xTemp.length; j++){
	    x[j + 1][0] = xTemp[j][0];
	}
    }
}
