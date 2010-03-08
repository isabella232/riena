/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.swt;

import java.util.concurrent.CountDownLatch;

import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.TimelineState;
import org.pushingpixels.trident.callback.TimelineCallbackAdapter;
import org.pushingpixels.trident.ease.Sine;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import org.eclipse.riena.ui.swt.utils.IPropertyNameProvider;
import org.eclipse.riena.ui.swt.utils.ImageStore;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Widget class for a message the turns up on top of the SubModuleView. No user
 * interaction is possible and it closes after a few seconds. <br>
 * It is possible to set a message and an icon.
 * 
 * @since 2.0
 */
//TODO [sac] a lot of stuff has to be put into the LnF.
public class InfoFlyout implements IPropertyNameProvider {

	private static final int WIDTH = 300;
	private static final int HEIGHT = 46;
	private static final int SHELL_RIGHT_INDENT = 21;
	private static final int ICON_LEFT_INDENT = 13;
	private static final int TEXT_LEFT_INDENT = 3;

	private String message;
	private String icon;

	private String bindingId;

	private Shell shell;
	private final Composite parent;

	private Timeline tShow;
	private Timeline tWait;
	private Timeline tHide;

	private Label rightLabel;
	private Label leftLabel;
	private Rectangle topLevelShellBounds;
	private int xPosition;
	private int startY;
	private int endY;

	private CountDownLatch latch;

	public InfoFlyout(Composite parent) {
		this.parent = parent;
		message = ""; //$NON-NLS-1$
		icon = null;
		latch = new CountDownLatch(0);
		initializeLayout();
	}

	public void openFlyout() {
		if (!isAnimationGoingOn()) {
			latch = new CountDownLatch(1);
			updateIconAndMessage();
			updateLocation();
			updateLayoutData();
			initializeTimelines();

			shell.setVisible(true);
			shell.open();
			tShow.play();
		}
	}

	/**
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public final void setPropertyName(String bindingId) {
		this.bindingId = bindingId;
	}

	public final String getPropertyName() {
		return bindingId;
	}

	/**
	 * Lets the InfoFlyout wait until the last one is closed.
	 */
	public void waitForClosing() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// helping methods
	//////////////////
	private void initializeLayout() {
		Assert.isTrue(shell == null); // only call once

		Shell parentShell = parent.getShell();
		parentShell.addControlListener(new CloseOnParentMove());

		shell = new Shell(parentShell, SWT.MODELESS | SWT.NO_TRIM);
		shell.addPaintListener(new BorderPainter());
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(shell);
		LocalResourceManager rsm = new LocalResourceManager(JFaceResources.getResources(), shell);
		Color bgColor = rsm.createColor(new RGB(222, 237, 244));
		shell.setBackground(bgColor);

		leftLabel = UIControlsFactory.createLabel(shell, ""); //$NON-NLS-1$
		leftLabel.setImage(ImageStore.getInstance().getImage("arrowright")); //$NON-NLS-1$
		leftLabel.setBackground(bgColor);

		rightLabel = UIControlsFactory.createLabel(shell, message, SWT.WRAP);
		rightLabel.setBackground(bgColor);
		FontData fontData = rightLabel.getFont().getFontData()[0];
		fontData.setStyle(SWT.BOLD);
		Font boldFont = rsm.createFont(FontDescriptor.createFrom(fontData));
		rightLabel.setFont(boldFont);

		updateLocation();
		updateLayoutData();
	}

	private void updateIconAndMessage() {
		rightLabel.setText(message);
		leftLabel.setImage(ImageStore.getInstance().getImage(icon));
		shell.layout(true);
	}

	private void initializeTimelines() {
		tShow = new Timeline(shell);
		tWait = new Timeline(shell);
		tHide = new Timeline(shell);

		tShow.addPropertyToInterpolate(Timeline
				.<Point> property("location").fromCurrent().to(new Point(xPosition, endY))); //$NON-NLS-1$
		tShow.addPropertyToInterpolate(Timeline.<Point> property("size").fromCurrent() //$NON-NLS-1$
				.to(new Point(WIDTH, HEIGHT)));

		tHide.addPropertyToInterpolate(Timeline
				.<Point> property("location").fromCurrent().to(new Point(xPosition, startY))); //$NON-NLS-1$
		tHide.addPropertyToInterpolate(Timeline.<Point> property("size").fromCurrent() //$NON-NLS-1$
				.to(new Point(WIDTH, 0)));

		tShow.addCallback(new TimelineCallbackAdapter() {
			public void onTimelineStateChanged(TimelineState oldState, TimelineState newState, float durationFraction,
					float timelinePosition) {
				if (newState == TimelineState.DONE) {
					tWait.play();
				}
			}
		});

		tWait.addCallback(new TimelineCallbackAdapter() {
			public void onTimelineStateChanged(TimelineState oldState, TimelineState newState, float durationFraction,
					float timelinePosition) {
				if (newState == TimelineState.DONE) {
					tHide.play();
				}
			}
		});

		tHide.addCallback(new TimelineCallbackAdapter() {
			public void onTimelineStateChanged(TimelineState oldState, TimelineState newState, float durationFraction,
					float timelinePosition) {
				if (newState == TimelineState.DONE) {
					synchronized (this) {
						latch.countDown();
						System.err.println("countdown");
						//						latch = new CountDownLatch(1);
					}
				}
			}
		});

		tShow.setDuration(1500);
		tWait.setDuration(2500);
		tHide.setDuration(1500);

		tHide.setEase(new Sine());
		tShow.setEase(new Sine());

	}

	private void updateLayoutData() {
		int topIndent = (HEIGHT - rightLabel.getBounds().height) / 2;
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).indent(ICON_LEFT_INDENT, topIndent).applyTo(
				leftLabel);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).indent(TEXT_LEFT_INDENT, topIndent).hint(250,
				SWT.DEFAULT).applyTo(rightLabel);

	}

	private void updateLocation() {
		topLevelShellBounds = PlatformUI.getWorkbench().getDisplay().getShells()[0].getBounds();
		xPosition = topLevelShellBounds.x + topLevelShellBounds.width - WIDTH - SHELL_RIGHT_INDENT;
		startY = parent.getDisplay().map(parent.getParent(), null, parent.getBounds()).y;
		endY = startY - HEIGHT;

		shell.setSize(WIDTH, 0);
		shell.setLocation(xPosition, startY);
	}

	private boolean isAnimationGoingOn() {
		if (tShow == null || tWait == null || tHide == null) {
			return false;
		}
		boolean isPlaying = tShow.getState() == TimelineState.PLAYING_FORWARD;
		isPlaying |= tWait.getState() == TimelineState.PLAYING_FORWARD;
		isPlaying |= tHide.getState() == TimelineState.PLAYING_FORWARD;
		return isPlaying;
	}

	// helping classes
	//////////////////

	private static final class BorderPainter implements PaintListener {

		public void paintControl(PaintEvent e) {
			Control control = (Control) e.widget;
			GC gc = e.gc;

			Color oldFg = gc.getForeground();
			int oldWidth = gc.getLineWidth();
			gc.setForeground(control.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			gc.setLineWidth(1);

			Rectangle bounds = control.getBounds();
			gc.drawLine(0, 0, bounds.width - 1, 0);
			gc.drawLine(0, 0, 0, bounds.height - 1);
			gc.drawLine(bounds.width - 1, 0, bounds.width - 1, bounds.height);

			gc.setForeground(oldFg);
			gc.setLineWidth(oldWidth);
		}

	}

	/**
	 * Closes the InfoFlyout, if the shell is moved or resized.
	 */
	private final class CloseOnParentMove implements ControlListener {

		public void controlMoved(ControlEvent e) {
			close();
		}

		public void controlResized(ControlEvent e) {
			close();
		}

		// helping methods
		//////////////////

		private void close() {
			if (!shell.isDisposed() && shell.isVisible()) {
				shell.setVisible(false);
				tShow.abort();
				tWait.abort();
				tHide.abort();
				latch.countDown();
			}
		}
	}

}