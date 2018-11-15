package org.app.view.mail.send;

import java.util.HashSet;
import java.util.Set;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

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

	public WriteMail(EmailService service) {

		String uploadPath;
		d.time(0);
		subContent = new VerticalLayout();
		this.setWidth("50%");
		this.setCaption(i18n.EMAIL_NEW);
		this.setContent(subContent);
		this.center();

		i18n = new I18n();
		
		MailServer mailServer = new MailServer();
		d.time(1);
		Smtp smtp = new Smtp();
		MailOut mailOut = new MailOut();
		uploadPath = MAIL_UPLOAD_PATH_ABSOLUT + mailOut.getMesssageID() + "/";
		uploadPath = uploadPath.replaceAll(">", "").replaceAll("<", "");
		UploadAttachedFiles uploadAttachedFiles = new UploadAttachedFiles(uploadPath);
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

//			mailOut.setFrom(mailServer.getSmtpUsername());
//			mailOut.setReplyTo(mailServer.getSmtpReplyTo());
//			mailOut.setTo(txfTo.getValue());
//			mailOut.setCc(txfCC.getValue());
//			mailOut.setBcc(txfBC.getValue());
//			mailOut.setSubject(txfSubject.getValue());
//			mailOut.setHtmlContent(rta.getValue());
//			mailOut.setAiFiles(uploadAttachedFiles.getAiFiles());

//			try {
//				Smtp smtp = new Smtp();
//				smtp.send(mailOut);
//				PersistMail persistMail = new PersistMail();
//				persistMail.saveMail(mailOut.getMessage(), service, MAIL_TYPE.SMTP);
//				Notification.show("Send success");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				Notification.show("Send Error");
//			} finally {
//				getUI().getCurrent().removeWindow(this);
//			}

		});

		sendButton.setIcon(VaadinIcons.LOCATION_ARROW_CIRCLE_O);

		mainContent.addComponent(txfTo);
		mainContent.addComponent(txfCC);
		mainContent.addComponent(txfBC);
		mainContent.addComponent(txfSubject);
		mainContent.addComponent(rta);

		bottomBar.addComponent(sendButton);
//		bottomBar.addComponent(uploadAttachedFiles);

		subContent.addComponent(mainContent);
		subContent.addComponent(bottomBar);

	}
	


	private void echo(Integer pos) {
		Long start=0L;
		if (pos == 0) 
			start = System.currentTimeMillis();
		else
		System.out.printf("--- point" +pos +": %d%n", System.currentTimeMillis() - start);
		System.out.println("");
	}

}