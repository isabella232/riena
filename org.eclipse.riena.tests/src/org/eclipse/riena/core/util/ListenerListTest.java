/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.util;

import java.util.EventListener;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.ridgets.listener.IWindowRidgetListener;

/**
 * Tests the <code>ListenerList</code>
 */
@NonUITestCase
public class ListenerListTest extends RienaTestCase {

	private ListenerList<TestListener> listenerList = new ListenerList<TestListener>(TestListener.class);

	public void testFresh() {
		EventListener[] list = listenerList.getListeners();
		assertNotNull(list);
		assertEquals(0, list.length);
		TestListener.pieps = 0;
		for (TestListener listener : listenerList.getListeners()) {
			listener.piep();
		}
		assertEquals(0, TestListener.pieps);
	}

	public void testAddOneRemoveOne() {
		EventListener[] list = listenerList.getListeners();
		assertNotNull(list);
		assertEquals(0, list.length);
		TestListener t = new TestListener();
		listenerList.add(t);
		assertEquals(1, listenerList.getListeners().length);
		TestListener.pieps = 0;
		for (TestListener listener : listenerList.getListeners()) {
			listener.piep();
		}
		assertEquals(listenerList.getListeners().length, TestListener.pieps);

		listenerList.remove(t);
		assertEquals(0, listenerList.getListeners().length);
	}

	public void testAddMoreRemoveAll() {
		EventListener[] list = listenerList.getListeners();
		assertNotNull(list);
		assertEquals(0, list.length);
		TestListener[] listeners = new TestListener[10];
		for (int i = 0; i < listeners.length; i++) {
			listeners[i] = new TestListener();
			listenerList.add(listeners[i]);
			TestListener.pieps = 0;
			for (TestListener listener : listenerList.getListeners()) {
				listener.piep();
			}
			assertEquals(listenerList.getListeners().length, TestListener.pieps);
		}
		for (int i = listeners.length - 1; i >= 0; i--) {
			listenerList.remove(listeners[i]);
			assertEquals(i, listenerList.getListeners().length);
			TestListener.pieps = 0;
			for (TestListener listener : listenerList.getListeners()) {
				listener.piep();
			}
			assertEquals(listenerList.getListeners().length, TestListener.pieps);
		}
		assertEquals(0, listenerList.getListeners().length);
	}

	/**
	 * Test a bug fix for an ArrayStoreException that was thrown when
	 * adding/removing several anonymous classes from the same listener list.
	 */
	public void testFixArrayStoreExceptionWithAnonymousClasses() {
		final ListenerList<IWindowRidgetListener> windowListenerList = new ListenerList<IWindowRidgetListener>(
				IWindowRidgetListener.class);

		IWindowRidgetListener listener1 = new IWindowRidgetListener() {
			public void activated() {
			}

			public void closed() {
			}
		};
		windowListenerList.add(listener1);

		IWindowRidgetListener listener2 = new IWindowRidgetListener() {
			public void activated() {
			}

			public void closed() {
			}
		};
		windowListenerList.add(listener2);

		windowListenerList.remove(listener1);
		windowListenerList.remove(listener2);
	}

	private static class TestListener implements EventListener {
		private static int pieps;

		public void piep() {
			pieps++;
		}
	}
}
