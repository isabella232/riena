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
import org.eclipse.riena.navigation.ISubApplicationNodeExtension;
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

	private Map<String, INavigationAssemblyExtension> assemblyId2AssemblyCache = new WeakHashMap<String, INavigationAssemblyExtension>();
	private Map<String, INavigationAssemblyExtension> nodeId2AssemblyCache = new WeakHashMap<String, INavigationAssemblyExtension>();
	private Map<String, ISubApplicationNodeExtension> nodeId2SubApplicationCache = new WeakHashMap<String, ISubApplicationNodeExtension>();
	private Map<String, IModuleGroupNodeExtension> nodeId2ModuleGroupCache = new WeakHashMap<String, IModuleGroupNodeExtension>();
	private Map<String, IModuleNodeExtension> nodeId2ModuleCache = new WeakHashMap<String, IModuleNodeExtension>();
	private Map<String, ISubModuleNodeExtension> nodeId2SubModuleCache = new WeakHashMap<String, ISubModuleNodeExtension>();

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
			INavigationAssemblyExtension assembly = getAssembly(targetId);
			if (assembly != null) {
				INavigationAssembler builder = assembly.createNavigationAssembler();
				if (builder == null) {
					builder = createDefaultBuilder();
				}
				prepareNavigationNodeBuilder(assembly, targetId, builder);
				targetNode = builder.buildNode(targetId, argument);
				INavigationNode parentNode = null;
				if (argument != null && argument.getParentNodeId() != null) {
					parentNode = _provideNode(sourceNode, argument.getParentNodeId(), null);
				} else {
					parentNode = _provideNode(sourceNode, new NavigationNodeId(assembly.getParentTypeId()), null);
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
	public INavigationAssemblyExtension getAssembly(NavigationNodeId targetId) {

		if (targetNN == null || targetId == null || targetId.getTypeId() == null) {
			return null;
		} else {
			INavigationAssemblyExtension assembly = getAssemblyByNodeId(targetId);
			if (assembly != null) {
				return assembly;
			} else {
				return getAssemblyByAssemblyId(targetId.getTypeId());
			}
		}
	}

	protected INavigationAssemblyExtension getAssemblyByAssemblyId(String assemblyId) {
		return assemblyId2AssemblyCache.get(assemblyId);
	}

	protected INavigationAssemblyExtension getAssemblyByNodeId(NavigationNodeId targetId) {
		return nodeId2AssemblyCache.get(targetId.getTypeId());
	}

	protected ISubApplicationNodeExtension getSubApplicationNodeDefinition(NavigationNodeId targetId) {
		return nodeId2SubApplicationCache.get(targetId.getTypeId());
	}

	protected IModuleGroupNodeExtension getModuleGroupNodeDefinition(NavigationNodeId targetId) {
		return nodeId2ModuleGroupCache.get(targetId.getTypeId());
	}

	protected IModuleNodeExtension getModuleNodeDefinition(NavigationNodeId targetId) {
		return nodeId2ModuleCache.get(targetId.getTypeId());
	}

	protected ISubModuleNodeExtension getSubModuleNodeDefinition(NavigationNodeId targetId) {
		return nodeId2SubModuleCache.get(targetId.getTypeId());
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

	public void register(INavigationAssemblyExtension assembly) {

		assemblyId2AssemblyCache.put(assembly.getTypeId(), assembly);
		// TODO register for parent?
		if (assembly.getSubApplicationNode() != null) {
			register(assembly.getSubApplicationNode(), assembly);
		}
		if (assembly.getModuleGroupNode() != null) {
			register(assembly.getModuleGroupNode(), assembly);
		}
		if (assembly.getModuleNode() != null) {
			register(assembly.getModuleNode(), assembly);
		}
		if (assembly.getSubModuleNode() != null) {
			register(assembly.getSubModuleNode(), assembly);
		}
	}

	public void register(ISubApplicationNodeExtension subapplication, INavigationAssemblyExtension assembly) {

		if (subapplication.getTypeId() != null) {
			nodeId2AssemblyCache.put(subapplication.getTypeId(), assembly);
			nodeId2SubApplicationCache.put(subapplication.getTypeId(), subapplication);
		}
		for (IModuleGroupNodeExtension group : subapplication.getModuleGroupNodes()) {
			register(group, assembly);
		}
	}

	public void register(IModuleGroupNodeExtension group, INavigationAssemblyExtension assembly) {

		if (group.getTypeId() != null) {
			nodeId2AssemblyCache.put(group.getTypeId(), assembly);
			nodeId2ModuleGroupCache.put(group.getTypeId(), group);
		}
		for (IModuleNodeExtension module : group.getModuleNodes()) {
			register(module, assembly);
		}
	}

	public void register(IModuleNodeExtension module, INavigationAssemblyExtension assembly) {

		if (module.getTypeId() != null) {
			nodeId2AssemblyCache.put(module.getTypeId(), assembly);
			nodeId2ModuleCache.put(module.getTypeId(), module);
		}
		for (ISubModuleNodeExtension submodule : module.getSubModuleNodes()) {
			register(submodule, assembly);
		}
	}

	public void register(ISubModuleNodeExtension submodule, INavigationAssemblyExtension assembly) {

		if (submodule.getTypeId() != null) {
			nodeId2AssemblyCache.put(submodule.getTypeId(), assembly);
			nodeId2SubModuleCache.put(submodule.getTypeId(), submodule);
		}
		for (ISubModuleNodeExtension nestedSubmodule : submodule.getSubModuleNodes()) {
			register(nestedSubmodule, assembly);
		}
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

			//			assemblyId2AssemblyCache.clear();
			//			nodeId2AssemblyCache.clear();
			//			nodeId2ModuleGroupCache.clear();
			//			nodeId2ModuleCache.clear();
			//			nodeId2SubModuleCache.clear();

			for (INavigationAssemblyExtension assembly : data) {
				register(assembly);
			}
		}

		public INavigationAssemblyExtension[] getData() {
			return data.clone();
		}
	}
}
