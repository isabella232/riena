/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.tree;

import java.util.HashMap;
import java.util.Map;

/**
 * Tree node with the nodes value.
 *
 * @author Carsten Drossel
 */
public class ValueTreeNode {

	private String value;
	private Map<String, ValueTreeNode> children;

	ValueTreeNode( String value ) {
		this.value = value;
		children = new HashMap<String, ValueTreeNode>();
	}

	String getValue() {
		return value;
	}

	void add( ValueTreeNode child ) {
		children.put( child.getValue(), child );
	}

	void remove( ValueTreeNode child ) {
		children.remove( child.getValue() );
	}

	boolean hasChildren() {
		return !children.isEmpty();
	}

	ValueTreeNode getChild( Object childValue ) {
		return children.get( childValue );
	}

}
