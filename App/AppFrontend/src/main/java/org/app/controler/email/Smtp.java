package org.app.controler.email;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.app.controler.email.smtp.EmailToSend;

public class Smtp {

	private MimeMultipart multipart;
	private MimeBodyPart messageBodyPart;

	public void smtp() {

	}

	public void send(EmailToSend pemail) {
		multipart = new MimeMultipart();

		final String from = "h.g.gloeckler@gmail.com";
		final String username = "h.g.gloeckler@gmail.com";// change accordingly
		final String password = "1234:Atgfd";// change accordingly

		// Assuming you are sending email through relay.jangosmtp.net
		String host = "smtp.gmail.com";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		// Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			if (pemail.getSendTo() != null && !pemail.getSendTo().trim().isEmpty())
				message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(pemail.getSendTo(), true));
			if (pemail.getSendToCC() != null && !pemail.getSendToCC().trim().isEmpty())
				message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(pemail.getSendToCC(), true));
			if (pemail.getSendToBC() != null && !pemail.getSendToBC().trim().isEmpty())
				message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(pemail.getSendToBC(), true));
			if (pemail.getSubject() != null && !pemail.getSubject().trim().isEmpty())
				message.setSubject(pemail.getSubject());

			// Plain Text
			if (pemail.getText() != null && !pemail.getText().trim().isEmpty()) {
				messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText(pemail.getText());
				multipart.addBodyPart(messageBodyPart);
			}

			// HTML Text
			if (pemail.getTextHTML() != null && !pemail.getTextHTML().trim().isEmpty()) {
				messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(pemail.getTextHTML(), "text/html");
				multipart.addBodyPart(messageBodyPart);
			}

			// With Attachment
			if (pemail.isWithAttachment()) {
				for (FileDataSource tmp : pemail.getAttachments()) {
					messageBodyPart = new MimeBodyPart();
					messageBodyPart.setDataHandler(new DataHandler(tmp));
					messageBodyPart.setFileName(tmp.getName());
					messageBodyPart.setHeader("Content-ID", "<image>");
					multipart.addBodyPart(messageBodyPart);
				}
			}

			message.setContent(multipart);

			// Send message
			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
