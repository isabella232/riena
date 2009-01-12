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

import java.util.Dictionary;
import java.util.Hashtable;

import junit.framework.TestCase;

import org.eclipse.riena.tests.collect.NonUITestCase;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;

/**
 *
 */
@NonUITestCase
public class FilterTest extends TestCase {

	public void testFilters() throws Exception {
		Filter filter = FrameworkUtil.createFilter("(value>=2)");
		Dictionary<String, Integer> dict = new Hashtable<String, Integer>();
		dict.put("value", 1);
		System.out.println(filter.match(dict));
	}
}
