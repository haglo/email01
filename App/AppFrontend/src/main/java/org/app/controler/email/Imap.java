package org.app.controler.email;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.app.controler.EmailService;
import org.app.controler.email.imap.ExtractContent;
import org.app.controler.email.imap.ExtractHeader;
import org.app.helper.I18n;
import org.app.model.entity.Pmail;

public class Imap {

	private Pmail pmail;
	private Store store;
	private Folder emailFolder;
	private ExtractHeader extractHeader;
	private ExtractContent extractContent;

	public void readFromImap(EmailService service) {
		String tmp = "";

		try {
			Properties properties = new Properties();

			String imapHost = "imap.gmx.net";
			String username = "benjamin_strobel@gmx.de";
			String password = "123atgfd";
			Integer port = 993;
			boolean isSSL = true;

			properties.put("mail.imap.user", username);
			properties.put("mail.imap.host", imapHost);
			properties.put("mail.imap.port", port);
			properties.put("mail.imap.ssl.enable", isSSL);
			properties.put("mail.imap.auth", true);

			Session emailSession = Session.getDefaultInstance(properties);

			store = isSSL ? emailSession.getStore("imaps") : emailSession.getStore("imap");
			store.connect(imapHost, username, password);
			if (store.isConnected()) {
				System.out.println("Connect to Imap: true");
			}

			emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);
			UIDFolder uidEmailFolder = (UIDFolder) emailFolder;

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();

			for (int i = 0; i < messages.length; i++) {
				System.out.println("Message: " + i);
				Message message = messages[i];
				extractHeader = new ExtractHeader(message);
				extractContent = new ExtractContent(message);

				pmail = new Pmail();
				pmail.setPimapUid(uidEmailFolder.getUID(message));
				System.out.println("Message Count: " + i + " -- " + uidEmailFolder.getUID(message));

				tmp = "";
				for (int n = 0; n < extractHeader.getFrom().length; n++) {
					if (n == 0) {
						tmp = tmp + extractHeader.getFrom()[n];
					} else {
						tmp = tmp + ", " + extractHeader.getFrom()[n];
					}
				}
				pmail.setPfrom(tmp);

				pmail.setPsubject(extractHeader.getSubject());

				tmp = "";
				for (int n = 0; n < extractHeader.getTo().length; n++) {
					if (n == 0) {
						tmp = tmp + extractHeader.getTo()[n];
					} else {
						tmp = tmp + ", " + extractHeader.getTo()[n];
					}
				}
				pmail.setPrecipientTO(tmp);

				tmp = "";
				for (int n = 0; n < extractHeader.getCc().length; n++) {
					if (n == 0) {
						tmp = tmp + extractHeader.getCc()[n];
					} else {
						tmp = tmp + ", " + extractHeader.getCc()[n];
					}
				}
				pmail.setPrecipientCC(tmp);

				tmp = "";
				for (int n = 0; n < extractHeader.getBcc().length; n++) {
					if (n == 0) {
						tmp = tmp + extractHeader.getBcc()[n];
					} else {
						tmp = tmp + ", " + extractHeader.getBcc()[n];
					}
				}
				pmail.setPrecipientBCC(tmp);

				pmail.setPsendDate(extractHeader.getSendDate());
				pmail.setPreceiveDate(extractHeader.getReceiveDate());

				pmail.setPnumberOfAttachments(extractContent.getNumberOfAttachments());

				tmp = "";
				for (String s : extractContent.getAttachedFileNames()) {
					if (tmp.isEmpty()) {
						tmp = tmp + s;
					} else {
						tmp = tmp + ", " + s;
					}
				}
				pmail.setPfilenamesOfAttachments(tmp);

				pmail.setPcontent(I18n.encodeToBase64(extractContent.getEmailContent()));

				pmail.setPmessage(convertMessageToString((MimeMessage) message));

				service.getPmailDAO().create(pmail);

			}
			// close the store and folder objects
			emailFolder.close(false);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String convertMessageToString(MimeMessage msg) {
		String result = "";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			msg.writeTo(out);
			result = Base64.getMimeEncoder().encodeToString(out.toByteArray());
		} catch (IOException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}