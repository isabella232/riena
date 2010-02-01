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
package org.eclipse.riena.internal.ui.workarea.registry;

import java.util.Hashtable;
import java.util.SortedSet;
import java.util.TreeSet;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import org.eclipse.riena.internal.ui.workarea.Activator;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.ui.ridgets.controller.IController;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaDefinition;
import org.eclipse.riena.ui.workarea.spi.IWorkareaDefinitionRegistry;

public final class WorkareaDefinitionRegistryFacade implements IWorkareaDefinitionRegistry, ServiceTrackerCustomizer {

	private static final WorkareaDefinitionRegistryFacade INSTANCE = new WorkareaDefinitionRegistryFacade();

	private SortedSet<WorkareaDefinitionRegistryWithRank> contributedRegistries;

	public static WorkareaDefinitionRegistryFacade getInstance() {
		return INSTANCE;
	}

	private WorkareaDefinitionRegistryFacade() {
		contributedRegistries = new TreeSet<WorkareaDefinitionRegistryWithRank>();
		registerExplicitDefinitionRegistry();
	}

	/**
	 * {@inheritDoc}
	 */
	public IWorkareaDefinition getDefinition(Object id) {

		for (IWorkareaDefinitionRegistry registry : contributedRegistries) {
			IWorkareaDefinition definition = registry.getDefinition(id);
			if (definition != null) {
				return definition;
			}

			if (id instanceof INavigationNode<?>) {
				INavigationNode<?> node = (INavigationNode<?>) id;
				if (node.getNodeId() != null && node.getNodeId().getTypeId() != null) {
					definition = registry.getDefinition(node.getNodeId().getTypeId());
					if (definition != null) {
						return definition;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Creates a workarea definition ({@code IWorkareaDefinition}) with the
	 * given information and registers it with the specified type ID.
	 * 
	 * @param id
	 *            type ID
	 * @param controllerClass
	 *            class of the controller to be used with the view representing
	 *            the working area
	 * @param viewId
	 *            the id of this work areas view (viewId as contributed to the
	 *            <code>org.eclipse.ui.views</code> extension point)
	 * @param shared
	 *            <code>true</code> if the view associated with this node should
	 *            be shared, <code>false</code> otherwise
	 * @return the registered workarea definition
	 */
	public IWorkareaDefinition registerDefinition(Object id, Class<? extends IController> controllerClass,
			Object viewId, boolean shared) {
		WorkareaDefinition def = new WorkareaDefinition(controllerClass, viewId);
		def.setViewShared(shared);
		return register(id, def);
	}

	/**
	 * {@inheritDoc}
	 */
	public IWorkareaDefinition register(Object id, IWorkareaDefinition definition) {

		for (IWorkareaDefinitionRegistry registry : contributedRegistries) {
			IWorkareaDefinition def = registry.register(id, definition);
			if (def != null) {
				return def;
			}
		}

		return null;
	}

	private void registerExplicitDefinitionRegistry() {

		BundleContext context = Activator.getDefault().getBundle().getBundleContext();
		IWorkareaDefinitionRegistry registry = ExplicitWorkareaDefinitionRegistry.getInstance();
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.SERVICE_RANKING, 100);

		context.registerService(IWorkareaDefinitionRegistry.class.getName(), registry, props);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object addingService(ServiceReference reference) {

		// get service and increase use count
		BundleContext bundleContext = Activator.getDefault().getBundle().getBundleContext();
		IWorkareaDefinitionRegistry registry = (IWorkareaDefinitionRegistry) bundleContext.getService(reference);

		// add registry
		contributedRegistries.add(createEntry(reference, registry));

		return registry;
	}

	/**
	 * {@inheritDoc}
	 */
	public void modifiedService(ServiceReference reference, Object service) {

		// maybe ranking has changed
		WorkareaDefinitionRegistryWithRank entry = createEntry(reference, service);
		contributedRegistries.remove(entry);
		contributedRegistries.add(entry);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removedService(ServiceReference reference, Object service) {

		// remove registry
		//Object rankingProperty = reference.getProperty(Constants.SERVICE_RANKING);
		contributedRegistries.remove(createEntry(reference, service));

		// unget service and decrease use count
		BundleContext bundleContext = Activator.getDefault().getBundle().getBundleContext();
		bundleContext.ungetService(reference);
	}

	private WorkareaDefinitionRegistryWithRank createEntry(ServiceReference reference, Object registry) {

		Object rankingProperty = reference.getProperty(Constants.SERVICE_RANKING);
		int ranking = (rankingProperty instanceof Integer) ? ((Integer) rankingProperty).intValue() : 0;
		return new WorkareaDefinitionRegistryWithRank(ranking, (IWorkareaDefinitionRegistry) registry);
	}

	private static class WorkareaDefinitionRegistryWithRank implements IWorkareaDefinitionRegistry,
			Comparable<WorkareaDefinitionRegistryWithRank> {

		private int ranking;
		private IWorkareaDefinitionRegistry registry;

		WorkareaDefinitionRegistryWithRank(int ranking, IWorkareaDefinitionRegistry registry) {
			this.ranking = ranking;
			this.registry = registry;
		}

		public int getRanking() {
			return ranking;
		}

		public IWorkareaDefinitionRegistry getRegistry() {
			return registry;
		}

		/**
		 * @see org.eclipse.riena.ui.workarea.spi.IWorkareaDefinitionRegistry#
		 *      getDefinition(Object)
		 */
		public IWorkareaDefinition getDefinition(Object id) {
			return registry.getDefinition(id);
		}

		/**
		 * @see org.eclipse.riena.ui.workarea.spi.IWorkareaDefinitionRegistry#register
		 *      (java.lang.Object)
		 */
		public IWorkareaDefinition register(Object id, IWorkareaDefinition definition) {
			return registry.register(id, definition);
		}

		/**
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(WorkareaDefinitionRegistryWithRank other) {

			if (this.getRanking() != other.getRanking()) {
				return this.getRanking() > other.getRanking() ? -1 : 1;
			}
			return 0;
		}

		@Override
		public boolean equals(Object other) {

			if (other instanceof WorkareaDefinitionRegistryWithRank) {
				return registry.equals(((WorkareaDefinitionRegistryWithRank) other).getRegistry());
			} else {
				return registry.equals(other);
			}
		}

		@Override
		public int hashCode() {
			return registry.hashCode();
		}
	}

}
