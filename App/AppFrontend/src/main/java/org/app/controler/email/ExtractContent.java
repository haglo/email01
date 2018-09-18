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
	private String emailEnvelope;
	private List<File> Attachments;

	private static int counter;
	private int tmp1;
	private int tmp2;

	private String subject;
	private String result;
	private String htmlText;
	private String plainText;

	private boolean isAlternativeActive;
	private boolean isMultiPart;
	private boolean isMessageRfc;
	private boolean isMultiPartAlternative;

	private String emailContent;

	/**
	 * Initialized with an Email-Message
	 * 
	 * @param msg
	 */
	public ExtractContent(Message msg) {
		init();
		counter++;
		try {
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
			EmailType type = getEmailTpe((MimeMessage) msg);

			if (type == EmailType.ASCII_PUR) {
				extractAsciiEmailContent((MimeMessage) msg);
			} else {
				extractMimeEmailContent((MimeMessage) msg);
			}

			if (isAlternativeActive) {
				System.out.println(">>> 1 Write now multipart/alternative in " + tmp1);
//				System.out.println(">>> 1 Write plainText in " + plainText);
//				System.out.println(">>> 1 Write htmlText in " + htmlText);
				if (htmlText.isEmpty())
					result += plainText;
				if (!htmlText.isEmpty())
					result += htmlText;
			}

			setEmailContent(result);
			
		} catch (Exception e) {
			System.out.println(">>> 1 Exception in main ");
			setEmailContent(I18n.EMAIL_FAILURE);
		}

	}

	private void init() {
		tmp1 = 0;
		tmp2 = 0;

		result = "";
		subject = "";
		htmlText = "";
		plainText = "";

		isMessageRfc = false;
		isMultiPart = false;
		isAlternativeActive = false;
		isMultiPartAlternative = false;

		setEmailContent("");
	}

	private EmailType getEmailTpe(Part p) throws Exception {
		if (p.isMimeType("message/rfc822") || p.isMimeType("multipart/*")) {
			return EmailType.MIME;
		} else {
			return EmailType.ASCII_PUR;
		}

	}

	/**
	 * Extract ASCII-Mail
	 * 
	 * @param mimeMessage
	 * @return
	 * @throws Exception
	 */
	private void extractAsciiEmailContent(Part p) throws Exception {

		System.out.println("------------------ ASCII-Mail -------------------------");
		System.out.println(">>> Enter pure ASCII-Mail");
		if (p.isMimeType("text/plain")) {
			result += MimeUtility.decodeText(plainTextToHTML(p.getContent().toString()));
		}

		if (p.isMimeType("text/html")) {
			String html = (String) MimeUtility.decodeText(p.getContent().toString());
			Whitelist whiteList = Whitelist.relaxed();
			result += Jsoup.clean(html, whiteList);
		}
	}

	/**
	 * Extract MIME-Mail
	 * 
	 * @param mimeMessage
	 * @return
	 * @throws Exception
	 */
	private void extractMimeEmailContent(Part p) throws Exception {
		tmp1++;

		/**
		 * Special: Content is a nested message
		 */
		if (p.isMimeType("message/rfc822")) {
			System.out.println("---------------- Message/rfc822 --------------------");
			isMessageRfc = true;
			extractMimeEmailContent((Part) p.getContent());
		}

		/**
		 * Content is MultiPart
		 */
		if (p.isMimeType("multipart/*")) {
			System.out.println("---------------- Multipart -------------------------");
			isMultiPart = true;
			Multipart multipart = (Multipart) p.getContent();
			for (int n = 0; n < multipart.getCount(); n++) {
				tmp2++;
				BodyPart bodyPart = multipart.getBodyPart(n);
				System.out.println(">>> Enter MulitPart: " + tmp2);
				System.out.println("ContetType of MultiPart: " + multipart.getContentType().toString());
				System.out.println("ContntType of BodyPart: " + bodyPart.getContentType().toString());

				try {
					isMultiPartAlternative = multipart.getContentType().toString().contains("multipart/alternative");
					if (isMultiPartAlternative) {
						if (bodyPart.isMimeType("text/plain")) {
							plainText = MimeUtility.decodeText(plainTextToHTML(bodyPart.getContent().toString()));
						}
						if (bodyPart.isMimeType("text/html")) {
							String html = (String) MimeUtility.decodeText(bodyPart.getContent().toString());
							Whitelist whiteList = Whitelist.relaxed();
							htmlText = Jsoup.clean(html, whiteList);
						}
						isAlternativeActive = true;
					}
				} catch (Exception e) {
					isMultiPartAlternative = false;
				}

				System.out.println("boolean of isMultipartAlternative in " + tmp2 + " -- " + isMultiPartAlternative);
				System.out.println("boolean of isAlternativeActive in " + tmp2 + " -- " + isAlternativeActive);

				/**
				 * Write Alternative, if there are more
				 */
				if (!isMultiPartAlternative && isAlternativeActive) {
					System.out.println(">>> 2 Write now (if multipart/alternative) in " + tmp2);
					if (htmlText.isEmpty())
						result += plainText;
					if (!htmlText.isEmpty())
						result += htmlText;
					isAlternativeActive = false;
				}
				
				/**
				 * Write no Alternative
				 */
				if (!isMultiPartAlternative && !isAlternativeActive) {
					System.out.println(">>> 3 Write now (if no multipart/alternative) in  " + tmp2);
					if (bodyPart.isMimeType("text/plain")) {
						result += MimeUtility.decodeText(plainTextToHTML(bodyPart.getContent().toString()));
					}
					if (bodyPart.isMimeType("text/html")) {
						String html = (String) MimeUtility.decodeText(bodyPart.getContent().toString());
						Whitelist whiteList = Whitelist.relaxed();
						result += Jsoup.clean(html, whiteList);
					}
				}

				extractMimeEmailContent(bodyPart);
			}
		}

//		if (isMultiPart && !isMultiPartAlternative) {
//			if (p.isMimeType("text/plain")) {
//				result += MimeUtility.decodeText(plainTextToHTML(p.getContent().toString()));
//			}
//
//			if (p.isMimeType("text/html")) {
//				String html = (String) MimeUtility.decodeText(p.getContent().toString());
//				Whitelist whiteList = Whitelist.relaxed();
//				result += Jsoup.clean(html, whiteList);
//			}
//		}
//
//		if (isMultiPart && isMultiPartAlternative) {
//			if (p.isMimeType("text/plain")) {
////				System.out.println("E) text/plain with Multipart and Alternative " + tmp1);
//				plainText = MimeUtility.decodeText(plainTextToHTML(p.getContent().toString()));
//			}
//
//			if (p.isMimeType("text/html")) {
////				System.out.println("E) text/html with no Multipart and Alternative " + tmp1);
//				String html = (String) MimeUtility.decodeText(p.getContent().toString());
//				Whitelist whiteList = Whitelist.relaxed();
//				htmlText = Jsoup.clean(html, whiteList);
//			}
//		}

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

	private String getMultiPartContent(Part p) throws IOException, MessagingException {
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