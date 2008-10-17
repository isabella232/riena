package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.riena.example.client.controllers.FilterExternalDefinitionSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class FilterExternalDefinitionSubModuleView extends SubModuleView<FilterExternalDefinitionSubModuleController> {

	@Override
	protected void basicCreatePartControl(Composite parent) {

		parent.setLayout(new GridLayout(1, false));

		Group group1 = createFiltersGroup(parent);
		GridDataFactory.fillDefaults().grab(false, false).applyTo(group1);

	}

	private Group createFiltersGroup(Composite parent) {

		Group group = UIControlsFactory.createGroup(parent, "UI-Filters (External definition)"); //$NON-NLS-1$

		int defaultVSpacing = new GridLayout().verticalSpacing;
		GridLayoutFactory.swtDefaults().numColumns(1).equalWidth(false).margins(10, 20).spacing(10, defaultVSpacing)
				.applyTo(group);

		//		Label label1 = UIControlsFactory.createLabel(group, "Node Label"); //$NON-NLS-1$
		//		GridDataFactory.fillDefaults().grab(false, false).span(1, 1).applyTo(label1);
		//		Text nodeLabel = UIControlsFactory.createText(group);
		//		addUIControl(nodeLabel, "nodeLabel"); //$NON-NLS-1$
		//		GridDataFactory.fillDefaults().grab(true, false).span(1, 1).applyTo(nodeLabel);
		//
		//		ChoiceComposite filterType = new ChoiceComposite(group, SWT.NONE, false);
		//		filterType.setOrientation(SWT.HORIZONTAL);
		//		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(filterType);
		//		addUIControl(filterType, "filterType"); //$NON-NLS-1$
		//
		//		Combo filterTypeValues = UIControlsFactory.createCombo(group);
		//		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(filterTypeValues);
		//		addUIControl(filterTypeValues, "filterTypeValues"); //$NON-NLS-1$

		Button addFilter = UIControlsFactory.createButton(group);
		addFilter.setText("Enable offline filter"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).span(1, 1).applyTo(addFilter);
		addUIControl(addFilter, "addFilter"); //$NON-NLS-1$

		Button removeFilters = UIControlsFactory.createButton(group);
		removeFilters.setText("Disable offline filter"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).span(1, 1).applyTo(removeFilters);
		addUIControl(removeFilters, "removeFilters"); //$NON-NLS-1$

		return group;

	}
}
