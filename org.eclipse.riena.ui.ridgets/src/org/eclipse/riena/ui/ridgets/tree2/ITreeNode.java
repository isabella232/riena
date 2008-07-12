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
package org.eclipse.riena.ui.ridgets.tree2;

import java.io.Serializable;
import java.util.List;

import org.eclipse.riena.ui.ridgets.ITreeRidget;

/**
 * Defines the requirements for an object that can be used as a tree node
 * together with a {@link ITreeRidget}.
 * <p>
 * It is recommended to use {@link TreeNode}. This is a default implementation
 * of this interface that wraps a generic value Object.
 * <p>
 * You are <b>not required</b> to provide your own implementation. You can bind
 * your value objects directly to the {@link ITreeRidget}, as long as those
 * objects satisfy the following requirements:
 * <ol>
 * <li>they must all have the same type</li>
 * <li>they must be beans (i.e. support for change listenrs and change
 * notification)</li>
 * <li>they must provide an read-only accessor for a value (i.e. public Object
 * getXXXX)</li>
 * <li>they must provide read/write accessor for a List of children (i.e. public
 * List&lt;Object&gt; get/setXXXX)</li>
 * <li>(optional) they must be able to provide their parent instance (i.e.
 * public Object getXXXXX), which may be null if this object is a tree-root
 * </ol>
 * 
 * @see ITreeRidget#bindToModel(Object, Class, String, String)
 */
public interface ITreeNode extends Serializable {

	/**
	 * Property name of the value property ("value").
	 * 
	 * @see #getValue()
	 * @see #setValue(Object)
	 */
	String PROP_VALUE = "value"; //$NON-NLS-1$

	/**
	 * Property name of the children property ("children").
	 * 
	 * @see #getChildren()
	 * @see #setChildren(List)
	 */
	String PROP_CHILDREN = "children"; //$NON-NLS-1$

	/**
	 * Returns the children objects of this node.
	 * <p>
	 * The returned list must be a copy, to prevent accidental external
	 * modification of this node's state.
	 * 
	 * @return a List of ITreeNodes; never null; may be empty
	 */
	public List<ITreeNode> getChildren();

	/**
	 * Return the value object for this node
	 * 
	 * @return an Object instance (may be null)
	 */
	public Object getValue();

	/**
	 * Set the children object for this node.
	 * <p>
	 * The given list should be copied, to prevent accidental external
	 * modification of this node's state.
	 * 
	 * @param children
	 *            a List of ITreeNodes; may be null.
	 */
	public void setChildren(List<ITreeNode> children);

	/**
	 * Set teh value object for this node
	 * 
	 * @param newValue
	 *            an Object instance (may be null)
	 */
	public void setValue(Object newValue);

	/**
	 * Returns the parent tree node <code>ITreeNode</code> of the receiver.
	 * 
	 * @return parent an ITreeNode instance; may be null if this node is a root
	 */
	ITreeNode getParent();
} // end interface
