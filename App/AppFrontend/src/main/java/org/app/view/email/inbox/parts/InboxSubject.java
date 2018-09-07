package org.app.view.email.inbox.parts;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.app.controler.EmailService;
import org.app.helper.I18n;
import org.app.model.entity.Pmail;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.TextRenderer;

@SuppressWarnings("serial")
public class InboxSubject extends VerticalLayout {

	private InboxMessage inboxDetailView;

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

		grid.addColumn(Pmail::getPfrom)
				.setRenderer(from -> from != null ? I18n.decodeFromBase64(from) : null, new TextRenderer())
				.setCaption("From");
		grid.addColumn(Pmail::getPsubject)
				.setRenderer(subject -> subject != null ? I18n.decodeFromBase64(subject) : null, new TextRenderer())
				.setCaption("Subject");
		grid.addColumn(Pmail::getPcontent)
				.setRenderer(content -> content != null ? I18n.decodeFromBase64(content) : null, new TextRenderer())
				.setCaption("Inhalt");

		grid.addSelectionListener(event -> {
			selectedMail = new Pmail();
			selectedMails = new HashSet<Pmail>();
			selectedMails = event.getAllSelectedItems();
			if (selectedMails.size() != 1) {
				inboxDetailView = new InboxMessage("");
				inboxDetailView.setVisible(false);
			} else {
				selectedMail = getTheSelectedMail(selectedMails);
				if (selectedMail != null) {
					inboxDetailView = new InboxMessage(I18n.decodeFromBase64(selectedMail.getPcontent()));
					inboxDetailView.setContent(I18n.decodeFromBase64(selectedMail.getPcontent()));
					inboxDetailView.setVisible(true);

					Notification.show(I18n.decodeFromBase64(selectedMail.getPcontent()));
				}
			}
		});

		addComponent(grid);
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