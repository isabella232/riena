/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.tree;

import java.util.HashMap;
import java.util.Map;

/**
 * Tree node with the nodes value.
 */
public class ValueTreeNode {

	private String value;
	private Map<String, ValueTreeNode> children;

	ValueTreeNode(String value) {
		this.value = value;
		children = new HashMap<String, ValueTreeNode>();
	}

	String getValue() {
		return value;
	}

	void add(ValueTreeNode child) {
		children.put(child.getValue(), child);
	}

	void remove(ValueTreeNode child) {
		children.remove(child.getValue());
	}

	boolean hasChildren() {
		return !children.isEmpty();
	}

	ValueTreeNode getChild(Object childValue) {
		return children.get(childValue);
	}

}
