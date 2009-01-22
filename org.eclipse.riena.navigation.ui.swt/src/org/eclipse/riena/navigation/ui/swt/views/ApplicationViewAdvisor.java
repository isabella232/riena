/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.navigation.ui.swt.Activator;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.listener.ApplicationNodeListener;
import org.eclipse.riena.navigation.listener.ISubApplicationNodeListener;
import org.eclipse.riena.navigation.listener.NavigationTreeObserver;
import org.eclipse.riena.navigation.listener.SubApplicationNodeListener;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.swt.binding.InjectSwtViewBindingDelegate;
import org.eclipse.riena.navigation.ui.swt.component.MenuCoolBarComposite;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.EmbeddedBorderRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ModuleGroupRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ShellBorderRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ShellLogoRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ShellRenderer;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProviderAccessor;
import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentation;
import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.ridgets.swt.uibinding.AbstractViewBindingDelegate;
import org.eclipse.riena.ui.swt.AbstractTitleBarMouseListener;
import org.eclipse.riena.ui.swt.Statusline;
import org.eclipse.riena.ui.swt.StatuslineSpacer;
import org.eclipse.riena.ui.swt.lnf.ILnfRenderer;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.renderer.AbstractTitleBarRenderer;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.ImageUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.osgi.service.log.LogService;

public class ApplicationViewAdvisor extends WorkbenchWindowAdvisor {

	private static final Logger LOGGER = Activator.getDefault().getLogger(ApplicationViewAdvisor.class);
	/**
	 * System property defining the initial width of the application window.
	 */
	private static final String PROPERTY_RIENA_APPLICATION_WIDTH = "riena.application.width"; //$NON-NLS-1$
	/**
	 * System property defining the minimum height of the application window.
	 */
	private static final String PROPERTY_RIENA_APPLICATION_HEIGHT = "riena.application.height"; //$NON-NLS-1$
	/**
	 * The default height of the status line.
	 */
	public static final int STATUSLINE_HEIGHT = 22;
	/**
	 * The default and the minimum size of the application.
	 */
	private static final Point APPLICATION_MIN_SIZE = new Point(800, 600);
	private static final int COOLBAR_TOP_MARGIN = 2;
	public static final String SHELL_RIDGET_PROPERTY = "applicationWindow"; //$NON-NLS-1$

	enum BtnState {
		NONE, HOVER, HOVER_SELECTED;
	}

	private ApplicationController controller;
	private AbstractViewBindingDelegate binding;

	private Composite switcherComposite;
	private TitlelessShellMouseListener mouseListener;

	public ApplicationViewAdvisor(IWorkbenchWindowConfigurer configurer, ApplicationController pController) {
		super(configurer);
		controller = pController;
		binding = createBinding();
		initializeListener();
	}

	public void addUIControl(Composite control, String propertyName) {
		binding.addUIControl(control, propertyName);
	}

	private void initializeListener() {
		ISubApplicationNodeListener subApplicationListener = new MySubApplicationNodeListener();
		NavigationTreeObserver navigationTreeObserver = new NavigationTreeObserver();
		navigationTreeObserver.addListener(subApplicationListener);
		navigationTreeObserver.addListener(new MyApplicationNodeListener());
		navigationTreeObserver.addListenerTo(controller.getNavigationNode());
	}

	/**
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowOpen()
	 */
	@Override
	public void preWindowOpen() {
		configureWindow();
	}

	/**
	 * Configures the window of the application.
	 */
	private void configureWindow() {

		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setTitle(controller.getNavigationNode().getLabel());
		initApplicationSize(configurer);
		if (LnfManager.getLnf().getBooleanSetting(LnfKeyConstants.SHELL_HIDE_OS_BORDER)) {
			// don't show the shell border (with the minimize, maximize and
			// close buttons) of the operation system
			configurer.setShellStyle(SWT.NO_TRIM | SWT.DOUBLE_BUFFERED);
		}

	}

	/**
	 * Reads the two properties for the initial width and the initial height of
	 * the application.
	 * 
	 * @param configurer
	 */
	private void initApplicationSize(IWorkbenchWindowConfigurer configurer) {

		int width = APPLICATION_MIN_SIZE.x;
		String widthStrg = System.getProperty(PROPERTY_RIENA_APPLICATION_WIDTH);
		if (!StringUtils.isEmpty(widthStrg)) {
			width = Integer.parseInt(widthStrg);
		}
		if (width < APPLICATION_MIN_SIZE.x) {
			width = APPLICATION_MIN_SIZE.x;
			String message = "The initial width of the application is less than the minimum width!"; //$NON-NLS-1$
			LOGGER.log(LogService.LOG_WARNING, message);
		}

		int height = APPLICATION_MIN_SIZE.y;
		String heightStrg = System.getProperty(PROPERTY_RIENA_APPLICATION_HEIGHT);
		if (!StringUtils.isEmpty(heightStrg)) {
			height = Integer.parseInt(heightStrg);
		}
		if (height < APPLICATION_MIN_SIZE.y) {
			height = APPLICATION_MIN_SIZE.y;
			String message = "The initial height of the application is less than the minimum height!"; //$NON-NLS-1$
			LOGGER.log(LogService.LOG_WARNING, message);
		}

		configurer.setInitialSize(new Point(width, height));

	}

	/**
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowOpen()
	 */
	@Override
	public void postWindowOpen() {
		if (switcherComposite != null) {
			// Redraw the switcher so that the active tab is displayed correct
			switcherComposite.setRedraw(false);
			switcherComposite.setRedraw(true);
		}
		super.postWindowOpen();
		doInitialBinding();
	}

	private void doInitialBinding() {
		binding.injectAndBind(controller);
		controller.afterBind();
		controller.getNavigationNode().activate();
	}

	/**
	 * Creates a delegate for the binding of view and controller.
	 * 
	 * @return delegate for binding
	 */
	protected AbstractViewBindingDelegate createBinding() {
		return new InjectSwtViewBindingDelegate();
	}

	/**
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#dispose()
	 */
	@Override
	public void dispose() {

		super.dispose();

		if (mouseListener != null) {
			mouseListener.dispose();
			mouseListener = null;
		}

	}

	/**
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#createWindowContents(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	public void createWindowContents(final Shell shell) {

		initShell(shell);

		// create and layouts the composite of switcher, menu, tool bar etc.
		shell.setLayout(new FormLayout());
		Composite grabCorner = createGrabCorner(shell);
		createStatusLine(shell, grabCorner);
		Composite titleComposite = createTitleComposite(shell);
		Composite menuBarComposite = createMenuBarComposite(shell, titleComposite);
		Composite coolBarComposite = createCoolBarComposite(shell, menuBarComposite);
		createMainComposite(shell, coolBarComposite);
	}

	private void createStatusLine(Composite shell, Composite grabCorner) {
		Statusline statusLine = new Statusline(shell, SWT.None, StatuslineSpacer.class);
		FormData fd = new FormData();
		fd.height = STATUSLINE_HEIGHT;
		Rectangle navigationBounds = TitlelessStackPresentation.calcNavigationBounds(shell);
		fd.left = new FormAttachment(0, navigationBounds.x + navigationBounds.width
				+ TitlelessStackPresentation.PADDING_RIGHT);
		fd.right = new FormAttachment(grabCorner, 0);
		fd.bottom = new FormAttachment(100, -5);
		statusLine.setLayoutData(fd);
		addUIControl(statusLine, "statuslineRidget"); //$NON-NLS-1$
	}

	/**
	 * Initializes the given shell.
	 * 
	 * @param shell
	 *            - shell to initialize
	 */
	private void initShell(final Shell shell) {
		shell.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.TITLELESS_SHELL_BACKGROUND));
		shell.addPaintListener(new ShellPaintListener());

		shell.setImage(ImageUtil.getImage(controller.getNavigationNode().getIcon()));
		shell.setMinimumSize(APPLICATION_MIN_SIZE);

		// prepare shell for binding
		addUIControl(shell, SHELL_RIDGET_PROPERTY);

		if (getShellRenderer() != null) {
			getShellRenderer().setShell(shell);
		}
	}

	/**
	 * Returns the menu manager of the main menu (menu bar).
	 * 
	 * @return menu manager
	 */
	private MenuManager getMenuManager() {

		WorkbenchWindow workbenchWindow = (WorkbenchWindow) getWindowConfigurer().getWindow();
		return workbenchWindow.getMenuManager();

	}

	private static class MyApplicationNodeListener extends ApplicationNodeListener {

		@Override
		public void filterAdded(IApplicationNode source, IUIFilter filter) {
			super.filterAdded(source, filter);
			try {
				NavigationViewPart navi = (NavigationViewPart) getActivePage().showView(NavigationViewPart.ID);
				navi.updateNavigationSize();
			} catch (PartInitException e) {
				e.printStackTrace();
			}

		}

		private IWorkbenchPage getActivePage() {
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		}

		@Override
		public void filterRemoved(IApplicationNode source, IUIFilter filter) {
			super.filterRemoved(source, filter);

			try {
				NavigationViewPart navi = (NavigationViewPart) getActivePage().showView(NavigationViewPart.ID);
				navi.updateNavigationSize();
			} catch (PartInitException e) {
				e.printStackTrace();
			}

		}

	}

	private class MySubApplicationNodeListener extends SubApplicationNodeListener {

		/**
		 * @see org.eclipse.riena.navigation.model.NavigationTreeAdapter#activated(org.eclipse.riena.navigation.ISubApplicationNode)
		 */
		@Override
		public void activated(ISubApplicationNode source) {
			if (source != null) {
				showPerspective(source);
				switcherComposite.setRedraw(false);
				switcherComposite.setRedraw(true);
			}
			super.activated(source);
		}

		private void showPerspective(ISubApplicationNode source) {
			try {
				PlatformUI.getWorkbench().showPerspective(
						SwtViewProviderAccessor.getViewProvider().getSwtViewId(source).getId(),
						PlatformUI.getWorkbench().getActiveWorkbenchWindow());
			} catch (WorkbenchException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns the margin between the top of the shell and the widget with the
	 * sub-application switchers.
	 * 
	 * @return margin
	 */
	private int getSwitchterTopMargin() {

		int margin = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TOP_MARGIN);
		return margin;

	}

	/**
	 * Returns the of the sub-application switcher.
	 * 
	 * @return height
	 */
	private int getSwitchterHeight() {

		int margin = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.SUB_APPLICATION_SWITCHER_HEIGHT);
		return margin;

	}

	/**
	 * Create the composite that contains the:
	 * <ul>
	 * <li>shell title and title buttons</li>
	 * <li>the logo</li>
	 * <li>the sub application switcher</li>
	 * </ul>
	 * 
	 * @param parent
	 *            the parent composite (non null)
	 * @return the title composite (never null)
	 */
	private Composite createTitleComposite(final Composite parent) {
		Composite result = new Composite(parent, SWT.NONE);

		// force result's background into logo and switcher
		result.setBackgroundMode(SWT.INHERIT_FORCE);
		// sets the background of the composite
		Image image = LnfManager.getLnf().getImage(LnfKeyConstants.TITLELESS_SHELL_BACKGROUND_IMAGE);
		result.setBackgroundImage(image);

		result.setLayout(new FormLayout());
		ShellBorderRenderer borderRenderer = (ShellBorderRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.TITLELESS_SHELL_BORDER_RENDERER);
		int borderWidth = borderRenderer.getBorderWidth();
		FormData data = new FormData();
		data.top = new FormAttachment(parent, borderWidth);
		data.left = new FormAttachment(0, borderWidth);
		data.right = new FormAttachment(100, -borderWidth);
		result.setLayoutData(data);

		createLogoComposite(result);
		switcherComposite = createSwitcherComposite(result);

		result.addPaintListener(new TitlelessPaintListener());

		mouseListener = new TitlelessShellMouseListener();
		result.addMouseListener(mouseListener);
		result.addMouseMoveListener(mouseListener);
		result.addMouseTrackListener(mouseListener);

		return result;
	}

	/**
	 * Creates and positions the composite of the logo.
	 * 
	 * @param parent
	 *            - parent composite
	 */
	private void createLogoComposite(Composite parent) {

		Assert.isTrue(parent.getLayout() instanceof FormLayout);

		Composite logoComposite = new Composite(parent, SWT.DOUBLE_BUFFERED);
		FormData logoData = new FormData();
		int topInset = 4;
		int leftRightInset = 12;
		logoData.top = new FormAttachment(0, topInset);
		int height = getSwitchterTopMargin() + getSwitchterHeight() - 1;
		logoData.bottom = new FormAttachment(0, height);
		Image logoImage = getLogoImage();
		if (logoImage == null) {
			return;
		}
		ImageData imageData = logoImage.getImageData();
		if (imageData == null) {
			return;
		}
		logoData.width = imageData.width + ShellLogoRenderer.getHorizontalLogoMargin() * 2;
		Integer hPos = getHorizontalLogoPosition();
		switch (hPos) {
		case SWT.CENTER:
			logoData.left = new FormAttachment(50, -logoData.width / 2);
			break;
		case SWT.RIGHT:
			logoData.right = new FormAttachment(100, -leftRightInset);
			break;
		default:
			logoData.left = new FormAttachment(0, leftRightInset);
			break;
		}
		logoComposite.setLayoutData(logoData);

		logoComposite.addPaintListener(new LogoPaintListener());

	}

	/**
	 * Creates and positions the corner to grab.
	 * 
	 * @param shell
	 */
	private GrabCorner createGrabCorner(final Shell shell) {

		if (GrabCorner.isResizeable() && LnfManager.getLnf().getBooleanSetting(LnfKeyConstants.SHELL_HIDE_OS_BORDER)) {
			return new GrabCorner(shell, SWT.DOUBLE_BUFFERED);
		}

		return null;

	}

	/**
	 * Creates and positions the composite for the sub-application switcher.
	 * 
	 * @param parent
	 *            - parent of composite
	 * @return composite
	 */
	private Composite createSwitcherComposite(Composite parent) {

		Assert.isTrue(parent.getLayout() instanceof FormLayout);

		int padding = getShellPadding();

		Composite composite = new Composite(parent, SWT.DOUBLE_BUFFERED);
		composite.setLayout(new FillLayout());
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, getSwitchterTopMargin() + padding);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, -0);
		formData.height = getSwitchterHeight();
		composite.setLayoutData(formData);
		ApplicationNode node = (ApplicationNode) controller.getNavigationNode();
		SubApplicationSwitcherViewPart switcherViewPart = new SubApplicationSwitcherViewPart(node);
		switcherViewPart.createPartControl(composite);

		return composite;

	}

	/**
	 * Creates and positions the composite for the menu bar.
	 * 
	 * @param parent
	 *            - parent of composite
	 * @param previous
	 *            - previous composite in the layout
	 * @return composite
	 */
	private Composite createMenuBarComposite(Composite parent, Composite previous) {

		Assert.isTrue(parent.getLayout() instanceof FormLayout);

		int padding = getShellPadding();

		// menu bar
		MenuCoolBarComposite composite = new MenuCoolBarComposite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());

		createMenuBar(composite);

		FormData formData = new FormData();
		formData.top = new FormAttachment(previous, COOLBAR_TOP_MARGIN);
		formData.left = new FormAttachment(0, padding);
		formData.right = new FormAttachment(100, -padding);
		composite.setLayoutData(formData);

		return composite;

	}

	/**
	 * Creates a cool bar with menus.
	 * 
	 * @param parent
	 * @return cool bar with menus
	 */
	private void createMenuBar(MenuCoolBarComposite parent) {

		IContributionItem[] contribItems = getMenuManager().getItems();
		for (int i = 0; i < contribItems.length; i++) {
			if (contribItems[i] instanceof MenuManager) {
				MenuManager topMenuManager = (MenuManager) contribItems[i];
				parent.createAndAddMenu(topMenuManager);
			}
		}

	}

	/**
	 * Creates and positions the composite for the cool bar.
	 * 
	 * @param parent
	 *            - parent of composite
	 * @param previous
	 *            - previous composite in the layout
	 * @return composite
	 */
	private Composite createCoolBarComposite(Composite parent, Composite previous) {

		Assert.isTrue(parent.getLayout() instanceof FormLayout);

		int padding = getCoolBarSeparatorPadding();

		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData formData = new FormData();
		formData.top = new FormAttachment(previous);
		formData.left = new FormAttachment(0, padding);
		formData.right = new FormAttachment(100, -padding);
		separator.setLayoutData(formData);

		padding = getShellPadding();
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		formData = new FormData();
		formData.top = new FormAttachment(previous, COOLBAR_TOP_MARGIN);
		formData.left = new FormAttachment(0, padding);
		formData.right = new FormAttachment(100, -padding);
		composite.setLayoutData(formData);

		Control control = getWindowConfigurer().createCoolBarControl(composite);
		if (control instanceof CoolBar) {
			CoolBar coolbar = (CoolBar) control;
			MenuCoolBarComposite.initCoolBar(coolbar);
		}

		return composite;

	}

	private int getCoolBarSeparatorPadding() {

		ModuleGroupRenderer mgRenderer = (ModuleGroupRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.MODULE_GROUP_RENDERER);
		if (mgRenderer == null) {
			mgRenderer = new ModuleGroupRenderer();
		}
		int padding = mgRenderer.getModuleGroupPadding();

		EmbeddedBorderRenderer borderRenderer = (EmbeddedBorderRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.SUB_MODULE_VIEW_BORDER_RENDERER);
		if (borderRenderer == null) {
			borderRenderer = new EmbeddedBorderRenderer();
		}
		padding += borderRenderer.getBorderWidth();

		padding += getShellPadding();

		return padding;
	}

	/**
	 * Creates the main composite.
	 * 
	 * @param parent
	 *            - parent of composite
	 * @param previous
	 *            - previous composite in the layout
	 * @return composite
	 */
	private Composite createMainComposite(Composite parent, Composite previous) {

		Assert.isTrue(parent.getLayout() instanceof FormLayout);

		int padding = getShellPadding();

		Composite composite = new Composite(parent, SWT.DOUBLE_BUFFERED);
		composite.setLayout(new FillLayout());
		FormData formData = new FormData();
		formData.top = new FormAttachment(previous, 0, 0);
		formData.bottom = new FormAttachment(100, -padding);
		formData.left = new FormAttachment(0, padding);
		formData.right = new FormAttachment(100, -padding);
		composite.setLayoutData(formData);
		getWindowConfigurer().createPageComposite(composite);
		return composite;

	}

	/**
	 * Returns the padding between shell border and content.
	 * 
	 * @return padding
	 */
	private int getShellPadding() {

		ShellBorderRenderer borderRenderer = (ShellBorderRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.TITLELESS_SHELL_BORDER_RENDERER);
		return borderRenderer.getCompleteBorderWidth();

	}

	/**
	 * Returns the image of the logo.
	 * 
	 * @return logo image
	 */
	private Image getLogoImage() {
		return LnfManager.getLnf().getImage(LnfKeyConstants.TITLELESS_SHELL_LOGO);
	}

	/**
	 * Returns the horizontal position of the logo inside the shell.
	 * 
	 * @return horizontal position (SWT.LEFT, SWT.CENTER, SWT.RIGHT)
	 */
	private int getHorizontalLogoPosition() {

		Integer hPos = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.TITLELESS_SHELL_HORIZONTAL_LOGO_POSITION);
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
				LnfKeyConstants.TITLELESS_SHELL_RENDERER);
		return shellRenderer;
	}

	/**
	 * This listener paints (top part of) the shell.
	 */
	private class TitlelessPaintListener implements PaintListener {

		/**
		 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
		 */
		public void paintControl(PaintEvent e) {
			onPaint(e);
		}

		/**
		 * Paints the title, buttons and background of the (titleless) shell.
		 * 
		 * @param e
		 *            - event
		 */
		private void onPaint(PaintEvent e) {

			if (e.getSource() instanceof Control) {

				Control shell = (Control) e.getSource();

				ShellRenderer shellRenderer = getShellRenderer();
				Rectangle shellBounds = shell.getBounds();
				Rectangle bounds = new Rectangle(0, 0, shellBounds.width, shellRenderer.getHeight());
				shellRenderer.setBounds(bounds);
				RienaDefaultLnf lnf = LnfManager.getLnf();
				shellRenderer.setCloseable(lnf.getBooleanSetting(LnfKeyConstants.TITLELESS_SHELL_SHOW_CLOSE));
				shellRenderer.setMaximizable(lnf.getBooleanSetting(LnfKeyConstants.TITLELESS_SHELL_SHOW_MAX));
				shellRenderer.setMinimizable(lnf.getBooleanSetting(LnfKeyConstants.TITLELESS_SHELL_SHOW_MIN));
				shellRenderer.paint(e.gc, shell);

			}

		}
	}

	/**
	 * This listener paints the shell (the border of the shell).
	 */
	private static class ShellPaintListener implements PaintListener {

		/**
		 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
		 */
		public void paintControl(PaintEvent e) {
			onPaint(e);
		}

		/**
		 * Paints the border of the (titleless) shell.
		 * 
		 * @param e
		 *            - event
		 */
		private void onPaint(PaintEvent e) {

			if (e.getSource() instanceof Control) {

				Control shell = (Control) e.getSource();

				Rectangle shellBounds = shell.getBounds();
				Rectangle bounds = new Rectangle(0, 0, shellBounds.width, shellBounds.height);

				ILnfRenderer borderRenderer = LnfManager.getLnf().getRenderer(
						LnfKeyConstants.TITLELESS_SHELL_BORDER_RENDERER);
				borderRenderer.setBounds(bounds);
				borderRenderer.paint(e.gc, null);

			}

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
		 * @param e
		 *            - an event containing information about the paint
		 */
		private void onPaint(PaintEvent e) {

			Composite logoComposite = (Composite) e.getSource();
			Rectangle compositeBounds = logoComposite.getBounds();
			ShellLogoRenderer renderer = (ShellLogoRenderer) LnfManager.getLnf().getRenderer(
					LnfKeyConstants.TITLELESS_SHELL_LOGO_RENDERER);
			renderer.setBounds(compositeBounds);
			renderer.paint(e.gc, null);

		}

	}

	/**
	 * After any mouse operation a method of this listener is called.
	 */
	private class TitlelessShellMouseListener extends AbstractTitleBarMouseListener {

		@Override
		protected AbstractTitleBarRenderer getTitleBarRenderer() {
			return getShellRenderer();
		}

	}

}
