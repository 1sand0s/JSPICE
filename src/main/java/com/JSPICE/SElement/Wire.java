package com.JSPICE.SElement;

import com.JSPICE.Util.ComponentTerminals;

import java.util.ArrayList;

/**
 * @author 1sand0s
 *
 */
public class Wire {
    private ArrayList<SElement> elements;
    private ArrayList<ComponentTerminals> terminals;
    private boolean isGND;
    private int nodeIndex;

    public Wire() {
        elements = new ArrayList<SElement>();
        terminals = new ArrayList<ComponentTerminals>();
        isGND = false;
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param element  : SElement to add
     * @param terminal : the terminal of the elment to add
     * @since 1.0.0
     * @version 1.0.0
     */
    public void addTerminal(SElement element,
                            ComponentTerminals terminal) {
        elements.add(element);
        terminals.add(terminal);

        if (element instanceof GND)
            isGND = true;
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param element  : SElement to add
     * @param terminal : the terminal of the elment to add
     * @since 1.0.0
     * @version 1.0.0
     */
    public boolean isGND() {
        return isGND;
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param element  : SElement to add
     * @param terminal : the terminal of the elment to add
     * @since 1.0.0
     * @version 1.0.0
     */
    public int getNumElements() {
        return elements.size();
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param element  : SElement to add
     * @param terminal : the terminal of the elment to add
     * @since 1.0.0
     * @version 1.0.0
     */
    public SElement getElementAtIndex(int index) {
        return elements.get(index);
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param element  : SElement to add
     * @param terminal : the terminal of the elment to add
     * @since 1.0.0
     * @version 1.0.0
     */
    public ComponentTerminals getTerminalAtIndex(int index) {
        return terminals.get(index);
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param i : list index to remove
     * @since 1.0.0
     * @version 1.0.0
     */
    public void deleteNode(int i) {
        elements.remove(i);
        terminals.remove(i);
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param i : list index to remove
     * @since 1.0.0
     * @version 1.0.0
     */
    public void setNodeIndex(int nodeIndex){
	this.nodeIndex = nodeIndex;
    }

        /**
     * regular
     * 
     * @author 1sand0s
     * @param i : list index to remove
     * @since 1.0.0
     * @version 1.0.0
     */
    public int getNodeIndex(){
	return nodeIndex;
    }
}
