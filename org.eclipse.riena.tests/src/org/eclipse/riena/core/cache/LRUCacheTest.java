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
package org.eclipse.riena.core.cache;

import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.ManualTestCase;

/**
 * Tests the LRUCache class.
 * 
 */
@ManualTestCase
public class LRUCacheTest extends RienaTestCase {

	private IGenericObjectCache<String, Integer> genericCache;
	private IGenericObjectCache<Integer, TestRunner> genericCache2;
	private IGenericObjectCache<Integer, String> genericCache3;

	public void setUp() throws Exception {
		super.setUp();
		genericCache = new LRUCache<String, Integer>();
	}

	public void tearDown() throws Exception {
		genericCache = null;
		super.tearDown();
	}

	/**
	 * test basic instantiation
	 */
	public void testInstantiate() {
		genericCache.put("test", Integer.valueOf(2));
		assertNotNull("did not find put object in cache", genericCache.get("test"));
		assertTrue("object in cache has incorrect value", genericCache.get("test").equals(Integer.valueOf(2)));
		genericCache.clear();
	}

	/**
	 * @throws Exception
	 */
	public void testTimeout() throws Exception {
		genericCache.put("test", Integer.valueOf(3));
		genericCache.setTimeout(500);
		Thread.sleep(100);
		assertNotNull("did not find put object in cache", genericCache.get("test"));
		Thread.sleep(600);
		assertNull("must not find object in cache after timeout", genericCache.get("test"));
		genericCache.clear();
	}

	/**
	 * @throws Exception
	 */
	public void testPutMultiple() throws Exception {
		genericCache3 = new LRUCache<Integer, String>();
		for (int i = 0; i < 30; i++) {
			genericCache3.put(Integer.valueOf(i), "testvalue");
		}

		for (int i = 0; i < 30; i++) {
			assertNotNull("object not found in cache", genericCache3.get(Integer.valueOf(i)));
		}
		genericCache.clear();
	}

	/**
	 * @throws Exception
	 */
	public void testMultiThread() throws Exception {
		genericCache2 = new LRUCache<Integer, TestRunner>();
		Thread[] runner = new Thread[10];
		for (int i = 0; i < 10; i++) {
			runner[i] = new TestRunner(i * 20, i * 20 + 19, 10);
		}
		runAndCheckThreads(runner);
		genericCache2.clear();
	}

	/**
	 * @throws Exception
	 */
	public void testMultiThreadSameRange() throws Exception {
		genericCache2 = new LRUCache<Integer, TestRunner>();
		Thread[] runner = new Thread[10];
		for (int i = 0; i < 10; i++) {
			runner[i] = new TestRunner(0, 20, 10);
		}
		runAndCheckThreads(runner);
		genericCache2.clear();
	}

	/**
	 * @throws Exception
	 */
	public void testMultiThreadLargeNoOfThreadsSameRange() throws Exception {
		genericCache2 = new LRUCache<Integer, TestRunner>();
		Thread[] runner = new Thread[100];
		for (int i = 0; i < 100; i++) {
			runner[i] = new TestRunner(0, 20, 100);
		}
		runAndCheckThreads(runner);
		genericCache2.clear();
	}

	/**
	 * @throws Exception
	 */
	public void testMultiThreadLargeNoOfThreads() throws Exception {
		genericCache2 = new LRUCache<Integer, TestRunner>();
		Thread[] runner = new Thread[100];
		for (int i = 0; i < 100; i++) {
			runner[i] = new TestRunner(i * 20, i * 20 + 19, 100);
		}
		runAndCheckThreads(runner);
		genericCache2.clear();
	}

	private void runAndCheckThreads(Thread[] runner) {
		for (int i = 0; i < runner.length; i++) {
			//			trace("starting thread " + i);
			runner[i].start();
		}
		int count = 0; // number of running threads
		boolean first = true;
		Thread.yield();
		while (count != 0 || first) {
			count = 0;
			for (int i = 0; i < runner.length; i++) {
				if (runner[i].isAlive()) {
					count++;
				}
			}
			first = false;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// no action
			}

		}
	}

	class TestRunner extends Thread {
		private int lowRange;
		private int highRange;
		private int runs;

		TestRunner(int lowRange, int highRange, int runs) {
			this.lowRange = lowRange;
			this.highRange = highRange;
			this.runs = runs;
			//			trace("low=" + lowRange + " high=" + highRange + " runs=" + runs);
		}

		public void run() {
			for (int i = 0; i < runs; i++) {
				for (int k = lowRange; k <= highRange; k++) {
					genericCache2.put(Integer.valueOf(k), this);
				}
				for (int k = lowRange; k <= highRange; k++) {
					assertNotNull("object not found in cache", genericCache2.get(Integer.valueOf(k)));
				}
			}
		}
	}
}