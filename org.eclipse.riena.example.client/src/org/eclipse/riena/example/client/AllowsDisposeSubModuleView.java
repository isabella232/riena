package org.eclipse.riena.example.client;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.MessageBox;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

public class AllowsDisposeSubModuleView extends SubModuleView {

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		GridLayoutFactory.swtDefaults().applyTo(parent);
		UIControlsFactory.createButtonCheck(parent, "allow Dispose", "allowsDispose"); //$NON-NLS-1$ //$NON-NLS-2$

		final MessageBox messageBox = UIControlsFactory.createMessageBox(parent);
		addUIControl(messageBox, "messageBox"); //$NON-NLS-1$
	}

}
