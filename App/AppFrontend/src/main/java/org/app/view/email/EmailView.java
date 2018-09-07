package org.app.view.email;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.EmailService;
import org.app.helper.I18n;
import org.app.view.email.inbox.InboxView;
import org.app.view.email.settings.SettingsView;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(I18n.EMAIL_VIEW)
public class EmailView extends VerticalLayout implements View {

	@Inject
	InboxView inboxView;

	@Inject
	EmailService service;
	
	private I18n i18n;
	private VerticalLayout mainView;
	private EmailTopMenu emailTopMenu;
	private HorizontalLayout emailContentView;
	private VerticalLayout emailContentLeftBar;
	private VerticalLayout emailContentRightBar;
	private Button inboxButton;
	private Button outboxButton;
	private Button trashButton;
	private Button archiveButton;
	private Button lostButton;
	private Button settingsButton;

	public EmailView() {
		i18n = new I18n();
		setSizeFull();
		setMargin(new MarginInfo(true, true, true, true));
	}

	@PostConstruct
	void init() {
		mainView = new VerticalLayout();
		mainView.setMargin(false);
		emailTopMenu = new EmailTopMenu(service);
		emailContentView = new HorizontalLayout();
		emailContentView.setMargin(false);
		emailContentView.setSizeFull();

		emailContentLeftBar = new VerticalLayout();
		emailContentLeftBar.setMargin(false);
		emailContentRightBar = new VerticalLayout();
		emailContentLeftBar.setMargin(false);
		emailContentRightBar.setSizeFull();

		emailContentLeftBar.addComponent(showInboxView());
		emailContentLeftBar.addComponent(showOutboxView());
		emailContentLeftBar.addComponent(showTrashView());
		emailContentLeftBar.addComponent(showArchiveView());
		emailContentLeftBar.addComponent(showLostView());
		emailContentLeftBar.addComponent(showSettingsView());
		emailContentRightBar.addComponent(inboxView);

		emailContentView.addComponent(emailContentLeftBar);
		emailContentView.addComponent(emailContentRightBar);
		emailContentView.setSizeFull();

		emailContentView.setExpandRatio(emailContentLeftBar, 0.2f);
		emailContentView.setExpandRatio(emailContentRightBar, 0.8f);

		mainView.addComponent(emailTopMenu);
		mainView.addComponent(emailContentView);
		addComponent(mainView);
	}

	private Button showInboxView() {
		inboxButton = new Button(i18n.EMAIL_INBOX, new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				emailContentRightBar.removeAllComponents();
				emailContentRightBar.addComponent(inboxView);
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




}
