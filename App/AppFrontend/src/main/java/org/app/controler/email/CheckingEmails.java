package org.app.controler.email;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import org.app.controler.EmailService;
import org.app.controler.email.MailServer.Mailprovider;
import org.app.model.entity.Pmail;
import org.app.helper.I18n;

public class CheckingEmails {

	private Pmail pmail;
	private Store store;
	private MailServer mailServer;

	public void readEmails(EmailService service) {

		mailServer = new MailServer();
//		mailServer.init(Mailprovider.PRIVATE);
		mailServer.init();

		try {

			Session emailSession = Session.getDefaultInstance(mailServer.getProperties());
			Store store = emailSession.getStore("imap");

			store.connect(mailServer.getImapHost(), mailServer.getUsername(), mailServer.getPassword());
			if (store.isConnected()) {
				System.out.println("Connect to Imap: true");
			}

			// create the folder object and open it
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();
			System.out.println("messages.length---" + messages.length);

			for (int i = 0, n = messages.length; i < n; i++) {
				pmail = new Pmail();
				Message message = messages[i];
				pmail.setPsubject(I18n.encodeToBase64(message.getSubject()));
				pmail.setPfrom(I18n.encodeToBase64(message.getFrom()[0].toString()));
				pmail.setPcontent(I18n.encodeToBase64(message.getContent().toString()));
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

//	public TreeSet<String> getEmailAddesses() throws MessagingException {
//		// open the inbox folder
//		Folder inbox = getEmailStore().getFolder("INBOX");
//		inbox.open(Folder.READ_ONLY);
//
//		// get a list of javamail messages as an array of messages
//		Message[] messages = inbox.getMessages();
//
//		TreeSet<String> treeSet = new TreeSet<String>();
//
//		for (int i = 0; i < messages.length; i++) {
//			String from = getFrom(messages[i]);
//			if (from != null) {
//				from = removeQuotes(from);
//				treeSet.add(from);
//			}
//		}
//		return treeSet;
//	}

	public Store getEmailStore() throws MessagingException {
		try {
			Properties properties = new Properties();

			String imapHost = "imap.gmail.com";
			String username = "h.g.gloeckler@gmail.com";
			String password = "1234:Atgfd";

			properties.put("mail.imap.user", username);
			properties.put("mail.imap.host", imapHost);
			properties.put("mail.imap.port", 993);
			properties.put("mail.imap.ssl.enable", true);

			Session emailSession = Session.getDefaultInstance(properties);
			store = emailSession.getStore("imaps");
			store.connect(imapHost, username, password);

//			store.getFolder("[Gmail]/Sent Mail");
//			store.getFolder("[Gmail]/Drafts");

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			store.close();
		} catch (MessagingException e) {
			e.printStackTrace();
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
			store.close();
		}

		return store;
	}

	public void check() {

		try {
			Properties properties = new Properties();

			String imapHost = "imap.gmail.com";
			String username = "h.g.gloeckler@gmail.com";
			String password = "1234:Atgfd";

			properties.put("mail.imap.user", username);
			properties.put("mail.imap.host", imapHost);
			properties.put("mail.imap.port", 993);
			properties.put("mail.imap.ssl.enable", true);

			Session emailSession = Session.getDefaultInstance(properties);
			Store store = emailSession.getStore("imaps");

			store.connect(imapHost, username, password);
			if (store.isConnected()) {
				System.out.println("Connect to Imap: true");
			}

			// create the folder object and open it
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();
			System.out.println("messages.length---" + messages.length);

			for (int i = 0, n = messages.length; i < n; i++) {
				Message message = messages[i];
				System.out.println("---------------------------------");
				System.out.println("Email Number " + (i + 1));
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("Text: " + message.getContent().toString());

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

	public void check1(EmailService service) {

		try {
			Properties properties = new Properties();

			String imapHost = "195.201.215.12";
			String username = "hans-georg.gloeckler@gimtex.de";
			String password = "1234:Atgfd";

			properties.put("mail.imap.user", username);
			properties.put("mail.imap.host", imapHost);
			properties.put("mail.imap.port", 143);
			properties.put("mail.imap.ssl.enable", false);

			Session emailSession = Session.getDefaultInstance(properties);
			Store store = emailSession.getStore("imaps");

			store.connect(imapHost, username, password);
			if (store.isConnected()) {
				System.out.println("Connect to Imap: true");
			}

			// create the folder object and open it
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();
			System.out.println("messages.length---" + messages.length);

			for (int i = 0, n = messages.length; i < n; i++) {
				Message message = messages[i];
				System.out.println("---------------------------------");
				System.out.println("-------New Email-----------------");
				System.out.println("---------------------------------");
				System.out.println("Email Number " + (i + 1));
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("Text: " + message.getContent().toString());

				System.out.println("---------------------------------");
				pmail = new Pmail();
				pmail.setPsubject(I18n.encodeToBase64(message.getSubject()));
				System.out.println("Subject von Pmail: " + pmail.getPsubject());

				pmail.setPfrom(I18n.encodeToBase64(message.getFrom()[0].toString()));
				pmail.setPcontent(I18n.encodeToBase64(message.getContent().toString()));
				service.getPmailDAO().create(pmail);

			}

//			for (int i = 0, n = messages.length; i < n; i++) {
//				pmail = new Pmail();
//				Message message = messages[i];
//				pmail.setPsubject(I18n.encodeToBase64(message.getSubject()));
//				pmail.setPfrom(I18n.encodeToBase64(message.getFrom()[0].toString()));
//				pmail.setPcontent(I18n.encodeToBase64(message.getContent().toString()));
//				service.getPmailDAO().create(pmail);
//			}
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
//	private static String getFrom(Message javaMailMessage) throws MessagingException {
//		String from = "";
//		Address a[] = javaMailMessage.getFrom();
//		if (a == null)
//			return null;
//		for (int i = 0; i < a.length; i++) {
//			Address address = a[i];
//			from = from + address.toString();
//		}
//
//		return from;
//	}
//
//	private static String removeQuotes(String stringToModify) {
//		int indexOfFind = stringToModify.indexOf(stringToModify);
//		if (indexOfFind < 0)
//			return stringToModify;
//
//		StringBuffer oldStringBuffer = new StringBuffer(stringToModify);
//		StringBuffer newStringBuffer = new StringBuffer();
//		for (int i = 0, length = oldStringBuffer.length(); i < length; i++) {
//			char c = oldStringBuffer.charAt(i);
//			if (c == '"' || c == '\'') {
//				// do nothing
//			} else {
//				newStringBuffer.append(c);
//			}
//
//		}
//		return new String(newStringBuffer);
//	}

}