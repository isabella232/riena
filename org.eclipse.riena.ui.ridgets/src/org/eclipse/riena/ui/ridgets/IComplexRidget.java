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
package org.eclipse.riena.ui.ridgets;


/**
 * A Ridget that is bound to an IComplexComponent. The implementation of the
 * IComplexRidget must have a property for each UI-control of the
 * IComplexComponent. The type of the property must be a Ridget class matching
 * the type of the UI control.
 * 
 * @see IComplexComponent
 */
public interface IComplexRidget extends IRidget, IRidgetContainer {
}
