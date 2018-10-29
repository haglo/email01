package org.app.view.email.inbox;

import com.vaadin.ui.*;


/**
 * -- V1:Vertiacal Layout
 * ----H1: Horizontal Layout with 2 Labels
 *              Label1: From
 *              Label1: Subject
 * ----H2: Horizontal Layout with for rendering the Content of the Email in HTML
 */
public class MyView extends Composite {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyView(Email email) {
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
        String from;
        String subject;
        String content;
    }
}

