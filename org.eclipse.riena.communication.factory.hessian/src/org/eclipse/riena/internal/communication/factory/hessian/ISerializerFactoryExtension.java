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
package org.eclipse.riena.internal.communication.factory.hessian;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;

/**
 * Extension interface for defining Riena specific serializer factories.
 */
@ExtensionInterface
public interface ISerializerFactoryExtension {

	String EXTENSION_ID = "org.eclipse.riena.communication.hessian.AbstractSerializerFactory"; //$NON-NLS-1$

	AbstractRienaSerializerFactory createImplementation();

	String getImplementation();

}
