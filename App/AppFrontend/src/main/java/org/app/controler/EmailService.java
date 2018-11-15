package org.app.controler;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

import org.app.model.dao.PmailDAO;
import org.app.model.dao.PmailFolder01DAO;


@RequestScoped
public class EmailService implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EJB
	private PmailDAO pmailDAO;

	@EJB
	private PmailFolder01DAO pmailFolder01DAO;
	
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

	public PmailFolder01DAO getPmailFolder01DAO() {
		return pmailFolder01DAO;
	}

}
