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
package org.eclipse.riena.navigation.ui.application;

import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.internal.navigation.ui.Activator;
import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationAssembler;
import org.eclipse.riena.navigation.INavigationAssemblyExtension;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeProvider;
import org.eclipse.riena.navigation.INodeExtension;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.model.NavigationNodeProvider;
import org.eclipse.riena.navigation.model.NavigationNodeProviderAccessor;
import org.eclipse.riena.ui.core.resource.IIconManager;
import org.eclipse.riena.ui.core.uiprocess.ProgressProviderBridge;
import org.osgi.service.log.LogService;

/**
 * Abstract application defining the basic structure of a Riena application
 */
public abstract class AbstractApplication implements IApplication {

	private final static Logger LOGGER = Activator.getDefault().getLogger(AbstractApplication.class);
	public static final String DEFAULT_APPLICATION_TYPEID = "application"; //$NON-NLS-1$

	public Object start(IApplicationContext context) throws Exception {
		IApplicationNode node = createModel();
		createStartupsFromExtensions(node);
		ApplicationNodeManager.registerApplicationNode(node);
		initializeNode(node);
		setProgressProviderBridge();
		return createView(context, node);
	}

	private void setProgressProviderBridge() {
		ProgressProviderBridge bridge = ProgressProviderBridge.instance();
		Job.getJobManager().setProgressProvider(bridge);
		bridge.setVisualizerFactory(new VisualizerFactory());
	}

	/**
	 * Overwrite to create own application model
	 * 
	 * @return IApplicationModelProvider - root of the configured application
	 *         model
	 */
	protected IApplicationNode createModel() {
		IApplicationNode applicationModel = new ApplicationNode(new NavigationNodeId(DEFAULT_APPLICATION_TYPEID));
		return applicationModel;
	}

	protected void createStartupsFromExtensions(IApplicationNode applicationNode) {

		SortedSet<StartupSortable> startups = new TreeSet<StartupSortable>();

		INavigationNodeProvider nnp = NavigationNodeProviderAccessor.current().getNavigationNodeProvider();
		Integer sequence = null;
		String id = null;
		for (INavigationAssembler assembler : ((NavigationNodeProvider) nnp).getNavigationAssemblers()) {
			sequence = getAutostartSequence(assembler.getAssembly());
			if (sequence != null) {
				id = getTypeId(assembler.getAssembly().getSubApplicationNode());
				if (id != null) {
					startups.add(new StartupSortable(StartupLevel.SUBAPPLICATION, sequence, id));
				} else {
					id = getTypeId(assembler.getAssembly().getModuleGroupNode());
					if (id != null) {
						startups.add(new StartupSortable(StartupLevel.MODULEGROUP, sequence, id));
					} else {
						id = getTypeId(assembler.getAssembly().getModuleNode());
						if (id != null) {
							startups.add(new StartupSortable(StartupLevel.MODULE, sequence, id));
						} else {
							id = getTypeId(assembler.getAssembly().getSubModuleNode());
							if (id != null) {
								startups.add(new StartupSortable(StartupLevel.SUBMODULE, sequence, id));
							} else {
								id = assembler.getAssembly().getTypeId();
								if (id != null) {
									startups.add(new StartupSortable(StartupLevel.UNKNOWN, sequence, id));
								}
							}
						}
					}
				}
			}
		}

		for (StartupSortable startup : startups) {
			String message = "creating startup module %s [level=%s sequence=%d]\n"; //$NON-NLS-1$
			LOGGER.log(LogService.LOG_INFO, String.format(message, startup.id, startup.level, startup.sequence));
			applicationNode.create(new NavigationNodeId(startup.id));
		}
	}

	protected void initializeNode(IApplicationNode model) {
		initializeModelDefaults(model);
	}

	protected void initializeModelDefaults(IApplicationNode model) {
		initializeNodeDefaults(model);
	}

	protected void initializeNodeDefaults(IApplicationNode node) {

		for (ISubApplicationNode child : node.getChildren()) {
			initializeNodeDefaults(child);
		}
	}

	protected void initializeNodeDefaults(ISubApplicationNode node) {

		for (IModuleGroupNode child : node.getChildren()) {
			initializeNodeDefaults(child);
		}
	}

	protected void initializeNodeDefaults(IModuleGroupNode node) {

		for (IModuleNode child : node.getChildren()) {
			initializeNodeDefaults(child);
		}
	}

	protected void initializeNodeDefaults(IModuleNode node) {

		initializeNodeDefaultIcon(node);
		for (ISubModuleNode child : node.getChildren()) {
			initializeNodeDefaults(child);
		}
	}

	protected void initializeNodeDefaults(ISubModuleNode node) {

		initializeNodeDefaultIcon(node);
		for (ISubModuleNode child : node.getChildren()) {
			initializeNodeDefaults(child);
		}
	}

	protected void initializeNodeDefaultIcon(INavigationNode<?> node) {

		if (node.getIcon() == null) {
			node.setIcon(IIconManager.DEFAULT_ICON);
		}
	}

	abstract protected Object createView(IApplicationContext context, IApplicationNode pNode) throws Exception;

	private Integer getAutostartSequence(INavigationAssemblyExtension assembly) {

		try {
			return assembly.getAutostartSequence();
		} catch (Exception ignore) {
			// does not seem to be an integer, assume no autostart
			return null;
		}
	}

	private String getTypeId(INodeExtension extension) {

		if (extension == null) {
			return null;
		} else {
			return extension.getTypeId();
		}
	}

	enum StartupLevel {
		SUBAPPLICATION, MODULEGROUP, MODULE, SUBMODULE, UNKNOWN
	}

	static class StartupSortable implements Comparable<StartupSortable> {

		public final StartupLevel level;
		public final int sequence;
		public final String id;

		public StartupSortable(StartupLevel level, Integer sequence, String id) {
			this.level = level;
			this.sequence = sequence;
			this.id = id;
		}

		public int compareTo(StartupSortable o) {
			if (level.compareTo(o.level) != 0) {
				return level.compareTo(o.level);
			} else {
				return sequence - o.sequence;
			}
		}
	}
}
