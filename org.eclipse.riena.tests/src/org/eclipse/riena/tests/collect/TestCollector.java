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
package org.eclipse.riena.tests.collect;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.riena.core.util.Iter;
import org.objectweb.asm.ClassReader;
import org.osgi.framework.Bundle;

/**
 * A helper for collecting test classes.
 */
public final class TestCollector {

	private static final String PLUS = " + ";

	private TestCollector() {
		// utility
	}

	/**
	 * Create a {@code TestSuite} that contains all test case in the given
	 * bundle.
	 * 
	 * @param bundle
	 *            bundle to collect all {@code TestCase}s
	 * @param withinPackage
	 *            if not null, only {@code TestCase}s within this package are
	 *            collected
	 * @param annotationClasses
	 *            only {@code TestCase}s that have these annotations are
	 *            collected
	 * @return
	 */
	public static TestSuite createTestSuiteWith(final Bundle bundle, final Package withinPackage,
			final Class<? extends Annotation>... annotationClasses) {
		StringBuilder bob = new StringBuilder("Tests within bundle '").append(bundle.getSymbolicName()).append(
				"' and package '");
		bob.append(withinPackage == null ? "all" : withinPackage.getName()).append("'");
		bob.append(" and restricted to '");
		for (Class<? extends Annotation> annotationClass : annotationClasses) {
			bob.append(annotationClass.getSimpleName()).append(PLUS);
		}
		if (annotationClasses.length > 0) {
			bob.setLength(bob.length() - PLUS.length());
		} else {
			bob.append("none");
		}
		bob.append("'.");
		TestSuite suite = new TestSuite(bob.toString());
		for (Class<? extends TestCase> clazz : collectWith(bundle, withinPackage, annotationClasses)) {
			suite.addTestSuite(clazz);
		}
		return suite;
	}

	/**
	 * Collect all {@code TestCase}e within the given bundle.
	 * 
	 * @param bundle
	 *            bundle to collect all {@code TestCase}s
	 * @param withinPackage
	 *            if not null, only {@code TestCase}s within this package are
	 *            collected
	 * @param annotationClasses
	 *            only {@code TestCase}s that have these annotations are
	 *            collected
	 * @return
	 */
	public static List<Class<? extends TestCase>> collectWith(final Bundle bundle, final Package withinPackage,
			final Class<? extends Annotation>... annotationClasses) {
		List<Class<? extends TestCase>> testClasses = new ArrayList<Class<? extends TestCase>>();

		for (Class<? extends TestCase> testClass : collect(bundle, withinPackage)) {
			boolean collect = true;
			for (Class<? extends Annotation> annotationClass : annotationClasses) {
				if (!testClass.isAnnotationPresent(annotationClass)) {
					// Skip class with wrong annotation
					collect = false;
					continue;
				}
			}
			if (collect) {
				testClasses.add(testClass);
			}
		}

		return testClasses;
	}

	/**
	 * Collect all unmarked {@code TestCase}e within the given bundle.
	 * 
	 * @param bundle
	 *            bundle to collect all {@code TestCase}s
	 * @param withinPackage
	 *            if not null, only {@code TestCase}s within this package are
	 *            collected
	 * @return
	 */
	public static List<Class<? extends TestCase>> collectUnmarked(final Bundle bundle, final Package withinPackage) {
		List<Class<? extends TestCase>> testClasses = new ArrayList<Class<? extends TestCase>>();

		for (Class<? extends TestCase> testClass : collect(bundle, withinPackage)) {
			if (testClass.getAnnotations().length == 0) {
				testClasses.add(testClass);
			}
		}

		return testClasses;
	}

	/**
	 * Collect all {@code TestCase}e within the given bundle.
	 * 
	 * @param bundle
	 *            bundle to collect all {@code TestCase}s
	 * @param withinPackage
	 *            if not null, only {@code TestCase}s within this package are
	 *            collected
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Class<? extends TestCase>> collect(final Bundle bundle, final Package withinPackage) {
		List<Class<? extends TestCase>> testClasses = new ArrayList<Class<? extends TestCase>>();
		Enumeration<URL> allClasses = bundle.findEntries("", "*.class", true);
		for (URL entryURL : Iter.able(allClasses)) {
			String url = entryURL.toExternalForm();
			if (url.contains("$")) {
				// Skip inner classes
				continue;
			}
			String className = getClassName(entryURL);
			if (className == null) {
				trace("Could not get class name from ", url);
				continue;
			}
			Class<? extends TestCase> clazz = null;
			try {
				clazz = bundle.loadClass(className);
			} catch (ClassNotFoundException e) {
				System.out.println(e);
				continue;
			}
			if (withinPackage != null && !className.equals(withinPackage.getName() + "." + clazz.getSimpleName())) {
				continue;
			}
			if (!TestCase.class.isAssignableFrom(clazz)) {
				continue;
			}
			if (clazz.isAnnotationPresent(NonGatherableTestCase.class)) {
				continue;
			}
			if (Modifier.isAbstract(clazz.getModifiers())) {
				continue;
			}
			testClasses.add(clazz);
		}
		return testClasses;
	}

	private static String getClassName(URL entryURL) {
		try {
			ClassReader classReader = new ClassReader(entryURL.openStream());
			return classReader.getClassName().replace('/', '.');
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	private static void trace(Object... objects) {
		StringBuilder bob = new StringBuilder();
		for (Object object : objects) {
			bob.append(object);
		}
		System.err.println(bob.toString());
	}
}
