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
public class TransientSpiceSolver extends AbstractSpiceSolver {

    private double tmin = 0;
    private double tmax = 0;
    private double tStep = 0;
    private int numPoints = 0;
    private AbstractSpiceSolver.TimeStepType type;
    
    /**
     * 
     */
    public TransientSpiceSolver() {
        circuitElements = new ArrayList<SElement>();
        wires = new ArrayList<Wire>();
    }

    @Override
    public void setTimeStep(double tmin,
			    double tmax,
			    int numPoints,
			    AbstractSpiceSolver.TimeStepType type) {
	this.tmin = tmin;
	this.tmax = tmax;
	this.type = type;
	this.numPoints = numPoints;
	this.tStep = (tmax - tmin) / numPoints;
    }

    @Override
    public void setTimeStep(double tmin,
			    double tmax,
			    double tStep) {
	this.tmin = tmin;
	this.tmax = tmax;
	this.tStep = tStep;
	this.type = AbstractSpiceSolver.TimeStepType.LINEAR;
    }

    @Override
    public void solve() {
	
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
