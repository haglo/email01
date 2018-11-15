package org.app.view.mail;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.app.controler.EmailService;
import org.app.helper.I18n;
import org.app.mail.common.Const;
import org.app.model.entity.Pmail;
import org.app.model.entity.PmailFolder01;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(I18n.MAIL_MAIN_VIEW)
public class MainView extends VerticalLayout implements View, Const {

	@Inject
	EmailService service;

	private I18n i18n;
	private TopMenu emailTopMenu;
	private ListView overviewMail;
	private DetailView detailMail;
	private HorizontalSplitPanel emailContent;
	private VerticalLayout emailContentLeftBar;
	private HorizontalSplitPanel emailContentRightBar;

	public MainView() {
		setSizeFull();
		setMargin(false);
	}

	@PostConstruct
	void init() {

		emailTopMenu = new TopMenu(service);
		emailTopMenu.setSizeFull();
		emailContent = new HorizontalSplitPanel();
		emailContent.setSizeFull();
		emailContent.setSplitPosition(15, Unit.PERCENTAGE);
		emailContentLeftBar = new VerticalLayout();
		emailContentLeftBar.setMargin(false);
		emailContentLeftBar.setSizeFull();

		List<PmailFolder01> pmailFolder01s = service.getPmailFolder01DAO().findAll();
		
		for (PmailFolder01 pmf01 : pmailFolder01s) {
			Button btn = new Button(pmf01.getPfolderName(), ev -> {
				overviewMail = new ListView(this, pmf01);
				detailMail = new DetailView();
				emailContentRightBar.setFirstComponent(overviewMail);
				emailContentRightBar.setSecondComponent(detailMail);
				
			});	
			emailContentLeftBar.addComponent(btn);
		}


		
		
//		Button inbox = new Button(i18n.EMAIL_INBOX, ev -> {
//			inboxSubject = new CallMail(this);
//			if (EMAIL_SECURITY_LEVEL == ESECURITY.PLAIN_TEXT) {
//				inboxMessagePlainText = new HtmlTextMail();
//				emailContentRightBar.setFirstComponent(inboxSubject);
//				emailContentRightBar.setSecondComponent(inboxMessagePlainText);
//			}
//		});
//
//		Button sent = new Button(i18n.EMAIL_SENT, ev -> {
//		});
//
//		Button template = new Button(i18n.EMAIL_TEMPLATE, ev -> {
//		});
//
//		Button draft = new Button(i18n.EMAIL_DRAFT, ev -> {
//		});
//
//		Button trash = new Button(i18n.EMAIL_TRASH, ev -> {
//		});
//
//		Button archive = new Button(i18n.EMAIL_ARCHIVE, ev -> {
//		});
//
//
//		Button settings = new Button(i18n.EMAIL_SETTINGS, ev -> {
//		});

		/*
		 * Left Navigation
		 */

//		emailContentLeftBar.addComponent(inbox);
//		emailContentLeftBar.addComponent(sent);
//		emailContentLeftBar.addComponent(template);
//		emailContentLeftBar.addComponent(draft);
//		emailContentLeftBar.addComponent(trash);
//		emailContentLeftBar.addComponent(archive);
//		emailContentLeftBar.addComponent(settings);
		
		
		
//		for (int i =0; i<2; i++) {
//			Button button = new Button("hallo " + i);
//			addComponent(button);
//		}

		/*
		 * Right Content Side
		 */
		emailContentRightBar = new HorizontalSplitPanel();
		emailContentRightBar.setSizeFull();
		emailContentRightBar.setSplitPosition(40, Unit.PERCENTAGE);
		emailContent.setFirstComponent(emailContentLeftBar);
		emailContent.setSecondComponent(emailContentRightBar);

		addComponent(emailTopMenu);
		addComponent(emailContent);
		setExpandRatio(emailTopMenu, 0.2f);
		setExpandRatio(emailContent, 0.8f);

	}

	public EmailService getEmailService() {
		return service;
	}

	public HorizontalSplitPanel getEmailContentRightBar() {
		return emailContentRightBar;
	}

}
