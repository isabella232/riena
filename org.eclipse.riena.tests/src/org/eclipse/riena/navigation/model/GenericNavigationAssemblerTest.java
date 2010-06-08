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
package org.eclipse.riena.navigation.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.extension.IModuleGroupNode2Extension;
import org.eclipse.riena.navigation.extension.IModuleNode2Extension;
import org.eclipse.riena.navigation.extension.INode2Extension;
import org.eclipse.riena.navigation.extension.ISubApplicationNode2Extension;
import org.eclipse.riena.navigation.extension.ISubModuleNode2Extension;
import org.eclipse.riena.navigation.extension.ModuleGroupNode2Extension;
import org.eclipse.riena.navigation.extension.ModuleNode2Extension;
import org.eclipse.riena.navigation.extension.SubApplicationNode2Extension;
import org.eclipse.riena.navigation.extension.SubModuleNode2Extension;

/**
 * Tests of the class {@link GenericNavigationAssembler}.
 */
@NonUITestCase
public class GenericNavigationAssemblerTest extends TestCase {

	/**
	 * Tests the method {@code createNavigationNodeIdFromTemplate}.
	 */
	public void testCreateNavigationNodeIdFromTemplate() {

		MyGenericNavigationAssembler assembler = new MyGenericNavigationAssembler();
		NavigationNodeId template = new NavigationNodeId("type1", "inst1");
		ModuleNode2Extension extension = new ModuleNode2Extension();
		extension.setNodeId("type2");
		NavigationNodeId createdId = assembler.createNavigationNodeIdFromTemplate(template, extension, null);
		assertEquals("type2", createdId.getTypeId());
		assertEquals("inst1", createdId.getInstanceId());

	}

	/**
	 * Tests the method {@code build(ISubModuleNode2Extension...)}.
	 */
	public void testBuildISubModuleNode2Extension() {

		MyGenericNavigationAssembler assembler = new MyGenericNavigationAssembler();
		NavigationNodeId targetId = new NavigationNodeId("type1", "inst1");

		SubModuleNode2Extension extension1 = new SubModuleNode2Extension();
		extension1.setNodeId("type2");
		extension1.setName("sub1");
		extension1.setSelectable(false);
		extension1.setIcon("icon1");

		SubModuleNode2Extension extension2 = new SubModuleNode2Extension();
		extension2.setNodeId("type22");
		extension2.setName("sub2");
		extension2.setSelectable(true);
		extension2.setIcon("icon2");

		extension1.setChildNodes(new ISubModuleNode2Extension[] { extension2 });

		Map<String, Object> context = new HashMap<String, Object>();
		ISubModuleNode sm1 = assembler.build(extension1, targetId, null, context);

		assertEquals("sub1", sm1.getLabel());
		assertFalse(sm1.isSelectable());
		assertEquals("icon1", sm1.getIcon());
		assertEquals("type2", sm1.getNodeId().getTypeId());
		assertEquals("inst1", sm1.getNodeId().getInstanceId());
		assertEquals(1, sm1.getChildren().size());
		ISubModuleNode sm2 = sm1.getChild(0);
		assertEquals("sub2", sm2.getLabel());
		assertTrue(sm2.isSelectable());
		assertEquals("icon2", sm2.getIcon());
		assertEquals("type22", sm2.getNodeId().getTypeId());
		assertEquals("inst1", sm2.getNodeId().getInstanceId());

	}

	/**
	 * Tests the method {@code build(IModuleNode2Extension...)}.
	 */
	public void testBuildIModuleNode2Extension() {

		MyGenericNavigationAssembler assembler = new MyGenericNavigationAssembler();
		NavigationNodeId targetId = new NavigationNodeId("type1", "inst1");

		ModuleNode2Extension extension1 = new ModuleNode2Extension();
		extension1.setNodeId("type2");
		extension1.setName("m1");
		extension1.setIcon("icon1");

		SubModuleNode2Extension subExtension1 = new SubModuleNode2Extension();
		subExtension1.setNodeId("type12");
		subExtension1.setName("sub1");

		SubModuleNode2Extension subExtension2 = new SubModuleNode2Extension();
		subExtension2.setNodeId("type22");
		subExtension2.setName("sub2");

		extension1.setChildNodes(new ISubModuleNode2Extension[] { subExtension1, subExtension2 });

		Map<String, Object> context = new HashMap<String, Object>();
		IModuleNode m1 = assembler.build(extension1, targetId, null, context);

		assertEquals("m1", m1.getLabel());
		assertEquals("icon1", m1.getIcon());
		assertEquals("type2", m1.getNodeId().getTypeId());
		assertEquals("inst1", m1.getNodeId().getInstanceId());
		assertEquals(2, m1.getChildren().size());

		ISubModuleNode sm1 = m1.getChild(0);
		assertEquals("sub1", sm1.getLabel());
		assertEquals("type12", sm1.getNodeId().getTypeId());
		assertEquals("inst1", sm1.getNodeId().getInstanceId());

		ISubModuleNode sm2 = m1.getChild(1);
		assertEquals("sub2", sm2.getLabel());
		assertEquals("type22", sm2.getNodeId().getTypeId());
		assertEquals("inst1", sm2.getNodeId().getInstanceId());

	}

	/**
	 * Tests the method {@code build(IModuleGroupNode2Extension...)}.
	 */
	public void testBuildIModuleGroupNode2Extension() {

		MyGenericNavigationAssembler assembler = new MyGenericNavigationAssembler();
		NavigationNodeId targetId = new NavigationNodeId("type1", "inst1");

		ModuleGroupNode2Extension extension1 = new ModuleGroupNode2Extension();
		extension1.setNodeId("type2");
		extension1.setName("mg1");
		extension1.setIcon("icon1");

		ModuleNode2Extension mExtension1 = new ModuleNode2Extension();
		mExtension1.setNodeId("type22");
		mExtension1.setName("m2");
		mExtension1.setIcon("icon2");

		extension1.setChildNodes(new IModuleNode2Extension[] { mExtension1 });

		Map<String, Object> context = new HashMap<String, Object>();
		IModuleGroupNode mg1 = assembler.build(extension1, targetId, null, context);

		assertEquals("mg1", mg1.getLabel());
		assertEquals("icon1", mg1.getIcon());
		assertEquals("type2", mg1.getNodeId().getTypeId());
		assertEquals("inst1", mg1.getNodeId().getInstanceId());
		assertEquals(1, mg1.getChildren().size());

		IModuleNode m1 = mg1.getChild(0);
		assertEquals("m2", m1.getLabel());
		assertEquals("icon2", m1.getIcon());
		assertEquals("type22", m1.getNodeId().getTypeId());
		assertEquals("inst1", m1.getNodeId().getInstanceId());

	}

	/**
	 * Tests the method {@code build(ISubApplicationNode2Extension...)}.
	 */
	public void testBuildISubApplicationNode2Extension() {

		MyGenericNavigationAssembler assembler = new MyGenericNavigationAssembler();
		NavigationNodeId targetId = new NavigationNodeId("type1", "inst1");

		SubApplicationNode2Extension extension1 = new SubApplicationNode2Extension();
		extension1.setNodeId("type2");
		extension1.setName("sa1");
		extension1.setIcon("icon1");
		extension1.setPerspectiveId("p1");

		ModuleGroupNode2Extension mgExtension1 = new ModuleGroupNode2Extension();
		mgExtension1.setNodeId("type22");
		mgExtension1.setName("mg2");
		mgExtension1.setIcon("icon2");

		extension1.setChildNodes(new IModuleGroupNode2Extension[] { mgExtension1 });

		Map<String, Object> context = new HashMap<String, Object>();
		ISubApplicationNode sa1 = assembler.build(extension1, targetId, null, context);
		assertEquals("sa1", sa1.getLabel());
		assertEquals("icon1", sa1.getIcon());
		assertEquals("type2", sa1.getNodeId().getTypeId());
		assertEquals("inst1", sa1.getNodeId().getInstanceId());
		assertEquals(1, sa1.getChildren().size());

		IModuleGroupNode mg1 = sa1.getChild(0);
		assertEquals("mg2", mg1.getLabel());
		assertEquals("icon2", mg1.getIcon());
		assertEquals("type22", mg1.getNodeId().getTypeId());
		assertEquals("inst1", mg1.getNodeId().getInstanceId());

	}

	/**
	 * Tests all {@code resolveTargetIds} methods.
	 */
	public void testResolveTargetIds() {

		MyGenericNavigationAssembler assembler = new MyGenericNavigationAssembler();

		SubApplicationNode2Extension extension1 = new SubApplicationNode2Extension();
		extension1.setNodeId("sa1");

		ModuleGroupNode2Extension extension2 = new ModuleGroupNode2Extension();
		extension2.setNodeId("mg1");
		extension1.setChildNodes(new IModuleGroupNode2Extension[] { extension2 });

		ModuleNode2Extension extension3 = new ModuleNode2Extension();
		extension3.setNodeId("m1");
		extension2.setChildNodes(new IModuleNode2Extension[] { extension3 });

		SubModuleNode2Extension extension4 = new SubModuleNode2Extension();
		extension4.setNodeId("sub1");
		extension3.setChildNodes(new ISubModuleNode2Extension[] { extension4 });

		SubModuleNode2Extension extension5 = new SubModuleNode2Extension();
		extension5.setNodeId("sub11");

		SubModuleNode2Extension extension6 = new SubModuleNode2Extension();
		extension6.setNodeId("sub12");
		extension4.setChildNodes(new ISubModuleNode2Extension[] { extension5, extension6 });

		ReflectionUtils.invokeHidden(assembler, "resolveTargetIds", extension1);

		assertTrue(assembler.acceptedTargetIdsContains("sa1"));
		assertTrue(assembler.acceptedTargetIdsContains("mg1"));
		assertTrue(assembler.acceptedTargetIdsContains("m1"));
		assertTrue(assembler.acceptedTargetIdsContains("sub11"));
		assertTrue(assembler.acceptedTargetIdsContains("sub11"));
		assertTrue(assembler.acceptedTargetIdsContains("sub12"));
		assertFalse(assembler.acceptedTargetIdsContains("dummy"));

	}

	/**
	 * Assembler with a simple created set of target IDs. Also the visibility of
	 * some protected methods is changed for testing.
	 */
	private class MyGenericNavigationAssembler extends GenericNavigationAssembler {

		public MyGenericNavigationAssembler() {
			acceptedTargetIds = new HashSet<String>();
		}

		@Override
		public void updateAcceptedTargetIds(final String typeId) {
			if (typeId != null) {
				acceptedTargetIds.add(typeId);
			}
		}

		public boolean acceptedTargetIdsContains(String typeId) {
			return acceptedTargetIds.contains(typeId);
		}

		@Override
		public NavigationNodeId createNavigationNodeIdFromTemplate(NavigationNodeId template,
				INode2Extension nodeExtension, NavigationArgument navigationArgument) {
			return super.createNavigationNodeIdFromTemplate(template, nodeExtension, navigationArgument);
		}

		@Override
		public IModuleNode build(IModuleNode2Extension moduleDefinition, NavigationNodeId targetId,
				NavigationArgument navigationArgument, Map<String, Object> context) {
			return super.build(moduleDefinition, targetId, navigationArgument, context);
		}

		@Override
		public IModuleGroupNode build(IModuleGroupNode2Extension groupDefinition, NavigationNodeId targetId,
				NavigationArgument navigationArgument, Map<String, Object> context) {
			return super.build(groupDefinition, targetId, navigationArgument, context);
		}

		@Override
		public ISubApplicationNode build(ISubApplicationNode2Extension subApplicationDefinition,
				NavigationNodeId targetId, NavigationArgument navigationArgument, Map<String, Object> context) {
			return super.build(subApplicationDefinition, targetId, navigationArgument, context);
		}

		@Override
		public ISubModuleNode build(ISubModuleNode2Extension subModuleDefinition, NavigationNodeId targetId,
				NavigationArgument navigationArgument, Map<String, Object> context) {
			return super.build(subModuleDefinition, targetId, navigationArgument, context);
		}

	}

}
