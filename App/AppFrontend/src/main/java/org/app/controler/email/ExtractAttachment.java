package org.app.controler.email;

import java.io.BufferedReader;
import org.apache.commons.lang3.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.app.helper.I18n;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class ExtractAttachment {

	private List<File> attachments;
	private int numberOfAttachements = 0;

	public ExtractAttachment(Message msg) {
		try {
			extractNumberOfAttachments(msg);
			extractAttachments((MimeMessage) msg);
		} catch (Exception e) {
			this.attachments = null;
		}
	}
	
	private void extractNumberOfAttachments(Message msg) throws IOException, MessagingException {

		int nAttachments = 0;
		Multipart mp = (Multipart) msg.getContent();

		for (int i = 0, n = mp.getCount(); i < n; i++) {
			Part part = mp.getBodyPart(i);

			String disposition = part.getDisposition();

			if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT) || (disposition.equals(Part.INLINE)))))
				nAttachments++;
		}
		setNumberOfAttachements(nAttachments);
	}


	private void extractAttachments(MimeMessage msg) throws IOException, MessagingException {
		List<File> attachments = new ArrayList<File>();

		Multipart multipart = (Multipart) msg.getContent();

		for (int i = 0; i < multipart.getCount(); i++) {
			BodyPart bodyPart = multipart.getBodyPart(i);
			if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())
					&& StringUtils.isBlank(bodyPart.getFileName())) {
				continue; // dealing with attachments only
			}
			InputStream is = bodyPart.getInputStream();
			File f = new File("/tmp/" + bodyPart.getFileName());
			FileOutputStream fos = new FileOutputStream(f);
			byte[] buf = new byte[4096];
			int bytesRead;
			while ((bytesRead = is.read(buf)) != -1) {
				fos.write(buf, 0, bytesRead);
			}
			fos.close();
			attachments.add(f);
		}
		setAttachments(attachments);
	}

	private static void attachments(final BodyPart body, final BiConsumer<String, InputStream> consumer)
			throws MessagingException, IOException {
		final Multipart content;
		try {
			content = (Multipart) body.getContent();
			for (int i = 0; i < content.getCount(); i++) {
				attachments(content.getBodyPart(i), consumer);
			}
			return;
		} catch (final ClassCastException cce) {
		}
		if (!Part.ATTACHMENT.equalsIgnoreCase(body.getDisposition())) {
			return;
		}
		final String name = body.getFileName();
		if (name == null || name.trim().isEmpty()) {
			return;
		}
		try (final InputStream stream = body.getInputStream()) {
			consumer.accept(name, stream);
		}
	}

	public static void attachments(final Message message, final BiConsumer<String, InputStream> consumer)
			throws IOException, MessagingException {
		final Multipart content;
		try {
			content = (Multipart) message.getContent();
		} catch (final ClassCastException cce) {
			return;
		}
		for (int i = 0; i < content.getCount(); i++) {
			attachments(content.getBodyPart(i), consumer);
		}
	}


	public List<File> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<File> attachments) {
		this.attachments = attachments;
	}

	public int getNumberOfAttachements() {
		return numberOfAttachements;
	}

	public void setNumberOfAttachements(int numberOfAttachements) {
		this.numberOfAttachements = numberOfAttachements;
	}

}