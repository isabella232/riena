package org.eclipse.riena.navigation.ui.swt.presentation.stack;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.presentations.AbstractPresentationFactory;
import org.eclipse.ui.presentations.IStackPresentationSite;
import org.eclipse.ui.presentations.StackPresentation;

public class TitlelessStackPresentationFactory extends AbstractPresentationFactory {

	private Map<IStackPresentationSite, TitlelessStackPresentation> presentations;

	public TitlelessStackPresentationFactory() {
		presentations = new HashMap<IStackPresentationSite, TitlelessStackPresentation>();
	}

	@Override
	public StackPresentation createEditorPresentation(Composite parent, IStackPresentationSite site) {
		return new TitlelessStackPresentation(parent, site);
	}

	@Override
	public StackPresentation createStandaloneViewPresentation(Composite parent, IStackPresentationSite site, boolean showTitle) {
		return new TitlelessStackPresentation(parent, site);
	}

	@Override
	public StackPresentation createViewPresentation(Composite parent, IStackPresentationSite site) {
		return getPresentation(parent, site);
	}

	private StackPresentation getPresentation(Composite parent, IStackPresentationSite site) {
		if (presentations.get(site) == null) {
			presentations.put(site, new TitlelessStackPresentation(parent, site));
		}
		return presentations.get(site);
	}

}
