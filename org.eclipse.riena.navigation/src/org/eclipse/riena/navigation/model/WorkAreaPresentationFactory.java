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

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.navigation.Activator;
import org.eclipse.riena.navigation.IWorkAreaPresentationDefinition;
import org.eclipse.riena.navigation.IWorkAreaPresentationFactory;

/**
 * 
 */
public class WorkAreaPresentationFactory implements IWorkAreaPresentationFactory {

	private static final String ID = "org.eclipse.riena.navigation.WorkAreaPresentation";

	protected NodePresentationData target = null;

	public WorkAreaPresentationFactory() {
		// TODO Auto-generated constructor stub

		// instantiation of this class would populate instance variable
		// <code>webBrowserCreator</code>

		target = new NodePresentationData();
		Inject.extension(ID).into(target).andStart(Activator.getDefault().getContext());
	}

	public IWorkAreaPresentationDefinition getPresentationDefinition(String targetId) {

		if (target == null || ((NodePresentationData) target).getData().length == 0) {
			return null;
		} else {
			IWorkAreaPresentationDefinition[] data = ((NodePresentationData) target).getData();
			for (int i = 0; i < data.length; i++) {
				if (data[i].getPresentationId() != null && data[i].getPresentationId().equals(targetId)) {
					return data[i];
				}

			}
		}
		return null;

	}

	public class NodePresentationData {

		private IWorkAreaPresentationDefinition[] data;

		public void update(IWorkAreaPresentationDefinition[] data) {
			this.data = data;

		}

		public IWorkAreaPresentationDefinition[] getData() {
			return data;
		}

	}

}
