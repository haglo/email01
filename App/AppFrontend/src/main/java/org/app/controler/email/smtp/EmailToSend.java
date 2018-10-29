package org.app.controler.email.smtp;

import java.util.Set;

import javax.activation.FileDataSource;

public class EmailToSend {

	private String sendTo;
	private String sendToCC;
	private String sendToBC;
	private String subject;
	private String text;
	private String textHTML;
	private Set<FileDataSource> attachments;
	private boolean withAttachment;

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo.trim();
	}

	public String getSendToCC() {
		return sendToCC;
	}

	public void setSendToCC(String sendToCC) {
		this.sendToCC = sendToCC.trim();
	}

	public String getSendToBC() {
		return sendToBC;
	}

	public void setSendToBC(String sendToBC) {
		this.sendToBC = sendToBC.trim();
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTextHTML() {
		return textHTML;
	}

	public void setTextHTML(String textHTML) {
		this.textHTML = textHTML;
	}

	public void setWithAttachment(boolean withAttachment) {
		this.withAttachment = withAttachment;
	}


	public boolean isWithAttachment() {
		return withAttachment;
	}

	public Set<FileDataSource> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<FileDataSource> attachments) {
		this.attachments = attachments;
	}


}
