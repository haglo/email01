package org.app.view.email.inbox;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.app.controler.EmailService;
import org.app.helper.I18n;
import org.app.model.entity.Pmail;
import org.app.view.email.EmailView;

import com.google.common.base.Strings;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.TextRenderer;

@SuppressWarnings("serial")
public class InboxSubject extends VerticalLayout implements View {

	private InboxMessagePlainText inboxMessage;
	private InboxMessageHtmlText inboxMessage2;
	private Grid<Pmail> grid;
	private ListDataProvider<Pmail> dataProvider;
	private Set<Pmail> selectedMails;
	private Pmail selectedMail;

	public InboxSubject(EmailView emailView) {
		this.inboxMessage = new InboxMessagePlainText();
		setMargin(new MarginInfo(false, true, false, false));
		setSizeFull();

		List<Pmail> list = emailView.getEmailService().getPmailDAO().findAll();
		dataProvider = DataProvider.ofCollection(list);

		grid = new Grid<Pmail>();
		grid.setSizeFull();
		grid.setWidth("100%");
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.setDataProvider(dataProvider);

		grid.addColumn(Pmail::getId).setRenderer(id -> id != null ? id : null, new TextRenderer()).setCaption("ID");
		grid.addColumn(Pmail::getPfrom).setRenderer(from -> from != null ? from : null, new TextRenderer())
				.setCaption("From");
		grid.addColumn(Pmail::getPsubject).setRenderer(subject -> subject != null ? subject : null, new TextRenderer())
				.setCaption("Subject");
		grid.addColumn(p -> convertTimestamp(p.getPreceiveDate())).setCaption("Receive Date").setId("receiveDate");
//		grid.addColumn(Pmail::getPreceiveDate)
//				.setRenderer(receivedate -> receivedate != null ? receivedate : null, new TextRenderer())
//				.setCaption("Receive Date");
//		grid.addColumn(Pmail::getPcontent)
//				.setRenderer(content -> content != null ? I18n.decodeFromBase64(content) : null, new TextRenderer())
//				.setCaption("Content");

//		Grid.Column receiveColumn = grid.getColumn("receiveDate");
//		receiveColumn.setRenderer(new DateRenderer("%1$tB %1$te, %1$tY", Locale.ENGLISH));

		grid.addSelectionListener(event -> {
			selectedMail = new Pmail();
			selectedMails = new HashSet<Pmail>();
			selectedMails = event.getAllSelectedItems();
			if (selectedMails.size() != 1) {
				inboxMessage.setMessageContent("");
				inboxMessage.init();
			} else {
				selectedMail = getSelectedMail(selectedMails);
				if (selectedMail != null) {
					inboxMessage.init();
					if (!Strings.isNullOrEmpty(selectedMail.getPfrom()))
						inboxMessage.getLblFrom().setValue("Von " + selectedMail.getPfrom());
					if (!Strings.isNullOrEmpty(selectedMail.getPsubject()))
						inboxMessage.getLblSubject().setValue("Betreff " + selectedMail.getPsubject());
					if (!Strings.isNullOrEmpty(selectedMail.getPrecipientTO()))
						inboxMessage.getLblTO().setValue("An " + selectedMail.getPrecipientTO());
					if (!Strings.isNullOrEmpty(selectedMail.getPrecipientCC()))
						inboxMessage.getLblCC().setValue("CC " + selectedMail.getPrecipientCC());
					if (!Strings.isNullOrEmpty(selectedMail.getPrecipientBCC()))
						inboxMessage.getLblBCC().setValue("BCC " + selectedMail.getPrecipientBCC());
					inboxMessage.getLblSendDate().setValue("Sendedatum " + selectedMail.getPsendDate());
					inboxMessage.setMessageContent(I18n.decodeFromBase64(selectedMail.getPcontent()));
					inboxMessage.refresh();
					
					inboxMessage2 = new InboxMessageHtmlText(I18n.decodeFromBase64(selectedMail.getPcontent()));
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

	private String convertTimestamp(String dateString) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm", Locale.GERMAN);
		ZonedDateTime dateTime = ZonedDateTime.parse(dateString, inputFormatter);
		String out = dateTime.format(outputFormatter);
		return out;
	}

}