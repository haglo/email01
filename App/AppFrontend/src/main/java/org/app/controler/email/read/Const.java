package org.app.controler.email.read;

public interface Const {

	public final String DOWNLOAD_PATH_ATTACHMENT = "C:/dev/wildfly/wildfly/ressources/attachments/";
	public final String DOWNLOAD_PATH_CID = "C:/dev/wildfly/wildfly/ressources/images/";
	public final ESECURITY EMAIL_SECURITY_LEVEL = ESECURITY.PLAIN_TEXT;

	enum ESECURITY {
		PLAIN_TEXT, HTML_TEXT;
	}
	
	
	public final String PATH_ATTACHMENT = "C:\\dev\\workspace\\email01\\EmailParser2\\ressources\\attachments\\";
	public final String PATH_INLINE_IMAGES = "C:\\dev\\workspace\\email01\\EmailParser2\\ressources\\images\\";
	
	public final String ORIG_EMAIL_FILE = "C:\\dev\\workspace\\email01\\EmailParser2\\ressources\\origEmail.eml";
	public final String NEW_EMAIL_FILE = "C:\\dev\\workspace\\email01\\EmailParser2\\ressources\\newEmail.eml";
	public final String HTML_CONTENT_00 = "C:\\dev\\workspace\\email01\\EmailParser2\\ressources\\html00.html";
	public final String HTML_CONTENT_01 = "C:\\dev\\workspace\\email01\\EmailParser2\\ressources\\html01.html";
	public final String HTML_CONTENT_02 = "C:\\dev\\workspace\\email01\\EmailParser2\\ressources\\html02.html";

}
