package org.app.view.email.inbox;

import javax.mail.Message;

import org.app.helper.I18n;
import com.google.common.base.Strings;
import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(I18n.INBOX_MESSAGE_PLAIN_TEXT)
public class InboxMessagePlainText extends  VerticalLayout implements View {

	private Label lblFrom;
	private Label lblSubject;
	private Label lblReplyTo;
	private Label lblTO;
	private Label lblCC;
	private Label lblBCC;
	private Label lblSendDate;
	private Label lblAttachmentNumber;
	private Label lblAttachmentFileNames;
	
	private Button mailRawData;
	private Label htmlArea;
	
	private String rawMail;
	private HorizontalLayout footer;


	public InboxMessagePlainText() {

		lblFrom = new Label();
		lblSubject = new Label();
		lblReplyTo = new Label();
		lblTO = new Label();
		lblCC = new Label();
		lblBCC = new Label();
		lblSendDate = new Label();
		lblAttachmentNumber = new Label();
		lblAttachmentFileNames = new Label();

		mailRawData = new Button("QuellCode", ev -> {
			getUI().addWindow(new MailSourceCode(getRawMail()));
		});

		htmlArea = new Label();
		htmlArea.setSizeFull();
		htmlArea.setContentMode(ContentMode.HTML);
		footer = new HorizontalLayout();
		footer.setHeight(10, Unit.PERCENTAGE);
		
	}

	public void setMessageContent(String messageText) {
		htmlArea.setValue(messageText);
	}

	public void init() {
		removeAllComponents();
		lblFrom.setValue("");
		lblSubject.setValue("");
		lblReplyTo.setValue("");
		lblTO.setValue("");
		lblCC.setValue("");
		lblBCC.setValue("");
		lblSendDate.setValue("");
		lblAttachmentNumber.setValue("");
		lblAttachmentFileNames.setValue("");
		htmlArea.setValue("");
	}

	public void refresh() {
		removeAllComponents();
		if (!Strings.isNullOrEmpty(lblFrom.getValue()))
			addComponent(lblFrom);
		if (!Strings.isNullOrEmpty(lblSubject.getValue()))
			addComponent(lblSubject);
		if (!Strings.isNullOrEmpty(lblReplyTo.getValue()))
			addComponent(lblReplyTo);
		if (!Strings.isNullOrEmpty(lblTO.getValue()))
			addComponent(lblTO);
		if (!Strings.isNullOrEmpty(lblCC.getValue()))
			addComponent(lblCC);
		if (!Strings.isNullOrEmpty(lblBCC.getValue()))
			addComponent(lblBCC);
		addComponent(lblSendDate);
		if (!Strings.isNullOrEmpty(lblAttachmentNumber.getValue()))
			addComponent(lblAttachmentNumber);
		if (!Strings.isNullOrEmpty(lblAttachmentFileNames.getValue()))
			addComponent(lblAttachmentFileNames);

		addComponent(mailRawData);
		addComponent(htmlArea);
		addComponent(footer);
	}

	public void addTextToHeader(String text) {
	}

	public Label getLblFrom() {
		return lblFrom;
	}

	public void setLblFrom(Label lblFrom) {
		this.lblFrom = lblFrom;
	}

	public Label getLblSubject() {
		return lblSubject;
	}

	public void setLblSubject(Label lblSubject) {
		this.lblSubject = lblSubject;
	}

	public Label getLblReplyTo() {
		return lblReplyTo;
	}

	public void setLblReplyTo(Label lblReply) {
		this.lblReplyTo = lblReply;
	}

	public Label getLblTO() {
		return lblTO;
	}

	public void setLblTO(Label lblTO) {
		this.lblTO = lblTO;
	}

	public Label getLblCC() {
		return lblCC;
	}

	public void setLblCC(Label lblCC) {
		this.lblCC = lblCC;
	}

	public Label getLblBCC() {
		return lblBCC;
	}

	public void setLblBCC(Label lblBCC) {
		this.lblBCC = lblBCC;
	}

	public Label getLblSendDate() {
		return lblSendDate;
	}

	public void setLblSendDate(Label lblSendDate) {
		this.lblSendDate = lblSendDate;
	}
	
	public Label getLblAttachmentNumber() {
		return lblAttachmentNumber;
	}

	public void setLblAttachmentNumber(Label lblAttachmentNumber) {
		this.lblAttachmentNumber = lblAttachmentNumber;
	}

	public Label getLblAttachmentFileNames() {
		return lblAttachmentFileNames;
	}

	public void setLblAttachmentFileNames(Label lblAttachmentFileNames) {
		this.lblAttachmentFileNames = lblAttachmentFileNames;
	}

	public String getRawMail() {
		return rawMail;
	}

	public void setRawMail(String rawMail) {
		this.rawMail = rawMail;
	}



}