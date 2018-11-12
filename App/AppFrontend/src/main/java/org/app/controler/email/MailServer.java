package org.app.controler.email;

import java.util.Properties;

public class MailServer {

	private String smtpSender;
	private String smtpUsername;
	private String smtpPassword;
	private String smtpHost;
	private Integer smtpPort;
	private boolean isSmtpSSL = true;
	private boolean isSmtpStartTls = true;
	private boolean isSmtpAuthentication = true;

	private String imapSender;
	private String imapUsername;
	private String imapPassword;
	private String imapHost;
	private Integer imapPort;
	private boolean isImapSSL = true;

	public void init() {

	}

	public void initImap() {
		setImapSender("benjamin_strobel@gimtex.de");
		setImapUsername("benjamin_strobel@gmx.de");
		setImapPassword("123atgfd");
		setImapHost("imap.gmx.net");
		setImapPort(993);
		setImapSSL(true);

	}

	public void initSmtp() {
		setSmtpSender("h.g.gloeckler@gmail.com");
		setSmtpUsername("h.g.gloeckler@gmail.com");
		setSmtpPassword("1234:Atgfd");
		setSmtpHost("smtp.gmail.com");
		setSmtpPort(587);
		setSmtpSSL(true);
		setSmtpStartTls(true);
		setSmtpAuthentication(true);

	}


	public String getSmtpSender() {
		return smtpSender;
	}

	public void setSmtpSender(String smtpSender) {
		this.smtpSender = smtpSender;
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

	public String getImapSender() {
		return imapSender;
	}

	public void setImapSender(String imapSender) {
		this.imapSender = imapSender;
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


	enum Mailprovider {
		GOOGLE, GMX, PRIVATE;
	}

}
