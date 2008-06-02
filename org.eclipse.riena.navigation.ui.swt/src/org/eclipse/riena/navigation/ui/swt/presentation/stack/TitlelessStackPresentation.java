package org.eclipse.riena.navigation.ui.swt.presentation.stack;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.SubModuleViewRenderer;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.internal.presentations.PresentablePart;
import org.eclipse.ui.presentations.IPresentablePart;
import org.eclipse.ui.presentations.IStackPresentationSite;
import org.eclipse.ui.presentations.StackDropResult;
import org.eclipse.ui.presentations.StackPresentation;

public class TitlelessStackPresentation extends StackPresentation {

	private final static int PADDING_LEFT = 2;
	private final static int PADDING_RIGHT = 2;
	private final static int PADDING_TOP = 10;
	private final static int PADDING_BOTTOM = 2;

	public static String PROPERTY_NAVIGATION = "navigation"; //$NON-NLS-1$

	public static String PROPERTY_APPLICATION = "applications"; //$NON-NLS-1$

	private static double NAVIGATION_WIDTH_FACTOR = 0.25;

	private static final int APPLICATION_SWITCHER_HEIGHT = 65;

	// private static final int SUB_MODULE_HEADER_HIGHT = 25;

	// private static final int TOP_V_SPACE = 10;

	private Control current;

	private Control navigation;

	private Control applications;

	private Composite parent;

	private SubModuleViewRenderer renderer;

	public TitlelessStackPresentation(Composite parent, IStackPresentationSite stackSite) {
		super(stackSite);
		createContentArea(parent);
	}

	private void createContentArea(final Composite parent) {
		this.parent = parent;
		parent.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		parent.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {

				if (current != null && current.isVisible()) {
					if (getRenderer() != null) {
						String[] txt = parts.get(current).getPane().getCompoundId().split(":"); //$NON-NLS-1$
						SubModuleNode node = SwtPresentationManagerAccessor.getManager().getNavigationNode(txt[0],
								txt[1], SubModuleNode.class);
						getRenderer().setBounds(e.x + PADDING_LEFT, e.y + PADDING_TOP,
								e.width - PADDING_LEFT - PADDING_RIGHT, e.height - PADDING_TOP - PADDING_BOTTOM);
						getRenderer().paint(e.gc, node);
					}
				}
			}
		});
	}

	@Override
	public void addPart(IPresentablePart newPart, Object cookie) {
	}

	@Override
	public void dispose() {
		if (getRenderer() != null) {
			getRenderer().dispose();
		}
	}

	@Override
	public StackDropResult dragOver(Control currentControl, Point location) {
		return null;
	}

	@Override
	public Control getControl() {
		return parent;
	}

	@Override
	public Control[] getTabList(IPresentablePart part) {
		return null;
	}

	@Override
	public void removePart(IPresentablePart oldPart) {
		// TODO Auto-generated method stub
	}

	@Override
	public void selectPart(IPresentablePart toSelect) {
		if (toSelect == current) {
			return;
		}
		if (toSelect.getPartProperty(PROPERTY_NAVIGATION) != null) {
			// show navigation tree
			navigation = toSelect.getControl();
		} else if (toSelect.getPartProperty(PROPERTY_APPLICATION) != null) {
			// show applications
			applications = toSelect.getControl();
		} else {
			if (current != null) {
				current.setVisible(false);
			}
			current = toSelect.getControl();
			parts.put(current, (PresentablePart) toSelect);
		}
		updateBounds();
	}

	private Map<Control, PresentablePart> parts = new HashMap<Control, PresentablePart>();

	private void updateBounds() {
		if (current != null) {
			updateControl(current, calcOpenPartBounds());
		}
		if (navigation != null) {
			updateControl(navigation, new Rectangle(0, APPLICATION_SWITCHER_HEIGHT,
					(int) (parent.getBounds().width * NAVIGATION_WIDTH_FACTOR), parent.getBounds().height
							- APPLICATION_SWITCHER_HEIGHT));
		}
		if (applications != null) {
			updateControl(applications, new Rectangle(0, 0, parent.getBounds().width, APPLICATION_SWITCHER_HEIGHT));
		}
	}

	private Rectangle calcOpenPartBounds() {
		Rectangle outerBounds = new Rectangle((int) (parent.getBounds().width * NAVIGATION_WIDTH_FACTOR + 15),
				APPLICATION_SWITCHER_HEIGHT + PADDING_TOP,
				(int) (parent.getBounds().width * (1 - NAVIGATION_WIDTH_FACTOR)) - 25 - 5, parent.getBounds().height
						- APPLICATION_SWITCHER_HEIGHT - 50);
		GC gc = new GC(current);
		Rectangle innerBounds = getRenderer().computeInnerBounds(gc, outerBounds);
		return innerBounds;
	}

	@Override
	public void setActive(int newState) {
		if (newState != 0) {
			parent.setVisible(true);
			updateBounds();
			return;
		}
		hideAll();
	}

	private void hideAll() {
		if (current != null) {
			current.setVisible(false);
		}
		if (navigation != null) {
			navigation.setVisible(false);
		}

		if (applications != null) {
			applications.setVisible(false);
		}
		parent.setVisible(false);
	}

	private void updateControl(Control ctrl, Rectangle bounds) {
		ctrl.setVisible(false);
		if (!ctrl.getBounds().equals(bounds)) {
			ctrl.setBounds(bounds);
		}
		ctrl.setVisible(true);
	}

	@Override
	public void setBounds(Rectangle bounds) {
		parent.setBounds(bounds);
		updateBounds();
	}

	@Override
	public void setState(int state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVisible(boolean isVisible) {
		parent.setVisible(isVisible);
	}

	@Override
	public void showPaneMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showSystemMenu() {
		// TODO Auto-generated method stub

	}

	private SubModuleViewRenderer getRenderer() {
		if (renderer == null) {
			renderer = (SubModuleViewRenderer) LnfManager.getLnf().getRenderer("SubModuleView.renderer"); //$NON-NLS-1$
		}
		return renderer;
	}

}
