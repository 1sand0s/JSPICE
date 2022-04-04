/**
 * 
 */
package com.JSPICE.SPICESolver;

import com.JSPICE.SElement.DCVoltage;
import com.JSPICE.SElement.SinusoidVoltage;
import com.JSPICE.SElement.GND;
import com.JSPICE.SElement.Resistor;
import com.JSPICE.SElement.Diode;
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
public class TransientSpiceSolver_Test {

    /**
     * @brief Test case for a simple voltage divider
     * 
     * @author 1sand0s
     * @since 1.0.0
     * @version 1.0.0
     */
    @Test
    public void testVoltageDivider_Transient() {
	/* Tolerance for comparing solution */
	double tol = 1e-5;

	/* Solution after Transient analysis */
        Complex expected[][] = { { new Complex(0, 0) }, { new Complex(10, 0) }, { new Complex(5, 0) }, { new Complex(-0.05, 0) } };
	TransientSpiceResult expectedResult = new TransientSpiceResult();
	expectedResult.updateResult(expected);
	
	/* Instantiate TransientSpiceSolver */
	AbstractSpiceSolver solver = new TransientSpiceSolver();

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

	/* Set SImulation time settings */
	double tMin = 0.0;
	double tMax = 1.0;
	int numPoints = 10;
	AbstractSpiceSolver.TimeStepType timeStepType = AbstractSpiceSolver.TimeStepType.LINEAR;

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

	/* Add simulation time settings to solver */
	solver.setTimeStep(tMin, tMax, numPoints, timeStepType);

	/* Solve for unknown node voltages and branch currents */
        solver.solve();

	TransientSpiceResult actual = (TransientSpiceResult) solver.getResult();

	/* Assert if solver result matches expected solution */
	assertTrue(actual.resultMatch(0, expectedResult, tol));
	assertEquals(expectedResult.getElementVoltage(0, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(0, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(0, r2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(0, r2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
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
    public void testWheatstoneBridge_Transient() {
	/* Tolerance for comparing solution */
	double tol = 1e-5;

	/* Solution after DC analysis */
        Complex expected[][] = { { new Complex(0, 0) }, { new Complex(10, 0) }, { new Complex(5, 0) }, { new Complex(5, 0) }, { new Complex(-0.2, 0) } };
	TransientSpiceResult expectedResult = new TransientSpiceResult();
	expectedResult.updateResult(expected);
	
	/* Instantiate DCSpiceSolver */
	AbstractSpiceSolver solver = new TransientSpiceSolver();

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

	/* Set SImulation time settings */
	double tMin = 0.0;
	double tMax = 1.0;
	int numPoints = 10;
	AbstractSpiceSolver.TimeStepType timeStepType = AbstractSpiceSolver.TimeStepType.LINEAR;

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
	
	/* Add simulation time settings to solver */
	solver.setTimeStep(tMin, tMax, numPoints, timeStepType);

	/* Solve for unknown node voltages and branch currents */
        solver.solve();

	TransientSpiceResult actual = (TransientSpiceResult) solver.getResult();
	
	/* Assert if solver result matches expected solution */
	assertTrue(actual.resultMatch(0, expectedResult, tol));
    }

    /**
     * @brief Test case for a simple voltage divider
     * 
     * @author 1sand0s
     * @since 1.0.0
     * @version 1.0.0
     */
    @Test
    public void testVoltageDividerSinusoidSource_Transient() {
	/* Tolerance for comparing solution */
	double tol = 1e-5;

	/* Solution after Transient analysis */
        Complex expected[][] = { { new Complex(0, 0) }, { new Complex(-5.87785252, 0) }, { new Complex(-2.93892626, 0) }, { new Complex(0.029389262, 0) } };
	TransientSpiceResult expectedResult = new TransientSpiceResult();
	expectedResult.updateResult(expected);

	/* Instantiate TransientSpiceSolver */
	AbstractSpiceSolver solver = new TransientSpiceSolver();

	/* Create a Sinusoidal Source*/
	SinusoidVoltage source = new SinusoidVoltage();

	/* Create resistors */
	Resistor r1 = new Resistor();
        Resistor r2 = new Resistor();

	/* Create circuit GND element */
	GND g1 = new GND();

	/* Create wires to connect circuit elements */
        Wire w1 = new Wire();
        Wire w2 = new Wire();
        Wire w3 = new Wire();

	/* Set Simulation time settings */
	double tMin = 0.0;
	double tMax = 1e-3;
	int numPoints = 10;
	AbstractSpiceSolver.TimeStepType timeStepType = AbstractSpiceSolver.TimeStepType.LINEAR;

	/* Set amplitude of sinusoid source voltage to 10V */
        source.setValue(10);

	/* Set frequency of sinusoid source to 1KHz */
	source.setFrequency(1e3);

	/* Set r1 and r2  to 100 Ohm */
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

	/* Add simulation time settings to solver */
	solver.setTimeStep(tMin, tMax, numPoints, timeStepType);

	/* Solve for unknown node voltages and branch currents */
        solver.solve();

	TransientSpiceResult actual = (TransientSpiceResult) solver.getResult();

	/* Assert if solver result matches expected solution */
	assertTrue(actual.resultMatch(9, expectedResult, tol));
	assertEquals(expectedResult.getElementVoltage(0, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(9, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(0, r2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(9, r2, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	//assertEquals(0.05, r1.getCurrent(actual, 0)[0].magnitude(), tol);
	//assertEquals(0.05, r2.getCurrent(actual, 0)[0].magnitude(), tol);
    }

    /**
     * @brief Test case for a simple Half Wave rectifier
     * 
     * @author 1sand0s
     * @since 1.0.0
     * @version 1.0.0
     */
    @Test
    public void testDiodeHalfWaveRectifier_DC() {
	/* Tolerance for comparing solution */
	double tol = 1e-5;
	
	/* Solution after Transient analysis */
	Complex expected[][] = { { new Complex(0, 0) }, { new Complex(-5.877852, 0) }, { new Complex(-0.48092389, 0) }, { new Complex(0.0539692862, 0) } };
	TransientSpiceResult expectedResult = new TransientSpiceResult();
	expectedResult.updateResult(expected);
	
	/* Instantiate TransientSpiceSolver */
	AbstractSpiceSolver solver = new TransientSpiceSolver();

	/* Create a Sinusoidal Source*/
	SinusoidVoltage source = new SinusoidVoltage();

	/* Create resistor and Didoe */
	Resistor r1 = new Resistor();
        Diode d1 = new Diode();

	/* Create circuit GND element */
	GND g1 = new GND();

	/* Create wires to connect circuit elements */
        Wire w1 = new Wire();
        Wire w2 = new Wire();
        Wire w3 = new Wire();

	/* Set Simulation time settings */
	double tMin = 0.0;
	double tMax = 1e-3;
	int numPoints = 10;
	AbstractSpiceSolver.TimeStepType timeStepType = AbstractSpiceSolver.TimeStepType.LINEAR;
	
	/* Set DC source voltage to 10V */
        source.setValue(10);

	/* Set frequency of sinusoid source to 1KHz */
	source.setFrequency(1e3);

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
	 *        ~ ~                      / \
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

	/* Add simulation time settings to solver */
	solver.setTimeStep(tMin, tMax, numPoints, timeStepType);

	/* Solve for unknown node voltages and branch currents */
        solver.solve();

	TransientSpiceResult actual = (TransientSpiceResult) solver.getResult();

	/* Assert if solver result matches expected solution */
	assertTrue(actual.resultMatch(9, expectedResult, tol));
	assertEquals(expectedResult.getElementVoltage(0, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(9, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(0, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(9, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	//assertEquals(0.05, r1.getCurrent(actual, 0)[0].magnitude(), tol);
	//assertEquals(0.05, r2.getCurrent(actual, 0)[0].magnitude(), tol);
    }

    /**
     * @brief Test case for a simple Full Wave rectifier
     * 
     * @author 1sand0s
     * @since 1.0.0
     * @version 1.0.0
     */
    @Test
    public void testDiodeFullWaveRectifier_DC() {
	/* Tolerance for comparing solution */
	double tol = 1e-5;
	
	/* Solution after Transient analysis */
	Complex expected1[][] = {{ new Complex(0.0, 0.0) }, { new Complex(0.0, 0.0) }, { new Complex(0.0, 0.0) }, { new Complex(0.0, 0.0) }, { new Complex(0.0, 0.0) }};
	Complex expected2[][] = {{ new Complex(0.0, 0.0) }, { new Complex(5.877852522924723, 0.0) }, { new Complex(5.399329920507995, 0.0) }, { new Complex(0.478522602416728, 0.0) }, { new Complex(-0.04920807418090963, 0.0) }};
	Complex expected3[][] = {{ new Complex(0.0, 0.0) }, { new Complex(9.510565162951497, 0.0) }, { new Complex(9.01775493886029, 0.0) }, { new Complex(0.49281022409120817, 0.0) }, { new Complex(-0.08524944814767775, 0.0) }};
	Complex expected4[][] = {{ new Complex(0.0, 0.0) }, { new Complex(9.510565162951513, 0.0) }, { new Complex(9.017754939035965, 0.0) }, { new Complex(0.49281022391554835, 0.0) }, { new Complex(-0.08524944815119637, 0.0) }};
	Complex expected5[][] = {{ new Complex(0.0, 0.0) }, { new Complex(5.877852522924732, 0.0) }, { new Complex(5.399329921003171, 0.0) }, { new Complex(0.4785226019215606, 0.0) }, { new Complex(-0.049208074190816305, 0.0) }};
	Complex expected6[][] = {{ new Complex(0.0, 0.0) }, { new Complex(0.0, 0.0) }, { new Complex(0.0, 0.0) }, { new Complex(0.0, 0.0) }, { new Complex(0.0, 0.0) }};
	Complex expected7[][] = {{ new Complex(0.0, 0.0) }, { new Complex(-5.877852522924741, 0.0) }, { new Complex(-0.478522602416728, 0.0) }, { new Complex(-5.399329920508012, 0.0) }, { new Complex(0.04920807418091706, 0.0) }};
	Complex expected8[][] = {{ new Complex(0.0, 0.0) }, { new Complex(-9.510565162951577, 0.0) }, { new Complex(-0.4928102240912082, 0.0) }, { new Complex(-9.017754938860367, 0.0) }, { new Complex(0.08524944814770248, 0.0) }};
	Complex expected9[][] = {{ new Complex(0.0, 0.0) }, { new Complex(-9.510565162951453, 0.0) }, { new Complex(-0.49281022391554796, 0.0) }, { new Complex(-9.017754939035903, 0.0) }, { new Complex(0.08524944815117598, 0.0) }};
	Complex expected10[][] = {{ new Complex(0.0, 0.0) }, { new Complex(-5.877852522924735, 0.0) }, { new Complex(-0.4785226019215606, 0.0) }, { new Complex(-5.399329921003175, 0.0) }, { new Complex(0.04920807419081662, 0.0) }};

	TransientSpiceResult expectedResult = new TransientSpiceResult();
	expectedResult.updateResult(expected1);
	expectedResult.updateResult(expected2);
	expectedResult.updateResult(expected3);
	expectedResult.updateResult(expected4);
	expectedResult.updateResult(expected5);
	expectedResult.updateResult(expected6);
	expectedResult.updateResult(expected7);
	expectedResult.updateResult(expected8);
	expectedResult.updateResult(expected9);
	expectedResult.updateResult(expected10);
						
	/* Instantiate TransientSpiceSolver */
	AbstractSpiceSolver solver = new TransientSpiceSolver();

	/* Create a Sinusoidal Source*/
	SinusoidVoltage source = new SinusoidVoltage();

	/* Create resistor and Didoe */
	Resistor r1 = new Resistor();
        Diode d1 = new Diode();
	Diode d2 = new Diode();
	Diode d3 = new Diode();
	Diode d4 = new Diode();

	/* Create circuit GND element */
	GND g1 = new GND();

	/* Create wires to connect circuit elements */
        Wire w1 = new Wire();
        Wire w2 = new Wire();
        Wire w3 = new Wire();
	Wire w4 = new Wire();

	/* Set Simulation time settings */
	double tMin = 0.0;
	double tMax = 1e-3;
	int numPoints = 10;
	AbstractSpiceSolver.TimeStepType timeStepType = AbstractSpiceSolver.TimeStepType.LINEAR;
	
	/* Set DC source voltage to 10V */
        source.setValue(10);

	/* Set frequency of sinusoid source to 1KHz */
	source.setFrequency(1e3);

	/* Set r1 resistance to 100 Ohm */
	r1.setValue(100);

	/* Diode parameters for 1N4007
	 * See : https://www.mouser.com/datasheet/2/149/1N4007-888322.pdf
	 */
	d1.setReverseSaturationCurrent(5E-10);
	d1.setCrystalFactor(1);
	d1.setThermalVoltage(2.6E-2);

	d2.setReverseSaturationCurrent(5E-10);
	d2.setCrystalFactor(1);
	d2.setThermalVoltage(2.6E-2);

	d3.setReverseSaturationCurrent(5E-10);
	d3.setCrystalFactor(1);
	d3.setThermalVoltage(2.6E-2);

	d4.setReverseSaturationCurrent(5E-10);
	d4.setCrystalFactor(1);
	d4.setThermalVoltage(2.6E-2);

	/*                Circuit Topology
	 *
	 *                     w1              
	 *         ~------------------------~---------------------~
	 *         |                        |                     |
	 *         |                        |                     |
	 *         |                        |                     |               
	 *         |                        |                     |
	 *         |                       ~~~                  ~~~~~
	 *         |                       \ /  d1               / \  d2
	 *         |                      ~~~~~                  ~~~
	 *         |                        |                     |
         *         |                        |     r1 100 Ohms     |
	 *         |                     w2 ~-----^v^v^v^v^v------~ w3
	 *         |                        |                     |
	 *         |                        |                     |
	 *        ~~~                       |                     |               
	 *       ~ + ~ source               |                     |
	 *       ~ - ~  10V               ~~~~~                  ~~~
	 *        ~ ~   1KHz               / \  d3               \ /  d4
	 *         |                       ~~~                  ~~~~~
	 *         |                        |                     |
         *         |          w4            |                     |
	 *         ~------------------------~---------------------~  
	 *       -----  
	 *        --- g1
	 *         -
	 */

	/* Use wires to connect the circuit elements as shown above */
        w1.addTerminal(source, ComponentTerminals.POS_NODE);
	w1.addTerminal(d1, ComponentTerminals.ANODE);
	w1.addTerminal(d2, ComponentTerminals.CATHODE);
	
        w2.addTerminal(r1, ComponentTerminals.POS_NODE);
        w2.addTerminal(d1, ComponentTerminals.CATHODE);
        w2.addTerminal(d3, ComponentTerminals.CATHODE);

	w3.addTerminal(r1, ComponentTerminals.NEG_NODE);
        w3.addTerminal(d2, ComponentTerminals.ANODE);
        w3.addTerminal(d4, ComponentTerminals.ANODE);

        w4.addTerminal(source, ComponentTerminals.NEG_NODE);
        w4.addTerminal(d3, ComponentTerminals.ANODE);
	w4.addTerminal(d4, ComponentTerminals.CATHODE);
        w4.addTerminal(g1, ComponentTerminals.GND);

	/* Add circuit elements to the solver */
        solver.addElement(source);
        solver.addElement(r1);
        solver.addElement(d1);
	solver.addElement(d2);	
	solver.addElement(d3);
	solver.addElement(d4);
        solver.addElement(g1);
        solver.addWire(w1);
        solver.addWire(w2);
        solver.addWire(w3);
        solver.addWire(w4);
	
	/* Add simulation time settings to solver */
	solver.setTimeStep(tMin, tMax, numPoints, timeStepType);

	/* Solve for unknown node voltages and branch currents */
        solver.solve();

	TransientSpiceResult actual = (TransientSpiceResult) solver.getResult();

	/* Assert if solver result matches expected solution */
	assertTrue(actual.resultMatch(expectedResult, tol));

	
	assertEquals(expectedResult.getElementVoltage(0, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(0, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(0, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(0, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(0, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(0, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(0, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(0, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(0, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(0, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	
	
	assertEquals(expectedResult.getElementVoltage(1, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
	     actual.getElementVoltage(1, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(1, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(1, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(1, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(1, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(1, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(1, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(1, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(1, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	
	
	assertEquals(expectedResult.getElementVoltage(2, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(2, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(2, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(2, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(2, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(2, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(2, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(2, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(2, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(2, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	
	
	assertEquals(expectedResult.getElementVoltage(3, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(3, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(3, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(3, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(3, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(3, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(3, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(3, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(3, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(3, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	
	
	assertEquals(expectedResult.getElementVoltage(4, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(4, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(4, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(4, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(4, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(4, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(4, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(4, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(4, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(4, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	
	
	assertEquals(expectedResult.getElementVoltage(5, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(5, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(5, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(5, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(5, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(5, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(5, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(5, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(5, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(5, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	
	
	assertEquals(expectedResult.getElementVoltage(6, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(6, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(6, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(6, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(6, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(6, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(6, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(6, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(6, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(6, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	
	
	assertEquals(expectedResult.getElementVoltage(7, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(7, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(7, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(7, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(7, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(7, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(7, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(7, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(7, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(7, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	
	
	assertEquals(expectedResult.getElementVoltage(8, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(8, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(8, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(8, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(8, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(8, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(8, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(8, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(8, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(8, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	

	assertEquals(expectedResult.getElementVoltage(9, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     actual.getElementVoltage(9, r1, ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(9, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(9, d1, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(9, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(9, d2, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(9, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(9, d3, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	assertEquals(expectedResult.getElementVoltage(9, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     actual.getElementVoltage(9, d4, ComponentTerminals.ANODE, ComponentTerminals.CATHODE),
		     tol);
	
	//assertEquals(0.05, r1.getCurrent(actual, 0)[0].magnitude(), tol);
	//assertEquals(0.05, r2.getCurrent(actual, 0)[0].magnitude(), tol);
    }
}
