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
package org.eclipse.riena.tests;

import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.eclipse.riena.internal.tests.Activator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

/**
 * @author campo
 * 
 */
public class RienaTestCase extends TestCase {

	/**
	 * 
	 */
	public RienaTestCase() {
		super();
	}

	/**
	 * @param name
	 */
	public RienaTestCase(String name) {
		super(name);
	}

	public void startBundles(String truePattern, String falsePattern) throws BundleException {
		if (truePattern == null) {
			throw new UnsupportedOperationException("truePattern must be set");
		}
		if (falsePattern == null) {
			falsePattern = "";
		}
		Pattern truePat = Pattern.compile(truePattern);
		Pattern falsePat = Pattern.compile(falsePattern);
		BundleContext context = Activator.getContext();

		Bundle[] bundles = context.getBundles();
		for (Bundle bundle : bundles) {
			if (truePat.matcher(bundle.getSymbolicName()).matches() && !(falsePat.matcher(bundle.getSymbolicName()).matches())) {
				if (bundle.getState() == Bundle.RESOLVED || bundle.getState() == Bundle.STARTING /* STARTING==LAZY */) {
					bundle.start();
				} else {
					if (bundle.getState() == Bundle.INSTALLED) {
						throw new RuntimeException("can't start required bundle because it is not RESOLVED but only INSTALLED : " + bundle.getSymbolicName());
					}
				}
			}
		}
	}
}
