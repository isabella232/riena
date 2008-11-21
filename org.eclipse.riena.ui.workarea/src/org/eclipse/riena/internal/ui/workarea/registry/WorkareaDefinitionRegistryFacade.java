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
package org.eclipse.riena.internal.ui.workarea.registry;

import java.util.Hashtable;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.riena.internal.ui.workarea.Activator;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.ui.ridgets.controller.IController;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaDefinition;
import org.eclipse.riena.ui.workarea.spi.IWorkareaDefinitionRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class WorkareaDefinitionRegistryFacade implements IWorkareaDefinitionRegistry, ServiceTrackerCustomizer {

	private static final WorkareaDefinitionRegistryFacade instance = new WorkareaDefinitionRegistryFacade();

	private SortedSet<WorkareaDefinitionRegistryWithRank> contributedRegistries;

	public static WorkareaDefinitionRegistryFacade getInstance() {
		return instance;
	}
	
	private WorkareaDefinitionRegistryFacade() {
		
		contributedRegistries = new TreeSet<WorkareaDefinitionRegistryWithRank>();
		registerExplicitDefinitionRegistry();
	}

	public IWorkareaDefinition getDefinition(Object id) {

		for (IWorkareaDefinitionRegistry registry : contributedRegistries) {
			IWorkareaDefinition definition = registry.getDefinition(id);
			if (definition != null) {
				return definition;
			}
			
			if (id instanceof ISubModuleNode) {
				ISubModuleNode node = (ISubModuleNode)id;
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

	public IWorkareaDefinition registerDefinition(Object id, Class<? extends IController> controllerClass, Object viewId, boolean isViewShared) {
		
		return register(id, new WorkareaDefinition(controllerClass, viewId, isViewShared));
	}

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

	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#addingService(org.osgi.framework.ServiceReference)
	 */
	public Object addingService(ServiceReference reference) {

		// get service and increase use count
		BundleContext bundleContext = Activator.getDefault().getBundle().getBundleContext();
		IWorkareaDefinitionRegistry registry = (IWorkareaDefinitionRegistry)bundleContext.getService(reference); 

		// add registry
		contributedRegistries.add(createEntry(reference, registry));
		
		return registry;
	}

	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#modifiedService(org.osgi.framework.ServiceReference, java.lang.Object)
	 */
	public void modifiedService(ServiceReference reference, Object service) {

		// maybe ranking has changed
		WorkareaDefinitionRegistryWithRank entry = createEntry(reference, service);
		contributedRegistries.remove(entry);
		contributedRegistries.add(entry);
	}

	/* (non-Javadoc)
	 * @see org.osgi.util.tracker.ServiceTrackerCustomizer#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
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
		return new WorkareaDefinitionRegistryWithRank(ranking, (IWorkareaDefinitionRegistry)registry);
	}
	
	private static class WorkareaDefinitionRegistryWithRank implements IWorkareaDefinitionRegistry, Comparable<WorkareaDefinitionRegistryWithRank> {
	
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

		/* (non-Javadoc)
		 * @see org.eclipse.riena.ui.workarea.spi.IWorkareaDefinitionRegistry#getDefinition(java.lang.Object)
		 */
		public IWorkareaDefinition getDefinition(Object id) {
			return registry.getDefinition(id);
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.riena.ui.workarea.spi.IWorkareaDefinitionRegistry#register(java.lang.Object)
		 */
		public IWorkareaDefinition register(Object id, IWorkareaDefinition definition) {
			return registry.register(id, definition);
		}

		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(WorkareaDefinitionRegistryWithRank other) {
			
			if (this.getRanking() != other.getRanking())
				return this.getRanking() > other.getRanking() ? -1 : 1;
			return 0;
		}
		
		public boolean equals(Object other) {
			
			if (other instanceof WorkareaDefinitionRegistryWithRank) {
				return registry.equals(((WorkareaDefinitionRegistryWithRank)other).getRegistry());
			} else {
				return registry.equals(other);
			}
		}
		
		public int hashCode() {
			return registry.hashCode();
		}
	}
}
