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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import org.eclipse.riena.example.client.controllers.DetachedSubModuleController;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.model.SimpleNavigationNodeAdapter;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.DetachedViewsManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Example of opening "detached" views for a given node.
 * 
 * @see DetachedViewsManager
 */
public class DetachedSubModuleView extends SubModuleView<DetachedSubModuleController> {

	public static final String ID = DetachedSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));
		GridDataFactory fillFactory = GridDataFactory.fillDefaults().grab(true, true);

		String msg = "This sample opens two detached views.\n" //$NON-NLS-1$
				+ "Both of them are only visible when the corresponding navigation node is selected.\n"; //$NON-NLS-1$
		Label msgLabel = UIControlsFactory.createLabel(parent, msg);
		fillFactory.applyTo(msgLabel);

		new NodeListener();
	}

	// helping classes
	//////////////////

	/**
	 * Listens to selection / de-selection / disposal of this view's navigation
	 * node.
	 */
	private final class NodeListener extends SimpleNavigationNodeAdapter {

		private final DetachedViewsManager dvManager = new DetachedViewsManager(getSite());

		private NodeListener() {
			getNavigationNode().addSimpleListener(this);
		}

		@Override
		public void activated(INavigationNode<?> source) {
			dvManager.showView(TreeSubModuleView.ID, TreeSubModuleView.class, SWT.RIGHT);
			dvManager.showView(ChoiceSubModuleView.ID, ChoiceSubModuleView.class, SWT.BOTTOM);
		}

		@Override
		public void deactivated(INavigationNode<?> source) {
			dvManager.hideView(TreeSubModuleView.ID);
			dvManager.hideView(ChoiceSubModuleView.ID);
		}

		@Override
		public void disposed(INavigationNode<?> source) {
			// closes all detached views by this manager
			dvManager.dispose();
			// remove this listener - if not removing here, this can also be done in in 
			// the view's dispose method.
			getNavigationNode().removeSimpleListener(this);
		}
	}
}
