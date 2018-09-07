package org.app.view.help;

import javax.annotation.PostConstruct;
import org.app.helper.I18n;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(I18n.HELP_VIEW)
@UIScoped
public class HelpView extends HorizontalLayout implements View {

	public HelpView() {
		setMargin(new MarginInfo(false, true, true, true));
	}

	@PostConstruct
	void init() {
		setSizeFull();
		setWidth(I18n.WINDOW_WIDTH);
 
		VerticalLayout content = new VerticalLayout();

		content.addComponent(headingLabel());
		content.addComponent(someText());
		addComponent(content);
		setDefaultComponentAlignment(Alignment.TOP_CENTER);
	}

	private Label headingLabel() {
		return new Label("Help without HTML");
	}

	private Label someText() {
		Label label = new Label("Help <b>with HTML</b>");
		label.setContentMode(ContentMode.HTML);
		return label;
	}

}
