package org.app.controler.email;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.app.controler.EmailService;
import org.app.controler.email.imap.AIFile;
import org.app.controler.email.smtp.MailOut;

public class Smtp {

	private Session session;
	private MailServer mailServer;

	public Smtp() {
		
		mailServer = new MailServer();
		mailServer.initSmtp();


		String host = mailServer.getSmtpHost();
		String username = mailServer.getSmtpUsername();// change accordingly
		String password = mailServer.getSmtpPassword();// change accordingly
		Integer port = mailServer.getSmtpPort();
		boolean isSSL = mailServer.isSmtpSSL();
		boolean isStartTls = mailServer.isSmtpStartTls();
		boolean isSmtpAuth = mailServer.isSmtpAuthentication();

		/**
		 * https://javaee.github.io/javamail/docs/api/com/sun/mail/smtp/package-summary.html
		 */
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", isSmtpAuth);
		props.put("mail.smtp.starttls.enable", isStartTls);

		// Get the Session object.
		this.session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

	}

	public void send(MailOut mailOut) {
		// Send message
		try {
			Transport.send(mailOut.getMessage());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

}
