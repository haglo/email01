package org.app.view.email.send;

import java.util.HashSet;
import java.util.Set;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import org.app.controler.EmailService;
import org.app.controler.email.Const;
import org.app.controler.email.PersistMail;
import org.app.controler.email.Smtp;
import org.app.controler.email.imap.AIFile;
import org.app.controler.email.imap.AIFile.FILE_TYPE;
import org.app.controler.email.smtp.MailOut;
import org.app.helper.I18n;
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
	private PersistMail persistMessage;

	public WriteMail(EmailService service) {
		String uploadPath;
		subContent = new VerticalLayout();
		this.setWidth("50%");
		this.setCaption(i18n.EMAIL_NEW);
		this.setContent(subContent);
		this.center();

		i18n = new I18n();
		
		Smtp smtp = new Smtp();
		MailOut mailOut = new MailOut(smtp.getSession());
		uploadPath = MAIL_UPLOAD_PATH_ABSOLUT + mailOut.getSmtpMesssageID() + "/";
		uploadPath = uploadPath.replaceAll(">", "").replaceAll("<", "");
		UploadAttachedFiles uploadAttachedFiles = new UploadAttachedFiles(uploadPath);

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

			mailOut.setFrom("hans-georg.gloeckler@gmx.de");
			mailOut.setReplyTo("hans-georg.gloeckler@gmx.de");
			mailOut.setTo(txfTo.getValue());
			mailOut.setCc(txfCC.getValue());
			mailOut.setBcc(txfBC.getValue());
			mailOut.setSubject(txfSubject.getValue());
			mailOut.setHtmlContent(rta.getValue());
			mailOut.setAiFiles(uploadAttachedFiles.getAiFiles());

			try {
				smtp.send(mailOut);
				persistMessage = new PersistMail();
				persistMessage.saveSmtpToDatabase(mailOut.getMessage(), service);
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

}