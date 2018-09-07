package org.app.controler.email;

import java.util.Properties;

public class MailServer {

	private String sender;
	private String username;
	private String password;
	private String smtpHost;
	private String smtpPort;
	private String imapHost;
	private String imapPort;
	private Properties properties;

//	public void init(Mailprovider provider) {
	public void init() {

//		if (provider.GOOGLE != null) {
//			setSender("h.g.gloeckler@gmail.com");
//			setUsername("h.g.gloeckler@gmail.com");
//			setPassword("1234:Atgfd");
//			setSmtpHost("smtp.gmail.com");
//			setSmtpPort("587");
//			setImapHost("imap.gmail.com");
//			setImapPort("993");
//
//			properties.put("mail.smtp.auth", "true");
//			properties.put("mail.smtp.starttls.enable", "true");
//			properties.put("mail.smtp.host", smtpHost);
//			properties.put("mail.smtp.port", smtpPort);
//
//			properties.put("mail.imap.user", username);
//			properties.put("mail.imap.host", imapHost);
//			properties.put("mail.imap.port", imapPort);
//			properties.put("mail.imap.ssl.enable", true);
//			properties.put("mail.store.protocol", "imaps");
//		}
//		
//		if (provider.PRIVATE != null) {
		setSender("hans-georg.gloeckler@gimtex.de");
		setUsername("hans-georg.gloeckler@gimtex.de");
		setPassword("1234:Atgfd");
		setSmtpHost("195.201.215.12");
		setSmtpPort("587");
		setImapHost("195.201.215.12");
		setImapPort("143");

		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", false);
		properties.put("mail.smtp.host", smtpHost);
		properties.put("mail.smtp.port", smtpPort);
		properties.put("mail.imap.user", username);
		properties.put("mail.imap.host", imapHost);
		properties.put("mail.imap.port", imapPort);

		properties.put("mail.imap.ssl.enable", false);
		properties.put("mail.imap.starttls.enable", true);
		properties.put("mail.store.protocol", "imap");
//		}

	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		smtpPort = smtpPort;
	}

	public String getImapPort() {
		return imapPort;
	}

	public void setImapPort(String imapPort) {
		imapPort = imapPort;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getImapHost() {
		return imapHost;
	}

	public void setImapHost(String imapHost) {
		this.imapHost = imapHost;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		properties = properties;
	}

	enum Mailprovider {
		GOOGLE, GMX, PRIVATE;
	}

}
