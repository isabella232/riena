package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.ui.ridgets.viewcontroller.IViewController;

public interface INavigationNodeView<C extends IViewController, N extends INavigationNode<?>> {

	/**
	 * Binds the navigation node to the view. Creates the widgets and the
	 * controller if necessary.
	 * 
	 * @param node
	 *            The node to bind.
	 */
	void bind(N node);

	/**
	 * Unbinds the navigation node from the view.
	 */
	void unbind();

	/**
	 * Returns the navigation node of this view.
	 * 
	 * @return navigation node
	 */
	N getNavigationNode();

	void addUpdateListener(IComponentUpdateListener listener);

}
