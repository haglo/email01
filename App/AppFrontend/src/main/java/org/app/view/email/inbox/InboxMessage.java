package org.app.view.email.inbox;

import org.app.helper.I18n;
import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(I18n.INBOX_MESSAGE)
public class InboxMessage extends VerticalLayout implements View {

	private TextArea area;

	public InboxMessage() {
		setMargin(false);
		setSizeFull();
		area = new TextArea();
		area.setValue("Hall Ö Ü ß Welt");
		area.setSizeFull();
		addComponent(area);
	}

	public void setContent(String messageText) {
		area.setValue(messageText);
		removeAllComponents();
		addComponent(area);
	}

}