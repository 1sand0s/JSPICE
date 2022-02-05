/**
 * 
 */
package com.JSPICE.SPICESolver;

import com.JSPICE.SElement.SElement;
import com.JSPICE.SMath.Complex;
import com.JSPICE.SElement.Wire;
import com.JSPICE.SElement.VSource;

import java.util.ArrayList;

/**
 * @author 1sand0s
 *
 */
public abstract class AbstractSpiceSolver {
    protected ArrayList<SElement> circuitElements;
    protected ArrayList<Wire> wires;
    protected Complex G[][];
    protected Complex B[][];
    protected Complex C[][];
    protected Complex D[][];
    protected Complex x[][];
    protected Complex z[][];
    protected int iVSource = 0;
    protected int iISource = 0;

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
    public void addElement(SElement element) {
        circuitElements.add(element);
        if (element instanceof VSource)
            iVSource++;
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
    public void removeElement(int index) {
        for (int j = 0; j < circuitElements.size(); j++)
            if (circuitElements.get(j).getId_Int() == index) {
                if (circuitElements.get(j) instanceof VSource)
                    iVSource--;
                circuitElements.remove(j);
                break;
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
    public void addWire(Wire wire) {
        wires.add(wire);
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
    public abstract void solve();

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
    public Complex[][] getResult() {
        return x;
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param frequency
     * @return
     * @since
     * @version 1.0.0
     * @exception
     */
    public void setFrequency(double frequency) {
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
    public void numberNodes() {
        int index = 0;
        int gndIndex = -1;
        for (int j = 0; j < wires.size(); j++) {
            if (!wires.get(j).isGND()) {
                for (int i = 0; i < wires.get(j).getNumElements(); i++)
                    wires.get(j).getElementAtIndex(i).setTerminalIndex(index, wires.get(j).getTerminalAtIndex(i));
                index++;
            } else
                gndIndex = j;
        }

        if (gndIndex != -1)
            for (int i = 0; i < wires.get(gndIndex).getNumElements(); i++)
                wires.get(gndIndex).getElementAtIndex(i).setTerminalIndex(index,
                        wires.get(gndIndex).getTerminalAtIndex(i));
    }
}
