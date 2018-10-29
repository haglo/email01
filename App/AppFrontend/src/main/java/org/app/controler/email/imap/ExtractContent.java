package org.app.controler.email.imap;

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

import org.app.controler.email.Const;
import org.app.helper.I18n;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class ExtractContent implements Const {
	private List<File> attachedFiles;
	private List<String> attachedFileNames;
	private int numberOfAttachments;

	private List<File> inlineFiles;
	private List<String> inlineFileNames;
	private int numberOfInlineAttachments;

	private String result;
	private String plainText;
	private String htmlText;

	private boolean isMultiPartMixed;
	private boolean isMultiPartRelated;
	private boolean isMultiPartAlternative;
	private boolean isMultiPartAlternativeActive;
	private boolean isMultiPartAlternativeHtml;
	private boolean isMultiPartAlternativePlain;

	private String emailContent;
	private ExtractAttachment extractAttachment;

	public ExtractContent(Part msg) {
		extractAttachment = new ExtractAttachment();
		init();

		try {
			EmailType type = getEmailType((MimeMessage) msg);

			if (type == EmailType.ASCII_PUR) {
				extractAsciiEmailContent((MimeMessage) msg);
			} else {
				extractMimeEmailContent((MimeMessage) msg);
			}

			/**
			 * Special: Write if Alternative and there are no more others, only Alternative
			 */
			if (isMultiPartAlternativeActive) {
				if (htmlText.isEmpty())
					result += plainText;
				if (!htmlText.isEmpty())
					result += htmlText;
			}

			setEmailContent(result);
			attachedFiles = extractAttachment.getAttachedFiles();
			attachedFileNames = extractAttachment.getAttachedFileNames();
			numberOfAttachments = attachedFileNames.size();

			inlineFiles = extractAttachment.getInlineFiles();
			inlineFileNames = extractAttachment.getInlineFileNames();
			numberOfInlineAttachments = inlineFileNames.size();
		} catch (Exception e) {
			System.out.println(">>> 1 Exception in main ");
			setEmailContent("Error by reading EmailContent");
		}

	}

	private void init() {
		result = "";
		htmlText = "";
		plainText = "";

		isMultiPartMixed = false;
		isMultiPartRelated = false;
		isMultiPartAlternative = false;
		isMultiPartAlternativeActive = false;
		isMultiPartAlternativePlain = false;
		isMultiPartAlternativeHtml = false;

		attachedFiles = new ArrayList<File>();
		attachedFileNames = new ArrayList<String>();

		setEmailContent("");
	}

	private EmailType getEmailType(Part p) throws Exception {
		if (p.isMimeType("message/rfc822") || p.isMimeType("multipart/*")) {
			return EmailType.MIME;
		} else {
			return EmailType.ASCII_PUR;
		}

	}

	private void extractAsciiEmailContent(Part p) throws Exception {
		result += MimeUtility.decodeText(plainTextToHTML(p.getContent().toString()));
	}

	private void extractMimeEmailContent(Part p) throws Exception {

		/**
		 * Special: Content is a nested message
		 */
		if (p.isMimeType("message/rfc822")) {
			extractMimeEmailContent((Part) p.getContent());
		}

		/**
		 * Default: Content is MultiPart
		 */
		if (p.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) p.getContent();

			if (!isMultiPartMixed)
				isMultiPartMixed = multipart.getContentType().toString().contains("multipart/mixed");

			if (!isMultiPartRelated)
				isMultiPartRelated = multipart.getContentType().toString().contains("multipart/related");

			if (!isMultiPartAlternative) {
				isMultiPartAlternative = multipart.getContentType().toString().contains("multipart/alternative");
				isMultiPartMixed = false;
				isMultiPartRelated = false;
			}
			if (!isMultiPartAlternativeActive)
				isMultiPartAlternativeActive = multipart.getContentType().toString().contains("multipart/alternative");

			for (int n = 0; n < multipart.getCount(); n++) {
				BodyPart bodyPart = multipart.getBodyPart(n);

				// read Text of the message
				if (bodyPart.getDisposition() == null) {
					try {
						if (!isMultiPartAlternativeActive && !isMultiPartMixed) {
							if (bodyPart.isMimeType("text/plain")) {
								plainText = MimeUtility.decodeText(plainTextToHTML(bodyPart.getContent().toString()));
								result += plainText;
							}
							if (bodyPart.isMimeType("text/html")) {
								htmlText = (String) MimeUtility.decodeText(bodyPart.getContent().toString());
								result += htmlText;
							}
						}
						if (isMultiPartAlternativeActive && !isMultiPartMixed) {
							if (bodyPart.isMimeType("text/plain")) {
								plainText = MimeUtility.decodeText(plainTextToHTML(bodyPart.getContent().toString()));
								isMultiPartAlternativePlain = true;
							}
							if (bodyPart.isMimeType("text/html")) {
								htmlText = (String) MimeUtility.decodeText(bodyPart.getContent().toString());
								isMultiPartAlternativeHtml = true;
							}
							// prefer HTML
							if (isMultiPartAlternativePlain && isMultiPartAlternativeHtml) {
								result += htmlText;
								htmlText = "";
								plainText = "";
								isMultiPartAlternativeActive = false;
							}
						}
					} catch (Exception e) {
						result = "Error by reading Text of the message";
					}
				}

				// save Attachment of the content
				if (bodyPart.getDisposition() != null) {
					try {
						if (bodyPart.isMimeType("text/*")) {
							extractAttachment.extract(bodyPart);
						}
						if (bodyPart.isMimeType("image/*")) {
							extractAttachment.extract(bodyPart);
						}
						if (bodyPart.isMimeType("audio/*")) {
							extractAttachment.extract(bodyPart);
						}
						if (bodyPart.isMimeType("video/*")) {
							extractAttachment.extract(bodyPart);
						}
						if (bodyPart.isMimeType("application/*")) {
							extractAttachment.extract(bodyPart);
						}

					} catch (Exception e) {
						result = "Error by reading Text of the Email";
					}

				}
				extractMimeEmailContent(bodyPart);

			} // end for
		} // end if
	} // end method

	/**
	 * extractMimeEmailContent(bodyPart); } } }
	 * 
	 * /** Helper-Method: Transform Plain-Text to HTML-Text, that it is shown
	 * correct in the HTML-Text-Field of the EmailClient. EmailCLient has only
	 * HTML-Text-Field
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

	public List<File> getAttachedFiles() {
		return attachedFiles;
	}

	public void setAttachedFiles(List<File> attachedFiles) {
		this.attachedFiles = attachedFiles;
	}

	public List<String> getAttachedFileNames() {
		return attachedFileNames;
	}

	public void setAttachedFileNames(List<String> attachedFileNames) {
		this.attachedFileNames = attachedFileNames;
	}

	public int getNumberOfAttachments() {
		return numberOfAttachments;
	}

	public void setNumberOfAttachments(int numberOfAttachments) {
		this.numberOfAttachments = numberOfAttachments;
	}

	public List<File> getInlineFiles() {
		return inlineFiles;
	}

	public void setInlineFiles(List<File> inlineFiles) {
		this.inlineFiles = inlineFiles;
	}

	public List<String> getInlineFileNames() {
		return inlineFileNames;
	}

	public void setInlineFileNames(List<String> inlineFileNames) {
		this.inlineFileNames = inlineFileNames;
	}

	public int getNumberOfInlineAttachments() {
		return numberOfInlineAttachments;
	}

	public void setNumberOfInlineAttachments(int numberOfInlineAttachments) {
		this.numberOfInlineAttachments = numberOfInlineAttachments;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String content) {
		this.emailContent = content;
	}

}