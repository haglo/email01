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

	/**
	 * Initialized with an Email-Message
	 * 
	 * @param msg
	 */
	public ExtractContent(Message msg) {
		tmp1 = 0;
		tmp2 = 0;
		saveAttachments = true;
		result = "";
		System.out.println("--------------------------------------");

		try {
			counter++;
			System.out.println("Email Nr: " + counter);
			subject = msg.getSubject().toString();
			System.out.println("Email Subj: " + subject);
		} catch (Exception e) {
			subject = I18n.EMAIL_FAILURE;
		}
		try {
			setEmailContent(getEmailContent((MimeMessage) msg));
//			dumpPart(msg);
		} catch (Exception e) {
			setEmailContent(I18n.EMAIL_FAILURE);
		}

	}

	public void dumpPart(Part p) throws Exception {
		tmp1++;

		/**
		 * Email has only Text as text/plain or text/html; 
		 */
		if ( !( p.isMimeType("multipart/*") || p.isMimeType("message/rfc822") ) && (tmp1 == 1) ) {
			if (p.isMimeType("text/plain")) {
				result += MimeUtility.decodeText(plainTextToHTML(p.getContent().toString()));
			}

			if (p.isMimeType("text/html")) {
				String html = (String) MimeUtility.decodeText(p.getContent().toString());
				Whitelist whiteList = Whitelist.relaxed();
				result += Jsoup.clean(html, whiteList);
			}

			else {
				dumpPart(p);
			}
		}

		/**
		 * Nested Message, recursive call again
		 */
		if (p.isMimeType("message/rfc822")) {
			System.out.println("PPPP-Entering: rfc822: " + tmp1);
			level++;
			dumpPart((Part) p.getContent());
			level--;
		}

		/**
		 * Mulitpart - Alternative
		 */
		else if ((p.isMimeType("multipart/*")) && (p.isMimeType("multipart/alternative"))) {
			System.out.println("PPPP-Entering: p.isMimeType(multipart/alternative) " + tmp1);
			/**
			 * prefer html text over plain text
			 */
			Multipart multiPart = (Multipart) p.getContent();
			for (int i = 0; i < multiPart.getCount(); i++) {
				Part bodyPart = multiPart.getBodyPart(i);

				/**
				 * prefer HTML-Text, therefore first with continue
				 */
				if (bodyPart.isMimeType("text/html")) {
					try {
						String html = (String) MimeUtility.decodeText(bodyPart.getContent().toString());
						Whitelist whiteList = Whitelist.relaxed();
						result += Jsoup.clean(html, whiteList);
						continue;
					} catch (Exception e) {
						result += "PPPP: Mit Fehler in 2";
						System.out.println("PPPP: Mit Fehler in 2 " + tmp1);
						continue;
					}
				}

				else if (bodyPart.isMimeType("text/plain")) {
					try {
						result +=  MimeUtility.decodeText(plainTextToHTML(bodyPart.getContent().toString()));
						continue;
					} catch (Exception e) {
						result += "PPPP: Mit Fehler in 3";
						System.out.println("PPPP: Mit Fehler in 3 " + tmp1);
						continue;
					}
				}
				
				else if (bodyPart.isMimeType("image/*")) {
					System.out.println("--------> image/jpeg");
				}

				else if (bodyPart.isMimeType("multipart/*")){
					dumpPart(bodyPart);
				}

				else if (bodyPart.isMimeType("message/rfc822")){
					dumpPart(bodyPart);
				}
			}
		}
		/**
		 * Mulitpart - Not Alternative
		 */
		else if ((p.isMimeType("multipart/*")) && !(p.isMimeType("multipart/alternative"))) {
			System.out.println("PPPP-Entering: p.isMimeType(multipart/*) and not multipart/alternative " + tmp1);
			Multipart multiPart = (Multipart) p.getContent();
			for (int i = 0; i < multiPart.getCount(); i++) {
				Part bodyPart = multiPart.getBodyPart(i);
				/**
				 * Attachment
				 */
				String disposition = bodyPart.getDisposition();
				if (disposition!=null) {
					if (disposition.equals(Part.ATTACHMENT)) {
						
					}
				}

				/**
				 * Text
				 */
				if (bodyPart.isMimeType("text/html")) {
					try {
						String html = (String) MimeUtility.decodeText(bodyPart.getContent().toString());
						Whitelist whiteList = Whitelist.relaxed();
						result += Jsoup.clean(html, whiteList);
					} catch (Exception e) {
						result += "PPPP: Mit Fehler in 2";
						System.out.println("PPPP: Mit Fehler in 2 " + tmp1);
					}
				}

				else if (bodyPart.isMimeType("text/plain")) {
					try {
						result += MimeUtility.decodeText(plainTextToHTML(bodyPart.getContent().toString()));
					} catch (Exception e) {
						result += "PPPP: Mit Fehler in 3";
						System.out.println("PPPP: Mit Fehler in 3 " + tmp1);
					}
				}

				/**
				 * Image, always shown inline
				 */
				else if (p.getContentType().contains("image/")) {
					String imgString = (String) MimeUtility.decodeText(bodyPart.getContent().toString());
				}


				else {
					dumpPart(bodyPart);
				}
			}
		}
		

		else {
			System.out.println("Entering - Dangerous Part");
			if (!showStructure && !saveAttachments) {

				/**
				 * If we actually want to see the data, and it's not a MIME type we know, fetch
				 * it and check its Java type.
				 */
				Object o = p.getContent();

				if (o instanceof InputStream) {
					pr("This is just an input stream");
					pr("---------------------------");
					InputStream is = (InputStream) o;
//					int c = 0;
//					while ((c = is.read()) != -1)
//						System.out.write(c);
				}

				else {
					Notification.show("Email contains unknown content");
				}
			}

			else {
				// something else, normaly just a separator
			}
		}

//		/*
//		 * If we're saving attachments, write out anything that looks like an attachment
//		 * into an appropriately named file. Don't overwrite existing files to prevent
//		 * mistakes.
//		 */
//		if (saveAttachments && level != 0 && p instanceof MimeBodyPart && !p.isMimeType("multipart/*")) {
//			String disp = p.getDisposition();
//
//			// many mailers don't include a Content-Disposition
//			if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)) {
//				if (filename == null)
//					filename = "Attachment" + attnum++;
//
//				try {
//					File f = new File(filename);
//					if (f.exists())
//						throw new IOException("file exists");
//					((MimeBodyPart) p).saveFile(f);
//				} catch (IOException ex) {
//					Notification.show("Failed to save attachment: " + ex);
//				}
//			}
//		}

		setEmailContent(result);
	}

	/**
	 * Extract Text from MIME-Email With inline images
	 * 
	 * @throws IOException
	 * @throws MessagingException
	 */
	private void extractMimeEmail(Part p) {
		tmp2++;
		System.out.println("PPPP-Entering: extractMimeEmail " + tmp2);

		try {

//			if (p.isMimeType("multipart/*")) {
//				try {
//					Multipart multiPart = (Multipart) p.getContent();
//					level++;
//					for (int i = 0; i < multiPart.getCount(); i++)
//						extractMimeEmail(multiPart.getBodyPart(i));
//					level--;
//				} catch (Exception e) {
//					result += "PPPP: Mit Fehler in 1";
//					System.out.println("PPPP: Mit Fehler in 1" + tmp2);
//
//				}
//			}

			if (p.isMimeType("multipart/alternative")) {
				System.out.println("PPPP-Entering: p.isMimeType(multipart/alternative)" + tmp2);
				/**
				 * prefer html text over plain text
				 */
				Multipart multiPart = (Multipart) p.getContent();
				for (int i = 0; i < multiPart.getCount(); i++) {
					Part bodyPart = multiPart.getBodyPart(i);

					/**
					 * prefer HTML-Text, therefore first with continue
					 */
					if (bodyPart.isMimeType("text/html")) {
						try {
							String html = (String) p.getContent();
							Whitelist whiteList = Whitelist.relaxed();
							result += Jsoup.clean(html, whiteList);
							continue;
						} catch (Exception e) {
							result += "PPPP: Mit Fehler in 2";
							System.out.println("PPPP: Mit Fehler in 2 " + tmp2);
							continue;
						}
					}

					else if (bodyPart.isMimeType("text/plain")) {
						try {
							result += plainTextToHTML(bodyPart.getContent().toString());
							continue;
						} catch (Exception e) {
							result += "PPPP: Mit Fehler in 3";
							System.out.println("PPPP: Mit Fehler in 3 " + tmp2);
							continue;
						}
					}

					else if (p.isMimeType("multipart/*")) {
						System.out.println(
								"PPPP-Entering: p.isMimeType(multipart/alternative) -> p.isMimeType(multipart/*) "
										+ tmp2);
						try {
							extractMimeEmail(multiPart.getBodyPart(i));
						} catch (Exception e) {
							result += "PPPP: Mit Fehler in 4";
							System.out.println("PPPP: Mit Fehler in 4" + tmp2);
						}
					}

				}

			}
		} catch (MessagingException | IOException e) {
//			e.printStackTrace();
			System.out.println("---------------------");
			System.out.println("Error in: " + subject);
			System.out.println("Error in: " + counter);
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
	 * Print a, possibly indented, string.
	 */
	public void pr(String s) {
		if (showStructure)
			System.out.print(indentStr.substring(0, level * 2));
		System.out.println(s);
	}

	/**
	 * Extract Email Part can be used for MimeMessage & BodyPart
	 * 
	 * @param mimeMessage
	 * @return
	 * @throws Exception
	 */
	private String getEmailContent(Part p) throws Exception {
		int i;
		byte[] bArray = new byte[0];
		String result = "";

		/**
		 * Plain-Text
		 */
		if (p.isMimeType("text/plain")) {
			result = p.getContent().toString();
			result = plainTextToHTML(result);
		}

		/**
		 * HTML-Text
		 */
		else if (p.isMimeType("text/html")) {
			String html = (String) p.getContent();
			Whitelist whiteList = Whitelist.relaxed();
			result = Jsoup.clean(html, whiteList);
		}

		/**
		 * Email is Multi-Part
		 */
		else if (p.isMimeType("multipart/*")) {
			MimeMultipart multipart = (MimeMultipart) p.getContent();
			int count = multipart.getCount();

			if (count == 0) {
				throw new MessagingException("Multipart with no body parts not supported.");
			}

			/**
			 * Iterate over all BodyParts
			 */
			for (int n = 0; n < count; n++)
				result = result + getMimeMultipartContent(multipart);
		}

		/**
		 * Content is a nested message
		 */
		else if (p.isMimeType("message/rfc822")) {
			getEmailContent((Part) p.getContent());
		}

		/**
		 * Content is an inline image
		 */
		else if (p.isMimeType("image/jpeg")) {
			result = getInlineImageContent((BodyPart) p.getContent());
		}

		/**
		 * Content is an attached image
		 */
		else if (p.getContentType().contains("image/")) {
//			System.out.println("content type" + p.getContentType());
			File f = new File("image" + new Date().getTime() + ".jpg");
			DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
			com.sun.mail.util.BASE64DecoderStream test = (com.sun.mail.util.BASE64DecoderStream) p.getContent();
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = test.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
		}

		/**
		 * Content is something else
		 */
		else {
			Object o = p.getContent();
			if (o instanceof String) {
				System.out.println("1-PThis is a string");
				System.out.println("---------------------------");
//				System.out.println((String) o);
			} else if (o instanceof InputStream) {
				System.out.println("2-PThis is just an input stream");
				System.out.println("---------------------------");
				InputStream is = (InputStream) o;
				is = (InputStream) o;
				int c;
//				while ((c = is.read()) != -1)
//					System.out.write(c);
			} else {
				System.out.println("3-PThis is an unknown type");
				System.out.println("---------------------------");
//				System.out.println(o.toString());
			}
		}

		/**
		 * get Attachment as file
		 */
		String disposition = p.getDisposition();
		if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) {
			System.out.println("4-PThis Mail have some attachment");
			DataHandler handler = p.getDataHandler();
			System.out.println("5-PThis file name : " + handler.getName());
		}

		return result;

	}

	private String getMimeMultipartContent(MimeMultipart mimeMultipart) throws IOException, MessagingException {

		int count = mimeMultipart.getCount();
		if (count == 0) {
			throw new MessagingException("Multipart with no body parts not supported.");
		}

		boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
		if (multipartAlt) {
			// alternatives appear in an order of increasing
			// faithfulness to the original content. Customize as req'd.
			return getBodyPartContent(mimeMultipart.getBodyPart(count - 1));
		}

		/**
		 * Normal Mulitpart Iterates over BodyParts of the MultiPart
		 */
		String result = "";
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			result = result + getBodyPartContent(bodyPart);
		}
		return result;
	}

	private String getBodyPartContent(BodyPart bodyPart) throws IOException, MessagingException {

		String result = "";
		if (bodyPart.isMimeType("text/plain")) {
			String tmp = (String) bodyPart.getContent().toString();
			result = plainTextToHTML(tmp);
		}

		else if (bodyPart.isMimeType("text/html")) {
			String html = (String) bodyPart.getContent();
			Whitelist whiteList = Whitelist.relaxed();
			result = Jsoup.clean(html, whiteList);
		}

		else if (bodyPart.isMimeType("image/jpeg")) {
			result = getInlineImageContent(bodyPart);
		}

		else if (bodyPart.getContent() instanceof MimeMultipart) {
			result = getMimeMultipartContent((MimeMultipart) bodyPart.getContent());
		}
		return result;
	}

	/**
	 * Content is an inline image
	 */
	private String getInlineImageContent(BodyPart bodyPart) throws IOException, MessagingException {

		int i;
		byte[] bArray = new byte[0];
		String result = "";
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
		result = fileOutputStream.toString();
		fileOutputStream.close();
		x.close();
		return result;
	}

//	/**
//	 * Return the primary text content of the message.
//	 */
//	private String getFirstText(Part p) throws MessagingException, IOException {
//
//		boolean textIsHtml = false;
//
//		if (p.isMimeType("text/*")) {
//			String s = (String) p.getContent();
//			textIsHtml = p.isMimeType("text/html");
//			return s;
//		}
//
//		if (p.isMimeType("multipart/alternative")) {
//			// prefer html text over plain text
//			Multipart mp = (Multipart) p.getContent();
//			String text = null;
//			for (int i = 0; i < mp.getCount(); i++) {
//				Part bp = mp.getBodyPart(i);
//				if (bp.isMimeType("text/plain")) {
//					if (text == null)
//						text = getFirstText(bp);
//					continue;
//				} else if (bp.isMimeType("text/html")) {
//					String s = getFirstText(bp);
//					if (s != null)
//						return s;
//				} else {
//					return getFirstText(bp);
//				}
//			}
//			return text;
//		} else if (p.isMimeType("multipart/*")) {
//			Multipart mp = (Multipart) p.getContent();
//			for (int i = 0; i < mp.getCount(); i++) {
//				String s = getFirstText(mp.getBodyPart(i));
//				if (s != null)
//					return s;
//			}
//		}
//
//		return null;
//	}
//

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String content) {
		this.emailContent = content;
	}

}