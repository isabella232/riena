/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.riena.ui.ridgets.obsolete.ITreeAdapter;

/**
 * Model of a <code>DynamicTree</code>.
 * 
 * @author Thorsten Schenkel
 * @author Carsten Drossel
 */
public class DynamicLoadTreeModel extends DefaultTreeModel {

	private List<DynamicTreeModificationProcess> treeModificationProcesses;
	private Collection<IDynamicTreeReloadListener> reloadListeners;

	/**
	 * Constructor. Create a new instance of <code>DynamicLoadTreeModel</code>.
	 * 
	 * @param root -
	 *            the root tree element of the dynamic tree.
	 */
	public DynamicLoadTreeModel(DynamicTreeNode root) {
		super(root);

		treeModificationProcesses = new ArrayList<DynamicTreeModificationProcess>(2);
		reloadListeners = new ArrayList<IDynamicTreeReloadListener>(1);
		resetRootChildren();
	}

	/**
	 * Adds listener.
	 * 
	 * @param l -
	 *            the listener to add.
	 */
	public void addDynamicTreeReloadListener(IDynamicTreeReloadListener l) {

		reloadListeners.add(l);

	}

	/**
	 * Removes listener.
	 * 
	 * @param l -
	 *            the listener to remove.
	 */
	public void removeDynamicTreeReloadListener(IDynamicTreeReloadListener l) {

		reloadListeners.remove(l);

	}

	/**
	 * Notifies all listeners that have registered interest for notification on
	 * this event type.
	 * 
	 * @param eventType
	 *            The type of the event that will be fired; the value should be
	 *            one of: DynamicTreeReloadEvent.BEGIN and
	 *            DynamicTreeReloadEvent.END.
	 */
	void fireEvent(DynamicTreeNode node, int eventType) {

		DynamicTreeReloadEvent event = new DynamicTreeReloadEvent(node, eventType);
		for (IDynamicTreeReloadListener listener : reloadListeners) {
			listener.reload(event);
		}
	}

	/**
	 * Returns a tree structure with the values of the currently loaded nodes of
	 * the tree.
	 * 
	 * @return Returns a tree structure with the values of the currently loaded
	 *         nodes of the tree.
	 */
	ValueTreeNode getLoadedValuesState() {
		return ((DynamicTreeNode) root).getLoadedSubTreeValues();
	}

	/**
	 * Executes the specified action while retaining the expansion state of tree
	 * pathes that stay the same based on the displayed value of the nodes. The
	 * node instances themselves may change during the process.
	 * 
	 * @param action
	 *            An action that changes the tree structure by performing some
	 *            change on the underlying model (e.g. changing the filter of
	 *            the nodes)
	 * @param tree
	 *            The tree.
	 */
	public void executeRetainingExpansion(Runnable action, ITreeAdapter tree) {
		queueTreeModificationProcess(new ExpansionRetainingDynamicTreeModificationProcess(this, action, tree));
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.DefaultTreeModel#getChild(java.lang.Object,
	 *      int)
	 */
	@Override
	public Object getChild(Object parent, int index) {
		DynamicTreeNode child = (DynamicTreeNode) super.getChild(parent, index);
		if (!child.isLoaded() && !isLoading(child)) {
			queueTreeModificationProcess(new LoadNodeProcess(this, child));
		}
		return child;
	}

	private boolean isLoading(DynamicTreeNode node) {
		for (DynamicTreeModificationProcess process : treeModificationProcesses) {
			if (process instanceof LoadNodeProcess && ((LoadNodeProcess) process).getNode() == node) {
				return true;
			}
		}
		return false;
	}

	private void resetRootChildren() {
		queueTreeModificationProcess(new ResetRootChildrenProcess(this));
	}

	/**
	 * Adds a process that modifies the tree structure to the queue. If the
	 * queue is empty (i.e. no process is running) the new process will be
	 * started.
	 * 
	 * @param treeModificationProcess
	 *            The process to add.
	 */
	private void queueTreeModificationProcess(final DynamicTreeModificationProcess treeModificationProcess) {
		boolean isProcessRunning = !treeModificationProcesses.isEmpty();
		treeModificationProcesses.add(treeModificationProcess);
		if (!isProcessRunning) {
			treeModificationProcess.start();
		}
	}

	/**
	 * Removes all queued processes modifying the tree structure.
	 */
	void clearTreeModificationProcessQueue() {
		treeModificationProcesses.clear();
	}

	/**
	 * Starts the next process modifying the tree structure. Invoked when the
	 * current process was finished, so the first element of the queue will be
	 * removed.
	 */
	void startNextTreeModificationProcess() {
		treeModificationProcesses.remove(0);
		if (!treeModificationProcesses.isEmpty()) {
			treeModificationProcesses.get(0).start();
		}
	}

}
