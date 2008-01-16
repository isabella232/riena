/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.core.config;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;

import org.eclipse.riena.internal.core.Activator;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ConfigurationPlugin;
import org.osgi.service.cm.ManagedService;

/**
 * This class gets notified for each and every configuration that ConfigAdmin applies. ConfigSymbolReplace itself is
 * configurable by sending configuration to SERVICE_PID = "org.eclipse.riena.config.symbols"
 * 
 */
public class ConfigSymbolReplace implements ConfigurationPlugin, ManagedService {

    private HashMap<String, String> symbols = new HashMap<String, String>();

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.service.cm.ConfigurationPlugin#modifyConfiguration(org.osgi.framework.ServiceReference,
     *      java.util.Dictionary)
     */
    public void modifyConfiguration(ServiceReference reference, Dictionary properties) {
        // don't do symbolsource replace for configurations of myself
        if (reference != null && Activator.getDefault().getContext().getService(reference) == this) {
            return;
        }
        Enumeration<?> enumeration = properties.keys();
        while (enumeration.hasMoreElements()) {
            Object key = enumeration.nextElement();
            Object value = properties.get(key);
            if (value instanceof String) {
                if (((String) value).contains("${")) {
                    properties.put(key, replaceSymbol((String) value));
                }
            }
        }
    }

    /**
     * looks for symbols in a specific property value
     * 
     * @param value
     * @return
     */
    private String replaceSymbol(String value) {
        StringBuilder sb = new StringBuilder(value);
        int i;
        while ((i = sb.indexOf("${")) > -1) {
            int x = sb.substring(i).indexOf('}');
            if (x > 0) {
                String key = value.substring(i + 2, i + x);
                sb.replace(i, i + x + 1, lookupSymbol(key));
            }
        }
        return sb.toString();
    }

    /**
     * looks up a symbol in its table
     * 
     * @param symbol
     * @return
     */
    private String lookupSymbol(String symbol) {
        String value = symbols.get(symbol);
        if (value == null) {
            throw new RuntimeException("symbol " + symbol + " not found.");
        } else {
            return value;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
     */
    public void updated(Dictionary properties) throws ConfigurationException {
        if (properties == null) {
            return;
        }
        HashMap<String, String> tempSymbols = new HashMap<String, String>();
        Enumeration<String> enumeration = properties.keys();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = (String) properties.get(key);
            tempSymbols.put(key, value);
        }
        this.symbols = tempSymbols;
    }

}
