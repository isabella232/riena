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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.navigation.IPresentationDefinition;

/**
 *
 */
public abstract class AbstractWorkAreaPresentationFactory<E extends IPresentationDefinition> extends
		AbstractDefinitionInjector<E> {

	public E getPresentationDefinition(String targetId) {

		if (target == null || target.getData().length == 0) {
			return null;
		} else {
			E[] data = target.getData();
			for (int i = 0; i < data.length; i++) {
				if (data[i].getPresentationId() != null && data[i].getPresentationId().equals(targetId)) {
					return data[i];
				}

			}
		}
		return null;

	}

}
