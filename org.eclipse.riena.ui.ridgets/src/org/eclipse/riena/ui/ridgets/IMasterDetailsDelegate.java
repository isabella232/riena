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
 * TODO [ev] docs
 */
public interface IMasterDetailsDelegate {

	/**
	 * This method is called after all ridgets are injected.
	 * 
	 * @param container
	 *            TODO [ev] docs
	 */
	void configureRidgets(IRidgetContainer container);

	/**
	 * Creates a workingCopyObject. The workingCopyObject represents the model
	 * behind the detail fields.Its always an instance of the bean class of the
	 * master model.
	 * 
	 * @return an Object instance; never null.
	 */
	Object createWorkingCopyObject();

	/**
	 * Copies the content of one given bean instance into another given bean
	 * instance.
	 * 
	 * 
	 * @param source
	 *            The source bean. If null, a fresh instance obtained from
	 *            {@link #createWorkingCopyObject()} will be used as the source.
	 * @param target
	 *            The target bean. If null, a fresh instance obtained from
	 *            {@link #createWorkingCopyObject()} will be used as the target
	 * @return returns the target bean; never null.
	 */
	Object copyBean(Object source, Object target);

	/**
	 * Returns the workingCopy object. The workingCopyObject represents the
	 * model behind the detail fields.Its always an instance of the bean class
	 * of the master model.
	 * 
	 * @return an Object; never null
	 */
	Object getWorkingCopy();

	//	/**
	//	 * States whether the input is valid. For example checks if a textfield
	//	 * should contain digits or not.
	//	 * 
	//	 * @return true if input is valid
	//	 */
	//	boolean isInputValid();

	/**
	 * Updates all details from the model. Typically an implementation calls
	 * updateFromModel for every given detail ridget.
	 * 
	 * @param IRidgetContainer
	 *            container TODO [ev] docs
	 */
	void updateDetails(IRidgetContainer container);

}
