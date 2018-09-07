package org.app.view.email.inbox.parts;

import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class InboxMessage extends VerticalLayout {

	private VerticalLayout mainContent;
	private TextArea area;

	public InboxMessage(String messageText) {
		mainContent = new VerticalLayout();
		setMargin(new MarginInfo(false, true, false, false));
		setSizeFull();
		area = new TextArea();
		area.setSizeFull();
		area.setValue(messageText);
		area.setVisible(true);
		mainContent.addComponent(area);
		addComponent(mainContent);
	}
	
	public void setContent(String messageText) {
		area = new TextArea();
		area.setSizeFull();
		area.setValue(messageText);
		area.setVisible(true);	
		mainContent.addComponent(area);
	}
	
}