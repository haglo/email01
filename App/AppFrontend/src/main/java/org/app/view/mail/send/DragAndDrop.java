package org.app.view.mail.send;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.StreamVariable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.dnd.FileDropTarget;
import java.io.OutputStream;

public class DragAndDrop {

	public void drop() {
    final Label infoLabel = new Label();
    infoLabel.setWidth(240.0f, Unit.PIXELS);

    final VerticalLayout dropPane = new VerticalLayout(infoLabel);
    dropPane.setComponentAlignment(infoLabel, Alignment.MIDDLE_CENTER);
    dropPane.addStyleName("drop-area");
    dropPane.setSizeUndefined();

    ProgressBar progress = new ProgressBar();
    progress.setIndeterminate(true);
    progress.setVisible(false);
    dropPane.addComponent(progress);

    new FileDropTarget<>(dropPane, fileDropEvent -> {
        final int fileSizeLimit = 2 * 1024 * 1024; // 2MB

        fileDropEvent.getFiles().forEach(html5File -> {
            final String fileName = html5File.getFileName();

            if (html5File.getFileSize() > fileSizeLimit) {
                Notification.show(
                        "File rejected. Max 2MB files are accepted by Sampler",
                        Notification.Type.WARNING_MESSAGE);
            } else {
                final ByteArrayOutputStream bas = new ByteArrayOutputStream();
                final StreamVariable streamVariable = new StreamVariable() {

                    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
                    public OutputStream getOutputStream() {
                        return bas;
                    }

                    @Override
                    public boolean listenProgress() {
                        return false;
                    }

                    @Override
                    public void onProgress(
                            final StreamingProgressEvent event) {
                    }

                    @Override
                    public void streamingStarted(
                            final StreamingStartEvent event) {
                    }

                    @Override
                    public void streamingFinished(
                            final StreamingEndEvent event) {
                        progress.setVisible(false);
                        showFile(fileName, bas);
                    }

                    @Override
                    public void streamingFailed(
                            final StreamingErrorEvent event) {
                        progress.setVisible(false);
                    }

                    @Override
                    public boolean isInterrupted() {
                        return false;
                    }
                };
                html5File.setStreamVariable(streamVariable);
                progress.setVisible(true);
            }
        });
    });
	}

	private void showFile(final String name, final ByteArrayOutputStream bas) {
		// resource for serving the file contents
		final StreamSource streamSource = () -> {
			if (bas != null) {
				final byte[] byteArray = bas.toByteArray();
				return new ByteArrayInputStream(byteArray);
			}
			return null;
		};
		final StreamResource resource = new StreamResource(streamSource, name);

		// show the file contents - images only for now
		final Embedded embedded = new Embedded(name, resource);
		showComponent(embedded, name);
	}

	private void showComponent(final Component c, final String name) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeUndefined();
		layout.setMargin(true);
		final Window w = new Window(name, layout);
		w.addStyleName("dropdisplaywindow");
		w.setSizeUndefined();
		w.setResizable(false);
		c.setSizeUndefined();
		layout.addComponent(c);
		UI.getCurrent().addWindow(w);
	}

}
