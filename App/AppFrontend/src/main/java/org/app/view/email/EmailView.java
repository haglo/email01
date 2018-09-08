package org.app.view.email;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.EmailService;
import org.app.helper.I18n;
import org.app.view.email.inbox.InboxMessage;
import org.app.view.email.inbox.InboxSubject;
import org.app.view.email.settings.SettingsView;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(I18n.EMAIL_VIEW)
public class EmailView extends VerticalLayout implements View {

	@Inject
	InboxMessage inboxView;

	@Inject
	EmailService service;

	private I18n i18n;
	private EmailTopMenu emailTopMenu;
	private HorizontalSplitPanel emailContent;
	private VerticalLayout emailContentLeftBar;
	private HorizontalSplitPanel emailContentRightBar;

	private Button inboxButton;
	private Button outboxButton;
	private Button trashButton;
	private Button archiveButton;
	private Button lostButton;
	private Button settingsButton;

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

		/*
		 * Left Navigation
		 */
		emailContentLeftBar = new VerticalLayout();
		emailContentLeftBar.setMargin(false);
		emailContentLeftBar.setSizeFull();
		emailContentLeftBar.addComponent(showInboxView());
		emailContentLeftBar.addComponent(showOutboxView());
		emailContentLeftBar.addComponent(showTrashView());
		emailContentLeftBar.addComponent(showArchiveView());
		emailContentLeftBar.addComponent(showLostView());
		emailContentLeftBar.addComponent(showSettingsView());

		/*
		 * Right Content Side
		 */
		emailContentRightBar = new HorizontalSplitPanel();
		emailContentRightBar.setSizeFull();

		emailContent.setFirstComponent(emailContentLeftBar);
		emailContent.setSecondComponent(emailContentRightBar);

		addComponent(emailTopMenu);
		addComponent(emailContent);
		setExpandRatio(emailTopMenu, 0.2f);
		setExpandRatio(emailContent, 0.8f);

	}

	private Button showInboxView() {
		inboxButton = new Button(i18n.EMAIL_INBOX, new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				emailContentRightBar.setFirstComponent(new InboxSubject(service));
				emailContentRightBar.setSecondComponent(new InboxMessage());
			}
		});
		return inboxButton;
	}

	private Button showOutboxView() {
		outboxButton = new Button(i18n.EMAIL_SENT, new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				emailContentRightBar.removeAllComponents();
				emailContentRightBar.addComponent(inboxView);
			}
		});
		return outboxButton;
	}

	private Button showTrashView() {
		trashButton = new Button(i18n.EMAIL_TRASH, new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				emailContentRightBar.removeAllComponents();
				emailContentRightBar.addComponent(inboxView);
			}
		});
		return trashButton;
	}

	private Button showArchiveView() {
		archiveButton = new Button(i18n.EMAIL_ARCHIVE, new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				emailContentRightBar.removeAllComponents();
				emailContentRightBar.addComponent(inboxView);
			}
		});
		return archiveButton;
	}

	private Button showLostView() {
		lostButton = new Button(i18n.EMAIL_LOST, new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				emailContentRightBar.removeAllComponents();
				emailContentRightBar.addComponent(inboxView);
			}
		});
		return lostButton;
	}

	private Button showSettingsView() {
		settingsButton = new Button(i18n.EMAIL_SETTINGS, new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().addWindow(new SettingsView());
			}
		});
		return settingsButton;
	}

	public HorizontalSplitPanel getEmailContentRightBar() {
		return emailContentRightBar;
	}
}
