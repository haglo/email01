package org.app.view.email.inbox;

import java.io.ByteArrayInputStream;
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
import org.app.controler.email.Const.ESECURITY;
import org.app.controler.email.imap.ExtractContent;
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
public class InboxSubject extends VerticalLayout implements View, Const {

	private InboxMessagePlainText inboxMessage;
	private Grid<Pmail> grid;
	private ListDataProvider<Pmail> dataProvider;
	private Set<Pmail> selectedMails;
	private Pmail selectedMail;

	public InboxSubject(EmailView emailView) {
//		if (EMAIL_SECURITY_LEVEL == ESECURITY.PLAIN_TEXT) {
//			this.inboxMessage = new InboxMessagePlainText();
//		}
//		if (EMAIL_SECURITY_LEVEL == ESECURITY.HTML_TEXT) {
//			this.inboxMessage = new InboxMessageHtmlText();
//		}
		inboxMessage = new InboxMessagePlainText();
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
					if (selectedMail.getPnumberOfAttachments()>0)
						inboxMessage.getLblAttachmentNumber().setValue("Number of Attachments " + selectedMail.getPnumberOfAttachments());
					if (!Strings.isNullOrEmpty(selectedMail.getPfilenamesOfAttachments()))
						inboxMessage.getLblAttachmentFileNames().setValue("Filename " + selectedMail.getPfilenamesOfAttachments());

					inboxMessage.setMessageContent(I18n.decodeFromBase64(selectedMail.getPcontent()));
					
					byte[] byteDecodedEmail = Base64.getMimeDecoder().decode(selectedMail.getPmessage());
					String decodedEmail = new String(byteDecodedEmail);
					inboxMessage.setRawMail(decodedEmail);

					
					
//					ExtractContent extractContent = new ExtractContent(createMessage(decodedEmail));
					
//					inboxMessage.setRawMail(Base64.getMimeDecoder().decode(selectedMail.getPmessage()));

//					inboxMessage.getLblSendDate().setValue("Sendedatum " + selectedMail.getPsendDate());
//					String tmp = getEmailContent(selectedMail.getPmessage());
//					inboxMessage.setMessageContent(tmp);
				
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

//	private String getEmailContent(String email) {
//		String result = "";
//		try {
//			Properties props = System.getProperties();
//			props.put("mail.host", "smtp.dummydomain.com");
//			props.put("mail.transport.protocol", "smtp");
//			Session mailSession = Session.getDefaultInstance(props, null);
//			Message message = new MimeMessage(mailSession);
//			message.setText(email);
//			ExtractContent extractContent = new ExtractContent(message);
//			result = extractContent.getEmailContent();
//			result = parseCID(result);
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return result;
//	}

	private Message createMessage(String in) {
		//replace end of line
		String STRING1 = "\r\n";	//Linebreak Windows
		String STRING2 = "\n";		//Linebreak Linux
		String STRING3 = "<br>";
		String tmp1 = in.replaceAll(STRING1, STRING2);
		String tmp2 = tmp1.replaceAll(STRING2, STRING3);

		InputStream source = null;
		MimeMessage message = null;
		Properties props = System.getProperties();
		props.put("mail.host", "smtp.dummydomain.com");
		props.put("mail.transport.protocol", "smtp");
		Session mailSession = Session.getDefaultInstance(props, null);
		try {
			source = new ByteArrayInputStream(tmp2.getBytes("UTF_8"));
			message = new MimeMessage(mailSession, source);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
	
	@SuppressWarnings("static-access")
	private static String parseCID(String in) {
		String OLD_STRING = "\"cid:";
		String NEW_STRING = "\"" + PATH_INLINE_IMAGES;
		Pattern pattern = Pattern.compile(OLD_STRING);
		Matcher matcher = pattern.matcher(in);
		in = matcher.replaceAll(matcher.quoteReplacement(NEW_STRING));
		return in;

	}

}