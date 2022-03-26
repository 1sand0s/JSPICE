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

    private double tMin = 0;
    private double tMax = 0;
    private double tStep = 0;
    private int numPoints = 0;
    private double time[];
    private AbstractSpiceSolver.TimeStepType type;
    
    /**
     * 
     */
    public TransientSpiceSolver() {
        circuitElements = new ArrayList<SElement>();
        wires = new ArrayList<Wire>();
    }

    @Override
    public void setTimeStep(double tMin,
			    double tMax,
			    int numPoints,
			    AbstractSpiceSolver.TimeStepType type) {
	this.tMin = tMin;
	this.tMax = tMax;
	this.type = type;
	this.numPoints = numPoints;
    }

    @Override
    public void setTimeStep(double tMin,
			    double tMax,
			    double tStep) {
	this.tMin = tMin;
	this.tMax = tMax;
	this.tStep = tStep;
	this.type = AbstractSpiceSolver.TimeStepType.LINEAR;
    }

    @Override
    public void expandTime() {
	switch(type){
	case LINEAR:
	    if(numPoints == 0){
		numPoints = (int)((tMax - tMin) / tStep);
	    }
	    else if(tStep == 0){
		tStep = (tMax - tMin) / numPoints;
	    }
	    time = new double[numPoints];
	    for(int j = 0; j < numPoints; j++){
		time[j] = tMin + j * tStep;
	    }
	    break;
	case LOGARITHMIC:
	    break;
	case PWL:
	    break;
	}
    }
    
    @Override
    public void solve() {
	solve(circuitElements,
	      wires);
    }

    @Override
    public void solve(ArrayList<SElement> circuitElements,
		      ArrayList<Wire> wires) {

	/* Populate time vector 
	 * 
	 * The time steps here may not be respected if 
	 * convergence is not achieved 
	 */
	expandTime();

	/* We could use all zeroes as the initial guess for solution
	 * but the solution of a DC solver will get us to converge faster
	 */
	DCSpiceSolver dcSolver = new DCSpiceSolver();
	dcSolver.solve(circuitElements,
		       wires);
	x = dcSolver.getResult();
	
	for(int j = 0; j < time.length; j++){
	    solve(circuitElements,
		  wires,
		  time[j]);
	}
    }
    
    @Override
    public void solve(ArrayList<SElement> circuitElements,
		      ArrayList<Wire> wires,
		      double t){
	
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
            element.stampMatrixTransient(G, B, C, D, z, x, vSourceIndex, t);
	    
            if (element instanceof VSource)
                vSourceIndex++;
        }
	
	/* Exclude Row and Column corresponding to GND node to prevent singular matrix */
        Complex A[][] = constructMNAMatrix(G, B, C, D);
	
        x = ComplexMatrixOperations.computeLinearEquation(A, z);
	x = addGNDToResult(x);
    }
}
