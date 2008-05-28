package org.eclipse.riena.navigation.ui.swt.component;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ISubModuleNodeListener;
import org.eclipse.riena.navigation.model.SubModuleNodeAdapter;
import org.eclipse.riena.navigation.ui.swt.utils.ImageUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class SubModuleNavigationComponent extends AbstractNavigationComponent<ISubModuleNode> {

	private TreeItem treeItem;

	// observe model
	private ISubModuleNodeListener subModuleObserver;
	// observe ui
	private Listener treeUiObserver;

	private TreeItem parentTreeItem;

	public SubModuleNavigationComponent(ISubModuleNode node, Tree tree) {
		this(node, tree, null);
	}

	public SubModuleNavigationComponent(ISubModuleNode node, Tree tree, TreeItem parentItem) {
		super(node, tree);
		this.parentTreeItem = parentItem;
		initUI();
		buildInitialTree();
		updateAcitivity();
	}

	private void updateAcitivity() {
		if (getModelNode().isActivated()) {
			getTree().showItem(getTreeItem());
			getTree().setSelection(getTreeItem());
		}
	}

	protected TreeItem getParentTreeItem() {
		return parentTreeItem;
	}

	@Override
	protected void buildInitialTree() {
		for (ISubModuleNode subNode : getModelNode().getChildren()) {
			createSubModuleComponent(subNode);
		}
	}

	private void createSubModuleComponent(ISubModuleNode subNode) {
		new SubModuleNavigationComponent(subNode, getTree(), getTreeItem());
	}

	protected TreeItem getTreeItem() {
		return treeItem;
	}

	private final class SubModuleNodeObserver extends SubModuleNodeAdapter {

		@Override
		public void activated(ISubModuleNode source) {
			updateAcitivity();
		}

		@Override
		public void childAdded(ISubModuleNode source, ISubModuleNode child) {
			createSubModuleComponent(child);
		}

	}

	private final class TreeUIObserver implements Listener {

		public void handleEvent(Event event) {
			TreeItem[] selection = getTree().getSelection();
			if (selection != null && selection.length > 0 && selection[0].getData().equals(getModelNode())) {
				nodeSelected();
			}
		}
	}

	@Override
	protected void initObserver() {
		subModuleObserver = new SubModuleNodeObserver();
		getModelNode().addListener(subModuleObserver);
		treeUiObserver = new TreeUIObserver();
		getTree().addListener(SWT.Selection, treeUiObserver);
	}

	protected Tree getTree() {
		return (Tree) getParentComposite();
	}

	@Override
	protected void initUI() {
		if (getParentTreeItem() != null) {
			treeItem = new TreeItem(getParentTreeItem(), SWT.BORDER);
		} else {
			treeItem = new TreeItem(getTree(), SWT.BORDER);
		}
		treeItem.setText(getModelNode().getLabel());
		treeItem.setData(getModelNode());
		if (getModelNode().getIcon() != null) {
			treeItem.setImage(ImageUtil.getImage(getModelNode().getIcon()));
		}
	}

	public void nodeSelected() {
		getModelNode().activate();
	}

	@Override
	Composite getUI() {
		return getTree();
	}

}
