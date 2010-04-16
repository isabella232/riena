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
package org.eclipse.riena.example.client.navigation.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.riena.example.client.controllers.DemoTargetSubModuleController;
import org.eclipse.riena.example.client.views.DemoTargetSubModuleView;
import org.eclipse.riena.navigation.AbstractNavigationAssembler;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 *
 */
public class DemoTargetNodeAssembler extends AbstractNavigationAssembler {

	public final static String ID = "org.eclipse.riena.example.navigate.demotarget"; //$NON-NLS-1$

	private Set<String> knownTargetIds = null;

	public boolean acceptsToBuildNode(NavigationNodeId nodeId, NavigationArgument argument) {
		if (knownTargetIds == null) {
			knownTargetIds = new HashSet<String>(Arrays.asList(ID));
			knownTargetIds = Collections.unmodifiableSet(knownTargetIds);
		}

		return knownTargetIds.contains(nodeId.getTypeId());
	}

	public INavigationNode<?>[] buildNode(NavigationNodeId nodeId, NavigationArgument navigationArgument) {
		ISubModuleNode subModule = new SubModuleNode(new NavigationNodeId(ID), "Target"); //$NON-NLS-1$
		IWorkareaDefinition def = WorkareaManager.getInstance().registerDefinition(subModule,
				DemoTargetSubModuleController.class, DemoTargetSubModuleView.ID);
		def.setRequiredPreparation(true);
		return new INavigationNode<?>[] { subModule };
	}

}
