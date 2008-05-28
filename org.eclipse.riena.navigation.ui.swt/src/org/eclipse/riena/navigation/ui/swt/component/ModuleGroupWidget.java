package org.eclipse.riena.navigation.ui.swt.component;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.utils.ImageUtil;
import org.eclipse.riena.navigation.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class ModuleGroupWidget extends Canvas {

	private static final int ITEM_WIDTH = 165;
	private static final int BORDER_WIDTH = 1;

	private List<ModuleWidget> items;

	private int itemHeight = 26;

	private ModuleWidget openItem;

	protected INavigationResizer navigationResizer;

	public ModuleGroupWidget(Composite parent, int style) {
		super(parent, style);
		items = new ArrayList<ModuleWidget>();
		addListeners();
	}

	protected void addListeners() {
		addPaintDelegation();
		addMouseListeners();
	}

	public void setNavigationResizer(INavigationResizer view) {
		this.navigationResizer = view;
	}

	protected void addMouseListeners() {
		addMouseListener(new SelectionListener());
	}

	private class SelectionListener implements MouseListener {
		private ModuleWidget mouseDownItem;

		public void mouseUp(MouseEvent e) {
			ModuleWidget item = getItem(new Point(1, e.y));
			if ((item) == null) {
				return;
			}
			if (item == mouseDownItem) {
				openItem(item);
				fireModuleNodeSelected(item.getData());
			}

		}

		public void mouseDown(MouseEvent e) {
			mouseDownItem = getItem(new Point(1, e.y));
		}

		public void mouseDoubleClick(MouseEvent arg0) {
		}
	}

	protected void addPaintDelegation() {
		addPaintListener(new PaintDelegation());
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

	protected void openItem(ModuleWidget item) {
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

	protected ModuleWidget getItem(Point point) {

		int ys = 0;
		int ye = 0;

		for (ModuleWidget item : getItems()) {
			ye += itemHeight;
			if (item == openItem) {
				ye += calcCurrentOpenSize();
			}
			if (point.y >= ys && point.y <= ye) {
				return item;
			}
		}
		return null;
	}

	protected List<ModuleWidget> getItems() {
		return items;
	}

	private class PaintDelegation implements PaintListener {

		public void paintControl(PaintEvent e) {
			onPaint(e);
		}
	}

	private static int SPACER = 1;

	protected void onPaint(PaintEvent e) {
		GC gc = e.gc;
		if (gc.getAdvanced()) {
			gc.setTextAntialias(SWT.ON);
		}
		gc.setInterpolation(SWT.HIGH);
		gc.setAntialias(SWT.ON);
		Color unselectedBackGroundColor = new Color(getParent().getDisplay(), 189, 206, 212);
		Color selectedBackGroundColor = new Color(getParent().getDisplay(), 143, 214, 107);
		Color unselectedForeGroundColor = new Color(getParent().getDisplay(), 219, 230, 233);
		Color selectedForeGroundColor = new Color(getParent().getDisplay(), 186, 226, 156);
		Color unselectedGroupBorderColor = new Color(getParent().getDisplay(), 171, 207, 208);
		Color selectedGroupBorderColor = new Color(getParent().getDisplay(), 80, 170, 29);
		Color unselectedTitleBorderColor = new Color(getParent().getDisplay(), 89, 146, 154);
		Color selectedTitleBorderColor = new Color(getParent().getDisplay(), 78, 167, 20);
		Color unselectedTextColor = new Color(getParent().getDisplay(), 26, 80, 81);
		Color selectedTextColor = new Color(getParent().getDisplay(), 57, 106, 38);
		int borderThickness = 2;
		int inset = 4;

		int y = 0;
		for (ModuleWidget item : getItems()) {
			gc.setBackground(item == openItem ? selectedBackGroundColor : unselectedBackGroundColor);
			gc.setForeground(item == openItem ? selectedForeGroundColor : unselectedForeGroundColor);
			gc.fillRoundRectangle(0 + inset, y + inset, ITEM_WIDTH - 1 - inset * 2, itemHeight - inset * 2, 5, 5);
			gc.setForeground(item == openItem ? selectedTitleBorderColor : unselectedTitleBorderColor);
			gc.drawRoundRectangle(0 + inset - 1, y + inset - 1, ITEM_WIDTH - 1 - inset * 2 + 1, itemHeight - inset * 2
					+ 1, 5, 5);
			gc.setForeground(item == openItem ? selectedTextColor : unselectedTextColor);
			if (item.getText() != null) {
				int textPosX = 30;
				int textPosY = y + 5;
				String text = item.getText();
				text = SwtUtilities.clipText(gc, text, ITEM_WIDTH - textPosX);
				gc.drawText(text, textPosX, textPosY, true);
			}
			if (item.getIcon() != null) {
				Image image = ImageUtil.getImage(item.getIcon());
				if (image != null) {
					gc.drawImage(image, 5, y + 5);
				}
			}

			int y1 = y;

			y += itemHeight;

			if (item == openItem) {
				item.getBody().layout();
				item.getBody().setBounds(BORDER_WIDTH + 1, y, ITEM_WIDTH - 2 * BORDER_WIDTH - 2,
						calcCurrentOpenSize() - 1);
				item.getBody().setVisible(true);
				y += calcCurrentOpenSize();
			}
			gc.setLineWidth(1);
			gc.setForeground(item == openItem ? selectedGroupBorderColor : unselectedGroupBorderColor);
			for (int i = 0; i < borderThickness; i++) {
				gc.drawRoundRectangle(i, y1 + i, ITEM_WIDTH - 1 - i * 2, y - y1 - 1 - i * 2, 5, 5);
			}
			if (item != getItems().get(getItems().size() - 1)) {
				y += SPACER;
			}
		}
	}

	private int calcCurrentOpenSize() {
		return openItem.getOpenHeight();
	}

	protected void registerItem(ModuleWidget navigationItem) {
		getItems().add(navigationItem);
		redraw();
	}

	@Override
	public Point computeSize(int wHint, int hHint) {
		int height = itemHeight * getItems().size() + SPACER * (getItems().size() - 1);
		if (openItem != null) {
			height += calcCurrentOpenSize();
		}
		return new Point(ITEM_WIDTH + 4, height + 4);

	}

	public void closeCurrent() {
		if (openItem != null) {
			hidePrevious();
			openItem = null;
			redraw();
		}

	}

	public void sizeNavigation() {
		ModuleWidget old = openItem;
		openItem = null;
		openItem(old);
		navigationResizer.sizeNavigation();
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

}
