package org.app.view.email.inbox;

import com.vaadin.ui.Composite;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class InboxContentHtmlText extends Composite {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InboxContentHtmlText(Email email) {
        VerticalLayout layout = new VerticalLayout(
            new HorizontalLayout(
                new Label(email.from),
                new Label(email.subject)
            ),
            mailContent(email)
        );
        setCompositionRoot(layout);
    }

    private CustomLayout mailContent(Email email) {
        CustomLayout c = new CustomLayout();
        c.setTemplateContents(email.content);
        return c;
        /* Another way, using inputstream
        try {
            return new CustomLayout(new ByteArrayInputStream(email.content.getBytes()));
        } catch (IOException e) {
            // handle error
            throw new RuntimeException(e);
        }
        */
    }

    public static class Email {
        String from = "From: xxx";
        String subject = "Subject: xxxx";
        String content = "Content: XXXX";
    }
}
