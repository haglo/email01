package org.app.view.email.send;

import java.util.HashSet;
import java.util.Set;

import javax.activation.FileDataSource;

import org.app.controler.email.EmailToSend;
import org.app.controler.email.Smtp;
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

@SuppressWarnings("serial")
public class SendView extends Window {

	private I18n i18n;
	private VerticalLayout subContent;
	private CssLayout bottomBar;
	private VerticalLayout mainContent;
	private Smtp smtp;
	private EmailToSend email;
	private EmailUpload uploadExample;

	public SendView() {
		i18n = new I18n();
		smtp = new Smtp();
		email = new EmailToSend();
		uploadExample = new EmailUpload();
		uploadExample.init("basic");

		subContent = new VerticalLayout();
		this.setWidth("50%");
		this.setCaption(i18n.EMAIL_NEW);
		this.setContent(subContent);
		this.center();

		bottomBar = new CssLayout();
		bottomBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		mainContent = new VerticalLayout();
		mainContent.setSizeFull();

		Button sendButton = new Button(i18n.EMAIL_SEND, ev -> {

			email.setSendTo("h.g.gloeckler@gmx.de");
			email.setSendToCC("h.g.gloeckler@gmail.com");
			email.setSendToBC("hans-georg.gloeckler@uni-ulm.de, hans-georg.gloeckler@gimtex.de");
			email.setSubject("Test-Email von Pilgerapp");
			email.setTextHTML("<b>Hallo Welt with HTML bold<b>");
			
			Set<FileDataSource> tmp = new HashSet<FileDataSource>();
			tmp.add(new FileDataSource("C:/dev/upload/Image01.png"));
			tmp.add(new FileDataSource("C:/dev/upload/Geld01.xlsx"));
			email.setAttachments(tmp);
			
//			email.addAttachment("C:/dev/upload/Image01.png");
//			email.addAttachment("C:/dev/upload/Geld01.xlsx");

			
			email.setWithAttachment(true);
			try {
				smtp.send(email);
				Notification.show("Send success");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Notification.show("Send Error");
			} finally {
				getUI().getCurrent().removeWindow(this);
			}

		});

		Button attachmentButton = new Button(i18n.EMAIL_ATTACHMENT, ev -> {

		});

		sendButton.setIcon(VaadinIcons.LOCATION_ARROW_CIRCLE_O);
		bottomBar.addComponent(sendButton);
		bottomBar.addComponent(attachmentButton);
		bottomBar.addComponent(uploadExample);

		TextField txfTo = new TextField();
		txfTo.setValue(i18n.EMAIL_TO);
		TextField txfCC = new TextField();
		txfCC.setValue(i18n.EMAIL_CC);
		TextField txfBC = new TextField();
		txfBC.setValue(i18n.EMAIL_BC);
		TextField txfSubject = new TextField();
		txfSubject.setValue(i18n.EMAIL_SUBJECT);
		RichTextArea rta = new RichTextArea();
		rta.setSizeFull();

		mainContent.addComponent(txfTo);
		mainContent.addComponent(txfCC);
		mainContent.addComponent(txfBC);
		mainContent.addComponent(txfSubject);
		mainContent.addComponent(rta);

		subContent.addComponent(mainContent);
		subContent.addComponent(bottomBar);
		subContent.addComponent(uploadExample);

	}

}