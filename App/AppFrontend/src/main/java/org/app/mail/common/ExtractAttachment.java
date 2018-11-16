package org.app.mail.common;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;

import org.app.mail.common.AIFile.FILE_TYPE;

public class ExtractAttachment implements Const {

	/**
	 * Both: Attached File and Inline Image
	 */
	private AIFile aiFile;
	private List<AIFile> aiFiles;
	private String id;

	public ExtractAttachment(String pathDef) {
		init();
		setId(pathDef);
	}

	private void init() {
		aiFile = new AIFile();
		aiFiles = new ArrayList<AIFile>();

	}

	public void extract(Part p) throws IOException, MessagingException {
		boolean inlineIsAttachment = false;

		String attachedFileId = ""; // never exists
		String attachedFileName = "";
		String attachedFilePath = MAIL_ATTACHMENTS_PATH_ABSOLUT + getId().toString() + "/";
		String attachedFileExtension = "";
		String attachedFileFullName = "";

		String inlineFileId = "";
		String inlineFileName = "";
		String inlineFileExtension = "";
		String inlineFileFullName = "";
		String disposition = p.getDisposition();
		String inlineFilePath = MAIL_INLINE_IMAGES_PATH_ABSOLUT + getId().toString() + "/";

		MimeBodyPart mimeBodyPart = (MimeBodyPart) p;

		/**
		 * Image as Attachment for Download
		 */
		if (disposition.equals(Part.ATTACHMENT) && (!p.getFileName().isEmpty())) {
			attachedFileName = p.getFileName();
//			attachedFileExtension = extractContentType(mimeBodyPart.getContentType().toLowerCase());
//			attachedFileFullName = attachedFilePath + attachedFileName + attachedFileExtension;
			attachedFileFullName = attachedFilePath + attachedFileName;

			File directory = new File(attachedFilePath);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			File file = new File(attachedFileFullName);

			InputStream is = p.getInputStream();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buf = new byte[4096];
			int bytesRead;
			while ((bytesRead = is.read(buf)) != -1) {
				fos.write(buf, 0, bytesRead);
			}
			fos.close();
			aiFile = new AIFile();
			aiFile.setFileId(attachedFileId);
			aiFile.setFileName(attachedFileName);
			aiFile.setFilePath(attachedFilePath);
			aiFile.setFileExtension(attachedFileExtension);
			aiFile.setFileFullName(attachedFileFullName);
			aiFile.setFileType(FILE_TYPE.ATTACHMENT);
			aiFile.setFileBody(file);
			aiFiles.add(aiFile);
		}

		/**
		 * Inline Images
		 */
		if (disposition.equals(Part.INLINE) && (!p.getFileName().isEmpty())) {
			inlineIsAttachment = false;
			File attachedFileDir = new File(attachedFilePath);
			if (!attachedFileDir.exists()) {
				attachedFileDir.mkdirs();
			}
			File inlineFileDir = new File(inlineFilePath);
			if (!inlineFileDir.exists()) {
				inlineFileDir.mkdirs();
			}

			try {
				inlineFileId = mimeBodyPart.getContentID().replaceAll(">", "").replaceAll("<", "");
				inlineFileExtension = extractContentType(mimeBodyPart.getContentType().toLowerCase());
				inlineFileFullName = inlineFilePath + inlineFileId + inlineFileExtension;
			} catch (Exception e) {
				// Image as Attachment for Download
				inlineFileName = p.getFileName();
				inlineFileFullName = attachedFilePath + inlineFileName + inlineFileExtension;
				inlineIsAttachment = true;
			}

			File file = new File(inlineFileFullName);
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
				aiFile = new AIFile();
				aiFile.setFileId(mimeBodyPart.getContentID().replaceAll(">", "").replaceAll("<", ""));
				aiFile.setFileName(inlineFileName);
				aiFile.setFilePath(attachedFilePath);
				aiFile.setFileExtension(inlineFileExtension);
				aiFile.setFileFullName(inlineFileFullName);
				aiFile.setFileType(FILE_TYPE.ATTACHMENT);
				aiFile.setFileBody(file);
				aiFiles.add(aiFile);
			} else {
				aiFile = new AIFile();
				aiFile.setFileId(mimeBodyPart.getContentID().replaceAll(">", "").replaceAll("<", ""));
				aiFile.setFileName(inlineFileName);
				aiFile.setFilePath(inlineFilePath);
				aiFile.setFileExtension(inlineFileExtension);
				aiFile.setFileFullName(inlineFileFullName);
				aiFile.setFileType(FILE_TYPE.INLINE);
				aiFile.setFileBody(file);
				aiFiles.add(aiFile);
			}

		}

	}

	private String extractContentType(String in) {
		String result = "";
		if (in.contains("image/png"))
			result = ".png";
		if (in.contains("image/jpg"))
			result = ".jpg";
		if (in.contains("image/jpeg"))
			result = ".jpeg";
		if (in.contains("image/gif"))
			result = ".gif";
		return result;

	}

	public AIFile getAiFile() {
		return aiFile;
	}

	public void setAiFile(AIFile aiFile) {
		this.aiFile = aiFile;
	}

	public List<AIFile> getAiFiles() {
		return aiFiles;
	}

	public void setAiFiles(List<AIFile> aiFiles) {
		this.aiFiles = aiFiles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



}