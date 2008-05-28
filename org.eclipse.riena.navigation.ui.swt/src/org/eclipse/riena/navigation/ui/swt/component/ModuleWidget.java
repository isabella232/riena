package org.eclipse.riena.navigation.ui.swt.component;

import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class ModuleWidget {

	private int itemHEIGHT = 20;
	private Composite parent;
	private Composite body;
	private String text;
	private ModuleNavigationComponent moduleCmp;
	private Tree subModuleTree;

	private SubModuleNode activeSubModule;
	private String iconPath;

	public ModuleWidget(Composite parent, int style, ModuleNavigationComponent moduleCmp) {
		this.moduleCmp = moduleCmp;
		construct(parent);
	}

	protected void createSubModuleTree() {
		this.subModuleTree = new Tree(getBody(), SWT.NONE);
		this.subModuleTree.setBackground(this.subModuleTree.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		this.subModuleTree.setLinesVisible(false);
		this.subModuleTree.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				TreeItem[] selection = getTree().getSelection();
				activeSubModule = (SubModuleNode) selection[0].getData();
				resize();
			}

		});

		this.subModuleTree.addListener(SWT.Paint, new Listener() {

			public void handleEvent(Event event) {
				onTreePaint(event.gc);
			}

		});

		this.subModuleTree.addListener(SWT.Expand, new Listener() {

			public void handleEvent(Event event) {
				handleExpandCollapse(event, true);
			}

		});

		this.subModuleTree.addListener(SWT.Collapse, new Listener() {

			public void handleEvent(Event event) {
				handleExpandCollapse(event, false);
			}

		});

	}

	private void handleExpandCollapse(Event event, boolean expand) {
		TreeItem item = (TreeItem) event.item;
		INavigationNode<?> node = (INavigationNode<?>) item.getData();
		node.setExpanded(expand);
		resize();
	}

	protected void resize() {
		moduleCmp.updated();
	}

	public SubModuleNode getSelection() {
		return activeSubModule;
	}

	protected IModuleNode getModuleNode() {
		return moduleCmp.getModelNode();
	}

	private void construct(final Composite parent) {
		this.parent = parent;
		body = new Composite(parent, SWT.None);
		body.setLayout(new FillLayout());
		createBodyContent(parent);
		setText(getModuleNode().getLabel());
		setIcon(getModuleNode().getIcon());
	}

	public void setIcon(String icon) {
		this.iconPath = icon;
	}

	public String getIcon() {
		return iconPath;
	}

	protected void createBodyContent(final Composite parent) {
		createSubModuleTree();
		subModuleTree.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
	}

	public Composite getBody() {
		return body;
	}

	public void setText(String string) {
		this.text = string;
		parent.redraw();
	}

	public String getText() {
		return text;
	}

	protected int calcDepth() {
		IModuleNode moduleNode = getModuleNode();
		int sum = 0;
		for (INavigationNode<?> child : moduleNode.getChildren()) {
			sum += calcDepth(child);
		}
		return sum;

	}

	protected int calcDepth(INavigationNode<?> node) {
		int depth = 1;

		if (node.isExpanded()) {
			for (INavigationNode<?> child : node.getChildren()) {
				depth += calcDepth(child);
			}
		}

		return depth;
	}

	public int getOpenHeight() {
		return calcDepth() * itemHEIGHT;
	}

	public Tree getTree() {
		return subModuleTree;
	}

	public IModuleNode getData() {
		return getModuleNode();
	}

	/**
	 * Clipps (if necessary) the text of the given tree item and all child
	 * items.
	 * 
	 * @param gc
	 * @param item -
	 *            tree item
	 * @return true: some text was clipped; false: no text was clipped
	 */
	private boolean clipSubModuleTexts(GC gc, TreeItem item) {

		boolean clipped = clipSubModuleText(gc, item);

		TreeItem[] items = item.getItems();
		for (TreeItem childItem : items) {
			if (clipSubModuleTexts(gc, childItem)) {
				clipped = true;
			}
		}

		return clipped;

	}

	/**
	 * Clipps (if necessary) the text of the given tree item.
	 * 
	 * @param gc
	 * @param item -
	 *            tree item
	 * @return true: text was clipped; false: text was not clipped
	 */
	private boolean clipSubModuleText(GC gc, TreeItem item) {

		boolean clipped = false;

		Rectangle bodyBounds = getBody().getBounds();
		SubModuleNode data = (SubModuleNode) item.getData();
		String label = data.getLabel();
		Rectangle itemBounds = item.getBounds();
		int maxWidth = bodyBounds.width - itemBounds.x - 5;
		label = SwtUtilities.clipText(gc, label, maxWidth);
		item.setText(label);

		clipped = !data.getLabel().equals(label);
		return clipped;

	}

	/**
	 * Clipps (if necessary) the text of the tree items and hiddes the scroll
	 * bars.
	 * 
	 * @param gc
	 */
	private void onTreePaint(GC gc) {

		TreeItem[] items = getTree().getItems();
		for (TreeItem item : items) {
			clipSubModuleTexts(gc, item);
		}

		hiddeSrollBars();

	}

	/**
	 * Hiddes the vertical and horizontal scroll bar of the tree.<br>
	 * <i>TODO tsc@11.02.2008 - To hidde the scroll bar you have two options.
	 * Both solutions are not perfect.</i>
	 */
	private void hiddeSrollBars() {

		// 1. Solution
		// Hide scroll bars without OS
		// Problem: in some cases (e.g. collapse) this solution works not
		// correct

		// Rectangle treeBounds = getTree().getBounds();
		// int width = treeBounds.width;
		// int height = treeBounds.height;
		// if ((width < Integer.MAX_VALUE) || (height < Integer.MAX_VALUE)) {
		// getTree().setBounds(treeBounds.x, treeBounds.y, Integer.MAX_VALUE,
		// Integer.MAX_VALUE);
		// }

		// 2. Solution
		// Hide scroll bars with OS
		// Problem: OS is not accessible in some cases (operation system is not
		// windows?)

		int hwnd = getTree().handle;
		OS.ShowScrollBar(hwnd, OS.SB_VERT, false);

		hwnd = getTree().handle;
		OS.ShowScrollBar(hwnd, OS.SB_HORZ, false);

	}

}
