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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.app.helper.I18n;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class ExtractContent {
	private String downloadPath;

	private boolean showStructure = false;
	private boolean showMessage = false;
	private boolean saveAttachments = false;
	private String indentStr = " ";
	private int attnum = 1;
	private int level = 0;
	private String emailEnvelope;
	private List<File> attachedFiles;
	private List<String> attachedFileNames;

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
	private ExtractAttachment extractAttachment;

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
				extractAttachment = new ExtractAttachment(msg);
			}

			/**
			 * Special: Write if Alternative and there are no more others, only Alternative
			 */
			if (isAlternativeActive) {
				System.out.println(">>> 1 Write now multipart/alternative in " + tmp1
						+ ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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
		downloadPath = "C:/dev/upload/";

		isMessageRfc = false;
		isMultiPart = false;
		isAlternativeActive = false;
		isMultiPartAlternative = false;

		attachedFiles = new ArrayList<File>();
		attachedFileNames = new ArrayList<String>();

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
			System.out.println(">>> Enter text/plain");
			result += MimeUtility.decodeText(plainTextToHTML(p.getContent().toString()));
		}

		else if (p.isMimeType("text/html")) {
			System.out.println(">>> Enter text/html");
			String html = (String) MimeUtility.decodeText(p.getContent().toString());
			Whitelist whiteList = Whitelist.relaxed();
			result += Jsoup.clean(html, whiteList);
		}

		else {
			System.out.println(">>> Enter else");
			result += MimeUtility.decodeText(plainTextToHTML(p.getContent().toString()));
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
		else if (p.isMimeType("multipart/*")) {
			System.out.println("---------------- Multipart -------------------------");
			isMultiPart = true;
			Multipart multipart = (Multipart) p.getContent();
			for (int n = 0; n < multipart.getCount(); n++) {
				tmp2++;
				BodyPart bodyPart = multipart.getBodyPart(n);
				MimeBodyPart mimeBodyPart = (MimeBodyPart) multipart.getBodyPart(n);
				System.out.println(">>> Enter MulitPart: " + tmp2);
				System.out.println(">>> ContetType of MultiPart: " + multipart.getContentType().toString());
				System.out.println(">>> ContntType of BodyPart: " + bodyPart.getContentType().toString());

				try {
					isMultiPartAlternative = multipart.getContentType().toString().contains("multipart/alternative");
					if (isMultiPartAlternative) {
						if (checkShowInline(bodyPart) && bodyPart.isMimeType("text/plain")) {
							plainText = MimeUtility.decodeText(plainTextToHTML(bodyPart.getContent().toString()));
						}
						if (checkShowInline(bodyPart) && bodyPart.isMimeType("text/html")) {
							String html = (String) MimeUtility.decodeText(bodyPart.getContent().toString());
							Whitelist whiteList = Whitelist.relaxed();
							htmlText = Jsoup.clean(html, whiteList);
						}
						isAlternativeActive = true;
					}
				} catch (Exception e) {
					isMultiPartAlternative = false;
				}

				System.out
						.println(">>> boolean of isMultipartAlternative in " + tmp2 + " -- " + isMultiPartAlternative);
				System.out.println(">>> boolean of isAlternativeActive in " + tmp2 + " -- " + isAlternativeActive);

				/**
				 * Write if Alternative and there are more
				 */
				if (checkShowInline(bodyPart) && !isMultiPartAlternative && isAlternativeActive) {
					System.out.println(">>> 2 Write now (if multipart/alternative) in " + tmp2
							+ ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					if (htmlText.isEmpty())
						result += plainText;
					if (!htmlText.isEmpty())
						result += htmlText;
					isAlternativeActive = false;
				}

				/**
				 * Write if no Alternative
				 */
				if (checkShowInline(bodyPart) && !isMultiPartAlternative && !isAlternativeActive) {
					System.out.println(">>> 3 Write now (if no multipart/alternative) in  " + tmp2
							+ ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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

	}

	public boolean checkShowInline(Part p) {
		boolean toShow = true;

		try {
			if (p.getDisposition() != null) {
				toShow = false;
			}
		} catch (MessagingException e) {
			toShow = false;
		}
		return toShow;

	}

	/**
	 * Helper-Methode Transform Plain Text to HTML-Text, that it is shown correct in
	 * the HTML-Text-Field of the EmailClient EmailCLient has only HTML-Text-Field
	 * 
	 * @param pmessage
	 * @return
	 * @throws IOException
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

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String content) {
		this.emailContent = content;
	}

}