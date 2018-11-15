package org.app.view.mail;

import org.app.controler.EmailService;
import org.app.helper.I18n;
import org.app.mail.imap.Imap;
import org.app.view.mail.send.WriteMail;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class TopMenu extends CssLayout {

	private I18n i18n;
	private Imap imap;

	public TopMenu(EmailService service) {
		i18n = new I18n();
		
		Button callButton = new Button(i18n.EMAIL_CALL,
				e -> {
					imap = new Imap();
					imap.readFromImap(service);
					UI.getCurrent().getNavigator().navigateTo(I18n.MAIL_MAIN_VIEW);
				});
		callButton.setIcon(VaadinIcons.CLOUD_DOWNLOAD);
		callButton.addStyleName("icon-align-top");

		Button writeButton = new Button(i18n.EMAIL_WRITE, e -> getUI().addWindow(new WriteMail(service)));
		writeButton.setIcon(VaadinIcons.EDIT);
		writeButton.addStyleName("icon-align-top");

		Button answerButton = new Button(i18n.EMAIL_ANSWER, e -> getUI().addWindow(new WriteMail(service)));
		answerButton.setIcon(VaadinIcons.ARROW_BACKWARD);
		answerButton.addStyleName("icon-align-top");

		Button forwardButton = new Button(i18n.EMAIL_FORWARD, e -> getUI().addWindow(new WriteMail(service)));
		forwardButton.setIcon(VaadinIcons.LEVEL_RIGHT_BOLD);
		forwardButton.addStyleName("icon-align-top");

		Button printButton = new Button(i18n.BASIC_PRINT, e -> getUI().addWindow(new WriteMail(service)));
		printButton.setIcon(VaadinIcons.PRINT);
		printButton.addStyleName("icon-align-top");

		Button deleteButton = new Button(i18n.BASIC_DELETE, e -> getUI().addWindow(new WriteMail(service)));
		deleteButton.setIcon(VaadinIcons.TRASH);
		deleteButton.addStyleName("icon-align-top");

		CssLayout emailNavBar = new CssLayout(callButton, writeButton, answerButton, forwardButton, printButton,
				deleteButton);
		emailNavBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		
		addComponents(callButton, writeButton, answerButton, forwardButton, printButton,
				deleteButton);
		addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

	}

}
