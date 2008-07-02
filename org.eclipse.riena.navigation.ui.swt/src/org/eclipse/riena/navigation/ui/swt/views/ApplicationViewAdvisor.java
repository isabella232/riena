/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.ISubApplicationListener;
import org.eclipse.riena.navigation.model.NavigationTreeObserver;
import org.eclipse.riena.navigation.model.SubApplicationAdapter;
import org.eclipse.riena.navigation.ui.controllers.ApplicationViewController;
import org.eclipse.riena.navigation.ui.swt.binding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.ShellBorderRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.ShellLogoRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.ShellRenderer;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentation;
import org.eclipse.riena.navigation.ui.swt.utils.ImageUtil;
import org.eclipse.riena.ui.ridgets.uibinding.DefaultBindingManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.WorkbenchWindow;

public class ApplicationViewAdvisor extends WorkbenchWindowAdvisor {

	/**
	 * 
	 */
	private static final Point APPLICATION_SIZE = new Point(800, 600);

	enum BtnState {
		NONE, HOVER, HOVER_SELECTED;
	}

	private static final String SHELL_RIDGET_PROPERTY = "windowRidget"; //$NON-NLS-1$

	private ApplicationViewController controller;
	private ISubApplicationListener subApplicationListener;
	private NavigationTreeObserver navigationTreeObserver;

	private List<Object> uiControls;

	public ApplicationViewAdvisor(IWorkbenchWindowConfigurer configurer, ApplicationViewController pController) {
		super(configurer);
		uiControls = new ArrayList<Object>();
		controller = pController;
		initializeListener();
	}

	public void addUIControl(Composite control) {
		uiControls.add(control);
	}

	private void initializeListener() {
		subApplicationListener = new SubApplicationListener();
		navigationTreeObserver = new NavigationTreeObserver();
		navigationTreeObserver.addListener(subApplicationListener);
		navigationTreeObserver.addListenerTo(controller.getNavigationNode());
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionAdvisor(configurer, controller);
	}

	@Override
	public void preWindowOpen() {
		configureView();
	}

	private void configureView() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setShowMenuBar(controller.isMenubarVisible());
		configurer.setShowStatusLine(false);
		configurer.setShowCoolBar(false);
		configurer.setTitle(controller.getNavigationNode().getLabel());
		configurer.setInitialSize(APPLICATION_SIZE);
		if (LnfManager.getLnf().getBooleanSetting(ILnfKeyConstants.SHELL_HIDE_OS_BORDER)) {
			configurer.setShellStyle(SWT.NO_TRIM | SWT.DOUBLE_BUFFERED);
		}
	}

	/**
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#createWindowContents(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	public void createWindowContents(final Shell shell) {

		shell.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, SHELL_RIDGET_PROPERTY);
		addUIControl(shell);
		Image image = LnfManager.getLnf().getImage(ILnfKeyConstants.TITLELESS_SHELL_BACKGROUND_IMAGE);
		shell.setBackgroundImage(image);
		shell.setBackgroundMode(SWT.INHERIT_FORCE);
		shell.setImage(ImageUtil.getImage(controller.getNavigationNode().getIcon()));
		shell.setMinimumSize(APPLICATION_SIZE);
		addListeners(shell);

		// see WorkbenchWindow.createDefaultContents

		WorkbenchWindow workbenchWindow = (WorkbenchWindow) getWindowConfigurer().getWindow();
		Menu menuBar = workbenchWindow.getMenuManager().createMenuBar((Decorations) shell);
		if (getWindowConfigurer().getShowMenuBar()) {
			shell.setMenuBar(menuBar);
		}

		getWindowConfigurer().createCoolBarControl(shell); // ist noch
		// nicht zu
		// sehen.

		FormLayout layout = new FormLayout();
		shell.setLayout(layout);

		// composites for logo
		createLogoComposite(shell);

		// parent of page composite
		ShellBorderRenderer borderRenderer = (ShellBorderRenderer) LnfManager.getLnf().getRenderer(
				ILnfKeyConstants.TITLELESS_SHELL_BORDER_RENDERER);
		int padding = borderRenderer.getCompelteBorderWidth();
		Composite mainComposite = new Composite(shell, SWT.DOUBLE_BUFFERED);
		mainComposite.setLayout(new FillLayout());
		FormData mainData = new FormData();
		mainData.top = new FormAttachment(0, getTopMargin());
		mainData.bottom = new FormAttachment(100, -padding);
		mainData.left = new FormAttachment(0, padding);
		mainData.right = new FormAttachment(100, -padding);
		mainComposite.setLayoutData(mainData);
		// page composite
		getWindowConfigurer().createPageComposite(mainComposite);

		// Composite(shell, SWT.NONE);
		// topComposite.setBounds(100, 0, 12, getTopMargin());
		// GridData gridData = new GridData();
		// gridData.grabExcessHorizontalSpace = false;
		// gridData.grabExcessVerticalSpace = false;
		// topComposite.setLayoutData(gridData);

		// FormLayout layout = new FormLayout();
		// FillLayout fillLayout = new FillLayout();
		// shell.setLayout(layout);
		// FormData f = new FormData();
		// f.width = 790;
		// f.height = 600;
		// f.top = new FormAttachment(0, 0);
		//
		// fillLayout = new FillLayout();
		//
		// Composite c_base = new Composite(shell, SWT.NONE);
		// c_base.setLayoutData(f);
		// c_base.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		// c_base.setLayout(new FormLayout());
		//
		// Composite c1 = new Composite(c_base, SWT.NONE);
		// c1.setLayout(new FillLayout());
		// FormData fd = new FormData();
		// fd.height = 40;
		// fd.width = 790;
		// c1.setLayoutData(fd);
		//
		// fd = new FormData();
		// fd.top = new FormAttachment(c1, 10);
		// fd.height = shell.getBounds().height - 40;
		// fd.width = 790;
		// fd.left = new FormAttachment(0, 0);
		//
		// // new ApplicationSwitchViewPart((ApplicationModel)
		// // controller.getNavigationNode()).createPartControl(c1);
		//
		// Composite c2 = new Composite(c_base, SWT.NONE);
		// c2.setLayout(new FormLayout());
		// c2.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_RED));
		// c2.setLayoutData(fd);
		// // WorkbenchWindow workbenchWindow = (WorkbenchWindow)
		// // getWindowConfigurer().getWindow();
		// // workbenchWindow.getMenuManager().createMenuBar(c2);
		// // c2.setMenu(getWindowConfigurer().createMenuBar());
		//
		// // Shell shell2 = new Shell(shell);
		// Menu menuBar = new Menu(shell, SWT.BAR);
		// // Menu menuBar = getWindowConfigurer().createMenuBar();
		// shell.setMenuBar(menuBar);
		//
		// Control c = getWindowConfigurer().createCoolBarControl(c2);
		// c.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		// fd = new FormData();
		// fd.top = new FormAttachment(0, 00);
		// fd.height = 20;
		// fd.width = 790;
		// fd.left = new FormAttachment(0, 0);
		// c.setLayoutData(fd);
		// Control cx = getWindowConfigurer().createPageComposite(c2);
		// fd = new FormData();
		// fd.top = new FormAttachment(c, 50);
		// fd.height = shell.getBounds().height - 60;
		// fd.width = 1000;
		// fd.left = new FormAttachment(0, 0);
		// cx.setLayoutData(fd);
		//
		// // super.createWindowContents(shell);
	}

	/**
	 * Adds all necessary to the given shell.
	 * 
	 * @param shell
	 */
	private void addListeners(final Shell shell) {

		shell.addPaintListener(new TitlelessPaintListener());

		TitlelessShellListener shellListener = new TitlelessShellListener();
		shell.addShellListener(shellListener);
		shell.addControlListener(shellListener);

		TitlelessShellMouseListener mouseListener = new TitlelessShellMouseListener();
		shell.addMouseListener(mouseListener);
		shell.addMouseMoveListener(mouseListener);
		shell.addMouseTrackListener(mouseListener);

	}

	/**
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowOpen()
	 */
	@Override
	public void postWindowOpen() {
		super.postWindowOpen();
		doInitialBinding();
	}

	private void doInitialBinding() {
		DefaultBindingManager defaultBindingManager = createBindingManager();
		defaultBindingManager.injectRidgets(controller, uiControls);
		defaultBindingManager.bind(controller, uiControls);
		controller.afterBind();
	}

	protected DefaultBindingManager createBindingManager() {
		return new DefaultBindingManager(new SWTBindingPropertyLocator(), new DefaultSwtControlRidgetMapper());
	}

	private class SubApplicationListener extends SubApplicationAdapter {

		/**
		 * @see org.eclipse.riena.navigation.model.NavigationTreeAdapter#activated(org.eclipse.riena.navigation.ISubApplication)
		 */
		@Override
		public void activated(ISubApplication source) {
			if (source != null) {
				showPerspective(source);
			}
			super.activated(source);
		}
	}

	private void showPerspective(ISubApplication source) {
		try {
			PlatformUI.getWorkbench().showPerspective(
					SwtPresentationManagerAccessor.getManager().getSwtViewId(source).getId(),
					PlatformUI.getWorkbench().getActiveWorkbenchWindow());

		} catch (WorkbenchException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the margin between the top of the shell and the widget with the
	 * sub-application switchers.
	 * 
	 * @return margin
	 */
	private int getTopMargin() {

		int margin = LnfManager.getLnf().getIntegerSetting(ILnfKeyConstants.SUB_APPLICATION_SWITCHER_TOP_MARGIN);
		return margin;

	}

	/**
	 * Creates and positions the composite of the logo.
	 * 
	 * @param parent -
	 *            parent composite
	 */
	private void createLogoComposite(Composite parent) {

		assert parent.getLayout() instanceof FormLayout;

		FormData logoData = new FormData();
		ShellBorderRenderer borderRenderer = (ShellBorderRenderer) LnfManager.getLnf().getRenderer(
				ILnfKeyConstants.TITLELESS_SHELL_BORDER_RENDERER);
		int borderWidth = borderRenderer.getBorderWidth();
		Composite topLeftComposite = new Composite(parent, SWT.DOUBLE_BUFFERED);
		logoData.top = new FormAttachment(0, borderWidth);
		int height = TitlelessStackPresentation.calcTabHeight() + getTopMargin() - 1;
		logoData.bottom = new FormAttachment(0, height);
		logoData.width = getLogoImage().getImageData().width + ShellLogoRenderer.getHorizontalLogoMargin() * 2;
		Integer hPos = getHorizontalLogoPosition();
		switch (hPos) {
		case SWT.CENTER:
			logoData.left = new FormAttachment(50, -logoData.width / 2);
			break;
		case SWT.RIGHT:
			logoData.right = new FormAttachment(100, -borderWidth);
			break;
		default:
			logoData.left = new FormAttachment(0, borderWidth);
			break;
		}

		topLeftComposite.setLayoutData(logoData);
		topLeftComposite.addPaintListener(new LogoPaintListener());

	}

	private Image getLogoImage() {
		return LnfManager.getLnf().getImage(ILnfKeyConstants.TITLELESS_SHELL_LOGO);
	}

	/**
	 * Returns the horizontal position of the logo inside the shell.
	 * 
	 * @return horizontal position (SWT.LEFT, SWT.CENTER, SWT.RIGHT)
	 */
	private int getHorizontalLogoPosition() {

		Integer hPos = LnfManager.getLnf().getIntegerSetting(ILnfKeyConstants.TITLELESS_SHELL_HORIZONTAL_LOGO_POSITION);
		if (hPos == null) {
			hPos = SWT.LEFT;
		}
		return hPos;

	}

	/**
	 * Returns the renderer of the shell.
	 * 
	 * @return renderer
	 */
	private ShellRenderer getShellRenderer() {
		ShellRenderer shellRenderer = (ShellRenderer) LnfManager.getLnf().getRenderer(
				ILnfKeyConstants.TITLELESS_SHELL_RENDERER);
		return shellRenderer;
	}

	/**
	 * This listener paints the shell.
	 */
	private class TitlelessPaintListener implements PaintListener {

		/**
		 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
		 */
		public void paintControl(PaintEvent e) {
			onPaint(e);
		}

		/**
		 * Paints the border, title, buttons and background of the (titleless)
		 * shell.
		 * 
		 * @param e -
		 *            event
		 */
		private void onPaint(PaintEvent e) {

			if ((e.getSource() != null) && (e.getSource() instanceof Shell)) {

				Shell shell = (Shell) e.getSource();

				Rectangle bounds = new Rectangle(0, 0, e.width, e.height);

				GC gc = e.gc;

				ILnfRenderer shellRenderer = getShellRenderer();
				shellRenderer.setBounds(bounds);
				shellRenderer.paint(gc, shell);

				ILnfRenderer borderRenderer = LnfManager.getLnf().getRenderer(
						ILnfKeyConstants.TITLELESS_SHELL_BORDER_RENDERER);
				borderRenderer.setBounds(bounds);
				borderRenderer.paint(gc, null);

			}

		}

	}

	/**
	 * When the state of the shell is changed a redraw maybe necessary.
	 */
	private static class TitlelessShellListener implements ShellListener, ControlListener {

		private Rectangle moveBounds;

		/**
		 * @see org.eclipse.swt.events.ShellListener#shellActivated(org.eclipse.swt.events.ShellEvent)
		 */
		public void shellActivated(ShellEvent e) {
			onStateChanged(e);
		}

		/**
		 * @see org.eclipse.swt.events.ShellListener#shellClosed(org.eclipse.swt.events.ShellEvent)
		 */
		public void shellClosed(ShellEvent e) {
		}

		/**
		 * @see org.eclipse.swt.events.ShellListener#shellDeactivated(org.eclipse.swt.events.ShellEvent)
		 */
		public void shellDeactivated(ShellEvent e) {
			onStateChanged(e);
		}

		/**
		 * @see org.eclipse.swt.events.ShellListener#shellDeiconified(org.eclipse.swt.events.ShellEvent)
		 */
		public void shellDeiconified(ShellEvent e) {
			onStateChanged(e);
		}

		/**
		 * @see org.eclipse.swt.events.ShellListener#shellIconified(org.eclipse.swt.events.ShellEvent)
		 */
		public void shellIconified(ShellEvent e) {
		}

		/**
		 * Redraws the shell.
		 * 
		 * @param e -
		 *            event
		 */
		private void onStateChanged(ShellEvent e) {
			if ((e.getSource() != null) && (e.getSource() instanceof Shell)) {
				Shell shell = (Shell) e.getSource();
				shell.redraw();
			}
		}

		/**
		 * @see org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.ControlEvent)
		 */
		public void controlMoved(ControlEvent e) {
			if ((e.getSource() != null) && (e.getSource() instanceof Shell)) {
				Shell shell = (Shell) e.getSource();
				Display display = shell.getDisplay();
				if ((moveBounds == null) || (!displaySurrounds(display, moveBounds))) {
					shell.setRedraw(false);
					shell.setRedraw(true);
					shell.redraw();
				}
				moveBounds = shell.getBounds();
			}
		}

		/**
		 * @see org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.ControlEvent)
		 */
		public void controlResized(ControlEvent e) {
			// noting to do ?
		}

		/**
		 * Returns <code>true</code> if the given bounds are is inside the
		 * area of the display, and <code>false</code> otherwise.
		 * 
		 * @param display -
		 *            display
		 * @param bounds -
		 *            bounds to test for containment
		 * @return <code>true</code> if the rectangle contains the bounds and
		 *         <code>false</code> otherwise
		 */
		private boolean displaySurrounds(Display display, Rectangle bounds) {

			// top left
			if (!display.getBounds().contains(bounds.x, bounds.y)) {
				return false;
			}
			// top right
			if (!display.getBounds().contains(bounds.x + bounds.width, bounds.y)) {
				return false;
			}
			// bottom left
			if (!display.getBounds().contains(bounds.x, bounds.y + bounds.height)) {
				return false;
			}
			// bottom right
			if (!display.getBounds().contains(bounds.x + bounds.width, bounds.y + bounds.height)) {
				return false;
			}

			return true;

		}

	}

	/**
	 * This listener paints the logo.
	 */
	private static class LogoPaintListener implements PaintListener {

		/**
		 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
		 */
		public void paintControl(PaintEvent e) {
			onPaint(e);
		}

		/**
		 * Paints the image of the logo.
		 * 
		 * @param e -
		 *            an event containing information about the paint
		 */
		private void onPaint(PaintEvent e) {

			ShellLogoRenderer renderer = (ShellLogoRenderer) LnfManager.getLnf().getRenderer(
					ILnfKeyConstants.TITLELESS_SHELL_LOGO_RENDERER);
			renderer.setBounds(e.x, e.y, e.width, e.height);
			renderer.paint(e.gc, null);

		}

	}

	/**
	 * After any mouse operation a method of this listener is called.
	 */
	private class TitlelessShellMouseListener implements MouseListener, MouseTrackListener, MouseMoveListener {

		private final static int BTN_COUNT = 3;
		private final static int CLOSE_BTN_INDEX = 0;
		private final static int MAX_BTN_INDEX = 1;
		private final static int MIN_BTN_INDEX = 2;
		private BtnState[] btnStates = new BtnState[BTN_COUNT];
		private boolean mouseDownOnButton;
		private boolean move;
		private Point moveStartPoint;

		public TitlelessShellMouseListener() {
			resetBtnStates();
			mouseDownOnButton = false;
			move = false;
		}

		/**
		 * Returns the shell on which the event initially occurred.
		 * 
		 * @param e -
		 *            mouse event
		 * @return shell or <code>null</code> if source is not a shell.
		 */
		private Shell getShell(MouseEvent e) {

			if (e.getSource() == null) {
				return null;
			}
			if (!(e.getSource() instanceof Shell)) {
				return null;
			}
			return (Shell) e.getSource();

		}

		/**
		 * Resets the states of the buttons.
		 */
		private void resetBtnStates() {
			for (int i = 0; i < btnStates.length; i++) {
				changeBtnState(BtnState.NONE, i);
			}
		}

		/**
		 * Sets the state of a button (and resets the others).
		 * 
		 * @param newState -
		 *            state to set
		 * @param btnIndex -
		 *            button index
		 */
		private void changeBtnState(BtnState newState, int btnIndex) {
			if (newState != BtnState.NONE) {
				resetBtnStates();
			}
			btnStates[btnIndex] = newState;
		}

		/**
		 * Updates the states of the buttons.
		 * 
		 * @param e -
		 *            mouse event
		 */
		private void updateButtonStates(MouseEvent e) {

			Point pointer = new Point(e.x, e.y);
			boolean insideAButton = false;

			resetBtnStates();
			if (getShellRenderer().isInsideCloseButton(pointer)) {
				if (mouseDownOnButton) {
					changeBtnState(BtnState.HOVER_SELECTED, CLOSE_BTN_INDEX);
				} else {
					changeBtnState(BtnState.HOVER, CLOSE_BTN_INDEX);
				}
				insideAButton = true;
			} else if (getShellRenderer().isInsideMaximizeButton(pointer)) {
				if (mouseDownOnButton) {
					changeBtnState(BtnState.HOVER_SELECTED, MAX_BTN_INDEX);
				} else {
					changeBtnState(BtnState.HOVER, MAX_BTN_INDEX);
				}
				insideAButton = true;
			} else if (getShellRenderer().isInsideMinimizeButton(pointer)) {
				if (mouseDownOnButton) {
					changeBtnState(BtnState.HOVER_SELECTED, MIN_BTN_INDEX);
				} else {
					changeBtnState(BtnState.HOVER, MIN_BTN_INDEX);
				}
				insideAButton = true;
			}
			if (!insideAButton) {
				mouseDownOnButton = false;
			}

			boolean redraw = false;
			for (int i = 0; i < btnStates.length; i++) {
				boolean hover = btnStates[i] == BtnState.HOVER;
				boolean pressed = btnStates[i] == BtnState.HOVER_SELECTED && mouseDownOnButton;
				switch (i) {
				case CLOSE_BTN_INDEX:
					if (getShellRenderer().isCloseButtonHover() != hover) {
						getShellRenderer().setCloseButtonHover(hover);
						redraw = true;
					}
					if (getShellRenderer().isCloseButtonPressed() != pressed) {
						getShellRenderer().setCloseButtonPressed(pressed);
						redraw = true;
					}
					break;
				case MAX_BTN_INDEX:
					if (getShellRenderer().isMaximizedButtonHover() != hover) {
						getShellRenderer().setMaximizedButtonHover(hover);
						redraw = true;
					}
					if (getShellRenderer().isMaximizedButtonPressed() != pressed) {
						getShellRenderer().setMaximizedButtonPressed(pressed);
						redraw = true;
					}
					break;
				case MIN_BTN_INDEX:
					if (getShellRenderer().isMinimizedButtonHover() != hover) {
						getShellRenderer().setMinimizedButtonHover(hover);
						redraw = true;
					}
					if (getShellRenderer().isMinimizedButtonPressed() != pressed) {
						getShellRenderer().setMinimizedButtonPressed(pressed);
						redraw = true;
					}
					break;
				}
			}

			if (redraw) {
				Shell shell = getShell(e);
				shell.setRedraw(false);
				shell.setRedraw(true);
			}

		}

		/**
		 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseDoubleClick(MouseEvent e) {
		}

		/**
		 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseDown(MouseEvent e) {
			mouseDownOnButton = true;
			updateButtonStates(e);
			if (!mouseDownOnButton) {
				Point pointer = new Point(e.x, e.y);
				if (getShellRenderer().isInsideMoveArea(pointer)) {
					move = true;
					moveStartPoint = pointer;
				} else {
					move = false;
				}
			}
		}

		/**
		 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseUp(MouseEvent e) {

			Point pointer = new Point(e.x, e.y);

			if (mouseDownOnButton && (getShell(e) != null)) {
				if (getShellRenderer().isInsideCloseButton(pointer)) {
					if (btnStates[CLOSE_BTN_INDEX] == BtnState.HOVER_SELECTED) {
						getShell(e).close();
					}
				} else if (getShellRenderer().isInsideMaximizeButton(pointer)) {
					if (btnStates[MAX_BTN_INDEX] == BtnState.HOVER_SELECTED) {
						boolean maximized = getShell(e).getMaximized();
						getShell(e).setMaximized(!maximized);
					}
				} else if (getShellRenderer().isInsideMinimizeButton(pointer)) {
					if (btnStates[MIN_BTN_INDEX] == BtnState.HOVER_SELECTED) {
						getShell(e).setMinimized(true);
					}
				}
			}

			mouseDownOnButton = false;
			updateButtonStates(e);
			move = false;

		}

		/**
		 * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseEnter(MouseEvent e) {
			updateButtonStates(e);
			move = false;
		}

		/**
		 * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseExit(MouseEvent e) {
			updateButtonStates(e);
			move = false;
		}

		/**
		 * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseHover(MouseEvent e) {
		}

		/**
		 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseMove(MouseEvent e) {
			updateButtonStates(e);
			if (move) {
				Point pointer = new Point(e.x, e.y);
				if (!getShellRenderer().isInsideMoveArea(pointer)) {
					move = false;
				}
				if (move) {
					move(e);
				}
			}
		}

		private void move(MouseEvent e) {
			Point moveEndPoint = new Point(e.x, e.y);
			Shell shell = getShell(e);
			int xMove = moveStartPoint.x - moveEndPoint.x;
			int yMove = moveStartPoint.y - moveEndPoint.y;
			int x = shell.getLocation().x - xMove;
			int y = shell.getLocation().y - yMove;
			shell.setLocation(x, y);
		}

	}

}
