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
package org.eclipse.riena.core.exceptionmanager;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.eclipse.riena.internal.core.exceptionmanager.TopologicalNode;
import org.eclipse.riena.internal.core.exceptionmanager.TopologicalSort;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests the Topological sorting
 */
@NonUITestCase
public class TopologicalSortTest extends TestCase {

	public void testSortEmpty() {
		final List<TopologicalNode<Integer>> nodes = new ArrayList<TopologicalNode<Integer>>();
		final List<Integer> result = TopologicalSort.sort(nodes);

		Assert.assertNotNull(result);
		Assert.assertEquals("expected size", 0, result.size());
	}

	public void testSortSimple() {
		final List<TopologicalNode<Integer>> nodes = new ArrayList<TopologicalNode<Integer>>();
		final TopologicalNode<Integer> node = new TopologicalNode<Integer>("shoes", null, 1);
		nodes.add(node);
		final List<Integer> result = TopologicalSort.sort(nodes);

		Assert.assertNotNull(result);
		Assert.assertEquals("expected size", 1, result.size());
		Assert.assertEquals("expected type", Integer.class, result.get(0).getClass());
	}

	public void testSortTwo() {
		final List<TopologicalNode<Integer>> nodes = new ArrayList<TopologicalNode<Integer>>();
		TopologicalNode<Integer> node = new TopologicalNode<Integer>("shoes", null, 1);
		nodes.add(node);
		node = new TopologicalNode<Integer>("socks", "shoes", 2);
		nodes.add(node);

		final List<Integer> result = TopologicalSort.sort(nodes);

		Assert.assertNotNull(result);
		Assert.assertEquals("expected size", 2, result.size());
		Assert.assertEquals("expected element", 2, (int) result.get(0));
		Assert.assertEquals("expected element", 1, (int) result.get(1));
	}

	public void testSortTwoStar() {
		final List<TopologicalNode<Integer>> nodes = new ArrayList<TopologicalNode<Integer>>();
		TopologicalNode<Integer> node = new TopologicalNode<Integer>("shoes", null, 1);
		nodes.add(node);
		node = new TopologicalNode<Integer>("socks", "*", 2);
		nodes.add(node);

		final List<Integer> result = TopologicalSort.sort(nodes);

		Assert.assertNotNull(result);
		Assert.assertEquals("expected size", 2, result.size());
		Assert.assertEquals("expected element", 2, (int) result.get(0));
		Assert.assertEquals("expected element", 1, (int) result.get(1));
	}

	public void testSortTwoSwitched() {
		final List<TopologicalNode<Integer>> nodes = new ArrayList<TopologicalNode<Integer>>();
		TopologicalNode<Integer> node;
		node = new TopologicalNode<Integer>("socks", "shoes", 2);
		nodes.add(node);
		node = new TopologicalNode<Integer>("shoes", null, 1);
		nodes.add(node);

		final List<Integer> result = TopologicalSort.sort(nodes);

		Assert.assertNotNull(result);
		Assert.assertEquals("expected size", 2, result.size());
		Assert.assertEquals("expected element", 2, (int) result.get(0));
		Assert.assertEquals("expected element", 1, (int) result.get(1));
	}

	public void testSortTwoStarSwitched() {
		final List<TopologicalNode<Integer>> nodes = new ArrayList<TopologicalNode<Integer>>();
		TopologicalNode<Integer> node;
		node = new TopologicalNode<Integer>("socks", "*", 2);
		nodes.add(node);
		node = new TopologicalNode<Integer>("shoes", null, 1);
		nodes.add(node);

		final List<Integer> result = TopologicalSort.sort(nodes);

		Assert.assertNotNull(result);
		Assert.assertEquals("expected size", 2, result.size());
		Assert.assertEquals("expected element", 2, (int) result.get(0));
		Assert.assertEquals("expected element", 1, (int) result.get(1));
	}

	public void testSortTwoPointOne() {
		final List<TopologicalNode<Integer>> nodes = new ArrayList<TopologicalNode<Integer>>();
		TopologicalNode<Integer> node = new TopologicalNode<Integer>("shoes", null, 1);
		nodes.add(node);
		node = new TopologicalNode<Integer>("socks", "shoes", 2);
		nodes.add(node);
		node = new TopologicalNode<Integer>("pant", "shoes", 3);
		nodes.add(node);

		final List<Integer> result = TopologicalSort.sort(nodes);

		Assert.assertNotNull(result);
		Assert.assertEquals("expected size", 3, result.size());
		Assert.assertEquals("expected element", 2, (int) result.get(0));
		Assert.assertEquals("expected element", 3, (int) result.get(1));
		Assert.assertEquals("expected element", 1, (int) result.get(2));
	}

	public void testSortTwoPointOneStar() {
		final List<TopologicalNode<Integer>> nodes = new ArrayList<TopologicalNode<Integer>>();
		TopologicalNode<Integer> node = new TopologicalNode<Integer>("shoes", null, 1);
		nodes.add(node);
		node = new TopologicalNode<Integer>("socks", "*", 2);
		nodes.add(node);
		node = new TopologicalNode<Integer>("pant", "shoes", 3);
		nodes.add(node);

		final List<Integer> result = TopologicalSort.sort(nodes);

		Assert.assertNotNull(result);
		Assert.assertEquals("expected size", 3, result.size());
		Assert.assertEquals("expected element", 2, (int) result.get(0));
		Assert.assertEquals("expected element", 3, (int) result.get(1));
		Assert.assertEquals("expected element", 1, (int) result.get(2));
	}

	public void testSortTwoPointPoint() {
		final List<TopologicalNode<Integer>> nodes = new ArrayList<TopologicalNode<Integer>>();
		TopologicalNode<Integer> node;
		node = new TopologicalNode<Integer>("shoes", "socks", 3);
		nodes.add(node);
		node = new TopologicalNode<Integer>("socks", "shoes", 2);
		nodes.add(node);

		final List<Integer> result = TopologicalSort.sort(nodes);

		Assert.assertNotNull(result);
		Assert.assertEquals("expected size", 2, result.size());
		Assert.assertTrue("expected element", result.contains(2));
		Assert.assertTrue("expected element", result.contains(3));
	}

	public void testSortTwoOrphans() {
		final List<TopologicalNode<Integer>> nodes = new ArrayList<TopologicalNode<Integer>>();
		TopologicalNode<Integer> node;
		node = new TopologicalNode<Integer>("socks", null, 2);
		nodes.add(node);
		node = new TopologicalNode<Integer>("shoes", null, 3);
		nodes.add(node);

		final List<Integer> result = TopologicalSort.sort(nodes);

		Assert.assertNotNull(result);
		Assert.assertEquals("expected size", 2, result.size());
	}
}
