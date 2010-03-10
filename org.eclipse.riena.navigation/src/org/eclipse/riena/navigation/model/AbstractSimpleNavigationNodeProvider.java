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
package org.eclipse.riena.navigation.model;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.navigation.Activator;
import org.eclipse.riena.navigation.IAssemblerProvider;
import org.eclipse.riena.navigation.IGenericNavigationAssembler;
import org.eclipse.riena.navigation.INavigationAssembler;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeProvider;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.NodePositioner;
import org.eclipse.riena.navigation.StartupNodeInfo;
import org.eclipse.riena.navigation.StartupNodeInfo.Level;
import org.eclipse.riena.navigation.extension.ICommonNavigationAssemblyExtension;
import org.eclipse.riena.navigation.extension.INavigationAssembly2Extension;
import org.eclipse.riena.navigation.extension.INode2Extension;

/**
 * This class provides navigation nodes that are defined by assemlies2
 * extensions.
 */
public abstract class AbstractSimpleNavigationNodeProvider implements INavigationNodeProvider, IAssemblerProvider {

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(),
			AbstractSimpleNavigationNodeProvider.class);
	private static Random random = null;

	private final Map<String, INavigationAssembler> assemblyId2AssemblerCache = new HashMap<String, INavigationAssembler>();

	/**
	 * {@inheritDoc}
	 */
	public List<StartupNodeInfo> getSortedStartupNodeInfos() {
		List<StartupNodeInfo> startups = new ArrayList<StartupNodeInfo>();

		for (INavigationAssembler assembler : getNavigationAssemblers()) {
			if (assembler.getStartOrder() > 0) {
				StartupNodeInfo startupNodeInfo = createStartupSortable(assembler, assembler.getStartOrder());
				if (startupNodeInfo != null) {
					startups.add(startupNodeInfo);
				}
			}
		}
		Collections.sort(startups);
		return startups;
	}

	private StartupNodeInfo createStartupSortable(final INavigationAssembler assembler, final Integer sequence) {

		INavigationAssembly2Extension assembly = assembler.getAssembly();
		if (assembly == null) {
			return null;
		}

		String id = getTypeId(assembly.getSubApplications());
		if (id != null) {
			return new StartupNodeInfo(Level.SUBAPPLICATION, sequence, id);
		}
		id = getTypeId(assembly.getModuleGroups());
		if (id != null) {
			return new StartupNodeInfo(Level.MODULEGROUP, sequence, id);
		}
		id = getTypeId(assembly.getModules());
		if (id != null) {
			return new StartupNodeInfo(Level.MODULE, sequence, id);
		}
		id = getTypeId(assembly.getSubModules());
		if (id != null) {
			return new StartupNodeInfo(Level.SUBMODULE, sequence, id);
		}

		id = assembler.getId();
		Assert.isNotNull(assembly.getNavigationAssembler(), "Assembly '" + id //$NON-NLS-1$
				+ "' must have an assembler specified since no immediate child has a typeId."); //$NON-NLS-1$
		if (id != null) {
			return new StartupNodeInfo(Level.CUSTOM, sequence, id);
		}

		return null;

	}

	private String getTypeId(INode2Extension[] extensions) {
		if ((extensions != null) && (extensions.length > 0)) {
			return extensions[0].getNodeId();
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public INavigationNode<?> provideNode(INavigationNode<?> sourceNode, NavigationNodeId targetId,
			NavigationArgument argument) {
		return provideNodeHook(sourceNode, targetId, argument);
	}

	/**
	 * Returns a navigationNode identified by the given navigationNodeId. The
	 * node is created if it not yet exists.
	 * 
	 * @param sourceNode
	 *            an existing node in the navigation model
	 * @param targetId
	 *            the ID of the target node
	 * @param argument
	 *            contains information passed used for providing the target node
	 * @return target node
	 */
	@SuppressWarnings("rawtypes")
	protected INavigationNode<?> provideNodeHook(INavigationNode<?> sourceNode, NavigationNodeId targetId,
			NavigationArgument argument) {

		INavigationNode<?> targetNode = findNode(getRootNode(sourceNode), targetId);
		if (targetNode == null) {
			if (LOGGER.isLoggable(LogService.LOG_DEBUG)) {
				LOGGER.log(LogService.LOG_DEBUG, "createNode: " + targetId); //$NON-NLS-1$
			}

			INavigationAssembler assembler = getNavigationAssembler(targetId, argument);
			if (assembler != null) {
				NavigationNodeId parentTypeId = getParentTypeId(argument, assembler);
				// Call of findNode() on the result of method provideNodeHook() fixes problem for the case when the result 
				// is not the node with typeId parentTypeId but one of the nodes parents (i.e. when the nodes assembler also 
				// builds some of its parent nodes). 
				INavigationNode parentNode = findNode(provideNodeHook(sourceNode, parentTypeId, null), parentTypeId);
				prepareNavigationAssembler(targetId, assembler, parentNode);
				INavigationNode<?>[] targetNodes = assembler.buildNode(targetId, argument);
				if ((targetNodes != null) && (targetNodes.length > 0)) {
					NodePositioner nodePositioner = argument != null ? argument.getNodePositioner()
							: NodePositioner.ADD_END;
					for (INavigationNode<?> node : targetNodes) {
						storeNavigationArgument(node, argument);
						nodePositioner.addChildToParent(parentNode, node);
						if (node.getNodeId() == null && assembler.getId().equals(targetId.getTypeId())) {
							node.setNodeId(targetId);
						}
					}
					targetNode = targetNodes[0];
				}

			} else {
				throw new ExtensionPointFailure("No assembler found for ID=" + targetId.getTypeId()); //$NON-NLS-1$
			}
		} else {
			storeNavigationArgument(targetNode, argument);
		}
		if (argument != null) {
			if (argument.isPrepareAll()) {
				prepareAll(targetNode);
			}
		}

		return targetNode;
	}

	/**
	 * Stores the given argument in the context of the given node
	 * 
	 * @param targetNode
	 *            target node
	 * @param argument
	 *            contains information passed used for providing the target node
	 */
	private void storeNavigationArgument(INavigationNode<?> targetNode, NavigationArgument argument) {
		if (argument != null) {
			targetNode.setContext(NavigationArgument.CONTEXTKEY_ARGUMENT, argument);
		}
	}

	/**
	 * Creates the assembler ({@link INavigationAssembler}) for the given
	 * assembly and registers it.
	 * 
	 * @param assembly
	 *            assembly to register
	 */
	public void register(INavigationAssembly2Extension assembly) {

		String assemblyId = assembly.getId();

		if (assemblyId == null) {
			if (random == null) {
				try {
					random = SecureRandom.getInstance("SHA1PRNG"); //$NON-NLS-1$
				} catch (NoSuchAlgorithmException e) {
					random = new Random(System.currentTimeMillis());
				}
			}
			assemblyId = "Riena.random.assemblyid." + Long.valueOf(random.nextLong()).toString(); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_DEBUG, "Assembly has no id. Generated a random '" + assemblyId //$NON-NLS-1$
					+ "'. For Assembler=" + assembly.getNavigationAssembler()); //$NON-NLS-1$
		}

		INavigationAssembler assembler = assembly.createNavigationAssembler();
		if (assembler == null) {
			assembler = createDefaultAssembler();
		}
		assembler.setId(assemblyId);
		assembler.setParentNodeId(assembly.getParentNodeId());
		assembler.setStartOrder(getStartOrder(assembly));
		assembler.setAssembly(assembly);

		registerNavigationAssembler(assemblyId, assembler);

	}

	private int getStartOrder(ICommonNavigationAssemblyExtension assembly) {
		try {
			INavigationAssembly2Extension assembly2 = (INavigationAssembly2Extension) assembly;
			return assembly2.getStartOrder();
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * Returns the root node in the navigation model tree for the given node.
	 * 
	 * @param node
	 *            child node
	 * @return root node
	 */
	protected INavigationNode<?> getRootNode(INavigationNode<?> node) {
		if (node.getParent() == null) {
			return node;
		}
		return getRootNode(node.getParent());
	}

	/**
	 * Searches for a node that has the given ID and it's a child of the given
	 * node.
	 * 
	 * @param node
	 *            the node form that the search is started
	 * @param targetId
	 *            ID of the node that should be found
	 * @return the found node or {@code null} if no matching node was found
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

	public INavigationAssembler getNavigationAssembler(NavigationNodeId nodeId, NavigationArgument argument) {

		if (nodeId != null && nodeId.getTypeId() != null) {
			for (INavigationAssembler probe : getNavigationAssemblers()) {
				if (probe.acceptsToBuildNode(nodeId, argument)) {
					return probe;
				}
			}
		}

		return null;
	}

	/**
	 * Returns the ID of the parent node.
	 * 
	 * @param argument
	 *            contains information passed used for providing the target
	 *            node. One information can be a parent node
	 * @param assembler
	 *            navigation assembler. If the navigation argument has no parent
	 *            node information the parent node ID of the assembler is
	 *            returned.
	 * @return ID of the parent node
	 */
	private NavigationNodeId getParentTypeId(NavigationArgument argument, INavigationAssembler assembler) {
		if (argument != null && argument.getParentNodeId() != null) {
			return argument.getParentNodeId();
		} else {
			String parentTypeId = assembler.getParentNodeId();
			if (StringUtils.isEmpty(parentTypeId)) {
				String id = assembler.getId();
				throw new ExtensionPointFailure("parentTypeId cannot be null or blank for assembly ID=" //$NON-NLS-1$
						+ id);
			}
			return new NavigationNodeId(parentTypeId);
		}
	}

	/**
	 * Prepares the given node and all its children.
	 * 
	 * @param node
	 *            navigation node to prepare.
	 */
	private void prepareAll(INavigationNode<?> node) {
		node.prepare();
		for (INavigationNode<?> child : node.getChildren()) {
			prepareAll(child);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void cleanUp() {
		assemblyId2AssemblerCache.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public INavigationAssembler getNavigationAssembler(String assemblyId) {
		return assemblyId2AssemblerCache.get(assemblyId);
	}

	/**
	 * Returns all registered assemblers.
	 * 
	 * @return collection of all assemblers
	 */
	public Collection<INavigationAssembler> getNavigationAssemblers() {
		return assemblyId2AssemblerCache.values();
	}

	/**
	 * Registers the given assembler and associates it with the given assembly
	 * ID.
	 * <p>
	 * If another assembler is already registered with the same ID an exception
	 * ({@link IllegalStateException}) is thrown.
	 * 
	 * @param id
	 *            ID of the assembly
	 * @param assembler
	 *            assembler to register
	 */
	public void registerNavigationAssembler(String id, final INavigationAssembler assembler) {
		INavigationAssembler oldAssembler = assemblyId2AssemblerCache.put(id, assembler);
		if (oldAssembler != null) {
			String msg = String.format("There are two assembly extension definitions for '%s'.", id); //$NON-NLS-1$
			RuntimeException runtimeExc = new IllegalStateException(msg);
			LOGGER.log(LogService.LOG_ERROR, msg, runtimeExc);
			throw runtimeExc;
		}
	}

	/**
	 * Used to prepare the assembler in a application specific way.
	 * 
	 * @param targetId
	 * @param assembler
	 * @param parentNode
	 */
	protected void prepareNavigationAssembler(NavigationNodeId targetId, INavigationAssembler assembler,
			INavigationNode<?> parentNode) {
		if (assembler instanceof IGenericNavigationAssembler) {
			((IGenericNavigationAssembler) assembler).setAssemblerProvider(this);
		}

	}

	/**
	 * Creates a generic assembler that is used if the assembly has no
	 * assembler.
	 * 
	 * @return default assembler
	 */
	protected INavigationAssembler createDefaultAssembler() {
		return new GenericNavigationAssembler();
	}

}
