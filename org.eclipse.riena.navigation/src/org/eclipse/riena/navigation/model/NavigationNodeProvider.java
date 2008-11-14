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

import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.navigation.Activator;
import org.eclipse.riena.navigation.IGenericNavigationAssembler;
import org.eclipse.riena.navigation.IModuleGroupNodeExtension;
import org.eclipse.riena.navigation.IModuleNodeExtension;
import org.eclipse.riena.navigation.INavigationAssembler;
import org.eclipse.riena.navigation.INavigationAssemblyExtension;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeProvider;
import org.eclipse.riena.navigation.ISubModuleNodeExtension;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.osgi.service.log.LogService;

/**
 * This class provides service methods to get information provided by
 * WorkAreaPresentationDefinitions and NavigationNodePresentationDefitinios
 * identified by a given presentationID.
 */
public class NavigationNodeProvider implements INavigationNodeProvider {

	private final static Logger LOGGER = Activator.getDefault().getLogger(NavigationNodeProvider.class);

	private static final String EP_NAVIGATION_ASSEMBLIES = "org.eclipse.riena.navigation.assemblies"; //$NON-NLS-1$

	private NavigationAssembliesExtensionInjectionHelper targetNN;

	private Map<String, ISubModuleNodeExtension> id2SubModuleCache = new WeakHashMap<String, ISubModuleNodeExtension>();

	/**
	 * 
	 */
	public NavigationNodeProvider() {

		targetNN = new NavigationAssembliesExtensionInjectionHelper();
		Inject.extension(EP_NAVIGATION_ASSEMBLIES).useType(getNavigationNodeTypeDefinitonIFSafe()).into(targetNN)
				.andStart(Activator.getDefault().getContext());

	}

	private Class<? extends INavigationAssemblyExtension> getNavigationNodeTypeDefinitonIFSafe() {

		if (getNavigationNodeTypeDefinitonIF() != null && getNavigationNodeTypeDefinitonIF().isInterface()) {
			return getNavigationNodeTypeDefinitonIF();
		} else {
			return INavigationAssemblyExtension.class;
		}
	}

	public Class<? extends INavigationAssemblyExtension> getNavigationNodeTypeDefinitonIF() {

		return INavigationAssemblyExtension.class;
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeProvider#createNode(org.eclipse.riena.navigation.INavigationNode,
	 *      org.eclipse.riena.navigation.NavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	@SuppressWarnings("unchecked")
	protected INavigationNode<?> _provideNode(INavigationNode<?> sourceNode, NavigationNodeId targetId,
			NavigationArgument argument) {
		INavigationNode<?> targetNode = findNode(getRootNode(sourceNode), targetId);
		if (targetNode == null) {
			if (LOGGER.isLoggable(LogService.LOG_DEBUG)) {
				LOGGER.log(LogService.LOG_DEBUG, "createNode: " + targetId); //$NON-NLS-1$
			}
			INavigationAssemblyExtension navigationNodeTypeDefiniton = getNavigationNodeTypeDefinition(targetId);
			if (navigationNodeTypeDefiniton != null) {
				INavigationAssembler builder = navigationNodeTypeDefiniton.createNavigationAssembler();
				if (builder == null) {
					builder = createDefaultBuilder();
				}
				prepareNavigationNodeBuilder(navigationNodeTypeDefiniton, targetId, builder);
				targetNode = builder.buildNode(targetId, argument);
				INavigationNode parentNode = null;
				if (argument != null && argument.getParentNodeId() != null) {
					parentNode = _provideNode(sourceNode, argument.getParentNodeId(), null);
				} else {
					parentNode = _provideNode(sourceNode, new NavigationNodeId(navigationNodeTypeDefiniton
							.getParentTypeId()), null);
				}
				parentNode.addChild(targetNode);
			} else {
				throw new ExtensionPointFailure("NavigationNodeType not found. ID=" + targetId.getTypeId()); //$NON-NLS-1$
			}
		}
		return targetNode;
	}

	protected GenericNavigationAssembler createDefaultBuilder() {
		return new GenericNavigationAssembler();
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeProvider#createNode(org.eclipse.riena.navigation.INavigationNode,
	 *      org.eclipse.riena.navigation.NavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	public INavigationNode<?> provideNode(INavigationNode<?> sourceNode, NavigationNodeId targetId,
			NavigationArgument argument) {
		final INavigationNode<?> targetNode = _provideNode(sourceNode, targetId, argument);
		return targetNode;
	}

	/**
	 * Used to prepare the NavigationNodeBuilder in a application specific way.
	 * 
	 * @param targetId
	 * @param builder
	 */
	protected void prepareNavigationNodeBuilder(INavigationAssemblyExtension navigationNodeTypeDefiniton,
			NavigationNodeId targetId, INavigationAssembler builder) {

		if (builder instanceof IGenericNavigationAssembler) {
			// the extension interface of the navigation node definition is injected into the builder   
			((IGenericNavigationAssembler) builder).setNodeDefinition(navigationNodeTypeDefiniton);
		}
	}

	/**
	 * @param targetId
	 * @return
	 */
	public INavigationAssemblyExtension getNavigationNodeTypeDefinition(NavigationNodeId targetId) {
		if (targetNN == null || targetNN.getData().length == 0 || targetId == null) {
			return null;
		} else {
			INavigationAssemblyExtension found = null;
			for (INavigationAssemblyExtension assembly : targetNN.getData()) {
				if (assembly.getTypeId() != null && assembly.getTypeId().equals(targetId.getTypeId())) {
					return assembly;
				}
				if (assembly.getModuleGroupNode() != null) {
					found = getNavigationNodeTypeDefinition(assembly, assembly.getModuleGroupNode(), targetId
							.getTypeId());
					if (found != null) {
						return found;
					}
				}
				if (assembly.getModuleNode() != null) {
					found = getNavigationNodeTypeDefinition(assembly, assembly.getModuleNode(), targetId.getTypeId());
					if (found != null) {
						return found;
					}
				}
				if (assembly.getSubModuleNode() != null) {
					found = getNavigationNodeTypeDefinition(assembly, assembly.getSubModuleNode(), targetId.getTypeId());
					if (found != null) {
						return found;
					}
				}
			}
		}
		return null;
	}

	private INavigationAssemblyExtension getNavigationNodeTypeDefinition(INavigationAssemblyExtension assembly,
			IModuleGroupNodeExtension mg, String targetId) {

		if (mg.getTypeId() != null && mg.getTypeId().equals(targetId)) {
			return assembly;
		} else {
			for (IModuleNodeExtension m : mg.getModuleNodes()) {
				INavigationAssemblyExtension found = getNavigationNodeTypeDefinition(assembly, m, targetId);
				if (found != null) {
					return found;
				}
			}
		}
		return null;
	}

	private INavigationAssemblyExtension getNavigationNodeTypeDefinition(INavigationAssemblyExtension assembly,
			IModuleNodeExtension module, String targetId) {

		if (module.getTypeId() != null && module.getTypeId().equals(targetId)) {
			return assembly;
		} else {
			for (ISubModuleNodeExtension submodule : module.getSubModuleNodes()) {
				INavigationAssemblyExtension found = getNavigationNodeTypeDefinition(assembly, submodule, targetId);
				if (found != null) {
					return found;
				}
			}
		}
		return null;
	}

	private INavigationAssemblyExtension getNavigationNodeTypeDefinition(INavigationAssemblyExtension assembly,
			ISubModuleNodeExtension submodule, String targetId) {

		if (submodule.getTypeId() != null && submodule.getTypeId().equals(targetId)) {
			return assembly;
		} else {
			for (ISubModuleNodeExtension nestedSubmodule : submodule.getSubModuleNodes()) {
				INavigationAssemblyExtension found = getNavigationNodeTypeDefinition(assembly, nestedSubmodule,
						targetId);
				if (found != null) {
					return found;
				}
			}
		}
		return null;
	}

	protected ISubModuleNodeExtension getSubModuleNodeDefinition(NavigationNodeId targetId) {
		return id2SubModuleCache.get(targetId.getTypeId());
	}

	/**
	 * @param node
	 * @return
	 */
	protected INavigationNode<?> getRootNode(INavigationNode<?> node) {
		if (node.getParent() == null) {
			return node;
		}
		return getRootNode(node.getParent());
	}

	/**
	 * @param node
	 * @param targetId
	 * @return
	 */
	protected INavigationNode<?> findNode(INavigationNode<?> node, NavigationNodeId targetId) {
		if (targetId == null) {
			return null;
		}
		if (targetId.equals(node.getNodeId())) {
			return node;
		}
		for (INavigationNode<?> child : node.getChildren()) {
			INavigationNode<?> foundNode = findNode(child, targetId);
			if (foundNode != null) {
				return foundNode;
			}
		}
		return null;
	}

	public void register(String subModuleNodeid, ISubModuleNodeExtension definition) {
		id2SubModuleCache.put(subModuleNodeid, definition);
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeProvider#cleanUp()
	 */
	public void cleanUp() {
		// TODO: implement, does nothing special yet
	}

	public class NavigationAssembliesExtensionInjectionHelper {
		private INavigationAssemblyExtension[] data;

		public void update(INavigationAssemblyExtension[] data) {
			this.data = data.clone();

		}

		public INavigationAssemblyExtension[] getData() {
			return data.clone();
		}
	}
}
