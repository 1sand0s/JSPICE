/**
 * 
 */
package com.JSPICE.SPICESolver;

import com.JSPICE.SElement.SElement;
import com.JSPICE.SMath.Complex;
import com.JSPICE.SMath.ComplexMatrixOperations;
import com.JSPICE.SElement.Wire;
import com.JSPICE.SElement.VSource.VSource;
import com.JSPICE.SElement.ISource.ISource;

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
     * @brief Adds a circuit element to the circuit
     * 
     * @author 1sand0s
     * @param element The element to be added to the circuit
     * @return void
     * @since 1.0.0
     * @version 1.0.0
     * @exception
     */
    public void addElement(SElement element) {
        circuitElements.add(element);
        if (element instanceof VSource)
            iVSource++;
	else if (element instanceof ISource)
            iISource++;
    }

    /**
     * @brief Adds a list of circuit elements to the circuit
     * 
     * @author 1sand0s
     * @param elements The list of elements to be added to the circuit
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public void addElements(ArrayList<SElement> elements) {
	for(int j = 0; j < elements.size(); j++){
	    this.addElement(elements.get(j));   
	}
    }

    /**
     * @brief Removes an indexed circuit element from the circuit 
     * 
     * @author 1sand0s
     * @param index The index of the element to be removed
     * @since 1.0.0
     * @version 1.0.0
     * @exception
     */
    public void removeElement(int index) {
        for (int j = 0; j < circuitElements.size(); j++) {
            if (circuitElements.get(j).getId_Int() == index) {
                if (circuitElements.get(j) instanceof VSource)
                    iVSource--;
		else if (circuitElements.get(j) instanceof ISource)
		    iISource--;
                circuitElements.remove(j);
                break;
            }
	}
    }

    /**
     * @brief Adds a wire to the circuit 
     * 
     * @author 1sand0s
     * @param wire Wire to be added to the circuit
     * @since 1.0.0
     * @version 1.0.0
     * @exception
     */
    public void addWire(Wire wire) {
        wires.add(wire);
    }

    /**
     * @brief Adds a list of wires to the circuit
     * 
     * @author 1sand0s
     * @param wires List of wires to be added to the circuit
     * @since 1.0.0
     * @version 1.0.0
     * @exception
     */
    public void addWires(ArrayList<Wire> wire) {
        for(int j = 0; j < wire.size(); j++) {
	    this.addWire(wire.get(j));
	}
    }

    /**
     * @brief Solves the matrix equation to compute node voltages and branch currents.
     *        Must be overriden in deriving class
     * 
     * @author 1sand0s
     * @since 1.0.0
     * @version 1.0.0
     * @exception
     */
    public abstract void solve();

    /**
     * @brief Solves the matrix equation to compute node voltages and branch currents,
     *        given a list of circuit elements and connecting wires. Must be overriden 
     *        in deriving class
     * 
     * @author 1sand0s
     * @param circuitElements List of elements in the circuit
     * @param wires List of wires connecting the circuit elements
     * @since 1.0.0
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
     * @exception UnsupportedOperationException
     */
    public void solve(ArrayList<SElement> circuitElements,
		      ArrayList<Wire> wires,
		      double t,
		      double deltaT) throws UnsupportedOperationException {
	throw new UnsupportedOperationException("Error : Cannot call solve on an instance of " + this.getClass().toString());
    }

    /**
     * @breif Returns the result after solve
     * 
     * @author 1sand0
     * @return The result returned depends on the type of analysis and can be [DC/AC/Transient]SpiceResult instance
     * @since 1.0.0
     * @version 1.0.0
     */
    public AbstractSpiceResult getResult() {
        return result;
    }

    /**
     * @brief Sets the frequency for the AC analysis
     * 
     * @author 1sand0s
     * @param frequency Frequency to solve for during AC analysis
     * @since 1.0.0
     * @version 1.0.0
     * @exception UnsupportedOperationException
     */
    public void setFrequency(double frequency) {
	throw new UnsupportedOperationException("Error: Must be invoked for ACSpiceSolver");
    }
    
    /**
     * @brief Sets the time step for transient solver (may not be respected if
     *        convergence cannot be achieved)
     * 
     * @author 1sand0s
     * @param tMin Minimum of time range for transient solver
     * @param tMax Maximum of time range for transient solver
     * @param numPoints Number of points to solve for in the interval [tMin, tMax]
     * @param type Time step in [tMin, tMax] can either be LINEAR, LOGARITHMIC or PWL
     * @since 1.0.0
     * @version 1.0.0
     * @exception UnsupportedOperationException
     */
    public void setTimeStep(double tmin,
			    double tmax,
			    int numPoints,
			    TimeStepType type) {
	throw new UnsupportedOperationException("Error: Must be invoked for TransientSpiceSolver");
    }
    
    /**
     * @brief Sets the time step for transient solver (may not be respected if
     *        convergence cannot be achieved)
     * 
     * @author 1sand0s
     * @param tMin Minimum of time range for transient solver
     * @param tMax Maximum of time range for transient solver
     * @param tStep Time step in the interval [tMin, tMax]
     * @since 1.0.0
     * @version 1.0.0
     * @exception UnsupportedOperationException
     */    
    public void setTimeStep(double tMin,
			    double tMax,
			    double tStep) {
	throw new UnsupportedOperationException("Error: Must be invoked for TransientSpiceSolver");
    }

    /**
     * @brief Populates simulation time vector. Valid only
     *        for Transient simulation
     * 
     * @author 1sand0s
     * @since 1.0.0
     * @version 1.0.0
     * @exception UnsupportedOperationException
     */
    public void expandTime(){
	throw new UnsupportedOperationException("Error: Must be invoked for TransientSpiceSolver");
    }

    /**
     * @brief Assigns each circuit node an Id/number
     * 
     * @author 1sand0
     * @since 1.0.0
     * @version 1.0.0
     */
    public void numberNodes() {
        int index = 1;
        int gndIndex = -1;
        for (int j = 0; j < wires.size(); j++) {
            if (!wires.get(j).isGND()) {
                for (int i = 0; i < wires.get(j).getNumElements(); i++){
                    wires.get(j).getElementAtIndex(i).setTerminalIndex(index, wires.get(j).getTerminalAtIndex(i));
		    wires.get(j).setNodeIndex(index);
		}
		
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
     * @brief Removes GND row back to solver result
     * 
     * @author 1sand0s
     * @param x MNA matrix with GND/referenced terminal column and row
     * @return x1 MNA matrix with GND/referenced terminal column and row removed
     * @since 1.0.0
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
     * @brief Sets the tolerance for NR convergence
     * 
     * @author 1sand0s
     * @param tol tolerance for convergence
     * @since 1.0.0
     * @version 1.0.0
     */
    public void setTolerance(double tol) {
	this.tol = tol;
    }
}
