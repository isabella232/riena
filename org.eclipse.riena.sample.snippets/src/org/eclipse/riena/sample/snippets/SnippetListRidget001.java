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
package org.eclipse.riena.sample.snippets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.beans.common.AbstractBean;
import org.eclipse.riena.beans.common.TypedBean;
import org.eclipse.riena.ui.ridgets.IListRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates listening to selection changes on a list ridget.
 */
public class SnippetListRidget001 {

	public SnippetListRidget001(Shell shell) {
		shell.setLayout(new FillLayout());
		org.eclipse.swt.widgets.List list = UIControlsFactory.createList(shell, false, true);

		IListRidget listRidget = (IListRidget) SwtRidgetFactory.createRidget(list);
		List<MyNode> input = createInput();
		listRidget.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		listRidget.bindToModel(new WritableList(input, MyNode.class), MyNode.class, "english"); //$NON-NLS-1$
		listRidget.updateFromModel();

		final TypedBean<MyNode> selection = new TypedBean<MyNode>(null);
		selection.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				MyNode node = selection.getValue();
				System.out.println("Selection: " + node.getEnglish()); //$NON-NLS-1$
			}
		});
		listRidget.bindSingleSelectionToModel(selection, "value"); //$NON-NLS-1$
	}

	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			new SnippetListRidget001(shell);
			shell.pack();
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} finally {
			display.dispose();
		}
	}

	private List<MyNode> createInput() {
		List<MyNode> nodes = new ArrayList<MyNode>(7);

		nodes.add(new MyNode("Monday")); //$NON-NLS-1$
		nodes.add(new MyNode("Tuesday")); //$NON-NLS-1$
		nodes.add(new MyNode("Wednesday")); //$NON-NLS-1$
		nodes.add(new MyNode("Thursday")); //$NON-NLS-1$
		nodes.add(new MyNode("Friday")); //$NON-NLS-1$
		nodes.add(new MyNode("Saturday")); //$NON-NLS-1$
		nodes.add(new MyNode("Sunday")); //$NON-NLS-1$

		return nodes;
	}

	private static class MyNode extends AbstractBean {
		private String english;

		public MyNode(String english) {
			this.english = english;
		}

		public String getEnglish() {
			return english;
		}
	}

}
