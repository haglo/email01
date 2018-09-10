package org.app.controler.email;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.app.controler.EmailService;
import org.app.helper.I18n;
import org.app.model.entity.Pmail;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class CheckingEmails {

	private Pmail pmail;
	private Store store;
	private Folder emailFolder;
	private MailServer mailServer;

	public void check3(EmailService service) {
		String messageContent = "";

//		mailServer = new MailServer();
//		mailServer.init(Mailprovider.PRIVATE);
//		mailServer.init();

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
			store = emailSession.getStore("imaps");
			store.connect(imapHost, username, password);
			if (store.isConnected()) {
				System.out.println("Connect to Imap: true");
			}

//			Session emailSession = Session.getDefaultInstance(properties);
//			store = emailSession.getStore("imaps");
//			store.connect(imapHost, username, password);
//
//			Folder emailFolder = store.getFolder("[Gmail]/Sent Mail");
//			Folder emailFolder = store.getFolder("[Gmail]/Drafts");

			emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();
			for (int i = 0; i < messages.length; i++) {
				Message message = messages[i];
				if (message instanceof MimeMessage) {
					MimeMessage mimeMessage = (MimeMessage) message;
					messageContent = getContentFromEmail(mimeMessage);
				} else {
					messageContent = message.toString();
				}

				pmail = new Pmail();
				pmail.setPsubject(I18n.decodeHeader(message.getSubject()));
				pmail.setPfrom(I18n.decodeHeader(message.getFrom()[0].toString()));
				pmail.setPrecipients(I18n.decodeHeader(message.getRecipients(Message.RecipientType.TO).toString()));
				pmail.setPcontent(I18n.encodeToBase64(messageContent));
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

	private String getContentFromEmail(MimeMessage mimeMessage) throws Exception {
		String resultMessage = "";

		Object msgContent = mimeMessage.getContent();

		if (msgContent instanceof Multipart) {
			Multipart multipart = (Multipart) msgContent;
			for (int j = 0; j < multipart.getCount(); j++) {
				BodyPart bodyPart = multipart.getBodyPart(j);
				String disposition = bodyPart.getDisposition();
				if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) {
					System.out.println("Mail have some attachment");
					DataHandler handler = bodyPart.getDataHandler();
					System.out.println("file name : " + handler.getName());
				} else {
					resultMessage = getTextFromBodyPart(bodyPart);
				}
			}
		} else {
			resultMessage = mimeMessage.getContent().toString();
			resultMessage = transformPlainTextToMessage(resultMessage);
		}
		return resultMessage;

	}

	private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws IOException, MessagingException {

		int count = mimeMultipart.getCount();
		if (count == 0) {
			throw new MessagingException("Multipart with no body parts not supported.");
		}

		boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
		if (multipartAlt) {
			// alternatives appear in an order of increasing
			// faithfulness to the original content. Customize as req'd.
			return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
		}

		String result = "";
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			result += getTextFromBodyPart(bodyPart);
		}
		return result;
	}

	private String getTextFromBodyPart(BodyPart bodyPart) throws IOException, MessagingException {

		String result = "";
		if (bodyPart.isMimeType("text/plain")) {
			String tmp = (String) bodyPart.getContent().toString();
			result = transformPlainTextToMessage(tmp);
		} else if (bodyPart.isMimeType("text/html")) {
			String html = (String) bodyPart.getContent();
			Whitelist whiteList = Whitelist.relaxed();
			result = Jsoup.clean(html, whiteList);
		} else if (bodyPart.getContent() instanceof MimeMultipart) {
			result = getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
		}
		return result;
	}

	/*
	 * This method checks for content-type based on which, it processes and fetches
	 * the content of the message
	 */
	public static void writePart(Part p) throws Exception {
		final String downloadPath = "C:/dev/upload/";

		int i;
		byte[] bArray = new byte[0];
		if (p instanceof Message)
			// Call methos writeEnvelope
			writeEnvelope((Message) p);

		System.out.println("----------------------------");
		System.out.println("CONTENT-TYPE: " + p.getContentType());

		// check if the content is plain text
		if (p.isMimeType("text/plain")) {
			System.out.println("This is plain text");
			System.out.println("---------------------------");
			System.out.println((String) p.getContent());
		}
		// check if the content has attachment
		else if (p.isMimeType("multipart/*")) {
			System.out.println("This is a Multipart");
			System.out.println("---------------------------");
			Multipart mp = (Multipart) p.getContent();
			int count = mp.getCount();
			for (int n = 0; n < count; n++)
				writePart(mp.getBodyPart(n));
		}
		// check if the content is a nested message
		else if (p.isMimeType("message/rfc822")) {
			System.out.println("This is a Nested Message");
			System.out.println("---------------------------");
			writePart((Part) p.getContent());
		}
		// check if the content is an inline image
		else if (p.isMimeType("image/jpeg")) {
			System.out.println("--------> image/jpeg");
			Object o = p.getContent();

			InputStream x = (InputStream) o;
			// Construct the required byte array
			System.out.println("x.length = " + x.available());
			while ((i = (int) ((InputStream) x).available()) > 0) {
				int result = (int) (((InputStream) x).read(bArray));
				if (result == -1)
					i = 0;
				bArray = new byte[x.available()];

				break;
			}
			FileOutputStream f2 = new FileOutputStream(downloadPath + "image.jpg");
			f2.write(bArray);
		}
		// check if the content is an attached image
		else if (p.getContentType().contains("image/")) {
			System.out.println("content type" + p.getContentType());
			File f = new File("image" + new Date().getTime() + ".jpg");
			DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
			com.sun.mail.util.BASE64DecoderStream test = (com.sun.mail.util.BASE64DecoderStream) p.getContent();
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = test.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
		} else {
			Object o = p.getContent();
			if (o instanceof String) {
				System.out.println("This is a string");
				System.out.println("---------------------------");
				System.out.println((String) o);
			} else if (o instanceof InputStream) {
				System.out.println("This is just an input stream");
				System.out.println("---------------------------");
				InputStream is = (InputStream) o;
				is = (InputStream) o;
				int c;
				while ((c = is.read()) != -1)
					System.out.write(c);
			} else {
				System.out.println("This is an unknown type");
				System.out.println("---------------------------");
				System.out.println(o.toString());
			}
		}

	}

	/*
	 * This method would print FROM,TO and SUBJECT of the message
	 */
	public static void writeEnvelope(Message m) throws Exception {
		System.out.println("This is the message envelope");
		System.out.println("---------------------------");
		Address[] a;

		// FROM
		if ((a = m.getFrom()) != null) {
			for (int j = 0; j < a.length; j++)
				System.out.println("FROM: " + a[j].toString());
		}

		// TO
		if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
			for (int j = 0; j < a.length; j++)
				System.out.println("TO: " + a[j].toString());
		}

		// SUBJECT
		if (m.getSubject() != null)
			System.out.println("SUBJECT: " + m.getSubject());

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

	public String transformPlainTextToMessage(String pmessage) {
		StringBuilder sb = new StringBuilder();
		try {
			InputStream is = new ByteArrayInputStream(pmessage.getBytes());
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				sb.append(line);
				sb.append("<br>");
			}

		} catch (

		IOException e) {
			return e.getMessage();
		}
		return sb.toString();

	}
}