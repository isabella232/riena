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
		System.out.print("bind:");
		add("bind", DepOne.class);
	}

	public void unbind(DepOne dep) {
		System.out.print("unbind:");
		remove("bind", DepOne.class);
	}

	public void bind(DepTwo dep) {
		System.out.print("bind:");
		add("bind", DepTwo.class);
	}

	public void unbind(DepTwo dep) {
		System.out.print("unbind:");
		remove("bind", DepTwo.class);
	}

	public void binde(DepOne dep) {
		System.out.print("binde:");
		add("binde", DepOne.class);
	}

	public void entbinde(DepOne dep) {
		System.out.print("entbinde:");
		remove("binde", DepOne.class);
	}

	public void bind(IRanking ranking) {
		System.out.print("bind:");
		add("bind", IRanking.class);
		this.ranking = ranking;
	}

	public void unbind(IRanking ranking) {
		System.out.print("unbind:");
		remove("bind", IRanking.class);
	}

	public void bind(DepOneOne dep) {
		System.out.print("bind:");
		add("bind", DepOneOne.class);
	}

	public void unbind(DepOneOne dep) {
		System.out.print("unbind:");
		remove("bind", DepOneOne.class);
	}

	public int count(String method, Class<?> clazz) {
		method = method + "(" + clazz.getName() + ")";
		List<Class<?>> deps4method = deps.get(method);
		if (deps4method != null)
			return deps4method.size();
		return 0;
	}

	public int getDepRanking() {
		return ranking.getRanking();
	}

	private void add(String method, Class<?> depClass) {
		method = method + "(" + depClass.getName() + ")";
		List<Class<?>> deps4method = deps.get(method);
		if (deps4method == null) {
			deps4method = new ArrayList<Class<?>>();
			deps.put(method, deps4method);
		}
		deps4method.add(depClass);
		System.out.println(" " + method + " " + depClass.getName());
	}

	private void remove(String method, Class<?> depClass) {
		method = method + "(" + depClass.getName() + ")";
		List<Class<?>> deps4method = deps.get(method);
		if (deps4method != null)
			deps4method.remove(depClass);
		System.out.println(" " + method + " " + depClass.getName());
	}

}
