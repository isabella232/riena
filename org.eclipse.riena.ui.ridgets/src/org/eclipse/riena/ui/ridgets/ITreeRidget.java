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
package org.eclipse.riena.ui.ridgets;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.riena.ui.ridgets.tree.IObservableTreeModel;
import org.eclipse.riena.ui.ridgets.tree.ITreeNode;

/**
 * Ridget for a tree. The model value is an ITreeModel.
 */
public interface ITreeRidget extends IRidget, ISelectableRidget {

	/**
	 * @return The observable value of the Ridget. Can be used as target value
	 *         in a custom binding with a model value.
	 * 
	 * @see DataBindingContext#bindValue(IObservableValue, IObservableValue,
	 *      org.eclipse.core.databinding.UpdateValueStrategy,
	 *      org.eclipse.core.databinding.UpdateValueStrategy)
	 */
	IObservableTreeModel getRidgetObservable();

	/**
	 * Creates a default binding between the Ridget value and the specified
	 * model value. The UpdateValueStrategy will be POLICY_UPDATE to the model
	 * value (automatic update) and POLICY_ON_REQUEST from the model value.
	 * 
	 * @see #updateFromModel()
	 * 
	 * @param observableValue
	 *            The model value.
	 * 
	 * @deprecated see {@link #bindToModel(Object, Class, String, String)}
	 */
	void bindToModel(IObservableTreeModel observableTreeModel);

	/**
	 * Creates a default binding between the Tree Ridget and the specified
	 * rootElement value. The UpdateValueStrategy will be POLICY_UPDATE to the
	 * model value (automatic update) and POLICY_ON_REQUEST from the model
	 * value.
	 * <p>
	 * The rootElement must have an accessor that provides a list of children
	 * (List) and an accessor that provides a value (Object) for each child. It
	 * is assumed that the rootElement and all children are of the same type.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * AnyBean rootElement;
	 * // AnyBean has getChildren() and getValue() methods
	 * treeRidget.bind(rootElement, AnyBean.class, &quot;children&quot;, &quot;value&quot;);
	 * </pre>
	 * <p>
	 * Note that invoking this method will discard any queued expand/collapse
	 * operations on the ridget.
	 * 
	 * @param treeRoot
	 *            an Object the root of the tree (non-null). Note that the
	 *            children of the treeRoot will be shown in level-0 of the tree
	 *            and the treeRoot itself is not shown.
	 * @param treeElementClass
	 *            the type of the elements in the tree (i.e. treeRoot and all
	 *            children).
	 * @param childrenAccessor
	 *            a non-null, non-empty String specifying an accessor for
	 *            obtaining a List of children from the rootElementClass
	 *            (example "children" specifies "getChildren()"). The returned
	 *            children will be shown underneath their parent.
	 * @param valueAccessor
	 *            a non-null, non-empty String specifying an accessor for
	 *            obtaining an Object value from each child (example "value"
	 *            specifies "getValue()"). The returned value will be shown in
	 *            the corresponding tree node.
	 */
	void bindToModel(Object treeRoot, Class<? extends Object> treeElementClass, String childrenAccessor,
			String valueAccessor);

	/**
	 * Expands all nodes of the tree based on the current ITreeModel value if
	 * the Ridget is currently bound to a tree UI-control.
	 * <p>
	 * If the UI-control is null when this method is invoked, the expansion will
	 * be queued and applied to the ridget at a later time. Re-binding the
	 * ridget to another model will cancel any queued expand/collapse
	 * operations.
	 * 
	 * @see #bindToModel(Object, Class, String, String)
	 * @see IRidget#getUIControl()
	 */
	void expandTree();

	/**
	 * Collapses all nodes of the tree if the Ridget is currently bound to a
	 * tree UI-control.
	 * <p>
	 * If the UI-control is null when this method is invoked, the collapsing
	 * will be queued and applied to the ridget at a later time. Re-binding the
	 * ridget to another model will cancel any queued expand/collapse
	 * operations.
	 * 
	 * @see #bindToModel(Object, Class, String, String)
	 * @see IRidget#getUIControl()
	 */
	void collapseTree();

	/**
	 * @deprecated see {@link #expand(Object)}
	 */
	void expand(ITreeNode node);

	/**
	 * @deprecated see {@link #collapse(Object)}
	 */
	void collapse(ITreeNode node);

	/**
	 * Expands a node if it is part of the data-model currently bound to the
	 * tree. If the node is not part of the data-model nothing will happen.
	 * <p>
	 * If the UI-control is null when this method is invoked, the expansion will
	 * be queued and applied to the ridget at a later time. Re-binding the
	 * ridget to another model will cancel any queued expand/collapse
	 * operations.
	 * 
	 * @param node
	 *            The node to expand (non-null).
	 * 
	 * @see #bindToModel(Object, Class, String, String)
	 * @see IRidget#getUIControl()
	 */
	void expand(Object element);

	/**
	 * Collapses a node if it is part of the data-model currently bound to the
	 * tree. If the node is not part of the model nothing will happen.
	 * <p>
	 * If the UI-control is null when this method is invoked, the collapsing
	 * will be queued and applied to the ridget at a later time. Re-binding the
	 * ridget to another model will cancel any queued expand/collapse
	 * operations.
	 * 
	 * @param node
	 *            The node to collapse.
	 * 
	 * @see #bindToModel(Object, Class, String, String)
	 * @see IRidget#getUIControl()
	 */
	void collapse(Object element);

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the bound control is double-clicked.
	 * <p>
	 * The same listener may be added more than once, and will be called as many
	 * times as it is added.
	 * 
	 * @param listener
	 *            a non-null {@link IActionListener} instance
	 * @throws RuntimeException
	 *             if listener is null
	 */
	void addDoubleClickListener(IActionListener listener);

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the bound control is double-clicked.
	 * 
	 * @param listener
	 *            an {@link IActionListener} instance
	 */
	void removeDoubleClickListener(IActionListener listener);

}
