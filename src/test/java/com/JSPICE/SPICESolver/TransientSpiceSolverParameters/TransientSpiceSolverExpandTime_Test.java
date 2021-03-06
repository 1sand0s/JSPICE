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
public class TransientSpiceSolverExpandTime_Test {

    /**
     * @brief Test case for a simple voltage divider
     * 
     * @author 1sand0s
     * @since 1.0.0
     * @version 1.0.0
     */
    @Test
    public void testExpandTimeLinear() {
	/* Instantiate TransientSpiceSolver */
	AbstractSpiceSolver solver = new TransientSpiceSolver();

	/* Set Simulation time settings */
	double tMin = 0.0;
	double tMax = 1.0;
	int numPoints = 10;
	AbstractSpiceSolver.TimeStepType timeStepType = AbstractSpiceSolver.TimeStepType.LINEAR;

	/* Add simulation time settings to solver */
	solver.setTimeStep(tMin, tMax, numPoints, timeStepType);

	/* Generate time steps linearly between tMin and tMax */
	solver.expandTime();
	
	
    }
}
