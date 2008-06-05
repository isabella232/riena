package org.eclipse.riena.navigation.ui.swt.component;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ISubModuleNodeListener;
import org.eclipse.riena.navigation.model.SubModuleNodeAdapter;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Image;
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
		tree.addTreeListener(new ExpandTreeListener());
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
		RienaDefaultLnf lnf = LnfManager.getLnf();
		if (getParentTreeItem() != null) {
			treeItem = new TreeItem(getParentTreeItem(), SWT.BORDER);
			if (getParentTreeItem().getItemCount() == 1) {
				updateFolderImage(getParentTreeItem(), false);
			}
		} else {
			treeItem = new TreeItem(getTree(), SWT.BORDER);
		}
		treeItem.setText(getModelNode().getLabel());
		treeItem.setData(getModelNode());
		Image image = null;
		// if (getModelNode().getIcon() != null) {
		// image = ImageUtil.getImage(getModelNode().getIcon());
		// }
		if (image == null) {
			image = lnf.getImage("treeDocumentLeaf.icon"); //$NON-NLS-1$
		}
		treeItem.setImage(image);
	}

	public void nodeSelected() {
		getModelNode().activate();
	}

	@Override
	Composite getUI() {
		return getTree();
	}

	/**
	 * Updates the image of a folder in the tree.
	 * 
	 * @param item -
	 *            item to be updated.
	 */
	private void updateFolderImage(TreeItem item, boolean expanded) {

		RienaDefaultLnf lnf = LnfManager.getLnf();
		if (item.getItemCount() > 0) {
			String folderIcon = "treeFolderClosed.icon"; //$NON-NLS-1$
			if (expanded) {
				folderIcon = "treeFolderOpen.icon"; //$NON-NLS-1$
			}
			Image image = lnf.getImage(folderIcon);
			item.setImage(image);
		}

	}

	/**
	 * After a tree branch is collapsed or expanded the images of the tree item
	 * must be updated.
	 */
	private class ExpandTreeListener implements TreeListener {

		/**
		 * @see org.eclipse.swt.events.TreeListener#treeCollapsed(org.eclipse.swt.events.TreeEvent)
		 */
		public void treeCollapsed(TreeEvent e) {
			updateFolderImage((TreeItem) e.item, false);
		}

		/**
		 * @see org.eclipse.swt.events.TreeListener#treeExpanded(org.eclipse.swt.events.TreeEvent)
		 */
		public void treeExpanded(TreeEvent e) {
			updateFolderImage((TreeItem) e.item, true);
		}

	}

}
