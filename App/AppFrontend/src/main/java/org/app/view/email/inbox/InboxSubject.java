package org.app.view.email.inbox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.EmailService;
import org.app.helper.I18n;
import org.app.model.entity.Pmail;
import org.app.view.email.EmailView;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.TextRenderer;

@SuppressWarnings("serial")
@CDIView(I18n.INBOX_SUBJECT)
public class InboxSubject extends VerticalLayout implements View {

	@Inject
	InboxMessage inboxMessage;

	@Inject
	EmailView emailView;

	private Grid<Pmail> grid;
	private TextArea textArea;
	private ListDataProvider<Pmail> dataProvider;

	private Set<Pmail> selectedMails;
	private Pmail selectedMail;
	private EmailService service;

	public InboxSubject(EmailService service) {
		this.service = service;
		setMargin(new MarginInfo(false, true, false, false));
		setSizeFull();

		List<Pmail> list = service.getPmailDAO().findAll();
		dataProvider = DataProvider.ofCollection(list);

		grid = new Grid<Pmail>();
		grid.setSizeFull();
		grid.setWidth("100%");
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.setDataProvider(dataProvider);

//		grid.addColumn(Pmail::getPfrom)
//				.setRenderer(from -> from != null ? I18n.decodeFromBase64(from) : null, new TextRenderer())
//				.setCaption("From");
//		grid.addColumn(Pmail::getPsubject)
//				.setRenderer(subject -> subject != null ? I18n.decodeFromBase64(subject) : null, new TextRenderer())
//				.setCaption("Subject");
//		grid.addColumn(Pmail::getPcontent)
//				.setRenderer(content -> content != null ? I18n.decodeFromBase64(content) : null, new TextRenderer())
//				.setCaption("Content");

		grid.addColumn(Pmail::getPfrom)
				.setRenderer(from -> from != null ? from : null, new TextRenderer())
				.setCaption("From");
		grid.addColumn(Pmail::getPsubject)
				.setRenderer(subject -> subject != null ? subject : null, new TextRenderer())
				.setCaption("Subject");
		grid.addColumn(Pmail::getPcontent)
				.setRenderer(content -> content != null ? I18n.decodeFromBase64(content) : null, new TextRenderer())
				.setCaption("Content");
		
//		grid.addSelectionListener(event -> {
//			selectedMail = new Pmail();
//			selectedMails = new HashSet<Pmail>();
//			selectedMails = event.getAllSelectedItems();
//			if (selectedMails.size() != 1) {
//				inboxMessage.setVisible(false);
//			} else {
//				selectedMail = getTheSelectedMail(selectedMails);
//				if (selectedMail != null) {
//					InboxMessage ib = new InboxMessage();
//					inboxMessage.setContent(I18n.decodeFromBase64(selectedMail.getPcontent()));
//					ib.setContent("Hallo Du");
//					ib.setVisible(true);
//					emailView.getEmailContentRightBar().setSecondComponent(ib);
//					Notification.show("Hallo1 " + I18n.decodeFromBase64("Hallo1 " + selectedMail.getPcontent()));
//				}
//			}
//		});

		grid.addSelectionListener(event -> {
		selectedMail = new Pmail();
		selectedMails = new HashSet<Pmail>();
		selectedMails = event.getAllSelectedItems();
		if (selectedMails.size() != 1) {
			inboxMessage.setVisible(false);
		} else {
			selectedMail = getTheSelectedMail(selectedMails);
			if (selectedMail != null) {
				inboxMessage.setContent("Hello 2");
				Notification.show("Hallo1 " + I18n.decodeFromBase64("Hallo1 " + selectedMail.getPcontent()));
			}
		}
	});

		addComponent(grid);
	}

	@PostConstruct
	void init() {
		grid.addSelectionListener(event -> {
			selectedMail = new Pmail();
			selectedMails = new HashSet<Pmail>();
			selectedMails = event.getAllSelectedItems();
			if (selectedMails.size() != 1) {
				inboxMessage.setVisible(false);
			} else { 
				selectedMail = getTheSelectedMail(selectedMails);
				if (selectedMail != null) {
//					inboxMessage.setContent(I18n.decodeFromBase64(selectedMail.getPcontent()));
					inboxMessage.setContent("Hallo Du");
					inboxMessage.setVisible(true);
					emailView.getEmailContentRightBar().setSecondComponent(inboxMessage);
//					Notification.show(I18n.decodeFromBase64(selectedMail.getPcontent()));
					Notification.show("Hallo2 " + I18n.decodeFromBase64(selectedMail.getPcontent()));
				}
			}
		});
	}

	private Pmail getTheSelectedMail(Set<Pmail> selectedMails) {
		selectedMail = new Pmail();
		if (selectedMails.size() > 1) {
			Notification.show("Only one Item");
			return null;
		}
		if (selectedMails.size() < 1) {
			Notification.show("Exact one Item");
			return null;
		}
		if (selectedMails.size() == 1) {
			for (Pmail pmail : selectedMails) {
				selectedMail = pmail;
			}
		}
		return selectedMail;

	}

	public void refreshGrid() {
		List<Pmail> list = this.service.getPmailDAO().findAll();
		grid.setItems(list);
	}

}