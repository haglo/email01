package org.app.controler.email;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.app.helper.I18n;

public class EmailParser {
	private String result;
	private String emailContent;

	/**
	 * Initialized with an Email-Message
	 * 
	 * @param msg
	 */
	public EmailParser(Message msg) {
		init();
		try {
			parseCID((MimeMessage) msg);
			setEmailContent(result);

		} catch (Exception e) {
			System.out.println(">>> 1 Exception in main ");
			setEmailContent(I18n.EMAIL_FAILURE);
		}

	}

	private void init() {
		result = "";
		setEmailContent("");
	}

	private void parseCID(Part p) throws IOException, MessagingException {
		Multipart multipart = (Multipart) p.getContent();
		for (int n = 0; n < multipart.getCount(); n++) {
			BodyPart bodyPart = multipart.getBodyPart(n);
			MimeBodyPart mimeBodyPart = (MimeBodyPart) multipart.getBodyPart(n);
			if (bodyPart.isMimeType("text/html")) {
				String newText = bodyPart.toString().replaceAll("\"cid:", "\"file:///C:/dev/upload/cid/");
				
			}
		}
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String content) {
		this.emailContent = content;
	}

}