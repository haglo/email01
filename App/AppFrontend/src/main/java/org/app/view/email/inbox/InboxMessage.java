package org.app.view.email.inbox;

import org.app.helper.I18n;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(I18n.INBOX_MESSAGE)
public class InboxMessage extends VerticalLayout implements View {

	private VerticalLayout header;
	private VerticalLayout content;
	private Label htmlArea;
	private HorizontalLayout footer;

	private Label lblFrom;
	private Label lblSubject;
	private Label lblReply;
	private Label lblTo;

	public InboxMessage() {
		header = new VerticalLayout();
		content = new VerticalLayout();
		htmlArea = new Label();
		htmlArea.setSizeFull();
		htmlArea.setContentMode(ContentMode.HTML);
//		area.setStyleName("v-emailMessage-label");
		footer = new HorizontalLayout();

		lblFrom = new Label("Von ");
		lblSubject = new Label("Betreff ");
		lblReply = new Label("Antwort an ");
		lblTo = new Label("An ");

		footer.setHeight(10, Unit.PERCENTAGE);

		addComponents(lblFrom, lblSubject, lblReply, lblTo);
		addComponent(htmlArea);
		addComponent(footer);
//		setExpandRatio(area1, 0.9f);
//		setExpandRatio(footer, 0.1f);
	}

	public void setHtmlMessageContent(String messageText) {
		htmlArea.setValue(messageText);
	}


	public void addTextToHeader(String text) {
	}

}