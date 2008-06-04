package org.eclipse.riena.navigation.ui.swt.component;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.ModuleGroupRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class ModuleGroupWidget extends Canvas {

	private List<ModuleItem> items;

	private int itemHeight;

	private IModuleGroupNode moduleGroupNode;
	private ModuleItem openItem;

	private ModuleGroupRenderer renderer;

	public ModuleGroupWidget(Composite parent, int style, IModuleGroupNode moduleGroupNode) {
		super(parent, style);
		this.moduleGroupNode = moduleGroupNode;
		itemHeight = computeItemHeight();
		items = new ArrayList<ModuleItem>();
		addListeners();
	}

	private int computeItemHeight() {
		GC gc = new GC(this);
		int h = getRenderer().computeItemHeight(gc);
		gc.dispose();
		return h;
	}

	protected void addListeners() {
		addPaintListener(new PaintDelegation());
		SelectionListener selectionListener = new SelectionListener();
		addMouseListener(selectionListener);
		addMouseTrackListener(selectionListener);
		addMouseMoveListener(selectionListener);
	}

	public int calcBounds(int hint) {
		Composite component = this;
		component.setBackground(component.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		Point p = component.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, hint);
		fd.left = new FormAttachment(0, 10);
		hint += p.y;
		fd.width = p.x;
		fd.bottom = new FormAttachment(0, hint);
		component.setLayoutData(fd);
		component.layout();
		component.update();
		hint += 3;
		return hint;
	}

	protected void openItem(ModuleItem item) {
		hidePrevious();
		if (item != openItem) {
			openItem = item;
		} else if (allowsDeactivate()) {
			openItem = null;
		}

		redraw();
		// wrong place???
		item.getModuleNode().activate();
	}

	protected void selectActive() {
		if (openItem != null) {
			ISubModuleNode sm = openItem.getSelection();
			if (sm != null) {
				sm.activate();
			}
		}
	}

	protected boolean allowsDeactivate() {
		return false;
	}

	protected void hidePrevious() {
		if (openItem != null) {
			openItem.getBody().setBounds(0, 0, 0, 0);
			openItem.getBody().setVisible(false);
		}

	}

	protected ModuleItem getItem(Point point) {

		int itemWidth = getRenderer().getItemWidth();
		int xs = (getBounds().width - itemWidth) / 2;
		int xe = xs + itemWidth;
		if (point.x < xs || point.x > xe) {
			return null;
		}

		int ys = 2;
		int ye = 0;
		for (ModuleItem item : getItems()) {
			ye = ys + itemHeight - 2;
			if (point.y >= ys && point.y <= ye) {
				return item;
			}
			ye += 4;
			if (item == openItem) {
				ye += openItem.getOpenHeight();
			}
			ys = ye;
		}
		return null;

	}

	protected List<ModuleItem> getItems() {
		return items;
	}

	protected void onPaint(PaintEvent e) {

		getRenderer().setItems(items);
		Point size = getRenderer().computeSize(e.gc, SWT.DEFAULT, SWT.DEFAULT);
		getRenderer().setBounds(0, 0, size.x, size.y);
		getRenderer().paint(e.gc, getModuleGroupNode());

	}

	protected void registerItem(ModuleItem navigationItem) {
		getItems().add(navigationItem);
		redraw();
	}

	@Override
	public Point computeSize(int wHint, int hHint) {

		GC gc = new GC(Display.getCurrent());
		getRenderer().setItems(getItems());
		Point size = getRenderer().computeSize(gc, wHint, hHint);
		gc.dispose();
		return size;

	}

	public void closeCurrent() {
		if (openItem != null) {
			hidePrevious();
			openItem = null;
			redraw();
		}

	}

	private List<IGroupListener> groupListeners = new ArrayList<IGroupListener>();

	public void addGroupListener(IGroupListener listener) {
		groupListeners.add(listener);

	}

	protected void fireModuleNodeSelected(IModuleNode node) {
		for (IGroupListener listener : groupListeners) {
			listener.moduleSelected(node);
		}
	}

	private ModuleGroupRenderer getRenderer() {
		if (renderer == null) {
			renderer = (ModuleGroupRenderer) LnfManager.getLnf().getRenderer("ModuleGroup.renderer"); //$NON-NLS-1$
		}
		return renderer;
	}

	/**
	 * @return the moduleGroupNode
	 */
	private IModuleGroupNode getModuleGroupNode() {
		return moduleGroupNode;
	}

	private class SelectionListener implements MouseListener, MouseTrackListener, MouseMoveListener {

		private ModuleItem mouseDownItem;
		private ModuleItem mouseHoverItem;

		public void mouseUp(MouseEvent e) {
			ModuleItem item = getItem(new Point(e.x, e.y));
			if (item == null) {
				return;
			}
			if (item == mouseDownItem) {
				openItem(item);
				fireModuleNodeSelected(item.getData());
			}
			setMouseNotDown();

		}

		public void mouseDown(MouseEvent e) {
			mouseDownItem = getItem(new Point(e.x, e.y));
			if (mouseDownItem != null) {
				mouseDownItem.setPressed(true);
			}
		}

		public void mouseDoubleClick(MouseEvent e) {

		}

		private void setMouseNotDown() {
			if (mouseDownItem != null) {
				mouseDownItem.setPressed(false);
			}
			mouseDownItem = null;
		}

		private void setMouseNotHover() {
			if (mouseHoverItem != null) {
				mouseHoverItem.setHover(false);
			}
			mouseHoverItem = null;
		}

		private void hoverOrNot(int x, int y) {

			ModuleItem item = getItem(new Point(x, y));
			if ((item == null) || (item != mouseDownItem)) {
				setMouseNotDown();
			}
			if ((item == null) || (item != mouseHoverItem)) {
				setMouseNotHover();
				mouseHoverItem = item;
				if (mouseHoverItem != null) {
					mouseHoverItem.setHover(true);
				}
			}

		}

		/**
		 * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseEnter(MouseEvent e) {
			hoverOrNot(e.x, e.y);
		}

		/**
		 * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseExit(MouseEvent e) {
			hoverOrNot(e.x, e.y);
		}

		/**
		 * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseHover(MouseEvent e) {
			hoverOrNot(e.x, e.y);
		}

		/**
		 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseMove(MouseEvent e) {
			hoverOrNot(e.x, e.y);
		}

	}

	private class PaintDelegation implements PaintListener {

		public void paintControl(PaintEvent e) {
			onPaint(e);
		}
	}

}
