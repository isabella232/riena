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
package org.eclipse.riena.ui.ridgets;


/**
 * Ridget for a tree. The model value is an ITreeModel.
 */
public interface ITreeRidget extends IRidget, ISelectableRidget {

	/**
	 * Creates a binding between the Tree Ridget and the specified treeRoots.
	 * <p>
	 * Each tree element must have an accessor that provides a list of children
	 * (List), an accessor that provides a parent (null for the a root element)
	 * and an accessor that provides a value (Object) for each child. It is
	 * assumed that the rootElement and all children are of the same type.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * MyType[] rootElements = { root1 };
	 * treeRidget.bind(rootElements, MyType.class, &quot;children&quot;, &quot;parent&quot;, &quot;value&quot;);
	 * </pre>
	 * <p>
	 * In the example above MyType must have methods specified in the bind(...)
	 * invocation, i.e.: getChildren(), getParent() and getValue(). It is
	 * suggested that MyType follows the 'beans' convention by providing add /
	 * removePropertyChangeListener(PropertyChangeListener) methods and firing
	 * events on containment or value modifications. This will keep the tree in
	 * sync automatically. Otherwise {@link #updateFromModel()} has to be called
	 * to resync.
	 * <p>
	 * Note that invoking this method will discard any queued expand/collapse
	 * operations on the ridget.
	 * 
	 * @param treeRoots
	 *            the root elements of the tree (non-null, non-empty).
	 * @param treeElementClass
	 *            the type of the elements in the tree (i.e. for treeRoot and
	 *            all children).
	 * @param childrenAccessor
	 *            a non-null, non-empty String specifying an accessor for
	 *            obtaining a List of children from an object in the tree. For
	 *            example "children" specifies "getChildren()". The returned
	 *            children will be shown underneath their parent.
	 * @param parentAccessor
	 *            a non-null, non-empty String specifying an accessor for
	 *            abtaining the parent Object from an object in the tree. The
	 *            accessor is allowed to return null for root elements. For
	 *            example "parent" specifies "getParent()". The parents are used
	 *            when determining the correct way to expand or collapse a tree
	 *            element.
	 * @param valueAccessor
	 *            a non-null, non-empty String specifying an accessor for
	 *            obtaining an Object value from each child (example "value"
	 *            specifies "getValue()"). The returned value will be shown in
	 *            the corresponding tree node.
	 */
	void bindToModel(Object[] treeRoots, Class<? extends Object> treeElementClass, String childrenAccessor,
			String parentAccessor, String valueAccessor);

	/**
	 * Creates a binding between the Tree Ridget and the specified treeRoots.
	 * <p>
	 * Each tree element must have an accessor that provides a list of children
	 * (List), an accessor that provides a parent (null for the a root element)
	 * and an accessor that provides a value (Object) for each child. It is
	 * assumed that the rootElement and all children are of the same type.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * MyType[] rootElements = { root1 };
	 * treeRidget.bind(rootElements, MyType.class, &quot;children&quot;, &quot;parent&quot;, &quot;value&quot;, &quot;enabled&quot;, &quot;visible&quot;);
	 * </pre>
	 * <p>
	 * In the example above MyType must have the methods specified in the
	 * bind(...) invocation, i.e.: getChildren(), getParent(), getValue(),
	 * getEnabled() and getVisible(). It is suggested that MyType follows the
	 * 'beans' convention by providing add /
	 * removePropertyChangeListener(PropertyChangeListener) methods and firing
	 * events on containment or value modifications. This will keep the tree in
	 * sync automatically. Otherwise {@link #updateFromModel()} has to be called
	 * to resync.
	 * <p>
	 * Note that invoking this method will discard any queued expand/collapse
	 * operations on the ridget.
	 * 
	 * @param treeRoots
	 *            the root elements of the tree (non-null, non-empty).
	 * @param treeElementClass
	 *            the type of the elements in the tree (i.e. for treeRoot and
	 *            all children).
	 * @param childrenAccessor
	 *            a non-null, non-empty String specifying an accessor for
	 *            obtaining a List of children from an object in the tree. For
	 *            example "children" specifies "getChildren()". The returned
	 *            children will be shown underneath their parent.
	 * @param parentAccessor
	 *            a non-null, non-empty String specifying an accessor for
	 *            abtaining the parent Object from an object in the tree. The
	 *            accessor is allowed to return null for root elements. For
	 *            example "parent" specifies "getParent()". The parents are used
	 *            when determining the correct way to expand or collapse a tree
	 *            element.
	 * @param valueAccessor
	 *            a non-null, non-empty String specifying an accessor for
	 *            obtaining an Object value from each child (example "value"
	 *            specifies "getValue()"). The returned value will be shown in
	 *            the corresponding tree node.
	 * @param enablementAccessor
	 *            a String specifying an accessor for obtaining an boolean
	 *            enablement value (example "enabled" specifies "isEnabled()" or
	 *            "getEnabled()"). Nodes that are not enabled, will be colored
	 *            gray and will not be selectable. The enablementAccessor can be
	 *            null; in that case all tree nodes will be enabled.
	 * @param visibilityAccessor
	 *            a String specifying an accessor for obtaining a boolean
	 *            visible value (example "visible" specifies "isVisible()" or
	 *            "isEnabled()"). Nodes that are not visible, will not be shown
	 *            in the tree. The visibilityAccessor can be null; in that case
	 *            all tree nodes will be shown.
	 */
	void bindToModel(Object[] treeRoots, Class<? extends Object> treeElementClass, String childrenAccessor,
			String parentAccessor, String valueAccessor, String enablementAccessor, String visibilityAccessor);

	/**
	 * Creates a binding between the Tree Ridget and the specified treeRoots.
	 * <p>
	 * Each tree element must have an accessor that provides a list of children
	 * (List), an accessor that provides a parent (null for the a root element)
	 * and an accessor that provides a value (Object) for each child. It is
	 * assumed that the rootElement and all children are of the same type.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * MyType[] rootElements = { root1 };
	 * treeRidget.bind(rootElements, MyType.class, &quot;children&quot;, &quot;parent&quot;, &quot;value&quot;, &quot;enabled&quot;, &quot;visible&quot;, &quot;icon&quot;);
	 * </pre>
	 * <p>
	 * In the example above MyType must have the methods specified in the
	 * bind(...) invocation, i.e.: getChildren(), getParent() and getValue(). It
	 * is suggested that MyType follows the 'beans' convention by providing add
	 * / removePropertyChangeListener(PropertyChangeListener) methods and firing
	 * events on containment or value modifications. This will keep the tree in
	 * sync automatically. Otherwise {@link #updateFromModel()} has to be called
	 * to resync.
	 * <p>
	 * Note that invoking this method will discard any queued expand/collapse
	 * operations on the ridget.
	 * 
	 * @param treeRoots
	 *            the root elements of the tree (non-null, non-empty).
	 * @param treeElementClass
	 *            the type of the elements in the tree (i.e. for treeRoot and
	 *            all children).
	 * @param childrenAccessor
	 *            a non-null, non-empty String specifying an accessor for
	 *            obtaining a List of children from an object in the tree. For
	 *            example "children" specifies "getChildren()". The returned
	 *            children will be shown underneath their parent.
	 * @param parentAccessor
	 *            a non-null, non-empty String specifying an accessor for
	 *            abtaining the parent Object from an object in the tree. The
	 *            accessor is allowed to return null for root elements. For
	 *            example "parent" specifies "getParent()". The parents are used
	 *            when determining the correct way to expand or collapse a tree
	 *            element.
	 * @param valueAccessor
	 *            a non-null, non-empty String specifying an accessor for
	 *            obtaining an Object value from each child (example "value"
	 *            specifies "getValue()"). The returned value will be shown in
	 *            the corresponding tree node.
	 * @param enablementAccessor
	 *            a String specifying an accessor for obtaining an boolean
	 *            enablement value (example "enabled" specifies "isEnabled()" or
	 *            "getEnabled()"). Nodes that are not enabled, will be colored
	 *            gray and will not be selectable. The enablementAccessor can be
	 *            null; in that case all tree nodes will be enabled.
	 * @param visibilityAccessor
	 *            a String specifying an accessor for obtaining a boolean
	 *            visible value (example "visible" specifies "isVisible()" or
	 *            "isEnabled()"). Nodes that are not visible, will not be shown
	 *            in the tree. The visibilityAccessor can be null; in that case
	 *            all tree nodes will be shown.
	 * @param imageAccessor
	 *            a String specifying an accessor for obtaining a String value
	 *            with the key of an icon (example "icon" specifies "getIcon()"
	 *            ). Nodes that only the icons of leaves are showing and if the
	 *            accessor does not return an icon the default icon of a leaf is
	 *            showing.
	 */
	void bindToModel(Object[] treeRoots, Class<? extends Object> treeElementClass, String childrenAccessor,
			String parentAccessor, String valueAccessor, String enablementAccessor, String visibilityAccessor,
			String imageAccessor);

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
	void expandAll();

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
	void collapseAll();

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
	 * Adding the same listener several times has no effect.
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
	 *            a non-null {@link IActionListener} instance
	 * @throws RuntimeException
	 *             if listener is null
	 */
	void removeDoubleClickListener(IActionListener listener);

	/**
	 * Returns true, if the roots of the tree shall be shown and false
	 * otherwise.
	 * <p>
	 * The default setting is true.
	 */
	boolean getRootsVisible();

	/**
	 * Indicates that the roots of the tree should be shown, if the argument is
	 * true, and not be shown otherwise.
	 * 
	 * @param showRoots
	 *            true to show the roots, false otherwise
	 */
	void setRootsVisible(boolean showRoots);

}
