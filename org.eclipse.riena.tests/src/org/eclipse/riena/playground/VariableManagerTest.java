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
package org.eclipse.riena.playground;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IStringVariable;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.riena.core.util.VariableManagerUtil;
import org.eclipse.riena.tests.RienaTestCase;

/**
 *
 */
public class VariableManagerTest extends RienaTestCase {

	public void xtestVM() throws CoreException {
		addPluginXml(VariableManagerTest.class, "dynamicvar.xml");
		addPluginXml(VariableManagerTest.class, "contributedvar.xml");
		IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
		IValueVariable[] variables = new IValueVariable[1];
		variables[0] = variableManager.newValueVariable((String) "key@x", null, true, (String) "value");
		variableManager.addVariables(variables);

		substitute(variableManager, "key@x");

		substitute(variableManager, "eclipse_home");

		//removeExtension("org.eclipse.core.variables.dynamicVariables.double");

		// substitute(variableManager, "env_var");
		// substitute(variableManager, "system");
		// substitute(variableManager, "system_property");
		//
		// substitute(variableManager, "workspace_loc");
		// substitute(variableManager, "project_loc");
		// substitute(variableManager, "project_path");
	}

	public void testJavaUserHome() throws CoreException {
		addPluginXml(VariableManagerTest.class, "dynamicjavavar.xml");
		String result = VariableManagerUtil.substitute("${java_user.home}");
		System.out.println(result);

	}

	public void testListVariables() throws CoreException {
		IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
		System.out.println("ValueVariables: ");
		for (IValueVariable var : variableManager.getValueVariables()) {
			System.out.println(var.getName() + ":" + var.getValue());
		}
		System.out.println("StringVariables: ");
		for (IStringVariable var : variableManager.getVariables()) {
			System.out.println(var.getName() + ", " + var.getDescription());
		}
		System.out.println("DynamicVariables: ");
		for (IDynamicVariable var : variableManager.getDynamicVariables()) {
			System.out.println(var.getName() + ", " + var.getValue(null));
		}
	}

	private void substitute(IStringVariableManager variableManager, String string) {
		try {
			System.out.println(string + ": " + variableManager.performStringSubstitution("${" + string + "}"));
		} catch (CoreException e) {
			System.out.println(string + ": <undefined>");
		}
	}
}
