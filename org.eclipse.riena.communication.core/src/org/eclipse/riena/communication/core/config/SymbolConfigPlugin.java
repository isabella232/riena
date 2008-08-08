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
package org.eclipse.riena.communication.core.config;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationPlugin;

/**
 * @deprecated
 */
public class SymbolConfigPlugin implements ConfigurationPlugin {

	private HashMap<String, String> symbolTable = new HashMap<String, String>();

	public SymbolConfigPlugin() {
		super();

		IExtensionRegistry registry = RegistryFactory.getRegistry();
		IExtension[] cfgExtensions = registry.getExtensionPoint("org.eclipse.riena.communication.core.configProxies").getExtensions(); //$NON-NLS-1$
		for (IExtension cfgExt : cfgExtensions) {
			IConfigurationElement[] configs = cfgExt.getConfigurationElements();
			for (IConfigurationElement config : configs) {
				IConfigurationElement[] properties = config.getChildren();
				for (IConfigurationElement prop : properties) {
					String name = prop.getAttribute("name"); //$NON-NLS-1$
					String value = prop.getAttribute("value"); //$NON-NLS-1$
					symbolTable.put(name, value);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void modifyConfiguration(ServiceReference servicereference, Dictionary dictionary) {
		Enumeration<String> keyEnum = dictionary.keys();
		while (keyEnum.hasMoreElements()) {
			String key = keyEnum.nextElement();
			String value = (String) dictionary.get(key);
			dictionary.put(key, replaceSymbol(value));
		}
	}

	private String replaceSymbol(String value) {
		StringBuilder sb = new StringBuilder(value);
		for (int i = 0; i < sb.length() - 1; i++) {
			if (sb.substring(i, i + 2).equals("${")) { //$NON-NLS-1$
				int x = sb.substring(i).indexOf('}');
				if (x > 0) {
					String key = value.substring(i + 2, i + x);
					sb.replace(i, i + x + 1, lookupSymbol(key));
				}
			}
		}
		return sb.toString();
	}

	private String lookupSymbol(String symbol) {
		String value = symbolTable.get(symbol);
		if (value == null) {
			return "${" + symbol + "}"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return value;
		}
	}
}
