package org.app.view.mail.raw;

import javax.mail.Message;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class RawMail extends Window {

	private static final long serialVersionUID = 1L;
	private Label origEmail;
	
	private Message message;

	public RawMail(String email) {
    
		this.setCaption("Quellcode der Email");
		VerticalLayout subContent = new VerticalLayout();
		this.setContent(subContent);
		this.center();

		try {
			origEmail = new Label(email);
			subContent.addComponent(origEmail);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

}
