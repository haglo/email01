package org.app.view.email.inbox;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.app.controler.EmailService;
import org.app.controler.email.Const;
import org.app.controler.email.Imap;
import org.app.controler.email.Const.ESECURITY;
import org.app.controler.email.imap.ExtractContent;
import org.app.helper.I18n;
import org.app.model.entity.Pmail;
import org.app.view.email.EmailView;

import com.google.common.base.Strings;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.TextRenderer;

@SuppressWarnings("serial")
public class CallMail extends VerticalLayout implements View, Const {

	private HtmlTextMail inboxMessage;
	private Grid<Pmail> grid;
	private ListDataProvider<Pmail> dataProvider;
	private Set<Pmail> selectedMails;
	private Pmail selectedMail;

	public CallMail(EmailView emailView) {
		inboxMessage = new HtmlTextMail();
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

					if (selectedMail.getPattachmentNumber() > 0) {
						inboxMessage.getLblAttachmentNumber()
								.setValue("Number of Attachments " + selectedMail.getPattachmentNumber());
					}

					if (!Strings.isNullOrEmpty(selectedMail.getPattachmentFileName()))
						inboxMessage.getLblAttachmentFileNames()
								.setValue("Filename " + selectedMail.getPattachmentFileName());

					if (!Strings.isNullOrEmpty(selectedMail.getPattachmentFilePath()))
						inboxMessage.getLblAttachmentFilePath()
								.setValue("Filename " + selectedMail.getPattachmentFilePath());

					if (!Strings.isNullOrEmpty(selectedMail.getPattachmentFileFullName()))
						inboxMessage.getLblAttachmentFullFileName()
								.setValue("Filename " + selectedMail.getPattachmentFileFullName());

					if (!Strings.isNullOrEmpty(selectedMail.getPattachmentFileFullName())) {
						for (Button tmp : createAttachment(selectedMail.getPattachmentFileFullName())) {
							inboxMessage.getAttachmentPanel().addComponent(tmp);
						}
					}

					String tmp = I18n.decodeFromBase64(selectedMail.getPcontent());
					inboxMessage.setMessageContent(tmp);

					byte[] byteDecodedEmail = Base64.getMimeDecoder().decode(selectedMail.getPmessage());
					String decodedEmail = new String(byteDecodedEmail);
					inboxMessage.setRawMail(decodedEmail);

					inboxMessage.refresh();
				}
			}
			// Important
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

	private List<Button> createAttachment(String in) {
		Button downloadButton;
		List<Button> result = new ArrayList<Button>();

		String tmp1 = "\\";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ATTACHMENT_DELIMITER.length(); i++) {
			sb.append(tmp1);
			sb.append(ATTACHMENT_DELIMITER.charAt(i));
		}
		String delimiter = new String(sb);

		List<String> attachments = new ArrayList<String>(Arrays.asList(in.split(delimiter)));

		for (String str : attachments) {
			File file = new File(str);
			downloadButton = new Button(file.getName(), e -> {
//				UI.getCurrent().getNavigator().navigateTo(I18n.EMAIL_VIEW);
			});
			downloadButton.setIcon(VaadinIcons.CLOUD_DOWNLOAD);
			downloadButton.addStyleName("icon-align-top");

			Resource res = new FileResource(file);
			FileDownloader fd = new FileDownloader(res);
			fd.extend(downloadButton);
			result.add(downloadButton);

		}
		return result;
	}

}