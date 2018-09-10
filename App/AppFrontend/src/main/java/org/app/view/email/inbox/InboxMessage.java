package org.app.view.email.inbox;

import org.app.helper.I18n;
import org.jsoup.Jsoup;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(I18n.INBOX_MESSAGE)
public class InboxMessage extends VerticalLayout implements View {

	private VerticalLayout header;
	private VerticalLayout content;
	private Label area1;
	private RichTextArea area;
	private HorizontalLayout footer;
	
	private Label lblFrom;
	private Label lblSubject;
	private Label lblReply;
	private Label lblTo;
	

	public InboxMessage() {
		header = new VerticalLayout();
		content = new VerticalLayout();
		area = new RichTextArea();
		area1 = new Label();
		area1.setContentMode(ContentMode.HTML);
		area1.setStyleName("v-emailMessage-label");
		footer = new HorizontalLayout();
		
		lblFrom = new Label("Von ");
		lblSubject = new Label("Betreff ");
		lblReply = new Label("Antwort an ");
		lblTo = new Label("An ");
		
//		footer.setHeight(10, Unit.PERCENTAGE);
		
//		area.setValue("Hallo Ö Ü ß Welt");
//		area.setSizeFull();
		
		area1.setSizeFull();
		
		addComponents(lblFrom, lblSubject, lblReply, lblTo);
		addComponent(area1);
		addComponent(footer);
//		setExpandRatio(area1, 0.9f);
//		setExpandRatio(footer, 0.1f);
	}


	
	public void setMessageContent(String messageText) {
		area1.setValue(Jsoup.parse(messageText).text());
	}
	
	public void addTextToHeader(String text) {
		Label label = new Label(text);
//		header.addComponent(label);
	}
	

	

}