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
package org.eclipse.riena.core.ping;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests class {@link PingVisitor PingVisitor}.
 */
@NonUITestCase
public class PingVisitorTest extends RienaTestCase {

	protected static class PingMock extends DefaultPingable {
		private boolean pingCalled;
		private boolean expectPingCalled = true;
		private boolean fail;

		public void setExpectPingCalled(boolean expectation) {
			expectPingCalled = expectation;
		}

		public void letPingFail() {
			fail = true;
		}

		public void reset() {
			pingCalled = false;
			expectPingCalled = true;
			fail = false;
		}

		public void verify() {
			assertEquals(expectPingCalled, pingCalled);
		}

		/**
		 * @see DefaultPingable#ping(PingVisitor)
		 */
		@Override
		public PingVisitor ping(PingVisitor visitor) {
			pingCalled = true;
			if (fail) {
				throw new RuntimeException("ping");
			}
			return super.ping(visitor);
		}
	}

	protected static class PingMockWithPingMethods extends PingMock {
		private boolean pingdbCalled;
		private boolean expectPingDBCalled = true;
		private boolean pingDBFail;

		public void setExpectPingDBCalled(boolean expectation) {
			expectPingDBCalled = expectation;
		}

		protected void pingDB() {
			if (pingdbCalled) {
				throw new AssertionError("pingDB() already called on " + getPingFingerprint());
			}
			pingdbCalled = true;
			if (pingDBFail) {
				throw new RuntimeException("pingDB");
			}
		}

		public void letPingDBFail() {
			pingDBFail = true;
		}

		protected boolean isPingDBCalled() {
			return pingdbCalled;
		}

		/**
		 * @see PingMock#reset()
		 */
		@Override
		public void reset() {
			super.reset();
			pingdbCalled = false;
			expectPingDBCalled = true;
			pingDBFail = false;
		}

		/**
		 * @see PingMock#verify()
		 */
		@Override
		public void verify() {
			super.verify();
			assertEquals(expectPingDBCalled, pingdbCalled);
		}

		protected void pingarassabum() {
			throw new AssertionError("Not allowed");
		}
	}

	private PingMock mock1;
	private PingMock mock2;
	private PingMock mock3;
	private PingMock mock4;
	private PingMock mock5;

	/**
	 * Constructor for PingVisitorTest.
	 * 
	 * @param arg0
	 */
	public PingVisitorTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests method {@link PingVisitor#visit(IPingable) visit()} with a failure
	 * in method ping().
	 */
	public void testVisitWithFailureInPing() {
		mock4 = new PingMock();
		mock5 = new PingMock();
		mock3 = new PingMockWithPingMethods() {
			@SuppressWarnings("unused")
			private IPingable pingable1 = mock4;
			@SuppressWarnings("unused")
			private IPingable pingable2 = mock5;
		};
		mock2 = new PingMock();
		mock1 = new PingMock() {
			@SuppressWarnings("unused")
			private IPingable pingable1 = mock2;
			@SuppressWarnings("unused")
			private IPingable pingable2 = mock3;
		};

		mock4.letPingFail();

		PingVisitor visitor = new PingVisitor();
		try {
			mock1.ping(visitor);
			fail("Expected failure");
		} catch (RuntimeException e) {
			assertEquals("ping", e.getMessage());
		}
	}

	/**
	 * Tests method {@link PingVisitor#visit(IPingable) visit()} with a failure
	 * in method pingDB().
	 */
	public void testVisitWithFailureInPingMethod() {
		mock4 = new PingMock();
		mock5 = new PingMock();
		mock3 = new PingMockWithPingMethods() {
			@SuppressWarnings("unused")
			private IPingable pingable1 = mock4;
			@SuppressWarnings("unused")
			private IPingable pingable2 = mock5;
		};
		mock2 = new PingMock();
		mock1 = new PingMock() {
			@SuppressWarnings("unused")
			private IPingable pingable1 = mock2;
			@SuppressWarnings("unused")
			private IPingable pingable2 = mock3;
		};

		((PingMockWithPingMethods) mock3).letPingDBFail();

		PingVisitor visitor = new PingVisitor();
		try {
			mock1.ping(visitor);
			fail("Expected failure");
		} catch (RuntimeException e) {
			assertTrue(e.getCause().toString(), e.getCause() instanceof InvocationTargetException);
			assertEquals("pingDB", ((InvocationTargetException) e.getCause()).getCause().getMessage());
		}
	}

	/**
	 * Tests method {@link PingVisitor#visit(IPingable) visit()} with a cycle.
	 */
	public void testVisitWithCycle() {
		mock4 = new PingMock();
		mock5 = new PingMock() {
			// cycle
			@SuppressWarnings("unused")
			private IPingable pingable1 = this;
		};
		mock3 = new PingMockWithPingMethods() {
			@SuppressWarnings("unused")
			private IPingable pingable1 = mock4;
			@SuppressWarnings("unused")
			private IPingable pingable2 = mock5;
		};
		mock2 = new PingMock() {
			// cycle
			@SuppressWarnings("unused")
			private IPingable pingable1 = this;
		};
		mock1 = new PingMock() {
			@SuppressWarnings("unused")
			private IPingable pingable1 = mock2;
			@SuppressWarnings("unused")
			private IPingable pingable2 = mock3;
		};

		PingVisitor visitor = new PingVisitor();
		mock1.ping(visitor);

		mock1.verify();
		mock2.verify();
		mock3.verify();
		mock4.verify();
		mock5.verify();
	}

	/**
	 * Tests method {@link PingVisitor#visit(IPingable) visit()}.
	 */
	public void testVisit() {
		mock4 = new PingMock();
		mock5 = new PingMock();
		mock3 = new PingMockWithPingMethods() {
			@SuppressWarnings("unused")
			private IPingable pingable1 = mock4;

			@SuppressWarnings("unused")
			private Iterable<IPingable> getAdditionalPingables() {
				return Arrays.asList(new IPingable[] { mock5 });
			}
		};
		mock2 = new PingMock();
		mock1 = new PingMock() {
			@SuppressWarnings("unused")
			private IPingable pingable1 = mock2;
			@SuppressWarnings("unused")
			private IPingable pingable2 = mock3;
		};

		PingVisitor visitor = new PingVisitor();
		mock1.ping(visitor);

		mock1.verify();
		mock2.verify();
		mock3.verify();
		mock4.verify();
		mock5.verify();
	}

	/**
	 * Tests method {@link PingVisitor#visit(IPingable) visit()}.
	 */
	public void testVisitorReturnsThis() {
		mock1 = new PingMock();
		PingVisitor visitor = new PingVisitor();

		PingVisitor result = mock1.ping(visitor);

		assertSame(visitor, result);
		mock1.verify();
	}

	/**
	 * Tests method {@link PingVisitor#getChildPingablesOf(IPingable)
	 * getChildPingablesOf()}.
	 */
	public void testGetChildPingablesOf() {
		mock4 = new PingMock();
		mock5 = new PingMock();
		mock3 = new PingMock();
		mock2 = new PingMock();
		mock1 = new PingMockWithPingMethods() {
			@SuppressWarnings("unused")
			private IPingable pingable1 = mock2;
			@SuppressWarnings("unused")
			private IPingable pingable2 = mock3;

			@SuppressWarnings("unused")
			private Iterable<IPingable> getAdditionalPingables() {
				return Arrays.asList(new IPingable[] { mock4, mock5 });
			}
		};

		PingVisitor visitor = new PingVisitor();
		Collection<IPingable> set = visitor.getChildPingablesOf(mock1);

		assertEquals(5, set.size());
		assertTrue(set.remove(mock2));
		assertTrue(set.remove(mock3));
		assertTrue(set.remove(mock4));
		assertTrue(set.remove(mock5));

		IPingable pingable = set.iterator().next();
		assertTrue(pingable instanceof PingMethodAdapter);
		pingable.ping(visitor);
		assertTrue(((PingMockWithPingMethods) mock1).isPingDBCalled());
	}

	/**
	 * Tests method {@link PingVisitor#collectPingMethods(IPingable, Set)
	 * collectPingMethods()}.
	 */
	public void testCollectPingMethods() {
		PingVisitor visitor = new PingVisitor();
		PingMockWithPingMethods mock = new PingMockWithPingMethods();
		Set<IPingable> set = new HashSet<IPingable>();

		visitor.collectPingMethods(mock, set);

		assertEquals(1, set.size());
		set.iterator().next().ping(visitor);
		assertEquals(true, mock.isPingDBCalled());
	}

	/**
	 * Tests method {@link PingVisitor#collectPingableMembers(IPingable, Set)
	 * collectPingableMembers()}.
	 */
	public void testCollectPingableMembers() {
		mock3 = new PingMock();
		mock2 = new PingMock();
		PingMock mock1 = new PingMock() {
			@SuppressWarnings("unused")
			private IPingable pingable1 = mock2;
			@SuppressWarnings("unused")
			private IPingable pingable2 = mock3;
		};

		Set<IPingable> set = new HashSet<IPingable>();
		PingVisitor visitor = new PingVisitor();
		visitor.collectPingableMembers(mock1, set);

		assertEquals(2, set.size());
		assertTrue(set.contains(mock2));
		assertTrue(set.contains(mock3));
	}

	/**
	 * Tests method
	 * {@link PingVisitor#collectAdditionalPingables(IPingable, Set)
	 * collectAdditionalPingables()}.
	 */
	public void testCollectAdditionalPingables() {
		mock3 = new PingMock();
		mock2 = new PingMock();
		PingMock mock1 = new PingMock() {
			@SuppressWarnings("unused")
			private Iterable<IPingable> getAdditionalPingables() {
				return Arrays.asList(new IPingable[] { mock2, mock3 });
			}
		};

		Set<IPingable> set = new HashSet<IPingable>();
		PingVisitor visitor = new PingVisitor();
		visitor.collectAdditionalPingables(mock1, set);

		assertEquals(2, set.size());
		assertTrue(set.contains(mock2));
		assertTrue(set.contains(mock3));
	}

	/**
	 * Tests method
	 * {@link PingVisitor#collectAdditionalPingables(IPingable, Set)
	 * collectAdditionalPingables()}.
	 */
	public void testCollectAdditionalPingablesWithWildcards() {
		mock3 = new PingMock();
		mock2 = new PingMock();
		PingMock mock1 = new PingMock() {
			@SuppressWarnings("unused")
			private Iterable<? extends IPingable> getAdditionalPingables() {
				return Arrays.asList(new IPingable[] { mock2, mock3 });
			}
		};

		Set<IPingable> set = new HashSet<IPingable>();
		PingVisitor visitor = new PingVisitor();
		visitor.collectAdditionalPingables(mock1, set);

		assertEquals(2, set.size());
		assertTrue(set.contains(mock2));
		assertTrue(set.contains(mock3));
	}

	/**
	 * Tests method {@link PingVisitor#getPingResults(IPingable)
	 * getPingResults()}.
	 */
	public void testGetGetPingResults() {
		mock4 = new PingMock();
		mock5 = new PingMock();
		mock3 = new PingMockWithPingMethods() {
			@SuppressWarnings("unused")
			private IPingable pingable1 = mock4;

			@SuppressWarnings("unused")
			private Iterable<IPingable> getAdditionalPingables() {
				return Arrays.asList(new IPingable[] { mock5 });
			}
		};
		mock2 = new PingMock();
		mock1 = new PingMock() {
			@SuppressWarnings("unused")
			private IPingable pingable2 = mock3;
			@SuppressWarnings("unused")
			private IPingable pingable1 = mock2;
		};

		PingVisitor visitor = new PingVisitor();
		mock1.ping(visitor);

		List<PingResult> pingResult = visitor.getPingResults();
		assertNotNull(pingResult);
		assertEquals(pingResult.toString(), 6, pingResult.size());
		List<PingFingerprint> fingerprints = getFingerprints(pingResult);
		assertTrue(fingerprints.remove(mock1.getPingFingerprint()));
		assertTrue(fingerprints.remove(mock2.getPingFingerprint()));
		assertTrue(fingerprints.remove(mock3.getPingFingerprint()));
		assertTrue(fingerprints.remove(mock4.getPingFingerprint()));
		assertTrue(fingerprints.remove(mock5.getPingFingerprint()));
		assertTrue(fingerprints.get(0).toString().contains("#pingDB"));
	}

	private List<PingFingerprint> getFingerprints(List<PingResult> list) {
		List<PingFingerprint> result = new ArrayList<PingFingerprint>();
		for (PingResult pingResult : list) {
			result.add(pingResult.getPingFingerprint());
		}
		return result;
	}
}
