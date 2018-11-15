package org.app.view;

import org.app.helper.I18n;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class TopMainMenu extends CustomComponent {

	public TopMainMenu() {
		setupLayout();
	}

	Button helpViewButton = new Button("Help", e -> UI.getCurrent().getNavigator().navigateTo(I18n.HELP_VIEW));

	Button emailViewButton = new Button("Email", e -> UI.getCurrent().getNavigator().navigateTo(I18n.MAIL_MAIN_VIEW));

	private void setupLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		helpViewButton.setIcon(VaadinIcons.QUESTION);
		helpViewButton.addStyleName("icon-align-top");
		emailViewButton.setIcon(VaadinIcons.ENVELOPE);
		emailViewButton.addStyleName("icon-align-top");

		layout.addComponent(helpViewButton);
		layout.addComponent(emailViewButton);
		setCompositionRoot(layout);
	}

}
