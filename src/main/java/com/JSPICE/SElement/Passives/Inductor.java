package com.JSPICE.SElement.Passives;

import com.JSPICE.SMath.Complex;
import com.JSPICE.SMath.ComplexMatrixOperations;
import com.JSPICE.Util.ComponentDenominations;
import com.JSPICE.Util.ComponentTerminals;
import com.JSPICE.SElement.SElement;
import com.JSPICE.SElement.Wire;
import com.JSPICE.SElement.Terminals;

/**
 * @author 1sand0s
 *
 */
public class Inductor extends SElement {

    private double inductance;
    private double gMinResistance;

    public Inductor() {
        denomination = ComponentDenominations.L;
        inductance = 0;
	gMinResistance = 1e-3;
        terminals = new Terminals(2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE);
    }

    @Override
    public double getValue() {
        return inductance;
    }

    @Override
    public void setValue(double value) {
        inductance = value;
    }
    
    @Override
    public Complex[] getVoltage(Complex[][] result) {
	int posNode = terminals.getTerminal(ComponentTerminals.POS_NODE);
        int negNode = terminals.getTerminal(ComponentTerminals.NEG_NODE);

	return (ComplexMatrixOperations.SubArrays(result[posNode],
						  result[negNode]));
    }
    
    @Override
    public Complex[] getCurrent(Complex[][] result,
				double frequency) {
	return ComplexMatrixOperations.Multiply(ComplexMatrixOperations.ScalarMultiply(getVoltage(result),
										       1 / (2 * Math.PI * frequency * inductance)),
						new Complex(0, -1));
    }
    
    @Override
    public void stampMatrixDC(Complex[][] G,
                              Complex[][] B,
                              Complex[][] C,
                              Complex[][] D,
                              Complex[][] z,
			      Complex[][] result,
                              int iSourceIndex) {
        int posNode = terminals.getTerminal(ComponentTerminals.POS_NODE);
        int negNode = terminals.getTerminal(ComponentTerminals.NEG_NODE);

        /* Stamp low impedance during DC (prevents singular matrices) */
        G[posNode][posNode].add(new Complex(1 / gMinResistance, 0));
        G[negNode][negNode].add(new Complex(1 / gMinResistance, 0));
        G[posNode][negNode].add(new Complex(-1 / gMinResistance, 0));
        G[negNode][posNode].add(new Complex(-1 / gMinResistance, 0));
    }

    @Override
    public void stampMatrixAC(Complex[][] G,
                              Complex[][] B,
                              Complex[][] C,
                              Complex[][] D,
                              Complex[][] z,
			      Complex[][] result,
                              int iSourceIndex,
                              double frequency) {
        int posNode = terminals.getTerminal(ComponentTerminals.POS_NODE);
        int negNode = terminals.getTerminal(ComponentTerminals.NEG_NODE);

        /* Stamp inductive reactance during AC */
        G[posNode][posNode].add(new Complex(0, 1 / (inductance * frequency * 2 * Math.PI)));
        G[negNode][negNode].add(new Complex(0, 1 / (inductance * frequency * 2 * Math.PI)));
        G[posNode][negNode].add(new Complex(0, -1 / (inductance * frequency * 2 * Math.PI)));
        G[negNode][posNode].add(new Complex(0, -1 / (inductance * frequency * 2 * Math.PI)));
    }
    
    @Override
    public void stampMatrixTransient(Complex[][] G,
				     Complex[][] B,
				     Complex[][] C,
				     Complex[][] D,
				     Complex[][] z,
				     Complex[][] result,
				     int iSourceIndex,
				     double time,
				     double deltaT) {
	int posNode = terminals.getTerminal(ComponentTerminals.POS_NODE);
	int negNode = terminals.getTerminal(ComponentTerminals.NEG_NODE);
	
        /* Inductor transient equation
	 *
	 *  v = L * di/dt
	 *  
	 * If 'dt' is small then variation of 'i(t)' about t in the range (t - dt, t) 
	 * can be assumed to be linear, therefore
	 *
	 *  v = L * (i(t) - i(t - dt))/(dt)
	 *  (i(t) - i(t - dt))/v = dt/L
	 *
	 *  Therefore, for impedance we stamp 'L/dt'
	 */
        G[posNode][posNode].add(new Complex(-(deltaT / inductance), 0));
        G[negNode][negNode].add(new Complex(-(deltaT / inductance), 0));
        G[posNode][negNode].add(new Complex(+(deltaT / inductance), 0));
        G[negNode][posNode].add(new Complex(+(deltaT / inductance), 0));
        
        /* Inductors stamped as current sources
	 *
	 * is = -i(t - dt) = -v(t - dt) * dt / L
	 */
	double voltage = getVoltage(result)[0].getReal(); // v(t - dt)
        z[posNode][0].add(new Complex(-voltage * deltaT / inductance, 0));
	z[negNode][0].add(new Complex(+voltage * deltaT / inductance, 0));
    }
}
