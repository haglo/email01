
package org.app.model.entity;

import java.io.Serializable;
import java.security.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@SuppressWarnings("all")
@NamedQuery(name = Pmail.QUERY_GET_ALL, query = "SELECT c FROM Pmail c")
public class Pmail extends Superclass implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String QUERY_GET_ALL = "Pmail.GetAll";

	private String pfrom;

	private String psubject;

	@Lob
	private String pcontent;

	public String getPfrom() {
		return pfrom;
	}

	public void setPfrom(String pfrom) {
		this.pfrom = pfrom;
	}

	public String getPsubject() {
		return psubject;
	}

	public void setPsubject(String psubject) {
		this.psubject = psubject;
	}

	public String getPcontent() {
		return pcontent;
	}

	public void setPcontent(String pcontent) {
		this.pcontent = pcontent;
	}

}
