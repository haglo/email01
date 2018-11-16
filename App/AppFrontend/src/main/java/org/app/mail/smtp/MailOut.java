package org.app.mail.smtp;

import java.util.Set;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.app.mail.common.AIFile;

public class MailOut {

	private String messsageID;
	private String inReplyToID;
	private String from;
	private String subject;
	private String to;
	private String cc;
	private String bcc;
	private String replyTo;
	private String htmlContent;
	private Set<AIFile> aiFiles;
	private MimeMessage message;
	private Session session;

	public MailOut() {
	}

	public MailOut(Session session) {
		setSession(session);
	}


	public String getMesssageID() {
		return messsageID;
	}


	public void setMesssageID(String messsageID) {
		this.messsageID = messsageID;
	}


	public String getInReplyToID() {
		return inReplyToID;
	}

	public void setInReplyToID(String inReplyToID) {
		this.inReplyToID = inReplyToID;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public Set<AIFile> getAiFiles() {
		return aiFiles;
	}

	public void setAiFiles(Set<AIFile> aiFiles) {
		this.aiFiles = aiFiles;
	}

	public MimeMessage getMessage() {
		return createMimeMessage();
	}

	public void setMessage(MimeMessage message) {
		this.message = message;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public MimeMessage createMimeMessage() {
		MimeMessage mimeMessage;
		MimeMultipart multipart = new MimeMultipart();
		MimeBodyPart messageBodyPart = new MimeBodyPart();

		try {
			mimeMessage = new MimeMessage(getSession());
			mimeMessage.setFrom(new InternetAddress(getFrom()));
			if (this.getTo() != null && !this.getTo().trim().isEmpty())
				mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(this.getTo(), true));
			if (this.getCc() != null && !this.getCc().trim().isEmpty())
				mimeMessage.addRecipients(Message.RecipientType.CC, InternetAddress.parse(this.getCc(), true));
			if (this.getBcc() != null && !this.getBcc().trim().isEmpty())
				mimeMessage.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(this.getBcc(), true));
			if (this.getSubject() != null && !this.getSubject().trim().isEmpty())
				mimeMessage.setSubject(this.getSubject());

			// HTML Text
			if (this.getHtmlContent() != null && !this.getHtmlContent().trim().isEmpty()) {
				messageBodyPart.setContent(this.getHtmlContent(), "text/html");
				multipart.addBodyPart(messageBodyPart);
			}

			// With Attachment
			if (!this.getAiFiles().isEmpty()) {
				for (AIFile tmp : this.getAiFiles()) {
					FileDataSource fileDataSource = new FileDataSource(tmp.getFileFullName());
					messageBodyPart = new MimeBodyPart();
					messageBodyPart.setDataHandler(new DataHandler(fileDataSource));
					messageBodyPart.setFileName(tmp.getFileName());
//					messageBodyPart.setHeader("Content-ID", "<image>");
					multipart.addBodyPart(messageBodyPart);
				}
			}

			mimeMessage.setContent(multipart); 
			mimeMessage.saveChanges();
			messsageID = mimeMessage.getMessageID();
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

//		setMessage(mimeMessage);
		return mimeMessage;
		
	}

}
