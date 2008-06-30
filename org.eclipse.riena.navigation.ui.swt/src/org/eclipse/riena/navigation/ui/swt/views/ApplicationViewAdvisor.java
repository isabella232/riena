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
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentation;
import org.eclipse.riena.navigation.ui.swt.utils.ImageUtil;
import org.eclipse.riena.ui.ridgets.uibinding.DefaultBindingManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
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
		configurer.setInitialSize(new Point(800, 600));
		// configurer.setShellStyle(SWT.NO_TRIM | SWT.DOUBLE_BUFFERED);
	}

	@Override
	public void createWindowContents(final Shell shell) {

		Image image = LnfManager.getLnf().getImage(ILnfKeyConstants.TITLELESS_SHELL_BACKGROUND_IMAGE);
		shell.setBackgroundImage(image);
		shell.setBackgroundMode(SWT.INHERIT_FORCE);
		shell.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, SHELL_RIDGET_PROPERTY);
		addUIControl(shell);
		shell.setImage(ImageUtil.getImage(controller.getNavigationNode().getIcon()));
		shell.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				onPaint(e);
			}

		});

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
		Composite mainComposite = new Composite(shell, SWT.NONE);
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

	private void onPaint(PaintEvent e) {

		if (e.getSource() instanceof Shell) {

			Rectangle bounds = new Rectangle(0, 0, e.width, e.height);

			// muss in den Renderer
			GC gc = e.gc;
			gc.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.TITLELESS_SHELL_BACKGROUND));
			Image logo = getBackgroundImage();
			if (logo != null) {
				int y = logo.getImageData().height;
				int h = bounds.height - y;
				gc.fillRectangle(0, y, bounds.width, h);
			}

			ILnfRenderer borderRenderer = LnfManager.getLnf().getRenderer(
					ILnfKeyConstants.TITLELESS_SHELL_BORDER_RENDERER);
			borderRenderer.setBounds(bounds);
			borderRenderer.paint(gc, null);

		}

	}

	private Image getBackgroundImage() {
		return LnfManager.getLnf().getImage(ILnfKeyConstants.TITLELESS_SHELL_BACKGROUND_IMAGE);
	}

	private Image getLogoImage() {
		return LnfManager.getLnf().getImage(ILnfKeyConstants.TITLELESS_SHELL_LOGO);
	}

	/**
	 * Returns the margin between the top of the shell and the widget with the
	 * sub-application switchers.<br>
	 * The margin depends on the height of the background image.
	 * 
	 * @return margin.
	 */
	private int getTopMargin() {

		int margin = LnfManager.getLnf().getIntegerSetting(ILnfKeyConstants.TITLELESS_SHELL_PADDING);

		Image bgImage = getBackgroundImage();
		if (bgImage != null) {
			int tabHeight = TitlelessStackPresentation.calcTabHeight();
			int logoMargin = bgImage.getImageData().height - tabHeight;
			margin = Math.max(margin, logoMargin);
		}

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

		ShellBorderRenderer borderRenderer = (ShellBorderRenderer) LnfManager.getLnf().getRenderer(
				ILnfKeyConstants.TITLELESS_SHELL_BORDER_RENDERER);
		int borderWidth = borderRenderer.getBorderWidth();
		int leftNumerator = 0;
		int rightNumerator = 33;
		int leftOffset = 0;
		int rightOffset = 0;
		Integer hPos = getHorizontalLogoPosition();
		switch (hPos) {
		case SWT.CENTER:
			leftNumerator = 34;
			rightNumerator = 66;
			break;
		case SWT.RIGHT:
			leftNumerator = 67;
			rightNumerator = 100;
			rightOffset = -borderWidth;
			break;
		default:
			leftNumerator = 0;
			rightNumerator = 33;
			leftOffset = borderWidth;
			break;
		}

		Image bgImage = getBackgroundImage();
		int height = 0;
		if (bgImage != null) {
			height = bgImage.getImageData().height - 1;
		}
		Composite topLeftComposite = new Composite(parent, SWT.NONE);
		FormData logoData = new FormData();
		logoData.top = new FormAttachment(0, borderWidth);
		logoData.bottom = new FormAttachment(0, height);
		logoData.left = new FormAttachment(leftNumerator, leftOffset);
		logoData.right = new FormAttachment(rightNumerator, rightOffset);
		topLeftComposite.setLayoutData(logoData);
		topLeftComposite.addPaintListener(new LogoPaintListener());

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
	 * Returns the vertical position of the logo inside the shell.
	 * 
	 * @return horizontal position (SWT.TOP, SWT.CENTER, SWT.BOTTOM)
	 */
	private int getVerticalLogoPosition() {

		Integer hPos = LnfManager.getLnf().getIntegerSetting(ILnfKeyConstants.TITLELESS_SHELL_VERTICAL_LOGO_POSITION);
		if (hPos == null) {
			hPos = SWT.TOP;
		}
		return hPos;

	}

	/**
	 * Returns the horizontal margin of the logo image.<br>
	 * Gap between the shell border an the image.
	 * 
	 * @return horizontal margin
	 */
	private Integer getHorizontalLogoMargin() {

		Integer margin = LnfManager.getLnf().getIntegerSetting(ILnfKeyConstants.TITLELESS_SHELL_HORIZONTAL_LOGO_MARGIN);
		if (margin == null) {
			margin = 0;
		}
		return margin;

	}

	/**
	 * Returns the vertical margin of the logo image.<br>
	 * Gap between the shell border an the image.
	 * 
	 * @return horizontal margin
	 */
	private Integer getVerticalLogoMargin() {

		Integer margin = LnfManager.getLnf().getIntegerSetting(ILnfKeyConstants.TITLELESS_SHELL_VERTICAL_LOGO_MARGIN);
		if (margin == null) {
			margin = 0;
		}
		return margin;

	}

	/**
	 * This listener paints the logo.
	 */
	private class LogoPaintListener implements PaintListener {

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

			GC gc = e.gc;

			Image logo = getLogoImage();
			if (logo == null) {
				return;
			}
			int logoWidth = logo.getImageData().width;
			int logoHeight = logo.getImageData().height;
			int hMargin = getHorizontalLogoMargin();
			int vMargin = getVerticalLogoMargin();

			int x = 0;
			Integer hPos = getHorizontalLogoPosition();
			switch (hPos) {
			case SWT.CENTER:
				x = e.width / 2 - logoWidth / 2;
				break;
			case SWT.RIGHT:
				x = e.width - logoWidth - hMargin;
				break;
			default:
				x = hMargin;
				break;
			}
			int y = 0;
			Integer vPos = getVerticalLogoPosition();
			switch (vPos) {
			case SWT.CENTER:
				y = e.height / 2 - logoHeight / 2;
				break;
			case SWT.BOTTOM:
				y = e.height - logoHeight - vMargin;
				break;
			default:
				y = vMargin;
				break;
			}

			gc.drawImage(logo, x, y);

		}

	}

}
