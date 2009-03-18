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
 * Delegate for {@link IMasterDetailsRidget}, responsible for driving the
 * details area:
 * <ul>
 * <li>configuring the ridgets for the details area</li>
 * <li></li>
 * </ul>
 * <p>
 * Developers using a {@link IMasterDetailsRidget} must introduce an appropriate
 * implementation of this interface to the ridget, by invoking
 * {@link IMasterDetailsRidget#setDelegate(IMasterDetailsDelegate)}.
 */
public interface IMasterDetailsDelegate {

	/**
	 * This method is called once, after all ridgets are injected.
	 * <p>
	 * Implementors must use this method to bind the ridgets from the details
	 * area to the appropriate data. The recommended approach is to create an
	 * instance of the working copy object and bind that instance to the
	 * ridgets. The bound instance should remain the same over the lifetime of
	 * the delegate (see {@link #getWorkingCopy()}). It should be updated as
	 * necessary (see {@link #updateDetails(IRidgetContainer)}).
	 * 
	 * @param container
	 *            an IRidgetContainer container that holds the ridgets available
	 *            to the delegate. Invoke {@code container#getRidget(String id)}
	 *            to obtain a reference to a ridget with that id. Never null.
	 */
	void configureRidgets(IRidgetContainer container);

	/**
	 * Creates a workingCopy. The workingCopy object represents the model behind
	 * the detail fields. It is always an instance of the bean class of the
	 * master model.
	 * 
	 * @return an Object instance; never null.
	 */
	Object createWorkingCopy();

	/**
	 * Copies the content of one given bean instance into another given bean
	 * instance.
	 * 
	 * @param source
	 *            The source bean. If null, a fresh instance obtained from
	 *            {@link #createWorkingCopy()} will be used as the source.
	 * @param target
	 *            The target bean. If null, a fresh instance obtained from
	 *            {@link #createWorkingCopy()} will be used as the target
	 * @return returns the target bean; never null.
	 */
	Object copyBean(Object source, Object target);

	/**
	 * Returns the workingCopy object. The workingCopyObject represents the
	 * model behind the detail fields.It is always an instance of the bean class
	 * of the master model.
	 * <p>
	 * It is recommended that the instance returned by this method stays the
	 * same over the lifetime of the delegate (i.e. always return the same
	 * instance).
	 * 
	 * @return an Object; never null
	 */
	Object getWorkingCopy();

	/**
	 * Returns true, if there there is a difference between the two beans, with
	 * respect to the data that is editable in the details area. Returns false
	 * is both beans are the equal, with respect to that data.
	 * <p>
	 * The return value determines the enablement state of the 'update' button
	 * in the {@link IMasterDetailsRidget}.
	 * <p>
	 * The minimal recommended implementation of this method is:
	 * 
	 * <pre>
	 * public boolean isChanged(Object source, Object target) {
	 * 	return true;
	 * }
	 * 
	 * </pre>
	 * 
	 * @param source
	 *            the source bean; never null. Holds the original values
	 * @param target
	 *            the target bean; never null. Holds the latest values from the
	 *            detail area.
	 */
	boolean isChanged(Object source, Object target);

	/**
	 * Returns null, if the data in details area (i.e. working copy) is valid.
	 * <p>
	 * The minimal recommended implementation of this method is:
	 * 
	 * <pre>
	 * public String isValid() {
	 * 	return null;
	 * }
	 * </pre>
	 * 
	 * @param container
	 *            an IRidgetContainer container that holds the ridgets available
	 *            to the delegate. Invoke {@code container#getRidget(String id)}
	 *            to obtain a reference to a ridget with that id. Never null.
	 * @return null if the data is valid; an error message if the data is
	 *         invalid
	 */
	String isValid(IRidgetContainer container);

	/**
	 * Updates all details from the model.
	 * <p>
	 * Typically this is called when the selection in the master/details ridget
	 * has changed. The {@link IMasterDetailsRidget} will first update the
	 * working copy by invoking {@code copyBean(selection, getWorkingCopy())}
	 * and then invoke this method.
	 * <p>
	 * The minimal recommended implementation of this method is:
	 * 
	 * <pre>
	 * public void updateDetails(IRidgetContainer container) {
	 * 	for (IRidget ridget : container.getRidgets()) {
	 * 		ridget.updateFromModel();
	 * 	}
	 * }
	 * </pre>
	 * 
	 * @param container
	 *            an IRidgetContainer container that holds the ridgets available
	 *            to the delegate. Invoke {@code container#getRidget(String id)}
	 *            to obtain a reference to a ridget with that id. Never null.
	 */
	void updateDetails(IRidgetContainer container);

}
