package org.eclipse.riena.navigation.ui.swt.component;

import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.riena.navigation.ui.swt.utils.SwtUtilities;
import org.eclipse.riena.navigation.ui.swt.win32.SwtOsUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * An item of a module, used by the module group.
 */
public class ModuleItem {

	private Composite parent;
	private Composite body;
	private ModuleNavigationComponent moduleCmp;
	private Tree subModuleTree;
	private boolean pressed;
	private boolean hover;
	private Rectangle bounds;

	private SubModuleNode activeSubModule;

	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param parent -
	 *            module group which will be the parent of the new instance
	 */
	public ModuleItem(ModuleGroupWidget parent, ModuleNavigationComponent moduleCmp) {

		this.parent = parent;
		this.moduleCmp = moduleCmp;
		pressed = false;
		hover = false;

		construct(parent);

	}

	private void construct(Composite parent) {
		body = new Composite(parent, SWT.None);
		body.setLayout(new FillLayout());
		createSubModuleTree();
	}

	/**
	 * Creates the tree for the sub-modules.
	 */
	protected void createSubModuleTree() {

		subModuleTree = new Tree(getBody(), SWT.NONE);
		subModuleTree.setLinesVisible(false);

		addListeners();

		new SubModuleToolTip(subModuleTree);

		redraw();

	}

	/**
	 * Adds listeners to the sub-module tree.
	 */
	private void addListeners() {

		getTree().addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				TreeItem[] selection = getTree().getSelection();
				activeSubModule = (SubModuleNode) selection[0].getData();
				resize();
			}

		});

		getTree().addListener(SWT.Paint, new Listener() {

			public void handleEvent(Event event) {
				onTreePaint(event.gc);
			}

		});

		getTree().addListener(SWT.Expand, new Listener() {

			public void handleEvent(Event event) {
				handleExpandCollapse(event, true);
			}

		});

		getTree().addListener(SWT.Collapse, new Listener() {

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

	public IModuleNode getModuleNode() {
		return moduleCmp.getModelNode();
	}

	public Composite getBody() {
		return body;
	}

	/**
	 * Returns the height of the open item.
	 * 
	 * @return height.
	 */
	public int getOpenHeight() {
		IModuleNode moduleNode = getModuleNode();
		int itemHeight = getTree().getItemHeight();
		return moduleNode.calcDepth() * itemHeight + 1;
	}

	/**
	 * Returns the tree with the sub-module items.
	 * 
	 * @return tree
	 */
	public Tree getTree() {
		return subModuleTree;
	}

	/**
	 * Clips (if necessary) the text of the given tree item and all child items.
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
	 * Clips (if necessary) the text of the given tree item.
	 * 
	 * @param gc
	 * @param item -
	 *            tree item
	 * @return true: text was clipped; false: text was not clipped
	 */
	private boolean clipSubModuleText(GC gc, TreeItem item) {

		boolean clipped = false;

		Rectangle bodyBounds = getBody().getBounds();
		ISubModuleNode subModule = (ISubModuleNode) item.getData();
		String longText = subModule.getLabel();
		Rectangle itemBounds = item.getBounds();
		int maxWidth = bodyBounds.width - itemBounds.x - 5;
		String text = SwtUtilities.clipText(gc, longText, maxWidth);
		item.setText(text);

		clipped = !longText.equals(text);
		return clipped;

	}

	/**
	 * Clips (if necessary) the text of the tree items and hides the scroll
	 * bars.
	 * 
	 * @param gc
	 */
	private void onTreePaint(GC gc) {

		TreeItem[] items = getTree().getItems();
		for (TreeItem item : items) {
			clipSubModuleTexts(gc, item);
		}

		SwtOsUtilities.hiddeSrollBars(getTree());

	}

	/**
	 * Returns if the module item is pressed or not.
	 * 
	 * @param pressed -
	 *            true, if mouse over the module and pressed; otherwise false.
	 */
	public boolean isPressed() {
		return pressed;
	}

	/**
	 * Sets if the module item is pressed or not.<br>
	 * If the given state differs from the current state, the parent of item is
	 * redrawn.
	 * 
	 * @param pressed -
	 *            true, if mouse over the module and pressed; otherwise false.
	 */
	public void setPressed(boolean pressed) {
		if (this.pressed != pressed) {
			this.pressed = pressed;
			if (!parent.isDisposed()) {
				parent.redraw();
			}
		}
	}

	/**
	 * Returns if the module item is highlighted, because the mouse hovers over
	 * the item.
	 * 
	 * @return true, if mouse over the module; otherwise false.
	 */
	public boolean isHover() {
		return hover;
	}

	/**
	 * Sets if the module item is highlighted, because the mouse hovers over the
	 * item.<br>
	 * If the given hover state differs from the current state, the parent of
	 * item is redrawn.
	 * 
	 * @param hover -
	 *            true, if mouse over the module; otherwise false.
	 */
	public void setHover(boolean hover) {
		if (this.hover != hover) {
			this.hover = hover;
			if (!parent.isDisposed()) {
				parent.redraw();
			}
		}
	}

	/**
	 * Disposes this module item.
	 */
	public void dispose() {
		getBody().dispose();
		body = null;
		getTree().dispose();
		subModuleTree = null;
	}

	/**
	 * Returns a rectangle describing the size and location of this module item.
	 * 
	 * @return the bounds
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * Sets a rectangle describing the size and location of this module item.
	 * 
	 * @param bounds
	 *            the bounds to set
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	/**
	 * Redraws the tree with the sub-module items.
	 */
	public void redraw() {
		subModuleTree.setBackground(LnfManager.getLnf().getColor("SubModuleTree.background")); //$NON-NLS-1$
	}

}
