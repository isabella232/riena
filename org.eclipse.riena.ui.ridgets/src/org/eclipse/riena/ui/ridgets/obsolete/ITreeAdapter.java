/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.obsolete;

import java.util.List;

import org.eclipse.riena.ui.ridgets.IActionCallback;
import org.eclipse.riena.ui.ridgets.IMarkableAdapter;
import org.eclipse.riena.ui.ridgets.tree.ITreeModel;
import org.eclipse.riena.ui.ridgets.tree.ITreeModelListener;
import org.eclipse.riena.ui.ridgets.tree.ITreeNode;

/**
 * Adapter for the UI representation of a tree.
 * 
 * @author Thorsten Schenkel
 * @author Carsten Drossel
 */

/**
 * @deprecated
 */
@Deprecated
// public interface ITreeAdapter extends IMarkableAdapter, IDragnDropCapability,
// IPopupMenuCapable {
public interface ITreeAdapter extends IMarkableAdapter {

	/**
	 * Bound property name for <code>selectedTreeNode</code>
	 */
	String PROPERTY_SELECTED_TREE_NODE = "selectedTreeNode";
	/**
	 * Bound property name for <code>selectedTreeNode</code>
	 */
	String PROPERTY_SELECTED_TREE_NODE_LIST = "selectedTreeNodeList";
	/**
	 * "Property" without value. Is fired to refresh the tree.
	 */
	String PROPERTY_REFRESH_TREE = "refreshTree";
	/**
	 * Bound property name for <code>expand</code>
	 */
	String PROPERTY_EXPAND = "expand";
	/**
	 * Bound property name for <code>collapseTree</code>
	 */
	String PROPERTY_COLLAPSE_TREE = "collapseTree";
	/**
	 * Bound property name for <code>collapseTreeRecursive</code>
	 */
	String PROPERTY_COLLAPSE_TREE_RECURSIVE = "collapseTreeRecursive";
	/**
	 * Action "property" to trigger the saving of the expansion and selection
	 * state.
	 */
	String PROPERTY_SAVE_EXPANSION_AND_SELECTION_BY_VALUE = "saveExpansion";
	/**
	 * Action "property" to trigger the restoration of the expansion and
	 * selection state.
	 */
	String PROPERTY_RESTORE_EXPANSION_AND_SELECTION_BY_VALUE = "restoreExpansion";
	/**
	 * Bound property name for <code>collapseTreeRecursive</code>
	 */
	String PROPERTY_SELECTIONTYPE = "selectionType";
	/**
	 * Comment for <code>SELECTION_TYPE_SINGLE</code>
	 */
	int SELECTION_TYPE_SINGLE = 2;
	/**
	 * Comment for <code>SELECTION_TYPE_MULTIPLE</code>
	 */
	int SELECTION_TYPE_MULTIPLE = 4;

	/**
	 * Return the tree model of the property.
	 * 
	 * @return the string value of the property.
	 */
	ITreeModel getTreeModel();

	/**
	 * Adds a listener for the <code>ITreeModelEvent</code> posted after the
	 * tree changes.
	 * 
	 * @param l
	 *            - listener to add.
	 */
	void addTreeModelListener(ITreeModelListener l);

	/**
	 * Removes a listener previously added with
	 * <code>addTreeModelListener</code>.
	 * 
	 * @param l
	 *            - listener to remove.
	 */
	void removeTreeModelListener(ITreeModelListener l);

	/**
	 * get selected tree node.
	 * 
	 * @return object reference of the selected tree node.
	 */
	ITreeNode getSelection();

	/**
	 * Returns the number of nodes selected.
	 * 
	 * @return number of selected nodes.
	 */
	int getSelectionCount();

	/**
	 * Sets the number of selected nodes. <br>
	 * <b>Note: </b> This method exists only for the communication between
	 * <code>TreeSwingAdapter</code> and <code>TreeAdpater</code> (and only this
	 * direction). Do not use this method!
	 * 
	 * @param count
	 *            - number of selected nodes.
	 */
	void setSelectionCount(int count);

	/**
	 * set selected tree node.
	 * 
	 * @param node
	 *            object reference of the selected tree node.
	 */
	void setSelection(ITreeNode node);

	/**
	 * Sets the list of the selected tree nodes
	 * 
	 * @param nodes
	 *            - list of the selected nodes
	 */
	void setSelectionList(List nodes);

	/**
	 * Returns the list of the selected tree nodes
	 * 
	 * @return list of the selected nodes
	 */
	List getSelectionList();

	/**
	 * Returns a list with all the expanded pathes in the tree. <br>
	 * <b>Attention: </b> the tree paths contains the node of a SWING tree.
	 * <i>for internal use only! </i>
	 * 
	 * @return list with expandes pathes
	 */
	List getExpandedList();

	/**
	 * Sets a list with all the expanded pathes in the tree. <br>
	 * <b>Attention: </b> the tree paths contains the node of a SWING tree.
	 * <i>for internal use only! </i>
	 * 
	 * @param list
	 *            - list with expandes pathes
	 */
	void setExpandedList(List list);

	/**
	 * Clears the selection.
	 */
	void clearSelection();

	/**
	 * <b>Attention </b>: This method does not compare the tree nodes or the
	 * user objects of the nodes. It compares the string representation of the
	 * objects. So that this method works correct the string representation of
	 * the siblings must be unique.
	 * 
	 * @param node
	 *            the tree node object reference.
	 */
	void selectNode(ITreeNode node);

	/**
	 * Refresh the tree.
	 */
	void refreshTree();

	/**
	 * Refreshs the model of the tree.
	 */
	void refreshTreeModel();

	/**
	 * Collapse the whole tree. <br>
	 * The first level nodes will be still visible.
	 */
	void collapseTree();

	/**
	 * Collapse the whole tree and not only the top level nodes also all open
	 * sub trees. <br>
	 * The first level nodes will be still visible.
	 */
	void collapseTreeRecursive();

	/**
	 * Expands the given node. IMPORTANT: must not be called until after the
	 * adapter was bound since the expansion state is not stored in the adapter
	 * but only in the UI control. Calling this method on an unbound adapter
	 * will do nothing.
	 * 
	 * @see de.compeople.spirit.core.client.controller.AbstractExtendedController#onAfterBind()
	 * 
	 * @param node
	 *            - node to expand
	 */
	void expand(ITreeNode node);

	/**
	 * Saves the expansion and selection state of the tree by displayed value.
	 */
	void saveExpansionAndSelectionByValue();

	/**
	 * Attempts to restore the expansion and selection state of the tree by
	 * displayed value.
	 */
	void restoreExpansionAndSelectionByValue();

	/**
	 * Called after a double click of the left mouse button.
	 */
	void callbackDoubleClick();

	/**
	 * Sets the callback "method", that is called after a double click of the
	 * left mouse button.
	 * 
	 * @param callback
	 *            - the callback of double click; null for no action.
	 */
	void setDoubleClickCallback(IActionCallback callback);

	/**
	 * Called whenever an item in the tree has been expanded.
	 * 
	 * @param node
	 *            - node that has been expanded.
	 */
	void callbackExpand(ITreeNode node);

	/**
	 * Sets the callback "method", that is called whenever an item in the tree
	 * has been expanded. <br>
	 * The parameter of the callback method, is the node (<code>ITreeNode</code>
	 * ) that has been expanded.
	 * 
	 * @param callback
	 *            - the callback of expand; null for no action.
	 */
	void setExpandCallback(IActionCallback callback);

	/**
	 * Called whenever an item in the tree has been collapsed. <br>
	 * 
	 * @param node
	 *            - node that has been collapsed.
	 */
	void callbackCollapse(ITreeNode node);

	/**
	 * Sets the callback "method", that is called whenever an item in the tree
	 * has been collapsed. The parameter of the callback method, is the node (
	 * <code>ITreeNode</code>) that has been collapsed.
	 * 
	 * @param callback
	 *            - the callback of collapse; null for no action.
	 */
	void setCollapseCallback(IActionCallback callback);

	/**
	 * Call this to activate the method on the target bean after the selection
	 * in the tree has changed.
	 */
	void callbackSelection();

	/**
	 * @param selectionCallback
	 *            The selectionCallback to set.
	 */
	void setSelectionCallback(IActionCallback selectionCallback);

	/**
	 * Returns the selection type
	 * 
	 * @return type of selection (single or multi).
	 * 
	 */
	int getSelectionType();

	/**
	 * Sets the selection type (single or multi)
	 * 
	 * @param selectionType
	 *            - single (<code>SELECTION_TYPE_SINGLE</code>) or multi (
	 *            <code>SELECTION_TYPE_MULI</code>)
	 */
	void setSelectionType(int selectionType);

}