package org.eclipse.riena.navigation.ui.swt.component;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.ISubApplicationListener;
import org.eclipse.riena.navigation.model.SubApplicationAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * This class manages the navigation of a SubApplicationNode.
 */

public class SubApplicationNavigationComponent extends AbstractNavigationComponent<ISubApplication> {

	private Map<IModuleGroupNode, ModuleGroupNavigationComponent> moduleGroupComponents;
	private IComponentUpdateListener componentUpdateListener;
	private ISubApplicationListener moduleGroupObserver;

	public SubApplicationNavigationComponent(ISubApplication subApplication, Composite parent) {
		super(subApplication, parent);
		initializeMgNodeMapping();
		initUI();
		buildInitialTree();
		initObserver();
	}

	protected ISubApplicationListener getModuleGroupObserver() {
		return moduleGroupObserver;
	}

	protected IComponentUpdateListener getUpdateListener() {
		return componentUpdateListener;
	}

	private void initializeMgNodeMapping() {
		moduleGroupComponents = new HashMap<IModuleGroupNode, ModuleGroupNavigationComponent>();
	}

	protected void addMapping(IModuleGroupNode node, ModuleGroupNavigationComponent cmp) {
		moduleGroupComponents.put(node, cmp);
	}

	protected void removeMapping(IModuleGroupNode node) {
		moduleGroupComponents.remove(node);
	}

	private final class ComponentUpdateListener implements IComponentUpdateListener {

		public void update(INavigationNode node) {
			closeInactive((IModuleGroupNode) node);
			sizeNavigation();
		}
	}

	private final class ModuleGroupObserver extends SubApplicationAdapter {

		@Override
		public void childAdded(ISubApplication source, IModuleGroupNode child) {
			addModuleGroupComponent(child);
		}
	}

	protected void initObserver() {
		this.componentUpdateListener = new ComponentUpdateListener();
		this.moduleGroupObserver = new ModuleGroupObserver();
		getModelNode().addListener(getModuleGroupObserver());
	}

	/*
	 * As we register to the subApplication long after it has been created we
	 * might have missed some ModuleGroups(events). So we have to create the
	 * basic tree structure manually.
	 */
	protected void buildInitialTree() {
		ISubApplication root = getModelNode();
		for (IModuleGroupNode mgNode : root.getChildren()) {
			addModuleGroupComponent(mgNode);
		}
		sizeNavigation();
	}

	protected void addModuleGroupComponent(IModuleGroupNode mgNode) {
		ModuleGroupNavigationComponent mgCmp = new ModuleGroupNavigationComponent(mgNode, getParentComposite());
		mgCmp.addComponentUpdateListener(getUpdateListener());
		addMapping(mgNode, mgCmp);
	}

	public void closeInactive(IModuleGroupNode selected) {
		for (IModuleGroupNode mg : getModelNode().getChildren()) {
			if (mg != selected) {
				ModuleGroupNavigationComponent w = (ModuleGroupNavigationComponent) moduleGroupComponents.get(mg);
				((ModuleGroupWidget) w.getUI()).closeCurrent();
			}
		}
	}

	public void sizeNavigation() {
		ISubApplication root = getModelNode();
		int yPos = 10;
		for (IModuleGroupNode moduleGroup : root.getChildren()) {
			yPos = moduleGroupComponents.get(moduleGroup).recalculate(yPos);
		}
		getParentComposite().layout();
	}

	@Override
	protected void initUI() {
		getParentComposite().setBackground(getParentComposite().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		FormLayout formLayout = new FormLayout();
		getParentComposite().setLayout(formLayout);
	}

	@Override
	Composite getUI() {
		// TODO Auto-generated method stub
		return null;
	}

}
