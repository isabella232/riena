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
package org.eclipse.riena.ui.core.uiprocess;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class UIProcess extends PlatformObject implements IUIMonitor {

	public final static QualifiedName PROPERTY_CONTEXT = new QualifiedName("uiProcess", "context"); //$NON-NLS-1$//$NON-NLS-2$

	/** The name of the extension point to this class. */
	protected static final String EXTENSION_POINT_ID = "org.riena.ui.core.uiprocess"; //$NON-NLS-1$
	/**
	 * The name of the element within the extension point to define which
	 * UISynchronizer implementation should be used, if none is given. The
	 * element has to contain an attribute 'class'.
	 */
	protected static final String EXTENSION_POINT_UI_SYNCHRONIZER_ELEMENT = "uisynchronizer"; //$NON-NLS-1$
	private UICallbackDispatcher callbackDispatcher;

	private Job job;

	/**
	 * Creates a new UIProcess. This constructor assumes the plug-in defines
	 * exactly one extension point to this class like this:
	 * 
	 * <pre>
	 * &lt;code&gt;
	 * &lt;extension point=&quot;org.riena.ui.core.uiprocess&quot;&gt;
	 *  &lt;uisynchronizer class=&quot;org.eclipse.riena.ui.swt.uiprocess.SwtUISynchronizer&quot;/&gt;
	 * &lt;/extension&gt;
	 * </pre>
	 * 
	 * </code></dd> In case multiple extension points exist, which define an
	 * implementation class for the UIprocesses UIsynchronizer, there is no
	 * guarantee which one will be used. <br>
	 * <br>
	 * 
	 * @param name
	 *            the name of the job
	 * 
	 * @throws some_kind_of_runtime_exception
	 *             if no configuration point is found or if an object of the
	 *             class given in the extension point can not be instantiated.
	 * 
	 */
	public UIProcess(final String name) {
		this(name, false);
	}

	public UIProcess(final String name, boolean user) {
		this(name, user, new Object());
	}

	public UIProcess(final String name, boolean user, Object context) {
		this(name, getSynchronizerFromExtensionPoint(), user, context);
	}

	private static IUISynchronizer getSynchronizerFromExtensionPoint() {
		final IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		final IConfigurationElement[] configurations = extensionRegistry
				.getConfigurationElementsFor(EXTENSION_POINT_ID);
		if (configurations.length == 0) {
			throw new IllegalStateException("No configuration element for extension point '" + EXTENSION_POINT_ID //$NON-NLS-1$
					+ '\'');
		}
		// if one extension point exists, which contains a "uisynchronizer"
		for (final IConfigurationElement configuration : configurations) {
			if (EXTENSION_POINT_UI_SYNCHRONIZER_ELEMENT.equals(configuration.getName())) {
				try {
					return (IUISynchronizer) configuration.createExecutableExtension("class"); //$NON-NLS-1$
				} catch (final CoreException e) {
					throw new IllegalStateException("Could not create object from attribute 'class' in element '" //$NON-NLS-1$
							+ EXTENSION_POINT_UI_SYNCHRONIZER_ELEMENT + "' in extension point '" + EXTENSION_POINT_ID //$NON-NLS-1$
							+ '\'', e);
				}
			}

		}
		throw new IllegalStateException("No element '" + EXTENSION_POINT_UI_SYNCHRONIZER_ELEMENT //$NON-NLS-1$
				+ "' in extension point '" + EXTENSION_POINT_ID + '\''); //$NON-NLS-1$
	}

	public UIProcess(String name, IUISynchronizer syncher, boolean user, Object context) {
		this(name, new UICallbackDispatcher(syncher), user, context);
	}

	private UIProcess(String name, UICallbackDispatcher dispatcher, boolean user, Object context) {
		this.callbackDispatcher = dispatcher;
		createJob(name, user, context);
		configure();
	}

	public UIProcess(final Job job) {
		this.callbackDispatcher = new UICallbackDispatcher(getSynchronizerFromExtensionPoint());
		this.job = job;
		configure();
	}

	private void configure() {
		register();
		configureProcessInfo();
	}

	private void configureProcessInfo() {
		if (callbackDispatcher != null) {
			ProcessInfo processInfo = callbackDispatcher.getProcessInfo();
			processInfo.setContext(job.getProperty(PROPERTY_CONTEXT));
			processInfo.addPropertyChangeListener(new CancelListener());
			processInfo.setDialogVisible(job.isUser());
			processInfo.setNote(job.getName());
			processInfo.setTitle(job.getName());
		}
	}

	private void createJob(String name, boolean user, Object context) {
		this.job = new InternalJob(name);
		job.setUser(user);
		job.setProperty(PROPERTY_CONTEXT, context);
	}

	private final class CancelListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent event) {
			if (ProcessInfo.PROPERTY_CANCELED.equals(event.getPropertyName())) {
				job.cancel();
			}
		}
	}

	private final class InternalJob extends Job {

		public InternalJob(String name) {
			super(name);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			monitor.beginTask(getName(), getTotalWork());
			boolean state = runJob(monitor);
			monitor.done();
			return state ? Status.OK_STATUS : Status.CANCEL_STATUS;
		}

	}

	protected int getTotalWork() {
		return 0;
	}

	private void register() {
		callbackDispatcher.addUIMonitor(this);
		ProgressProviderBridge.instance().registerMapping(job, this);
	}

	public UICallbackDispatcher getCallbackDispatcher() {
		return callbackDispatcher;
	}

	public void updateProgress(int progress) {
	};

	public void initialUpdateUI(int totalWork) {
	};

	public void finalUpdateUI() {
	};

	public boolean runJob(IProgressMonitor monitor) {
		return true;
	}

	public void start() {
		job.schedule();
	}

	@Override
	public Object getAdapter(Class adapter) {
		Object adapted = super.getAdapter(adapter);
		if (adapted == null) {
			if (adapter.isInstance(this)) {
				return this;
			}
			if (adapter.equals(UICallbackDispatcher.class)) {
				adapted = getCallbackDispatcher();
			}
		}
		return adapted;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		getProcessInfo().setNote(note);
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		getProcessInfo().setTitle(title);
	}

	public void setIcon(String icon) {
		getProcessInfo().setIcon(icon);
	}

	/**
	 * @return the info
	 */
	private ProcessInfo getProcessInfo() {
		return getCallbackDispatcher().getProcessInfo();
	}

	public boolean isActive(IUIMonitorContainer container) {
		return true;
	}

}
