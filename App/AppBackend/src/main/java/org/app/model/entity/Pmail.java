
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.app.model.entity.enums.EmailFolder;

@Entity
@SuppressWarnings("all")
@NamedQuery(name = Pmail.QUERY_GET_ALL, query = "SELECT c FROM Pmail c")
public class Pmail extends Superclass implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String QUERY_GET_ALL = "Pmail.GetAll";

	private Long pimapUid;
	
	private String pmessagID;
	
	private String preplyToID;
	
	private String pfrom;
	
	@Lob
	private String precipientTO;
	
	@Lob
	private String precipientCC;
	
	@Lob
	private String precipientBCC;

	private String psubject;
	
	private String psendDate;
	
	private String preceiveDate;
	
	private Integer pnumberOfAttachments;
	
	private String pfilenamesOfAttachments;
	
	private String pflags;
	
	private String plabels;
	
	/**
	 * Einbinden: Enum EmailFolder über ComboBox
	 */
	@Enumerated(EnumType.STRING)
	private EmailFolder emailFolder;

	/**
	 * body of the Email
	 */
	@Lob
	private String pcontent;
	
	/**
	 * Original Email
	 */
	@Lob
	private String pmessage;	
	

	/*
	 * Getter - Setter
	 */

	public Long getPimapUid() {
		return pimapUid;
	}

	public void setPimapUid(Long pimapUid) {
		this.pimapUid = pimapUid;
	}

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

	public String getPsendDate() {
		return psendDate;
	}

	public void setPsendDate(String psendDate) {
		this.psendDate = psendDate;
	}

	public String getPreceiveDate() {
		return preceiveDate;
	}

	public void setPreceiveDate(String receiveDate) {
		this.preceiveDate = receiveDate;
	}

	public String getPrecipientTO() {
		return precipientTO;
	}

	public void setPrecipientTO(String precipientTO) {
		this.precipientTO = precipientTO;
	}

	public String getPrecipientCC() {
		return precipientCC;
	}

	public void setPrecipientCC(String precipientCC) {
		this.precipientCC = precipientCC;
	}

	public String getPrecipientBCC() {
		return precipientBCC;
	}

	public void setPrecipientBCC(String precipientBCC) {
		this.precipientBCC = precipientBCC;
	}

	public String getPcontent() {
		return pcontent;
	}

	public void setPcontent(String pcontent) {
		this.pcontent = pcontent;
	}

}
