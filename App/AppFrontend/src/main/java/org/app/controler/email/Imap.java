package org.app.controler.email;

import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import org.app.controler.EmailService;
import org.app.controler.email.imap.ExtractContent;
import org.app.controler.email.imap.ExtractHeader;

public class Imap implements Const {

	private Store store;
	private Folder emailFolder;
	private ExtractHeader extractHeader;
	private ExtractContent extractContent;
	private PersistMail persistMessage;
	private MailServer mailServer;

	public void readFromImap(EmailService service) {
		mailServer = new MailServer();
		mailServer.initImap();
		
		

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

			// retrieve the messages from the folder in an array and print it
			Message[] messages = emailFolder.getMessages();

			for (int i = 0; i < messages.length; i++) {
				System.out.println("Message: " + i);
				Message message = messages[i];
				extractHeader = new ExtractHeader(message);
				extractContent = new ExtractContent(message, uidEmailFolder.getUID(message));
				persistMessage = new PersistMail();
				persistMessage.setImapMessageID(uidEmailFolder.getUID(message));
				persistMessage.saveImapToDatabase(message, service);
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