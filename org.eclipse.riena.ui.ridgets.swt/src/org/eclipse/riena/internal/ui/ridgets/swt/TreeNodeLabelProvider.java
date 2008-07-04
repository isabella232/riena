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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

/**
 * Label provider for TreeViewers that provides different images based on the
 * expansion state of a tree element:
 * <ul>
 * <li>expandable node - collapsed</li>
 * <li>expandable node - expaned</li>
 * <li>leaf (i.e. node with no children)</li>
 *</ul>
 * This label provider will automatically adjust the images when the expansion
 * state of a node changes.
 */
// TODO [ev] unit tests
final class TreeNodeLabelProvider extends LabelProvider implements ITreeViewerListener {

	private final TreeViewer viewer;

	private Object lastElement;
	private boolean lastElementExpanded;

	TreeNodeLabelProvider(TreeViewer viewer) {
		Assert.isNotNull(viewer);
		this.viewer = viewer;
		viewer.addTreeListener(this);
	}

	@Override
	public Image getImage(Object element) {
		String key = getImageKey(element);
		return Activator.getSharedImage(key);
	}

	/*
	 * For some reason viewer.getExpandedState(element) returns the "old" value
	 * when we are in the ITreeViewerListener methods below.
	 * 
	 * Workaround:
	 * 
	 * Store the lastElement, and lastElementExpanded state in a field and
	 * return the correct value when viewer.update(lastElement, null) triggers a
	 * call to get Image.
	 */

	public synchronized void treeCollapsed(TreeExpansionEvent event) {
		lastElement = event.getElement();
		lastElementExpanded = false;
		viewer.update(lastElement, null);
	}

	public synchronized void treeExpanded(TreeExpansionEvent event) {
		lastElement = event.getElement();
		lastElementExpanded = true;
		viewer.update(lastElement, null);
	}

	// helping methods
	// ////////////////

	private String getImageKey(Object element) {
		String key = SharedImages.IMG_LEAF;
		boolean isExpandable = viewer.isExpandable(element);
		if (isExpandable) {
			// if element is the lastElement return stored expansion state
			boolean isExpanded;
			if (element == lastElement) {
				lastElement = null;
				isExpanded = lastElementExpanded;
			} else {
				isExpanded = viewer.getExpandedState(element);
			}
			key = isExpanded ? SharedImages.IMG_NODE_EXPANDED : SharedImages.IMG_NODE_COLLAPSED;
		}
		return key;
	}

}
