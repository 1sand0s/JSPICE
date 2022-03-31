/**
 * 
 */
package com.JSPICE.SPICESolver;

import com.JSPICE.SElement.ACVoltage;
import com.JSPICE.SElement.GND;
import com.JSPICE.SElement.Resistor;
import com.JSPICE.SElement.Inductor;
import com.JSPICE.SElement.Capacitor;
import com.JSPICE.SElement.Wire;
import com.JSPICE.SMath.ComplexMatrixOperations;
import com.JSPICE.Util.ComponentTerminals;
import com.JSPICE.SMath.Complex;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * @author 1sand0s
 *
 */
public class ACSpiceSolver_Test {

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
    @Test
    public void testVoltageDivider_AC() {
	/* Tolerance for comparing solution */
	double tol = 1e-5;

	/* Frequency of AC analysis */
	double frequency = 1e9;

	/* Solution after DC analysis */
        Complex expected[][] = { { new Complex(0, 0) }, { new Complex(10, 0) }, { new Complex(5, 0) }, { new Complex(-0.05, 0) } };
	ACSpiceResult expectedResult = new ACSpiceResult();
	expectedResult.updateResult(expected);
	    
	/* Instantiate ACSpiceSolver */
	AbstractSpiceSolver solver = new ACSpiceSolver();

	/* Create an AC Source */
	ACVoltage source = new ACVoltage();

	/* Create resistors */
	Resistor r1 = new Resistor();
        Resistor r2 = new Resistor();

	/* Create circuit GND element */
	GND g1 = new GND();

	/* Create wires to connect circuit elements*/
        Wire w1 = new Wire();
        Wire w2 = new Wire();
        Wire w3 = new Wire();

	/* Set AC source voltage to 10V */
        source.setValue(10);

	/* Set Frequency to 1GHz */
	solver.setFrequency(frequency);

	/* Set r1 and r2 resistances to 100 Ohm each */
        r1.setValue(100);
        r2.setValue(100);

	/*                Circuit Topology
	 *
	 *
	 *        w1       r1 100         w2
	 *         ~-----^v^v^v^v^v---------~
	 *         |                        |
	 *         |                        |
	 *        ~~~                       >
	 *       ~ + ~ source               < r2
	 *       ~ - ~   10V                > 100
	 *        ~ ~                       <
	 *         |                        >
	 *         |                        |
         *         |          w3            |
	 *         ~------------------------~
	 *       -----  
	 *        --- g1
	 *         -
	 */

	/* Use wires to connect the circuit elements as shown above */
	w1.addTerminal(source, ComponentTerminals.POS_NODE);
        w1.addTerminal(r1, ComponentTerminals.POS_NODE);

        w2.addTerminal(r1, ComponentTerminals.NEG_NODE);
        w2.addTerminal(r2, ComponentTerminals.POS_NODE);

        w3.addTerminal(r2, ComponentTerminals.NEG_NODE);
        w3.addTerminal(source, ComponentTerminals.NEG_NODE);
        w3.addTerminal(g1, ComponentTerminals.GND);

	/* Add circuit elements to the solver */
        solver.addElement(source);
        solver.addElement(r1);
        solver.addElement(r2);
        solver.addElement(g1);
        solver.addWire(w1);
        solver.addWire(w2);
        solver.addWire(w3);

	/* Solve for unknow node voltages and branch currents*/
        solver.solve();

	ACSpiceResult actual = (ACSpiceResult) solver.getResult();

	/* Assert if solver result matches expected solution */
        assertTrue(actual.resultMatch(expectedResult, tol));
	assertEquals(expectedResult.getElementVoltage(0, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE).magnitude(),
		     actual.getElementVoltage(0, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE).magnitude(),
		     tol);
	assertEquals(expectedResult.getElementVoltage(0, r2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE).magnitude(),
		     actual.getElementVoltage(0, r2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE).magnitude(),
		     tol);
	//assertEquals(0.05, r1.getCurrent(actual, frequency)[0].magnitude(), tol);
	//assertEquals(0.05, r2.getCurrent(actual, frequency)[0].magnitude(), tol);
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
    @Test
    public void testVoltageDividerInductor_AC() {
	/* Tolerance for comparing solution */
	double tol = 1e-5;

	/* Frequency of AC analysis */
	double frequency = 1e9;

	/* Solution after DC analysis */
        Complex expected[][] = { { new Complex(0, 0) }, { new Complex(10, 0) }, { new Complex(5, 0) }, { new Complex(0, -0.7957747) } };
	ACSpiceResult expectedResult = new ACSpiceResult();
	expectedResult.updateResult(expected);
	
	/* Instantiate ACSpiceSolver */
	AbstractSpiceSolver solver = new ACSpiceSolver();

	/* Create an AC Source */
	ACVoltage source = new ACVoltage();

	/* Create inductors */
	Inductor l1 = new Inductor();
        Inductor l2 = new Inductor();

	/* Create circuit GND element */
	GND g1 = new GND();

	/* Create wires to connect circuit elements*/
        Wire w1 = new Wire();
        Wire w2 = new Wire();
        Wire w3 = new Wire();

	/* Set AC source voltage to 10V */
        source.setValue(10);

	/* Set Frequency to 1GHz */
	solver.setFrequency(frequency);

	/* Set l1 and l2 inductances to 1nH each */
        l1.setValue(1e-9);
        l2.setValue(1e-9);

	/*                Circuit Topology
	 *
	 *
	 *        w1       l1 1nH         w2
	 *         ~-----^v^v^v^v^v---------~
	 *         |                        |
	 *         |                        |
	 *        ~~~                       >
	 *       ~ + ~ source               < l2
	 *       ~ - ~   10V                > 1nH
	 *        ~ ~                       <
	 *         |                        >
	 *         |                        |
         *         |          w3            |
	 *         ~------------------------~
	 *       -----  
	 *        --- g1
	 *         -
	 */

	/* Use wires to connect the circuit elements as shown above */
	w1.addTerminal(source, ComponentTerminals.POS_NODE);
        w1.addTerminal(l1, ComponentTerminals.POS_NODE);

        w2.addTerminal(l1, ComponentTerminals.NEG_NODE);
        w2.addTerminal(l2, ComponentTerminals.POS_NODE);

        w3.addTerminal(l2, ComponentTerminals.NEG_NODE);
        w3.addTerminal(source, ComponentTerminals.NEG_NODE);
        w3.addTerminal(g1, ComponentTerminals.GND);

	/* Add circuit elements to the solver */
        solver.addElement(source);
        solver.addElement(l1);
        solver.addElement(l2);
        solver.addElement(g1);
        solver.addWire(w1);
        solver.addWire(w2);
        solver.addWire(w3);

	/* Solve for unknow node voltages and branch currents*/
        solver.solve();
	
	ACSpiceResult actual = (ACSpiceResult) solver.getResult();
	
	/* Assert if solver result matches expected solution */
	assertTrue(actual.resultMatch(expectedResult, tol));
	assertEquals(expectedResult.getElementVoltage(0, l1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE).magnitude(),
		     actual.getElementVoltage(0, l1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE).magnitude(),
		     tol);
	assertEquals(expectedResult.getElementVoltage(0, l2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE).magnitude(),
		     actual.getElementVoltage(0, l2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE).magnitude(),
		     tol);
	//assertEquals(0.7957747, l1.getCurrent(actual, frequency)[0].magnitude(), tol);
	//assertEquals(0.7957747, l2.getCurrent(actual, frequency)[0].magnitude(), tol);
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
    @Test
    public void testVoltageDividerCapacitor_AC() {
	/* Tolerance for comparing solution */
	double tol = 1e-5;

	/* Frequency of AC analysis */
	double frequency = 1e9;
	
	/* Solution after DC analysis */
        Complex expected[][] = { { new Complex(0, 0) }, { new Complex(10, 0) }, { new Complex(5, 0) }, { new Complex(0, 31.415926) } };
	ACSpiceResult expectedResult = new ACSpiceResult();
	expectedResult.updateResult(expected);

	/* Instantiate ACSpiceSolver */
	AbstractSpiceSolver solver = new ACSpiceSolver();

	/* Create an AC Source */
	ACVoltage source = new ACVoltage();

	/* Create capacitors */
	Capacitor c1 = new Capacitor();
        Capacitor c2 = new Capacitor();
	
	/* Create circuit GND element */
	GND g1 = new GND();

	/* Create wires to connect circuit elements*/
        Wire w1 = new Wire();
        Wire w2 = new Wire();
        Wire w3 = new Wire();

	/* Set AC source voltage to 10V */
        source.setValue(10);

	/* Set Frequency to 1GHz */
	solver.setFrequency(frequency);

	/* Set c1 and c2 capacitances to 1nF each */
        c1.setValue(1e-9);
        c2.setValue(1e-9);

	/*                Circuit Topology
	 *
	 *
	 *        w1       c1 1nF         w2
	 *         ~-----------||-----------~
	 *         |                        |
	 *         |                        |
	 *        ~~~                       |
	 *       ~ + ~ source             ---- c2
	 *       ~ - ~   10V              ---- 1nF
	 *        ~ ~                       |
	 *         |                        |
	 *         |                        |
         *         |          w3            |
	 *         ~------------------------~
	 *       -----  
	 *        --- g1
	 *         -
	 */

	/* Use wires to connect the circuit elements as shown above */
	w1.addTerminal(source, ComponentTerminals.POS_NODE);
        w1.addTerminal(c1, ComponentTerminals.POS_NODE);

        w2.addTerminal(c1, ComponentTerminals.NEG_NODE);
        w2.addTerminal(c2, ComponentTerminals.POS_NODE);

        w3.addTerminal(c2, ComponentTerminals.NEG_NODE);
        w3.addTerminal(source, ComponentTerminals.NEG_NODE);
        w3.addTerminal(g1, ComponentTerminals.GND);

	/* Add circuit elements to the solver */
        solver.addElement(source);
        solver.addElement(c1);
        solver.addElement(c2);
        solver.addElement(g1);
        solver.addWire(w1);
        solver.addWire(w2);
        solver.addWire(w3);

	/* Solve for unknow node voltages and branch currents*/
        solver.solve();

	ACSpiceResult actual = (ACSpiceResult) solver.getResult();

	/* Assert if solver result matches expected solution */
	assertTrue(actual.resultMatch(expectedResult, tol));
	assertEquals(expectedResult.getElementVoltage(0, c1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE).magnitude(),
		     actual.getElementVoltage(0, c1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE).magnitude(),
		     tol);
	assertEquals(expectedResult.getElementVoltage(0, c2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE).magnitude(),
		     actual.getElementVoltage(0, c2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE).magnitude(),
		     tol);
	//assertEquals(31.415926, c1.getCurrent(actual, frequency)[0].magnitude(), tol);
	//assertEquals(31.415926, c2.getCurrent(actual, frequency)[0].magnitude(), tol);
    }

    /**
     * @brief Test case for Wheatstone bridge topology
     * 
     * @author 1sand0s
     * @since 1.0.0
     * @version 1.0.0
     */
    @Test
    public void testWheatstoneBridge_DC() {
	/* Tolerance for comparing solution */
	double tol = 1e-5;

	/* Frequency of AC analysis */
	double frequency = 1e9;

	/* Solution after DC analysis */
        Complex expected[][] = { { new Complex(0, 0) }, { new Complex(10, 0) }, { new Complex(5, 0) }, { new Complex(5, 0) }, { new Complex(-0.2, 0) } };
	ACSpiceResult expectedResult = new ACSpiceResult();
	expectedResult.updateResult(expected);

	/* Instantiate DCSpiceSolver */
	AbstractSpiceSolver solver = new ACSpiceSolver();

	/* Create a DC Source*/
	ACVoltage source = new ACVoltage();

	/* Create resistors */
	Resistor r1 = new Resistor();
        Resistor r2 = new Resistor();
	Resistor r3 = new Resistor();
	Resistor r4 = new Resistor();
	Resistor r5 = new Resistor();
	
	/* Create circuit GND element */
	GND g1 = new GND();

	/* Create wires to connect circuit elements */
        Wire w1 = new Wire();
        Wire w2 = new Wire();
        Wire w3 = new Wire();
	Wire w4 = new Wire();

	/* Set DC source voltage to 10V */
        source.setValue(10);

	/* Set frequency of AC analysis */
	solver.setFrequency(frequency);

	/* Set r1 and r2 resistances to 100 Ohm each */
	r1.setValue(100);
        r2.setValue(100);
	r3.setValue(100);
	r4.setValue(100);
	r5.setValue(100);

	/*                  Circuit Topology
	 *
	 *                           - w2
	 *                          / \
	 *                         /   \
	 *                        R1    R2
         *                       /       \
	 *               w1     /         \
	 *         ~-----------/-----R5----\-~
	 *         |           \           / |
	 *         |            \         /  |
	 *        ~~~            \       /   |                                   
	 *       ~ + ~ source     R3    R4   |
	 *       ~ - ~   10V       \   /     |
	 *        ~ ~               \ /      |
	 *         |                 - w3    |
         *         |          w4             |
	 *         ~-------------------------~
	 *       -----  
	 *        --- g1
	 *         -
	 */

	/* Use wires to connect the circuit elements as shown above */
        w1.addTerminal(source, ComponentTerminals.POS_NODE);
        w1.addTerminal(r1, ComponentTerminals.POS_NODE);
	w1.addTerminal(r3, ComponentTerminals.POS_NODE);
	w1.addTerminal(r5, ComponentTerminals.POS_NODE);

        w2.addTerminal(r1, ComponentTerminals.NEG_NODE);
        w2.addTerminal(r2, ComponentTerminals.POS_NODE);

	w3.addTerminal(r3, ComponentTerminals.NEG_NODE);
        w3.addTerminal(r4, ComponentTerminals.POS_NODE);

        w4.addTerminal(r2, ComponentTerminals.NEG_NODE);
	w4.addTerminal(r4, ComponentTerminals.NEG_NODE);
	w4.addTerminal(r5, ComponentTerminals.NEG_NODE);
	w4.addTerminal(source, ComponentTerminals.NEG_NODE);
        w4.addTerminal(g1, ComponentTerminals.GND);

	/* Add circuit elements to the solver */
        solver.addElement(source);
        solver.addElement(r1);
        solver.addElement(r2);
	solver.addElement(r3);
	solver.addElement(r4);
	solver.addElement(r5);
	solver.addElement(g1);
        solver.addWire(w1);
        solver.addWire(w2);
        solver.addWire(w3);
	solver.addWire(w4);

	/* Solve for unknown node voltages and branch currents */
        solver.solve();

	ACSpiceResult actual = (ACSpiceResult) solver.getResult();
	
	/* Assert if solver result matches expected solution */
        assertTrue(actual.resultMatch(expectedResult, tol));
    }
}
