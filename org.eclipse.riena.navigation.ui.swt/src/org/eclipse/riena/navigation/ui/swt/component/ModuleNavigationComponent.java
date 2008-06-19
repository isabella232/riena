package org.eclipse.riena.navigation.ui.swt.component;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.ModuleNodeAdapter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

public class ModuleNavigationComponent extends AbstractNavigationComponent<IModuleNode> {

	private Map<ISubModuleNode, SubModuleNavigationComponent> subModuleNodeComponents;
	private ModuleItem ui;
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
		getModelNode().addListener(new ModuleNodeObserver());
	}

	private final class ModuleNodeObserver extends ModuleNodeAdapter {

		/**
		 * @see org.eclipse.riena.navigation.model.NavigationNodeAdapter#childAdded(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		public void childAdded(IModuleNode source, ISubModuleNode child) {
			createSubModuleComponent(child);
		}

		/**
		 * @see org.eclipse.riena.navigation.model.NavigationNodeAdapter#activated(org.eclipse.riena.navigation.INavigationNode)
		 */
		public void activated(IModuleNode source) {
			super.activated(source);
			((ModuleGroupWidget) groupComponent.getUI()).openItem(ui);
			groupComponent.updated();
		}

		/**
		 * @see org.eclipse.riena.navigation.model.NavigationNodeAdapter#disposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void disposed(IModuleNode node) {
			super.disposed(node);
			groupComponent.removeMapping(node);
		}

	}

	@Override
	protected void initUI() {
		initializeSMNodeMapping();
		ui = new ModuleItem(getGroupComponent().getUI(), this);
		((ModuleGroupWidget) getGroupComponent().getUI()).registerItem(ui);
	}

	@Override
	protected void buildInitialTree() {
		ui.getTree().removeAll();
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

	protected ModuleItem getModuleItem() {
		return ui;
	}

	protected void activateIn(ModuleGroupWidget groupUI) {
		groupUI.openItem(this.ui);
	}

	/**
	 * Rebuilds the tree items of the sub-modules.
	 */
	public void rebuild() {
		TreeItem[] treeItems = getModuleItem().getTree().getItems();
		updateItems(treeItems);
	}

	private void updateItems(TreeItem[] treeItems) {

		for (TreeItem treeItem : treeItems) {
			ISubModuleNode subNode = (ISubModuleNode) treeItem.getData();
			SubModuleNavigationComponent subCmp = subModuleNodeComponents.get(subNode);
			if (subCmp != null) {
				subCmp.updateItemImage(treeItem);
			}
			if (treeItem.getItemCount() > 0) {
				updateItems(treeItem.getItems());
			}
		}

	}

}
