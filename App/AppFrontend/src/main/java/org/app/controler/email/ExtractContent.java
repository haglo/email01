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
import java.util.List;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.app.helper.I18n;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import com.vaadin.ui.Notification;

public class ExtractContent {
	final String downloadPath = "C:/dev/upload/";

	private boolean showStructure = false;
	private boolean showMessage = false;
	private boolean saveAttachments = false;
	private String indentStr = " ";
	private int attnum = 1;
	private int level = 0;
	private String result;
	private String emailContent;
	private String emailEnvelope;
	private List<File> Attachments;

	private static int counter = 0;
	private int tmp1 = 0;
	private int tmp2 = 0;
	private static String subject;

	private boolean isAlternativeActive = false;
	private String htmlText = "";
	private String plainText = "";
	private boolean isMultiPart = false;
	private boolean isMessageRfc = false;
	private boolean isMultiPartAlternative = false;

	/**
	 * Initialized with an Email-Message
	 * 
	 * @param msg
	 */
	public ExtractContent(Message msg) {
		result = "";
		try {
			counter++;
			System.out.println(
					"===================================================================================================================================================");
			System.out.println("Email Nr: " + counter);
			subject = msg.getSubject().toString();
			System.out.println("Email Subj: " + subject);
			System.out.println(
					"===================================================================================================================================================");
		} catch (Exception e) {
			subject = I18n.EMAIL_FAILURE;
		}

		try {
			extractEmailContent((MimeMessage) msg);
			if (isAlternativeActive) {
				System.out.println(">>> Write now multipart/alternative in " );
				if (htmlText.isEmpty())
					result += plainText;
				if (!htmlText.isEmpty())
					result += htmlText;
			}
			setEmailContent(result);
		} catch (Exception e) {
			setEmailContent(I18n.EMAIL_FAILURE);
		}

	}

	/**
	 * Extract Email Part can be used for MimeMessage & BodyPart
	 * 
	 * @param mimeMessage
	 * @return
	 * @throws Exception
	 */
	private void extractEmailContent(Part p) throws Exception {
		tmp1++;

//		if (p instanceof BodyPart) {
//			System.out.println("Instance of BodyPart " + tmp1);
//		}

//		try {
//			isMultiPart = ((BodyPart) p).getParent().getContentType().matches("multipart/*");
//			System.out.println("A) boolean isMultipart: " + tmp1 + " " + isMultiPart);
//		} catch (Exception e) {
//			isMultiPart = false;
//			System.out.println("A) Error - boolean isMultipart: " + tmp1 + " " + isMultiPart);
//		}
//		try {
//			isMultiPartAlternative = ((BodyPart) p).getParent().getContentType().matches("multipart/alternative");
//			System.out.println("A) boolean isMultipartAlternative: " + tmp1 + " " + isMultiPartAlternative);
//		} catch (Exception e) {
//			isMultiPartAlternative = false;
//			System.out.println("A) Error - boolean isMultipartAlternative: " + tmp1 + " " + isMultiPartAlternative);
//		}
//
//		try {
//			isMessageRfc = ((BodyPart) p).getParent().getContentType().matches("message/rfc822");
//			System.out.println("A) boolean isMessageRfc: " + tmp1 + " " + isMessageRfc);
//		} catch (Exception e) {
//			isMessageRfc = false;
//			System.out.println("A) Error - boolean isMessageRfc: " + tmp1 + " " + isMessageRfc);
//		}

//		if (p.isMimeType("text/plain")) {
//			System.out.println("1) text/plain with no Multipart and no rfc822 " + tmp1);
//			result += MimeUtility.decodeText(plainTextToHTML(p.getContent().toString()));
//		}
//
//		if (p.isMimeType("text/html")) {
//			System.out.println("1) text/html with no Multipart and no rfc822 " + tmp1);
//			String html = (String) MimeUtility.decodeText(p.getContent().toString());
//			Whitelist whiteList = Whitelist.relaxed();
//			result += Jsoup.clean(html, whiteList);
//		}

		/**
		 * Special: Content is a nested message
		 */
		if (p.isMimeType("message/rfc822")) {
			isMessageRfc = true;
			System.out.println("---------------- Message/rfc822 --------------------");
			extractEmailContent((Part) p.getContent());
		}

		if (p.isMimeType("multipart/*")) {
			isMultiPart = true;
			System.out.println("---------------- Multipart -------------------------");
			Multipart multipart = (Multipart) p.getContent();
			for (int n = 0; n < multipart.getCount(); n++) {
				tmp2++;
				BodyPart bodyPart = multipart.getBodyPart(n);
				System.out.println(">>> Enter MulitPart: " + tmp2);
				System.out.println("CotentType of MultiPart: " + multipart.getContentType().toString());
				System.out.println("CotentType of BodyPart: " + bodyPart.getContentType().toString());

				try {
					isMultiPartAlternative = multipart.getContentType().toString().contains("multipart/alternative");
					if (isMultiPartAlternative) {
						isAlternativeActive = true;
					}
				} catch (Exception e) {
					isMultiPartAlternative = false;
				}
				System.out.println("C) boolean isMultipartAlternative in " + tmp2 + " -- " + isMultiPartAlternative);
				System.out.println("C) boolean isAlternativeActive in " + tmp2 + " -- " + isAlternativeActive);

//				try {
//					isMessageRfc = bodyPart.getParent().getContentType().matches("message/rfc822");
//					System.out.println("C) boolean isMessageRfc: " + tmp1 + " " + isMessageRfc);
//				} catch (Exception e) {
//					isMessageRfc = false;
//				}

				if (!isMultiPartAlternative && isAlternativeActive) {
					System.out.println(">>> Write now multipart/alternative in " + tmp2);
					if (htmlText.isEmpty())
						result += plainText;
					if (!htmlText.isEmpty())
						result += htmlText;
					isAlternativeActive = false;
				}
				extractEmailContent(bodyPart);
			}
		}

		/**
		 * Special: Email has only Text text/plain or text/html, no multipart or rfc822;
		 */
		if (!isMultiPart && !isMessageRfc) {
			System.out.println("----------- No Multipart - it is ASCII-Mail -----------");
			System.out.println(">>> Enter ASCII-Mail: " + tmp1);
			if (p.isMimeType("text/plain")) {
//				System.out.println("2) text/plain with no Multipart and no rfc822 " + tmp1);
				result += MimeUtility.decodeText(plainTextToHTML(p.getContent().toString()));
			}

			if (p.isMimeType("text/html")) {
//				System.out.println("2) text/html with no Multipart and no rfc822 " + tmp1);
				String html = (String) MimeUtility.decodeText(p.getContent().toString());
				Whitelist whiteList = Whitelist.relaxed();
				result += Jsoup.clean(html, whiteList);
			}
			isMultiPart = false;
			isMessageRfc = false;

		}

		if (isMultiPart && !isMultiPartAlternative) {
			if (p.isMimeType("text/plain")) {
//				System.out.println("D) text/plain with Multipart and no Alternative " + tmp1);
				result += MimeUtility.decodeText(plainTextToHTML(p.getContent().toString()));
			}

			if (p.isMimeType("text/html")) {
//				System.out.println("D) text/html with Multipart and no Alternative " + tmp1);
				String html = (String) MimeUtility.decodeText(p.getContent().toString());
				Whitelist whiteList = Whitelist.relaxed();
				result += Jsoup.clean(html, whiteList);
			}
		}

		if (isMultiPart && isMultiPartAlternative) {
			if (p.isMimeType("text/plain")) {
//				System.out.println("E) text/plain with Multipart and Alternative " + tmp1);
				plainText = MimeUtility.decodeText(plainTextToHTML(p.getContent().toString()));
			}

			if (p.isMimeType("text/html")) {
//				System.out.println("E) text/html with no Multipart and Alternative " + tmp1);
				String html = (String) MimeUtility.decodeText(p.getContent().toString());
				Whitelist whiteList = Whitelist.relaxed();
				htmlText = Jsoup.clean(html, whiteList);
			}
		}

//		/**
//		 * Default: Whole Email is Multi-Part
//		 */
//		else if (p.isMimeType("multipart/*")) {
//			String htmlText = "";
//			String plainText = "";
//			boolean withHTML = false;
//			boolean withPlain = false;
//			boolean isMimeAlternative = false;
//
//			MimeMultipart multipart = (MimeMultipart) p.getContent();
//
//			if (multipart.getCount() == 0) {
//				throw new MessagingException("Multipart with no body parts not supported.");
//			}
//
////			isMimeAlternative = new ContentType(multipart.getContentType()).match("multipart/alternative");
//
//			// Iterate over all BodyParts
//			for (int n = 0; n < multipart.getCount(); n++) {
//
//				System.out.println("Is part " + n);
//				BodyPart bodyPart = multipart.getBodyPart(n);
//				isMimeAlternative = false;
//				withHTML = false;
//				withPlain = false;
//
//				if (bodyPart.getParent().getContentType().matches("multipart/alternative")) {
//					isMimeAlternative = true;
//					withHTML = new ContentType(bodyPart.getContentType()).match("text/html");
//					withPlain = new ContentType(bodyPart.getContentType()).match("text/plain");
//					if (withHTML) {
//						String html = MimeUtility.decodeText(bodyPart.getContent().toString());
//						Whitelist whiteList = Whitelist.relaxed();
//						htmlText = Jsoup.clean(html, whiteList);
//						plainText = "";
//					}
//					if (withPlain && htmlText.isEmpty()) {
//						plainText = MimeUtility.decodeText(plainTextToHTML(bodyPart.getContent().toString()));
//					}
//
//				}
//				if (!isMimeAlternative && (!htmlText.isEmpty() || !plainText.isEmpty())) {
//					if (!htmlText.isEmpty()) {
//						result += htmlText;
//					} else {
//						result += plainText;
//					}
//					htmlText = "";
//					plainText = "";
//				}
//
////				if 
////
////				if (isMimeAlternative) {
////					withHTML = false;
////					withHTML = new ContentType(bodyPart.getContentType()).match("text/html");
////					if (withHTML) {
////						String html = MimeUtility.decodeText(bodyPart.getContent().toString());
////						Whitelist whiteList = Whitelist.relaxed();
////						htmlText += Jsoup.clean(html, whiteList);
////					}
////					if (!withHTML) {
////						plainText = MimeUtility.decodeText(plainTextToHTML(bodyPart.getContent().toString()));
////					}
////					result += htmlText;
//////					plainText = getMultiPartContent(bodyPart);
////					System.out.println("Is multipart/alternative " + n);
//////					getMultiPartContent(bodyPart);
//
//				else {
//					System.out.println("Is multipart/* " + n);
//					getNormalMimeMultipartContent(bodyPart);
//				}
//			}
//		}
//
//		/**
//		 * Content is something else
//		 */
//		else {
//			Object o = p.getContent();
//			if (o instanceof String) {
//				System.out.println("1-PThis is a string");
//				System.out.println("---------------------------");
////				System.out.println((String) o);
//			} else if (o instanceof InputStream) {
//				System.out.println("2-PThis is just an input stream");
//				System.out.println("---------------------------");
//				InputStream is = (InputStream) o;
//				is = (InputStream) o;
//				int c;
////				while ((c = is.read()) != -1)
////					System.out.write(c);
//			} else {
//				System.out.println("3-PThis is an unknown type");
//				System.out.println("---------------------------");
////				System.out.println(o.toString());
//			}
//		}
//
//		/**
//		 * get Attachment as file
//		 */
//		String disposition = p.getDisposition();
//		if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) {
//			System.out.println("4-PThis Mail have some attachment");
//			DataHandler handler = p.getDataHandler();
//			System.out.println("5-PThis file name : " + handler.getName());
//		}


	}

	private void saveAlternativeContent(BodyPart bodyPart) {

	}

	private String getMultiPartContent(Part p) throws IOException, MessagingException {
		// prefer HTML

		/**
		 * tmp1: wie oft aufgerufen
		 */
		tmp1++;
		boolean isHTML = new ContentType(p.getContentType()).match("text/html");

		if (isHTML) {
			try {
				String html = MimeUtility.decodeText(p.getContent().toString());
				Whitelist whiteList = Whitelist.relaxed();
				result = Jsoup.clean(html, whiteList);
				System.out.println("isHTML: " + tmp1);
			} catch (Exception e) {
				result += "PPPP: Mit Fehler in 2";
				System.out.println("PPPP: Mit Fehler in 2 " + tmp1);
			}

		} else {
			try {
				String plain = MimeUtility.decodeText(plainTextToHTML(p.getContent().toString()));
				result = plain;
				System.out.println("isPlainText: " + tmp1);
			} catch (Exception e) {
				result += "PPPP: Mit Fehler in 3";
				System.out.println("PPPP: Mit Fehler in 3 " + tmp1);
			}
		}

		return result;

//		if (p.isMimeType("text/html")) {
//			isHTML = true;
//			try {
//				String html = MimeUtility.decodeText(p.getContent().toString());
//				Whitelist whiteList = Whitelist.relaxed();
//				result += Jsoup.clean(html, whiteList);
//			} catch (Exception e) {
//				result += "PPPP: Mit Fehler in 2";
//				System.out.println("PPPP: Mit Fehler in 2 " + tmp1);
//			}
//		}
//
//		else if (p.isMimeType("text/plain") && isHTML == false) {
//			try {
//				result += MimeUtility.decodeText(plainTextToHTML(p.getContent().toString()));
//			} catch (Exception e) {
//				result += "PPPP: Mit Fehler in 3";
//				System.out.println("PPPP: Mit Fehler in 3 " + tmp1);
//			}
//		}

	}

	private void getNormalMimeMultipartContent(Part p) throws IOException, MessagingException {
		tmp2++;
		System.out.println("Acthung in getNormalMimeMultipartContent " + tmp2);

		if (p.isMimeType("text/html")) {
			try {
				String html = MimeUtility.decodeText(p.getContent().toString());
				Whitelist whiteList = Whitelist.relaxed();
				result += Jsoup.clean(html, whiteList);
			} catch (Exception e) {
				result += "PPPP: Mit Fehler in 2";
				System.out.println("PPPP: Mit Fehler in 2 " + tmp1);
			}
		}

		else if (p.isMimeType("text/plain")) {
			try {
				result += MimeUtility.decodeText(plainTextToHTML(p.getContent().toString()));
			} catch (Exception e) {
				result += "PPPP: Mit Fehler in 3";
				System.out.println("PPPP: Mit Fehler in 3 " + tmp1);
			}
		}

	}

	/**
	 * Helper-Methode Transform Plain Text to HTML-Text, that it is shown correct in
	 * the HTML-Text-Field of the EmailClient EmailCLient has only HTML-Text-Field
	 * 
	 * @param pmessage
	 * @return
	 */
	public String plainTextToHTML(String pmessage) {
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

	/**
	 * Content is an inline image
	 */
	private void getInlineImageContent(BodyPart bodyPart) throws IOException, MessagingException {

		int i;
		byte[] bArray = new byte[0];
		Object o = bodyPart.getContent();

		InputStream x = (InputStream) o;
		while ((i = (int) ((InputStream) x).available()) > 0) {
			int j = (int) (((InputStream) x).read(bArray));
			if (j == -1)
				i = 0;
			bArray = new byte[x.available()];
			break;
		}

		FileOutputStream fileOutputStream = new FileOutputStream(downloadPath + "image.jpg");
		fileOutputStream.write(bArray);
		result += fileOutputStream.toString();

		fileOutputStream.close();
		x.close();
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String content) {
		this.emailContent = content;
	}

}