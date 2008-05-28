package org.eclipse.riena.navigation.ui.swt.component;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.ModuleNodeAdapter;
import org.eclipse.swt.widgets.Composite;

public class ModuleNavigationComponent extends AbstractNavigationComponent<IModuleNode> {

	private Map<ISubModuleNode, SubModuleNavigationComponent> subModuleNodeComponents;
	private ModuleNodeObserver observer;
	private ModuleWidget ui;
	private ModuleGroupNavigationComponent groupComponent;

	public ModuleNavigationComponent(IModuleNode node, ModuleGroupNavigationComponent groupComponent) {
		super(node, null);
		this.groupComponent = groupComponent;
		initUI();
		buildInitialTree();
	}

	protected ModuleGroupNavigationComponent getGroupComponent() {
		return groupComponent;
	}

	private void initializeSMNodeMapping() {
		this.subModuleNodeComponents = new HashMap<ISubModuleNode, SubModuleNavigationComponent>();
	}

	protected void addMapping(ISubModuleNode node, SubModuleNavigationComponent cmp) {
		subModuleNodeComponents.put(node, cmp);
	}

	protected void removeMapping(ISubModuleNode node) {
		subModuleNodeComponents.remove(node);
	}

	@Override
	protected void initObserver() {
		this.observer = new ModuleNodeObserver();
		getModelNode().addListener(this.observer);
	}

	private final class ModuleNodeObserver extends ModuleNodeAdapter {
		@Override
		public void childAdded(IModuleNode source, ISubModuleNode child) {
			createSubModuleComponent(child);
		}

		@Override
		public void activated(IModuleNode source) {
			super.activated(source);
			((ModuleGroupWidget) groupComponent.getUI()).openItem(ui);
			groupComponent.updated();
		}

	}

	@Override
	protected void initUI() {
		initializeSMNodeMapping();
		ui = new ModuleWidget(getGroupComponent().getUI(), 0, this);
		((ModuleGroupWidget) getGroupComponent().getUI()).registerItem(ui);
	}

	@Override
	protected void buildInitialTree() {
		for (ISubModuleNode subNode : getModelNode().getChildren()) {
			createSubModuleComponent(subNode);
		}

	}

	private void createSubModuleComponent(ISubModuleNode subNode) {
		SubModuleNavigationComponent subCmp = new SubModuleNavigationComponent(subNode, ui.getTree());
		addMapping(subNode, subCmp);
	}

	@Override
	Composite getUI() {
		return ui.getBody();
	}

	protected void activateIn(ModuleGroupWidget groupUI) {
		groupUI.openItem(this.ui);
	}

}
