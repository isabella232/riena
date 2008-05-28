package org.eclipse.riena.navigation.ui.swt.views;

import java.util.List;

import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.model.ApplicationModel;
import org.eclipse.riena.navigation.model.NavigationTreeObserver;
import org.eclipse.riena.navigation.model.SubApplicationAdapter;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class ApplicationSwitchViewPart extends ViewPart {

	public final static String ID = "org.eclipse.riena.navigation.ui.swt.applicationSwicther"; //$NON-NLS-1$
	private Composite composite;
	private int selected_idx = 0;

	public ApplicationSwitchViewPart() {
	}

	protected Composite getComposite() {
		return composite;
	}

	private class Switcher extends Canvas {

		private Point maxTextSize;
		private int textMargin = 20;
		private int tabHeight = 25;

		public Switcher(Composite parent, int style) {
			super(parent, style);
			addMouseListener(new TabSelector());
			addPaintListener(new PaintListener() {

				public void paintControl(PaintEvent e) {
					GC gc = e.gc;
					gc.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_WHITE));
					gc.fillRectangle(0, 0, getParent().getSize().x, getParent().getSize().y);
					// gc.setForeground(new Color(getParent().getDisplay(), 30,
					// 144, 255));
					// gc.fillGradientRectangle(0, 0, getParent().getSize().x,
					// getParent().getSize().y, true);
					paintTabs(gc);
				}

			});
		}

		/**
		 * Prototype for riena subApplicationTabs
		 */
		private class TabSelector extends MouseAdapter {
			@Override
			public void mouseDown(MouseEvent e) {
				int tabWidth = calcTabWidth();
				Point p = calcTabStartPosition(tabWidth);
				int x0 = p.x;
				int y0 = p.y;
				int px = e.x;
				int py = e.y;
				if (px >= x0 && px <= x0 + getNumTabs() * tabWidth) {
					if (py >= y0 && py <= y0 + tabHeight) {
						int offset = px - x0;
						int part = Math.abs(offset / tabWidth);
						selected_idx = part;
						getApplicationModel().getChildren().get(selected_idx).activate();
						redraw();
					}
				}

			}

		}

		private int calcTabWidth() {
			return maxTextSize.x + 2 * textMargin;
		}

		private int getNumTabs() {
			return getApplicationModel().getChildren().size();
		}

		private void paintTabs(GC gc) {
			calcMaxTextSize(gc);
			int tabWidth = calcTabWidth();
			Point p = calcTabStartPosition(tabWidth);
			int x0 = p.x;
			int y0 = p.y;
			gc.setForeground(getParent().getDisplay().getSystemColor(SWT.COLOR_BLACK));
			int i = 0;
			for (ISubApplication subApp : getApplicationModel().getChildren()) {
				gc.setAntialias(SWT.ON);
				gc.setAdvanced(true);
				gc.setLineWidth(2);
				if (i != selected_idx) {
					gc.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
					gc.drawRoundRectangle(x0 + i * tabWidth, y0, tabWidth, tabHeight, 5, 5);
					gc.fillRoundRectangle(x0 + i * tabWidth, y0, tabWidth, tabHeight, 5, 5);
				} else {
					gc.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_WHITE));
					for (int yy = 0; yy < 4; yy++) {
						gc.drawRoundRectangle(x0 + i * tabWidth, y0 - 3 - yy, tabWidth, tabHeight + 3, 5, 5);
					}
					gc.fillRoundRectangle(x0 + i * tabWidth, y0 - 3, tabWidth, tabHeight + 3, 5, 5);
				}

				gc.drawString(subApp.getLabel(), x0 + i * tabWidth + textMargin, y0 + 6);
				i++;
			}

			gc.drawLine(0, y0 + tabHeight, x0 - 1, y0 + tabHeight);
			gc.drawLine(x0 + getNumTabs() * tabWidth, y0 + tabHeight, getClientArea().width, y0 + tabHeight);

		}

		private Point calcTabStartPosition(int tabWidth) {
			return new Point((getClientArea().width - getNumTabs() * tabWidth) / 2, getClientArea().height - tabHeight);
		}

		private void calcMaxTextSize(GC gc) {
			Point max = new Point(0, 0);
			List<ISubApplication> subApps = getApplicationModel().getChildren();
			for (ISubApplication app : subApps) {
				Point val = gc.textExtent(app.getLabel());
				if (val.x > max.x) {
					max = val;
				}
			}
			maxTextSize = max;
		}
	}

	@Override
	public void createPartControl(final Composite parent) {
		setPartProperty(TitlelessStackPresentation.PROPERTY_APPLICATION, Boolean.TRUE.toString());
		observeSelectedIndex();
		this.composite = new Switcher(parent, SWT.DOUBLE_BUFFERED);
		parent.setLayout(new FillLayout());
	}

	protected ApplicationModel getApplicationModel() {
		return getSubApplication().getParent().getTypecastedAdapter(ApplicationModel.class);
	}

	protected ISubApplication getSubApplication() {
		String perspectiveID = getViewSite().getPage().getPerspective().getId();
		return SwtPresentationManagerAccessor.getManager().getNavigationNode(perspectiveID, ISubApplication.class);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

	private void observeSelectedIndex() {
		NavigationTreeObserver observer = new NavigationTreeObserver();
		observer.addListener(new SubApplicationAdapter() {

			@Override
			public void activated(ISubApplication source) {
				selected_idx = getApplicationModel().getIndexOfChild(source);
			}
		});

		observer.addListenerTo(getApplicationModel());

		int i = 0;
		for (ISubApplication subApp : getApplicationModel().getChildren()) {
			if (subApp.isActivated()) {
				selected_idx = i;
				return;
			}
			i++;
		}

	}

}
