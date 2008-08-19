package org.eclipse.riena.sample.app.client.rcpmail;

import org.eclipse.riena.ui.ridgets.swt.SwtBindingDelegate;
import org.eclipse.riena.ui.ridgets.viewcontroller.IController;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

public class NavigationView extends ViewPart {
	public static final String ID = "org.eclipse.riena.sample.app.client.rcpmail.navigationView";
	
	private SwtBindingDelegate delegate = new SwtBindingDelegate();
	private IController controller = new NavigationViewController();
	private Tree tree;

	/**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
	public void createPartControl(Composite parent) {
		tree = new Tree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		delegate.addUIControl(tree, "tree");
		
		delegate.injectAndBind(controller);
		parent.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				delegate.unbind(controller);
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		tree.setFocus();
	}
}