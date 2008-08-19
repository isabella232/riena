/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation;

import java.util.HashMap;
import java.util.Map;

/**
 * This class holds the ApplicationModel(s) of a Riena application in a static
 * way. If you need more than one ApplicationModel, you must specify a unique
 * ApplicationModel names.
 * 
 * @author Stefan Flick
 */
public class ApplicationModelManager {
	private static String defaultModelName = "default"; //$NON-NLS-1$
	private static Map<String, IApplicationModel> modelMap = new HashMap<String, IApplicationModel>();

	/**
	 * Answer the default applicationModel
	 * 
	 * @see org.eclipse.riena.navigation.IApplicationModel
	 * @return the default application model or null if no defaultmodel is
	 *         present. Usually only one (the default model) model is used.
	 */
	public static IApplicationModel getApplicationModel() {
		IApplicationModel model = getApplicationModel(defaultModelName);
		if (model == null && modelMap.size() == 1) {
			// fallback strategy
			return modelMap.values().iterator().next();
		}
		return model;
	}

	/**
	 * Answer the ApplicationModel with the given name
	 * 
	 * @param label
	 *            the name of the mApplicationModel
	 * 
	 * @see org.eclipse.riena.navigation.IApplicationModel
	 * @return the ApplicationModel with the matching name or null if no
	 *         matching model is present.
	 */
	public static IApplicationModel getApplicationModel(String name) {
		String modelName = name;
		if (modelName == null) {
			modelName = defaultModelName;
		}
		return modelMap.get(modelName);
	}

	/**
	 * Clear the ApplicationModel map
	 * 
	 * @see org.eclipse.riena.navigation.IApplicationModel
	 */
	public static synchronized void clear() {
		modelMap = new HashMap<String, IApplicationModel>();
	}

	/**
	 * Register the given ApplicationModel. If the ApplicationModel already
	 * exists, an ApplicationModelFailure is thrwon
	 * 
	 * @see org.eclipse.riena.navigation.IApplicationModel
	 * @see org.eclipse.riena.navigation.ApplicationModelFailure
	 * @param model
	 *            the model to register
	 */
	public static synchronized void registerApplicationModel(IApplicationModel model) {
		String modelName = model.getLabel();
		if (modelName == null) {
			modelName = defaultModelName;
		}
		if (modelMap.containsKey(modelName)) {
			throw new ApplicationModelFailure("ApplicationModel '" + modelName + "' already registered"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		modelMap.put(modelName, model);
		return;
	}

}
