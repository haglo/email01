package org.app.view;

import java.util.Optional;

import javax.inject.Inject;

import org.app.controler.EmailService;
import org.app.helper.I18n;
import org.app.view.email.EmailView;
import org.app.view.help.HelpView;
import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("default")
@CDIUI("")
public class MainUI extends UI {
	@Inject
	CDIViewProvider viewProvider;
	
	@Inject
	EmailService service;

	@Inject
	private HelpView helpView;

	private Navigator navigator;

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout mainLayout = new VerticalLayout();
		final HorizontalLayout menuView = new HorizontalLayout();
		final HorizontalLayout contentView = new HorizontalLayout();
		contentView.setSizeFull();
		contentView.setDefaultComponentAlignment(Alignment.TOP_CENTER);

		TopMainMenu topNavBar = new TopMainMenu();
		menuView.addComponent(topNavBar);

		mainLayout.addComponent(menuView);
		mainLayout.addComponent(contentView);

		mainLayout.setComponentAlignment(menuView, Alignment.TOP_CENTER);
		mainLayout.setComponentAlignment(contentView, Alignment.TOP_CENTER);

		mainLayout.setExpandRatio(menuView, 0.10f);
		mainLayout.setExpandRatio(contentView, 0.90f);

		mainLayout.setSizeFull();

		setContent(mainLayout);

		navigator = new Navigator(this, contentView);
		navigator.addProvider(viewProvider);
		navigator.setErrorView(helpView);

		String initialState = Optional.ofNullable(navigator.getState()).filter(state -> !state.trim().isEmpty())
				.orElse(I18n.HELP_VIEW);
		navigator.navigateTo(initialState);
	}

}