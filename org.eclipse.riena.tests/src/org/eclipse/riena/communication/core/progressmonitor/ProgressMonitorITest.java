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
package org.eclipse.riena.communication.core.progressmonitor;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.attachment.Attachment;
import org.eclipse.riena.communication.core.factory.RemoteServiceFactory;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.sample.app.common.attachment.IAttachmentService;
import org.eclipse.riena.tests.RienaTestCase;

import org.osgi.framework.BundleContext;

/**
 * This Testclass sends large byte streams to the Attachment Service and other
 * services and installs a progress monitor and watches the behaviour.
 * 
 */
public final class ProgressMonitorITest extends RienaTestCase {

	private IAttachmentService attachService;
	private IRemoteServiceRegistration regAttachmentService;
	private IProgressMonitorRegistry registry;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() throws Exception {
		super.setUp();
		regAttachmentService = new RemoteServiceFactory().createAndRegisterProxy(IAttachmentService.class,
				"http://localhost:8080/hessian/AttachmentService", "hessian", null);
		attachService = (IAttachmentService) Activator.getDefault().getContext().getService(
				Activator.getDefault().getContext().getServiceReference(IAttachmentService.class.getName()));
		BundleContext context = Activator.getDefault().getContext();
		registry = (IProgressMonitorRegistry) context.getService(context
				.getServiceReference(IProgressMonitorRegistry.class.getName()));

	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	public void tearDown() throws Exception {
		super.tearDown();
		regAttachmentService.unregister();
		attachService = null;
	}

	/**
	 * Nomen est Omen
	 * 
	 * @throws Exception
	 */
	public void testSimpleProgress() throws Exception {
		registry.addProgressMonitor(attachService, new IProgressMonitor() {

			public void end() {
				System.out.println("end");
			}

			public void request(ProgressMonitorEvent event) {
				System.out.println(event.getBytesProcessed());
			}

			public void response(ProgressMonitorEvent event) {
				System.out.println(event.getBytesProcessed());
			}

			public void start() {
				System.out.println("start");
			}

		}, IProgressMonitorRegistry.MONITOR_ONE_CALL);

		Attachment attachment = generateLargeAttachment(15000);
		int i = attachService.sendAttachmentAndReturnSize(attachment);
		System.out.println("done");
		assertTrue(i == 15000);
	}

	private Attachment generateLargeAttachment(final int countInBytes) throws IOException {
		return new Attachment(new InputStream() {

			private int count = countInBytes;
			private SecureRandom random = new SecureRandom();

			@Override
			public int read() throws IOException {
				count--;
				if (count >= 0) {
					return random.nextInt();
				} else {
					return -1;
				}
			}
		});
	}

	private void trace(Object object) {
		System.out.println(object.toString());
	}

}