/**
 * 
 */
package com.JSPICE.SMath;

/**
 * @author 1sand0s
 *
 */

public class ComplexMatrixOperations {
    static Complex Yj;
    private static Complex[][] Ymat;

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
    public static Complex Add(Complex num,
                              Complex num1) {
        Complex num2 = new Complex(0, 0);
        num2.setReal(num.getReal() + num1.getReal());
        num2.setImaginary(num.getImaginary() + num1.getImaginary());
        return num2;
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
    public static Complex Sub(Complex num,
                              Complex num1) {
        Complex num2 = new Complex(0, 0);
        num2.setReal(num.getReal() - num1.getReal());
        num2.setImaginary(num.getImaginary() - num1.getImaginary());
        return num2;
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
    public static Complex[] AddArrays(Complex num1[],
                                      Complex num2[]) {
        Complex num3[] = new Complex[num1.length];
        for (int i = 1; i < num1.length; i++)
            num3[i] = Add(num1[i], num2[i]);
        return num3;
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
    public static Complex[] SubArrays(Complex num1[],
                                      Complex num2[]) {
        Complex num3[] = new Complex[num1.length];
        for (int i = 0; i < num1.length; i++)
            num3[i] = Sub(num1[i], num2[i]);
        return num3;
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
    public static Complex Multiply(Complex num1,
                                   Complex num2) {
        Complex num3 = new Complex(0, 0);
        num3.setReal(num1.getReal() * num2.getReal() - num1.getImaginary() * num2.getImaginary());
        num3.setImaginary(num1.getReal() * num2.getImaginary() + num1.getImaginary() * num2.getReal());
        return num3;
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
    public static Complex[] Multiply(Complex num1[],
                                     Complex num2[]) {
        Complex num3[] = new Complex[num2.length];
        for (int i = 0; i < num1.length; i++)
            num3[i + 1] = Multiply(num1[i], num2[i + 1]);
        return num3;
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
    public static Complex[] Multiply(Complex num1[],
                                     Complex num2) {
        Complex num3[] = new Complex[num1.length];
        for (int i = 0; i < num1.length; i++)
            num3[i] = Multiply(num1[i], num2);
        return num3;
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
    public static Complex ScalarAdd(Complex num,
                                    double scalar) {
        Complex num1 = new Complex(0, num.getImaginary());
        num1.setReal(num.getReal() + scalar);
        return num1;
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
    public static Complex ScalarMultiply(Complex num,
                                         double scalar) {
        Complex num1 = new Complex(0, 0);
        num1.setReal(num.getReal() * scalar);
        num1.setImaginary(num.getImaginary() * scalar);
        return num1;
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
    public static Complex[] ScalarMultiply(Complex num[],
                                           double scalar[]) {
        Complex num1[] = new Complex[scalar.length];
        for (int i = 0; i < num.length; i++)
            num1[i + 1] = ScalarMultiply(num[i], scalar[i + 1]);
        return num1;
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
    public static Complex[] ScalarMultiply(Complex num[],
                                           double scalar) {
        Complex num1[] = new Complex[num.length];
        for (int i = 0; i < num.length; i++)
            num1[i] = ScalarMultiply(num[i], scalar);
        return num1;
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
    public static double Abs(Complex num) {
        return Math.hypot(num.getReal(), num.getImaginary());
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
    public static double computeNorm(Complex num[]) {
        double d = 0;
        for (int i = 0; i < num.length; i++) {
            d += Math.pow(num[i].getReal(), 2) + Math.pow(num[i].getImaginary(), 2);
        }
        return Math.sqrt(d);
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
    public static Complex[][] transpose(Complex d1[][]) {
        Complex d[][] = new Complex[d1[0].length][d1.length];
        for (int i = 0; i < d1.length; i++) {
            for (int j = 0; j < d1[0].length; j++) {
                d[j][i] = new Complex(0, 0);
                d[j][i].setReal(d1[i][j].getReal());
                d[j][i].setImaginary(d1[i][j].getImaginary());
            }
        }
        return d;
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
    public static Complex conjugate(Complex num) {
        Complex num1 = new Complex(0, 0);
        num1.setReal(num.getReal());
        num1.setImaginary(-num.getImaginary());
        return num1;
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
    public static Complex[][] conjugate(Complex num[][]) {
        Complex num1[][] = new Complex[num.length][num.length];
        for (int i = 0; i < num.length; i++) {
            for (int j = 0; j < num.length; j++) {
                num1[i][j] = conjugate(num[i][j]);
            }
        }
        return num1;
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
    public static Complex dotProduct(Complex num1[],
                                     Complex num2[]) {
        Complex num = new Complex(0, 0);
        for (int i = 0; i < num1.length; i++) {
            num = Add(num, Multiply(num1[i], conjugate(num2[i])));
        }
        return num;
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
    public static Complex[][][] computeQR(Complex A1[][]) {
        Complex[][] Q, R, A;
        Q = new Complex[A1.length][A1.length];
        R = new Complex[A1.length][A1.length];
        A = new Complex[A1.length][A1.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                A[j][i] = new Complex(0, 0);
                A[j][i].setReal(A1[i][j].getReal());
                A[j][i].setImaginary(A1[i][j].getImaginary());
            }
        }
        Complex u[] = new Complex[A[0].length];
        Complex e[] = ScalarMultiply(A[0], 1 / computeNorm(A[0]));
        Q[0] = e;
        for (int j = 1; j < A.length; j++) {
            System.arraycopy(A[j], 0, u, 0, A[j].length);
            for (int i = j - 1; i >= 0; i--) {
                u = SubArrays(u, Multiply(Q[i], dotProduct(A[j], Q[i])));
            }
            e = ScalarMultiply(u, 1 / computeNorm(u));
            Q[j] = e;
        }
        for (int j = 0; j < Q.length; j++) {
            for (int i = 0; i < j; i++)
                R[j][i] = new Complex(0, 0);
            for (int i = j; i < Q.length; i++) {
                R[j][i] = dotProduct(A[i], Q[j]);
            }
        }
        return new Complex[][][] { Q, R };
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
    public static Complex[][] multiplyMatrix(Complex d1[][],
                                             Complex d2[][]) {
        Complex d[][] = new Complex[d1.length][d2.length];
        for (int i = 0; i < d1.length; i++) {
            for (int j = 0; j < d2.length; j++) {
                d[i][j] = multiply(d1[i], d2[j]);
            }
        }
        return d;
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
    public static Complex multiply(Complex num1[],
                                   Complex num2[]) {
        Complex num = new Complex(0, 0);
        for (int i = 0; i < num1.length; i++) {
            num = Add(num, Multiply(num1[i], num2[i]));
        }
        return num;
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
    public static Complex divideComplex(Complex num1,
                                        Complex num2) {
        return Multiply(num1, ScalarMultiply(conjugate(num2), 1 / Math.pow(Abs(num2), 2)));
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
    public static Complex[][] computeLinearEquation(Complex A1[][],
                                                    Complex z[][]) {
        Complex[][][] QR = computeQR(A1);
        Complex[][] Q = conjugate(QR[0]);
        Complex[][] R = QR[1];
        Complex x[][] = new Complex[z.length][z[0].length];
        initializeMatrices(x);
        Complex Y[][] = multiplyMatrix(Q, transpose(z));
        for (int k = 0; k < x[0].length; k++) {
            for (int i = x.length - 1; i >= 0; i--) {
                Complex u = new Complex(0, 0);
                for (int j = i; j < x.length; j++) {
                    u = Sub(u, Multiply(R[i][j], x[j][k]));
                }
                x[i][k] = divideComplex(Add(Y[i][k], u), R[i][i]);
                x[i][k] = removeRedundancy(x[i][k]);
            }
        }
        return x;
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
    public static Complex[][] eye(int size) {
        Complex d[][] = new Complex[size][size];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d.length; j++) {
                if (i == j)
                    d[i][j] = new Complex(1, 0);
                else
                    d[i][j] = new Complex(0, 0);
            }
        }
        return d;
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
    public static Complex removeRedundancy(Complex d) {
        Complex d1 = new Complex(0, 0);
        d1.setReal(Math.abs(d.getReal()) < 1e-10 ? 0 : d.getReal());
        d1.setImaginary(Math.abs(d.getImaginary()) < 1e-10 ? 0 : d.getImaginary());
        return d1;
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
    public static void initializeMatrices(Complex[][] A) {
        for (int j = 0; j < A.length; j++)
            for (int i = 0; i < A[j].length; i++)
                A[j][i] = new Complex(0, 0);
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
    public static void printMatrices(Complex[][] A) {
        for (int j = 0; j < A.length; j++) {
            for (int i = 0; i < A[j].length; i++)
                System.out.print("(" + A[j][i].getReal() + " + j" + A[j][i].getImaginary() + "), ");
            System.out.println("\n");
        }
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
    public static boolean compareMatrices(Complex[][] A,
                                          Complex[][] B,
                                          double tol) {
        boolean flag = true;

        for (int j = 0; j < A.length; j++)
            for (int i = 0; i < A[j].length; i++)
                if (!(Math.abs(A[j][i].getReal() - B[j][i].getReal()) < tol
                        && Math.abs(A[j][i].getImaginary() - B[j][i].getImaginary()) < tol)) {
                    flag = false;
                    break;
                }
        return flag;
    }
}
