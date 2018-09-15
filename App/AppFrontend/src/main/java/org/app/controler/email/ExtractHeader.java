package org.app.controler.email;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import org.app.helper.I18n;

public class ExtractHeader {

	private String from;
	private String subject;
	private String to;
	private String cc;
	private String bcc;
	private String replyTo;
	private String sendDate;
	private String flags;

	public ExtractHeader(Message msg) {
		try {
			extractFrom(msg);
			extractReplyTo(msg);
			extractSubject(msg);
			extractTO(msg);
			extractCC(msg);
			extractBCC(msg);
			extractSendDate(msg);
			extractFlags(msg);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private void extractFrom(Message message) {
		String result = "";
		try {
			result = MimeUtility.decodeText(message.getFrom()[0].toString());
			result = MimeUtility.decodeText(message.getSubject());
		} catch (Exception e) {
			result = I18n.EMAIL_FAILURE;
		}
		setFrom(result);
	}

	private void extractReplyTo(Message message) {
		String result = "";
		try {
			result = message.getReplyTo().toString();
		} catch (Exception e) {
			result = I18n.EMAIL_FAILURE;
		}
		setReplyTo(result);
	}

	private void extractSubject(Message message) {
		String result = "";
		try {
			result = MimeUtility.decodeText(message.getSubject());
		} catch (Exception e) {
			result = I18n.EMAIL_FAILURE;
		}
		setSubject(result);
	}

	private void extractTO(Message message) {
		String result = "";
		try {
			result = MimeUtility.decodeText(InternetAddress.toString(message.getRecipients(Message.RecipientType.TO)));
		} catch (Exception e) {
			result = I18n.EMAIL_FAILURE;
		}
		setTo(result);
	}

	private void extractCC(Message message) {
		String result = "";
		try {
			result = MimeUtility.decodeText(InternetAddress.toString(message.getRecipients(Message.RecipientType.CC)));
		} catch (Exception e) {
			result = I18n.EMAIL_FAILURE;
		}
		setCc(result);
	}

	private void extractBCC(Message message) {
		String result = "";
		try {
			result = MimeUtility.decodeText(InternetAddress.toString(message.getRecipients(Message.RecipientType.BCC)));
		} catch (Exception e) {
			result = I18n.EMAIL_FAILURE;
		}
		setBcc(result);
	}

	private void extractSendDate(Message message) {
		Date date;
		String result = "";
		try {
			date = message.getSentDate();
			result = (date != null ? date.toString() : "UNKNOWN");
		} catch (Exception e) {
			result = I18n.EMAIL_FAILURE;
		}
		setSendDate(result);
	}

	private void extractFlags(Message message) throws MessagingException {
		// FLAGS
		Flags flags = message.getFlags();
		StringBuffer sb = new StringBuffer();
		Flags.Flag[] sf = flags.getSystemFlags(); // get the system flags

		boolean first = true;
		for (int i = 0; i < sf.length; i++) {
			String s;
			Flags.Flag f = sf[i];
			if (f == Flags.Flag.ANSWERED)
				s = "\\Answered";
			else if (f == Flags.Flag.DELETED)
				s = "\\Deleted";
			else if (f == Flags.Flag.DRAFT)
				s = "\\Draft";
			else if (f == Flags.Flag.FLAGGED)
				s = "\\Flagged";
			else if (f == Flags.Flag.RECENT)
				s = "\\Recent";
			else if (f == Flags.Flag.SEEN)
				s = "\\Seen";
			else
				continue; // skip it
			if (first)
				first = false;
			else
				sb.append(' ');
			sb.append(s);
		}

		String[] uf = flags.getUserFlags(); // get the user flag strings
		for (int i = 0; i < uf.length; i++) {
			if (first)
				first = false;
			else
				sb.append(' ');
			sb.append(uf[i]);
		}

		setFlags(sb.toString());
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

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getFlags() {
		return flags;
	}

	public void setFlags(String flags) {
		this.flags = flags;
	}

}