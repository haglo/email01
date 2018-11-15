package org.app.view.mail.settings;

import org.app.helper.I18n;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class Settings extends Window {

	private I18n i18n;
	private VerticalLayout subContent;
	private CssLayout topBar;
	private VerticalLayout mainContent;

	public Settings() {
		i18n = new I18n();
		subContent = new VerticalLayout();
		this.setWidth("80%");
		this.setCaption(i18n.EMAIL_SETTINGS);
		this.setContent(subContent);
		this.center();
		
		topBar = new CssLayout();
		topBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		mainContent = new VerticalLayout();
		mainContent.setSizeFull();

		Button signatureButton = new Button(i18n.EMAIL_SIGNATURE, ev -> {

		});
		signatureButton.setIcon(VaadinIcons.FILE_TEXT_O);
		signatureButton.addStyleName("icon-align-top");

		topBar.addComponent(signatureButton);

		subContent.addComponent(topBar);
		subContent.addComponent(mainContent);

	}

}