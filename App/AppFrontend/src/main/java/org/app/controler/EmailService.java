package org.app.controler;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

import org.app.model.dao.PmailDAO;


@RequestScoped
public class EmailService implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EJB
	private PmailDAO pmailDAO;
	
	private boolean isEditing = false;

	public boolean getEditing() {
		return isEditing;
	}

	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}

	public void toggleEditing() {
		this.isEditing = !this.isEditing;
	}

	
	public PmailDAO getPmailDAO() {
		return pmailDAO;
	}

}
