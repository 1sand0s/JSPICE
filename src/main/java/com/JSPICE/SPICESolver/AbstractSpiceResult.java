/**
 * 
 */
package com.JSPICE.SPICESolver;

import com.JSPICE.SElement.SElement;
import com.JSPICE.SMath.Complex;
import com.JSPICE.Util.ComponentTerminals;

/**
 * @author 1sand0s
 *
 */
public abstract class AbstractSpiceResult {

    public abstract void updateResult(Complex x[][]);

    public abstract boolean resultMatch(AbstractSpiceResult result, double tol);

    public boolean resultMatch(int index, AbstractSpiceResult result, double tol){ return false;}

    public void clearResult(){}
}
    
