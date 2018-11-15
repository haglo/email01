package org.app.mail.common;

import java.util.Properties;

public class MailServer {

	private String smtpUsername;
	private String smtpPassword;
	private String smtpReplyTo;
	private String smtpHost;
	private Integer smtpPort;
	private boolean isSmtpSSL = true;
	private boolean isSmtpStartTls = true;
	private boolean isSmtpAuthentication = true;

	private String imapReplyTo;
	private String imapUsername;
	private String imapPassword;
	private String imapHost;
	private Integer imapPort;
	private boolean isImapSSL = true;

	public MailServer() {
		initImap();
		initSmtp();

	}

	private void initImap() {
		setImapReplyTo("benjamin_strobel@gmx.de");
		setImapUsername("benjamin_strobel@gmx.de");
		setImapPassword("123atgfd");
		setImapHost("imap.gmx.net");
		setImapPort(993);
		setImapSSL(true);

	}

	private void initSmtp() {
//		setSmtpReplyTo("h.g.gloeckler@gmail.com");
//		setSmtpUsername("h.g.gloeckler@gmail.com");
//		setSmtpPassword("1234:Atgfd");
//		setSmtpHost("smtp.gmail.com");
//		setSmtpPort(587);
//		setSmtpSSL(true);
//		setSmtpStartTls(true);
//		setSmtpAuthentication(true);
		
		setSmtpReplyTo("benjamin_strobel@gmx.de");
		setSmtpUsername("benjamin_strobel@gmx.de");
		setSmtpPassword("123atgfd");
		setSmtpHost("mail.gmx.net");
		setSmtpPort(465);
		setSmtpSSL(true);
		setSmtpStartTls(true);
		setSmtpAuthentication(true);

	}



	public String getSmtpUsername() {
		return smtpUsername;
	}

	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public Integer getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}

	public boolean isSmtpSSL() {
		return isSmtpSSL;
	}

	public void setSmtpSSL(boolean isSmtpSSL) {
		this.isSmtpSSL = isSmtpSSL;
	}

	public boolean isSmtpStartTls() {
		return isSmtpStartTls;
	}

	public void setSmtpStartTls(boolean isSmtpStartTls) {
		this.isSmtpStartTls = isSmtpStartTls;
	}

	public boolean isSmtpAuthentication() {
		return isSmtpAuthentication;
	}

	public void setSmtpAuthentication(boolean isSmtpAuthentication) {
		this.isSmtpAuthentication = isSmtpAuthentication;
	}

	public String getImapUsername() {
		return imapUsername;
	}

	public void setImapUsername(String imapUsername) {
		this.imapUsername = imapUsername;
	}

	public String getImapPassword() {
		return imapPassword;
	}

	public void setImapPassword(String imapPassword) {
		this.imapPassword = imapPassword;
	}

	public String getImapHost() {
		return imapHost;
	}

	public void setImapHost(String imapHost) {
		this.imapHost = imapHost;
	}

	public Integer getImapPort() {
		return imapPort;
	}

	public void setImapPort(Integer imapPort) {
		this.imapPort = imapPort;
	}

	public boolean isImapSSL() {
		return isImapSSL;
	}

	public void setImapSSL(boolean isImapSSL) {
		this.isImapSSL = isImapSSL;
	}


	public String getSmtpReplyTo() {
		return smtpReplyTo;
	}

	public void setSmtpReplyTo(String smtpReplyTo) {
		this.smtpReplyTo = smtpReplyTo;
	}

	public String getImapReplyTo() {
		return imapReplyTo;
	}

	public void setImapReplyTo(String imapReplyTo) {
		this.imapReplyTo = imapReplyTo;
	}


	enum Mailprovider {
		GOOGLE, GMX, PRIVATE;
	}

}
