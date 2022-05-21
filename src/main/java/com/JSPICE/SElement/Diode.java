package com.JSPICE.SElement;

import com.JSPICE.Util.ComponentTerminals;
import com.JSPICE.SMath.*;
import com.JSPICE.Util.ComponentDenominations;

/**
 * @author 1sand0s
 *
 */
public class Diode extends SElement {

    private double iSat;
    private double cFactor;
    private double tVoltage;

    public Diode() {
        denomination = ComponentDenominations.D;
        iSat = 0;
	cFactor = 0;
	tVoltage = 0;
        terminals = new Terminals(2,
                new ComponentTerminals[] { ComponentTerminals.ANODE, ComponentTerminals.CATHODE });
    }

    /**
     * @brief Sets the reverse saturation current of the diode model
     *        (see Diode Shockley Equation)
     * 
     * @author 1sand0s
     * @param double : iSat The reverse saturation current of the diode
     *                      in Amperes
     * @since 1.0.0
     * @version 1.0.0
     */
    public void setReverseSaturationCurrent(double iSat){
	this.iSat = iSat;
    }

    /**
     * @brief Sets the crytal factor of the diode model
     *        (see Diode Shockley Equation)
     * 
     * @author 1sand0s
     * @param double : cFactor The crystal factor of the diode
     * @since 1.0.0
     * @version 1.0.0
     */
    public void setCrystalFactor(double cFactor){
	this.cFactor = cFactor;
    }

    /**
     * @brief Sets the thermal voltage of the diode model
     *        (see Diode Shockley Equation)
     * 
     * @author 1sand0s
     * @param double : tVoltage The thermal voltage of the diode
     * @since 1.0.0
     * @version 1.0.0
     */
    public void setThermalVoltage(double tVoltage){
	this.tVoltage = tVoltage;
    }

    @Override
    public Complex[] getVoltage(Complex[][] result){
        int anode = terminals.getTerminal(ComponentTerminals.ANODE);
        int cathode = terminals.getTerminal(ComponentTerminals.CATHODE);

        return (ComplexMatrixOperations.SubArrays(result[anode],
						  result[cathode]));
    }

    /**
     * @brief Evaluates the Shockley equation given the diode drop
     *        to compute the current through the diode
     * 
     * @author 1sand0s
     * @param double : vd The voltage drop across the diode
     * @since 1.0.0
     * @version 1.0.0
     */
    public double evaluateShockelyEquation(double vd){
	return iSat * (Math.exp(vd / (cFactor * tVoltage)) - 1);
    }

    @Override
    public Complex[] getCurrent(Complex[][] result,
				double frequency) {
	return null;
    }
    
    @Override
    public void stampMatrixDC(Complex[][] G,
                              Complex[][] B,
                              Complex[][] C,
                              Complex[][] D,
                              Complex[][] z,
			      Complex[][] result,
                              int iSourceIndex) {
        int anode = terminals.getTerminal(ComponentTerminals.ANODE);
        int cathode = terminals.getTerminal(ComponentTerminals.CATHODE);

	/*
	 *      id
	 *       ^
	 *       |          Shockley Equation  -> id(vd) = Isat * (exp(vd / (n * Vt)) - 1)
	 *       |           ~
	 *       |           ~
	 *       |           ~      
	 *       |           ~         `1st order Taylor expansion about vd* -> id(vd) = id(vd*) + id'(vd*) * (vd - vd*)
	 *       |           ~       `
	 *       |           ~     `
	 *       |           ~   `      
	 *       |          ~  `    
	 *    id*|---------~ `
	 *       |~ ~ ~ ~  `|
	 *      0--------`------------------> vd
	 *       |     `    vd*
	 *       |   `
	 *       | `
	 *       `
	 *       | 
	 *       |
	 *       |
	 *       |
	 *       v
	 *
	 *
	 * - To find the impedances and current sources to be stamped in, we first linearize the diode
	 *   Shockely equation about the updated vd point for each iteration of Newton-Raphson. 
	 *  
	 * - The updated vd for an example iteration is given above as vd*
	 *
	 *   Therefore,
	 *
	 *   The conductance stamped in is the slope of the 1st order Taylor expansion
	 *
	 *   id'(vd*) = (Isat * exp(vd* / (n * Vt))) / (n * Vt) = 1/R
	 *
	 *   
	 *   The current source stamped in is the y-intercept of the 1st order Taylor expansion
	 *
	 *   id(0) = id(vd*) + id'(vd*) * (-vd*) 
	 *   id(0) = id(vd*) - vd* / R
	 *    
	 */

	double voltage = getVoltage(result)[0].getReal();
	voltage = voltage > 0.8 ? 0.8 : voltage;
	double id = evaluateShockelyEquation(voltage);
	double R = (cFactor * tVoltage) / (id + iSat);
	double id0 = id - voltage / R;

        G[anode][anode].add(new Complex(1 / R, 0));
        G[cathode][cathode].add(new Complex(1 / R, 0));
        G[anode][cathode].add(new Complex(-1 / R, 0));
        G[cathode][anode].add(new Complex(-1 / R, 0));

       	z[anode][0].add(new Complex(-id0, 0));
	z[cathode][0].add(new Complex(id0, 0));
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
        stampMatrixDC(G, B, C, D, z, result, iSourceIndex);
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
	stampMatrixDC(G, B, C, D, z, result, iSourceIndex);
    }
}
