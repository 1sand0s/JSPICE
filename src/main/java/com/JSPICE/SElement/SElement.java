/**
 * 
 */
package com.JSPICE.SElement;

import com.JSPICE.Util.ComponentTerminals;
import com.JSPICE.Util.UnitConversion;
import com.JSPICE.Util.ComponentDenominations;
import com.JSPICE.SMath.Complex;

/**
 * @author audi
 *
 */

public abstract class SElement {

    protected ComponentDenominations denomination;
    protected int id;
    protected Terminals terminals;

    /**
     * @brief Returns the Id of this SElement as a String
     * 
     * @author 1sand0s
     * @return String : Id with appropriate denomination (R,C,L etc)
     * @since 1.0.0
     * @version 1.0.0
     */
    public String getId() {
        return (UnitConversion.convertComponentDenominationToString(denomination) + id);
    }

    /**
     * @brief Gets the value of the SElement
     * 
     * @author 1sand0s
     * @return double : Value of the component (Resistance, Capacitance etc)
     * @since 1.0.0
     * @version 1.0.0
     * @exception throws UnsupportedOperationException Some SElements such as 
     *            Diodes, Transistor etc do not have any particular value
     *            associated with them therefore throw this exception
     */
    public double getValue() throws UnsupportedOperationException {
	throw new UnsupportedOperationException("Error : getValue() not implemented for instance of " + this.getClass().toString());
    }

    /**
     * @brief Sets the value of the SElement
     * 
     * @author 1sand0s
     * @param double : value of the component (Resistance, Capacitance etc)
     * @since 1.0.0
     * @version 1.0.0
     * @exception throws UnsupportedOperationException Some SElements such as 
     *            Diodes, Transistor etc do not have any particular value
     *            associated with them therefore throw this exception
     */
    public void setValue(double value) throws UnsupportedOperationException {
	throw new UnsupportedOperationException("Error : setValue() not implemented for instance of " + this.getClass().toString());
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @return Int : Id of the component as an integer
     * @since 1.0.0
     * @version 1.0.0
     */
    public int getId_Int() {
        return id;
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @return Int[] : Terminal indices (global)
     * @since 1.0.0
     * @version 1.0.0
     */
    public int[] getTerminalIndices() {
        return terminals.getTerminals();
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @return Int[] : Terminal indices (global)
     * @since 1.0.0
     * @version 1.0.0
     */
    public int getTerminalIndex(ComponentTerminals terminal) {
        return terminals.getTerminal(terminal);
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @return ComponentTerminals[] : Terminal names
     * @since 1.0.0
     * @version 1.0.0
     */
    public ComponentTerminals[] getTerminalNames() {
        return terminals.getTerminalNames();
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @return Int[] : Terminal indices (global)
     * @since 1.0.0
     * @version 1.0.0
     */
    public void setTerminalIndex(int index,
                                 ComponentTerminals terminal) {
        terminals.setTerminal(index, terminal);
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param result : double array of results after solve
     * @return Complex : get voltage
     * @since 1.0.0
     * @version 1.0.0
     */
    public abstract Complex[] getVoltage(Complex[][] result);
    
    /**
     * regular
     * 
     * @author 1sand0s
     * @param result : double array of results after solve
     * @param frequency : frequency 
     * @return Complex : get current
     * @since 1.0.0
     * @version 1.0.0
     */
    public abstract Complex[] getCurrent(Complex[][] result,
					 double frequency);

    /**
     * @brief Stamps the complex admittances of the concerned
     *        SElement. The value of independent sources are also
     *        stamped here. (Must be implemented by derived classes)
     * 
     * @author 1sand0s
     * @param Complex[][] : G       The admittance matrix (determined by the 
     *                              interconnections between the passive circuit 
     *                              elements and linearized equivalent passive 
     *                              models of active circuit elements)
     * @param Complex[][] : B       Source mapping matrix (determined by the 
     *                              interconnections of the voltage sources)
     * @param Complex[][] : C       Source mapping matrix (determined by the 
     *                              interconnections of the voltage sources)
     * @param Complex[][] : D       Source mapping matrix (determined by the 
     *                              interconnections of the voltage sources)
     * @param Complex[][] : z       Contains the values of the independent 
     *                              voltage and current sources
     * @param Complex[][] : result  The result of the previous Newton-Raphson
     *                              iteration
     * @param int : iSourceIndex    Number of voltage sources in the circuit
     * @param double : time         Used in transient simulation
     * @param double : deltaT       Time step between two transient solves
     * @since 1.0.0
     * @version 1.0.0
     * @exception
     */
    public abstract void stampMatrixDC(Complex[][] G,
                                       Complex[][] B,
                                       Complex[][] C,
                                       Complex[][] D,
                                       Complex[][] z,
				       Complex[][] result,
                                       int iSourceIndex);

    /**
     * @brief Stamps the complex admittances of the concerned
     *        SElement. The value of independent sources are also
     *        stamped here. (Must be implemented by derived classes)
     * 
     * @author 1sand0s
     * @param Complex[][] : G       The admittance matrix (determined by the 
     *                              interconnections between the passive circuit 
     *                              elements and linearized equivalent passive 
     *                              models of active circuit elements)
     * @param Complex[][] : B       Source mapping matrix (determined by the 
     *                              interconnections of the voltage sources)
     * @param Complex[][] : C       Source mapping matrix (determined by the 
     *                              interconnections of the voltage sources)
     * @param Complex[][] : D       Source mapping matrix (determined by the 
     *                              interconnections of the voltage sources)
     * @param Complex[][] : z       Contains the values of the independent 
     *                              voltage and current sources
     * @param Complex[][] : result  The result of the previous Newton-Raphson
     *                              iteration
     * @param int : iSourceIndex    Number of voltage sources in the circuit
     * @param double : time         Used in transient simulation
     * @param double : deltaT       Time step between two transient solves
     * @since 1.0.0
     * @version 1.0.0
     * @exception
     */
    public abstract void stampMatrixAC(Complex[][] G,
                                       Complex[][] B,
                                       Complex[][] C,
                                       Complex[][] D,
                                       Complex[][] z,
				       Complex[][] result,
                                       int iSourceIndex,
                                       double frequency);
    /**
     * @brief Stamps the complex admittances of the concerned
     *        SElement. The value of independent sources are also
     *        stamped here. (Must be implemented by derived classes)
     * 
     * @author 1sand0s
     * @param Complex[][] : G       The admittance matrix (determined by the 
     *                              interconnections between the passive circuit 
     *                              elements and linearized equivalent passive 
     *                              models of active circuit elements)
     * @param Complex[][] : B       Source mapping matrix (determined by the 
     *                              interconnections of the voltage sources)
     * @param Complex[][] : C       Source mapping matrix (determined by the 
     *                              interconnections of the voltage sources)
     * @param Complex[][] : D       Source mapping matrix (determined by the 
     *                              interconnections of the voltage sources)
     * @param Complex[][] : z       Contains the values of the independent 
     *                              voltage and current sources
     * @param Complex[][] : result  The result of the previous Newton-Raphson
     *                              iteration
     * @param int : iSourceIndex    Number of voltage sources in the circuit
     * @param double : time         Used in transient simulation
     * @param double : deltaT       Time step between two transient solves
     * @since 1.0.0
     * @version 1.0.0
     * @exception
     */
    public abstract void stampMatrixTransient(Complex[][] G,
					      Complex[][] B,
					      Complex[][] C,
					      Complex[][] D,
					      Complex[][] z,
					      Complex[][] result,
					      int iSourceIndex,
					      double time,
					      double deltaT);
}
