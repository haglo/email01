package org.app.view.mail;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.app.helper.I18n;
import org.app.mail.common.Const;
import org.app.model.entity.Pmail;
import org.app.model.entity.PmailFolder01;

import com.google.common.base.Strings;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.TextRenderer;

@SuppressWarnings("serial")
public class ListView extends VerticalLayout implements View, Const {

	private DetailView detailView;
	private Grid<Pmail> grid;
	private ListDataProvider<Pmail> dataProvider;
	private Set<Pmail> selectedMails;
	private Pmail selectedMail;

	public ListView(MainView emailView, PmailFolder01 folderName) {
		detailView = new DetailView();
		setMargin(new MarginInfo(false, true, false, false));
		setSizeFull();

		List<Pmail> list = emailView.getEmailService().getPmailDAO().findByFolderName(folderName);
		dataProvider = DataProvider.ofCollection(list);

		grid = new Grid<Pmail>();
		grid.setSizeFull();
		grid.setWidth("100%");
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.setDataProvider(dataProvider);

		grid.addColumn(Pmail::getId).setRenderer(id -> id != null ? id : null, new TextRenderer()).setCaption("ID");
		grid.addColumn(Pmail::getPfrom).setRenderer(from -> from != null ? from : null, new TextRenderer())
				.setCaption("From");
		grid.addColumn(Pmail::getPsubject).setRenderer(subject -> subject != null ? subject : null, new TextRenderer())
				.setCaption("Subject");
		grid.addColumn(p -> convertTimestamp(p.getPreceiveDate())).setCaption("Receive Date").setId("receiveDate");

		grid.addSelectionListener(event -> {
			selectedMail = new Pmail();
			selectedMails = new HashSet<Pmail>();
			selectedMails = event.getAllSelectedItems();
			if (selectedMails.size() != 1) {
				detailView.setMessageContent("");
				detailView.init();
			} else {
				selectedMail = getSelectedMail(selectedMails);
				if (selectedMail != null) {
					detailView.init();
					if (!Strings.isNullOrEmpty(selectedMail.getPfrom()))
						detailView.getLblFrom().setValue("Von " + selectedMail.getPfrom());
					if (!Strings.isNullOrEmpty(selectedMail.getPsubject()))
						detailView.getLblSubject().setValue("Betreff " + selectedMail.getPsubject());
					if (!Strings.isNullOrEmpty(selectedMail.getPrecipientTO()))
						detailView.getLblTO().setValue("An " + selectedMail.getPrecipientTO());
					if (!Strings.isNullOrEmpty(selectedMail.getPrecipientCC()))
						detailView.getLblCC().setValue("CC " + selectedMail.getPrecipientCC());
					if (!Strings.isNullOrEmpty(selectedMail.getPrecipientBCC()))
						detailView.getLblBCC().setValue("BCC " + selectedMail.getPrecipientBCC());

					if (selectedMail.getPattachmentNumber() > 0) {
						detailView.getLblAttachmentNumber()
								.setValue("Number of Attachments " + selectedMail.getPattachmentNumber());
					}

					if (!Strings.isNullOrEmpty(selectedMail.getPattachmentFileName()))
						detailView.getLblAttachmentFileNames()
								.setValue("Filename1 " + selectedMail.getPattachmentFileName());

					if (!Strings.isNullOrEmpty(selectedMail.getPattachmentFilePath()))
						detailView.getLblAttachmentFilePath()
								.setValue("Filename2 " + selectedMail.getPattachmentFilePath());

					if (!Strings.isNullOrEmpty(selectedMail.getPattachmentFileFullName()))
						detailView.getLblAttachmentFullFileName()
								.setValue("Filename3 " + selectedMail.getPattachmentFileFullName());

					if (!Strings.isNullOrEmpty(selectedMail.getPattachmentFileFullName())) {
						for (Button tmp : createAttachment(selectedMail.getPattachmentFileFullName())) {
							detailView.getAttachmentPanel().addComponent(tmp);
						}
					}

					String tmp = I18n.decodeFromBase64(selectedMail.getPcontent());
					detailView.setMessageContent(tmp);

					byte[] byteDecodedEmail = Base64.getMimeDecoder().decode(selectedMail.getPmessage());
					String decodedEmail = new String(byteDecodedEmail);
					detailView.setRawMail(decodedEmail);

					detailView.refresh();
				}
			}
			// Important
			emailView.getEmailContentRightBar().setSecondComponent(detailView);
		});

		addComponent(grid);
	}

	private Pmail getSelectedMail(Set<Pmail> selectedMails) {
		selectedMail = new Pmail();
		if (selectedMails.size() > 1) {
			Notification.show("Only one Item");
			return null;
		}
		if (selectedMails.size() < 1) {
			Notification.show("Exact one Item");
			return null;
		}
		if (selectedMails.size() == 1) {
			for (Pmail pmail : selectedMails) {
				selectedMail = pmail;
			}
		}
		return selectedMail;

	}

	private String convertTimestamp(String dateString) {
		if (dateString.isEmpty()) {
			return null;
		}
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm", Locale.GERMAN);
		ZonedDateTime dateTime = ZonedDateTime.parse(dateString, inputFormatter);
		String out = dateTime.format(outputFormatter);
		return out;
	}

	private List<Button> createAttachment(String in) {
		Button downloadButton;
		List<Button> result = new ArrayList<Button>();

		String tmp1 = "\\";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ATTACHMENT_DELIMITER.length(); i++) {
			sb.append(tmp1);
			sb.append(ATTACHMENT_DELIMITER.charAt(i));
		}
		String delimiter = new String(sb);

		List<String> attachments = new ArrayList<String>(Arrays.asList(in.split(delimiter)));

		for (String str : attachments) {
			File file = new File(str);
			downloadButton = new Button(file.getName(), e -> {
//				UI.getCurrent().getNavigator().navigateTo(I18n.EMAIL_VIEW);
			});
			downloadButton.setIcon(VaadinIcons.CLOUD_DOWNLOAD);
			downloadButton.addStyleName("icon-align-top");

			Resource res = new FileResource(file);
			FileDownloader fd = new FileDownloader(res);
			fd.extend(downloadButton);
			result.add(downloadButton);

		}
		return result;
	}

}