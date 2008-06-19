package org.eclipse.riena.navigation.ui.swt.component;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.model.ModuleGroupNodeAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;

public class ModuleGroupNavigationComponent extends AbstractNavigationComponent<IModuleGroupNode> {

	private Map<IModuleNode, ModuleNavigationComponent> moduleNodeComponents;
	private ModuleGroupNodeObserver nodeObserver;
	private IComponentUpdateListener moduleUpdateObserver;
	private SubApplicationNavigationComponent subAppComponent;

	private ModuleGroupWidget ui;

	public ModuleGroupNavigationComponent(IModuleGroupNode mgNode, Composite parent,
			SubApplicationNavigationComponent subAppComponent) {
		super(mgNode, parent);
		this.subAppComponent = subAppComponent;
		initializeMNodeMapping();
		initUI();
		buildInitialTree();
		updateActivityToUi();
	}

	private void initializeMNodeMapping() {
		this.moduleNodeComponents = new HashMap<IModuleNode, ModuleNavigationComponent>();
	}

	protected void addMapping(IModuleNode node, ModuleNavigationComponent cmp) {
		moduleNodeComponents.put(node, cmp);
	}

	protected ModuleNavigationComponent getMapping(IModuleNode node) {
		return moduleNodeComponents.get(node);
	}

	protected void removeMapping(IModuleNode node) {
		ModuleNavigationComponent moduleComponent = getMapping(node);
		moduleNodeComponents.remove(node);
		ui.unregisterItem(moduleComponent.getModuleItem());
	}

	@Override
	protected void initObserver() {
		this.nodeObserver = new ModuleGroupNodeObserver();
		getModelNode().addListener(nodeObserver);
		this.moduleUpdateObserver = new ModuleUpdateObserver();
	}

	/*
	 * we might have missed activity events. synch the ui.
	 */
	protected void updateActivityToUi() {
		if (getModelNode().isActivated()) {
			for (IModuleNode child : getModelNode().getChildren()) {
				if (child.isActivated()) {
					ModuleNavigationComponent cmp = moduleNodeComponents.get(child);
					cmp.activateIn(ui);
				}
			}
		}
	}

	private final class ModuleGroupNodeObserver extends ModuleGroupNodeAdapter {

		@Override
		public void childAdded(IModuleGroupNode source, IModuleNode child) {
			addModuleComponent(child);
		}

		@Override
		public void activated(IModuleGroupNode source) {
			updateActivityToUi();
		}

		/**
		 * @see org.eclipse.riena.navigation.model.NavigationNodeAdapter#disposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void disposed(IModuleGroupNode groupNode) {
			super.disposed(groupNode);
			subAppComponent.removeMapping(groupNode);
		}

	}

	private final class WidgetSelectionListener extends MouseAdapter {

		@Override
		public void mouseUp(MouseEvent e) {
			updated();
		}
	}

	@Override
	protected void initUI() {
		ui = new ModuleGroupWidget(getParentComposite(), SWT.NONE, getModelNode());
		ui.addMouseListener(new WidgetSelectionListener());
	}

	@Override
	protected void buildInitialTree() {
		IModuleGroupNode mgNode = getModelNode();
		for (IModuleNode mNode : mgNode.getChildren()) {
			addModuleComponent(mNode);
		}

	}

	private class ModuleUpdateObserver implements IComponentUpdateListener {

		@SuppressWarnings("unchecked")
		public void update(INavigationNode node) {
			updated();
		}

	}

	protected void addModuleComponent(IModuleNode node) {
		ModuleNavigationComponent moduleCmp = new ModuleNavigationComponent(node, this);
		moduleCmp.addComponentUpdateListener(moduleUpdateObserver);
		addMapping(node, moduleCmp);
	}

	@Override
	Composite getUI() {
		return ui;
	}

	public int recalculate(int hint) {
		return ui.calcBounds(hint);
	}

	public void dispose() {
		if ((getUI() == null) && !getUI().isDisposed()) {
			getUI().dispose();
		}
	}

	/**
	 * Rebuilds the tree items of the sub-modules.
	 */
	public void rebuild() {
		IModuleGroupNode mgNode = getModelNode();
		for (IModuleNode mNode : mgNode.getChildren()) {
			moduleNodeComponents.get(mNode).rebuild();
		}
	}

}
