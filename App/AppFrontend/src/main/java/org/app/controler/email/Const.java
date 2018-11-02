package org.app.controler.email;

public interface Const {

//	public final String DOWNLOAD_PATH_ATTACHMENT = "C:/dev/wildfly/wildfly/ressources/attachments/";
//	public final String DOWNLOAD_PATH_CID = "C:/dev/wildfly/wildfly/ressources/images/";
	public final ESECURITY EMAIL_SECURITY_LEVEL = ESECURITY.PLAIN_TEXT;

	enum ESECURITY {
		PLAIN_TEXT, HTML_TEXT;
	}
	
	
//	public final String PATH_ATTACHMENT = "C:\\dev\\workspace\\email01\\EmailParser2\\ressources\\attachments\\";
//	public final String PATH_INLINE_IMAGES = "C:\\dev\\workspace\\email01\\EmailParser2\\ressources\\images\\";

	public final String MAIL_CONTENT_PATH = "C:/dev/wcontent/";	//see standalone of wildfly
	public final String MAIL_STORAGE_PATH_ABSOLUT = MAIL_CONTENT_PATH + "mail/";
	public final String MAIL_STORAGE_PATH_CANONICAL = "../my-content/";
	
	public final String MAIL_ATTACHMENTS_PATH_ABSOLUT = MAIL_STORAGE_PATH_ABSOLUT + "attachments/";
	public final String MAIL_INLINE_IMAGES_PATH_ABSOLUT = MAIL_STORAGE_PATH_ABSOLUT + "images/";

	public final String MAIL_ATTACHMENTS_PATH_CANONICAL = MAIL_STORAGE_PATH_CANONICAL + "mail/attachments/";
	public final String MAIL_INLINE_IMAGES_PATH_CANONICAL = MAIL_STORAGE_PATH_CANONICAL + "mail/images/";

//	public final String PATH_ATTACHMENT = MAIL_STORAGE_PATH + "attachments/";
//	public final String PATH_INLINE_IMAGES = MAIL_STORAGE_PATH + "images/";
	
//	public final String ORIG_EMAIL_FILE = "C:\\dev\\workspace\\email01\\EmailParser2\\ressources\\origEmail.eml";
//	public final String NEW_EMAIL_FILE = "C:\\dev\\workspace\\email01\\EmailParser2\\ressources\\newEmail.eml";
//	public final String HTML_CONTENT_00 = "C:\\dev\\workspace\\email01\\EmailParser2\\ressources\\html00.html";
//	public final String HTML_CONTENT_01 = "C:\\dev\\workspace\\email01\\EmailParser2\\ressources\\html01.html";
//	public final String HTML_CONTENT_02 = "C:\\dev\\workspace\\email01\\EmailParser2\\ressources\\html02.html";

}
