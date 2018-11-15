package org.app.mail.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import org.app.helper.I18n;

public class ExtractHeader {

	private String MesssageID;
	private String InReplyToID;
	private String[] from;
	private String subject;
	private String[] to;
	private String[] cc;
	private String[] bcc;
	private String[] replyTo;
	private String sendDate;
	private String receiveDate;
	private String flags;

	public ExtractHeader(Message msg) {
		try {
			init();
			extractHeader(msg);
			extractFrom(msg);
			extractReplyTo(msg);
			extractSubject(msg);
			extractTO(msg);
			extractCC(msg);
			extractBCC(msg);
			extractSendDate(msg);
			extractReceiveDate(msg);
			extractFlags(msg);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		setMesssageID("");
		setInReplyToID("");
		setSubject("");
		setSendDate("");
		setReceiveDate("");
		setFlags("");
	}

	private void extractHeader(Message message) {
		setMesssageID("");
		setInReplyToID("");
		Enumeration<Header> headers;
		try {
			headers = message.getAllHeaders();
			while (headers.hasMoreElements()) {
				Header header = (Header) headers.nextElement();
				String mID = header.getName();
				if (mID.contains("Message-ID")) {
					setMesssageID(header.getValue());
				}
				if (mID.contains("In-Reply-To")) {
					setInReplyToID(header.getValue());
				}
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void extractFrom(Message message) {
		Address[] a;
		String[] result;
		try {
			a = message.getFrom();
			result = new String[a.length];
			for (int i = 0; i < a.length; i++)
				result[i] = MimeUtility.decodeText(InternetAddress.toString(a));
		} catch (Exception e) {
			result = new String[1];
			result[0] = "";
		}
		setFrom(result);
	}

	private void extractReplyTo(Message message) {
		Address[] a;
		String[] result;
		try {
			a = message.getReplyTo();
			result = new String[a.length];
			for (int i = 0; i < a.length; i++)
				result[i] = MimeUtility.decodeText(InternetAddress.toString(a));
		} catch (Exception e) {
			result = new String[1];
			result[0] = "";
		}
		setReplyTo(result);
	}

	private void extractSubject(Message message) {
		String result = "";
		try {
			result = MimeUtility.decodeText(message.getSubject());
		} catch (Exception e) {
			result = "";
		}
		setSubject(result);
	}

	private void extractTO(Message message) {
		Address[] a;
		String[] result;
		try {
			a = message.getRecipients(Message.RecipientType.TO);
			result = new String[a.length];
			for (int i = 0; i < a.length; i++)
				result[i] = MimeUtility.decodeText(InternetAddress.toString(a));
		} catch (Exception e) {
			result = new String[1];
			result[0] = "";
		}

		setTo(result);
	}

	private void extractCC(Message message) {
		Address[] a;
		String[] result;
		try {
			a = message.getRecipients(Message.RecipientType.CC);
			result = new String[a.length];
			for (int i = 0; i < a.length; i++)
				result[i] = MimeUtility.decodeText(InternetAddress.toString(a));
		} catch (Exception e) {
			result = new String[1];
			result[0] = "";
		}

		setCc(result);
	}

	private void extractBCC(Message message) {
		Address[] a;
		String[] result;
		try {
			a = message.getRecipients(Message.RecipientType.BCC);
			result = new String[a.length];
			for (int i = 0; i < a.length; i++)
				result[i] = MimeUtility.decodeText(InternetAddress.toString(a));
		} catch (Exception e) {
			result = new String[1];
			result[0] = "";
		}

		setBcc(result);
	}

	private void extractSendDate(Message message) {
		Date date;
		String result = "";
		try {
			date = message.getSentDate();
			result = date.toString();
		} catch (Exception e) {
			result = "";
		}
		setSendDate(result);
	}

	private void extractReceiveDate(Message message) {
		Date date;
		String result = "";
		try {
			date = message.getReceivedDate();
			result = date.toString();
		} catch (Exception e) {
			result = "";
		}
		setReceiveDate(result);
	}

	private void extractFlags(Message message) throws MessagingException {
		// FLAGS
		Flags flags = message.getFlags();
		StringBuffer sb = new StringBuffer();

		// Systemflags
		Flags.Flag[] sf = flags.getSystemFlags();

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

		// Userflags
		String[] uf = flags.getUserFlags();
		for (int i = 0; i < uf.length; i++) {
			if (first) {
				first = false;
			} else {
				sb.append(' ');
			}

			sb.append(uf[i]);
		}

		setFlags(sb.toString());
	}

	public String getMesssageID() {
		return MesssageID;
	}

	public void setMesssageID(String messsageID) {
		MesssageID = messsageID;
	}

	public String getInReplyToID() {
		return InReplyToID;
	}

	public void setInReplyToID(String inReplyToID) {
		InReplyToID = inReplyToID;
	}

	public String[] getFrom() {
		return from;
	}

	public void setFrom(String[] from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String[] getTo() {
		return to;
	}

	public void setTo(String[] to) {
		this.to = to;
	}

	public String[] getCc() {
		return cc;
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public String[] getBcc() {
		return bcc;
	}

	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}

	public String[] getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String[] replyTo) {
		this.replyTo = replyTo;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getFlags() {
		return flags;
	}

	public void setFlags(String flags) {
		this.flags = flags;
	}

}