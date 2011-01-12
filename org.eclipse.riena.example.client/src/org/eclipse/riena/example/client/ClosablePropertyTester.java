package org.eclipse.riena.example.client;

import org.eclipse.core.expressions.PropertyTester;

import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;

public class ClosablePropertyTester extends PropertyTester {

	public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
		if (property.equals("isClosable")) { //$NON-NLS-1$
			if (receiver instanceof SubModuleNode) {
				return ((SubModuleNode) receiver).isClosable();
			} else {
				return ((ModuleNode) receiver).isClosable();
			}
		}
		return false;
	}

}
