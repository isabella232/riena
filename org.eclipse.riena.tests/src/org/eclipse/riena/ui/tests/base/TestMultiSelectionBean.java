/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2008 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.tests.base;

import java.util.ArrayList;
import java.util.List;

public class TestMultiSelectionBean {

	private List<Object> selectionList = new ArrayList<Object>();

	public List<Object> getSelectionList() {
		return selectionList;
	}

	public void setSelectionList(List<Object> selectionList) {
		this.selectionList = selectionList;
	}

}