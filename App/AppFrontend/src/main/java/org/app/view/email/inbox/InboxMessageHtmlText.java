package org.app.view.email.inbox;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.app.helper.I18n;

import com.google.common.base.Strings;
import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(I18n.INBOX_MESSAGE)
public class InboxMessageHtmlText extends VerticalLayout implements View {


	private CustomLayout cl;
	private String content = "<b> Hallo Welt </b> ";

	public InboxMessageHtmlText(String content) {
		String template = "<div style='width: 800px; text-align: center;'>"
				+ "  <div style='width: 750px; display: inline-block; border: 1px black solid;'>"
				+ "    <div style='border-bottom: 1px gray solid;'>A Caption</div>"
				+ "    <div style='overflow: auto;'>"
				+ "      <div location='emailContent' style='display: inline-block;'></div>"
				+ "    </div>"
				+ "  </div>"
				+ "</div>";

		// Read it through an input stream
		ByteArrayInputStream ins = new ByteArrayInputStream(template.getBytes());
		try {
//			cl = new CustomLayout(new ByteArrayInputStream(content.getBytes())); 
			cl = new CustomLayout(ins);
//			cl.addComponent(content, "emailContent");
		} catch (IOException e) {
			addComponent(new Label("Bad CustomLayout input stream"));
			return;
		}
		cl.setWidth("100%");
		addComponent(cl);
	}



	public void init() {
		removeAllComponents();
	}

	public void refresh() {
		removeAllComponents();
	}

	

}