package org.app.view.email.inbox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.app.controler.EmailService;
import org.app.helper.I18n;
import org.app.model.entity.Pmail;
import org.app.view.email.EmailView;
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
public class InboxSubject extends VerticalLayout implements View {

	private InboxMessage inboxMessage;
	private EmailView emailView;

	private Grid<Pmail> grid;
	private TextArea textArea;
	private ListDataProvider<Pmail> dataProvider;

	private Set<Pmail> selectedMails;
	private Pmail selectedMail;
	private EmailService service;

	public InboxSubject(EmailView emailView) {
		this.emailView = emailView;
		this.inboxMessage = new InboxMessage();
		setMargin(new MarginInfo(false, true, false, false));
		setSizeFull();

		List<Pmail> list = emailView.getEmailService().getPmailDAO().findAll();
		dataProvider = DataProvider.ofCollection(list);

		grid = new Grid<Pmail>();
		grid.setSizeFull();
		grid.setWidth("100%");
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.setDataProvider(dataProvider);

		grid.addColumn(Pmail::getPfrom).setRenderer(from -> from != null ? from : null, new TextRenderer())
				.setCaption("From");
		grid.addColumn(Pmail::getPsubject).setRenderer(subject -> subject != null ? subject : null, new TextRenderer())
				.setCaption("Subject");
		grid.addColumn(Pmail::getPcontent)
				.setRenderer(content -> content != null ? I18n.decodeFromBase64(content) : null, new TextRenderer())
				.setCaption("Content");

		grid.addSelectionListener(event -> {
			selectedMail = new Pmail();
			selectedMails = new HashSet<Pmail>();
			selectedMails = event.getAllSelectedItems();
			if (selectedMails.size() != 1) {
				inboxMessage.setMessageContent("");
			} else {
				selectedMail = getSelectedMail(selectedMails);
				if (selectedMail != null) {
					inboxMessage.addTextToHeader("An " + "Maier");
					inboxMessage.setMessageContent(I18n.decodeFromBase64(selectedMail.getPcontent()));
				}
			}
			emailView.getEmailContentRightBar().setSecondComponent(inboxMessage);
		});

		addComponent(grid);
	}

	private Pmail getSelectedMail(Set<Pmail> selectedMails) {
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

}