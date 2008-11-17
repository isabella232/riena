package org.eclipse.riena.internal.ui.workarea.registry;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.spi.IWorkareaDefinitionRegistry;

class ExplicitWorkareaDefinitionRegistry implements IWorkareaDefinitionRegistry {

	private static final ExplicitWorkareaDefinitionRegistry instance = new ExplicitWorkareaDefinitionRegistry(); 
	
	private Map<Object,IWorkareaDefinition> workareas = new HashMap<Object, IWorkareaDefinition>();  
	
	static ExplicitWorkareaDefinitionRegistry getInstance() {
		return instance;
	}
	
	private ExplicitWorkareaDefinitionRegistry() {
	}
	
	public IWorkareaDefinition getDefinition(Object id) {

		return workareas.get(id);
	}

	public IWorkareaDefinition register(Object id, IWorkareaDefinition definition) {

		if (id instanceof ISubModuleNode) {
			return registerDefinition((ISubModuleNode)id, definition);
		} else {
			return workareas.put(id, definition);
		}
	}
	
	private IWorkareaDefinition registerDefinition(ISubModuleNode submodule, IWorkareaDefinition definition) {
		
		// this may be specific - register with submodule
		workareas.put(submodule, definition);
		if (submodule.getNodeId() != null || submodule.getNodeId().getTypeId() != null) {
			if (getDefinition(submodule.getNodeId().getTypeId()) == null) {
				// if id is not registered yet register for all potential users
				// TODO use method below?
				workareas.put(submodule.getNodeId().getTypeId(), definition);
			}
			// TODO throw exception?
		}

		return definition;
	}
}
