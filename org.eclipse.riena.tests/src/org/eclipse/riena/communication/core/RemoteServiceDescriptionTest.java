/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.communication.core;

import org.easymock.EasyMock;

import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

import org.eclipse.riena.communication.core.publisher.RSDPublisherProperties;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.tests.Activator;

/**
 *
 */
@NonUITestCase
public class RemoteServiceDescriptionTest extends RienaTestCase {

	public void testDefaultCreation() {
		final RemoteServiceDescription rsd = new RemoteServiceDescription();
		assertNull(rsd.getBundleName());
		assertNull(rsd.getPath());
		assertNull(rsd.getProperty(""));
		assertNull(rsd.getProtocol());
		assertNull(rsd.getService());
		assertNull(rsd.getServiceInterfaceClassName());
		assertNull(rsd.getURL());
		assertNull(rsd.getVersion());
		assertNull(rsd.getServiceInterfaceClass());
		assertNull(rsd.getServiceRef());
		assertEquals(RemoteServiceDescription.State.REGISTERED, rsd.getState());
	}

	public void testNonDefaultCreationObjectClass() {
		final ServiceReference ref = EasyMock.createMock(ServiceReference.class);

		EasyMock.expect(ref.getPropertyKeys()).andReturn(new String[] { Constants.OBJECTCLASS });
		EasyMock.expect(ref.getBundle()).andReturn(Activator.getDefault().getBundle());
		EasyMock.replay(ref);

		final Object service = "Service";
		final RemoteServiceDescription rsd = new RemoteServiceDescription(ref, service, String.class);

		assertNull(rsd.getProperty(""));
		assertNotNull(rsd.getBundleName());
		assertEquals(Activator.getDefault().getBundle().getSymbolicName(), rsd.getBundleName());
		assertNull(rsd.getPath());
		assertNull(rsd.getProtocol());
		assertTrue(service == rsd.getService());
		assertEquals(String.class.getName(), rsd.getServiceInterfaceClassName());
		assertNull(rsd.getURL());
		assertNull(rsd.getVersion());
		assertEquals(String.class, rsd.getServiceInterfaceClass());
		assertNotNull(rsd.getServiceRef());
		assertEquals(RemoteServiceDescription.State.REGISTERED, rsd.getState());
	}

	public void testNonDefaultCreationPropRemoteProtocol() {
		final ServiceReference ref = EasyMock.createMock(ServiceReference.class);

		EasyMock.expect(ref.getPropertyKeys()).andReturn(new String[] { RSDPublisherProperties.PROP_REMOTE_PROTOCOL });
		EasyMock.expect(ref.getProperty(RSDPublisherProperties.PROP_REMOTE_PROTOCOL)).andReturn("https");
		EasyMock.expect(ref.getBundle()).andReturn(Activator.getDefault().getBundle());
		EasyMock.replay(ref);

		final Object service = "Service";
		final RemoteServiceDescription rsd = new RemoteServiceDescription(ref, service, String.class);

		assertNull(rsd.getProperty(""));
		assertNotNull(rsd.getBundleName());
		assertEquals(Activator.getDefault().getBundle().getSymbolicName(), rsd.getBundleName());
		assertNull(rsd.getPath());
		assertEquals("https", rsd.getProtocol());
		assertTrue(service == rsd.getService());
		assertNull(rsd.getServiceInterfaceClassName());
		assertNull(rsd.getURL());
		assertNull(rsd.getVersion());
		assertNull(rsd.getServiceInterfaceClass());
		assertNotNull(rsd.getServiceRef());
		assertEquals(RemoteServiceDescription.State.REGISTERED, rsd.getState());
	}

	public void testNonDefaultCreationPropRemotePath() {
		final ServiceReference ref = EasyMock.createMock(ServiceReference.class);

		EasyMock.expect(ref.getPropertyKeys()).andReturn(new String[] { RSDPublisherProperties.PROP_REMOTE_PATH });
		EasyMock.expect(ref.getProperty(RSDPublisherProperties.PROP_REMOTE_PATH)).andReturn("/server/here");
		EasyMock.expect(ref.getBundle()).andReturn(Activator.getDefault().getBundle());
		EasyMock.replay(ref);

		final Object service = "Service";
		final RemoteServiceDescription rsd = new RemoteServiceDescription(ref, service, String.class);

		assertNull(rsd.getProperty(""));
		assertNotNull(rsd.getBundleName());
		assertEquals(Activator.getDefault().getBundle().getSymbolicName(), rsd.getBundleName());
		assertEquals("/server/here", rsd.getPath());
		assertNull(rsd.getProtocol());
		assertTrue(service == rsd.getService());
		assertNull(rsd.getServiceInterfaceClassName());
		assertNull(rsd.getURL());
		assertNull(rsd.getVersion());
		assertNull(rsd.getServiceInterfaceClass());
		assertNotNull(rsd.getServiceRef());
		assertEquals(RemoteServiceDescription.State.REGISTERED, rsd.getState());
	}

	public void testNonDefaultCreationPropConfigId() {
		final ServiceReference ref = EasyMock.createMock(ServiceReference.class);

		EasyMock.expect(ref.getPropertyKeys()).andReturn(new String[] { RSDPublisherProperties.PROP_CONFIG_ID });
		EasyMock.expect(ref.getProperty(RSDPublisherProperties.PROP_CONFIG_ID)).andReturn("org.eclipse.riena.configid");
		EasyMock.expect(ref.getBundle()).andReturn(Activator.getDefault().getBundle());
		EasyMock.replay(ref);

		final Object service = "Service";
		final RemoteServiceDescription rsd = new RemoteServiceDescription(ref, service, String.class);

		assertNull(rsd.getProperty(""));
		assertNotNull(rsd.getBundleName());
		assertEquals(Activator.getDefault().getBundle().getSymbolicName(), rsd.getBundleName());
		assertNull(rsd.getPath());
		assertNull(rsd.getProtocol());
		assertTrue(service == rsd.getService());
		assertNull(rsd.getServiceInterfaceClassName());
		assertNull(rsd.getURL());
		assertNull(rsd.getVersion());
		assertNull(rsd.getServiceInterfaceClass());
		assertNotNull(rsd.getServiceRef());
		assertEquals(RemoteServiceDescription.State.REGISTERED, rsd.getState());
	}

	public void testNonDefaultCreationPropElse() {
		final ServiceReference ref = EasyMock.createMock(ServiceReference.class);

		EasyMock.expect(ref.getPropertyKeys()).andReturn(
				new String[] { "the Answer to Life, the Universe, and Everything" });
		EasyMock.expect(ref.getProperty("the Answer to Life, the Universe, and Everything")).andReturn("42");
		EasyMock.expect(ref.getBundle()).andReturn(Activator.getDefault().getBundle());
		EasyMock.replay(ref);

		final Object service = "Service";
		final RemoteServiceDescription rsd = new RemoteServiceDescription(ref, service, String.class);

		assertEquals("42", rsd.getProperty("the Answer to Life, the Universe, and Everything"));
		assertNotNull(rsd.getBundleName());
		assertEquals(Activator.getDefault().getBundle().getSymbolicName(), rsd.getBundleName());
		assertNull(rsd.getPath());
		assertNull(rsd.getProtocol());
		assertTrue(service == rsd.getService());
		assertNull(rsd.getServiceInterfaceClassName());
		assertNull(rsd.getURL());
		assertNull(rsd.getVersion());
		assertNull(rsd.getServiceInterfaceClass());
		assertNotNull(rsd.getServiceRef());
		assertEquals(RemoteServiceDescription.State.REGISTERED, rsd.getState());
	}

	public void testNonDefaultCreationAllTheStuff() {
		final ServiceReference ref = EasyMock.createMock(ServiceReference.class);

		EasyMock.expect(ref.getPropertyKeys()).andReturn(
				new String[] { Constants.OBJECTCLASS, RSDPublisherProperties.PROP_REMOTE_PATH,
						RSDPublisherProperties.PROP_REMOTE_PROTOCOL, RSDPublisherProperties.PROP_CONFIG_ID,
						"the Answer to Life, the Universe, and Everything" });
		EasyMock.expect(ref.getProperty(Constants.OBJECTCLASS)).andReturn(String.class);
		EasyMock.expect(ref.getProperty("the Answer to Life, the Universe, and Everything")).andReturn("42");
		EasyMock.expect(ref.getProperty(RSDPublisherProperties.PROP_REMOTE_PROTOCOL)).andReturn("https");
		EasyMock.expect(ref.getProperty(RSDPublisherProperties.PROP_REMOTE_PATH)).andReturn("/server/here");
		EasyMock.expect(ref.getProperty(RSDPublisherProperties.PROP_CONFIG_ID)).andReturn("org.eclipse.riena.configid");
		EasyMock.expect(ref.getBundle()).andReturn(Activator.getDefault().getBundle());
		EasyMock.replay(ref);

		final Object service = "Service";
		final RemoteServiceDescription rsd = new RemoteServiceDescription(ref, service, String.class);

		assertEquals("42", rsd.getProperty("the Answer to Life, the Universe, and Everything"));
		assertNotNull(rsd.getBundleName());
		assertEquals(Activator.getDefault().getBundle().getSymbolicName(), rsd.getBundleName());
		assertEquals("/server/here", rsd.getPath());
		assertEquals("https", rsd.getProtocol());
		assertTrue(service == rsd.getService());
		assertEquals(String.class.getName(), rsd.getServiceInterfaceClassName());
		assertNull(rsd.getURL());
		assertNull(rsd.getVersion());
		assertEquals(String.class, rsd.getServiceInterfaceClass());
		assertNotNull(rsd.getServiceRef());
		assertEquals(RemoteServiceDescription.State.REGISTERED, rsd.getState());
	}
}
