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
	private IRemoteProgressMonitorRegistry registry;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() throws Exception {
		super.setUp();
		regAttachmentService = new RemoteServiceFactory().createAndRegisterProxy(IAttachmentService.class,
				"http://localhost:8080/hessian/AttachmentService", "hessian", Activator.getDefault().getContext());
		attachService = (IAttachmentService) Activator.getDefault().getContext().getService(
				Activator.getDefault().getContext().getServiceReference(IAttachmentService.class.getName()));
		BundleContext context = Activator.getDefault().getContext();
		registry = (IRemoteProgressMonitorRegistry) context.getService(context
				.getServiceReference(IRemoteProgressMonitorRegistry.class.getName()));

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
	public void testSendSimpleAttachmentProgress() throws Exception {
		TestProgressMonitor monitor = new TestProgressMonitor();
		registry.addProgressMonitor(attachService, monitor, IRemoteProgressMonitorRegistry.MONITOR_ONE_CALL);

		Attachment attachment = generateLargeAttachment(15000);
		int i = attachService.sendAttachmentAndReturnSize(attachment);
		assertTrue(i == 15000);
		monitor.validate(15000 / 512 + 1, 1);
		registry.removeAllProgressMonitors(attachService);
	}

	public void testSendLargeAttachmentProgress() throws Exception {
		TestProgressMonitor monitor = new TestProgressMonitor();
		registry.addProgressMonitor(attachService, monitor, IRemoteProgressMonitorRegistry.MONITOR_ONE_CALL);

		Attachment attachment = generateLargeAttachment(15000000);
		int i = attachService.sendAttachmentAndReturnSize(attachment);
		assertTrue(i == 15000000);
		monitor.validate(15000000 / 512 + 1, 1);
		registry.removeAllProgressMonitors(attachService);
	}

	public void testReceiveSimpleAttachmentProgress() throws Exception {
		TestProgressMonitor monitor = new TestProgressMonitor();
		registry.addProgressMonitor(attachService, monitor, IRemoteProgressMonitorRegistry.MONITOR_ONE_CALL);

		Attachment attachment = attachService.returnAttachmentForSize(15000);
		int i = getSize(attachment);
		assertTrue(i == 15000);
		monitor.validate(1, i / 512 + 1);
		registry.removeAllProgressMonitors(attachService);
	}

	public void testReceiveLargeAttachmentProgress() throws Exception {
		TestProgressMonitor monitor = new TestProgressMonitor();
		registry.addProgressMonitor(attachService, monitor, IRemoteProgressMonitorRegistry.MONITOR_ONE_CALL);

		Attachment attachment = attachService.returnAttachmentForSize(15000000);
		int i = getSize(attachment);
		assertTrue(i == 15000000);
		monitor.validate(1, i / 512 + 1);
		registry.removeAllProgressMonitors(attachService);
	}

	private int getSize(Attachment attachment) throws IOException {
		InputStream input = attachment.readAsStream();
		int count = 0;
		while (input.read() != -1) {
			count++;
		}
		return count;
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

	/**
	 *
	 */
	private static final class TestProgressMonitor implements IRemoteProgressMonitor {

		private boolean start = false;
		private boolean end = false;
		int requestCount = 0;
		int responseCount = 0;
		int lastResponseLength = -1;
		private boolean requestDivideable = true;
		private boolean responseDividable = true;

		public void validate(int requests, int responses) {
			assertTrue("start method not called", start);
			assertTrue("end method not called", end);
			// request count and response count can be larger with with larger size, the protocol overhead gets more (which is included in the count)
			if (requests != -1) {
				assertTrue("expected " + requests + " but was " + requestCount + " for number of requestEvents",
						requests <= requestCount);
			}
			if (responses != -1) {
				assertTrue("expected " + responses + " but was " + responseCount + " for number of responseEvents",
						responses <= responseCount);
			}
		}

		public void request(RemoteProgressMonitorEvent event) {
			System.out.println(event.getBytesProcessed());
			if (event.getBytesProcessed() % 512 != 0) {
				if (requestDivideable == true) {
					requestDivideable = false;
				} else {
					assertTrue(
							"expected bytesProcess in request to be always dividable by 512 (expect in the last call) :"
									+ event.getBytesProcessed(), event.getBytesProcessed() % 512 == 0);
				}
			}

			requestCount++;
		}

		public void response(RemoteProgressMonitorEvent event) {
			System.out.println(event.getBytesProcessed());
			if (event.getBytesProcessed() % 512 != 0) {
				if (responseDividable == true) {
					responseDividable = false;
				} else {
					assertTrue(
							"expected bytesProcess in response to be always dividable by 512 (expect in the last call) :"
									+ event.getBytesProcessed(), event.getBytesProcessed() % 512 == 0);
				}
			}
			responseCount++;
			lastResponseLength = event.getBytesProcessed();
		}

		public void start() {
			assertFalse(start);
			System.out.println("start");
			start = true;
		}

		public void end() {
			assertFalse(end);
			System.out.println("end");
			end = true;
		}
	}
}