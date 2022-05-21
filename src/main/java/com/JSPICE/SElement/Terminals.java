/**
 * 
 */
package com.JSPICE.SElement;

import com.JSPICE.Util.ComponentTerminals;

import java.util.ArrayList;

/**
 * @author 1sand0s
 *
 */
public class Terminals {
    ArrayList<Node> nodes;

    /**
     * @param n     : number of terminals
     * @param names : name of terminals
     */
    public Terminals(int n, ComponentTerminals... names) {
        nodes = new ArrayList<Node>(n);

        for (int j = 0; j < n; j++)
            nodes.add(new Node(names[j]));
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param terminal : global indices to assign to the nodes
     * @since 1.0.0
     * @version 1.0.0
     */
    public void setTerminals(int[] terminal) {
        for (int j = 0; j < nodes.size(); j++)
            nodes.get(j).setIndex(terminal[j]);
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
    public void setTerminal(int index,
                            ComponentTerminals terminal) {
        for (int j = 0; j < nodes.size(); j++)
            if (nodes.get(j).getName() == terminal) {
                nodes.get(j).setIndex(index);
                break;
            }
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @return int[] : node indices
     * @since 1.0.0
     * @version 1.0.0
     */
    public int[] getTerminals() {
        int[] terminal = new int[nodes.size()];

        for (int j = 0; j < nodes.size(); j++)
            terminal[j] = nodes.get(j).getIndex();

        return terminal;
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
    public int getTerminal(ComponentTerminals terminal) {
        int index = -1;

        for (int j = 0; j < nodes.size(); j++)
            if (nodes.get(j).getName() == terminal) {
                index = nodes.get(j).getIndex();
                break;
            }
        return index;
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @return ComponentTerminals[] : node names
     * @since 1.0.0
     * @version 1.0.0
     */
    public ComponentTerminals[] getTerminalNames() {
        ComponentTerminals[] terminal = new ComponentTerminals[nodes.size()];

        for (int j = 0; j < nodes.size(); j++)
            terminal[j] = nodes.get(j).getName();

        return terminal;
    }
}
