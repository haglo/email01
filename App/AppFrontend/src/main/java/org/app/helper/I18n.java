package org.app.helper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.mail.internet.AddressException;
import javax.mail.internet.MimeUtility;

public final class I18n {

	public static final String HELP_VIEW = "Help";
	public static final String EMAIL_VIEW = "Email";
	public static final String INBOX_MESSAGE_PLAIN_TEXT = "InboxMessagePlainText";
	public static final String INBOX_MESSAGE_HTML_TEXT = "InboxMessageHtmlText";
	public static final String INBOX_SUBJECT = "InboxSubject";

	public static final String EMAIL_CALL = "Call";
	public static final String EMAIL_WRITE = "Write";
	public static final String EMAIL_ANSWER = "Answer";
	public static final String EMAIL_FORWARD = "Forward";
	public static final String BASIC_PRINT = "Print";
	public static final String EMAIL_NEW = "New";
	public static final String EMAIL_SEND = "Send";
	public static final String EMAIL_ATTACHMENT = "Attachment";
	public static final String BASIC_DELETE = "Delete";
	public static final String EMAIL_INBOX = "Inbox";
	public static final String EMAIL_SENT = "Sent";
	public static final String EMAIL_TRASH = "Trash";
	public static final String EMAIL_ARCHIVE = "Archive";
	public static final String EMAIL_LOST = "Lost";
	public static final String EMAIL_SETTINGS = "Settings";
	public static final String EMAIL_TO = "To";
	public static final String EMAIL_CC = "CC";
	public static final String EMAIL_BC = "BCC";
	public static final String EMAIL_SUBJECT = "Subject";
	public static final String EMAIL_FROM = "From";
	public static final String EMAIL_SIGNATURE = "Signature";
	public static final String EMAIL_FAILURE = "Failure in reading value";

	public static String WINDOW_WIDTH = "";

	public static String decodeHeader(String s) {
		try {
			String str = MimeUtility.decodeText(s);
			return str;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return s;
		}
	}

	public static String encodeToBase64(String token) {
		String converted;
		converted = Base64.getEncoder().encodeToString(token.getBytes());
		return converted;
	}

	public static String decodeFromBase64(String token) {
		String decoded;
		try {
			byte[] asBytes = Base64.getDecoder().decode(token);
			decoded = new String(asBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return decoded;
	}

}
