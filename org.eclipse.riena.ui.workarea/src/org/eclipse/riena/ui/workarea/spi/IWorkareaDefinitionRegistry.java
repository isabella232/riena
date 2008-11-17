package org.eclipse.riena.ui.workarea.spi;

import org.eclipse.riena.ui.workarea.IWorkareaDefinition;

public interface IWorkareaDefinitionRegistry {

	IWorkareaDefinition getDefinition(Object id);
	
	IWorkareaDefinition register(Object id, IWorkareaDefinition definition);
}
