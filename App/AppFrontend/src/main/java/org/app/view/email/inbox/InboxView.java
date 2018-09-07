package org.app.view.email.inbox;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.EmailService;
import org.app.helper.I18n;
import org.app.view.email.inbox.parts.InboxMessage;
import org.app.view.email.inbox.parts.InboxSubject;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(I18n.INBOX_VIEW)
public class InboxView extends VerticalLayout implements View {

	@Inject
	EmailService service;

	private HorizontalSplitPanel mainContent;
	private InboxSubject inboxOverView;
	private InboxMessage inboxDetailView;

	public InboxView() {
		setMargin(false);
		setSizeFull();

	}

	@PostConstruct
	void init() {
		mainContent = new HorizontalSplitPanel();
		mainContent.setSplitPosition(50, Unit.PERCENTAGE);
		mainContent.setSizeFull();

		inboxOverView = new InboxSubject(service);
		inboxDetailView = new InboxMessage("");

		mainContent.setFirstComponent(inboxOverView);
		mainContent.setSecondComponent(inboxDetailView);
		addComponent(mainContent);
	}

}