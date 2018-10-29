package org.app.view.email;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.EmailService;
import org.app.controler.email.Const;
import org.app.helper.I18n;
import org.app.view.email.inbox.InboxMessagePlainText;
import org.app.view.email.inbox.InboxSubject;
import org.app.view.email.settings.SettingsView;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(I18n.EMAIL_VIEW)
public class EmailView extends VerticalLayout implements View, Const {

	@Inject
	EmailService service;

	private I18n i18n;
	private EmailTopMenu emailTopMenu;
	private InboxSubject inboxSubject;
	private InboxMessagePlainText inboxMessagePlainText;
	private HorizontalSplitPanel emailContent;
	private VerticalLayout emailContentLeftBar;
	private HorizontalSplitPanel emailContentRightBar;

	public EmailView() {
		setSizeFull();
		setMargin(false);
	}

	@PostConstruct
	void init() {

		emailTopMenu = new EmailTopMenu(service);
		emailTopMenu.setSizeFull();
		emailContent = new HorizontalSplitPanel();
		emailContent.setSizeFull();
		emailContent.setSplitPosition(15, Unit.PERCENTAGE);

		Button inbox = new Button(i18n.EMAIL_INBOX, ev -> {
			inboxSubject = new InboxSubject(this);
			if (EMAIL_SECURITY_LEVEL == ESECURITY.PLAIN_TEXT) {
				inboxMessagePlainText = new InboxMessagePlainText();
				emailContentRightBar.setFirstComponent(inboxSubject);
				emailContentRightBar.setSecondComponent(inboxMessagePlainText);
			}
		});

		Button sent = new Button(i18n.EMAIL_SENT, ev -> {
		});

		Button trash = new Button(i18n.EMAIL_TRASH, ev -> {
		});

		Button archive = new Button(i18n.EMAIL_ARCHIVE, ev -> {
		});

		Button lost = new Button(i18n.EMAIL_LOST, ev -> {
		});

		Button settings = new Button(i18n.EMAIL_SETTINGS, ev -> {
		});

		/*
		 * Left Navigation
		 */
		emailContentLeftBar = new VerticalLayout();
		emailContentLeftBar.setMargin(false);
		emailContentLeftBar.setSizeFull();
		emailContentLeftBar.addComponent(inbox);
		emailContentLeftBar.addComponent(sent);
		emailContentLeftBar.addComponent(trash);
		emailContentLeftBar.addComponent(archive);
		emailContentLeftBar.addComponent(lost);
		emailContentLeftBar.addComponent(settings);

		/*
		 * Right Content Side
		 */
		emailContentRightBar = new HorizontalSplitPanel();
		emailContentRightBar.setSizeFull();
		emailContentRightBar.setSplitPosition(40, Unit.PERCENTAGE);
		emailContent.setFirstComponent(emailContentLeftBar);
		emailContent.setSecondComponent(emailContentRightBar);

		addComponent(emailTopMenu);
		addComponent(emailContent);
		setExpandRatio(emailTopMenu, 0.2f);
		setExpandRatio(emailContent, 0.8f);

	}

	public EmailService getEmailService() {
		return service;
	}

	public HorizontalSplitPanel getEmailContentRightBar() {
		return emailContentRightBar;
	}

}
