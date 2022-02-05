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
public class ComputeQR_Test {

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
    public void computeQRofIdentityMatrix() {
        double tol = 1e-5;
        Complex A[][] = ComplexMatrixOperations.eye(2);
        Complex[][][] QR = ComplexMatrixOperations.computeQR(A);
        Complex[][] Q = ComplexMatrixOperations.conjugate(QR[0]);
        Complex[][] R = QR[1];

        Complex Ar[][] = { { new Complex(1, 0), new Complex(0, 0) }, { new Complex(0, 0), new Complex(1, 0) } };
        Complex Qr[][] = { { new Complex(1, 0), new Complex(0, 0) }, { new Complex(0, 0), new Complex(1, 0) } };
        Complex Rr[][] = { { new Complex(1, 0), new Complex(0, 0) }, { new Complex(0, 0), new Complex(1, 0) } };

        assertTrue(ComplexMatrixOperations.compareMatrices(A, Ar, tol));
        assertTrue(ComplexMatrixOperations.compareMatrices(Q, Qr, tol));
        assertTrue(ComplexMatrixOperations.compareMatrices(R, Rr, tol));
    }
}
