package org.app.mail.common;

public interface Const {
	
	public final String ATTACHMENT_DELIMITER = "|$||$|";
	public final String FOLDER_DELIMITER = "|$||$|";

	public final ESECURITY EMAIL_SECURITY_LEVEL = ESECURITY.PLAIN_TEXT;

	public final String MAIL_CONTENT_PATH = "C:/dev/wcontent/mail/"; // see standalone of wildfly
	
	public final String MAIL_STORAGE_PATH_ABSOLUT = MAIL_CONTENT_PATH;
	public final String MAIL_STORAGE_PATH_CANONICAL = "../my-content/";

	public final String MAIL_UPLOAD_PATH_ABSOLUT = MAIL_CONTENT_PATH + "uploads/";
	public final String MAIL_ATTACHMENTS_PATH_ABSOLUT = MAIL_CONTENT_PATH + "attachments/";
	public final String MAIL_INLINE_IMAGES_PATH_ABSOLUT =MAIL_CONTENT_PATH + "images/";

	public final String MAIL_UPLOAD_PATH_CANONICAL = MAIL_STORAGE_PATH_CANONICAL + "uploads/";
	public final String MAIL_ATTACHMENTS_PATH_CANONICAL = MAIL_STORAGE_PATH_CANONICAL + "attachments/";
	public final String MAIL_INLINE_IMAGES_PATH_CANONICAL = MAIL_STORAGE_PATH_CANONICAL + "images/";
	
	

	enum ESECURITY {
		PLAIN_TEXT, HTML_TEXT;
	}

}
