package com.JSPICE.SPICESolver;

import com.JSPICE.SElement.*;
import com.JSPICE.SElement.Passives.*;
import com.JSPICE.SElement.VSource.*;
import com.JSPICE.SElement.ISource.*;
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
public class DCSpiceSolver_Test {

    /**
     * @brief Test case for a simple voltage divider
     * 
     * @author 1sand0s
     * @since 1.0.0
     * @version 1.0.0
     */
    @Test
    public void testVoltageDivider_DC() {
	/* Tolerance for comparing solution */
	double tol = 1e-5;

	/* Solution after DC analysis */
	Complex expected[][] = { { new Complex(0, 0) }, { new Complex(10, 0) }, { new Complex(5, 0) }, { new Complex(-0.05, 0) } };
	DCSpiceResult expectedResult = new DCSpiceResult();
	expectedResult.updateResult(expected);
	
	/* Instantiate DCSpiceSolver */
	AbstractSpiceSolver solver = new DCSpiceSolver();

	/* Create a DC Source*/
	DCVoltage source = new DCVoltage();

	/* Create resistors */
	Resistor r1 = new Resistor();
        Resistor r2 = new Resistor();

	/* Create circuit GND element */
	GND g1 = new GND();

	/* Create wires to connect circuit elements */
        Wire w1 = new Wire();
        Wire w2 = new Wire();
        Wire w3 = new Wire();

	/* Set DC source voltage to 10V */
        source.setValue(10);

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

	/* Solve for unknown node voltages and branch currents */
        solver.solve();

	DCSpiceResult actual = (DCSpiceResult) solver.getResult();

	/* Assert if solver result matches expected solution */
        assertTrue(actual.resultMatch(expectedResult, tol));
	assertEquals(expectedResult.getElementVoltage(r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(r2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(r2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	//assertEquals(0.05, r1.getCurrent(actual, 0)[0].magnitude(), tol);
	//assertEquals(0.05, r2.getCurrent(actual, 0)[0].magnitude(), tol);
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

	/* Solution after DC analysis */
        Complex expected[][] = { { new Complex(0, 0) }, { new Complex(10, 0) }, { new Complex(5, 0) }, { new Complex(5, 0) }, { new Complex(-0.2, 0) } };
	DCSpiceResult expectedResult = new DCSpiceResult();
	expectedResult.updateResult(expected);

	/* Instantiate DCSpiceSolver */
	AbstractSpiceSolver solver = new DCSpiceSolver();

	/* Create a DC Source*/
	DCVoltage source = new DCVoltage();

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

	DCSpiceResult actual = (DCSpiceResult) solver.getResult();
	
	/* Assert if solver result matches expected solution */
        assertTrue(actual.resultMatch(expectedResult, tol));
    }

    /**
     * @brief Test case for a simple Half Wave rectifier
     * 
     * @author 1sand0s
     * @since 1.0.0
     * @version 1.0.0
     */
    @Test
    public void testDiodeRectifier_DC() {
	/* Tolerance for comparing solution */
	double tol = 1e-5;
	
	/* Solution after DC analysis */
	Complex expected[][] = { { new Complex(0, 0) }, { new Complex(10, 0) }, { new Complex(9.999999, 0) }, { new Complex(-4.999999E-10, 0) } };
	DCSpiceResult expectedResult = new DCSpiceResult();
	expectedResult.updateResult(expected);
	
	/* Instantiate DCSpiceSolver */
	AbstractSpiceSolver solver = new DCSpiceSolver();

	/* Create a DC Source*/
	DCVoltage source = new DCVoltage();

	/* Create resistor and Didoe */
	Resistor r1 = new Resistor();
        Diode d1 = new Diode();

	/* Create circuit GND element */
	GND g1 = new GND();

	/* Create wires to connect circuit elements */
        Wire w1 = new Wire();
        Wire w2 = new Wire();
        Wire w3 = new Wire();

	/* Set DC source voltage to 10V */
        source.setValue(10);

	/* Set r1 resistance to 100 Ohm */
	r1.setValue(100);

	/* Diode parameters for 1N4007
	 * See : https://www.mouser.com/datasheet/2/149/1N4007-888322.pdf
	 */
	d1.setReverseSaturationCurrent(5E-10);
	d1.setCrystalFactor(1);
	d1.setThermalVoltage(2.6E-2);

	/*                Circuit Topology
	 *
	 *
	 *        w1       r1 100         w2
	 *         ~-----^v^v^v^v^v---------~
	 *         |                        |
	 *         |                        |
	 *        ~~~                       |                                   
	 *       ~ + ~ source               |   d1
	 *       ~ - ~   10V              ~~~~~ 
	 *        ~ ~                      ~ ~
	 *         |                       ~~~
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
        w2.addTerminal(d1, ComponentTerminals.CATHODE);

        w3.addTerminal(d1, ComponentTerminals.ANODE);
        w3.addTerminal(source, ComponentTerminals.NEG_NODE);
        w3.addTerminal(g1, ComponentTerminals.GND);

	/* Add circuit elements to the solver */
        solver.addElement(source);
        solver.addElement(r1);
        solver.addElement(d1);
        solver.addElement(g1);
        solver.addWire(w1);
        solver.addWire(w2);
        solver.addWire(w3);

	/* Solve for unknown node voltages and branch currents */
        solver.solve();

	DCSpiceResult actual = (DCSpiceResult) solver.getResult();

	/* Assert if solver result matches expected solution */
        assertTrue(actual.resultMatch(expectedResult, tol));
	assertEquals(expectedResult.getElementVoltage(r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	//assertEquals(0.05, r1.getCurrent(actual, 0)[0].magnitude(), tol);
	//assertEquals(0.05, r2.getCurrent(actual, 0)[0].magnitude(), tol);
    }

    /**
     * @brief Test case for circuit with VCVS
     * 
     * @author 1sand0s
     * @since 1.0.0
     * @version 1.0.0
     */
    @Test
    public void testVCVS1_DC() {
	/* Tolerance for comparing solution */
	double tol = 1e-5;
	
	/* Solution after DC analysis */
	Complex expected[][] = { { new Complex(0, 0) }, { new Complex(9.9999999, 0) }, { new Complex(9.090909, 0) }, { new Complex(-0.0090909, 0) }, { new Complex(-0.0818181, 0) } };
	DCSpiceResult expectedResult = new DCSpiceResult();
	expectedResult.updateResult(expected);
	
	/* Instantiate DCSpiceSolver */
	AbstractSpiceSolver solver = new DCSpiceSolver();

	/* Create a DC Source*/
	DCVoltage source = new DCVoltage();

	/* Create a DC VCVS */
	DCVCVS vcvs = new DCVCVS();

	/* Create resistors */
	Resistor r1 = new Resistor();
        Resistor r2 = new Resistor();

	/* Create circuit GND element */
	GND g1 = new GND();

	/* Create wires to connect circuit elements */
        Wire w1 = new Wire();
        Wire w2 = new Wire();
        Wire w3 = new Wire();

	/* Set DC source voltage to 10V */
        source.setValue(10);

	/* Set DC VCVS gain t0 10 */
	vcvs.setGain(10);
	
	/* Set r1 and r2 resistances to 100 Ohm each */
	r1.setValue(100);
        r2.setValue(100);

	/*                Circuit Topology
	 *
	 *
	 *        w1       r1 100         w2
	 *         ~-----^v^v^v^v^v---------~-------------------~
	 *         |                        |                   |
	 *         |                        |                   |
	 *        ~~~                       >                   |                  
	 *       ~ + ~ source               < r2               /+\  
	 *       ~ - ~   10V                > 100             /   \10xV(r1)
	 *        ~ ~                       <                 \   /
	 *         |                        >                  \-/
	 *         |                        |                   |
         *         |          w3            |                   |
	 *         ~------------------------~-------------------~
	 *       -----  
	 *        --- g1
	 *         -
	 */

	/* Use wires to connect the circuit elements as shown above */
        w1.addTerminal(source, ComponentTerminals.POS_NODE);
        w1.addTerminal(r1, ComponentTerminals.POS_NODE);

        w2.addTerminal(r1, ComponentTerminals.NEG_NODE);
        w2.addTerminal(r2, ComponentTerminals.POS_NODE);
	w2.addTerminal(vcvs, ComponentTerminals.POS_NODE);

        w3.addTerminal(r2, ComponentTerminals.NEG_NODE);
        w3.addTerminal(source, ComponentTerminals.NEG_NODE);
        w3.addTerminal(g1, ComponentTerminals.GND);
	w3.addTerminal(vcvs, ComponentTerminals.NEG_NODE);

	/* Add reference nodes for dependent voltage source*/
	vcvs.setPositiveReference(w1);
	vcvs.setNegativeReference(w2);

	/* Add circuit elements to the solver */
        solver.addElement(source);
	solver.addElement(vcvs);
        solver.addElement(r1);
        solver.addElement(r2);
        solver.addElement(g1);
        solver.addWire(w1);
        solver.addWire(w2);
        solver.addWire(w3);

	/* Solve for unknown node voltages and branch currents */
        solver.solve();

	DCSpiceResult actual = (DCSpiceResult) solver.getResult();

	/* Assert if solver result matches expected solution */
        assertTrue(actual.resultMatch(expectedResult, tol));
	assertEquals(expectedResult.getElementVoltage(r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(r2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(r2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	//assertEquals(0.05, r1.getCurrent(actual, 0)[0].magnitude(), tol);
	//assertEquals(0.05, r2.getCurrent(actual, 0)[0].magnitude(), tol);
    }

    /**
     * @brief Test case for a simple current divider
     * 
     * @author 1sand0s
     * @since 1.0.0
     * @version 1.0.0
     */
    @Test
    public void testCurrentDivider_DC() {
	/* Tolerance for comparing solution */
	double tol = 1e-5;

	/* Solution after DC analysis */
	Complex expected[][] = { { new Complex(0, 0) }, { new Complex(500, 0) }, { new Complex(-10, 0) } };
	DCSpiceResult expectedResult = new DCSpiceResult();
	expectedResult.updateResult(expected);
	
	/* Instantiate DCSpiceSolver */
	AbstractSpiceSolver solver = new DCSpiceSolver();

	/* Create a DC Source*/
	DCCurrent source = new DCCurrent();

	/* Create resistors */
	Resistor r1 = new Resistor();
        Resistor r2 = new Resistor();

	/* Create circuit GND element */
	GND g1 = new GND();

	/* Create wires to connect circuit elements */
        Wire w1 = new Wire();
        Wire w2 = new Wire();

	/* Set DC source current to 10A */
        source.setValue(10);

	/* Set r1 and r2 resistances to 100 Ohm each */
	r1.setValue(100);
        r2.setValue(100);

	/*                Circuit Topology
	 *
	 *
	 *                       w1                
	 *         ~------------------------~-----------~       
	 *         |                        |           |
	 *         |                        |           |
	 *        ~~~                       >           >                        
	 *       ~ ^ ~ source               < r1        < r2
	 *       ~ | ~   10A                > 100       > 100
	 *        ~ ~                       <           <
	 *         |                        >           >
	 *         |                        |           |
         *         |          w2            |           | 
	 *         ~------------------------~-----------~
	 *       -----  
	 *        --- g1
	 *         -
	 */

	/* Use wires to connect the circuit elements as shown above */
        w1.addTerminal(source, ComponentTerminals.POS_NODE);
        w1.addTerminal(r1, ComponentTerminals.POS_NODE);
        w1.addTerminal(r2, ComponentTerminals.POS_NODE);

	w2.addTerminal(source, ComponentTerminals.NEG_NODE);
        w2.addTerminal(r1, ComponentTerminals.NEG_NODE);
        w2.addTerminal(r2, ComponentTerminals.NEG_NODE);
        w2.addTerminal(g1, ComponentTerminals.GND);

	/* Add circuit elements to the solver */
        solver.addElement(source);
        solver.addElement(r1);
        solver.addElement(r2);
        solver.addElement(g1);
        solver.addWire(w1);
        solver.addWire(w2);

	/* Solve for unknown node voltages and branch currents */
        solver.solve();

	DCSpiceResult actual = (DCSpiceResult) solver.getResult();

	/* Assert if solver result matches expected solution */
        assertTrue(actual.resultMatch(expectedResult, tol));
	assertEquals(expectedResult.getElementVoltage(r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(r2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(r2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	//assertEquals(0.05, r1.getCurrent(actual, 0)[0].magnitude(), tol);
	//assertEquals(0.05, r2.getCurrent(actual, 0)[0].magnitude(), tol);
    }
 }
