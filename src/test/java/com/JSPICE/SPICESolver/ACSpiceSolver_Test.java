/**
 * 
 */
package com.JSPICE.SPICESolver;

import com.JSPICE.SElement.ACVoltage;
import com.JSPICE.SElement.GND;
import com.JSPICE.SElement.Resistor;
import com.JSPICE.SElement.Wire;
import com.JSPICE.SMath.ComplexMatrixOperations;
import com.JSPICE.Util.ComponentTerminals;
import com.JSPICE.SMath.Complex;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

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

	/* Solution after DC analysis */
        Complex x[][] = { { new Complex(10, 0) }, { new Complex(5, 0) }, { new Complex(-0.05, 0) } };

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

	/* Assert if solver result matches expected solution */
        assertTrue(ComplexMatrixOperations.compareMatrices(x, solver.getResult(), tol));
    }
}
