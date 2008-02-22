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
package org.eclipse.riena.core.service;

/**
 * ServiceId and Injector simplify finding of OSGi Services and injects them
 * into a target object. To do so the Injector contains a service tracker
 * listening to appearing and disappearing services and injects them into the
 * target. A target object defines named and typed bind and unbind methods. The
 * Injector calls the bind method when the specified service was registered or
 * modified. Injector calls the unbind method when the specified service becomes
 * unregistered.
 * <p>
 * The service injector tracks the specified OSGi Service after calls
 * {@link #doStart()} and stop tracks after calls {@link #stop()}.
 * <p>
 * The ServiceId and Injector are implemented as a ´fluent interface´ allowing
 * constructs like:
 * <ol>
 * <li>new ServiceId("id1").injectInto(target).andStart(context)</li>
 * <li>new
 * ServiceId("id2").useFilter(filter).injectInto(target).bind("register").unbind("unregister").andStart(context)</li>
 * <li>new
 * ServiceId("id3").useRanking().injectInto(target).bind("register").unbind("unregister").andStart(context)</li>
 * <li>..</li>
 * </ol>
 * <p>
 * This fluent interface makes a few assumptions (defaults) that makes writing
 * service injectors short and expressive , e.g. item one the list, means use no
 * service ranking, no filter, and the bind method name is "bind" and the
 * un-bind method name is "unbind".
 * <p>
 * A service or services may be injected into the target by specifying
 * <code>useRanking</code> and/or by specifying a filter expression with
 * <code>userFilter("filter")</code>.
 * <p>
 * The bind and un-bind method that get called by the injector can be
 * polymorphic, i.e. the target can have multiple bind/un-bind methods with the
 * same name but with different signatures. The injector takes responsibility
 * for choosing the appropriate bind/unbind methods.
 */
public class ServiceId {

	/**
	 * Default service ranking for all riena services.
	 */
	public final static Integer DEFAULT_RANKING = -100;

	private String serviceId;
	private boolean ranking;
	private String filter;

	public ServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * Use filtering {@link org.osgi.framework.Filter} for picking the tracked
	 * service(s) to inject.
	 * 
	 * @throws IllegalStateException
	 *             if a filter has already been set.
	 * @return this service id
	 */
	public ServiceId useFilter(String filter) {
		if (this.filter != null)
			throw new IllegalStateException("Filter has already been set!");
		this.filter = filter;
		return this;
	}

	/**
	 * Use service ranking {@link org.osgi.framework.Constants#SERVICE_RANKING}
	 * picking the tracked service to inject.
	 * 
	 * @throws IllegalStateException
	 *             if ranking has already been activated
	 * @return this service id
	 */
	public ServiceId useRanking() {
		if (ranking)
			throw new IllegalStateException("Ranking has already been set!");
		ranking = true;
		return this;
	}

	/**
	 * Inject this service id into the specified target.
	 * 
	 * @param target
	 * @throws IllegalArgumentException
	 *             on target == null
	 * @return the injector responsible for tracking this service id
	 */
	public Injector injectInto(Object target) {
		if (target == null)
			throw new IllegalArgumentException("target may not be null.");

		return ranking ? new RankingInjector(this, target) : new FilterInjector(this, target);
	}

	/**
	 * @return
	 */
	String getServiceId() {
		return serviceId;
	}

	/**
	 * @return the filter
	 */
	String getFilter() {
		return filter;
	}

}
