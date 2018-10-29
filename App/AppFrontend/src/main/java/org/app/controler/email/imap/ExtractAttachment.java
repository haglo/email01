package org.app.controler.email.imap;

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
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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

import org.app.controler.email.Const;
import org.app.helper.I18n;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class ExtractAttachment implements Const{

	private List<File> attachedFiles;
	private List<String> attachedFileNames;
	private List<File> inlineFiles;
	private List<String> inlineFileNames;

	public ExtractAttachment() {
		init();
	}

	private void init() {
		attachedFiles = new ArrayList<File>();
		attachedFileNames = new ArrayList<String>();
		inlineFiles = new ArrayList<File>();
		inlineFileNames = new ArrayList<String>();
	}

	public void extract(Part p) throws IOException, MessagingException {
		boolean inlineIsAttachment = false;
		String attachedFileName = "";
		String inlineFileName = "";
		String imgFileName = "";
		DataSource fds = null;
		String disposition = p.getDisposition();

		MimeBodyPart mimeBodyPart = (MimeBodyPart) p;

		/**
		 * Image as Attachment for Download
		 */
		if (disposition.equals(Part.ATTACHMENT) && (!p.getFileName().isEmpty())) {
			attachedFileName = p.getFileName();
			File file = new File(PATH_ATTACHMENT + attachedFileName);
			System.out.println("$$$ 1bbb) Attachment-Standard - Filename: " + attachedFileName);

			InputStream is = p.getInputStream();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buf = new byte[4096];
			int bytesRead;
			while ((bytesRead = is.read(buf)) != -1) {
				fos.write(buf, 0, bytesRead);
			}
			fos.close();
			attachedFileNames.add(attachedFileName);
			attachedFiles.add(file);
		}

		/**
		 * Inline Images
		 */
		if (disposition.equals(Part.INLINE) && (!p.getFileName().isEmpty())) {
			try {
				inlineFileName = mimeBodyPart.getContentID().replaceAll(">", "").replaceAll("<", "");
				imgFileName = PATH_INLINE_IMAGES + inlineFileName;
				inlineFileNames.add(inlineFileName);
			} catch (Exception e) {
				//Image as Attachment for Download
				inlineFileName = p.getFileName();
				imgFileName = PATH_ATTACHMENT + inlineFileName;
				attachedFileNames.add(imgFileName);
				inlineIsAttachment = true;
			}
			
			fds = new FileDataSource(imgFileName);
			File file = new File(imgFileName);

			DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
			com.sun.mail.util.BASE64DecoderStream test = (com.sun.mail.util.BASE64DecoderStream) mimeBodyPart
					.getContent();
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = test.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
			mimeBodyPart.saveFile(file);
			output.close();
			if (inlineIsAttachment) {
				attachedFiles.add(file);
			} else {
				inlineFiles.add(file);
			}
			
		}

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

}