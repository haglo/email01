package org.app.model.dao;

import java.util.List;
import org.app.model.entity.Pmail;
import org.app.model.entity.PmailFolder01;

public interface PmailDAO {

	public Pmail create(Pmail pmail);

	public Pmail update(Pmail pmail);

	public void remove(Integer id);

	public Pmail findByID(Integer id);

	public List<Pmail> findAll();

	public List<Pmail> findByFolderName(PmailFolder01 pmailFolder01);

	public Pmail findByIampID(Long imapID);

	public Pmail findByMessageID(String messageID);
}