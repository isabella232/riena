package org.eclipse.riena.navigation.ui.swt.component;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubApplicationNodeListener;
import org.eclipse.riena.navigation.model.SubApplicationNodeAdapter;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * This class manages the navigation of a SubApplicationNode.
 */
public class SubApplicationNavigationComponent extends AbstractNavigationComponent<ISubApplicationNode> {

	private Map<IModuleGroupNode, ModuleGroupNavigationComponent> moduleGroupComponents;
	private IComponentUpdateListener componentUpdateListener;
	private ISubApplicationNodeListener moduleGroupObserver;

	public SubApplicationNavigationComponent(ISubApplicationNode subApplication, Composite parent) {
		super(subApplication, parent);
		initializeMgNodeMapping();
		initUI();
		buildInitialTree();
	}

	protected ISubApplicationNodeListener getModuleGroupObserver() {
		return moduleGroupObserver;
	}

	protected IComponentUpdateListener getUpdateListener() {
		return componentUpdateListener;
	}

	private void initializeMgNodeMapping() {
		moduleGroupComponents = new HashMap<IModuleGroupNode, ModuleGroupNavigationComponent>();
	}

	protected void addMapping(IModuleGroupNode node, ModuleGroupNavigationComponent cmp) {
		if (!moduleGroupComponents.containsKey(node)) {
			moduleGroupComponents.put(node, cmp);
			cmp.addComponentUpdateListener(getUpdateListener());
		}
	}

	private ModuleGroupNavigationComponent getMapping(IModuleGroupNode node) {
		return moduleGroupComponents.get(node);
	}

	protected void removeMapping(IModuleGroupNode node) {
		if (moduleGroupComponents.containsKey(node)) {
			getMapping(node).removeComponentUpdateListener(getUpdateListener());
			getMapping(node).getUI().dispose();
			moduleGroupComponents.remove(node);
		}

	}

	private final class ComponentUpdateListener implements IComponentUpdateListener {

		/**
		 * @see org.eclipse.riena.navigation.ui.swt.component.IComponentUpdateListener#update(org.eclipse.riena.navigation.INavigationNode)
		 */
		public void update(INavigationNode node) {
			closeInactive((IModuleGroupNode) node);
			sizeNavigation();
		}
	}

	/**
	 * This observer resizes the navigation after a child was added or removed.<br>
	 * 
	 */
	private final class ModuleGroupObserver extends SubApplicationNodeAdapter {

		/**
		 * @see org.eclipse.riena.navigation.model.NavigationNodeAdapter#childAdded(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void childAdded(ISubApplicationNode source, IModuleGroupNode child) {
			super.childAdded(source, child);
			addModuleGroupComponent(child);
			sizeNavigation();
		}

		/**
		 * @see org.eclipse.riena.navigation.model.NavigationNodeAdapter#childRemoved(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void childRemoved(ISubApplicationNode source, IModuleGroupNode child) {
			super.childRemoved(source, child);
			removeMapping(child);
			sizeNavigation();
		}

	}

	@Override
	protected void initObserver() {
		this.componentUpdateListener = new ComponentUpdateListener();
		this.moduleGroupObserver = new ModuleGroupObserver();
		getModelNode().addListener(getModuleGroupObserver());
	}

	/**
	 * As we register to the subApplication long after it has been created we
	 * might have missed some ModuleGroups(events). So we have to create the
	 * basic tree structure manually.
	 */
	@Override
	protected void buildInitialTree() {
		ISubApplicationNode root = getModelNode();
		for (IModuleGroupNode mgNode : root.getChildren()) {
			addModuleGroupComponent(mgNode);
		}
		sizeNavigation();
	}

	protected void addModuleGroupComponent(IModuleGroupNode mgNode) {
		ModuleGroupNavigationComponent mgCmp = new ModuleGroupNavigationComponent(mgNode, getParentComposite(), this);
		addMapping(mgNode, mgCmp);
	}

	public void closeInactive(IModuleGroupNode selected) {
		for (IModuleGroupNode mg : getModelNode().getChildren()) {
			if (mg != selected) {
				ModuleGroupNavigationComponent w = moduleGroupComponents.get(mg);
				w.getUI().closeCurrent();
			}
		}
	}

	public void sizeNavigation() {
		ISubApplicationNode root = getModelNode();
		int yPos = 0;
		for (IModuleGroupNode moduleGroup : root.getChildren()) {
			yPos = moduleGroupComponents.get(moduleGroup).recalculate(yPos);
		}
		getParentComposite().layout();
	}

	@Override
	protected void initUI() {
		FormLayout formLayout = new FormLayout();
		getParentComposite().setLayout(formLayout);
	}

	@Override
	Composite getUI() {
		return null;
	}

	/**
	 * Rebuilds the tree items of the sub-modules.
	 */
	public void rebuild() {
		ISubApplicationNode root = getModelNode();
		for (IModuleGroupNode moduleGroup : root.getChildren()) {
			moduleGroupComponents.get(moduleGroup).rebuild();
		}

	}

}
