package org.eclipse.riena.example.client.views;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.riena.example.client.controllers.CustomerDetailViewController;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CustomerDetailView extends SubModuleNodeView<CustomerDetailViewController> {

	private static final int FIELD_WIDTH = 100;
	private final static int TOP = 10;
	private static final int LEFT = 10;
	private static final int SECTION_LABEL_WIDTH = 100;
	private static final int LABEL_WIDTH = 90;
	private static final int LINE_GAP = 12;
	private static final int COL_GAP = 30;
	private Composite contentArea;

	public CustomerDetailView() {
		addPartPropertyListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				System.out.println(event);
			}
		});
	}

	@Override
	public void dispose() {
	}

	@Override
	public void basicCreatePartControl(Composite parent) {

		this.contentArea = parent;
		contentArea.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		contentArea.setLayout(new FormLayout());

		Label personLabel = createSectionLabel(contentArea, "Person");
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, TOP);
		fd.left = new FormAttachment(0, LEFT);
		personLabel.setLayoutData(fd);

		Label kundennummerLabel = new Label(contentArea, SWT.LEFT);
		kundennummerLabel.setText("Customer No.");
		kundennummerLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(personLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(personLabel, SECTION_LABEL_WIDTH, SWT.LEFT);
		kundennummerLabel.setLayoutData(fd);

		Text kundennummerText = new Text(contentArea, SWT.SINGLE);
		kundennummerText.setEditable(false);
		fd = new FormData();
		fd.top = new FormAttachment(kundennummerLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(kundennummerLabel, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		kundennummerText.setLayoutData(fd);

		Label nameLabel = new Label(contentArea, SWT.LEFT);
		nameLabel.setText("Last Name");
		nameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(kundennummerLabel, LINE_GAP);
		fd.left = new FormAttachment(kundennummerLabel, 0, SWT.LEFT);
		nameLabel.setLayoutData(fd);

		Text nameText = new Text(contentArea, SWT.BORDER | SWT.SINGLE);
		fd = new FormData();
		fd.top = new FormAttachment(nameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(kundennummerText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		nameText.setLayoutData(fd);
		nameText.setText(getController().getNavigationNode().getLabel());

		Label vornameLabel = new Label(contentArea, SWT.LEFT);
		vornameLabel.setText("First Name");
		vornameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(nameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(nameLabel, COL_GAP + FIELD_WIDTH + LABEL_WIDTH, SWT.LEFT);
		vornameLabel.setLayoutData(fd);

		Text vornameText = new Text(contentArea, SWT.BORDER | SWT.SINGLE);
		fd = new FormData();
		fd.top = new FormAttachment(vornameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(vornameLabel, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		vornameText.setLayoutData(fd);

		Label birthdayLabel = new Label(contentArea, SWT.LEFT);
		birthdayLabel.setText("Birthday");
		birthdayLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(nameLabel, LINE_GAP);
		fd.left = new FormAttachment(nameLabel, 0, SWT.LEFT);
		birthdayLabel.setLayoutData(fd);

		DateTime birthdayText = new DateTime(contentArea, SWT.BORDER | SWT.SINGLE);
		fd = new FormData();
		fd.top = new FormAttachment(birthdayLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(nameText, 0, SWT.LEFT);
		birthdayText.setLayoutData(fd);

		Label birthplaceLabel = new Label(contentArea, SWT.LEFT);
		birthplaceLabel.setText("Birthplace");
		birthplaceLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(birthdayLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(birthdayLabel, COL_GAP + FIELD_WIDTH + LABEL_WIDTH, SWT.LEFT);
		birthplaceLabel.setLayoutData(fd);

		Text birthplaceText = new Text(contentArea, SWT.BORDER | SWT.SINGLE);
		fd = new FormData();
		fd.top = new FormAttachment(birthplaceLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(birthplaceLabel, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		birthplaceText.setLayoutData(fd);

		Button openOffers = new Button(contentArea, 0);
		openOffers.setText("Offers");
		fd = new FormData();
		fd.top = new FormAttachment(birthplaceText, LINE_GAP);
		fd.left = new FormAttachment(birthdayText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		openOffers.setLayoutData(fd);

		Button saveButton = new Button(contentArea, 0);
		saveButton.setText("Save");
		// saveButton.addSelectionListener(new StoreCustomerListener());
		fd = new FormData();
		fd.top = new FormAttachment(birthplaceText, LINE_GAP);
		fd.left = new FormAttachment(birthplaceText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		saveButton.setLayoutData(fd);
	}

	private Label createSectionLabel(Composite parent, String text) {

		Label label = new Label(parent, SWT.LEFT);
		label.setText(text);
		label.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
		label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		return label;

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView#createController(org.eclipse.riena.navigation.ISubModuleNode)
	 */
	@Override
	protected CustomerDetailViewController createController(ISubModuleNode subModuleNode) {
		return new CustomerDetailViewController(subModuleNode);
	}

}
