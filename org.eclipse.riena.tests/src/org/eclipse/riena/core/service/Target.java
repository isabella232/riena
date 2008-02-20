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
package org.eclipse.riena.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The target for injecting
 */
public class Target {

	private Map<String, List<Class<?>>> deps = new HashMap<String, List<Class<?>>>();
	private IRanking ranking;

	public void bind(DepOne dep) {
		called("bind", DepOne.class, dep);
		add("bind", DepOne.class);
	}

	public void unbind(DepOne dep) {
		called("unbind", DepOne.class, dep);
		remove("bind", DepOne.class);
	}

	public void bind(DepTwo dep) {
		called("bind", DepTwo.class, dep);
		add("bind", DepTwo.class);
	}

	public void unbind(DepTwo dep) {
		called("unbind", DepTwo.class, dep);
		remove("bind", DepTwo.class);
	}

	public void binde(DepOne dep) {
		called("binde", DepOne.class, dep);
		add("binde", DepOne.class);
	}

	public void entbinde(DepOne dep) {
		called("entbinde", DepOne.class, dep);
		remove("binde", DepOne.class);
	}

	public void bind(IRanking dep) {
		called("bind", IRanking.class, dep);
		add("bind", IRanking.class);
		this.ranking = dep;
	}

	public void unbind(IRanking dep) {
		called("unbind", IRanking.class, dep);
		remove("bind", IRanking.class);
	}

	public void bind(DepOneOne dep) {
		called("bind", DepOneOne.class, dep);
		add("bind", DepOneOne.class);
	}

	public void unbind(DepOneOne dep) {
		called("unbind", DepOneOne.class, dep);
		remove("bind", DepOneOne.class);
	}

	/**
	 * @param string
	 * @param object
	 */
	private void called(String methodName, Class<?> type, Object object) {
		System.out.println(methodName + "(" + type.getSimpleName() + ") -> " + object.getClass().getSimpleName());
	}

	public int count(String method, Class<?> clazz) {
		List<Class<?>> deps4method = deps.get(key(method, clazz));
		if (deps4method != null)
			return deps4method.size();
		return 0;
	}

	public int getDepRanking() {
		return ranking.getRanking();
	}

	private void add(String method, Class<?> depClass) {
		List<Class<?>> deps4method = deps.get(key(method, depClass));
		if (deps4method == null) {
			deps4method = new ArrayList<Class<?>>();
			deps.put(key(method, depClass), deps4method);
		}
		deps4method.add(depClass);
	}

	private void remove(String method, Class<?> depClass) {
		List<Class<?>> deps4method = deps.get(key(method, depClass));
		if (deps4method != null)
			deps4method.remove(depClass);
	}

	private String key(String methodKey, Class<?> type) {
		return methodKey + "(" + type.getName() + ")";
	}
}
