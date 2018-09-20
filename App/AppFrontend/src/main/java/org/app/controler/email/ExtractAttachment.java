package org.app.controler.email;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
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
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.app.helper.I18n;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class ExtractAttachment {

	private String downloadPathAttachment;
	private String downloadPathCID;
	private List<File> attachedFiles;
	private List<String> attachedFileNames;
	private List<File> inlineFiles;
	private List<String> inlineFileNames;
	private int numberOfAttachments;

	public ExtractAttachment(Message msg) {
		init();
		try {
			extractAttachments((MimeMessage) msg);
		} catch (Exception e) {
			this.attachedFiles = null;
		}
	}

	private void init() {
		downloadPathAttachment = "C:/dev/upload/attachment/";
		downloadPathCID = "C:/dev/upload/cid/";
		attachedFiles = new ArrayList<File>();
		attachedFileNames = new ArrayList<String>();
		inlineFiles = new ArrayList<File>();
		inlineFileNames = new ArrayList<String>();
		numberOfAttachments = 0;

	}

	private void extractAttachments(MimeMessage msg) throws IOException, MessagingException {
		Multipart multipart = (Multipart) msg.getContent();

		for (int i = 0; i < multipart.getCount(); i++) {
			String attachedFileName = "";
			String inlineFileName = "";
			MimeBodyPart mimeBodyPart = (MimeBodyPart) multipart.getBodyPart(i);
			BodyPart bodyPart = multipart.getBodyPart(i);
			String disposition = bodyPart.getDisposition();

			/**
			 * Attachments
			 */
			if ((disposition != null) && (disposition.equals(Part.ATTACHMENT)) && (!bodyPart.getFileName().isEmpty())) {
				attachedFileName = bodyPart.getFileName();
				File file = new File(downloadPathAttachment + attachedFileName);
//				DataHandler handler = bodyPart.getDataHandler();
//				System.out.println(">>> 1a) Attachment-Standard - Filename: " + handler.getName());
				System.out.println(">>> 1bbb) Attachment-Standard - Filename: " + attachedFileName);
//				attachedFileNames.add(handler.getName().toString());

				InputStream is = bodyPart.getInputStream();
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buf = new byte[4096];
				int bytesRead;
				while ((bytesRead = is.read(buf)) != -1) {
					fos.write(buf, 0, bytesRead);
				}
				fos.close();
				attachedFileNames.add(attachedFileName);
				attachedFiles.add(file);
				numberOfAttachments++;
			}

			/**
			 * Inline Images
			 */
			if ((disposition != null) && (disposition.equals(Part.INLINE))) {
				if (bodyPart.getContentType().contains("image/")) {
//					inlineFileName = saveImage(mimeBodyPart);
//					System.out.println(">>> 1) Attachment-Inline - Filename: " + inlineFileName);
					
					inlineFileName = mimeBodyPart.getContentID().replaceAll(">", "").replaceAll("<", "");
					File file = new File(downloadPathCID + inlineFileName);
					System.out.println(">>> 2bbb) Attachment-Inline - Content-ID: " + mimeBodyPart.getContentID());

					DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
					com.sun.mail.util.BASE64DecoderStream test = (com.sun.mail.util.BASE64DecoderStream) mimeBodyPart.getContent();
					byte[] buffer = new byte[1024];
					int bytesRead;
					while ((bytesRead = test.read(buffer)) != -1) {
						output.write(buffer, 0, bytesRead);
					}
					mimeBodyPart.saveFile(file);
					output.close();
					inlineFiles.add(file);
				}
			}
		}
	}

//	private String saveImage(MimeBodyPart p) throws MessagingException, IOException {
////		String tmp = p.getContentID().replaceAll(">", "").replaceAll("<", "");
////		File file = new File(downloadPathCID + p.getFileName());
//		String fileName = p.getContentID().replaceAll(">", "").replaceAll("<", "");
//		File file = new File(downloadPathCID + fileName);
//		System.out.println(">>> 2) Attachment-Inline - Content-ID: " + p.getContentID());
//
//		DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
//		com.sun.mail.util.BASE64DecoderStream test = (com.sun.mail.util.BASE64DecoderStream) p.getContent();
//		byte[] buffer = new byte[1024];
//		int bytesRead;
//		while ((bytesRead = test.read(buffer)) != -1) {
//			output.write(buffer, 0, bytesRead);
//		}
//		p.saveFile(file);
//		output.close();
//		return (file.getPath() + file.getName());
//	}

//	private static void attachments(final BodyPart body, final BiConsumer<String, InputStream> consumer)
//			throws MessagingException, IOException {
//		final Multipart content;
//		try {
//			content = (Multipart) body.getContent();
//			for (int i = 0; i < content.getCount(); i++) {
//				attachments(content.getBodyPart(i), consumer);
//			}
//			return;
//		} catch (final ClassCastException cce) {
//		}
//		if (!Part.ATTACHMENT.equalsIgnoreCase(body.getDisposition())) {
//			return;
//		}
//		final String name = body.getFileName();
//		if (name == null || name.trim().isEmpty()) {
//			return;
//		}
//		try (final InputStream stream = body.getInputStream()) {
//			consumer.accept(name, stream);
//		}
//	}

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

	public int getNumberOfAttachments() {
		return numberOfAttachments;
	}

	public void setNumberOfAttachments(int numberOfAttachments) {
		this.numberOfAttachments = numberOfAttachments;
	}

}