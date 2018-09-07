package org.app.view.email;

import org.app.controler.EmailService;
import org.app.controler.email.CheckingEmails;
import org.app.helper.I18n;
import org.app.view.email.send.SendView;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class EmailTopMenu extends CustomComponent {

	private I18n i18n;
	private CheckingEmails checkingEmails;

	public EmailTopMenu(EmailService service) {
		i18n = new I18n();
		Button callButton = new Button(i18n.EMAIL_CALL,
				e -> {
					checkingEmails = new CheckingEmails();
//					checkingEmails.readEmails(service);
					checkingEmails.check1(service);
					UI.getCurrent().getNavigator().navigateTo(I18n.EMAIL_VIEW);
				});
		callButton.setIcon(VaadinIcons.CLOUD_DOWNLOAD);
		callButton.addStyleName("icon-align-top");

		Button writeButton = new Button(i18n.EMAIL_WRITE, e -> getUI().addWindow(new SendView()));
		writeButton.setIcon(VaadinIcons.EDIT);
		writeButton.addStyleName("icon-align-top");

		Button answerButton = new Button(i18n.EMAIL_ANSWER, e -> getUI().addWindow(new SendView()));
		answerButton.setIcon(VaadinIcons.ARROW_BACKWARD);
		answerButton.addStyleName("icon-align-top");

		Button forwardButton = new Button(i18n.EMAIL_FORWARD, e -> getUI().addWindow(new SendView()));
		forwardButton.setIcon(VaadinIcons.LEVEL_RIGHT_BOLD);
		forwardButton.addStyleName("icon-align-top");

		Button printButton = new Button(i18n.BASIC_PRINT, e -> getUI().addWindow(new SendView()));
		printButton.setIcon(VaadinIcons.PRINT);
		printButton.addStyleName("icon-align-top");

		Button deleteButton = new Button(i18n.BASIC_DELETE, e -> getUI().addWindow(new SendView()));
		deleteButton.setIcon(VaadinIcons.TRASH);
		deleteButton.addStyleName("icon-align-top");

		CssLayout emailNavBar = new CssLayout(callButton, writeButton, answerButton, forwardButton, printButton,
				deleteButton);
		emailNavBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		setCompositionRoot(emailNavBar);
	}

}
