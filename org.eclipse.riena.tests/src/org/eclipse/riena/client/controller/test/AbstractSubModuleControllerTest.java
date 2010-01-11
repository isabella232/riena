/**
 * 
 */
package org.eclipse.riena.client.controller.test;

import org.easymock.EasyMock;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.core.RienaStatus;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.INavigationProcessor;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 * Abstract class for SubModuleController testing.
 */
@NonUITestCase
public abstract class AbstractSubModuleControllerTest<C extends SubModuleController> extends RienaTestCase {

	private C controller;
	private INavigationProcessor mockNavigationProcessor = EasyMock.createMock(INavigationProcessor.class);

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		System.getProperties().put(RienaStatus.RIENA_TEST_SYSTEM_PROPERTY, "true");

		// only used to get the initial mappings
		SwtControlRidgetMapper.getInstance();

		Display display = Display.getDefault();
		Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm);

		ModuleNode module = new ModuleNode();
		SubModuleNode node = new SubModuleNode();
		node.setParent(module);
		module.addChild(node);
		node.setNavigationProcessor(getMockNavigationProcessor());
		controller = createController(node);

		controller.configureRidgets();
		controller.afterBind();

	}

	protected C getController() {
		return controller;
	}

	protected INavigationProcessor getMockNavigationProcessor() {
		return mockNavigationProcessor;
	}

	protected abstract C createController(ISubModuleNode node);
}
