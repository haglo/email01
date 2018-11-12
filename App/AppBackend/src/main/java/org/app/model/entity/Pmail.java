
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

	private Integer pattachmentNumber;
	private String pattachmentFilePath;

	@Lob
	private String pattachmentFileName;

	@Lob
	private String pattachmentFileFullName;

	private String pflags;

	private String plabels;

	/**
	 * Einbinden: Enum EmailFolder über ComboBox
	 */
	@Enumerated(EnumType.STRING)
	private EmailFolder emailFolder;

	/**
	 * content of Email
	 */
	@Lob
	private String pcontent;

	/**
	 * Original Email
	 */
	@Lob
	private String pmessage;

	public Long getPimapUid() {
		return pimapUid;
	}

	public void setPimapUid(Long pimapUid) {
		this.pimapUid = pimapUid;
	}

	public String getPmessagID() {
		return pmessagID;
	}

	public void setPmessagID(String pmessagID) {
		this.pmessagID = pmessagID;
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

	public EmailFolder getEmailFolder() {
		return emailFolder;
	}

	public void setEmailFolder(EmailFolder emailFolder) {
		this.emailFolder = emailFolder;
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
