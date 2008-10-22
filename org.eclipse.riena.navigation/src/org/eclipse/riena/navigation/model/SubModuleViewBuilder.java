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

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.navigation.Activator;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleExtension;
import org.eclipse.riena.navigation.ISubModuleViewBuilder;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.ui.ridgets.controller.IController;

/**
 *
 */
public class SubModuleViewBuilder implements ISubModuleViewBuilder {
	private final static Logger LOGGER = Activator.getDefault().getLogger(NavigationNodeProvider.class.getName());
	private static final String EP_SUBMODULETYPE = "org.eclipse.riena.navigation.subModule"; //$NON-NLS-1$
	private SubModuleExtensionInjectionHelper targetSM;

	/**
	 * 
	 */
	public SubModuleViewBuilder() {

		targetSM = new SubModuleExtensionInjectionHelper();
		Inject.extension(EP_SUBMODULETYPE).useType(getSubModuleTypeDefinitionIFSafe()).into(targetSM).andStart(
				Activator.getDefault().getContext());

	}

	private Class<? extends ISubModuleExtension> getSubModuleTypeDefinitionIFSafe() {

		if (getSubModuleTypeDefinitionIF() != null && getSubModuleTypeDefinitionIF().isInterface()) {
			return getSubModuleTypeDefinitionIF();
		} else {
			return ISubModuleExtension.class;
		}
	}

	/**
	 * @param targetId
	 * @return
	 */
	protected ISubModuleExtension getSubModuleTypeDefinition(String targetId) {
		if (targetSM == null || targetSM.getData().length == 0) {
			return null;
		} else {
			ISubModuleExtension[] data = targetSM.getData();
			for (int i = 0; i < data.length; i++) {
				if (data[i].getTypeId() != null && data[i].getTypeId().equals(targetId)) {
					return data[i];
				}
			}
		}
		return null;
	}

	/**
	 * This is the basic SWT implementation from Riena. It returns the matching
	 * view id for the given navigationNodeId
	 * 
	 * @see org.eclipse.riena.navigation.INavigationNodeProvider#createView
	 *      (org.eclipse.riena.navigation.NavigationNodeId)
	 */
	public Object provideView(NavigationNodeId nodeId) {
		ISubModuleExtension subModuleTypeDefinition = getSubModuleTypeDefinition(nodeId.getTypeId());
		if (subModuleTypeDefinition != null) {
			return subModuleTypeDefinition.getView();
		} else {
			throw new ExtensionPointFailure("SubModuleType not found. ID=" + nodeId.getTypeId()); //$NON-NLS-1$
		}
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeProvider#provideController(org.eclipse.riena.navigation.INavigationNode)
	 */
	public IController provideController(INavigationNode<?> node) {
		ISubModuleExtension subModuleTypeDefinition = getSubModuleTypeDefinition(node.getNodeId().getTypeId());
		IController controller = null;

		if (subModuleTypeDefinition != null) {
			controller = subModuleTypeDefinition.createController();
		}

		return controller;
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeProvider#isViewShared(org.eclipse.riena.navigation.NavigationNodeId)
	 */
	public boolean isViewShared(NavigationNodeId targetId) {
		ISubModuleExtension subModuleTypeDefinition = getSubModuleTypeDefinition(targetId.getTypeId());

		if (subModuleTypeDefinition != null) {
			return subModuleTypeDefinition.isShared();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.IPresentationProviderService#
	 * getSubModuleTypeDefitinions()
	 */
	public ISubModuleExtension[] getSubModuleTypeDefinitions() {
		if (targetSM != null) {
			return targetSM.getData();
		}
		return null;
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeProvider#cleanUp()
	 */
	public void cleanUp() {
		// TODO: implement, does nothing special yet
	}

	public Class<? extends ISubModuleExtension> getSubModuleTypeDefinitionIF() {

		return ISubModuleExtension.class;
	}

	public class SubModuleExtensionInjectionHelper {
		private ISubModuleExtension[] data;

		public void update(ISubModuleExtension[] data) {
			this.data = data.clone();

		}

		public ISubModuleExtension[] getData() {
			return data.clone();
		}
	}

}
