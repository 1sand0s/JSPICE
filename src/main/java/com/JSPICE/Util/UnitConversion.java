/**
 * 
 */
package com.JSPICE.Util;

/**
 * @author 1sand0s
 *
 */
public class UnitConversion {

    private static final char metricPrefixs[] = { 'f', // Femto
            'p', // Pico
            'n', // Nano
            'u', // Micro
            'm', // Milli
            'k', // Kilo
            'M', // Mega
            'G' };// Giga

    private static final double metricPrefixScale[] = { Math.pow(10, -15), // Femto
            Math.pow(10, -12), // Pico
            Math.pow(10, -9), // Nano
            Math.pow(10, -6), // Micro
            Math.pow(10, -3), // Milli
            Math.pow(10, 3), // Kilo
            Math.pow(10, 6), // Mega
            Math.pow(10, 9) }; // Giga

    private static final ComponentDenominations denominations[] = { ComponentDenominations.R, // Resistor
            ComponentDenominations.L, // Inductor
            ComponentDenominations.C, // Capacitor
            ComponentDenominations.V, // Voltage Source
            ComponentDenominations.D, // Diode
            ComponentDenominations.Q, // Transistor
            ComponentDenominations.G};// Ground

    private static final String denominationNames[] = { "R", // Resistor
            "L", // Inductor
            "C", // Capacitor
            "V", // Voltage Source
            "D", // Diode
            "Q", // Transistor
            "G"};// Ground

    /**
     * regular
     * 
     * @author 1sand0s
     * @param value : Value in string with metrix prefixs
     * @return Double : value with metric prefixs accounted for
     * @since 1.0.0
     * @version 1.0.0
     */
    public static double convertMetricPrefixToValue(String value) {
        double val = Double.parseDouble(value);

        for (int j = 0; j < metricPrefixs.length; j++) {
            if (value.charAt(value.length() - 1) == metricPrefixs[j]) {
                val = Double.parseDouble(value.substring(0, value.length() - 1)) * metricPrefixScale[j];
                break;
            }
        }
        return val;
    }

    /**
     * regular
     * 
     * @author 1sand0s
     * @param den : denomination of component
     * @return String : string equivalent of denomination
     * @since 1.0.0
     * @version 1.0.0
     */
    public static String convertComponentDenominationToString(ComponentDenominations den) {
        String val = "";

        for (int j = 0; j < denominations.length; j++)
            if (den == denominations[j]) {
                val = denominationNames[j];
                break;
            }
        return val;
    }
}
