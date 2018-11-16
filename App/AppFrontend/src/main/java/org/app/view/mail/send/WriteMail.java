package org.app.view.mail.send;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.MimeMessage;

import org.app.controler.EmailService;
import org.app.helper.I18n;
import org.app.helper.d;
import org.app.mail.common.AIFile;
import org.app.mail.common.Const;
import org.app.mail.common.MailServer;
import org.app.mail.common.PersistMail;
import org.app.mail.common.AIFile.FILE_TYPE;
import org.app.mail.common.PersistMail.MAIL_TYPE;
import org.app.mail.smtp.MailOut;
import org.app.mail.smtp.Smtp;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Window to write emails
 * 
 * @author haglo
 *
 */
@SuppressWarnings("serial")
public class WriteMail extends Window implements Const {

	private I18n i18n;
	private VerticalLayout subContent;
	private CssLayout bottomBar;
	private VerticalLayout mainContent;
	private UploadAttachedFiles uploadAttachedFiles;
	private MailOut mailOut;

	public WriteMail(EmailService service) {

		d.time(0);
		subContent = new VerticalLayout();
		this.setWidth("50%");
		this.setCaption(i18n.EMAIL_NEW);
		this.setContent(subContent);
		this.center();

		i18n = new I18n();

		d.time(1);
		MailServer mailServer = new MailServer();
		Smtp smtp = new Smtp();
		mailOut = new MailOut(smtp.getSession());
		uploadAttachedFiles = new UploadAttachedFiles();

		d.time(2);

		mainContent = new VerticalLayout();
		mainContent.setSizeFull();
		bottomBar = new CssLayout();
		bottomBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		TextField txfTo = new TextField();
		txfTo.setValue("h.g.gloeckler@gmx.de");
		TextField txfCC = new TextField();
		txfCC.setValue("h.g.gloeckler@gmail.com");
		TextField txfBC = new TextField();
		txfBC.setValue("hans-georg.gloeckler@uni-ulm.de, hans-georg.gloeckler@gimtex.de");
		TextField txfSubject = new TextField();
		txfSubject.setValue("Test-Email von Pilgerapp");
		RichTextArea rta = new RichTextArea();
		rta.setValue("<b>Hallo Welt with HTML bold<b>");
		rta.setSizeFull();

		Button sendButton = new Button(i18n.EMAIL_SEND, ev -> {

			mailOut.setFrom(mailServer.getSmtpUsername());
			mailOut.setReplyTo(mailServer.getSmtpReplyTo());
			mailOut.setTo(txfTo.getValue());
			mailOut.setCc(txfCC.getValue());
			mailOut.setBcc(txfBC.getValue());
			mailOut.setSubject(txfSubject.getValue());
			mailOut.setHtmlContent(rta.getValue());
			mailOut.setAiFiles(uploadAttachedFiles.getAiFiles());

			try {
				smtp.send(mailOut.getMessage());
				MimeMessage m = mailOut.getMessage();
//				MailOut mo = modifyMailOut(mailOut);
				PersistMail persistMail = new PersistMail();
				persistMail.saveMail(mailOut.getMessage(), service, MAIL_TYPE.SMTP);
//				moveUploadFolder();
				Notification.show("Send success");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Notification.show("Send Error");
			} finally {
				getUI().getCurrent().removeWindow(this);
			}

		});

		sendButton.setIcon(VaadinIcons.LOCATION_ARROW_CIRCLE_O);

		mainContent.addComponent(txfTo);
		mainContent.addComponent(txfCC);
		mainContent.addComponent(txfBC);
		mainContent.addComponent(txfSubject);
		mainContent.addComponent(rta);

		bottomBar.addComponent(sendButton);
		bottomBar.addComponent(uploadAttachedFiles);

		subContent.addComponent(mainContent);
		subContent.addComponent(bottomBar);

	}

//	private void moveUploadFolder() {
//
//	}
//
//	private MimeMessage modifyMailOut(MailOut in) {
//		Set<AIFile> tmp = new HashSet<AIFile>();
//		MailOut newMailOut = new MailOut();
//		String old = "_tmp/" + uploadAttachedFiles.getUuid().toString();
//		String rep = in.getMesssageID();
//		rep = rep.replaceAll(">", "").replaceAll("<", "");
//		for (AIFile ai : in.getAiFiles()) {
//			ai.setFilePath(ai.getFilePath().replaceAll(old, rep));
//			System.out.println("FilePath: " + ai.getFilePath());
//			ai.setFileName(ai.getFileFullName().replaceAll(old, rep));
//			tmp.add(ai);
//		}
//		newMailOut = in;
//		newMailOut.setAiFiles(tmp);
//		return newMailOut;
//	}

}