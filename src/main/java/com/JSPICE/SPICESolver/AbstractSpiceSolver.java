/**
 * 
 */
package com.JSPICE.SPICESolver;

import com.JSPICE.SElement.SElement;
import com.JSPICE.SMath.Complex;
import com.JSPICE.SMath.ComplexMatrixOperations;
import com.JSPICE.SElement.Wire;
import com.JSPICE.SElement.VSource;

import java.util.ArrayList;

/**
 * @author 1sand0s
 *
 */
public abstract class AbstractSpiceSolver {
    protected ArrayList<SElement> circuitElements;
    protected ArrayList<Wire> wires;
    protected Complex G[][];
    protected Complex B[][];
    protected Complex C[][];
    protected Complex D[][];
    protected Complex x[][];
    protected Complex z[][];
    protected int iVSource = 0;
    protected int iISource = 0;
    protected int numHarmonics = 1;
    protected AbstractSpiceResult result;
    protected double tol;
    
    public enum TimeStepType{
	LINEAR,
	LOGARITHMIC,
	PWL
    };
    
    /**
     * regular
     * 
     * @author 1sand0s
     * @param
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public void addElement(SElement element) {
        circuitElements.add(element);
        if (element instanceof VSource)
            iVSource++;
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public void addElements(ArrayList<SElement> elements) {
	for(int j = 0; j < elements.size(); j++){
	    circuitElements.add(elements.get(j));
	    if (elements.get(j) instanceof VSource)
		iVSource++;   
	}
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public void removeElement(int index) {
        for (int j = 0; j < circuitElements.size(); j++)
            if (circuitElements.get(j).getId_Int() == index) {
                if (circuitElements.get(j) instanceof VSource)
                    iVSource--;
                circuitElements.remove(j);
                break;
            }
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public void addWire(Wire wire) {
        wires.add(wire);
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public void addWires(ArrayList<Wire> wire) {
        for(int j = 0; j < wire.size(); j++)
	    wires.add(wire.get(j));
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public abstract void solve();

    /**
     * regular
     * 
     * @author 1sand0s
     * @param
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public abstract void solve(ArrayList<SElement> circuitElements,
			       ArrayList<Wire> wires);

    /**
     * regular
     * 
     * @author 1sand0s
     * @param
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public void solve(ArrayList<SElement> circuitElements,
		      ArrayList<Wire> wires,
		      double t,
		      double deltaT){
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public AbstractSpiceResult getResult() {
        return result;
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param frequency
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public void setFrequency(double frequency) {
    }
    
    /**
     * regular
     * 
     * @author 1sand0s
     * @param frequency
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public void setTimeStep(double tmin,
			    double tmax,
			    int numPoints,
			    TimeStepType type) {
    }
    
    /**
     * regular
     * 
     * @author 1sand0s
     * @param frequency
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */    
    public void setTimeStep(double tmin,
			    double tmax,
			    double tStep) {
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public void numberNodes() {
        int index = 1;
        int gndIndex = -1;
        for (int j = 0; j < wires.size(); j++) {
            if (!wires.get(j).isGND()) {
                for (int i = 0; i < wires.get(j).getNumElements(); i++)
                    wires.get(j).getElementAtIndex(i).setTerminalIndex(index, wires.get(j).getTerminalAtIndex(i));
                index++;
            } else
                gndIndex = j;
        }

        if (gndIndex != -1)
            for (int i = 0; i < wires.get(gndIndex).getNumElements(); i++)
                wires.get(gndIndex).getElementAtIndex(i).setTerminalIndex(0,
                        wires.get(gndIndex).getTerminalAtIndex(i));
    }
    
    /**
     * @brief Constructs the MNA matrix and also excludes columns and rows
     *        corresponding to the GND node to prevent a singular matrix
     * 
     * @author 1sand0s
     * @param
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public Complex[][] constructMNAMatrix(Complex g[][],
					  Complex b[][],
					  Complex c[][],
					  Complex d[][]) {
        Complex x[][] = new Complex[g.length + c.length - 1][g.length + c.length - 1];
	for (int i = 1; i < g.length; i++) {
            System.arraycopy(g[i], 1, x[i - 1], 0, g.length - 1);
            System.arraycopy(b[i], 0, x[i - 1], g.length - 1, c.length);
        }
        for (int i = 0; i < c.length; i++) {
            System.arraycopy(c[i], 1, x[i + g.length - 1], 0, b.length - 1);
            System.arraycopy(d[i], 0, x[i + g.length - 1], b.length - 1, d.length);
        }
        return x;
    }

    /**
     * @brief Adds GND row back to solver result
     * 
     * @author 1sand0s
     * @param
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public Complex[][] addGNDToResult(Complex x[][]) {
	Complex x1[][] = new Complex[x.length + 1][numHarmonics];
	ComplexMatrixOperations.initializeMatrices(x1);
	
	for(int j = 0; j < x.length; j++){
	    for(int k = 0; k < x[j].length; k++){
		x1[j + 1][k] = x[j][k];
	    }
	}
        return x1;
    }

        /**
     * @brief Adds GND row back to solver result
     * 
     * @author 1sand0s
     * @param
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public Complex[][] removeGNDFromResult(Complex x[][]) {
	Complex x1[][] = new Complex[x.length - 1][numHarmonics];
	ComplexMatrixOperations.initializeMatrices(x1);
	
	for(int j = 0; j < (x.length - 1); j++){
	    for(int k = 0; k < x[j].length; k++){
		x1[j][k] = x[j + 1][k];
	    }
	}
        return x1;
    }

    /**
     * @brief Populates simulation time vector. Valid only
     *        for Transient simulation
     * 
     * @author 1sand0s
     * @param
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public void expandTime() {
    }

    /**
     * @brief Populates simulation time vector. Valid only
     *        for Transient simulation
     * 
     * @author 1sand0s
     * @param
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public void setTolerance(double tol) {
	this.tol = tol;
    }
}
