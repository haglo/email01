package org.app.view.mail.send;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.app.mail.common.AIFile;
import org.app.mail.common.Const;
import org.app.mail.common.AIFile.FILE_TYPE;

import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

public class UploadAttachedFiles extends CustomComponent implements Const {
	private static final long serialVersionUID = -4292553844521293140L;

	private String uploadPath;
	private Set<AIFile> aiFiles;
	private UUID uuid;

	public UploadAttachedFiles() {
		uuid = UUID.randomUUID();
		aiFiles = new HashSet<AIFile>();
		uploadPath = MAIL_UPLOAD_PATH_ABSOLUT + "_tmp/" + uuid.toString() + "/";
		VerticalLayout layout = new VerticalLayout();
		init(layout);
		setCompositionRoot(layout);
	}

	private void init(VerticalLayout layout) {
		// BEGIN-EXAMPLE: component.upload.basic
		// Show uploaded file in this placeholder
//		final Image image = new Image("Uploaded Image");
//		image.setVisible(false);
		Label success = new Label();

		// Implement both receiver that saves upload in a file and
		// listener for successful upload
		class ImageReceiver implements Receiver, SucceededListener {
			private static final long serialVersionUID = -1276759102490466761L;

			public File file;
			private int ctr = 0;

			public OutputStream receiveUpload(String filename, String mimeType) {
				// Create upload stream
				FileOutputStream fos = null; // Stream to write to
				try {
					// Open the file for writing.
					file = new File(uploadPath + filename);
					fos = new FileOutputStream(file);
				} catch (final java.io.FileNotFoundException e) {
					new Notification("Could not open file ", e.getMessage(), Notification.Type.ERROR_MESSAGE)
							.show(Page.getCurrent());
					return null;
				}
				return fos; // Return the output stream to write to
			}

			public void uploadSucceeded(SucceededEvent event) {
				String tmp;
				ctr++;
				if (ctr == 1) {
					tmp = "Upload done: " + file.getName();
				} else {
					tmp = ", " + file.getName();
				}

				success.setValue(success.getValue() + tmp);
			}
		}
		ImageReceiver receiver = new ImageReceiver();

		// Create the upload with a caption and set receiver later
		final Upload upload = new Upload("", receiver);
		upload.setButtonCaption("Upload");
		upload.addSucceededListener(receiver);

		// Prevent too big downloads
		final long UPLOAD_LIMIT = 1000000l;
		upload.addStartedListener(new StartedListener() {
			private static final long serialVersionUID = 4728847902678459488L;

			@Override
			public void uploadStarted(StartedEvent event) {
				if (event.getContentLength() > UPLOAD_LIMIT) {
					Notification.show("Too big file to upload", Notification.Type.ERROR_MESSAGE);
					upload.interruptUpload();
				}
			}
		});

		// Check the size also during progress
		upload.addProgressListener(new ProgressListener() {
			private static final long serialVersionUID = 8587352676703174995L;

			@Override
			public void updateProgress(long readBytes, long contentLength) {
				if (readBytes > UPLOAD_LIMIT) {
					Notification.show("Too big file", Notification.Type.ERROR_MESSAGE);
					upload.interruptUpload();
				}
			}
		});

		upload.addSucceededListener(event -> {
			AIFile aiFile = new AIFile();
			aiFile.setFileExtension(event.getMIMEType());
			aiFile.setFileName(event.getFilename());
			aiFile.setFilePath(uploadPath);
			aiFile.setFileFullName(uploadPath + event.getFilename());
			aiFile.setFileType(FILE_TYPE.ATTACHMENT);
			aiFiles.add(aiFile);
		});

		VerticalLayout panelContent = new VerticalLayout();
		panelContent.setMargin(true);
		panelContent.addComponents(upload, success);

		// Create uploads directory
		File uploads = new File(uploadPath);

		if (!uploads.exists() && !uploads.mkdirs())
			layout.addComponent(new Label("ERROR: Could not create upload dir " + uploads.getAbsolutePath()));

		layout.addComponent(panelContent);
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public Set<AIFile> getAiFiles() {
		return aiFiles;
	}

	public void setAiFiles(Set<AIFile> aiFiles) {
		this.aiFiles = aiFiles;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

}