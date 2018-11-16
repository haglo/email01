
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.app.model.entity.enums.EmailFolder;
import org.hibernate.annotations.Type;

@Entity
@SuppressWarnings("all")

@NamedQueries({ 
	@NamedQuery(name = Pmail.QUERY_FIND_ALL, query = "SELECT c FROM Pmail c"),
	@NamedQuery(name = Pmail.QUERY_FIND_BY_FOLDERNAME, query = "SELECT c FROM Pmail c WHERE c.pmailFolder01 =  :pmailFolder01"),
	@NamedQuery(name = Pmail.QUERY_FIND_BY_IMAPID, query = "SELECT c FROM Pmail c WHERE c.pimapID =  :pimapID"),
	@NamedQuery(name = Pmail.QUERY_FIND_BY_MESSAGEID, query = "SELECT c FROM Pmail c WHERE c.pmessageID =  :pmessageID")
})
public class Pmail extends Superclass implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String QUERY_FIND_ALL = "Pmail.FindAll";
	public static final String QUERY_FIND_BY_FOLDERNAME = "Pmail.FindByFolderName";
	public static final String QUERY_FIND_BY_IMAPID = "Pmail.FindByImapID";
	public static final String QUERY_FIND_BY_MESSAGEID = "Pmail.FindByMessageID";

	private Long pimapID;

	private String pmessageID;
	
	/**
	 * Einbinden: Entity FolderName
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FOLDER01_ID")
	private PmailFolder01 pmailFolder01;
	
	private String preplyToID;

	private String pfrom;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String precipientTO;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String precipientCC;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String precipientBCC;

	private String psubject;

	private String psendDate;

	private String preceiveDate;

	private Integer pattachmentNumber;

	private String pattachmentFilePath;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String pattachmentFileName;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String pattachmentFileFullName;

	private String pflags;

	private String plabels;


	/**
	 * content of Email
	 */
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String pcontent;

	/**
	 * Original Email
	 */
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String pmessage;


	public Long getPimapID() {
		return pimapID;
	}

	public void setPimapID(Long pimapID) {
		this.pimapID = pimapID;
	}

	public String getPmessageID() {
		return pmessageID;
	}

	public void setPmessageID(String pmessageID) {
		this.pmessageID = pmessageID;
	}

	public PmailFolder01 getPmailFolder01() {
		return pmailFolder01;
	}

	public void setPmailFolder01(PmailFolder01 pmailFolder01) {
		this.pmailFolder01 = pmailFolder01;
	}

	public String getPreplyToID() {
		return preplyToID;
	}

	public void setPreplyToID(String preplyToID) {
		this.preplyToID = preplyToID;
	}

	public String getPfrom() {
		return pfrom;
	}

	public void setPfrom(String pfrom) {
		this.pfrom = pfrom;
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

	public void setPreceiveDate(String preceiveDate) {
		this.preceiveDate = preceiveDate;
	}

	public Integer getPattachmentNumber() {
		return pattachmentNumber;
	}

	public void setPattachmentNumber(Integer pattachmentNumber) {
		this.pattachmentNumber = pattachmentNumber;
	}

	public String getPattachmentFilePath() {
		return pattachmentFilePath;
	}

	public void setPattachmentFilePath(String pattachmentFilePath) {
		this.pattachmentFilePath = pattachmentFilePath;
	}

	public String getPattachmentFileName() {
		return pattachmentFileName;
	}

	public void setPattachmentFileName(String pattachmentFileName) {
		this.pattachmentFileName = pattachmentFileName;
	}


	public String getPattachmentFileFullName() {
		return pattachmentFileFullName;
	}

	public void setPattachmentFileFullName(String pattachmentFileFullName) {
		this.pattachmentFileFullName = pattachmentFileFullName;
	}

	public String getPflags() {
		return pflags;
	}

	public void setPflags(String pflags) {
		this.pflags = pflags;
	}

	public String getPlabels() {
		return plabels;
	}

	public void setPlabels(String plabels) {
		this.plabels = plabels;
	}

	public String getPcontent() {
		return pcontent;
	}

	public void setPcontent(String pcontent) {
		this.pcontent = pcontent;
	}

	public String getPmessage() {
		return pmessage;
	}

	public void setPmessage(String pmessage) {
		this.pmessage = pmessage;
	}

}
