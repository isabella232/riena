package org.eclipse.riena.navigation.ui.swt.component;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.swt.widgets.Composite;

abstract public class AbstractNavigationComponent<T extends INavigationNode<?>> {

	private T modelNode;

	private List<IComponentUpdateListener> updateListeners;
	private Composite parentComposite;

	public AbstractNavigationComponent(T node, Composite parent) {
		setModelNode(node);
		this.parentComposite = parent;
		this.updateListeners = new ArrayList<IComponentUpdateListener>();
		initObserver();
	}

	protected Composite getParentComposite() {
		return parentComposite;
	}

	abstract protected void initUI();

	abstract protected void initObserver();

	abstract Composite getUI();

	public void addComponentUpdateListener(IComponentUpdateListener listener) {
		updateListeners.add(listener);
	};

	protected void updated() {
		for (IComponentUpdateListener listener : updateListeners) {
			listener.update(getModelNode());
		}
	}

	abstract protected void buildInitialTree();

	protected T getModelNode() {
		return modelNode;
	}

	protected void setModelNode(T node) {
		this.modelNode = node;
	}

}
