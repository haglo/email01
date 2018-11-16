package org.app.mail.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.app.mail.common.AIFile.FILE_TYPE;

public class ExtractContent implements Const {
	private List<AIFile> aiFiles;
	private int numberOfAttachments;
	private int numberOfInlines;

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

	public ExtractContent(Part msg, String pathDef) {
		extractAttachment = new ExtractAttachment(pathDef);
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

			aiFiles = extractAttachment.getAiFiles();
			for (AIFile ai : aiFiles) {
				if (ai.getFileType().equals(FILE_TYPE.ATTACHMENT))
					numberOfAttachments++;
				if (ai.getFileType().equals(FILE_TYPE.INLINE))
					numberOfInlines++;

			}

			result = result.replaceAll("\r\n", "");
			result = result.replaceAll("\n", "");
			result = parseCID(result, pathDef);

			setEmailContent(result);

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

		aiFiles = new ArrayList<AIFile>();

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
						result = "Error-Content by reading Text of the message";
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
						result = "Error-Attachment by reading Text of the Email";
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

	@SuppressWarnings("static-access")
	private String parseCID(String in, String id) {

		StringBuffer tmpSb;

		String CID_STRING = "\"cid:";
		String NEW_STRING = "\"" + MAIL_INLINE_IMAGES_PATH_CANONICAL + id + "/";
		String fileExt = ".png1";
		StringBuffer sb = new StringBuffer(in);
		int cstart = 0;
		int clength = 0;
		int num = in.indexOf(CID_STRING);

		while (num >= 0) {
			String tmp = "";
			tmpSb = new StringBuffer();

			cstart = num;
			clength = CID_STRING.length();
			
			
			// get correct extrension
			for (int i = cstart + clength; i <= sb.length() - 1; i++) {
				if (sb.charAt(i) != '"') {
					tmpSb.append(sb.charAt(i));
				} else {
					break;
				}
			}
			for (AIFile aif : aiFiles) {
				if (aif.getFileId().equalsIgnoreCase(tmpSb.toString())) {
					fileExt = aif.getFileExtension();
				}
			}

			
			sb.replace(cstart, cstart + clength, NEW_STRING);
			int j = cstart + clength + NEW_STRING.length();
			for (int n = j; n <= sb.length() - 1; n++) {
				if (sb.charAt(n) == '"') {
					sb.insert(n, fileExt);
					break;
				}
			}

			in = sb.toString();
			num = in.indexOf(CID_STRING, num + 1);
		}

		return in;

	}

	public List<AIFile> getAiFiles() {
		return aiFiles;
	}

	public void setAiFiles(List<AIFile> aiFiles) {
		this.aiFiles = aiFiles;
	}

	public int getNumberOfAttachments() {
		return numberOfAttachments;
	}

	public void setNumberOfAttachments(int numberOfAttachments) {
		this.numberOfAttachments = numberOfAttachments;
	}

	public int getNumberOfInlines() {
		return numberOfInlines;
	}

	public void setNumberOfInlines(int numberOfInlines) {
		this.numberOfInlines = numberOfInlines;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String content) {
		this.emailContent = content;
	}

}