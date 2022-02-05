/**
 * 
 */
package com.JSPICE.SMath;

import com.JSPICE.SMath.ComplexMatrixOperations;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * @author 1sand0s
 *
 */
public class ComputeLinearEquation_Test {

    /**
    * regular
    * @author 1sand0s
    * @param 
    * @return 
    * @since 
    * @version 1.0.0
    * @exception 
    */
    @Test
    public void solveLinearEquation_2x2Matrix() {
        double tol = 1e-5;
        Complex A[][] = ComplexMatrixOperations.eye(2);
        Complex z[][] = { { new Complex(1, 0) }, { new Complex(1, 0) } };

        Complex x[][] = ComplexMatrixOperations.computeLinearEquation(A, z);
        Complex xr[][] = { { new Complex(1, 0) }, { new Complex(1, 0) } };

        assertTrue(ComplexMatrixOperations.compareMatrices(x, xr, tol));
    }

    /**
    * regular
    * @author 1sand0s
    * @param 
    * @return 
    * @since 
    * @version 1.0.0
    * @exception 
    */
    @Test
    public void solveLinearEquation_3x3Matrix() {
        double tol = 1e-5;
        Complex A[][] = ComplexMatrixOperations.eye(3);
        Complex z[][] = { { new Complex(1, 0) }, { new Complex(1, 0) }, { new Complex(1, 0) } };

        Complex x[][] = ComplexMatrixOperations.computeLinearEquation(A, z);
        Complex xr[][] = { { new Complex(1, 0) }, { new Complex(1, 0) }, { new Complex(1, 0) } };

        assertTrue(ComplexMatrixOperations.compareMatrices(x, xr, tol));
    }
}
