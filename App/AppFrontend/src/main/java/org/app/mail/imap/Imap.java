package org.app.mail.imap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.internet.MimeMessage;

import org.app.controler.EmailService;
import org.app.mail.common.Const;
import org.app.mail.common.ExtractContent;
import org.app.mail.common.ExtractHeader;
import org.app.mail.common.MailServer;
import org.app.mail.common.PersistMail;
import org.app.mail.common.PersistMail.MAIL_TYPE;
import org.app.model.entity.Pmail;

@Default
public class Imap implements Const {

	@Inject
	EmailService service;

	private Store store;
	private Folder emailFolder;
	private ExtractHeader extractHeader;
	private ExtractContent extractContent;
	private PersistMail persistMessage;
	private MailServer mailServer;

	public void readFromImap(EmailService service) {
		mailServer = new MailServer();

		try {
			Properties properties = new Properties();

			String imapHost = mailServer.getImapHost();
			String username = mailServer.getImapUsername();
			String password = mailServer.getImapPassword();
			Integer port = mailServer.getImapPort();
			boolean isSSL = mailServer.isImapSSL();

			properties.put("mail.imap.user", username);
			properties.put("mail.imap.host", imapHost);
			properties.put("mail.imap.port", port);
			properties.put("mail.imap.ssl.enable", isSSL);
			properties.put("mail.imap.auth", true);

			Session emailSession = Session.getDefaultInstance(properties);

			store = isSSL ? emailSession.getStore("imaps") : emailSession.getStore("imap");
			store.connect(imapHost, username, password);
			if (store.isConnected()) {
				System.out.println("Connect to Imap: true");
			}

			emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);
			UIDFolder uidEmailFolder = (UIDFolder) emailFolder;

			// retrieve the messages from the folder in an array
			Message[] messages = emailFolder.getMessages();

			List<Pmail> pmails = service.getPmailDAO().findAll();
			List<Long> list = new ArrayList<Long>();
			for (Pmail mail : pmails) {
				list.add(mail.getPimapID());
			}

			for (int i = 0; i < messages.length; i++) {
				Message message = messages[i];
				if (list.contains(uidEmailFolder.getUID(message))) {
					continue;
				}
				extractHeader = new ExtractHeader(message);
				extractContent = new ExtractContent(message, uidEmailFolder.getUID(message));
				persistMessage = new PersistMail();
				persistMessage.setImapID(uidEmailFolder.getUID(message));
				persistMessage.saveMail(message, service, MAIL_TYPE.IMAP);
			}
			// close the store and folder objects
			emailFolder.close(false);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}