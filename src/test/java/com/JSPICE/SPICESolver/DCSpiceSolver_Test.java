/**
 * 
 */
package com.JSPICE.SPICESolver;

import com.JSPICE.SElement.DCVoltage;
import com.JSPICE.SElement.GND;
import com.JSPICE.SElement.Resistor;
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
        Complex x[][] = { { new Complex(0, 0) }, { new Complex(10, 0) }, { new Complex(5, 0) }, { new Complex(-0.05, 0) } };

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

	Complex result[] = new Complex[solver.getResult().length];
	for(int j = 0; j < result.length; j++){
	    result[j] = solver.getResult()[j][0];
	}

	/* Assert if solver result matches expected solution */
        assertTrue(ComplexMatrixOperations.compareMatrices(x, solver.getResult(), tol));
	assertEquals(5, r1.getVoltage(result).magnitude(), tol);
	assertEquals(5, r2.getVoltage(result).magnitude(), tol);
	assertEquals(0.05, r1.getCurrent(result, 0).magnitude(), tol);
	assertEquals(0.05, r2.getCurrent(result, 0).magnitude(), tol);
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
        Complex x[][] = { { new Complex(0, 0) }, { new Complex(10, 0) }, { new Complex(5, 0) }, { new Complex(5, 0) }, { new Complex(-0.2, 0) } };

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
	
	/* Assert if solver result matches expected solution */
        assertTrue(ComplexMatrixOperations.compareMatrices(x, solver.getResult(), tol));
    }
}
